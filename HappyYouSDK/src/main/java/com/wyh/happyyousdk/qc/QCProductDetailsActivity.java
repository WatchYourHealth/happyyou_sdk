package com.wyh.happyyousdk.qc;

import static com.wyh.happyyousdk.rewards.fragment.LevelFragment.equivalentAmount;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.QCCategoryID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.databinding.ActivityQcproductDetailsBinding;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.request.qc.QCProductDetailsRequest;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.qc.productDescription.ProductDetailsResponse;
import com.wyh.happyyousdk.model.response.qc.productDescription.QCProductDetailsResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.fragment.LevelFragment;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QCProductDetailsActivity extends AppCompatActivity {

    ActivityQcproductDetailsBinding binding;
    Context context;

    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    int totalPoints = 0;
    String productSKU;
    ProductDetailsResponse productDetailsResponse;
    List<String> amountsList = new ArrayList<>();
    int orderAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qcproduct_details);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeBack.tvBack.setText("Vouchers");
        binding.includeBack.llBack.setOnClickListener(view -> finish());

        totalPoints = getIntent().getIntExtra("points", 0);
        productSKU = getIntent().getStringExtra("sku");

        int level = SharedPref.getCurrentLevel();
        double total = (int) (Integer.parseInt(SharedPref.getTotalpoints()) * 0.2);
        if (level >= 6) {
            binding.includeBack.tvPoints.setVisibility(View.VISIBLE);
            binding.includeBack.tvPoints.setText("Rs. " + String.format("%.2f", total));
            if (LevelFragment.equivalentAmount == 0)
                binding.includeBack.tvPoints.setText("Rs. " + String.format("%.2f", total));
            if (equivalentAmount == 0)
                equivalentAmount = total;
        }else{
            binding.includeBack.tvPoints.setVisibility(View.GONE);
        }

        binding.tvPositive.setOnClickListener(view -> {
            if (TextUtils.isEmpty(binding.tvQuantity.getText()) || Integer.parseInt(binding.tvQuantity.getText().toString()) < 1) {
                binding.tvQuantity.setText("1");
            } else {
                binding.tvQuantity.setText(String.valueOf(Integer.parseInt(binding.tvQuantity.getText().toString()) + 1));
            }
        });

        binding.tvNegative.setOnClickListener(view -> {
            if (TextUtils.isEmpty(binding.tvQuantity.getText())) {
                return;
            } else if (Integer.parseInt(binding.tvQuantity.getText().toString()) == 1) {
                return;
            } else {
                binding.tvQuantity.setText(String.valueOf(Integer.parseInt(binding.tvQuantity.getText().toString()) - 1));
            }
        });

        binding.btnAddToCart.setOnClickListener(view -> {
            int quantity = Integer.parseInt(binding.tvQuantity.getText().toString());
            /*if (equivalentAmount < (quantity * orderAmount)) {
                Toast.makeText(context, "You don't have enough amount to redeem", Toast.LENGTH_SHORT).show();
                return;
            }*/
            Intent intent = new Intent(context, QCCartActivity.class);
            intent.putExtra("amount", equivalentAmount);
            intent.putExtra("points", totalPoints);
            intent.putExtra("product", new Gson().toJson(productDetailsResponse));
            intent.putExtra("quantity", binding.tvQuantity.getText().toString());
            intent.putExtra("orderAmount", orderAmount);
            startActivityForResult(intent, 1212);
        });

        getProductDetails();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212 && resultCode == RESULT_OK) {
            boolean isSuccess = data.getBooleanExtra("isSuccess", false);
            if (isSuccess) {
                Intent intent = new Intent();
                intent.putExtra("isSuccess", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private void getProductDetails() {
        try{
            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
            QCProductDetailsRequest request = new QCProductDetailsRequest(QCCategoryID, productSKU);
            APIInterface apiInterface = RetrofitHandler.apiInterface();
            apiInterface.getProductDetails(SharedPref.getAuthToken(), request).enqueue(new Callback<QCProductDetailsResponse>() {
                @Override
                public void onResponse(Call<QCProductDetailsResponse> call, Response<QCProductDetailsResponse> response) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.code() == 401) {
                        refreshAuthToken();
                    }
                    if (response.code() == 200 && response.body() != null) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_get_product_details_success));

                        productDetailsResponse = response.body().getData().getProductDetailsResponse();
                        if (productDetailsResponse != null) {
                            Glide.with(context)
                                    .load(productDetailsResponse.getImages().getMobile())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.ivBrandImage);
                            binding.tvTitle.setText(productDetailsResponse.getName());
                            binding.tvDescription.setText(productDetailsResponse.getDescription());

                            if (productDetailsResponse.getPrice().getDenominations().size() > 0) {
                                orderAmount = Integer.parseInt(productDetailsResponse.getPrice().getDenominations().get(0));
                                ArrayAdapter<String> spinnerAmountAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, productDetailsResponse.getPrice().getDenominations());
                                spinnerAmountAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                                binding.spinnerAmount.setPrompt("Select Amount");
                                binding.spinnerAmount.setAdapter(spinnerAmountAdapter);
                            } else {
                                int minimumAmount = Integer.parseInt(productDetailsResponse.getPrice().getMin());
                                int maximumAmount = Integer.parseInt(productDetailsResponse.getPrice().getMax());
                                int loopEnd = maximumAmount / 500;
                                amountsList.add(String.valueOf(minimumAmount));
                                int newAmount = minimumAmount;
                                for (int i = 0; i < loopEnd; i++) {
                                    if (i == loopEnd - 1) {
                                        amountsList.add(String.valueOf(maximumAmount));
                                    } else {
                                        newAmount = newAmount + 500;
                                        amountsList.add(String.valueOf(newAmount));
                                    }
                                }
                                ArrayAdapter<String> spinnerAmountAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, amountsList);
                                spinnerAmountAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                                binding.spinnerAmount.setPrompt("Select Amount");
                                binding.spinnerAmount.setAdapter(spinnerAmountAdapter);
                            }
                            binding.spinnerAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                    if (productDetailsResponse.getPrice().getDenominations().size() > 0)
                                        orderAmount = Integer.parseInt(productDetailsResponse.getPrice().getDenominations().get(position));
                                    else
                                        orderAmount = Integer.parseInt(amountsList.get(position));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            binding.tvTnC.setText(Html.fromHtml(productDetailsResponse.getTnc().getContent()));
                        }
                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_get_product_details_failed));
                        Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<QCProductDetailsResponse> call, Throwable t) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_get_product_details_failed));
                    Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

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
                    getProductDetails();
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
}