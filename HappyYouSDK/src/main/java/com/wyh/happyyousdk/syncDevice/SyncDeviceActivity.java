package com.wyh.happyyousdk.syncDevice;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.convertMinutesIntoHour;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;
import static com.wyh.happyyousdk.utils.Constants.perCalorieInStep;
import static com.wyh.happyyousdk.utils.Constants.perStepInKm;
import static com.wyhsdk.main.NewSleepLogic.getNewSleepTime;
import static com.wyhsdk.utils.Utilities.getTodayDateNew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
;
import com.wyh.happyyousdk.APIEncryption.BackgroundWork.CoroutineClass;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.absorb.QuickReadActivity;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.PostLoginActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivitySyncDeviceBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.NewSmallPopUpRewardsBinding;
import com.wyh.happyyousdk.databinding.ScratchCardPopUpBinding;
import com.wyh.happyyousdk.databinding.ScratchCardPopUpBonusBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.PendingActivityDashboard;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.sendActivityData.SendDataToServerReceiver;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.main.WatchYourHealth;
import com.wyhsdk.sharedPreferences.SharedPreference;
import com.wyhsdk.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SyncDeviceActivity extends AppCompatActivity implements ScratchListener {

    ActivitySyncDeviceBinding binding;
    Context context;
    private final int REQUEST_CODE_ACTIVITY_RECOGNITION = 1;
    WatchYourHealth watchYourHealth;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    boolean sendToServer = false;
    int totalStepsDays, totalStandDays, totalMinuteStepsDays;
    boolean isStamps = false;
    boolean isFirst;
    boolean isPositiveBtn = false;
    AlertDialog alertDialogRewardPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sync_device);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        watchYourHealth = new WatchYourHealth(this, SharedPref.getUuid());
        watchYourHealth.initializeAPIClient(savedInstanceState);

        isFirst = getIntent().getBooleanExtra("isFirst", false);

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "SyncDevice");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.includeToolbar.llBack.setOnClickListener(v -> {
            if (isFirst) {
                Intent intent = new Intent(context, PostLoginActivity.class);
                startActivity(intent);
            }
            finish();
        });

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });




        binding.rlGoogleFit.setOnClickListener(v -> {
            if (SharedPreference.getGoogleFitConnection()) {
                Toast.makeText(context, "You are already connected to Google Fit", Toast.LENGTH_SHORT).show();
            } else {
//                    binding.drawer.closeDrawer(Gravity.RIGHT);
                getFitPermission();
            }
        });

