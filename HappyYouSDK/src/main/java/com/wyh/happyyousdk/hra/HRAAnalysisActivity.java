package com.wyh.happyyousdk.hra;

import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.utils.viewtooltip.ViewTooltip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.crypto.RSAEncryption;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.databinding.ActivityHraanalysisBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
;
import com.wyh.happyyousdk.databinding.InfoConcernLayoutBinding;
import com.wyh.happyyousdk.databinding.InfoStressPercentageBinding;
import com.wyh.happyyousdk.databinding.LayoutLifeStyleWelcomeBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding;
import com.wyh.happyyousdk.databinding.UpdateSerDetailsLayoutBinding;
import com.wyh.happyyousdk.hra.adapter.HRAConcernInfoAdapter;
import com.wyh.happyyousdk.hra.adapter.HappinessAdapter;
import com.wyh.happyyousdk.hra.adapter.HealthIndexAdapter;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.InfoConcernPojo;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.CommunityIDs;
import com.wyh.happyyousdk.model.request.ConversationIdReq;
import com.wyh.happyyousdk.model.request.FaceScanRegistrationRequest;
import com.wyh.happyyousdk.model.request.FacnScanInTribeRequest;
import com.wyh.happyyousdk.model.request.UserDetailsRequest;
import com.wyh.happyyousdk.model.request.faceScan.AddFaceScanVitalsRequest;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.ConversationIdResponse;
import com.wyh.happyyousdk.model.response.DownloadFaceScanResponse;
import com.wyh.happyyousdk.model.response.FaceScanInTribeResponse;
import com.wyh.happyyousdk.model.response.FaceScanRegistrationResponse;
import com.wyh.happyyousdk.model.response.UserDetailResponse;
import com.wyh.happyyousdk.model.response.faceScan.FetchFaceScanVitalsData;
import com.wyh.happyyousdk.model.response.faceScan.FetchFaceScanVitalsResponse;
import com.wyh.happyyousdk.model.response.faceScan.GetFaceKeysResponse;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HRAAnalysisActivity extends AppCompatActivity implements ScratchListener, rewardDialogCloseListener {

    ActivityHraanalysisBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    GetAnalysisResponse getAnalysisResponse;

    List<PopUpShowModel> popUpShowModelsList;
    AddFaceScanVitalsRequest request;
    ArrayList<CommunityIDs> list = new ArrayList<>();
    Calendar mainStartCalender;
    String gender = "";
    String user = "self";
    String otherGender = "";
    private static final int FACE_SCAN_REQUEST_CODE = 40;


    String dobStr;

    AlertDialog alertDialogBonusRewards, alertDialogStamp, alertDialogBonusStamp;

    int stampId = -1;

    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;
    boolean isPositiveBtn = false, isStamp = false;
    AssignRewardsResponse.SpinRewardsData spinRewardsData = null;
    QuizathonRewardData quizathonRewardData = null;
    boolean quizathonRewardBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hraanalysis);
        context = this;
        SharedPref.init(context);

        try {

            apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
            progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");


            getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
            try {
                spinRewardsData = (AssignRewardsResponse.SpinRewardsData) getIntent().getExtras().get("spinrewards");
            } catch (Exception ex) {

            }
            try {
                quizathonRewardBool = getIntent().getBooleanExtra("quizrewards", false);
            } catch (Exception ex) {

            }
            String popups = getIntent().getStringExtra("popups");
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            if (spinRewardsData != null) {
                getSpinRewardPopup(spinRewardsData);
            } else if (checkIsFromQuizqathon() && quizathonRewardBool) {
                FetchQuizReward();
                //getQuizathonRewardPopup(quizathonRewardData);
            } else if (popups != null) {
                popUpShowModelsList = gson.fromJson(popups, new TypeToken<List<PopUpShowModel>>() {
                }.getType());
                showRewardsPopupDialogBox();
            }

            JSONObject customObj = new JSONObject();
            try {
                customObj.put("PAGE_ID", "HealthScoreAnalysis");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            binding.btnOk.setOnClickListener(view -> finish());

            binding.btnRetake.setOnClickListener(view -> {
                Intent intent = new Intent(context, HRAQuestionsActivity.class);
                intent.putExtra("isFromRetake", true);
                startActivity(intent);
                finish();
            });

            binding.ivHome.setOnClickListener(view -> {
                Intent intent = new Intent(this, NewDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
            binding.llVitals.setVisibility(View.GONE);

            binding.includeBack.tvBack.setText("Health Score");
            binding.includeBack.llBack.setOnClickListener(view -> finish());

            binding.tvScore.setText("" + getAnalysisResponse.getAnalysisData().getScore());
            binding.progressHealthScore.setProgress(getAnalysisResponse.getAnalysisData().getScore());

            int bpScore = getAnalysisResponse.getAnalysisData().getNewScore().getSections().get(0).getScore();
            int lifestyleScore = getAnalysisResponse.getAnalysisData().getNewScore().getSections().get(1).getScore();
            int stressScore = getAnalysisResponse.getAnalysisData().getNewScore().getSections().get(2).getScore();
            int dietScore = getAnalysisResponse.getAnalysisData().getNewScore().getSections().get(3).getScore();

            int healthScore = getAnalysisResponse.getAnalysisData().getScore();
            binding.tvHealthScoreMeanText.setBackgroundColor(Color.TRANSPARENT);
            if (healthScore >= 0 && healthScore <= 180) {
                binding.tvHealthScoreMeanText.loadData("<body style=\"margin: 0; padding: 0\">" + getString(R.string.health_score_mean_text) + getString(R.string.health_score_mean_low), "text/html", "utf-8");
            } else if (healthScore <= 540) {
                binding.tvHealthScoreMeanText.loadData("<body style=\"margin: 0; padding: 0\">" + getString(R.string.health_score_mean_text) + getString(R.string.health_score_mean_med), "text/html", "utf-8");
            } else if (healthScore <= 810) {
                binding.tvHealthScoreMeanText.loadData("<body style=\"margin: 0; padding: 0\">" + getString(R.string.health_score_mean_text) + getString(R.string.health_score_mean_good), "text/html", "utf-8");
            } else if (healthScore <= 900) {
                binding.tvHealthScoreMeanText.loadData("<body style=\"margin: 0; padding: 0\">" + getString(R.string.health_score_mean_text) + getString(R.string.health_score_mean_great), "text/html", "utf-8");
            }
            binding.tvWaistDesc.loadData(getString(R.string.WHtRDescription), "text/html", "utf-8");
            binding.wvStress.loadData(getString(R.string.stress_text), "text/html", "utf-8");

            binding.tvWaterDesc.loadData(getString(R.string.waterDescription), "text/html", "utf-8");


            binding.tvBodyProfileScore.setText(bpScore + "%");
            binding.tvLifestyleScore.setText(lifestyleScore + "%");
            binding.tvStressScore.setText(stressScore + "%");
            binding.tvDietScore.setText(dietScore + "%");

            binding.ivInfoStress.setOnClickListener(view -> {
                viewToolTip(binding.ivInfoStress, "Higher the score lower the stress", ViewTooltip.Position.BOTTOM, ViewTooltip.ALIGN.END);
            });

            binding.ivInfoConcern.setOnClickListener(view -> {
                showConcernInfoLayout();
            });

            binding.ivInfoStressPercentage.setOnClickListener(view -> {
                showConcernInfoStressLayout();
            });


            if (getImageId(bpScore) == 0) {
                Glide.with(context).load(CommonUtils.getBaseUrlForAPI(context)
                        + SDKConstants.endPointForImages + "ic_hra_20_60.png").into(binding.ivBearBodyProfile);
            } else {
                binding.ivBearBodyProfile.setImageDrawable(ContextCompat.getDrawable(context, getImageId(bpScore)));
            }

            if (getImageId(lifestyleScore) == 0) {
                Glide.with(context).load(CommonUtils.getBaseUrlForAPI(context)
                        + SDKConstants.endPointForImages + "ic_hra_20_60.png").into(binding.ivBearStress);
            } else {
                binding.ivBearStress.setImageDrawable(ContextCompat.getDrawable(context, getImageId(lifestyleScore)));
            }

            if (getImageId(dietScore) == 0) {
                Glide.with(context).load(CommonUtils.getBaseUrlForAPI(context)
                        + SDKConstants.endPointForImages + "ic_hra_20_60.png").into(binding.ivBearDiet);
            } else {
                binding.ivBearDiet.setImageDrawable(ContextCompat.getDrawable(context, getImageId(dietScore)));
            }

            if (getImageId(stressScore) == 0) {
                Glide.with(context).load(CommonUtils.getBaseUrlForAPI(context)
                        + SDKConstants.endPointForImages + "ic_hra_20_60.png").into(binding.ivBearLifestyle);
            } else {
                binding.ivBearLifestyle.setImageDrawable(ContextCompat.getDrawable(context, getImageId(stressScore)));
            }


            //BMI
            Double bmiDouble = CommonUtils.onlyOneDecimalPoint(getAnalysisResponse.getAnalysisData().getAvgBMI().getBmi());
            Double idealWeightDouble = CommonUtils.onlyOneDecimalPoint(getAnalysisResponse.getAnalysisData().getIdealBMIWeight().getIdealBMI());
            Double bFatPercentageDouble = CommonUtils.onlyOneDecimalPoint(getAnalysisResponse.getAnalysisData().getBodyFatPercentage().getBodyFatPercentage());
            binding.tvBMI.setText(bmiDouble + "");
            binding.tvIdealWeight.setText(idealWeightDouble + "");
            binding.tvBFatPercentage.setText(bFatPercentageDouble + "%");

            //WtHR
            double WHtR = getAnalysisResponse.getAnalysisData().getWtHRatio().getWthRatio();
            binding.tvWthrValue.setText(String.format("%.2f", WHtR));
            if (WHtR < 0.34)
                binding.ivSelectedSmall.setVisibility(View.VISIBLE);
            else if (WHtR >= 0.34 && WHtR <= 0.42)
                binding.ivSelectedMedium.setVisibility(View.VISIBLE);
            else if (WHtR >= 0.43 && WHtR <= 0.52)
                binding.ivSelectedLarge.setVisibility(View.VISIBLE);
            else if (WHtR >= 0.53 && WHtR <= 0.57)
                binding.ivSelectedXLarge.setVisibility(View.VISIBLE);
            else if (WHtR > 0.57)
                binding.ivSelectedXXLarge.setVisibility(View.VISIBLE);

            //Risk
            String cardivarscular, diabetesScore, depressionScore, nutritionScore, hydrationScore, sleepScore, weightScore;
            if (getAnalysisResponse.getAnalysisData().getWorryAbout().get(0).getCalculatedRating().contains(".")) {
                cardivarscular = String.format("%.1f", Double.valueOf(getAnalysisResponse.getAnalysisData().getWorryAbout().get(0).getCalculatedRating()));
            } else {
                cardivarscular = getAnalysisResponse.getAnalysisData().getWorryAbout().get(0).getCalculatedRating();
            }

            if (getAnalysisResponse.getAnalysisData().getWorryAbout().get(1).getCalculatedRating().contains(".")) {
                diabetesScore = String.format("%.1f", Double.valueOf(getAnalysisResponse.getAnalysisData().getWorryAbout().get(1).getCalculatedRating()));
            } else {
                diabetesScore = getAnalysisResponse.getAnalysisData().getWorryAbout().get(1).getCalculatedRating();
            }

            if (getAnalysisResponse.getAnalysisData().getWorryAbout().get(2).getCalculatedRating().contains(".")) {
                depressionScore = String.format("%.1f", Double.valueOf(getAnalysisResponse.getAnalysisData().getWorryAbout().get(2).getCalculatedRating()));
            } else {
                depressionScore = getAnalysisResponse.getAnalysisData().getWorryAbout().get(2).getCalculatedRating();
            }

            if (getAnalysisResponse.getAnalysisData().getWorryAbout().get(3).getCalculatedRating().contains(".")) {
                nutritionScore = String.format("%.1f", Double.valueOf(getAnalysisResponse.getAnalysisData().getWorryAbout().get(3).getCalculatedRating()));
            } else {
                nutritionScore = getAnalysisResponse.getAnalysisData().getWorryAbout().get(3).getCalculatedRating();
            }

            if (getAnalysisResponse.getAnalysisData().getWorryAbout().get(4).getCalculatedRating().contains(".")) {
                hydrationScore = String.format("%.1f", Double.valueOf(getAnalysisResponse.getAnalysisData().getWorryAbout().get(4).getCalculatedRating()));
            } else {
                hydrationScore = getAnalysisResponse.getAnalysisData().getWorryAbout().get(4).getCalculatedRating();
            }

            if (getAnalysisResponse.getAnalysisData().getWorryAbout().get(5).getCalculatedRating().contains(".")) {
                sleepScore = String.format("%.1f", Double.valueOf(getAnalysisResponse.getAnalysisData().getWorryAbout().get(5).getCalculatedRating()));
            } else {
                sleepScore = getAnalysisResponse.getAnalysisData().getWorryAbout().get(5).getCalculatedRating();
            }

            if (getAnalysisResponse.getAnalysisData().getWorryAbout().get(6).getCalculatedRating().contains(".")) {
                weightScore = String.format("%.1f", Double.valueOf(getAnalysisResponse.getAnalysisData().getWorryAbout().get(6).getCalculatedRating()));
            } else {
                weightScore = getAnalysisResponse.getAnalysisData().getWorryAbout().get(6).getCalculatedRating();
            }

            binding.tvCardioScore.setText(cardivarscular + "/10");
            binding.progressCardio.setProgress(Float.parseFloat(cardivarscular));
            binding.tvDiabetesScore.setText(diabetesScore + "/10");
            binding.progressDiabetes.setProgress(Float.parseFloat(diabetesScore));
            binding.tvDepressionScore.setText(depressionScore + "/10");
            binding.progressDepression.setProgress(Float.parseFloat(depressionScore));
            binding.tvNutritionScore.setText(nutritionScore + "/10");
            binding.progressNutrition.setProgress(Float.parseFloat(nutritionScore));
            binding.tvHydrationScore.setText(hydrationScore + "/10");
            binding.progressHydration.setProgress(Float.parseFloat(hydrationScore));
            binding.tvSleepScore.setText(sleepScore + "/10");
            binding.progressSleep.setProgress(Float.parseFloat(sleepScore));
            binding.tvWeightScore.setText(weightScore + "/10");
            binding.progressWeight.setProgress(Float.parseFloat(weightScore));

            switch (getAnalysisResponse.getAnalysisData().getWorryAbout().get(0).getCls()) {
                case "G":
                    binding.progressCardio.setProgressBarColor(getResources().getColor(R.color.blue));
                    break;
                case "Y":
                    binding.progressCardio.setProgressBarColor(getResources().getColor(R.color.light_orange));
                    break;
                case "R":
                    binding.progressCardio.setProgressBarColor(getResources().getColor(R.color.pink));
                    break;
            }
            switch (getAnalysisResponse.getAnalysisData().getWorryAbout().get(1).getCls()) {
                case "G":
                    binding.progressDiabetes.setProgressBarColor(getResources().getColor(R.color.blue));
                    break;
                case "Y":
                    binding.progressDiabetes.setProgressBarColor(getResources().getColor(R.color.light_orange));
                    break;
                case "R":
                    binding.progressDiabetes.setProgressBarColor(getResources().getColor(R.color.pink));
                    break;
            }
            switch (getAnalysisResponse.getAnalysisData().getWorryAbout().get(2).getCls()) {
                case "G":
                    binding.progressDepression.setProgressBarColor(getResources().getColor(R.color.blue));
                    break;
                case "Y":
                    binding.progressDepression.setProgressBarColor(getResources().getColor(R.color.light_orange));
                    break;
                case "R":
                    binding.progressDepression.setProgressBarColor(getResources().getColor(R.color.pink));
                    break;
            }
            switch (getAnalysisResponse.getAnalysisData().getWorryAbout().get(3).getCls()) {
                case "G":
                    binding.progressNutrition.setProgressBarColor(getResources().getColor(R.color.blue));
                    break;
                case "Y":
                    binding.progressNutrition.setProgressBarColor(getResources().getColor(R.color.light_orange));
                    break;
                case "R":
                    binding.progressNutrition.setProgressBarColor(getResources().getColor(R.color.pink));
                    break;
            }
            switch (getAnalysisResponse.getAnalysisData().getWorryAbout().get(4).getCls()) {
                case "G":
                    binding.progressHydration.setProgressBarColor(getResources().getColor(R.color.blue));
                    break;
                case "Y":
                    binding.progressHydration.setProgressBarColor(getResources().getColor(R.color.light_orange));
                    break;
                case "R":
                    binding.progressHydration.setProgressBarColor(getResources().getColor(R.color.pink));
                    break;
            }
            switch (getAnalysisResponse.getAnalysisData().getWorryAbout().get(5).getCls()) {
                case "G":
                    binding.progressSleep.setProgressBarColor(getResources().getColor(R.color.blue));
                    break;
                case "Y":
                    binding.progressSleep.setProgressBarColor(getResources().getColor(R.color.light_orange));
                    break;
                case "R":
                    binding.progressSleep.setProgressBarColor(getResources().getColor(R.color.pink));
                    break;
            }
            switch (getAnalysisResponse.getAnalysisData().getWorryAbout().get(6).getCls()) {
                case "G":
                    binding.progressWeight.setProgressBarColor(getResources().getColor(R.color.blue));
                    break;
                case "Y":
                    binding.progressWeight.setProgressBarColor(getResources().getColor(R.color.light_orange));
                    break;
                case "R":
                    binding.progressWeight.setProgressBarColor(getResources().getColor(R.color.pink));
                    break;
            }

            //Happiness Index
            HappinessAdapter happinessAdapter = new HappinessAdapter(context, getAnalysisResponse.getAnalysisData().getHappinessIndex());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            binding.rvHappiness.setAdapter(happinessAdapter);
            binding.rvHappiness.setLayoutManager(linearLayoutManager);
            //noinspection InvalidSetHasFixedSize
            binding.rvHappiness.setHasFixedSize(true);
            binding.tvAge.setText(getAnalysisResponse.getAnalysisData().getNewScore().getAge() + " years");
            binding.tvMetabolicAge.setText(getAnalysisResponse.getAnalysisData().getBiologicalAge() + " years");
            binding.progressHappinessIndex.setProgress(getAnalysisResponse.getAnalysisData().getNewScore().getHappinessScore());
            binding.tvHappinessPercentage.setText(getAnalysisResponse.getAnalysisData().getNewScore().getHappinessScore() + "%");

            //Health Index
            List<String> stringList = new ArrayList<>();
            stringList.addAll(getAnalysisResponse.getAnalysisData().getAnalyticPositive());
            stringList.addAll(getAnalysisResponse.getAnalysisData().getAnalyticNegative());
            HealthIndexAdapter healthIndexAdapter = new HealthIndexAdapter(context, stringList);
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
            linearLayoutManager2.setOrientation(RecyclerView.VERTICAL);
            binding.rvHealthIndex.setAdapter(healthIndexAdapter);
            binding.rvHealthIndex.setLayoutManager(linearLayoutManager2);
            //noinspection InvalidSetHasFixedSize
            binding.rvHealthIndex.setHasFixedSize(true);

            //Stress
            int stressMeterScore = getAnalysisResponse.getAnalysisData().getStressScore();
            binding.progressStress.setProgress(100 - stressMeterScore);
            if (stressMeterScore <= 20)
                binding.tvStress.setText("Warning");
            else if (stressMeterScore <= 40)
                binding.tvStress.setText("High");
            else if (stressMeterScore <= 60)
                binding.tvStress.setText("Moderate");
            else
                binding.tvStress.setText("Low");

            //Water
            binding.tvDrinkingGlasses.setText("" + getAnalysisResponse.getAnalysisData().getWaterWidget().getActualDrinkingGlass());
            binding.tvShouldDrinkGlasses.setText("" + getAnalysisResponse.getAnalysisData().getWaterWidget().getShouldDrinkGlass());

            //Sleep
            binding.tvSleepHours.setText(getAnalysisResponse.getAnalysisData().getSleepAnalysis().getSleepHours() + " hours");
            binding.tvSleepTextInVisible.setText(getAnalysisResponse.getAnalysisData().getSleepAnalysis().getSaText());
            binding.tvSleepText.setText(getAnalysisResponse.getAnalysisData().getSleepAnalysis().getSaText());
            binding.tvSleepTextInVisible.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = binding.tvSleepTextInVisible.getLineCount();
//                Toast.makeText(context, "line count"+ lineCount, Toast.LENGTH_SHORT).show();
                    if (lineCount > 6) {
                        binding.tvSleepTextKnowMore.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvSleepTextKnowMore.setVisibility(View.GONE);
                    }
                }
            });

            binding.tvSleepTextKnowMore.setOnClickListener(view -> showLifestyleWelcomeLayout("\n" + getAnalysisResponse.getAnalysisData().getSleepAnalysis().getSaText()));

            //Calories
            binding.tvCaloriesGoal.setText("" + getAnalysisResponse.getAnalysisData().getBmr());
            binding.tvRMRCalories.setText("" + getAnalysisResponse.getAnalysisData().getRmr());
//        SharedPref.putBmr("" + getAnalysisResponse.getAnalysisData().getBmr());
            SharedPref.putUserAge("" + getAnalysisResponse.getAnalysisData().getNewScore().getAge());
            SharedPref.putUserWeight("" + getAnalysisResponse.getAnalysisData().getNewScore().getWeight());
            SharedPref.putUserGender("" + getAnalysisResponse.getAnalysisData().getNewScore().getGender());
            SharedPref.putUserHeightFt("" + getAnalysisResponse.getAnalysisData().getNewScore().getHeights());

            String height = getAnalysisResponse.getAnalysisData().getNewScore().getHeights();
            String tempHeight = height;
            if (height.contains(".")) {
                String feet, inch;
                feet = height.split("\\.")[0];
                inch = tempHeight.split("\\.")[1];
                SharedPref.putUserHeight(convertFeetInchesToCentimeter(feet, inch));
            } else
                SharedPref.putUserHeight(convertFeetInchesToCentimeter(height, "0"));


        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        quizathonRewardData = response.body().getQuizathonRewardData();
                        getQuizathonRewardPopup(response.body().getQuizathonRewardData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public static String convertFeetInchesToCentimeter(String feet, String inches) {
        double heightInFeet = 0;
        double heightInInches = 0;
        try {
            if (feet != null && feet.trim().length() != 0) {
                heightInFeet = Double.parseDouble(feet);
            }
            if (inches != null && inches.trim().length() != 0) {
                heightInInches = Double.parseDouble(inches);
            }
        } catch (NumberFormatException nfe) {

        }
        return String.valueOf(Math.round((heightInFeet * 30.48) + (heightInInches * 2.54)));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void viewToolTip(ImageView view, String text, ViewTooltip.Position position, ViewTooltip.ALIGN align) {
        ViewTooltip viewTooltip = ViewTooltip
                .on((Activity) context, view)
                .autoHide(true, 2000)
                .clickToHide(true)
                .align(align)
                .position(position)
                .text(text)
                .textColor(Color.BLACK)
                .color(context.getResources().getColor(R.color.light_pink))
                .corner(10)
                .arrowWidth(15)
                .arrowHeight(15)
                .distanceWithView(0)
                .setTextGravity(Gravity.CENTER)
                //change the opening animation
                .animation(new ViewTooltip.FadeTooltipAnimation(500))
                //listeners
                .onDisplay(new ViewTooltip.ListenerDisplay() {
                    @Override
                    public void onDisplay(View view) {

                    }
                })
                .onHide(new ViewTooltip.ListenerHide() {
                    @Override
                    public void onHide(View view) {

                    }
                });
        viewTooltip.show();
    }

    private void checkHraDraft() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        ConversationIdReq conversationIdReq = new ConversationIdReq(getString(R.string.hra_i_id));
        Call<ConversationIdResponse> call = apiInterfaceWyh.checkHraDraft(SharedPref.getAuthToken(), conversationIdReq);
        Log.d("check Hra Draft", new Gson().toJson(conversationIdReq));
        call.enqueue(new Callback<ConversationIdResponse>() {
            @Override
            public void onResponse(Call<ConversationIdResponse> call, Response<ConversationIdResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

                Log.d("check Hra Draft", new Gson().toJson(response.body()));
                Log.d("check Hra Draft", new Gson().toJson(response.code()));
                if (response.code() == 401) {
                    refreshAuthToken();
                }
                if (response.code() == 200 || response.code() == 204) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.create_hra_draft_success));
                    Intent intent = new Intent(context, HRAQuestionsActivity.class);
                    intent.putExtra("isFromRetake", true);
                    intent.putExtra("isFromRetakeData", response.body() == null);
                    startActivity(intent);
                    finish();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.create_hra_draft_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConversationIdResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.create_hra_draft_failed));
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
                    checkHraDraft();
//                    generateConversationID();
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

    private void showRewardsPopupDialogBox() {
        if (popUpShowModelsList.size() > 0) {
            int i = 0;
            PopUpShowModel firstData = popUpShowModelsList.get(i);
            if (popUpShowModelsList.size() > 1 && Objects.equals(firstData.getKey(), "Rewards")) {
                i = 1;
                firstData = popUpShowModelsList.get(i);
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
            popUpShowModelsList.remove(i);
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
                    instance.showPopUpFeedback(HRAAnalysisActivity.this, NewDashboardHelper.Companion.getFeedbackResponseData());
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


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

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
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);

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
        alertDialogBonusStamp = alertBuilder.create();
        alertDialogBonusStamp.setCancelable(true);
        if (!alertDialogBonusStamp.isShowing())
            alertDialogBonusStamp.show();

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
        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusStamp.dismiss();
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

    @Override
    public void onScratchComplete() {
        if (quizathonRewardData != null) {
            QuizRewardDialog.INSTANCE.QuizScratchCard(HRAAnalysisActivity.this, quizathonRewardData.getTransId(), true);
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

    private void showLifestyleWelcomeLayout(String content) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
        LayoutLifeStyleWelcomeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_life_style_welcome, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        binding.tvTitle.setVisibility(View.GONE);
        binding.tvContent.setVisibility(View.GONE);
        binding.content.setText(content);

        binding.btnOK.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private int getImageId(int bpScore) {
        if (bpScore >= 0 && bpScore <= 20)
            return R.drawable.ic_hra_profile_0_20_risk;
        else if (bpScore > 20 && bpScore <= 60)
            return 0;
        else if (bpScore > 60 && bpScore <= 90)
            return R.drawable.ic_hra_profile_60_90_risk;
        else if (bpScore > 90)
            return R.drawable.ic_hra_profile_90_above_risk;
        else
            return R.drawable.ic_hra_body_profile_0;

    }

    private void showConcernInfoLayout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
        InfoConcernLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.info_concern_layout, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        List<InfoConcernPojo> data = new ArrayList<>();

        data.add(new InfoConcernPojo("Cardiovascular", "0 - 10 (non-smoke)", "0-7 (smoke)", "7-10 (smoke)"));
        data.add(new InfoConcernPojo("Diabetes", "0 - 3", "3-7", "7-10"));
        data.add(new InfoConcernPojo("Depression", "0 - 3", "3-7", "7-10"));
        data.add(new InfoConcernPojo("Sleep", "0 - 3", "3-7", "7-10"));
        data.add(new InfoConcernPojo("Nutritional", "0 - 3", "3-7", "7-10"));
        data.add(new InfoConcernPojo("Hydration", "0 - 3", "3-7", "7-10"));
        data.add(new InfoConcernPojo("Body Weight", "0 - 3", "3-7", "7-10"));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        HRAConcernInfoAdapter dassInfoAdapter = new HRAConcernInfoAdapter(context, data);
        binding.rvConcernData.setAdapter(dassInfoAdapter);
        binding.rvConcernData.setLayoutManager(linearLayoutManager);
        //noinspection InvalidSetHasFixedSize
        binding.rvConcernData.setHasFixedSize(true);

        binding.btnOK.setOnClickListener(view -> alertDialog.dismiss());
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome), FROM_HTML_MODE_LEGACY));
        }else{
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome)));
        }

        binding.btnOK.setOnClickListener(v-> {
            alertDialog.dismiss();
        });
*/
        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.7f));
    }

    private void showConcernInfoStressLayout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
        InfoStressPercentageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.info_stress_percentage, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();


        binding.btnOK.setOnClickListener(view -> alertDialog.dismiss());
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome), FROM_HTML_MODE_LEGACY));
        }else{
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome)));
        }

        binding.btnOK.setOnClickListener(v-> {
            alertDialog.dismiss();
        });
*/
        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.4f));
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