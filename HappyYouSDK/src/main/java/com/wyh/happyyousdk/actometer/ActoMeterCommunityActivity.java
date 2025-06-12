package com.wyh.happyyousdk.actometer;

import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.getWeekWithDates;
import static com.wyh.happyyousdk.utils.CommonUtils.getWeeklyDataBarCommunity;
import static com.wyh.happyyousdk.utils.Constants.BURNEDGRAPH;
import static com.wyh.happyyousdk.utils.Constants.CALORIE;
import static com.wyh.happyyousdk.utils.Constants.CONSUMEDGRAPH;
import static com.wyh.happyyousdk.utils.Constants.DAILY;
import static com.wyh.happyyousdk.utils.Constants.HappyMartPharmacy;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.MONTHLY;
import static com.wyh.happyyousdk.utils.Constants.QUARTERLY;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.SYSTOLIC_NORMAL_MAX;
import static com.wyh.happyyousdk.utils.Constants.WATER;
import static com.wyh.happyyousdk.utils.Constants.WEEKLY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;
;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.WellBeingActivity;
import com.wyh.happyyousdk.WellBeingDisclaimerActivity;
import com.wyh.happyyousdk.absorb.HealthHacksActivity;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.happyMarket.NewHappyMartActivity;
import com.wyh.happyyousdk.model.request.absorb.AddBookmarkRequest;
import com.wyh.happyyousdk.model.request.absorb.GetDashboardDataRequest;
import com.wyh.happyyousdk.model.response.absorb.AddBookmarkResponse;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.actometer.adapter.ActometerStackBarDiagnosticTagsAdapter;
import com.wyh.happyyousdk.actometer.adapter.ActometerStackBarHappyInsightsTagsAdapter;
import com.wyh.happyyousdk.actometer.adapter.StackBarUsersAdapter;
import com.wyh.happyyousdk.model.request.MultipleGraphDataRequest;
import com.wyh.happyyousdk.model.response.MultipleGraphDataResponse;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;

import com.wyh.happyyousdk.dass21.Dass21AnalysisActivity;
import com.wyh.happyyousdk.dass21.Dass21QuestionsActivity;
import com.wyh.happyyousdk.databinding.ActivityActoMeterCommunityBinding;
import com.wyh.happyyousdk.happyMarket.HappyMartDisclaimerActivity;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.ira.IRAAnalysisActivity;
import com.wyh.happyyousdk.ira.IraActivity;
import com.wyh.happyyousdk.model.response.ehr.FetchDiagnosticTypeResponse;
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse;
import com.wyh.happyyousdk.model.response.dass21.DassAnalysisResponse;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.trends.MarkerDataClass;
import com.wyh.happyyousdk.trends.adapter.TrendsQuickReadsAdapter;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.MyMarkerViewForCommunity;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActoMeterCommunityActivity extends AppCompatActivity {
    ActivityActoMeterCommunityBinding binding;
    Context context;
    ApiInterfaceWyh apiInterfaceWyh;
    ProgressDialog progressDialog;
    float axisPadding = 0.5f;
    MyMarkerViewForCommunity mv;
    boolean btnNutrition = true, btnWorkout = false;
    List<String> userList = new ArrayList<>();
    List<String> sortedUserList = new ArrayList<>();
    List<GetDashboardDataResponse.Data.TagName> tagNameList;
    List<String> tagNameListString = new ArrayList<>();
    List<LegendEntry> legendEntries = new ArrayList<>();

    MultipleGraphDataResponse multipleGraphDataResponse, multipleGraphDataDiagnosticResponse;

    List<FetchDiagnosticTypeResponse.Datum> diagnosticTypeList;

    int communityId, positionReads = 0, positionTribe = 0, currentGraphIndex = 0, currentGraphIndexDiagostics = 0;
    String selectedTag = "Happy Footprints", activityType, communityType, communityName, diagnosticType, activityPeriodType = DAILY, diagnosticPeriod, diagnosticPeriodType = DAILY, diagnosticUnit, diagnosticMaxVal, diagnosticMinVal, adminTribe;
    ActometerStackBarHappyInsightsTagsAdapter actoMeterHappyInsightsTagsAdapter;
    ActometerStackBarDiagnosticTagsAdapter actoMeterDiagnosticTagsAdapter;

    String dateGraph = "";
    boolean usersFetched = true, cameFrom;
    boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_acto_meter_community);
        context = this;

        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());

        adminTribe = getIntent().getStringExtra("adminTribe");

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "ActOMeterTribe");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        getIntentData();

        //getAllUserCommunities();

        fetureArrow();
        prevGraphButton();
        nextGraphButton();
        fetureArrowDiastolic();
        prevGraphButtonDiastolic();
        nextGraphButtonDiastolic();
        initialValues();


        getAbsorbDashboard("Happy footprint");

