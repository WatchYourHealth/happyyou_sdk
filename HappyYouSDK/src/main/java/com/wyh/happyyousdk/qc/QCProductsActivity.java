package com.wyh.happyyousdk.qc;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.QCCategoryID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wyh.happyyousdk.APIEncryption.AESEncryption;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.databinding.ActivityQcproductsBinding;
import com.wyh.happyyousdk.utils.Master;

import com.wyh.happyyousdk.model.request.QCGetCategoryResponse;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.request.qc.AllQCProductsRequest;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.login.VerifyOtpResponse;
import com.wyh.happyyousdk.qc.adapter.QCProductsAdapter;
import com.wyh.happyyousdk.model.response.qc.allProducts.AllQCProductsResponse;
import com.wyh.happyyousdk.model.response.qc.allProducts.Products;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.RewardsCollectiblesActivity;
import com.wyh.happyyousdk.rewards.RewardsHistoryActivity;
import com.wyh.happyyousdk.rewards.fragment.LevelFragment;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QCProductsActivity extends AppCompatActivity implements QCProductsAdapter.ClickListenerInterface {

    ActivityQcproductsBinding binding;
    Context context;
    QCProductsAdapter qcProductsAdapter;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    List<Products> productsList;
    int totalPoints = 0;
    int cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qcproducts);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeBack.tvBack.setText("Vouchers");
        binding.includeBack.llBack.setOnClickListener(view -> finish());

        binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_404.json");

        totalPoints = getIntent().getIntExtra("points", 0);



        int level = SharedPref.getCurrentLevel();
        double total = (int) (Integer.parseInt(SharedPref.getTotalpoints()) * 0.2);
        if (level >= 6) {
            binding.includeBack.tvPoints.setVisibility(View.VISIBLE);
            binding.includeBack.tvPoints.setText("Rs. " + String.format("%.2f", total));
            if (LevelFragment.equivalentAmount == 0){
                binding.includeBack.tvPoints.setText("Rs. " + String.format("%.2f", total));
            }

        }else{
            binding.includeBack.tvPoints.setVisibility(View.GONE);
        }
        binding.includeBack.ivMenu.setVisibility(View.VISIBLE);

        binding.includeBack.ivMenu.setOnClickListener(view -> {
            showPopup(binding.includeBack.ivMenu);
        });

        //getAllQCProducts();
        getQCCategory();

        binding.edtComName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString().toLowerCase());
            }
        });

    }


    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_qc_product, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_order_list) {
                Intent intent = new Intent(context, QCOrderListActivity.class);
                startActivity(intent);
            }
            return false;
        });
    }


    private void getQCCategory(){
        try{
            CommonUtils.showProgressDialige(this);
            APIInterface apiInterface = RetrofitHandler.apiInterface();
            apiInterface.getCategory(SharedPref.getAuthToken()).enqueue(new Callback<QCGetCategoryResponse>() {
                @Override
                public void onResponse(Call<QCGetCategoryResponse> call, Response<QCGetCategoryResponse> response) {
                    CommonUtils.dismissDialoge();
                    if(response.code() == 200 && response.body().getSuccess()){
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.category_success));
                            cid = response.body().getData().getCategoryResponse().id;
                            getAllQCProducts(cid);
                    }else{
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.category_failed));
                    }
                }
                @Override
                public void onFailure(Call<QCGetCategoryResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.category_failed));
                }
            });

        }catch (Exception e){
            CommonUtils.dismissDialoge();
            e.printStackTrace();
        }
    }

    private void getAllQCProducts(int cid) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AllQCProductsRequest request = new AllQCProductsRequest(cid);
        APIInterface apiInterface = RetrofitHandler.apiInterface();
        apiInterface.getAllQCProducts(SharedPref.getAuthToken(), request).enqueue(new Callback<AllQCProductsResponse>() {
            @Override
            public void onResponse(Call<AllQCProductsResponse> call, Response<AllQCProductsResponse> response) {
                try{
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.code() == 401) {
                        refreshAuthToken();
                    }
                    if (response.code() == 200 && response.body() != null) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_get_product_list_success));

                        if (response.body().getData() != null) {
                            productsList = response.body().getData().getProductListResponse().getProducts();
                            if (response.body().getData().getProductListResponse() != null && response.body().getData().getProductListResponse().getProducts().size() > 0) {

                                if (productsList.size() == 0) {
                                    binding.llNoRecordFound.setVisibility(View.VISIBLE);
                                    binding.rvVouchers.setVisibility(View.GONE);
                                } else {
                                    binding.llNoRecordFound.setVisibility(View.GONE);
                                    binding.rvVouchers.setVisibility(View.VISIBLE);
                                    qcProductsAdapter = new QCProductsAdapter(context,
                                            response.body().getData().getProductListResponse().getProducts(), QCProductsActivity.this);
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                                    gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                    binding.rvVouchers.setAdapter(qcProductsAdapter);
                                    binding.rvVouchers.setLayoutManager(gridLayoutManager);
                                    binding.rvVouchers.setItemViewCacheSize(30);
                                }
                            }
                        }
                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_get_product_list_failed));
                    }

                }catch (Exception e){
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllQCProductsResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.qc_get_product_list_failed));
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
                    getAllQCProducts(cid);
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
    public void onItemClickActivities(Products product, int bgDrawable) {
        Intent intent = new Intent(context, QCProductDetailsActivity.class);
        intent.putExtra("points", totalPoints);
        intent.putExtra("sku", product.getSku());
        startActivityForResult(intent, 1212);
        /*int level = SharedPref.getCurrentLevel();
        if (level >= 6) {
            Intent intent = new Intent(context, QCProductDetailsActivity.class);
            intent.putExtra("points", totalPoints);
            intent.putExtra("sku", product.getSku());
            startActivityForResult(intent, 1212);
        } else {
            showPopUp();
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212 && resultCode == RESULT_OK && data != null) {
            boolean isSuccess = data.getBooleanExtra("isSuccess", false);
            if (isSuccess) {
                finish();
            }
        }
    }

    private void filter(String text) {
        try{
            List<Products> temp = new ArrayList<>();
            for (Products p : productsList) {
                if (p.getName().toLowerCase().contains(text)) {
                    temp.add(p);
                }
            }

            if (temp.size() == 0) {
                binding.llNoRecordFound.setVisibility(View.VISIBLE);
                binding.rvVouchers.setVisibility(View.GONE);
            } else {
                binding.llNoRecordFound.setVisibility(View.GONE);
                binding.rvVouchers.setVisibility(View.VISIBLE);
                qcProductsAdapter.updateList(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void showPopUp() {
        CustomYesNoDialog customYesNoDialog = new CustomYesNoDialog(context, R.style.Theme_Dialog);
        customYesNoDialog.show();
        customYesNoDialog.setCancelable(false);
        customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
        customYesNoDialog.binding.llActionRequired.setVisibility(View.GONE);
        customYesNoDialog.binding.txtInfoPopUpDesc.setText("Please reach up to level 6 to redeem your points");
        customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
        customYesNoDialog.binding.btnYes.setText("OK");
        customYesNoDialog.binding.btnYes.setBackground(ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp));
        customYesNoDialog.binding.btnYes.setOnClickListener(view1 -> customYesNoDialog.dismiss());
    }
}