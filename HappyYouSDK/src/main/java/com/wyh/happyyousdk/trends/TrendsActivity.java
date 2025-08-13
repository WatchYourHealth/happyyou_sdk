package com.wyh.happyyousdk.trends;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.rewards.fragment.LevelFragment.CAMERA_PERMISSION_CODE;
import static com.wyh.happyyousdk.utils.CommonUtils.convertMinutesIntoHour;
import static com.wyh.happyyousdk.utils.CommonUtils.convertMinutesIntoHourInteger;
import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.getWeekWithDates;
import static com.wyh.happyyousdk.utils.CommonUtils.getWeeklyDataBar;
import static com.wyh.happyyousdk.utils.CommonUtils.hideKeyboard;
import static com.wyh.happyyousdk.utils.CommonUtils.todayDateInFormat;
import static com.wyh.happyyousdk.utils.CommonUtils.validateLetters;
import static com.wyh.happyyousdk.utils.Constants.ACTIVEHOURS;
import static com.wyh.happyyousdk.utils.Constants.ACTIVE_HOUR_GOAL;
import static com.wyh.happyyousdk.utils.Constants.ACTIVE_STEPS_COUNT;
import static com.wyh.happyyousdk.utils.Constants.BURNEDGRAPH;
import static com.wyh.happyyousdk.utils.Constants.CALORIE;
import static com.wyh.happyyousdk.utils.Constants.CALORIE_BURNED;
import static com.wyh.happyyousdk.utils.Constants.CALORIE_CONSUME;
import static com.wyh.happyyousdk.utils.Constants.CONSUMEDGRAPH;
import static com.wyh.happyyousdk.utils.Constants.DAILY;
import static com.wyh.happyyousdk.utils.Constants.FeedbackPOPUP;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.MEDITATION;
import static com.wyh.happyyousdk.utils.Constants.MONTHLY;
import static com.wyh.happyyousdk.utils.Constants.QUARTERLY;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.SLEEP;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.TAG_CURRENT_LEVEL;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;
import static com.wyh.happyyousdk.utils.Constants.WATER;
import static com.wyh.happyyousdk.utils.Constants.WEEKLY;
import static com.wyh.happyyousdk.utils.Constants.WEIGHT;
import static com.wyhsdk.utils.Utilities.getTodayDateNew;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.BackgroundWork.CoroutineClass;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.WebActivity;
import com.wyh.happyyousdk.absorb.HealthHacksActivity;
import com.wyh.happyyousdk.absorb.HealthTVDashboard;
import com.wyh.happyyousdk.absorb.QuickReadDashboard;
import com.wyh.happyyousdk.absorb.adapter.HealthHacksHealthTvAdapter;
import com.wyh.happyyousdk.absorb.adapter.HealthHacksWebinarAdapter;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.musicLib.MoreMusicListActivity;
import com.wyh.happyyousdk.musicLib.adapter.MusicLibAdapter;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.request.absorb.AddBookmarkRequest;
import com.wyh.happyyousdk.model.request.absorb.GetDashboardDataRequest;
import com.wyh.happyyousdk.model.request.absorb.VideoBookmarkRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.request.rewards.ActivityProgressRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;
import com.wyh.happyyousdk.model.response.absorb.AddBookmarkResponse;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;
import com.wyh.happyyousdk.contacts.ContactsActivityNew;

import com.wyh.happyyousdk.databinding.ActivityTrendsBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalUploadBinding;
import com.wyh.happyyousdk.databinding.CustomPopupRewardsBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.ScratchCardPopUpBinding;
import com.wyh.happyyousdk.diary.AddDiaryActivity;
import com.wyh.happyyousdk.ehr.EhrActivity;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.ira.IRAAnalysisActivity;
import com.wyh.happyyousdk.ira.IraActivity;
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.FetchRewardsRequest;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.request.rewards.GetRewardsDashboardRequest;
import com.wyh.happyyousdk.model.response.FetchRewardsResponse;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.model.response.rewards.ActivityProgressResponse;
import com.wyh.happyyousdk.model.response.rewards.TopUp;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.model.response.ProfileDetailsResponse;
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.PendingActivityDashboard;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.rewards.adapter.level.LevelActivitiesAdapter;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.request.rewards.StartRewardsActivityRequest;
import com.wyh.happyyousdk.model.response.rewards.LevelActivity;
import com.wyh.happyyousdk.model.response.rewards.LevelDashboardResponse;
import com.wyh.happyyousdk.model.response.rewards.RewardsLevel;
import com.wyh.happyyousdk.sendActivityData.SendDataToServerReceiver;
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity;
import com.wyh.happyyousdk.trends.adapter.TrendsQuickReadsAdapter;
import com.wyh.happyyousdk.model.request.trends.AddReminderDataRequest;
import com.wyh.happyyousdk.model.request.trends.AddWeightRequest;
import com.wyh.happyyousdk.model.request.trends.FetchGraphRequest;
import com.wyh.happyyousdk.model.request.trends.GetCalorieDataRequest;
import com.wyh.happyyousdk.model.response.trends.CalorieDataResponse;
import com.wyh.happyyousdk.model.response.trends.FetchGraphResponse;
import com.wyh.happyyousdk.unwind.UnwindActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;
import com.wyh.happyyousdk.utils.MyMarkerView;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyhsdk.main.WatchYourHealth;
import com.wyhsdk.sharedPreferences.SharedPreference;
import com.wyhsdk.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

public class TrendsActivity extends AppCompatActivity implements ScratchListener, LevelActivitiesAdapter.ClickListenerInterface, rewardDialogCloseListener {
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    FetchGraphResponse fetchGraphResponse;
    ActivityTrendsBinding binding;
    ProgressDialog progressDialog;
    float axisPadding = 0.5f;
    MyMarkerView mv;
    WatchYourHealth watchYourHealth;
    AlertDialog alertDialogBonusRewards, alertDialogStamp, alertDialogBonusStamp;
    double perStepInKm = 0.0008;
    double perCalorieInStep = 0.04;
    private final int REQUEST_CODE_ACTIVITY_RECOGNITION = 1;
    int totalStepsDays, totalStandDays, totalMinuteStepsDays;
    boolean sendToServer = false;

    String activityType;
    int waterIntakeGoal;
    boolean waterIntakeAllowed = false;
    int communityId, positionWinnings = 0, positionTribes = 0, positionReads = 0, positionHealthTv = 0, positionMusicLib = 0, positionWebinar = 0;
    boolean btnDaily = false, btnWeekly = false, btnMonthly = false, btnQuarterly = false, btnNutrition = true, btnWorkout = false, btnTodayMeal = false, btnTodayWorkout = false;
    String min;
    double medMinutes = 0;
    double waterIntake = 0;
    double activeHrTodays = 0;
    double stepIntake = 0;

    public static String feedBackModel = "";

    int glasses;
    GetAnalysisResponse getAnalysisResponse;
    RewardsLevel currentLevelActivity;
    ArrayList<FileData> fileDataList = new ArrayList<>();
    int positionCurrentActivities = 0;
    private LevelActivity uploadFileActivity;
    String yesNoAnswer, journalContent;
    String fileUploadKey = "ActivityUploads";
    LevelActivitiesAdapter levelActivitiesAdapter;
    CustomPopupRewardsBinding levelActivityBinding;
    CustomPopUpRewardsJournalUploadBinding customPopUpRewardsJournalUploadBinding;
    String activityEventType = "";
    CustomPopUpRewardsJournalBinding levelActivityJournalBinding;
    AlertDialog levelActivityAlertDialog, levelActivityJournalUploadAlertDialog;
    String tagName;
    List<GetDashboardDataResponse.Data.TagName> tagNameList;
    List<GetDashboardDataResponse.Data.AudioFiles> audioList;
    List<GetDashboardDataResponse.Data.HealthTv> healthTvList;
    List<ProfileDetailsResponse.Data.LatestWinning> myWinningList;
    boolean isFirst = true, isStamp = false;
    DecimalFormat format = new DecimalFormat("0.##");
    int stampId = -1, currentGraphIndex = 0;
    boolean isPositiveBtn = false;
    String selectedGraphType = DAILY;


    DecimalFormat numberFormat = new DecimalFormat("0.##");

    AssignRewardsResponse.SpinRewardsData spinRewardData;
    QuizathonRewardData quizathonRewardData;

    @Override
    protected void onResume() {
        initialView();
        if (feedBackModel != null && !feedBackModel.isEmpty()) {
            FeedbackResponseData responseNew = new Gson().fromJson(feedBackModel, FeedbackResponseData.class);
            if (responseNew.getFeedbackModel() != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedback(TrendsActivity.this, responseNew);
                        feedBackModel = "";
                    }
                }, 3000);

            }
        }
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trends);
        context = this;

        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        watchYourHealth = new WatchYourHealth(this, SharedPref.getUuid());
        watchYourHealth.initializeAPIClient(savedInstanceState);

        activityType = getIntent().getStringExtra("activityType");

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "TrendsInfo");
            customObj.put("activityType", activityType);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        initialView();
        fetureArrow();
        prevGraphButton();
        nextGraphButton();

