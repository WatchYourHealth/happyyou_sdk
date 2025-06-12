package com.wyh.happyyousdk.fileshare;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;
import com.wyh.happyyousdk.databinding.ActvityFilleShareListBinding;
import com.wyh.happyyousdk.model.response.FileShareResp;
import com.wyh.happyyousdk.play_and_win.PlayAndWinActivity;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyhsdk.sharedPreferences.SharedPreference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFileShareList extends AppCompatActivity {
    ActvityFilleShareListBinding binding;
    Context context;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.actvity_fille_share_list);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);


        apiInterfaceWyh = RetrofitHandler.apiInterface();
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        binding.ivBackPressed.setOnClickListener(v -> {
           onBackPressed();
        });


        GetShareFileList();
    }

    private void GetShareFileList() {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);
            apiInterfaceWyh.GetFilePopup(SharedPref.getAuthToken()).enqueue(new Callback<FileShareResp>() {
                @Override
                public void onResponse(@NonNull Call<FileShareResp> call, @NonNull Response<FileShareResp> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getData() != null) {
                            setData(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FileShareResp> call, @NonNull Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }
    public void setData(FileShareResp rewardResponse) {
        boolean isDataFound = false;
        binding.scrollRewardList.setVisibility(View.VISIBLE);
        binding.tvNDF.setVisibility(View.GONE);
        List<FileShareResp.Data> jsonList = rewardResponse.getData()
                .stream()
                .filter(FileShareResp.Data::getIsExpired)  // Keep only items where isExpired == false
                .collect(Collectors.toList());


        List<FileShareResp.Data> jsonList2 = rewardResponse.getData()
                .stream()
                .filter(item -> !item.getIsExpired())  // Keep only items where isExpired == false
                .collect(Collectors.toList());
        if (jsonList2.size()>3)
        {
            binding.imActive.setVisibility(View.VISIBLE);
        }
        else {
            binding.imActive.setVisibility(View.GONE);
        }
        if (jsonList.size()>3)
        {
            binding.imExpired.setVisibility(View.VISIBLE);
        }
        else {
            binding.imExpired.setVisibility(View.GONE);
        }
        binding.rlActive.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_View_ALL_ACTIVE",context);
            if (jsonList2.size()>3)
            {
                Intent in = new Intent(ActivityFileShareList.this, ActivityFileShareAllList.class);
                in.putExtra("type","Active");
                startActivity(in);
            }

        });
        binding.rlExpired.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_View_ALL_Expired",context);
            if (jsonList.size()>3)
            {
                Intent in = new Intent(ActivityFileShareList.this, ActivityFileShareAllList.class);
                in.putExtra("type","Expired");
                startActivity(in);
            }

        });
        if (!jsonList2.isEmpty()) {
            isDataFound = true;
            binding.rlActive.setVisibility(View.VISIBLE);
            binding.rvGridActive.setVisibility(View.VISIBLE);
            binding.rvGridActiveIndicator.setVisibility(View.VISIBLE);
            int maxLength = jsonList2.size();
            if (jsonList2.size() > 9) {
                maxLength = 9;
            }
            FileShareGridAdapterMain gridCardsAdapter = new FileShareGridAdapterMain(jsonList2.subList(0,maxLength));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            binding.rvGridActive.setLayoutManager(linearLayoutManager);
            binding.rvGridActive.setAdapter(gridCardsAdapter);
            LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
            quickReadLinearSnapHelper.attachToRecyclerView(binding.rvGridActive);
            LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(this);
            absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            int indicatorSize = 1;

            if (maxLength > 3) {
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(maxLength)) / 3.0);
            }

            if (indicatorSize > 1) {
                binding.rvGridActiveIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.rvGridActiveIndicator.setVisibility(View.GONE);
            }
            IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(this, indicatorSize, 0);
            binding.rvGridActiveIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
            binding.rvGridActiveIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
            binding.rvGridActiveIndicator.setHasFixedSize(true);

            binding.rvGridActive.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int position = 0;
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            position = linearLayoutManager.findFirstVisibleItemPosition();
                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(position);
                    }
                }
            });
        } else {
            binding.rlActive.setVisibility(View.GONE);
            binding.rvGridActive.setVisibility(View.GONE);
            binding.rvGridActiveIndicator.setVisibility(View.GONE);
        }

        if (!jsonList.isEmpty()) {
            isDataFound = true;
            binding.rlExpired.setVisibility(View.VISIBLE);
            binding.rvGridExpired.setVisibility(View.VISIBLE);
            int maxLength = jsonList.size();
            if (jsonList.size() > 9) {
                maxLength = 9;
            }
            FileShareGridAdapterMain gridCardsAdapter = new FileShareGridAdapterMain(jsonList.subList(0,maxLength));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            binding.rvGridExpired.setLayoutManager(linearLayoutManager);
            binding.rvGridExpired.setAdapter(gridCardsAdapter);

            LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
            quickReadLinearSnapHelper.attachToRecyclerView(binding.rvGridExpired);
            LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(this);
            absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            int indicatorSize = 1;

            if (maxLength > 3) {
                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(maxLength)) / 3.0);
            }

            if (indicatorSize > 1) {
                binding.rvGridExpiredIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.rvGridExpiredIndicator.setVisibility(View.GONE);
            }
            IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(this, indicatorSize, 0);
            binding.rvGridExpiredIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
            binding.rvGridExpiredIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
            binding.rvGridExpiredIndicator.setHasFixedSize(true);

            binding.rvGridExpired.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int position = 0;
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            position = linearLayoutManager.findFirstVisibleItemPosition();
                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(position);
                    }
                }
            });
        } else {
            binding.rlExpired.setVisibility(View.GONE);
            binding.rvGridExpired.setVisibility(View.GONE);
            binding.rvGridExpiredIndicator.setVisibility(View.GONE);
        }
        if (!isDataFound) {
            binding.scrollRewardList.setVisibility(View.GONE);
            binding.tvNDF.setVisibility(View.VISIBLE);
        }


    }

}
