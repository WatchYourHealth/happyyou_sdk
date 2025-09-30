package com.wyh.happyyousdk.rewards;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;
import static com.wyh.happyyousdk.utils.Constants.WEIGHT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;
;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.WebActivity;
import com.wyh.happyyousdk.WellBeingActivity;
import com.wyh.happyyousdk.contacts.ContactsActivityNew;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityPendingDashboardBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpFeedbackBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalUploadBinding;
import com.wyh.happyyousdk.databinding.CustomPopupRewardsBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.ehr.EhrActivity;
import com.wyh.happyyousdk.happyMarket.NewHappyMartActivity;
import com.wyh.happyyousdk.syncDevice.ConnectApp;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.earnAndGrab.StartActivityRequest;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.request.rewards.ActivityProgressRequest;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.rewards.ActivityProgressResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity;
import com.wyh.happyyousdk.rewards.adapter.AllPendingActivitiesAdapter;
import com.wyh.happyyousdk.model.request.rewards.AppFeedbackRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.response.rewards.PendingActivitiesResponse;
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

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

public class PendingActivityDashboard extends AppCompatActivity implements AllPendingActivitiesAdapter.ClickListenerInterface, ScratchListener {
    ActivityPendingDashboardBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    AlertDialog alertDialogStamp, alertDialogBonusStamp, alertDialog, customPopupRewardsAlertDialog, levelActivityJournalUploadAlertDialog, levelActivityAlertDialog;
    Context context;
    PendingActivitiesResponse.Datum uploadFileActivity;
    ArrayList<FileData> fileDataList = new ArrayList<>();
    CustomPopupRewardsBinding customPopupRewardsBinding;
    private static final int CAMERA_PERMISSION_CODE = 100;
    CustomPopUpRewardsJournalBinding levelActivityJournalBinding;
    CustomPopUpRewardsJournalUploadBinding customPopUpRewardsJournalUploadBinding;
    String activityEventType = "", journalContent;
    boolean isRewards, isStamp;
    int stampId = -1;

    boolean isPositiveBtn = false;

    String imageName = "";

    List<MultipartBody.Part> parts = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pending_dashboard);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Activities");

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "AllEnGActivities");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        // showStampsPopup("Stamps Tribe;Walk minimum 60,000 steps per week.;3;19", context);


