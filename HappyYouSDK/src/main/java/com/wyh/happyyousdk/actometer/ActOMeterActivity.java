package com.wyh.happyyousdk.actometer;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.convertMinutesIntoHour;
import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.getWeekStrings;
import static com.wyh.happyyousdk.utils.CommonUtils.getWeekWithDates;
import static com.wyh.happyyousdk.utils.CommonUtils.getWeeklyDataBarCommunity;
import static com.wyh.happyyousdk.utils.CommonUtils.getWeeklyDataBarSingle;
import static com.wyh.happyyousdk.utils.Constants.BURNEDGRAPH;
import static com.wyh.happyyousdk.utils.Constants.CALORIE;
import static com.wyh.happyyousdk.utils.Constants.COMMUNITY;
import static com.wyh.happyyousdk.utils.Constants.CONSUMEDGRAPH;
import static com.wyh.happyyousdk.utils.Constants.DAILY;
import static com.wyh.happyyousdk.utils.Constants.INDIVIDUAL;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.MONTHLY;
import static com.wyh.happyyousdk.utils.Constants.QUARTERLY;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.SYSTOLIC_NORMAL_MAX;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;
import static com.wyh.happyyousdk.utils.Constants.WEEKLY;
import static com.wyhsdk.utils.Utilities.getTodayDateNew;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
;
import com.wyh.happyyousdk.APIEncryption.BackgroundWork.CoroutineClass;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.WellBeingActivity;
import com.wyh.happyyousdk.WellBeingDisclaimerActivity;
import com.wyh.happyyousdk.absorb.HealthHacksActivity;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityActOmeterBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.happyMarket.NewHappyMartActivity;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.absorb.AddBookmarkRequest;
import com.wyh.happyyousdk.model.request.absorb.GetDashboardDataRequest;
import com.wyh.happyyousdk.model.response.absorb.AddBookmarkResponse;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.actometer.adapter.ActometerDiagnosticTagsAdapter;
import com.wyh.happyyousdk.actometer.adapter.ActometerHappyInsightsTagsAdapter;
import com.wyh.happyyousdk.model.request.MultipleGraphDataRequest;
import com.wyh.happyyousdk.model.response.MultipleGraphDataResponse;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;

import com.wyh.happyyousdk.dass21.Dass21AnalysisActivity;
import com.wyh.happyyousdk.dass21.Dass21QuestionsActivity;
import com.wyh.happyyousdk.happyMarket.HappyMartDisclaimerActivity;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.ira.IRAAnalysisActivity;
import com.wyh.happyyousdk.ira.IraActivity;
import com.wyh.happyyousdk.model.response.ehr.FetchDiagnosticTypeResponse;
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.dass21.DassAnalysisResponse;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.sendActivityData.SendDataToServerReceiver;
import com.wyh.happyyousdk.trends.adapter.TrendsQuickReadsAdapter;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.MyMarkerView;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyhsdk.main.WatchYourHealth;
import com.wyhsdk.sharedPreferences.SharedPreference;
import com.wyhsdk.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActOMeterActivity extends AppCompatActivity implements ScratchListener {
    ActivityActOmeterBinding binding;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    ProgressDialog progressDialog;
    String activityType, communityType, communityName, diagnosticType;
    MultipleGraphDataResponse fetchGraphResponse;
    float axisPadding = 0.5f;
    MyMarkerView mv;
    GetAnalysisResponse getAnalysisResponse;
    IRAHealthScoreResponse iraHealthScoreResponse;
    boolean btnNutrition = true, btnWorkout = false;
    int selectedItem;
    String dashboardPop = "";
    Boolean firstClick = false;
    AlertDialog alertDialogRewardPopup;
    WatchYourHealth watchYourHealth;
    private final int REQUEST_CODE_ACTIVITY_RECOGNITION = 1;
    int totalStepsDays, totalStandDays, totalMinuteStepsDays;
    int communityId = -1, positionReads = 0, positionTribe = 0, currentGraphIndex = 0, currentGraphIndexDiagostics = 0;
    List<GetDashboardDataResponse.Data.TagName> tagNameList;
    boolean btnDiagnosticsPeriodTimeClicked = true;
    boolean btnPeriodTimeClicked = true;
    List<String> userList = new ArrayList<>();
    List<String> sortedUserList = new ArrayList<>();
    String userName, periodType = DAILY, diagnosticPeriod = DAILY, diagnosticPeriodType = DAILY;
    ActometerDiagnosticTagsAdapter actoMeterDiagnosticTagsAdapter;
    ActometerHappyInsightsTagsAdapter actoMeterHappyInsightsTagsAdapter;

    ArrayAdapter<String> spinnerAdapter;
    boolean usersFetched = true;
    String type, user, diagnosticUnit, diagnosticMaxVal, diagnosticMinVal;
    List<FetchDiagnosticTypeResponse.Datum> diagnosticTypeList;

    boolean isPositiveBtn = false;

    /*String selectedGraphType = DAILY;
    String selectedGraphTypeDiagnostic = DAILY;*/

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_act_ometer);
        context = this;

        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        watchYourHealth = new WatchYourHealth(this, SharedPref.getUuid());
        watchYourHealth.initializeAPIClient(savedInstanceState);

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "ActOMeter");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        


        if (!sortedUserList.isEmpty() && fetchGraphResponse != null) {
            setSpinnerData(sortedUserList, fetchGraphResponse, DAILY);
        }

        getGoogleFitData();
        fetureArrow();
        prevGraphButton();
        nextGraphButton();
        fetureArrowDiastolic();
        prevGraphButtonDiastolic();
        nextGraphButtonDiastolic();


        binding.rlHappyServices.setOnClickListener(view -> {
            Intent intent = new Intent(context, NewHappyMartActivity.class);
            startActivity(intent);
        });



        binding.rlPharmacy.setOnClickListener(view -> {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", com.wyh.happyyousdk.utils.Constants.HappyMartPharmacy);
            intent.putExtra("toolbarname", "Pharmacy");
            startActivity(intent);
        });

        binding.rlTeleConsultation.setOnClickListener(view -> {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", com.wyh.happyyousdk.utils.Constants.HappyMartTeleconsulatation);
            intent.putExtra("toolbarname", "Tele-Consultation");
            startActivity(intent);
        });
        binding.rlDiagnostics.setOnClickListener(view -> {
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            intent.putExtra("came_from", com.wyh.happyyousdk.utils.Constants.HappyMartDiagnostics);
            intent.putExtra("toolbarname", "Diagnostics and LAB Tests");
            startActivity(intent);
        });



        binding.rlKnowYourHealth.setOnClickListener(view -> {
            boolean isAnalysis = (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0);
            boolean isShown = SharedPref.getWellBeingIntroShownHealth();
            if (!isShown) {
                SharedPref.putWellBeingIntroShownHealth(true);
                gotoIntroPage("KnowYourHealth", "Health Score", isAnalysis, true);
            } else {
                Intent intent;
                if (isAnalysis) {
                    intent = new Intent(context, HRAAnalysisActivity.class);
                } else {
                    intent = new Intent(context, HRAQuestionsActivity.class);
                }
                startActivity(intent);
            }
        });

        binding.rlKnowYourImmunity.setOnClickListener(view -> {
            boolean isAnalysis = (iraHealthScoreResponse != null && iraHealthScoreResponse.getIraHealthScoreData() != null &&
                    iraHealthScoreResponse.getIraHealthScoreData().getPlaySports() != null && !iraHealthScoreResponse.getIraHealthScoreData()
                    .getPlaySports().isEmpty() && Integer.parseInt(iraHealthScoreResponse.getIraHealthScoreData().getPlaySports()) > 0);
            boolean isShown = SharedPref.getWellBeingIntroShownImmunity();
            if (!isShown) {
                SharedPref.putWellBeingIntroShownImmunity(true);
                gotoIntroPage("KnowYourImmunity", "Immunity Score", isAnalysis, true);
            } else {
                Intent intent;
                if (isAnalysis) {
                    intent = new Intent(context, IRAAnalysisActivity.class);
                } else {
                    intent = new Intent(context, IraActivity.class);
                }
                intent.putExtra("from", IRA_STATUS_COMPLETED);
                startActivity(intent);
            }
        });

        binding.rlDass21.setOnClickListener(view -> {
            DassAnalysisResponse dassAnalysisResponse = new Gson().fromJson(SharedPref.getDASSAnalysis(), DassAnalysisResponse.class);
            boolean isAnalysis = (dassAnalysisResponse != null && dassAnalysisResponse.getData() != null);
            boolean isShown = SharedPref.getWellBeingIntroShownDAS();
            if (!isShown) {
                SharedPref.putWellBeingIntroShownDAS(true);
                gotoIntroPage("KnowYourDAS", "DAS Score", isAnalysis, true);
            } else {
                Intent intent;
                if (isAnalysis) {
                    intent = new Intent(context, Dass21AnalysisActivity.class).putExtra("comingFrom","");
                } else {
                    intent = new Intent(context, Dass21QuestionsActivity.class);
                }
                startActivity(intent);
            }
        });

        if(getIntent().getStringExtra("comingFrom") != null){
            dashboardPop = getIntent().getStringExtra("comingFrom");
        }

        if (getIntent().getStringExtra("activityType") != null) {
            activityType = getIntent().getStringExtra("activityType");
        }

        if (getIntent().getStringExtra("communityT" +
                "ype") != null) {
            communityType = getIntent().getStringExtra("communityType");
        }

        if (getIntent().getStringExtra("type") != null) {
            type = getIntent().getStringExtra("type");
        }

        if (getIntent().getStringExtra("user") != null) {
            user = getIntent().getStringExtra("user");
        }

       //if (communityType.equals("Family")) {
        binding.llDiagnostic.setVisibility(View.VISIBLE);
        /*} else {
            binding.llDiagnostic.setVisibility(View.GONE);
        }*/
        if (getIntent().getStringExtra("communityId") != null) {
            communityId = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("communityId")));
        }
        if (getIntent().getStringExtra("communityName") != null) {
            communityName = getIntent().getStringExtra("communityName");
        }

        if (activityType.equals(CALORIE)) {
            binding.llConsumed.setVisibility(View.VISIBLE);
        } else {
            binding.llConsumed.setVisibility(View.GONE);
        }

        binding.ivHome.setOnClickListener(view -> {
            Intent i = new Intent(this, NewDashboardActivity.class);
            startActivity(i);
            finish();
        });

        binding.tvCommunityName.setVisibility(View.VISIBLE);
        binding.tvCommunityName.setText(communityName);
        binding.llHealthHacks.setVisibility(View.VISIBLE);

        if (type.equals(COMMUNITY)) {
            binding.includeToolbar.tvBack.setText("ACT-O-METER: " + communityType);
        } else {
            binding.includeToolbar.tvBack.setText("ACT-O-METER: Individual");
        }

        binding.rlQuickRead.setOnClickListener(view -> {
            Intent i = new Intent(this, HealthHacksActivity.class);
            startActivity(i);
            /*if (tagNameList.size() > 0) {
                Intent i = new Intent(this, QuickReadDashboard.class);
                i.putExtra("taglist", (Serializable) tagNameList);
                startActivity(i);
            }*/
        });


        switch (activityType) {
            case "WATER":
                selectedItem = 2;
                break;
            case "SLEEP":
                selectedItem = 1;
                break;
            case "STEPS":
                selectedItem = 0;
                break;
            case "CONSUMEDGRAPH":
            case "BURNEDGRAPH":
            case "CALORIES":
                selectedItem = 3;
                break;
            case "MEDITATION":
                selectedItem = 4;
                break;
            case "WEIGHT":
                selectedItem = 5;
                break;

        }

        setHappyInsightTags();