//      setDiagnosticTags();
        setHappyInsightTags();
        binding.ivHome.setOnClickListener(view -> {
            Intent i = new Intent(this, NewDashboardActivity.class);
            startActivity(i);
            finish();
        });

        binding.rlQuickRead.setOnClickListener(view -> {
            Intent i = new Intent(this, HealthHacksActivity.class);
            startActivity(i);
        });

        binding.llSpinner.setOnClickListener(v -> {

        });

        binding.rlTribes.setOnClickListener(view -> {

        });
        binding.rlHappyServices.setOnClickListener(view -> {
            Intent intent = new Intent(context, NewHappyMartActivity.class);
            startActivity(intent);
        });

        binding.llWellBeing.setOnClickListener(v -> {
            Intent intent = new Intent(context, WellBeingActivity.class);
            startActivity(intent);
        });

        binding.btnDaily.setOnClickListener(view -> {
            activityPeriodType = DAILY;
            currentGraphIndex = 0;
            /*actoMeterDiagnosticTagsAdapter.updatePeriodType(activityPeriodType);
            actoMeterDiagnosticTagsAdapter.notifyDataSetChanged();*/
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphHappyInsightsStack(activityPeriodType, CONSUMEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                } else {
                    fetchCommunityGraphHappyInsightsStack(activityPeriodType, BURNEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                }
            } else {
                fetchCommunityGraphHappyInsightsStack(activityPeriodType, activityType, communityId, diagnosticPeriod, diagnosticType);
            }
        });
        binding.btnWeekly.setOnClickListener(view -> {
            activityPeriodType = WEEKLY;
            currentGraphIndex = 0;
            /*actoMeterDiagnosticTagsAdapter.updatePeriodType(activityPeriodType);
            actoMeterDiagnosticTagsAdapter.notifyDataSetChanged();*/
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphHappyInsightsStack(activityPeriodType, CONSUMEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                } else {
                    fetchCommunityGraphHappyInsightsStack(activityPeriodType, BURNEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                }
            } else {
                fetchCommunityGraphHappyInsightsStack(activityPeriodType, activityType, communityId, diagnosticPeriod, diagnosticType);
            }
        });
        binding.btnMonthly.setOnClickListener(view -> {
            activityPeriodType = MONTHLY;
            currentGraphIndex = 0;
            /*actoMeterDiagnosticTagsAdapter.updatePeriodType(activityPeriodType);
            actoMeterDiagnosticTagsAdapter.notifyDataSetChanged();*/
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));

            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphHappyInsightsStack(activityPeriodType, CONSUMEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                } else {
                    fetchCommunityGraphHappyInsightsStack(activityPeriodType, BURNEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                }
            } else {
                fetchCommunityGraphHappyInsightsStack(activityPeriodType, activityType, communityId, diagnosticPeriod, diagnosticType);
            }
        });
        binding.btnQuarterly.setOnClickListener(view -> {
            activityPeriodType = QUARTERLY;
            currentGraphIndex = 0;
            /*actoMeterDiagnosticTagsAdapter.updatePeriodType(activityPeriodType);
            actoMeterDiagnosticTagsAdapter.notifyDataSetChanged();*/
            binding.btnDaily.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeekly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthly.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterly.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphHappyInsightsStack(activityPeriodType, CONSUMEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                } else {
                    fetchCommunityGraphHappyInsightsStack(activityPeriodType, BURNEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                }
            } else {
                fetchCommunityGraphHappyInsightsStack(activityPeriodType, activityType, communityId, diagnosticPeriod, diagnosticType);
            }
        });

        binding.btnDailyDiagnostics.setOnClickListener(view -> {
            diagnosticPeriodType = DAILY;
            currentGraphIndexDiagostics = 0;
            binding.btnDailyDiagnostics.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnWeeklyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));

            fetchCommunityGraphDiagnosticsStack(activityPeriodType, activityType, communityId, DAILY, diagnosticType);
        });
        binding.btnWeeklyDiagnostics.setOnClickListener(view -> {
            diagnosticPeriodType = WEEKLY;
            currentGraphIndexDiagostics = 0;
            binding.btnDailyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeeklyDiagnostics.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnMonthlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));

            fetchCommunityGraphDiagnosticsStack(activityPeriodType, activityType, communityId, WEEKLY, diagnosticType);

        });
        binding.btnMonthlyDiagnostics.setOnClickListener(view -> {
            diagnosticPeriodType = MONTHLY;
            currentGraphIndexDiagostics = 0;
            binding.btnDailyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeeklyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));
            binding.btnQuarterlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            fetchCommunityGraphDiagnosticsStack(activityPeriodType, activityType, communityId, MONTHLY, diagnosticType);
        });
        binding.btnQuarterlyDiagnostics.setOnClickListener(view -> {
            diagnosticPeriodType = QUARTERLY;
            currentGraphIndexDiagostics = 0;
            binding.btnDailyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnWeeklyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnMonthlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_btn_grey_border));
            binding.btnQuarterlyDiagnostics.setBackground(getDrawable(R.drawable.wyh_round_btn_grey));

            fetchCommunityGraphDiagnosticsStack(activityPeriodType, activityType, communityId, QUARTERLY, diagnosticType);

        });

        binding.btnCalConsumed.setOnClickListener(view -> {
            activityPeriodType = DAILY;
            binding.btnCalConsumed.setText("Calories Consumed");
            btnNutrition = true;
            btnWorkout = false;

            binding.btnCalConsumed.setTextColor(getColor(R.color.white));
            binding.btnCalBurned.setTextColor(getColor(R.color.black));

            binding.btnCalConsumed.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));
            binding.btnCalBurned.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            fetchCommunityGraphHappyInsightsStack(activityPeriodType, CONSUMEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
        });

        binding.btnCalBurned.setOnClickListener(view -> {
            activityPeriodType = DAILY;

            binding.btnCalBurned.setText("Calories Burned");
            btnNutrition = false;
            btnWorkout = true;

            binding.btnCalBurned.setTextColor(getColor(R.color.white));
            binding.btnCalConsumed.setTextColor(getColor(R.color.black));
            binding.btnCalConsumed.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnCalBurned.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));
            fetchCommunityGraphHappyInsightsStack(activityPeriodType, BURNEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
        });
        if(selectedTag != null && (selectedTag.equals(CALORIE) || selectedTag.equals(BURNEDGRAPH))){
            binding.btnCalBurned.performClick();
        }

        binding.btnSystolic.setOnClickListener(view -> {
            diagnosticPeriod = DAILY;
            binding.btnSystolic.setTextColor(getColor(R.color.white));
            binding.btnDiastolic.setTextColor(getColor(R.color.black));

            binding.btnSystolic.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));
            binding.btnDiastolic.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            fetchCommunityGraphDiagnosticsStack(activityPeriodType, activityType, communityId, diagnosticPeriod, "systolic");

        });

        binding.btnDiastolic.setOnClickListener(view -> {
            diagnosticPeriod = DAILY;
            binding.btnDiastolic.setTextColor(getColor(R.color.white));
            binding.btnSystolic.setTextColor(getColor(R.color.black));
            binding.btnSystolic.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
            binding.btnDiastolic.setBackground(getDrawable(R.drawable.wyh_round_btn_orange));
            fetchCommunityGraphDiagnosticsStack(activityPeriodType, activityType, communityId, diagnosticPeriod, "diastolic");
        });

        LinearSnapHelper linearSnapHelper2 = new SnapHelperOneByOne();
        linearSnapHelper2.attachToRecyclerView(binding.rvQuickReads);

        /*binding.rlMedPay.setOnClickListener(view -> {
            openHappyMartDisclaimer(HappyMartOPD);
        });*/

        binding.rlPharmacy.setOnClickListener(view -> {
            openHappyMartDisclaimer(HappyMartPharmacy);
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
        /*binding.rlFitness.setOnClickListener(view -> {
            openHappyMartDisclaimer(HappyMartFitness);
        });*/

        binding.rlKnowYourHealth.setOnClickListener(view -> {
            GetAnalysisResponse getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
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
            IRAHealthScoreResponse iraHealthScoreResponse = new Gson().fromJson(SharedPref.getIRAHealthData(), IRAHealthScoreResponse.class);
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

        binding.ivWellBeingInfo1.setOnClickListener(v -> {
            GetAnalysisResponse getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
            boolean isAnalysis = (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0);
            gotoIntroPage("KnowYourHealth", "Health Score", isAnalysis, false);
        });


        binding.ivWellBeingInfo2.setOnClickListener(view -> {
            IRAHealthScoreResponse iraHealthScoreResponse = new Gson().fromJson(SharedPref.getIRAHealthData(), IRAHealthScoreResponse.class);

            boolean isAnalysis = (iraHealthScoreResponse != null && iraHealthScoreResponse.getIraHealthScoreData() != null &&
                    iraHealthScoreResponse.getIraHealthScoreData().getPlaySports() != null && !iraHealthScoreResponse.getIraHealthScoreData()
                    .getPlaySports().isEmpty() && Integer.parseInt(iraHealthScoreResponse.getIraHealthScoreData().getPlaySports()) > 0);
            gotoIntroPage("KnowYourImmunity", "Immunity Score", isAnalysis, false);
        });

        binding.ivWellBeingInfo3.setOnClickListener(view -> {
            DassAnalysisResponse dassAnalysisResponse = new Gson().fromJson(SharedPref.getDASSAnalysis(), DassAnalysisResponse.class);
            Intent intent;
            boolean isAnalysis = (dassAnalysisResponse != null && dassAnalysisResponse.getData() != null);
            gotoIntroPage("KnowYourDAS", "DAS Score", isAnalysis, false);
        });

        binding.refreshHappyInsights.setOnClickListener(v -> {
            if (activityType.equals(CALORIE)) {
                if (btnNutrition) {
                    fetchCommunityGraphHappyInsightsStack(activityPeriodType, CONSUMEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                } else {
                    fetchCommunityGraphHappyInsightsStack(activityPeriodType, BURNEDGRAPH, communityId, diagnosticPeriod, diagnosticType);
                }
            } else {
                fetchCommunityGraphHappyInsightsStack(activityPeriodType, activityType, communityId, diagnosticPeriod, diagnosticType);
            }
        });

    }

    private void setHappyInsightTags() {
        tagNameListString.clear();
        tagNameListString.add("Happy Footprints");
        tagNameListString.add("Pillow Time");
        tagNameListString.add("H20");
        tagNameListString.add("Cal - Count");
        tagNameListString.add("Zen Zone");
        if (communityType.equalsIgnoreCase("family")) {
            tagNameListString.add("Weight");
        }
        Log.d("rvTag", new Gson().toJson(tagNameListString));
        int selectedPosition = 0;
        if(selectedTag.equals(WATER)){
            selectedPosition = 2;
        }else if(selectedTag.equals(CALORIE) || selectedTag.equals(CONSUMEDGRAPH)){
            selectedPosition = 3;
        }
        actoMeterHappyInsightsTagsAdapter = new ActometerStackBarHappyInsightsTagsAdapter(context, tagNameListString, selectedPosition, communityId,
                diagnosticPeriod, diagnosticType);
        LinearLayoutManager actoMeterHappyInsightsLinearLayoutManager = new LinearLayoutManager(context);
        actoMeterHappyInsightsLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvTags.setOnFlingListener(null);
        binding.rvTags.setLayoutManager(actoMeterHappyInsightsLinearLayoutManager);
        binding.rvTags.setAdapter(actoMeterHappyInsightsTagsAdapter);
        if(selectedTag.equals(WATER)){
            actoMeterHappyInsightsLinearLayoutManager.scrollToPosition(2);

        }else if(selectedTag.equals(CALORIE) || selectedTag.equals(CONSUMEDGRAPH)){
            actoMeterHappyInsightsLinearLayoutManager.scrollToPosition(3);
        }else{
            actoMeterHappyInsightsLinearLayoutManager.scrollToPosition(0);

        }


        LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvTags);

    }

    private void setDiagnosticTags() {
        List<String> tagNameList = new ArrayList<>();

        tagNameList.add("Heart rate");
        tagNameList.add("Respiratory rate");
        tagNameList.add("Oxygen level");
        tagNameList.add("Blood Pressure");
        actoMeterDiagnosticTagsAdapter = new ActometerStackBarDiagnosticTagsAdapter(context,
                tagNameList, 0, communityId, activityType, activityPeriodType);
        LinearLayoutManager actoMeterDiagnosticLinearLayoutManager = new LinearLayoutManager(context);
        actoMeterDiagnosticLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvDiagnosticsTags.setOnFlingListener(null);
        binding.rvDiagnosticsTags.setLayoutManager(actoMeterDiagnosticLinearLayoutManager);
        binding.rvDiagnosticsTags.setAdapter(actoMeterDiagnosticTagsAdapter);


//        actoMeterDiagnosticLinearLayoutManager.scrollToPosition(selectedItem);

        LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvDiagnosticsTags);

    }

    private void getIntentData() {
        cameFrom = getIntent().getBooleanExtra("cameFrom", false);
        if (getIntent().getStringExtra("communityType") != null) {
            communityType = getIntent().getStringExtra("communityType");
        }
        if (getIntent().getStringExtra("communityId") != null) {
            communityId = Integer.parseInt(getIntent().getStringExtra("communityId"));
        }
        if (getIntent().getStringExtra("communityName") != null) {
            communityName = getIntent().getStringExtra("communityName");
        }
        if (getIntent().getStringExtra("selectedTag") != null) {
            selectedTag = getIntent().getStringExtra("selectedTag");
            if(selectedTag != null && selectedTag.equals(CALORIE)){
                //selectedTag = BURNEDGRAPH;
                binding.btnCalBurned.performClick();
            }
            activityType = selectedTag;
        }
        /*Type type = new TypeToken<List<TribeSpinnerData>>() {
        }.getType();
        tribeSpinnerDataList = new Gson().fromJson(getIntent().getStringExtra("tribeData"), type);*/
    }

    private void initialValues() {
        binding.includeToolbar.tvBack.setText("TRIBE-O-METER: " + communityType);
        binding.tvCommunityName.setText(communityName);
        activityPeriodType = DAILY;
        if(cameFrom){
            cameFrom = false;
            activityType = selectedTag;
        } else {
            activityType = STEPS;
        }
        diagnosticPeriod = DAILY;
    }

    private void initialCommunityView() {
        if (communityType.equalsIgnoreCase("family")) {
            binding.llDiagnostic.setVisibility(View.VISIBLE);
            if (actoMeterHappyInsightsTagsAdapter != null) {
                if (!tagNameListString.contains("Weight")) {
                    tagNameListString.add("Weight");
                    actoMeterHappyInsightsTagsAdapter.notifyDataSetChanged();
                }
            }
        } else {
            if (actoMeterHappyInsightsTagsAdapter != null) {
                if (tagNameListString.contains("Weight")) {
                    tagNameListString.remove("Weight");
                    actoMeterHappyInsightsTagsAdapter.notifyDataSetChanged();
                }
            }
            binding.llDiagnostic.setVisibility(View.GONE);
        }

        fetchCommunityGraphDiagnosticsStack(activityPeriodType, activityType, communityId, activityPeriodType, diagnosticType);
        fetchCommunityGraphHappyInsightsStack(activityPeriodType, activityType, communityId, activityPeriodType, diagnosticType);
    }

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
        this.diagnosticPeriodType = diagnosticPeriod;
        this.activityPeriodType = periodType;
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
        /*if (diagnosticType.equalsIgnoreCase("Systolic") || diagnosticType.equalsIgnoreCase("diastolic") || diagnosticType.equalsIgnoreCase("blood pressure")) {
            binding.llBloodpressure.setVisibility(View.VISIBLE);
        } else {
            binding.llBloodpressure.setVisibility(View.GONE);
        }*/
        if (progressDialog != null) {
            progressDialog.show();
        }

        MultipleGraphDataRequest multipleGraphDataRequest = new MultipleGraphDataRequest(communityId, periodType, currentGraphIndex, activityType, diagnosticType, diagnosticPeriod, 0);
        Call<MultipleGraphDataResponse> call = apiInterfaceWyh.fetchGraphMultiple(SharedPref.getAuthToken(), multipleGraphDataRequest);
        Log.d("AuthToken","Fetch Graph Request "+new Gson().toJson(multipleGraphDataRequest));
        Log.v("API_Req", call.request().url().toString() + "\n" + new Gson().toJson(multipleGraphDataRequest) + "\n" + SharedPref.getAuthToken() + ", " + SharedPref.getUuid());
        call.enqueue(new Callback<MultipleGraphDataResponse>() {
            @Override
            public void onResponse(Call<MultipleGraphDataResponse> call, Response<MultipleGraphDataResponse> response) {
                try{
                    if (response.isSuccessful() && response.code() == 200) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_success));
                        Log.v("API_RESPONSE", new Gson().toJson(response.body()));
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        multipleGraphDataResponse = response.body();


                        if (multipleGraphDataResponse != null) {
                            userList.clear();
                            for (int i = 0; i < multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().size(); i++) {
                                for (int j = 0; j < multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().get(i).getPoint().size(); j++) {
                                    if (!userList.contains(multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().get(i).getPoint().get(j).getName())) {
                                        userList.add(multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().get(i).getPoint().get(j).getName());
                                    }
                                }
                            }
                            for (int i = 0; i < multipleGraphDataResponse.getData().getDiagnosticGraph().getRecordDate().size(); i++) {
                                for (int j = 0; j < multipleGraphDataResponse.getData().getDiagnosticGraph().getRecordDate().get(i).getPoint().size(); j++) {
                                    if (!userList.contains(multipleGraphDataResponse.getData().getDiagnosticGraph().getRecordDate().get(i).getPoint().get(j).getName())) {
                                        userList.add(multipleGraphDataResponse.getData().getDiagnosticGraph().getRecordDate().get(i).getPoint().get(j).getName());
                                    }
                                }
                            }

                            usersFetched = false;

                            setDateRange();
                        }



                        binding.stepsBarChart.setData(generateBarChartCommunityHappyInsights(periodType, response.body()));
                        binding.stepsBarChart.invalidate();

                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_failed));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<MultipleGraphDataResponse> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Log.d("error2", ""+t.getMessage());
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_failed));
            }
        });
    }

    private void getAbsorbDashboard(String activityType) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetDashboardDataRequest dashboardDataRequest = new GetDashboardDataRequest(activityType, "");
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
//                    List<GetDashboardDataResponse.Data.HealthTv> healthTvList = response.body().getData().getHealthTv();
//                    List<GetDashboardDataResponse.Data.Webinar> webinarList = response.body().getData().getWebinar();
                    tagNameList = response.body().getData().getTagName();

                    if (quickReadList.size() > 0) {
                        setQuickReadData(quickReadList);
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_dashboard_failed));
//                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetDashboardDataResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("error3", ""+t.getMessage());
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
        TrendsQuickReadsAdapter trendsQuickReadsAdapter = new TrendsQuickReadsAdapter(context, dataList, false, true, false, false, new TrendsQuickReadsAdapter.OnItemClickListener() {
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
        this.diagnosticPeriodType = diagnosticPeriod;
        this.activityPeriodType = periodType;
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
        /*if (diagnosticType.equalsIgnoreCase("Systolic") || diagnosticType.equalsIgnoreCase("diastolic") || diagnosticType.equalsIgnoreCase("blood pressure")) {
            binding.llBloodpressure.setVisibility(View.VISIBLE);
        } else {
            binding.llBloodpressure.setVisibility(View.GONE);
        }*/

        MultipleGraphDataRequest multipleGraphDataRequest = new MultipleGraphDataRequest(communityId, periodType, 0, activityType, diagnosticType, diagnosticPeriod, currentGraphIndexDiagostics);
        Call<MultipleGraphDataResponse> call = apiInterfaceWyh.fetchGraphMultiple(SharedPref.getAuthToken(), multipleGraphDataRequest);
        Log.d("AuthToken","Diagnostics Request "+new Gson().toJson(multipleGraphDataRequest));
        Log.v("API_Req", call.request().url().toString() + "\n" + new Gson().toJson(multipleGraphDataRequest) + "\n" + SharedPref.getAuthToken() + ", " + SharedPref.getUuid());
        call.enqueue(new Callback<MultipleGraphDataResponse>() {
            @Override
            public void onResponse(Call<MultipleGraphDataResponse> call, Response<MultipleGraphDataResponse> response) {
                try{
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.code() == 200) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_success));
                        Log.v("API_RESPONSE 2", new Gson().toJson(response.body()));
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (response.body() != null) {
                            multipleGraphDataDiagnosticResponse = response.body();
                            setDateRangeDiastolic();
                            binding.diagnosticsBarChart.setData(generateBarChartCommunityDiagnostics(diagnosticPeriod, response.body()));
                            binding.diagnosticsBarChart.invalidate();
                        }
                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_failed));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MultipleGraphDataResponse> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Log.d("error4", ""+t.getMessage());
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_graph_community_wise_failed));
            }
        });
    }

    private void setUserList() {
        StackBarUsersAdapter stackBarUsersAdapter = new StackBarUsersAdapter(context, userList, String.valueOf(communityId), communityName, communityType, getColors(userList.size()), activityType);
        LinearLayoutManager stackBarUsersLinearLayoutManager = new LinearLayoutManager(context);
        stackBarUsersLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvUsers.setOnFlingListener(null);
        binding.rvUsers.setLayoutManager(stackBarUsersLinearLayoutManager);
        binding.rvUsers.setAdapter(stackBarUsersAdapter);
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
        binding.stepsBarChart.getLegend().setWordWrapEnabled(true);
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
            for (int i = 0; i < multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().size(); i++) {
                week.add(multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().get(i).getWeeklyRange());
            }
        }

        binding.stepsBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getWeekWithDates(pattern, week, this.getResources())));

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        entries1.clear();

        entries1.addAll(getWeeklyDataBarCommunity(multipleGraphDataResponse, "happy insights"));

