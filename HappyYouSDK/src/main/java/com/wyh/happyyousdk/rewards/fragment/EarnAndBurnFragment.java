package com.wyh.happyyousdk.rewards.fragment;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;
import static com.wyh.happyyousdk.utils.Constants.WEIGHT;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
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

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.WebActivity;
import com.wyh.happyyousdk.WellBeingActivity;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;
import com.wyh.happyyousdk.contacts.ContactsActivityNew;

import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.CustomPopUpFeedbackBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalUploadBinding;
import com.wyh.happyyousdk.databinding.CustomPopupRewardsBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.EarnAndBurnFragmentBinding;
import com.wyh.happyyousdk.ehr.EhrActivity;
import com.wyh.happyyousdk.happyMarket.NewHappyMartActivity;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.earnAndGrab.BurnTokenRequest;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.request.earnAndGrab.StartActivityRequest;
import com.wyh.happyyousdk.model.request.rewards.ActivityProgressRequest;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.rewards.ActivityProgressResponse;
import com.wyh.happyyousdk.model.response.rewards.EarnPopModel;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity;
import com.wyh.happyyousdk.rewards.PendingActivityDashboard;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.rewards.UnscratchedTokensActivity;
import com.wyh.happyyousdk.rewards.adapter.ActivitiesAdapter;
import com.wyh.happyyousdk.rewards.adapter.GrabOpportunitiesAdapter;
import com.wyh.happyyousdk.rewards.adapter.UnlockedAdapter;
import com.wyh.happyyousdk.rewards.adapter.UnscratchedAdapter;
import com.wyh.happyyousdk.model.request.rewards.AppFeedbackRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.response.rewards.EandBAvailableVoucher;
import com.wyh.happyyousdk.model.response.rewards.EandBDashboardResponse;
import com.wyh.happyyousdk.model.response.rewards.EandBPendingActivity;
import com.wyh.happyyousdk.model.response.rewards.EandBUnlockedVoucher;
import com.wyh.happyyousdk.model.response.rewards.EandBUserData;
import com.wyh.happyyousdk.model.response.rewards.UnscratchedTokensData;
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarnAndBurnFragment extends Fragment implements ActivitiesAdapter.ClickListenerInterface,
        GrabOpportunitiesAdapter.ClickListenerInterface, UnlockedAdapter.ClickListenerInterface, ScratchListener, UnscratchedAdapter.ClickListenerInterface {
    EarnAndBurnFragmentBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    EandBUserData userData;
    AlertDialog alertDialog, levelActivityAlertDialog, levelActivityJournalUploadAlertDialog, customPopupRewardsAlertDialog;
    ArrayList<FileData> fileDataList = new ArrayList<>();
    int positionGrabOpportunities = 0, positionPendingActivity = 0, positionUnlocked = 0;
    EandBPendingActivity uploadFileActivity;

    String imageName = "";
    private static final int CAMERA_PERMISSION_CODE = 100;
    public int totalStamp = 0;
    CustomPopupRewardsBinding customPopupRewardsBinding;
    CustomPopupRewardsBinding levelActivityBinding;
    CustomPopUpRewardsJournalBinding levelActivityJournalBinding;
    CustomPopUpRewardsJournalUploadBinding customPopUpRewardsJournalUploadBinding;
    String activityEventType = "";
    String fileUploadKey = "ActivityUploads", journalContent;
    boolean isRewards, isScratch = false, isPositiveBtn = false;
    int stampId = -1;

    List<EarnPopModel> tempPopup = new ArrayList<>();
    public AlertDialog alertDialogStamp, alertDialogBonusStamp;


    public EarnAndBurnFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.earn_and_burn_fragment, container, false);

        context = getActivity();
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        SharedPreference.init(context);
        SharedPref.init(context);

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


        binding.rlActivities.setOnClickListener(view -> {
            Intent i = new Intent(context, PendingActivityDashboard.class);
            startActivity(i);
        });
        binding.rlGrabOpportunities.setOnClickListener(view -> {
            Intent i = new Intent(context, UnscratchedTokensActivity.class);
            startActivity(i);
        });


        if(RewardsActivity.comingFrom.equalsIgnoreCase("NewDashboard")){
            binding.extraHeight.setVisibility(View.VISIBLE);
        }else{
            binding.extraHeight.setVisibility(View.GONE);

        }

        binding.btnRedeem.setOnClickListener(view -> {
            showConcernInfoLayout();
        });

        binding.earnBurnInfo.setOnClickListener(new View.OnClickListener() {
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
        getEearnAndBurnDashboardData();
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
        customYesNoDialog.binding.txtInfoPopUpDesc.setText("Thank you for your interest. The offers you are looking for are available for a limited period on a first come, first served basis. Please watch this space for more details.");
        customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
        customYesNoDialog.binding.btnYes.setText("OK");
        customYesNoDialog.binding.btnYes.setBackground(ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp));
        customYesNoDialog.binding.btnYes.setOnClickListener(view1 -> customYesNoDialog.dismiss());

    }


    public void getEearnAndBurnDashboardData() {
        Call<EandBDashboardResponse> call = apiInterfaceWyh.getEandBDashboardData(SharedPref.getAuthToken());
        call.enqueue(new Callback<EandBDashboardResponse>() {
            @Override
            public void onResponse(Call<EandBDashboardResponse> call, Response<EandBDashboardResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_user_dashboard_success));

                    List<EandBAvailableVoucher> availableVoucherList = response.body().getData().getAvailableVoucher();
                    List<EandBPendingActivity> pendingActivityList = response.body().getData().getPendingActivity();
                    List<EandBUnlockedVoucher> unlockedVoucherList = response.body().getData().getUnlockedVoucher();
                    List<UnscratchedTokensData> unscratchedTokensList = response.body().getData().getUnscratchedTokens();

                    userData = response.body().getData().getUserData();

                    binding.progressMilestone.setMax(userData.getCurrentMileStoneTotalEvents());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.progressMilestone.setProgress(userData.getCurrentMileStoneEventsCompleted(), true);
                    } else {
                        binding.progressMilestone.setProgress(userData.getCurrentMileStoneEventsCompleted());
                    }

                    int currentMilestone = response.body().getData().getUserData().getCurrentMileStone();
                    totalStamp = userData.getUserTokens();
                    SharedPref.putStampsHistory(totalStamp);
                    binding.tvMilestone.setText("Milestone " + currentMilestone);
                    binding.tvGrabStamps.setText("" + totalStamp);
                    binding.tvNextMilestoneMsg.setText("Minimum Grab stamps to next Milestone: " + userData.getCurrentMileStoneTotalEvents());
                    setActivitiesCompletedView(currentMilestone);

                    int nextMilestone = currentMilestone + 1;
                    binding.tvNextMilestone.setText("Milestone: " + nextMilestone);

                    /*if (availableVoucherList.size() > 0) {
                        binding.llGrabOpportunities.setVisibility(View.VISIBLE);
                        setGrabOpportunitiesAdapter(availableVoucherList);
                    } else {
                        binding.llGrabOpportunities.setVisibility(View.GONE);
                    }*/

                    if (pendingActivityList.size() > 0) {
                        binding.llActivities.setVisibility(View.VISIBLE);
                        setActivitiesAdapter(pendingActivityList);
                    } else {
                        binding.llActivities.setVisibility(View.GONE);
                    }
                    if (unlockedVoucherList.size() > 0) {
                        binding.llUnlocked.setVisibility(View.VISIBLE);
                        setUnlockedVoucherAdapter(unlockedVoucherList);
                    } else {
                        binding.llUnlocked.setVisibility(View.GONE);
                    }
                    if (unscratchedTokensList.size() > 0) {
                        List<UnscratchedTokensData> sortData = new ArrayList<>();
                        List<UnscratchedTokensData> redCard = new ArrayList<>();
                        List<UnscratchedTokensData> orangeCard = new ArrayList<>();
                        for (UnscratchedTokensData data : unscratchedTokensList) {
                            if (data.getActivityId() > 0) {
                                redCard.add(data);
                            } else {
                                orangeCard.add(data);
                            }
                        }
                        sortData.addAll(redCard);
                        sortData.addAll(orangeCard);

                        binding.llGrabOpportunities.setVisibility(View.VISIBLE);
                        setUnscratchedTokensAdapter(sortData);
                    } else {
                        binding.llGrabOpportunities.setVisibility(View.VISIBLE);
                        setUnscratchedTokensAdapter(unscratchedTokensList);
                    }

                    /*if(unscratchedTokensList.size() > 0){
                        binding.llGrabOpportunities.setVisibility(View.VISIBLE);
                        setUnscratchedTokensAdapter(unscratchedTokensList);
                    }else{
                        binding.llGrabOpportunities.setVisibility(View.GONE);
                    }*/

                    NewDashboardHelper.Companion.getPopUpShowModels().clear();
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));
                    }

                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                    }

                    if (response.body().getData().getEngPopup() != null && response.body().getData().getEngPopup().size() > 0) {
                        tempPopup = response.body().getData().getEngPopup();
                        String value = tempPopup.get(0).getActivityName() + ";Congratulations!;" + tempPopup.get(0).getActivityToken() + ";" + tempPopup.get(0).getActivityId();
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, value));
//                            showRewardsPopupNew(tempPopup.get(0).getPopupMessage(), context);
                    }

                    showRewardsPopupDialogBox();