//        setDiagnosticTags();


        binding.btnDaily.setOnClickListener(view -> {
            periodType = DAILY;
            currentGraphIndex = 0;
            /*actoMeterDiagnosticTagsAdapter.updatePeriodType(periodType);
            actoMeterDiagnosticTagsAdapter.notifyDataSetChanged();*/
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            btnDiagnosticsPeriodTimeClicked = false;
            btnPeriodTimeClicked = true;
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphDataSingle(DAILY, CONSUMEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                } else {
                    fetchCommunityGraphDataSingle(DAILY, BURNEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                }
            } else {
                fetchCommunityGraphDataSingle(DAILY, activityType, communityId, diagnosticPeriod, diagnosticType);
            }
        });
        binding.btnWeekly.setOnClickListener(view -> {
            periodType = WEEKLY;
            currentGraphIndex = 0;
            /*actoMeterDiagnosticTagsAdapter.updatePeriodType(periodType);
            actoMeterDiagnosticTagsAdapter.notifyDataSetChanged();*/
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            btnDiagnosticsPeriodTimeClicked = false;
            btnPeriodTimeClicked = true;
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphDataSingle(WEEKLY, CONSUMEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                } else {
                    fetchCommunityGraphDataSingle(WEEKLY, BURNEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                }
            } else {
                fetchCommunityGraphDataSingle(WEEKLY, activityType, communityId, diagnosticPeriod, diagnosticType);
            }
        });
        binding.btnMonthly.setOnClickListener(view -> {
            periodType = MONTHLY;
            currentGraphIndex = 0;
            /*actoMeterDiagnosticTagsAdapter.updatePeriodType(periodType);
            actoMeterDiagnosticTagsAdapter.notifyDataSetChanged();*/
            btnPeriodTimeClicked = true;
            btnDiagnosticsPeriodTimeClicked = false;

            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));

            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphDataSingle(MONTHLY, CONSUMEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                } else {
                    fetchCommunityGraphDataSingle(MONTHLY, BURNEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                }
            } else {
                fetchCommunityGraphDataSingle(MONTHLY, activityType, communityId, diagnosticPeriod, diagnosticType);
            }
        });
        binding.btnQuarterly.setOnClickListener(view -> {
            periodType = QUARTERLY;
            currentGraphIndex = 0;
            /*actoMeterDiagnosticTagsAdapter.updatePeriodType(periodType);
            actoMeterDiagnosticTagsAdapter.notifyDataSetChanged();*/
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            btnDiagnosticsPeriodTimeClicked = false;
            btnPeriodTimeClicked = true;
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphDataSingle(QUARTERLY, CONSUMEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                } else {
                    fetchCommunityGraphDataSingle(QUARTERLY, BURNEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                }
            } else {
                fetchCommunityGraphDataSingle(QUARTERLY, activityType, communityId, diagnosticPeriod, diagnosticType);
            }
        });

        binding.btnDailyDiagnostics.setOnClickListener(view -> {
            binding.btnDailyDiagnostics.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnWeeklyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            btnDiagnosticsPeriodTimeClicked = true;
            btnPeriodTimeClicked = false;
            diagnosticPeriodType = DAILY;
            currentGraphIndexDiagostics = 0;


            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphDataSingle(periodType, CONSUMEDGRAPH, communityId, DAILY, diagnosticType);
                } else {
                    fetchCommunityGraphDataSingle(periodType, BURNEDGRAPH, communityId, DAILY, diagnosticType);
                }
            } else {
                fetchCommunityGraphDataSingle(periodType, activityType, communityId, DAILY, diagnosticType);
            }
        });
        binding.btnWeeklyDiagnostics.setOnClickListener(view -> {
            binding.btnDailyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeeklyDiagnostics.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnMonthlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            btnDiagnosticsPeriodTimeClicked = true;
            btnPeriodTimeClicked = false;
            diagnosticPeriodType = WEEKLY;
            currentGraphIndexDiagostics = 0;
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphDataSingle(periodType, CONSUMEDGRAPH, communityId, DAILY, diagnosticType);
                } else {
                    fetchCommunityGraphDataSingle(periodType, BURNEDGRAPH, communityId, WEEKLY, diagnosticType);
                }
            } else {
                fetchCommunityGraphDataSingle(periodType, activityType, communityId, WEEKLY, diagnosticType);
            }
        });
        binding.btnMonthlyDiagnostics.setOnClickListener(view -> {
            binding.btnDailyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeeklyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnQuarterlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            diagnosticPeriodType = MONTHLY;
            btnDiagnosticsPeriodTimeClicked = true;
            btnPeriodTimeClicked = false;
            currentGraphIndexDiagostics = 0;
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphDataSingle(periodType, CONSUMEDGRAPH, communityId, MONTHLY, diagnosticType);
                } else {
                    fetchCommunityGraphDataSingle(periodType, BURNEDGRAPH, communityId, MONTHLY, diagnosticType);
                }
            } else {
                fetchCommunityGraphDataSingle(periodType, activityType, communityId, MONTHLY, diagnosticType);
            }
        });
        binding.btnQuarterlyDiagnostics.setOnClickListener(view -> {
            binding.btnDailyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeeklyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            diagnosticPeriodType = QUARTERLY;
            btnDiagnosticsPeriodTimeClicked = true;
            btnPeriodTimeClicked = false;
            currentGraphIndexDiagostics = 0;

            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphDataSingle(periodType, CONSUMEDGRAPH, communityId, QUARTERLY, diagnosticType);
                } else {
                    fetchCommunityGraphDataSingle(periodType, BURNEDGRAPH, communityId, QUARTERLY, diagnosticType);
                }
            } else {
                fetchCommunityGraphDataSingle(periodType, activityType, communityId, QUARTERLY, diagnosticType);
            }
        });


        binding.btnCalConsumed.setOnClickListener(view -> {
            periodType = DAILY;
            binding.btnCalConsumed.setText("Calories Consumed");
            btnNutrition = true;
            btnWorkout = false;

            binding.btnCalConsumed.setTextColor(getColor(R.color.white));
            binding.btnCalBurned.setTextColor(getColor(R.color.black));

            binding.btnCalConsumed.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));
            binding.btnCalBurned.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            fetchCommunityGraphDataSingle(periodType, CONSUMEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
        });

        binding.btnCalBurned.setOnClickListener(view -> {
            periodType = DAILY;

            binding.btnCalBurned.setText("Calories Burned");
            btnNutrition = false;
            btnWorkout = true;

            binding.btnCalBurned.setTextColor(getColor(R.color.white));
            binding.btnCalConsumed.setTextColor(getColor(R.color.black));
            binding.btnCalConsumed.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnCalBurned.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));
            fetchCommunityGraphDataSingle(periodType, BURNEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
        });

        binding.btnSystolic.setOnClickListener(view -> {
            diagnosticPeriod = DAILY;

//            binding.btnSystolic.setText("Calories Consumed");

            binding.btnSystolic.setTextColor(getColor(R.color.white));
            binding.btnDiastolic.setTextColor(getColor(R.color.black));

            binding.btnSystolic.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));
            binding.btnDiastolic.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            fetchCommunityGraphDataSingle(periodType, activityType, communityId, diagnosticPeriod, "systolic");

        });

        binding.btnDiastolic.setOnClickListener(view -> {
            diagnosticPeriod = DAILY;


//            binding.btnCalBurned.setText("Calories Burned");

            binding.btnDiastolic.setTextColor(getColor(R.color.white));
            binding.btnSystolic.setTextColor(getColor(R.color.black));
            binding.btnSystolic.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnDiastolic.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));
            fetchCommunityGraphDataSingle(periodType, activityType, communityId, diagnosticPeriod, "diastolic");
        });

        binding.llWellBeing.setOnClickListener(v -> {
            Intent intent = new Intent(context, WellBeingActivity.class);
            startActivity(intent);
        });


        binding.ivWellBeingInfo1.setOnClickListener(v -> {
            boolean isAnalysis = (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0);
            gotoIntroPage("KnowYourHealth", "Health Score", isAnalysis, false);
        });


        binding.ivWellBeingInfo2.setOnClickListener(view -> {
            iraHealthScoreResponse = new Gson().fromJson(SharedPref.getIRAHealthData(), IRAHealthScoreResponse.class);

            boolean isAnalysis = (iraHealthScoreResponse != null && iraHealthScoreResponse.getIraHealthScoreData() != null &&
                    iraHealthScoreResponse.getIraHealthScoreData().getPlaySports() != null && !iraHealthScoreResponse.getIraHealthScoreData()
                    .getPlaySports().isEmpty() && Integer.parseInt(iraHealthScoreResponse.getIraHealthScoreData().getPlaySports()) > 0);
            gotoIntroPage("KnowYourImmunity", "Immunity Score", isAnalysis, false);
        });

        binding.ivWellBeingInfo3.setOnClickListener(view -> {
            DassAnalysisResponse dassAnalysisResponse = new Gson().fromJson(SharedPref.getDASSAnalysis(), DassAnalysisResponse.class);
            boolean isAnalysis = (dassAnalysisResponse != null && dassAnalysisResponse.getData() != null);
            gotoIntroPage("KnowYourDAS", "DAS Score", isAnalysis, false);
        });


        LinearSnapHelper linearSnapHelper2 = new SnapHelperOneByOne();
        linearSnapHelper2.attachToRecyclerView(binding.rvQuickReads);
    }

    private void getAbsorbDashboard(String activityType) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetDashboardDataRequest dashboardDataRequest = new GetDashboardDataRequest(activityType, "");
        Call<GetDashboardDataResponse> call = apiInterfaceWyh.getAbsorbDashboard(SharedPref.getAuthToken(), dashboardDataRequest);
        //Log.v("Url_Request_save", call.request().url() + "\n" + new Gson().toJson(dashboardDataRequest) + "\n" + SharedPref.getAuthToken());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetDashboardDataResponse> call, @NonNull Response<GetDashboardDataResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_dashboard_success));
                    List<GetDashboardDataResponse.Data.QucikRead> quickReadList = response.body().getData().getQucikReads();
