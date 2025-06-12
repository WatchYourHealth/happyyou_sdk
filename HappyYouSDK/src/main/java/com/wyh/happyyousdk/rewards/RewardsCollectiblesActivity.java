package com.wyh.happyyousdk.rewards;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.DASHBOARD_BANNER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityRewardsCollectiblesBinding;
import com.wyh.happyyousdk.databinding.PopUpScratchCardScratchableBinding;
import com.wyh.happyyousdk.happyMarket.HappyMartCategory;
import com.wyh.happyyousdk.happyMarket.HappyMartDisclaimerActivity;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.request.HappyMartCategoryRequest;
import com.wyh.happyyousdk.model.request.VoucherIdRequest;
import com.wyh.happyyousdk.model.request.rewards.GetRewardsDashboardRequest;
import com.wyh.happyyousdk.model.response.HappyMartCategoryResponse;
import com.wyh.happyyousdk.model.response.rewards.LevelDashboardResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.adapter.CollectiblesAdapter;
import com.wyh.happyyousdk.model.response.rewards.AllCollectiblesResponse;
import com.wyh.happyyousdk.model.response.rewards.RewardsCollectible;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardsCollectiblesActivity extends AppCompatActivity implements CollectiblesAdapter.ClickListenerInterface, ScratchListener {

    ActivityRewardsCollectiblesBinding binding;
    Context context;
    ApiInterfaceWyh apiInterfaceWyh;
    ProgressDialog progressDialog;
    List<FreeVoucher> freeVoucherList = new ArrayList<>();
    List<RewardsCollectible> allCollectibles = new ArrayList<>();
    List<RewardsCollectible> activityCollectibles = new ArrayList<>();
    List<RewardsCollectible> earnAndGrabCollectibles = new ArrayList<>();

    CollectiblesAdapter collectiblesAdapter;
    public VoucherIdRequest voucherIdRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rewards_collectibles);
        context = this;

        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeToolbar.tvBack.setText("Collectible");
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "AllCollectibles");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        String[] collectibleTypes = {"All", "Level", "Earn & Grab"};
        ArrayAdapter<String> collectibleTypesAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, collectibleTypes);
        collectibleTypesAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerCollectibleType.setAdapter(collectibleTypesAdapter);

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.spinnerCollectibleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        collectiblesAdapter = new CollectiblesAdapter(context, allCollectibles, RewardsCollectiblesActivity.this);
                        binding.rvCollectibles.setAdapter(collectiblesAdapter);
                        break;
                    case 1:
                        collectiblesAdapter = new CollectiblesAdapter(context, activityCollectibles, RewardsCollectiblesActivity.this);
                        binding.rvCollectibles.setAdapter(collectiblesAdapter);
                        break;
                    case 2:
                        collectiblesAdapter = new CollectiblesAdapter(context, earnAndGrabCollectibles, RewardsCollectiblesActivity.this);
                        binding.rvCollectibles.setAdapter(collectiblesAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getRewardsDashboardData();

    }
    public void getRewardsDashboardData() {
        try{
            CommonUtils.showProgressDialige(this);
            GetRewardsDashboardRequest request = new GetRewardsDashboardRequest("");
            Call<LevelDashboardResponse> call = apiInterfaceWyh.getRewardsDashboardData(SharedPref.getAuthToken(), request);
            call.enqueue(new Callback<LevelDashboardResponse>() {
                @Override
                public void onResponse(Call<LevelDashboardResponse> call, Response<LevelDashboardResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.code() == 200 && response.body() != null) {
                        Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.get_rewards_dash_board_data_success));
                        if (response.body().getData() != null) {
                            getAllUserCollectibles();
                            if (response.body().getData().getFreebieVoucher() != null){
                                freeVoucherList = response.body().getData().getFreebieVoucher();
                                if (freeVoucherList!=null && freeVoucherList.size()>0){
                                    for(FreeVoucher data : freeVoucherList){
                                        RewardsCollectible rewrd = new RewardsCollectible();
                                        rewrd.setVendorLogo(data.getVendorLogo());
                                        rewrd.setVendorName(data.getVendorName());
                                        rewrd.setVoucherDescription(data.getVoucherDescription());
                                        rewrd.setVoucherTitle(data.getVoucherTitle());
                                        rewrd.setIsscrached(data.isScratched());
                                        rewrd.setId(data.getFreebieID());
                                        if(data.getVoucherCode() != null){
                                            rewrd.setVoucherCode(data.getVoucherCode());
                                        }
                                        if(data.getVoucherValue() != null){
                                            rewrd.setVoucherValue(Integer.valueOf(data.getVoucherValue()));
                                        }
                                        allCollectibles.add(rewrd);
                                        activityCollectibles.add(rewrd);
                                    }
                                }

                            }
                        }
                    } else {
                        getAllUserCollectibles();
                        Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.get_rewards_dash_board_data_failed));
                        Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LevelDashboardResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();

                    getAllUserCollectibles();
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.get_rewards_dash_board_data_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            CommonUtils.dismissDialoge();
            e.printStackTrace();
        }

    }
    private void getAllUserCollectibles() {
        /*if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();*/
        Call<AllCollectiblesResponse> call = apiInterfaceWyh.getAllCollectibles(SharedPref.getAuthToken());
        call.enqueue(new Callback<AllCollectiblesResponse>() {
            @Override
            public void onResponse(Call<AllCollectiblesResponse> call, Response<AllCollectiblesResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_user_collectibles_success));

                    if (response.body().getData() != null) {

                        if (response.body().getData().getEnGCollectible() != null && response.body().getData().getEnGCollectible().size() > 0) {
                            allCollectibles.addAll(response.body().getData().getEnGCollectible());
                            earnAndGrabCollectibles = response.body().getData().getEnGCollectible();
                        }

                        collectiblesAdapter = new CollectiblesAdapter(context, allCollectibles, RewardsCollectiblesActivity.this);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        binding.rvCollectibles.setAdapter(collectiblesAdapter);
                        binding.rvCollectibles.setLayoutManager(gridLayoutManager);
                        binding.rvCollectibles.setItemViewCacheSize(30);

                    }

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_user_collectibles_failed));
                }
            }

            @Override
            public void onFailure(Call<AllCollectiblesResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_user_collectibles_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClickScratchCard(RewardsCollectible rewardsCollectible, int bgDrawable) {
        showScratchCard(rewardsCollectible, bgDrawable);
    }


    private void showScratchCard(RewardsCollectible rewardsCollectible, int drawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        PopUpScratchCardScratchableBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pop_up_scratch_card_scratchable, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);



        if(rewardsCollectible.getVoucherCode() != null){
            binding.llCopy.setVisibility(View.VISIBLE);
            binding.btnRedeem.setVisibility(View.GONE);
            binding.llScratchview.setVisibility(View.GONE);
            binding.tvAmount.setVisibility(View.GONE);
        }else{
            binding.llCopy.setVisibility(View.GONE);
            binding.btnRedeem.setVisibility(View.VISIBLE);
            binding.llScratchview.setVisibility(View.VISIBLE);
            binding.tvAmount.setVisibility(View.VISIBLE);
        }

      //binding.llScratchview.setVisibility(View.VISIBLE);
        binding.scratchView.setScratchListener(this);
        if (rewardsCollectible.isIsscrached()){
            binding.scratchView.onFullReveal();
        }else {
            voucherIdRequest = new VoucherIdRequest(rewardsCollectible.getId());
        }

        binding.tvTitle.setText(rewardsCollectible.getVendorName());
        binding.tvValue.setText(rewardsCollectible.getVoucherValue() + " off");
        binding.tvDescription.setText(rewardsCollectible.getVoucherDescription());
        binding.tvCouponCode.setText(rewardsCollectible.getVoucherCode());
        binding.tvAmount.setText("₹"+rewardsCollectible.getVoucherValue());

        if (!alertDialog.isShowing())
            alertDialog.show();


        binding.btnRedeem.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            if (rewardsCollectible.getVendorName().toLowerCase().contains("hobby tribe")) {
                intent.putExtra("came_from", Constants.HappyMartHobby);
            } else if (rewardsCollectible.getVendorName().toLowerCase().contains("mind")) {
                intent.putExtra("came_from", Constants.HappyMentalWellbeing);
            } else if (rewardsCollectible.getVendorName().toLowerCase().contains("pharmeasy")) {
                intent.putExtra("came_from", Constants.HappyMartPharmacy);
            } else if (rewardsCollectible.getVendorName().toLowerCase().contains("medpay")) {
                intent.putExtra("came_from", Constants.HappyMartOPD);
            } else if (rewardsCollectible.getVendorName().toLowerCase().contains("coach")) {
                intent.putExtra("came_from", Constants.HappyMartFitness);
            } else if (rewardsCollectible.getVendorName().toLowerCase().contains("acto")) {
                intent.putExtra("came_from", Constants.HappyMartDevice);
            } else if (rewardsCollectible.getVendorName().toLowerCase().contains("connect") || rewardsCollectible.getVendorName().toLowerCase().contains("health")) {
                intent.putExtra("came_from", Constants.HappyMartDiagnostics);
            } else {
                intent.putExtra("came_from", Constants.HappyMartOther);
            }
            intent.putExtra("toolbarname", rewardsCollectible.getVendorName());
            startActivity(intent);
        });
        binding.btnNegative.setOnClickListener(view -> {
            alertDialog.dismiss();
        });


        binding.tvCopy.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", rewardsCollectible.getVoucherCode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
        });

        Glide.with(context)
                .load(rewardsCollectible.getVendorLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivVendorLogo);

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }


    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i >= 20) {
            scratchCardLayout.onFullReveal();
            if (voucherIdRequest!=null){
                updateScratchStatus();
            }



        }
    }
    private void updateScratchStatus() {
        Call<CommonSuccessResponse> call = apiInterfaceWyh.updateScratchStatus(SharedPref.getAuthToken(), voucherIdRequest);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_success));
                      voucherIdRequest= null;
                      allCollectibles.get(0).setIsscrached(true);
                      activityCollectibles.get(0).setIsscrached(true);
                      collectiblesAdapter.notifyItemChanged(0);

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_failed));
            }
        });
    }
    @Override
    public void onScratchStarted() {

    }

}