//                    showStampsPopup("Stamps Tribe;Walk minimum 60,000 steps per week.;3", context, false);

                } else if (response.code() == 401) {
                    refreshAuthToken();
                } else {
                    Analytics.logEvent(context, "","A_106_"+response.code()+"_"+SharedPref.getEncryptedMobileNo());
                    
                }
            }

            @Override
            public void onFailure(Call<EandBDashboardResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, "","A_106_Failed"+SharedPref.getEncryptedMobileNo());
                
            }
        });
    }

    private void setActivitiesCompletedView(int currentMilestone) {

        switch (userData.getCurrentMileStoneTotalEvents()) {
            case 0:
                binding.ivFirst.setVisibility(View.INVISIBLE);
                binding.ivSecond.setVisibility(View.INVISIBLE);
                binding.ivThird.setVisibility(View.INVISIBLE);
                binding.ivFourth.setVisibility(View.INVISIBLE);
                binding.ivFifth.setVisibility(View.INVISIBLE);
                binding.ivSixth.setVisibility(View.INVISIBLE);
                break;
            case 1:
                binding.ivFirst.setVisibility(View.VISIBLE);
                binding.ivSecond.setVisibility(View.INVISIBLE);
                binding.ivThird.setVisibility(View.INVISIBLE);
                binding.ivFourth.setVisibility(View.INVISIBLE);
                binding.ivFifth.setVisibility(View.INVISIBLE);
                binding.ivSixth.setVisibility(View.INVISIBLE);
                break;
            case 2:
                binding.ivFirst.setVisibility(View.VISIBLE);
                binding.ivSecond.setVisibility(View.VISIBLE);
                binding.ivThird.setVisibility(View.INVISIBLE);
                binding.ivFourth.setVisibility(View.INVISIBLE);
                binding.ivFifth.setVisibility(View.INVISIBLE);
                binding.ivSixth.setVisibility(View.INVISIBLE);
                break;
            case 3:
                binding.ivFirst.setVisibility(View.VISIBLE);
                binding.ivSecond.setVisibility(View.VISIBLE);
                binding.ivThird.setVisibility(View.VISIBLE);
                binding.ivFourth.setVisibility(View.INVISIBLE);
                binding.ivFifth.setVisibility(View.INVISIBLE);
                binding.ivSixth.setVisibility(View.INVISIBLE);
                break;
            case 4:
                binding.ivFirst.setVisibility(View.VISIBLE);
                binding.ivSecond.setVisibility(View.VISIBLE);
                binding.ivThird.setVisibility(View.VISIBLE);
                binding.ivFourth.setVisibility(View.VISIBLE);
                binding.ivFifth.setVisibility(View.INVISIBLE);
                binding.ivSixth.setVisibility(View.INVISIBLE);
                break;
            case 5:
                binding.ivFirst.setVisibility(View.VISIBLE);
                binding.ivSecond.setVisibility(View.VISIBLE);
                binding.ivThird.setVisibility(View.VISIBLE);
                binding.ivFourth.setVisibility(View.VISIBLE);
                binding.ivFifth.setVisibility(View.VISIBLE);
                binding.ivSixth.setVisibility(View.INVISIBLE);
                break;
            case 6:
                binding.ivFirst.setVisibility(View.VISIBLE);
                binding.ivSecond.setVisibility(View.VISIBLE);
                binding.ivThird.setVisibility(View.VISIBLE);
                binding.ivFourth.setVisibility(View.VISIBLE);
                binding.ivFifth.setVisibility(View.VISIBLE);
                binding.ivSixth.setVisibility(View.VISIBLE);
                break;
        }

        if (userData.getCurrentMileStoneEventsCompleted() == 0) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (userData.getCurrentMileStoneEventsCompleted() == 1) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (userData.getCurrentMileStoneEventsCompleted() == 2) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (userData.getCurrentMileStoneEventsCompleted() == 3) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (userData.getCurrentMileStoneEventsCompleted() == 4) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (userData.getCurrentMileStoneEventsCompleted() == 5) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_lock));
        } else if (userData.getCurrentMileStoneEventsCompleted() == 6) {
            binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivThird.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFourth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivFifth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
            binding.ivSixth.setImageDrawable(context.getDrawable(R.drawable.ic_yes));
        }
    }

    private void setUnlockedVoucherAdapter(List<EandBUnlockedVoucher> unlockedVoucherList) {
        UnlockedAdapter unlockedAdapter = new UnlockedAdapter(context, unlockedVoucherList, this::onItemClickUnlockedVouchers);
        LinearLayoutManager unlockedLinearLayoutManager = new LinearLayoutManager(context);
        unlockedLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvUnlocked.setLayoutManager(unlockedLinearLayoutManager);
        binding.rvUnlocked.setAdapter(unlockedAdapter);
        binding.rvUnlocked.setOnFlingListener(null);

        LinearSnapHelper unlockedLinearSnapHelper = new SnapHelperOneByOne();
        unlockedLinearSnapHelper.attachToRecyclerView(binding.rvUnlocked);

        int indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(unlockedVoucherList.size())) / 3.0);
        LinearLayoutManager unlockedLinearLayoutManager1 = new LinearLayoutManager(context);
        unlockedLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter unlockedIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvUnlockedIndicator.setAdapter(unlockedIndicatorsAdapter);
        binding.rvUnlockedIndicator.setLayoutManager(unlockedLinearLayoutManager1);
        binding.rvUnlockedIndicator.setHasFixedSize(true);

        binding.rvUnlocked.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (unlockedLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionUnlocked = unlockedLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionUnlocked = unlockedLinearLayoutManager.findFirstVisibleItemPosition();
                    unlockedIndicatorsAdapter.updateSelectedIndex(positionUnlocked);
                }
            }
        });
    }

    private void setUnscratchedTokensAdapter(List<UnscratchedTokensData> dataList) {
        if (dataList != null) {
            if (dataList.size() > 9) {
                dataList = dataList.subList(0, 9);
            }
            UnscratchedAdapter unlockedAdapter = new UnscratchedAdapter(context, dataList, this::onItemClickUnscratchedVouchers);
            LinearLayoutManager unlockedLinearLayoutManager = new LinearLayoutManager(context);
            unlockedLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            binding.rvGrabOpportunities.setLayoutManager(unlockedLinearLayoutManager);
            binding.rvGrabOpportunities.setAdapter(unlockedAdapter);
            binding.rvGrabOpportunities.setOnFlingListener(null);

            LinearSnapHelper unlockedLinearSnapHelper = new SnapHelperOneByOne();
            unlockedLinearSnapHelper.attachToRecyclerView(binding.rvGrabOpportunities);

            int indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(dataList.size())) / 3.0) + 1;

            if (dataList.size() > 6) {
                indicatorSize = 3;
            }

            if (indicatorSize > 1) {
                binding.rvGrabOpportunitiesIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.rvGrabOpportunitiesIndicator.setVisibility(View.GONE);
            }

            LinearLayoutManager unlockedLinearLayoutManager1 = new LinearLayoutManager(context);
            unlockedLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            IndicatorsAdapter unlockedIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
            binding.rvGrabOpportunitiesIndicator.setAdapter(unlockedIndicatorsAdapter);
            binding.rvGrabOpportunitiesIndicator.setLayoutManager(unlockedLinearLayoutManager1);
            binding.rvGrabOpportunitiesIndicator.setHasFixedSize(true);

            binding.rvGrabOpportunities.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (unlockedLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            positionGrabOpportunities = unlockedLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        } else
                            positionGrabOpportunities = unlockedLinearLayoutManager.findFirstVisibleItemPosition();
                        unlockedIndicatorsAdapter.updateSelectedIndex(positionGrabOpportunities);
                    }
                }
            });
        }
    }

    private void setActivitiesAdapter(List<EandBPendingActivity> pendingActivityList) {
        List<EandBPendingActivity> inProgress = new ArrayList<>();
        List<EandBPendingActivity> notStarted = new ArrayList<>();
        List<EandBPendingActivity> completed = new ArrayList<>();
        for (int i = 0; i < pendingActivityList.size(); i++) {
            if (pendingActivityList.get(i).getIsStarted() == 1 && pendingActivityList.get(i).getIsCompleted() == 0)
                inProgress.add(pendingActivityList.get(i));
            else if (pendingActivityList.get(i).getIsStarted() == 0 && pendingActivityList.get(i).getIsCompleted() == 0)
                notStarted.add(pendingActivityList.get(i));
            else
                completed.add(pendingActivityList.get(i));
        }
        pendingActivityList.clear();
        pendingActivityList.addAll(inProgress);
        pendingActivityList.addAll(notStarted);
        pendingActivityList.addAll(completed);
        ActivitiesAdapter activitiesAdapter = new ActivitiesAdapter(context, pendingActivityList, this);
        LinearLayoutManager activitiesLinearLayoutManager = new LinearLayoutManager(context);
        activitiesLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvActivities.setLayoutManager(activitiesLinearLayoutManager);
        binding.rvActivities.setAdapter(activitiesAdapter);
        binding.rvActivities.setOnFlingListener(null);

        LinearSnapHelper activitiesLinearSnapHelper = new SnapHelperOneByOne();
        activitiesLinearSnapHelper.attachToRecyclerView(binding.rvActivities);

        int indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(pendingActivityList.size())) / 3.0);
        LinearLayoutManager activitiesLinearLayoutManager1 = new LinearLayoutManager(context);
        activitiesLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter activitiesIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvActivitiesIndicator.setAdapter(activitiesIndicatorsAdapter);
        binding.rvActivitiesIndicator.setLayoutManager(activitiesLinearLayoutManager1);
        binding.rvActivitiesIndicator.setHasFixedSize(true);

        binding.rvActivities.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (activitiesLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionPendingActivity = activitiesLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionPendingActivity = activitiesLinearLayoutManager.findFirstVisibleItemPosition();
                    activitiesIndicatorsAdapter.updateSelectedIndex(positionPendingActivity);
                }
            }
        });
    }

    private void setGrabOpportunitiesAdapter(List<EandBAvailableVoucher> grabOpportunitiesList) {
        GrabOpportunitiesAdapter grabOpportunitiesAdapter = new GrabOpportunitiesAdapter(context, grabOpportunitiesList, this);
        LinearLayoutManager trendingLinearLayoutManager = new LinearLayoutManager(context);
        trendingLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvGrabOpportunities.setLayoutManager(trendingLinearLayoutManager);
        binding.rvGrabOpportunities.setAdapter(grabOpportunitiesAdapter);
        binding.rvGrabOpportunities.setOnFlingListener(null);

        LinearSnapHelper trendingLinearSnapHelper = new SnapHelperOneByOne();
        trendingLinearSnapHelper.attachToRecyclerView(binding.rvGrabOpportunities);

        int indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(grabOpportunitiesList.size())) / 3.0);
        LinearLayoutManager trendingLinearLayoutManager1 = new LinearLayoutManager(context);
        trendingLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter trendingIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvGrabOpportunitiesIndicator.setAdapter(trendingIndicatorsAdapter);
        binding.rvGrabOpportunitiesIndicator.setLayoutManager(trendingLinearLayoutManager1);
        binding.rvGrabOpportunitiesIndicator.setHasFixedSize(true);

        binding.rvGrabOpportunities.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (trendingLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionGrabOpportunities = trendingLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionGrabOpportunities = trendingLinearLayoutManager.findFirstVisibleItemPosition();
                    trendingIndicatorsAdapter.updateSelectedIndex(positionGrabOpportunities);
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
                    getEearnAndBurnDashboardData();
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
    public void onItemClickActivities(EandBPendingActivity eandBPendingActivity, int bgDrawable) {

        if (eandBPendingActivity.getEventType().equalsIgnoreCase("JournalUpload"))
            showCustomPopUpJournalUpload(eandBPendingActivity, bgDrawable);
        else if (eandBPendingActivity.getEventType().equalsIgnoreCase("journal"))
            showCustomPopUpJournal(eandBPendingActivity, bgDrawable);
        else if (eandBPendingActivity.getEventType().equalsIgnoreCase("Feedback"))
            showCustomPopUpFeedback(eandBPendingActivity, bgDrawable);
        else
            showCustomPopUp(eandBPendingActivity, bgDrawable);
    }

    @Override
    public void onUploadClick(EandBPendingActivity eandBPendingActivity) {
        uploadFileActivity = eandBPendingActivity;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, false);
        } else
            checkImagePicker();
    }

    @Override
    public void onItemClickUnlockedVouchers(EandBUnlockedVoucher eandBUnlockedVoucher) {

    }

    @Override
    public void onItemClickNewVouchers(EandBAvailableVoucher eandBAvailableVoucher) {
        if (userData.getUserTokens() >= eandBAvailableVoucher.getTokens()) {
            showCustomPopUp(eandBAvailableVoucher);
        } else {
            CustomYesNoDialog customYesNoDialog = new CustomYesNoDialog(context, R.style.Theme_Dialog);
            customYesNoDialog.show();
            customYesNoDialog.setCancelable(false);
            customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
            customYesNoDialog.binding.llActionRequired.setVisibility(View.GONE);
            customYesNoDialog.binding.txtInfoPopUpDesc.setText("Seems like you don't have enough stamps, please earn them by completing the activities");
            customYesNoDialog.binding.btnCancel.setVisibility(View.GONE);
            customYesNoDialog.binding.btnYes.setText("OK");
            customYesNoDialog.binding.btnYes.setBackground(ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp));
            customYesNoDialog.binding.btnYes.setOnClickListener(view -> customYesNoDialog.dismiss());
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

    public void checkImagePicker() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            openImagePicker();
        } else {
            openGalleryOnly();
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
            if (uploadFileActivity.getEventType().equalsIgnoreCase("journalupload")) {
                parts.add(prepareFilePart("journalupload", fileDataList.get(0).getPath()));
                uploadFileWithContent(parts, uploadFileActivity);
            } else {
                parts.add(prepareFilePart("ActivityUploads", fileDataList.get(0).getPath()));
                uploadFileOnly(parts, uploadFileActivity.getEventId());
            }
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<MultipartBody.Part> parts = new ArrayList<>();
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
/*
                if(singleFileSize > 1000000){

                }else{
                    fileDataList.add(fileData);
                }*/
            }
            if (fileDataList.size() > 0) {
                if (uploadFileActivity.getEventType().equalsIgnoreCase("journalupload")) {
                    parts.add(prepareFilePart("journalupload", fileDataList.get(0).getPath()));
                    uploadFileWithContent(parts, uploadFileActivity);
                } else {
                    parts.add(prepareFilePart("ActivityUploads", fileDataList.get(0).getPath()));
                    uploadFileOnly(parts, uploadFileActivity.getEventId());
                }
            }
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }

    private void uploadFileWithContent(List<MultipartBody.Part> parts, EandBPendingActivity eandBPendingActivity) {
        CommonUtils.showProgressDialige(context);
        String filename = fileDataList.get(0).getPath().substring(fileDataList.get(0).getPath().lastIndexOf("/") + 1);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
                .addFormDataPart("eventId", String.valueOf(eandBPendingActivity.getEventId()))
                .addFormDataPart("eventName", String.valueOf(eandBPendingActivity.getEventName()))
                .addFormDataPart("eventType", eandBPendingActivity.getEventType())
                .addFormDataPart("journalContent", journalContent == null ? "" : journalContent)
                .addFormDataPart("eventCategory", TAG_REWARD_EVENT)
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "EnG/EarnJournalTokens")
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
                    levelActivityJournalUploadAlertDialog.dismiss();
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    if (commonSuccessResponse.isSuccess()) {
                        uploadFileActivity = null;
                        fileDataList.clear();
                        ((RewardsActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                getEearnAndBurnDashboardData();
                                levelActivityJournalUploadAlertDialog.dismiss();
                                NewDashboardHelper.Companion.getPopUpShowModels().clear();
                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
//                        showRewardsPopup(commonSuccessResponse.getRewards().getReward());
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                                }

                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
//                        showBonusRewardsPopup(commonSuccessResponse.getRewards().getBonusRewards(), context);
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                                }

                                if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getTokens() != null) {
//                        showStampsPopup(commonSuccessResponse.getEnGTokens().getTokens(), context);
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, commonSuccessResponse.getEnGTokens().getTokens()));
                                }
                                if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getBonusTokens() != null && !TextUtils.isEmpty(commonSuccessResponse.getEnGTokens().getBonusTokens())) {
//                        showBonusRewardsPopup(commonSuccessResponse.getEnGTokens().getBonusTokens(), context);
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

    private void showCustomPopUp(EandBAvailableVoucher eandBAvailableVoucher) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        CustomPopupRewardsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_rewards, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        Glide.with(context)
                .load(eandBAvailableVoucher.getVendorLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivLogo);
        binding.rlLogo.setBackgroundResource(0);
        binding.tvTitle.setText(eandBAvailableVoucher.getVoucherTitle());
        binding.tvDescription.setText(eandBAvailableVoucher.getVoucherDescription());
        binding.btnPositive.setText("Buy");
        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        binding.btnPositive.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            burnUserStamps(eandBAvailableVoucher);
        });

        Rect displayRectangle = new Rect();
        Window window = ((RewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showCustomPopUpJournalUpload(EandBPendingActivity eandBPendingActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        customPopUpRewardsJournalUploadBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_rewards_journal_upload, null, false);
        alertBuilder.setView(customPopUpRewardsJournalUploadBinding.getRoot());
        levelActivityJournalUploadAlertDialog = alertBuilder.create();
        levelActivityJournalUploadAlertDialog.setCancelable(false);
        if (!levelActivityJournalUploadAlertDialog.isShowing())
            levelActivityJournalUploadAlertDialog.show();

        Glide.with(context)
                .load(eandBPendingActivity.getEventLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(customPopUpRewardsJournalUploadBinding.ivLogo);
        customPopUpRewardsJournalUploadBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        customPopUpRewardsJournalUploadBinding.tvTitle.setText(eandBPendingActivity.getEventName());
        customPopUpRewardsJournalUploadBinding.tvDescription.setText(eandBPendingActivity.getEventDescription());
        if (eandBPendingActivity.getIsStarted() == 1 || eandBPendingActivity.getIsCompleted() == 1) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.GONE);
        }

        activityEventType = eandBPendingActivity.getEventType();
        checkActivityPopUpConditionsJournalUpload(eandBPendingActivity, activityEventType);

        if (eandBPendingActivity.getWhatTo() != null) {
            customPopUpRewardsJournalUploadBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.tvWhatToDo.setText(eandBPendingActivity.getWhatTo());
            customPopUpRewardsJournalUploadBinding.tvHowToDo.setText(eandBPendingActivity.getHowTo());
            customPopUpRewardsJournalUploadBinding.tvWhyToDo.setText(eandBPendingActivity.getWhyTo());
        }

    }

    private void getActivityProgress(LinearProgressIndicator progressBar, EandBPendingActivity eandBPendingActivity, TextView tvSteps) {
        ActivityProgressRequest request = new ActivityProgressRequest(eandBPendingActivity.getEventId(), "EnG");
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

    private void checkActivityPopUpConditionsJournalUpload(EandBPendingActivity eandBPendingActivity, String activityEventType) {
        customPopUpRewardsJournalUploadBinding.btnNegative.setOnClickListener(view -> {
            levelActivityJournalUploadAlertDialog.dismiss();
            getEearnAndBurnDashboardData();
        });

        customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view1 -> {
            startNewActivity(eandBPendingActivity);
        });

        if (eandBPendingActivity.getIsStarted() == 1 && eandBPendingActivity.getIsCompleted() == 0) {
            customPopUpRewardsJournalUploadBinding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(customPopUpRewardsJournalUploadBinding.progressBar, eandBPendingActivity, null);
        } else if (eandBPendingActivity.getIsCompleted() == 1) {
            customPopUpRewardsJournalUploadBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100, true);
            } else
                customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100);
        }

        if (eandBPendingActivity.getIsCompleted() == 0 && eandBPendingActivity.getIsStarted() == 1
                && eandBPendingActivity.getEventType().equalsIgnoreCase("JournalUpload")) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setText("Submit");
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(customPopUpRewardsJournalUploadBinding.edtJournal.getText()) && customPopUpRewardsJournalUploadBinding.edtJournal.getText().length() >= 15) {
                    journalContent = customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString();
                    fileUploadKey = "JournalUpload";
                    uploadFileActivity = eandBPendingActivity;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        levelActivityJournalUploadAlertDialog.dismiss();
                        ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, false);
                    } else {
                        //levelActivityJournalUploadAlertDialog.dismiss();
                        checkImagePicker();

                    }
                } else {
                    customPopUpRewardsJournalUploadBinding.edtJournal.requestFocus();
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (eandBPendingActivity.getIsCompleted() == 1) {
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.GONE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.GONE);
        }

        Rect displayRectangle = new Rect();
        Window window = ((RewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        if (eandBPendingActivity.getIsStarted() == 1 && eandBPendingActivity.getIsCompleted() == 0) {
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.8f));
        } else {
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.6f));
        }


    }

    private void showCustomPopUpJournal(EandBPendingActivity eandBPendingActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        levelActivityJournalBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_rewards_journal, null, false);
        alertBuilder.setView(levelActivityJournalBinding.getRoot());
        levelActivityAlertDialog = alertBuilder.create();
        levelActivityAlertDialog.setCancelable(false);
        if (!levelActivityAlertDialog.isShowing())
            levelActivityAlertDialog.show();

        Glide.with(context)
                .load(eandBPendingActivity.getEventLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(levelActivityJournalBinding.ivLogo);
        levelActivityJournalBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        levelActivityJournalBinding.tvTitle.setText(eandBPendingActivity.getEventName());
        levelActivityJournalBinding.tvDescription.setText(eandBPendingActivity.getEventDescription());

        if (eandBPendingActivity.getIsCompleted() == 1) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
        }

        if (eandBPendingActivity.getWhatTo() != null) {
            levelActivityJournalBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.tvWhatToDo.setText(eandBPendingActivity.getWhatTo());
            levelActivityJournalBinding.tvHowToDo.setText(eandBPendingActivity.getHowTo());
            levelActivityJournalBinding.tvWhyToDo.setText(eandBPendingActivity.getWhyTo());
        }

        activityEventType = eandBPendingActivity.getEventType();

        checkActivityPopUpConditionsJournal(eandBPendingActivity, activityEventType);

        Rect displayRectangle = new Rect();
        Window window = ((RewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        levelActivityAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.8f));
    }

    private void checkActivityPopUpConditionsJournal(EandBPendingActivity eandBPendingActivity, String activityEventType) {

        if (eandBPendingActivity.getIsStarted() == 1 && activityEventType.equalsIgnoreCase("upload")) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setText("Upload");
        }

        if (eandBPendingActivity.getIsStarted() == 1 && eandBPendingActivity.getIsCompleted() == 0) {
            levelActivityJournalBinding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(levelActivityJournalBinding.progressBar, eandBPendingActivity, null);
        } else if (eandBPendingActivity.getIsCompleted() == 1) {
            levelActivityJournalBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                levelActivityJournalBinding.progressBar.setProgress(100, true);
            } else
                levelActivityJournalBinding.progressBar.setProgress(100);
        }

        if (eandBPendingActivity.getIsStarted() == 1 && activityEventType.equalsIgnoreCase("journal")) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setText("Submit");
            levelActivityJournalBinding.llJournal.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(levelActivityJournalBinding.edtJournal.getText()) &&
                        levelActivityJournalBinding.edtJournal.getText().toString().length() > 15) {
                    levelActivityAlertDialog.dismiss();
                    journalContent = levelActivityJournalBinding.edtJournal.getText().toString();
//                    uploadContentOnly(eandBPendingActivity);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                    levelActivityJournalBinding.edtJournal.requestFocus();
                }
            });
        }

        if (eandBPendingActivity.getIsStarted() == 0) {
            levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                startNewActivity(eandBPendingActivity);
            });
        }

        levelActivityJournalBinding.btnNegative.setOnClickListener(view -> {
            levelActivityAlertDialog.dismiss();
            getEearnAndBurnDashboardData();
        });


        if (eandBPendingActivity.getIsCompleted() == 1) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
            levelActivityJournalBinding.llJournal.setVisibility(View.GONE);
        }


    }

    private void burnUserStamps(EandBAvailableVoucher eandBAvailableVoucher) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        BurnTokenRequest request = new BurnTokenRequest(eandBAvailableVoucher.getTokens(), eandBAvailableVoucher.getVoucherId());
        Call<CommonSuccessResponse> call = apiInterfaceWyh.burnStamps(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_tokens_success));
                    if (response.body().isSuccess()) {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        getEearnAndBurnDashboardData();
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_tokens_failed));
                    
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_tokens_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCustomPopUp(EandBPendingActivity eandBPendingActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        customPopupRewardsBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_rewards, null, false);
        alertBuilder.setView(customPopupRewardsBinding.getRoot());
        customPopupRewardsAlertDialog = alertBuilder.create();
        customPopupRewardsAlertDialog.setCancelable(false);
        if (!customPopupRewardsAlertDialog.isShowing())
            customPopupRewardsAlertDialog.show();

        Glide.with(context)
                .load(eandBPendingActivity.getEventLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(customPopupRewardsBinding.ivLogo);
        customPopupRewardsBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        customPopupRewardsBinding.tvTitle.setText(eandBPendingActivity.getEventName());
        customPopupRewardsBinding.tvDescription.setText(eandBPendingActivity.getEventDescription());


        checkEandBPendingActivityConditions(eandBPendingActivity);

        Rect displayRectangle = new Rect();
        Window window = ((RewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        customPopupRewardsAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void checkEandBPendingActivityConditions(EandBPendingActivity eandBPendingActivity) {

        if (eandBPendingActivity.getIsStarted() == 1 && eandBPendingActivity.getIsCompleted() == 0) {
            if (eandBPendingActivity.getEventType() != null) {
                customPopupRewardsBinding.btnPositive.setVisibility(View.VISIBLE);
                if (eandBPendingActivity.getEventType().equalsIgnoreCase("upload")) {
                    customPopupRewardsBinding.btnPositive.setText("Upload");
//                    onUploadActivity(eandBPendingActivity);
                } else if (eandBPendingActivity.getEventName().toLowerCase().equalsIgnoreCase("retail therapy")) {
                    customPopupRewardsBinding.btnPositive.setText("Upload");
//                    onUploadActivity(eandBPendingActivity);
                } else {
                    customPopupRewardsBinding.btnPositive.setText("Complete");
                }
            } else {
                customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);
            }
        }

        if (eandBPendingActivity.getIsStarted() == 1 && eandBPendingActivity.getIsCompleted() == 0) {
            customPopupRewardsBinding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(customPopupRewardsBinding.progressBar, eandBPendingActivity, customPopupRewardsBinding.tvSteps);
        } else if (eandBPendingActivity.getIsCompleted() == 1) {
            customPopupRewardsBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                customPopupRewardsBinding.progressBar.setProgress(100, true);
            } else
                customPopupRewardsBinding.progressBar.setProgress(100);
        }

        if (eandBPendingActivity.getIsCompleted() == 1) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);
        }

        if (eandBPendingActivity.getWhatTo() != null) {
            customPopupRewardsBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            customPopupRewardsBinding.tvWhatToDo.setText(eandBPendingActivity.getWhatTo());
            customPopupRewardsBinding.tvHowToDo.setText(eandBPendingActivity.getHowTo());
            customPopupRewardsBinding.tvWhyToDo.setText(eandBPendingActivity.getWhyTo());
        }

        if (eandBPendingActivity.getIsCompleted() == 1) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);
        } else {
            customPopupRewardsBinding.btnPositive.setVisibility(View.VISIBLE);
        }


        customPopupRewardsBinding.btnPositive.setOnClickListener(view1 -> {
//            customPopupRewardsAlertDialog.dismiss();
            if (eandBPendingActivity.getIsStarted() == 1) {
                if (eandBPendingActivity.getEventType().equalsIgnoreCase("upload") || eandBPendingActivity.getEventName().toLowerCase().equalsIgnoreCase("retail therapy")) {
                    customPopupRewardsAlertDialog.dismiss();
                    uploadFileActivity = eandBPendingActivity;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, false);
                    } else {
                        customPopupRewardsAlertDialog.dismiss();
                        checkImagePicker();

                    }
                } else {
                    customPopupRewardsAlertDialog.dismiss();
                    activityRedirection(eandBPendingActivity);
                }
            } else {
                startNewActivity(eandBPendingActivity);
            }
        });

        customPopupRewardsBinding.btnNegative.setOnClickListener(view1 -> {
            customPopupRewardsAlertDialog.dismiss();
            getEearnAndBurnDashboardData();
        });


    }

    private void onUploadActivity(EandBPendingActivity eandBPendingActivity) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (eandBPendingActivity.getEventType() != null && eandBPendingActivity.getEventType().equalsIgnoreCase("upload")) {
            if (eandBPendingActivity.getEventEndDate() != null) {
                try {
                    Date endDate = simpleDateFormat.parse(eandBPendingActivity.getEventEndDate().split("T")[0]);
                    Calendar endDateCalendar = Calendar.getInstance();
                    Calendar todayCalendar = Calendar.getInstance();
                    Date todayDate = todayCalendar.getTime();
                    long msDiff = endDate.getTime() - todayDate.getTime();
                    long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
                    endDateCalendar.setTime(endDate);
                    if (daysDiff <= 0) {
                        customPopupRewardsBinding.btnPositive.setVisibility(View.VISIBLE);
                        customPopupRewardsBinding.btnPositive.setText("Upload");
                        customPopupRewardsBinding.btnPositive.setOnClickListener(view -> {
                            uploadFileActivity = eandBPendingActivity;
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                                ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, false);
                            } else {
                                customPopupRewardsAlertDialog.dismiss();
                                checkImagePicker();

                            }
                        });
                    } else
                        customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                customPopupRewardsBinding.btnPositive.setVisibility(View.VISIBLE);
                customPopupRewardsBinding.btnPositive.setText("Upload");
                customPopupRewardsBinding.btnPositive.setOnClickListener(view -> {
                    uploadFileActivity = eandBPendingActivity;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        ((RewardsActivity) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, false);
                    } else {
                        customPopupRewardsAlertDialog.dismiss();
                        checkImagePicker();

                    }
                });
            }
        }

    }

    private void activityRedirection(EandBPendingActivity eandBPendingActivity) {
        if (eandBPendingActivity.getEventType() != null) {
            switch (eandBPendingActivity.getEventType().toLowerCase()) {
                case "ehr":
                    Intent intent = new Intent(context, EhrActivity.class);
                    startActivity(intent);
                    break;
                case "happy mart":
                    Intent intent1 = new Intent(context, NewHappyMartActivity.class);
                    startActivity(intent1);
                    break;
                case "well being":
                    Intent intent2 = new Intent(context, WellBeingActivity.class);
                    startActivity(intent2);
                    break;
                case "googlefit":
                    Intent intent3 = new Intent(context, SyncDeviceActivity.class);
                    startActivity(intent3);
                    /*if(SharedPref.getGoogleFitStatus()){

                    }*/
                    break;
                case "quiz":
                    Intent intent4 = new Intent(context, QuizathonViewAllActivity.class);
                    intent4.putExtra("type", "quiz");
                    intent4.putExtra("quiz_cat", "All");
                    intent4.putExtra("name", "Play and Learn");
                    startActivity(intent4);
                    break;
                case "weight":
                    intent4 = new Intent(context, TrendsActivity.class);
                    intent4.putExtra("activityType", WEIGHT);
                    startActivity(intent4);
                    break;
                case "happy footprint":
                    intent4 = new Intent(context, TrendsActivity.class);
                    intent4.putExtra("activityType", STEPS);
                    startActivity(intent4);
                    break;
                case "invite":
                    intent4 = new Intent(context, ContactsActivityNew.class);
                    intent4.putExtra("comingFrom", "");
                    startActivity(intent4);
                    break;
                case "hra":
                    GetAnalysisResponse getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
                    if (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0) {
                        intent4 = new Intent(context, HRAAnalysisActivity.class);
                    } else {
                        intent4 = new Intent(context, HRAQuestionsActivity.class);
                    }
                    startActivity(intent4);
                    break;
                case "exercise tracker":
                    openWebView(getResources().getString(R.string.addExerciseUrl));
                    break;
            }
        }
    }

    private void startNewActivity(EandBPendingActivity eandBPendingActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        StartActivityRequest request = new StartActivityRequest(eandBPendingActivity.getEventId());
        Call<CommonSuccessResponse> call = apiInterfaceWyh.startEAndGActivity(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    getEearnAndBurnDashboardData();
                    if (response.body().isSuccess()) {
                        eandBPendingActivity.setIsStarted(1);
                        Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_start_activity_success));
                        Toast.makeText(context, "Activity has been started", Toast.LENGTH_SHORT).show();
                        if (!eandBPendingActivity.getEventType().equalsIgnoreCase("Feedback")) {
                            if (!eandBPendingActivity.getEventType().toLowerCase().equals("upload")) {
                                if (!eandBPendingActivity.getEventType().toLowerCase().equals("journalupload")) {
                                    if (eandBPendingActivity.getEventType() != null) {
                                        if (customPopupRewardsAlertDialog != null) {
                                            customPopupRewardsAlertDialog.dismiss();
                                        }
                                        activityRedirection(eandBPendingActivity);
                                    } else {
                                        checkEandBPendingActivityConditions(eandBPendingActivity);
                                    }
                                } else {
                                    checkActivityPopUpConditionsJournalUpload(eandBPendingActivity, eandBPendingActivity.getEventType());
                                }
                            } else {
                                checkEandBPendingActivityConditions(eandBPendingActivity);
                            }
                        }
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_start_activity_failed));
                    
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_start_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openWebView(String url) {
        Intent i = new Intent(context, WebActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("Url", url);
        context.startActivity(i);
    }

    private void uploadFiles(List<MultipartBody.Part> parts, int activityId) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<CommonSuccessResponse> call = apiInterfaceWyh.uploadEAndGFiles(SharedPref.getAuthToken(), parts, activityId);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_upload_activity_files_success));
                    if (response.body().isSuccess()) {
                        uploadFileActivity = null;
                        fileDataList.clear();
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        NewDashboardHelper.Companion.getPopUpShowModels().clear();
                        if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));
                        }

                        if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                        }

                        showRewardsPopupDialogBox();
                        getEearnAndBurnDashboardData();
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_upload_activity_files_failed));
                    
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_upload_activity_files_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileOnly(List<MultipartBody.Part> parts, int activityId) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        String filename = fileDataList.get(0).getPath().substring(fileDataList.get(0).getPath().lastIndexOf("/") + 1);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
                .addFormDataPart("ActivityId", String.valueOf(activityId))
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "EnG/UploadActivityFiles")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                deleteImage();
                fileDataList.clear();
