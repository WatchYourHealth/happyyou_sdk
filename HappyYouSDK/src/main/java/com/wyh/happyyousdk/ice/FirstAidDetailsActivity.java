package com.wyh.happyyousdk.ice;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.FirstAidDetailsPageBinding;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.model.request.ice.InjuryTypeReq;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.ice.FetchInjuryDetailsResp;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstAidDetailsActivity extends AppCompatActivity {

    FirstAidDetailsPageBinding binding;
    ProgressDialog progressDialog;
    Context context;
    ApiInterfaceWyh apiInterfaceWyh;
    String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.first_aid_details_page);
        context = this;
        SharedPref.init(context);
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        fetchInjuriesDetails();

        source = getIntent().getStringExtra("source");

        binding.includeToolbar.tvBack.setText(getIntent().getStringExtra("injuryType"));
        binding.includeToolbar.tvBack.setTextColor(getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getColor(R.color.white));
        binding.includeToolbar.llBack.setOnClickListener(view -> {
            onBackPressed();
        });
        /*binding.tvTitle.setOnClickListener(view -> {
            onBackPressed();
        });*/

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


    }

    private void fetchInjuriesDetails() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        InjuryTypeReq injuryTypeReq = new InjuryTypeReq(getIntent().getStringExtra("injuryType"));
        Call<FetchInjuryDetailsResp> call = apiInterfaceWyh.fetchInjuriyDetails(SharedPref.getAuthToken(), injuryTypeReq);
        call.enqueue(new Callback<FetchInjuryDetailsResp>() {
            @Override
            public void onResponse(Call<FetchInjuryDetailsResp> call, Response<FetchInjuryDetailsResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_fetch_injury_details_success));
                    if (response.body().getData() != null && response.body().getSuccess()) {
                        Glide.with(context)
                                .load(response.body().getData().get(0).getInjuryImagePath())
                                .placeholder(R.drawable.ic_ambulance)
                                .into(binding.ivActivityBg);
                        binding.tvBlogDescription.setText(Html.fromHtml(response.body().getData().get(0).getInjuryDescription()));
                        binding.tvSourceLink.setText(source);
                    } else {
                        Toast.makeText(FirstAidDetailsActivity.this, "No Injuries data found", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 401) {
                    refreshAuthToken(null);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_fetch_injury_details_failed));
                    Toast.makeText(FirstAidDetailsActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchInjuryDetailsResp> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_fetch_injury_details_failed));
                Toast.makeText(FirstAidDetailsActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshAuthToken(String comName) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        String deviceModel = Build.BRAND + " " + Build.MODEL;
        String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
        String appVersion = SDKConstants.appVersionName;
        RefreshTokenRequest request = new RefreshTokenRequest(deviceModel, osVersion, appVersion);
        Call<RefreshTokenResponse> call = apiInterfaceWyh.refreshToken(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess() &&
                        response.body().getData().getAuthToken() != null && !response.body().getData().getAuthToken().equals("")) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer "+response.body().getData().getAuthToken());
                    SharedPreference.init(context);
                    SharedPreference.putAuthToken("Bearer "+response.body().getData().getAuthToken());
                    fetchInjuriesDetails();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                    /*Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                    Master.INSTANCE.logOut(context);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                /*Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

}
