package com.wyh.happyyousdk.rewards;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.ACTIVE;
import static com.wyh.happyyousdk.utils.Constants.ALL;
import static com.wyh.happyyousdk.utils.Constants.BURNED;
import static com.wyh.happyyousdk.utils.Constants.EARNED;
import static com.wyh.happyyousdk.utils.Constants.EXPIRED;
import static com.wyh.happyyousdk.utils.Constants.HistoryEarn;
import static com.wyh.happyyousdk.utils.Constants.REDEEMED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityRewardsHistoryBinding;
import com.wyh.happyyousdk.model.request.rewards.GetRewardsDashboardRequest;
import com.wyh.happyyousdk.model.response.rewards.RewardsHistoryActivityRewardsData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.adapter.ActivitiesHistoryAdapter;
import com.wyh.happyyousdk.model.request.rewards.RewardsHistoryRequest;
import com.wyh.happyyousdk.model.response.rewards.CommonRewardHistory;
import com.wyh.happyyousdk.model.response.rewards.RewardsHistoryActivityData;
import com.wyh.happyyousdk.model.response.rewards.RewardsHistoryEnG;
import com.wyh.happyyousdk.model.response.rewards.RewardsHistoryResponse;
import com.wyh.happyyousdk.model.response.rewards.LevelDashboardResponse;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardsHistoryActivity extends AppCompatActivity {

    ActivityRewardsHistoryBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    ActivitiesHistoryAdapter activitiesHistoryAdapter;
    List<CommonRewardHistory> activityDataList, enGList, dataList, activityRewardsData;
    String historyType = Constants.HistoryAll, statusType = Constants.HistoryAll, statusTypeActivity = Constants.HistoryActive;
    int totalPoint;
    String comingFrom;
    int totalStamp, totalEranPoints = 0, totalBurnedPoints = 0, totalEranStamp = 0, totalBurnedStamp = 0;
    List<String> spinnerHistoryFilterType = new ArrayList<>(Arrays.asList(ALL, EARNED, BURNED, ACTIVE, REDEEMED, EXPIRED));
    ArrayAdapter<String> spinnerHistoryFilterTypeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rewards_history);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeToolbar.tvBack.setText("History");
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        totalPoint = SharedPref.getPointsHistory();
        totalStamp = SharedPref.getStampsHistory();


        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "WinningsHistory");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        getRewardsHistory();

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


        binding.btnAll.setOnClickListener(v -> {
            btnAll();
        });
        binding.btnLevel.setOnClickListener(v -> {
            btnActivities();
        });
        binding.btnEng.setOnClickListener(v -> {
            btnEng();
        });
        binding.btnActivity.setOnClickListener(v -> {
            btnActivityRewards();
        });
    }

    private void setActivityRewardsSpinner() {
        String[] spinnerHistoryFilterType = {ALL, ACTIVE, REDEEMED, EXPIRED};
        ArrayAdapter<String> spinnerHistoryFilterTypeAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, spinnerHistoryFilterType);
        spinnerHistoryFilterTypeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerActivityRewardsHistoryFilterType.setAdapter(spinnerHistoryFilterTypeAdapter);

        binding.spinnerActivityRewardsHistoryFilterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        statusTypeActivity = Constants.HistoryAll;
                        break;
                    case 1:
                        statusTypeActivity = Constants.HistoryActive;
                        break;
                    case 2:
                        statusTypeActivity = Constants.HistoryBurnOrRedeemed;
                        break;
                    case 3:
                        statusTypeActivity = Constants.HistoryExpired;
                        break;
                }
                setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showMainSpinner(boolean isAll) {
        if (isAll) {
            if (!spinnerHistoryFilterType.contains(ACTIVE))
                spinnerHistoryFilterType.add(ACTIVE);
            if (!spinnerHistoryFilterType.contains(REDEEMED))
                spinnerHistoryFilterType.add(REDEEMED);
            if (!spinnerHistoryFilterType.contains(EXPIRED))
                spinnerHistoryFilterType.add(EXPIRED);
        } else {
            spinnerHistoryFilterType.remove(ACTIVE);
            spinnerHistoryFilterType.remove(REDEEMED);
            spinnerHistoryFilterType.remove(EXPIRED);
        }
        binding.spinnerActivityRewardsHistoryFilterType.setVisibility(View.GONE);
        binding.spinnerHistoryFilterType.setVisibility(View.VISIBLE);
    }

    private void showActivityRewardsSpinner() {
        spinnerHistoryFilterType.remove(ACTIVE);
        spinnerHistoryFilterType.remove(REDEEMED);
        spinnerHistoryFilterType.remove(EXPIRED);
        binding.spinnerActivityRewardsHistoryFilterType.setVisibility(View.VISIBLE);
        binding.spinnerHistoryFilterType.setVisibility(View.GONE);
    }

    private void setSpinner() {

        String[] spinnerHistoryType = {"All", "Level", "Earn & Grab"};
        ArrayAdapter<String> spinnerHistoryTypeAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, spinnerHistoryType);
        spinnerHistoryTypeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerHistoryType.setAdapter(spinnerHistoryTypeAdapter);


        spinnerHistoryFilterTypeAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, spinnerHistoryFilterType);
        spinnerHistoryFilterTypeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerHistoryFilterType.setAdapter(spinnerHistoryFilterTypeAdapter);

        binding.spinnerHistoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        btnAll();
                        break;
                    case 1:
                        btnActivities();
                        break;
                    case 2:
                        btnEng();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerHistoryFilterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        statusTypeActivity = Constants.HistoryAll;
                        btnFilterAll();
                        break;
                    case 1:
                        btnFilterEarn();
                        break;
                    case 2:
                        btnFilterBurn();
                        break;
                    case 3:
                        statusTypeActivity = Constants.HistoryActive;
                        statusType = Constants.HistoryActive;
                        setData();
                        break;
                    case 4:
                        statusTypeActivity = Constants.HistoryBurnOrRedeemed;
                        statusType = Constants.HistoryBurnOrRedeemed;
                        setData();
                        break;
                    case 5:
                        statusTypeActivity = Constants.HistoryExpired;
                        statusType = Constants.HistoryExpired;
                        setData();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getRewardsHistory() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        RewardsHistoryRequest request = new RewardsHistoryRequest("");
        Call<RewardsHistoryResponse> call = apiInterfaceWyh.getRewardsHistory(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<RewardsHistoryResponse>() {
            @Override
            public void onResponse(Call<RewardsHistoryResponse> call, Response<RewardsHistoryResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                getRewardsDashboardData();
                dataList = new ArrayList<>();
                activityDataList = new ArrayList<>();
                enGList = new ArrayList<>();
                activityRewardsData = new ArrayList<>();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_history_data_success));

                    if (response.body().getData().getActivities() != null && response.body().getData().getActivities().size() > 0) {
                        for (RewardsHistoryActivityData element : response.body().getData().getActivities()) {
                            CommonRewardHistory temp = new CommonRewardHistory();
                            temp.setEventName(element.getEventName());
                            temp.setAmount(element.getAmount() + " Points");
                            temp.setTransStatus(element.getTransStatus());
                            temp.setTransDate(element.getTransDate());
                            if (Objects.equals(element.getTransStatus().toLowerCase(), HistoryEarn)) {
                                totalEranPoints += element.getAmount();
                            } else {
                                totalBurnedPoints += element.getAmount();
                            }
                            activityDataList.add(temp);
                        }
                        dataList.addAll(activityDataList);
                    }
                    if (response.body().getData().getEnG() != null && response.body().getData().getEnG().size() > 0) {
                        for (RewardsHistoryEnG element : response.body().getData().getEnG()) {
                            CommonRewardHistory temp = new CommonRewardHistory();
                            temp.setEventName(element.getEventName());
                            temp.setAmount(element.getTokens() + " Stamps");
                            temp.setTransStatus(element.getTransStatus());
                            temp.setTransDate(element.getTransDate());
                            if (Objects.equals(element.getTransStatus().toLowerCase(), HistoryEarn)) {
                                totalEranStamp += element.getTokens();
                            } else {
                                totalBurnedStamp += element.getTokens();
                            }
                            enGList.add(temp);
                        }
                        dataList.addAll(enGList);
                    }
                    if (response.body().getData().getActivityRewards() != null && !response.body().getData().getActivityRewards().isEmpty()) {
                        for (RewardsHistoryActivityRewardsData element : response.body().getData().getActivityRewards()) {
                            CommonRewardHistory temp = new CommonRewardHistory();
                            temp.setEventName(element.getActivityName());
                            temp.setAmount(element.getRewardType());
                            temp.setTransStatus(element.getStatus());
                            temp.setTransDate(element.getTransactionDate());
                            temp.setCampaignID("" + element.getActivityCampaignId());
                            temp.setStartDate(element.getActivityStartDate());
                            temp.setEndDate(element.getActivityEndDate());
                            temp.setHeader(element.getActivityHeader());
                            temp.setSelfStatus(element.getSelfStatus());
                            activityRewardsData.add(temp);
                        }
                        dataList.addAll(activityRewardsData);
                    }


                    binding.earnAndBurnedPoints.setText("Available Points: " + totalPoint);
                    binding.earnAndBurnedStamps.setText("Available Stamps: " + totalStamp);


                    setSpinner();
                    setActivityRewardsSpinner();
                    if (dataList.size() > 0) {
//                        dataList = sortByDate(dataList);
                        /*Collections.sort(dataList, new Comparator<MyDateTimeCompare>() {
                            public int compare(MyDateTimeCompare o1, MyDateTimeCompare o2) {
                                return o1.getDateTime().compareTo(o2.getDateTime());
                            }
                        });*/
                        Collections.sort(dataList, new sortItems());
                        binding.llNoRecordFound.setVisibility(View.GONE);
                        binding.llDataAvailable.setVisibility(View.VISIBLE);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        activitiesHistoryAdapter = new ActivitiesHistoryAdapter(context, dataList);
                        binding.rvAll.setLayoutManager(linearLayoutManager);
                        binding.rvAll.setAdapter(activitiesHistoryAdapter);
                    } else {
                        binding.llNoRecordFound.setVisibility(View.VISIBLE);
                        binding.llDataAvailable.setVisibility(View.GONE);
                    }


                    if (getIntent().getStringExtra("comingFrom") != null) {
                        comingFrom = getIntent().getStringExtra("comingFrom");
                        if (comingFrom.equalsIgnoreCase("stamps")) {
                            btnEng();
                        }
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_history_data_failed));
                }
            }

            @Override
            public void onFailure(Call<RewardsHistoryResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_history_data_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<CommonRewardHistory> sortByDate(List<CommonRewardHistory> commonRewardHistoryList) {
        Collections.sort(commonRewardHistoryList, new Comparator<CommonRewardHistory>() {
            DateFormat f = new SimpleDateFormat("dd/mm/yyyy");

            @Override
            public int compare(CommonRewardHistory lhs, CommonRewardHistory rhs) {
                try {
                    return f.parse(lhs.getTransDate()).compareTo(f.parse(rhs.getTransDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        return commonRewardHistoryList;
    }

    private void setData() {
        List<CommonRewardHistory> temp = new ArrayList<>();
        if (Objects.equals(historyType, Constants.HistoryAll) && Objects.equals(statusType, Constants.HistoryAll)) {
            if (dataList.size() > 0) {
                temp.addAll(dataList);
            }
        } else if (Objects.equals(historyType, Constants.HistoryActivities) && Objects.equals(statusType, Constants.HistoryAll)) {
            if (activityDataList.size() > 0) {
                temp.addAll(activityDataList);

            }
        } else if (Objects.equals(historyType, Constants.HistoryEng) && Objects.equals(statusType, Constants.HistoryAll)) {
            if (enGList.size() > 0) {
                temp.addAll(enGList);
            }
        } else if (Objects.equals(historyType, Constants.HistoryAll) && Objects.equals(statusType, Constants.HistoryFilterEarn)) {
            if (dataList.size() > 0) {
                for (CommonRewardHistory element : dataList) {
                    if (element.getTransStatus().toLowerCase().equals(HistoryEarn)) {
                        temp.add(element);
                    }
                }
            }
        } else if (Objects.equals(historyType, Constants.HistoryAll) && Objects.equals(statusType, Constants.HistoryFilterBurn)) {
            if (dataList.size() > 0) {
                for (CommonRewardHistory element : dataList) {
                    if (element.getTransStatus().toLowerCase().equals(Constants.HistoryBurnOrRedeemed)) {
                        temp.add(element);
                    }
                }
            }
        } else if (Objects.equals(historyType, Constants.HistoryActivities) && Objects.equals(statusType, Constants.HistoryFilterEarn)) {
            if (activityDataList.size() > 0) {
                for (CommonRewardHistory element : activityDataList) {
                    if (element.getTransStatus().toLowerCase().equals(HistoryEarn)) {
                        temp.add(element);
                    }
                }
            }
        } else if (Objects.equals(historyType, Constants.HistoryActivities) && Objects.equals(statusType, Constants.HistoryFilterBurn)) {
            if (activityDataList.size() > 0) {
                for (CommonRewardHistory element : activityDataList) {
                    if (element.getTransStatus().toLowerCase().equals(Constants.HistoryBurnOrRedeemed)) {
                        temp.add(element);
                    }
                }
            }
        } else if (Objects.equals(historyType, Constants.HistoryEng) && Objects.equals(statusType, Constants.HistoryFilterEarn)) {
            if (enGList.size() > 0) {
                for (CommonRewardHistory element : enGList) {
                    if (element.getTransStatus().toLowerCase().equals(HistoryEarn)) {
                        temp.add(element);
                    }
                }
            }
        } else if (Objects.equals(historyType, Constants.HistoryEng) && Objects.equals(statusType, Constants.HistoryFilterBurn)) {
            if (enGList.size() > 0) {
                for (CommonRewardHistory element : enGList) {
                    if (element.getTransStatus().toLowerCase().equals(Constants.HistoryBurnOrRedeemed)) {
                        temp.add(element);
                    }
                }
            }
        } else if (Objects.equals(historyType, Constants.HistoryActivityRewards) && Objects.equals(statusTypeActivity, Constants.HistoryAll)) {
            if (!activityRewardsData.isEmpty()) {
                temp.addAll(activityRewardsData);
            }
        } else if (Objects.equals(statusTypeActivity, Constants.HistoryActive) || Objects.equals(statusType, Constants.HistoryActive)) {
            if (!activityRewardsData.isEmpty()) {
                for (CommonRewardHistory element : activityRewardsData) {
                    if (element.getTransStatus().toLowerCase().equals(Constants.HistoryActive)) {
                        temp.add(element);
                    }
                }
            }
        } else if (Objects.equals(statusTypeActivity, Constants.HistoryBurnOrRedeemed) || Objects.equals(statusType, Constants.HistoryBurnOrRedeemed)) {
            if (!activityRewardsData.isEmpty()) {
                for (CommonRewardHistory element : activityRewardsData) {
                    if (element.getTransStatus().toLowerCase().equals(Constants.HistoryBurnOrRedeemed)) {
                        temp.add(element);
                    }
                }
            }
        } else if (Objects.equals(statusTypeActivity, Constants.HistoryExpired) || Objects.equals(statusType, Constants.HistoryExpired)) {
            if (!activityRewardsData.isEmpty()) {
                for (CommonRewardHistory element : activityRewardsData) {
                    if (element.getTransStatus().toLowerCase().equals(Constants.HistoryExpired)) {
                        temp.add(element);
                    }
                }
            }
        }

        if (temp.size() > 0) {
            Collections.sort(temp, new sortItems());
            binding.llNoRecordFound.setVisibility(View.GONE);
            binding.llDataAvailable.setVisibility(View.VISIBLE);
            activitiesHistoryAdapter.updateList(temp);
        } else {
            binding.llNoRecordFound.setVisibility(View.VISIBLE);
            binding.llDataAvailable.setVisibility(View.GONE);
        }


    }

    //set Button
    private void btnAll() {
        historyType = Constants.HistoryAll;
        binding.btnAll.setBackground(getDrawable(R.drawable.ic_blue_button_bg));
        binding.btnLevel.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnEng.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnActivity.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.tvPointsHeading.setText("Points");
        binding.earnAndBurnedPoints.setVisibility(View.VISIBLE);
        binding.earnAndBurnedStamps.setVisibility(View.VISIBLE);
        showMainSpinner(true);
        setData();
    }

    private void btnActivities() {
        historyType = Constants.HistoryActivities;
        binding.btnAll.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnLevel.setBackground(getDrawable(R.drawable.ic_blue_button_bg));
        binding.btnEng.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnActivity.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.tvPointsHeading.setText("Points");
        binding.earnAndBurnedPoints.setVisibility(View.VISIBLE);
        binding.earnAndBurnedStamps.setVisibility(View.INVISIBLE);
        binding.imgInfoSpace.setVisibility(View.GONE);
        showMainSpinner(false);
        setData();
    }

    private void btnEng() {
        historyType = Constants.HistoryEng;
        binding.btnAll.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnLevel.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnEng.setBackground(getDrawable(R.drawable.ic_blue_button_bg));
        binding.btnActivity.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.tvPointsHeading.setText("Points");
        binding.earnAndBurnedPoints.setVisibility(View.INVISIBLE);
        binding.earnAndBurnedStamps.setVisibility(View.VISIBLE);
        binding.imgInfoSpace.setVisibility(View.GONE);
        showMainSpinner(false);
        setData();
    }

    private void btnActivityRewards() {
        historyType = Constants.HistoryActivityRewards;
        binding.btnAll.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnLevel.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnEng.setBackground(getDrawable(R.drawable.wyh_btn_grey_8dp));
        binding.btnActivity.setBackground(getDrawable(R.drawable.ic_blue_button_bg));
        binding.tvPointsHeading.setText("Rewards");
        binding.earnAndBurnedPoints.setVisibility(View.INVISIBLE);
        binding.earnAndBurnedStamps.setVisibility(View.INVISIBLE);
        binding.imgInfoSpace.setVisibility(View.VISIBLE);
        showActivityRewardsSpinner();
        setData();
    }

    private void btnFilterAll() {
        statusType = Constants.HistoryAll;
        binding.earnAndBurnedPoints.setText("Available Points: " + totalPoint);
        binding.earnAndBurnedStamps.setText("Available Stamps: " + totalStamp);
        setData();
    }

    private void btnFilterEarn() {
        statusType = Constants.HistoryFilterEarn;
        binding.earnAndBurnedPoints.setText("Earned Points: " + totalEranPoints);
        binding.earnAndBurnedStamps.setText("Earned Stamp: " + totalEranStamp);
        setData();
    }

    private void btnFilterBurn() {
        statusType = Constants.HistoryFilterBurn;
        binding.earnAndBurnedPoints.setText("Burned Points: " + totalBurnedPoints);
        binding.earnAndBurnedStamps.setText("Burned Stamp: " + totalBurnedStamp);
        setData();
    }

    /*private void checkIsPoints(List<CommonRewardHistory> data, String key) {
        for (CommonRewardHistory element : data) {
            if (element.getTransStatus().toLowerCase().equals(key)) {
                totalPoint += 1;
            }
        }
    }

    private void checkIsStamp(List<CommonRewardHistory> data, String key) {
        for (CommonRewardHistory element : data) {
            if (element.getTransStatus().toLowerCase().equals(key)) {
                totalStamp += 1;
            }
        }
    }*/

    public void getRewardsDashboardData() {
        GetRewardsDashboardRequest request = new GetRewardsDashboardRequest("");
        Call<LevelDashboardResponse> call = apiInterfaceWyh.getRewardsDashboardData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<LevelDashboardResponse>() {
            @Override
            public void onResponse(Call<LevelDashboardResponse> call, Response<LevelDashboardResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.get_rewards_dash_board_data_success));
                    Double totalEarnAmount = response.body().getData().getEquivalentAmount();
                    totalPoint = (int) Math.round(totalEarnAmount / 0.2);
                    binding.earnAndBurnedPoints.setText("Available Points: " + totalPoint);
                    binding.earnAndBurnedStamps.setText("Available Stamps: " + totalStamp);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.get_rewards_dash_board_data_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LevelDashboardResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.get_rewards_dash_board_data_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class sortItems implements Comparator<CommonRewardHistory> {

    // Method of this class
    @Override
    public int compare(CommonRewardHistory a, CommonRewardHistory b) {

        // Returning the value after comparing the objects
        // this will sort the data in Ascending order

        return b.getTransDate().compareTo(a.getTransDate());
    }
}

class sortItemsBYTime implements Comparator<CommonRewardHistory> {

    // Method of this class
    @Override
    public int compare(CommonRewardHistory a, CommonRewardHistory b) {

        // Returning the value after comparing the objects
        // this will sort the data in Ascending order

        String date1 = a.getTransDate().split("T")[0] + a.getTransDate().split("T")[1];
        String date2 = b.getTransDate().split("T")[0] + b.getTransDate().split("T")[1];

        return date2.compareTo(date1);
    }
}