package com.wyh.happyyousdk.sendActivityData;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.ScratchCardPopUpBinding;
import com.wyh.happyyousdk.databinding.ScratchCardPopUpBonusBinding;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.sendActivityData.LastDateDataSendToServer.LastDataDateSynced;
import com.wyh.happyyousdk.sendActivityData.LastDateDataSendToServer.LastDateDataRequest;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyhsdk.main.WatchYourHealth;
import com.wyhsdk.model.LastDateData;
import com.wyhsdk.sharedPreferences.SharedPreference;
import com.wyhsdk.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SendDataToServerReceiver extends BroadcastReceiver implements LastDataDateSynced {

    WatchYourHealth watchYourHealth = null;
    String stepsDate, sleepDate, activeHourDate;
    Context context;
    boolean allowHeader;
    String baseUrl;
    AlertDialog alertDialogBonusRewards;
    private static boolean firstConnect = false;

    @Override
    public void onReceive(final Context context, Intent intent) {

        String action = intent.getAction();


        this.context = context;

        stepsDate = "";
        sleepDate = "";
        activeHourDate = "";

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            //startServiceDirectly(context);
            startServiceByAlarm(context);
        } else {

           /* boolean isDebuggable = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
            if (isDebuggable) {
                baseUrl = context.getString(R.string.test_activity_url);
            } else {*/
            baseUrl = CommonUtils.getBaseUrlForAPI(context) + "Trends/";
            // }

            allowHeader = true; //Change this to true if api needs Headers: Token & CRN

            SharedPreference.init(context);
            boolean googleFitConnection = SharedPreference.getGoogleFitConnection();

            if (googleFitConnection) {
                if (!firstConnect) {
                    watchYourHealth = new WatchYourHealth(context);
                    new LastDateDataRequest(context, allowHeader, this).execute();

                    firstConnect = true;
                }
            } else {
            }

        }
        new Handler().postDelayed(() -> firstConnect = false, 20 * 1000);
    }

    @Override
    public synchronized void onLastDateDataCompleted(Context context, ArrayList<LastDateData> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                Log.d("AuthToken", "Sending Sleep Data " + arrayList.size());
                String dateStr = arrayList.get(i).getTimeStamp();
                SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    watchYourHealth = new WatchYourHealth(context);
                    Log.d("AuthToken", "Sending Sleep Data " + baseUrl);

                    switch (arrayList.get(i).getTrendType()) {
                        case "STEPS": {
                            Date dateObj = curFormater.parse(dateStr);
                            Calendar calendar = Calendar.getInstance();
                            stepsDate = curFormater.format(dateObj);
                            /*if (dateObj != null) {
                                calendar.setTime(dateObj);
                                calendar.add(Calendar.DAY_OF_MONTH, -2);
                                stepsDate = curFormater.format(calendar.getTime());
                            } else {
                                stepsDate = curFormater.format(dateObj);
                            }*/
                            break;
                        }
                        case "SLEEP": {
                            Date dateObj = curFormater.parse(dateStr);
                            sleepDate = curFormater.format(dateObj);
                            break;
                        }
                        case "ACTIVEHOURS": {
                            Date dateObj = curFormater.parse(dateStr);
                            activeHourDate = curFormater.format(dateObj);
                            break;
                        }
                    }
                } catch (Exception e) {
                }
            }
            sendData(context);
        } else {
            //Sending Data first time
            Log.d("AuthToken", "Sending Sleep Data " + baseUrl);
            watchYourHealth.sendSingleStepDataNew(context, baseUrl, "", allowHeader);
            watchYourHealth.sendSingleSleepDataNew(context, baseUrl, "", allowHeader);
            watchYourHealth.sendSingleActiveHourDataNew(context, baseUrl, activeHourDate, allowHeader);
        }
    }

    public void showRewardsPopup(String rewards, Context context) {
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

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, RewardsActivity.class);
            context.startActivity(intent);
        });

        binding.scratchView.onFullReveal();
        binding.scratchView.setScratchListener(((NewDashboardActivity) context));

        Rect displayRectangle = new Rect();
        Window window = ((NewDashboardActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }


    private synchronized void sendData(Context context) {

        watchYourHealth.sendSingleStepDataNew(context, baseUrl, stepsDate, allowHeader);
        watchYourHealth.sendSingleSleepDataNew(context, baseUrl, sleepDate, allowHeader);
        watchYourHealth.sendSingleActiveHourDataNew(context, baseUrl, activeHourDate, allowHeader);
    }

    @Override
    public void onLastDateDataError(String s) {
        // Log.v("Request", "Last Date failed");
    }

    private void startServiceByAlarm(Context context) {
        Intent alarmIntent = new Intent(context, SendDataToServerReceiver.class);
//        CommonUtils.startReminder(context, 8, 0, alarmIntent, 1000 * 60 * 60, 123);
    }

}
