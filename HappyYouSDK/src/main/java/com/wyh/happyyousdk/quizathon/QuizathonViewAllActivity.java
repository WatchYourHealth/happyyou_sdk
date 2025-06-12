package com.wyh.happyyousdk.quizathon;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

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
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.ActivityClaimReclaimList;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.Activities.spinwheelreward.SpinWheelRewardsActivity;
import com.wyh.happyyousdk.SpinWheel.Utilities.MessageInfoDialog;
import com.wyh.happyyousdk.SpinWheel.Utilities.RewardInfoBottomSheet;
import com.wyh.happyyousdk.SpinWheel.Utilities.onRewardDialogClick;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.SpinWheel.spinRewardCallBack;
import com.wyh.happyyousdk.common.adapter.ClaimReClaimAdapter;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.RedirectionMethod;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityQuizathonViewAllBinding;
import com.wyh.happyyousdk.databinding.WarerIntakeLayoutBinding;
import com.wyh.happyyousdk.databinding.ZenZoneLayoutBinding;
import com.wyh.happyyousdk.fileshare.ActivityFileShareList;
import com.wyh.happyyousdk.model.BurnRegPointsReq;
import com.wyh.happyyousdk.model.BurnRegPointsResp;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.RedirectionModel;
import com.wyh.happyyousdk.model.request.GetQuestionRequest;
import com.wyh.happyyousdk.model.request.SaveQuizRegistration;
import com.wyh.happyyousdk.model.request.diary.AddDiaryRequest;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.model.request.postloginreward.StartSpinActivityRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.request.trends.AddReminderDataRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;
import com.wyh.happyyousdk.model.response.GetQuizQuestions;
import com.wyh.happyyousdk.model.response.GetQuizResponseModel;
import com.wyh.happyyousdk.model.response.diary.AddDiaryResponse;
import com.wyh.happyyousdk.model.response.diary.UploadUserFileResponse;
import com.wyh.happyyousdk.model.response.playwin.ClaimReClaimRewardModel;
import com.wyh.happyyousdk.model.response.playwin.QuizFeedbackModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonResponseModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.model.response.playwin.SaveQuizRegistrationResponse;
import com.wyh.happyyousdk.model.response.postloginreward.CommonResponse;
import com.wyh.happyyousdk.model.response.postloginreward.EarnedRewardResponse;
import com.wyh.happyyousdk.model.response.postloginreward.GetActivityRewardsClaimListResp;
import com.wyh.happyyousdk.model.response.postloginreward.RewardItem;
import com.wyh.happyyousdk.model.response.quiz.TriviaHistoryData;
import com.wyh.happyyousdk.model.response.quiz.TriviaHistoryResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.play_and_win.PlayAndWinActivity;
import com.wyh.happyyousdk.play_and_win.adapter.PlayWinCardViewAdapter;
import com.wyh.happyyousdk.quiz.QuizActivity;
import com.wyh.happyyousdk.quiz.QuizHistoryActivity;
import com.wyh.happyyousdk.quiz.adapter.QuizOptionAdapter;
import com.wyh.happyyousdk.quizathon.adapter.QuizRewardViewAdapter;
import com.wyh.happyyousdk.quizathon.adapter.QuizathonGridCardsAdapterNew;
import com.wyh.happyyousdk.quizathon.dialog.QuizMessageDialog;
import com.wyh.happyyousdk.quizathon.dialog.RetakeAlertDialog;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyh.happyyousdk.utils.dialog.RegistrationDialog;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

public class QuizathonViewAllActivity extends AppCompatActivity implements onRewardClick, spinRewardCallBack, ScratchListener, rewardDialogCloseListener {
    ActivityQuizathonViewAllBinding binding;
    List<QuizFeedbackModel> quizFeedbackModels = new ArrayList<>();
    List<QuizathonModel> quizathonModelList = new ArrayList<>();
    List<GetQuizQuestions> quizQuestionsList = new ArrayList<>();
    String type, name, quiz_cat;
    ApiInterfaceWyh apiInterfaceWyh;
    APIInterface apiInterface;
    boolean isAssigned = false;
    String qId = "";
    RewardItem reward;
    FeedbackResponseData feedbackResponseData = null;
    String titleContent = "";
    private static final int CAMERA_PERMISSION_CODE = 100;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    int glasses;
    DecimalFormat format = new DecimalFormat("0.##");
    QuizathonRewardData quizreward;
    ProgressDialog progressDialog;
    Context context;
    String comingFrom = "Quiz";
    QuizathonModel quizathonModelPointBurn;
    QuizathonModel quizathonModelRegister;
    String burnType = "";

