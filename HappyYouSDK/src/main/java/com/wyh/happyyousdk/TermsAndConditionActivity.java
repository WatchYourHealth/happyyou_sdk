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
import android.widget.Toast;

;
import com.wyh.happyyousdk.databinding.ActivityTermsandConditionBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class TermsAndConditionActivity extends AppCompatActivity {

    ActivityTermsandConditionBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_termsand_condition);
        context = this;

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "TermsAndConditions");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        setToolBar();

        binding.tvContent.setBackgroundColor(Color.TRANSPARENT);
//        binding.tvContent.loadData(getResources().getString(R.string.terms_and_condition_content), "text/html", "utf-8");
        binding.tvContent.loadUrl("file:///android_asset/terms_and_condition_content.html");
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.setText(Html.fromHtml(getString(R.string.terms_and_condition_content), FROM_HTML_MODE_LEGACY));
        }else{
            binding.tvContent.setText(Html.fromHtml(getString(R.string.terms_and_condition_content)));
        }*/

    }

    private void setToolBar(){
        binding.includeToolbar.tvBack.setText("Terms of Use");
        binding.includeToolbar.ivMenu.setVisibility(View.GONE);
        binding.includeToolbar.llBack.setOnClickListener(v->{onBackPressed();});
    }
}