//        getActivitiesData();

    }

    private void getActivitiesData() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<PendingActivitiesResponse> call = apiInterfaceWyh.getAllActivities(SharedPref.getAuthToken());
        call.enqueue(new Callback<PendingActivitiesResponse>() {
            @Override
            public void onResponse(Call<PendingActivitiesResponse> call, Response<PendingActivitiesResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_pending_events_success));
                    
                    setActivitiesData(response.body().getData());

                } else if (response.code() == 401) {
                    refreshAuthToken();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_pending_events_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PendingActivitiesResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_pending_events_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setActivitiesData(List<PendingActivitiesResponse.Datum> pendingActivityList) {
        List<PendingActivitiesResponse.Datum> inProgress = new ArrayList<>();
        List<PendingActivitiesResponse.Datum> notStarted = new ArrayList<>();
        List<PendingActivitiesResponse.Datum> completed = new ArrayList<>();
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
        AllPendingActivitiesAdapter allPendingActivitiesAdapter = new AllPendingActivitiesAdapter(context, pendingActivityList, this::onItemClickActivities);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
            binding.rvAllActivities.setAdapter(allPendingActivitiesAdapter);
        binding.rvAllActivities.setLayoutManager(gridLayoutManager);
        binding.rvAllActivities.setItemViewCacheSize(30);

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
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    getActivitiesData();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                    /*Toast.makeText(context, context.getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    context.startActivity(intent);
                    finishAffinity();*/
                    Master.INSTANCE.logOut(context);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                /*Toast.makeText(context, context.getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    context.startActivity(intent);
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    @Override
    public void onItemClickActivities(PendingActivitiesResponse.Datum datum, int bgDrawable) {
        if (datum.getEventType().equalsIgnoreCase("JournalUpload"))
            showCustomPopUpJournalUpload(datum, bgDrawable);
        else if (datum.getEventType().equalsIgnoreCase("journal"))
            showCustomPopUpJournal(datum, bgDrawable);
        else if (datum.getEventType().equalsIgnoreCase("Feedback"))
            showCustomPopUpFeedback(datum, bgDrawable);
        else
            showCustomPopUp(datum, bgDrawable);
    }

    private void showCustomPopUp(PendingActivitiesResponse.Datum eandBPendingActivity, int bgDrawable) {
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
        customPopupRewardsBinding.btnNegative.setOnClickListener(view -> {
            customPopupRewardsAlertDialog.dismiss();
            getActivitiesData();
        });

        checkEandBPendingActivityConditions(eandBPendingActivity);

        Rect displayRectangle = new Rect();
        Window window = ((PendingActivityDashboard) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        customPopupRewardsAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void checkEandBPendingActivityConditions(PendingActivitiesResponse.Datum eandBPendingActivity) {

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
                    checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                } else {
                    customPopupRewardsAlertDialog.dismiss();
                    try {
                        activityRedirection(eandBPendingActivity);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                startNewActivity(eandBPendingActivity);
            }


        });

//        onUploadActivity(eandBPendingActivity);


    }

    private void onUploadActivity(PendingActivitiesResponse.Datum eandBPendingActivity) {
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
                            customPopupRewardsAlertDialog.dismiss();
                            uploadFileActivity = eandBPendingActivity;
                            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                        });
                    } else
                        customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (eandBPendingActivity.getIsStarted() == 1) {
                customPopupRewardsBinding.btnPositive.setVisibility(View.VISIBLE);
                customPopupRewardsBinding.btnPositive.setText("Upload");
                customPopupRewardsBinding.btnPositive.setOnClickListener(view -> {
                    customPopupRewardsAlertDialog.dismiss();
                    uploadFileActivity = eandBPendingActivity;
                    checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                });
            }
        }

    }

    private void showCustomPopUpJournalUpload(PendingActivitiesResponse.Datum eandBPendingActivity, int bgDrawable) {
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
        checkActivityPopUpConditionsJournalUpload(eandBPendingActivity);

        if (eandBPendingActivity.getWhatTo() != null) {
            customPopUpRewardsJournalUploadBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.tvWhatToDo.setText(eandBPendingActivity.getWhatTo());
            customPopUpRewardsJournalUploadBinding.tvHowToDo.setText(eandBPendingActivity.getHowTo());
            customPopUpRewardsJournalUploadBinding.tvWhyToDo.setText(eandBPendingActivity.getWhyTo());
        }

    }

    private void checkActivityPopUpConditionsJournalUpload(PendingActivitiesResponse.Datum eandBPendingActivity) {
        customPopUpRewardsJournalUploadBinding.btnNegative.setOnClickListener(view -> {
            levelActivityJournalUploadAlertDialog.dismiss();
            getActivitiesData();
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
                    uploadFileActivity = eandBPendingActivity;
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                    } else {
                        //levelActivityJournalUploadAlertDialog.dismiss();
                        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
                            openImagePicker();
                        }else{
                            openGalleryOnly();
                        }
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
        Window window = ((PendingActivityDashboard) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        if(eandBPendingActivity.getIsStarted() == 1 && eandBPendingActivity.getIsCompleted() == 0){
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.8f));
        }else{
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.6f));
        }


    }

    private void getActivityProgress(LinearProgressIndicator progressBar, PendingActivitiesResponse.Datum eandBPendingActivity, TextView tvSteps) {
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
                        }else if(tvSteps != null && response.body().getData().getTotalCount() != 0){
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

    private void showCustomPopUpJournal(PendingActivitiesResponse.Datum eandBPendingActivity, int bgDrawable) {
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

        checkActivityPopUpConditionsJournal(eandBPendingActivity);

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        levelActivityAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.8f));
    }

    private void checkActivityPopUpConditionsJournal(PendingActivitiesResponse.Datum eandBPendingActivity) {
        String activityEventType = eandBPendingActivity.getEventType().toLowerCase();
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
            getActivitiesData();
        });


        if (eandBPendingActivity.getIsCompleted() == 1) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
            levelActivityJournalBinding.llJournal.setVisibility(View.GONE);
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

    public void openImagePicker(){
        try{
            Intent intent = new Intent();
            intent.putExtra(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,1);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void saveImage(Uri imageURI){
        FileData fileData = new FileData();
        fileData.setPath(CommonUtils.getRealPathFromURI(imageURI,context));
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
//                    uploadFiles(parts, uploadFileActivity.getEventId());
                uploadFileOnly(parts, uploadFileActivity.getEventId());
            }
        }
        //Log.d("FileData", new Gson().toJson(fileDataList));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{

            if(requestCode == 1){
                parts.clear();
                if(data.getData() != null){
                    Uri imageURI = data.getData();
                    saveImage(imageURI);
                }else{
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri imageURI = item.getUri();
                        saveImage(imageURI);
                    }
                }
            }

            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                List<Image> image2 = ImagePicker.getImages(data);
                for (int i = 0; i < image2.size(); i++) {
                    FileData fileData = new FileData();
                    fileData.setPath(getFilePathFromImage(image2.get(i)));
                    long singleFileSize = getFileSizeFromPath(fileData.getPath());
                    //Log.d("comparedata singleFileSize", singleFileSize+"");
                    FileData fileDataNew = new FileData();
                    //Log.d("comparedata singleFileSize", singleFileSize+"");
                    File file1 = CommonUtils.compressImageToJPEG(context, image2.get(i).getUri());
                    fileDataNew.setPath(file1.getPath());
                    fileDataNew.setMimeType("application/png");
                    imageName = file1.getName();
                    if (file1.length() > 5000000) {
                        Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                    } else {
                        fileDataList.add(fileDataNew);
                    }
                }
                if (fileDataList.size() > 0) {
                    if (uploadFileActivity.getEventType().equalsIgnoreCase("journalupload")) {
                        parts.add(prepareFilePart("journalupload", fileDataList.get(0).getPath()));
                        uploadFileWithContent(parts, uploadFileActivity);
                    } else {
                        parts.add(prepareFilePart("ActivityUploads", fileDataList.get(0).getPath()));
//                    uploadFiles(parts, uploadFileActivity.getEventId());
                        uploadFileOnly(parts, uploadFileActivity.getEventId());
                    }
                }
            }
            Log.d("FileData", new Gson().toJson(fileDataList));
        }catch (Exception e){
            e.printStackTrace();
        }


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

    private void uploadFiles(List<MultipartBody.Part> parts, int activityId) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<CommonSuccessResponse> call = apiInterfaceWyh.uploadEAndGFiles(SharedPref.getAuthToken(), parts, activityId);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_upload_activity_files_success));

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
                        getActivitiesData();
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_upload_activity_files_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_upload_activity_files_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileOnly(List<MultipartBody.Part> parts, int activityId) {
        CommonUtils.showProgressDialige(this);
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
                CommonUtils.dismissDialoge();
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
                        uploadFileActivity = null;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                getActivitiesData();
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

    private void uploadFileWithContent(List<MultipartBody.Part> parts, PendingActivitiesResponse.Datum eandBPendingActivity) {
        CommonUtils.showProgressDialige(this);
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

        Log.d("upload image request", new Gson().toJson(request));
        Log.d("upload image body", new Gson().toJson(body));

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                CommonUtils.dismissDialoge();
                levelActivityJournalUploadAlertDialog.dismiss();
                deleteImage();
                fileDataList.clear();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_earn_and_burn_failed));
//                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                CommonUtils.dismissDialoge();
                deleteImage();
                fileDataList.clear();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_earn_and_burn_success));
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    if (commonSuccessResponse.isSuccess()) {
                        uploadFileActivity = null;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                levelActivityJournalUploadAlertDialog.dismiss();
                                Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                getActivitiesData();
                                NewDashboardHelper.Companion.getPopUpShowModels().clear();
                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                              //showRewardsPopup(commonSuccessResponse.getRewards().getReward());
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                                }

                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                              //showBonusRewardsPopup(commonSuccessResponse.getRewards().getBonusRewards(), context);
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                                }

                                if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getTokens() != null) {
                                //showStampsPopup(commonSuccessResponse.getEnGTokens().getTokens(), context);
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, commonSuccessResponse.getEnGTokens().getTokens()));
                                }
                                if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getBonusTokens() != null && !TextUtils.isEmpty(commonSuccessResponse.getEnGTokens().getBonusTokens())) {
                                //showBonusRewardsPopup(commonSuccessResponse.getEnGTokens().getBonusTokens(), context);
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, commonSuccessResponse.getEnGTokens().getBonusTokens()));
                                }
                                showRewardsPopupDialogBox();
                            }
                        });
                    }
                } else{
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_earn_and_burn_failed));
                }
            }
        });
    }


    private void startNewActivity(PendingActivitiesResponse.Datum eandBPendingActivity) {
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
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_start_activity_success));

                    if (response.body().isSuccess()) {
                        Toast.makeText(context, "Activity has been started", Toast.LENGTH_SHORT).show();
                        getActivitiesData();
                        eandBPendingActivity.setIsStarted(1);

                        if (!eandBPendingActivity.getEventType().equalsIgnoreCase("Feedback")) {
                            if (!eandBPendingActivity.getEventType().toLowerCase().equals("journal")) {
                                if (eandBPendingActivity.getEventType().toLowerCase().equalsIgnoreCase("journalupload")) {
                                    checkActivityPopUpConditionsJournalUpload(eandBPendingActivity);
                                } else if (eandBPendingActivity.getEventType().toLowerCase().equals("upload")) {
                                    checkEandBPendingActivityConditions(eandBPendingActivity);
                                } else if (eandBPendingActivity.getEventType() != null) {
                                    if (customPopupRewardsAlertDialog != null)
                                        customPopupRewardsAlertDialog.dismiss();
                                    activityRedirection(eandBPendingActivity);
                                } else {
                                    checkEandBPendingActivityConditions(eandBPendingActivity);
                                }
                            } else {
                                checkActivityPopUpConditionsJournal(eandBPendingActivity);
                            }
                        }
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_start_activity_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_start_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void activityRedirection(PendingActivitiesResponse.Datum eandBPendingActivity) {
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
                    Intent intent3 = new Intent(context, ConnectApp.class);
                    startActivity(intent3);
                    break;
                case "quiz":
                    Intent intent4 = new Intent(context, QuizathonViewAllActivity.class);
                    intent4.putExtra("type", "quiz");
                    intent4.putExtra("quiz_cat", "All");
                    intent4.putExtra("name", "Play and Learn");
                    startActivity(intent4);
                    break;
                case "invite":
                    intent4 = new Intent(context, ContactsActivityNew.class);
                    intent4.putExtra("comingFrom","");
                    startActivity(intent4);
                    break;
                case "happy footprint":
                    intent4 = new Intent(context, TrendsActivity.class);
                    intent4.putExtra("activityType", STEPS);
                    startActivity(intent4);
                    break;
                case "weight":
                    intent4 = new Intent(context, TrendsActivity.class);
                    intent4.putExtra("activityType", WEIGHT);
                    startActivity(intent4);
                    break;
                case "hra":
                    GetAnalysisResponse getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
                    if (getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0) {
                        intent4 = new Intent(context, HRAAnalysisActivity.class);
                    } else {
                        intent4 = new Intent(context, HRAQuestionsActivity.class);
                    }
                    startActivity(intent4);
                    break;
                case "exercise tracker":
                    openWebView(CommonUtils.getBaseUrlForAddExercise(context));
                    break;
            }
        }
    }

    private void openWebView(String url) {
        Intent i = new Intent(context, WebActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("comingFrom","");
        i.putExtra("Url", url);
        context.startActivity(i);
    }

    private void showCustomPopUpFeedback(PendingActivitiesResponse.Datum eandBPendingActivity, int bgDrawable) {
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
                String msg = binding.edtFeedback.getText().toString().replace(" ", "");
                if (!TextUtils.isEmpty(binding.edtFeedback.getText()) && msg.length() >= 35) {
                    alertDialog.dismiss();
                    appFeedback(binding.edtFeedback.getText().toString());
                } else {
                    binding.edtFeedback.requestFocus();
                    Toast.makeText(context, getResources().getString(R.string.at_least_35_char), Toast.LENGTH_SHORT).show();
                }
            });
//            startNewActivity(eandBPendingActivity);
        });

        if (eandBPendingActivity.getIsStarted() == 1 && eandBPendingActivity.getEventType().equalsIgnoreCase("Feedback")
                && eandBPendingActivity.getIsCompleted() == 0) {
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

        if (eandBPendingActivity.getIsCompleted() == 1) {
            binding.btnPositive.setVisibility(View.GONE);
        }

        Rect displayRectangle = new Rect();
        Window window = getWindow();

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
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.app_feedback_success));
                    Toast.makeText(context, "Feedback sent successfully", Toast.LENGTH_SHORT).show();
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));
                    }
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                    }
                    getActivitiesData();
                    showRewardsPopupDialogBox();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.app_feedback_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (levelActivityJournalUploadAlertDialog != null && levelActivityJournalUploadAlertDialog.isShowing())
                    levelActivityJournalUploadAlertDialog.dismiss();
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
                    openImagePicker();
                }else{
                    openGalleryOnly();
                }
                // Showing the toast message
                Toast.makeText(context, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (context == null) {
            context = this;
        }
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
        } else {
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
                openImagePicker();
            }else{
                openGalleryOnly();
            }
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

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }
        alertDialogStamp.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
            } else {
                showRewardsPopupDialogBox();
            }
        });
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));

        /*if(isOrange){
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

        binding.btnNegative.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(PendingActivityDashboard.this);

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PendingActivityDashboard) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogStamp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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
            alertDialogBonusStamp.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(PendingActivityDashboard.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PendingActivityDashboard) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i > 20) {
            if (isStamp) {
                isStamp = false;
                scratchTokenReward();
            }
            scratchCardLayout.onFullReveal();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if((alertDialogBonusStamp!= null && alertDialogBonusStamp.isShowing())){
                        alertDialogBonusStamp.dismiss();
                    }
                    if((alertDialogStamp!= null && alertDialogStamp.isShowing())){
                        alertDialogStamp.dismiss();
                    }
                }
            }, 3000);

        }
    }

    @Override
    public void onScratchStarted() {

    }

    private void showRewardsPopupDialogBox() {
        Log.d("sized", NewDashboardHelper.Companion.getPopUpShowModels().size() + "");
        if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0) {
            int i = 0;
            PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 1 && Objects.equals(firstData.getKey(), "Rewards")) {
                i = 1;
                firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            }
            if (!Objects.equals(firstData.getValue(), "")) {
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
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
            Log.d("sized", NewDashboardHelper.Companion.getPopUpShowModels().size() + "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getActivitiesData();
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

    public void deleteImage(){
        if(imageName != ""){
            CommonUtils.deleteImage(imageName);
            imageName = "";
        }
    }
}