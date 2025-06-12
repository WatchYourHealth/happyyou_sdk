package com.wyh.happyyousdk;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.esafirm.imagepicker.features.ImagePicker;
import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.SpinWheel.Utilities.RewardInfoBottomSheet;
import com.wyh.happyyousdk.SpinWheel.Utilities.onRewardDialogClick;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.SpinWheel.spinRewardCallBack;
import com.wyh.happyyousdk.absorb.HealthTVDashboard;
import com.wyh.happyyousdk.common.adapter.ClaimReClaimAdapter;

import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.RedirectionMethod;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityAllFileShareListBinding;
import com.wyh.happyyousdk.databinding.ActivityClaimReclaimListBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.WarerIntakeLayoutBinding;
import com.wyh.happyyousdk.databinding.ZenZoneLayoutBinding;
import com.wyh.happyyousdk.fileshare.FileShareGridAllList;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.RedirectionModel;
import com.wyh.happyyousdk.model.request.postloginreward.StartSpinActivityRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.request.quizathon.ClaimRewardRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.request.trends.AddReminderDataRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;
import com.wyh.happyyousdk.model.response.FileShareResp;
import com.wyh.happyyousdk.model.response.GetRewardsClaimListResp;
import com.wyh.happyyousdk.model.response.playwin.ClaimReClaimRewardModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.model.response.postloginreward.CommonResponse;
import com.wyh.happyyousdk.model.response.postloginreward.RewardItem;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.ActivityClaimReclaimList;
import com.wyh.happyyousdk.quizathon.QuizathonScoreActivity;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityClaimReclaimList extends AppCompatActivity implements ScratchListener, rewardDialogCloseListener, onRewardClick, spinRewardCallBack {
    Context context;
    ActivityClaimReclaimListBinding binding;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    AlertDialog alertDialogBonusRewards;
    String type = "";
    boolean isPositiveBtn = false, isStamp = false;
    QuizathonRewardData quizreward;
    ApiInterfaceWyh apiInterfaceWyh;
    int stampId = -1;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    List<ClaimReClaimRewardModel> claimReClaimRewardModelList = new ArrayList<>();
    int glasses;
    DecimalFormat format = new DecimalFormat("0.##");
    RewardItem reward;
    FeedbackResponseData feedbackResponseData = null;
    String fileUploadKey = "JournalUpload";
    String titleContent = "";
    private static final int CAMERA_PERMISSION_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_claim_reclaim_list);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);
        apiInterface = RetrofitHandler.apiInterface();
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        GetClaimReclaimList();
        binding.llMain.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void GetClaimReclaimList() {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);
            apiInterface.GetRewardsClaimList(SharedPref.getAuthToken()).enqueue(new Callback<GetRewardsClaimListResp>() {
                @Override
                public void onResponse(@NonNull Call<GetRewardsClaimListResp> call, @NonNull Response<GetRewardsClaimListResp> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getData() != null && response.body().getData().getUserRewardClaims() != null && response.body().getData().getUserRewardClaims().size() > 0) {
                            claimReClaimRewardModelList.addAll(response.body().getData().getUserRewardClaims());
                        }
                        if (response.body().getData() != null && response.body().getData().getActiviRewardClaim() != null && response.body().getData().getActiviRewardClaim().size() > 0) {
                            claimReClaimRewardModelList.addAll(response.body().getData().getActiviRewardClaim());
                        }

                        if (claimReClaimRewardModelList != null && claimReClaimRewardModelList.size() > 0) {
                            binding.llm2.setVisibility(View.VISIBLE);
                            binding.tvNDF.setVisibility(View.GONE);
                            setData(claimReClaimRewardModelList);
                        } else {
                            binding.llm2.setVisibility(View.GONE);
                            binding.tvNDF.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure
                        (@NonNull Call<GetRewardsClaimListResp> call, @NonNull Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (
                Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    public void setData(List<ClaimReClaimRewardModel> claimReClaimRewardModelList) {
        binding.rlActive.setVisibility(View.VISIBLE);
        binding.gvCardFileShare.setVisibility(View.VISIBLE);
        ClaimReClaimAdapter gridCardsAdapter = new ClaimReClaimAdapter(context, claimReClaimRewardModelList, this);
        binding.gvCardFileShare.setColumnWidth(3);
        binding.gvCardFileShare.setAdapter(gridCardsAdapter);
    }

    public void ClaimRewardById(
            String transId,
            boolean isClaim,
            boolean isReclaim,
            String burnValue,
            ClaimReClaimRewardModel claimReClaimRewardModel
    ) {
        ClaimRewardRequest request = new ClaimRewardRequest(transId, isClaim, isReclaim, burnValue);

        Call<CommonSuccessResponse> call = apiInterface.ClaimRewardById(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    try {
                        if (response.body().getQuizathonRewardData() != null) {
                            quizreward = response.body().getQuizathonRewardData();
                            getQuizathonRewardPopup(quizreward);
                            GetClaimReclaimList();
                        } else {
                            String message = response.body().getMsg();
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Unable to claim the rewards", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                Toast.makeText(context, "Something went wrong! Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getQuizathonRewardPopup(QuizathonRewardData data) {
        try {
            quizreward = data;
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
                } else if (rewardtype.equalsIgnoreCase("future")) {
                    QuizRewardDialog.INSTANCE.showFutureRewardDialog(context, data.getDialogModel(), data.getClaimDate());
                }

            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void onScratchComplete() {
        if (quizreward != null) {
            QuizRewardDialog.INSTANCE.QuizScratchCard(this, quizreward.getTransId(), true);
        }
    }

    @Override
    public void onScratchStarted() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i > 20) {
            if (quizreward != null) {
                QuizRewardDialog.INSTANCE.QuizScratchCard(this, quizreward.getTransId(), true);
            } else {
                if (isStamp) {
                    isStamp = false;
                    scratchTokenReward();
                }

                scratchCardLayout.onFullReveal();


            }
        }
    }

    private void scratchTokenReward() {
        RewardsPopupRequest request = new RewardsPopupRequest(stampId);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.scratchTokenReward(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {

                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_success));

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {

                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
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
            showRewardsPopupDialogBox();
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 3000);

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");


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
        binding.scratchView.setScratchListener(ActivityClaimReclaimList.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onRewardClick(Object item, String type) {
        if (item instanceof ClaimReClaimRewardModel) {
            ClaimReClaimRewardModel claimReClaimRewardModel = (ClaimReClaimRewardModel) item;
            reward = claimReClaimRewardModel.getRewardItem();
            type = "Active";
            RewardInfoBottomSheet dialog = new RewardInfoBottomSheet(ActivityClaimReclaimList.this, reward, type, new onRewardDialogClick() {
                @Override
                public void onStartClick() {

                    StartSpinActivity(reward.getActivityTransId());

                    if (reward.getRedirectionKey().contains("feedback")) {
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedbackCallback(ActivityClaimReclaimList.this, feedbackResponseData, ActivityClaimReclaimList.this);
                    }
                }

                @Override
                public void onCompleteClick() {
                    if (reward.getRedirectionKey().contains("feedback")) {
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedbackCallback(ActivityClaimReclaimList.this, feedbackResponseData, ActivityClaimReclaimList.this);
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("journalUpload")) {
                        fileUploadKey = "JournalUpload";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            openGalleryOnly();
                        }
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("activityimageupload")) {
                        fileUploadKey = "ActivityUpload";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            openGalleryOnly();
                        }
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("water")) {
                        waterIntakeDialoge(ActivityClaimReclaimList.this, NewDashboardHelper.Companion.getWaterIntakeGoal(), NewDashboardHelper.Companion.getWaterIntake(), NewDashboardHelper.Companion.getWaterIntakeAllowed());
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("meditation")) {
                        showPopUpZenZone(ActivityClaimReclaimList.this, Double.parseDouble(NewDashboardHelper.Companion.getCurrentValueZenZone()));
                    } else {
                        RedirectionModel redirectionModel = new RedirectionModel(reward.getRedirectionKey(), false, "", "", "", "", "", "", 0, "", "", "", "", "");
                        RedirectionMethod.INSTANCE.redirectionScreen(redirectionModel, ActivityClaimReclaimList.this);
                    }
                }

                @Override
                public void onCloseClick() {
                    //cancelDialog();
                }

                @Override
                public void onSubmit(String title) {
                    titleContent = title;
                    if (reward.getRedirectionKey().equalsIgnoreCase("journalUpload")) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            openImagePicker();
                        } else {
                            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                        }
                    }
                }
            });
            dialog.setFromRewardList(true);
            dialog.show(getSupportFragmentManager(), "expandableBottomSheet");
        }
    }

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(ActivityClaimReclaimList.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ActivityClaimReclaimList.this, new String[]{permission}, requestCode);
        } else {
            openGalleryOnly();
        }
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

    public void openCameraOnly() throws IOException {
        ImagePicker.cameraOnly().imageDirectory("Camera").start(this);
    }

    public void openGalleryOnly() {
        ImagePicker.create(this)
                .includeVideo(false).multi()
                .imageDirectory("Camera")
                .limit(1)
                .enableLog(true)
                .showCamera(false)
                .start();
    }

    private void StartSpinActivity(int ActivityTransId) {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(ActivityClaimReclaimList.this);
            apiInterface.StartQuizActivity(SharedPref.getAuthToken(), new StartSpinActivityRequest(ActivityTransId)).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (reward.getRedirectionKey().equalsIgnoreCase("water")) {
                            waterIntakeDialoge(ActivityClaimReclaimList.this, NewDashboardHelper.Companion.getWaterIntakeGoal(), NewDashboardHelper.Companion.getWaterIntake(), NewDashboardHelper.Companion.getWaterIntakeAllowed());
                        } else if (reward.getRedirectionKey().equalsIgnoreCase("meditation")) {
                            showPopUpZenZone(ActivityClaimReclaimList.this, Double.parseDouble(NewDashboardHelper.Companion.getCurrentValueZenZone()));
                        } else {
                            RedirectionModel redirectionModel = new RedirectionModel(reward.getRedirectionKey(), false, "", "", "", "", "", "", 0, "", "", "", "", "");
                            RedirectionMethod.INSTANCE.redirectionScreen(redirectionModel, ActivityClaimReclaimList.this);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    public void waterIntakeDialoge(Context context) {
        try {
            CustomYesNoDialog customYesNoDialog = new CustomYesNoDialog(context, R.style.Theme_Dialog);
            customYesNoDialog.show();
            customYesNoDialog.setCancelable(false);
            customYesNoDialog.binding.llActionRequired.setVisibility(View.GONE);
            customYesNoDialog.binding.txtInfoPopUpDesc.setText("You have exceeded the water intake limit, still, you have wanted to add water ?");
            customYesNoDialog.binding.btnYes.setText("Yes");
            customYesNoDialog.binding.btnCancel.setText("No");
            customYesNoDialog.binding.btnYes.setBackground(ContextCompat.getDrawable(context, R.drawable.blue_rc_bg_8dp));
            customYesNoDialog.binding.btnCancel.setBackground(ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp));
            customYesNoDialog.binding.btnYes.setOnClickListener(view -> {
                customYesNoDialog.dismiss();
                SharedPref.putWaterIntake(true);
            });

            customYesNoDialog.binding.btnCancel.setOnClickListener(view -> {
                customYesNoDialog.dismiss();
                SharedPref.putWaterIntake(false);
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waterIntakeDialoge(
            Context context,
            int waterIntakeGoal,
            double waterIntake,
            boolean waterIntakeAllowed
    ) {
        try {
            builder = new AlertDialog.Builder(context);
            WarerIntakeLayoutBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.warer_intake_layout, null, false
            );

            builder.setView(binding.getRoot());
            alertDialog = builder.create();
            builder.setCancelable(true);

            binding.ivAdd.setOnClickListener(view -> {
                increaseNumber(
                        binding.tvGlassAmt.getText().toString(),
                        binding,
                        waterIntakeGoal,
                        waterIntake,
                        context
                );
            });

            binding.ivMinus.setOnClickListener(view -> {
                decreaseNumber(binding, context);
            });

            /* binding.btnViewMore.setOnClickListener(view -> {
                Intent intent = new Intent(context, TrendsActivity.class);
                intent.putExtra("activityType", Constants.WATER);
                context.startActivity(intent);
            }); */

            binding.btnClosed.setOnClickListener(view -> {
                SharedPref.putWaterIntake(false);
                alertDialog.dismiss();
            });

            binding.btnAddWater.setOnClickListener(view -> {
                int glasses = Integer.parseInt(binding.tvGlassAmt.getText().toString()) + (int) waterIntake;
                if (Integer.parseInt(binding.tvGlassAmt.getText().toString()) > 0) {
                    alertDialog.dismiss();
                    UploadData(Constants.WATER, glasses, context);
                } else {
                    Toast.makeText(context, "Please add water intake", Toast.LENGTH_SHORT).show();
                }
            });

            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }

            Rect displayRectangle = new Rect();
            Window window = getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow().setLayout(
                    (int) (displayRectangle.width() * 0.8f),
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    //Todo
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void increaseNumber(String waterValue, WarerIntakeLayoutBinding binding, int waterIntakeGoal, double waterIntake, Context context) {
        glasses = (int) Double.parseDouble(waterValue);
        int actualGlasses = glasses + 1;
        int currentDayWaterCount = (int) (actualGlasses + waterIntake);
        if (actualGlasses > waterIntakeGoal || waterIntake > waterIntakeGoal || currentDayWaterCount > waterIntakeGoal) {
            if (!SharedPref.getWaterIntake()) {
                waterIntakeDialoge(context);
                return;
            } else {
                if (glasses < 30) {
                    display(glasses + 1, binding, context);
                }
            }
        }
        if (glasses < 30) {
            display(glasses + 1, binding, context);
        }
    }

    public void decreaseNumber(WarerIntakeLayoutBinding binding, Context context) {
        glasses = (int) Double.parseDouble(binding.tvGlassAmt.getText().toString());
        if (glasses > 0) {
            display(glasses - 1, binding, context);
        }
    }

    private void display(int i, WarerIntakeLayoutBinding binding, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.progressBar.setProgress(i, true);
        } else {
            binding.progressBar.setProgress(i);
        }
        binding.tvGlassAmt.setText(format.format(i));
        if (i >= 16) {
            binding.tvGlassAmt.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            binding.tvGlassAmt.setTextColor(context.getResources().getColor(R.color.dark_pink));
        }
    }

    public void UploadData(String activityName, double countOrTime, Context context) {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);

            // Create request object
            AddReminderDataRequest request = new AddReminderDataRequest(
                    activityName,
                    (int) Math.round(countOrTime),
                    CommonUtils.todayDateInFormat("yyyy-MM-dd")
            );

            // Initialize API call
            ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                    .create(ApiInterfaceWyh.class);
            Call<CommonSuccessResponse> call = apiInterfaceWyh.addActivityData(SharedPref.getAuthToken(), request);

            Log.d("AuthToken", new Gson().toJson(call.request()));

            call.enqueue(new Callback<CommonSuccessResponse>() {
                @Override
                public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                    CommonUtils.dismissDialoge();

                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        SharedPref.putWaterIntake(false);
                        Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.log_reminder_success));
                        NewDashboardHelper.Companion.setWaterIntake(countOrTime);
                        // Handle rewards & tokens if available
                        CommonSuccessResponse responseBody = response.body();
                         if (checkIsFromQuizqathon()) {
                            //QuizReward Api Call
                            FetchQuizReward();
                        }
                        Toast.makeText(context, "Data Added Successfully!!!", Toast.LENGTH_SHORT).show();

                        Log.d("call", "upload data");

                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.log_reminder_failed));
                    }
                }

                @Override
                public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.log_reminder_failed));
                    Toast.makeText(context, "Failed to upload data. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("UploadData", "API Call Failed", t);
                }
            });

        } catch (Exception e) {
            CommonUtils.dismissDialoge();
            Log.e("UploadData", "Exception occurred", e);
        }
    }

    public void showPopUpZenZone(Context context1, double currentTime) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context1);
        ZenZoneLayoutBinding zenZoneBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context1),
                R.layout.zen_zone_layout, null, false
        );
        alertBuilder.setView(zenZoneBinding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                alertDialog.dismiss();
            }
        });

        zenZoneBinding.btnTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zenZoneBinding.etMins.setText("10");
            }
        });

        zenZoneBinding.btnTwenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zenZoneBinding.etMins.setText("20");
            }
        });

        zenZoneBinding.btnForty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zenZoneBinding.etMins.setText("40");
            }
        });

        zenZoneBinding.btnClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        zenZoneBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!zenZoneBinding.etMins.getText().toString().isEmpty()) {
                    alertDialog.dismiss();
                    int meditationMinutes = Integer.parseInt(zenZoneBinding.etMins.getText().toString());
                    UploadActivityData(Constants.MEDITATION, meditationMinutes + currentTime);
                } else {
                    Toast.makeText(context1, "Please add time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        zenZoneBinding.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(context1, TrendsActivity.class);
                intent.putExtra("activityType", Constants.MEDITATION);
                context1.startActivity(intent);
            }
        });

        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }

        Rect displayRectangle = new Rect();
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(
                    (int) (displayRectangle.width() * 0.8f),
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
        }

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //Api Call
            }
        });
    }

    public void UploadActivityData(String activityName, double countOrTime) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        AddReminderDataRequest request = new AddReminderDataRequest(
                activityName,
                (int) Math.round(countOrTime),
                CommonUtils.todayDateInFormat("yyyy-MM-dd")
        );

        Call<CommonSuccessResponse> call = apiInterfaceWyh.addActivityData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(
                            context,
                            context.getClass().getName(),
                            context.getString(R.string.zen_zone_update_success)
                    );

                    if (checkIsFromQuizqathon()) {
                        //QuizReward Api Call
                        FetchQuizReward();
                        //getQuizathonRewardPopup(response.body().getQuizathonRewardData());
                    }

                    Toast.makeText(context, "Data Added Successfully!!!", Toast.LENGTH_SHORT).show();

                } else {
                    Analytics.logEvent(
                            context,
                            context.getClass().getName(),
                            context.getString(R.string.zen_zone_update_failed)
                    );
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                Analytics.logEvent(
                        context,
                        context.getClass().getName(),
                        context.getString(R.string.zen_zone_update_failed)
                );
            }
        });
    }


    public void cancelDialog() {
        try {
            if (reward != null && reward.getRewardType() != null && !reward.getRewardType().isEmpty()) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, reward.getRewardType(), this);
            }
            if (quizreward != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, quizreward.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogDismiss() {
        CommonUtils.dismissDialoge();
        //Api Call
        cancelDialog();
    }

    @Override
    public void onRewardRecieved(AssignRewardsResponse.SpinRewardsData spinRewardsData) {

    }

    @Override
    public void onQuizRewardRecieved(QuizathonRewardData quizathonRewardData) {

    }

    public boolean checkIsFromQuizqathon() {
        if (NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()) {
            return true;
        }
        return false;
    }

    private void FetchQuizReward() {
        ActivityRewardRequest activityRewardRequest = new ActivityRewardRequest(NewDashboardHelper.Companion.getTrasactionId(), NewDashboardHelper.Companion.getFeatureName());
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
                t.printStackTrace();
            }
        });
    }
}
