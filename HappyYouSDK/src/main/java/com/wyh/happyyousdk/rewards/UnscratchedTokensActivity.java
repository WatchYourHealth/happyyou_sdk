package com.wyh.happyyousdk.rewards;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityUnscratchedTokensBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.utils.Master;

import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.adapter.MoreUnscratchedAdapter;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.response.rewards.EandBDashboardResponse;
import com.wyh.happyyousdk.model.response.rewards.UnscratchedTokensData;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnscratchedTokensActivity extends AppCompatActivity implements ScratchListener, MoreUnscratchedAdapter.ClickListenerInterface {
    ActivityUnscratchedTokensBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    int stampId = -1;
    boolean isPositiveBtn = false;

    AlertDialog alertDialogStamp, alertDialogBonusStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unscratched_tokens);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_unscratched_tokens);

        context = this;
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        SharedPreference.init(context);
        SharedPref.init(context);

        setToolBar();

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "AllEnGScratchCards");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        getEearnAndBurnDashboardData();

        binding.ivHome.setOnClickListener(view -> {
            Intent i = new Intent(this, NewDashboardActivity.class);
            startActivity(i);
            finish();
        });

    }

    private void setToolBar() {
        binding.includeToolbar.tvBack.setText(getString(R.string.grab_opportunities));
        binding.includeToolbar.ivMenu.setVisibility(View.GONE);
        binding.includeToolbar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void setUnscratchedTokensAdapter(List<UnscratchedTokensData> unlockedVoucherList) {
        MoreUnscratchedAdapter unlockedAdapter = new MoreUnscratchedAdapter(context, unlockedVoucherList, this);
        GridLayoutManager unlockedLinearLayoutManager = new GridLayoutManager(context, 3);
        unlockedLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvGrabOpportunities.setLayoutManager(unlockedLinearLayoutManager);
        binding.rvGrabOpportunities.setAdapter(unlockedAdapter);
        binding.rvGrabOpportunities.setOnFlingListener(null);
    }

    public void getEearnAndBurnDashboardData() {
        Call<EandBDashboardResponse> call = apiInterfaceWyh.getEandBDashboardData(SharedPref.getAuthToken());
        call.enqueue(new Callback<EandBDashboardResponse>() {
            @Override
            public void onResponse(Call<EandBDashboardResponse> call, Response<EandBDashboardResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_user_dashboard_success));

                    List<UnscratchedTokensData> unscratchedTokensList = response.body().getData().getUnscratchedTokens();

                    if (unscratchedTokensList.size() > 0) {
                        List<UnscratchedTokensData> sortData = new ArrayList<>();
                        List<UnscratchedTokensData> redCard = new ArrayList<>();
                        List<UnscratchedTokensData> orangeCard = new ArrayList<>();
                        for (UnscratchedTokensData data : unscratchedTokensList) {
                            if (data.getActivityId() > 0) {
                                redCard.add(data);
                            } else {
                                orangeCard.add(data);
                            }
                        }

                        sortData.addAll(redCard);
                        sortData.addAll(orangeCard);

                        binding.rvGrabOpportunities.setVisibility(View.VISIBLE);
                        binding.llNoRecordFound.setVisibility(View.GONE);
                        setUnscratchedTokensAdapter(sortData);
                    } else {
                        binding.rvGrabOpportunities.setVisibility(View.GONE);
                        binding.llNoRecordFound.setVisibility(View.VISIBLE);
                    }

                } else if (response.code() == 401) {
                    refreshAuthToken();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_user_dashboard_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EandBDashboardResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_user_dashboard_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
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
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    getEearnAndBurnDashboardData();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.refresh_token_failed));
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
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.refresh_token_failed));
                /*Toast.makeText(context, context.getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    context.startActivity(intent);
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    @Override
    public void onItemClickUnscratchedVouchers(UnscratchedTokensData data) {
        stampId = data.getActivityId();
        showScratchPopup(data.getActivityName() + ";" + data.getActivityToken());
    }

    private void showScratchPopup(String rewards) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String points = rewards.split(";")[1];

        binding.tvTitle.setText(title);
        binding.tvDescription2.setText("No. of Stamps: " + points);

        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        binding.scratchView.setScratchListener(this);
        if (stampId == -1) {
            binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));
        } else {
            binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));
        }

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }

    private void scratchTokenReward() {
        RewardsPopupRequest request = new RewardsPopupRequest(stampId);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.scratchTokenReward(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_success));
                    NewDashboardHelper.Companion.getPopUpShowModels().clear();
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));
                    }

                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                    }

                        /*NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, "Stamps Tribe;Walk minimum 60,000 steps per week.;3"));
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, "500"));*/
                    showRewardsPopupDialogBox();
                    getEearnAndBurnDashboardData();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i >= 20) {
            scratchTokenReward();
            scratchCardLayout.onFullReveal();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if((alertDialogBonusStamp!= null && alertDialogBonusStamp.isShowing())){
                        alertDialogBonusStamp.dismiss();
                    }
                    if((alertDialogStamp!= null && alertDialogStamp.isShowing())){
                        alertDialogStamp.dismiss();
                    }
                }
            }, 3000);
        }
    }

    @Override
    public void onScratchStarted() {

    }

    private void showRewardsPopupDialogBox() {
        Log.d("sized", NewDashboardHelper.Companion.getPopUpShowModels().size() + "");
        if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0) {
            int i = 0;
            PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 1 && Objects.equals(firstData.getKey(), "Rewards")) {
                i = 1;
                firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            }
            switch (firstData.getKey()) {
                case "TokenStamp":
                    showStampsPopup(firstData.getValue(), context);
                    break;
                case "TokenStampBounce":
                    showBonusStampPopup(firstData.getValue(), context);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
            Log.d("sized", NewDashboardHelper.Companion.getPopUpShowModels().size() + "");
        }
    }

    private void showStampsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogStamp = alertBuilder.create();
        alertDialogStamp.setCancelable(true);
        if (!alertDialogStamp.isShowing())
            alertDialogStamp.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }
        alertDialogStamp.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });

        /*if(isOrange){
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));
        }else{
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));
        }*/
//        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


        /*if (rewards.contains("First Login")) {
            binding.btnNegative.setVisibility(View.GONE);
            binding.btnPositive.setText("OK");
            binding.btnPositive.setOnClickListener(view -> {
                alertDialog.dismiss();
                Intent intent = new Intent(context, SyncDeviceActivity.class);
                startActivity(intent);
            });
        } else {

        }*/

//        binding.scratchView.onFullReveal();
        binding.btnPositive.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.btnNegative.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogStamp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusStamp = alertBuilder.create();
        alertDialogBonusStamp.setCancelable(true);
        if (!alertDialogBonusStamp.isShowing())
            alertDialogBonusStamp.show();


        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

        alertDialogBonusStamp.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);

        binding.btnPositive.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }
}