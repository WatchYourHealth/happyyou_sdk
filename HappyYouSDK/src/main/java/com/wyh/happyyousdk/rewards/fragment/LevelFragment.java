package com.wyh.happyyousdk.rewards.fragment;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.validateLetters;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.MEDITATION;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;
import static com.wyh.happyyousdk.utils.Constants.TAG_TOP_UP_EVENT;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;
import static com.wyh.happyyousdk.utils.Constants.WATER;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APILogs;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.WebActivity;
import com.wyh.happyyousdk.WellBeingActivity;
import com.wyh.happyyousdk.absorb.HealthHacksActivity;
import com.wyh.happyyousdk.absorb.QuickReadDashboard;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;
import com.wyh.happyyousdk.contacts.ContactsActivityNew;

import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.CongratsPopUpBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpFeedbackBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalUploadBinding;
import com.wyh.happyyousdk.databinding.CustomPopupRewardsBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.LevelFragmentBinding;
import com.wyh.happyyousdk.databinding.PopUpScratchCardScratchableBinding;
import com.wyh.happyyousdk.diary.AddDiaryActivity;
import com.wyh.happyyousdk.ehr.EhrActivity;
import com.wyh.happyyousdk.happyMarket.NewHappyMartActivity;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.happyMarket.HappyMartDisclaimerActivity;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.ira.IRAAnalysisActivity;
import com.wyh.happyyousdk.ira.IraActivity;
import com.wyh.happyyousdk.model.request.rewards.ActivityProgressRequest;
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.VoucherIdRequest;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.request.rewards.GetRewardsDashboardRequest;
import com.wyh.happyyousdk.model.response.ScratchAndWinResponse;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.rewards.ActivityProgressResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity;
import com.wyh.happyyousdk.rewards.MoreCurrentActivities;
import com.wyh.happyyousdk.rewards.MoreTopUpsActivity;
import com.wyh.happyyousdk.rewards.MoreUpcomingActivities;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.rewards.RewardsCollectiblesActivity;
import com.wyh.happyyousdk.rewards.adapter.ScratchAndWinAdapter;
import com.wyh.happyyousdk.rewards.adapter.level.LevelActivitiesAdapter;
import com.wyh.happyyousdk.rewards.adapter.level.TopUpAdapter;
import com.wyh.happyyousdk.rewards.adapter.level.UpcomingLevelAdapter;
import com.wyh.happyyousdk.model.request.rewards.AppFeedbackRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.request.rewards.StartRewardsActivityRequest;
import com.wyh.happyyousdk.model.response.rewards.LevelActivity;
import com.wyh.happyyousdk.model.response.rewards.LevelDashboardResponse;
import com.wyh.happyyousdk.model.response.rewards.PopRewardModel;
import com.wyh.happyyousdk.model.response.rewards.RewardsLevel;
import com.wyh.happyyousdk.model.response.rewards.TopUp;
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.unwind.UnwindActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LevelFragment extends Fragment implements LevelActivitiesAdapter.ClickListenerInterface, TopUpAdapter.ClickListenerInterface,
        UpcomingLevelAdapter.ClickListenerInterface, ScratchAndWinAdapter.ClickListenerInterface {
    LevelFragmentBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    List<FreeVoucher> freeVoucherList = new ArrayList<>();
    RewardsLevel currentLevelActivity, upcomingLevelActivity;
    List<TopUp> topUpList = new ArrayList<>();
    AlertDialog alertDialog, levelActivityAlertDialog, levelActivityJournalUploadAlertDialog, levelTopUpAlertDialog;

    ArrayList<FileData> fileDataList = new ArrayList<>();

    int positionScratchAndWin = 0, positionCurrentActivities = 0, positionUpcomingActivities = 0, positionTopUps = 0, totalPoints = 0;
    public static double equivalentAmount = 0;
    private LevelActivity uploadFileActivity;
    private TopUp uploadFileTopUp;
    String yesNoAnswer, journalContent;
    ScratchAndWinAdapter scratchAndWinAdapter;
    LevelActivitiesAdapter levelActivitiesAdapter;
    TopUpAdapter topUpAdapter;
    UpcomingLevelAdapter upcomingLevelAdapter;
    String fileUploadKey = "ActivityUploads";
    public VoucherIdRequest voucherIdRequest;
    public double totalEarnPoints = 0.0;
    public static final int CAMERA_PERMISSION_CODE = 100;
    CustomPopupRewardsBinding levelActivityBinding;
    CustomPopUpRewardsJournalBinding levelActivityJournalBinding;
    CustomPopUpRewardsJournalUploadBinding customPopUpRewardsJournalUploadBinding;
    CustomPopupRewardsBinding customPopupRewardsBinding;
    String activityEventType = "";
    String topUpEventType = "";
    List<PopRewardModel> tempPopup = new ArrayList<>();
    String congratsText = "";
    boolean isPositiveBtn = false;

    public AlertDialog alertDialogBonusRewards, alertDialogScratched;

    String imageName = "";


    public LevelFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.level_fragment, container, false);

        context = getActivity();
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        Glide.with(context)
                .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "ic_tree_cloud_bg.png")
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        binding.rlHeader.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        SharedPref.init(context);
        SharedPreference.init(context);

        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(binding.rvScratchAndWin);

        LinearSnapHelper linearSnapHelper1 = new SnapHelperOneByOne();
        linearSnapHelper1.attachToRecyclerView(binding.rvActivities);

        LinearSnapHelper linearSnapHelper2 = new SnapHelperOneByOne();
        linearSnapHelper2.attachToRecyclerView(binding.rvTopUps);

        LinearSnapHelper linearSnapHelper3 = new SnapHelperOneByOne();
        linearSnapHelper3.attachToRecyclerView(binding.rvUpcomingActivities);

        binding.rlActivities.setOnClickListener(view -> {
            Intent i = new Intent(context, MoreCurrentActivities.class);
            startActivity(i);
        });

        if (!RewardsActivity.comingFrom.equalsIgnoreCase("")) {
            if (RewardsActivity.comingFrom.equalsIgnoreCase("NewDashboard")) {
                binding.extraHeight.setVisibility(View.VISIBLE);
            } else {
                binding.extraHeight.setVisibility(View.GONE);
            }
        } else {
            binding.extraHeight.setVisibility(View.GONE);

        }


        binding.rlTopUps.setOnClickListener(view -> {
            Intent i = new Intent(context, MoreTopUpsActivity.class);
            startActivity(i);
        });

        binding.rlUpcomingActivities.setOnClickListener(view -> {
            Intent i = new Intent(context, MoreUpcomingActivities.class);
            startActivity(i);
        });

        binding.scracthWinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RewardsCollectiblesActivity.class));
            }
        });

        //showRewardsPopupNew("Eat Healthier;Congratulations! You have earned 125 points for completing this event", context);

        binding.btnRedeem.setOnClickListener(view -> {
            if (currentLevelActivity != null && currentLevelActivity.getLevelID() >= 6) {
                Intent intent = new Intent(context, NewHappyMartActivity.class);
                intent.putExtra("points", totalPoints);
                intent.putExtra("amount", equivalentAmount);
                startActivity(intent);
            } else {
                showConcernInfoLayout();
            }
        });


        binding.ivInfoLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConcernInfoLayout();
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRewardsDashboardData();
        if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0) {
            showRewardsPopupDialogBox();
        }
    }


    private void showConcernInfoLayout() {
        CustomYesNoDialog customYesNoDialog = new CustomYesNoDialog(context, R.style.Theme_Dialog);
        customYesNoDialog.show();
        customYesNoDialog.setCancelable(false);
        customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
        customYesNoDialog.binding.llActionRequired.setVisibility(View.GONE);
        customYesNoDialog.binding.txtInfoPopUpDesc.setText("Please reach up to level 6 to redeem your points");
        customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
        customYesNoDialog.binding.btnYes.setText("OK");
        customYesNoDialog.binding.btnYes.setBackground(ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp));
        customYesNoDialog.binding.btnYes.setOnClickListener(view1 -> customYesNoDialog.dismiss());

    }

    public void getRewardsDashboardData() {
        GetRewardsDashboardRequest request = new GetRewardsDashboardRequest("");
        Call<LevelDashboardResponse> call = apiInterfaceWyh.getRewardsDashboardData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<LevelDashboardResponse>() {
            @Override
            public void onResponse(Call<LevelDashboardResponse> call, Response<LevelDashboardResponse> response) {
                try {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.code() == 401) {
                        refreshAuthToken();
                    }
                    if (response.code() == 200 && response.body() != null) {
                        //Log.v("response",new Gson().toJson(response.body()));

                        if (response.body().getData() != null) {
                            Double totalEarnAmount = response.body().getData().getEquivalentAmount();
                            totalEarnPoints = Math.round(totalEarnAmount / 0.2);
                       /* btotalEarnPoints = Math.round(totalEarnAmount / 0.25);
                        totalEarnPoints = Math.round(totalEarnAmount / 0.25);*/


                            if (response.body().getData().getFreebieVoucher() != null)
                                freeVoucherList = response.body().getData().getFreebieVoucher();

                            if (response.body().getData().getCurrentLevel() != null)
                                currentLevelActivity = response.body().getData().getCurrentLevel();
                            if (response.body().getData().getUpcomingLevel() != null)
                                upcomingLevelActivity = response.body().getData().getUpcomingLevel();
                            if (response.body().getData().getTopUps() != null)
                                topUpList = response.body().getData().getTopUps();

                            binding.progressLevel.setMax(currentLevelActivity.getMinimumPoint());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                binding.progressLevel.setProgress(currentLevelActivity.getCurrentPoint(), true);
                            } else {
                                binding.progressLevel.setProgress(currentLevelActivity.getCurrentPoint());
                            }

                            int currentLevel = currentLevelActivity.getLevelID();

                            if (currentLevelActivity.getLevelID() >= 6) {
                                binding.ivInfoLevel.setVisibility(View.GONE);
                            } else {
                                binding.ivInfoLevel.setVisibility(View.VISIBLE);

                            }

                            if (currentLevel == 7)
                                binding.ivScratch.setVisibility(View.VISIBLE);
                            else
                                binding.ivScratch.setVisibility(View.GONE);

                            if (currentLevel < 6)
                                binding.btnRedeem.setBackground(ContextCompat.getDrawable(context, R.drawable.dark_gray_rc_bg_8dp));
                            else
                                binding.btnRedeem.setBackground(ContextCompat.getDrawable(context, R.drawable.wyh_round_btn_dark_red));

                            totalPoints = response.body().getData().getTotalPoints();
                            SharedPref.putPointsHistory(totalPoints);
                            equivalentAmount = response.body().getData().getEquivalentAmount();
                            binding.tvLevel.setText("Level " + currentLevel);
                            binding.tvCurrentPoints.setText("Current level points: " + currentLevelActivity.getCurrentPoint());
                            binding.tvGrabStamps.setText("" + totalPoints);
                            binding.tvNextMilestoneMsg.setText("Minimum points to next level: " + currentLevelActivity.getMinimumPoint());

                            int nextLevel = currentLevel + 1;
                            binding.tvNextLevel.setText("Level: " + nextLevel);

                            if (freeVoucherList.size() > 0) {
                                binding.llGrabOpportunities.setVisibility(View.VISIBLE);
                                setVouchersAdapter();
                            } else {
                                binding.llGrabOpportunities.setVisibility(View.GONE);
                            }
                            setActivitiesAdapter();
                            setTopUpsAdapter();
                            if (upcomingLevelActivity != null && upcomingLevelActivity.getActivities() != null && upcomingLevelActivity.getActivities().size() > 0) {
                                setUpcomingActivitiesAdapter();
                            } else {
                                binding.tvNextMilestoneMsg.setText("Minimum points to complete the level: " + currentLevelActivity.getMinimumPoint());
                                binding.tvNextLevel.setVisibility(View.GONE);
                            }


                            NewDashboardHelper.Companion.getPopUpShowModels().clear();
//                        response.body().getData().getPopRewards().add(new PopRewardModel(101, "tribe test;cong 200 points"));
//                        response.body().getData().getPopRewards().add(new PopRewardModel(102, "tribe test2;cong 100 points"));
                            if (response.body().getData().getPopRewards() != null && response.body().getData().getPopRewards().size() > 0) {
                                tempPopup = response.body().getData().getPopRewards();
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, tempPopup.get(0).getPopupMessage()));
//                            showRewardsPopupNew(tempPopup.get(0).getPopupMessage(), context);
                            }


                            //response.body().getData().setLevelPopup("5");
                            if (response.body().getData().getLevelPopup() != null && !response.body().getData().getLevelPopup().isEmpty()) {
                                congratsText = response.body().getData().getLevelPopup();
                            }

                            showRewardsPopupDialogBox();

                        /*tempPopup.add(new PopRewardModel(501, "data;hVxhv 502 jasbxbz"));
                        tempPopup.add(new PopRewardModel(502, "data2;hVxhv 503 jasbxbz"));
                        showRewardsPopupNew(tempPopup.get(0).getPopupMessage(), context);*/

                            setActivitiesCompletedView();
//                      checkLevel(currentLevel);
                        }
                    } else {
                        Analytics.logEvent(context, "", "A_105_" + response.code() + "_" + SharedPref.getEncryptedMobileNo());
                    }
                } catch (Exception e) {
                    APILogs.INSTANCE.sendLogs(com.wyh.happyyousdk.utils.Constants.EXCEPTION, e.getMessage(), "Exception", context);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<LevelDashboardResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                APILogs.INSTANCE.sendLogs(com.wyh.happyyousdk.utils.Constants.EXCEPTION, t.getMessage(), "Exception", context);
                Analytics.logEvent(context, "", "A_105_Failed_" + SharedPref.getEncryptedMobileNo());
            }
        });
    }

    private void setActivitiesCompletedView() {

        binding.ivFirst.setVisibility(View.VISIBLE);
        binding.ivSecond.setVisibility(View.VISIBLE);
        binding.ivThird.setVisibility(View.VISIBLE);
        binding.ivFourth.setVisibility(View.VISIBLE);
        binding.ivFifth.setVisibility(View.VISIBLE);
        binding.ivSixth.setVisibility(View.VISIBLE);

        int completedActivities = currentLevelActivity.getCompletedActivities();

        if (completedActivities == 0) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (completedActivities == 1) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (completedActivities == 2) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (completedActivities == 3) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (completedActivities == 4) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (completedActivities == 5) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (completedActivities >= 6) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
        }
    }

    private void setVouchersAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        scratchAndWinAdapter = new ScratchAndWinAdapter(context, freeVoucherList, this);
        binding.rvScratchAndWin.setAdapter(scratchAndWinAdapter);
        binding.rvScratchAndWin.setLayoutManager(linearLayoutManager);
        scratchAndWinAdapter.notifyDataSetChanged();

        int indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(freeVoucherList.size())) / 3.0);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        if (indicatorSize > 3)
            indicatorSize = 3;
