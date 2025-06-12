package com.wyh.happyyousdk.dashboard;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityLifeStyleWelcomeBinding;
import com.wyh.happyyousdk.databinding.ActivityLifestyleBinding;
import com.wyh.happyyousdk.utils.SharedPref;

public class LifeStyleWelcomeActivity extends AppCompatActivity {

    ActivityLifeStyleWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_life_style_welcome);
        SharedPref.init(this);
        binding.commonToolBar.tvBack.setText("Lifestyle");
        binding.commonToolBar.llBack.setOnClickListener(v->{onBackPressed();});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome), FROM_HTML_MODE_LEGACY));
        }else{
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome)));
        }

        binding.btnNext.setOnClickListener(v->{
            Intent intent = new Intent(this, LifestyleActivity.class);
            startActivity(intent);
            finish();
        });
    }
}