//                    List<GetDashboardDataResponse.Data.HealthTv> healthTvList = response.body().getData().getHealthTv();
//                    List<GetDashboardDataResponse.Data.Webinar> webinarList = response.body().getData().getWebinar();
                    tagNameList = response.body().getData().getTagName();

                    if (!quickReadList.isEmpty()) {
                        setQuickReadData(quickReadList);
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_dashboard_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetDashboardDataResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_dashboard_failed));
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
                false, true, false, this::addBookmark);
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

        if (indicatorSize > 1) {
            binding.rvQuickReadsIndicator.setVisibility(View.VISIBLE);
        } else {
            binding.rvQuickReadsIndicator.setVisibility(View.INVISIBLE);
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


        binding.refreshHappyInsights.setOnClickListener(v -> {
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphDataSingle(periodType, CONSUMEDGRAPH, communityId, DAILY, diagnosticType);
                } else {
                    fetchCommunityGraphDataSingle(periodType, BURNEDGRAPH, communityId, DAILY, diagnosticType);
                }
            } else {
                fetchCommunityGraphDataSingle(periodType, activityType, communityId, DAILY, diagnosticType);
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

    private void initialSingleView() {
        if (communityId != 0) {
            fetchCommunityGraphDataSingle(DAILY, activityType, communityId, DAILY, diagnosticType);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void fetchCommunityGraphDataSingle(String periodType, String activityType, int communityId, String diagnosticPeriod, String diagnosticType) {
        if (periodType.equals(DAILY)) {
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.llMoreGraphData.setVisibility(View.GONE);
        }

        if (diagnosticPeriod.equals(DAILY)) {
            binding.btnDailyDiagnostics.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnWeeklyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.llMoreGraphDataDiastolic.setVisibility(View.GONE);
        }

        this.activityType = activityType;
        this.diagnosticType = diagnosticType;
        this.diagnosticPeriod = diagnosticPeriod;
        this.diagnosticPeriodType = diagnosticPeriod;
        this.periodType = periodType;
        if (actoMeterDiagnosticTagsAdapter != null)
            actoMeterDiagnosticTagsAdapter.updateActivityType(activityType);
        if (actoMeterHappyInsightsTagsAdapter != null) {
            actoMeterHappyInsightsTagsAdapter.updateDiagnosticType(diagnosticType);
            actoMeterHappyInsightsTagsAdapter.updateDiagnosticPeriod(diagnosticPeriod);
        }
        if (activityType.equals(CALORIE) || activityType.equals(CONSUMEDGRAPH) || activityType.equals(BURNEDGRAPH)) {
            binding.llConsumed.setVisibility(View.VISIBLE);
        } else {
            binding.llConsumed.setVisibility(View.GONE);
        }

        if (progressDialog != null) {
            progressDialog.show();
        }

        MultipleGraphDataRequest multipleGraphDataRequest = new MultipleGraphDataRequest(communityId, periodType, currentGraphIndex, activityType, diagnosticType, diagnosticPeriod, currentGraphIndexDiagostics);
        Call<MultipleGraphDataResponse> call = apiInterfaceWyh.fetchGraphMultiple(SharedPref.getAuthToken(), multipleGraphDataRequest);
        Log.d("API_Req", new Gson().toJson(multipleGraphDataRequest));

        //        Log.v("API_Req", call.request().url().toString() + "\n" + new Gson().toJson(multipleGraphDataRequest) + "\n" + SharedPref.getAuthToken() + ", " + SharedPref.getUuid());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MultipleGraphDataResponse> call, @NonNull Response<MultipleGraphDataResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_success));

                    //Log.v("API_RESPONSE", new Gson().toJson(response.body()));
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    fetchGraphResponse = response.body();
                    setDateRange();
                    setDateRangeDiastolic();

                    if (!usersFetched) {
                        setGraphDataHappyInsights(fetchGraphResponse, periodType);
                        setGraphDataDiagnostics(fetchGraphResponse, diagnosticPeriod);
                    }

                    if (usersFetched) {
                        if (fetchGraphResponse != null) {
                            for (int i = 0; i < fetchGraphResponse.getData().getHappyinsights().getDataPoints().size(); i++) {
                                for (int j = 0; j < fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(i).getPoint().size(); j++) {
                                    if (!userList.contains(fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(i).getPoint().get(j).getName())) {
                                        userList.add(fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(i).getPoint().get(j).getName());
                                    }
                                }
                            }
                            for (int i = 0; i < fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate().size(); i++) {
                                for (int j = 0; j < fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate().get(i).getPoint().size(); j++) {
                                    if (!userList.contains(fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate().get(i).getPoint().get(j).getName())) {
                                        userList.add(fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate().get(i).getPoint().get(j).getName());
                                    }
                                }
                            }

                            for (int i = 0; i < userList.size(); i++) {
                                if (user != null) {
                                    if (user.equals(userList.get(i))) {
                                        sortedUserList.add(0, userList.get(i));
                                    } else {
                                        sortedUserList.add(userList.get(i));
                                    }
                                } else {
                                    if (SharedPref.getUserName().equals(userList.get(i))) {
                                        sortedUserList.add(0, userList.get(i));
                                    } else {
                                        sortedUserList.add(userList.get(i));
                                    }
                                }

                            }
                            setSpinnerData(sortedUserList, fetchGraphResponse, periodType);
                            usersFetched = false;
                        }
                    }
                    Log.d("AuthToken", String.valueOf(firstClick));
                    Log.d("AuthToken", dashboardPop);

                    if (dashboardPop.equalsIgnoreCase("dashboard")) {
                        if (!firstClick) {
                            binding.btnCalBurned.performClick();
                            firstClick = true;
                        }
                    }


                } else if (response.code() == 401) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    refreshAuthToken(periodType, activityType, communityId, diagnosticPeriod, diagnosticType);
                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_failed));
                    Toast.makeText(context, "Something went wrong" + response.code(), Toast.LENGTH_SHORT).show();
                    //Log.v("Steps Data", "" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MultipleGraphDataResponse> call, @NonNull Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_failed));
//                Toast.makeText(VitalsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                Log.v("API_RESPONSE", "" + t.getMessage());
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void fetchCommunityGraphHappyInsightsStack(String periodType, String activityType, int communityId, String diagnosticPeriod, String diagnosticType) {
        if (periodType.equals(DAILY)) {
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
        }

        if (diagnosticPeriod.equals(DAILY)) {
            binding.btnDailyDiagnostics.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnWeeklyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
        }

        this.activityType = activityType;
        this.diagnosticType = diagnosticType;
        this.diagnosticPeriod = diagnosticPeriod;
        if (actoMeterDiagnosticTagsAdapter != null)
            actoMeterDiagnosticTagsAdapter.updateActivityType(activityType);
        if (actoMeterHappyInsightsTagsAdapter != null) {
            actoMeterHappyInsightsTagsAdapter.updateDiagnosticType(diagnosticType);
            actoMeterHappyInsightsTagsAdapter.updateDiagnosticPeriod(diagnosticPeriod);
        }
        if (activityType.equals(CALORIE) || activityType.equals(CONSUMEDGRAPH) || activityType.equals(BURNEDGRAPH)) {
            binding.llConsumed.setVisibility(View.VISIBLE);
        } else {
            binding.llConsumed.setVisibility(View.GONE);
        }
        if (diagnosticType.equalsIgnoreCase("Systolic") || diagnosticType.equalsIgnoreCase("diastolic") || diagnosticType.equalsIgnoreCase("bloodpressure")) {
            binding.llBloodpressure.setVisibility(View.VISIBLE);
        } else {
            binding.llBloodpressure.setVisibility(View.GONE);
        }

        MultipleGraphDataRequest multipleGraphDataRequest = new MultipleGraphDataRequest(communityId, periodType, 0, activityType, diagnosticType, diagnosticPeriod, 0);
        Call<MultipleGraphDataResponse> call = apiInterfaceWyh.fetchGraphMultiple(SharedPref.getAuthToken(), multipleGraphDataRequest);
        //Log.v("API_Req", call.request().url().toString() + "\n" + new Gson().toJson(multipleGraphDataRequest) + "\n" + SharedPref.getAuthToken() + ", " + SharedPref.getUuid());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MultipleGraphDataResponse> call, @NonNull Response<MultipleGraphDataResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_success));
                    //Log.v("API_RESPONSE", new Gson().toJson(response.body()));
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        binding.stepsBarChart.setData(generateBarChartCommunityHappyInsights(periodType, response.body()));
                        binding.stepsBarChart.invalidate();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_failed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MultipleGraphDataResponse> call, @NonNull Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_failed));
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void fetchCommunityGraphDiagnosticsStack(String periodType, String activityType, int communityId, String diagnosticPeriod, String diagnosticType) {
        if (periodType.equals(DAILY)) {
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
        }

        if (diagnosticPeriod.equals(DAILY)) {
            binding.btnDailyDiagnostics.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnWeeklyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
        }

        this.activityType = activityType;
        this.diagnosticType = diagnosticType;
        this.diagnosticPeriod = diagnosticPeriod;
        if (actoMeterDiagnosticTagsAdapter != null)
            actoMeterDiagnosticTagsAdapter.updateActivityType(activityType);
        if (actoMeterHappyInsightsTagsAdapter != null) {
            actoMeterHappyInsightsTagsAdapter.updateDiagnosticType(diagnosticType);
            actoMeterHappyInsightsTagsAdapter.updateDiagnosticPeriod(diagnosticPeriod);
        }
        if (activityType.equals(CALORIE) || activityType.equals(CONSUMEDGRAPH) || activityType.equals(BURNEDGRAPH)) {
            binding.llConsumed.setVisibility(View.VISIBLE);
        } else {
            binding.llConsumed.setVisibility(View.GONE);
        }
        if (diagnosticType.equalsIgnoreCase("Systolic") || diagnosticType.equalsIgnoreCase("diastolic") || diagnosticType.equalsIgnoreCase("bloodpressure")) {
            binding.llBloodpressure.setVisibility(View.VISIBLE);
        } else {
            binding.llBloodpressure.setVisibility(View.GONE);
        }

        MultipleGraphDataRequest multipleGraphDataRequest = new MultipleGraphDataRequest(communityId, periodType, 0, activityType, diagnosticType, diagnosticPeriod, 0);
        Call<MultipleGraphDataResponse> call = apiInterfaceWyh.fetchGraphMultiple(SharedPref.getAuthToken(), multipleGraphDataRequest);
        //Log.v("API_Req", call.request().url().toString() + "\n" + new Gson().toJson(multipleGraphDataRequest) + "\n" + SharedPref.getAuthToken() + ", " + SharedPref.getUuid());
        call.enqueue(new Callback<MultipleGraphDataResponse>() {
            @Override
            public void onResponse(@NonNull Call<MultipleGraphDataResponse> call, @NonNull Response<MultipleGraphDataResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_success));
                    //Log.v("API_RESPONSE", new Gson().toJson(response.body()));
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        binding.diagnosticsBarChart.setData(generateBarChartCommunityDiagnostics(diagnosticPeriod, response.body()));
                        binding.diagnosticsBarChart.invalidate();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_failed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MultipleGraphDataResponse> call, @NonNull Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_failed));
            }
        });
    }

    private void setSpinnerData(List<String> userList, MultipleGraphDataResponse fetchGraphResponse, String periodType) {
        spinnerAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, userList);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerUserName.setPrompt("Select User");
        binding.spinnerUserName.setAdapter(spinnerAdapter);