//                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                deleteImage();
                fileDataList.clear();
                if (response.code() == 200 && response.body() != null) {
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    if (commonSuccessResponse.isSuccess()) {
                        uploadFileActivity = null;
                        fileDataList.clear();
                        ((RewardsActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                getEearnAndBurnDashboardData();
                                NewDashboardHelper.Companion.getPopUpShowModels().clear();
                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
//                        showRewardsPopup(commonSuccessResponse.getRewards().getReward());
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                                }

                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
//                        showBonusRewardsPopup(commonSuccessResponse.getRewards().getBonusRewards(), context);
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                                }

                                if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getTokens() != null) {
//                        showStampsPopup(commonSuccessResponse.getEnGTokens().getTokens(), context);
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, commonSuccessResponse.getEnGTokens().getTokens()));
                                }
                                if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getBonusTokens() != null && !TextUtils.isEmpty(commonSuccessResponse.getEnGTokens().getBonusTokens())) {
//                        showBonusRewardsPopup(commonSuccessResponse.getEnGTokens().getBonusTokens(), context);
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

    private void showCustomPopUpFeedback(EandBPendingActivity eandBPendingActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        CustomPopUpFeedbackBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_feedback, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        Glide.with(context)
                .load(eandBPendingActivity.getEventLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivLogo);
        binding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        binding.tvTitle.setText(eandBPendingActivity.getEventName());
        binding.tvDescription.setText(eandBPendingActivity.getEventDescription());
        if (eandBPendingActivity.getIsStarted() == 1) {
            binding.btnPositive.setVisibility(View.GONE);
        }

        if (eandBPendingActivity.getWhatTo() != null) {
            binding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            binding.tvWhatToDo.setText(eandBPendingActivity.getWhatTo());
            binding.tvHowToDo.setText(eandBPendingActivity.getHowTo());
            binding.tvWhyToDo.setText(eandBPendingActivity.getWhyTo());
        }

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        if (eandBPendingActivity.getIsStarted() == 1 && eandBPendingActivity.getIsCompleted() == 0) {
            binding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(binding.progressBar, eandBPendingActivity, null);
        } else if (eandBPendingActivity.getIsCompleted() == 1) {
            binding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.progressBar.setProgress(100, true);
            } else
                binding.progressBar.setProgress(100);
        }

        binding.btnPositive.setOnClickListener(view1 -> {
            startNewActivity(eandBPendingActivity);
            binding.btnPositive.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnPositive.setText("Submit");
            binding.tvLabel.setText("Your feedback");
            binding.llJournal.setVisibility(View.VISIBLE);
            binding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(binding.edtFeedback.getText()) && binding.edtFeedback.getText().length() >= 35) {
                    alertDialog.dismiss();
                    appFeedback(binding.edtFeedback.getText().toString());
                } else {
                    binding.edtFeedback.requestFocus();
                    Toast.makeText(context, getResources().getString(R.string.at_least_35_char), Toast.LENGTH_SHORT).show();
                }
            });
