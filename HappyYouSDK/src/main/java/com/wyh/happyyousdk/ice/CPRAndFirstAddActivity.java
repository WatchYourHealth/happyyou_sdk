package com.wyh.happyyousdk.ice;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityCprFirstAddBinding;
import com.wyh.happyyousdk.databinding.LayoutLifeStyleWelcomeBinding;
import com.wyh.happyyousdk.ice.adapter.CPRVideosAdapter;
import com.wyh.happyyousdk.ice.adapter.DisasterManagementAdapter;
import com.wyh.happyyousdk.ice.adapter.FetchInjuriesAdapter;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Master;

import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;

import com.wyh.happyyousdk.model.response.ice.FetchCPRDetailsResp;
import com.wyh.happyyousdk.model.response.ice.FetchInjuriesResp;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CPRAndFirstAddActivity extends AppCompatActivity {
    ActivityCprFirstAddBinding binding;
    ProgressDialog progressDialog;
    Context context;
    ApiInterfaceWyh apiInterfaceWyh;
    int positionVideo = 0, positionCurrentInjuries = 0;
    List<String> cprTips = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cpr_first_add);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        Glide.with(context)
                .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "splash_bg.png")
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        binding.llMain.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        fetchCPRDetails();
        fetchInjuries();
        binding.includeBack.llBack.setOnClickListener(view -> finish());
        binding.includeBack.tvBack.setText("ICE (In Case of Emergency)");

        binding.btnSubmit.setOnClickListener(view -> {
            finish();
        });

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.llFirebrigade.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:101"));
            startActivity(intent);
        });
        binding.ivCallHelp.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_call_for_help));
        binding.ivTips.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tips));
        binding.ivFirstAid.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_first_aid));

        LinearSnapHelper linearSnapHelper1 = new SnapHelperOneByOne();
        linearSnapHelper1.attachToRecyclerView(binding.rvInjuries);
    }

    private void fetchInjuries() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<FetchInjuriesResp> call = apiInterfaceWyh.fetchInjury(SharedPref.getAuthToken());
        call.enqueue(new Callback<FetchInjuriesResp>() {
            @Override
            public void onResponse(Call<FetchInjuriesResp> call, Response<FetchInjuriesResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_fetch_injuries_success));
                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        List<FetchInjuriesResp.Datum> allData = response.body().getData();
                        List<FetchInjuriesResp.Datum> fetchInjuries = new ArrayList<>();
                        List<FetchInjuriesResp.Datum> disasterManagement = new ArrayList<>();

                        for (FetchInjuriesResp.Datum item : allData) {
                            if (item.getInjuryName().equalsIgnoreCase("fire guide") || item.getInjuryName().equalsIgnoreCase("Earthquake guide") || item.getInjuryName().equalsIgnoreCase("floods guide")) {
                                disasterManagement.add(item);
                            } else {
                                fetchInjuries.add(item);
                            }
                        }


                        setInjuriesAdapter(fetchInjuries);

//                        binding.rvInjuries.setLayoutManager(new GridLayoutManager(context, 3));

//                        List<FetchInjuriesResp.Datum> fetchInjuries = new FetchInjuriesResp().getData();
//                        List<FetchInjuriesResp.Datum> fetchInjuries = response.body().getData().subList(response.body().getData().size() - 3, response.body().getData().size());

                        /*for (int i = response.body().getData().size(); i >= response.body().getData().size() - 3; i--) {
                            fetchInjuries.add(response.body().getData().get(i));
                        }*/
                        DisasterManagementAdapter disasterManagementAdapter = new DisasterManagementAdapter(context, disasterManagement);
                        binding.rvDisasterManagement.setAdapter(disasterManagementAdapter);
                        binding.rvDisasterManagement.setLayoutManager(new GridLayoutManager(context, 3));
                    } else {
                        Toast.makeText(CPRAndFirstAddActivity.this, "No Injuries data found", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 401) {
                    refreshAuthToken(null);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_fetch_injuries_failed));
                    Toast.makeText(CPRAndFirstAddActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchInjuriesResp> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_fetch_injuries_failed));
                Toast.makeText(CPRAndFirstAddActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setInjuriesAdapter(List<FetchInjuriesResp.Datum> dataList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        FetchInjuriesAdapter fetchInjuriesAdapter = new FetchInjuriesAdapter(context, dataList);
        binding.rvInjuries.setAdapter(fetchInjuriesAdapter);
        binding.rvInjuries.setLayoutManager(linearLayoutManager);

        int size = (dataList.size()) + 4;
        size = (int) Math.ceil(Double.parseDouble(String.valueOf(size)) / 3.0);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter indicatorsAdapter = new IndicatorsAdapter(context, size, 0);
        binding.rvInjuriesIndicators.setAdapter(indicatorsAdapter);
        binding.rvInjuriesIndicators.setLayoutManager(linearLayoutManager1);

        binding.rvInjuries.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionCurrentInjuries = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionCurrentInjuries = linearLayoutManager.findFirstVisibleItemPosition();
                    indicatorsAdapter.updateSelectedIndex(positionCurrentInjuries);
                }
            }
        });

        binding.tvKnowMore.setOnClickListener(view -> {
            showCPRTipsPopUp();
        });

    }

    private void showCPRTipsPopUp() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
        LayoutLifeStyleWelcomeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_life_style_welcome, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        binding.content.setVisibility(View.GONE);
        binding.tvContent.loadData(getResources().getString(R.string.cpr_tips_all), "text/html", "utf-8");

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.load(Html.fromHtml(getString(R.string.cpr_tips_all), FROM_HTML_MODE_LEGACY));
        } else {
            binding.tvContent.setText(Html.fromHtml(getString(R.string.cpr_tips_all)));
        }*/

        binding.tvTitle.setText("Tips");

        binding.btnOK.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.7f));
    }

    private void fetchCPRDetails() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<FetchCPRDetailsResp> call = apiInterfaceWyh.fetchCPRDetails(SharedPref.getAuthToken());
        call.enqueue(new Callback<FetchCPRDetailsResp>() {
            @Override
            public void onResponse(Call<FetchCPRDetailsResp> call, Response<FetchCPRDetailsResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_fetch_cpr_details_success));
                    if (response.body().getData() != null && response.body().getData().getCprTips().size() > 0) {
                        cprTips = response.body().getData().getCprTips();
                        binding.tvTip.setText(cprTips.get(0).replace("\n", " "));
                        CPRVideosAdapter cPRVideosAdapter = new CPRVideosAdapter(context, response.body());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        binding.rvCPRVideo.setAdapter(cPRVideosAdapter);
                        binding.rvCPRVideo.setLayoutManager(linearLayoutManager);

                        List<FetchCPRDetailsResp.IceBePrepared> iceBePrepared = response.body().getData().getIceBePrepared();
                        binding.llFirstAidKit.setOnClickListener(view -> {
                            Intent intent = new Intent(context, KitDetailsActivity.class);
                            intent.putExtra("kitDetails", iceBePrepared.get(0).getTipDescription());
                            intent.putExtra("title", iceBePrepared.get(0).getTipName());
                            intent.putExtra("imgPath", iceBePrepared.get(0).getTipImagePath());
                            startActivity(intent);
                        });
                        binding.llEmergencyKit.setOnClickListener(view -> {
                            Intent intent = new Intent(context, KitDetailsActivity.class);
                            intent.putExtra("kitDetails", iceBePrepared.get(1).getTipDescription());
                            intent.putExtra("title", iceBePrepared.get(1).getTipName());
                            intent.putExtra("imgPath", iceBePrepared.get(1).getTipImagePath());
                            startActivity(intent);
                        });

                        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
                        linearSnapHelper.attachToRecyclerView(binding.rvCPRVideo);

                        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
                        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
                        IndicatorsAdapter indicatorsAdapter = new IndicatorsAdapter(context,
                                response.body().getData().getCprVideourl().size(), 0);
                        binding.rvIndicators.setAdapter(indicatorsAdapter);
                        binding.rvIndicators.setLayoutManager(linearLayoutManager1);
                        binding.rvIndicators.setHasFixedSize(true);

                        binding.rvCPRVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1)
                                        positionVideo = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                                    else
                                        positionVideo = linearLayoutManager.findFirstVisibleItemPosition();
                                }
                                indicatorsAdapter.updateSelectedIndex(positionVideo);
                            }
                        });
                    } else {
                        Toast.makeText(CPRAndFirstAddActivity.this, "No CPR details found", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 401) {
                    refreshAuthToken(null);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_fetch_cpr_details_failed));
                    Toast.makeText(CPRAndFirstAddActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchCPRDetailsResp> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_fetch_cpr_details_failed));
                Toast.makeText(CPRAndFirstAddActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshAuthToken(String comName) {
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
                        response.body().getData().getAuthToken() != null && !response.body().getData().getAuthToken().isEmpty()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.init(context);
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    fetchCPRDetails();
                    fetchInjuries();
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
            public void onFailure(@NonNull Call<RefreshTokenResponse> call, @NonNull Throwable t) {
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
}