//        if (viewType.equals("Fitness Plus")) {
//        int position = spinnerAdapter.getPosition(viewType);
//        binding.spinnerUserName.setSelection(position);
//        }

        binding.spinnerUserName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.v("Url_Response_spinner_item", activityNames.get(position) + "\n" + activityId);


                userName = userList.get(position);

                if (!usersFetched) {
                    setGraphDataHappyInsights(fetchGraphResponse, periodType);
                    setGraphDataDiagnostics(fetchGraphResponse, diagnosticPeriod);
                }
//                else
//                {
//                }
//                healthDataTypeId = healthDataTypeIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setGraphDataHappyInsights(MultipleGraphDataResponse fetchGraphResponse, String periodType) {
        List<Double> happySightsResponse = new ArrayList<>();
        List<String> weeklyRange = new ArrayList<>();
        if (userName != null) {
            for (int i = 0; i < fetchGraphResponse.getData().getHappyinsights().getDataPoints().size(); i++) {
                for (int j = 0; j < fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(i).getPoint().size(); j++) {
                    if (fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(i).getPoint().get(j).getName().equals(userName)) {
                        happySightsResponse.add(fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(i).getPoint().get(j).getPoint());
                        weeklyRange.add(fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(i).getWeeklyRange());
                    }
                }
            }

            Log.w("happy_sights_response", new Gson().toJson(happySightsResponse));

            binding.stepsBarChart.setData(generateStepsBarChart(periodType, happySightsResponse, weeklyRange));
            binding.stepsBarChart.invalidate();
        }

    }

    private void setGraphDataDiagnostics(MultipleGraphDataResponse fetchGraphResponse, String periodType) {
        List<Double> diagnosticsResponse = new ArrayList<>();
        List<String> weeklyRange = new ArrayList<>();
        if (userName != null) {

            for (int i = 0; i < fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate().size(); i++) {
                for (int j = 0; j < fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate().get(i).getPoint().size(); j++) {
                    if (fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate().get(i).getPoint().get(j).getName().equals(userName)) {
                        diagnosticsResponse.add(fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate().get(i).getPoint().get(j).getPoint());
                        weeklyRange.add(fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate().get(i).getWeeklyRange());

                    }
                }
            }

            Log.w("diagnostic_response", new Gson().toJson(diagnosticsResponse));

            binding.diagnosticsBarChart.setData(generateDiagnosticBarChart(periodType, diagnosticsResponse, weeklyRange));
            binding.diagnosticsBarChart.invalidate();
        }

    }

    private void getGoogleFitData() {
        getFitPermission();
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
                    new CoroutineClass().runBackGroundTask(WatchYourHealth.LAST_DATA, context);
                    //new ViewStepsCount(WatchYourHealth.LAST_DATA).execute();
                }
            }
        }, 1000);
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
            new CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR, context);
            // new ViewStepsCount(WatchYourHealth.YEAR).execute();
        }
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i > 20) {
            scratchCardLayout.onFullReveal();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                if (alertDialogRewardPopup != null && alertDialogRewardPopup.isShowing()) {
                    alertDialogRewardPopup.dismiss();
                }
            }, 3000);
        }
    }

    @Override
    public void onScratchStarted() {

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
                    //Log.v("Sleep Last Date", lastDate + "." + days);
                } else {
                    totalMinuteStepsDays = 30;
                }
                //new ViewStepsCount(WatchYourHealth.SLEEP_DATA).execute();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            totalMinuteStepsDays = 30;
            // new ViewStepsCount(WatchYourHealth.SLEEP_DATA).execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                fetchCommunityGraphDataSingle(DAILY, activityType, communityId, DAILY, diagnosticType);
                Toast.makeText(this, "GoogleFit Connected Successfully!! ", Toast.LENGTH_SHORT).show();
                updateRewards();
            } else {
                Toast.makeText(this, "GoogleFit Connection Failed..", Toast.LENGTH_SHORT).show();
            }
        }
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
                                new CoroutineClass().runBackGroundTask(WatchYourHealth.MONTH, context);

//                                handler.postDelayed(this, 1000 * 30);
                            }
                        }, 1000);
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
                            new CoroutineClass().runBackGroundTask(WatchYourHealth.MONTH, context);
