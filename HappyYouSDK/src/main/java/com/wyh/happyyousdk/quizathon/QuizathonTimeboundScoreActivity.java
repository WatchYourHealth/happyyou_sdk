package com.wyh.happyyousdk.quizathon;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.quizathon.QuizathonActivity.areListsEqual;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.Utilities.MessageInfoDialog;
import com.wyh.happyyousdk.SpinWheel.Utilities.RewardInfoBottomSheet;
import com.wyh.happyyousdk.SpinWheel.Utilities.onRewardDialogClick;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.SpinWheel.spinRewardCallBack;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.RedirectionMethod;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityQuizathonScoreBinding;
import com.wyh.happyyousdk.databinding.ActivityTimeboundScoreBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.WarerIntakeLayoutBinding;
import com.wyh.happyyousdk.databinding.ZenZoneLayoutBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.RedirectionModel;
import com.wyh.happyyousdk.model.request.GetQuizathonDetailsRequest;
import com.wyh.happyyousdk.model.request.SaveQuizRegistration;
import com.wyh.happyyousdk.model.request.diary.AddDiaryRequest;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.model.request.postloginreward.StartSpinActivityRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.request.quizathon.RetakeQuestionRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.request.trends.AddReminderDataRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;
import com.wyh.happyyousdk.model.response.GetQuizathonDetailsResponse;
import com.wyh.happyyousdk.model.response.QuestionModel;
import com.wyh.happyyousdk.model.response.SaveQuizAnswerData;
import com.wyh.happyyousdk.model.response.diary.AddDiaryResponse;
import com.wyh.happyyousdk.model.response.diary.UploadUserFileResponse;
import com.wyh.happyyousdk.model.response.playwin.DialogModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.model.response.playwin.RetakeResponseModel;
import com.wyh.happyyousdk.model.response.playwin.SaveQuizRegistrationResponse;
import com.wyh.happyyousdk.model.response.postloginreward.CommonResponse;
import com.wyh.happyyousdk.model.response.postloginreward.RewardItem;
import com.wyh.happyyousdk.model.response.quizathon.SaveQuizathonAnsModel;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.play_and_win.PlayAndWinActivity;
import com.wyh.happyyousdk.quiz.QuizScoreActivity;
import com.wyh.happyyousdk.quizathon.adapter.QuizScoreAdapter;
import com.wyh.happyyousdk.quizathon.adapter.QuizStreakAdapter;
import com.wyh.happyyousdk.quizathon.dialog.PreActivityDialog;
import com.wyh.happyyousdk.quizathon.dialog.QuizMessageDialog;
import com.wyh.happyyousdk.quizathon.dialog.RetakeAlertDialog;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyh.happyyousdk.utils.dialog.RegistrationDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizathonTimeboundScoreActivity extends AppCompatActivity implements ScratchListener, rewardDialogCloseListener, ReTakeQuizListener,
        onRewardClick, spinRewardCallBack {
    ActivityTimeboundScoreBinding binding;
    Context context;
    ArrayList<QuestionModel> data;
    String categoryName, category, rewards, stamps, rewardsBonus, stampsBonus, comingFrom, rewardDate;
    String yourScore, totalScore;
    boolean isStamp = false, stampsToken;
    int stampId = -1;
    ApiInterfaceWyh apiInterfaceWyh;
    boolean isPositiveBtn = false;
    String titleContent = "";
    String type = "";
    AlertDialog alertDialogBonusRewards, alertDialogStamp, alertDialogBonusStamp;
    SaveQuizAnswerData quizAnswerData;
    boolean isFromAdapter = false;
    boolean isRetakeAvailable = false;
    boolean showNextGameButton = false;
    boolean hasSufficientBalance = false;
    boolean withActivity = false;
    int retakePointstoBurn = 0;
    SaveQuizathonAnsModel saveQuizathonAnsModel = null;
    FeedbackResponseData feedbackResponseData = null;
    String qid = "";
    APIInterface apiInterface;


    QuizathonRewardData quizathonRewardData;
    DialogModel dialogModel = null;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    int glasses;
    DecimalFormat format = new DecimalFormat("0.##");
    private static final int CAMERA_PERMISSION_CODE = 100;
    RewardItem reward = new RewardItem();
    ProgressDialog progressDialog;
    String retakeId = null;
    String nextGameRedirectTo = null;
    boolean isFeedback = false;
    boolean isFuture = false;

    ArrayList<FileData> fileDataList = new ArrayList<>();
    List<MultipartBody.Part> parts = new ArrayList<>();
    String imageName = "";
    String fileUploadKey = "JournalUpload";

    QuizathonModel quizathonModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_timebound_score);
        context = this;
        SharedPref.init(context);
        apiInterface = RetrofitHandler.apiInterface();
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        Bundle extras = getIntent().getExtras();
        stampsToken = getIntent().getExtras().getBoolean("stampsToken", false);
        APILogs.INSTANCE.activityTracker("A_PlayAndWinDashboard_Quizathon_Summary_View", context);
        if (extras != null) {
            String value = extras.getString("data");
            categoryName = extras.getString("CategoryName");
            category = extras.getString("Category");
            rewards = extras.getString("rewards");
            qid = extras.getString("qid");
            isRetakeAvailable = extras.getBoolean("isRetakeAvailable", false);
            retakeId = extras.getString("RetakeId");
            hasSufficientBalance = extras.getBoolean("hasSufficientBalance", false);
            retakePointstoBurn = extras.getInt("retakePointstoBurn");
            withActivity = extras.getBoolean("withActivity", false);
            saveQuizathonAnsModel = (SaveQuizathonAnsModel) extras.get("quizdata");
//            feedbackResponseData = (FeedbackResponseData) extras.get("feedback");

            progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            showNextGameButton = extras.getBoolean("showNextGameButton", false);
            nextGameRedirectTo = extras.getString("nextGameRedirectTo", "");
            if (nextGameRedirectTo != null && !nextGameRedirectTo.isEmpty()) {
                if (nextGameRedirectTo.contains("_")) {
                    List<String> valList = Arrays.asList(nextGameRedirectTo.split("_"));
                    if (valList != null && valList.size() > 1) {
                        qid = valList.get(1);
                    }
                }
            }
            try {
                quizAnswerData = (SaveQuizAnswerData) getIntent().getExtras().get("data_savequiz");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                type = getIntent().getStringExtra("type");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                quizathonRewardData = (QuizathonRewardData) extras.get("quizrewards");
                quizathonModel = (QuizathonModel) extras.get("quizModel");
            } catch (Exception ex) {
                quizathonRewardData = null;
                quizathonModel = null;
            }


            try {
                dialogModel = (DialogModel) extras.get("dialogModel");
            } catch (Exception ex) {
                dialogModel = null;
            }

            rewardsBonus = extras.getString("rewardsBonus");
            stampsBonus = extras.getString("stampsBonus");
            stamps = extras.getString("stamps");

            comingFrom = extras.getString("comingFrom");
            rewardDate = extras.getString("rewardDate");
            if (category != null && !category.isEmpty()) {
                binding.tvBack.setText(category);
            } else {
                binding.tvBack.setText(categoryName);
            }
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            data = gson.fromJson(value, new TypeToken<List<QuestionModel>>() {
            }.getType());


            try {
                yourScore = extras.getString("your_score");
                if (comingFrom != null && comingFrom.equalsIgnoreCase("feedback")) {
                    isFeedback = true;
                    binding.scoreLayout.setVisibility(View.GONE);
                    binding.txtYourScore.setText("YOUR FEEDBACK");
                    binding.btnRequiz.setVisibility(View.GONE);
                }
            } catch (Exception ex) {

            }
            if (yourScore == null) {
                try {
                    yourScore = "0";
                    int score = 0;
                    for (QuestionModel q : data) {

                        List<String> correctAnswer = new ArrayList<>();
                        if (q.getAnswer() != null && q.getAnswer().contains(",")) {
                            correctAnswer = Arrays.asList(q.getAnswer().split(","));
                        } else {
                            correctAnswer.add(q.getAnswer());
                        }
                        if (correctAnswer != null && q.getAnswers() != null && q.getAnswers().size() > 0) {
                            if (areListsEqual(correctAnswer, q.getAnswers())) {
                                score++;
                            }
                        }

                        yourScore = "" + score;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            try {
                totalScore = extras.getString("total_score");
            } catch (Exception ex) {

            }

            if (totalScore == null) {
                try {
                    if (data != null && data.size() > 0) {
                        totalScore = String.valueOf(data.size());
                    }
                } catch (Exception e1) {
                    totalScore = "0";
                }
            }
        }

        if (comingFrom != null && !comingFrom.equalsIgnoreCase("")) {
            if (comingFrom.equalsIgnoreCase("bannerQuiz")) {
                binding.rewardsTv.setVisibility(View.VISIBLE);
                binding.rewardsTv.setText(rewardDate);
            }
        }

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "QuizScore");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        
        //if (quizAnswerData != null && quizAnswerData.getShowPopup() && spinRewardsData == null) {
        if (quizAnswerData != null && quizAnswerData.getShowPopup() && quizathonRewardData == null) {
            MessageInfoDialog messageInfoDialog = new MessageInfoDialog(context, ContextCompat.getDrawable(context, R.drawable.ic_oops), "Oops!", quizAnswerData.getMessage());
            messageInfoDialog.show();

        } else if (quizathonRewardData != null) {
            if (quizathonRewardData.getRewardType().equalsIgnoreCase("future")) {
                isFuture = true;
            }
            getQuizathonRewardPopup(quizathonRewardData);
        } else if (!Objects.equals(rewards, "") && rewards != null) {
            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, rewards));
        }

        if (!Objects.equals(rewardsBonus, "") && rewardsBonus != null) {
            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, rewardsBonus));
        }


        if (!Objects.equals(stamps, "") && stamps != null) {
            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, stamps));
        }

        if (!Objects.equals(stampsBonus, "") && stampsBonus != null) {
            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, stampsBonus));
        }

        if (quizathonModel != null && quizathonModel.getStreakModels() != null && quizathonModel.getStreakModels().size() > 0) {
            binding.llStrealScore.setVisibility(View.VISIBLE);
            binding.totalStreak.setText("/" + quizathonModel.getStreakModels().size() + "");
            int cout = 0;
            for (int i = 0; i < quizathonModel.getStreakModels().size(); i++) {

                if (quizathonModel.getStreakModels().get(i).isQuizTaken()) {
                    cout++;
                }
            }
            binding.yourStreak.setText(cout + "");
            QuizStreakAdapter adapter = new QuizStreakAdapter(context, quizathonModel.getStreakModels());
            binding.rvStreak.setAdapter(adapter);
        }
        showRewardsPopupDialogBox();

        setToolBar();
        QuizScoreAdapter adapter = new QuizScoreAdapter(context, data, isFeedback);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setAdapter(adapter);

        if (quizathonModel.getShowConsent() != null && quizathonModel.getShowConsent()) {

            PreActivityDialog dialog = new PreActivityDialog(this, quizathonModel, this);
            dialog.show();
            quizathonModel.setQuizDesc("");
        } else if (withActivity && comingFrom != null) {
            quizathonModel.setQuizDesc("");
            if(saveQuizathonAnsModel != null){
                saveQuizathonAnsModel.setRewardDescription(null);
            }
            showBottomSheet();
        } else if (dialogModel != null && dialogModel.getType() != null && !isFuture) {
            quizathonModel.setQuizDesc("");
            if(saveQuizathonAnsModel != null){
                saveQuizathonAnsModel.setRewardDescription(null);
            }
            QuizMessageDialog messageInfoDialog = new QuizMessageDialog(context, dialogModel, rewardDate, apiInterfaceWyh);
            messageInfoDialog.show();

        } else {
            quizathonModel.setQuizDesc("");
            if(saveQuizathonAnsModel != null){
                saveQuizathonAnsModel.setRewardDescription(null);
            }
            if (saveQuizathonAnsModel != null) {
                reward.setHowToDo(saveQuizathonAnsModel.getHowToDo());
                reward.setWhatToDo(saveQuizathonAnsModel.getWhatToDo());
                reward.setWhyToDo(saveQuizathonAnsModel.getWhyToDo());
                reward.setRewardName(saveQuizathonAnsModel.getRewardName());
                reward.setRewardType(saveQuizathonAnsModel.getRewardType());
                reward.setRewardDescription(saveQuizathonAnsModel.getRewardDescription());
                reward.setActivity(saveQuizathonAnsModel.getActivity());
                reward.setExpireOn(saveQuizathonAnsModel.getExpireOn());
                reward.setIsStarted(saveQuizathonAnsModel.isStarted());
                reward.setRedirectionKey(saveQuizathonAnsModel.getRedirectionKey());
                if (saveQuizathonAnsModel.getActivityTransId() != null && !saveQuizathonAnsModel.getActivityTransId().isEmpty()) {
                    reward.setActivityTransId(Integer.parseInt(saveQuizathonAnsModel.getActivityTransId()));
                }
            }
        }


    }

    public void FetchQuizReward() {
        ActivityRewardRequest activityRewardRequest = new ActivityRewardRequest(NewDashboardHelper.Companion.getTrasactionId(), NewDashboardHelper.Companion.getFeatureName());
        Call<CommonSuccessResponse> call = apiInterface.FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest);

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
                t.printStackTrace();
            }
        });
    }

    private void FetchQuizReward(String transactionId, String featureName) {
        ActivityRewardRequest activityRewardRequest = new ActivityRewardRequest(transactionId, featureName);
        Call<CommonSuccessResponse> call = apiInterface.FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest);

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
                t.printStackTrace();
            }
        });
    }

    public boolean checkIsFromQuizqathon() {
        if (NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()) {
            return true;
        }
        return false;
    }

    public void showBottomSheet() {
        if (saveQuizathonAnsModel != null) {


            reward.setHowToDo(saveQuizathonAnsModel.getHowToDo());
            reward.setWhatToDo(saveQuizathonAnsModel.getWhatToDo());
            reward.setWhyToDo(saveQuizathonAnsModel.getWhyToDo());
            reward.setRewardName(saveQuizathonAnsModel.getRewardName());
            reward.setRewardType(saveQuizathonAnsModel.getRewardType());
            reward.setRewardDescription(saveQuizathonAnsModel.getRewardDescription());
            reward.setActivity(saveQuizathonAnsModel.getActivity());
            reward.setExpireOn(saveQuizathonAnsModel.getExpireOn());
            reward.setIsStarted(saveQuizathonAnsModel.isStarted());
            reward.setRedirectionKey(saveQuizathonAnsModel.getRedirectionKey());
            if (saveQuizathonAnsModel.getActivityTransId() != null && !saveQuizathonAnsModel.getActivityTransId().isEmpty()) {
                reward.setActivityTransId(Integer.parseInt(saveQuizathonAnsModel.getActivityTransId()));
            }

            if(quizathonModel != null  && quizathonModel.getRedirectionKey() != null
                    && quizathonModel.getRedirectionKey().equalsIgnoreCase("activityimageupload")
                    && quizathonModel.getActivityImageUploaded()){
                MessageInfoDialog messageInfoDialog=new MessageInfoDialog(QuizathonTimeboundScoreActivity.this,ContextCompat.getDrawable(context, R.drawable.ic_hu_info_red),"Information","You have already uploaded an image. You will receive your rewards once it is approved");
                messageInfoDialog.show();
                //Toast.makeText(context, "You have already uploaded an image. You will receive your rewards once it is approved", Toast.LENGTH_SHORT).show();
                return;
            }
            RewardInfoBottomSheet dialog = new RewardInfoBottomSheet(context, reward, "", new onRewardDialogClick() {
                @Override
                public void onStartClick() {
                    if (comingFrom != null && comingFrom.equalsIgnoreCase("feedback")) {
                        StartFeedbackActivity(reward.getActivityTransId());
                    } else {
                        StartQuizActivity(reward.getActivityTransId());
                    }
                    if (reward.getRedirectionKey().contains("feedback")) {
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedbackCallback(QuizathonTimeboundScoreActivity.this, feedbackResponseData, QuizathonTimeboundScoreActivity.this);
                    }
                }

                @Override
                public void onCompleteClick() {
                    if (reward.getRedirectionKey().contains("feedback")) {
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedbackCallback(QuizathonTimeboundScoreActivity.this, feedbackResponseData, QuizathonTimeboundScoreActivity.this);
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("journalUpload")) {
                        fileUploadKey = "JournalUpload";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            openGalleryOnly();
                        }
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("activityimageupload")) {
                        fileUploadKey = "ActivityUploads";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            openGalleryOnly();
                        }
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("water")) {
                        waterIntakeDialoge(QuizathonTimeboundScoreActivity.this, NewDashboardHelper.Companion.getWaterIntakeGoal(), NewDashboardHelper.Companion.getWaterIntake(), NewDashboardHelper.Companion.getWaterIntakeAllowed());
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("meditation")) {
                        showPopUpZenZone(QuizathonTimeboundScoreActivity.this, Double.parseDouble(NewDashboardHelper.Companion.getCurrentValueZenZone()));
                    } else {
                        RedirectionModel redirectionModel = new RedirectionModel(reward.getRedirectionKey(), false, "", "", "", "", "", "", 0, "", "", "", "", "");
                        RedirectionMethod.INSTANCE.redirectionScreen(redirectionModel, context);
                    }
                }

                @Override
                public void onCloseClick() {
                    //cancelDialog();
                }

                @Override
                public void onSubmit(String title) {
                    titleContent = title;
                    if (reward.getRedirectionKey().equalsIgnoreCase("journalUpload") || reward.getRedirectionKey().equalsIgnoreCase("activityimageupload")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                        }
                    }
                }
            });
            dialog.setComingFrom(comingFrom);
            dialog.setFromRewardList(true);
            dialog.show(getSupportFragmentManager(), "expandableBottomSheet");
        }
    }

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(QuizathonTimeboundScoreActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(QuizathonTimeboundScoreActivity.this, new String[]{permission}, requestCode);
        } else {
            openGalleryOnly();
        }
    }

    private void showRewardsPopupDialogBox() {
        if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0) {
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
                    break;
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
        }
    }

    private void StartQuizActivityForClaim(int ActivityTransId) {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);
            apiInterface.StartQuizActivity(SharedPref.getAuthToken(), new StartSpinActivityRequest(ActivityTransId)).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    private void StartQuizActivity(int ActivityTransId) {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);
            apiInterface.StartQuizActivity(SharedPref.getAuthToken(), new StartSpinActivityRequest(ActivityTransId)).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (reward.getRedirectionKey().equalsIgnoreCase("water")) {
                            waterIntakeDialoge(QuizathonTimeboundScoreActivity.this, NewDashboardHelper.Companion.getWaterIntakeGoal(), NewDashboardHelper.Companion.getWaterIntake(), NewDashboardHelper.Companion.getWaterIntakeAllowed());
                        } else if (reward.getRedirectionKey().equalsIgnoreCase("meditation")) {
                            showPopUpZenZone(QuizathonTimeboundScoreActivity.this, Double.parseDouble(NewDashboardHelper.Companion.getCurrentValueZenZone()));
                        } else {
                            RedirectionModel redirectionModel = new RedirectionModel(reward.getRedirectionKey(), false, "", "", "", "", "", "", 0, "", "", "", "", "");
                            RedirectionMethod.INSTANCE.redirectionScreen(redirectionModel, context);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    private void StartFeedbackActivity(int ActivityTransId) {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);
            apiInterface.StartFeedbackActivity(SharedPref.getAuthToken(), new StartSpinActivityRequest(ActivityTransId)).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (reward.getRedirectionKey().equalsIgnoreCase("water")) {
                            waterIntakeDialoge(QuizathonTimeboundScoreActivity.this, NewDashboardHelper.Companion.getWaterIntakeGoal(), NewDashboardHelper.Companion.getWaterIntake(), NewDashboardHelper.Companion.getWaterIntakeAllowed());
                        } else if (reward.getRedirectionKey().equalsIgnoreCase("meditation")) {
                            showPopUpZenZone(QuizathonTimeboundScoreActivity.this, Double.parseDouble(NewDashboardHelper.Companion.getCurrentValueZenZone()));
                        } else {
                            RedirectionModel redirectionModel = new RedirectionModel(reward.getRedirectionKey(), false, "", "", "", "", "", "", 0, "", "", "", "", "");
                            RedirectionMethod.INSTANCE.redirectionScreen(redirectionModel, context);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
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

        Log.d("stamps", rewards);

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


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogStamp.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
        });

        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));


        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((QuizScoreActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogStamp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
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
        });
        binding.scratchView.setScratchListener(QuizathonTimeboundScoreActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((NewDashboardActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusRewards.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 3000);

        String points = message.replaceAll("[^0-9]", "");
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
        });

        binding.scratchView.onFullReveal();

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(QuizathonTimeboundScoreActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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

        Log.d("stamp:", rewards);
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
            isPositiveBtn = true;
            alertDialogBonusStamp.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(QuizathonTimeboundScoreActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((QuizathonTimeboundScoreActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i > 20) {
            if (quizathonRewardData != null) {
                QuizRewardDialog.INSTANCE.QuizScratchCard(this, quizathonRewardData.getTransId(), true);
            } else {
                if (isStamp) {
                    isStamp = false;
                    scratchTokenReward();
                }

                scratchCardLayout.onFullReveal();

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

    public void setToolBar() {
        //binding.tvBack.setText(categoryName);
        binding.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.ivBack.setColorFilter(getResources().getColor(R.color.white));
        binding.llBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.yourScore.setText("" + yourScore);
        binding.totalScore.setText("/" + totalScore);
        if (!comingFrom.equalsIgnoreCase("feedback")) {
            if (quizathonModel.getShowConsent() != null && !quizathonModel.getShowConsent())
                binding.btnRequiz.setVisibility(View.VISIBLE);
            else {
                binding.btnRequiz.setVisibility(View.GONE);
            }
        } else {
            binding.btnRequiz.setVisibility(View.GONE);
        }
//        if (isRetakeAvailable && hasSufficientBalance) {
//            binding.btnRequiz.setVisibility(View.VISIBLE);
//        } else {
//            binding.btnRequiz.setVisibility(View.GONE);
//        }
        /*if(saveQuizathonAnsModel.getShowConsent() != null && saveQuizathonAnsModel.getShowConsent())
            binding.btnRequiz.setVisibility(View.GONE);
        else if(isRetakeAvailable){
            binding.btnRequiz.setVisibility(View.VISIBLE);
        }*/
        if (!showNextGameButton && !isRetakeAvailable) {
            binding.btnRequiz.setEnabled(false);
            binding.btnRequiz.setClickable(false);
            binding.btnRequiz.setAlpha(0.3f);
        }
        if (showNextGameButton) {
            binding.tvReQuiz.setText("Next Game");
        }
        binding.btnRequiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetakeQuizClicked();


            }
        });
    }

    private void nextButtonAction(int quizID) {
        try {
            CommonUtils.showProgressDialige(this);
            GetQuizathonDetailsRequest request = new GetQuizathonDetailsRequest(quizID);
            apiInterfaceWyh.getQuizthonDetails(SharedPref.getAuthToken(), request).enqueue(new Callback<GetQuizathonDetailsResponse>() {
                @Override
                public void onResponse(Call<GetQuizathonDetailsResponse> call, Response<GetQuizathonDetailsResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.code() == 200 && response.isSuccessful() && response.body() != null) {
                        // QuizathonModel quizathonModel = (QuizathonModel) item;
                        QuizathonModel quizathonModel = response.body().getData();
                        if (quizathonModel.getAnswerJson() != null && !quizathonModel.getAnswerJson().isEmpty() && quizathonModel.isQuizCompleted()) {
                            Intent intent = new Intent(context, QuizathonTimeboundScoreActivity.class);
                            intent.putExtra("data", quizathonModel.getAnswerJson());
                            intent.putExtra("your_score", quizathonModel.getUserScore());
                            intent.putExtra("total_score", quizathonModel.getTotalScore());
                            intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                            intent.putExtra("qid", quizathonModel.getQuizId());
                            intent.putExtra("comingFrom", "quiz");
                            intent.putExtra("rewardText", "");
                            intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                            intent.putExtra("isRetakeAvailable", quizathonModel.getRetakeAvailable());
                            intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                            intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                            intent.putExtra("hasSufficientBalance", quizathonModel.isHasSufficientBalance());
                            intent.putExtra("retakePointstoBurn", quizathonModel.getRetakePointstoBurn());
                            intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                            intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());
                            intent.putExtra("quizdata", quizathonModel.getModel());
                            intent.putExtra("withActivity", false);
                            intent.putExtra("quizdata", quizathonModel);
                            ;
                            context.startActivity(intent);
                        } else if (!quizathonModel.getIsUserRegistered() && quizathonModel.getRegistrationQuestions() != null && !quizathonModel.getRegistrationQuestions().isEmpty() && quizathonModel.getIsRegistrationRequired()) {
                            Intent intent = new Intent(context, QuizathonActivity.class);
                            intent.putExtra("data",
                                    (quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted())
                                            ? quizathonModel.getAnswerJson()
                                            : quizathonModel.getQuestionJson());
                            intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                            intent.putExtra("Category", quizathonModel.getQuizDesc());
                            intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                            intent.putExtra("comingFrom", "");
                            intent.putExtra("IsRetake", quizathonModel.getRetake());
                            intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                            intent.putExtra("qId", quizathonModel.getQuizId());
                            intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                            intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());
                            intent.putExtra("quizModel", quizathonModel);
                            RegistrationDialog dialog = new RegistrationDialog(QuizathonTimeboundScoreActivity.this, quizathonModel.getRegistrationQuestions(), quizathonModel.getRegTitle(), quizathonModel.getQuizId(), intent, quizathonModel.getQuizStarted(), quizathonModel.getQuizDesc());
                            if (quizathonModel.getRegIcon() != null && !quizathonModel.getRegIcon().isEmpty()) {
                                dialog.setIconUrl(quizathonModel.getRegIcon());
                            }
                            dialog.show();
                        } else if (!quizathonModel.getIsRegistrationRequired()) {
                            Intent intent = new Intent(context, QuizathonActivity.class);
                            intent.putExtra("data",
                                    (quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted())
                                            ? quizathonModel.getAnswerJson()
                                            : quizathonModel.getQuestionJson());
                            intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                            intent.putExtra("Category", quizathonModel.getQuizDesc());
                            intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                            intent.putExtra("comingFrom", "");
                            intent.putExtra("IsRetake", quizathonModel.getRetake());
                            intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                            intent.putExtra("qId", quizathonModel.getQuizId());
                            intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                            intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());
                            intent.putExtra("quizModel", quizathonModel);
                            context.startActivity(intent);

                        } else if (quizathonModel.getDialog_model() != null && quizathonModel.getDialog_model().getType() != null) {
                            QuizMessageDialog dialog = new QuizMessageDialog(QuizathonTimeboundScoreActivity.this, quizathonModel.getDialog_model(), apiInterfaceWyh);
                            dialog.show();
                        } else {
                            Intent intent = new Intent(context, QuizathonActivity.class);
                            intent.putExtra("data", quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted() ? quizathonModel.getAnswerJson() : quizathonModel.getQuestionJson());
                            intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                            intent.putExtra("Category", quizathonModel.getQuizDesc());
                            intent.putExtra("comingFrom", "");
                            intent.putExtra("IsRetake", quizathonModel.getRetake());
                            intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                            intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                            intent.putExtra("qId", quizathonModel.getQuizId());
                            intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                            intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());
                            intent.putExtra("quizModel", quizathonModel);
                            startActivity(intent);
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<GetQuizathonDetailsResponse> call, Throwable t) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getQuizathonRewardPopup(QuizathonRewardData data) {
        try {
            if (data.getRewardType() != null && !data.getRewardType().isEmpty()) {
                String rewardtype = data.getRewardType();
                if (rewardtype.equalsIgnoreCase("Points")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerPointsPopupCallBack(data.getRewardTitle(), context, "Trends", data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this, withActivity);
                } else if (rewardtype.equalsIgnoreCase("Offers")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerOfferCallBack(context, data.getPartnerLogo(), data.getRewardDescription(), data.getPartnerName()
                            , data.getPartnerUrl(), "Trends", data.getExpiryInHours(), data.getRewardTitle(), data.getRewardHeader1(), data.getRewardHeader2(), this, this, withActivity);
                } else if (rewardtype.equalsIgnoreCase("Voucher")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerVoucherCallBack(context, data.getRewardLogo(), data.getCouponCode(), data.getRewardDescription()
                            , data.getRewardTitle(), data.getExpiryInHours(), "Trends", data.getRewardHeader1(), data.getRewardHeader2(), this, this, withActivity);
                } else if (rewardtype.equalsIgnoreCase("Badge")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerBadgePopupCallBack(context, "Trends", data.getRewardLogo(), data.getRewardTitle(), data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this, withActivity);
                } else if (rewardtype.equalsIgnoreCase("Stamps")) {
                    QuizRewardDialog.INSTANCE.showStampsPopupCallBack(data.getRewardHeader1(), data.getRewardHeader2(), data.getRewardTitle(), data.getRewardValue(), this, this, this, withActivity);
                } else if (rewardtype.equalsIgnoreCase("future")) {
                    QuizRewardDialog.INSTANCE.showFutureRewardDialog(context, data.getDialogModel(), data.getClaimDate());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void getRetakeQuestion(String qid) {
        Call<RetakeResponseModel> call = null;

        call = apiInterfaceWyh.GetRetakeQuestion(SharedPref.getAuthToken(), new RetakeQuestionRequest(qid));
        call.enqueue(new Callback<RetakeResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<RetakeResponseModel> call, @NonNull Response<RetakeResponseModel> response) {

                if (response.body() != null && response.code() == 200) {
                    if (response.body().getDatamodel() != null && response.body().getDatamodel().getQuestionJson() != null) {
                        if (response.body().getDatamodel().getMessage() != null) {
                            DialogModel modelDialog = new DialogModel();
                            modelDialog.setButton_text("Close");
                            modelDialog.setTitle("information");
                            modelDialog.setType("NoReward");
                            modelDialog.setMessage_top(response.body().getDatamodel().getMessage());
                            QuizMessageDialog dialog = new QuizMessageDialog(QuizathonTimeboundScoreActivity.this, modelDialog, apiInterfaceWyh);
                            dialog.show();
                        } else {
                            retakeId = response.body().getDatamodel().getRetakeId();
                            Intent intent = new Intent(context, QuizathonActivity.class);
                            intent.putExtra("data", response.body().getDatamodel().getQuestionJson());
                            intent.putExtra("CategoryName", categoryName);
                            if (category != null && !category.isEmpty()) {
                                intent.putExtra("Category", category);
                            } else {
                                intent.putExtra("Category", categoryName);
                            }
                            intent.putExtra("comingFrom", comingFrom);
                            intent.putExtra("rewardDate", rewardDate);//As confirmed by farhan
                            intent.putExtra("IsRetake", true);
                            intent.putExtra("RetakeId", retakeId);
                            intent.putExtra("qId", response.body().getDatamodel().getQuizId());
                            intent.putExtra("quizModel", quizathonModel);
                            startActivity(intent);
                            finish();
                        }
                    } else if (response.body().getDatamodel() != null && response.body().getDatamodel().getMessage() != null) {
                        Toast.makeText(QuizathonTimeboundScoreActivity.this, response.body().getDatamodel().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RetakeResponseModel> call, Throwable t) {

                Toast.makeText(QuizathonTimeboundScoreActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void waterIntakeDialoge(Context context) {
        try {
            CustomYesNoDialog customYesNoDialog = new CustomYesNoDialog(context, R.style.Theme_Dialog);
            customYesNoDialog.show();
            customYesNoDialog.setCancelable(false);
            customYesNoDialog.binding.llActionRequired.setVisibility(View.GONE);
            customYesNoDialog.binding.txtInfoPopUpDesc.setText("You have exceeded the water intake limit, still, you have wanted to add water ?");
            customYesNoDialog.binding.btnYes.setText("Yes");
            customYesNoDialog.binding.btnCancel.setText("No");
            customYesNoDialog.binding.btnYes.setBackground(ContextCompat.getDrawable(context, R.drawable.blue_rc_bg_8dp));
            customYesNoDialog.binding.btnCancel.setBackground(ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp));
            customYesNoDialog.binding.btnYes.setOnClickListener(view -> {
                customYesNoDialog.dismiss();
                SharedPref.putWaterIntake(true);
            });

            customYesNoDialog.binding.btnCancel.setOnClickListener(view -> {
                customYesNoDialog.dismiss();
                SharedPref.putWaterIntake(false);
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void waterIntakeDialoge(
            Context context,
            int waterIntakeGoal,
            double waterIntake,
            boolean waterIntakeAllowed
    ) {
        try {
            builder = new AlertDialog.Builder(context);
            WarerIntakeLayoutBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.warer_intake_layout, null, false
            );

            builder.setView(binding.getRoot());
            alertDialog = builder.create();
            builder.setCancelable(true);

            binding.ivAdd.setOnClickListener(view -> {
                increaseNumber(
                        binding.tvGlassAmt.getText().toString(),
                        binding,
                        waterIntakeGoal,
                        waterIntake,
                        context
                );
            });

            binding.ivMinus.setOnClickListener(view -> {
                decreaseNumber(binding, context);
            });

            /* binding.btnViewMore.setOnClickListener(view -> {
                Intent intent = new Intent(context, TrendsActivity.class);
                intent.putExtra("activityType", Constants.WATER);
                context.startActivity(intent);
            }); */

            binding.btnClosed.setOnClickListener(view -> {
                SharedPref.putWaterIntake(false);
                alertDialog.dismiss();
            });

            binding.btnAddWater.setOnClickListener(view -> {
                int glasses = Integer.parseInt(binding.tvGlassAmt.getText().toString()) + (int) waterIntake;
                if (Integer.parseInt(binding.tvGlassAmt.getText().toString()) > 0) {
                    alertDialog.dismiss();
                    UploadData(Constants.WATER, glasses, context);
                } else {
                    Toast.makeText(context, "Please add water intake", Toast.LENGTH_SHORT).show();
                }
            });

            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }

            Rect displayRectangle = new Rect();
            Window window = getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow().setLayout(
                    (int) (displayRectangle.width() * 0.8f),
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    //Api Call
                }
            });
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    //Api Call
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void increaseNumber(String waterValue, WarerIntakeLayoutBinding binding, int waterIntakeGoal, double waterIntake, Context context) {
        glasses = (int) Double.parseDouble(waterValue);
        int actualGlasses = glasses + 1;
        int currentDayWaterCount = (int) (actualGlasses + waterIntake);
        if (actualGlasses > waterIntakeGoal || waterIntake > waterIntakeGoal || currentDayWaterCount > waterIntakeGoal) {
            if (!SharedPref.getWaterIntake()) {
                waterIntakeDialoge(context);
                return;
            } else {
                if (glasses < 30) {
                    display(glasses + 1, binding, context);
                }
            }
        }
        if (glasses < 30) {
            display(glasses + 1, binding, context);
        }
    }

    public void decreaseNumber(WarerIntakeLayoutBinding binding, Context context) {
        glasses = (int) Double.parseDouble(binding.tvGlassAmt.getText().toString());
        if (glasses > 0) {
            display(glasses - 1, binding, context);
        }
    }

    private void display(int i, WarerIntakeLayoutBinding binding, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.progressBar.setProgress(i, true);
        } else {
            binding.progressBar.setProgress(i);
        }
        binding.tvGlassAmt.setText(format.format(i));
        if (i >= 16) {
            binding.tvGlassAmt.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            binding.tvGlassAmt.setTextColor(context.getResources().getColor(R.color.dark_pink));
        }
    }

    public void showPopUpZenZone(Context context1, double currentTime) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context1);
        ZenZoneLayoutBinding zenZoneBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context1),
                R.layout.zen_zone_layout, null, false
        );
        alertBuilder.setView(zenZoneBinding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                alertDialog.dismiss();
            }
        });

        zenZoneBinding.btnTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zenZoneBinding.etMins.setText("10");
            }
        });

        zenZoneBinding.btnTwenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zenZoneBinding.etMins.setText("20");
            }
        });

        zenZoneBinding.btnForty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zenZoneBinding.etMins.setText("40");
            }
        });

        zenZoneBinding.btnClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        zenZoneBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!zenZoneBinding.etMins.getText().toString().isEmpty()) {
                    int meditationMinutes = Integer.parseInt(zenZoneBinding.etMins.getText().toString());
                    if (meditationMinutes > 0) {
                        alertDialog.dismiss();
                        UploadActivityData(Constants.MEDITATION, meditationMinutes + currentTime);
                    }else{
                        Toast.makeText(context1, "Please add time", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context1, "Please add time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        zenZoneBinding.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(context1, TrendsActivity.class);
                intent.putExtra("activityType", Constants.MEDITATION);
                context1.startActivity(intent);
            }
        });

        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }

        Rect displayRectangle = new Rect();
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(
                    (int) (displayRectangle.width() * 0.8f),
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
        }

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //Api Call
            }
        });
    }

    public void UploadActivityData(String activityName, double countOrTime) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        AddReminderDataRequest request = new AddReminderDataRequest(
                activityName,
                (int) Math.round(countOrTime),
                CommonUtils.todayDateInFormat("yyyy-MM-dd")
        );

        Call<CommonSuccessResponse> call = apiInterfaceWyh.addActivityData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(
                            context,
                            context.getClass().getName(),
                            context.getString(R.string.zen_zone_update_success)
                    );

                    if (checkIsFromQuizqathon()) {
                        //QuizReward Api Call
                        FetchQuizReward();
                        //getQuizathonRewardPopup(response.body().getQuizathonRewardData());
                    }

                    Toast.makeText(context, "Data Added Successfully!!!", Toast.LENGTH_SHORT).show();

                } else {
                    Analytics.logEvent(
                            context,
                            context.getClass().getName(),
                            context.getString(R.string.zen_zone_update_failed)
                    );
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                Analytics.logEvent(
                        context,
                        context.getClass().getName(),
                        context.getString(R.string.zen_zone_update_failed)
                );
            }
        });
    }


    public void cancelDialog() {
        try {
            if (reward != null && reward.getRewardType() != null && !reward.getRewardType().isEmpty()) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, reward.getRewardType(), this);
            } else if (quizathonRewardData != null && quizathonRewardData.getRewardType() != null && !quizathonRewardData.getRewardType().isEmpty()) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, quizathonRewardData.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogDismiss() {
        CommonUtils.dismissDialoge();
        //Api Call
        cancelDialog();
    }

    @Override
    public void onRewardClick(Object item, String type) {

    }

    @Override
    public void onRewardRecieved(AssignRewardsResponse.SpinRewardsData spinRewardsData) {

    }

    @Override
    public void onQuizRewardRecieved(QuizathonRewardData quizathonRewardData) {

    }


    public void UploadData(String activityName, double countOrTime, Context context) {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);

            // Create request object
            AddReminderDataRequest request = new AddReminderDataRequest(
                    activityName,
                    (int) Math.round(countOrTime),
                    CommonUtils.todayDateInFormat("yyyy-MM-dd")
            );

            // Initialize API call
            ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                    .create(ApiInterfaceWyh.class);
            Call<CommonSuccessResponse> call = apiInterfaceWyh.addActivityData(SharedPref.getAuthToken(), request);

            Log.d("AuthToken", new Gson().toJson(call.request()));

            call.enqueue(new Callback<CommonSuccessResponse>() {
                @Override
                public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                    CommonUtils.dismissDialoge();

                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        SharedPref.putWaterIntake(false);
                        Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.log_reminder_success));
                        NewDashboardHelper.Companion.setWaterIntake(countOrTime);
                        // Handle rewards & tokens if available
                        CommonSuccessResponse responseBody = response.body();
                        if (checkIsFromQuizqathon()) {
                            //QuizReward Api Call
                            FetchQuizReward();
                            //getQuizathonRewardPopup(responseBody.getQuizathonRewardData());
                        }
                        Toast.makeText(context, "Data Added Successfully!!!", Toast.LENGTH_SHORT).show();

                        Log.d("call", "upload data");

                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.log_reminder_failed));
                    }
                }

                @Override
                public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.log_reminder_failed));
                    Toast.makeText(context, "Failed to upload data. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("UploadData", "API Call Failed", t);
                }
            });

        } catch (Exception e) {
            CommonUtils.dismissDialoge();
            Log.e("UploadData", "Exception occurred", e);
        }
    }

    public void SaveQuizRegistration(
            String RegistrationID,
            String AnswerJson, Intent inte, boolean isQuizStarted) {
        CommonUtils.dismissDialoge();
        CommonUtils.showProgressDialige(context);
        SaveQuizRegistration request = new SaveQuizRegistration(RegistrationID, AnswerJson);
        Call<SaveQuizRegistrationResponse> call = apiInterfaceWyh.SaveQuizRegistration(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<SaveQuizRegistrationResponse>() {
            @Override
            public void onResponse(Call<SaveQuizRegistrationResponse> call, Response<SaveQuizRegistrationResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    CommonUtils.dismissDialoge();
                    try {
                        if (isQuizStarted) {
                            startActivity(inte);
                        } else {
                            QuizMessageDialog dialog = new QuizMessageDialog(QuizathonTimeboundScoreActivity.this,
                                    response.body().getQuizRegistrationData().getDialogModel(), apiInterfaceWyh);
                            dialog.show();

                        }
                    } catch (Exception ex) {
                        CommonUtils.dismissDialoge();
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SaveQuizRegistrationResponse> call, Throwable t) {
                CommonUtils.dismissDialoge();
                Toast.makeText(QuizathonTimeboundScoreActivity.this, "Something went wrong! Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) {
            try {
                if (data.getData() != null) {
                    Uri imageURI = data.getData();
                    saveImage(imageURI);
                } else {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri imageURI = item.getUri();
                        saveImage(imageURI);
                    }
                }
                String currentTime = CommonUtils.todayDate();
                if (fileUploadKey.equalsIgnoreCase("JournalUpload")) {
                    addDiary(currentTime, titleContent, titleContent);
                } else {
                    String title=comingFrom.equalsIgnoreCase("quiz") ? "Quiz":"Feedback";
                    uploadFileWithContent(title, titleContent, quizathonModel.getTransId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            fileDataList.clear();
            parts.clear();

            List<Image> image2 = ImagePicker.getImages(data);
            FileData fileData = new FileData();
            fileData.setPath(getFilePathFromImage(image2.get(0)));
            long singleFileSize = getFileSizeFromPath(fileData.getPath());
            File imgFile = new File(String.valueOf(fileData.getPath()));
            FileData fileDataNew = new FileData();

            if (singleFileSize > 1000000) {
                File file1 = CommonUtils.compressImage(context, image2.get(0).getUri());
                Log.d("comparedata singleFileSize", singleFileSize + "");
                fileDataNew.setPath(file1.getPath());
                fileDataNew.setMimeType("application/png");
                imageName = file1.getName();
                if (file1.length() > 5000000) {
                    fileDataList.clear();
                    parts.clear();
                    Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                } else {
                    fileDataList.add(fileDataNew);
                }
            } else {
                fileDataList.add(fileData);
            }

            if (fileDataList.size() > 0) {

                parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                String currentTime = CommonUtils.todayDate();
                if (fileUploadKey.equalsIgnoreCase("JournalUpload")) {
                    addDiary(currentTime, titleContent, titleContent);
                } else {
                    String title=comingFrom.equalsIgnoreCase("quiz") ? "Quiz":"Feedback";
                    uploadFileWithContent(title, titleContent, quizathonModel.getTransId());
                }
            }

        }

    }


    public void saveImage(Uri imageURI) {
        //List<MultipartBody.Part> parts = new ArrayList<>();

        FileData fileData = new FileData();
        fileData.setPath(CommonUtils.getRealPathFromURI(imageURI, context));
        FileData fileDataNew = new FileData();
        File file1 = CommonUtils.compressImageToJPEG(context, imageURI);
        fileDataNew.setPath(file1.getPath());
        fileDataNew.setMimeType("application/png");
        imageName = file1.getName();
        if (file1.length() > 5000000) {
            Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
        } else {
            fileDataList.add(fileDataNew);
        }


        if (fileDataList.size() > 0) {

            parts.clear();
            parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }

    private MultipartBody.Part prepareFilePart(String partName, String path) {
        try {
            File file = new File(path);
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        } catch (Exception e) {
            Log.d("call", "error");
        }

        return null;
    }

    private String getFilePathFromImage(Image image) {
        if (image == null)
            return null;
        if (!TextUtils.isEmpty(image.getPath()) && new File(image.getPath()).exists()) {
            return image.getPath();
        } else if (image.getUri() != null) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(image.getUri(),
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgFilePath = null;
            if (columnIndex != -1) {
                imgFilePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return imgFilePath;
        }
        return null;
    }

    private long getFileSizeFromPath(String filePath) {
        if (filePath == null)
            return 0;
        File file = new File(filePath);
        return file.length();
    }

    public void openGalleryOnly() {
        ImagePicker.create(this)
                .includeVideo(false).multi()
                .imageDirectory("Camera")
                .limit(1)
                .enableLog(true)
                .showCamera(false)
                .start();
    }

    public void openImagePicker() {
        try {
            Intent intent = new Intent();
            intent.putExtra(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openCameraOnly() throws IOException {
        ImagePicker.cameraOnly().imageDirectory("Camera").start(this);
    }

    private void addDiary(String date, String title, String content) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();


        AddDiaryRequest request = new AddDiaryRequest(date, title, content);
        Call<AddDiaryResponse> call = apiInterfaceWyh.addDiary(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<AddDiaryResponse>() {
            @Override
            public void onResponse(Call<AddDiaryResponse> call, Response<AddDiaryResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                AddDiaryResponse res = response.body();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.add_diary_success));

                    Toast.makeText(context, "Journal added successfully.", Toast.LENGTH_SHORT).show();
                    if (response.body().getData() > 0) {
                        if (parts.size() > 0) {
                            uploadFileWithContent(parts, response.body().getData());
                        }


                    }
                    if (checkIsFromQuizqathon()) {
                        //QuizReward Api Call
                        FetchQuizReward();
                    }

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.add_diary_failed));
                    Toast.makeText(context, "API" + getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddDiaryResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.add_diary_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileWithContent(List<MultipartBody.Part> parts, int diaryId) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
                .addFormDataPart("DiaryId", String.valueOf(diaryId))
                .build();

        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "UserJournal/UploadFiles")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        Log.d("new request", new Gson().toJson(request));

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                fileDataList.clear();
                deleteImageFile();
                Log.d("upload file", e.getMessage());
//                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();


                String res = response.body().string();
                Log.d("upload file", res);
                if (response.code() == 200 && response.body() != null) {
                    fileDataList.clear();
                    deleteImageFile();
                    UploadUserFileResponse uploadUserFileResponse = new Gson().fromJson(res, UploadUserFileResponse.class);
                    if (uploadUserFileResponse.isSuccess()) {

                    }
                }
            }
        });
    }


    private void uploadFileWithContent(String actvityEvent, String actvityDescription, int transId) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();

        RequestBody eventBody = RequestBody.create(MediaType.parse("multipart/form-data"), actvityEvent);
        RequestBody descBody = RequestBody.create(MediaType.parse("multipart/form-data"), actvityDescription);
        RequestBody transIdBody = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(transId));

//        Call<CommonResponseWyh> call = apiInterface.UploadActivityImage(SharedPref.getAuthToken(),eventBody,descBody,transIdBody,parts.get(0));
//
//        call.enqueue(new Callback<CommonResponseWyh>() {
//            @Override
//            public void onResponse(Call<CommonResponseWyh> call, Response<CommonResponseWyh> response) {
////                if (progressDialog != null && progressDialog.isShowing())
////                    progressDialog.dismiss();
//
//                if (response.code() == 200 && response.body() != null) {
//                    fileDataList.clear();
//                    deleteImageFile();
//                    if (checkIsFromQuizqathon()) {
//                        FetchQuizReward();
//                    }
//                }else{
//                    response.errorBody();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CommonResponseWyh> call, Throwable t) {
//                if (progressDialog != null && progressDialog.isShowing())
//                    progressDialog.dismiss();
//                fileDataList.clear();
//                deleteImageFile();
//                if (checkIsFromQuizqathon()) {
//                    FetchQuizReward();
//                }
//                Log.d("upload file", t.getMessage());
//            }
//        });



        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
                .addFormDataPart("ActivityEvent",null, eventBody)
                .addFormDataPart("ActivityDescription", "",descBody)
                .addFormDataPart("TransId", null,transIdBody)
                .build();

        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "HealthQuiz/UploadActivityImage")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        Log.d("new request", new Gson().toJson(request));

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                fileDataList.clear();
                deleteImageFile();

//                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();


                String res = response.body().string();
                Log.d("upload file", res);
                if (response.code() == 200 && response.body() != null) {
                    fileDataList.clear();
                    deleteImageFile();

                }
            }
        });
    }

    public void deleteImageFile() {
        if (imageName != "") {
            CommonUtils.deleteImage(imageName);
            imageName = "";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NewDashboardHelper.Companion.setTrasactionId(null);
        NewDashboardHelper.Companion.setFeatureName(null);
        NewDashboardHelper.Companion.setActivityName(null);
    }


    @Override
    public void onRetakeQuizClicked() {
        if (showNextGameButton) {
            if (nextGameRedirectTo != null && !nextGameRedirectTo.isEmpty()) {
                if (nextGameRedirectTo.contains("_")) {
                    nextButtonAction(Integer.parseInt(qid));
                } else if (nextGameRedirectTo.equalsIgnoreCase("play&win")) {
                    Intent intent = new Intent(QuizathonTimeboundScoreActivity.this, PlayAndWinActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (nextGameRedirectTo.equalsIgnoreCase("quizathon")) {
                    Intent intent = new Intent(QuizathonTimeboundScoreActivity.this, QuizathonViewAllActivity.class);
                    intent.putExtra("type", type);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(QuizathonTimeboundScoreActivity.this, PlayAndWinActivity.class);
                startActivity(intent);
            }

        } else {
            if (isRetakeAvailable) {
                if (quizathonModel.getShowConsent() != null && quizathonModel.getShowConsent()) {
                    getRetakeQuestion(qid);
                } else {
                    String title = "Requiz";
                    String message = "Got an answer wrong? No worries!\n" +
                            "You can retake the quiz using your HappyYou points";
                    if (retakePointstoBurn == 0) {
                        message = "No worries! You can take another quiz";
                    }
                    RetakeAlertDialog retakeAlertDialog = new RetakeAlertDialog(QuizathonTimeboundScoreActivity.this, retakePointstoBurn, ContextCompat.getDrawable(QuizathonTimeboundScoreActivity.this, R.drawable.ic_banner_quiz), title, message, (position, name) -> {
                        if (name.equalsIgnoreCase("Close")) {

                        } else if (name.equalsIgnoreCase("Retake")) {
                            getRetakeQuestion(qid);
                        }
                    });
                    retakeAlertDialog.show();
                }
            } else {
                       /* DialogModel modelDialog = new DialogModel();
                        modelDialog.setButton_text("Close");
                        modelDialog.setTitle("information");
                        modelDialog.setType("NoReward");
                        modelDialog.setMessage_top("You have exhausted your number of retakes.");
                        QuizMessageDialog dialog = new QuizMessageDialog(QuizathonScoreActivity.this, modelDialog);
                        dialog.show();*/
                Intent intent = new Intent(QuizathonTimeboundScoreActivity.this, PlayAndWinActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClaimClicked() {
        if (withActivity) {
            if (saveQuizathonAnsModel.getActivityTransId() != null) {
              /* if (comingFrom != null && comingFrom.equalsIgnoreCase("feedback")) {
                    StartFeedbackActivity(Integer.parseInt(saveQuizathonAnsModel.getActivityTransId()));
                } else {
                    StartQuizActivityForClaim(Integer.parseInt(saveQuizathonAnsModel.getActivityTransId()));
                }*/
                StartQuizActivityForClaim(Integer.parseInt(saveQuizathonAnsModel.getActivityTransId()));
                if(saveQuizathonAnsModel != null){
                    saveQuizathonAnsModel.setRewardDescription(null);
                }
                showBottomSheet();
            }

        } else {
            if (saveQuizathonAnsModel.getActivityTransId() != null) {
                FetchQuizReward(String.valueOf(saveQuizathonAnsModel.getActivityTransId()), "Quiz");
            }

        }
    }
}