//        showRewardsPopupNew("Stamps Tribe;Walk minimum 60,000 steps per week.", context);

    }

    @Override
    public void onBackPressed() {
        if (isFirst) {
            Intent intent = new Intent(context, PostLoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ACTIVITY_RECOGNITION) {
            // If request is cancelled, the result arrays are empty.
            //Log.v("Data", "onRequestPermisson called");
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                watchYourHealth.connectAPIClient();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(this, "Physical activity permission required to use step counts.",
                        Toast.LENGTH_LONG).show();

                if (isFirst) {
                    Intent intent = new Intent(context, PostLoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        watchYourHealth.getGoogleFitPermission(requestCode, resultCode);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                SharedPreference.putGoogleFitConnection(true);
                getSteps();
                Toast.makeText(this, "GoogleFit Connected Successfully!! ", Toast.LENGTH_SHORT).show();
                updateRewards();
            } else {
                if (isFirst) {
                    Intent intent = new Intent(context, PostLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(this, "GoogleFit Connection Failed..", Toast.LENGTH_SHORT).show();
            }
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
                .url(getBaseUrlForAPI(context) + "Rewards/EarnRewards")
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

    private void showRewardsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        ScratchCardPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.scratch_card_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvPoints.setText(points);
//        binding.btnPositive.setText("Collect");
        binding.btnNegative.setVisibility(View.GONE);

        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            if(isFirst){
                Intent intent = new Intent(context, PostLoginActivity.class);
                startActivity(intent);
                finish();
            }

        });
        binding.scratchView.setScratchListener(SyncDeviceActivity.this);

        binding.btnNegative.setOnClickListener(view -> {
            alertDialog.dismiss();
            finish();
        });

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
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
                if(isFirst) {
                    Intent intent = new Intent(context, PostLoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    finish();
                }

            }
        });


        binding.tvPoints.setText(points);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> {
            alertDialogRewardPopup.dismiss();
        });


        binding.btnPositive.setOnClickListener(view -> {
           isPositiveBtn = true;
            alertDialogRewardPopup.dismiss();
        });
        binding.scratchView.setScratchListener(SyncDeviceActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialogRewardPopup.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();


        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogRewardPopup.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialogRewardPopup.getWindow().setLayout((int) (displayRectangle.width() * 0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusRewardsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

         alertDialog.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            }else{
                showRewardsPopupDialogBox();
            }
        });
        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);

        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
            finish();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
            finish();
        });
        binding.scratchView.setScratchListener(SyncDeviceActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((SyncDeviceActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }

    private void showRewardsPopupDialogBox() {
        PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(0);
        showRewardsPopupNew(firstData.getValue(), context);
    }

    private void getFitPermission() {
        Handler handler = new Handler();
        if (!SharedPref.getFitBitConnection()) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                            REQUEST_CODE_ACTIVITY_RECOGNITION);
                } else {
                    //Log.v("Data", "Inside Loop MONTH");
                    watchYourHealth.connectAPIClient();
                    boolean googleFitConnection = SharedPreference.getGoogleFitConnection();
                    if (googleFitConnection) {
                        //new ViewStepsCount(WatchYourHealth.MONTH).execute();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 45 seconds
                                //new SyncDeviceActivity.ViewStepsCount(WatchYourHealth.MONTH).execute();
                                new CoroutineClass().runBackGroundTask(WatchYourHealth.MONTH,context);
                                handler.postDelayed(this, 1000 * 30);
                            }
                        }, 1000);
                    }
                }
            } else {
                watchYourHealth.connectAPIClient();
                boolean googleFitConnection = SharedPreference.getGoogleFitConnection();
                if (googleFitConnection) {
                    /*if (!SharedPref.getGoogleFitBadge()) {
                        assignBadge("3", "tech", "GoogleFit", "Health");
                    }*/
                    //new ViewStepsCount(WatchYourHealth.MONTH).execute();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Log.v("Data", "Inside Loop MONTH");
                            //Do something after 45 seconds
                            //new SyncDeviceActivity.ViewStepsCount(WatchYourHealth.MONTH).execute();
                            new CoroutineClass().runBackGroundTask(WatchYourHealth.MONTH,context);
                            handler.postDelayed(this, 1000 * 30);
                        }
                    }, 1000);
                }
            }
        }
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {

        if (i > 20) {

           /* if (isStamps) {
                isStamps = false;
                scratchTokenReward();
            }*/
            scratchCardLayout.onFullReveal();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(alertDialogRewardPopup!= null && alertDialogRewardPopup.isShowing()){
                        alertDialogRewardPopup.dismiss();
                    }
                }
            }, 3000);
        }
    }

    @Override
    public void onScratchStarted() {

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
            //new SyncDeviceActivity.ViewStepsCount(WatchYourHealth.YEAR).execute();
            new CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR,context);
        }
    }

    public void getMinuteSteps() {
        if (watchYourHealth.isMinuteStepExist()) {
            try {
                String lastDate = watchYourHealth.getLastMinuteStepsDate();
                //String lastDate = "2019-11-20";
                String todayDateNew = getTodayDateNew();
                //Toast.makeText(MainService.this, lastDate, Toast.LENGTH_LONG).show();
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
                //new SyncDeviceActivity.ViewStepsCount(WatchYourHealth.SLEEP_DATA).execute();
                new CoroutineClass().runBackGroundTask(WatchYourHealth.SLEEP_DATA,context);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            totalMinuteStepsDays = 30;
            //new SyncDeviceActivity.ViewStepsCount(WatchYourHealth.SLEEP_DATA).execute();
            new CoroutineClass().runBackGroundTask(WatchYourHealth.SLEEP_DATA,context);
        }
    }

    private void getSteps() {
        //watchYourHealth = new WatchYourHealth(HomeActivity.this);
        if (SharedPreference.getGoogleFitConnection()) {
            String stepCount = watchYourHealth.getTotalSteps(Constants.SOURCE_GOOGLEFIT);
            SharedPref.putTodaySteps(stepCount);
            double totalWalkedSteps = Integer.parseInt(stepCount) * perStepInKm;
            String walkedValue = String.format("%.2f", totalWalkedSteps);

            int totalCalorieBurned = (int) Math.round(Integer.parseInt(stepCount) * perCalorieInStep);
           /* if (homeCarouselAdapter != null)
                homeCarouselAdapter.notifyDataSetChanged();
            if (homeCardsAdapter != null)
                homeCardsAdapter.notifyDataSetChanged();*/
        }
    }

    private void getSleep() {
        //watchYourHealth = new WatchYourHealth(HomeActivity.this);
        int sleep = 0;
        if (SharedPreference.getGoogleFitConnection()) {
            sleep = watchYourHealth.getSleep(Constants.SOURCE_GOOGLEFIT, CommonUtils.todayDate());
        }
        if (sleep != 0) {
            String sleepHourCount = convertMinutesIntoHour(sleep);
        } else {
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
                //new SyncDeviceActivity.ViewStepsCount(WatchYourHealth.YEAR_HOUR).execute();
                new CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR_HOUR,context);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            totalStandDays = -1;
            //new SyncDeviceActivity.ViewStepsCount(WatchYourHealth.YEAR_HOUR).execute();
            new CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR_HOUR,context);
        }
    }


    private void showStampsPopup(String rewards, Context context) {
        isStamps = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if(rewards.split(";").length == 4){
            String id = rewards.split(";")[3];
            //stampId = Integer.parseInt(id);
        }

        /*if(isOrange){
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));
        }else{
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));
        }*/
//        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


        /*if (rewards.contains("First Login")) {
            binding.btnNegative.setVisibility(View.GONE);
            binding.btnPositive.setText("OK");
            binding.btnPositive.setOnClickListener(view -> {
                alertDialog.dismiss();
                Intent intent = new Intent(context, SyncDeviceActivity.class);
                startActivity(intent);
            });
        } else {

        }*/

//        binding.scratchView.onFullReveal();
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PendingActivityDashboard) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }

    private void showBonusStampPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();


        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];

        if(rewards.split(";").length == 4){
            String id = rewards.split(";")[3];
            //stampId = Integer.parseInt(id);
        }

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);

        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(SyncDeviceActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((SyncDeviceActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }
}