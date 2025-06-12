package com.wyh.happyyousdk.absorb;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import com.wyh.happyyousdk.Eventbus.BookmarkhealthtvEvent;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.absorb.adapter.AllVideosAdapter;
import com.wyh.happyyousdk.absorb.adapter.HealthHacksHealthTvAdapter;
import com.wyh.happyyousdk.absorb.adapter.HealthHacksTagsAdapter;
import com.wyh.happyyousdk.absorb.adapter.SearchListAdapter;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityHealthTvListingBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.absorb.GetDashboardDataRequest;
import com.wyh.happyyousdk.model.request.absorb.VideoBookmarkRequest;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthTvListingActivity extends AppCompatActivity implements ScratchListener, SearchKeyClick {
    ActivityHealthTvListingBinding binding;
    Context context;
    int positionHealthTv = 0;
    AlertDialog alertDialogBonusRewards;
    int positionHealthTvbookmark = 0, positionHealthTvrvHealthTvRecommend = 0;

    boolean isPositiveBtn = false;
    public static boolean isPlayedFull = false;
    String tagname = "";
    int selectedItem = 0;
    String selectedSearchText = "";

    List<GetDashboardDataResponse.Data.HealthTv> healthTvList = new ArrayList<>();
    List<GetDashboardDataResponse.Data.HealthTv> healthTvListbookmarked = new ArrayList<>();
    List<GetDashboardDataResponse.Data.HealthTv> healthTvListRecommend = new ArrayList<>();

    public static List<GetDashboardDataResponse.Data.HealthTv> allVideos = new ArrayList<>();
    List<GetDashboardDataResponse.Data.QucikRead> allBlogsList;
    List<GetDashboardDataResponse.Data.HealthTv> allVideosList;

    ArrayList<GetDashboardDataResponse.Data.HealthTv> searchAllVideos = new ArrayList<>();


    ArrayList<String> videoSearchKeyFilter = new ArrayList<>();
    ArrayList<String> videoTitleKeyFilter = new ArrayList<>();
    ArrayList<String> quickReadMainFilterList = new ArrayList<>();

    ArrayList<String> searchKeyFilter = new ArrayList<>();
    ArrayList<String> titleKeyFilter = new ArrayList<>();

    SearchListAdapter searchListAdapter;


    Boolean emptySearch = true;


    String cameFrom;
    List<GetDashboardDataResponse.Data.TagName> tagList;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_health_tv_listing);
        context = this;
        SharedPref.init(this);

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Health TV");

        cameFrom = getIntent().getStringExtra("cameFrom");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tagList = (List<GetDashboardDataResponse.Data.TagName>) extras.getSerializable("taglist");
            tagname = extras.getString("tagName");
            selectedItem = extras.getInt("selectedItem", 0);
            if (tagname == null && tagList != null && !tagList.isEmpty()) {
                tagname = "";
            }
            setTagNameData(tagList);
        }



        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.rlHealthTv.setOnClickListener(view -> {
            if (!healthTvList.isEmpty()) {
                Intent i = new Intent(this, HealthTVDashboard.class);
                i.putExtra("healthTV", (Serializable) healthTvList);
                i.putExtra("titleName", "Health TV");
                startActivity(i);
            }
        });

        binding.rlHealthTvbookmark.setOnClickListener(view -> {
            if (!healthTvListbookmarked.isEmpty()) {
                Intent i = new Intent(this, HealthTVDashboard.class);
                i.putExtra("healthTV", (Serializable) healthTvListbookmarked);
                i.putExtra("isBookmark", true);
                i.putExtra("titleName", "Bookmarks TV");
                startActivity(i);
            }
        });

        binding.rlHealthTvRecommend.setOnClickListener(view -> {
            if (!healthTvListbookmarked.isEmpty()) {
                Intent i = new Intent(this, HealthTVDashboard.class);
                i.putExtra("healthTV", (Serializable) healthTvListRecommend);
                i.putExtra("titleName", "Recommended Health TV video");
                startActivity(i);
            }
        });

        binding.rlHealthTvAllVidoes.setOnClickListener(v -> {
            if (!allVideos.isEmpty()) {
                Intent i = new Intent(HealthTvListingActivity.this, HealthTVDashboard.class);
                i.putExtra("healthTV", "");
                i.putExtra("titleName", "All Videos");
                startActivity(i);
            }
        });

        binding.healthHacksSearchImg.setOnClickListener(view -> {
            if (binding.healthListingSearchLayout.getVisibility() == View.INVISIBLE) {
                binding.healthListingSearchLayout.setVisibility(View.VISIBLE);
            } else {
                binding.healthListingSearchLayout.setVisibility(View.INVISIBLE);
            }
        });

        binding.healthListingCancel.setOnClickListener(view -> binding.healthListingSearchEt.setText(""));

        binding.healthListingSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (0 == (long) charSequence.length()) {
                    binding.healthListingCancel.setVisibility(View.GONE);
                    binding.searchViewCard.setVisibility(View.GONE);
                    selectedSearchText = "";
                    if (emptySearch) {
                        emptySearch = false;
                        getAbsorbDashboard(tagname);
                    }
                } else if (selectedSearchText.equalsIgnoreCase(charSequence.toString())) {
                    binding.searchViewCard.setVisibility(View.GONE);
                } else {
                    if ((long) charSequence.length() >= 3) {
                        emptySearch = true;
                        binding.healthListingCancel.setVisibility(View.VISIBLE);
                        binding.searchViewCard.setVisibility(View.VISIBLE);
                        searchItems(charSequence.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        LinearSnapHelper absorbHealthTvLinearSnapHelper = new SnapHelperOneByOne();
        absorbHealthTvLinearSnapHelper.attachToRecyclerView(binding.rvHealthTvbookmark);

    }

    private void searchItems(String searchKey) {
        try {
            searchKeyFilter.clear();
            titleKeyFilter.clear();
            quickReadMainFilterList.clear();


            for (int i = 0; i < allVideosList.size(); i++) {
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

            for (int k = 0; k < searchKeyFilter.size(); k++) {
                if (searchKeyFilter.get(k).contains(",")) {
                    searchKeyFilter.remove(k);
                } else {
                    if (searchKeyFilter.get(k).toLowerCase().contains(searchKey.toLowerCase()) && !quickReadMainFilterList.contains(searchKeyFilter.get(k))) {
                        quickReadMainFilterList.add(searchKeyFilter.get(k));
                    }
                }
            }

            quickReadMainFilterList.addAll(titleKeyFilter);

            for (int k = 0; k < videoSearchKeyFilter.size(); k++) {
                if (videoSearchKeyFilter.get(k).contains(",")) {
                    videoSearchKeyFilter.remove(k);
                } else {
                    if (videoSearchKeyFilter.get(k).toLowerCase().contains(searchKey.toLowerCase()) && !quickReadMainFilterList.contains(videoSearchKeyFilter.get(k))) {
                        quickReadMainFilterList.add(videoSearchKeyFilter.get(k));

                    }
                }
            }

            quickReadMainFilterList.addAll(videoTitleKeyFilter);

            if (!quickReadMainFilterList.isEmpty()) {
                binding.searchViewCard.setVisibility(View.VISIBLE);
                /*if(quickReadMainFilterList.size() < 5){
                    ViewGroup.LayoutParams params = binding.searchViewCard.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    binding.searchViewCard.setLayoutParams(params);
                }else{
                    ViewGroup.LayoutParams params = binding.searchViewCard.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = 500;
                    binding.searchViewCard.setLayoutParams(params);

                }*/
                binding.searchListingList.setLayoutManager(new LinearLayoutManager(this));
                binding.searchListingList.hasFixedSize();
                searchListAdapter = new SearchListAdapter(this, quickReadMainFilterList, this);
                binding.searchListingList.setAdapter(searchListAdapter);
            } else {
                binding.searchViewCard.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("HappyYou", "Error occurred", e);
        }
    }

    @Override
    public void onSearchClick(String searchKey) {
        selectedSearchText = searchKey;
        setSearchData(searchKey);
    }

    private void setSearchData(String searchKey) {
        try {
            binding.searchViewCard.setVisibility(View.GONE);
            binding.healthListingSearchEt.setText(searchKey);
            selectedSearchText = searchKey;
            List<GetDashboardDataResponse.Data.HealthTv> filterHealthTvList = new ArrayList<>();
            List<GetDashboardDataResponse.Data.HealthTv> filterHealthTvListbookmarked = new ArrayList<>();
            List<GetDashboardDataResponse.Data.HealthTv> filterHealthTvListRecommend = new ArrayList<>();
            List<GetDashboardDataResponse.Data.HealthTv> filterAllVideo = new ArrayList<>();

            for (int i = 0; i < healthTvList.size(); i++) {
                if (healthTvList.get(i).getTitle().toLowerCase().contains(searchKey.toLowerCase())) {
                    filterHealthTvList.add(healthTvList.get(i));
                }
            }

            for (int i = 0; i < healthTvListbookmarked.size(); i++) {
                if (healthTvListbookmarked.get(i).getTitle().toLowerCase().contains(searchKey.toLowerCase())) {
                    filterHealthTvListbookmarked.add(healthTvListbookmarked.get(i));
                }
            }

            for (int i = 0; i < healthTvListRecommend.size(); i++) {
                if (healthTvListRecommend.get(i).getTitle().toLowerCase().contains(searchKey.toLowerCase())) {
                    filterHealthTvListRecommend.add(healthTvListRecommend.get(i));
                }
            }

            for (int i = 0; i < allVideos.size(); i++) {
                if (allVideos.get(i).getTitle().toLowerCase().contains(searchKey.toLowerCase())) {
                    filterAllVideo.add(allVideos.get(i));
                }
            }

            setHealthTvData(filterHealthTvList);
            setHealthTvBookmarkData(filterHealthTvListbookmarked);
            setHealthTvRecommendData(filterHealthTvListRecommend);
            setAllVideos(filterAllVideo);

        } catch (Exception e) {
            Log.e("HappyYou", "Error occurred", e);
        }
    }

    private List<GetDashboardDataResponse.Data.HealthTv> getBookmarkedlist(List<GetDashboardDataResponse.Data.HealthTv> healthTvList) {

        List<GetDashboardDataResponse.Data.HealthTv> bookmarkedlist = new ArrayList<>();
        for (GetDashboardDataResponse.Data.HealthTv data : healthTvList) {
            if (data.getIsBookMarked() == 1) {
                bookmarkedlist.add(data);
            }
        }
        return bookmarkedlist;
    }


    private void setHealthTvData(List<GetDashboardDataResponse.Data.HealthTv> healthTvList) {
        if (healthTvList.isEmpty()) {
            binding.llAll.setVisibility(View.GONE);
        } else {
            binding.llAll.setVisibility(View.VISIBLE);
            List<GetDashboardDataResponse.Data.HealthTv> dataList = new ArrayList<>();
            if (healthTvList.size() > 6) {
                for (int i = 0; i < 6; i++) {
                    dataList.add(healthTvList.get(i));
                }
            } else {
                dataList.addAll(healthTvList);
            }
            HealthHacksHealthTvAdapter absorbHealthTvAdapter = new HealthHacksHealthTvAdapter(context, dataList, (id, isBookmark) -> addVideoBookmark(isBookmark, id));
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

    }

    private void setAllVideos(List<GetDashboardDataResponse.Data.HealthTv> allVideos) {
        try {
            List<GetDashboardDataResponse.Data.HealthTv> dataList = new ArrayList<>();
            if (allVideos.size() > 6) {
                for (int i = 0; i < 6; i++) {
                    dataList.add(allVideos.get(i));
                }
            } else {
                dataList.addAll(allVideos);
            }

            AllVideosAdapter allVideosAdapter = new AllVideosAdapter(context, dataList, (id, isBookmark) -> addVideoBookmark(isBookmark, id));


            LinearLayoutManager allVideoLinearLayoutManager = new LinearLayoutManager(context);
            allVideoLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvHealthTvAllVidoes.setLayoutManager(allVideoLinearLayoutManager);
            binding.rvHealthTvAllVidoes.setAdapter(allVideosAdapter);
            binding.rvHealthTvAllVidoes.setOnFlingListener(null);

            LinearSnapHelper absorbHealthTvLinearSnapHelper = new SnapHelperOneByOne();
            absorbHealthTvLinearSnapHelper.attachToRecyclerView(binding.rvHealthTvAllVidoes);


            int indicatorSize;

            if (allVideos.size() > 6) {
                indicatorSize = (int) Math.ceil(6.0 / 2.0);
            } else {
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(allVideos.size())) / 2.0);
            }

            if (indicatorSize > 1) {
                binding.rvHealthTvAllVideosIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.rvHealthTvAllVideosIndicator.setVisibility(View.INVISIBLE);
            }

            LinearLayoutManager allVideoLinearLayoutManager1 = new LinearLayoutManager(context);
            allVideoLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvHealthTvAllVideosIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
            binding.rvHealthTvAllVideosIndicator.setLayoutManager(allVideoLinearLayoutManager1);
            binding.rvHealthTvAllVideosIndicator.setHasFixedSize(true);

            binding.rvHealthTvAllVidoes.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (allVideoLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionHealthTv = allVideoLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionHealthTv = allVideoLinearLayoutManager.findFirstVisibleItemPosition();
                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(positionHealthTv);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("HappyYou", "Error occurred", e);
        }
    }

    private void setHealthTvBookmarkData(List<GetDashboardDataResponse.Data.HealthTv> healthTvList) {
        if (healthTvList.isEmpty()) {
            binding.rlHealthTvbookmark.setVisibility(View.GONE);
            binding.rvHealthTvbookmark.setVisibility(View.GONE);
            binding.rvHealthTvbookmarkIndicator.setVisibility(View.GONE);
        } else {
            binding.rlHealthTvbookmark.setVisibility(View.VISIBLE);
            binding.rvHealthTvbookmark.setVisibility(View.VISIBLE);
            binding.rvHealthTvbookmarkIndicator.setVisibility(View.VISIBLE);
            List<GetDashboardDataResponse.Data.HealthTv> dataList = new ArrayList<>();
            if (healthTvList.size() > 6) {
                for (int i = 0; i < 6; i++) {
                    dataList.add(healthTvList.get(i));
                }
            } else {
                dataList.addAll(healthTvList);
            }
            HealthHacksHealthTvAdapter absorbHealthTvAdapter = new HealthHacksHealthTvAdapter(context, dataList, (id, isBookmark) -> addVideoBookmark(isBookmark, id));
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
            LinearLayoutManager absorbHealthTvLinearLayoutManager = new LinearLayoutManager(context);
            absorbHealthTvLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvHealthTvbookmark.setLayoutManager(absorbHealthTvLinearLayoutManager);
            binding.rvHealthTvbookmark.setAdapter(absorbHealthTvAdapter);
            binding.rvHealthTvbookmark.setOnFlingListener(null);


            int indicatorSize;

            if (healthTvList.size() > 6) {
                indicatorSize = (int) Math.ceil(6.0 / 2.0);
            } else {
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(healthTvList.size())) / 2.0);
            }

            if (indicatorSize > 1) {
                binding.rvHealthTvbookmarkIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.rvHealthTvbookmarkIndicator.setVisibility(View.INVISIBLE);
            }

            LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(context);
            absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvHealthTvbookmarkIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
            binding.rvHealthTvbookmarkIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
            binding.rvHealthTvbookmarkIndicator.setHasFixedSize(true);

            binding.rvHealthTvbookmark.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionHealthTvbookmark = absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionHealthTvbookmark = absorbHealthTvLinearLayoutManager.findFirstVisibleItemPosition();
                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(positionHealthTvbookmark);
                    }
                }
            });
        }

    }


    private void setHealthTvRecommendData(List<GetDashboardDataResponse.Data.HealthTv> healthTvList) {
        if (healthTvList.isEmpty()) {
            binding.llRecommend.setVisibility(View.GONE);
        } else {
            binding.llRecommend.setVisibility(View.VISIBLE);
            List<GetDashboardDataResponse.Data.HealthTv> dataList = new ArrayList<>();
            if (healthTvList.size() > 6) {
                for (int i = 0; i < 6; i++) {
                    dataList.add(healthTvList.get(i));
                }
            } else {
                dataList.addAll(healthTvList);
            }
            HealthHacksHealthTvAdapter absorbHealthTvAdapter = new HealthHacksHealthTvAdapter(context, dataList, (id, isBookmark) -> addVideoBookmark(isBookmark, id));
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
            LinearLayoutManager absorbHealthTvLinearLayoutManager = new LinearLayoutManager(context);
            absorbHealthTvLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvHealthTvRecommend.setLayoutManager(absorbHealthTvLinearLayoutManager);
            binding.rvHealthTvRecommend.setAdapter(absorbHealthTvAdapter);
            binding.rvHealthTvRecommend.setOnFlingListener(null);

            LinearSnapHelper absorbHealthTvLinearSnapHelper = new SnapHelperOneByOne();
            absorbHealthTvLinearSnapHelper.attachToRecyclerView(binding.rvHealthTvRecommend);


            int indicatorSize;

            if (healthTvList.size() > 6) {
                indicatorSize = (int) Math.ceil(6.0 / 2.0);
            } else {
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(healthTvList.size())) / 2.0);
            }

            if (indicatorSize > 1) {
                binding.rvHealthTvRecommendIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.rvHealthTvRecommendIndicator.setVisibility(View.INVISIBLE);
            }

            LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(context);
            absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvHealthTvRecommendIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
            binding.rvHealthTvRecommendIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
            binding.rvHealthTvRecommendIndicator.setHasFixedSize(true);

            binding.rvHealthTvRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionHealthTvrvHealthTvRecommend = absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionHealthTvrvHealthTvRecommend = absorbHealthTvLinearLayoutManager.findFirstVisibleItemPosition();
                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(positionHealthTvrvHealthTvRecommend);
                    }
                }
            });
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
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                finish();
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.code() == 200 && response.body() != null) {
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    HealthTVDashboard.isPlayedFull = false;
                    if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                        runOnUiThread(() -> {
                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                            }

                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                            }

                            showRewardsPopupDialogBox();
                        });
                        finish();
                    } else
                        finish();
                }
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

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(alertDialog::dismiss, 3000);

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> alertDialog.dismiss());

        binding.scratchView.onFullReveal();


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(HealthTvListingActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i >= 20) {
            scratchCardLayout.onFullReveal();
            if (alertDialogBonusRewards != null && alertDialogBonusRewards.isShowing()) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> alertDialogBonusRewards.dismiss(), 5000);
            }
        }
    }

    @Override
    public void onScratchStarted() {

    }


    private void showRewardsPopupDialogBox() {
        if (!NewDashboardHelper.Companion.getPopUpShowModels().isEmpty()) {
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

    @SuppressLint("UseCompatLoadingForDrawables")
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
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialogBonusRewards.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212 && HealthTVDashboard.isPlayedFull) {
            updateRewards();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(BookmarkhealthtvEvent event) {
        refreshAdapter(event.data, event.isbookmarked);
    }

    private void refreshAdapter(GetDashboardDataResponse.Data.HealthTv data, boolean isbookmarked) {
        if (isbookmarked) {
            checkdataandadd(data);
        } else {
            checkdataandremove(data);
        }
    }

    private void checkdataandadd(GetDashboardDataResponse.Data.HealthTv data) {
        boolean issetdata = (healthTvListbookmarked.isEmpty());
        for (GetDashboardDataResponse.Data.HealthTv datatv : healthTvList) {
            if (Objects.equals(data.getId(), datatv.getId())) {
                healthTvListbookmarked.add(data);
            }
        }
        if (issetdata) {
            setHealthTvBookmarkData(healthTvListbookmarked);
        }
    }

    private void checkdataandremove(GetDashboardDataResponse.Data.HealthTv data) {
        boolean issetdata = (healthTvListbookmarked.isEmpty());
        for (GetDashboardDataResponse.Data.HealthTv datatv : healthTvList) {
            if (Objects.equals(data.getId(), datatv.getId())) {
                healthTvListbookmarked.remove(datatv);

            }
        }
        if (issetdata) {
            setHealthTvBookmarkData(healthTvListbookmarked);
        }
    }

    private void addVideoBookmark(boolean isBookmark, int id) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

        if (!progressDialog.isShowing())
            progressDialog.show();
        VideoBookmarkRequest request = new VideoBookmarkRequest(id, "video", isBookmark);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.addBookmarkVideo(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_success));

                    Log.d("BookMark", new Gson().toJson(response.body()));
                    if (isBookmark) {
                        Toast.makeText(context, "Successfully added to Bookmark", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Remove from Bookmark", Toast.LENGTH_SHORT).show();
                    }

                    getAbsorbDashboard(tagname);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_failed));
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_failed));
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAbsorbDashboard(String tagName) {
        this.tagname = tagName;
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        GetDashboardDataRequest dashboardDataRequest = new GetDashboardDataRequest(tagName, "");
        Call<GetDashboardDataResponse> call = apiInterfaceWyh.getAbsorbDashboard(SharedPref.getAuthToken(), dashboardDataRequest);
        //Log.v("Url_Request_save", call.request().url() + "\n" + new Gson().toJson(dashboardDataRequest) + "\n" + SharedPref.getAuthToken());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetDashboardDataResponse> call, @NonNull Response<GetDashboardDataResponse> response) {

                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    if (response.body().getData() != null) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_dashboard_success));
                        healthTvList = response.body().getData().getHealthTv();
                        healthTvListbookmarked = getBookmarkedlist(healthTvList);
                        List<GetDashboardDataResponse.Data.HealthTv> isBookmarkRecommend = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.HealthTv> nonBookMarkRecommend = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.HealthTv> tempRecommend = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.HealthTv> isBookmark = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.HealthTv> nonBookMark = new ArrayList<>();
                        List<GetDashboardDataResponse.Data.HealthTv> temp = new ArrayList<>();
                        allVideosList = response.body().getData().getAllvideos();
                        allBlogsList = response.body().getData().getAllBlogs();
                        healthTvListRecommend = response.body().getData().getRecommendedVideos();

                        for (GetDashboardDataResponse.Data.HealthTv item : healthTvList) {
                            if (item.getIsBookMarked() == 1) {
                                isBookmark.add(item);
                            } else {
                                nonBookMark.add(item);
                            }
                        }

                        temp.addAll(isBookmark);
                        temp.addAll(nonBookMark);
                        healthTvList.clear();
                        healthTvList = temp;

                        allVideos = response.body().getData().getAllvideos();

                        for (GetDashboardDataResponse.Data.HealthTv item : healthTvListRecommend) {
                            if (item.getIsBookMarked() == 1) {
                                isBookmarkRecommend.add(item);
                            } else {
                                nonBookMarkRecommend.add(item);
                            }
                        }

                        tempRecommend.addAll(isBookmarkRecommend);
                        tempRecommend.addAll(nonBookMarkRecommend);
                        healthTvListRecommend.clear();
                        healthTvListRecommend = tempRecommend;

                        if (!healthTvList.isEmpty()) {
                            binding.llAll.setVisibility(View.VISIBLE);
                            setHealthTvData(healthTvList);
                        } else {
                            binding.llAll.setVisibility(View.GONE);
                        }

                        if (!healthTvListbookmarked.isEmpty()) {
                            binding.llBookmark.setVisibility(View.VISIBLE);
                            setHealthTvBookmarkData(healthTvListbookmarked);
                        } else {
                            binding.llBookmark.setVisibility(View.GONE);
                        }

                        if (!healthTvListRecommend.isEmpty()) {
                            binding.llRecommend.setVisibility(View.VISIBLE);
                            setHealthTvRecommendData(healthTvListRecommend);
                        } else {
                            binding.llRecommend.setVisibility(View.GONE);
                        }

                        if (!allVideos.isEmpty()) {
                            binding.llAllVideos.setVisibility(View.VISIBLE);
                            setAllVideos(allVideos);
                        } else {
                            binding.llAllVideos.setVisibility(View.GONE);
                        }

                        if (!selectedSearchText.isEmpty()) {
                            setSearchData(selectedSearchText);
                        }

                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_dashboard_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetDashboardDataResponse> call, @NonNull Throwable t) {
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_dashboard_failed));
            }
        });
    }

    private void setTagNameData(List<GetDashboardDataResponse.Data.TagName> tagNameList) {
        HealthHacksTagsAdapter absorbQuickReadsAdapter = new HealthHacksTagsAdapter(context, tagNameList, tagname, selectedItem);
        LinearLayoutManager absorbLinearLayoutManager = new LinearLayoutManager(context);
        absorbLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvTags.setOnFlingListener(null);
        binding.rvTags.setLayoutManager(absorbLinearLayoutManager);
        binding.rvTags.setAdapter(absorbQuickReadsAdapter);

        LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvTags);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAbsorbDashboard(tagname);
    }


}