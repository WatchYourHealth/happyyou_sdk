package com.wyh.happyyousdk;

import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_COMPLETED;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.dass21.Dass21AnalysisActivity;
import com.wyh.happyyousdk.dass21.Dass21QuestionsActivity;
import com.wyh.happyyousdk.databinding.ActivityWellBeingDisclaimerBinding;
;
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding;
import com.wyh.happyyousdk.heartAge.HeartAgeAnalysisActivity;
import com.wyh.happyyousdk.heartAge.HeartAgeQuestionsActivity;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.ira.IRAAnalysisActivity;
import com.wyh.happyyousdk.ira.IraActivity;
import com.wyh.happyyousdk.model.request.CommunityIDs;
import com.wyh.happyyousdk.model.request.FacnScanInTribeRequest;
import com.wyh.happyyousdk.model.response.DownloadFaceScanResponse;
import com.wyh.happyyousdk.model.response.FaceScanInTribeResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WellBeingDisclaimerActivity extends AppCompatActivity {

    ActivityWellBeingDisclaimerBinding binding;
    Context context;
    String categoryName, cameFrom;
    boolean isAnalysis, shownScreen;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh, apiInterfaceWyhFaceScan;
    List<Integer> tribeListId = new ArrayList<>();
    ArrayList<CommunityIDs> list = new ArrayList<>();

    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_being_disclaimer);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_well_being_disclaimer);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        apiInterfaceWyhFaceScan = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cameFrom = extras.getString("came_from");
            categoryName = extras.getString("CategoryName");
            isAnalysis = extras.getBoolean("isAnalysis");
            shownScreen = extras.getBoolean("shownScreen");
        }

        setToolBar();
    }

    private void setToolBar() {
        binding.includeToolbar.tvBack.setText(categoryName);
        binding.includeToolbar.ivMenu.setVisibility(View.GONE);
        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));
        binding.includeToolbar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.tvContent.loadData(getResources().getString(getMessage(cameFrom)), "text/html", "utf-8");

        binding.ivImage.setBackgroundResource(getImage(cameFrom));

        if (shownScreen) {
            binding.btnStartNow.setText("Proceed");
        } else {
            binding.btnStartNow.setText("OK");
        }

        binding.btnStartNow.setOnClickListener(v -> {
            if (shownScreen) {
                nextScreen(cameFrom);
            } else {
                finish();
            }
        });
    }

    private void nextScreen(String cameFrom) {
        Intent intent;
        switch (cameFrom) {
            case "KnowYourHealth":
                if (isAnalysis) {
                    intent = new Intent(context, HRAAnalysisActivity.class);
                } else {
                    intent = new Intent(context, HRAQuestionsActivity.class);
                }
                startActivity(intent);
                finish();
                break;
            case "KnowYourImmunity":
                if (isAnalysis) {
                    intent = new Intent(context, IRAAnalysisActivity.class);
                } else {
                    intent = new Intent(context, IraActivity.class);
                }
                intent.putExtra("from", IRA_STATUS_COMPLETED);
                startActivity(intent);
                finish();
                break;
            case "KnowYourDAS":
                if (isAnalysis) {
                    intent = new Intent(context, Dass21AnalysisActivity.class).putExtra("comingFrom","");
                } else {
                    intent = new Intent(context, Dass21QuestionsActivity.class);
                }
                startActivity(intent);
                finish();
                break;
            case "HeartAge":
                if (isAnalysis) {
                    intent = new Intent(context, HeartAgeAnalysisActivity.class);
                } else {
                    intent = new Intent(context, HeartAgeQuestionsActivity.class);
                }
                startActivity(intent);
                finish();
                break;
        }
    }



    private int getMessage(String cameFrom) {
        switch (cameFrom) {
            case "KnowYourHealth":
                return R.string.hra_msg;
            case "KnowYourImmunity":
                return R.string.know_your_immunity_msg;
            case "KnowYourDAS":
                return R.string.das_msg;
            case "HeartAge":
                return R.string.heart_age_msg_new;
        }

        return R.string.hra_msg;
    }

    private int getImage(String cameFrom) {
        switch (cameFrom) {
            case "KnowYourHealth":
                return R.drawable.know_your_health;
            case "KnowYourImmunity":
                return R.drawable.running;
            case "KnowYourDAS":
                return R.drawable.ic_dass_dis_bg;
            case "HeartAge":
                return R.drawable.ic_heart_age_dis_bg;
        }

        return R.drawable.dummy_image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(data.hasExtra("request")){
                Log.d("AuthToken", "Well "+data.getStringExtra("request"));
            }
            if(requestCode == 65 && resultCode == RESULT_OK){
                if(data.hasExtra("request")) {
                    Log.d("AuthToken", "Well "+data.getStringExtra("request"));
                    Intent intent = new Intent();
                    intent.putExtra("request", data.getStringExtra("request"));
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    finish();
                }
            }
        }else {
            finish();
        }

    }
}