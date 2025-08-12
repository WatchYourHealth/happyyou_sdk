package com.wyh.happyyousdk.ice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityFirstAidOtherDetailsBinding;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

public class FirstAidOtherDetailsActivity extends AppCompatActivity {

    ActivityFirstAidOtherDetailsBinding binding;
    String cameFrom;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_first_aid_other_details);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);


        cameFrom = getIntent().getStringExtra("came_from");

        binding.includeBack.llBack.setOnClickListener(view -> {
            finish();
        });
        binding.includeBack.tvBack.setText(getToolBarName(cameFrom));


        Glide.with(context).load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + getImage(cameFrom))
                .into(binding.ivICEImage);

//        binding.ivICEImage.setImageResource(getImage(cameFrom));

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }

    private String getToolBarName(String cameFrom) {
        switch (cameFrom) {
            case "cpr":
                return "CPR";
            case "stroke":
                return "Stroke";
            case "seizure":
                return "Seizure";
            case "loss_of_breath":
                return "Loss of Breath";
        }
        return "";
    }

    private String getImage(String cameFrom) {
        switch (cameFrom) {
            case "cpr":
                return "ice_cpr.jpg";
            case "stroke":
                return "ice_stroke.jpg";
            case "seizure":
                return "ice_seizure.jpg";
            case "loss_of_breath":
                return "ice_loss_of_breath.jpg";
        }
        return "";
    }

}