//                            handler.postDelayed(this, 1000 * 30);
                        }
                    }, 1000);
                }
            }
        }
    }

    private void getSteps() {
        //watchYourHealth = new WatchYourHealth(HomeActivity.this);
        if (SharedPreference.getGoogleFitConnection()) {
            String stepCount = watchYourHealth.getTotalSteps(Constants.SOURCE_GOOGLEFIT);
            SharedPref.putTodaySteps(stepCount);
//            binding.tvStepsCount.setText(stepCount + "/10000");
          /*  binding.tvStepCount.setText(stepCount);
            double totalWalkedSteps = Integer.parseInt(stepCount) * perStepInKm;
            String walkedValue = String.format("%.2f", totalWalkedSteps);
            binding.tvDistanceCovered.setText(walkedValue + " Kms");

            int totalCalorieBurned = (int) Math.round(Integer.parseInt(stepCount) * perCalorieInStep);
            binding.tvCalBurn.setText(totalCalorieBurned + " Cal");*/
        }
    }

    private void getSleep() {
        //watchYourHealth = new WatchYourHealth(HomeActivity.this);
        int sleep = 0;
        if (SharedPreference.getGoogleFitConnection()) {
            sleep = watchYourHealth.getSleep(Constants.SOURCE_GOOGLEFIT, CommonUtils.todayDate());
        }
        if (sleep != 0) {
            String sleepHourCount = convertMinutesIntoHour(sleep);
            /*binding.tvSleepHrs.setText(sleepHourCount);
            String sleepHour = convertMinutesIntoHourInteger(sleep);
            binding.tvSleepHr.setText(sleepHour);

            binding.circleViewScore.setProgress((int) Math.round(Double.parseDouble(sleepHour)));
            binding.circleViewScore.setMax(8);*/
        } else {
        }
    }


    private void setHappyInsightTags() {
        List<String> tagNameList = new ArrayList<>();
        tagNameList.add("Happy Footprints");
        tagNameList.add("Pillow Time");
        tagNameList.add("H20");
        tagNameList.add("Cal - Count");
        tagNameList.add("Zen Zone");
        tagNameList.add("Weight");
        actoMeterHappyInsightsTagsAdapter = new ActometerHappyInsightsTagsAdapter(context, tagNameList, selectedItem, communityId,
                diagnosticPeriod, diagnosticType);
        LinearLayoutManager actoMeterHappyInsightsLinearLayoutManager = new LinearLayoutManager(context);
        actoMeterHappyInsightsLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvTags.setOnFlingListener(null);
        binding.rvTags.setLayoutManager(actoMeterHappyInsightsLinearLayoutManager);
        binding.rvTags.setAdapter(actoMeterHappyInsightsTagsAdapter);

        actoMeterHappyInsightsLinearLayoutManager.scrollToPosition(selectedItem);

        LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvTags);

    }

    private void setDiagnosticTags() {
        List<String> tagNameList = new ArrayList<>();

        tagNameList.add("Heart rate");
        tagNameList.add("Respiratory rate");
        tagNameList.add("Oxygen level");
        tagNameList.add("Blood Pressure");
        actoMeterDiagnosticTagsAdapter = new ActometerDiagnosticTagsAdapter(context,
                tagNameList, 0, communityId, activityType, periodType);
        LinearLayoutManager actoMeterDiagnosticLinearLayoutManager = new LinearLayoutManager(context);
        actoMeterDiagnosticLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvDiagnosticsTags.setOnFlingListener(null);
        binding.rvDiagnosticsTags.setLayoutManager(actoMeterDiagnosticLinearLayoutManager);
        binding.rvDiagnosticsTags.setAdapter(actoMeterDiagnosticTagsAdapter);


//        actoMeterDiagnosticLinearLayoutManager.scrollToPosition(selectedItem);

        LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvDiagnosticsTags);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
        iraHealthScoreResponse = new Gson().fromJson(SharedPref.getIRAHealthData(), IRAHealthScoreResponse.class);
        fetchDiagnosticType();

    }



    private BarData generateBarChartCommunityHappyInsights(String pattern, MultipleGraphDataResponse multipleGraphDataResponse) {
        binding.stepsBarChart.clear();
        binding.stepsBarChart.setTouchEnabled(true);
        binding.stepsBarChart.setDragEnabled(false);
        binding.stepsBarChart.setScaleEnabled(false);
        binding.stepsBarChart.setScaleXEnabled(false);
        binding.stepsBarChart.setScaleYEnabled(false);
        binding.stepsBarChart.setPinchZoom(false);
        binding.stepsBarChart.setDoubleTapToZoomEnabled(false);
        binding.stepsBarChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        binding.stepsBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

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

//        binding.binding.stepsBarChart.getAxisLeft().setAxisMaxValue(300);

        binding.stepsBarChart.setExtraOffsets(0f, 0f, 0f, 16f);
        binding.stepsBarChart.setDrawBorders(false);
        binding.stepsBarChart.getDescription().setEnabled(false);
        binding.stepsBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getWeekStrings(pattern, this.getResources())));

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        entries1.clear();

        entries1.addAll(getWeeklyDataBarCommunity(multipleGraphDataResponse, "happy insights"));

        BarDataSet set1 = null;

        int setColor3 = this.getResources().getColor(R.color.light_pink);

        set1 = new BarDataSet(entries1, "Steps");

        set1.setColors(getColors(), this);
        binding.stepsBarChart.getLegend().setEnabled(false);
        binding.stepsBarChart.getDescription().setEnabled(false);

        set1.setDrawValues(false);
        BarData barData;

        barData = new BarData(set1);

        barData.setValueTextColor(Color.WHITE);
        barData.setValueTextSize(9f);
        barData.setBarWidth(0.25f);

        xAxis1.setAxisMinimum(-axisPadding);
        xAxis1.setAxisMaxValue(barData.getXMax() + axisPadding);

        mv = new MyMarkerView(this, R.layout.barchart_marker_view_layout);
        binding.stepsBarChart.setMarker(mv);
        if (type.equals(INDIVIDUAL)) {
            binding.rlSpinner.setVisibility(View.GONE);
            binding.rlCommunityName.setVisibility(View.GONE);
        } else {
            binding.rlSpinner.setVisibility(View.VISIBLE);
            binding.rlCommunityName.setVisibility(View.VISIBLE);
        }

        return barData;
    }

    private BarData generateBarChartCommunityDiagnostics(String pattern, MultipleGraphDataResponse multipleGraphDataResponse) {
        binding.diagnosticsBarChart.clear();
        binding.diagnosticsBarChart.setTouchEnabled(true);
        binding.diagnosticsBarChart.setDragEnabled(false);
        binding.diagnosticsBarChart.setScaleEnabled(false);
        binding.diagnosticsBarChart.setScaleXEnabled(false);
        binding.diagnosticsBarChart.setScaleYEnabled(false);
        binding.diagnosticsBarChart.setPinchZoom(false);
        binding.diagnosticsBarChart.setDoubleTapToZoomEnabled(false);
        binding.diagnosticsBarChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        binding.diagnosticsBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        YAxis rightAxis1 = binding.diagnosticsBarChart.getAxisRight();
        rightAxis1.setEnabled(false);
        rightAxis1.setDrawLabels(false);
        rightAxis1.setDrawGridLines(false);
        rightAxis1.setAxisMinimum(0);
        rightAxis1.setDrawAxisLine(true);

        YAxis leftAxis1 = binding.diagnosticsBarChart.getAxisLeft();
        leftAxis1.setDrawAxisLine(true);
        leftAxis1.setDrawLabels(true);
        leftAxis1.setAxisMinimum(0); // VitalsActivity.this replaces setStartAtZero(true)
        leftAxis1.setEnabled(true);
        leftAxis1.setDrawLimitLinesBehindData(false);
        leftAxis1.setDrawGridLines(false);

        XAxis xAxis1 = binding.diagnosticsBarChart.getXAxis();
        xAxis1.setDrawGridLines(false);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setDrawAxisLine(true);
        xAxis1.setGranularity(1f);

//        binding.binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue(300);

        binding.diagnosticsBarChart.setExtraOffsets(0f, 0f, 0f, 16f);
        binding.diagnosticsBarChart.setDrawBorders(false);
        binding.diagnosticsBarChart.getDescription().setEnabled(false);
        binding.diagnosticsBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getWeekStrings(pattern, this.getResources())));

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        entries1.clear();

        entries1.addAll(getWeeklyDataBarCommunity(multipleGraphDataResponse, "Diagnostics"));

//        if (entries1.size() > 0 && mv != null){
//            mv.refreshContent(entries1.get(0), new Highlight(entries1.get(0).getX(), entries1.get(0).getY(), 0));
//        }
        BarDataSet set1 = null;


        int setColor3 = this.getResources().getColor(R.color.light_pink);

        set1 = new BarDataSet(entries1, "Steps");

        set1.setColor(setColor3);
        binding.diagnosticsBarChart.getLegend().setEnabled(false);
        binding.diagnosticsBarChart.getDescription().setEnabled(false);

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
        binding.diagnosticsBarChart.setMarker(mv);
        if (type.equals(INDIVIDUAL)) {
            binding.rlSpinner.setVisibility(View.GONE);
            binding.rlCommunityName.setVisibility(View.GONE);
        } else {
            binding.rlSpinner.setVisibility(View.VISIBLE);
            binding.rlCommunityName.setVisibility(View.VISIBLE);
        }

        return barData;
    }


    private BarData generateStepsBarChart(String pattern, List<Double> happySightsResponse, List<String> weeklyRange) {
        binding.stepsBarChart.clear();
        binding.stepsBarChart.setTouchEnabled(true);
        binding.stepsBarChart.setDragEnabled(false);
        binding.stepsBarChart.setScaleEnabled(false);
        binding.stepsBarChart.setScaleXEnabled(false);
        binding.stepsBarChart.setScaleYEnabled(false);
        binding.stepsBarChart.setPinchZoom(false);
        binding.stepsBarChart.setDoubleTapToZoomEnabled(false);
        binding.stepsBarChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        binding.stepsBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

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

//        binding.binding.stepsBarChart.getAxisLeft().setAxisMaxValue(300);

        binding.stepsBarChart.setExtraOffsets(0f, 0f, 0f, 16f);
        binding.stepsBarChart.setDrawBorders(false);
        binding.stepsBarChart.getDescription().setEnabled(false);
        List<String> week = new ArrayList<>();
        if (pattern.equals(WEEKLY)) {
            for (int i = 0; i < weeklyRange.size(); i++) {
                week.add(weeklyRange.get(i));
            }
        }
        binding.stepsBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getWeekWithDates(pattern, week, this.getResources())));

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        entries1.clear();

        entries1.addAll(getWeeklyDataBarSingle(happySightsResponse, "happy insights"));