//        if (entries1.size() > 0 && mv != null){
//            mv.refreshContent(entries1.get(0), new Highlight(entries1.get(0).getX(), entries1.get(0).getY(), 0));
//        }


        BarDataSet set1 = null;

        MarkerDataClass markerDataClass = new MarkerDataClass();
        if (entries1.size() > 0) {
            markerDataClass = (MarkerDataClass) entries1.get(0).getData();
        }

        String[] userNames;

        userNames = markerDataClass.getUserName().toArray(new String[0]);

        if (userNames.length == 1)
            set1 = new BarDataSet(entries1, userNames[0]);
        else
            set1 = new BarDataSet(entries1, "");

        set1.setStackLabels(userNames);

        set1.setColors(getColors(userNames.length), this);
//        binding.stepsBarChart.getLegend().setEnabled(false);
//        binding.stepsBarChart.getDescription().setEnabled(false);

        set1.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData barData;

        barData = new BarData(dataSets);

        String label = "Steps";

        switch (activityType) {
            case "STEPS":
                label = "Steps";
                break;
            case "SLEEP":
                label = "Sleep\n(hrs)";
                break;
            case "WATER":
                label = "Water\n(gls)";
                break;
            case "ACTIVEHOURS":
                label = "ActiveHours\n(hrs)";
                break;
            case "CALORIES":
            case "CONSUMEDGRAPH":
            case "BURNEDGRAPH":
                label = "Calories\n(cal)";
                break;
            case "MEDITATION":
                label = "Meditation\n(mins)";
                break;
            case "WEIGHT":
                label = "Weight\n(kg)";
                break;
        }

        binding.tvUnit.setText(label);

        /*legendEntries.clear();
        legendEntries = Arrays.asList(binding.stepsBarChart.getLegend().getEntries());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();
        Gson gson = gsonBuilder.create();
        Log.d("legendEntries", gson.toJson(legendEntries));

        int newPos = 0;
        for (int i = 0; i < legendEntries.size(); i++) {
            if (newPos < userList.size() && userList.get(newPos) != null) {
                legendEntries.get(i).label = userList.get(newPos).split(" ")[0];
                newPos++;
            }
        }

        Log.d("legendEntries", gson.toJson(legendEntries));
        if (legendEntries.size() > 0)
            binding.stepsBarChart.getLegend().setCustom(legendEntries);*/

        /*legendEntries = Arrays.asList(binding.stepsBarChart.getLegend().getEntries());
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
        if (legendEntries.size() > 0) {
            legendEntries = legendEntries.subList(0, 1);
            binding.stepsBarChart.getLegend().setCustom(legendEntries);
        }*/

        binding.stepsBarChart.invalidate();
        binding.stepsBarChart.setFitBars(true);
        barData.setValueTextColor(Color.WHITE);
        barData.setValueTextSize(9f);
        barData.setBarWidth(0.25f);

        xAxis1.setAxisMinimum(-axisPadding);
        xAxis1.setAxisMaxValue(barData.getXMax() + axisPadding);

        mv = new MyMarkerViewForCommunity(this, R.layout.barchart_marker_view_layout);
        binding.stepsBarChart.setMarker(mv);

        return barData;
    }

    public int[] getColors(int size) {
        if (size == 0)
            size = 1;
        int[] colors = new int[]{R.color.blue_cyan, R.color.blue, R.color.orange, R.color.happy_dark_grey,
                R.color.btn_blue, R.color.dark_pink, R.color.light_blue, R.color.light_orange,
                R.color.purple_500, R.color.kotakDarkBrown};
        int[] colorsNew = new int[size];
        for (int i = 0; i < size; i++) {
            colorsNew[i] = colors[i];
        }
        return colorsNew;
    }

    private void setUserLabelList() {
        try {

        } catch (Exception e) {

        }
    }

    private BarData generateBarChartCommunityDiagnostics(String pattern, MultipleGraphDataResponse multipleGraphDataResponse) {
        BarData barData = new BarData();

        try{
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

            //binding.binding.diagnosticsBarChart.getAxisLeft().setAxisMaxValue(300);

            binding.diagnosticsBarChart.setExtraOffsets(0f, 0f, 0f, 16f);
            binding.diagnosticsBarChart.setDrawBorders(false);
            binding.diagnosticsBarChart.getDescription().setEnabled(false);
            binding.diagnosticsBarChart.getLegend().setWordWrapEnabled(true);
            List<String> week = new ArrayList<>();
            if (pattern.equals(WEEKLY)) {
                for (int i = 0; i < multipleGraphDataResponse.getData().getDiagnosticGraph().getRecordDate().size(); i++) {
                    week.add(multipleGraphDataResponse.getData().getDiagnosticGraph().getRecordDate().get(i).getWeeklyRange());
                }
            }
            binding.diagnosticsBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getWeekWithDates(pattern, week, this.getResources())));

            ArrayList<BarEntry> entries1 = new ArrayList<>();
            entries1.clear();

            entries1.addAll(getWeeklyDataBarCommunity(multipleGraphDataResponse, "Diagnostics"));

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


            int setColor3 = this.getResources().getColor(R.color.light_pink);

            set1 = new BarDataSet(entries1, "");
            MarkerDataClass markerDataClass = new MarkerDataClass();
            if (entries1.size() > 0) {
                markerDataClass = (MarkerDataClass) entries1.get(0).getData();
            }

            String[] userNames;

            userNames = markerDataClass.getUserName().toArray(new String[0]);

            if (userNames.length == 1)
                set1 = new BarDataSet(entries1, userNames[0]);
            else
                set1 = new BarDataSet(entries1, "");

            set1.setStackLabels(userNames);

            set1.setColors(getColors(userNames.length), this);
