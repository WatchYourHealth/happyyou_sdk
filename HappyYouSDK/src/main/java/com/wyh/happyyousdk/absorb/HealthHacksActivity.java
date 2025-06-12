package com.wyh.happyyousdk.absorb;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.SearchKey;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.absorb.adapter.HealthHacksHealthTvAdapter;
import com.wyh.happyyousdk.absorb.adapter.HealthHacksQuickReadsAdapter;
import com.wyh.happyyousdk.absorb.adapter.HealthHacksTagsAdapter;
import com.wyh.happyyousdk.absorb.adapter.HealthHacksWebinarAdapter;
import com.wyh.happyyousdk.absorb.adapter.SearchListAdapter;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.model.request.SearchRequest;
import com.wyh.happyyousdk.model.request.absorb.AddBookmarkRequest;
import com.wyh.happyyousdk.model.request.absorb.GetDashboardDataRequest;
import com.wyh.happyyousdk.model.request.absorb.VideoBookmarkRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.absorb.AddBookmarkResponse;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;

import com.wyh.happyyousdk.databinding.ActivityAbsorbBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.musicLib.MoreMusicListActivity;
import com.wyh.happyyousdk.musicLib.adapter.MusicLibAdapter;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthHacksActivity extends AppCompatActivity implements ScratchListener, SearchKeyClick, TextWatcher, rewardDialogCloseListener {
    ActivityAbsorbBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    String tagName, cameFrom, newSearchKey;

    int positionReads = 0, positionHealthTv = 0, positionHealthTvBookmark = 0, positionWebinar = 0, positionTags = 0, positionMusicLib = 0;
    List<GetDashboardDataResponse.Data.TagName> tagNameList;
    boolean fetchTagsData = true, isPositiveBtn = false;
    List<GetDashboardDataResponse.Data.HealthTv> healthTvList, healthTvBookmarkList;
    AlertDialog alertDialogBonusRewards;
    List<GetDashboardDataResponse.Data.AudioFiles> audioList;
    List<GetDashboardDataResponse.Data.QucikRead> quickReadSearchList;
    List<GetDashboardDataResponse.Data.HealthTv> healthTvSearchList;

    List<GetDashboardDataResponse.Data.AudioFiles> audioLibSearchList;

    List<GetDashboardDataResponse.Data.HealthTv> healthTvBookmark;

    List<GetDashboardDataResponse.Data.QucikRead> quickReadList;

    List<GetDashboardDataResponse.Data.QucikRead> allBlogsList;

    String selectedSearchText = "";

    List<GetDashboardDataResponse.Data.HealthTv> allVideosList;
    ArrayList<String> searchKeyFilter = new ArrayList<>();
    ArrayList<String> titleKeyFilter = new ArrayList<>();

    ArrayList<String> videoSearchKeyFilter = new ArrayList<>();
    ArrayList<String> videoTitleKeyFilter = new ArrayList<>();

    ArrayList<String> quickReadMainFilterList = new ArrayList<>();

    ArrayList<GetDashboardDataResponse.Data.QucikRead> searchAllBlogs = new ArrayList<>();
    ArrayList<GetDashboardDataResponse.Data.HealthTv> searchAllVideos = new ArrayList<>();

    SearchListAdapter searchListAdapter;
    HealthHacksTagsAdapter absorbQuickReadsAdapter;

    Boolean emptySearch = true;

    AssignRewardsResponse.SpinRewardsData spinRewardsData;
    QuizathonRewardData quizathonRewardData;


    LinearLayoutManager quickReadLinearLayoutManager, healthTvLinearLayoutManager, musicLibLinearLayoutManager, healthTvBookmarkLayoutManager;

    int indicatorSize;
    String searchKey;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_absorb);

        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        apiInterface = RetrofitHandler.apiInterface();
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        cameFrom = getIntent().getStringExtra("cameFrom");
        searchKey = getIntent().getStringExtra(SearchKey);

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "HealthHacks");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Health Hacks");
        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));


        if (searchKey != null && !searchKey.isEmpty() && searchKey.length() >= 3) {
            binding.healthHacksSearchLayout.show();
            binding.healthHacksSearchEt.setText(searchKey);
            getAbsorbDashboard("", searchKey);
        } else {
            getAbsorbDashboard("", "");
        }


        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.rlMusicLib.setOnClickListener(view -> {
            Intent i = new Intent(this, MoreMusicListActivity.class);
            i.putExtra("data", new Gson().toJson(audioList));
            startActivity(i);
        });

        binding.rlQuickRead.setOnClickListener(view -> {
            if (tagNameList != null && tagNameList.size() > 0) {
                Intent i = new Intent(this, QuickReadDashboard.class);
                i.putExtra("taglist", (Serializable) tagNameList);
                i.putExtra("tagName", tagName);
                i.putExtra("selectedItem", absorbQuickReadsAdapter.selectedItem);
                startActivity(i);
            }
        });

        binding.rlHealthTv.setOnClickListener(view -> {
            if (healthTvList != null && healthTvList.size() > 0) {
                Intent i = new Intent(this, HealthTvListingActivity.class);
                i.putExtra("taglist", (Serializable) tagNameList);
                i.putExtra("tagName", tagName);
                i.putExtra("selectedItem", absorbQuickReadsAdapter.selectedItem);
                startActivity(i);
            }
        });

        binding.rlHealthTvBookmark.setOnClickListener(view -> {
            if (healthTvBookmarkList.size() > 0) {
                Intent i = new Intent(this, HealthTVDashboard.class);
                i.putExtra("healthTV", (Serializable) healthTvBookmarkList);
                i.putExtra("titleName", "Bookmark Health TV Video");
                startActivity(i);
            }
        });

        binding.healthHackCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.healthHacksSearchEt.setText("");
            }
        });

        binding.healthHacksSearchEt.addTextChangedListener(this);


        binding.healthHacksSearchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.healthHacksSearchLayout.getVisibility() == View.VISIBLE) {
                    getAbsorbDashboard(tagName, "");
                    binding.healthHacksSearchLayout.hide();
                } else {
                    binding.healthHacksSearchLayout.show();
                }
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Log.d("AuthToken","onTextChanged "+ charSequence.chars().count());
        if (charSequence.chars().count() == 0) {
            binding.healthHackCancel.setVisibility(View.GONE);
            binding.searchViewCard.setVisibility(View.GONE);
            binding.laBear.setVisibility(View.VISIBLE);
            selectedSearchText = "";
            if (emptySearch) {
                emptySearch = false;
                getAbsorbDashboard(tagName, "");
            }
        } else if (selectedSearchText.equalsIgnoreCase(charSequence.toString())) {
            binding.searchViewCard.setVisibility(View.GONE);
            binding.laBear.setVisibility(View.VISIBLE);
        } else {
            if (charSequence.chars().count() >= 3) {
                emptySearch = true;
                binding.healthHackCancel.setVisibility(View.VISIBLE);
                binding.searchViewCard.setVisibility(View.VISIBLE);
                binding.laBear.setVisibility(View.GONE);
                searchItems(charSequence.toString());
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    public void searchItems(String searchKey) {
        try {
            searchKeyFilter.clear();
            titleKeyFilter.clear();
            quickReadMainFilterList.clear();
            searchAllBlogs.clear();
//124
            //226

            for (int i = 0; i < allBlogsList.size(); i++) {

                if (allBlogsList.get(i).getSearchkey() != null && !allBlogsList.get(i).getSearchkey().equals("")) {
                    if (allBlogsList.get(i).getSearchkey().toLowerCase().contains(searchKey)) {
                        if (allBlogsList.get(i).getSearchkey().contains(",")) {
                            searchKeyFilter.addAll(Arrays.asList(allBlogsList.get(i).getSearchkey().split(",")));
                        }
                        searchKeyFilter.add(allBlogsList.get(i).getSearchkey());
                        searchAllBlogs.add(allBlogsList.get(i));
                    }
                    if (allBlogsList.get(i).getArticleName().toLowerCase().contains(searchKey)) {
                        titleKeyFilter.add(allBlogsList.get(i).getArticleName());
                        searchAllBlogs.add(allBlogsList.get(i));
                    }
                }

            }
            for (int i = 0; i < allVideosList.size(); i++) {
                Log.e("tags videos", String.valueOf(i));
                if (allVideosList.get(i).getSearchKey() != null && !allVideosList.get(i).getSearchKey().equals("")) {
                    if (allVideosList.get(i).getSearchKey().toLowerCase().contains(searchKey)) {
                        if (allVideosList.get(i).getSearchKey().contains(",")) {
                            videoSearchKeyFilter.addAll(Arrays.asList(allVideosList.get(i).getSearchKey().split(",")));
                        }
                        videoSearchKeyFilter.add(allVideosList.get(i).getSearchKey());
                        searchAllVideos.add(allVideosList.get(i));

                    }
                    if (allVideosList.get(i).getTitle().toLowerCase().contains(searchKey)) {
                        videoTitleKeyFilter.add(allVideosList.get(i).getTitle());
                        searchAllVideos.add(allVideosList.get(i));
                    }
                }

            }

            for (int k = 0; k < searchKeyFilter.size(); k++) {
                if (searchKeyFilter.get(k).contains(",")) {

                    searchKeyFilter.remove(k);
                } else {
                    if (searchKeyFilter.get(k).toLowerCase().contains(searchKey.toLowerCase()) && !quickReadMainFilterList.contains(searchKeyFilter.get(k))) {
                        quickReadMainFilterList.add(searchKeyFilter.get(k));
                    }
                }
            }

            for (int k = 0; k < titleKeyFilter.size(); k++) {
                quickReadMainFilterList.add(titleKeyFilter.get(k));
            }

            for (int k = 0; k < videoSearchKeyFilter.size(); k++) {
                if (videoSearchKeyFilter.get(k).contains(",")) {
                    videoSearchKeyFilter.remove(k);
                } else {
                    if (videoSearchKeyFilter.get(k).toLowerCase().contains(searchKey.toLowerCase()) && !quickReadMainFilterList.contains(videoSearchKeyFilter.get(k))) {
                        quickReadMainFilterList.add(videoSearchKeyFilter.get(k));

                    }
                }
            }

            for (int k = 0; k < videoTitleKeyFilter.size(); k++) {
                quickReadMainFilterList.add(videoTitleKeyFilter.get(k));
            }

            if (quickReadMainFilterList.size() > 0) {
                binding.searchViewCard.setVisibility(View.VISIBLE);
                binding.searchRecyclerList.setLayoutManager(new LinearLayoutManager(this));
                binding.searchRecyclerList.hasFixedSize();
                /*Set<String> filteredList = new HashSet<>(quickReadMainFilterList);
                quickReadMainFilterList.clear();
                quickReadMainFilterList.addAll(filteredList);*/
                searchListAdapter = new SearchListAdapter(this, quickReadMainFilterList, this);
                binding.searchRecyclerList.setAdapter(searchListAdapter);
            } else {
                binding.searchViewCard.setVisibility(View.GONE);
                this.searchKey = searchKey;
                addSearchHistory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSearchHistory() {
        /*if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();*/

        SearchRequest request = new SearchRequest(searchKey);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.addSearchHistory(SharedPref.getAuthToken(), request);

        //Log.d("search history", "request: "+new Gson().toJson(request));
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                /*if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();*/
                if (response.code() == 200 && response.body() != null) {
                    //Log.d("search history", "Success: "+new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {
                /*if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();*/
                //Log.d("search history", "failed "+t.getMessage());
            }
        });
    }

    private void searchLogic(String searchKey) {
        binding.searchViewCard.setVisibility(View.GONE);
        binding.laBear.setVisibility(View.VISIBLE);
        selectedSearchText = searchKey;
        binding.healthHacksSearchEt.setText(searchKey);

        ArrayList<GetDashboardDataResponse.Data.QucikRead> data = new ArrayList<>();
        ArrayList<GetDashboardDataResponse.Data.HealthTv> videoData = new ArrayList<>();
        ArrayList<GetDashboardDataResponse.Data.HealthTv> bookMarkVideo = new ArrayList<>();
        for (int i = 0; i < allBlogsList.size(); i++) {
            Log.e("tags", String.valueOf(i));
            if (allBlogsList.get(i).getArticleName() != null && !allBlogsList.get(i).getArticleName().equals("") && allBlogsList.get(i).getSearchkey() != null && !allBlogsList.get(i).getSearchkey().equals("")) {
                if (allBlogsList.get(i).getArticleName().toLowerCase().contains(searchKey.toLowerCase()) || allBlogsList.get(i).getSearchkey().toLowerCase().contains(searchKey.toLowerCase())) {
                    data.add(allBlogsList.get(i));
                }
            }

        }

        for (int i = 0; i < allVideosList.size(); i++) {
            if (allVideosList.get(i).getTitle() != null && !allVideosList.get(i).getTitle().equals("") && allVideosList.get(i).getSearchKey() != null && !allVideosList.get(i).getSearchKey().equals("")) {
                if (allVideosList.get(i).getTitle().toLowerCase().contains(searchKey.toLowerCase()) || allVideosList.get(i).getSearchKey().toLowerCase().contains(searchKey.toLowerCase())) {
                    videoData.add(allVideosList.get(i));
                    if (allVideosList.get(i).getIsBookMarked() == 1) {
                        bookMarkVideo.add(allVideosList.get(i));
                    }
                }
            }
        }

        searchQuickData(data);
        searchHealthTvData(videoData);
        searchBookMark(bookMarkVideo);
    }

    @Override
    public void onSearchClick(String searchKey) {
        selectedSearchText = searchKey;
        searchLogic(searchKey);
    }

    private void searchQuickData(List<GetDashboardDataResponse.Data.QucikRead> dataList) {
        try {
            if (dataList.size() == 0) {
                binding.rlQuickRead.setVisibility(View.GONE);
            } else {
                binding.rlQuickRead.setVisibility(View.VISIBLE);
            }
            HealthHacksQuickReadsAdapter healthHacksQuickReadsAdapter = new HealthHacksQuickReadsAdapter(context, dataList, new HealthHacksQuickReadsAdapter.OnItemClickListener() {
                @Override
                public void onClick(String articleCode, boolean isBookmark) {
                    addBookmark(articleCode, isBookmark);
                }
            });
            binding.rvQuickReads.setLayoutManager(quickReadLinearLayoutManager);
            quickReadLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.healthHacksSearchEt.addTextChangedListener(this);
            binding.rvQuickReads.setAdapter(healthHacksQuickReadsAdapter);
            setUpQuickReadIndicator(quickReadLinearLayoutManager, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void searchHealthTvData(List<GetDashboardDataResponse.Data.HealthTv> dataList) {
        try {
            if (dataList.size() == 0) {
                binding.llHealthTv.setVisibility(View.GONE);
            } else {
                binding.llHealthTv.setVisibility(View.VISIBLE);
            }
            HealthHacksHealthTvAdapter absorbHealthTvAdapter = new HealthHacksHealthTvAdapter(context, dataList, new HealthHacksHealthTvAdapter.OnItemClickListener() {
                @Override
                public void onClick(int id, boolean isBookmark) {
                    addVideoBookmark(isBookmark, id);
                }
            });

            binding.rvHealthTv.setLayoutManager(healthTvLinearLayoutManager);
            healthTvLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.healthHacksSearchEt.addTextChangedListener(this);
            binding.rvHealthTv.setAdapter(absorbHealthTvAdapter);
            setUpHealthTvIndicator(healthTvLinearLayoutManager, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void searchBookMark(List<GetDashboardDataResponse.Data.HealthTv> dataList) {
        try {
            if (dataList.size() == 0) {
                binding.rlHealthTvBookmark.setVisibility(View.GONE);
            } else {
                binding.rlHealthTvBookmark.setVisibility(View.VISIBLE);
            }

            HealthHacksHealthTvAdapter absorbHealthTvAdapter = new HealthHacksHealthTvAdapter(context, dataList, new HealthHacksHealthTvAdapter.OnItemClickListener() {
                @Override
                public void onClick(int id, boolean isBookmark) {
                    addVideoBookmark(isBookmark, id);
                }
            });
            binding.rvHealthTvBookmark.setLayoutManager(healthTvBookmarkLayoutManager);
            healthTvBookmarkLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvHealthTvBookmark.setAdapter(absorbHealthTvAdapter);
            binding.rvHealthTvBookmark.setOnFlingListener(null);
            setUpBookMarkIndicator(healthTvBookmarkLayoutManager, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void resetQuickRead() {
        try {
            if (quickReadList.size() == 0) {
                binding.rlQuickRead.setVisibility(View.GONE);
            } else {
                binding.rlQuickRead.setVisibility(View.VISIBLE);
            }
            HealthHacksQuickReadsAdapter healthHacksQuickReadsAdapter = new HealthHacksQuickReadsAdapter(context, quickReadList, new HealthHacksQuickReadsAdapter.OnItemClickListener() {
                @Override
                public void onClick(String articleCode, boolean isBookmark) {
                    addBookmark(articleCode, isBookmark);
                }
            });
            //Log.d("AuthToken","Reset Size: "+quickReadList.size());
            binding.rvQuickReads.setLayoutManager(quickReadLinearLayoutManager);
            quickReadLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.healthHacksSearchEt.addTextChangedListener(this);
            binding.rvQuickReads.setAdapter(healthHacksQuickReadsAdapter);
            setUpQuickReadIndicator(quickReadLinearLayoutManager, quickReadList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetHealthTv() {
        try {
            if (healthTvList.size() == 0) {
                binding.llHealthTv.setVisibility(View.GONE);
            } else {
                binding.llHealthTv.setVisibility(View.VISIBLE);
            }
            HealthHacksHealthTvAdapter absorbHealthTvAdapter = new HealthHacksHealthTvAdapter(context, healthTvList, new HealthHacksHealthTvAdapter.OnItemClickListener() {
                @Override
                public void onClick(int id, boolean isBookmark) {
                    addVideoBookmark(isBookmark, id);
                }
            });
            //Log.d("AuthToken","Reset Size: "+healthTvList.size());
            binding.rvHealthTv.setLayoutManager(healthTvLinearLayoutManager);
            healthTvLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.healthHacksSearchEt.addTextChangedListener(this);
            binding.rvHealthTv.setAdapter(absorbHealthTvAdapter);
            setUpHealthTvIndicator(healthTvLinearLayoutManager, healthTvList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getAbsorbDashboard(String tagName, String searckey) {
        this.tagName = tagName;
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        newSearchKey = binding.healthHacksSearchEt.getText().toString();
        GetDashboardDataRequest dashboardDataRequest = new GetDashboardDataRequest(tagName, binding.healthHacksSearchEt.getText().toString());
        Call<GetDashboardDataResponse> call = apiInterfaceWyh.getAbsorbDashboard(SharedPref.getAuthToken(), dashboardDataRequest);
        Log.v("AuthToken", "Absorb Response" + new Gson().toJson(dashboardDataRequest) + "\n" + SharedPref.getAuthToken());
        Log.v("AuthToken", "Absorb Response" + new Gson().toJson(call.request().url()) + "\n" + SharedPref.getAuthToken());

        call.enqueue(new Callback<GetDashboardDataResponse>() {
            @Override
            public void onResponse(Call<GetDashboardDataResponse> call, Response<GetDashboardDataResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    if (response.body().getData() != null) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_dashboard_success));
                        quickReadList = response.body().getData().getQucikReads();
                        healthTvList = response.body().getData().getHealthTv();
                        healthTvBookmarkList = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.Webinar> webinarList = response.body().getData().getWebinar();
                        List<GetDashboardDataResponse.Data.HealthTv> isBookmark = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.HealthTv> nonBookMark = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.HealthTv> temp = new ArrayList<>();
                        tagNameList = response.body().getData().getTagName();
                        audioList = response.body().getData().getAudioFiles();
                        allVideosList = response.body().getData().getAllvideos();
                        allBlogsList = response.body().getData().getAllBlogs();
                        healthTvBookmarkList.clear();
                        if (healthTvList.size() > 0) {
                            for (GetDashboardDataResponse.Data.HealthTv item : healthTvList) {
                                if (item.getIsBookMarked() == 1) {
                                    healthTvBookmarkList.add(item);
                                    isBookmark.add(item);
                                } else {
                                    nonBookMark.add(item);
                                }
                            }

                        }


                        temp.addAll(isBookmark);
                        temp.addAll(nonBookMark);
                        healthTvList.clear();
                        healthTvList = temp;


                        if (quickReadList.size() > 0) {
                            setQuickReadData(quickReadList);
                        }
                        if (healthTvList.size() > 0) {
                            binding.llHealthTv.setVisibility(View.VISIBLE);
                            setHealthTvData(healthTvList);
                        } else {
                            binding.llHealthTv.setVisibility(View.GONE);
                        }

                        binding.llHealthTvBookmark.setVisibility(View.GONE);
                        if (healthTvBookmarkList.size() > 0) {
                            binding.llHealthTvBookmark.setVisibility(View.VISIBLE);
                            setHealthTvBookmarkData(healthTvBookmarkList);
                        }
                        if (webinarList.size() > 0) {
                            binding.llWebinar.setVisibility(View.VISIBLE);
                            setWebinarData(webinarList);
                        } else {
                            binding.llWebinar.setVisibility(View.GONE);
                        }

                        if (Objects.equals(tagName, "Meditation") && audioList.size() > 0) {
                            binding.llMusicLib.setVisibility(View.VISIBLE);
                            setMusicLibData(audioList);
                        } else {
                            binding.llMusicLib.setVisibility(View.GONE);
                        }

                        quickReadLinearLayoutManager = new LinearLayoutManager(context);
                        healthTvLinearLayoutManager = new LinearLayoutManager(context);
                        musicLibLinearLayoutManager = new LinearLayoutManager(context);
                        healthTvBookmarkLayoutManager = new LinearLayoutManager(context);

                        List<GetDashboardDataResponse.Data.QucikRead> dataList = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.HealthTv> healthDataList = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.AudioFiles> audioDataList = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.HealthTv> bookMarkList = new ArrayList<>();

                        quickReadSearchList = new ArrayList<>();
                        healthTvSearchList = new ArrayList<>();
                        audioLibSearchList = new ArrayList<>();
                        healthTvBookmark = new ArrayList<>();


                        if (quickReadList.size() > 6) {
                            for (int i = 0; i < 6; i++) {
                                dataList.add(quickReadList.get(i));
                            }
                        } else {
                            dataList.addAll(quickReadList);
                        }


                        if (healthTvList.size() > 6) {
                            for (int i = 0; i < 6; i++) {
                                healthDataList.add(healthTvList.get(i));
                            }
                        } else {
                            healthDataList.addAll(healthTvList);
                        }

                        if (audioList.size() > 6) {
                            for (int i = 0; i < 6; i++) {
                                audioDataList.add(audioList.get(i));
                            }
                        } else {
                            audioDataList.addAll(audioList);
                        }

                        if (healthTvBookmarkList.size() > 6) {
                            for (int i = 0; i < 6; i++) {
                                bookMarkList.add(healthTvBookmarkList.get(i));
                            }
                        } else {
                            bookMarkList.addAll(healthTvBookmarkList);
                        }


                        if (fetchTagsData) {
                            if (tagNameList.size() > 0) {
                                setTagNameData(tagNameList);
                            }
                            fetchTagsData = false;
                        }

                        if (!selectedSearchText.equals("")) {
                            searchLogic(selectedSearchText);
                        }
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

    private void setTagNameData(List<GetDashboardDataResponse.Data.TagName> tagNameList) {
        absorbQuickReadsAdapter = new HealthHacksTagsAdapter(context, tagNameList);
        LinearLayoutManager absorbLinearLayoutManager = new LinearLayoutManager(context);
        absorbLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvTags.setOnFlingListener(null);
        binding.rvTags.setLayoutManager(absorbLinearLayoutManager);
        binding.rvTags.setAdapter(absorbQuickReadsAdapter);

        LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvTags);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1212 && HealthTVDashboard.isPlayedFull) {
            updateRewards();

        }
    }

    private void updateRewards() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("ActivityUploads", "")
                .addFormDataPart("eventName", "HealthTv")
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

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.code() == 200 && response.body() != null) {
                    JsonParser parser = new JsonParser();
                    String json = response.body().string();
                    JsonElement mJson = parser.parse(json);
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(mJson, CommonSuccessResponse.class);
                    HealthTVDashboard.isPlayedFull = false;
                    if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (commonSuccessResponse.getSpinTheWheelRewardsModel() != null) {
                                    spinRewardsData = commonSuccessResponse.getSpinTheWheelRewardsModel();
                                    getSpinRewardPopup(spinRewardsData);
                                }
                                if (checkIsFromQuizqathon()) {
                                    //QuizReward Api Call
                                    FetchQuizReward();
                                    //quizathonRewardData = commonSuccessResponse.getQuizathonRewardData();
                                    //getQuizathonRewardPopup(quizathonRewardData);
                                }

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

    private void FetchQuizReward() {
        ActivityRewardRequest activityRewardRequest = new ActivityRewardRequest(NewDashboardHelper.Companion.getTrasactionId(),NewDashboardHelper.Companion.getFeatureName());
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


    public  boolean checkIsFromQuizqathon(){
        if(NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()){
            return true;
        }
        return false;
    }

    private void setQuickReadData(List<GetDashboardDataResponse.Data.QucikRead> quickReadList) {

        List<GetDashboardDataResponse.Data.QucikRead> dataList = new ArrayList<>();
        List<GetDashboardDataResponse.Data.HealthTv> healthDataList = new ArrayList<>();
        List<GetDashboardDataResponse.Data.AudioFiles> audioDataList = new ArrayList<>();
        List<GetDashboardDataResponse.Data.HealthTv> bookMarkList = new ArrayList<>();


        LinearLayoutManager quickReadLinearLayoutManager = new LinearLayoutManager(context);
        LinearLayoutManager healthTvLinearLayoutManager = new LinearLayoutManager(context);
        LinearLayoutManager musicLibLinearLayoutManager = new LinearLayoutManager(context);
        LinearLayoutManager healthTvBookmarkLayoutManager = new LinearLayoutManager(context);

        quickReadSearchList = new ArrayList<>();
        healthTvSearchList = new ArrayList<>();
        audioLibSearchList = new ArrayList<>();
        healthTvBookmark = new ArrayList<>();

        if (quickReadList.size() > 6) {
            for (int i = 0; i < 6; i++) {
                dataList.add(quickReadList.get(i));
            }
        } else {
            dataList.addAll(quickReadList);
        }


        if (healthTvList.size() > 6) {
            for (int i = 0; i < 6; i++) {
                healthDataList.add(healthTvList.get(i));
            }
        } else {
            healthDataList.addAll(healthTvList);
        }

        if (audioList.size() > 6) {
            for (int i = 0; i < 6; i++) {
                audioDataList.add(audioList.get(i));
            }
        } else {
            audioDataList.addAll(audioList);
        }

        if (healthTvBookmarkList.size() > 6) {
            for (int i = 0; i < 6; i++) {
                bookMarkList.add(healthTvBookmarkList.get(i));
            }
        } else {
            bookMarkList.addAll(healthTvBookmarkList);
        }

        HealthHacksQuickReadsAdapter healthHacksQuickReadsAdapter = new HealthHacksQuickReadsAdapter(context, dataList, new HealthHacksQuickReadsAdapter.OnItemClickListener() {
            @Override
            public void onClick(String articleCode, boolean isBookmark) {
                addBookmark(articleCode, isBookmark);
            }
        });

        quickReadLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        healthTvLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        musicLibLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        healthTvBookmarkLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvQuickReads.setLayoutManager(quickReadLinearLayoutManager);
        binding.rvHealthTv.setLayoutManager(healthTvLinearLayoutManager);
        binding.rvMusicLib.setLayoutManager(musicLibLinearLayoutManager);
        binding.rvHealthTvBookmark.setLayoutManager(healthTvBookmarkLayoutManager);


        binding.rvQuickReads.setAdapter(healthHacksQuickReadsAdapter);


        binding.rvQuickReads.setOnFlingListener(null);

        LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvQuickReads);

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


        LinearLayoutManager quickReadLinearLayoutManager1 = new LinearLayoutManager(context);
        quickReadLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter quickReadIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
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
        });
    }

    private void clearQuickReadSearch(LinearLayoutManager quickReadLinearLayoutManager, List<GetDashboardDataResponse.Data.QucikRead> dataList) {
        try {
            dataList.clear();
            quickReadLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvQuickReads.setLayoutManager(quickReadLinearLayoutManager);
            if (quickReadList.size() > 6) {
                for (int a = 0; a < 6; a++) {
                    dataList.add(quickReadList.get(a));
                }
            } else {
                dataList.addAll(quickReadList);
            }
            HealthHacksQuickReadsAdapter healthHacksQuickReadsAdapter = new HealthHacksQuickReadsAdapter(context, dataList, new HealthHacksQuickReadsAdapter.OnItemClickListener() {
                @Override
                public void onClick(String articleCode, boolean isBookmark) {
                    addBookmark(articleCode, isBookmark);
                }
            });
            binding.rvQuickReads.setOnFlingListener(null);

            LinearSnapHelper absorbHealthTvLinearSnapHelper = new SnapHelperOneByOne();
            absorbHealthTvLinearSnapHelper.attachToRecyclerView(binding.rvQuickReads);
            binding.rvQuickReads.setAdapter(healthHacksQuickReadsAdapter);
            setUpQuickReadIndicator(quickReadLinearLayoutManager, dataList);
        } catch (Exception e) {
            e.printStackTrace();
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
                }
                else if (rewardtype.equalsIgnoreCase("future")) {
                    QuizRewardDialog.INSTANCE.showFutureRewardDialog(context,data.getDialogModel(),data.getClaimDate());
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
            if(spinRewardsData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, spinRewardsData.getRewardType(), this);
            }else if(quizathonRewardData != null){
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, quizathonRewardData.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }

    private void clearHealthtvSearch(LinearLayoutManager healthTvLinearLayoutManager, List<GetDashboardDataResponse.Data.HealthTv> dataList) {
        try {
            dataList.clear();
            healthTvLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvHealthTv.setLayoutManager(healthTvLinearLayoutManager);
            if (healthTvList.size() > 6) {
                for (int a = 0; a < 6; a++) {
                    dataList.add(healthTvList.get(a));
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
            binding.rvHealthTv.setOnFlingListener(null);

            LinearSnapHelper absorbHealthTvLinearSnapHelper = new SnapHelperOneByOne();
            absorbHealthTvLinearSnapHelper.attachToRecyclerView(binding.rvHealthTv);
            binding.rvHealthTv.setAdapter(absorbHealthTvAdapter);
            setUpHealthTvIndicator(healthTvLinearLayoutManager, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearMusicLibSerach(LinearLayoutManager MusicLibLinearLayoutManager, List<GetDashboardDataResponse.Data.AudioFiles> dataList) {
        try {
            dataList.clear();
            MusicLibLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvMusicLib.setLayoutManager(MusicLibLinearLayoutManager);
            if (audioList.size() > 6) {
                for (int a = 0; a < 6; a++) {
                    dataList.add(audioList.get(a));
                }
            } else {
                dataList.addAll(audioList);
            }

            MusicLibAdapter absorbHealthTvAdapter = new MusicLibAdapter(context, dataList);
            binding.rvMusicLib.setLayoutManager(MusicLibLinearLayoutManager);
            binding.rvMusicLib.setAdapter(absorbHealthTvAdapter);
            binding.rvMusicLib.setOnFlingListener(null);

            LinearSnapHelper absorbHealthTvLinearSnapHelper = new SnapHelperOneByOne();
            absorbHealthTvLinearSnapHelper.attachToRecyclerView(binding.rvMusicLib);

            binding.rvMusicLib.setAdapter(absorbHealthTvAdapter);
            setUpAudioLibIndicator(MusicLibLinearLayoutManager, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void clearHealthTvBoomarkSerach(LinearLayoutManager healthTvBookmarkLayoutManager, List<GetDashboardDataResponse.Data.HealthTv> dataList) {
        try {
            dataList.clear();
            healthTvBookmarkLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvHealthTvBookmark.setLayoutManager(healthTvBookmarkLayoutManager);
            if (healthTvBookmarkList.size() > 6) {
                for (int a = 0; a < 6; a++) {
                    dataList.add(healthTvBookmarkList.get(a));
                }
            } else {
                dataList.addAll(healthTvBookmarkList);
            }

            HealthHacksHealthTvAdapter absorbHealthTvAdapter = new HealthHacksHealthTvAdapter(context, dataList, new HealthHacksHealthTvAdapter.OnItemClickListener() {
                @Override
                public void onClick(int id, boolean isBookmark) {
                    addVideoBookmark(isBookmark, id);
                }
            });
            binding.rvHealthTvBookmark.setLayoutManager(healthTvBookmarkLayoutManager);
            binding.rvHealthTvBookmark.setAdapter(absorbHealthTvAdapter);
            binding.rvHealthTvBookmark.setOnFlingListener(null);

            LinearSnapHelper absorbHealthTvLinearSnapHelper = new SnapHelperOneByOne();
            absorbHealthTvLinearSnapHelper.attachToRecyclerView(binding.rvHealthTvBookmark);

            binding.rvHealthTvBookmark.setAdapter(absorbHealthTvAdapter);
            setUpBookMarkIndicator(healthTvBookmarkLayoutManager, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void serachQuickReadText(LinearLayoutManager quickReadLinearLayoutManager, List<GetDashboardDataResponse.Data.QucikRead> dataList) {
        try {
            quickReadSearchList.clear();
            quickReadLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvQuickReads.setLayoutManager(quickReadLinearLayoutManager);
            if (quickReadList.size() > 6) {
                dataList.clear();
                for (int k = 0; k < 6; k++) {
                    dataList.add(quickReadList.get(k));
                }
            } else {
                dataList.clear();
                dataList.addAll(quickReadList);
            }
            if (dataList.size() == 0) {
                binding.rlQuickRead.setVisibility(View.GONE);
            } else {
                binding.rlQuickRead.setVisibility(View.VISIBLE);
            }
            HealthHacksQuickReadsAdapter healthHacksQuickReadsAdapter = new HealthHacksQuickReadsAdapter(context, dataList, new HealthHacksQuickReadsAdapter.OnItemClickListener() {
                @Override
                public void onClick(String articleCode, boolean isBookmark) {
                    addBookmark(articleCode, isBookmark);
                }
            });
            binding.rvQuickReads.setAdapter(healthHacksQuickReadsAdapter);
            setUpQuickReadIndicator(quickReadLinearLayoutManager, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void searchHealthTvText(LinearLayoutManager healthTvLinearLayoutManager, List<GetDashboardDataResponse.Data.HealthTv> dataList) {
        try {
            healthTvSearchList.clear();
            /*for(int j=0; j< healthTvList.size();j++){
                if(healthTvList.get(j).getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())){
                    healthTvSearchList.add(healthTvList.get(j));
                }
            }*/

            healthTvLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvHealthTv.setLayoutManager(healthTvLinearLayoutManager);
            if (healthTvList.size() > 6) {
                dataList.clear();
                for (int k = 0; k < 6; k++) {
                    dataList.add(healthTvList.get(k));
                }
            } else {
                dataList.clear();
                dataList.addAll(healthTvList);
            }
            if (dataList.size() == 0) {
                binding.rlHealthTv.setVisibility(View.GONE);
            } else {
                binding.rlHealthTv.setVisibility(View.VISIBLE);
            }
            HealthHacksHealthTvAdapter absorbHealthTvAdapter = new HealthHacksHealthTvAdapter(context, dataList, new HealthHacksHealthTvAdapter.OnItemClickListener() {
                @Override
                public void onClick(int id, boolean isBookmark) {
                    addVideoBookmark(isBookmark, id);
                }
            });
            binding.rvHealthTv.setAdapter(absorbHealthTvAdapter);
            setUpHealthTvIndicator(healthTvLinearLayoutManager, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void searchAudioFileText(LinearLayoutManager audioLibLinearLayoutManager, List<GetDashboardDataResponse.Data.AudioFiles> dataList) {
        try {
            audioLibSearchList.clear();
            /*for(int j=0; j< audioList.size();j++){
                if(audioList.get(j).getTitle().toLowerCase().contains(charSequence)){
                    audioLibSearchList.add(audioList.get(j));
                }
            }*/

            audioLibLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvMusicLib.setLayoutManager(audioLibLinearLayoutManager);
            if (audioList.size() > 6) {
                dataList.clear();
                for (int k = 0; k < 6; k++) {
                    dataList.add(audioList.get(k));
                }
            } else {
                dataList.clear();
                dataList.addAll(audioList);
            }

            if (dataList.size() == 0) {
                binding.rlMusicLib.setVisibility(View.GONE);
            } else {
                binding.rlMusicLib.setVisibility(View.VISIBLE);
            }

            MusicLibAdapter absorbHealthTvAdapter = new MusicLibAdapter(context, dataList);
            binding.rvMusicLib.setLayoutManager(audioLibLinearLayoutManager);
            binding.rvMusicLib.setAdapter(absorbHealthTvAdapter);
            binding.rvMusicLib.setOnFlingListener(null);
            binding.rvMusicLib.setAdapter(absorbHealthTvAdapter);
            setUpAudioLibIndicator(audioLibLinearLayoutManager, dataList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void searchBookMarkVideo(LinearLayoutManager audioLibLinearLayoutManager, List<GetDashboardDataResponse.Data.HealthTv> dataList) {
        try {
            healthTvBookmark.clear();
            /*for(int j=0; j< healthTvBookmarkList.size();j++){
                if(healthTvBookmarkList.get(j).getTitle().toLowerCase().contains(charSequence)){
                    healthTvBookmark.add(healthTvBookmarkList.get(j));
                }
            }*/

            audioLibLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvHealthTvBookmark.setLayoutManager(audioLibLinearLayoutManager);
            if (healthTvBookmarkList.size() > 6) {
                dataList.clear();
                for (int k = 0; k < 6; k++) {
                    dataList.add(healthTvBookmarkList.get(k));
                }
            } else {
                dataList.clear();
                dataList.addAll(healthTvBookmarkList);
            }

            if (dataList.size() == 0) {
                binding.rlHealthTvBookmark.setVisibility(View.GONE);
            } else {
                binding.rlHealthTvBookmark.setVisibility(View.VISIBLE);
            }

            HealthHacksHealthTvAdapter absorbHealthTvAdapter = new HealthHacksHealthTvAdapter(context, dataList, new HealthHacksHealthTvAdapter.OnItemClickListener() {
                @Override
                public void onClick(int id, boolean isBookmark) {
                    addVideoBookmark(isBookmark, id);
                }
            });
            LinearLayoutManager absorbHealthTvLinearLayoutManager = new LinearLayoutManager(context);
            absorbHealthTvLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvHealthTvBookmark.setLayoutManager(absorbHealthTvLinearLayoutManager);
            binding.rvHealthTvBookmark.setAdapter(absorbHealthTvAdapter);
            binding.rvHealthTvBookmark.setOnFlingListener(null);
            setUpBookMarkIndicator(absorbHealthTvLinearLayoutManager, dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setUpQuickReadIndicator(LinearLayoutManager quickReadLinearLayoutManager, List<GetDashboardDataResponse.Data.QucikRead> dataList) {
        try {
            if (dataList.size() > 6) {
                indicatorSize = (int) Math.ceil(6.0 / 2.0);
            } else {
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(dataList.size())) / 2.0);
            }
            if (indicatorSize > 1) {
                binding.rvQuickReadsIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.rvQuickReadsIndicator.setVisibility(View.INVISIBLE);
            }

            LinearLayoutManager quickReadLinearLayoutManager1 = new LinearLayoutManager(context);
            quickReadLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            IndicatorsAdapter quickReadIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
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
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpHealthTvIndicator(LinearLayoutManager quickReadLinearLayoutManager, List<GetDashboardDataResponse.Data.HealthTv> dataList) {
        try {
            if (dataList.size() > 6) {
                indicatorSize = (int) Math.ceil(6.0 / 2.0);
            } else {
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(dataList.size())) / 2.0);
            }

            if (indicatorSize > 1) {
                binding.rvHealthTvIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.rvHealthTvIndicator.setVisibility(View.INVISIBLE);
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
                        if (quickReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionHealthTv = quickReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionHealthTv = quickReadLinearLayoutManager.findFirstVisibleItemPosition();
                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(positionHealthTv);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpAudioLibIndicator(LinearLayoutManager quickReadLinearLayoutManager, List<GetDashboardDataResponse.Data.AudioFiles> dataList) {
        try {
            if (dataList.size() > 6) {
                binding.ivMusicLibraryMore.setVisibility(View.VISIBLE);
                binding.rlMusicLib.setEnabled(true);
                indicatorSize = (int) Math.ceil(6.0 / 2.0);
            } else {
                binding.ivMusicLibraryMore.setVisibility(View.GONE);
                binding.rlMusicLib.setEnabled(false);
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(dataList.size())) / 2.0);
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
                        if (quickReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionMusicLib = quickReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionMusicLib = quickReadLinearLayoutManager.findFirstVisibleItemPosition();
                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(positionMusicLib);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpBookMarkIndicator(LinearLayoutManager quickReadLinearLayoutManager, List<GetDashboardDataResponse.Data.HealthTv> dataList) {
        try {
            if (dataList.size() > 6) {
                binding.ivBookmarkHealthTvMore.setVisibility(View.VISIBLE);
                binding.rlHealthTvBookmark.setEnabled(true);
                indicatorSize = (int) Math.ceil(6.0 / 2.0);
            } else {
                binding.ivBookmarkHealthTvMore.setVisibility(View.GONE);
                binding.rlHealthTvBookmark.setEnabled(false);
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(dataList.size())) / 2.0);
            }

            if (indicatorSize > 1) {
                binding.rvHealthTvIndicatorBookmark.setVisibility(View.VISIBLE);
            } else {
                binding.rvHealthTvIndicatorBookmark.setVisibility(View.INVISIBLE);
            }

            LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(context);
            absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvHealthTvIndicatorBookmark.setAdapter(absorbHealthTvIndicatorsAdapter);
            binding.rvHealthTvIndicatorBookmark.setLayoutManager(absorbHealthTvLinearLayoutManager1);
            binding.rvHealthTvIndicatorBookmark.setHasFixedSize(true);

            binding.rvHealthTvBookmark.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (quickReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionHealthTvBookmark = quickReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionHealthTvBookmark = quickReadLinearLayoutManager.findFirstVisibleItemPosition();
                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(positionHealthTvBookmark);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            indicatorSize = (int) Math.ceil(6.0 / 2.0);
        } else {
            indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(healthTvList.size())) / 2.0);
        }

        if (indicatorSize > 1) {
            binding.rvHealthTvIndicator.setVisibility(View.VISIBLE);
        } else {
            binding.rvHealthTvIndicator.setVisibility(View.INVISIBLE);
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

    private void setHealthTvBookmarkData(List<GetDashboardDataResponse.Data.HealthTv> healthTvList) {
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
        binding.rvHealthTvBookmark.setLayoutManager(absorbHealthTvLinearLayoutManager);
        binding.rvHealthTvBookmark.setAdapter(absorbHealthTvAdapter);
        binding.rvHealthTvBookmark.setOnFlingListener(null);

        LinearSnapHelper absorbHealthTvLinearSnapHelper = new SnapHelperOneByOne();
        absorbHealthTvLinearSnapHelper.attachToRecyclerView(binding.rvHealthTvBookmark);


        int indicatorSize;

        if (healthTvList.size() > 6) {
            binding.ivBookmarkHealthTvMore.setVisibility(View.VISIBLE);
            binding.rlHealthTvBookmark.setEnabled(true);
            indicatorSize = (int) Math.ceil(6.0 / 2.0);
        } else {
            binding.ivBookmarkHealthTvMore.setVisibility(View.GONE);
            binding.rlHealthTvBookmark.setEnabled(false);
            indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(healthTvList.size())) / 2.0);
        }

        if (indicatorSize > 1) {
            binding.rvHealthTvIndicatorBookmark.setVisibility(View.VISIBLE);
        } else {
            binding.rvHealthTvIndicatorBookmark.setVisibility(View.INVISIBLE);
        }

        LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(context);
        absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvHealthTvIndicatorBookmark.setAdapter(absorbHealthTvIndicatorsAdapter);
        binding.rvHealthTvIndicatorBookmark.setLayoutManager(absorbHealthTvLinearLayoutManager1);
        binding.rvHealthTvIndicatorBookmark.setHasFixedSize(true);

        binding.rvHealthTvBookmark.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionHealthTvBookmark = absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionHealthTvBookmark = absorbHealthTvLinearLayoutManager.findFirstVisibleItemPosition();
                    absorbHealthTvIndicatorsAdapter.updateSelectedIndex(positionHealthTvBookmark);
                }
            }
        });
    }

    private void setWebinarData(List<GetDashboardDataResponse.Data.Webinar> webinarList) {
        List<GetDashboardDataResponse.Data.Webinar> dataList = new ArrayList<>();
        if (webinarList.size() > 6) {
            for (int i = 0; i < 6; i++) {
                dataList.add(webinarList.get(i));
            }
        } else {
            dataList.addAll(webinarList);
        }
        HealthHacksWebinarAdapter absorbWebinarAdapter = new HealthHacksWebinarAdapter(context, dataList);
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

        int indicatorSize;
        if (dataList.size() > 6) {
            indicatorSize = (int) Math.ceil(6.0 / 2.0);
        } else {
            indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(dataList.size())) / 2.0);
        }

        if (indicatorSize > 1) {
            binding.rvWebinarIndicator.setVisibility(View.VISIBLE);
        } else {
            binding.rvWebinarIndicator.setVisibility(View.INVISIBLE);
        }

        LinearLayoutManager absorbWebinarLinearLayoutManager1 = new LinearLayoutManager(context);
        absorbWebinarLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter absorbWebinarIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
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

    private void addBookmark(String articleCode, boolean isBookmark) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AddBookmarkRequest request = new AddBookmarkRequest(articleCode, isBookmark);
        //Log.d("Url_Request_save book", new Gson().toJson(request) + " " + tagName);
        Call<AddBookmarkResponse> call = apiInterfaceWyh.addBookMark(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<AddBookmarkResponse>() {
            @Override
            public void onResponse(Call<AddBookmarkResponse> call, Response<AddBookmarkResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_success));

                    Log.d("BookMark", new Gson().toJson(response.body()));
                    if (isBookmark) {
                        Toast.makeText(context, "Successfully added to Bookmark", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Remove from Bookmark", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_failed));
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
                getAbsorbDashboard(tagName, binding.healthHacksSearchEt.getText().toString());
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

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(HealthHacksActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onScratchComplete() {
        if (quizathonRewardData != null) {
            QuizRewardDialog.INSTANCE.QuizScratchCard(this, quizathonRewardData.getTransId(), true);
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i >= 20) {
            scratchCardLayout.onFullReveal();
              {
                if (alertDialogBonusRewards != null && alertDialogBonusRewards.isShowing()) {
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialogBonusRewards.dismiss();
                        }
                    }, 3000);
                }
            }
        }
    }

    @Override
    public void onScratchStarted() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAbsorbDashboard(tagName, binding.healthHacksSearchEt.getText().toString());
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
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        } else {
            finish();
        }
    }

    private void showBonusRewardsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusRewards = alertBuilder.create();
        alertDialogBonusRewards.setCancelable(false);
        if (!alertDialogBonusRewards.isShowing())
            alertDialogBonusRewards.show();


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

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);

        binding.scratchView.onFullReveal();


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusRewards.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
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

                    getAbsorbDashboard(tagName, binding.healthHacksSearchEt.getText().toString());
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_failed));
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }


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
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
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


}