//        String title = activityType.substring(0, 1).toUpperCase() + activityType.substring(1).toLowerCase();
//        binding.commonToolBar.tvTitle.setText(title);
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));

        if (activityType.equals(STEPS)) {
            if (SharedPreference.getGoogleFitConnection()) {
                getGoogleFitData();
            } else {
                getFitPermission();

            }
        }


        binding.btnDaily.setOnClickListener(view -> {
            selectedGraphType = DAILY;
            currentGraphIndex = 0;
            btnDailySelected();
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchGraphData(DAILY, CONSUMEDGRAPH);
                } else {
                    fetchGraphData(DAILY, BURNEDGRAPH);
                }
            } else {
                fetchGraphData(DAILY, activityType);
            }

            if (activityType.equals(SLEEP)) {
                binding.btnDaily.setBackground(getDrawable(R.drawable.ic_blue_button_bg));
                binding.btnDaily.setTextColor(getResources().getColor(R.color.white));
            } else {
                binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_round_btn_light_red));
                binding.btnDaily.setTextColor(getResources().getColor(R.color.white));
            }
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnWeekly.setTextColor(getResources().getColor(R.color.black));
            binding.btnMonthly.setTextColor(getResources().getColor(R.color.black));
            binding.btnQuarterly.setTextColor(getResources().getColor(R.color.black));
        });
        binding.btnWeekly.setOnClickListener(view -> {
            currentGraphIndex = 0;
            btnDaily = false;
            btnWeekly = true;
            btnMonthly = false;
            btnQuarterly = false;
            selectedGraphType = WEEKLY;
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchGraphData(WEEKLY, CONSUMEDGRAPH);
                } else {
                    fetchGraphData(WEEKLY, BURNEDGRAPH);
                }
            } else {
                fetchGraphData(WEEKLY, activityType);
            }
            if (activityType.equals(SLEEP)) {
                binding.btnWeekly.setBackground(getDrawable(R.drawable.ic_blue_button_bg));
                binding.btnWeekly.setTextColor(getResources().getColor(R.color.white));
            } else {
                binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_round_btn_light_red));
                binding.btnWeekly.setTextColor(getResources().getColor(R.color.white));
            }
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnDaily.setTextColor(getResources().getColor(R.color.black));
            binding.btnMonthly.setTextColor(getResources().getColor(R.color.black));
            binding.btnQuarterly.setTextColor(getResources().getColor(R.color.black));
        });
        binding.btnMonthly.setOnClickListener(view -> {
            btnDaily = false;
            btnWeekly = false;
            btnMonthly = true;
            btnQuarterly = false;
            currentGraphIndex = 0;
            selectedGraphType = MONTHLY;
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchGraphData(MONTHLY, CONSUMEDGRAPH);
                } else {
                    fetchGraphData(MONTHLY, BURNEDGRAPH);
                }
            } else {
                fetchGraphData(MONTHLY, activityType);
            }
            if (activityType.equals(SLEEP)) {
                binding.btnMonthly.setBackground(getDrawable(R.drawable.ic_blue_button_bg));
                binding.btnMonthly.setTextColor(getResources().getColor(R.color.white));
            } else {
                binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_round_btn_light_red));
                binding.btnMonthly.setTextColor(getResources().getColor(R.color.white));
            }
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnWeekly.setTextColor(getResources().getColor(R.color.black));
            binding.btnDaily.setTextColor(getResources().getColor(R.color.black));
            binding.btnQuarterly.setTextColor(getResources().getColor(R.color.black));
        });

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        binding.btnQuarterly.setOnClickListener(view -> {
            btnDaily = false;
            btnWeekly = false;
            btnMonthly = false;
            btnQuarterly = true;
            currentGraphIndex = 0;
            selectedGraphType = QUARTERLY;

            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchGraphData(QUARTERLY, CONSUMEDGRAPH);
                } else {
                    fetchGraphData(QUARTERLY, BURNEDGRAPH);
                }
            } else {
                fetchGraphData(QUARTERLY, activityType);
            }
            if (activityType.equals(SLEEP)) {
                binding.btnQuarterly.setBackground(getDrawable(R.drawable.ic_blue_button_bg));
                binding.btnQuarterly.setTextColor(getResources().getColor(R.color.white));
            } else {
                binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_round_btn_light_red));
                binding.btnQuarterly.setTextColor(getResources().getColor(R.color.white));
            }
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnWeekly.setTextColor(getResources().getColor(R.color.black));
            binding.btnMonthly.setTextColor(getResources().getColor(R.color.black));
            binding.btnDaily.setTextColor(getResources().getColor(R.color.black));
        });

        binding.btnCalAdd.setOnClickListener(view -> {
            Log.d("btnWorkout", btnWorkout + "");
            if (btnWorkout) {
                openWebView(getResources().getString(R.string.viewExerciseUrl));
            } else {
                openWebView(getResources().getString(R.string.viewFoodUrl));
            }
        });

        binding.btnAddTodays.setOnClickListener(view -> {
            Log.d("btnTodayMeal", btnTodayMeal + "");
            if (btnTodayMeal) {
                openWebView(getResources().getString(R.string.addFoodUrl));
            } else {
                openWebView(getResources().getString(R.string.addExerciseUrl));
            }
        });

        binding.ivRefresh.setOnClickListener(v -> {
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchGraphData(selectedGraphType, CONSUMEDGRAPH);
                } else {
                    fetchGraphData(selectedGraphType, BURNEDGRAPH);
                }
            } else {
                fetchGraphData(selectedGraphType, activityType);
            }
        });


        switch (activityType) {
            case "STEPS":
                tagName = "Happy Footprint";
                binding.llBmiChart.setVisibility(View.GONE);
                binding.llConsumed.setVisibility(View.GONE);
                binding.includeToolbar.tvBack.setText("Happy Footprints");
                binding.tvGraphTopMsg.setVisibility(View.GONE);
                binding.ivActivityBg.setImageDrawable(getDrawable(R.drawable.bg_steps_without_bear));
                binding.laBear.setVisibility(View.VISIBLE);
                binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_walking.json");
                binding.rlSleepDetails.setVisibility(View.GONE);
                binding.rlWaterDetailsIntake.setVisibility(View.GONE);
                binding.llTodaySteps.setVisibility(View.VISIBLE);
                binding.llStepsDetails.setVisibility(View.VISIBLE);
                binding.rlMeditation.setVisibility(View.GONE);
                binding.rlWaterDetails.setVisibility(View.GONE);
                binding.llCal.setVisibility(View.GONE);
                binding.rlFootprintDeatils.setVisibility(View.VISIBLE);
                binding.rlCalCountDetails.setVisibility(View.GONE);
                binding.rlActiveHoursDetails.setVisibility(View.GONE);
                break;
            case "SLEEP":
                tagName = "Pillow Time";
                binding.llBmiChart.setVisibility(View.GONE);
                binding.llConsumed.setVisibility(View.GONE);
                binding.includeToolbar.tvBack.setText("Pillow Time");
//                binding.tvGraphTopMsg.setVisibility(View.VISIBLE);
                binding.ivActivityBg.setImageDrawable(getDrawable(R.drawable.ic_hra_pink_bg));
                binding.laBear.setVisibility(View.VISIBLE);
                binding.laBear.setAnimation(R.raw.anim_bear_sleeping);
                binding.rlSleepDetails.setVisibility(View.VISIBLE);
                binding.rlWaterDetailsIntake.setVisibility(View.GONE);
                binding.llTodaySteps.setVisibility(View.GONE);
                binding.llStepsDetails.setVisibility(View.GONE);
                binding.rlMeditation.setVisibility(View.GONE);
                binding.rlWaterDetails.setVisibility(View.GONE);
                binding.llCal.setVisibility(View.GONE);
                binding.rlFootprintDeatils.setVisibility(View.GONE);
                binding.rlCalCountDetails.setVisibility(View.GONE);
                binding.rlActiveHoursDetails.setVisibility(View.GONE);
                break;
            case "WATER":
                tagName = "H2O";
                binding.llBmiChart.setVisibility(View.GONE);
                binding.llConsumed.setVisibility(View.GONE);
                binding.includeToolbar.tvBack.setText("H2O");
//                binding.tvGraphTopMsg.setVisibility(View.VISIBLE);
                binding.ivActivityBg.setImageDrawable(getDrawable(R.drawable.bg_water_without_bear));
                binding.laBear.setVisibility(View.VISIBLE);
                binding.rlWaterDetailsIntake.setVisibility(View.VISIBLE);
                //Changed lottie animation source
                binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_water.json");
                binding.rlSleepDetails.setVisibility(View.GONE);
                binding.llTodaySteps.setVisibility(View.GONE);
                binding.llStepsDetails.setVisibility(View.GONE);
                binding.rlMeditation.setVisibility(View.GONE);
                binding.rlWaterDetails.setVisibility(View.VISIBLE);
                binding.llCal.setVisibility(View.GONE);
                binding.rlFootprintDeatils.setVisibility(View.GONE);
                binding.rlCalCountDetails.setVisibility(View.GONE);
                binding.rlActiveHoursDetails.setVisibility(View.GONE);
                binding.tvGlassAmt.setFocusable(false);
                binding.tvGlassAmt.setEnabled(false);
                binding.tvGlassAmt.setClickable(false);
                binding.tvGlassAmt.setText("0");
                break;
            case "WEIGHT":
                tagName = "Calories";
                binding.llBmiChart.setVisibility(View.GONE);
                binding.llConsumed.setVisibility(View.GONE);
                binding.includeToolbar.tvBack.setText("Weight Watcher");
                binding.ivActivityBg.setImageDrawable(getDrawable(R.drawable.bg_water_without_bear));
                binding.laBear.setVisibility(View.VISIBLE);
                binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_weight_check.json");
                binding.rlWaterDetailsIntake.setVisibility(View.GONE);
                binding.rlSleepDetails.setVisibility(View.GONE);
                binding.llTodaySteps.setVisibility(View.GONE);
                binding.llStepsDetails.setVisibility(View.GONE);
                binding.rlMeditation.setVisibility(View.GONE);
                binding.rlWaterDetails.setVisibility(View.VISIBLE);
                binding.llCal.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.rlFootprintDeatils.setVisibility(View.GONE);
                binding.rlCalCountDetails.setVisibility(View.GONE);
                binding.rlActiveHoursDetails.setVisibility(View.GONE);
                binding.llRecommend.setVisibility(View.GONE);
                binding.tvGlassAmt.setFocusable(true);
                binding.tvGlassAmt.setEnabled(true);
                binding.tvGlassAmt.setClickable(true);
                if (SharedPref.getWeight().equals("")) {
                    binding.tvGlassAmt.setText("0");
                } else {
                    binding.tvGlassAmt.setText(SharedPref.getWeight());

                }
                binding.tvMsg1.setText("Add Weight");
                binding.ivMsg1.setImageResource(R.drawable.ic_ideal_weight);
                break;
            case "ACTIVEHOURS":
                tagName = "Happy Footprint";
                binding.llBmiChart.setVisibility(View.GONE);
                binding.llConsumed.setVisibility(View.GONE);
                binding.includeToolbar.tvBack.setText("Active Hours");
                binding.tvGraphTopMsg.setVisibility(View.GONE);
                binding.ivActivityBg.setImageDrawable(getDrawable(R.drawable.bg_water_without_bear));
                binding.laBear.setVisibility(View.VISIBLE);
                binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_waving.json");
                binding.laBear.requestLayout();
                binding.rlWaterDetailsIntake.setVisibility(View.GONE);
                binding.laBear.getLayoutParams().height = 600;
                binding.laBear.getLayoutParams().width = 600;
                binding.rlSleepDetails.setVisibility(View.GONE);
                binding.llTodaySteps.setVisibility(View.GONE);
                binding.llStepsDetails.setVisibility(View.GONE);
                binding.rlMeditation.setVisibility(View.GONE);
                binding.rlWaterDetails.setVisibility(View.GONE);
                binding.llCal.setVisibility(View.GONE);
                binding.rlFootprintDeatils.setVisibility(View.GONE);
                binding.rlCalCountDetails.setVisibility(View.GONE);
                binding.rlActiveHoursDetails.setVisibility(View.VISIBLE);
                break;
            case "CALORIES":
                tagName = "Calories";
                binding.llBmiChart.setVisibility(View.VISIBLE);
                binding.llConsumed.setVisibility(View.VISIBLE);
                binding.includeToolbar.tvBack.setText("Cal - Count");
                binding.tvGraphTopMsg.setVisibility(View.GONE);
                binding.ivActivityBg.setImageDrawable(getDrawable(R.drawable.bg_calories_without_bear));
                binding.laBear.setVisibility(View.VISIBLE);
                binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_carrot.json");
                binding.rlSleepDetails.setVisibility(View.GONE);
                binding.llTodaySteps.setVisibility(View.GONE);
                binding.rlWaterDetailsIntake.setVisibility(View.GONE);
                binding.llStepsDetails.setVisibility(View.GONE);
                binding.rlMeditation.setVisibility(View.GONE);
                binding.rlWaterDetails.setVisibility(View.GONE);
                binding.llCal.setVisibility(View.VISIBLE);
                binding.rlFootprintDeatils.setVisibility(View.GONE);
                binding.rlCalCountDetails.setVisibility(View.GONE);
                binding.rlActiveHoursDetails.setVisibility(View.GONE);
                break;
            case "MEDITATION":
                tagName = "Meditation";
                binding.llBmiChart.setVisibility(View.GONE);
                binding.llConsumed.setVisibility(View.GONE);
                binding.includeToolbar.tvBack.setText("Zen Zone");
                binding.tvGraphTopMsg.setVisibility(View.GONE);
                binding.ivActivityBg.setImageDrawable(getDrawable(R.drawable.bg_calories_without_bear));
                binding.laBear.setVisibility(View.VISIBLE);
                binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_meditation.json");
                binding.rlSleepDetails.setVisibility(View.GONE);
                binding.rlWaterDetailsIntake.setVisibility(View.GONE);
                binding.llTodaySteps.setVisibility(View.GONE);
                binding.llStepsDetails.setVisibility(View.GONE);
                binding.rlMeditation.setVisibility(View.VISIBLE);
                binding.rlWaterDetails.setVisibility(View.GONE);
                binding.llCal.setVisibility(View.GONE);
                binding.rlFootprintDeatils.setVisibility(View.GONE);
                binding.rlCalCountDetails.setVisibility(View.GONE);
                binding.rlActiveHoursDetails.setVisibility(View.GONE);
                break;

            case "CALORIES_CONSUME":
                tagName = "Calories";
                binding.llBmiChart.setVisibility(View.VISIBLE);
                binding.llConsumed.setVisibility(View.VISIBLE);
                binding.includeToolbar.tvBack.setText("Cal - Count");
                binding.tvGraphTopMsg.setVisibility(View.GONE);
                binding.ivActivityBg.setImageDrawable(getDrawable(R.drawable.bg_calories_without_bear));
                binding.laBear.setVisibility(View.VISIBLE);
                binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_carrot.json");
                binding.rlSleepDetails.setVisibility(View.GONE);
                binding.llTodaySteps.setVisibility(View.GONE);
                binding.rlWaterDetailsIntake.setVisibility(View.GONE);
                binding.llStepsDetails.setVisibility(View.GONE);
                binding.rlMeditation.setVisibility(View.GONE);
                binding.rlWaterDetails.setVisibility(View.GONE);
                binding.llCal.setVisibility(View.VISIBLE);
                binding.rlFootprintDeatils.setVisibility(View.GONE);
                binding.rlCalCountDetails.setVisibility(View.GONE);
                binding.rlActiveHoursDetails.setVisibility(View.GONE);
                currentGraphIndex = 0;
                selectedGraphType = DAILY;
                binding.tvCalMsg.setText("Today’s Intake");
                binding.btnCalConsumed.setText("Calories Consumed");
                binding.btnAddTodays.setText("Add a meal");
                btnNutrition = true;
                btnWorkout = false;
                btnTodayWorkout = false;
                btnTodayMeal = true;
                binding.calIntakeOrBurn.setVisibility(View.VISIBLE);
                binding.llCalorieDetails.setVisibility(View.VISIBLE);
                binding.laBear.clearAnimation();
                binding.laBear.setAnimation(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_carrot.json");
                binding.laBear.playAnimation();


                binding.btnCalConsumed.setTextColor(getColor(R.color.white));
                binding.btnCalBurned.setTextColor(getColor(R.color.black));

                binding.btnCalConsumed.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));
                binding.btnCalBurned.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));

                fetchGraphData(DAILY, CONSUMEDGRAPH);
                getCalorieData();
                btnDailyView();
                break;

            case "CALORIES_BURNED":
                tagName = "Calories";
                binding.llBmiChart.setVisibility(View.VISIBLE);
                binding.llConsumed.setVisibility(View.VISIBLE);
                binding.includeToolbar.tvBack.setText("Cal - Count");
                binding.tvGraphTopMsg.setVisibility(View.GONE);
                binding.ivActivityBg.setImageDrawable(getDrawable(R.drawable.bg_calories_without_bear));
                binding.laBear.setVisibility(View.VISIBLE);
                binding.laBear.setAnimation(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_carrot.json");
                binding.rlSleepDetails.setVisibility(View.GONE);
                binding.llTodaySteps.setVisibility(View.GONE);
                binding.rlWaterDetailsIntake.setVisibility(View.GONE);
                binding.llStepsDetails.setVisibility(View.GONE);
                binding.rlMeditation.setVisibility(View.GONE);
                binding.rlWaterDetails.setVisibility(View.GONE);
                binding.llCal.setVisibility(View.VISIBLE);
                binding.rlFootprintDeatils.setVisibility(View.GONE);
                binding.rlCalCountDetails.setVisibility(View.GONE);
                binding.rlActiveHoursDetails.setVisibility(View.GONE);
                currentGraphIndex = 0;
                selectedGraphType = DAILY;
                binding.tvCalMsg.setText("Today's Workout");
                binding.btnCalBurned.setText("Calories Burned");
                binding.btnAddTodays.setText("Add a Workout");
                btnNutrition = false;
                btnWorkout = true;
                btnTodayWorkout = true;
                btnTodayMeal = false;
                binding.llCalorieDetails.setVisibility(View.GONE);
                binding.laBear.clearAnimation();
                binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_calorie.json");
                binding.laBear.playAnimation();

                binding.btnCalBurned.setTextColor(getColor(R.color.white));
                binding.btnCalConsumed.setTextColor(getColor(R.color.black));

                binding.btnCalConsumed.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
                binding.btnCalBurned.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));

                fetchGraphData(DAILY, BURNEDGRAPH);
                getCalorieBurnedData();
                btnDailyView();
                break;


        }


        getAbsorbDashboard(tagName);

        binding.btnCalConsumed.setOnClickListener(view -> {
            currentGraphIndex = 0;
            selectedGraphType = DAILY;
            binding.tvCalMsg.setText("Today’s Intake");
            binding.btnCalConsumed.setText("Calories Consumed");
            binding.btnAddTodays.setText("Add a meal");
            btnNutrition = true;
            btnWorkout = false;
            btnTodayWorkout = false;
            btnTodayMeal = true;
            binding.calIntakeOrBurn.setVisibility(View.VISIBLE);
            binding.llCalorieDetails.setVisibility(View.VISIBLE);
            binding.laBear.clearAnimation();
            binding.laBear.setAnimation(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_carrot.json");
            binding.laBear.playAnimation();


            binding.btnCalConsumed.setTextColor(getColor(R.color.white));
            binding.btnCalBurned.setTextColor(getColor(R.color.black));

            binding.btnCalConsumed.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));
            binding.btnCalBurned.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));

//            if (btnDaily) {
            fetchGraphData(DAILY, CONSUMEDGRAPH);
            getCalorieData();
            btnDailyView();