    ArrayList<FileData> fileDataList = new ArrayList<>();
    List<MultipartBody.Part> parts = new ArrayList<>();
    String imageName = "";
    String fileUploadKey = "JournalUpload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quizathon_view_all);
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(QuizathonViewAllActivity.this)).create(ApiInterfaceWyh.class);
        apiInterface = RetrofitHandler.apiInterface();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(this, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        type = getIntent().getStringExtra("type");
        quiz_cat = getIntent().getStringExtra("quiz_cat");
        name = getIntent().getStringExtra("name");

        if(quiz_cat == null){
            quiz_cat="All";
        }

        binding.tvTitle.setText(name != null && !name.isEmpty() ? name : "Quiz");
        if (type.equalsIgnoreCase("quizathon"))
        {
            binding.llBottom.setVisibility(View.VISIBLE);
        }
        else {
            binding.llBottom.setVisibility(View.GONE);
        }
        binding.ivBackPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(QuizathonViewAllActivity.this, QuizClaimActivityRewardList.class);
                startActivity(in);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //quizathonModelPointBurn=null;
        //quizathonModelRegister=null;
        if (type != null) {
            if (type.equalsIgnoreCase("feedback")) {
                GetActiveQuizathon();
            } else if (type.equalsIgnoreCase("quizathon")) {
                GetActiveQuizathon();
            } else if (type.equalsIgnoreCase("quiz")) {
                getQuestionData(quiz_cat);
            }else if (type.equalsIgnoreCase("trivia")) {
                getQuestionData("trivia");
            } else if (type.equalsIgnoreCase("Active")) {
                GetQuizActivityList();
            } else if (type.equalsIgnoreCase("Redeemed")) {
                GetQuizActivityList();
            } else if (type.equalsIgnoreCase("Expired")) {
                GetQuizActivityList();
            } else if (type.equalsIgnoreCase("RewardActive")) {
                GetFeedbackActivityListReward();
            } else if (type.equalsIgnoreCase("RewardRedeemed")) {
                GetFeedbackActivityListReward();
            } else if (type.equalsIgnoreCase("RewardExpired")) {
                GetFeedbackActivityListReward();
            } else if (type.equalsIgnoreCase("AllActiveClaim")) {
                GetActivityRewardsClaimList();
            }
        }
    }

    public void getTriviaData() {
        Call<TriviaHistoryResponse> call = null;

        call = apiInterfaceWyh.GetTriviaHistory(SharedPref.getAuthToken());
        call.enqueue(new Callback<TriviaHistoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<TriviaHistoryResponse> call, @NonNull Response<TriviaHistoryResponse> response) {

                if (response.body() != null && response.code() == 200) {
                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        TriviaHistoryData triviaHistoryData = response.body().getData().get(0);
                        Intent intent = new Intent(QuizathonViewAllActivity.this, QuizathonScoreActivity.class);
                        intent.putExtra("data", triviaHistoryData.getAnswerJson());
                        intent.putExtra("your_score", triviaHistoryData.getTriviaScore());
                        intent.putExtra("total_score", triviaHistoryData.getTriviaTotalScore());
                        intent.putExtra("CategoryName", triviaHistoryData.getCategory());
                        intent.putExtra("type", type);
                        intent.putExtra("comingFrom", "quiz");
                        intent.putExtra("rewardText", "");

                        startActivity(intent);
                    } else {
                        startActivity(new Intent(QuizathonViewAllActivity.this, QuizHistoryActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<TriviaHistoryResponse> call, Throwable t) {

                Toast.makeText(QuizathonViewAllActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetQuizActivityList() {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(QuizathonViewAllActivity.this);
            apiInterface.GetQuizActivityList(SharedPref.getAuthToken()).enqueue(new Callback<EarnedRewardResponse>() {
                @Override
                public void onResponse(Call<EarnedRewardResponse> call, Response<EarnedRewardResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getData() != null) {
                            QuizRewardViewAdapter adapter = null;
                            List<RewardItem> itemList = new ArrayList<>();
                            feedbackResponseData = response.body().getFeedbackDetails();
                            if (type.equalsIgnoreCase("Active")) {
                                if (response.body().getData() != null && response.body().getData().getActiveList() != null) {
                                    itemList = response.body().getData().getActiveList();
                                    binding.gvCardPalywin.setNumColumns(3);
                                } else {
                                    itemList = new ArrayList<>();
                                }
                            } else if (type.equalsIgnoreCase("Redeemed")) {
                                if (response.body().getData() != null && response.body().getData().getCompletedList() != null) {
                                    itemList = response.body().getData().getCompletedList();
                                    binding.gvCardPalywin.setNumColumns(3);
                                } else {
                                    itemList = new ArrayList<>();
                                }
                            } else if (type.equalsIgnoreCase("Expired")) {
                                if (response.body().getData() != null && response.body().getData().getExpiredList() != null) {
                                    itemList = response.body().getData().getExpiredList();
                                    binding.gvCardPalywin.setNumColumns(3);
                                } else {
                                    itemList = new ArrayList<>();
                                }
                            }
                            adapter = new QuizRewardViewAdapter(itemList, QuizathonViewAllActivity.this, type, QuizathonViewAllActivity.this);
                            binding.gvCardPalywin.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<EarnedRewardResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    private void GetFeedbackActivityListReward() {
        try {
            comingFrom = "Feedback";
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(QuizathonViewAllActivity.this);
            apiInterface.GetFeedbackActivityList(SharedPref.getAuthToken()).enqueue(new Callback<EarnedRewardResponse>() {
                @Override
                public void onResponse(Call<EarnedRewardResponse> call, Response<EarnedRewardResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getData() != null) {
                            QuizRewardViewAdapter adapter = null;
                            List<RewardItem> itemList = new ArrayList<>();
                            feedbackResponseData = response.body().getFeedbackDetails();
                            if (type.equalsIgnoreCase("RewardActive")) {
                                if (response.body().getData() != null && response.body().getData().getActiveList() != null) {
                                    itemList = response.body().getData().getActiveList();
                                    binding.gvCardPalywin.setNumColumns(3);
                                } else {
                                    itemList = new ArrayList<>();
                                }
                            } else if (type.equalsIgnoreCase("RewardRedeemed")) {
                                if (response.body().getData() != null && response.body().getData().getCompletedList() != null) {
                                    itemList = response.body().getData().getCompletedList();
                                    binding.gvCardPalywin.setNumColumns(3);
                                } else {
                                    itemList = new ArrayList<>();
                                }
                            } else if (type.equalsIgnoreCase("RewardExpired")) {
                                if (response.body().getData() != null && response.body().getData().getExpiredList() != null) {
                                    itemList = response.body().getData().getExpiredList();
                                    binding.gvCardPalywin.setNumColumns(3);
                                } else {
                                    itemList = new ArrayList<>();
                                }
                            }
                            adapter = new QuizRewardViewAdapter(itemList, QuizathonViewAllActivity.this, type, QuizathonViewAllActivity.this);
                            binding.gvCardPalywin.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<EarnedRewardResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    private void GetActiveQuizathon() {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(QuizathonViewAllActivity.this);
            apiInterface.GetActiveQuizathon(SharedPref.getAuthToken()).enqueue(new Callback<QuizathonResponseModel>() {
                @Override
                public void onResponse(Call<QuizathonResponseModel> call, Response<QuizathonResponseModel> response) {
                    CommonUtils.dismissDialoge();
                    Log.d("Assign Response", new Gson().toJson(response.body()));
                    if (response.body() != null && response.code() == 200 && response.body().getQuizathonData() != null && response.body().getQuizathonData().getQuizathonModels() != null) {
                        if (type.equalsIgnoreCase("feedback")) {
                            quizFeedbackModels = response.body().getQuizathonData().getQuizFeedbackModels();
                        } else {
                            quizathonModelList = response.body().getQuizathonData().getQuizathonModels();
                            if (response.body().getQuizathonData() != null &&  response.body().getQuizathonData().getQuizathonStreakModelList() != null && response.body().getQuizathonData().getQuizathonStreakModelList().size() > 0) {
                                quizathonModelList.addAll(response.body().getQuizathonData().getQuizathonStreakModelList());
                            }
                            if (quizathonModelPointBurn != null && Objects.equals(burnType, "")) {
                                callRegisterAfterPointBurn(quizathonModelPointBurn);
                            }
                            if (quizathonModelRegister != null && Objects.equals(burnType, "")) {
                                callQuizAfterPointBurn(quizathonModelRegister);
                            }
                        }
                        PlayWinCardViewAdapter adapter = new PlayWinCardViewAdapter(quizFeedbackModels, quizathonModelList, quizQuestionsList, QuizathonViewAllActivity.this, type, new onRewardClick() {
                            @Override
                            public void onRewardClick(Object item, String type) {
                                if (item instanceof GetQuizQuestions) {
                                    try {
                                        GetQuizQuestions quizCategory = (GetQuizQuestions) item;
                                        if (quizCategory.getCategoryName().equalsIgnoreCase("Trivia") && !isAssigned) {
                                            getTriviaData();
                                        } else {
                                            Intent intent = new Intent(QuizathonViewAllActivity.this, QuizActivity.class);
                                            intent.putExtra("data", new Gson().toJson(quizCategory.getQuestions()));
                                            intent.putExtra("CategoryName", quizCategory.getCategoryName());
                                            intent.putExtra("Category", quizCategory.getCategory());
                                            intent.putExtra("comingFrom", "");
                                            intent.putExtra("qId", qId);
                                            startActivity(intent);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (item instanceof QuizathonModel) {
                                    try {
                                        QuizathonModel quizathonModel = (QuizathonModel) item;
                                        if (type.equalsIgnoreCase("Spin")) {
                                            startActivity(new Intent(QuizathonViewAllActivity.this, SpinWheelRewardsActivity.class));
                                        } else if (type.equalsIgnoreCase("Others")) {
                                            Intent in = new Intent(QuizathonViewAllActivity.this, ActivityFileShareList.class);
                                            startActivity(in);
                                        } else {
                                            if (quizathonModel.getAnswerJson() != null && !quizathonModel.getAnswerJson().isEmpty() && quizathonModel.isQuizCompleted()) {
                                                Intent intent = new Intent(QuizathonViewAllActivity.this, QuizathonScoreActivity.class);
                                                if (quizathonModel.getQuizType() != null && !quizathonModel.getQuizType().isEmpty() && quizathonModel.getQuizType().equalsIgnoreCase("Streak")) {
                                                    intent = new Intent(QuizathonViewAllActivity.this, QuizathonTimeboundScoreActivity.class);
                                                }
                                                intent.putExtra("data", quizathonModel.getAnswerJson());
                                                intent.putExtra("your_score", quizathonModel.getUserScore());
                                                intent.putExtra("total_score", quizathonModel.getTotalScore());
                                                intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                                                intent.putExtra("Category", quizathonModel.getQuizDesc());
                                                intent.putExtra("qid", quizathonModel.getQuizId());
                                                intent.putExtra("comingFrom", "quiz");
                                                intent.putExtra("rewardText", "");
                                                intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                                                intent.putExtra("isRetakeAvailable", quizathonModel.getRetakeAvailable());
                                                intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                                                intent.putExtra("hasSufficientBalance", quizathonModel.isHasSufficientBalance());
                                                intent.putExtra("retakePointstoBurn", quizathonModel.getRetakePointstoBurn());
                                                intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                                                intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());
                                                intent.putExtra("quizdata", quizathonModel.getModel());
                                                intent.putExtra("withActivity", quizathonModel.getShowActivity());
                                                intent.putExtra("type", type);
                                                intent.putExtra("quizModel", quizathonModel);

                                                startActivity(intent);
                                            } else if (!quizathonModel.getIsUserRegistered() && quizathonModel.getRegistrationQuestions() != null && !quizathonModel.getRegistrationQuestions().isEmpty() && quizathonModel.getIsRegistrationRequired() && !quizathonModel.isRegistrationAllowed() && quizathonModel.getRegistrationPointBurn() > 0) {
                                                burnType = "QuizRegistration";
                                                quizathonModelPointBurn = quizathonModel;
                                                showBurnQuizDialog(quizathonModel.getRegistrationPointBurn(), quizathonModel.getBurnRegTitle(), quizathonModel.getBurnRegIcon(), quizathonModel.getBurnRegMessage(), quizathonModel.getQuizId());
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
                                                intent.putExtra("type", type);
                                                intent.putExtra("quizModel", quizathonModel);
                                                RegistrationDialog dialog = new RegistrationDialog(QuizathonViewAllActivity.this, quizathonModel.getRegistrationQuestions(), quizathonModel.getRegTitle(), quizathonModel.getQuizId(), intent, quizathonModel.getQuizStarted(), quizathonModel.getQuizDesc());
                                                if (quizathonModel.getRegIcon() != null && !quizathonModel.getRegIcon().isEmpty()) {
                                                    dialog.setIconUrl(quizathonModel.getRegIcon());
                                                }
                                                dialog.show();
                                            } else if (!quizathonModel.getIsRegistrationRequired()) {
                                                if (quizathonModel.getQuizBurnPoints() != null && Integer.parseInt(quizathonModel.getQuizBurnPoints())>0)
                                                {
                                                    burnType="QuizEntry";
                                                    quizathonModelRegister=quizathonModel;
                                                    showBurnQuizDialog(Integer.parseInt(quizathonModel.getQuizBurnPoints()), "Quiz", quizathonModel.getBurnRegIcon(), "You can participate in the quiz using "+quizathonModel.getQuizBurnPoints()+" HappyYou points", quizathonModel.getQuizId());
                                                }
                                                else {
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
                                                    intent.putExtra("type", type);
                                                    intent.putExtra("quizModel", quizathonModel);
                                                    context.startActivity(intent);
                                                }

                                            } else if (quizathonModel.getDialog_model() != null && quizathonModel.getDialog_model().getType() != null) {
                                                QuizMessageDialog dialog = new QuizMessageDialog(QuizathonViewAllActivity.this, quizathonModel.getDialog_model(), apiInterfaceWyh);
                                                dialog.show();
                                            } else {
                                                Intent intent = new Intent(QuizathonViewAllActivity.this, QuizathonActivity.class);
                                                intent.putExtra("data", quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted() ? quizathonModel.getAnswerJson() : quizathonModel.getQuestionJson());
                                                intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                                                intent.putExtra("Category", quizathonModel.getQuizDesc());
                                                intent.putExtra("comingFrom", "");
                                                intent.putExtra("IsRetake", quizathonModel.getRetake());
                                                intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                                                intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                                                intent.putExtra("qId", quizathonModel.getQuizId());
                                                intent.putExtra("type", type);
                                                intent.putExtra("quizModel", quizathonModel);
                                                startActivity(intent);
                                            }
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (item instanceof QuizFeedbackModel) {
                                    try {
                                        QuizFeedbackModel quizFeedbackModel = (QuizFeedbackModel) item;

                                        if (quizFeedbackModel.getFeedbackAnswerJson() != null && !quizFeedbackModel.getFeedbackAnswerJson().isEmpty() && quizFeedbackModel.getIsFeedbackTaken() && quizFeedbackModel.isShowActivity()) {

                                            Intent intent = new Intent(QuizathonViewAllActivity.this, QuizathonScoreActivity.class);
                                            intent.putExtra("data", quizFeedbackModel.getFeedbackAnswerJson());
                                            intent.putExtra("your_score", 0);
                                            intent.putExtra("total_score", 0);
                                            intent.putExtra("CategoryName", quizFeedbackModel.getFeedbackDesc());
                                            intent.putExtra("Category", quizFeedbackModel.getFeedbackDesc());
                                            intent.putExtra("type", type);
                                            intent.putExtra("rewardText", "");
                                            intent.putExtra("quizdata", quizFeedbackModel.getModel());
                                            intent.putExtra("withActivity", quizFeedbackModel.isShowActivity());
                                            intent.putExtra("comingFrom", "Feedback");

                                            startActivity(intent);


                                        } else {

                                            Intent intent = new Intent(QuizathonViewAllActivity.this, QuizathonActivity.class);
                                            intent.putExtra("data", quizFeedbackModel.getFeedbackQuestionJson());
                                            intent.putExtra("CategoryName", quizFeedbackModel.getFeedbackDesc());
                                            intent.putExtra("Category", quizFeedbackModel.getFeedbackDesc());
                                            intent.putExtra("comingFrom", "Feedback");
                                            intent.putExtra("IsRetake", false);
                                            intent.putExtra("RetakeId", 0);
                                            intent.putExtra("rewardDate", "");
                                            intent.putExtra("qId", quizFeedbackModel.getFeedbackId());
                                            intent.putExtra("type", type);
                                            startActivity(intent);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }else if(item instanceof ClaimReClaimRewardModel){
                                    ClaimReClaimRewardModel claimReClaimRewardModel = (ClaimReClaimRewardModel) item;
                                    reward = claimReClaimRewardModel.getRewardItem();
                                    type = "Active";
                                    RewardInfoBottomSheet dialog = new RewardInfoBottomSheet(QuizathonViewAllActivity.this, reward, type, new onRewardDialogClick() {
                                        @Override
                                        public void onStartClick() {

                                            StartSpinActivity(reward.getActivityTransId());

                                            if (reward.getRedirectionKey().contains("feedback")) {
                                                FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                                                instance.showPopUpFeedbackCallback(QuizathonViewAllActivity.this, feedbackResponseData, QuizathonViewAllActivity.this);
                                            }
                                        }

                                        @Override
                                        public void onCompleteClick() {
                                            if (reward.getRedirectionKey().contains("feedback")) {
                                                FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                                                instance.showPopUpFeedbackCallback(QuizathonViewAllActivity.this, feedbackResponseData, QuizathonViewAllActivity.this);
                                            } else if (reward.getRedirectionKey().equalsIgnoreCase("journalUpload")) {
                                                fileUploadKey = "JournalUpload";
                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                                    openImagePicker();
                                                } else {
                                                    openGalleryOnly();
                                                }
                                            } else if (reward.getRedirectionKey().equalsIgnoreCase("activityimageupload")) {
                                                fileUploadKey = "ActivityUpload";
                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                                    openImagePicker();
                                                } else {
                                                    openGalleryOnly();
                                                }
                                            } else if (reward.getRedirectionKey().equalsIgnoreCase("water")) {
                                                waterIntakeDialoge(QuizathonViewAllActivity.this, NewDashboardHelper.Companion.getWaterIntakeGoal(), NewDashboardHelper.Companion.getWaterIntake(), NewDashboardHelper.Companion.getWaterIntakeAllowed());
                                            } else if (reward.getRedirectionKey().equalsIgnoreCase("meditation")) {
                                                showPopUpZenZone(QuizathonViewAllActivity.this, Double.parseDouble(NewDashboardHelper.Companion.getCurrentValueZenZone()));
                                            } else {
                                                RedirectionModel redirectionModel = new RedirectionModel(reward.getRedirectionKey(), false, "", "", "", "", "", "", 0, "", "", "", "", "");
                                                RedirectionMethod.INSTANCE.redirectionScreen(redirectionModel, QuizathonViewAllActivity.this);
                                            }
                                        }

                                        @Override
                                        public void onCloseClick() {
                                            //cancelDialog();
                                        }

                                        @Override
                                        public void onSubmit(String title) {
                                            titleContent = title;
                                            if (reward.getRedirectionKey().equalsIgnoreCase("journalUpload")) {
                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                                    openImagePicker();
                                                } else {
                                                    checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                                                }
                                            }
                                        }
                                    });
                                    dialog.setFromRewardList(true);
                                    dialog.show(getSupportFragmentManager(), "expandableBottomSheet");
                                }
                            }

                        });
                        binding.gvCardPalywin.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<QuizathonResponseModel> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    private void showBurnQuizDialog(int pointsToBurn, String title, String icon, String message, String qid) {

        RetakeAlertDialog retakeAlertDialog = new RetakeAlertDialog(
                QuizathonViewAllActivity.this,
                pointsToBurn,
                icon,
                title,
                message,
                getRetakeDialogListener(qid, pointsToBurn) // Use extracted listener
        );

        retakeAlertDialog.show();
    }

    // Separate method to handle dialog button clicks
    private QuizOptionAdapter.OnItemClickListener getRetakeDialogListener(String qid, int pointToBurn) {
        return (position, name) -> {
            if (name.equalsIgnoreCase("Close")) {
                burnType = "";
                quizathonModelRegister = null;
                quizathonModelPointBurn = null;
                // Optional: Add logging or tracking here
            } else if (name.equalsIgnoreCase("Retake")) {

                BurnSpinData(Integer.parseInt(qid), pointToBurn);
            }
        };
    }

    private void getQuestionData(String type) {
        GetQuestionRequest request = new GetQuestionRequest(getString(R.string.quiz_i_id),type);
        Call<GetQuizResponseModel> call = apiInterfaceWyh.getQuestions(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<GetQuizResponseModel>() {
            @Override
            public void onResponse(Call<GetQuizResponseModel> call, Response<GetQuizResponseModel> response) {

                if (response.body() != null && response.code() == 200) {

                    if (response.body().getData().getQuestions() != null) {
                        GetQuizResponseModel getQuizResponseModel = response.body();
                        Log.d("json", getQuizResponseModel.getData().getQuestions());
                        isAssigned = getQuizResponseModel.getData().isAssigned();
                        ArrayList<GetQuizQuestions> getQuizQuestions = new Gson().fromJson(
                                getQuizResponseModel.getData().getQuestions(),
                                new TypeToken<List<GetQuizQuestions>>() {
                                }.getType());
                        Log.d("json", new Gson().toJson(getQuizQuestions));
                        qId = "" + response.body().getData().getQid();
                        quizQuestionsList = getQuizQuestions;
                        PlayWinCardViewAdapter adapter = new PlayWinCardViewAdapter(quizFeedbackModels, quizathonModelList, quizQuestionsList, QuizathonViewAllActivity.this, type, new onRewardClick() {
                            @Override
                            public void onRewardClick(Object item, String type) {
                                if (item instanceof GetQuizQuestions) {
                                    GetQuizQuestions quizCategory = (GetQuizQuestions) item;
                                    if (quizCategory.getCategoryName().equalsIgnoreCase("Trivia") && !isAssigned) {
                                        getTriviaData();
                                    } else {
                                        Intent intent = new Intent(QuizathonViewAllActivity.this, QuizActivity.class);
                                        intent.putExtra("data", new Gson().toJson(quizCategory.getQuestions()));
                                        intent.putExtra("CategoryName", quizCategory.getCategoryName());
                                        intent.putExtra("Category", quizCategory.getCategory());
                                        intent.putExtra("comingFrom", "");
                                        intent.putExtra("qId", qId);
                                        startActivity(intent);
                                    }
                                } else if (item instanceof QuizathonModel) {
                                    QuizathonModel quizathonModel = (QuizathonModel) item;
                                    if (type.equalsIgnoreCase("Spin")) {
                                        startActivity(new Intent(QuizathonViewAllActivity.this, SpinWheelRewardsActivity.class));
                                    } else if (type.equalsIgnoreCase("Others")) {
                                        Intent in = new Intent(QuizathonViewAllActivity.this, ActivityFileShareList.class);
                                        startActivity(in);
                                    } else {
                                        if (quizathonModel.getAnswerJson() != null && !quizathonModel.getAnswerJson().isEmpty() && quizathonModel.isQuizCompleted()) {
                                            Intent intent = new Intent(QuizathonViewAllActivity.this, QuizathonScoreActivity.class);
                                            if (quizathonModel.getQuizType() != null && quizathonModel.getQuizType().equalsIgnoreCase("Streak")) {
                                                intent = new Intent(QuizathonViewAllActivity.this, QuizathonTimeboundScoreActivity.class);
                                            }
                                            intent.putExtra("data", quizathonModel.getAnswerJson());
                                            intent.putExtra("your_score", quizathonModel.getUserScore());
                                            intent.putExtra("total_score", quizathonModel.getTotalScore());
                                            intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                                            intent.putExtra("Category", quizathonModel.getQuizDesc());
                                            intent.putExtra("qid", quizathonModel.getQuizId());
                                            intent.putExtra("comingFrom", "quiz");
                                            intent.putExtra("rewardText", "");
                                            intent.putExtra("withActivity", quizathonModel.getShowActivity());
                                            intent.putExtra("quizdata", quizathonModel.getModel());
                                            intent.putExtra("rewardText", quizathonModel.getRetakeId());
                                            intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                                            intent.putExtra("isRetakeAvailable", quizathonModel.getRetakeAvailable());
                                            intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                                            intent.putExtra("hasSufficientBalance", quizathonModel.isHasSufficientBalance());
                                            intent.putExtra("retakePointstoBurn", quizathonModel.getRetakePointstoBurn());
                                            intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                                            intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());
                                            intent.putExtra("type", type);
                                            intent.putExtra("quizModel", quizathonModel);
                                            startActivity(intent);
                                        } else if (!quizathonModel.getIsUserRegistered() && quizathonModel.getRegistrationQuestions() != null && !quizathonModel.getRegistrationQuestions().isEmpty()) {
                                            RegistrationDialog dialog = new RegistrationDialog(QuizathonViewAllActivity.this, quizathonModel.getRegistrationQuestions(), quizathonModel.getRegTitle(), quizathonModel.getQuizId(), quizathonModel.getQuizDesc());
                                            if (quizathonModel.getRegIcon() != null && !quizathonModel.getRegIcon().isEmpty()) {
                                                dialog.setIconUrl(quizathonModel.getRegIcon());
                                            }
                                            dialog.show();
                                        } else if (quizathonModel.getDialog_model() != null && quizathonModel.getDialog_model().getType() != null) {
                                            QuizMessageDialog dialog = new QuizMessageDialog(QuizathonViewAllActivity.this, quizathonModel.getDialog_model(), apiInterfaceWyh);
                                            dialog.show();
                                        } else {
                                            Intent intent = new Intent(QuizathonViewAllActivity.this, QuizathonActivity.class);
                                            intent.putExtra("data", quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted() ? quizathonModel.getAnswerJson() : quizathonModel.getQuestionJson());
                                            intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                                            intent.putExtra("Category", quizathonModel.getQuizDesc());
                                            intent.putExtra("comingFrom", "");
                                            intent.putExtra("IsRetake", quizathonModel.getRetake());
                                            intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                                            intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                                            intent.putExtra("qId", quizathonModel.getQuizId());
                                            intent.putExtra("type", type);
                                            startActivity(intent);
                                        }
                                    }
                                } else if (item instanceof QuizFeedbackModel) {
                                    QuizFeedbackModel quizFeedbackModel = (QuizFeedbackModel) item;
                                    if (quizFeedbackModel.getFeedbackAnswerJson() != null && !quizFeedbackModel.getFeedbackAnswerJson().isEmpty() && quizFeedbackModel.getIsFeedbackTaken() && quizFeedbackModel.isShowActivity()) {

                                        Intent intent = new Intent(QuizathonViewAllActivity.this, QuizathonScoreActivity.class);
                                        intent.putExtra("data", quizFeedbackModel.getFeedbackAnswerJson());
                                        intent.putExtra("your_score", 0);
                                        intent.putExtra("total_score", 0);
                                        intent.putExtra("CategoryName", quizFeedbackModel.getFeedbackDesc());
                                        intent.putExtra("Category", quizFeedbackModel.getFeedbackDesc());
                                        intent.putExtra("type", type);
                                        intent.putExtra("rewardText", "");
                                        intent.putExtra("quizdata", quizFeedbackModel.getModel());
                                        intent.putExtra("withActivity", quizFeedbackModel.isShowActivity());
                                        intent.putExtra("comingFrom", "Feedback");
                                        intent.putExtra("subCategory", quizFeedbackModel.getFeedbackDesc());
                                        startActivity(intent);


                                    } else {
                                        Intent intent = new Intent(QuizathonViewAllActivity.this, QuizathonActivity.class);
                                        intent.putExtra("data", quizFeedbackModel.getFeedbackQuestionJson());
                                        intent.putExtra("CategoryName", quizFeedbackModel.getFeedbackDesc());
                                        intent.putExtra("Category", quizFeedbackModel.getFeedbackDesc());
                                        intent.putExtra("comingFrom", "Feedback");
                                        intent.putExtra("IsRetake", false);
                                        intent.putExtra("RetakeId", 0);
                                        intent.putExtra("rewardDate", "");
                                        intent.putExtra("qId", quizFeedbackModel.getFeedbackId());
                                        intent.putExtra("type", type);
                                        startActivity(intent);
                                    }
                                }
                            }

                        });
                        binding.gvCardPalywin.setAdapter(adapter);

                    } else {
                        Toast.makeText(QuizathonViewAllActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(QuizathonViewAllActivity.this, getClass().getName(), getString(R.string.quiz_get_questions_failed));
                    Toast.makeText(QuizathonViewAllActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetQuizResponseModel> call, Throwable t) {

                Toast.makeText(QuizathonViewAllActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
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
                            QuizMessageDialog dialog = new QuizMessageDialog(QuizathonViewAllActivity.this,
                                    response.body().getQuizRegistrationData().getDialogModel(), apiInterfaceWyh);
                            dialog.show();
                            GetActiveQuizathon();
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
                Toast.makeText(QuizathonViewAllActivity.this, "Something went wrong! Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRewardClick(Object item, String type) {
        if (item instanceof RewardItem) {
            reward = (RewardItem) item;
            if(reward != null  && reward.getRedirectionKey() != null
                    && reward.getRedirectionKey().equalsIgnoreCase("activityimageupload")
                    && reward.getActivityImageUploaded()){
                MessageInfoDialog messageInfoDialog=new MessageInfoDialog(QuizathonViewAllActivity.this,ContextCompat.getDrawable(context, R.drawable.ic_hu_info_red),"Information","You have already uploaded an image. You will receive your rewards once it is approved");
                messageInfoDialog.show();
                //Toast.makeText(context, "You have already uploaded an image. You will receive your rewards once it is approved", Toast.LENGTH_SHORT).show();
                return;
            }
            RewardInfoBottomSheet dialog = new RewardInfoBottomSheet(QuizathonViewAllActivity.this, reward, type, new onRewardDialogClick() {
                @Override
                public void onStartClick() {
                    if (comingFrom.equalsIgnoreCase("Quiz")) {
                        StartSpinActivity(reward.getActivityTransId());
                    } else {
                        StartFeedbackActivity(reward.getActivityTransId());
                    }
                    if (reward.getRedirectionKey().contains("feedback")) {
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedbackCallback(QuizathonViewAllActivity.this, feedbackResponseData, QuizathonViewAllActivity.this);
                    }
                }

                @Override
                public void onCompleteClick() {
                    if (reward.getRedirectionKey().contains("feedback")) {
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedbackCallback(QuizathonViewAllActivity.this, feedbackResponseData, QuizathonViewAllActivity.this);
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("journalUpload")) {
                        fileUploadKey = "JournalUpload";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            openGalleryOnly();
                        }
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("activityimageupload")) {
                        fileUploadKey = "ActivityUpload";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            openGalleryOnly();
                        }
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("water")) {
                        waterIntakeDialoge(QuizathonViewAllActivity.this, NewDashboardHelper.Companion.getWaterIntakeGoal(), NewDashboardHelper.Companion.getWaterIntake(), NewDashboardHelper.Companion.getWaterIntakeAllowed());
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("meditation")) {
                        showPopUpZenZone(QuizathonViewAllActivity.this, Double.parseDouble(NewDashboardHelper.Companion.getCurrentValueZenZone()));
                    } else {
                        RedirectionModel redirectionModel = new RedirectionModel(reward.getRedirectionKey(), false, "", "", "", "", "", "", 0, "", "", "", "", "");
                        RedirectionMethod.INSTANCE.redirectionScreen(redirectionModel, QuizathonViewAllActivity.this);
                    }
                }

                @Override
                public void onCloseClick() {
                    //cancelDialog();
                }

                @Override
                public void onSubmit(String title) {
                    titleContent = title;
                    if (reward.getRedirectionKey().equalsIgnoreCase("journalUpload")) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                        }
                    }
                }
            });
            dialog.setFromRewardList(true);
            dialog.show(getSupportFragmentManager(), "expandableBottomSheet");
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
                    alertDialog.dismiss();
                    int meditationMinutes = Integer.parseInt(zenZoneBinding.etMins.getText().toString());
                    UploadActivityData(Constants.MEDITATION, meditationMinutes + currentTime);
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
                //Todo
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

                    if (response.body().getSpinTheWheelRewardsModel() != null) {
                        getSpinRewardPopup(response.body().getSpinTheWheelRewardsModel());
                    } else if (checkIsFromQuizqathon()) {
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

    private void FetchQuizReward() {
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

            }
        });
    }

    public boolean checkIsFromQuizqathon() {
        if (NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()) {
            return true;
        }
        return false;
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
                    //Todo
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
                        if (responseBody.getSpinTheWheelRewardsModel() != null) {
                            getSpinRewardPopup(responseBody.getSpinTheWheelRewardsModel());
                        } else if (checkIsFromQuizqathon()) {
                            //QuizReward Api Call
                            FetchQuizReward();
                            //getQuizathonRewardPopup(response.body().getQuizathonRewardData());
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

    private void getSpinRewardPopup(AssignRewardsResponse.SpinRewardsData data) {
        try {
            if (data.getRewardType() != null && !data.getRewardType().isEmpty()) {
                String rewardtype = data.getRewardType();
                if (rewardtype.equalsIgnoreCase("Points")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerPointsPopupCallBack(data.getRewardTitle(), this, "Trends", data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Offers")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerOfferCallBack(this, data.getPartnerLogo(), data.getRewardDescription(), data.getPartnerName()
                            , data.getPartnerUrl(), "Trends", data.getExpiryInHours(), data.getRewardTitle(), data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Voucher")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerVoucherCallBack(this, data.getRewardLogo(), data.getCouponCode(), data.getRewardDescription()
                            , data.getRewardTitle(), data.getExpiryInHours(), "Trends", data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Stamps")) {
                    QuizRewardDialog.INSTANCE.showStampsPopupCallBack(data.getRewardHeader1(), data.getRewardHeader2(), data.getRewardTitle(), data.getRewardValue(), this, this, this);
                } else if (rewardtype.equalsIgnoreCase("Badge")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerBadgePopupCallBack(this, "Trends", data.getRewardLogo(), data.getRewardTitle(), data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("future")) {
                    QuizRewardDialog.INSTANCE.showFutureRewardDialog(context, data.getDialogModel(), data.getClaimDate());
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
                    QuizRewardDialog.INSTANCE.showPostSpinnerPointsPopupCallBack(data.getRewardTitle(), QuizathonViewAllActivity.this, "Trends", data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Offers")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerOfferCallBack(QuizathonViewAllActivity.this, data.getPartnerLogo(), data.getRewardDescription(), data.getPartnerName()
                            , data.getPartnerUrl(), "Trends", data.getExpiryInHours(), data.getRewardTitle(), data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Voucher")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerVoucherCallBack(QuizathonViewAllActivity.this, data.getRewardLogo(), data.getCouponCode(), data.getRewardDescription()
                            , data.getRewardTitle(), data.getExpiryInHours(), "Trends", data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Badge")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerBadgePopupCallBack(QuizathonViewAllActivity.this, "Trends", data.getRewardLogo(), data.getRewardTitle(), data.getRewardDescription(),
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

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(QuizathonViewAllActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(QuizathonViewAllActivity.this, new String[]{permission}, requestCode);
        } else {
            openGalleryOnly();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalleryOnly();
            } else {

                Toast.makeText(QuizathonViewAllActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }


    }


    public void cancelDialog() {
        try {
            if (reward != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, reward.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }


    private void StartSpinActivity(int ActivityTransId) {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(QuizathonViewAllActivity.this);
            apiInterface.StartQuizActivity(SharedPref.getAuthToken(), new StartSpinActivityRequest(ActivityTransId)).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (reward.getRedirectionKey().equalsIgnoreCase("water")) {
                            waterIntakeDialoge(QuizathonViewAllActivity.this, NewDashboardHelper.Companion.getWaterIntakeGoal(), NewDashboardHelper.Companion.getWaterIntake(), NewDashboardHelper.Companion.getWaterIntakeAllowed());
                        } else if (reward.getRedirectionKey().equalsIgnoreCase("meditation")) {
                            showPopUpZenZone(QuizathonViewAllActivity.this, Double.parseDouble(NewDashboardHelper.Companion.getCurrentValueZenZone()));
                        } else {
                            RedirectionModel redirectionModel = new RedirectionModel(reward.getRedirectionKey(), false, "", "", "", "", "", "", 0, "", "", "", "", "");
                            RedirectionMethod.INSTANCE.redirectionScreen(redirectionModel, QuizathonViewAllActivity.this);
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
            CommonUtils.showProgressDialige(QuizathonViewAllActivity.this);
            apiInterface.StartFeedbackActivity(SharedPref.getAuthToken(), new StartSpinActivityRequest(ActivityTransId)).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (reward.getRedirectionKey().equalsIgnoreCase("water")) {
                            waterIntakeDialoge(QuizathonViewAllActivity.this, NewDashboardHelper.Companion.getWaterIntakeGoal(), NewDashboardHelper.Companion.getWaterIntake(), NewDashboardHelper.Companion.getWaterIntakeAllowed());
                        } else if (reward.getRedirectionKey().equalsIgnoreCase("meditation")) {
                            showPopUpZenZone(QuizathonViewAllActivity.this, Double.parseDouble(NewDashboardHelper.Companion.getCurrentValueZenZone()));
                        } else {
                            RedirectionModel redirectionModel = new RedirectionModel(reward.getRedirectionKey(), false, "", "", "", "", "", "", 0, "", "", "", "", "");
                            RedirectionMethod.INSTANCE.redirectionScreen(redirectionModel, QuizathonViewAllActivity.this);
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

    private void BurnSpinData(int quizId, int pointsToBurn) {
        BurnRegPointsReq request = new BurnRegPointsReq(quizId, pointsToBurn, burnType);
        Call<BurnRegPointsResp> call;
        if (burnType.equals("QuizEntry")) {
            call = apiInterface.BurnQuizPoints(SharedPref.getAuthToken(), request);
        } else {
            call = apiInterface.BurnQuizPoints(SharedPref.getAuthToken(), request);
        }


        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BurnRegPointsResp> call, @NonNull Response<BurnRegPointsResp> response) {
                if (response.body() != null && response.code() == 200)
                {
                    if (response.body().getSuccess()){
                        Toast.makeText(context, "Points burned. You can now register the Quiz", Toast.LENGTH_SHORT).show();
                        burnType="";
                        GetActiveQuizathon();
                    }
                    else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        quizathonModelPointBurn=null;
                        quizathonModelRegister=null;
                        burnType="";
                    }

                }
                else {
                    //Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    quizathonModelPointBurn=null;
                    quizathonModelRegister=null;
                    burnType="";
                }
            }

            @Override
            public void onFailure(@NonNull Call<BurnRegPointsResp> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onRewardRecieved(AssignRewardsResponse.SpinRewardsData spinRewardsData) {

    }

    @Override
    public void onQuizRewardRecieved(QuizathonRewardData quizathonRewardData) {

    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {

    }

    @Override
    public void onScratchStarted() {

    }

    @Override
    public void onDialogDismiss() {
        GetQuizActivityList();
        cancelDialog();
    }

    public void callRegisterAfterPointBurn(QuizathonModel quizathonModel) {
        quizathonModelPointBurn = null;
        if (!quizathonModel.getIsUserRegistered() && quizathonModel.getRegistrationQuestions() != null && !quizathonModel.getRegistrationQuestions().isEmpty() && quizathonModel.getIsRegistrationRequired()) {
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
            intent.putExtra("type", type);
            intent.putExtra("quizModel", quizathonModel);
            RegistrationDialog dialog = new RegistrationDialog(QuizathonViewAllActivity.this, quizathonModel.getRegistrationQuestions(), quizathonModel.getRegTitle(), quizathonModel.getQuizId(), intent, quizathonModel.getQuizStarted(), quizathonModel.getQuizDesc());
            if (quizathonModel.getRegIcon() != null && !quizathonModel.getRegIcon().isEmpty()) {
                dialog.setIconUrl(quizathonModel.getRegIcon());
            }
            dialog.show();

        }
    }

    public void callQuizAfterPointBurn(QuizathonModel quizathonModel) {
        quizathonModelRegister = null;
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
        intent.putExtra("type", type);
        intent.putExtra("quizModel", quizathonModel);
        context.startActivity(intent);

    }

    private void GetActivityRewardsClaimList() {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);
            apiInterface.getActivityRewardsClaimList(SharedPref.getAuthToken()).enqueue(new Callback<GetActivityRewardsClaimListResp>() {
                @Override
                public void onResponse(Call<GetActivityRewardsClaimListResp> call, Response<GetActivityRewardsClaimListResp> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getData() != null) {
                            setData(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetActivityRewardsClaimListResp> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    public void setData(GetActivityRewardsClaimListResp rewardResponse) {
//        Type postLogRewardType = new TypeToken<EarnedRewardResponse>() {
//        }.getType();
//         rewardResponse = new Gson().fromJson(response, postLogRewardType);
        boolean isDataFound = false;
        //binding.scrollRewardList.setVisibility(View.VISIBLE);
        // binding.tvNDF.setVisibility(View.GONE);
        List<RewardItem> allTasks = new ArrayList<>();
        if (rewardResponse.getData().getQuizActivityListModelLists() != null && !rewardResponse.getData().getQuizActivityListModelLists().getWip().isEmpty()) {

            allTasks.addAll(rewardResponse.getData().getQuizActivityListModelLists().getWip());
        }
        if (rewardResponse.getData().getQuizActivityListModelLists() != null && !rewardResponse.getData().getQuizActivityListModelLists().getActiveList().isEmpty()) {
            allTasks.addAll(rewardResponse.getData().getQuizActivityListModelLists().getActiveList());
        }
        if (rewardResponse.getData().getQuizActivityListModelLists() != null && !rewardResponse.getData().getQuizActivityListModelLists().getCompletedList().isEmpty()) {

            allTasks.addAll(rewardResponse.getData().getQuizActivityListModelLists().getCompletedList());
        }
        if (rewardResponse.getData().getQuizActivityListModelLists() != null && !rewardResponse.getData().getQuizActivityListModelLists().getExpiredList().isEmpty()) {

            allTasks.addAll(rewardResponse.getData().getQuizActivityListModelLists().getExpiredList());
        }


        QuizRewardViewAdapter adapter = null;
        adapter = new QuizRewardViewAdapter(allTasks, QuizathonViewAllActivity.this, type, QuizathonViewAllActivity.this);
        binding.gvCardPalywin.setAdapter(adapter);
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
                    try {
                        uploadFileWithContent(comingFrom, titleContent, Integer.parseInt(quizreward.getTransId()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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
                    try {
                        uploadFileWithContent(comingFrom, titleContent, Integer.parseInt(quizreward.getTransId()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
                .addFormDataPart("ActivityEvent", String.valueOf(actvityEvent))
                .addFormDataPart("ActivityDescription", String.valueOf(actvityDescription))
                .addFormDataPart("TransId", String.valueOf(transId))
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
                if (checkIsFromQuizqathon()) {
                    FetchQuizReward();
                }
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
}
