package com.wyh.happyyousdk.ira;

import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.IRA_INTEGRATION_ID;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_NEW;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
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
import com.wyh.happyyousdk.databinding.ActivityIraanalysisBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.model.request.ira.ConversationRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IRAAnalysisActivity extends AppCompatActivity implements ScratchListener, rewardDialogCloseListener {
    ActivityIraanalysisBinding binding;
    Context context;
    String assessmentDate;
    String healthScore;
    List<PopUpShowModel> popUpShowModelsList;
    int stampId = -1;
    ApiInterfaceWyh apiInterfaceWyh;
    AlertDialog alertDialogBonusRewards;
    boolean isPositiveBtn = false, isStamp = false;
    AlertDialog alertDialogStamp, alertDialogBonusStamp;
    AssignRewardsResponse.SpinRewardsData spinRewardsData = null;
    QuizathonRewardData quizreward;
    boolean quizRewardBoolean = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_iraanalysis);

        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

//        binding.commonToolBar.ivBack
        binding.includeToolbar.tvBack.setText(getText(R.string.title_immunity_review));
        binding.includeToolbar.tvBack.setTextColor(getColor(R.color.white));
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
//        binding.commonToolBar.tvTitle.setOnClickListener(view -> finish());
        binding.includeToolbar.ivBack.setColorFilter(getColor(R.color.white));
        try {
            spinRewardsData = (AssignRewardsResponse.SpinRewardsData) getIntent().getExtras().get("spinrewards");
        } catch (Exception ex) {

        }

        try {
            quizRewardBoolean = getIntent().getBooleanExtra("quizrewards", false);
        } catch (Exception ex) {
            quizRewardBoolean = false;
        }

        getIRAHealthScore();

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "ImmunityScoreAnalysis");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        String popups = getIntent().getStringExtra("popups");

        if (spinRewardsData != null) {
            getSpinRewardPopup(spinRewardsData);
        } else if (checkIsFromQuizqathon() && quizRewardBoolean) {
            FetchQuizReward();
        }
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        if (popups != null) {
            popUpShowModelsList = gson.fromJson(popups, new TypeToken<List<PopUpShowModel>>() {
            }.getType());
            showRewardsPopupDialogBox();
        }


        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.btnRetake.setOnClickListener(view -> {
            Intent i = new Intent(context, IraActivity.class);
            i.putExtra("from", IRA_STATUS_NEW);
            i.putExtra("isFromRetake", true);
            startActivity(i);
            finish();
        });

        binding.btnOk.setOnClickListener(view -> finish());


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
                        getQuizathonRewardPopup(response.body().getQuizathonRewardData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIRAHealthScore();
    }

    private void getIRAHealthScore() {
        ProgressDialog prDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        prDialog.setMessage("Please wait ...");
        prDialog.setCancelable(false);
        prDialog.show();
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        ConversationRequest iraRequest = new ConversationRequest(IRA_INTEGRATION_ID, "");
        Call<IRAHealthScoreResponse> call = apiInterfaceWyh.getIRAHealthScore(SharedPref.getAuthToken(), iraRequest);

        call.enqueue(new Callback<IRAHealthScoreResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<IRAHealthScoreResponse> call, Response<IRAHealthScoreResponse> response) {
                if (prDialog.isShowing())
                    prDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_ira_answers_score_success));
                    if (response.body().getIraHealthScoreData() != null && response.body().getIraHealthScoreData().getPlaySports() != null) {
                        healthScore = response.body().getIraHealthScoreData().getPlaySports();
                        binding.tvIraHealthScore.setText(healthScore);
                        IRAHealthScoreResponse iraHealthScoreResponse = response.body();

                        if (iraHealthScoreResponse != null) {
                            assessmentDate = formatDateFromString("MM/dd/yyyy", "dd/MM/yyyy", iraHealthScoreResponse.getIraHealthScoreData().getCreatedAt());

                            binding.tvIraHealthScore.setText(iraHealthScoreResponse.getIraHealthScoreData().getPlaySports());
                            int immunityScore = Integer.parseInt(iraHealthScoreResponse.getIraHealthScoreData().getPlaySports());

                            boolean isBlue = false;

                            if (immunityScore >= 0 && immunityScore <= 50) {
                                isBlue = false;
                                binding.circleViewScore.setProgressBarColor(getColor(R.color.dark_pink));
                                binding.tvRecommendations.setText("Aim to eat a diet high in fruits and vegetables\n\nGet Plenty of Sleep\n\n" +
                                        "In case you are addicted to substances (tobacco, alcohol etc.) – work towards quitting it\n\nWork towards reducing stress levels\n\n" +
                                        "Consider taking health supplements after consulting your health care provider\n\nGo for Walks or indulge in some moderate exercise");
                            } else if (immunityScore > 50 && immunityScore <= 100) {
                                isBlue = false;
                                binding.circleViewScore.setProgressBarColor(getColor(R.color.dark_pink));
                                binding.tvRecommendations.setText(
                                        "Eat a healthy diet\n\nExercise Regularly\n\nIn case you are addicted to substances (tobacco, alcohol etc.) – work towards quitting it\n\n" +
                                                "Reduce stress levels\n\nSleep for a minimum of 8 hrs");
                            } else if (immunityScore > 100 && immunityScore <= 150) {
                                isBlue = true;
                                binding.circleViewScore.setProgressBarColor(getColor(R.color.btn_blue));
                                binding.tvRecommendations.setText(
                                        "Maintain a healthy diet\n\nMinimize stress Level\n\nSleep for a minimum of 8 hrs\n\n" +
                                                "Maintain a healthy weight");
                            } else if (immunityScore > 150 && immunityScore <= 200) {
                                isBlue = true;
                                binding.circleViewScore.setProgressBarColor(getColor(R.color.btn_blue));
                                binding.tvRecommendations.setText(
                                        "Eat a diet high in fruits and vegetables\n\nExercise regularly");
                            } else if (immunityScore > 200 && immunityScore <= 250) {
                                isBlue = true;
                                binding.tvHeader1.setVisibility(View.GONE);
                                binding.circleViewScore.setProgressBarColor(getColor(R.color.btn_blue));
                                binding.tvRecommendations.setText(
                                        "Wow! Your Immune Score Is Excellent, Keep It Up...");
                            }

                            Glide.with(context)
                                    .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages
                                            + (isBlue ? "ira_analysis_blue_bg.png" : "ira_analysis_red_bg.png"))
                                    .into(new CustomTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                            binding.llSectionScore.setBackground(resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {

                                        }
                                    });

                            binding.tvAssessmentDate.setText(assessmentDate);
//                            binding.circleViewScore.setMax(250);
                            binding.circleViewScore.setProgress(Integer.parseInt(iraHealthScoreResponse.getIraHealthScoreData().getPlaySports()));
                        }
                        Gson gson = new Gson();
                        String IRAHealthData = gson.toJson(response.body());
                        SharedPref.putIRAHealthData(IRAHealthData);
