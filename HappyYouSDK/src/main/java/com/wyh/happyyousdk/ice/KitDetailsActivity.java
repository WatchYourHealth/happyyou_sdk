package com.wyh.happyyousdk.ice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityKitDetailsBinding;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

public class KitDetailsActivity extends AppCompatActivity {

    ActivityKitDetailsBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kit_details);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        binding.tvBlogDescription.setText(Html.fromHtml(getIntent().getStringExtra("kitDetails")));
        binding.includeToolbar.tvBack.setText(getIntent().getStringExtra("title"));
        Glide.with(context)
                .load(getIntent().getStringExtra("imgPath"))
                .placeholder(R.drawable.ic_ambulance)
                .into(binding.ivActivityBg);
        binding.includeToolbar.llBack.setOnClickListener(view -> {
            finish();
        });
        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}