package com.wyh.happyyousdk.absorb;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.absorb.adapter.HealthTvAdapter;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityRecommandHealthTvDashboradBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.absorb.GetQuickReadRequest;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.model.response.absorb.GetQuickReadResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommandHealthTvDashboradActivity extends AppCompatActivity implements ScratchListener {

    ActivityRecommandHealthTvDashboradBinding binding;
    AlertDialog alertDialogBonusRewards;
    Context context;
    int communityId;
    boolean isPositiveBtn = false;


    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recommand_health_tv_dashborad);
        context = this;

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Recommended Health TV Video");

        communityId = getIntent().getIntExtra("communityId", 0);

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


        getQuickReadDashboardAPI();

    }


    public void getQuickReadDashboardAPI() {
        GetQuickReadRequest getQuickReadRequest = new GetQuickReadRequest("Quick reads", "", "", communityId);
        Call<GetQuickReadResponse> call = apiInterfaceWyh.getQuickReadDashboard(SharedPref.getAuthToken(), getQuickReadRequest);
        Log.v("Url_Request_save", call.request().url() + "\n" + new Gson().toJson(getQuickReadRequest) + "\n" + SharedPref.getAuthToken());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetQuickReadResponse> call, @NonNull Response<GetQuickReadResponse> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_all_items_success));
                    List<GetDashboardDataResponse.Data.HealthTv> healthTvList = response.body().getData().getTribeVideos();
                    if (!healthTvList.isEmpty()) {
                        binding.rvHealthTvRecommend.setVisibility(View.VISIBLE);
                        /*List<GetDashboardDataResponse.Data.HealthTv> dataList = new ArrayList<>();
                        for(GetQuickReadResponse.Data.TribeVideos item : tribeVideosList){
                            GetDashboardDataResponse.Data.HealthTv element = new GetDashboardDataResponse.Data.HealthTv();
                            element.setDescription(item.getDescription());
                            element.setTitle(item.getVideoName());
                            element.setTitle(item.getVideoName());
                            element.setTitle(item.getVideoName());
                            dataList.add()
                        }*/
                        List<GetDashboardDataResponse.Data.HealthTv> isBookmark = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.HealthTv> nonBookMark = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.HealthTv> temp = new ArrayList<>();
                        for (GetDashboardDataResponse.Data.HealthTv item : healthTvList) {
                            if (item.getIsBookMarked() == 1) {
                                isBookmark.add(item);
                            } else {
                                nonBookMark.add(item);
                            }
                        }
                        temp.addAll(isBookmark);
                        temp.addAll(nonBookMark);
                        healthTvList.clear();
                        healthTvList = temp;
                        setRvHealthTVData(healthTvList);
                    } else {
                        binding.rvHealthTvRecommend.setVisibility(View.GONE);
                    }

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_all_items_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetQuickReadResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_all_items_failed));
            }
        });
    }

    private void setRvHealthTVData(List<GetDashboardDataResponse.Data.HealthTv> healthTvList) {
        HealthTvAdapter healthTvAdapter = new HealthTvAdapter(context, healthTvList, false);
        GridLayoutManager healthTvLinearLayoutManager = new GridLayoutManager(context, 2);
        healthTvLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvHealthTvRecommend.setLayoutManager(healthTvLinearLayoutManager);
        binding.rvHealthTvRecommend.setAdapter(healthTvAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212 && HealthTVDashboard.isPlayedFull) {
            updateRewards();
        }
    }

    private void updateRewards() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("ActivityUploads", "")
                .addFormDataPart("eventName", "HealthTv")
                .addFormDataPart("eventCategory", TAG_REWARD_EVENT)
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "Rewards/EarnRewards")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                finish();
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.code() == 200 && response.body() != null) {
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    HealthTVDashboard.isPlayedFull = false;
                    if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                        runOnUiThread(() -> {
                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                            }

                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                            }

                            showRewardsPopupDialogBox();
                        });
                        finish();
                    } else
                        finish();
                }
            }
        });
    }

    private void showRewardsPopupNew(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        alertDialog.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            }else{
                showRewardsPopupDialogBox();
            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(alertDialog::dismiss, 3000);

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> alertDialog.dismiss());

        binding.scratchView.onFullReveal();


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(RecommandHealthTvDashboradActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i >= 20) {
            scratchCardLayout.onFullReveal();
            if(alertDialogBonusRewards!= null && alertDialogBonusRewards.isShowing()){
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> alertDialogBonusRewards.dismiss(), 3000);
            }
        }
    }

    @Override
    public void onScratchStarted() {

    }


    private void showRewardsPopupDialogBox() {
        if (!NewDashboardHelper.Companion.getPopUpShowModels().isEmpty()) {
            int i = 0;
            PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 1 && Objects.equals(firstData.getKey(), "Rewards")) {
                i = 1;
                firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            }
            switch (firstData.getKey()) {
                case "Rewards":
                    showRewardsPopupNew(firstData.getValue(), context);
                    break;
                case "RewardsBounce":
                    showBonusRewardsPopup(firstData.getValue(), context);
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        } else {
            finish();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showBonusRewardsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusRewards = alertBuilder.create();
        alertDialogBonusRewards.setCancelable(false);
        if (!alertDialogBonusRewards.isShowing())
            alertDialogBonusRewards.show();


        alertDialogBonusRewards.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            }else{
                showRewardsPopupDialogBox();
            }
        });
        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);

        binding.scratchView.onFullReveal();


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusRewards.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialogBonusRewards.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }
}