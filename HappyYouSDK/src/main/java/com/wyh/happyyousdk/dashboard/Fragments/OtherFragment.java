package com.wyh.happyyousdk.dashboard.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.ChallangesModule.Activities.ChallangesActivity;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.Activities.spinwheelreward.SpinWheelRewardsActivity;
import com.wyh.happyyousdk.SpinWheel.Utilities.DownloadImage;
import com.wyh.happyyousdk.dashboard.LifestyleActivity;
import com.wyh.happyyousdk.dashboard.adapter.OtherFeatureAdapter;
import com.wyh.happyyousdk.databinding.OthersFragmentLayoutBinding;
import com.wyh.happyyousdk.ehr.EhrActivity;
import com.wyh.happyyousdk.happyMarket.WebinarWebview;
import com.wyh.happyyousdk.model.response.GetQuadrantsResponse;
import com.wyh.happyyousdk.unwind.UnwindActivity;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.wheelview.WheelItem;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherFragment extends Fragment implements OtherFeatureAdapter.FeatureClick {

    OthersFragmentLayoutBinding binding;
    Context context;
    OtherFeatureAdapter otherFeatureAdapter;

    ArrayList<String> featureList = new ArrayList<>();
    String[] feature = new String[]{"Health locker", "Digi coach", "Challenges", "Tribes", "Unwind", "Webinar"};
    APIInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.others_fragment_layout, container, false);
        binding.includeToolbar.ivBack.setVisibility(View.GONE);
        binding.includeToolbar.tvBack.setText("Others");
        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));

        context = getActivity();
        SharedPref.init(context);
        SharedPreference.init(context);
        apiInterface = RetrofitHandler.apiInterface();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        binding.otherFeature.setLayoutManager(gridLayoutManager);

        featureList.addAll(Arrays.asList(feature));

        featureList.add("Spin & Win");


        otherFeatureAdapter = new OtherFeatureAdapter(context, featureList, this::onClick);
        binding.otherFeature.setAdapter(otherFeatureAdapter);


        return binding.getRoot();
    }

    @Override
    public void onClick(String featureName) {
        if (featureName.equalsIgnoreCase("Lifestyle")) {
            startActivity(new Intent(context, LifestyleActivity.class));
        } else if (featureName.equalsIgnoreCase("Health locker")) {
            startActivity(new Intent(context, EhrActivity.class));
        } else if (featureName.equalsIgnoreCase("Digi coach")) {
            Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
        } else if (featureName.equalsIgnoreCase("Challenges")) {
            startActivity(new Intent(context, ChallangesActivity.class));
        } else if (featureName.equalsIgnoreCase("Unwind")) {
            startActivity(new Intent(context, UnwindActivity.class).putExtra("comingFrom", "dashboard"));
        } else if (featureName.equalsIgnoreCase("webinar")) {
            APILogs.INSTANCE.activityTracker("DASHBOARD_OTHER_WEBINAR", context);
            startActivity(new Intent(context, WebinarWebview.class)
                    .putExtra("Url", context.getResources().getString(R.string.webinarURL))
                    .putExtra("comingFrom", "redirection")
                    .putExtra("loginUrl", ""));
        } else if (featureName.equalsIgnoreCase("Spin & Win")) {
            startActivity(new Intent(context, SpinWheelRewardsActivity.class));
            //getQuadrants();
        }
    }

    private void getQuadrants() {
        try {
            CommonUtils.showProgressDialige(context);
            apiInterface.getQuadrant().enqueue(new Callback<GetQuadrantsResponse>() {
                @Override
                public void onResponse(Call<GetQuadrantsResponse> call, Response<GetQuadrantsResponse> response) {
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getData() != null && !response.body().getData().getQuadrantData().isEmpty()) {
                            for (int i = 0; i < response.body().getData().getQuadrantData().size(); i++) {
                                int finalI = i;
                                final Bitmap[] imageBitmap = {null};
                                new DownloadImage(bitmap -> {
                                    if (bitmap != null) {
                                        imageBitmap[0] = bitmap;
                                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap[0], 80, 80, true);

                                        com.wyh.happyyousdk.utils.wheelview.WheelItem wheelItem = new WheelItem(Color.parseColor(response.body().getData().getQuadrantData().get(finalI).getQuadrantColor()),
                                                scaledBitmap, response.body().getData().getQuadrantData().get(finalI).getRewardName());
                                        CommonUtils.mywheelItems.add(wheelItem);

                                        if (CommonUtils.mywheelItems.size() == response.body().getData().getQuadrantData().size()) {
                                            CommonUtils.dismissDialoge();
                                            Log.d("AuthToken", "Data " + response.body().getData().getQuadrantData().size() + "Wheel"
                                                    + CommonUtils.mywheelItems.size());

                                            startActivity(new Intent(context, SpinWheelRewardsActivity.class));
                                        }

                                    } else {
                                        CommonUtils.dismissDialoge();
                                        //Log.e("DownloadError", "Failed to download image.");
                                    }
                                }).execute(response.body().getData().getQuadrantData().get(i).getRewardIcon());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetQuadrantsResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }
}