//        if (entries1.size() > 0 && mv != null){
//            mv.refreshContent(entries1.get(0), new Highlight(entries1.get(0).getX(), entries1.get(0).getY(), 0));
//        }
        BarDataSet set1 = null;


        int setColor3 = this.getResources().getColor(R.color.pink);

        set1 = new BarDataSet(entries1, "");

        set1.setColor(setColor3);
        binding.stepsBarChart.getLegend().setEnabled(false);
        binding.stepsBarChart.getDescription().setEnabled(false);

        List<LegendEntry> legendEntries = Arrays.asList(binding.stepsBarChart.getLegend().getEntries());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();
        Gson gson = gsonBuilder.create();
        Log.d("legendEntries", gson.toJson(legendEntries));

        LegendEntry l1 = new LegendEntry("Steps",
                Legend.LegendForm.SQUARE, 10f, 2f, null, setColor3);

        if (legendEntries.size() > 0) {
            switch (activityType) {
                case "STEPS":
                    l1 = new LegendEntry("Steps",
                            Legend.LegendForm.SQUARE, 10f, 2f, null, setColor3);
                    break;
                case "SLEEP":
                    l1 = new LegendEntry("Sleep (hrs)",
                            Legend.LegendForm.SQUARE, 10f, 2f, null, setColor3);
                    break;
                case "WATER":
                    l1 = new LegendEntry("Water (glasses)",
                            Legend.LegendForm.SQUARE, 10f, 2f, null, setColor3);
                    break;
                case "ACTIVEHOURS":
                    l1 = new LegendEntry("Water (glasses)",
                            Legend.LegendForm.SQUARE, 10f, 2f, null, setColor3);
                    legendEntries.get(0).label = "Active Hours (hrs)";
                    break;
                case "CONSUMEDGRAPH":
                case "BURNEDGRAPH":
                    l1 = new LegendEntry("Calories (cal)",
                            Legend.LegendForm.SQUARE, 10f, 2f, null, setColor3);
                    legendEntries.get(0).label = "Calories (cal)";
                    break;
                case "MEDITATION":
                    l1 = new LegendEntry("Meditation (mins)",
                            Legend.LegendForm.SQUARE, 10f, 2f, null, setColor3);
                    legendEntries.get(0).label = "Meditation (mins)";
                    break;
                case "WEIGHT":
                    l1 = new LegendEntry("Weight (kg)",
                            Legend.LegendForm.SQUARE, 10f, 2f, null, setColor3);
                    legendEntries.get(0).label = "Weight (kg)";
                    break;
                default:
                    l1 = new LegendEntry("Steps",
                            Legend.LegendForm.SQUARE, 10f, 2f, null, setColor3);
            }
        }

        binding.stepsBarChart.getLegend().setCustom(new LegendEntry[]{l1});

        String label = "Steps";

        switch (activityType) {
            case "STEPS":
                label = "Steps";
                break;
            case "SLEEP":
                label = "Sleep (hrs)";
                break;
            case "WATER":
                label = "Water (glasses)";
                break;
            case "ACTIVEHOURS":
                label = "Active Hours (hrs)";
                break;
            case "CONSUMEDGRAPH":
            case "BURNEDGRAPH":
                label = "Calories (cal)";
                break;
            case "MEDITATION":
                label = "Meditation (mins)";
                break;
            case "WEIGHT":
                label = "Weight (kg)";
                break;
        }

        binding.tvUnit.setText(label);

        set1.setDrawValues(false);
        BarData barData;

        barData = new BarData(set1);

        barData.setValueTextColor(Color.WHITE);
        barData.setValueTextSize(9f);
        barData.setBarWidth(0.25f);

        xAxis1.setAxisMinimum(-axisPadding);
        xAxis1.setAxisMaxValue(barData.getXMax() + axisPadding);

        mv = new MyMarkerView(this, R.layout.barchart_marker_view_layout);
        binding.stepsBarChart.setMarker(mv);
        if (type.equals(INDIVIDUAL)) {
            binding.rlSpinner.setVisibility(View.GONE);
            binding.rlCommunityName.setVisibility(View.GONE);
        } else {
            binding.rlSpinner.setVisibility(View.VISIBLE);
            binding.rlCommunityName.setVisibility(View.VISIBLE);
        }

        return barData;
    }

    private BarData generateDiagnosticBarChart(String pattern, List<Double> diagnosticsResponse, List<String> weeklyRange) {
        binding.diagnosticsBarChart.clear();
        binding.diagnosticsBarChart.setTouchEnabled(true);
        binding.diagnosticsBarChart.setDragEnabled(false);
        binding.diagnosticsBarChart.setScaleEnabled(false);
        binding.diagnosticsBarChart.setScaleXEnabled(false);
        binding.diagnosticsBarChart.setScaleYEnabled(false);
        binding.diagnosticsBarChart.setPinchZoom(false);
        binding.diagnosticsBarChart.setDoubleTapToZoomEnabled(false);
        binding.diagnosticsBarChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        binding.diagnosticsBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        YAxis rightAxis1 = binding.diagnosticsBarChart.getAxisRight();
        rightAxis1.setEnabled(false);
        rightAxis1.setDrawLabels(false);
        rightAxis1.setDrawGridLines(false);
        rightAxis1.setAxisMinimum(0);
        rightAxis1.setDrawAxisLine(true);

        YAxis leftAxis1 = binding.diagnosticsBarChart.getAxisLeft();
        leftAxis1.setDrawAxisLine(true);
        leftAxis1.setDrawLabels(true);
        leftAxis1.setAxisMinimum(0); // VitalsActivity.this replaces setStartAtZero(true)
        leftAxis1.setEnabled(true);
        leftAxis1.setDrawLimitLinesBehindData(false);
        leftAxis1.setDrawGridLines(false);

        XAxis xAxis1 = binding.diagnosticsBarChart.getXAxis();
        xAxis1.setDrawGridLines(false);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setDrawAxisLine(true);
        xAxis1.setGranularity(1f);

//        binding.binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue(300);

        binding.diagnosticsBarChart.setExtraOffsets(0f, 0f, 0f, 16f);
        binding.diagnosticsBarChart.setDrawBorders(false);
        binding.diagnosticsBarChart.getDescription().setEnabled(false);
        List<String> week = new ArrayList<>();
        if (pattern.equals(WEEKLY)) {
            for (int i = 0; i < weeklyRange.size(); i++) {
                week.add(weeklyRange.get(i));
            }
        }
        binding.diagnosticsBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getWeekWithDates(pattern, week, this.getResources())));

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        entries1.clear();

        entries1.addAll(getWeeklyDataBarSingle(diagnosticsResponse, "Diagnostics"));

//        if (entries1.size() > 0 && mv != null){
//            mv.refreshContent(entries1.get(0), new Highlight(entries1.get(0).getX(), entries1.get(0).getY(), 0));
//        }
        BarDataSet set1 = null;

        Entry highestValue1 = new Entry();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            highestValue1 = entries1.stream().max(Comparator.comparing(Entry::getY)).get();
        }

        setLimitLinesToGraph(leftAxis1, highestValue1.getY());

        String label = "bpm";

        switch (diagnosticType.toLowerCase()) {
            case "heart rate":
                label = "Beats Per Minute";
                break;
            case "systolic":
            case "diastolic":
                label = "mmHg";
                break;
            case "respiratory rate":
                label = "Breaths Per Minute";
                break;
            case "oxygen level":
                label = "SpO2(%)";
                break;
        }

        binding.tvUnitDiagnostics.setText(diagnosticUnit);

        int setColor3 = this.getResources().getColor(R.color.pink);

        set1 = new BarDataSet(entries1, "Steps");

        set1.setColor(setColor3);
        binding.diagnosticsBarChart.getLegend().setEnabled(false);
        binding.diagnosticsBarChart.getDescription().setEnabled(false);

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
        binding.diagnosticsBarChart.setMarker(mv);
        if (type.equals(INDIVIDUAL)) {
            binding.rlSpinner.setVisibility(View.GONE);
            binding.rlCommunityName.setVisibility(View.GONE);
        } else {
            binding.rlSpinner.setVisibility(View.VISIBLE);
            binding.rlCommunityName.setVisibility(View.VISIBLE);
        }

        return barData;
    }

    private void setLimitLinesToGraph(YAxis leftAxis1, float highestValue1) {
        Float max = Float.parseFloat(String.valueOf(diagnosticMaxVal));
        Float min = Float.parseFloat(String.valueOf(diagnosticMinVal));

        leftAxis1.removeAllLimitLines();
        LimitLine limitLine = new LimitLine(max, "Ideal max");
        limitLine.setLineColor(context.getResources().getColor(R.color.light_blue));
        limitLine.setLineWidth(0.1f);
        limitLine.setTextColor(R.color.grayLight);
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        leftAxis1.addLimitLine(limitLine);

        LimitLine limitLine2 = new LimitLine(min, "Ideal min");
        limitLine2.setLineColor(context.getResources().getColor(R.color.light_blue));
        limitLine2.setLineWidth(0.1f);
        limitLine2.setTextColor(R.color.light_orange);
        limitLine2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        leftAxis1.addLimitLine(limitLine2);

        int extraValue = 20;
        if (diagnosticUnit.equals("%")) {
            extraValue = 2;
        }

        if (highestValue1 < 1) {
            binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (max + extraValue));
        } else {
            if (highestValue1 <= SYSTOLIC_NORMAL_MAX) {
                binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (max + extraValue));
            } else {
                binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue(highestValue1 + extraValue);
            }
        }

        /*leftAxis1.removeAllLimitLines();
        switch (diagnosticType.toLowerCase()) {
            case "systolic": {
                LimitLine limitLine = new LimitLine(Float.parseFloat(String.valueOf(SYSTOLIC_NORMAL_MIN)),
                        getString(R.string.ideal_min_sys_value_new));
                limitLine.setLineColor(context.getResources().getColor(R.color.light_blue));
                limitLine.setLineWidth(0.1f);
                limitLine.setTextColor(R.color.grayLight);
                limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
                leftAxis1.addLimitLine(limitLine);

                LimitLine limitLine2 = new LimitLine(Float.parseFloat(String.valueOf(SYSTOLIC_NORMAL_MAX)),
                        getString(R.string.ideal_max_sys_value_new));
                limitLine2.setLineColor(context.getResources().getColor(R.color.light_blue));
                limitLine2.setLineWidth(0.1f);
                limitLine2.setTextColor(R.color.grayLight);
                limitLine2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
                leftAxis1.addLimitLine(limitLine2);

                if (highestValue1 < 1) {
                    binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (SYSTOLIC_NORMAL_MAX + 20));
                } else {
                    if (highestValue1 <= SYSTOLIC_NORMAL_MAX) {
                        binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (SYSTOLIC_NORMAL_MAX + 20));
                    } else {
                        binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue(highestValue1 + 20);
                    }
                }

                break;
            }
            case "diastolic":
                LimitLine limitLine3 = new LimitLine(Float.parseFloat(String.valueOf(DIASTOLIC_NORMAL_MIN)),
                        getString(R.string.ideal_min_dia_value_new));
                limitLine3.setLineColor(context.getResources().getColor(R.color.light_blue));
                limitLine3.setLineWidth(0.1f);
                limitLine3.setTextColor(R.color.grayLight);
                limitLine3.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
                leftAxis1.addLimitLine(limitLine3);

                LimitLine limitLine4 = new LimitLine(Float.parseFloat(String.valueOf(DIASTOLIC_NORMAL_MAX)),
                        getString(R.string.ideal_max_dia_value_new));
                limitLine4.setLineColor(context.getResources().getColor(R.color.light_blue));
                limitLine4.setLineWidth(0.1f);
                limitLine4.setTextColor(R.color.grayLight);
                limitLine4.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
                leftAxis1.addLimitLine(limitLine4);

                if (highestValue1 < 1) {
                    binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (DIASTOLIC_NORMAL_MAX + 20));
                } else {
                    if (highestValue1 <= DIASTOLIC_NORMAL_MAX) {
                        binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (DIASTOLIC_NORMAL_MAX + 20));
                    } else {
                        binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue(highestValue1 + 20);
                    }
                }
                break;
            case "oxygen level": {
                LimitLine limitLine = new LimitLine(Float.parseFloat(String.valueOf(OXYGEN_NORMAL_MIN)),
                        getString(R.string.ideal_min_oxy));
                limitLine.setLineColor(context.getResources().getColor(R.color.light_blue));
                limitLine.setLineWidth(0.1f);
                limitLine.setTextColor(R.color.grayLight);
                limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
                leftAxis1.addLimitLine(limitLine);

                LimitLine limitLine2 = new LimitLine(Float.parseFloat(String.valueOf(OXYGEN_NORMAL_MAX)),
                        getString(R.string.ideal_max_oxy));
                limitLine2.setLineColor(context.getResources().getColor(R.color.light_blue));
                limitLine2.setLineWidth(0.1f);
                limitLine2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
                limitLine2.setTextColor(R.color.grayLight);
                leftAxis1.addLimitLine(limitLine2);

                if (highestValue1 < 1) {
                    binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (OXYGEN_NORMAL_MAX + 20));
                } else {
                    if (highestValue1 <= OXYGEN_NORMAL_MAX) {
                        binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (OXYGEN_NORMAL_MAX + 20));
                    } else {
                        binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue(highestValue1 + 20);
                    }
                }

                break;
            }
            case "heart rate": {
                LimitLine limitLine = new LimitLine(Float.parseFloat(String.valueOf(HR_NORMAL_MIN)),
                        getString(R.string.ideal_min_hr));
                limitLine.setLineColor(context.getResources().getColor(R.color.light_blue));
                limitLine.setLineWidth(0.1f);
                limitLine.setTextColor(R.color.grayLight);
                limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
                leftAxis1.addLimitLine(limitLine);

                LimitLine limitLine2 = new LimitLine(Float.parseFloat(String.valueOf(HR_NORMAL_MAX)),
                        getString(R.string.ideal_max_hr));
                limitLine2.setLineColor(context.getResources().getColor(R.color.light_blue));
                limitLine2.setLineWidth(0.1f);
                limitLine2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
                limitLine2.setTextColor(R.color.grayLight);
                leftAxis1.addLimitLine(limitLine2);

                if (highestValue1 < 1) {
                    binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (HR_NORMAL_MAX + 20));
                } else {
                    if (highestValue1 <= HR_NORMAL_MAX) {
                        binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (HR_NORMAL_MAX + 20));
                    } else {
                        binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue(highestValue1 + 20);
                    }
                }

                break;
            }
            case "respiratory rate": {
                LimitLine limitLine = new LimitLine(Float.parseFloat(String.valueOf(RR_NORMAL_MIN)),
                        getString(R.string.ideal_min_rr));
                limitLine.setLineColor(context.getResources().getColor(R.color.light_blue));
                limitLine.setLineWidth(0.1f);
                limitLine.setTextColor(R.color.grayLight);
                limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
                leftAxis1.addLimitLine(limitLine);

                LimitLine limitLine2 = new LimitLine(Float.parseFloat(String.valueOf(RR_NORMAL_MAX)),
                        getString(R.string.ideal_max_rr));
                limitLine2.setLineColor(context.getResources().getColor(R.color.light_blue));
                limitLine2.setLineWidth(0.1f);
                limitLine2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
                limitLine2.setTextColor(R.color.grayLight);
                leftAxis1.addLimitLine(limitLine2);

                if (highestValue1 < 1) {
                    binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (RR_NORMAL_MAX + 10));
                } else {
                    if (highestValue1 <= RR_NORMAL_MAX) {
                        binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue((float) (RR_NORMAL_MAX + 10));
                    } else {
                        binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue(highestValue1 + 10);
                    }
                }

                break;
            }
        }*/
    }


