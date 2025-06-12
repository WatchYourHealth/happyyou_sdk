package com.wyh.happyyousdk.fitConnect;

import static com.wyh.happyyousdk.utils.Constants.ACTIVEHOURS;
import static com.wyh.happyyousdk.utils.Constants.CALORIE;
import static com.wyh.happyyousdk.utils.Constants.MEDITATION;
import static com.wyh.happyyousdk.utils.Constants.SLEEP;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.WATER;
import static com.wyhsdk.utils.Constants.AUTH_PENDING;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.absorb.HealthHacksActivity;
import com.wyh.happyyousdk.databinding.ActivityFitConnectBinding;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.unwind.UnwindActivity;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.main.WatchYourHealth;
import com.wyhsdk.sharedPreferences.SharedPreference;
import com.wyhsdk.utils.Constants;

public class FitConnectActivity extends AppCompatActivity {
    ActivityFitConnectBinding binding;
    Context context;
    WatchYourHealth watchYourHealth;
    private final int REQUEST_CODE_ACTIVITY_RECOGNITION = 1;
    int totalStepsDays, totalStandDays, totalMinuteStepsDays;
    boolean sendToServer = false;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fit_connect);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(this);
        watchYourHealth = new WatchYourHealth(this, SharedPref.getUuid());
        watchYourHealth.initializeAPIClient(savedInstanceState);
        binding.rlGoogleFitConnect.setOnClickListener(view -> {
            connectToGooglefit();
           /* Intent intent1 = new Intent(getApplicationContext(), SendDataToServerReceiver.class);
            getApplicationContext().sendBroadcast(intent1);*/
        });

        binding.rlAbsorb.setOnClickListener(view -> {

            Intent i = new Intent(context, HealthHacksActivity.class);
            startActivity(i);
        }); binding.rlUnwind.setOnClickListener(view -> {

            Intent i = new Intent(context, UnwindActivity.class);
            i.putExtra("comingFrom","fitConnection");
            startActivity(i);
        });

        boolean googleFitToggle = SharedPreference.getGoogleFitConnection();
        if (googleFitToggle) {
            binding.fitSwitch.setChecked(true);
//            googleFitConnected.setTextColor(getResources().getColor(R.color.colorGreen));
//            googleFitConnected.setText("Connected");
        } else {
            binding.fitSwitch.setChecked(false);
        }
        binding.rlSteps.setOnClickListener(view -> {
            intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", STEPS);
            startActivity(intent);
        });
        binding.rlSleep.setOnClickListener(view -> {
            intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", SLEEP);
            startActivity(intent);

        });
        binding.rlWater.setOnClickListener(view -> {
            intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", WATER);
            startActivity(intent);
        });
        binding.rlActiveHours.setOnClickListener(view -> {
            intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", ACTIVEHOURS);
            startActivity(intent);
        });
        binding.rlCalorie.setOnClickListener(view -> {
            intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", CALORIE);
            startActivity(intent);
        });

        binding.rlMeditation.setOnClickListener(view -> {
            intent = new Intent(context, TrendsActivity.class);
            intent.putExtra("activityType", MEDITATION);
            startActivity(intent);
        });

        binding.fitSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (savedInstanceState != null) {
                    Constants.authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
                }
                //watchYourHealth.initializeAPIClient(savedInstanceState);
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACTIVITY_RECOGNITION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                                REQUEST_CODE_ACTIVITY_RECOGNITION);
                    } else {
                        watchYourHealth.connectAPIClient();
                    }
                } else {
                    watchYourHealth.connectAPIClient();
                }
//                googleFitConnected.setText("Connected");
//                googleFitConnected.setTextColor(getResources().getColor(R.color.colorGreen));
            } else {
                removeGmailPermission();
            }
        });
    }

    public void removeGmailPermission() {
        Constants.authInProgress = false;
        watchYourHealth.disconnectAPIClient();
        watchYourHealth.revokePermission(context);
//        fitBitSwitch.setChecked(false);
//        fitBitConnected.setTextColor(getResources().getColor(R.color.colorRed));
//        fitBitConnected.setText("Disconnected");
        //Toast.makeText(getApplicationContext(), "GoogleFit Disconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        watchYourHealth.getGoogleFitPermission(requestCode, resultCode);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    SharedPreference.init(this);
                    SharedPreference.putGoogleFitConnection(true);
                    //getSteps();
                    Toast.makeText(this, "GoogleFit Connected Successfully!! ", Toast.LENGTH_SHORT).show();
                    finish();
//                    binding.btnGoogleFit.setVisibility(View.GONE);
//                    getSteps();
//                    getSleep();

                } else {
                    Toast.makeText(this, "GoogleFit Connection Failed..", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ACTIVITY_RECOGNITION) {
            // If request is cancelled, the result arrays are empty.
            ////Log.v("Data", "onRequestPermisson called");
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                watchYourHealth.connectAPIClient();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                /*fitBitSwitch.setChecked(false);
                fitBitConnected.setTextColor(getResources().getColor(R.color.colorRed));
                fitBitConnected.setText("Disconnected");*/
                Toast.makeText(this, "Physical activity permission required to use step counts.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void connectToGooglefit() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                        REQUEST_CODE_ACTIVITY_RECOGNITION);
            } else {
                watchYourHealth.connectAPIClient();
            }
        } else {
            watchYourHealth.connectAPIClient();
        }
    }
}