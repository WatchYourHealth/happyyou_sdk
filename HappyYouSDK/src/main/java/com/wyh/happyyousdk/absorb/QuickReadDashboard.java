package com.wyh.happyyousdk.absorb;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.absorb.adapter.AllBlogsAdapter;
import com.wyh.happyyousdk.absorb.adapter.HealthHacksTagsAdapter;
import com.wyh.happyyousdk.absorb.adapter.BlogListAdapter;
import com.wyh.happyyousdk.absorb.adapter.BookmarkBlogListAdapter;
import com.wyh.happyyousdk.absorb.adapter.MostReadAdapter;
import com.wyh.happyyousdk.absorb.adapter.SearchListAdapter;
import com.wyh.happyyousdk.absorb.adapter.TrendingAdapter;
import com.wyh.happyyousdk.absorb.adapter.TribeBlogListAdapter;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.model.request.absorb.AddBookmarkRequest;
import com.wyh.happyyousdk.model.request.absorb.GetQuickReadRequest;
import com.wyh.happyyousdk.model.response.absorb.AddBookmarkResponse;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.model.response.absorb.GetQuickReadResponse;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;
import com.wyh.happyyousdk.databinding.ActivityQuickReadDashboardBinding;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickReadDashboard extends AppCompatActivity implements SearchKeyClick{
    ActivityQuickReadDashboardBinding binding;
    Context context;

    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    String tagname = "";
    int selectedItem = 0;

    List<GetQuickReadResponse.Data.TrendingBlog> trendingBlogList;
    List<GetQuickReadResponse.Data.TribeBlog> tribeBlogList, bookmarksList;
    List<GetQuickReadResponse.Data.MostRead> mostReadList;
    List<GetQuickReadResponse.Data.Blog> blogList;

    List<GetQuickReadResponse.Data.AllBlogs> allBlogsList;
    Boolean emptySearch = true;
    String selectedSearchText = "";

    ArrayList<String> searchKeyFilter = new ArrayList<>();
    ArrayList<String> titleKeyFilter = new ArrayList<>();
    ArrayList<String> quickReadMainFilterList = new ArrayList<>();
    ArrayList<GetQuickReadResponse.Data.AllBlogs> searchAllBlogs = new ArrayList<>();
    SearchListAdapter searchListAdapter;
    List<GetDashboardDataResponse.Data.TagName> tagList;



    int positionTrending = 0,  positionBookmark = 0, positionTribeBlogs = 0, positionBlogs = 0, positionMostRead = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quick_read_dashboard);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.la404Bear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_404.json");

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Quick Reads");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tagList = (List<GetDashboardDataResponse.Data.TagName>) extras.getSerializable("taglist");
            tagname = extras.getString("tagName");
            selectedItem = extras.getInt("selectedItem", 0);
            setTagNameData(tagList);
        }
        if(tagname == null && tagList != null && !tagList.isEmpty()){
            tagname = tagList.get(0).getTagName();
        }

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "QuickReadDashboard");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        


        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.rlTrending.setOnClickListener(view -> {
            Intent intent = new Intent(this, MoreQuickReadActivity.class);
            intent.putExtra("data", new Gson().toJson(trendingBlogList));
            intent.putExtra("title", "Trending");
            startActivity(intent);
        });

        binding.rlMostRead.setOnClickListener(view -> {
            Intent intent = new Intent(this, MoreQuickReadActivity.class);
            intent.putExtra("data", new Gson().toJson(mostReadList));
            intent.putExtra("title", "Most Read");
            startActivity(intent);
        });

        binding.rlBlogs.setOnClickListener(view -> {
            Intent intent = new Intent(this, MoreQuickReadActivity.class);
            intent.putExtra("data", new Gson().toJson(blogList));
            intent.putExtra("title", "Blogs");
            startActivity(intent);
        });

        binding.rlTribeBlogs.setOnClickListener(view -> {
            Intent intent = new Intent(this, MoreQuickReadActivity.class);
            intent.putExtra("data", new Gson().toJson(tribeBlogList));
            intent.putExtra("title", "Tribe Recommended");
            startActivity(intent);
        });

        binding.rlBookmark.setOnClickListener(view -> {
            Intent intent = new Intent(this, MoreQuickReadActivity.class);
            intent.putExtra("data", new Gson().toJson(bookmarksList));
            intent.putExtra("title", "Bookmarks");
            startActivity(intent);
        });

        binding.rlAllBlogs.setOnClickListener(view -> {
            Intent intent = new Intent(QuickReadDashboard.this, MoreQuickReadActivity.class);
            intent.putExtra("data", new Gson().toJson(allBlogsList));
            intent.putExtra("title", "All Blogs");
            startActivity(intent);
        });

        binding.quickReadSearchImg.setOnClickListener(view -> {
            if(binding.quickReadSearchLayout.getVisibility() == View.INVISIBLE){
                binding.quickReadSearchLayout.setVisibility(View.VISIBLE);
            }else{
                binding.quickReadSearchLayout.setVisibility(View.INVISIBLE);
            }
        });

        binding.quickReadCancel.setOnClickListener(view -> binding.quickReadSearchEt.setText(""));

        binding.quickReadSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if((long) charSequence.length() == 0){
                    binding.quickReadCancel.setVisibility(View.GONE);
                    binding.quickReadViewCard.setVisibility(View.GONE);
                    selectedSearchText = "";
                    if(emptySearch){
                        emptySearch = false;
                        getQuickReadDashboardAPI(tagname);
                    }
                }else if(selectedSearchText.equalsIgnoreCase(charSequence.toString())){
                    binding.quickReadViewCard.setVisibility(View.GONE);
                }
                else{
                    if((long) charSequence.length() >= 3){
                        emptySearch = true;
                        binding.quickReadCancel.setVisibility(View.VISIBLE);
                        binding.quickReadViewCard.setVisibility(View.VISIBLE);
                        searchItem(charSequence.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        getQuickReadDashboardAPI(tagname);
    }

    public void searchItem(String searchKey){
        try{
            searchKeyFilter.clear();
            titleKeyFilter.clear();
            quickReadMainFilterList.clear();
            searchAllBlogs.clear();


            for(int i =0; i < allBlogsList.size(); i++){
                if(allBlogsList.get(i).getArticleName().toLowerCase().contains(searchKey)){
                    if(allBlogsList.get(i).getArticleName().contains(",")){
                        searchKeyFilter.addAll(Arrays.asList(allBlogsList.get(i).getArticleName().split(",")));
                    }
                    searchKeyFilter.add(allBlogsList.get(i).getArticleName());
                    searchAllBlogs.add(allBlogsList.get(i));
                }
                /*if(allBlogsList.get(i).getArticleName().toLowerCase().contains(searchKey)){
                    titleKeyFilter.add(allBlogsList.get(i).getArticleName());
                    searchAllBlogs.add(allBlogsList.get(i));
                }*/
            }

            for (int k =0; k < searchKeyFilter.size(); k++){
                if(searchKeyFilter.get(k).contains(",")){
                    searchKeyFilter.remove(k);
                }else{
                    if(searchKeyFilter.get(k).toLowerCase().contains(searchKey.toLowerCase()) && !quickReadMainFilterList.contains(searchKeyFilter.get(k))){
                        quickReadMainFilterList.add(searchKeyFilter.get(k));
                    }
                }
            }

            quickReadMainFilterList.addAll(titleKeyFilter);


            if(!quickReadMainFilterList.isEmpty()) {
                binding.quickReadViewCard.setVisibility(View.VISIBLE);
                binding.searchListingList.setLayoutManager(new LinearLayoutManager(this));
                binding.searchListingList.hasFixedSize();
                searchListAdapter = new SearchListAdapter(this, quickReadMainFilterList, this);
                binding.searchListingList.setAdapter(searchListAdapter);
            } else {
                binding.quickReadViewCard.setVisibility(View.GONE);
                //this.searchKey = searchKey;
                //addSearchHistory();
            }
        }catch (Exception e){
            Log.e("HappyYou", "Error occurred", e);
        }
    }

    @Override
    public void onSearchClick(String searchKey) {
        selectedSearchText = searchKey;
        searchLogic(searchKey);
    }

    private void searchLogic(String searchKey){

        binding.quickReadViewCard.setVisibility(View.GONE);
        selectedSearchText = searchKey;
        binding.quickReadSearchEt.setText(searchKey);


        List<GetQuickReadResponse.Data.TrendingBlog> trendingBlogs = new ArrayList<>();
        List<GetQuickReadResponse.Data.TribeBlog> tribeBlogs = new ArrayList<>();
        List<GetQuickReadResponse.Data.TribeBlog> bookMark = new ArrayList<>();
        List<GetQuickReadResponse.Data.MostRead> mostRead = new ArrayList<>();
        List<GetQuickReadResponse.Data.AllBlogs> allBlogs = new ArrayList<>();


        for(int i=0; i < trendingBlogList.size(); i++){
            if(trendingBlogList.get(i).getArticleName().toLowerCase().contains(searchKey.toLowerCase()) || trendingBlogList.get(i).getTags().toLowerCase().contains(searchKey.toLowerCase())){
                trendingBlogs.add(trendingBlogList.get(i));
            }
        }
        for(int i=0; i < tribeBlogList.size(); i++){
            if(tribeBlogList.get(i).getArticleName().toLowerCase().contains(searchKey.toLowerCase()) || tribeBlogList.get(i).getTags().toLowerCase().contains(searchKey.toLowerCase())){
                tribeBlogs.add(tribeBlogList.get(i));
            }
        }for(int i=0; i < mostReadList.size(); i++){
            if(mostReadList.get(i).getArticleName().toLowerCase().contains(searchKey.toLowerCase()) || mostReadList.get(i).getTags().toLowerCase().contains(searchKey.toLowerCase())){
                mostRead.add(mostReadList.get(i));
            }
        }for(int i=0; i < allBlogsList.size(); i++){
            if(allBlogsList.get(i).getArticleName().toLowerCase().contains(searchKey.toLowerCase())){
                allBlogs.add(allBlogsList.get(i));
            }
        }for(int i=0; i < bookmarksList.size(); i++){
            if(bookmarksList.get(i).getArticleName().toLowerCase().contains(searchKey.toLowerCase()) || bookmarksList.get(i).getTags().toLowerCase().contains(searchKey.toLowerCase())){
                bookMark.add(bookmarksList.get(i));
            }
        }

        setTrendingBlogsData(trendingBlogs);
        setBookmarksBlogList(bookMark);
        setTribeBlogList(tribeBlogs);
        setMostReadBlogsData(mostRead);
        setAllBlogs(allBlogs);

    }

    private void setTagNameData(List<GetDashboardDataResponse.Data.TagName> tagNameList) {
        HealthHacksTagsAdapter absorbQuickReadsAdapter = new HealthHacksTagsAdapter(context, tagNameList,tagname, selectedItem);
        LinearLayoutManager absorbLinearLayoutManager = new LinearLayoutManager(context);
        absorbLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvTags.setOnFlingListener(null);
        binding.rvTags.setLayoutManager(absorbLinearLayoutManager);
        binding.rvTags.setAdapter(absorbQuickReadsAdapter);

        LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvTags);
    }


    public void getQuickReadDashboardAPI(String tagName) {
        this.tagname = tagName;
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        if (tagName.equalsIgnoreCase("ALL"))
        {
            tagName="";
        }
        GetQuickReadRequest getQuickReadRequest = new GetQuickReadRequest("Quick reads", tagName, "",  0);
        Call<GetQuickReadResponse> call = apiInterfaceWyh.getQuickReadDashboard(SharedPref.getAuthToken(), getQuickReadRequest);
        Log.v("Url_Request_save", call.request().url() + "\n" + new Gson().toJson(getQuickReadRequest) + "\n" + SharedPref.getAuthToken());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetQuickReadResponse> call, @NonNull Response<GetQuickReadResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_all_items_success));
                    mostReadList = response.body().getData().getMostRead();
                    trendingBlogList = response.body().getData().getTrendingBlogs();
                    blogList = response.body().getData().getBlogs();
                    tribeBlogList = response.body().getData().getTribeBlogs();
                    bookmarksList = response.body().getData().getBookMarkBlogs();
                    allBlogsList = response.body().getData().getAllBlogs();


                    if (!trendingBlogList.isEmpty()) {
                        Collections.shuffle(trendingBlogList);
                        binding.llTrending.setVisibility(View.VISIBLE);
                        setTrendingBlogsData(trendingBlogList);
                    } else {
                        binding.llTrending.setVisibility(View.GONE);
                    }
                    if (!mostReadList.isEmpty()) {
                        Collections.shuffle(mostReadList);
                        binding.llMostRead.setVisibility(View.VISIBLE);
                        setMostReadBlogsData(mostReadList);
                    } else {
                        binding.llMostRead.setVisibility(View.GONE);
                    }
                    if (!blogList.isEmpty()) {
                        Collections.shuffle(blogList);
                        binding.llBlogs.setVisibility(View.VISIBLE);
                        setBlogList(blogList);
                    } else {
                        binding.llBlogs.setVisibility(View.GONE);
                    }
                    if (!tribeBlogList.isEmpty()) {
                        /*tribeBlogList = get
                        Collections.shuffle(tribeBlogList);*/
                        List<GetQuickReadResponse.Data.TribeBlog> isBookmark = new ArrayList<>();
                        List<GetQuickReadResponse.Data.TribeBlog> nonBookMark = new ArrayList<>();
                        List<GetQuickReadResponse.Data.TribeBlog> temp = new ArrayList<>();
                        for (GetQuickReadResponse.Data.TribeBlog item : tribeBlogList) {
                            if (item.getIsBookMarked() == 1) {
                                isBookmark.add(item);
                            } else {
                                nonBookMark.add(item);
                            }
                        }
                        temp.addAll(isBookmark);
                        temp.addAll(nonBookMark);
                        tribeBlogList.clear();
                        tribeBlogList = temp;
                        binding.llTribeBlogs.setVisibility(View.VISIBLE);
                        setTribeBlogList(tribeBlogList);
                    } else {
                        binding.llTribeBlogs.setVisibility(View.GONE);
                    }
                    if (trendingBlogList.isEmpty() && mostReadList.isEmpty() && blogList.isEmpty() && tribeBlogList.isEmpty() && allBlogsList.isEmpty()) {
                        binding.llNoRecordFound.setVisibility(View.VISIBLE);
                    } else {
                        binding.llNoRecordFound.setVisibility(View.GONE);
                    }
                    if (!bookmarksList.isEmpty()) {
                        binding.llBookmark.setVisibility(View.VISIBLE);
                        setBookmarksBlogList(bookmarksList);
                    } else {
                        binding.llBookmark.setVisibility(View.GONE);
                    }

                    if (!allBlogsList.isEmpty()) {
                        setAllBlogs(allBlogsList);
                    } else {
                        binding.llAllBlogs.setVisibility(View.GONE);
                    }

                    if (!selectedSearchText.equalsIgnoreCase("")) {
                        searchLogic(selectedSearchText);
                    }

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_all_items_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetQuickReadResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_all_items_failed));
            }
        });
    }

    private void setTribeBlogList(List<GetQuickReadResponse.Data.TribeBlog> tribeBlogList) {
        if(tribeBlogList.isEmpty()){
            binding.llTribeBlogs.setVisibility(View.GONE);
        }else{
            binding.llTribeBlogs.setVisibility(View.VISIBLE);
            List<GetQuickReadResponse.Data.TribeBlog> dataList = new ArrayList<>();
            if(tribeBlogList.size() > 6){
                for(int i = 0; i < 6; i++){
                    dataList.add(tribeBlogList.get(i));
                }
            }else{
                dataList.addAll(tribeBlogList);
            }

            TribeBlogListAdapter tribeBlogListAdapter = new TribeBlogListAdapter(context, dataList, this::addBookmark);
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
            LinearLayoutManager tribeBlogLinearLayoutManager = new LinearLayoutManager(context);
            tribeBlogLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvTribeBlogs.setLayoutManager(tribeBlogLinearLayoutManager);
            binding.rvTribeBlogs.setAdapter(tribeBlogListAdapter);
            binding.rvTribeBlogs.setOnFlingListener(null);

            LinearSnapHelper tribeBlogLinearSnapHelper = new SnapHelperOneByOne();
            tribeBlogLinearSnapHelper.attachToRecyclerView(binding.rvTribeBlogs);

            int indicatorSize;

            if(tribeBlogList.size() > 6){
                binding.ivTribeRecommendedMore.setVisibility(View.VISIBLE);
                binding.rlTribeBlogs.setEnabled(true);
                indicatorSize = (int) Math.ceil(6.0 / 2.0);
            }else{
                binding.ivTribeRecommendedMore.setVisibility(View.GONE);
                binding.rlTribeBlogs.setEnabled(false);
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(tribeBlogList.size())) / 2.0);
            }

            if(indicatorSize > 1){
                binding.rvTribeBlogsIndicator.setVisibility(View.VISIBLE);
            }else{
                binding.rvTribeBlogsIndicator.setVisibility(View.INVISIBLE);
            }

            LinearLayoutManager tribeBlogLinearLayoutManager1 = new LinearLayoutManager(context);
            tribeBlogLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            IndicatorsAdapter tribeBlogIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvTribeBlogsIndicator.setAdapter(tribeBlogIndicatorsAdapter);
            binding.rvTribeBlogsIndicator.setLayoutManager(tribeBlogLinearLayoutManager1);
            binding.rvTribeBlogsIndicator.setHasFixedSize(true);

            binding.rvTribeBlogs.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (tribeBlogLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionTribeBlogs = tribeBlogLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionTribeBlogs = tribeBlogLinearLayoutManager.findFirstVisibleItemPosition();
                        tribeBlogIndicatorsAdapter.updateSelectedIndex(positionTribeBlogs);
                    }
                }
            });
        }

    }

    private void setAllBlogs(List<GetQuickReadResponse.Data.AllBlogs> allBlogsList){
        try{
            if(allBlogsList.isEmpty()){
                binding.llAllBlogs.setVisibility(View.GONE);
            }else{
                binding.llAllBlogs.setVisibility(View.VISIBLE);
                List<GetQuickReadResponse.Data.AllBlogs> dataList = new ArrayList<>();
                if(allBlogsList.size() > 6){
                    for(int i = 0; i < 6; i++){
                        dataList.add(allBlogsList.get(i));
                    }
                }else{
                    dataList.addAll(allBlogsList);
                }

                AllBlogsAdapter allBlogsAdapter = new AllBlogsAdapter(context,dataList, this::addBookmark);

                LinearLayoutManager allBlogsLinearLayoutManager = new LinearLayoutManager(context);
                allBlogsLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                binding.rvAllBlogs.setLayoutManager(allBlogsLinearLayoutManager);
                binding.rvAllBlogs.setAdapter(allBlogsAdapter);
                binding.rvAllBlogs.setOnFlingListener(null);

                LinearSnapHelper tribeBlogLinearSnapHelper = new SnapHelperOneByOne();
                tribeBlogLinearSnapHelper.attachToRecyclerView(binding.rvAllBlogs);

                int indicatorSize;

                if(allBlogsList.size() > 6){
                    binding.ivAllBlogsMore.setVisibility(View.VISIBLE);
                    binding.rlAllBlogs.setEnabled(true);
                    indicatorSize = (int) Math.ceil(6.0 / 2.0);
                }else{
                    binding.ivAllBlogsMore.setVisibility(View.GONE);
                    binding.rlAllBlogs.setEnabled(false);
                    indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(allBlogsList.size())) / 2.0);
                }

                if(indicatorSize > 1){
                    binding.rvAllBlogsIndicator.setVisibility(View.VISIBLE);
                }else{
                    binding.rvAllBlogsIndicator.setVisibility(View.INVISIBLE);
                }

                LinearLayoutManager allBlogsLinearLayoutManager1 = new LinearLayoutManager(context);
                allBlogsLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
                IndicatorsAdapter allBlogsIndicatorAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
                binding.rvAllBlogsIndicator.setAdapter(allBlogsIndicatorAdapter);
                binding.rvAllBlogsIndicator.setLayoutManager(allBlogsLinearLayoutManager1);
                binding.rvAllBlogsIndicator.setHasFixedSize(true);

                binding.rvAllBlogs.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            if (allBlogsLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                positionTribeBlogs = allBlogsLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                            } else
                                positionTribeBlogs = allBlogsLinearLayoutManager.findFirstVisibleItemPosition();
                            allBlogsIndicatorAdapter.updateSelectedIndex(positionTribeBlogs);
                        }
                    }
                });


            }

        }catch (Exception e){
            Log.e("HappyYou", "Error occurred", e);
        }
    }

    private void setBlogList(List<GetQuickReadResponse.Data.Blog> blogList) {

        List<GetQuickReadResponse.Data.Blog> dataList = new ArrayList<>();
        if(blogList.size() > 6){
            for(int i = 0; i < 6; i++){
                dataList.add(blogList.get(i));
            }
        }else{
            dataList.addAll(blogList);
        }

        BlogListAdapter BlogListAdapter = new BlogListAdapter(context, dataList, this::addBookmark);
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
        LinearLayoutManager BlogLinearLayoutManager = new LinearLayoutManager(context);
        BlogLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvBlogs.setLayoutManager(BlogLinearLayoutManager);
        binding.rvBlogs.setAdapter(BlogListAdapter);
        binding.rvBlogs.setOnFlingListener(null);

        LinearSnapHelper blogLinearSnapHelper = new SnapHelperOneByOne();
        blogLinearSnapHelper.attachToRecyclerView(binding.rvBlogs);

        int indicatorSize;

        if(blogList.size() > 6){
            binding.ivBlogsMore.setVisibility(View.VISIBLE);
            binding.rlBlogs.setEnabled(true);
            indicatorSize = (int) Math.ceil(6.0 / 2.0);
        }else{
            binding.ivBlogsMore.setVisibility(View.GONE);
            binding.rlBlogs.setEnabled(false);
            indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(blogList.size())) / 2.0);
        }

        if(indicatorSize > 1){
            binding.rvBlogsIndicator.setVisibility(View.VISIBLE);
        }else{
            binding.rvBlogsIndicator.setVisibility(View.INVISIBLE);
        }

        LinearLayoutManager blogLinearLayoutManager1 = new LinearLayoutManager(context);
        blogLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter blogsIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvBlogsIndicator.setAdapter(blogsIndicatorsAdapter);
        binding.rvBlogsIndicator.setLayoutManager(blogLinearLayoutManager1);
        binding.rvBlogsIndicator.setHasFixedSize(true);

        binding.rvBlogs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (BlogLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionBlogs = BlogLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionBlogs = BlogLinearLayoutManager.findFirstVisibleItemPosition();
                    blogsIndicatorsAdapter.updateSelectedIndex(positionBlogs);
                }
            }
        });
    }

    private void setTrendingBlogsData(List<GetQuickReadResponse.Data.TrendingBlog> trendingBlogList) {
        if(trendingBlogList.isEmpty()){
            binding.llTrending.setVisibility(View.GONE);
        }else{
            binding.llTrending.setVisibility(View.VISIBLE);
            List<GetQuickReadResponse.Data.TrendingBlog> dataList = new ArrayList<>();
            if(trendingBlogList.size() > 6){
                for(int i = 0; i < 6; i++){
                    dataList.add(trendingBlogList.get(i));
                }
            }else{
                dataList.addAll(trendingBlogList);
            }

            TrendingAdapter trendingAdapter = new TrendingAdapter(context, dataList, this::addBookmark);
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
            LinearLayoutManager trendingLinearLayoutManager = new LinearLayoutManager(context);
            trendingLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvTrending.setLayoutManager(trendingLinearLayoutManager);
            binding.rvTrending.setAdapter(trendingAdapter);
            binding.rvTrending.setOnFlingListener(null);

            LinearSnapHelper trendingLinearSnapHelper = new SnapHelperOneByOne();
            trendingLinearSnapHelper.attachToRecyclerView(binding.rvTrending);
            int indicatorSize;

            if(trendingBlogList.size() > 6){
                binding.ivTrendingMore.setVisibility(View.VISIBLE);
                binding.rlTrending.setEnabled(true);
                indicatorSize = (int) Math.ceil(6.0 / 2.0);
            }else{
                binding.ivTrendingMore.setVisibility(View.GONE);
                binding.rlTrending.setEnabled(false);
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(trendingBlogList.size())) / 2.0);
            }

            if(indicatorSize > 1){
                binding.rvTrendingIndicator.setVisibility(View.VISIBLE);
            }else{
                binding.rvTrendingIndicator.setVisibility(View.INVISIBLE);
            }

            LinearLayoutManager trendingLinearLayoutManager1 = new LinearLayoutManager(context);
            trendingLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            IndicatorsAdapter trendingIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvTrendingIndicator.setAdapter(trendingIndicatorsAdapter);
            binding.rvTrendingIndicator.setLayoutManager(trendingLinearLayoutManager1);
            binding.rvTrendingIndicator.setHasFixedSize(true);

            binding.rvTrending.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (trendingLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionTrending = trendingLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionTrending = trendingLinearLayoutManager.findFirstVisibleItemPosition();
                        trendingIndicatorsAdapter.updateSelectedIndex(positionTrending);
                    }
                }
            });
        }

    }

    private void setMostReadBlogsData(List<GetQuickReadResponse.Data.MostRead> mostReadList) {

        if(mostReadList.isEmpty()){
            binding.llMostRead.setVisibility(View.GONE);
        }else{
            binding.llMostRead.setVisibility(View.VISIBLE);
            List<GetQuickReadResponse.Data.MostRead> dataList = new ArrayList<>();
            if(mostReadList.size() > 6){
                for(int i = 0; i < 6; i++){
                    dataList.add(mostReadList.get(i));
                }
            }else{
                dataList.addAll(mostReadList);
            }


            MostReadAdapter mostReadAdapter = new MostReadAdapter(context, dataList, this::addBookmark);
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
            LinearLayoutManager mostReadLinearLayoutManager = new LinearLayoutManager(context);
            mostReadLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvMostRead.setLayoutManager(mostReadLinearLayoutManager);
            binding.rvMostRead.setAdapter(mostReadAdapter);
            binding.rvMostRead.setOnFlingListener(null);

            LinearSnapHelper trendingLinearSnapHelper = new SnapHelperOneByOne();
            trendingLinearSnapHelper.attachToRecyclerView(binding.rvMostRead);

            int indicatorSize;

            if(mostReadList.size() > 6){
                indicatorSize = (int) Math.ceil(6.0 / 2.0);
            }else{
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(mostReadList.size())) / 2.0);
            }

            if(indicatorSize > 1){
                binding.ivMostReadMore.setVisibility(View.VISIBLE);
                binding.rlMostRead.setEnabled(true);
                binding.rvMostReadIndicator.setVisibility(View.VISIBLE);
            }else{
                binding.ivMostReadMore.setVisibility(View.GONE);
                binding.rlMostRead.setEnabled(false);
                binding.rvMostReadIndicator.setVisibility(View.INVISIBLE);
            }


            LinearLayoutManager mostReadLinearLayoutManager1 = new LinearLayoutManager(context);
            mostReadLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            IndicatorsAdapter mostReadIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvMostReadIndicator.setAdapter(mostReadIndicatorsAdapter);
            binding.rvMostReadIndicator.setLayoutManager(mostReadLinearLayoutManager1);
            binding.rvMostReadIndicator.setHasFixedSize(true);

            binding.rvMostRead.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (mostReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionMostRead = mostReadLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionMostRead = mostReadLinearLayoutManager.findFirstVisibleItemPosition();
                        mostReadIndicatorsAdapter.updateSelectedIndex(positionMostRead);
                    }
                }
            });
        }

    }

    private void setBookmarksBlogList(List<GetQuickReadResponse.Data.TribeBlog> bookmarks) {
        if(bookmarks.isEmpty()){
            binding.llBookmark.setVisibility(View.GONE);
        }else{
            binding.llBookmark.setVisibility(View.VISIBLE);
            List<GetQuickReadResponse.Data.TribeBlog> dataList = new ArrayList<>();
            if(bookmarks.size() > 6){
                for(int i = 0; i < 6; i++){
                    dataList.add(bookmarks.get(i));
                }
            }else{
                dataList.addAll(bookmarks);
            }

            BookmarkBlogListAdapter tribeBlogListAdapter = new BookmarkBlogListAdapter(context, dataList, this::addBookmark);
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
            LinearLayoutManager bookmarkLinearLayoutManager = new LinearLayoutManager(context);
            bookmarkLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvBookmark.setLayoutManager(bookmarkLinearLayoutManager);
            binding.rvBookmark.setAdapter(tribeBlogListAdapter);
            binding.rvBookmark.setOnFlingListener(null);

            LinearSnapHelper bookmarkLinearSnapHelper = new SnapHelperOneByOne();
            bookmarkLinearSnapHelper.attachToRecyclerView(binding.rvBookmark);

            int indicatorSize;

            if(bookmarks.size() > 6){
                binding.ivBookmarksMore.setVisibility(View.VISIBLE);
                binding.rlBookmark.setEnabled(true);
                indicatorSize = (int) Math.ceil(6.0 / 2.0);
            }else{
                binding.ivBookmarksMore.setVisibility(View.GONE);
                binding.rlBookmark.setEnabled(false);
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(bookmarks.size())) / 2.0);
            }

            if(indicatorSize > 1){
                binding.rvBookmarkIndicator.setVisibility(View.VISIBLE);
            }else{
                binding.rvBookmarkIndicator.setVisibility(View.INVISIBLE);
            }

            LinearLayoutManager bookmarkLinearLayoutManager1 = new LinearLayoutManager(context);
            bookmarkLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            IndicatorsAdapter bookmarkIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvBookmarkIndicator.setAdapter(bookmarkIndicatorsAdapter);
            binding.rvBookmarkIndicator.setLayoutManager(bookmarkLinearLayoutManager1);
            binding.rvBookmarkIndicator.setHasFixedSize(true);

            binding.rvBookmark.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (bookmarkLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionBookmark = bookmarkLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionBookmark = bookmarkLinearLayoutManager.findFirstVisibleItemPosition();
                        bookmarkIndicatorsAdapter.updateSelectedIndex(positionBookmark);
                    }
                }
            });
        }

    }

    private void addBookmark(String articleCode, boolean isBookmark) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AddBookmarkRequest request = new AddBookmarkRequest(articleCode, isBookmark);
        Log.d("request book", new Gson().toJson(request));
        Call<AddBookmarkResponse> call = apiInterfaceWyh.addBookMark(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AddBookmarkResponse> call, @NonNull Response<AddBookmarkResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_success));
                    Log.d("request book", new Gson().toJson(response.body()));
                    if (isBookmark) {
                        Toast.makeText(context, "Successfully added to Bookmark", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Remove from Bookmark", Toast.LENGTH_SHORT).show();
                    }
                    getQuickReadDashboardAPI(tagname);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddBookmarkResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getQuickReadDashboardAPI(tagname);
    }


}