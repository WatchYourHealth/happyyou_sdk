package com.wyh.happyyousdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityAboutUsBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutUsActivity extends AppCompatActivity {

    ActivityAboutUsBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        context = this;

        binding.includeBack.llBack.setOnClickListener(view -> {
            finish();
        });
        binding.includeBack.tvBack.setText("About Us");
        binding.includeBack.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeBack.ivBack.setColorFilter(getResources().getColor(R.color.white));
        //binding.tvAppVersionVal.setText(com.wyh.happyyousdk.SDKConstants.appVersionName);

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "AboutUs");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.tvAboutUs.loadData(getResources().getString(R.string.about_us), "text/html", "utf-8");
        binding.tvAboutUs.setBackgroundColor(Color.TRANSPARENT);
         /*SpannableString textCondition = new SpannableString("Terms & Conditions and Privacy policy");
        ClickableSpan termsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TermsAndConditionActivity.class);
                startActivity(intent);
            }
        };
        ClickableSpan privacyPolicy = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        };
        textCondition.setSpan(termsAndCondition, 0, 18, 0);
        textCondition.setSpan(privacyPolicy, 22, 37, 0);

        binding.tvTandc.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvTandc.setText(textCondition, TextView.BufferType.SPANNABLE);
        binding.tvTandc.setText(textCondition, TextView.BufferType.SPANNABLE);*/
        binding.tvTandc.setOnClickListener(v->{
            Intent intent = new Intent(context, TermsAndConditionActivity.class);
            startActivity(intent);
        });
        binding.tvPp.setOnClickListener(v->{
            Intent intent = new Intent(context, PrivacyPolicyActivity.class);
            startActivity(intent);
        });
        String appVersion = com.wyh.happyyousdk.SDKConstants.appVersionName;

        binding.tvAppVersionVal.setText("v" + appVersion);


    }
}