//        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
//        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter indicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvScratchAndWinIndicator.setAdapter(indicatorsAdapter);
        binding.rvScratchAndWinIndicator.setLayoutManager(linearLayoutManager1);
        binding.rvScratchAndWinIndicator.setHasFixedSize(true);

        binding.rvScratchAndWin.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionScratchAndWin = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionScratchAndWin = linearLayoutManager.findFirstVisibleItemPosition();
                    indicatorsAdapter.updateSelectedIndex(positionScratchAndWin);
                }
            }
        });
    }

    private void setActivitiesAdapter() {
        if (currentLevelActivity != null && currentLevelActivity.getActivities() != null && currentLevelActivity.getActivities().size() > 0) {
            List<LevelActivity> currentActivities = currentLevelActivity.getActivities();
            List<LevelActivity> inProgress = new ArrayList<>();
            List<LevelActivity> notStarted = new ArrayList<>();
            List<LevelActivity> completed = new ArrayList<>();
            for (int i = 0; i < currentActivities.size(); i++) {
                if (currentActivities.get(i).isStarted() && !currentActivities.get(i).isIsCompleted())
                    inProgress.add(currentActivities.get(i));
                else if (!currentActivities.get(i).isStarted() && !currentActivities.get(i).isIsCompleted())
                    notStarted.add(currentActivities.get(i));
                else
                    completed.add(currentActivities.get(i));
            }
            currentActivities.clear();
            currentActivities.addAll(inProgress);
            currentActivities.addAll(notStarted);
            currentActivities.addAll(completed);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            levelActivitiesAdapter = new LevelActivitiesAdapter(context, currentActivities, this);
            binding.rvActivities.setAdapter(levelActivitiesAdapter);
            binding.rvActivities.setLayoutManager(linearLayoutManager);
            levelActivitiesAdapter.notifyDataSetChanged();

            int indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(currentLevelActivity.getActivities().size())) / 3.0);
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
            linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            if (indicatorSize > 3)
                indicatorSize = 3;
            IndicatorsAdapter indicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvActivitiesIndicator.setAdapter(indicatorsAdapter);
            binding.rvActivitiesIndicator.setLayoutManager(linearLayoutManager1);
            binding.rvActivitiesIndicator.setHasFixedSize(true);

            binding.rvActivities.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionCurrentActivities = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionCurrentActivities = linearLayoutManager.findFirstVisibleItemPosition();
                        indicatorsAdapter.updateSelectedIndex(positionCurrentActivities);
                    }
                }
            });
        } else {
            binding.llActivities.setVisibility(View.GONE);
        }
    }

    private void setTopUpsAdapter() {
        List<TopUp> data = topUpList;
        List<TopUp> inProgress = new ArrayList<>();
        List<TopUp> notStarted = new ArrayList<>();
        List<TopUp> completed = new ArrayList<>();
        List<TopUp> comingSoon = new ArrayList<>();
        List<TopUp> recurring = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getTopUpName().toLowerCase().contains("challenge") && !data.get(i).getTopUpName().equalsIgnoreCase("Safety challenge"))
                comingSoon.add(data.get(i));
            else if (data.get(i).isIsCompleted() && data.get(i).getRecurrenceDays() >= 7)
                recurring.add(data.get(i));
            else if (data.get(i).isIsCompleted())
                completed.add(data.get(i));
            else if (data.get(i).isStarted() && !data.get(i).isIsCompleted())
                inProgress.add(data.get(i));
            else
                notStarted.add(data.get(i));
        }
        data.clear();
        data.addAll(inProgress);
        data.addAll(notStarted);
        data.addAll(comingSoon);
        data.addAll(recurring);
        data.addAll(completed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        topUpAdapter = new TopUpAdapter(context, data, this);
        binding.rvTopUps.setAdapter(topUpAdapter);
        binding.rvTopUps.setLayoutManager(linearLayoutManager);
        topUpAdapter.notifyDataSetChanged();

        int indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(data.size())) / 3.0);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        if (indicatorSize > 3)
            indicatorSize = 3;
        IndicatorsAdapter indicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvTopUpsIndicator.setAdapter(indicatorsAdapter);
        binding.rvTopUpsIndicator.setLayoutManager(linearLayoutManager1);
        binding.rvTopUpsIndicator.setHasFixedSize(true);

        binding.rvTopUps.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionTopUps = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionTopUps = linearLayoutManager.findFirstVisibleItemPosition();
                    indicatorsAdapter.updateSelectedIndex(positionTopUps);
                }
            }
        });
    }

    private void setUpcomingActivitiesAdapter() {
        binding.llUpcomingActivities.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        upcomingLevelAdapter = new UpcomingLevelAdapter(context, upcomingLevelActivity.getActivities(), upcomingLevelActivity, this);
        binding.rvUpcomingActivities.setAdapter(upcomingLevelAdapter);
        binding.rvUpcomingActivities.setLayoutManager(linearLayoutManager);
        upcomingLevelAdapter.notifyDataSetChanged();

        int indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(upcomingLevelActivity.getActivities().size())) / 3.0);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter indicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvUpcomingActivitiesIndicator.setAdapter(indicatorsAdapter);
        binding.rvUpcomingActivitiesIndicator.setLayoutManager(linearLayoutManager1);
        binding.rvUpcomingActivitiesIndicator.setHasFixedSize(true);

        binding.rvUpcomingActivities.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionUpcomingActivities = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionUpcomingActivities = linearLayoutManager.findFirstVisibleItemPosition();
                    indicatorsAdapter.updateSelectedIndex(positionUpcomingActivities);
                }
            }
        });
    }

    private void refreshAuthToken() {
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
                        response.body().getData().getAuthToken() != null && !response.body().getData().getAuthToken().equals("")) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    getRewardsDashboardData();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.refresh_token_failed));
                   /* Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                    Master.INSTANCE.logOut(context);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.refresh_token_failed));
                /* Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    @Override
    public void onItemClickActivities(LevelActivity levelActivity, int bgDrawable) {
        if (levelActivity.getEventType().equalsIgnoreCase("JournalUpload"))
            showCustomPopUpJournalUpload(levelActivity, bgDrawable);
        else if (levelActivity.getEventType().equalsIgnoreCase("journal"))
            showCustomPopUpJournal(levelActivity, bgDrawable);
        else
            showCustomPopUp(levelActivity, bgDrawable);
    }

    @Override
    public void onUploadClick(LevelActivity levelActivity) {

    }

    @Override
    public void onItemClickTopUps(TopUp topUp, int bgDrawable) {
        if (topUp.getTopUpName().toLowerCase().equals("scratch & win")) {
            if (!topUp.isIsCompleted()) {
                startScratchAndWin(topUp);
            }
        } else if (topUp.getEventType().equalsIgnoreCase("JournalUpload"))
            showCustomPopUpJournalUploadTopUp(topUp, bgDrawable);
        else if (topUp.getEventType().equalsIgnoreCase("Feedback"))
            showCustomPopUpFeedback(topUp);
        else
            showCustomPopUpTopUp(topUp, bgDrawable);
    }

    @Override
    public void onItemClickUpcomingActivity(LevelActivity levelActivity, int bgDrawable) {

    }

    private void showCustomPopUpJournal(LevelActivity levelActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        levelActivityJournalBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_rewards_journal, null, false);
        alertBuilder.setView(levelActivityJournalBinding.getRoot());
        levelActivityAlertDialog = alertBuilder.create();
        levelActivityAlertDialog.setCancelable(false);
        if (!levelActivityAlertDialog.isShowing())
            levelActivityAlertDialog.show();

        Glide.with(context)
                .load(levelActivity.getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(levelActivityJournalBinding.ivLogo);
        levelActivityJournalBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        levelActivityJournalBinding.tvTitle.setText(levelActivity.getActivityName());
        levelActivityJournalBinding.tvDescription.setText(levelActivity.getActivityDesc());

        if (levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
        }

        if (levelActivity.getWhatTo() != null) {
            levelActivityJournalBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.tvWhatToDo.setText(levelActivity.getWhatTo());
            levelActivityJournalBinding.tvHowToDo.setText(levelActivity.getHowTo());
            levelActivityJournalBinding.tvWhyToDo.setText(levelActivity.getWhyTo());
        }

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(levelActivityJournalBinding.progressBar, levelActivity, null, null);
        } else if (levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                levelActivityJournalBinding.progressBar.setProgress(100, true);
            } else
                levelActivityJournalBinding.progressBar.setProgress(100);
        }

        activityEventType = levelActivity.getEventType();

        checkActivityPopUpConditionsJournal(levelActivity, activityEventType);

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        levelActivityAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.8f));
    }

    private void showCustomPopUp(LevelActivity levelActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        levelActivityBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_rewards, null, false);
        alertBuilder.setView(levelActivityBinding.getRoot());
        levelActivityAlertDialog = alertBuilder.create();
        levelActivityAlertDialog.setCancelable(false);
        if (!levelActivityAlertDialog.isShowing())
            levelActivityAlertDialog.show();

        Glide.with(context)
                .load(levelActivity.getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(levelActivityBinding.ivLogo);

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            levelActivityBinding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(levelActivityBinding.progressBar, levelActivity, null, levelActivityBinding.tvSteps);
        } else if (levelActivity.isIsCompleted()) {
            levelActivityBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                levelActivityBinding.progressBar.setProgress(100, true);
            } else
                levelActivityBinding.progressBar.setProgress(100);
        }

        levelActivityBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        levelActivityBinding.tvTitle.setText(levelActivity.getActivityName());
        levelActivityBinding.tvDescription.setText(levelActivity.getActivityDesc());

        if (levelActivity.isIsCompleted()) {
            levelActivityBinding.btnPositive.setVisibility(View.GONE);
        }

        if (levelActivity.getWhatTo() != null) {
            levelActivityBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            levelActivityBinding.tvWhatToDo.setText(levelActivity.getWhatTo());
            levelActivityBinding.tvHowToDo.setText(levelActivity.getHowTo());
            levelActivityBinding.tvWhyToDo.setText(levelActivity.getWhyTo());
        }

        activityEventType = levelActivity.getEventType();

        checkActivityPopUpConditions(levelActivity, activityEventType);

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        levelActivityAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void getActivityProgress(LinearProgressIndicator progressBar, LevelActivity levelActivity, TopUp topUp, TextView tvSteps) {
        ActivityProgressRequest request;
        if (levelActivity != null)
            request = new ActivityProgressRequest(levelActivity.getActivityID(), "Activity");
        else
            request = new ActivityProgressRequest(topUp.getTopUpID(), "Topup");
        Call<ActivityProgressResponse> call = apiInterfaceWyh.getActivityProgress(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<ActivityProgressResponse>() {
            @Override
            public void onResponse(Call<ActivityProgressResponse> call, Response<ActivityProgressResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getData() != null) {
                        if (tvSteps != null && response.body().getData().getTotalSteps() != 0) {
                            tvSteps.setVisibility(View.VISIBLE);
                            tvSteps.setText("Your steps: " + response.body().getData().getUserSteps() + " / " + response.body().getData().getTotalSteps());
                        } else if (tvSteps != null && response.body().getData().getTotalCount() != 0) {
                            tvSteps.setVisibility(View.VISIBLE);
                            tvSteps.setText("Your progress: " + response.body().getData().getUserCount() + " / " + response.body().getData().getTotalCount());
                        }
                        int progress = response.body().getData().getProgressPercentage();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            progressBar.setProgress(progress, true);
                        } else
                            progressBar.setProgress(progress);
                    }
                }
            }

            @Override
            public void onFailure(Call<ActivityProgressResponse> call, Throwable t) {

            }
        });
    }

    private void checkActivityPopUpConditions(LevelActivity levelActivity, String activityEventType) {

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            if (levelActivity.getRedirectTo() != null) {
                levelActivityBinding.btnPositive.setVisibility(View.VISIBLE);
                levelActivityBinding.btnPositive.setText("Complete");
            } else {
                levelActivityBinding.btnPositive.setVisibility(View.GONE);
            }
        }


        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("upload")) {
            levelActivityBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityBinding.btnPositive.setText("Upload");
           /* levelActivityBinding.btnPositive.setOnClickListener(view -> {
                fileUploadKey = "ActivityUploads";
                levelActivityAlertDialog.dismiss();
                uploadFileActivity = levelActivity;
                ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            });*/
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("yesno")) {
            levelActivityBinding.llYesNo.setVisibility(View.VISIBLE);
            levelActivityBinding.btnYes.setOnClickListener(view -> {
                levelActivityAlertDialog.dismiss();
                yesNoAnswer = "Yes";
                uploadContentOnly(levelActivity);
            });
            levelActivityBinding.btnNo.setOnClickListener(view -> {
                levelActivityAlertDialog.dismiss();
                yesNoAnswer = "No";
                uploadContentOnly(levelActivity);
            });
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("journal")) {
            levelActivityBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityBinding.btnPositive.setText("Submit");
            levelActivityBinding.llJournal.setVisibility(View.VISIBLE);
            levelActivityBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(levelActivityBinding.edtJournal.getText()) && validateLetters(levelActivityBinding.edtJournal.getText().toString())) {
                    levelActivityAlertDialog.dismiss();
                    journalContent = levelActivityBinding.edtJournal.getText().toString();
                    uploadContentOnly(levelActivity);
                } else {
                    levelActivityBinding.edtJournal.requestFocus();
                }
            });
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("JournalUpload")) {
            levelActivityBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityBinding.btnPositive.setText("Submit");
            levelActivityBinding.llJournal.setVisibility(View.VISIBLE);
            levelActivityBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(levelActivityBinding.edtJournal.getText()) && validateLetters(levelActivityBinding.edtJournal.getText().toString())) {
                    journalContent = levelActivityBinding.edtJournal.getText().toString();
                    fileUploadKey = "JournalUpload";
                    uploadFileActivity = levelActivity;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, true);
                    } else {
                        levelActivityAlertDialog.dismiss();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            openGalleryOnly();
                        }
                    }
                } else {
                    levelActivityBinding.edtJournal.requestFocus();
                }
            });
        }

        levelActivityBinding.btnNegative.setOnClickListener(view -> {
            levelActivityAlertDialog.dismiss();
            getRewardsDashboardData();
        });

        levelActivityBinding.btnPositive.setOnClickListener(view1 -> {
//            levelActivityAlertDialog.dismiss();

            if (levelActivityBinding.btnPositive.getText() == "Upload" && activityEventType.equalsIgnoreCase("Upload")) {
                fileUploadKey = "ActivityUploads";
                uploadFileActivity = levelActivity;
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, true);
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        openImagePicker();
                    } else {
                        openGalleryOnly();
                    }
                }
            } else {
                if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
                    if (levelActivity.getRedirectTo() != null) {
                        levelActivityAlertDialog.dismiss();
                        activitiesRedirection(levelActivity);
                    }
                } else {
                    startNewActivity(levelActivity, "");
                }
            }

        });


        if (levelActivity.isIsCompleted()) {
            levelActivityBinding.btnPositive.setVisibility(View.GONE);
        }


    }

    private void checkActivityPopUpConditionsJournal(LevelActivity levelActivity, String activityEventType) {

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            if (levelActivity.getRedirectTo() != null) {
                levelActivityJournalBinding.btnPositive.setVisibility(View.VISIBLE);
                levelActivityJournalBinding.btnPositive.setText("Complete");
            } else {
                levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
                levelActivityJournalBinding.llJournal.setVisibility(View.GONE);
            }
        }


        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("upload")) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setText("Upload");
           /* levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                fileUploadKey = "ActivityUploads";
                levelActivityAlertDialog.dismiss();
                uploadFileActivity = levelActivity;
                ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            });*/
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("yesno")) {
            levelActivityJournalBinding.llYesNo.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnYes.setOnClickListener(view -> {
                levelActivityAlertDialog.dismiss();
                yesNoAnswer = "Yes";
                uploadContentOnly(levelActivity);
            });
            levelActivityJournalBinding.btnNo.setOnClickListener(view -> {
                levelActivityAlertDialog.dismiss();
                yesNoAnswer = "No";
                uploadContentOnly(levelActivity);
            });
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("journal")) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setText("Submit");
            levelActivityJournalBinding.llJournal.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(levelActivityJournalBinding.edtJournal.getText()) &&
                        levelActivityJournalBinding.edtJournal.getText().toString().length() > 15) {
                    levelActivityAlertDialog.dismiss();
                    journalContent = levelActivityJournalBinding.edtJournal.getText().toString();
                    uploadContentOnly(levelActivity);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                    levelActivityJournalBinding.edtJournal.requestFocus();
                }
            });
        }

        if (levelActivity.isStarted() && activityEventType.equalsIgnoreCase("JournalUpload")) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setText("Submit");
            levelActivityJournalBinding.llJournal.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(levelActivityJournalBinding.edtJournal.getText()) && validateLetters(levelActivityJournalBinding.edtJournal.getText().toString())) {
                    journalContent = levelActivityJournalBinding.edtJournal.getText().toString();
                    fileUploadKey = "JournalUpload";
                    uploadFileActivity = levelActivity;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, true);
                    } else {
                        levelActivityAlertDialog.dismiss();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            openGalleryOnly();
                        }
                    }
                } else {
                    levelActivityJournalBinding.edtJournal.requestFocus();
                }
            });
        }

        if (!levelActivity.isStarted()) {
            levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                startNewActivity(levelActivity, "Journal");
            });
        }

        levelActivityJournalBinding.btnNegative.setOnClickListener(view -> {
            levelActivityAlertDialog.dismiss();
            getRewardsDashboardData();
        });


        if (levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
            levelActivityJournalBinding.llJournal.setVisibility(View.GONE);
        }


    }

    private void checkActivityPopUpConditionsJournalUpload(LevelActivity levelActivity, String activityEventType) {
        customPopUpRewardsJournalUploadBinding.btnNegative.setOnClickListener(view -> {
            levelActivityJournalUploadAlertDialog.dismiss();
            getRewardsDashboardData();
        });

        customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view1 -> {
            startNewActivity(levelActivity, "JournalUpload");
        });

        if (!levelActivity.isIsCompleted() && levelActivity.isStarted() && levelActivity.getEventType().equalsIgnoreCase("JournalUpload")) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setText("Submit");
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(customPopUpRewardsJournalUploadBinding.edtJournal.getText()) && customPopUpRewardsJournalUploadBinding.edtJournal.getText().length() >= 15) {
                    journalContent = customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString();
                    fileUploadKey = "JournalUpload";
                    uploadFileActivity = levelActivity;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, true);
                    } else {
                        //levelActivityJournalUploadAlertDialog.dismiss();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            openGalleryOnly();
                        }
                    }
                } else {
                    customPopUpRewardsJournalUploadBinding.edtJournal.requestFocus();
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (levelActivity.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.GONE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.GONE);
        }

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.8f));
        } else {
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.6f));
        }


    }

    private void checkActivityPopUpConditionsJournalUploadTopUp(TopUp topUp, String activityEventType) {
        customPopUpRewardsJournalUploadBinding.btnNegative.setOnClickListener(view -> {
            levelActivityJournalUploadAlertDialog.dismiss();
            getRewardsDashboardData();
        });

        customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view1 -> {
            startTopUps(topUp);
        });

        if (topUp.isStarted() && !topUp.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.progressBar.setVisibility(View.VISIBLE);
        } else if (topUp.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100, true);
            } else
                customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100);
        }

        if (!topUp.isIsCompleted() && topUp.isStarted() && topUp.getEventType().equalsIgnoreCase("JournalUpload")) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setText("Submit");
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(customPopUpRewardsJournalUploadBinding.edtJournal.getText()) && customPopUpRewardsJournalUploadBinding.edtJournal.getText().length() >= 15) {
                    journalContent = customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString();
                    fileUploadKey = "JournalUpload";
                    uploadFileTopUp = topUp;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        levelActivityJournalUploadAlertDialog.dismiss();
                        ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, true);
                    } else {
                        //levelActivityJournalUploadAlertDialog.dismiss();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            openGalleryOnly();
                        }
                    }
                } else {
                    customPopUpRewardsJournalUploadBinding.edtJournal.requestFocus();
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (topUp.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.GONE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.GONE);
        }

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        if (topUp.isStarted() && !topUp.isIsCompleted()) {
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.8f));
        } else {
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.6f));
        }


    }

    private void showCustomPopUpTopUp(TopUp topUp, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        customPopupRewardsBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_rewards, null, false);
        alertBuilder.setView(customPopupRewardsBinding.getRoot());
        alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        Glide.with(context)
                .load(topUp.getTopupIcon())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(customPopupRewardsBinding.ivLogo);

        customPopupRewardsBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        customPopupRewardsBinding.rlLogo.setVisibility(View.VISIBLE);
        customPopupRewardsBinding.tvTitle.setText(topUp.getTopUpName());
        customPopupRewardsBinding.tvDescription.setText(topUp.getTopUpDesc());
        if (topUp.isIsStarted() || topUp.isIsCompleted()) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);
        }

        if (topUp.getWhatTo() != null) {
            customPopupRewardsBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            customPopupRewardsBinding.tvWhatToDo.setText(topUp.getWhatTo());
            customPopupRewardsBinding.tvHowToDo.setText(topUp.getHowTo());
            customPopupRewardsBinding.tvWhyToDo.setText(topUp.getWhyTo());
        }

        if (topUp.isStarted() && !topUp.isIsCompleted()) {
            customPopupRewardsBinding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(customPopupRewardsBinding.progressBar, null, topUp, customPopupRewardsBinding.tvSteps);
        } else if (topUp.isIsCompleted()) {
            customPopupRewardsBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                customPopupRewardsBinding.progressBar.setProgress(100, true);
            } else
                customPopupRewardsBinding.progressBar.setProgress(100);
        }

        if (topUp.isIsCompleted()) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);
        }

        checkTopUpCondition(topUp);

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void showCustomPopUpFeedback(TopUp topUp) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        CustomPopUpFeedbackBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_feedback, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();


        binding.tvTitle.setText(topUp.getTopUpName());
        binding.tvDescription.setText(topUp.getTopUpDesc());
        if (topUp.isIsStarted()) {
            binding.btnPositive.setVisibility(View.GONE);
        }

        if (topUp.getWhatTo() != null) {
            binding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            binding.tvWhatToDo.setText(topUp.getWhatTo());
            binding.tvHowToDo.setText(topUp.getHowTo());
            binding.tvWhyToDo.setText(topUp.getWhyTo());
        }

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        if (topUp.isStarted() && !topUp.isIsCompleted()) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else if (topUp.isIsCompleted()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.progressBar.setProgress(100, true);
            } else
                binding.progressBar.setProgress(100);
        }

        binding.btnPositive.setOnClickListener(view1 -> {

            binding.btnPositive.setVisibility(View.VISIBLE);
            binding.btnPositive.setText("Submit");
            binding.tvLabel.setText("Your feedback");
            binding.llJournal.setVisibility(View.VISIBLE);
            binding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(binding.edtFeedback.getText()) && binding.edtFeedback.getText().length() >= 35) {
                    alertDialog.dismiss();
                    topUpFeedback(binding.edtFeedback.getText().toString());
                } else {
                    binding.edtFeedback.requestFocus();
                    Toast.makeText(context, getResources().getString(R.string.at_least_35_char), Toast.LENGTH_SHORT).show();
                }
            });

            startTopUps(topUp);
        });

        if (topUp.isIsStarted() && topUp.getEventType().equalsIgnoreCase("Feedback")
                && !topUp.isIsCompleted()) {
            binding.btnPositive.setVisibility(View.VISIBLE);
            binding.btnPositive.setText("Submit");
            binding.tvLabel.setText("Your feedback");

            binding.llJournal.setVisibility(View.VISIBLE);
            binding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(binding.edtFeedback.getText())) {
                    alertDialog.dismiss();
                    topUpFeedback(binding.edtFeedback.getText().toString());
                } else {
                    binding.edtFeedback.requestFocus();
                }
            });
        }

        if (topUp.isIsCompleted()) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);
        }

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.8f));
    }

    private void checkTopUpCondition(TopUp topUp) {

        customPopupRewardsBinding.btnNegative.setOnClickListener(view -> {
            alertDialog.dismiss();
            getRewardsDashboardData();
        });

        customPopupRewardsBinding.btnPositive.setOnClickListener(view1 -> {
//            alertDialog.dismiss();
            if (topUp.isIsStarted()) {
                if (topUp.getRedirectTo() != null) {
                    alertDialog.dismiss();
                    topUpRedirection(topUp);
                }
            } else {
                if (topUp.getTopUpName().equalsIgnoreCase("Scratch & Win")) {
                    startScratchAndWin(topUp);
                } else
                    startTopUps(topUp);
            }
        });

        if (topUp.isIsStarted() && !Objects.equals(topUp.getEventType(), "upload") && !topUp.isIsCompleted()) {
            if (topUp.getRedirectTo() != null) {
                customPopupRewardsBinding.btnPositive.setVisibility(View.VISIBLE);
                customPopupRewardsBinding.btnPositive.setText("Complete");
            } else {
                customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);
            }
        }

        if (topUp.isIsStarted() && topUp.getEventType().equalsIgnoreCase("upload") && !topUp.isIsCompleted()) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.VISIBLE);
            customPopupRewardsBinding.btnPositive.setText("Upload");
            customPopupRewardsBinding.btnPositive.setOnClickListener(view -> {
                fileUploadKey = "ActivityUploads";
                alertDialog.dismiss();
                uploadFileTopUp = topUp;
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, true);
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        openImagePicker();
                    } else {
                        openGalleryOnly();
                    }
                }
            });
        }

        if (topUp.isIsStarted() && topUp.getEventType().equalsIgnoreCase("yesno") && !topUp.isIsCompleted()) {
            customPopupRewardsBinding.llYesNo.setVisibility(View.VISIBLE);
            customPopupRewardsBinding.btnYes.setOnClickListener(view -> {
                alertDialog.dismiss();
                yesNoAnswer = "Yes";
                uploadContentOnlyTopUP(topUp);
            });
            customPopupRewardsBinding.btnNo.setOnClickListener(view -> {
                alertDialog.dismiss();
                yesNoAnswer = "No";
                uploadContentOnlyTopUP(topUp);
            });
        }

        if (topUp.isIsStarted() && topUp.getEventType().equalsIgnoreCase("journal") && !topUp.isIsCompleted()) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.VISIBLE);
            customPopupRewardsBinding.btnPositive.setText("Submit");
            customPopupRewardsBinding.llJournal.setVisibility(View.VISIBLE);
            customPopupRewardsBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(customPopupRewardsBinding.edtJournal.getText()) && validateLetters(customPopupRewardsBinding.edtJournal.getText().toString())) {
                    alertDialog.dismiss();
                    journalContent = customPopupRewardsBinding.edtJournal.getText().toString();
                    uploadContentOnlyTopUP(topUp);
                } else {
                    customPopupRewardsBinding.edtJournal.requestFocus();
                }
            });
        }

        if (!topUp.isIsCompleted() && topUp.isStarted() && topUp.getEventType().equalsIgnoreCase("JournalUpload")) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setText("Submit");
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(customPopUpRewardsJournalUploadBinding.edtJournal.getText()) && customPopUpRewardsJournalUploadBinding.edtJournal.getText().length() >= 15) {
                    journalContent = customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString();
                    fileUploadKey = "JournalUpload";
                    uploadFileTopUp = topUp;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, true);
                    } else {
                        //levelActivityJournalUploadAlertDialog.dismiss();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            openGalleryOnly();
                        }
                    }
                } else {
                    customPopUpRewardsJournalUploadBinding.edtJournal.requestFocus();
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (topUp.getTopUpName().toLowerCase().contains("challenge") && !topUp.getTopUpName().equalsIgnoreCase("Safety challenge")) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);
        }

    }

    private void startScratchAndWin(TopUp topUp) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        StartRewardsActivityRequest request = new StartRewardsActivityRequest(topUp.getTopUpID(), TAG_TOP_UP_EVENT);
        Call<ScratchAndWinResponse> call = apiInterfaceWyh.startScratchAndWin(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<ScratchAndWinResponse>() {
            @Override
            public void onResponse(Call<ScratchAndWinResponse> call, Response<ScratchAndWinResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_success));
                    if (response.body().isSuccess()) {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        showScratchAndWinPopup(String.valueOf(response.body().getData()), context);
                        Toast.makeText(context, "Top up has been started", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_failed));

                }
            }

            @Override
            public void onFailure(Call<ScratchAndWinResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showScratchAndWinPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = "Scratch & Win";
        binding.scratchView.setScratchDrawable(getResources().getDrawable(R.drawable.scratch_card_blue_new));
        binding.tvPoints.setText(rewards);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                getRewardsDashboardData();
            }
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

