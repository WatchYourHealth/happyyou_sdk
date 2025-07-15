package com.wyh.happyyousdk.happyMarket;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;


import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityMoreMyParchasesBinding;
import com.wyh.happyyousdk.happyMarket.adapter.MoreMyPurchasesAdapter;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.MyOrderResponse;

import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreMyPurchasesActivity extends AppCompatActivity implements  MoreMyPurchasesAdapter.ClickListenerInterface{
    ActivityMoreMyParchasesBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    MoreMyPurchasesAdapter adapter;
    Context context;
    List<MyOrderResponse.Data> myOrderDataList;
    List<String> myPurchasesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_more_my_parchases);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("My Purchases");

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        getMyOrder();
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
                    getMyOrder();
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

    private void setActivitiesAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        MoreMyPurchasesAdapter myPurchasesAdapter = new MoreMyPurchasesAdapter(context, myPurchasesList, this);
        binding.rvMyPurchases.setLayoutManager(gridLayoutManager);
        binding.rvMyPurchases.setAdapter(myPurchasesAdapter);
        binding.rvMyPurchases.setItemViewCacheSize(30);

    }

    private void getMyOrder() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<MyOrderResponse> call = apiInterfaceWyh.getAllOrders(SharedPref.getAuthToken());
        call.enqueue(new Callback<MyOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyOrderResponse> call, @NonNull Response<MyOrderResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if(response.code() == 401){
                    refreshAuthToken();
                }
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_all_orders_success));
                    myOrderDataList = response.body().getData();
                    myPurchasesList = new ArrayList<>();
                    if(myOrderDataList.size()>0){
                        for(MyOrderResponse.Data data: myOrderDataList){
                            if(!(myPurchasesList.contains(data.getVendor()))){
                                myPurchasesList.add(data.getVendor());
                            }
                        }
                        setActivitiesAdapter();
                    }

                    /*if(response.body().getFeedbackDetails() != null && response.body().getFeedbackDetails().getFeedbackModel() != null && response.body().getFeedbackDetails().getStarConfig() != null ){
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedback(MoreMyPurchasesActivity.this, response.body().getFeedbackDetails());
                    }*/
                }else{
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_all_orders_failed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyOrderResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_all_orders_failed));
//                Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(String vendor) {
        Intent intent = new Intent(context, MyOrderActivity.class);
        intent.putExtra("vendor", vendor);
        startActivity(intent);
    }
}