//            } else if (btnWeekly) {
//                fetchGraphData(WEEKLY, CONSUMEDGRAPH);
//                btnDailyView();
//            } else if (btnMonthly) {
//                fetchGraphData(MONTHLY, CONSUMEDGRAPH);
//                btnDailyView();
//            } else if (btnQuarterly) {
//                fetchGraphData(QUARTERLY, CONSUMEDGRAPH);
//                btnDailyView();
//            }
        });

        binding.btnCalBurned.setOnClickListener(view -> {
            currentGraphIndex = 0;
            selectedGraphType = DAILY;
            binding.tvCalMsg.setText("Today's Workout");
            binding.btnCalBurned.setText("Calories Burned");
            binding.btnAddTodays.setText("Add a Workout");
            btnNutrition = false;
            btnWorkout = true;
            btnTodayWorkout = true;
            btnTodayMeal = false;
//            binding.calIntakeOrBurn.setVisibility(View.GONE);
            binding.llCalorieDetails.setVisibility(View.GONE);
            binding.laBear.clearAnimation();
            binding.laBear.setAnimation(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_calorie.json");
            binding.laBear.playAnimation();

            binding.btnCalBurned.setTextColor(getColor(R.color.white));
            binding.btnCalConsumed.setTextColor(getColor(R.color.black));
            binding.btnCalConsumed.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnCalBurned.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));

            fetchGraphData(DAILY, BURNEDGRAPH);
            getCalorieBurnedData();
            btnDailyView();

        });

        binding.ivAdd.setOnClickListener(view -> {
            if (Objects.equals(activityType, WEIGHT)) {
                increaseWeight();
            } else {
                increaseNumber();
            }
        });
        binding.ivMinus.setOnClickListener(view -> {
            if (Objects.equals(activityType, WEIGHT)) {
                decreaseWeight();
            } else {
                decreaseNumber();
            }
        });

        binding.tvGlassAmt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(TrendsActivity.this);
                    binding.btnWaterAdd.performClick();
                    return true;
                }
                return false;
            }
        });

        //Rewards and badges

      /*TrendsRewardsAdapter communityGoalsAdapter = new TrendsRewardsAdapter(context, activityType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvRewards.setLayoutManager(linearLayoutManager);
        binding.rvRewards.setAdapter(communityGoalsAdapter);

        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(binding.rvRewards);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter indicatorsAdapter = new IndicatorsAdapter(context, 3, 0);
        binding.rvRewardsIndicator.setAdapter(indicatorsAdapter);
        binding.rvRewardsIndicator.setLayoutManager(linearLayoutManager1);
        binding.rvRewardsIndicator.setHasFixedSize(true);

        binding.rvRewards.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionWinnings = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else {
                        positionWinnings = linearLayoutManager.findFirstVisibleItemPosition();
                    }
                    indicatorsAdapter.updateSelectedIndex(positionWinnings);
                }
            }
        });*/


        binding.rlHealthTv.setOnClickListener(view -> {
            if (healthTvList != null && healthTvList.size() > 0) {
                Intent i = new Intent(this, HealthTVDashboard.class);
                i.putExtra("healthTV", (Serializable) healthTvList);
                i.putExtra("titleName", "Health TV");
                startActivity(i);
            }
        });
        binding.btnTen.setOnClickListener(view -> {
            min = "10";
            binding.etMins.setText(min);
        });
        binding.btnTwenty.setOnClickListener(view -> {
            min = "20";
            binding.etMins.setText(min);
        });
        binding.btnForty.setOnClickListener(view -> {
            min = "40";
            binding.etMins.setText(min);
        });

        binding.btnAdd.setOnClickListener(view -> {
            if (!binding.etMins.getText().toString().equals("")) {
                int meditationMinutes = (int) Double.parseDouble(binding.etMins.getText().toString());
                if (meditationMinutes > 0) {
                    UploadActivityData(MEDITATION, meditationMinutes + medMinutes);
                    binding.etMins.setText("");
//                    if (btnDaily) {
                    fetchGraphData(DAILY, activityType);

                    btnDailyView();
                } else {
                    Toast.makeText(context, "Please add time", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Please add time", Toast.LENGTH_SHORT).show();
            }
        });
        binding.rlQuickRead.setOnClickListener(view -> {
            if (tagNameList.size() > 0) {
                Intent i = new Intent(this, QuickReadDashboard.class);
                i.putExtra("taglist", (Serializable) tagNameList);
                startActivity(i);
            }
        });

        binding.rlMusicLib.setOnClickListener(view -> {
            Intent i = new Intent(this, MoreMusicListActivity.class);
            i.putExtra("data", new Gson().toJson(audioList));
            startActivity(i);
        });

        binding.rlWinnings.setOnClickListener(view -> {
            Intent intent = new Intent(context, RewardsActivity.class);
            startActivity(intent);
        });


        binding.btnWaterAdd.setOnClickListener(view -> {
            if (Objects.equals(activityType, WEIGHT)) {
                addWeight();
            } else {
                addWaterGlasses();
            }
        });

        LinearSnapHelper linearSnapHelper2 = new SnapHelperOneByOne();
        linearSnapHelper2.attachToRecyclerView(binding.rvQuickReads);

        LinearSnapHelper linearSnapHelper1 = new SnapHelperOneByOne();
        linearSnapHelper1.attachToRecyclerView(binding.rvActivities);
    }

    private void initialView() {
        if (activityType.equals(SLEEP)) {
            binding.btnDaily.setBackground(getDrawable(R.drawable.ic_blue_button_bg));
        } else {
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_round_btn_light_red));
        }
        binding.btnDaily.setTextColor(getResources().getColor(R.color.white));
        binding.btnWeekly.setTextColor(getResources().getColor(R.color.black));
        binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnMonthly.setTextColor(getResources().getColor(R.color.black));
        binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnQuarterly.setTextColor(getResources().getColor(R.color.black));
        binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        if (activityType.equals(CALORIE) || activityType.equals(CALORIE_CONSUME) || activityType.equals(CALORIE_BURNED)) {
            if (btnNutrition) {
                getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
                if (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null) {
                    double bmi = getAnalysisResponse.getAnalysisData().getAvgBMI().getBmi();
                    binding.llBmiChart.setVisibility(View.VISIBLE);

                    binding.lineChart.setData(generateLineChart(bmi));
                    binding.lineChart.invalidate();
                } else {
                    binding.llBmiChart.setVisibility(View.GONE);
                    binding.lineChart.setNoDataText("Please take HRA to view BMI chart");
                }
                fetchGraphData(DAILY, CONSUMEDGRAPH);
                getCalorieData();
            } else if (btnWorkout || btnTodayWorkout) {
                getCalorieBurnedData();
                fetchGraphData(DAILY, BURNEDGRAPH);
            } else if (btnTodayMeal) {
                getCalorieBurnedData();
                fetchGraphData(DAILY, BURNEDGRAPH);
            } else {
                fetchGraphData(DAILY, BURNEDGRAPH);
            }
        } else {
            fetchGraphData(DAILY, activityType);
        }
        btnDailySelected();
        if (btnWorkout) {
            btnNutrition = false;
            btnWorkout = true;
            btnTodayWorkout = true;
            btnTodayMeal = false;
        } else {
            btnTodayMeal = true;
            btnTodayWorkout = false;

            btnWorkout = false;
            btnNutrition = true;
        }

        getRewardsDashboardData();
        getAbsorbDashboard(tagName);

    }

    public void getAbsorbDashboard(String tagName) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetDashboardDataRequest dashboardDataRequest = new GetDashboardDataRequest(tagName, "");
        Call<GetDashboardDataResponse> call = apiInterfaceWyh.getAbsorbDashboard(SharedPref.getAuthToken(), dashboardDataRequest);
        Log.v("Url_Request_save", call.request().url() + "\n" + new Gson().toJson(dashboardDataRequest) + "\n" + SharedPref.getAuthToken());

        call.enqueue(new Callback<GetDashboardDataResponse>() {
            @Override
            public void onResponse(Call<GetDashboardDataResponse> call, Response<GetDashboardDataResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_dashboard_success));
                    List<GetDashboardDataResponse.Data.QucikRead> quickReadList = response.body().getData().getQucikReads();
                    healthTvList = response.body().getData().getHealthTv();
                    List<GetDashboardDataResponse.Data.Webinar> webinarList = response.body().getData().getWebinar();
                    tagNameList = response.body().getData().getTagName();
                    audioList = response.body().getData().getAudioFiles();

                    if (quickReadList.size() > 0) {
                        binding.llQuickRead.setVisibility(View.VISIBLE);
                        setQuickReadData(quickReadList);
                    } else {
                        binding.llQuickRead.setVisibility(View.GONE);
                    }
                    if (healthTvList.size() > 0) {
                        binding.llHealthTv.setVisibility(View.VISIBLE);
                        setHealthTvData(healthTvList);
                    } else {
                        binding.llHealthTv.setVisibility(View.GONE);
                    }
                    if (Objects.equals(activityType, MEDITATION) && audioList.size() > 0) {
                        binding.llMusicLib.setVisibility(View.VISIBLE);
                        setMusicLibData(audioList);
                    } else {
                        binding.llMusicLib.setVisibility(View.GONE);
                    }
                    if (webinarList.size() > 0) {
                        binding.llWebinar.setVisibility(View.VISIBLE);
                        setWebinarData(webinarList);
                    } else {
                        binding.llWebinar.setVisibility(View.GONE);
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_dashboard_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetDashboardDataResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_dashboard_failed));
            }
        });
    }


    private void setHealthTvData(List<GetDashboardDataResponse.Data.HealthTv> healthTvList) {
        List<GetDashboardDataResponse.Data.HealthTv> dataList = new ArrayList<>();
        if (healthTvList.size() > 6) {
            for (int i = 0; i < 6; i++) {
                dataList.add(healthTvList.get(i));
            }
        } else {
            dataList.addAll(healthTvList);
        }
        HealthHacksHealthTvAdapter absorbHealthTvAdapter = new HealthHacksHealthTvAdapter(context, dataList, new HealthHacksHealthTvAdapter.OnItemClickListener() {
            @Override
            public void onClick(int id, boolean isBookmark) {
                addVideoBookmark(isBookmark, id);
            }
        });
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
        LinearLayoutManager absorbHealthTvLinearLayoutManager = new LinearLayoutManager(context);
        absorbHealthTvLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvHealthTv.setLayoutManager(absorbHealthTvLinearLayoutManager);
        binding.rvHealthTv.setAdapter(absorbHealthTvAdapter);
        binding.rvHealthTv.setOnFlingListener(null);

        LinearSnapHelper absorbHealthTvLinearSnapHelper = new SnapHelperOneByOne();
        absorbHealthTvLinearSnapHelper.attachToRecyclerView(binding.rvHealthTv);


        int indicatorSize;

        if (healthTvList.size() > 6) {
            binding.ivHealthTVMore.setVisibility(View.VISIBLE);
            binding.rlHealthTv.setEnabled(true);
            indicatorSize = (int) Math.ceil(6.0 / 2.0);
        } else {
            binding.ivHealthTVMore.setVisibility(View.GONE);
            binding.rlHealthTv.setEnabled(false);
            indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(healthTvList.size())) / 2.0);
        }

        LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(context);
        absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvHealthTvIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
        binding.rvHealthTvIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
        binding.rvHealthTvIndicator.setHasFixedSize(true);

        binding.rvHealthTv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionHealthTv = absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionHealthTv = absorbHealthTvLinearLayoutManager.findFirstVisibleItemPosition();
                    absorbHealthTvIndicatorsAdapter.updateSelectedIndex(positionHealthTv);
                }
            }
        });
    }

    private void setMusicLibData(List<GetDashboardDataResponse.Data.AudioFiles> healthTvList) {
        List<GetDashboardDataResponse.Data.AudioFiles> dataList = new ArrayList<>();
        if (healthTvList.size() > 6) {
            for (int i = 0; i < 6; i++) {
                dataList.add(healthTvList.get(i));
            }
        } else {
            dataList.addAll(healthTvList);
        }
        MusicLibAdapter absorbHealthTvAdapter = new MusicLibAdapter(context, dataList);
        LinearLayoutManager absorbHealthTvLinearLayoutManager = new LinearLayoutManager(context);
        absorbHealthTvLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvMusicLib.setLayoutManager(absorbHealthTvLinearLayoutManager);
        binding.rvMusicLib.setAdapter(absorbHealthTvAdapter);
        binding.rvMusicLib.setOnFlingListener(null);

        LinearSnapHelper absorbHealthTvLinearSnapHelper = new SnapHelperOneByOne();
        absorbHealthTvLinearSnapHelper.attachToRecyclerView(binding.rvMusicLib);


        int indicatorSize;

        if (healthTvList.size() > 6) {
            binding.ivMusicLibraryMore.setVisibility(View.VISIBLE);
            binding.rlMusicLib.setEnabled(true);
            indicatorSize = (int) Math.ceil(6.0 / 2.0);
        } else {
            binding.ivMusicLibraryMore.setVisibility(View.GONE);
            binding.rlMusicLib.setEnabled(false);
            indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(healthTvList.size())) / 2.0);
        }

        LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(context);
        absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvMusicLibIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
        binding.rvMusicLibIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
        binding.rvMusicLibIndicator.setHasFixedSize(true);

        binding.rvMusicLib.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionMusicLib = absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionMusicLib = absorbHealthTvLinearLayoutManager.findFirstVisibleItemPosition();
                    absorbHealthTvIndicatorsAdapter.updateSelectedIndex(positionMusicLib);
                }
            }
        });
    }

    private void setWebinarData(List<GetDashboardDataResponse.Data.Webinar> webinarList) {
        HealthHacksWebinarAdapter absorbWebinarAdapter = new HealthHacksWebinarAdapter(context, webinarList);
//        GridLayoutManager absorbWebinarLinearLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        LinearLayoutManager absorbWebinarLinearLayoutManager = new LinearLayoutManager(context);
        absorbWebinarLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
//        LinearLayoutManager absorbWebinarLinearLayoutManager = new LinearLayoutManager(context);
//        absorbWebinarLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvWebinar.setLayoutManager(absorbWebinarLinearLayoutManager);
        binding.rvWebinar.setAdapter(absorbWebinarAdapter);
        binding.rvWebinar.setOnFlingListener(null);

        LinearSnapHelper absorbWebinarLinearSnapHelper = new SnapHelperOneByOne();
        absorbWebinarLinearSnapHelper.attachToRecyclerView(binding.rvWebinar);

        LinearLayoutManager absorbWebinarLinearLayoutManager1 = new LinearLayoutManager(context);
        absorbWebinarLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter absorbWebinarIndicatorsAdapter = new IndicatorsAdapter(context, webinarList.size() / 2, 0);
        binding.rvWebinarIndicator.setAdapter(absorbWebinarIndicatorsAdapter);
        binding.rvWebinarIndicator.setLayoutManager(absorbWebinarLinearLayoutManager1);
        binding.rvWebinarIndicator.setHasFixedSize(true);

        binding.rvWebinar.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (absorbWebinarLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionWebinar = absorbWebinarLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionWebinar = absorbWebinarLinearLayoutManager.findFirstVisibleItemPosition();
                    absorbWebinarIndicatorsAdapter.updateSelectedIndex(positionWebinar);
                }
            }
        });
    }

    private void setQuickReadData(List<GetDashboardDataResponse.Data.QucikRead> quickReadList) {

        List<GetDashboardDataResponse.Data.QucikRead> dataList = new ArrayList<>();
        if (quickReadList.size() > 6) {
            for (int i = 0; i < 6; i++) {
                dataList.add(quickReadList.get(i));
            }
        } else {
            dataList.addAll(quickReadList);
        }
        //Quick Reads
        TrendsQuickReadsAdapter trendsQuickReadsAdapter = new TrendsQuickReadsAdapter(context, dataList, false,
                false, false, true, new TrendsQuickReadsAdapter.OnItemClickListener() {
            @Override
            public void onClick(String articleCode, boolean isBookmark) {
                addBookmark(articleCode, isBookmark);
            }
        });
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(context);
        linearLayoutManager4.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvQuickReads.setLayoutManager(linearLayoutManager4);
        binding.rvQuickReads.setAdapter(trendsQuickReadsAdapter);


        int indicatorSize;

        if (quickReadList.size() > 6) {
            indicatorSize = (int) Math.ceil(6.0 / 2.0);
        } else {
            indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(quickReadList.size())) / 2.0);
        }
        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(context);
        linearLayoutManager5.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter indicatorsAdapter2 = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvQuickReadsIndicator.setAdapter(indicatorsAdapter2);
        binding.rvQuickReadsIndicator.setLayoutManager(linearLayoutManager5);
        binding.rvQuickReadsIndicator.setHasFixedSize(true);

        binding.rvQuickReads.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (linearLayoutManager4.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionReads = linearLayoutManager4.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionReads = linearLayoutManager4.findFirstVisibleItemPosition();
                    indicatorsAdapter2.updateSelectedIndex(positionReads);
                }
            }
        });
        /*
        //Quick Reads
        AbsorbQuickReadsAdapter absorbQuickReadsAdapter = new AbsorbQuickReadsAdapter(context, quickReadList);
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
        LinearLayoutManager quickReadLinearLayoutManager = new LinearLayoutManager(context);
        quickReadLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvQuickReads.setLayoutManager(quickReadLinearLayoutManager);
        binding.rvQuickReads.setAdapter(absorbQuickReadsAdapter);
        binding.rvQuickReads.setOnFlingListener(null);

        LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvQuickReads);

        LinearLayoutManager quickReadLinearLayoutManager1 = new LinearLayoutManager(context);
        quickReadLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter quickReadIndicatorsAdapter = new IndicatorsAdapter(context, quickReadList.size() / 3, 0);
        binding.rvQuickReadsIndicator.setAdapter(quickReadIndicatorsAdapter);
        binding.rvQuickReadsIndicator.setLayoutManager(quickReadLinearLayoutManager1);
        binding.rvQuickReadsIndicator.setHasFixedSize(true);

        binding.rvQuickReads.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (quickReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionReads = quickReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionReads = quickReadLinearLayoutManager.findFirstVisibleItemPosition();
                    quickReadIndicatorsAdapter.updateSelectedIndex(positionReads);
                }
            }
        });*/
    }


    private LineData generateLineChart(double bmi) {
        binding.lineChart.setTouchEnabled(true);
        binding.lineChart.setDragEnabled(false);
        binding.lineChart.setScaleEnabled(false);
        binding.lineChart.setScaleXEnabled(false);
        binding.lineChart.setScaleYEnabled(false);
        binding.lineChart.setPinchZoom(false);
        binding.lineChart.setDoubleTapToZoomEnabled(false);
        binding.lineChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        binding.lineChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //binding.lineChart.setBackgroundColor(setColor2);

        YAxis rightAxis1 = binding.lineChart.getAxisRight();
        rightAxis1.setEnabled(false);
        rightAxis1.setDrawLabels(false);
        rightAxis1.setDrawGridLines(false);
        rightAxis1.setAxisMinimum(0);
        rightAxis1.setDrawAxisLine(true);
        /*rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(true);
        rightAxis.setAxisMinimum(0f);*/ // VitalsActivity.this replaces setStartAtZero(true)

        YAxis leftAxis1 = binding.lineChart.getAxisLeft();
//        leftAxis1.setAxisMaxValue(200);
        //leftAxis1.setDrawGridLines(false);
        leftAxis1.setDrawAxisLine(true);
        leftAxis1.setDrawLabels(true);
        leftAxis1.setAxisMinimum(0); // VitalsActivity.this replaces setStartAtZero(true)
        leftAxis1.setEnabled(true);
        leftAxis1.setDrawLimitLinesBehindData(false);
        leftAxis1.setDrawGridLines(false);

        XAxis xAxis1 = binding.lineChart.getXAxis();
        xAxis1.setDrawGridLines(false);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setDrawAxisLine(true);
        xAxis1.setGranularity(1f);

//        binding.lineChart.getAxisLeft().setAxisMaxValue(300);

        binding.lineChart.setExtraOffsets(0f, 0f, 0f, 16f);
        binding.lineChart.setDrawBorders(false);
        binding.lineChart.getDescription().setEnabled(false);

        ArrayList<Entry> entries1 = new ArrayList<>();
        ArrayList<Entry> entries2 = new ArrayList<>();
        entries1.clear();
        entries2.clear();

        String json = "";

//        FetchStepsResponse fetchResponse = new Gson().fromJson(json, FetchStepsResponse.class);

//        entries1.add(new Entry(20f, 0.0F));
//        entries1.add(new Entry(30f, 3.0F));
        List<String> days = new ArrayList<>();
        List<String> reverseDays = new ArrayList<>();
        /*SimpleDateFormat sdf = new SimpleDateFormat("EE");
        for (int i = 7; i >= 1; i--) {
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, i);
            String day = sdf.format(calendar.getTime());
            days.add(day);
        }

        for (int i = days.size() - 1; i >= 0; i--) {
            reverseDays.add(days.get(i));
        }*/

        reverseDays.add("Mon");
        reverseDays.add("Tue");
        reverseDays.add("Wed");
        reverseDays.add("Thu");
        reverseDays.add("Fri");
        reverseDays.add("Sat");
        reverseDays.add("Sun");

        binding.lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(reverseDays));