//        binding.stepsBarChart.getLegend().setEnabled(false);
//        binding.stepsBarChart.getDescription().setEnabled(false);

            binding.diagnosticsBarChart.setFitBars(true);

            set1.setDrawValues(false);
            LegendEntry l1, l2;

            barData = new BarData(set1);

            barData.setValueTextColor(Color.WHITE);
            barData.setValueTextSize(9f);
            barData.setBarWidth(0.25f);

            xAxis1.setAxisMinimum(-axisPadding);
            xAxis1.setAxisMaxValue(barData.getXMax() + axisPadding);

            mv = new MyMarkerViewForCommunity(this, R.layout.barchart_marker_view_layout);
            binding.diagnosticsBarChart.setMarker(mv);

        }catch (Exception e){
            e.printStackTrace();
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
        limitLine2.setTextColor(R.color.grayLight);
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
//                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddBookmarkResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("error6", ""+t.getMessage());
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_failed));
                Toast.makeText(context, context.getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openHappyMartDisclaimer(String cameFrom) {
        Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
        intent.putExtra("came_from", com.wyh.happyyousdk.utils.Constants.HappyMartPharmacy);
        intent.putExtra("toolbarname", "Pharmacy");
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchDiagnosticType();
    }

    private void gotoIntroPage(String cameFrom, String categoryName, boolean isAnalysis, boolean shownScreen) {
        Intent intent = new Intent(context, WellBeingDisclaimerActivity.class);
        intent.putExtra("came_from", cameFrom);
        intent.putExtra("CategoryName", categoryName);
        intent.putExtra("isAnalysis", isAnalysis);
        intent.putExtra("shownScreen", shownScreen);
        startActivity(intent);
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
                            //initialValues();
                            initialCommunityView();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.actometer_fetch_diagnostic_type_failed));
                    getAbsorbDashboard("Happy footprint");
                    Log.d("AuthToken","fetchDiagnosticType");

                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchDiagnosticTypeResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("error7", ""+t.getMessage());
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.actometer_fetch_diagnostic_type_failed));
                Log.d("AuthToken","fetchDiagnosticType onFailure");
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetureArrow() {
        if (currentGraphIndex == 0) {
            binding.ivNext.setEnabled(false);
        } else {
            binding.ivNext.setEnabled(true);
        }

        if (currentGraphIndex > 9) {
            binding.ivPrev.setEnabled(false);
        } else {
            binding.ivPrev.setEnabled(true);
        }
    }

    private void prevGraphButton() {
        binding.ivPrev.setOnClickListener(v -> {
            if (activityPeriodType.equalsIgnoreCase(QUARTERLY)) {
                currentGraphIndex = currentGraphIndex - 4;
            } else if (activityPeriodType.equalsIgnoreCase(DAILY)) {
                if (currentGraphIndex == -6) {
                    return;
                } else {
                    currentGraphIndex--;
                }
            } else {
                currentGraphIndex--;
            }
            fetureArrow();
            fetchCommunityGraphHappyInsightsStack(activityPeriodType, activityType, communityId, diagnosticPeriod, diagnosticType);
        });
    }

    private void nextGraphButton() {
        binding.ivNext.setOnClickListener(v -> {
            if (activityPeriodType.equalsIgnoreCase(QUARTERLY)) {
                currentGraphIndex = currentGraphIndex + 4;
            } else {
                currentGraphIndex++;
            }
            fetureArrow();
            fetchCommunityGraphHappyInsightsStack(activityPeriodType, activityType, communityId, diagnosticPeriod, diagnosticType);
        });
    }

    private void setDateRange() {
        int lastIndex = multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().size() - 1;
        binding.llMoreGraphData.setVisibility(View.VISIBLE);


        String date = formatDateFromString("yyyy-MM-dd", "MMM dd yyyy", multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().get(0).getRecordDate()).split(" ")[2];
        if (activityPeriodType.equalsIgnoreCase(MONTHLY) || activityPeriodType.equalsIgnoreCase(QUARTERLY)) {
            binding.tvDateRange.setText("Jan 01 " + date + "- Dec 31 " + date);
        } else if (activityPeriodType.equalsIgnoreCase(DAILY)) {
            try {
                String startDate = formatDateFromString("yyyy-MM-dd", "dd MMM", multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().get(0).getRecordDate());
                String endDate = formatDateFromString("yyyy-MM-dd", "dd MMM", multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().get(6).getRecordDate());

                binding.tvDateRange.setText(startDate + " - " + endDate);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            binding.tvDateRange.setText(formatDateFromString("yyyy-MM-dd", "MMM yyyy",
                    multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().get(0).getRecordDate()));
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
            fetchCommunityGraphDiagnosticsStack(activityPeriodType, activityType, communityId, diagnosticPeriodType, diagnosticType);
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
            fetchCommunityGraphDiagnosticsStack(activityPeriodType, activityType, communityId, diagnosticPeriodType, diagnosticType);
        });
    }

    private void setDateRangeDiastolic() {
        try {
            List<MultipleGraphDataResponse.Data.DiagnosticGraph.RecordDate> data = multipleGraphDataDiagnosticResponse.getData().getDiagnosticGraph().getRecordDate();
            int lastIndex = data.size() - 1;
            binding.llMoreGraphDataDiastolic.setVisibility(View.VISIBLE);
            /*if(diagnosticPeriodType.equalsIgnoreCase(DAILY)){
                binding.llMoreGraphDataDiastolic.setVisibility(View.GONE);
            }else{

            }*/


            String date = formatDateFromString("yyyy-MM-dd", "MMM dd yyyy", multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().get(0).getRecordDate()).split(" ")[2];
            if (activityPeriodType.equalsIgnoreCase(MONTHLY) || activityPeriodType.equalsIgnoreCase(QUARTERLY)) {
                binding.tvDateRangeDiastolic.setText("Jan 01 " + date + "- Dec 31 " + date);
            } else if (activityPeriodType.equalsIgnoreCase(DAILY)) {
                try {
                    String startDate = formatDateFromString("yyyy-MM-dd", "dd MMM", multipleGraphDataDiagnosticResponse.getData().getDiagnosticGraph().getRecordDate().get(0).getGraphdate());
                    String endDate = formatDateFromString("yyyy-MM-dd", "dd MMM", multipleGraphDataDiagnosticResponse.getData().getDiagnosticGraph().getRecordDate().get(6).getGraphdate());

                    binding.tvDateRangeDiastolic.setText(startDate + " - " + endDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                binding.tvDateRangeDiastolic.setText(formatDateFromString("yyyy-MM-dd", "MMM yyyy", multipleGraphDataResponse.getData().getHappyinsights().getDataPoints().get(0).getRecordDate()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