//        binding.btnPositive.setText("Collect");
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener((RewardsActivity) context);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showCustomPopUpJournalUpload(LevelActivity levelActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        customPopUpRewardsJournalUploadBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_rewards_journal_upload, null, false);
        alertBuilder.setView(customPopUpRewardsJournalUploadBinding.getRoot());
        levelActivityJournalUploadAlertDialog = alertBuilder.create();
        levelActivityJournalUploadAlertDialog.setCancelable(false);
        if (!levelActivityJournalUploadAlertDialog.isShowing())
            levelActivityJournalUploadAlertDialog.show();

        Glide.with(context)
                .load(levelActivity.getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(customPopUpRewardsJournalUploadBinding.ivLogo);
        customPopUpRewardsJournalUploadBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        customPopUpRewardsJournalUploadBinding.tvTitle.setText(levelActivity.getActivityName());
        customPopUpRewardsJournalUploadBinding.tvDescription.setText(levelActivity.getActivityDesc());
        if (levelActivity.isStarted() || levelActivity.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.GONE);
        }

        activityEventType = levelActivity.getEventType();
        checkActivityPopUpConditionsJournalUpload(levelActivity, activityEventType);

        if (levelActivity.getWhatTo() != null) {
            customPopUpRewardsJournalUploadBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.tvWhatToDo.setText(levelActivity.getWhatTo());
            customPopUpRewardsJournalUploadBinding.tvHowToDo.setText(levelActivity.getHowTo());
            customPopUpRewardsJournalUploadBinding.tvWhyToDo.setText(levelActivity.getWhyTo());
        }

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(customPopUpRewardsJournalUploadBinding.progressBar, levelActivity, null, null);
        } else if (levelActivity.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100, true);
            } else
                customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100);
        }

    }

    private void showCustomPopUpJournalUploadTopUp(TopUp topUp, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        customPopUpRewardsJournalUploadBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_rewards_journal_upload, null, false);
        alertBuilder.setView(customPopUpRewardsJournalUploadBinding.getRoot());
        levelActivityJournalUploadAlertDialog = alertBuilder.create();
        levelActivityJournalUploadAlertDialog.setCancelable(false);
        if (!levelActivityJournalUploadAlertDialog.isShowing())
            levelActivityJournalUploadAlertDialog.show();

        Glide.with(context)
                .load(topUp.getTopupIcon())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(customPopUpRewardsJournalUploadBinding.ivLogo);
        customPopUpRewardsJournalUploadBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        customPopUpRewardsJournalUploadBinding.tvTitle.setText(topUp.getTopUpName());
        customPopUpRewardsJournalUploadBinding.tvDescription.setText(topUp.getTopUpDesc());
        if (topUp.isStarted() || topUp.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.GONE);
        }

        activityEventType = topUp.getEventType();
        checkActivityPopUpConditionsJournalUploadTopUp(topUp, activityEventType);

        if (topUp.getWhatTo() != null) {
            customPopUpRewardsJournalUploadBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.tvWhatToDo.setText(topUp.getWhatTo());
            customPopUpRewardsJournalUploadBinding.tvHowToDo.setText(topUp.getHowTo());
            customPopUpRewardsJournalUploadBinding.tvWhyToDo.setText(topUp.getWhyTo());
        }
    }

    public void checkImagePicker() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            openImagePicker();
        } else {
            openGalleryOnly();
        }
    }

    public void openGalleryOnly() {
        ImagePicker.create(this)
                .includeVideo(false)
                .imageDirectory("Camera")
                .enableLog(true)
                .includeAnimation(true)
                .limit(1)
                .showCamera(true)
                .start();
    }

    public void openImagePicker() {
        try {
            Intent intent = new Intent();
            intent.putExtra(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveImage(Uri imageURI) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        FileData fileData = new FileData();
        fileData.setPath(CommonUtils.getRealPathFromURI(imageURI, context));
        FileData fileDataNew = new FileData();
        File file1 = CommonUtils.compressImageToJPEG(context, imageURI);
        fileDataNew.setPath(file1.getPath());
        fileDataNew.setMimeType("application/png");
        imageName = file1.getName();
        if (file1.length() > 5000000) {
            Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
        } else {
            fileDataList.add(fileDataNew);
        }
        if (fileDataList.size() > 0) {
            if (uploadFileActivity != null) {
                parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                uploadFileWithContent(parts, uploadFileActivity);
            }
            if (uploadFileTopUp != null) {
                parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                uploadFileWithContentTopUP(parts, uploadFileTopUp);
            }
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (requestCode == 2121) {
            getRewardsDashboardData();
        }
        if (requestCode == 1) {
            if (data == null) {
                Toast.makeText(context, "No image Selected", Toast.LENGTH_SHORT).show();
            } else {
                if (data.getData() != null) {
                    Uri imageURI = data.getData();
                    saveImage(imageURI);
                } else {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri imageURI = item.getUri();
                        saveImage(imageURI);
                    }
                }
            }

        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<Image> image2 = ImagePicker.getImages(data);
            for (int i = 0; i < image2.size(); i++) {
                FileData fileData = new FileData();
                fileData.setPath(getFilePathFromImage(image2.get(i)));
                long singleFileSize = getFileSizeFromPath(fileData.getPath());
                Log.d("comparedata singleFileSize", singleFileSize + "");
                FileData fileDataNew = new FileData();
                Log.d("comparedata singleFileSize", singleFileSize + "");
                File file1 = CommonUtils.compressImageToJPEG(context, image2.get(i).getUri());
                fileDataNew.setPath(file1.getPath());
                fileDataNew.setMimeType("application/png");
                imageName = file1.getName();
                if (file1.length() > 5000000) {
                    Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                } else {
                    fileDataList.add(fileDataNew);
                }

               /* if(singleFileSize > 1000000){

                }else{
                    fileDataList.add(fileData);
                }*/
            }
            if (fileDataList.size() > 0) {
                if (uploadFileActivity != null) {
                    parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                    uploadFileWithContent(parts, uploadFileActivity);
                }
                if (uploadFileTopUp != null) {
                    parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                    uploadFileWithContentTopUP(parts, uploadFileTopUp);
                }
//                uploadFiles(parts, uploadFileActivity, yesNoAnswer, journalContent);
            }
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }


    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String path) {
        File file = new File(path);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private long getFileSizeFromPath(String filePath) {
        if (filePath == null)
            return 0;
        File file = new File(filePath);
        return file.length();
    }

    private String getFilePathFromImage(Image image) {
        if (image == null)
            return null;
        if (!TextUtils.isEmpty(image.getPath()) && new File(image.getPath()).exists()) {
            return image.getPath();
        } else if (image.getUri() != null) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(image.getUri(),
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgFilePath = null;
            if (columnIndex != -1) {
                imgFilePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return imgFilePath;
        }
        return null;
    }

    private void activitiesRedirection(LevelActivity levelActivity) {
        if (levelActivity.getRedirectTo() != null) {
            switch (levelActivity.getRedirectTo().toLowerCase()) {
                case "hra":
                    Intent intent;
                    GetAnalysisResponse getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
                    if (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0) {
                        intent = new Intent(context, HRAAnalysisActivity.class);
                    } else {
                        intent = new Intent(context, HRAQuestionsActivity.class);
                    }
                    startActivity(intent);
                    break;
                case "ira":
                    IRAHealthScoreResponse iraHealthScoreResponse = new Gson().fromJson(SharedPref.getIRAHealthData(), IRAHealthScoreResponse.class);
                    String healthScore = "";
                    if (iraHealthScoreResponse != null && iraHealthScoreResponse.getIraHealthScoreData() != null)
                        healthScore = iraHealthScoreResponse.getIraHealthScoreData().getPlaySports();
                    if (!healthScore.equals("0") && !healthScore.equals("")) {
                        intent = new Intent(context, IRAAnalysisActivity.class);
                    } else {
                        intent = new Intent(context, IraActivity.class);
                        intent.putExtra("from", IRA_STATUS_COMPLETED);
                    }
                    startActivity(intent);
                    break;
                case "blogread":
                    Intent intent1 = new Intent(context, QuickReadDashboard.class);
                    startActivity(intent1);
                    break;
                case "wellbeing":
                    intent1 = new Intent(context, WellBeingActivity.class);
                    startActivity(intent1);
                    break;
                case "waterintake":
                    Intent intent2 = new Intent(context, TrendsActivity.class);
                    intent2.putExtra("activityType", WATER);
                    startActivity(intent2);
                    break;
                case "meditate":
                    Intent intent3 = new Intent(context, TrendsActivity.class);
                    intent3.putExtra("activityType", MEDITATION);
                    startActivity(intent3);
                    break;
                case "quiz":
                    Intent intent4 = new Intent(context, QuizathonViewAllActivity.class);
                    intent4.putExtra("type", "quiz");
                    intent4.putExtra("quiz_cat", "All");
                    intent4.putExtra("name", "Play and Learn");
                    startActivity(intent4);
                    break;
                case "webinars":
                case "healthtv":
                    Intent intent5 = new Intent(context, HealthHacksActivity.class);
                    intent5.putExtra("cameFrom", "rewards");
                    startActivity(intent5);
                    break;
                case "eng":
                    ((RewardsActivity) context).binding.viewpager.setCurrentItem(1);
                    break;
                case "ehr":
                    Intent intent6 = new Intent(context, EhrActivity.class);
                    startActivity(intent6);
                    break;
                case "syncdevice":
                    Intent intent7;
                    intent7 = new Intent(context, SyncDeviceActivity.class);
                    startActivity(intent7);
                    /*if(SharedPref.getGoogleFitStatus()){

                    }*/
                    break;
                case "invite":
                    intent7 = new Intent(context, ContactsActivityNew.class);
                    intent7.putExtra("comingFrom", "share");
                    intent7.putExtra("isFromHRA", true);
                    startActivity(intent7);
                    break;
                case "journal":
                    intent7 = new Intent(context, AddDiaryActivity.class);
                    intent7.putExtra("title", "");
                    intent7.putExtra("content", "");
                    intent7.putExtra("date", "");
                    intent7.putExtra("came_from", "add");
                    intent7.putExtra("id", 0);
                    intent7.putExtra("imagePath", "");
                    startActivity(intent7);
                    break;
                case "unwind":
                    intent7 = new Intent(context, UnwindActivity.class);
                    intent7.putExtra("comingFrom", "level");
                    startActivity(intent7);
                    break;
                case "calorieintake":
                    openWebView(CommonUtils.getBaseUrlForAddFood(context));
                    break;
                case "calorieburn":
                    openWebView(CommonUtils.getBaseUrlForAddExercise(context));
                    break;
            }
        }
    }

    private void startNewActivity(LevelActivity levelActivity, String cameFrom) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        StartRewardsActivityRequest request = new StartRewardsActivityRequest(levelActivity.getActivityID(), TAG_REWARD_EVENT);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.startRewardsActivity(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_success));
                    if (response.body().isSuccess()) {
                        Toast.makeText(context, "Activity has been started", Toast.LENGTH_SHORT).show();
                        getRewardsDashboardData();
                        levelActivity.setStarted(true);

                        if (levelActivitiesAdapter != null)
                            levelActivitiesAdapter.notifyDataSetChanged();
                        if (scratchAndWinAdapter != null)
                            scratchAndWinAdapter.notifyDataSetChanged();
                        if (levelActivity.getRedirectTo() != null) {
                            if (levelActivityAlertDialog != null) {
                                levelActivityAlertDialog.dismiss();
                            }
                            activitiesRedirection(levelActivity);
                        } else {
                            if (Objects.equals(cameFrom, "JournalUpload")) {
                                checkActivityPopUpConditionsJournalUpload(levelActivity, activityEventType);
                            } else if (Objects.equals(cameFrom, "Journal")) {
                                checkActivityPopUpConditionsJournal(levelActivity, activityEventType);
                            } else {
                                checkActivityPopUpConditions(levelActivity, activityEventType);
                            }
                        }
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_failed));

                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openWebView(String url) {
        Intent i = new Intent(context, WebActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("comingFrom", "");
        i.putExtra("Url", url);
        context.startActivity(i);
    }

    private void topUpRedirection(TopUp topUp) {
        if (topUp.getRedirectTo() != null) {
            switch (topUp.getRedirectTo().toLowerCase()) {
                case "invite":
                    Intent intent = new Intent(context, ContactsActivityNew.class);
                    intent.putExtra("isFromHRA", true);
                    intent.putExtra("comingFrom", "share");
                    startActivity(intent);
                    break;
                case "quiz":
                    intent = new Intent(context, QuizathonViewAllActivity.class);
                    intent.putExtra("type", "quiz");
                    intent.putExtra("quiz_cat", "All");
                    intent.putExtra("name", "Play and Learn");
                    startActivityForResult(intent, 2121);
                    break;
                case "happy footprint":
                    intent = new Intent(context, TrendsActivity.class);
                    intent.putExtra("activityType", STEPS);
                    startActivity(intent);
                    break;
            }
        }

    }

    private void startTopUps(TopUp topUp) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        StartRewardsActivityRequest request = new StartRewardsActivityRequest(topUp.getTopUpID(), TAG_TOP_UP_EVENT);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.startRewardsActivity(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_success));
                    if (response.body().isSuccess()) {
                        Toast.makeText(context, "Top up has been started", Toast.LENGTH_SHORT).show();
                        getRewardsDashboardData();
                        topUp.setStarted(true);
                        if (levelActivitiesAdapter != null)
                            levelActivitiesAdapter.notifyDataSetChanged();
                        if (topUpAdapter != null)
                            topUpAdapter.notifyDataSetChanged();
                        if (scratchAndWinAdapter != null)
                            scratchAndWinAdapter.notifyDataSetChanged();
                        if (!topUp.getEventType().equalsIgnoreCase("Feedback")) {
                            if (topUp.getRedirectTo() != null) {
                                if (alertDialog != null && alertDialog.isShowing())
                                    alertDialog.dismiss();
                                if (topUp.getTopUpName().contains("Gift")) {
                                    if (levelActivityJournalUploadAlertDialog != null)
                                        levelActivityJournalUploadAlertDialog.dismiss();
                                    Intent intent = new Intent(context, NewHappyMartActivity.class);
                                    startActivity(intent);
                                }
                                topUpRedirection(topUp);
                            } else {
                                if (topUp.getEventType().equalsIgnoreCase("JournalUpload")) {
                                    checkActivityPopUpConditionsJournalUploadTopUp(topUp, topUp.getEventType());
                                } else {
                                    checkTopUpCondition(topUp);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_failed));

                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFiles(List<MultipartBody.Part> parts, LevelActivity levelActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<CommonSuccessResponse> call = apiInterfaceWyh.uploadLevelFiles(SharedPref.getAuthToken(), parts,
                levelActivity.getActivityID(), levelActivity.getEventType(), TAG_REWARD_EVENT, levelActivity.getActivityName(), yesNoAnswer, journalContent);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_rewards_success));
                    if (response.body().isSuccess()) {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        getRewardsDashboardData();
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_rewards_failed));

                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_rewards_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileWithContent(List<MultipartBody.Part> parts, LevelActivity levelActivity) {
        CommonUtils.showProgressDialige(context);
        String filename = fileDataList.get(0).getPath().substring(fileDataList.get(0).getPath().lastIndexOf("/") + 1);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
//                .addFormDataPart(fileUploadKey, filename,
//                        RequestBody.create(MediaType.parse("application/octet-stream"), new File(fileDataList.get(0).getPath())))
                .addFormDataPart("eventId", String.valueOf(levelActivity.getActivityID()))
                .addFormDataPart("eventType", levelActivity.getEventType())
                .addFormDataPart("eventName", levelActivity.getActivityTag())
                .addFormDataPart("yesNo", yesNoAnswer == null ? "" : yesNoAnswer)
                .addFormDataPart("journalContent", journalContent == null ? "" : journalContent)
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
                CommonUtils.dismissDialoge();
                levelActivityJournalUploadAlertDialog.dismiss();
                deleteImage();
                fileDataList.clear();
//                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                CommonUtils.dismissDialoge();
                deleteImage();
                fileDataList.clear();
                if (response.code() == 200 && response.body() != null) {
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    if (commonSuccessResponse.isSuccess()) {
                        ((RewardsActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NewDashboardHelper.Companion.getPopUpShowModels().clear();
                                levelActivityJournalUploadAlertDialog.dismiss();
                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
//                        showRewardsPopup(commonSuccessResponse.getRewards().getReward());
                                    Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                    getRewardsDashboardData();
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                                }

                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
//                        showBonusRewardsPopup(commonSuccessResponse.getRewards().getBonusRewards(), context);
                                    Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                    getRewardsDashboardData();
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                                }

                                if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getTokens() != null) {
//                        showStampsPopup(commonSuccessResponse.getEnGTokens().getTokens(), context);
                                    Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                    getRewardsDashboardData();
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, commonSuccessResponse.getEnGTokens().getTokens()));
                                }
                                if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getBonusTokens() != null && !TextUtils.isEmpty(commonSuccessResponse.getEnGTokens().getBonusTokens())) {
//                        showBonusRewardsPopup(commonSuccessResponse.getEnGTokens().getBonusTokens(), context);
                                    Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                    getRewardsDashboardData();
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, commonSuccessResponse.getEnGTokens().getBonusTokens()));
                                }

                                showRewardsPopupDialogBox();
                            }
                        });
                    }
                }
            }
        });
    }

    private void showRewardsPopupDialogBox() {
        if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0) {
            int i = 0;
            PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 1 && (Objects.equals(firstData.getKey(), "Rewards"))) {
                i = 1;
                firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            }

            switch (firstData.getKey()) {
                case "Rewards":
                    showRewardsPopupNew(firstData.getValue(), context);
                    break;
                case "RewardsBounce":
                    showBonusRewardsPopup(firstData.getValue(), context);
                case "TokenStamp":
                    showStampsPopup(firstData.getValue(), context);
                    break;
                case "TokenStampBounce":
                    showBonusStampPopup(firstData.getValue(), context);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        } else {
            if (!Objects.equals(congratsText, "")) {
                showCongratsPopup(congratsText);
            } else {
                if (freeVoucherList.size() > 0) {
                    for (FreeVoucher data : freeVoucherList) {
                        if (!data.isScratched() && !SharedPref.getIsScratchedFirstCard()) {

                            showScratchCard(data, R.drawable.scratch_card_orange_new);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void showStampsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];

        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
//            stampId = Integer.parseInt(id);
        }
        alertDialog.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(context, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                context.startActivity(intent);
            } else {
                showRewardsPopupDialogBox();
            }
        });


        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


//        binding.scratchView.onFullReveal();
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener((RewardsActivity) context);
        binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();


        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
//            stampId = Integer.parseInt(id);
        }
        alertDialog.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(context, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                context.startActivity(intent);
            } else {
                showRewardsPopupDialogBox();
            }
        });

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);

        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener((RewardsActivity) context);
        binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusRewardsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusRewards = alertBuilder.create();
        alertDialogBonusRewards.setCancelable(false);
        if (!alertDialogBonusRewards.isShowing())
            alertDialogBonusRewards.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);


        binding.btnPositive.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
        });
        binding.scratchView.setScratchListener((RewardsActivity) context);
        binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusRewards.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showRewardsPopupNew(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 3000);

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (tempPopup != null && tempPopup.size() > 0) {
                    removeRewardsPopup(tempPopup.get(0).getActivityId());
                } else {
                    showRewardsPopupDialogBox();
                }
            }
        });

        binding.scratchView.onFullReveal();
