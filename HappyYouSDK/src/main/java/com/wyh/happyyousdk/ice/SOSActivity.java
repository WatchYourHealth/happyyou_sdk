package com.wyh.happyyousdk.ice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivitySos2Binding;
import com.wyh.happyyousdk.model.response.ice.FetchEmergencyDetailsResp;
import com.wyh.happyyousdk.utils.SharedPref;

public class SOSActivity extends AppCompatActivity {
    ActivitySos2Binding binding;
    Context context;
    CountDownTimer countDownTimer;

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sos_2);
        context = this;
        SharedPref.init(context);

        binding.includeBack.tvBack.setText("ICE (In Case of Emergency)");
        binding.includeBack.llBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.includeBack.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeBack.ivBack.setColorFilter(getResources().getColor(R.color.white));

        binding.cvCancel.setOnClickListener(view -> {
            countDownTimer.cancel();
            finish();
        });
        countDownTimer = new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.tvTimer.setText("" + millisUntilFinished / 1000);
                // logic to set the EditText could go here
            }

            public void onFinish() {
                //binding.tvTimer.setText("done!");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                if (!SharedPref.getEmergencyContact().isEmpty() && SharedPref.getEmergencyContact() != null) {
                    intent.setData(Uri.parse("tel:" + SharedPref.getEmergencyContact()));
                } else {
                    intent.setData(Uri.parse("tel:112"));
                }
                startActivity(intent);
                finish();
            }

        }.start();

        binding.tvSkip.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            if (!SharedPref.getEmergencyContact().isEmpty() && SharedPref.getEmergencyContact() != null) {
                intent.setData(Uri.parse("tel:" + SharedPref.getEmergencyContact()));
            } else {
                intent.setData(Uri.parse("tel:112"));
            }
            startActivity(intent);
            countDownTimer.cancel();
            finish();
        });
    }
}