//                        binding.rlIra.setClickable(false);
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_ira_answers_score_failed));
                }
            }

            @Override
            public void onFailure(Call<IRAHealthScoreResponse> call, Throwable t) {
                if (prDialog != null && prDialog.isShowing())
                    prDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_ira_answers_score_failed));
            }
        });
    }

    private void showRewardsPopupDialogBox() {
        try {
            if (popUpShowModelsList.size() > 0) {
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
                    case "TokenStamp":
                        showStampsPopup(firstData.getValue(), context);
                        break;
                    case "TokenStampBounce":
                        showBonusStampPopup(firstData.getValue(), context);
                        break;
                    case "FeedbackPOPUP":
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedback(IRAAnalysisActivity.this, NewDashboardHelper.Companion.getFeedbackResponseData());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + firstData.getKey());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

    private void showStampsPopup(String rewards, Context context) {
        isStamp = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogStamp = alertBuilder.create();
        alertDialogStamp.setCancelable(false);
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
        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


//        binding.scratchView.onFullReveal();


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogStamp.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        isStamp = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
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
        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogStamp.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onScratchComplete() {
        if (quizreward != null) {
            QuizRewardDialog.INSTANCE.QuizScratchCard(this, quizreward.getTransId(), true);
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {

        if (i > 20) {
            scratchCardLayout.onFullReveal();
            {
                if (isStamp) {
                    isStamp = false;
                    scratchTokenReward();
                }


                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (alertDialogBonusRewards != null && alertDialogBonusRewards.isShowing()) {
                            alertDialogBonusRewards.dismiss();
                        }
                        if ((alertDialogBonusStamp != null && alertDialogBonusStamp.isShowing())) {
                            alertDialogBonusStamp.dismiss();
                        }
                        if ((alertDialogStamp != null && alertDialogStamp.isShowing())) {
                            alertDialogStamp.dismiss();
                        }
                    }
                }, 3000);
            }
        }
    }

    @Override
    public void onScratchStarted() {

    }

    private void scratchTokenReward() {
        RewardsPopupRequest request = new RewardsPopupRequest(stampId);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.scratchTokenReward(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {

                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_success));

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {

                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
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
            quizreward = data;
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
            } else if (quizreward != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, quizreward.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }
}