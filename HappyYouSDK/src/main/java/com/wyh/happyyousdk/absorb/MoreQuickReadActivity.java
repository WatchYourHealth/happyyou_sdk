package com.wyh.happyyousdk.absorb;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.Eventbus.Bookmarkevent;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.absorb.adapter.MoreQuickReadAdapter;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityMoreQuickReadBinding;
import com.wyh.happyyousdk.model.request.absorb.AddBookmarkRequest;
import com.wyh.happyyousdk.model.response.absorb.AddBookmarkResponse;
import com.wyh.happyyousdk.model.response.absorb.GetQuickReadResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreQuickReadActivity extends AppCompatActivity {

    ActivityMoreQuickReadBinding binding;
    List<GetQuickReadResponse.Data.TrendingBlog> dataList;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    MoreQuickReadAdapter moreQuickReadAdapter;
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_more_quick_read);
        context = this;

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_404.json");

        binding.includeToolbar.llBack.setOnClickListener(v->onBackPressed());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String data = extras.getString("data");
            title = extras.getString("title");
            Type type = new TypeToken<List<GetQuickReadResponse.Data.TrendingBlog>>() {
            }.getType();
            dataList = new Gson().fromJson(data, type);

            binding.includeToolbar.tvBack.setText(title);
            setRv();


            // do something with the customer
        }

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


    }

    private void setRv(){

        moreQuickReadAdapter = new MoreQuickReadAdapter(context, dataList, title,new MoreQuickReadAdapter.OnItemClickListener() {
            @Override
            public void onClick(String articleCode, boolean isBookmark, int position) {
                if(Objects.equals(title, "Bookmarks")){
                    addBookmark( articleCode, false, position);
                }else{
                    addBookmark( articleCode, isBookmark, position);
                }
            }

            @Override
            public void onClickItem(int position) {
            Intent intent = new Intent(context, QuickReadActivity.class);
            intent.putExtra("article_code", dataList.get(position).getArticleCode());
            intent.putExtra("dataPosition", position);
            startActivityForResult(intent, 202);
        }
    });
        binding.rvList.setAdapter(moreQuickReadAdapter);
        binding.rvList.setOnFlingListener(null);
}

    private void addBookmark(String articleCode, boolean isBookmark, int position) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AddBookmarkRequest request = new AddBookmarkRequest(articleCode, isBookmark);
        Log.d("request book", new Gson().toJson(request));
        Call<AddBookmarkResponse> call = apiInterfaceWyh.addBookMark(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
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

                    if (Objects.equals(title, "Bookmarks")) {
                        dataList.remove(position);
                        if (dataList.isEmpty()) {
                            finish();
                        }
                    } else {
                        if (isBookmark) {
                            dataList.get(position).setIsBookMarked(1);
                        } else {
                            dataList.get(position).setIsBookMarked(0);
                        }
                    }

                    if (moreQuickReadAdapter != null && !dataList.isEmpty())
                        moreQuickReadAdapter.notifyDataSetChanged();

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddBookmarkResponse> call, Throwable t) {
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
        if(moreQuickReadAdapter != null){
            moreQuickReadAdapter.notifyDataSetChanged();
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
    public void onMessageEvent(Bookmarkevent event) {
      refreshAdapter(event.position);
    }

    public void refreshAdapter(int dataPosition) {
        if(dataPosition != -1){
            int b = dataList.get(dataPosition).getIsBookMarked();
            if(b == 0 ){
                dataList.get(dataPosition).setIsBookMarked(1);
            }else{
                dataList.get(dataPosition).setIsBookMarked(0);
            }
            moreQuickReadAdapter.resetData(dataList);

        }
    }
}