package com.wyh.happyyousdk.SpinWheel.Activities.spinwheelreward;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.Utilities.DownloadImage;
import com.wyh.happyyousdk.SpinWheel.Utilities.MessageInfoDialog;
import com.wyh.happyyousdk.SpinWheel.Utilities.NextSpininfoDialog;
import com.wyh.happyyousdk.SpinWheel.Utilities.RewardInfoBottomSheet;
import com.wyh.happyyousdk.SpinWheel.Utilities.onRewardDialogClick;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.SpinWheel.spinRewardCallBack;

import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.RedirectionMethod;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivitySpinWheelRewardsBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.SpinnerCancelLayoutBinding;
import com.wyh.happyyousdk.databinding.WarerIntakeLayoutBinding;
import com.wyh.happyyousdk.databinding.ZenZoneLayoutBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.RedirectionModel;
import com.wyh.happyyousdk.model.request.diary.AddDiaryRequest;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.model.request.postloginreward.SpinActivityrequest;
import com.wyh.happyyousdk.model.request.postloginreward.StartSpinActivityRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.request.trends.AddReminderDataRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;
import com.wyh.happyyousdk.model.response.GetQuadrantsResponse;
import com.wyh.happyyousdk.model.response.diary.AddDiaryResponse;
import com.wyh.happyyousdk.model.response.diary.UploadUserFileResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.model.response.postloginreward.BurnSpinData;
import com.wyh.happyyousdk.model.response.postloginreward.CommonResponse;
import com.wyh.happyyousdk.model.response.postloginreward.EventSpinData;
import com.wyh.happyyousdk.model.response.postloginreward.FreeSpinData;
import com.wyh.happyyousdk.model.response.postloginreward.PostLogRewardResponse;
import com.wyh.happyyousdk.model.response.postloginreward.PostLoginRewardData;
import com.wyh.happyyousdk.model.response.postloginreward.Quadrant;
import com.wyh.happyyousdk.model.response.postloginreward.RewardItem;
import com.wyh.happyyousdk.model.response.postloginreward.SpinActivityResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.PendingActivityDashboard;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyh.happyyousdk.utils.wheelview.OnRotationListener;
import com.wyh.happyyousdk.utils.wheelview.WheelItem;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

public class SpinWheelRewardsActivity extends AppCompatActivity implements ScratchListener, spinRewardCallBack, rewardDialogCloseListener {
    ActivitySpinWheelRewardsBinding binding;
    APIInterface apiInterface;

    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    List<WheelItem> wheelItems = new ArrayList<>();
    int[] colors = new int[]{0xFF107E94, 0xFF13BCDC, 0xFF03687B, 0xFF1A8194, 0xFF42B3C8};

    String spinType = "";
    String eventID = "";
    RewardItem rewardItem = null;
    AssignRewardsResponse.SpinRewardsData spinRewardsData = null;
    QuizathonRewardData quizathonRewardData = null;
    RewardInfoBottomSheet dialog;
    PostLoginRewardData data;
    NextSpininfoDialog nextSpininfoDialog;
    BurnSpinData burnSpinData;
    FreeSpinData freeSpinData;
    EventSpinData eventSpinData;

    ArrayList<FileData> fileDataList = new ArrayList<>();
    List<MultipartBody.Part> parts = new ArrayList<>();
    String imageName = "";
    String fileUploadKey = "JournalUpload";
    ProgressDialog progressDialog;
    boolean isStamp = false;
    AlertDialog alertDialogBonusRewards, alertDialogStamp, alertDialogBonusStamp;
    boolean isPositiveBtn = false;
    int stampId = -1;

