package com.wyh.happyyousdk.heartAge;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityHeartAgeAnalysisBinding;
import com.wyh.happyyousdk.databinding.LayouHeartAgeInfoBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.heartAge.HeartAgeConversationIdReq;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeAnalysisResponse;
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeConversationIdResponse;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeartAgeAnalysisActivity extends AppCompatActivity implements ScratchListener, rewardDialogCloseListener {
    ActivityHeartAgeAnalysisBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    HeartAgeAnalysisResponse heartAgeAnalysisResponse;

    static FeedbackResponseData feedbackResponseData = null;
    List<PopUpShowModel> popUpShowModelsList;
    boolean isPositiveBtn = false;
    AlertDialog alertDialogBonusRewards;
    AssignRewardsResponse.SpinRewardsData spinRewardsData = null;
    QuizathonRewardData quizathonRewardData = null;
    boolean quizathonRewardBool = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_heart_age_analysis);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeToolbar.tvBack.setText("Heart Age");
        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivMenu.setVisibility(View.VISIBLE);
        binding.includeToolbar.ivMenu.setImageDrawable(getDrawable(R.drawable.ic_info));
        binding.includeToolbar.ivMenu.setColorFilter(getResources().getColor(R.color.white));
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.ivBack.setColorFilter(getColor(R.color.white));

        binding.includeToolbar.ivMenu.setOnClickListener(view -> showInfoLayout());

        JSONObject customObj = new JSONObject();


        try {
            if (getIntent().getExtras() != null && getIntent().getExtras().get("spinrewards") != null) {
                spinRewardsData = (AssignRewardsResponse.SpinRewardsData) getIntent().getExtras().get("spinrewards");
            } else {
                spinRewardsData = null;
            }
        } catch (Exception ex) {

        }
        try {
            quizathonRewardBool = getIntent().getBooleanExtra("quizrewards", false);
        } catch (Exception ex) {
            quizathonRewardBool = false;
        }
        try {
            customObj.put("PAGE_ID", "HeartAgeAnalysis");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        String popups = getIntent().getStringExtra("popups");
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();


        if (spinRewardsData != null) {
            getSpinRewardPopup(spinRewardsData);
        } else if (checkIsFromQuizqathon() && quizathonRewardBool) {
            FetchQuizReward();
        } else {
            if (popups != null) {
                popUpShowModelsList = gson.fromJson(popups, new TypeToken<List<PopUpShowModel>>() {
                }.getType());
                showRewardsPopupDialogBox();
            }
        }


        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.btnOk.setOnClickListener(view -> finish());
        binding.btnRetake.setOnClickListener(view -> {
            Intent intent;
            intent = new Intent(context, HeartAgeQuestionsActivity.class);
            startActivity(intent);
            finish();
//            generateHeartAgeConversationID();
        });

        heartAgeAnalysisResponse = new Gson().fromJson(SharedPref.getHeartAgeAnalysis(), HeartAgeAnalysisResponse.class);

        if (heartAgeAnalysisResponse.getFeedbackDetails() != null && heartAgeAnalysisResponse.getFeedbackDetails().getFeedbackModel() != null && heartAgeAnalysisResponse.getFeedbackDetails().getStarConfig() != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (feedbackResponseData != null) {
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedback(HeartAgeAnalysisActivity.this, feedbackResponseData);
                        feedbackResponseData = null;
                    }


                }
            }, 700);
        }


        setUI();

    }

    public boolean checkIsFromQuizqathon() {
        if (NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()) {
            return true;
        }
        return false;
    }


    private void FetchQuizReward() {
        ActivityRewardRequest activityRewardRequest = new ActivityRewardRequest(NewDashboardHelper.Companion.getTrasactionId(), NewDashboardHelper.Companion.getFeatureName());
        Call<CommonSuccessResponse> call = apiInterfaceWyh.FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewDashboardHelper.Companion.setTrasactionId(null);
                    NewDashboardHelper.Companion.setFeatureName(null);
                    NewDashboardHelper.Companion.setActivityName(null);
                    if (response.body().getQuizathonRewardData() != null) {
                        quizathonRewardData=response.body().getQuizathonRewardData();
                        getQuizathonRewardPopup(response.body().getQuizathonRewardData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {

            }
        });
    }

    private void generateHeartAgeConversationID() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        HeartAgeConversationIdReq conversationIdReq = new HeartAgeConversationIdReq(getString(R.string.heartage_i_id), "1.0.0.0");
        Call<HeartAgeConversationIdResponse> call = apiInterfaceWyh.createHeartAgeConversationID(SharedPref.getAuthToken(), conversationIdReq);
        Log.d("ConversationIdReqRequest", new Gson().toJson(conversationIdReq));

        call.enqueue(new Callback<HeartAgeConversationIdResponse>() {
            @Override
            public void onResponse(Call<HeartAgeConversationIdResponse> call, Response<HeartAgeConversationIdResponse> response) {
                Log.d("ConversationIdReqResponse", new Gson().toJson(response.body()));

                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {
                    refreshAuthToken();
                }
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_create_fresh_conversation_success));
                    SharedPref.putHeartAgeConversationID(response.body().getData().getConversationId());
                    Intent intent;
                    intent = new Intent(context, HeartAgeQuestionsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_create_fresh_conversation_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HeartAgeConversationIdResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_create_fresh_conversation_failed));
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
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    generateHeartAgeConversationID();
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

    private void setUI() {
        int percentageHeartAgeRiskScore = Math.round(heartAgeAnalysisResponse.getData().getHeartRiskScore() * 100);
        binding.tvHeartAgeScore.setText(Math.round(heartAgeAnalysisResponse.getData().getHeartAge()) + " years");
        binding.tvHeartAgeRiskScore.setText(percentageHeartAgeRiskScore + " %");
        binding.tvYourAgeScore.setText(heartAgeAnalysisResponse.getData().getAge() + " years");
        binding.tvRecommendations.setBackgroundColor(Color.TRANSPARENT);
        binding.tvRecommendations.loadData("<body style=\"margin: 0; padding: 0\">" + heartAgeAnalysisResponse.getData().getHeartAgeMessage().replaceAll("&#39;", "'"), "text/html", "UTF-8");
        binding.circleViewScore.setProgress(percentageHeartAgeRiskScore);
    }

    private void showInfoLayout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
        LayouHeartAgeInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layou_heart_age_info, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();


        binding.btnOK.setOnClickListener(view -> alertDialog.dismiss());
        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showRewardsPopupDialogBox() {
        if (!popUpShowModelsList.isEmpty()) {
            int i = 0;
            PopUpShowModel firstData = popUpShowModelsList.get(i);
            if (popUpShowModelsList.size() > 1 && Objects.equals(firstData.getKey(), "Rewards")) {
                i = 1;
                firstData = popUpShowModelsList.get(i);
            }
            popUpShowModelsList.remove(i);
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
            switch (firstData.getKey()) {
                case "Rewards":
                    showRewardsPopupNew(firstData.getValue(), context);
                    break;
                case "RewardsBounce":
                    showBonusRewardsPopup(firstData.getValue(), context);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
        }
    }

    private void showRewardsPopupNew(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 3000);

        alertDialog.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });
        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);


        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, HRAAnalysisActivity.class);
            startActivity(intent);
            finish();
        });

        binding.scratchView.onFullReveal();

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusRewardsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusRewards = alertBuilder.create();
        alertDialogBonusRewards.setCancelable(false);
        if (!alertDialogBonusRewards.isShowing())
            alertDialogBonusRewards.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

        alertDialogBonusRewards.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });
        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusRewards.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
            showRewardsPopupDialogBox();
            finish();
        });
        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusRewards.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onScratchComplete() {
        if (quizathonRewardData != null) {
            QuizRewardDialog.INSTANCE.QuizScratchCard(HeartAgeAnalysisActivity.this, quizathonRewardData.getTransId(), true);
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {

        if (i > 20) {
            scratchCardLayout.onFullReveal();
            {

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (alertDialogBonusRewards != null && alertDialogBonusRewards.isShowing()) {
                            alertDialogBonusRewards.dismiss();
                        }
                    }
                }, 3000);
            }
        }
    }

    @Override
    public void onScratchStarted() {

    }

    private void getSpinRewardPopup(AssignRewardsResponse.SpinRewardsData data) {
        try {
            if (data.getRewardType() != null && !data.getRewardType().isEmpty()) {
                String rewardtype = data.getRewardType();
                if (rewardtype.equalsIgnoreCase("Points")) {
                    PostSpinDialog.INSTANCE.showPostSpinnerPointsPopupCallBack(data.getRewardTitle(), context, "Trends", data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Offers")) {
                    PostSpinDialog.INSTANCE.showPostSpinnerOfferCallBack(context, data.getPartnerLogo(), data.getRewardDescription(), data.getPartnerName()
                            , data.getPartnerUrl(), "Trends", data.getExpiryInHours(), data.getRewardTitle(), data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Voucher")) {
                    PostSpinDialog.INSTANCE.showPostSpinnerVoucherCallBack(context, data.getRewardLogo(), data.getCouponCode(), data.getRewardDescription()
                            , data.getRewardTitle(), data.getExpiryInHours(), "Trends", data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Badge")) {
                    PostSpinDialog.INSTANCE.showPostSpinnerBadgePopupCallBack(context, "Trends", data.getRewardLogo(), data.getRewardTitle(), data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Stamps")) {
                    PostSpinDialog.INSTANCE.showStampsPopupCallBack(data.getRewardHeader1(), data.getRewardHeader2(), data.getRewardTitle(), data.getRewardValue(), this, this, this);
                }
            }
        } catch (Exception ex) {

        }
    }

    private void getQuizathonRewardPopup(QuizathonRewardData data) {
        try {
            if (data.getRewardType() != null && !data.getRewardType().isEmpty()) {
                String rewardtype = data.getRewardType();
                if (rewardtype.equalsIgnoreCase("Points")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerPointsPopupCallBack(data.getRewardTitle(), context, "Trends", data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Offers")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerOfferCallBack(context, data.getPartnerLogo(), data.getRewardDescription(), data.getPartnerName()
                            , data.getPartnerUrl(), "Trends", data.getExpiryInHours(), data.getRewardTitle(), data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Voucher")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerVoucherCallBack(context, data.getRewardLogo(), data.getCouponCode(), data.getRewardDescription()
                            , data.getRewardTitle(), data.getExpiryInHours(), "Trends", data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Badge")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerBadgePopupCallBack(context, "Trends", data.getRewardLogo(), data.getRewardTitle(), data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Stamps")) {
                    QuizRewardDialog.INSTANCE.showStampsPopupCallBack(data.getRewardHeader1(), data.getRewardHeader2(), data.getRewardTitle(), data.getRewardValue(), this, this, this);
                } else if (rewardtype.equalsIgnoreCase("future")) {
                    QuizRewardDialog.INSTANCE.showFutureRewardDialog(context, data.getDialogModel(), data.getClaimDate());
                }

            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void onDialogDismiss() {
        cancelDialog();
    }

    public void cancelDialog() {
        try {
            if (spinRewardsData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, spinRewardsData.getRewardType(), this);
            } else if (quizathonRewardData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, quizathonRewardData.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }
}
