package com.wyh.happyyousdk.qc;

import static com.wyh.happyyousdk.rewards.fragment.LevelFragment.equivalentAmount;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.hideKeyboard;
import static com.wyh.happyyousdk.utils.CommonUtils.isValidEmail;
import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;
import static com.wyh.happyyousdk.utils.CommonUtils.todayDateInFormat;
import static com.wyh.happyyousdk.utils.CommonUtils.validateLetters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.WebActivity;

import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityQccartBinding;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Master;

import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.request.qc.placeOrder.ProductDetails;
import com.wyh.happyyousdk.model.request.qc.placeOrder.QCOrder;
import com.wyh.happyyousdk.model.request.qc.placeOrder.QCOrderAddress;
import com.wyh.happyyousdk.model.request.qc.placeOrder.QCOrderBilling;
import com.wyh.happyyousdk.model.request.qc.placeOrder.QCOrderPayment;
import com.wyh.happyyousdk.model.request.qc.placeOrder.QCOrderProduct;
import com.wyh.happyyousdk.model.request.qc.placeOrder.QCPlaceOrderRequest;
import com.wyh.happyyousdk.model.request.rewards.GetRedeemableAmountRequest;
import com.wyh.happyyousdk.model.request.rewards.RedeemRewardsRequest;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.qc.productDescription.ProductDetailsResponse;
import com.wyh.happyyousdk.model.response.rewards.RedeemableAmountResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.fragment.LevelFragment;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QCCartActivity extends AppCompatActivity {

    ActivityQccartBinding binding;
    Context context;

    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    int totalPoints = 0, orderAmount = 0, quantity = 0, redeemableAmount = 0;
    double totalAmount = 0, payableAmount = 0;
    ProductDetailsResponse productDetailsResponse;

    String fName, lName, email, productName;
    /*ActivityResultLauncher<Intent> paymentactivitylauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getData() != null) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data.getBooleanExtra("paySuccess", false)) {

                        }

                    }
                }
            });*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qccart);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeBack.tvBack.setText("Cart");
        binding.includeBack.llBack.setOnClickListener(view -> {
            hideKeyboard(QCCartActivity.this);
            finish();
        });

        totalPoints = getIntent().getIntExtra("points", 0);
        orderAmount = getIntent().getIntExtra("orderAmount", 0);
        quantity = Integer.parseInt(getIntent().getStringExtra("quantity"));
        productDetailsResponse = new Gson().fromJson(getIntent().getStringExtra("product"), ProductDetailsResponse.class);

        totalAmount = quantity * orderAmount;
        int level = SharedPref.getCurrentLevel();
        if (level >= 6) {
            getRedeemableAmount();
            binding.includeBack.tvPoints.setVisibility(View.VISIBLE);
            binding.includeBack.tvPoints.setText("Rs. " + String.format("%.2f", equivalentAmount));
            /*if (LevelFragment.equivalentAmount == 0)
                binding.includeBack.tvPoints.setText("Rs. " + String.format("%.2f", DashboardActivity.equivalentAmount));*/
        }else{
            binding.includeBack.tvPoints.setVisibility(View.GONE);
            binding.checkBox.setVisibility(View.GONE);
        }
        binding.tvProductName.setText(productDetailsResponse.getName());
        binding.tvQuantity.setText("" + quantity);
        binding.tvOrderAmount.setText("" + orderAmount);


        binding.tvTotal.setText("Total: " + String.format("%.2f", totalAmount) + " Rs");



        if (SharedPref.getUserName().trim().contains(" ")) {
            binding.edtName.setText(SharedPref.getUserName().split(" ")[0]);
            binding.edtLName.setText(SharedPref.getUserName().split(" ")[1]);
        } else
            binding.edtName.setText(SharedPref.getUserName().split(" ")[0]);

        if (!SharedPref.getEmail().isEmpty())
            binding.edtEmail.setText(SharedPref.getEmail());

        binding.btnRedeem.setOnClickListener(view -> {
            hideKeyboard(QCCartActivity.this);
            if (TextUtils.isEmpty(binding.edtName.getText()) || !validateLetters(binding.edtName.getText().toString())) {
                binding.edtName.requestFocus();
                binding.edtName.setError("Please enter valid first name");
                showKeyboard(QCCartActivity.this);
                return;
            }
            if (TextUtils.isEmpty(binding.edtLName.getText()) || !validateLetters(binding.edtLName.getText().toString())) {
                binding.edtLName.requestFocus();
                binding.edtLName.setError("Please enter valid last name");
                showKeyboard(QCCartActivity.this);
                return;
            }
            if (TextUtils.isEmpty(binding.edtEmail.getText()) || !isValidEmail(binding.edtEmail.getText().toString())) {
                binding.edtEmail.requestFocus();
                binding.edtEmail.setError("Please enter valid email");
                showKeyboard(QCCartActivity.this);
                return;
            }
            fName = binding.edtName.getText().toString();
            lName = binding.edtLName.getText().toString();
            email = binding.edtEmail.getText().toString();
            validateBalanceAmount(fName, lName, email);
        });

    }

    private void getRedeemableAmount() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetRedeemableAmountRequest request = new GetRedeemableAmountRequest(SharedPref.getAesUuid(), (int) totalAmount);
        Call<RedeemableAmountResponse> call = apiInterfaceWyh.getRedeemableAmount(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<RedeemableAmountResponse>() {
            @Override
            public void onResponse(Call<RedeemableAmountResponse> call, Response<RedeemableAmountResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("AuthToken",new Gson().toJson(response.body()));
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getRedeemableAmount() > 0) {
                        redeemableAmount = response.body().getRedeemableAmount();
                        binding.checkBox.setVisibility(View.VISIBLE);
                        binding.checkBox.setText("Redeemable amount (" + redeemableAmount + " Rs)");
                    }
                }
            }

            @Override
            public void onFailure(Call<RedeemableAmountResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(QCCartActivity.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateBalanceAmount(String name, String lName, String email) {
        if (binding.checkBox.isChecked() && binding.checkBox.getVisibility() == View.VISIBLE) {
            if (totalAmount <= redeemableAmount) {
                //Redeemable amount is greater than cart amount then directly call place order and update balance
                placeOrder(name, lName, email);
            } else {
                //Redeemable amount is lesser than cart amount calculate payable amount and pass to the web view
                payableAmount = totalAmount - redeemableAmount;
                openWebView(CommonUtils.getBaseUrlForQCP(context), name, lName, email);
            }
        } else {
            //User don't want to redeem balance so forward the total amount as payable amount to web view
            payableAmount = totalAmount;
            openWebView(CommonUtils.getBaseUrlForQCP(context), name, lName, email);
        }
    }

    private void openWebView(String url, String name, String lName, String email) {
        Intent i = new Intent(context, WebActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("Url", url);
        i.putExtra("comingFrom","");
        i.putExtra("payableAmount", String.valueOf(payableAmount));
        i.putExtra("productName", productDetailsResponse.getName());
        i.putExtra("fullName", name + " " + lName);
        i.putExtra("email", email);
        startActivity(i);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(NewDashboardHelper.Companion.isPaymentSuccess()){
            placeOrder(fName, lName, email);
        }

    }

    private void placeOrder(String name, String lName, String email) {
        try{
            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
            APIInterface apiInterface = RetrofitHandler.apiInterface();
            apiInterface.placeQcOrder(SharedPref.getAuthToken(), createQCPlaceOrderRequest(name, lName, email)).enqueue(new Callback<CommonSuccessResponse>() {
                @Override
                public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                    if (response.code() == 401) {
                        refreshAuthToken(name, lName, email);
                    }
                    if (response.code() == 200 && response.body() != null) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_place_order_success));

                        if (response.body().isSuccess() && response.body().getMsg().contains("added")) {
                            updateUserRewardBalance();
                        } else {
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    } else {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_place_order_failed));
                        Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_place_order_failed));
                    Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateUserRewardBalance() {
        RedeemRewardsRequest request = new RedeemRewardsRequest(SharedPref.getAesUuid(), todayDateInFormat("yyyy-MM-dd"),
                (int) redeemableAmount, "QC voucher purchased");
        Call<CommonSuccessResponse> call = apiInterfaceWyh.updateUserRewards(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_redeem_rewards_success));
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(QCCartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("isSuccess", true);
                    setResult(RESULT_OK, intent);
                    NewDashboardHelper.Companion.setPaymentSuccess(false);
                    finish();
                } else {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_redeem_rewards_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_redeem_rewards_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshAuthToken(String name, String lName, String email) {
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
                    placeOrder(name, lName, email);
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

    private QCPlaceOrderRequest createQCPlaceOrderRequest(String name, String lName, String email) {
        String mobile;
        try {
//            mobile = SharedPref.getMobileNo();
            mobile =SharedPref.getDecryptMobileNo();
            //mobile = CryptoHelper.decrypt(this,SharedPref.getMobileNo());
        } catch (Exception e) {
            e.printStackTrace();
            mobile =SharedPref.getDecryptMobileNo();
//            mobile = SharedPref.getMobileNo();
        }
        mobile = "+91" + mobile;
        QCOrderAddress address = new QCOrderAddress(name, lName, email, mobile, "test Add Line 1", "test Add Line 1", "Thane",
                "Maharashtra", "IN", "400604", true);
        QCOrderBilling billing = new QCOrderBilling(name, lName, email, mobile, "test Add Line 1", "test Add Line 1", "Thane",
                "Maharashtra", "IN", "400604");
        List<QCOrderPayment> qcOrderPaymentList = new ArrayList<>();
        QCOrderPayment qcOrderPayment = new QCOrderPayment((int) totalAmount, productDetailsResponse.getPayout().getPaymentMethods().get(0));
        qcOrderPaymentList.add(qcOrderPayment);
        List<QCOrderProduct> qcOrderProductList = new ArrayList<>();
        QCOrderProduct qcOrderProduct = new QCOrderProduct(productDetailsResponse.getSku(), orderAmount, quantity, "356", "", "");
        qcOrderProductList.add(qcOrderProduct);

        boolean syncOnly = quantity <= 10;

        QCOrder qcOrder = new QCOrder(address, billing, qcOrderPaymentList, UUID.randomUUID().toString(), qcOrderProductList, syncOnly, null, "API");
        ProductDetails productDetails = new ProductDetails(productDetailsResponse.getName(), quantity, orderAmount, (int) totalAmount);

        QCPlaceOrderRequest request = new QCPlaceOrderRequest(qcOrder, productDetails);

        Log.d("RefNo", UUID.randomUUID().toString());

        return request;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyboard(QCCartActivity.this);
    }
}