//        Calendar calendar = Calendar.getInstance();
//        int day = calendar.get(Calendar.DAY_OF_WEEK);
//        int count = day - 1;

        for (int i = 6; i >= 0; i--) {
//            if (i > count) {
            entries1.add(new Entry(i, (float) bmi));
//            } else {
//                entries1.add(new Entry(i, 0f));
//            }
        }

//        for (int j = 0; j <= 7; j++) {
//            entries1.add();
//        }
        Collections.sort(entries1, new EntryXComparator());

//        entries1.add(new Entry(1, (float) bmi));
//        entries1.add(new Entry(2, (float) bmi));
//        entries1.add(new Entry(3, (float) bmi));
//        entries1.add(new Entry(4, (float) bmi));
//        entries1.add(new Entry(5, (float) bmi));
//        entries1.add(new Entry(6, (float) bmi));

//        int todayDate = entries1.size();
//        for (int i = 0; i < todayDate; i++) {
//            entries1.get(i).setY((float) bmi);
//        }
        //Collections.reverse(bloodPressureData);
//        entries1.addAll();
//        entries2.addAll(getDailyData(fetchResponse, true));

        if (bmi < 35) {
            binding.lineChart.getAxisLeft().setAxisMaxValue(35);
        } else {
            binding.lineChart.getAxisLeft().setAxisMaxValue((float) bmi);
        }

        LimitLine limitLine = new LimitLine(Float.parseFloat("24.9"), "Ideal BMI Max");
        limitLine.setLineColor(context.getResources().getColor(R.color.dark_pink));
        limitLine.setLineWidth(0.1f);
        limitLine.setTextColor(R.color.black);
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        leftAxis1.addLimitLine(limitLine);

        LimitLine limitLine2 = new LimitLine(Float.parseFloat("18.5"), "Ideal BMI Min");
        limitLine2.setLineColor(context.getResources().getColor(R.color.dark_pink));
        limitLine2.setLineWidth(0.1f);
        limitLine2.setTextColor(R.color.black);
        limitLine2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        leftAxis1.addLimitLine(limitLine2);

//        if (!isLimitsGenerated) {
//            setLimitLinesToGraph(leftAxis1, avgUserSBP, highestValue1.getY());
//        }


        int setColor3 = context.getResources().getColor(R.color.pink);
        int transparentColor = getResources().getColor(android.R.color.transparent);
        LineDataSet set1, set2 = null;
        set1 = new LineDataSet(entries1, "BMI");
        set1.setColor(setColor3);
        set1.setDrawValues(false);
        set1.setMode(LineDataSet.Mode.LINEAR);
        //set2.enableDashedLine(0.0f, 0.0f, 0.0f);
        set1.setLineWidth(1.5f);
        set1.disableDashedLine();
        set1.setDrawCircles(true);
        set1.setCircleColor(setColor3);
        set1.setCircleRadius(2f);
        set1.setCircleHoleColor(setColor3);

        int blackColor = context.getResources().getColor(R.color.pink);
        if (set2 != null) {
            set2.setDrawValues(false);
            set2.setMode(LineDataSet.Mode.LINEAR);
            //set2.enableDashedLine(0.0f, 0.0f, 0.0f);
            set2.setLineWidth(0f);
            set2.disableDashedLine();
            set2.setDrawCircles(true);
            set2.setCircleColor(blackColor);
            set2.setCircleRadius(1f);
            set2.setCircleHoleColor(blackColor);
        }

        binding.lineChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);

        LineData lineData;
        LegendEntry l1, l2;

        lineData = new LineData(set1);
            /*l1 = new LegendEntry(context.getResources().getString(R.string.weight_kgs),
                    Legend.LegendForm.SQUARE, 10f, 2f, null, setColor3);
            binding.lineChart.getLegend().setCustom(new LegendEntry[]{l1});*/

        lineData.setValueTextColor(Color.WHITE);
        lineData.setValueTextSize(9f);

        xAxis1.setAxisMinimum(-axisPadding);
        xAxis1.setAxisMaxValue(lineData.getXMax() + axisPadding);

        MyMarkerView mv = new MyMarkerView(context, R.layout.barchart_marker_view_layout);
        binding.lineChart.setMarker(mv);

        return lineData;
    }

    private void getCalorieData() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetCalorieDataRequest request = new GetCalorieDataRequest(SharedPref.getAesUuid(), todayDateInFormat("yyyy-MM-dd"));
        Call<CalorieDataResponse> call = apiInterfaceWyh.getCalorieConsumedDashboardData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CalorieDataResponse>() {
            @Override
            public void onResponse(Call<CalorieDataResponse> call, Response<CalorieDataResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.diet_tracker_get_dashboard_data_success));
                    if (btnNutrition) {
                        binding.calIntakeOrBurn.setText("Calories Consumed: " + response.body().getData().get(0).getConsumedCalorie());
                        binding.firstTvCount.setText(response.body().getData().get(0).getConsumedProtein() + " gm");
                        binding.secondTvCount.setText(response.body().getData().get(0).getConsumedFats() + " gm");
                        binding.thirdTvCount.setText(response.body().getData().get(0).getConsumedCarbs() + " gm");
                        if (TextUtils.isEmpty(SharedPref.getBmr())) {
                            binding.calorieCircleViewScore.setMax(2000);
                        } else {
                            binding.calorieCircleViewScore.setMax(Integer.parseInt(SharedPref.getBmr()));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            binding.calorieCircleViewScore.setProgress(response.body().getData().get(0).getConsumedCalorie(), true);
                        } else
                            binding.calorieCircleViewScore.setProgress(response.body().getData().get(0).getConsumedCalorie());
                        binding.tvCalorie.setText("" + response.body().getData().get(0).getConsumedCalorie());
                    } else if (btnWorkout) {
//                        binding.calIntakeOrBurn.setText(response.body().getData().get(0).getConsumedCalorie());
                        binding.calIntakeOrBurn.setVisibility(View.GONE);
                    }
//                    else
//                    {
//
//                    }
                  /*  binding.firstProgressBar.setMax(response.body().getData().get(0).getConsumedCalorie());
                    binding.firstProgressBar.setProgress(response.body().getData().get(0).getConsumedProtein());

                    binding.secondProgressBar.setMax(response.body().getData().get(0).getConsumedCalorie());
                    binding.secondProgressBar.setProgress(response.body().getData().get(0).getConsumedFats());

                    binding.thirdProgressBar.setMax(response.body().getData().get(0).getConsumedCalorie());
                    binding.thirdProgressBar.setProgress(response.body().getData().get(0).getConsumedFats());

                    binding.fourthProgressBar.setMax(response.body().getData().get(0).getConsumedCalorie());
                    binding.fourthProgressBar.setProgress(response.body().getData().get(0).getConsumedCarbs());*/