//            startNewActivity(eandBPendingActivity);
        });

        if (eandBPendingActivity.getIsStarted() == 1 && eandBPendingActivity.getEventType().equalsIgnoreCase("Feedback")) {
            binding.btnPositive.setVisibility(View.VISIBLE);
            binding.btnPositive.setText("Submit");
            binding.tvLabel.setText("Your feedback");
            binding.llJournal.setVisibility(View.VISIBLE);
            binding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(binding.edtFeedback.getText())) {
                    alertDialog.dismiss();
                    appFeedback(binding.edtFeedback.getText().toString());
                } else {
                    binding.edtFeedback.requestFocus();
                }
            });
        }

        Rect displayRectangle = new Rect();
        Window window = ((RewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.8f));
    }

    private void appFeedback(String feedback) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AppFeedbackRequest request = new AppFeedbackRequest(feedback);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.appFeedback(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.app_feedback_success));
                    Toast.makeText(context, "Successfully Feedback sent.", Toast.LENGTH_SHORT).show();
                    NewDashboardHelper.Companion.getPopUpShowModels().clear();
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));
                    }

                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                    }

                    showRewardsPopupDialogBox();
                    getEearnAndBurnDashboardData();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.app_feedback_failed));
                    
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.app_feedback_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStampsPopup(String rewards, Context context) {
        isScratch = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogStamp = alertBuilder.create();
        alertDialogStamp.setCancelable(true);
        if (!alertDialogStamp.isShowing())
            alertDialogStamp.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

        alertDialogStamp.setOnDismissListener(dialogInterface -> {
            if (tempPopup != null && tempPopup.size() > 0) {
                tempPopup.remove(0);
                if (tempPopup.size() > 0) {
                    String msg = tempPopup.get(0).getActivityName() + ";Congratulations!;" + tempPopup.get(0).getActivityToken() + ";" + tempPopup.get(0).getActivityId();
                    showStampsPopup(msg, context);
                }
            } else if (isPositiveBtn) {
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


        binding.btnPositive.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((RewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }

    private void showBonusStampPopup(String rewards, Context context) {
        isScratch = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusStamp = alertBuilder.create();
        alertDialogBonusStamp.setCancelable(true);
        if (!alertDialogBonusStamp.isShowing())
            alertDialogBonusStamp.show();


        String[] rewardParts = rewards.split(";");
        String points = "";
        if (rewardParts.length >= 3) {
            String title = rewardParts[0];
            String message = rewardParts[1];
            points = rewardParts[2];
            if (rewardParts.length == 4) {
                String id = rewardParts[3];
                stampId = Integer.parseInt(id);
            }
        }

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);

        binding.btnPositive.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((RewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
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
        }
    }

    private void showScratchPopup(String rewards, Context context) {
        isScratch = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String points = rewards.split(";")[1];

        binding.tvTitle.setText(title);
        binding.tvDescription2.setText("No. of Stamps: " + points);

        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        if (stampId == -1) {
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));
        } else {
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));
        }

        binding.scratchView.setScratchListener(this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((RewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        /*if(i >= 20 && isScratch){
            isScratch = false;
            scratchCardLayout.onFullReveal();
            scratchTokenReward();
        } else{
            if (i >= 20 && !isRewards) {
                scratchCardLayout.onFullReveal();
            }else{
                if(i >= 20){
                    scratchCardLayout.onFullReveal();
                }
            }
        }*/

        if (i > 20) {
            if (isScratch) {
                isScratch = false;
                scratchTokenReward();
            }

            scratchCardLayout.onFullReveal();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((alertDialogBonusStamp != null && alertDialogBonusStamp.isShowing())) {
                        alertDialogBonusStamp.dismiss();
                    }
                    if ((alertDialogStamp != null && alertDialogStamp.isShowing())) {
                        alertDialogStamp.dismiss();
                    }
                }
            }, 3000);
        }
    }

    @Override
    public void onScratchStarted() {

    }

    @Override
    public void onItemClickUnscratchedVouchers(UnscratchedTokensData data) {
        stampId = data.getActivityId();
        showScratchPopup(data.getActivityName() + ";" + data.getActivityToken(), context);
    }

    private void scratchTokenReward() {
        RewardsPopupRequest request = new RewardsPopupRequest(stampId);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.scratchTokenReward(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_success));
                    getEearnAndBurnDashboardData();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                    
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
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