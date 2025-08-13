package com.wyh.happyyousdk.policyDetails;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityPolicyDetailsBinding;
import com.wyh.happyyousdk.model.PolicyDetailsRequest;
import com.wyh.happyyousdk.model.request.policy.GetPolicyRequest;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.policyDetails.adapter.PolicyDetailsAdapter;
import com.wyh.happyyousdk.model.PolicyDetailsResponse;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyDetailsActivity extends AppCompatActivity {
    ActivityPolicyDetailsBinding binding;
    Context context;
    PolicyDetailsResponse policyDetails;
    int positionPolicy = 0;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_policy_details);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);


        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.la404Bear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_404.json");

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Kotak Policy");



        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            policyDetails = (PolicyDetailsResponse) extras.getSerializable("policyDetails");
            // do something with the customer
        }*/

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.btnSearchPolicy.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchPolicyActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void setRvForPolicyDetails(List<PolicyDetailsResponse.Datum> policyDetailsList) {
        PolicyDetailsAdapter policyDetailsAdapter = new PolicyDetailsAdapter(context, policyDetailsList);
        LinearLayoutManager policyDetailsLinearLayoutManager = new LinearLayoutManager(context);
        policyDetailsLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        binding.rvPolicies.setLayoutManager(policyDetailsLinearLayoutManager);
        binding.rvPolicies.setAdapter(policyDetailsAdapter);


        LinearSnapHelper policyDetailsLinearSnapHelper = new SnapHelperOneByOne();
        binding.rvPolicies.setOnFlingListener(null);
        policyDetailsLinearSnapHelper.attachToRecyclerView(binding.rvPolicies);

        LinearLayoutManager policyDetailsLinearLayoutManager1 = new LinearLayoutManager(context);
        policyDetailsLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter policyDetailsIndicatorsAdapter = new IndicatorsAdapter(context, policyDetailsList.size(), 0);
        binding.rvIndicatorsPolicies.setAdapter(policyDetailsIndicatorsAdapter);
        binding.rvIndicatorsPolicies.setLayoutManager(policyDetailsLinearLayoutManager1);
        binding.rvIndicatorsPolicies.setHasFixedSize(true);

        binding.rvPolicies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (policyDetailsLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionPolicy = policyDetailsLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionPolicy = policyDetailsLinearLayoutManager.findFirstVisibleItemPosition();
                    policyDetailsIndicatorsAdapter.updateSelectedIndex(positionPolicy);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPolicy();
    }

    private void getPolicy() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetPolicyRequest request = new GetPolicyRequest(SharedPref.getMobileNo());
        Call<PolicyDetailsResponse> call = apiInterfaceWyh.getPolicy(SharedPref.getAuthToken(), request);
        Log.d("policy req", new Gson().toJson(request));
        call.enqueue(new Callback<PolicyDetailsResponse>() {
            @Override
            public void onResponse(Call<PolicyDetailsResponse> call, Response<PolicyDetailsResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Log.d("policy res", new Gson().toJson(response.body()));
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_policy_success));
                    policyDetails = response.body();
                    if (policyDetails.getData().isEmpty()) {
                        binding.llNoPolicyFound.setVisibility(View.VISIBLE);
                        binding.includeToolbar.tvBack.setTextColor(getColor(R.color.black));
                        binding.includeToolbar.ivBack.setColorFilter(getColor(R.color.black));
                    } else if (policyDetails.getData().size() > 0) {
                        binding.includeToolbar.tvBack.setTextColor(getColor(R.color.white));
                        binding.includeToolbar.ivBack.setColorFilter(getColor(R.color.white));
                        binding.llNoPolicyFound.setVisibility(View.GONE);
                        setRvForPolicyDetails(policyDetails.getData());
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_policy_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PolicyDetailsResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
}