/*
    private BarData generateStepsBarChartCommunity(String pattern, MultipleGraphDataResponse fetchGraphResponse) {
        binding.stepsBarChart.clear();
        binding.stepsBarChart.setTouchEnabled(true);
        binding.stepsBarChart.setDragEnabled(false);
        binding.stepsBarChart.setScaleEnabled(false);
        binding.stepsBarChart.setScaleXEnabled(false);
        binding.stepsBarChart.setScaleYEnabled(false);
        binding.stepsBarChart.setPinchZoom(false);
        binding.stepsBarChart.setDoubleTapToZoomEnabled(false);
        binding.stepsBarChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        binding.stepsBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

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

//        binding.binding.stepsBarChart.getAxisLeft().setAxisMaxValue(300);

        binding.stepsBarChart.setExtraOffsets(0f, 0f, 0f, 16f);
        binding.stepsBarChart.setDrawBorders(false);
        binding.stepsBarChart.getDescription().setEnabled(false);
        binding.stepsBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getWeekStrings(pattern, this.getResources())));

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        entries1.clear();

        entries1.addAll(getWeeklyDataBarCommunity(fetchGraphResponse, "happy insights"));

//        if (entries1.size() > 0 && mv != null){
//            mv.refreshContent(entries1.get(0), new Highlight(entries1.get(0).getX(), entries1.get(0).getY(), 0));
//        }
        BarDataSet set1 = null;


//        int setColor3 = this.getResources().getColor(R.color.light_pink);


        set1 = new BarDataSet(entries1, "Steps");

//        set1.setColor(setColor3);

//        if (fetchGraphResponse.getData().getSt().getDataPoints() != null) {
        set1.setColors(getColors(), ActOMeterActivity.this);
//        }
        binding.stepsBarChart.getLegend().setEnabled(false);
        binding.stepsBarChart.getDescription().setEnabled(false);

        set1.setDrawValues(false);
        BarData barData;
        LegendEntry l1, l2;

        barData = new BarData(set1);

        barData.setValueTextColor(Color.WHITE);
        barData.setValueTextSize(9f);
        barData.setBarWidth(0.25f);

        xAxis1.setAxisMinimum(-axisPadding);
        xAxis1.setAxisMaxValue(barData.getXMax() + axisPadding);

        mv1 = new MyMarkerViewForCommunity(this, R.layout.barchart_marker_view_layout);
        binding.stepsBarChart.setMarker(mv1);

        return barData;
    }
*/

    private BarData generateDiagnosticsBarChartCommunity(String pattern, MultipleGraphDataResponse fetchGraphResponse) {
        binding.diagnosticsBarChart.clear();
        binding.diagnosticsBarChart.setTouchEnabled(true);
        binding.diagnosticsBarChart.setDragEnabled(false);
        binding.diagnosticsBarChart.setScaleEnabled(false);
        binding.diagnosticsBarChart.setScaleXEnabled(false);
        binding.diagnosticsBarChart.setScaleYEnabled(false);
        binding.diagnosticsBarChart.setPinchZoom(false);
        binding.diagnosticsBarChart.setDoubleTapToZoomEnabled(false);
        binding.diagnosticsBarChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        binding.diagnosticsBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        YAxis rightAxis1 = binding.diagnosticsBarChart.getAxisRight();
        rightAxis1.setEnabled(false);
        rightAxis1.setDrawLabels(false);
        rightAxis1.setDrawGridLines(false);
        rightAxis1.setAxisMinimum(0);
        rightAxis1.setDrawAxisLine(true);

        YAxis leftAxis1 = binding.diagnosticsBarChart.getAxisLeft();
        leftAxis1.setDrawAxisLine(true);
        leftAxis1.setDrawLabels(true);
        leftAxis1.setAxisMinimum(0); // VitalsActivity.this replaces setStartAtZero(true)
        leftAxis1.setEnabled(true);
        leftAxis1.setDrawLimitLinesBehindData(false);
        leftAxis1.setDrawGridLines(false);

        XAxis xAxis1 = binding.diagnosticsBarChart.getXAxis();
        xAxis1.setDrawGridLines(false);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setDrawAxisLine(true);
        xAxis1.setGranularity(1f);

//        binding.binding.stepsBarChart.getAxisLeft().setAxisMaxValue(300);

        binding.diagnosticsBarChart.setExtraOffsets(0f, 0f, 0f, 16f);
        binding.diagnosticsBarChart.setDrawBorders(false);
        binding.diagnosticsBarChart.getDescription().setEnabled(false);
        binding.diagnosticsBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getWeekStrings(pattern, this.getResources())));

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        entries1.clear();

//        entries1.addAll(getWeeklyDataBarCommunity(fetchGraphResponse, "Diagnostics"));

//        if (entries1.size() > 0 && mv != null){
//            mv.refreshContent(entries1.get(0), new Highlight(entries1.get(0).getX(), entries1.get(0).getY(), 0));
//        }
        BarDataSet set1 = null;


//        int setColor3 = this.getResources().getColor(R.color.light_pink);


        set1 = new BarDataSet(entries1, "Steps");

//        set1.setColor(setColor3);

//        if (fetchGraphResponse.getData().getSt().getDataPoints() != null) {
        set1.setColors(getColors(), ActOMeterActivity.this);
