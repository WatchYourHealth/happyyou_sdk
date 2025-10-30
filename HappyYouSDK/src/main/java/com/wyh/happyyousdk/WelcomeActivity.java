package com.wyh.happyyousdk;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
;


import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityWelcomeBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.RewardsModel;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {

    ActivityWelcomeBinding binding;
    Context context;
    ApiInterfaceWyh apiInterfaceWyh;
    RewardsModel rewardsModel = new RewardsModel();
    ProgressDialog progressDialog;
    boolean isCameFromSpinWheel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        context = this;

//        getWindow().getDecorView().findViewById(android.R.id.content);
//        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets bars = insets.getInsets(
//                    WindowInsetsCompat.Type.systemBars()
//            );
//            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
//            binding.getRoot().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//            return insets;
//        });

        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_waving.json");

        if (SharedPref.getUserName() == null || SharedPref.getUserName() == "") {
            SharedPref.putUserName("User");
        } else {
            binding.tvName.setText(SharedPref.getUserName());
        }
        isCameFromSpinWheel = getIntent().getBooleanExtra("isCameFromSpinWheel",false);


        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "WelcomePage");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        


        binding.llContinue.setOnClickListener(view -> {
            checkLoginRewards();
        });

    }

    private void checkLoginRewards() {
        Call<CommonSuccessResponse> call = apiInterfaceWyh.getLoginRewards(SharedPref.getAuthToken());
        CommonUtils.showProgressDialige(context);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                CommonUtils.dismissDialoge();
                Log.d("AuthToken", "Response: " + new Gson().toJson(response.body()) + ", code: " + response.code());
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getRewards() != null) {
                        rewardsModel = response.body().getRewards();
                    }
                }
                continueToDashboard();
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {
                Log.d("AuthToken", "OnFailure: " + t.getMessage());
                CommonUtils.dismissDialoge();
                continueToDashboard();
            }
        });
    }

    private void continueToDashboard() {
        Intent intent = new Intent(context, NewDashboardActivity.class);
        intent.putExtra("loginRewardsModel", (Serializable) rewardsModel);
        intent.putExtra("isCameFromSpinWheel",isCameFromSpinWheel);
        startActivity(intent);
        finishAffinity();
    }


}