//                    fetchGraphData();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.diet_tracker_get_dashboard_data_failed));
                }
            }

            @Override
            public void onFailure(Call<CalorieDataResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.diet_tracker_get_dashboard_data_failed));
            }
        });
    }


    private void getCalorieBurnedData() {
        /*if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetCalorieDataRequest request = new GetCalorieDataRequest(SharedPref.getAesUuid(), todayDateInFormat("yyyy-MM-dd"));
        Call<CalorieBurnedResponse> call = apiInterfaceWyh.getCalorieBurnedDashboardData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CalorieBurnedResponse>() {
            @Override
            public void onResponse(Call<CalorieBurnedResponse> call, Response<CalorieBurnedResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    if (btnWorkout || btnTodayWorkout) {
                        binding.calIntakeOrBurn.setVisibility(View.VISIBLE);
                        binding.calIntakeOrBurn.setText("Calories Burned: " + response.body().getData().get(0).getCaloriesBurned());
                    } else if (btnNutrition) {
//                        binding.calIntakeOrBurn.setText(response.body().getData().get(0).getConsumedCalorie());
                        binding.calIntakeOrBurn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CalorieBurnedResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

            }
        });*/
        if (btnWorkout || btnTodayWorkout) {
            binding.calIntakeOrBurn.setVisibility(View.VISIBLE);
            binding.calIntakeOrBurn.setText("Calories Burned: " + (int) Math.round(waterIntake));
        } else if (btnNutrition) {
//                        binding.calIntakeOrBurn.setText(response.body().getData().get(0).getConsumedCalorie());
            binding.calIntakeOrBurn.setVisibility(View.GONE);
        }
    }

    private void btnDailyView() {
        binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_round_btn_light_red));
        binding.btnDaily.setTextColor(getResources().getColor(R.color.white));
        binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnWeekly.setTextColor(getResources().getColor(R.color.black));
        binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnMonthly.setTextColor(getResources().getColor(R.color.black));
        binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnQuarterly.setTextColor(getResources().getColor(R.color.black));

    }

    private void addWeight() {
        if (!binding.tvGlassAmt.getText().toString().isEmpty()) {
            if (Double.parseDouble(binding.tvGlassAmt.getText().toString()) < 25 || Double.parseDouble(binding.tvGlassAmt.getText().toString()) > 200) {
                Toast.makeText(context, "Please enter valid weight", Toast.LENGTH_SHORT).show();
                binding.tvGlassAmt.requestFocus();
                CommonUtils.showKeyboard(TrendsActivity.this);
            } else {
                double weight = Double.parseDouble(binding.tvGlassAmt.getText().toString());
                if (weight > 0.0) {
                    SharedPref.putWeight(String.valueOf(weight));
                    UploadWeightData(weight);
                    fetchGraphData(DAILY, activityType);
                    btnDailyView();
                }
            }
        } else {
            binding.tvGlassAmt.requestFocus();
        }
    }

    private void addWaterGlasses() {
        glasses = Integer.parseInt(binding.tvGlassAmt.getText().toString());
        if (glasses > 0) {
            UploadActivityData(WATER, glasses + waterIntake);
//            if (btnDaily) {
            fetchGraphData(DAILY, activityType);
            btnDailyView();
            display(0);
            glasses = 0;
        } else {
            Toast.makeText(context, "Please add water intake", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWebView(String url) {
        Intent i = new Intent(context, WebActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("comingFrom", "");
        i.putExtra("Url", url);
        context.startActivity(i);
    }

    private void btnDailySelected() {
        btnDaily = true;
        btnWeekly = false;
        btnMonthly = false;
        btnQuarterly = false;
    }

    private void decreaseNumber() {
        glasses = (int) Double.parseDouble(binding.tvGlassAmt.getText().toString());
        if (glasses > 0) {
            display(glasses - 1);
        }
    }

    private void decreaseWeight() {
        if (!binding.tvGlassAmt.getText().toString().isEmpty()) {
            Double weight = Double.parseDouble(binding.tvGlassAmt.getText().toString());
            if (weight > 0) {
                binding.tvGlassAmt.setText(format.format((weight - 1)));
            }
        }
    }


    private void display(int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.progressBar.setProgress(i, true);
        } else {
            binding.progressBar.setProgress(i);
        }
        binding.tvGlassAmt.setText(format.format(i));
        if (i >= 16) {
            binding.tvGlassAmt.setTextColor(getResources().getColor(R.color.white));
        } else {
            binding.tvGlassAmt.setTextColor(getResources().getColor(R.color.dark_pink));
        }
    }

    private void increaseNumber() {
        glasses = (int) Double.parseDouble(binding.tvGlassAmt.getText().toString());
        int actualGlasses = glasses + 1;
        int currentDayWaterCount = (int) (actualGlasses + waterIntake);
        if (actualGlasses > waterIntakeGoal || waterIntake > waterIntakeGoal || currentDayWaterCount > waterIntakeGoal) {
            if (!waterIntakeAllowed) {
                waterIntakeDialoge(context);
                return;
            } else {
                if (glasses < 30) {
                    display(glasses + 1);
                }
            }
        }
        if (glasses < 30) {
            display(glasses + 1);
        }
    }

    private void increaseWeight() {
        if (!binding.tvGlassAmt.getText().toString().isEmpty()) {
            Double weight = Double.parseDouble(binding.tvGlassAmt.getText().toString());
            if (weight < 200) {
                binding.tvGlassAmt.setText(format.format((weight + 1)));
            }
        } else {
            binding.tvGlassAmt.setText("0");
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
                APILogs.INSTANCE.activityTracker("A_DB_BI_AZ_LS_H2O_Plus_Yes", context);
                customYesNoDialog.dismiss();
                waterIntakeAllowed = true;
            });

            customYesNoDialog.binding.btnCancel.setOnClickListener(view -> {
                APILogs.INSTANCE.activityTracker("A_DB_BI_AZ_LS_H2O_Plus_No", context);
                customYesNoDialog.dismiss();
                waterIntakeAllowed = false;
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UploadActivityData(String activityName, double countOrTime) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        AddReminderDataRequest request = new AddReminderDataRequest(activityName, (int) Math.round(countOrTime), todayDateInFormat("yyyy-MM-dd"));
        Call<CommonSuccessResponse> call = apiInterfaceWyh.addActivityData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("response", new Gson().toJson(response.body()));
                Log.d("response", new Gson().toJson(response.code()));
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.log_reminder_success));
                    if (response.body().getRewards() != null && response.body().getRewards().getReward() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, response.body().getRewards().getReward()));
                    }

                    if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, response.body().getRewards().getBonusRewards()));

                    }

                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));

                    }

                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                    }

                    if (response.body().getSpinTheWheelRewardsModel() != null) {
                        spinRewardData = response.body().getSpinTheWheelRewardsModel();
                        getSpinRewardPopup(spinRewardData);
                    } else if (checkIsFromQuizqathon()) {
                        //QuizReward Api Call
                        FetchQuizReward();
//                        quizathonRewardData = response.body().getQuizathonRewardData();
//                        getQuizathonRewardPopup(quizathonRewardData);
                    } else if (response.body().getFeedbackDetails() != null && response.body().getFeedbackDetails().getFeedbackModel() != null && response.body().getFeedbackDetails().getStarConfig() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(FeedbackPOPUP, ""));
                        NewDashboardHelper.Companion.setFeedbackResponseData(response.body().getFeedbackDetails());
                    }


                    Toast.makeText(TrendsActivity.this, "Data Added Successfully!!!", Toast.LENGTH_SHORT).show();
                    if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0) {
                        showRewardsPopupDialogBox();
                    } else {
                        if (activityName.equals(WATER) || activityName.equals(MEDITATION)) {
                            fetchRewards();
                        }
                    }

                    fetchGraphData(DAILY, activityType);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.log_reminder_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.log_reminder_failed));
            }
        });
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

    private void fetchRewards() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        String eventType;
        if (activityType.equals(WATER))
            eventType = "waterintake";
        else
            eventType = "meditate";
        FetchRewardsRequest request = new FetchRewardsRequest(eventType);
        Call<FetchRewardsResponse> call = apiInterfaceWyh.fetchRewards(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<FetchRewardsResponse>() {
            @Override
            public void onResponse(Call<FetchRewardsResponse> call, Response<FetchRewardsResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getData() != null) {
                        if (response.body().getData().size() > 0) {
                            showRewardsPopupNew(response.body().getData().get(0).getPopupMessage(), context);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FetchRewardsResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    private void UploadWeightData(Double weight) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        SharedPref.putWeight(String.valueOf(weight));
        AddWeightRequest request = new AddWeightRequest(weight, todayDateInFormat("yyyy-MM-dd"));
        Call<CommonSuccessResponse> call = apiInterfaceWyh.addWeight(SharedPref.getAuthToken(), request);
        Log.d("request", new Gson().toJson(call.request()));
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("response", new Gson().toJson(response.body()));
                Log.d("response", new Gson().toJson(response.code()));
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    NewDashboardHelper.Companion.getPopUpShowModels().clear();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.log_reminder_success));
                    if (response.body().getRewards() != null && response.body().getRewards().getReward() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, response.body().getRewards().getReward()));

                    }

                    if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, response.body().getRewards().getBonusRewards()));

                    }


                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));

                    }

                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                    }

                    if (response.body().getFeedbackDetails() != null && response.body().getFeedbackDetails().getFeedbackModel() != null && response.body().getFeedbackDetails().getStarConfig() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(FeedbackPOPUP, ""));
                        NewDashboardHelper.Companion.setFeedbackResponseData(response.body().getFeedbackDetails());
                    }

                    showRewardsPopupDialogBox();
                    Toast.makeText(TrendsActivity.this, "Data Added Successfully!!!", Toast.LENGTH_SHORT).show();
                    fetchGraphData(DAILY, activityType);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.log_reminder_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.log_reminder_failed));
            }
        });
    }

    private void showRewardsPopup(String rewards) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        ScratchCardPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.scratch_card_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvPoints.setText(points);


        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, RewardsActivity.class);
            context.startActivity(intent);
        });
        binding.scratchView.onFullReveal();
        binding.scratchView.setScratchListener(TrendsActivity.this);

        Rect displayRectangle = new Rect();
        Window window = ((TrendsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void showRewardsPopupNew(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
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
        });

        binding.scratchView.onFullReveal();

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(TrendsActivity.this);

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
        });
        binding.scratchView.setScratchListener(TrendsActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((TrendsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusRewards.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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
                case "FeedbackPOPUP":
                    FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                    instance.showPopUpFeedback(TrendsActivity.this, NewDashboardHelper.Companion.getFeedbackResponseData());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        }
    }

    private void getGoogleFitData() {
        //getFitPermission();
        getSteps();
        getSleep();
        getStand();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                /*String[] permissions = {Manifest.permission.ACTIVITY_RECOGNITION};
                ActivityCompat.requestPermissions(HomeActivity.this, permissions, REQUEST_CODE_ACTIVITY_RECOGNITION);*/
            } else {
                fetchStepsData();
            }
        } else {
            fetchStepsData();
        }

        Intent intent1 = new Intent(getApplicationContext(), SendDataToServerReceiver.class);
        getApplicationContext().sendBroadcast(intent1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalleryOnly();
                // Showing the toast message
                Toast.makeText(context, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_CODE_ACTIVITY_RECOGNITION) {
            // If request is cancelled, the result arrays are empty.
            //Log.v("Data", "onRequestPermisson called");
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                watchYourHealth.connectAPIClient();
//                SharedPreference.putGoogleFitConnection(true);
                getSteps();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(this, "Physical activity permission required to use step counts.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        watchYourHealth.getGoogleFitPermission(requestCode, resultCode);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                SharedPreference.init(this);
                SharedPreference.putGoogleFitConnection(true);
                getSteps();
                fetchGraphData(DAILY, activityType);
                Toast.makeText(this, "GoogleFit Connected Successfully!! ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "GoogleFit Connection Failed..", Toast.LENGTH_SHORT).show();
            }
        }
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<Image> image2 = ImagePicker.getImages(data);
            for (int i = 0; i < image2.size(); i++) {
                FileData fileData = new FileData();
                fileData.setPath(getFilePathFromImage(image2.get(i)));
                long singleFileSize = getFileSizeFromPath(fileData.getPath());

                if (singleFileSize > 5000000) {
                    Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                } else {
                    fileDataList.clear();
                    fileDataList.add(fileData);
                }
            }
            if (fileDataList.size() > 0) {
                if (uploadFileActivity != null) {
                    parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                    uploadFileWithContent(parts, uploadFileActivity);
                }
            }
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }

    private void getFitPermission() {
        Handler handler = new Handler();
        if (!SharedPref.getFitBitConnection()) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                            REQUEST_CODE_ACTIVITY_RECOGNITION);
                } else {
                    //Log.v("Data", "Inside Loop MONTH");
                    watchYourHealth.connectAPIClient();
                    boolean googleFitConnection = SharedPreference.getGoogleFitConnection();
                    if (googleFitConnection) {
                        //new ViewStepsCount(WatchYourHealth.MONTH).execute();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 45 seconds
                                //new ViewStepsCount(WatchYourHealth.MONTH).execute();
                                new CoroutineClass().runBackGroundTask(WatchYourHealth.MONTH, context);
                                handler.postDelayed(this, 10000 * 30);
                            }
                        }, 10000 * 30);
                    }
                }
            } else {
                watchYourHealth.connectAPIClient();
                boolean googleFitConnection = SharedPreference.getGoogleFitConnection();
                if (googleFitConnection) {
                    /*if (!SharedPref.getGoogleFitBadge()) {
                        assignBadge("3", "tech", "GoogleFit", "Health");
                    }*/
                    //new ViewStepsCount(WatchYourHealth.MONTH).execute();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Log.v("Data", "Inside Loop MONTH");
                            //Do something after 45 seconds
                            //new ViewStepsCount(WatchYourHealth.MONTH).execute();
                            new CoroutineClass().runBackGroundTask(WatchYourHealth.MONTH, context);
                            handler.postDelayed(this, 10000 * 30);
                        }
                    }, 10000 * 30);
                }
            }
        }
    }

    private void fetchStepsData() {
        final Handler handler2 = new Handler();
        handler2.postDelayed(() -> {
            SharedPreference.init(context);
            boolean googleFitConnection = SharedPreference.getGoogleFitConnection();
            //Log.v("Data", "Permission : " + googleFitConnection);
            /*if (!SharedPref.getGoogleFitBadge()) {
                assignBadge("3", "tech", "GoogleFit", "health");
            }*/
            if (googleFitConnection) {
                if (!SharedPreference.getAllStepDataSync()) {
                    //new ViewStepsCount(WatchYourHealth.YEAR).execute();
                    new CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR, context);
                } else {
                    getDaysSteps();
                    //new ViewStepsCount(WatchYourHealth.LAST_DATA).execute();
                    new CoroutineClass().runBackGroundTask(WatchYourHealth.LAST_DATA, context);
                }
            }
        }, 10000 * 30);
    }

    private void getSleep() {
        //watchYourHealth = new WatchYourHealth(HomeActivity.this);
        int sleep = (int) Math.round(activeHrTodays);
        String sleepFormat = numberFormat.format(activeHrTodays);
        /*if (SharedPreference.getGoogleFitConnection()) {
            sleep = watchYourHealth.getSleep(Constants.SOURCE_GOOGLEFIT, CommonUtils.todayDate());
        }*/
        if (sleep != 0) {
            String sleepHourCount = convertMinutesIntoHour(sleep);
            binding.tvSleepHrs.setText(sleepFormat);
            String sleepHour = convertMinutesIntoHourInteger(sleep);
            binding.tvSleepHr.setText(sleepFormat);

            binding.circleViewScore.setMax(8);
            binding.circleViewScore.setProgress(sleep);
        } else {
        }
    }

    @Override
    public void onScratchComplete() {
        if (quizathonRewardData != null) {
            QuizRewardDialog.INSTANCE.QuizScratchCard(this, quizathonRewardData.getTransId(), true);
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i > 20) {
            {
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

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (context == null) {
            context = this;
        }
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
        } else {
            openGalleryOnly();
        }

    }

    public void openGalleryOnly() {
        ImagePicker.create(this)
                .includeVideo(false)
                .imageDirectory("Camera")
                .enableLog(true)
                .includeAnimation(true)
                .limit(1)
                .showCamera(true)
                .start();
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String path) {
        File file = new File(path);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private long getFileSizeFromPath(String filePath) {
        if (filePath == null)
            return 0;
        File file = new File(filePath);
        return file.length();
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

    private void activitiesRedirection(LevelActivity levelActivity) {
        if (levelActivity.getRedirectTo() != null) {
            switch (levelActivity.getRedirectTo().toLowerCase()) {
                case "hra":
                    Intent intent;
                    GetAnalysisResponse getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
                    if (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0) {
                        intent = new Intent(context, HRAAnalysisActivity.class);
                    } else {
                        intent = new Intent(context, HRAQuestionsActivity.class);
                    }
                    startActivity(intent);
                    break;
                case "ira":
                    IRAHealthScoreResponse iraHealthScoreResponse = new Gson().fromJson(SharedPref.getIRAHealthData(), IRAHealthScoreResponse.class);
                    String healthScore = "";
                    if (iraHealthScoreResponse != null && iraHealthScoreResponse.getIraHealthScoreData() != null)
                        healthScore = iraHealthScoreResponse.getIraHealthScoreData().getPlaySports();
                    if (!healthScore.equals("0") && !healthScore.equals("")) {
                        intent = new Intent(context, IRAAnalysisActivity.class);
                    } else {
                        intent = new Intent(context, IraActivity.class);
                        intent.putExtra("from", IRA_STATUS_COMPLETED);
                    }
                    startActivity(intent);
                    break;
                case "blogread":
                    Intent intent1 = new Intent(context, QuickReadDashboard.class);
                    startActivity(intent1);
                    break;
                case "waterintake":
                    Intent intent2 = new Intent(context, TrendsActivity.class);
                    intent2.putExtra("activityType", WATER);
                    startActivity(intent2);
                    break;
                case "meditate":
                    Intent intent3 = new Intent(context, TrendsActivity.class);
                    intent3.putExtra("activityType", MEDITATION);
                    startActivity(intent3);
                    break;
                case "quiz":
                    Intent intent4 = new Intent(context, QuizathonViewAllActivity.class);
                    intent4.putExtra("type", "quiz");
                    intent4.putExtra("quiz_cat", "All");
                    intent4.putExtra("name", "Play and Learn");
                    startActivity(intent4);
                    break;
                case "webinars":
                case "healthtv":
                    Intent intent5 = new Intent(context, HealthHacksActivity.class);
                    startActivity(intent5);
                    break;
                case "ehr":
                    Intent intent6 = new Intent(context, EhrActivity.class);
                    startActivity(intent6);
                    break;
                case "syncdevice":
                    Intent intent7;
                    intent7 = new Intent(context, SyncDeviceActivity.class);
                    startActivity(intent7);
                    /*if(SharedPref.getGoogleFitStatus()){

                    }*/
                    break;
                case "invite":
                    intent7 = new Intent(context, ContactsActivityNew.class);
                    intent7.putExtra("isFromHRA", true);
                    intent7.putExtra("comingFrom", "");
                    startActivity(intent7);
                    break;
                case "journal":
                    intent7 = new Intent(context, AddDiaryActivity.class);
                    intent7.putExtra("title", "");
                    intent7.putExtra("content", "");
                    intent7.putExtra("date", "");
                    intent7.putExtra("came_from", "add");
                    intent7.putExtra("id", 0);
                    intent7.putExtra("imagePath", "");
                    startActivity(intent7);
                    break;
                case "unwind":
                    intent7 = new Intent(context, UnwindActivity.class);
                    intent7.putExtra("comingFrom", "trends");
                    startActivity(intent7);
                    break;
                case "calorieintake":
                    openWebView(getResources().getString(R.string.addFoodUrl));
                    break;
                case "calorieburn":
                    openWebView(getResources().getString(R.string.addExerciseUrl));
                    break;
            }
        }
    }

    private void startNewActivity(LevelActivity levelActivity, String cameFrom) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        StartRewardsActivityRequest request = new StartRewardsActivityRequest(levelActivity.getActivityID(), TAG_REWARD_EVENT);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.startRewardsActivity(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_success));
                    if (response.body().isSuccess()) {
                        Toast.makeText(context, "Activity has been started", Toast.LENGTH_SHORT).show();
                        getRewardsDashboardData();
                        levelActivity.setStarted(true);

                        if (levelActivitiesAdapter != null)
                            levelActivitiesAdapter.notifyDataSetChanged();
                        if (levelActivity.getRedirectTo() != null) {
                            levelActivityAlertDialog.dismiss();
                            activitiesRedirection(levelActivity);
                        } else {
                            if (Objects.equals(cameFrom, "JournalUpload")) {
                                checkActivityPopUpConditionsJournalUpload(levelActivity, activityEventType);
                            } else {
                                checkActivityPopUpConditions(levelActivity, activityEventType);
                            }
                        }
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadContentOnly(LevelActivity levelActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("ActivityUploads", "")
                .addFormDataPart("eventId", String.valueOf(levelActivity.getActivityID()))
                .addFormDataPart("eventType", levelActivity.getEventType())
                .addFormDataPart("eventName", levelActivity.getActivityTag())
                .addFormDataPart("yesNo", yesNoAnswer == null ? "" : yesNoAnswer)
                .addFormDataPart("journalContent", journalContent == null ? "" : journalContent)
                .addFormDataPart("eventCategory", TAG_REWARD_EVENT)
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "Rewards/EarnRewards")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.code() == 200 && response.body() != null) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(() -> Toast.makeText(context, getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show());
                    getRewardsDashboardData();
                }
            }
        });
    }

    private void uploadFileWithContent(List<MultipartBody.Part> parts, LevelActivity levelActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        String filename = fileDataList.get(0).getPath().substring(fileDataList.get(0).getPath().lastIndexOf("/") + 1);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
//                .addFormDataPart(fileUploadKey, filename,
//                        RequestBody.create(MediaType.parse("application/octet-stream"), new File(fileDataList.get(0).getPath())))
                .addFormDataPart("eventId", String.valueOf(levelActivity.getActivityID()))
                .addFormDataPart("eventType", levelActivity.getEventType())
                .addFormDataPart("eventName", levelActivity.getActivityTag())
                .addFormDataPart("yesNo", yesNoAnswer == null ? "" : yesNoAnswer)
                .addFormDataPart("journalContent", journalContent == null ? "" : journalContent)
                .addFormDataPart("eventCategory", TAG_REWARD_EVENT)
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "Rewards/EarnRewards")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(() -> Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show());
                    uploadFileActivity = null;
                    getRewardsDashboardData();
                }
            }
        });
    }

    @Override
    public void onItemClickActivities(LevelActivity levelActivity, int bgDrawable) {
        if (levelActivity.getEventType().equalsIgnoreCase("JournalUpload"))
            showCustomPopUpJournalUpload(levelActivity, bgDrawable);
        else if (levelActivity.getEventType().equalsIgnoreCase("journal"))
            showCustomPopUpJournal(levelActivity, bgDrawable);
        else
            showCustomPopUp(levelActivity, bgDrawable);
    }

    @Override
    public void onUploadClick(LevelActivity levelActivity) {

    }

    private void showCustomPopUp(LevelActivity levelActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        levelActivityBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_rewards, null, false);
        alertBuilder.setView(levelActivityBinding.getRoot());
        levelActivityAlertDialog = alertBuilder.create();
        levelActivityAlertDialog.setCancelable(false);
        if (!levelActivityAlertDialog.isShowing())
            levelActivityAlertDialog.show();

        Glide.with(context)
                .load(levelActivity.getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(levelActivityBinding.ivLogo);
        levelActivityBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        levelActivityBinding.tvTitle.setText(levelActivity.getActivityName());
        levelActivityBinding.tvDescription.setText(levelActivity.getActivityDesc());

        if (levelActivity.isIsCompleted()) {
            levelActivityBinding.btnPositive.setVisibility(View.GONE);
        }

        if (levelActivity.getWhatTo() != null) {
            levelActivityBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            levelActivityBinding.tvWhatToDo.setText(levelActivity.getWhatTo());
            levelActivityBinding.tvHowToDo.setText(levelActivity.getHowTo());
            levelActivityBinding.tvWhyToDo.setText(levelActivity.getWhyTo());
        }

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            levelActivityBinding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(levelActivityBinding.progressBar, levelActivity, null, levelActivityBinding.tvSteps);
        } else if (levelActivity.isIsCompleted()) {
            levelActivityBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                levelActivityBinding.progressBar.setProgress(100, true);
            } else
                levelActivityBinding.progressBar.setProgress(100);
        }

        activityEventType = levelActivity.getEventType();

        checkActivityPopUpConditions(levelActivity, activityEventType);

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        levelActivityAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void checkActivityPopUpConditions(LevelActivity levelActivity, String activityEventType) {

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            if (levelActivity.getRedirectTo() != null) {
                levelActivityBinding.btnPositive.setVisibility(View.VISIBLE);
                levelActivityBinding.btnPositive.setText("Complete");
            } else {
                levelActivityBinding.btnPositive.setVisibility(View.GONE);
            }
        }


        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("upload")) {
            levelActivityBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityBinding.btnPositive.setText("Upload");
           /* levelActivityBinding.btnPositive.setOnClickListener(view -> {
                fileUploadKey = "ActivityUploads";
                levelActivityAlertDialog.dismiss();
                uploadFileActivity = levelActivity;
                ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            });*/
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("yesno")) {
            levelActivityBinding.llYesNo.setVisibility(View.VISIBLE);
            levelActivityBinding.btnYes.setOnClickListener(view -> {
                levelActivityAlertDialog.dismiss();
                yesNoAnswer = "Yes";
                uploadContentOnly(levelActivity);
            });
            levelActivityBinding.btnNo.setOnClickListener(view -> {
                levelActivityAlertDialog.dismiss();
                yesNoAnswer = "No";
                uploadContentOnly(levelActivity);
            });
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("journal")) {
            levelActivityBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityBinding.btnPositive.setText("Submit");
            levelActivityBinding.llJournal.setVisibility(View.VISIBLE);
            levelActivityBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(levelActivityBinding.edtJournal.getText()) && validateLetters(levelActivityBinding.edtJournal.getText().toString())) {
                    levelActivityAlertDialog.dismiss();
                    journalContent = levelActivityBinding.edtJournal.getText().toString();
                    uploadContentOnly(levelActivity);
                } else {
                    levelActivityBinding.edtJournal.requestFocus();
                }
            });
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("JournalUpload")) {
            levelActivityBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityBinding.btnPositive.setText("Submit");
            levelActivityBinding.llJournal.setVisibility(View.VISIBLE);
            levelActivityBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(levelActivityBinding.edtJournal.getText()) && validateLetters(levelActivityBinding.edtJournal.getText().toString())) {
                    journalContent = levelActivityBinding.edtJournal.getText().toString();
                    fileUploadKey = "JournalUpload";
                    uploadFileActivity = levelActivity;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                    } else {
                        levelActivityAlertDialog.dismiss();
                        openGalleryOnly();
                    }
                } else {
                    levelActivityBinding.edtJournal.requestFocus();
                }
            });
        }

        levelActivityBinding.btnNegative.setOnClickListener(view -> {
            levelActivityAlertDialog.dismiss();
            getRewardsDashboardData();
        });

        levelActivityBinding.btnPositive.setOnClickListener(view1 -> {
//            levelActivityAlertDialog.dismiss();

            if (levelActivityBinding.btnPositive.getText() == "Upload" && activityEventType.equalsIgnoreCase("Upload")) {
                fileUploadKey = "ActivityUploads";
                uploadFileActivity = levelActivity;
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                } else {
                    levelActivityAlertDialog.dismiss();
                    openGalleryOnly();
                }
            } else {
                if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
                    if (levelActivity.getRedirectTo() != null) {
                        levelActivityAlertDialog.dismiss();
                        activitiesRedirection(levelActivity);
                    }
                } else {
                    startNewActivity(levelActivity, "");
                }
            }

        });

    }

    private void showCustomPopUpJournal(LevelActivity levelActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        levelActivityJournalBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_rewards_journal, null, false);
        alertBuilder.setView(levelActivityJournalBinding.getRoot());
        levelActivityAlertDialog = alertBuilder.create();
        levelActivityAlertDialog.setCancelable(false);
        if (!levelActivityAlertDialog.isShowing())
            levelActivityAlertDialog.show();

        Glide.with(context)
                .load(levelActivity.getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(levelActivityJournalBinding.ivLogo);
        levelActivityJournalBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        levelActivityJournalBinding.tvTitle.setText(levelActivity.getActivityName());
        levelActivityJournalBinding.tvDescription.setText(levelActivity.getActivityDesc());

        if (levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
        }

        if (levelActivity.getWhatTo() != null) {
            levelActivityJournalBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.tvWhatToDo.setText(levelActivity.getWhatTo());
            levelActivityJournalBinding.tvHowToDo.setText(levelActivity.getHowTo());
            levelActivityJournalBinding.tvWhyToDo.setText(levelActivity.getWhyTo());
        }

        activityEventType = levelActivity.getEventType();

        checkActivityPopUpConditionsJournal(levelActivity, activityEventType);

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(levelActivityJournalBinding.progressBar, levelActivity, null, null);
        } else if (levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                levelActivityJournalBinding.progressBar.setProgress(100, true);
            } else
                levelActivityJournalBinding.progressBar.setProgress(100);
        }

        Rect displayRectangle = new Rect();
        Window window = ((RewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        levelActivityAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.8f));
    }

    private void checkActivityPopUpConditionsJournal(LevelActivity levelActivity, String activityEventType) {

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            if (levelActivity.getRedirectTo() != null) {
                levelActivityJournalBinding.btnPositive.setVisibility(View.VISIBLE);
                levelActivityJournalBinding.btnPositive.setText("Complete");
            } else {
                levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
            }
        }


        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("upload")) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setText("Upload");
           /* levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                fileUploadKey = "ActivityUploads";
                levelActivityAlertDialog.dismiss();
                uploadFileActivity = levelActivity;
                ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            });*/
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("yesno")) {
            levelActivityJournalBinding.llYesNo.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnYes.setOnClickListener(view -> {
                levelActivityAlertDialog.dismiss();
                yesNoAnswer = "Yes";
                uploadContentOnly(levelActivity);
            });
            levelActivityJournalBinding.btnNo.setOnClickListener(view -> {
                levelActivityAlertDialog.dismiss();
                yesNoAnswer = "No";
                uploadContentOnly(levelActivity);
            });
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("journal")) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setText("Submit");
            levelActivityJournalBinding.llJournal.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(levelActivityJournalBinding.edtJournal.getText()) &&
                        levelActivityJournalBinding.edtJournal.getText().toString().length() > 15) {
                    levelActivityAlertDialog.dismiss();
                    journalContent = levelActivityJournalBinding.edtJournal.getText().toString();
                    uploadContentOnly(levelActivity);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                    levelActivityJournalBinding.edtJournal.requestFocus();
                }
            });
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("JournalUpload")) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setText("Submit");
            levelActivityJournalBinding.llJournal.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(levelActivityJournalBinding.edtJournal.getText()) && validateLetters(levelActivityJournalBinding.edtJournal.getText().toString())) {
                    journalContent = levelActivityJournalBinding.edtJournal.getText().toString();
                    fileUploadKey = "JournalUpload";
                    uploadFileActivity = levelActivity;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                    } else {
                        levelActivityAlertDialog.dismiss();
                        openGalleryOnly();
                    }
                } else {
                    levelActivityJournalBinding.edtJournal.requestFocus();
                }
            });
        }

        if (!levelActivity.isStarted()) {
            levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                startNewActivity(levelActivity, "Journal");
            });
        }

        levelActivityJournalBinding.btnNegative.setOnClickListener(view -> {
            levelActivityAlertDialog.dismiss();
            getRewardsDashboardData();
        });


        if (levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
        }


    }

    private void checkActivityPopUpConditionsJournalUpload(LevelActivity levelActivity, String activityEventType) {
        customPopUpRewardsJournalUploadBinding.btnNegative.setOnClickListener(view -> {
            levelActivityJournalUploadAlertDialog.dismiss();
            getRewardsDashboardData();
        });

        customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view1 -> {
            levelActivityJournalUploadAlertDialog.dismiss();
            startNewActivity(levelActivity, "JournalUpload");
        });

        if (!levelActivity.isIsCompleted() && levelActivity.isStarted() && levelActivity.getEventType().equalsIgnoreCase("JournalUpload")) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setText("Submit");
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(customPopUpRewardsJournalUploadBinding.edtJournal.getText()) &&
                        customPopUpRewardsJournalUploadBinding.edtJournal.getText().length() >= 15) {
                    journalContent = customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString();
                    fileUploadKey = "JournalUpload";
                    uploadFileActivity = levelActivity;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                    } else {
                        levelActivityJournalUploadAlertDialog.dismiss();
                        openGalleryOnly();
                    }
                } else {
                    customPopUpRewardsJournalUploadBinding.edtJournal.requestFocus();
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void showCustomPopUpJournalUpload(LevelActivity levelActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        customPopUpRewardsJournalUploadBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_rewards_journal_upload, null, false);
        alertBuilder.setView(customPopUpRewardsJournalUploadBinding.getRoot());
        levelActivityJournalUploadAlertDialog = alertBuilder.create();
        levelActivityJournalUploadAlertDialog.setCancelable(false);
        if (!levelActivityJournalUploadAlertDialog.isShowing())
            levelActivityJournalUploadAlertDialog.show();

        Glide.with(context)
                .load(levelActivity.getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(customPopUpRewardsJournalUploadBinding.ivLogo);
        customPopUpRewardsJournalUploadBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        customPopUpRewardsJournalUploadBinding.tvTitle.setText(levelActivity.getActivityName());
        customPopUpRewardsJournalUploadBinding.tvDescription.setText(levelActivity.getActivityDesc());
        if (levelActivity.isStarted() || levelActivity.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.GONE);
        }

        activityEventType = levelActivity.getEventType();
        checkActivityPopUpConditionsJournalUpload(levelActivity, activityEventType);

        if (levelActivity.getWhatTo() != null) {
            customPopUpRewardsJournalUploadBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.tvWhatToDo.setText(levelActivity.getWhatTo());
            customPopUpRewardsJournalUploadBinding.tvHowToDo.setText(levelActivity.getHowTo());
            customPopUpRewardsJournalUploadBinding.tvWhyToDo.setText(levelActivity.getWhyTo());
        }

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(customPopUpRewardsJournalUploadBinding.progressBar, levelActivity, null, null);
        } else if (levelActivity.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100, true);
            } else
                customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100);
        }

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.8f));
    }


    public void getHourlySteps() {
        if (watchYourHealth.isHourlyStepExist()) {
            try {
                String lastDate = watchYourHealth.getLastStandDate();
                //String lastDate = "2019-11-20";
                String todayDateNew = getTodayDateNew();
                //Toast.makeText(getApplicationContext(), lastDate, Toast
                // .LENGTH_LONG).show();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                Date date = sdf.parse(lastDate);
                Date date1 = sdf.parse(todayDateNew);
                long diff = date1.getTime() - date.getTime();
                int days = (int) TimeUnit.DAYS.convert(diff,
                        TimeUnit.MILLISECONDS);
                totalStandDays = days;
                //new ViewStepsCount(WatchYourHealth.YEAR_HOUR).execute();
                new CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR_HOUR, context);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            totalStandDays = -1;
            //new ViewStepsCount(WatchYourHealth.YEAR_HOUR).execute();
            new CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR_HOUR, context);
        }
    }

    private void getStand() {
        String minHourlyStepsDate = watchYourHealth.getMinHourlyStepsDate();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = formatter.parse(minHourlyStepsDate);
            Date endDate = formatter.parse(getTodayDateNew());

            Calendar start = Calendar.getInstance();
            start.setTime(startDate);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);
            //end.add(Calendar.DATE, 1);
            //Date newEndDate = end.getTime();
            ////Log.v("DAte",""+end);
            for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE,
                    1), date = start.getTime()) {
                // Do your job here with `date`.
                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                String convertDate = spf.format(date);
                //System.out.println("Stand Date : " + convertDate);
                //String todayDateNew = Utilities.getTodayDateNew();
                int standCount = watchYourHealth.getTodayHourlyStandCount(convertDate,
                        com.wyh.happyyousdk.utils.Constants.STEPS_COUNT);
                //System.out.println("Stand Count : " + standCount);
                watchYourHealth.insertStandCount(standCount, convertDate, true);

                int activeStandCount = watchYourHealth.getTodayHourlyStandCount(convertDate,
                        com.wyh.happyyousdk.utils.Constants.ACTIVE_STEPS_COUNT);
                //System.out.println("Active Stand Count : " + activeStandCount);
                watchYourHealth.insertActiveHourCount(activeStandCount, convertDate);
            }
        } catch (Exception e) {
            //Log.e("Stand Exception", e.getMessage());
        }

        int standCount = watchYourHealth.getTodayHourlyStandCount(getTodayDateNew(), com.wyh.happyyousdk.utils.Constants.STEPS_COUNT);
        watchYourHealth.insertStandCount(standCount, getTodayDateNew(), true);
        int stand = watchYourHealth.getStand();
        if (stand != 0) {
//            tvStandingHoursValue.setText(stand + " Hrs");
        } else {
//            tvStandingHoursValue.setText("0 Hrs");
        }

        int activeStandCount = watchYourHealth.getTodayHourlyStandCount(getTodayDateNew(),
                com.wyh.happyyousdk.utils.Constants.ACTIVE_STEPS_COUNT);
        watchYourHealth.insertActiveHourCount(activeStandCount, getTodayDateNew());
        /*int activeHour = watchYourHealth.getActiveHour();
        if (activeHour != 0) {
            tvActiveHoursValue.setText(activeHour + " Hrs");
        } else {
            tvActiveHoursValue.setText("0 Hrs");
        }*/
    }

    private void getDaysSteps() {
        if (watchYourHealth.isStepExist()) {
            try {
                String lastDate = watchYourHealth.getLastStepsDate();
                //String lastDate = "2019-11-20";
                String todayDateNew = getTodayDateNew();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                Date date = sdf.parse(lastDate);
                Date date1 = sdf.parse(todayDateNew);
                long diff = date1.getTime() - date.getTime();
                int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                totalStepsDays = days;
                //Toast.makeText(HomeActivity.this,""+totalStepsDays,Toast.LENGTH_SHORT).show();
                //new ViewStepsCount(WatchYourHealth.LAST_DATA).execute();
                //System.out.println("Days Data: " + totalStepsDays);
                //new ViewStepsCount(WatchYourHealth.LAST_DATA).execute();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            //new ViewStepsCount(WatchYourHealth.YEAR).execute();
            new CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR, context);
        }
    }

    public void getMinuteSteps() {
        if (watchYourHealth.isMinuteStepExist()) {
            try {
                String lastDate = watchYourHealth.getLastMinuteStepsDate();
                //String lastDate = "2019-11-20";
                String todayDateNew = getTodayDateNew();
                //Toast.makeText(MainService.this, lastDate, Toast.LENGTH_LONG).show();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                if (lastDate != null) {
                    Date date = sdf.parse(lastDate);
                    Date date1 = sdf.parse(todayDateNew);
                    long diff = date1.getTime() - date.getTime();
                    int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    totalMinuteStepsDays = days;
                    Log.v("Sleep Last Date", lastDate + "." + days);
                } else {
                    totalMinuteStepsDays = 30;
                }
                //new ViewStepsCount(WatchYourHealth.SLEEP_DATA).execute();
                new CoroutineClass().runBackGroundTask(WatchYourHealth.SLEEP_DATA, context);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            totalMinuteStepsDays = 30;
            //new ViewStepsCount(WatchYourHealth.SLEEP_DATA).execute();
            new CoroutineClass().runBackGroundTask(WatchYourHealth.SLEEP_DATA, context);
        }
    }


    public void fetchGraphData(String periodType, String activityType) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        FetchGraphRequest fetchGraphRequest = new FetchGraphRequest(activityType, periodType, currentGraphIndex);
        Call<FetchGraphResponse> call = apiInterfaceWyh.fetchGraph(SharedPref.getAuthToken(), fetchGraphRequest);
        Log.v("AuthToken", "Request " + new Gson().toJson(fetchGraphRequest));

        call.enqueue(new Callback<FetchGraphResponse>() {
            @Override
            public void onResponse(Call<FetchGraphResponse> call, Response<FetchGraphResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_success) + activityType);
                    Log.v("AuthToken", "Response " + new Gson().toJson(response.body()));
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    fetchGraphResponse = response.body();
                    int additionOfPoints = 0;
                    String todayDate = todayDateInFormat("yyyy-MM-dd");
                    Log.d("Active Hr:", todayDate);
                    for (int i = 0; i < fetchGraphResponse.getData().getDataPoints().size(); i++) {
                        if (periodType.equals(DAILY) && (activityType.equals(MEDITATION) || activityType.equals(WATER) || activityType.equals(BURNEDGRAPH) || activityType.equals(WEIGHT) || activityType.equals(STEPS) || activityType.equals(ACTIVEHOURS) || activityType.equals(SLEEP))) {

                            String todayDateFromAPI = formatDateFromString("yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd", fetchGraphResponse.getData().getDataPoints().get(i).getRecordDate());
                            waterIntakeGoal = Integer.parseInt(fetchGraphResponse.getData().getWaterIntakeGoal());

                            Log.d("Active Hr:", i + " " + todayDateFromAPI);
                            if (todayDateFromAPI.equals(todayDate)) {
                                medMinutes = fetchGraphResponse.getData().getDataPoints().get(i).getPoint();
                                waterIntake = fetchGraphResponse.getData().getDataPoints().get(i).getPoint();
                                stepIntake = fetchGraphResponse.getData().getDataPoints().get(i).getPoint();
                                activeHrTodays = fetchGraphResponse.getData().getDataPoints().get(i).getPoint();
                                if (waterIntake > 0 && activityType.equals(WEIGHT)) {
                                    binding.tvGlassAmt.setText(format.format(waterIntake));
                                }
                                binding.tvZenMinutes.setText(format.format(fetchGraphResponse.getData().getDataPoints().get(i).getPoint()));
                                int point = (int) Math.round(fetchGraphResponse.getData().getDataPoints().get(i).getPoint());
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.circleViewZen.setProgress(point, true);
                                } else
                                    binding.circleViewZen.setProgress(point);
                                binding.tvPeaceFulTime.setText(format.format(fetchGraphResponse.getData().getDataPoints().get(i).getPoint()) + " minutes");

                                if (activityType.equals(STEPS)) {
                                    getFootPrint();
                                }

                                if (activityType.equals(ACTIVEHOURS)) {
                                    getActiveHour();
                                }

                                if (activityType.equals(SLEEP))
                                    getSleep();
//                                binding..setText(fetchGraphResponse.getData().getDataPoints().get(i).getPoint());
                            }

                        }
//                        additionOfPoints = additionOfPoints + fetchGraphResponse.getData().getDataPoints().get(i).getPoint();

                    }
                    TrendsActivity.this.selectedGraphType = periodType;
                    setDateRange();
                    if (btnWorkout || btnTodayWorkout) {
                        getCalorieBurnedData();
                    } else {
                        getWaterIntake();
                    }

                    /*if (periodType.equals(DAILY)) {
                        if (activityType.equals(WATER)) {
                            int waterLtr = additionOfPoints * 250 / 1000;
                            binding.tvGraphTopMsg.setText("H20 in this week " + waterLtr + " Litres");
                        } else if (activityType.equals(SLEEP)) {
                            binding.tvGraphTopMsg.setText("Your daily pillow time stats");
                        } else {
                            binding.tvGraphTopMsg.setVisibility(View.GONE);
                        }
                    }*/


                    binding.stepsBarChart.setData(generateStepsBarChart(periodType, fetchGraphResponse, activityType));
                    binding.stepsBarChart.invalidate();
