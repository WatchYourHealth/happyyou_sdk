package com.wyh.happyyousdk.syncDevice;

import static com.wyh.happyyousdk.utils.CommonUtils.todayDateInFormat;
import static com.wyh.happyyousdk.utils.Constants.ACTIVE_STEPS_COUNT;
import static com.wyhsdk.main.NewSleepLogic.getNewSleepTime;
import static com.wyhsdk.utils.Constants.SOURCE_GOOGLEFIT;
import static com.wyhsdk.utils.Utilities.getTodayDateNew;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.CompoundButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityConnectAppBinding;
import com.wyh.happyyousdk.main.AppVisibilityTracker;
import com.wyh.happyyousdk.sendActivityData.SendDataToServerReceiver;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.HealthConnect.HealthConnectUtil;
import com.wyhsdk.main.WatchYourHealth;
import com.wyhsdk.sharedPreferences.SharedPreference;
import com.wyhsdk.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ConnectApp extends AppCompatActivity {
    ActivityConnectAppBinding binding;
    public HealthConnectUtil healthConnectUtil;
    WatchYourHealth watchYourHealth;
    int totalStepsDays, totalStandDays, totalMinuteStepsDays;
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
        appVisibilityTracker = new AppVisibilityTracker();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_connect_app);
        healthConnectUtil = new HealthConnectUtil(this, this, permissionLauncher);
        watchYourHealth = new WatchYourHealth(ConnectApp.this, SharedPref.getUuid());
        watchYourHealth.initializeAPIClient(savedInstanceState);
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.btnInstall.setOnClickListener(v -> healthConnectUtil.checkHealthConnectSdkStatusA(true));

        binding.HCSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {});
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
                if (appVisibilityTracker.isAppinForeground()) {
                    healthConnectUtil.aggregateStepsIntoDays(startTime, endTime);
                    healthConnectUtil.aggregateStepsIntoHour(startTime, endTime);
                    healthConnectUtil.aggregateStepsIntoMinutes(startTime, endTime);
                }
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

            }
            else {
                getDaysSteps();
                getHourlySteps();
                getMinuteSteps();
                LocalDateTime startTime = LocalDateTime.now().minusDays(totalStepsDays).withMinute(0).withHour(0).withSecond(0);
                LocalDateTime startTimeHours = LocalDateTime.now().minusDays(totalStandDays).withMinute(0).withHour(0).withSecond(0);
                LocalDateTime startTimeMinute = LocalDateTime.now().minusDays(totalMinuteStepsDays).withMinute(0).withHour(0).withSecond(0);
                LocalDateTime endTime = LocalDateTime.now();
                if (appVisibilityTracker.isAppinForeground()) {
                    healthConnectUtil.aggregateStepsIntoDays(startTime, endTime);
                    healthConnectUtil.aggregateStepsIntoHour(startTimeHours, endTime);
                }
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
        }catch (Exception e){}
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

        SharedPreference.putGoogleFitConnection(false);
        binding.HCSwitch.setChecked(false);
        binding.fitConnection.setTextColor(getResources().getColor(R.color.alert_red));
        binding.fitConnection.setText("Disconnected");
    }

    public void connectWithGoogleFit() {
        if (!SharedPref.getFitBitConnection()) {
            healthConnectUtil.checkHealthConnectSdkStatus();
        }
    }
}