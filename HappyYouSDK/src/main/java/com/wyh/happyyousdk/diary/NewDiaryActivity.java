package com.wyh.happyyousdk.diary;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityNewDiaryBinding;
import com.wyh.happyyousdk.diary.Fragment.MyDiaryFragment;
import com.wyh.happyyousdk.diary.Fragment.WinningFragment;
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.SampleFragmentPagerAdapter;
import com.wyh.happyyousdk.utils.APILogConstant;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewDiaryActivity extends AppCompatActivity {

    MyDiaryFragment diaryFragment = new MyDiaryFragment();
    WinningFragment winningFragment = new WinningFragment();
    int currentIndex = 0;

    ActivityNewDiaryBinding binding;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    List<String> levelList, engList, topUpList;
    List<DiaryEventListResponse.Datum> levelListData, engListData, topUpListData;
    List<DiaryEventListResponse.Datum> getEventListData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_diary);
        context = this;
        setContentView(binding.getRoot());
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(this)).create(ApiInterfaceWyh.class);

        binding.diarySlidingTabs.setupWithViewPager(binding.diaryViewpager);

        APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md(), context);

        currentIndex = getIntent().getIntExtra("actvities",0);
        setupViewPager(binding.diaryViewpager);

        getEventList();
        setToolBar();

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.includeToolbar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "MyDiaryDashboard");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.diarySlidingTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    diaryFragment.getAllDiary();
                } else {
                    winningFragment.getAllDiary();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setToolBar() {
        binding.includeToolbar.tvBack.setText("My Diary");
        binding.includeToolbar.ivAdd.setVisibility(View.GONE);

        binding.includeToolbar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.includeToolbar.ivMenu.setVisibility(View.VISIBLE);

        binding.includeToolbar.ivMenu.setOnClickListener(view -> {
            showPopup(binding.includeToolbar.ivMenu);
        });

    }

    public void getEventList() {
        Call<DiaryEventListResponse> call = apiInterfaceWyh.getDiaryEventList(SharedPref.getAuthToken());
        call.enqueue(new Callback<DiaryEventListResponse>() {
            @Override
            public void onResponse(Call<DiaryEventListResponse> call, Response<DiaryEventListResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(NewDiaryActivity.this, this.getClass().getName(), getString(R.string.fetch_pending_diary_events_success));

                    List<DiaryEventListResponse.Datum> data = response.body().getData();
                    getEventListData = response.body().getData();
                    setData(data);

                } else {
                    Analytics.logEvent(NewDiaryActivity.this, NewDiaryActivity.this.getClass().getName(), getString(R.string.fetch_pending_diary_events_failed));
                    Toast.makeText(NewDiaryActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DiaryEventListResponse> call, Throwable t) {
                Analytics.logEvent(NewDiaryActivity.this, NewDiaryActivity.this.getClass().getName(), getString(R.string.fetch_pending_diary_events_failed));
                Toast.makeText(NewDiaryActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.diary_add_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.menu_add_user_journal) {
                Intent intent = new Intent(this, AddDiaryActivity.class);
                intent.putExtra("came_from", "add");
                startActivityForResult(intent, 1);
            } else if (itemId == R.id.menu_user_journal_share_and_earn) {
                Intent intent2 = new Intent(this, AddDiaryActivity.class);
                intent2.putExtra("came_from", "share");
                intent2.putExtra("levelData", new Gson().toJson(levelListData));
                intent2.putExtra("leveldataStr", new Gson().toJson(levelList));
                intent2.putExtra("engData", new Gson().toJson(engListData));
                intent2.putExtra("engdataStr", new Gson().toJson(engList));
                intent2.putExtra("topUpData", new Gson().toJson(topUpListData));
                intent2.putExtra("topUpdataStr", new Gson().toJson(topUpList));

                startActivityForResult(intent2, 2);
            }
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            if (data.getBooleanExtra("isAdded", false)) {
                if (data.getStringExtra("came_from").equalsIgnoreCase("add")) {
                    if (binding.diarySlidingTabs.getSelectedTabPosition() == 1)
                        binding.diarySlidingTabs.getTabAt(0).select();
                    else
                        diaryFragment.getAllDiary();
                } else {
                    if (binding.diarySlidingTabs.getSelectedTabPosition() == 0)
                        binding.diarySlidingTabs.getTabAt(1).select();
                    else
                        winningFragment.getAllDiary();
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(diaryFragment, "My Journal");
        adapter.addFragment(winningFragment, "Activities");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentIndex);
    }

    private void setData(List<DiaryEventListResponse.Datum> data) {
        levelList = new ArrayList<>();
        engList = new ArrayList<>();
        topUpList = new ArrayList<>();
        levelListData = new ArrayList<>();
        engListData = new ArrayList<>();
        topUpListData = new ArrayList<>();
        levelList.add("Select your activity");
        engList.add("Select your activity");
        topUpList.add("Select your activity");

        if (data != null && data.size() > 0) {
            for (DiaryEventListResponse.Datum item : data) {
                if (item.getEventCategory().toLowerCase().equals("topup")) {
                    topUpList.add(item.getEventName());
                    topUpListData.add(item);
                } else if (item.getEventCategory().toLowerCase().equals("eng")) {
                    engList.add(item.getEventName());
                    engListData.add(item);
                } else {
                    levelList.add(item.getEventName());
                    levelListData.add(item);
                }
            }
        }

    }
}