//                    binding.stepsBarChart.notifyDataSetChanged();
                } else if (response.code() == 401) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    refreshAuthToken(periodType);
                } else {
                  /* if(binding.stepsBarChart.getLegend().getEntries().length > 0){
                       binding.stepsBarChart.getLegend().getEntries()[0].label = "";
                       binding.stepsBarChart.invalidate();
                   }*/
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_failed) + activityType);
                    Toast.makeText(context, "Something went wrong" + response.code(), Toast.LENGTH_SHORT).show();
                    Log.v("Steps Data", "" + response.code());
                }
            }

            @Override
            public void onFailure(Call<FetchGraphResponse> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_failed) + activityType);
                }
//                Toast.makeText(VitalsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                Log.v("API_RESPONSE", "" + t.getMessage());
            }
        });
    }

    private void getSteps() {
        //watchYourHealth = new WatchYourHealth(HomeActivity.this);
        if (SharedPreference.getGoogleFitConnection()) {
            String stepCount = watchYourHealth.getTotalSteps(Constants.SOURCE_GOOGLEFIT);
            SharedPref.putTodaySteps(stepCount);
//            binding.tvStepsCount.setText(stepCount + "/10000");
//            binding.tvStepCount.setText(stepCount);
            double totalWalkedSteps = Integer.parseInt(stepCount) * perStepInKm;
            String walkedValue = String.format("%.2f", totalWalkedSteps);
            binding.tvDistanceCovered.setText(walkedValue + " Kms");

            int totalCalorieBurned = (int) Math.round(Integer.parseInt(stepCount) * perCalorieInStep);
            binding.tvCalBurn.setText(totalCalorieBurned + " Cal");
        }
    }

    private void refreshAuthToken(String periodType) {
        CommonUtils.showProgressDialige(context);
        String deviceModel = Build.BRAND + " " + Build.MODEL;
        String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
        String appVersion = SDKConstants.appVersionName;
        RefreshTokenRequest request = new RefreshTokenRequest(deviceModel, osVersion, appVersion);
        Call<RefreshTokenResponse> call = apiInterfaceWyh.refreshToken(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                CommonUtils.dismissDialoge();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess() &&
                        response.body().getData().getAuthToken() != null && !response.body().getData().getAuthToken().equals("")) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.init(context);
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    fetchGraphData(periodType, activityType);

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
                CommonUtils.dismissDialoge();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                /*Toast.makeText(context, context.getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    context.startActivity(intent);
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    private BarData generateStepsBarChart(String pattern, FetchGraphResponse fetchGraphResponse, String activityType) {
        binding.stepsBarChart.clear();
        binding.stepsBarChart.setTouchEnabled(true);
        binding.stepsBarChart.setDragEnabled(false);
        binding.stepsBarChart.setScaleEnabled(false);
        binding.stepsBarChart.setScaleXEnabled(false);
        binding.stepsBarChart.setScaleYEnabled(false);
        binding.stepsBarChart.setPinchZoom(false);
        binding.stepsBarChart.setDoubleTapToZoomEnabled(false);
        binding.stepsBarChart.setHighlightFullBarEnabled(true);
        binding.stepsBarChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        binding.stepsBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);

        YAxis rightAxis1 = binding.stepsBarChart.getAxisRight();
        rightAxis1.setEnabled(false);
        rightAxis1.setDrawLabels(false);
        rightAxis1.setDrawGridLines(false);
        rightAxis1.setAxisMinimum(0);
        rightAxis1.setDrawAxisLine(true);

        YAxis leftAxis1 = binding.stepsBarChart.getAxisLeft();
        leftAxis1.setDrawAxisLine(true);
        leftAxis1.setDrawLabels(true);
        leftAxis1.setAxisMinimum(0); // VitalsActivity.this replaces setStartAtZero(true)
        leftAxis1.setEnabled(true);
        leftAxis1.setDrawLimitLinesBehindData(false);
        leftAxis1.setDrawGridLines(false);

        XAxis xAxis1 = binding.stepsBarChart.getXAxis();
        xAxis1.setDrawGridLines(false);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setDrawAxisLine(true);
        xAxis1.setGranularity(1f);

        //binding.binding.stepsBarChart.getAxisLeft().setAxisMaxValue(300);

        binding.stepsBarChart.setExtraOffsets(0f, 0f, 0f, 16f);
        binding.stepsBarChart.setDrawBorders(false);
        binding.stepsBarChart.getDescription().setEnabled(false);
        List<String> week = new ArrayList<>();
        if (pattern.equals(WEEKLY)) {
            for (int i = 0; i < fetchGraphResponse.getData().getDataPoints().size(); i++) {
                week.add(fetchGraphResponse.getData().getDataPoints().get(i).getWeeklyRange());
            }
        }
        binding.stepsBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getWeekWithDates(pattern, week, this.getResources())));

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        entries1.clear();

        entries1.addAll(getWeeklyDataBar(fetchGraphResponse, this));

//        if (entries1.size() > 0 && mv != null){
//            mv.refreshContent(entries1.get(0), new Highlight(entries1.get(0).getX(), entries1.get(0).getY(), 0));
//        }
        BarDataSet set1 = null;

        int setColor3;
        if (activityType.equals(SLEEP)) {
            setColor3 = this.getResources().getColor(R.color.blue);
        } else {
            setColor3 = this.getResources().getColor(R.color.dark_pink);
        }

        set1 = new BarDataSet(entries1, "");

        set1.setColor(setColor3);
        //binding.stepsBarChart.getLegend().setEnabled(false);
        //binding.stepsBarChart.getDescription().setEnabled(false);

        binding.stepsBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);

        List<LegendEntry> legendEntries = Arrays.asList(binding.stepsBarChart.getLegend().getEntries());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();
        Gson gson = gsonBuilder.create();
        Log.d("legendEntries", gson.toJson(legendEntries));

        if (legendEntries.size() > 0) {
            switch (activityType) {
                case "STEPS":
                    legendEntries.get(0).label = "Steps";
                    break;
                case "SLEEP":
                    legendEntries.get(0).label = "Sleep (hrs)";
                    break;
                case "WATER":
                    legendEntries.get(0).label = "Water (glasses)";
                    break;
                case "ACTIVEHOURS":
                    legendEntries.get(0).label = "Active Hours (hrs)";
                    break;
                case "CONSUMEDGRAPH":
                case "BURNEDGRAPH":
                    legendEntries.get(0).label = "Calories (cal)";
                    break;
                case "MEDITATION":
                    legendEntries.get(0).label = "Meditation (mins)";
                    break;
                case "WEIGHT":
                    legendEntries.get(0).label = "Weight (kg)";
                    break;
            }
        }

        Log.d("legendEntries", gson.toJson(legendEntries));
        if (legendEntries.size() > 0)
            binding.stepsBarChart.getLegend().setCustom(legendEntries);

        set1.setDrawValues(false);
        BarData barData;
        LegendEntry l1, l2;

        barData = new BarData(set1);

        barData.setValueTextColor(Color.WHITE);
        barData.setValueTextSize(9f);
        barData.setBarWidth(0.25f);

        xAxis1.setAxisMinimum(-axisPadding);
        xAxis1.setAxisMaxValue(barData.getXMax() + axisPadding);

        mv = new MyMarkerView(this, R.layout.barchart_marker_view_layout);
        binding.stepsBarChart.setMarker(mv);

        return barData;
    }

    private void getWaterIntake() {
        try {
            float goalWaterGlassLiter = SharedPref.getWaterGoals();
            int goalWaterGlass = (int) Math.round(goalWaterGlassLiter * 4);
            binding.txtWaterIntakeRecommended.setText("Recommended: " + goalWaterGlassLiter + " Liters");
            binding.tvRecommendation.setText("Recommended: " + waterIntakeGoal + " glasses");
            if (waterIntakeGoal != 0) {
                if (waterIntake != 0) {
                    double waterInLiters = (waterIntake * 0.25);
                    int progressbar = (int) Math.round(waterInLiters * 4);
                    Log.d("AuthToken", String.valueOf(waterInLiters));
                    binding.txtWaterIntake.setText("Water intake: " + waterInLiters + " Liters");
                    binding.txtWaterLitres.setText("" + waterInLiters);
                    binding.waterCircleViewScore.setProgress(progressbar);
                    binding.waterCircleViewScore.setMax(goalWaterGlass);
                }
            } else {
                goalWaterGlassLiter = 2;
                goalWaterGlass = (int) Math.round(goalWaterGlassLiter * 4);
                binding.txtWaterIntake.setText("Water intake: 0 Liters");
                binding.txtWaterIntakeRecommended.setText("Recommended: " + goalWaterGlass + " Liters");
                binding.tvRecommendation.setText("Recommended: " + goalWaterGlass + " glasses");
                waterIntakeGoal = 8;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getFootPrint() {
        int footprintGoals = SharedPref.getFootprintGoals();
        String stepFormat = numberFormat.format(stepIntake);
        int progressFootPrint = (int) Math.round(stepIntake);
        binding.tvStepCount.setText(stepFormat);
        binding.txtFootprintRecommended.setText("Recommended: " + footprintGoals + " Steps");
        if (footprintGoals != 0) {
            binding.txtFootprintIntake.setText("Steps taken: " + stepFormat + " Steps");
            binding.txtFootprint.setText(stepFormat);
            binding.footPrintCircleViewScore.setProgress(progressFootPrint);
            binding.footPrintCircleViewScore.setMax(footprintGoals);
        }
    }

    private void getActiveHour() {
        int activeHourGoal = ACTIVE_HOUR_GOAL;
        int progressActiveHrTodays = (int) Math.round(activeHrTodays);
        String activeFormat = numberFormat.format(activeHrTodays);
        binding.txtActiveHoursRecommended.setText("Recommended: " + activeHourGoal + " hrs");
        if (activeHourGoal != 0) {
            int activeStandCount = watchYourHealth.getTodayHourlyStandCount(getTodayDateNew(), ACTIVE_STEPS_COUNT);
            binding.txtActiveHoursDetails.setText("Active hours: " + activeFormat + " hrs");
            binding.txtActivehr.setText(activeFormat);
            binding.ActiveHrCircleViewScore.setProgress(progressActiveHrTodays);
            binding.ActiveHrCircleViewScore.setMax(activeHourGoal);
        }
    }

    public void getRewardsDashboardData() {
        GetRewardsDashboardRequest request = new GetRewardsDashboardRequest(TAG_CURRENT_LEVEL);
        Call<LevelDashboardResponse> call = apiInterfaceWyh.getRewardsDashboardData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<LevelDashboardResponse>() {
            @Override
            public void onResponse(Call<LevelDashboardResponse> call, Response<LevelDashboardResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    new Gson().toJson(response.body());
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_success));
                    if (response.body().getData() != null) {
                        if (response.body().getData().getCurrentLevel() != null)
                            currentLevelActivity = response.body().getData().getCurrentLevel();
                        setActivitiesAdapter();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LevelDashboardResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setActivitiesAdapter() {
        if (currentLevelActivity != null && currentLevelActivity.getActivities() != null && currentLevelActivity.getActivities().size() > 0) {
            List<LevelActivity> currentActivities = currentLevelActivity.getActivities();
            List<LevelActivity> inProgress = new ArrayList<>();
            List<LevelActivity> notStarted = new ArrayList<>();
            List<LevelActivity> dataList = new ArrayList<>();
            for (int i = 0; i < currentActivities.size(); i++) {
                if (currentActivities.get(i).isStarted() && !currentActivities.get(i).isIsCompleted())
                    inProgress.add(currentActivities.get(i));
                else if (!currentActivities.get(i).isStarted() && !currentActivities.get(i).isIsCompleted())
                    notStarted.add(currentActivities.get(i));
            }
            currentActivities.clear();
            currentActivities.addAll(inProgress);
            currentActivities.addAll(notStarted);

            if (currentActivities.size() > 9) {
                for (int i = 0; i < 9; i++) {
                    dataList.add(currentActivities.get(i));
                }
            } else {
                dataList.addAll(currentActivities);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            levelActivitiesAdapter = new LevelActivitiesAdapter(context, dataList, this);
            binding.rvActivities.setAdapter(levelActivitiesAdapter);
            binding.rvActivities.setLayoutManager(linearLayoutManager);
            levelActivitiesAdapter.notifyDataSetChanged();

            int indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(currentLevelActivity.getActivities().size())) / 3.0);
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
            linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            if (indicatorSize > 3)
                indicatorSize = 3;
            IndicatorsAdapter indicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvActivitiesIndicator.setAdapter(indicatorsAdapter);
            binding.rvActivitiesIndicator.setLayoutManager(linearLayoutManager1);
            binding.rvActivitiesIndicator.setHasFixedSize(true);

            binding.rvActivities.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionCurrentActivities = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionCurrentActivities = linearLayoutManager.findFirstVisibleItemPosition();
                        indicatorsAdapter.updateSelectedIndex(positionCurrentActivities);
                    }
                }
            });
        } else {
            binding.llActivities.setVisibility(View.GONE);
        }
    }

    private void addBookmark(String articleCode, boolean isBookmark) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AddBookmarkRequest request = new AddBookmarkRequest(articleCode, isBookmark);
        Call<AddBookmarkResponse> call = apiInterfaceWyh.addBookMark(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<AddBookmarkResponse>() {
            @Override
            public void onResponse(Call<AddBookmarkResponse> call, Response<AddBookmarkResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_success));
                    Log.d("BookMark", response.body().getMsg());
                    if (isBookmark) {
                        Toast.makeText(context, "Successfully added to Bookmark", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Remove from Bookmark", Toast.LENGTH_SHORT).show();
                    }
                    getAbsorbDashboard(tagName);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_failed));
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddBookmarkResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_failed));
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
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
        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

       /* if(isOrange){
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
        Window window = ((PendingActivityDashboard) context).getWindow();

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
        binding.scratchView.setScratchListener(TrendsActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((TrendsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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

    private void addVideoBookmark(boolean isBookmark, int id) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        VideoBookmarkRequest request = new VideoBookmarkRequest(id, "video", isBookmark);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.addBookmarkVideo(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_success));

                    Log.d("BookMark", new Gson().toJson(response.body()));
                    if (isBookmark) {
                        Toast.makeText(context, "Successfully added to Bookmark", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Remove from Bookmark", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_failed));
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }

                getAbsorbDashboard(tagName);

            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_failed));
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getActivityProgress(LinearProgressIndicator progressBar, LevelActivity levelActivity, TopUp topUp, TextView tvSteps) {
        ActivityProgressRequest request;
        if (levelActivity != null)
            request = new ActivityProgressRequest(levelActivity.getActivityID(), "Activity");
        else
            request = new ActivityProgressRequest(topUp.getTopUpID(), "Topup");
        Call<ActivityProgressResponse> call = apiInterfaceWyh.getActivityProgress(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<ActivityProgressResponse>() {
            @Override
            public void onResponse(Call<ActivityProgressResponse> call, Response<ActivityProgressResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getData() != null) {
                        if (tvSteps != null && response.body().getData().getTotalSteps() != 0) {
                            tvSteps.setVisibility(View.VISIBLE);
                            tvSteps.setText("Your steps: " + response.body().getData().getUserSteps() + " / " + response.body().getData().getTotalSteps());
                        }
                        int progress = response.body().getData().getProgressPercentage();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            progressBar.setProgress(progress, true);
                        } else
                            progressBar.setProgress(progress);
                    }
                }
            }

            @Override
            public void onFailure(Call<ActivityProgressResponse> call, Throwable t) {

            }
        });
    }


    private void fetureArrow() {
        Log.d("index", currentGraphIndex + "");
        if (currentGraphIndex == 0) {
            binding.ivNext.setVisibility(View.INVISIBLE);
        }
       /* if(currentGraphIndex == -6){
            binding.ivPrev.setVisibility(View.INVISIBLE);
        }*/

        binding.ivNext.setEnabled(currentGraphIndex != 0);
    }

    private void prevGraphButton() {
        binding.ivPrev.setOnClickListener(v -> {
            if (binding.ivNext.getVisibility() == View.INVISIBLE) {
                binding.ivNext.setVisibility(View.VISIBLE);
            }
            String at = activityType;
            if (selectedGraphType.equalsIgnoreCase(QUARTERLY)) {
                currentGraphIndex = currentGraphIndex - 4;
            } else if (selectedGraphType.equalsIgnoreCase(DAILY)) {
               /* if(currentGraphIndex == -6){
                    return;
                }else{
                    currentGraphIndex--;
                }*/
                currentGraphIndex--;
            } else {
                currentGraphIndex--;
            }
            if (activityType.equalsIgnoreCase("CALORIES")) {
                if (btnNutrition) {
                    at = CONSUMEDGRAPH;
                } else {
                    at = BURNEDGRAPH;
                }
            }
            fetureArrow();
            fetchGraphData(selectedGraphType, at);
        });
    }

    private void nextGraphButton() {
        binding.ivNext.setOnClickListener(v -> {
            if (binding.ivPrev.getVisibility() == View.INVISIBLE) {
                binding.ivPrev.setVisibility(View.VISIBLE);
            }
            String at = this.activityType;
            if (selectedGraphType.equalsIgnoreCase(QUARTERLY)) {
                currentGraphIndex = currentGraphIndex + 4;
            } else {
                Log.e("Graph Index", String.valueOf(currentGraphIndex));
                if (currentGraphIndex != 0) {
                    currentGraphIndex++;
                } else {
                    binding.ivNext.setVisibility(View.INVISIBLE);
                }

            }
            if (activityType.equalsIgnoreCase("CALORIES")) {
                if (btnNutrition) {
                    at = CONSUMEDGRAPH;
                } else {
                    at = BURNEDGRAPH;
                }
            }
            fetureArrow();
            fetchGraphData(selectedGraphType, at);
        });
    }


    private void setDateRange() {
        binding.llMoreGraphData.setVisibility(View.VISIBLE);

        String date = formatDateFromString("yyyy-MM-dd", "MMM dd yyyy", fetchGraphResponse.getData().getDateRange().getStartDate()).split(" ")[2];
        String startDate = formatDateFromString("yyyy-MM-dd", "dd MMM", fetchGraphResponse.getData().getDateRange().getStartDate());
        String endDate = formatDateFromString("yyyy-MM-dd", "dd MMM", fetchGraphResponse.getData().getDateRange().getEndDate());

        if (selectedGraphType.equalsIgnoreCase(MONTHLY) || selectedGraphType.equalsIgnoreCase(QUARTERLY)) {
            binding.tvDateRange.setText("Jan 01 " + date + "- Dec 31 " + date);
        } else if (selectedGraphType.equalsIgnoreCase(DAILY)) {
            binding.tvDateRange.setText(startDate + " - " + endDate);
        } else {
            binding.tvDateRange.setText(formatDateFromString("yyyy-MM-dd", "MMM yyyy", fetchGraphResponse.getData().getDateRange().getStartDate()));
        }
    }

    @Override
    public void onDialogDismiss() {
        cancelDialog();
    }

    public void cancelDialog() {
        try {
            if (spinRewardData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, spinRewardData.getRewardType(), this);
            } else if (quizathonRewardData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, quizathonRewardData.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }
}