//        }
        binding.diagnosticsBarChart.getLegend().setEnabled(false);
        binding.diagnosticsBarChart.getDescription().setEnabled(false);

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
        binding.diagnosticsBarChart.setMarker(mv);

        return barData;
    }

    public int[] getColors() {
        int[] colors = new int[]{R.color.blue_cyan, R.color.blue, R.color.light_orange, R.color.happy_light_grey, R.color.dark_pink, R.color.light_blue};
        return colors;
    }

    private void refreshAuthToken(String periodType, String activityType, int communityId, String diagnosticPeriod, String diagnosticType) {
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
                    SharedPreference.init(context);
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    fetchCommunityGraphDataSingle(periodType, activityType, communityId, diagnosticPeriod, diagnosticType);

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
                    getAbsorbDashboard("Happy footprint");
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

    private void fetchDiagnosticType() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<FetchDiagnosticTypeResponse> call = apiInterfaceWyh.fetchDiagnosticType(SharedPref.getAuthToken());
        call.enqueue(new Callback<FetchDiagnosticTypeResponse>() {
            @Override
            public void onResponse(Call<FetchDiagnosticTypeResponse> call, Response<FetchDiagnosticTypeResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                getAbsorbDashboard("Happy footprint");
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.actometer_fetch_diagnostic_type_success));
                    diagnosticTypeList = response.body().getData();


                    List<String> diagnosticTypeNames = new ArrayList<>();
                    for (FetchDiagnosticTypeResponse.Datum item : diagnosticTypeList) {
                        diagnosticTypeNames.add(item.getTestName());
                    }

                    ArrayAdapter<String> diagnosticTypeSpinnerAdapter = new ArrayAdapter<>(context,
                            R.layout.spinner_item, diagnosticTypeNames);
                    diagnosticTypeSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                    binding.spinnerDiagnosticType.setPrompt("Diagnostic Type");
                    binding.spinnerDiagnosticType.setAdapter(diagnosticTypeSpinnerAdapter);
                    binding.spinnerDiagnosticType.setSelection(0);

                    binding.spinnerDiagnosticType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            diagnosticType = diagnosticTypeList.get(position).getTestCode();
                            diagnosticUnit = diagnosticTypeList.get(position).getUnit();
                            diagnosticMaxVal = diagnosticTypeList.get(position).getMaxvalue();
                            diagnosticMinVal = diagnosticTypeList.get(position).getMinvalue();
                            initialSingleView();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.actometer_fetch_diagnostic_type_failed));
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchDiagnosticTypeResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                getAbsorbDashboard("Happy footprint");
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.actometer_fetch_diagnostic_type_failed));
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openHappyMartDisclaimer(String cameFrom) {
        Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
        intent.putExtra("came_from", cameFrom);
        startActivity(intent);
    }

    private void gotoIntroPage(String cameFrom, String categoryName, boolean isAnalysis, boolean shownScreen) {
        Intent intent = new Intent(context, WellBeingDisclaimerActivity.class);
        intent.putExtra("came_from", cameFrom);
        intent.putExtra("CategoryName", categoryName);
        intent.putExtra("isAnalysis", isAnalysis);
        intent.putExtra("shownScreen", shownScreen);
        startActivity(intent);
    }

    private void updateRewards() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("ActivityUploads", "")
                .addFormDataPart("eventName", "SyncDevice")
                .addFormDataPart("eventCategory", TAG_REWARD_EVENT)
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "Rewards/EarnRewards")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        Log.d("rewards", new Gson().toJson(request));

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                finish();
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Log.d("rewards", new Gson().toJson(response.body()));
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                                }
                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                                }
                                showRewardsPopupDialogBox();
                            }
                        });
                    }

                }
            }
        });
    }

    private void showRewardsPopupDialogBox() {
        PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(0);
        showRewardsPopupNew(firstData.getValue(), context);
    }

    private void showRewardsPopupNew(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogRewardPopup = alertBuilder.create();
        alertDialogRewardPopup.setCancelable(false);
        if (!alertDialogRewardPopup.isShowing())
            alertDialogRewardPopup.show();

        NewDashboardHelper.Companion.getPopUpShowModels().remove(0);

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");
//        binding.btnPositive.setText("Collect");


        alertDialogRewardPopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPositiveBtn) {
                    isPositiveBtn = false;
                    Intent intent = new Intent(context, RewardsActivity.class);
                    intent.putExtra("currentIndex", 1);
                    startActivity(intent);
                    finish();
                }
            }
        });


        binding.tvPoints.setText(points);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> alertDialogRewardPopup.dismiss());


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogRewardPopup.dismiss();
        });
        binding.scratchView.setScratchListener(ActOMeterActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialogRewardPopup.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();


        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialogRewardPopup.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialogRewardPopup.getWindow().setLayout((int) (displayRectangle.width() * 0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }


    private void fetureArrow() {
        binding.ivNext.setEnabled(currentGraphIndex != 0);
    }

    private void prevGraphButton() {
        binding.ivPrev.setOnClickListener(v -> {
            try {
                if (periodType.equalsIgnoreCase(QUARTERLY)) {
                    currentGraphIndex = currentGraphIndex - 4;
                } else if (periodType.equalsIgnoreCase(DAILY)) {
                    if (currentGraphIndex == -6) {
                        return;
                    } else {
                        currentGraphIndex--;
                    }
                } else {
                    currentGraphIndex--;
                }
                fetureArrow();
                fetchCommunityGraphDataSingle(periodType, activityType, communityId, diagnosticPeriod, diagnosticType);


            } catch (Exception e) {
                Log.e("HappyYou", "Error occurred", e);
            }
        });
    }

    private void nextGraphButton() {
        binding.ivNext.setOnClickListener(v -> {
            if (periodType.equalsIgnoreCase(QUARTERLY)) {
                currentGraphIndex = currentGraphIndex + 4;
            } else {
                currentGraphIndex++;
            }
            fetureArrow();
            fetchCommunityGraphDataSingle(periodType, activityType, communityId, diagnosticPeriod, diagnosticType);

        });
    }

    @SuppressLint("SetTextI18n")
    private void setDateRange() {

        Log.d("called", "called");
        int lastIndex = fetchGraphResponse.getData().getHappyinsights().getDataPoints().size() - 1;
        binding.llMoreGraphData.setVisibility(View.VISIBLE);

        String date = formatDateFromString("yyyy-MM-dd", "MMM dd yyyy", fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(0).getRecordDate()).split(" ")[2];
        String month = formatDateFromString("yyyy-MM-dd", "MMM", fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(0).getRecordDate());
        if (periodType.equalsIgnoreCase(MONTHLY) || periodType.equalsIgnoreCase(QUARTERLY)) {
            binding.tvDateRange.setText("Jan 01 " + date + "- Dec 31 " + date);
        } else if (periodType.equalsIgnoreCase(DAILY)) {
            try {
                String startDate = formatDateFromString("yyyy-MM-dd", "dd MMM", fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(0).getRecordDate());
                String endDate = formatDateFromString("yyyy-MM-dd", "dd MMM", fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(6).getRecordDate());

                binding.tvDateRange.setText(startDate + " - " + endDate);

            } catch (Exception e) {
                Log.e("HappyYou", "Error occurred", e);
            }

        } else {
            binding.tvDateRange.setText(formatDateFromString("yyyy-MM-dd", "MMM yyyy", fetchGraphResponse.getData().getHappyinsights().getDataPoints().get(0).getRecordDate()));
        }
    }

    private void fetureArrowDiastolic() {
        binding.ivNextDiastolic.setEnabled(currentGraphIndexDiagostics != 0);
    }

    private void prevGraphButtonDiastolic() {
        binding.ivPrevDiastolic.setOnClickListener(v -> {
            if (diagnosticPeriodType.equalsIgnoreCase(QUARTERLY)) {
                currentGraphIndexDiagostics = currentGraphIndexDiagostics - 4;
            } else if (diagnosticPeriodType.equalsIgnoreCase(DAILY)) {
                if (currentGraphIndexDiagostics == -6) {
                    return;
                } else {
                    currentGraphIndexDiagostics--;
                }
            } else {
                currentGraphIndexDiagostics--;
            }
            fetureArrowDiastolic();
            fetchCommunityGraphDataSingle(periodType, activityType, communityId, diagnosticPeriodType, diagnosticType);

        });
    }

    private void nextGraphButtonDiastolic() {
        binding.ivNextDiastolic.setOnClickListener(v -> {
            if (diagnosticPeriodType.equalsIgnoreCase(QUARTERLY)) {
                currentGraphIndexDiagostics = currentGraphIndexDiagostics + 4;
            } else {
                currentGraphIndexDiagostics++;
            }
            fetureArrowDiastolic();
            fetchCommunityGraphDataSingle(periodType, activityType, communityId, diagnosticPeriodType, diagnosticType);

        });
    }

    @SuppressLint("SetTextI18n")
    private void setDateRangeDiastolic() {
        Log.d("called", "called");

        List<MultipleGraphDataResponse.Data.DiagnosticGraph.RecordDate> data = fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate();
        int lastIndex = data.size() - 1;
        binding.llMoreGraphDataDiastolic.setVisibility(View.VISIBLE);


        String date = formatDateFromString("yyyy-MM-dd", "MMM dd yyyy", data.get(0).getGraphdate()).split(" ")[2];
        if (diagnosticPeriodType.equalsIgnoreCase(MONTHLY) || diagnosticPeriodType.equalsIgnoreCase(QUARTERLY)) {
            binding.tvDateRangeDiastolic.setText("Jan 01 " + date + "- Dec 31 " + date);
        } else if (periodType.equalsIgnoreCase(DAILY)) {
            try {
                String startDate = formatDateFromString("yyyy-MM-dd", "dd MMM", fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate().get(0).getGraphdate());
                String endDate = formatDateFromString("yyyy-MM-dd", "dd MMM", fetchGraphResponse.getData().getDiagnosticGraph().getRecordDate().get(6).getGraphdate());

                binding.tvDateRangeDiastolic.setText(startDate + " - " + endDate);

            } catch (Exception e) {
                Log.e("HappyYou", "Error occurred", e);
            }

        } else {
            binding.tvDateRangeDiastolic.setText(formatDateFromString("yyyy-MM-dd", "MMM yyyy", data.get(0).getGraphdate()));
        }

    }
}