    String titleContent = "";
    FeedbackResponseData feedbackResponseData = null;
    private CountDownTimer currentCountDownTimer;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    int glasses;
    DecimalFormat format = new DecimalFormat("0.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spin_wheel_rewards);

        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterface = RetrofitHandler.apiInterface();
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding.btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardItem = null;
                spinRewardsData = null;
                quizathonRewardData = null;
                spinType = "Event";
                assignSpinActivity(spinType, eventID);
                APILogs.INSTANCE.activityTracker("A_SPINDASHBOARD_TAPSPIN_EVENT", context);
            }
        });

        binding.btnWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APILogs.INSTANCE.activityTracker("A_SPINDASHBOARD_TAPSPIN_FREE", context);
                if (burnSpinData.getHasBurnSpin() && freeSpinData.getFreeExpireOn() != null && !freeSpinData.getFreeExpireOn().isEmpty()) {
                    if (freeSpinData.getFreeSpinMessage() != null && !freeSpinData.getFreeSpinMessage().isEmpty()) {
                        nextSpininfoDialog = new NextSpininfoDialog(context, true, freeSpinData, burnSpinData, new onRewardDialogClick() {
                            @Override
                            public void onSubmit(String title) {

                            }

                            @Override
                            public void onStartClick() {

                            }

                            @Override
                            public void onCompleteClick() {

                            }

                            @Override
                            public void onCloseClick() {
                                nextSpininfoDialog.dismiss();
                            }
                        });
                        nextSpininfoDialog.show();
                    }
                } else if (!freeSpinData.getHasFreeSpin() && !burnSpinData.getHasBurnSpin()
                        && freeSpinData.getFreeExpireOn() != null && !freeSpinData.getFreeExpireOn().isEmpty()) {
                    if (freeSpinData.getFreeSpinMessage() != null && !freeSpinData.getFreeSpinMessage().isEmpty()) {
                        nextSpininfoDialog = new NextSpininfoDialog(context, true, freeSpinData, burnSpinData, new onRewardDialogClick() {
                            @Override
                            public void onStartClick() {

                            }

                            @Override
                            public void onSubmit(String title) {

                            }

                            @Override
                            public void onCompleteClick() {

                            }

                            @Override
                            public void onCloseClick() {
                                nextSpininfoDialog.dismiss();
                            }
                        });
                        nextSpininfoDialog.show();
                    }
                } else if (!freeSpinData.getHasFreeSpin() && !burnSpinData.getHasBurnSpin()) {

                    MessageInfoDialog messageInfoDialog = new MessageInfoDialog(context, ContextCompat.getDrawable(context, R.drawable.no_reward_img), "Information", "Hey, it seems you have exhausted all your spin limit. Stay tuned, your Spin chance refills soon.");
                    messageInfoDialog.show();
                    messageInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            fetchQuadrants();
                        }
                    });

                } else {
                    rewardItem = null;
                    spinRewardsData = null;
                    quizathonRewardData = null;
                    spinType = "Free";
                    eventID = "";
                    assignSpinActivity(spinType, eventID);
                }
            }
        });
        binding.ivGeneralSpinInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APILogs.INSTANCE.activityTracker("A_SPINDASHBOARD_TAPSPIN_FREESPININFO", context);
                if (burnSpinData.getHasBurnSpin() && freeSpinData.getFreeExpireOn() != null && !freeSpinData.getFreeExpireOn().isEmpty()) {
                    if (freeSpinData.getFreeSpinMessage() != null && !freeSpinData.getFreeSpinMessage().isEmpty()) {
                        nextSpininfoDialog = new NextSpininfoDialog(context, true, freeSpinData, burnSpinData, new onRewardDialogClick() {
                            @Override
                            public void onSubmit(String title) {

                            }

                            @Override
                            public void onStartClick() {

                            }

                            @Override
                            public void onCompleteClick() {

                            }

                            @Override
                            public void onCloseClick() {
                                nextSpininfoDialog.dismiss();
                            }
                        });
                        nextSpininfoDialog.show();
                    }
                } else if (!freeSpinData.getHasFreeSpin() && !burnSpinData.getHasBurnSpin()
                        && freeSpinData.getFreeExpireOn() != null && !freeSpinData.getFreeExpireOn().isEmpty()) {
                    if (freeSpinData.getFreeSpinMessage() != null && !freeSpinData.getFreeSpinMessage().isEmpty()) {
                        nextSpininfoDialog = new NextSpininfoDialog(context, true, freeSpinData, burnSpinData, new onRewardDialogClick() {
                            @Override
                            public void onStartClick() {

                            }

                            @Override
                            public void onSubmit(String title) {

                            }

                            @Override
                            public void onCompleteClick() {

                            }

                            @Override
                            public void onCloseClick() {
                                nextSpininfoDialog.dismiss();
                            }
                        });
                        nextSpininfoDialog.show();
                    }
                }
            }
        });

        binding.btnBurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (data != null && data.getSpinTheWheelDetail().getBurnSpinData() != null) {
                        BurnSpinData burnSpinData = data.getSpinTheWheelDetail().getBurnSpinData();
                        if (!burnSpinData.isHasSufficientBalance() && burnSpinData.isShowPopup()) {
                            APILogs.INSTANCE.activityTracker("A_SPINDASHBOARD__BURNHYPOINT_POPUP", context);
                            nextSpininfoDialog = new NextSpininfoDialog(context, false, freeSpinData, burnSpinData, new onRewardDialogClick() {
                                @Override
                                public void onStartClick() {
                                    nextSpininfoDialog.dismiss();
                                }

                                @Override
                                public void onSubmit(String title) {

                                }

                                @Override
                                public void onCompleteClick() {

                                }

                                @Override
                                public void onCloseClick() {
                                    nextSpininfoDialog.dismiss();
                                }
                            });
                            nextSpininfoDialog.show();
                        } else if (burnSpinData.isHasSufficientBalance() && burnSpinData.isShowPopup() && burnSpinData.getPopupTitle() != null
                                && burnSpinData.getPopupMessage() != null) {
                            nextSpininfoDialog = new NextSpininfoDialog(context, false, freeSpinData, burnSpinData, new onRewardDialogClick() {
                                @Override
                                public void onStartClick() {
                                    rewardItem = null;
                                    spinRewardsData = null;
                                    quizathonRewardData = null;
                                    spinType = "Burn";
                                    eventID = "";
                                    assignSpinActivity(spinType, eventID);
                                    nextSpininfoDialog.dismiss();
                                }

                                @Override
                                public void onSubmit(String title) {

                                }

                                @Override
                                public void onCompleteClick() {

                                }

                                @Override
                                public void onCloseClick() {
                                    nextSpininfoDialog.dismiss();
                                }
                            });
                            nextSpininfoDialog.show();
                        } else {
                            APILogs.INSTANCE.activityTracker("A_SPINDASHBOARD_TAPSPIN_BURNHYPOINT_NOPOPUP", context);
                            rewardItem = null;
                            spinRewardsData = null;
                            quizathonRewardData = null;
                            spinType = "Burn";
                            eventID = "";
                            assignSpinActivity(spinType, eventID);
                        }
                    } else {
                        Toast.makeText(context, "No spin data found", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        binding.mcvMyRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APILogs.INSTANCE.activityTracker("A_SPINDASHBOARD_MYACTIVITY_CLICKED", context);
                startActivity(new Intent(SpinWheelRewardsActivity.this, RewardsListActivity.class));
            }
        });

        binding.tvRewardTitle.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("A_SPINDASHBOARD_BACK", context);
            finish();
        });

        binding.luckyWheel.setOnRotationListener(new OnRotationListener() {
            @Override
            public void onFinishRotation() {
                binding.btnBurn.setClickable(true);
                binding.btnWeekly.setClickable(true);
                binding.btnEvent.setClickable(true);
                binding.mcvMyRewards.setClickable(true);
                //Glide.with(context).load(R.raw.reward_celebration).into(binding.ivCelebration);
                if (spinRewardsData != null) {
                    APILogs.INSTANCE.activityTracker("A_ACTIVITYREWARDS_WITHOUTACTIVITY", context);
                    //binding.ivCelebration.setVisibility(View.VISIBLE);
                    //startRewardCelebrationHandler();
                    getSpinRewardPopup(spinRewardsData);
                } else if (checkIsFromQuizqathon()) {
                    //QuizReward Api Call
                    APILogs.INSTANCE.activityTracker("A_ACTIVITYREWARDS_WITHOUTACTIVITY", context);
                    FetchQuizReward();
                    //getQuizathonRewardPopup(quizathonRewardData);
                } else if (rewardItem != null) {
                    //binding.ivCelebration.setVisibility(View.VISIBLE);
                    //startRewardCelebrationHandler();
                    if (rewardItem.getRewardType() != null) {
                        dialog = new RewardInfoBottomSheet(context, rewardItem, "", new onRewardDialogClick() {
                            @Override
                            public void onStartClick() {
                                StartSpinActivity(rewardItem.getActivityTransId());
                                if (rewardItem.getRedirectionKey().contains("feedback")) {
                                    FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                                    instance.showPopUpFeedbackCallback(SpinWheelRewardsActivity.this, feedbackResponseData, SpinWheelRewardsActivity.this);
                                }
//                                else if (rewardItem.getRedirectionKey().equalsIgnoreCase("journalUpload")) {
//                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//                                        openImagePicker();
//                                    } else {
//                                        openGalleryOnly();
//                                    }
//                                }
                            }

                            @Override
                            public void onCompleteClick() {
                                if (rewardItem.getRedirectionKey().contains("feedback")) {
                                    FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                                    instance.showPopUpFeedbackCallback(SpinWheelRewardsActivity.this, feedbackResponseData, SpinWheelRewardsActivity.this);
                                } else if (rewardItem.getRedirectionKey().equalsIgnoreCase("journalUpload")) {
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                        openImagePicker();
                                    } else {
                                        openGalleryOnly();
                                    }
                                } else if (rewardItem.getRedirectionKey().equalsIgnoreCase("water")) {
                                    waterIntakeDialoge(SpinWheelRewardsActivity.this, NewDashboardHelper.Companion.getWaterIntakeGoal(), NewDashboardHelper.Companion.getWaterIntake(), NewDashboardHelper.Companion.getWaterIntakeAllowed());
                                } else if (rewardItem.getRedirectionKey().equalsIgnoreCase("meditation")) {
                                    showPopUpZenZone(SpinWheelRewardsActivity.this, Double.parseDouble(NewDashboardHelper.Companion.getCurrentValueZenZone()));
                                } else {
                                    RedirectionModel redirectionModel = new RedirectionModel(rewardItem.getRedirectionKey(), false, "", "", "", "", "", "", 0, "", "", "", "", "");
                                    RedirectionMethod.INSTANCE.redirectionScreen(redirectionModel, context);
                                }
                            }

                            @Override
                            public void onCloseClick() {
                                fetchQuadrants();
                                dialog.dismiss();
                            }

                            @Override
                            public void onSubmit(String title) {
                                titleContent = title;
                                if (rewardItem.getRedirectionKey().equalsIgnoreCase("journalUpload")) {
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                        openImagePicker();
                                    } else {
                                        openGalleryOnly();
                                    }
                                }
                            }
                        });
                        dialog.setFromRewardList(false);
                        dialog.show(getSupportFragmentManager(), "expandableBottomSheet");
                    } else {
                        APILogs.INSTANCE.activityTracker("A_SPINACTIVITIES_BETTERLUCKPOPUP", context);
                        //binding.ivCelebration.setVisibility(View.GONE);
                        MessageInfoDialog messageInfoDialog = new MessageInfoDialog(context, ContextCompat.getDrawable(context, R.drawable.no_reward_img), rewardItem.getRewardName(), rewardItem.getRewardDescription());
                        messageInfoDialog.show();
                        messageInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                fetchQuadrants();
                            }
                        });
                    }
                } else {
                    //binding.ivCelebration.setVisibility(View.GONE);
                    MessageInfoDialog messageInfoDialog = new MessageInfoDialog(context, ContextCompat.getDrawable(context, R.drawable.no_reward_img), rewardItem.getRewardName(), rewardItem.getRewardDescription());
                    messageInfoDialog.show();
                    messageInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            fetchQuadrants();
                        }
                    });
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
    void startRewardCelebrationHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.ivCelebration.setVisibility(View.GONE);
            }
        }, 6000);
    }

    public void cancelDialog() {
        try {
            if (rewardItem != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, rewardItem.getRewardType(), this);
            }

        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }

    public void setViewOnResponse(PostLogRewardResponse rewardResponse) {
        data = rewardResponse.getData();
        applyGradientText(binding.tvSpinTitle, colors);
        List<WheelItem> wheelItemList = new ArrayList<>();
        for (Quadrant q : rewardResponse.getData().getQuadrants()) {
            final Bitmap[] imageBitmap = {null};
            new DownloadImage(bitmap -> {
                if (bitmap != null) {
                    imageBitmap[0] = bitmap;
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap[0], 80, 80, true);

                    WheelItem wheelItem = new WheelItem(Color.parseColor(q.getQuadrantColor()),
                            scaledBitmap, q.getRewardName());
                    wheelItemList.add(wheelItem);
                    binding.luckyWheel.addWheelItems(wheelItemList);

                }
            }).execute(q.getRewardIcon());

        }

        if (rewardResponse.getData().getSpinTheWheelDetail() != null) {
            burnSpinData = rewardResponse.getData().getSpinTheWheelDetail().getBurnSpinData();
            freeSpinData = rewardResponse.getData().getSpinTheWheelDetail().getFreeSpinData();
            eventSpinData = rewardResponse.getData().getSpinTheWheelDetail().getEventSpinData();
            binding.llGeneral.setVisibility(View.GONE);
            if (eventSpinData.getHasEventSpin()) {
                // Priority 1: Show Event Button
                eventID = eventSpinData.getEventId();
                binding.btnEvent.setVisibility(View.VISIBLE);
                binding.tvEvent.setVisibility(View.VISIBLE);
                binding.btnBurn.setVisibility(View.GONE);
                binding.tvBurn.setVisibility(View.GONE);
                binding.btnWeekly.setVisibility(View.GONE);
                binding.tvWeekly.setVisibility(View.GONE);
                binding.btnEvent.setText("Tap To Spin");
                String message = "You have " + eventSpinData.getRemainingEventSpins() + " Event Spin";
                message += freeSpinData.getRemainingFreeSpins() != null && freeSpinData.getRemainingFreeSpins() != 0 ? " and " + freeSpinData.getRemainingFreeSpins() + " free spin available" : " available";
                binding.tvEvent.setText(message);
                binding.tvSpinTitle.setText(eventSpinData.getEventTitle());
            } else if (freeSpinData.getHasFreeSpin()) {
                // Priority 2: Show Free Spin Button
                binding.btnEvent.setVisibility(View.GONE);
                binding.tvEvent.setVisibility(View.GONE);
                binding.btnWeekly.setVisibility(View.VISIBLE);
                binding.tvWeekly.setVisibility(View.VISIBLE);
                binding.btnBurn.setVisibility(View.GONE);
                binding.tvBurn.setVisibility(View.GONE);
                binding.tvSpinTitle.setText(freeSpinData.getFreeSpinTitle());
                binding.btnWeekly.setText("Tap To Spin");
                String message = "You have " + freeSpinData.getRemainingFreeSpins() + " " + freeSpinData.getFreeSpinTitle().replace("Spin", "").replace("spin", "") + " spin available";
                binding.tvWeekly.setText(message);
            } else if (burnSpinData.getHasBurnSpin()) {
                // Priority 3: Show Burn Spin Button
                binding.btnEvent.setVisibility(View.GONE);
                binding.tvEvent.setVisibility(View.GONE);
                binding.btnWeekly.setVisibility(View.VISIBLE);
                binding.tvWeekly.setVisibility(View.GONE);
                binding.btnBurn.setVisibility(View.VISIBLE);
                binding.tvBurn.setVisibility(View.GONE);
                binding.tvSpinTitle.setText(burnSpinData.getBurnSpinTitle());
                binding.btnBurn.setText("Spin with " + burnSpinData.getPointsToBurn() + " HY Points");
                if (!burnSpinData.isHasSufficientBalance()) {
                    binding.btnBurn.setBackground(ContextCompat.getDrawable(context, R.drawable.button_disabled_background));
                }
                if (freeSpinData.getFreeExpireOn() != null && !freeSpinData.getFreeExpireOn().isEmpty()) {
                    binding.btnWeekly.setVisibility(View.GONE);
                    binding.llGeneral.setVisibility(View.VISIBLE);
                    startCountdownView(freeSpinData.getFreeExpireOn(), 0, 0, binding.tvGeneral);
                } else {
                    binding.btnWeekly.setVisibility(View.GONE);
                }
                if (burnSpinData.getRemainingBurnSpins() > 0) {
                    binding.tvBurn.setVisibility(View.VISIBLE);
                    binding.ivSpinInfo.setVisibility(View.GONE);
                    String message = "You have " + burnSpinData.getRemainingBurnSpins() + " HY Points spin available";
                    binding.tvBurn.setText(message);
                }
            } else if (!freeSpinData.getHasFreeSpin() && !burnSpinData.getHasBurnSpin() && freeSpinData.getFreeExpireOn() != null) {
                binding.btnEvent.setVisibility(View.GONE);
                binding.tvEvent.setVisibility(View.GONE);
                binding.btnWeekly.setVisibility(View.VISIBLE);
                binding.tvWeekly.setVisibility(View.GONE);
                binding.btnBurn.setVisibility(View.GONE);
                binding.tvBurn.setVisibility(View.GONE);
                binding.tvSpinTitle.setText(freeSpinData.getFreeSpinTitle());
                if (freeSpinData.getFreeExpireOn() != null && !freeSpinData.getFreeExpireOn().isEmpty()) {
                    startCountdown(freeSpinData.getFreeExpireOn(), 0, 0, binding.btnWeekly, ContextCompat.getDrawable(context, R.drawable.button_disabled_background));
                    binding.tvWeekly.setVisibility(View.VISIBLE);
                    binding.tvWeekly.setText("All your chances are exhausted.");
                } else {
                    binding.btnWeekly.setVisibility(View.GONE);
                }
            } else {
                binding.tvSpinTitle.setText("");
                binding.btnEvent.setVisibility(View.GONE);
                binding.tvEvent.setVisibility(View.GONE);
                binding.btnWeekly.setVisibility(View.VISIBLE);
                binding.btnWeekly.setText("Tap to Spin");
                binding.btnWeekly.setClickable(true);
                binding.btnWeekly.setBackground(ContextCompat.getDrawable(context, R.drawable.button_disabled_background));
                binding.tvWeekly.setVisibility(View.GONE);
                binding.btnBurn.setVisibility(View.GONE);
                binding.tvBurn.setVisibility(View.VISIBLE);
                binding.tvBurn.setText("0 spins left");
                binding.tvSpinTitle.setText("No spin available");

            }

        }
    }

    public void startCountdownView(String lastActionDate, int minutesToAdd, int daysToAdd, TextView view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        try {
            // Parse the lastActionDate
            Date startDate = dateFormat.parse(lastActionDate);
            if (startDate == null) throw new ParseException("Invalid date", 0);

            // Add minutes and days to the lastActionDate
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.MINUTE, minutesToAdd);
            calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

            long desiredTimeMillis = calendar.getTimeInMillis();
            long currentTimeMillis = System.currentTimeMillis();

            // Calculate the countdown duration
            long countdownDuration = desiredTimeMillis - currentTimeMillis;

            if (countdownDuration > 0) {
                // Cancel the existing countdown timer if it's running
                if (currentCountDownTimer != null) {
                    currentCountDownTimer.cancel();
                }
                view.setTextColor(ContextCompat.getColor(context, com.esafirm.imagepicker.R.color.ef_grey));
                // Start a new countdown timer
                currentCountDownTimer = new CountDownTimer(countdownDuration, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        long seconds = millisUntilFinished / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;

                        seconds %= 60;
                        minutes %= 60;

                        // Update the Button
                        String timeRemaining = String.format(Locale.getDefault(), "Free Spin in %02dh %02dm %02ds", hours, minutes, seconds);
                        view.setText(timeRemaining);

                    }

                    @Override
                    public void onFinish() {
                        // Update the TextView when the countdown finishes
                        view.setVisibility(View.GONE);
                        fetchQuadrants();
                    }

                }.start();
            } else {
                // If the desired time has already passed
                view.setVisibility(View.GONE);
                // Optionally call fetchQuadrants() here if needed
            }

        } catch (ParseException e) {
            // Handle parsing error
            view.setText("No free spins available");
            e.printStackTrace();
        }
    }

    public void startCountdown(String lastActionDate, int minutesToAdd, int daysToAdd, Button view, Drawable drawable) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        try {
            // Parse the lastActionDate
            Date startDate = dateFormat.parse(lastActionDate);
            if (startDate == null) throw new ParseException("Invalid date", 0);

            // Add minutes and days to the lastActionDate
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.MINUTE, minutesToAdd);
            calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

            long desiredTimeMillis = calendar.getTimeInMillis();
            long currentTimeMillis = System.currentTimeMillis();

            // Calculate the countdown duration
            long countdownDuration = desiredTimeMillis - currentTimeMillis;

            if (countdownDuration > 0) {
                // Set background drawable
                view.setBackground(drawable);
                // Start the countdown timer
                view.setTextColor(ContextCompat.getColor(context, com.esafirm.imagepicker.R.color.ef_grey));
                new CountDownTimer(countdownDuration, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        long seconds = millisUntilFinished / 1000;
                        long minutes = millisUntilFinished / 60000; // Convert millis to minutes
                        long hours = minutes / 60;  // Calculate total hours
                        seconds %= 60;
                        minutes %= 60;  // Calculate remaining minutes after extracting hours

                        // Update the Button to show only hours and minutes
                        String timeRemaining = String.format(Locale.getDefault(), "Free Spin in %02dh %02dm %02ds", hours, minutes, seconds);
                        view.setText(timeRemaining);
                    }

                    @Override
                    public void onFinish() {
                        // Update the Button when the countdown finishes
                        view.setText("Tap to Spin");
                        fetchQuadrants();
                    }

                }.start();
            } else {
                // If the desired time has already passed
                view.setText("Tap to Spin");
                //fetchQuadrants();
            }

        } catch (ParseException e) {
            // Handle parsing error
            view.setText("Invalid date format");
            e.printStackTrace();
        }
    }


    public void startCountdownOld(String lastActionDate, int minutesToAdd, int daysToAdd, Button view, Drawable drawable) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        try {
            //view.setClickable(false);
            // Parse the lastActionDate
            Date startDate = dateFormat.parse(lastActionDate);
            if (startDate == null) throw new ParseException("Invalid date", 0);

            // Add minutes and days to the lastActionDate
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.MINUTE, minutesToAdd);
            calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

            long desiredTimeMillis = calendar.getTimeInMillis();
            long currentTimeMillis = System.currentTimeMillis();

            // Calculate the countdown duration
            long countdownDuration = desiredTimeMillis - currentTimeMillis;

            if (countdownDuration > 0) {
                // Set background drawable
                view.setBackground(drawable);
                // Start the countdown timer
                view.setTextColor(ContextCompat.getColor(context, com.esafirm.imagepicker.R.color.ef_grey));
                new CountDownTimer(countdownDuration, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        long seconds = millisUntilFinished / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        long days = hours / 24;
                        seconds %= 60;
                        minutes %= 60;
                        hours %= 24;

                        // Update the Button
                        String timeRemaining;
                        if (days > 0) {
                            timeRemaining = String.format(Locale.getDefault(), "Free Spin in %d days %02dh %02dm %02ds", days, hours, minutes, seconds);
                        } else {
                            timeRemaining = String.format(Locale.getDefault(), "Free Spin in %02dh %02dm %02ds", hours, minutes, seconds);
                        }
                        view.setText(timeRemaining);
                    }

                    @Override
                    public void onFinish() {
                        // Update the Button when the countdown finishes
                        view.setText("Time's up!");
                    }

                }.start();
            } else {
                // If the desired time has already passed
                view.setText("Time's up!");
            }

        } catch (ParseException e) {
            // Handle parsing error
            view.setText("Invalid date format");
            e.printStackTrace();
        }
    }

    private void applyGradientText(TextView view, int[] colors) {
        TextPaint paint = view.getPaint();
        float width = paint.measureText(view.getText().toString()); // Dynamic width based on text
        Shader textShader = new LinearGradient(180, 0, width, view.getTextSize(),
                colors, null, Shader.TileMode.REPEAT);
        paint.setShader(textShader);
    }

    private void fetchQuadrants() {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);

            //binding.ivCelebration.setVisibility(View.GONE);
            apiInterface.fetchQuadrants(SharedPref.getAuthToken()).enqueue(new Callback<PostLogRewardResponse>() {
                @Override
                public void onResponse(Call<PostLogRewardResponse> call, Response<PostLogRewardResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        setViewOnResponse(response.body());
                    }
                }

                @Override
                public void onFailure(Call<PostLogRewardResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    private void assignSpinActivity(String spinType, String eventID) {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);
            Log.d("Assign Request", new Gson().toJson(new SpinActivityrequest(spinType, eventID)));
            apiInterface.assignSpinActivity(SharedPref.getAuthToken(), new SpinActivityrequest(spinType, eventID)).enqueue(new Callback<SpinActivityResponse>() {
                @Override
                public void onResponse(Call<SpinActivityResponse> call, Response<SpinActivityResponse> response) {
                    CommonUtils.dismissDialoge();
                    Log.d("Assign Response", new Gson().toJson(response.body()));
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getData() != null) {
                            binding.luckyWheel.rotateWheelTo(response.body().getData().getQuadrantPosition());
                            rewardItem = response.body().getData();
                            feedbackResponseData = response.body().getFeedbackDetails();
                            binding.btnBurn.setClickable(false);
                            binding.btnWeekly.setClickable(false);
                            binding.btnEvent.setClickable(false);
                            binding.mcvMyRewards.setClickable(false);
                        }
                        if (response.body().getSpinTheWheelRewardsModel() != null) {
                            spinRewardsData = response.body().getSpinTheWheelRewardsModel();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SpinActivityResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    private void StartSpinActivity(int ActivityTransId) {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);
            apiInterface.StartSpinActivity(SharedPref.getAuthToken(), new StartSpinActivityRequest(ActivityTransId)).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (rewardItem.getRedirectionKey().equalsIgnoreCase("water")) {
                            waterIntakeDialoge(SpinWheelRewardsActivity.this, NewDashboardHelper.Companion.getWaterIntakeGoal(), NewDashboardHelper.Companion.getWaterIntake(), NewDashboardHelper.Companion.getWaterIntakeAllowed());
                        } else if (rewardItem.getRedirectionKey().equalsIgnoreCase("meditation")) {
                            showPopUpZenZone(SpinWheelRewardsActivity.this, Double.parseDouble(NewDashboardHelper.Companion.getCurrentValueZenZone()));

                        } else {
                            RedirectionModel redirectionModel = new RedirectionModel(rewardItem.getRedirectionKey(), false, "", "", "", "", "", "", 0, "", "", "", "", "");
                            RedirectionMethod.INSTANCE.redirectionScreen(redirectionModel, context);
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


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) {
            try {
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
                String currentTime = CommonUtils.todayDate();
                addDiary(currentTime, titleContent, titleContent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            fileDataList.clear();
            parts.clear();

            List<Image> image2 = ImagePicker.getImages(data);
            FileData fileData = new FileData();
            fileData.setPath(getFilePathFromImage(image2.get(0)));
            long singleFileSize = getFileSizeFromPath(fileData.getPath());
            File imgFile = new File(String.valueOf(fileData.getPath()));
            FileData fileDataNew = new FileData();

            if (singleFileSize > 1000000) {
                File file1 = CommonUtils.compressImage(context, image2.get(0).getUri());
                Log.d("comparedata singleFileSize", singleFileSize + "");
                fileDataNew.setPath(file1.getPath());
                fileDataNew.setMimeType("application/png");
                imageName = file1.getName();
                if (file1.length() > 5000000) {
                    fileDataList.clear();
                    parts.clear();
                    Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                } else {
                    fileDataList.add(fileDataNew);
                }
            } else {
                fileDataList.add(fileData);
            }

            if (fileDataList.size() > 0) {

                parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                String currentTime = CommonUtils.todayDate();
                addDiary(currentTime, titleContent, titleContent);
            }

        }

    }

    public void saveImage(Uri imageURI) {
        //List<MultipartBody.Part> parts = new ArrayList<>();

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

            parts.clear();
            parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }

    private MultipartBody.Part prepareFilePart(String partName, String path) {
        try {
            File file = new File(path);
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        } catch (Exception e) {
            Log.d("call", "error");
        }

        return null;
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

    private long getFileSizeFromPath(String filePath) {
        if (filePath == null)
            return 0;
        File file = new File(filePath);
        return file.length();
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

    private void addDiary(String date, String title, String content) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();


        AddDiaryRequest request = new AddDiaryRequest(date, title, content);
        Call<AddDiaryResponse> call = apiInterfaceWyh.addDiary(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<AddDiaryResponse>() {
            @Override
            public void onResponse(Call<AddDiaryResponse> call, Response<AddDiaryResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                AddDiaryResponse res = response.body();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.add_diary_success));

                    Toast.makeText(context, "Journal added successfully.", Toast.LENGTH_SHORT).show();
                    if (response.body().getData() > 0) {
                        if (parts.size() > 0) {
                            uploadFileWithContent(parts, response.body().getData());
                        }
                    }
                    if (response.body().getSpinTheWheelRewardsModel() != null) {
                        getSpinRewardPopup(response.body().getSpinTheWheelRewardsModel());
                    } else if (checkIsFromQuizqathon()) {
                        //QuizReward Api Call
                        FetchQuizReward();
                        //getQuizathonRewardPopup(response.body().getQuizathonRewardData());
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.add_diary_failed));
                    Toast.makeText(context, "API" + getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddDiaryResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.add_diary_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileWithContent(List<MultipartBody.Part> parts, int diaryId) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
                .addFormDataPart("DiaryId", String.valueOf(diaryId))
                .build();

        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "UserJournal/UploadFiles")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        Log.d("new request", new Gson().toJson(request));

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                fileDataList.clear();
                deleteImageFile();
                Log.d("upload file", e.getMessage());
//                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();


                String res = response.body().string();
                Log.d("upload file", res);
                if (response.code() == 200 && response.body() != null) {
                    fileDataList.clear();
                    deleteImageFile();
                    UploadUserFileResponse uploadUserFileResponse = new Gson().fromJson(res, UploadUserFileResponse.class);
                    if (uploadUserFileResponse.isSuccess()) {

                    }
                }
            }
        });
    }

    public void deleteImageFile() {
        if (imageName != "") {
            CommonUtils.deleteImage(imageName);
            imageName = "";
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
    public void onScratchComplete() {
        if (quizathonRewardData != null) {
            QuizRewardDialog.INSTANCE.QuizScratchCard(this, quizathonRewardData.getTransId(), true);
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i > 20) {
            {
                if (isStamp) {
                    isStamp = false;
                    scratchTokenReward();
                }
                scratchCardLayout.onFullReveal();

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (alertDialogBonusRewards != null && alertDialogBonusRewards.isShowing()) {
                            alertDialogBonusRewards.dismiss();
                        }
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
    }

    @Override
    public void onScratchStarted() {

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

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");
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

        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusRewards.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(SpinWheelRewardsActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((SpinWheelRewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusRewards.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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
                    break;
                case "TokenStamp":
                    showStampsPopup(firstData.getValue(), context);
                    break;
                case "TokenStampBounce":
                    showBonusStampPopup(firstData.getValue(), context);
                    break;
                case "FeedbackPOPUP":
                    FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                    instance.showPopUpFeedback(SpinWheelRewardsActivity.this, NewDashboardHelper.Companion.getFeedbackResponseData());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        }
    }

    private void showStampsPopup(String rewards, Context context) {
        isStamp = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogStamp = alertBuilder.create();
        alertDialogStamp.setCancelable(true);
        if (!alertDialogStamp.isShowing())
            alertDialogStamp.show();


        alertDialogStamp.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });
        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

       /* if(isOrange){
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));
        }else{
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));
        }*/
//        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


        /*if (rewards.contains("First Login")) {
            binding.btnNegative.setVisibility(View.GONE);
            binding.btnPositive.setText("OK");
            binding.btnPositive.setOnClickListener(view -> {
                alertDialog.dismiss();
                Intent intent = new Intent(context, SyncDeviceActivity.class);
                startActivity(intent);
            });
        } else {

        }*/

//        binding.scratchView.onFullReveal();
        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogStamp.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PendingActivityDashboard) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        isStamp = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusStamp = alertBuilder.create();
        alertDialogBonusStamp.setCancelable(true);
        if (!alertDialogBonusStamp.isShowing())
            alertDialogBonusStamp.show();

        alertDialogBonusStamp.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });
        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusStamp.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((SpinWheelRewardsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 3000);


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
        binding.scratchView.setScratchListener(this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchQuadrants();
    }

    @Override
    public void onRewardRecieved(AssignRewardsResponse.SpinRewardsData spinRewardsData) {
        getSpinRewardPopup(spinRewardsData);
    }

    @Override
    public void onQuizRewardRecieved(QuizathonRewardData quizathonRewardData) {
        getQuizathonRewardPopup(quizathonRewardData);
    }

    @Override
    public void onDialogDismiss() {
        fetchQuadrants();
        cancelDialog();
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
                fetchQuadrants();
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

                    if (response.body().getSpinTheWheelRewardsModel() != null) {
                        getSpinRewardPopup(response.body().getSpinTheWheelRewardsModel());
                    } else if (checkIsFromQuizqathon()) {
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
                    fetchQuadrants();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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

                        // Handle rewards & tokens if available
                        CommonSuccessResponse responseBody = response.body();
                        if (responseBody.getSpinTheWheelRewardsModel() != null) {
                            getSpinRewardPopup(responseBody.getSpinTheWheelRewardsModel());
                        } else if (checkIsFromQuizqathon()) {
                            //QuizReward Api Call
                            FetchQuizReward();
                            //getQuizathonRewardPopup(responseBody.getQuizathonRewardData());
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

}