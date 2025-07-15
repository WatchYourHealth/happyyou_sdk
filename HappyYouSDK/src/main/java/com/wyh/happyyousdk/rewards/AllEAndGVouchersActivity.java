package com.wyh.happyyousdk.rewards;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityAllEandGvouchersBinding;
import com.wyh.happyyousdk.databinding.CustomPopupRewardsBinding;
import com.wyh.happyyousdk.utils.Master;

import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.earnAndGrab.BurnTokenRequest;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.adapter.AllVouchersAdapter;
import com.wyh.happyyousdk.model.response.rewards.AllVouchersResponse;
import com.wyh.happyyousdk.model.response.rewards.EandBAvailableVoucher;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllEAndGVouchersActivity extends AppCompatActivity implements AllVouchersAdapter.ClickListenerInterface {

    ActivityAllEandGvouchersBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    AlertDialog alertDialog;
    int tokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_eand_gvouchers);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        tokens = getIntent().getIntExtra("tokens", 0);

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Grab Opportunities");

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        getAllVouchers();
    }

    private void getAllVouchers() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<AllVouchersResponse> call = apiInterfaceWyh.getAllAvailableVouchers(SharedPref.getAuthToken());
        call.enqueue(new Callback<AllVouchersResponse>() {
            @Override
            public void onResponse(Call<AllVouchersResponse> call, Response<AllVouchersResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {
                    refreshAuthToken();
                }
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_available_voucher_success));

                    AllVouchersAdapter allVouchersAdapter = new AllVouchersAdapter(context, response.body().getData(), AllEAndGVouchersActivity.this);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                    gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    binding.rvVouchers.setAdapter(allVouchersAdapter);
                    binding.rvVouchers.setLayoutManager(gridLayoutManager);
                    binding.rvVouchers.setItemViewCacheSize(30);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_available_voucher_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AllVouchersResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_available_voucher_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshAuthToken() {
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
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    getAllVouchers();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                    /*Toast.makeText(context, context.getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    context.startActivity(intent);
                    finishAffinity();*/
                    Master.INSTANCE.logOut(context);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                /*Toast.makeText(context, context.getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    context.startActivity(intent);
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    @Override
    public void onItemClickNewVouchers(EandBAvailableVoucher eandBAvailableVoucher) {
        if (tokens >= eandBAvailableVoucher.getTokens()) {
            showCustomPopUp(eandBAvailableVoucher);
        } else {
            CustomYesNoDialog customYesNoDialog = new CustomYesNoDialog(context, R.style.Theme_Dialog);
            customYesNoDialog.show();
            customYesNoDialog.setCancelable(false);
            customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
            customYesNoDialog.binding.llActionRequired.setVisibility(View.GONE);
            customYesNoDialog.binding.txtInfoPopUpDesc.setText("Seems like you don't have enough stamps, please earn them by completing the activities");
            customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
            customYesNoDialog.binding.btnYes.setText("OK");
            customYesNoDialog.binding.btnYes.setBackground(ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp));
            customYesNoDialog.binding.btnYes.setOnClickListener(view -> customYesNoDialog.dismiss());
        }
    }

    private void showCustomPopUp(EandBAvailableVoucher eandBAvailableVoucher) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        CustomPopupRewardsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_rewards, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        Glide.with(context)
                .load(eandBAvailableVoucher.getVendorLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivLogo);
        binding.rlLogo.setBackgroundResource(0);
        binding.tvTitle.setText(eandBAvailableVoucher.getVoucherTitle());
        binding.tvDescription.setText(eandBAvailableVoucher.getVoucherDescription());
        binding.btnPositive.setText("Buy");
        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        binding.btnPositive.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            burnUserStamps(eandBAvailableVoucher);
        });

        Rect displayRectangle = new Rect();
        Window window = ((RewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.6f), (int) (displayRectangle.height() * 0.55f));
    }

    private void burnUserStamps(EandBAvailableVoucher eandBAvailableVoucher) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        BurnTokenRequest request = new BurnTokenRequest(eandBAvailableVoucher.getTokens(), eandBAvailableVoucher.getVoucherId());
        Call<CommonSuccessResponse> call = apiInterfaceWyh.burnStamps(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_tokens_success));
                    if (response.body().isSuccess()) {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        getAllVouchers();
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_tokens_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_tokens_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }
}