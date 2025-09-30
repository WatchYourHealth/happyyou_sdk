package com.wyh.happyyousdk.syncDevice;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.todayDateInFormat;
import static com.wyh.happyyousdk.utils.Constants.ACTIVE_STEPS_COUNT;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;
import static com.wyhsdk.main.NewSleepLogic.getNewSleepTime;
import static com.wyhsdk.utils.Constants.SOURCE_GOOGLEFIT;
import static com.wyhsdk.utils.Utilities.getTodayDateNew;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.dashboard.PostLoginActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityConnectAppBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.main.AppVisibilityTracker;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.sendActivityData.SendDataToServerReceiver;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.HealthConnect.HealthConnectUtil;
import com.wyhsdk.main.WatchYourHealth;
import com.wyhsdk.sharedPreferences.SharedPreference;
import com.wyhsdk.utils.Constants;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ConnectApp extends AppCompatActivity implements ScratchListener {
    ActivityConnectAppBinding binding;
    public HealthConnectUtil healthConnectUtil;
    WatchYourHealth watchYourHealth;
    int totalStepsDays, totalStandDays, totalMinuteStepsDays;
    ProgressDialog progressDialog;
    AlertDialog alertDialogRewardPopup;
    Context context;
    boolean hourlyStep = false;
    AppVisibilityTracker appVisibilityTracker;
    private final ActivityResultLauncher<Intent> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Handle the result of the settings activity


            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPref.init(getApplicationContext());
        SharedPreference.init(getApplicationContext());
        context = this;
        appVisibilityTracker = new AppVisibilityTracker();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_connect_app);

        progressDialog = new ProgressDialog(this, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        healthConnectUtil = new HealthConnectUtil(this, this, permissionLauncher);
        watchYourHealth = new WatchYourHealth(ConnectApp.this, SharedPref.getUuid());
        watchYourHealth.initializeAPIClient(savedInstanceState);
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.btnInstall.setOnClickListener(v -> healthConnectUtil.checkHealthConnectSdkStatusA(true));

        binding.HCSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPref.init(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //healthConnectUtil = new HealthConnectUtil(ActivityHomeDashBoardNewRevamp.this, ActivityHomeDashBoardNewRevamp.this, permissionLauncher);
                if (SharedPref.getFitBitConnection()) {
                    binding.btnInstall.setVisibility(View.GONE);
                    watchYourHealth.getTotalSteps(Constants.SOURCE_FITBIT);
                } else {
                    if (healthConnectUtil != null && !healthConnectUtil.checkHealthConnectSdkStatusOnly()) {
                        binding.btnInstall.setVisibility(View.VISIBLE);
                        binding.fitConnection.setText("Recommended");
                        binding.fitConnection.setTextColor(getResources().getColor(R.color.green_teal));
                    } else if (healthConnectUtil != null && healthConnectUtil.checkHealthConnectSdkStatusA(true)) {
                        binding.btnInstall.setVisibility(View.GONE);
                        checkHealthConnectPermission();
                    }
                }

                if (healthConnectUtil != null && healthConnectUtil.checkHealthConnectSdkStatusOnly() && !SharedPreference.getGoogleFitConnection()) {
                    binding.fitConnection.setTextColor(getResources().getColor(R.color.alert_red));
                    binding.fitConnection.setText("Disconnected");
                    setFitChecked(false);
                } else if (healthConnectUtil != null && healthConnectUtil.checkHealthConnectSdkStatusOnly()) {
                    //binding.btnInstall.setVisibility(View.GONE);
                    binding.fitConnection.setText("Connected");
                    binding.fitConnection.setTextColor(getResources().getColor(R.color.green_teal));
                    setFitChecked(true);
                }
            }
        }, 1000);
    }

    //region HealthConnect
    public void HealthConnectSteps() {
        try {
            if (!SharedPreference.getAllStepDataSync()) {

                LocalDateTime startTime = LocalDateTime.now().minusDays(30).withMinute(0).withHour(0).withSecond(0);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime endTime = LocalDateTime.now();
                healthConnectUtil.aggregateStepsIntoDays(startTime, endTime);
                healthConnectUtil.aggregateStepsIntoHour(startTime, endTime);
                healthConnectUtil.aggregateStepsIntoMinutes(startTime, endTime);
                /*if (appVisibilityTracker.isAppinForeground()) {
                    healthConnectUtil.aggregateStepsIntoDays(startTime, endTime);
                    healthConnectUtil.aggregateStepsIntoHour(startTime, endTime);
                    healthConnectUtil.aggregateStepsIntoMinutes(startTime, endTime);
                }*/
                getStand();
                getActiveHours();
                getMinuteSteps();
                if (!watchYourHealth.isSleepPresent(SOURCE_GOOGLEFIT, todayDateInFormat("yyyy-MM-dd")))
                    getNewSleepTime(ConnectApp.this, totalMinuteStepsDays, true);
                try {
                    final Handler handler2 = new Handler();
                    // Call getSteps method
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ConnectApp.this, SendDataToServerReceiver.class);
                            ConnectApp.this.sendBroadcast(intent);
                            SharedPreference.putAllStepDataSync(true);
                            getSteps();
                        }
                    }, 5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                getDaysSteps();
                getHourlySteps();
                getMinuteSteps();
                LocalDateTime startTime = LocalDateTime.now().minusDays(totalStepsDays).withMinute(0).withHour(0).withSecond(0);
                LocalDateTime startTimeHours = LocalDateTime.now().minusDays(totalStandDays).withMinute(0).withHour(0).withSecond(0);
                LocalDateTime startTimeMinute = LocalDateTime.now().minusDays(totalMinuteStepsDays).withMinute(0).withHour(0).withSecond(0);
                LocalDateTime endTime = LocalDateTime.now();
                healthConnectUtil.aggregateStepsIntoDays(startTime, endTime);
                healthConnectUtil.aggregateStepsIntoHour(startTimeHours, endTime);
                /*if (appVisibilityTracker.isAppinForeground()) {
                    healthConnectUtil.aggregateStepsIntoDays(startTime, endTime);
                    healthConnectUtil.aggregateStepsIntoHour(startTimeHours, endTime);
                }*/
                getStand();
                getActiveHours();
                if (!watchYourHealth.isSleepPresent(SOURCE_GOOGLEFIT, todayDateInFormat("yyyy-MM-dd")))
                    getNewSleepTime(ConnectApp.this, totalMinuteStepsDays, true);
                try {
                    final Handler handler2 = new Handler();
                    handler2.postDelayed(((ConnectApp) ConnectApp.this)::getSteps, 2000);
                    getSteps();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
    }

    public void getHourlySteps() {
        if (watchYourHealth.isHourlyStepExist()) {
            try {
                String lastDate = watchYourHealth.getLastStandDate();
                //String lastDate = "2019-11-20";
                String todayDateNew = getTodayDateNew();
                //Toast.makeText(getApplicationContext(), lastDate, Toast
                // .LENGTH_LONG).show();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                Date date = sdf.parse(lastDate);
                Date date1 = sdf.parse(todayDateNew);
                long diff = date1.getTime() - date.getTime();
                int days = (int) TimeUnit.DAYS.convert(diff,
                        TimeUnit.MILLISECONDS);
                totalStandDays = days;
                //new ViewStepsCount(WatchYourHealth.YEAR_HOUR).execute();
            } catch (ParseException e) {
                //e.printStackTrace();
            }
        } else {
            //totalStandDays = -1;
            totalStandDays = 30;
            // new ViewStepsCount(WatchYourHealth.YEAR_HOUR).execute();
        }
    }

    public void getSteps() {
        //watchYourHealth = new WatchYourHealth(HomeActivity.this);
        if (SharedPref.getFitBitConnection()) {
            watchYourHealth.getTotalSteps(Constants.SOURCE_FITBIT);
        } else {
            SharedPref.putTodaySteps(watchYourHealth.getTotalSteps(SOURCE_GOOGLEFIT));
        }
    }

    private void getStand() {
        if (!hourlyStep) {
            String minHourlyStepsDate = watchYourHealth.getMinHourlyStepsDate();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = formatter.parse(minHourlyStepsDate);
                Date endDate = formatter.parse(getTodayDateNew());

                Calendar start = Calendar.getInstance();
                start.setTime(startDate);
                Calendar end = Calendar.getInstance();
                end.setTime(endDate);
                //end.add(Calendar.DATE, 1);
                //Date newEndDate = end.getTime();
                //Log.v("DAte",""+end);
                for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE,
                        1), date = start.getTime()) {
                    // Do your job here with `date`.
                    SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                    String convertDate = spf.format(date);
                    System.out.println("Stand Date : " + convertDate);
                    //String todayDateNew = Utilities.getTodayDateNew();
                    int standCount =
                            watchYourHealth.getTodayHourlyStandCount(convertDate);
                    //System.out.println("Stand Count : " + standCount);
                    watchYourHealth.insertStandCount(standCount, convertDate, true);

                    int activeStandCount =
                            watchYourHealth.getTodayHourlyStandCount(convertDate, ACTIVE_STEPS_COUNT);
                    //System.out.println("Active Stand Count : " +
                    // activeStandCount);
                    Log.d("Active Stand Count : ", convertDate + " --> " + activeStandCount);
                    watchYourHealth.insertActiveHourCount(activeStandCount,
                            convertDate);
                }
                hourlyStep = true;
            } catch (Exception e) {
                Log.e("Stand Exception", e.getMessage());
            }
        }

        int standCount =
                watchYourHealth.getTodayHourlyStandCount(getTodayDateNew());
        watchYourHealth.insertStandCount(standCount, getTodayDateNew(), true);
        int stand = watchYourHealth.getStand();

        int activeStandCount =
                watchYourHealth.getTodayHourlyStandCount(getTodayDateNew(), ACTIVE_STEPS_COUNT);
        Log.d("Active Stand Count : ", getTodayDateNew() + " --> " + activeStandCount);
        watchYourHealth.insertActiveHourCount(activeStandCount,
                getTodayDateNew());
        int activeHour = watchYourHealth.getActiveHour();
    }


    private void getDaysSteps() {
        if (watchYourHealth.isStepExist()) {
            try {
                String lastDate = watchYourHealth.getLastStepsDate();
                //String lastDate = "2019-11-20";
                String todayDateNew = getTodayDateNew();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                Date date = sdf.parse(lastDate);
                Date date1 = sdf.parse(todayDateNew);
                long diff = date1.getTime() - date.getTime();
                int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                totalStepsDays = days;
                //Toast.makeText(HomeActivity.this,""+totalStepsDays,Toast.LENGTH_SHORT).show();
                //new ViewStepsCount(WatchYourHealth.LAST_DATA).execute();
                //System.out.println("Days Data: " + totalStepsDays);
                //new ViewStepsCount(WatchYourHealth.LAST_DATA).execute();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            //new ViewStepsCount(WatchYourHealth.YEAR).execute();
        }
    }

    public void getMinuteSteps() {
        if (watchYourHealth.isMinuteStepExist()) {
            try {
                String lastDate = watchYourHealth.getLastMinuteStepsDate();
                //String lastDate = "2019-11-20";
                String todayDateNew = getTodayDateNew();
                //Toast.makeText(HomeActivity.this, lastDate, Toast.LENGTH_LONG).show();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                if (lastDate != null) {
                    Date date = sdf.parse(lastDate);
                    Date date1 = sdf.parse(todayDateNew);
                    long diff = date1.getTime() - date.getTime();
                    int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    totalMinuteStepsDays = days;
                    //Log.v("Sleep Last Date", lastDate + "." + days);
                } else {
                    totalMinuteStepsDays = 30;
                }
                //new ViewStepsCount(WatchYourHealth.SLEEP_DATA).execute();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            totalMinuteStepsDays = 30;
            //new ViewStepsCount(WatchYourHealth.SLEEP_DATA).execute();
        }
    }

    public static String convertMinutesIntoHour(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        String convertedHour = "";
        String hour = "";
        if (hours < 10) {
            hour = "0" + hours;
        } else {
            hour = "" + hours;
        }
        if (minutes < 10) {
            convertedHour = hour + ":0" + minutes;
        } else {
            convertedHour = hour + ":" + minutes;
        }
        return convertedHour;
    }

    private void getActiveHours() {
        //watchYourHealth = new WatchYourHealth(HomeActivity.this);
        if (SharedPref.getFitBitConnection()) {
            watchYourHealth.getActiveHour();
        } else {
//            SharedPref.putTodayActiveHours(String.valueOf(watchYourHealth.getActiveHour()));
            //binding.tvActiveCount.setText(SharedPref.getTodayActiveHours());
        }
    }

    public void checkHealthConnectPermission() {
        // Calling the Kotlin wrapper function
        CompletableFuture<Boolean> futureResult = healthConnectUtil.checkPermissionsDisconnectForJava();

        // Handling the result
        futureResult.thenAccept(isPermissionGranted -> {
            if (isPermissionGranted) {
                System.out.println("Permissions granted!");
                runOnUiThread(() -> {
                    try {
                        HealthConnectSteps();
                        if (healthConnectUtil.checkPermissionsDisconnect()) {
                            new Handler().postDelayed(() -> {
                                try {
                                    if (SharedPreference.getGoogleFitConnection()) {
                                        //binding.btnInstall.setVisibility(View.GONE);
                                        updateRewards();
                                        binding.fitConnection.setText("Connected");
                                        binding.fitConnection.setTextColor(getResources().getColor(R.color.green_teal));
                                        setFitChecked(true);
                                    } else {
                                        binding.fitConnection.setTextColor(getResources().getColor(R.color.alert_red));
                                        binding.fitConnection.setText("Disconnected");
                                        setFitChecked(false);
                                    }
                                } catch (Exception e) {

                                }

                            }, 500);
                        }
                    } catch (Exception e) {

                    }
                });

            } else {
                System.out.println("Permissions not granted.");
                Log.d("openDialog", "Start");

                // Handle the case when permissions are not granted
            }
        }).exceptionally(throwable -> {
            // Handle any errors
            System.out.println("Error occurred: " + throwable.getMessage());
            return null;
        });
    }
    //endregion

    public void setFitChecked(boolean isChecked) {
        binding.HCSwitch.setOnCheckedChangeListener(null);
        binding.HCSwitch.setChecked(isChecked);
        binding.HCSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!SharedPreference.getGoogleFitConnection())
                        connectWithGoogleFit();
                } else {
                    if (SharedPreference.getGoogleFitConnection()) {
                        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    dialog.dismiss();
                                    removeGmailPermission();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    setFitChecked(true);
                                    break;
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConnectApp.this);
                        builder.setMessage("Are you sure you want to Disconnect?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();

                    }


                }


            }
        });
    }


    public void removeGmailPermission() {
        Constants.authInProgress = false;
        healthConnectUtil.showPermissionDeniedDialog(1);

        /*SharedPreference.putGoogleFitConnection(false);
        binding.HCSwitch.setChecked(false);
        binding.fitConnection.setTextColor(getResources().getColor(R.color.alert_red));
        binding.fitConnection.setText("Disconnected");*/
    }

    public void connectWithGoogleFit() {
        if (!SharedPref.getFitBitConnection()) {
            healthConnectUtil.checkHealthConnectSdkStatus();
        }
    }

    private void updateRewards() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("ActivityUploads", "")
                .addFormDataPart("eventName", "SyncDevice")
                .addFormDataPart("eventCategory", TAG_REWARD_EVENT)
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(this) + "Rewards/EarnRewards")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                finish();
                //Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    if (response.code() == 200 && response.body() != null) {
                        CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                        if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                                    }
                                    if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                                    }
                                    showRewardsPopupDialogBox();
                                }
                            });
                        } else if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getTokens() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, commonSuccessResponse.getEnGTokens().getTokens()));
                        } else if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getBonusTokens() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, commonSuccessResponse.getEnGTokens().getBonusTokens()));
                        } else {
                            finish();
                        }

                    } else
                        finish();
                }
            }
        });
    }

    private void showRewardsPopupDialogBox() {
        PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(0);
        showRewardsPopupNew(firstData.getValue(), context);
    }

    private void showRewardsPopupNew(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogRewardPopup = alertBuilder.create();
        alertDialogRewardPopup.setCancelable(false);
        if (!alertDialogRewardPopup.isShowing())
            alertDialogRewardPopup.show();

        NewDashboardHelper.Companion.getPopUpShowModels().remove(0);

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

//        binding.btnPositive.setText("Collect");


        alertDialogRewardPopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();

            }
        });


        binding.tvPoints.setText(points);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> {
            alertDialogRewardPopup.dismiss();
        });


        binding.btnPositive.setOnClickListener(view -> {
            alertDialogRewardPopup.dismiss();
        });
        binding.scratchView.setScratchListener(ConnectApp.this);

        binding.btnNegative.setOnClickListener(view -> alertDialogRewardPopup.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();


        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogRewardPopup.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialogRewardPopup.getWindow().setLayout((int) (displayRectangle.width() * 0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchStarted() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i > 20) {
            scratchCardLayout.onFullReveal();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                if (alertDialogRewardPopup != null && alertDialogRewardPopup.isShowing()) {
                    alertDialogRewardPopup.dismiss();
                }
            }, 3000);
        }
    }
}