//        binding.btnPositive.setText("Collect");
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener((RewardsActivity) context);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void uploadFileWithContentTopUP(List<MultipartBody.Part> parts, TopUp topUp) {
        CommonUtils.showProgressDialige(context);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
//                .addFormDataPart(fileUploadKey, filename,
//                        RequestBody.create(MediaType.parse("application/octet-stream"), new File(fileDataList.get(0).getPath())))
                .addFormDataPart("eventId", String.valueOf(topUp.getTopUpID()))
                .addFormDataPart("eventType", topUp.getEventType())
                .addFormDataPart("eventName", topUp.getTopUpTag())
                .addFormDataPart("yesNo", yesNoAnswer == null ? "" : yesNoAnswer)
                .addFormDataPart("journalContent", journalContent == null ? "" : journalContent)
                .addFormDataPart("eventCategory", TAG_TOP_UP_EVENT)
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "Rewards/EarnRewards")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                CommonUtils.dismissDialoge();
                levelActivityJournalUploadAlertDialog.dismiss();
                deleteImage();
                fileDataList.clear();
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                CommonUtils.dismissDialoge();
                deleteImage();
                fileDataList.clear();
                if (response.code() == 200 && response.body() != null) {
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    if (commonSuccessResponse.isSuccess()) {
                        ((RewardsActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                levelActivityJournalUploadAlertDialog.dismiss();
                                Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                uploadFileTopUp = null;
                                NewDashboardHelper.Companion.getPopUpShowModels().clear();
                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                                }

                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                                }

                                getRewardsDashboardData();
                                showRewardsPopupDialogBox();
                            }
                        });
                    }
                }
            }
        });
    }

    private void uploadContentOnly(LevelActivity levelActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("ActivityUploads", "")
                .addFormDataPart("eventId", String.valueOf(levelActivity.getActivityID()))
                .addFormDataPart("eventType", levelActivity.getEventType())
                .addFormDataPart("eventName", levelActivity.getActivityTag())
                .addFormDataPart("yesNo", yesNoAnswer == null ? "" : yesNoAnswer)
                .addFormDataPart("journalContent", journalContent == null ? "" : journalContent)
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
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.code() == 200 && response.body() != null) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    Looper.prepare();
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(() -> Toast.makeText(context, getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show());
                    getRewardsDashboardData();
                    ((RewardsActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NewDashboardHelper.Companion.getPopUpShowModels().clear();
                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
//                        showRewardsPopup(commonSuccessResponse.getRewards().getReward());
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                            }

                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
//                        showBonusRewardsPopup(commonSuccessResponse.getRewards().getBonusRewards(), context);
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                            }

                            showRewardsPopupDialogBox();
                        }
                    });
                }
            }
        });
    }

    private void uploadContentOnlyTopUP(TopUp topUp) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("ActivityUploads", "")
                .addFormDataPart("eventId", String.valueOf(topUp.getTopUpID()))
                .addFormDataPart("eventType", topUp.getEventType())
                .addFormDataPart("eventName", topUp.getTopUpTag())
                .addFormDataPart("yesNo", yesNoAnswer == null ? "" : yesNoAnswer)
                .addFormDataPart("journalContent", journalContent == null ? "" : journalContent)
                .addFormDataPart("eventCategory", TAG_TOP_UP_EVENT)
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "Rewards/EarnRewards")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.code() == 200 && response.body() != null) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    ((RewardsActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                            getRewardsDashboardData();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemClickScratchCard(FreeVoucher freeVoucher, int bgDrawable) {
        showScratchCard(freeVoucher, bgDrawable);
    }

    private void showScratchCard(FreeVoucher freeVoucher, int drawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        PopUpScratchCardScratchableBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pop_up_scratch_card_scratchable, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogScratched = alertBuilder.create();
        alertDialogScratched.setCancelable(true);
        if (!alertDialogScratched.isShowing())
            alertDialogScratched.show();

        if (freeVoucher.isScratched()) {
            binding.scratchView.onFullReveal();
        }

        if (freeVoucher.getVoucherCode() != null) {
            binding.llCopy.setVisibility(View.VISIBLE);
            binding.btnRedeem.setVisibility(View.GONE);
            binding.llScratchview.setVisibility(View.GONE);
            binding.tvAmount.setVisibility(View.GONE);
        } else {
            binding.llCopy.setVisibility(View.GONE);
            binding.btnRedeem.setVisibility(View.VISIBLE);
            binding.llScratchview.setVisibility(View.VISIBLE);
        }

        if (!freeVoucher.isScratched())
            voucherIdRequest = new VoucherIdRequest(freeVoucher.getFreebieID());

        if (!SharedPref.getIsScratchedFirstCard())
            SharedPref.putIsScratchedFirstCard(true);

        binding.scratchView.setScratchListener((RewardsActivity) context);
//        binding.llScratchview.setVisibility(View.VISIBLE);
        binding.scratchView.setScratchDrawable(ContextCompat.getDrawable(context, drawable));
        binding.tvTitle.setText(freeVoucher.getVoucherTitle());
        binding.tvValue.setText(freeVoucher.getVoucherValue() + " off");
        binding.tvDescription.setText(freeVoucher.getVoucherDescription());
        binding.tvCouponCode.setText(freeVoucher.getVoucherCode());
        binding.tvAmount.setText("₹" + freeVoucher.getVoucherValue());

        /*alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                alertDialog.dismiss();
            }
        });*/


        binding.btnRedeem.setOnClickListener(view -> {
            alertDialogScratched.dismiss();
            Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
            if (freeVoucher.getVendorName().toLowerCase().contains("hobby tribe")) {
                intent.putExtra("came_from", Constants.HappyMartHobby);
            } else if (freeVoucher.getVendorName().toLowerCase().contains("mind")) {
                intent.putExtra("came_from", Constants.HappyMentalWellbeing);
            } else if (freeVoucher.getVendorName().toLowerCase().contains("pharmeasy")) {
                intent.putExtra("came_from", Constants.HappyMartPharmacy);
            } else if (freeVoucher.getVendorName().toLowerCase().contains("medpay")) {
                intent.putExtra("came_from", Constants.HappyMartOPD);
            } else if (freeVoucher.getVendorName().toLowerCase().contains("coach")) {
                intent.putExtra("came_from", Constants.HappyMartFitness);
            } else if (freeVoucher.getVendorName().toLowerCase().contains("acto")) {
                intent.putExtra("came_from", Constants.HappyMartDevice);
            } else if (freeVoucher.getVendorName().toLowerCase().contains("connect") || freeVoucher.getVendorName().toLowerCase().contains("health")) {
                intent.putExtra("came_from", Constants.HappyMartDiagnostics);
            } else {
                intent.putExtra("came_from", Constants.HappyMartOther);
            }
            intent.putExtra("toolbarname", freeVoucher.getVendorName());
            startActivity(intent);
        });

        binding.btnNegative.setOnClickListener(view -> {
            alertDialogScratched.dismiss();
        });

        binding.tvCopy.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", freeVoucher.getVoucherCode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogScratched.dismiss();
        });

        Glide.with(context)
                .load(freeVoucher.getVendorLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivVendorLogo);

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogScratched.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogScratched.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }

    private void checkLevel(int level) {
        /*int previousLevel = SharedPref.getCurrentLevel();
        if (level > previousLevel) {
            showCongratsPopup(level);
        }*/
    }

    private void showCongratsPopup(String level) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        CongratsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.congrats_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();


        SharedPref.putCurrentLevel(Integer.parseInt(level));

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                congratsText = "";
                showRewardsPopupDialogBox();
            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 3000);

        binding.tvLevel.setText("Level " + level);

        binding.btnPositive.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void removeRewardsPopup(int activityId) {
        RewardsPopupRequest request = new RewardsPopupRequest(activityId);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.rewardsPopup(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_popup_success));
                    tempPopup.remove(0);
                    if (tempPopup != null && tempPopup.size() > 0) {
                        showRewardsPopupNew(tempPopup.get(0).getPopupMessage(), context);
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_popup_failed));

                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_popup_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void topUpFeedback(String feedback) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AppFeedbackRequest request = new AppFeedbackRequest(feedback);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.topUpFeedback(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.app_feedback_success));
                    Toast.makeText(context, "Feedback sent successfully", Toast.LENGTH_SHORT).show();
                    getRewardsDashboardData();
                    NewDashboardHelper.Companion.getPopUpShowModels().clear();
                    if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, response.body().getRewards().getBonusRewards()));
                    }
                    if (response.body().getRewards() != null && response.body().getRewards().getReward() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, response.body().getRewards().getReward()));
                    }
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                    }
                    if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0)
                        showRewardsPopupDialogBox();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.app_feedback_failed));

                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.app_feedback_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void deleteImage() {
        if (imageName != "") {
            CommonUtils.deleteImage(imageName);
            imageName = "";
        }
    }


}