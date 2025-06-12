package com.wyh.happyyousdk;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

;
import com.wyh.happyyousdk.databinding.ActivityPrivacyPolicyBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class PrivacyPolicyActivity extends AppCompatActivity {

    ActivityPrivacyPolicyBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        context = this;

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "PrivacyPolicy");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        setToolBar();
        binding.tvContent.setBackgroundColor(Color.TRANSPARENT);
//        binding.tvContent.loadData(getResources().getString(R.string.privacy_policy_content), "text/html", "utf-8");
        binding.tvContent.loadUrl("file:///android_asset/privacy_policy_content.html");
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.setText(Html.fromHtml(getString(R.string.privacy_policy_content), FROM_HTML_MODE_LEGACY));
        }else{
            binding.tvContent.setText(Html.fromHtml(getString(R.string.privacy_policy_content)));
        }*/
    }

    private void setToolBar(){
        binding.includeToolbar.tvBack.setText("Privacy Policy");
        binding.includeToolbar.ivMenu.setVisibility(View.GONE);
        binding.includeToolbar.llBack.setOnClickListener(v->{onBackPressed();});
    }
}