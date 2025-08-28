package com.wyh.happyyousdk.rewards;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.validateLetters;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.MEDITATION;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TAG_CURRENT_LEVEL;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;
import static com.wyh.happyyousdk.utils.Constants.WATER;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;
;
import com.wyh.happyyousdk.APIEncryption.APILogs;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.WebActivity;
import com.wyh.happyyousdk.WellBeingActivity;
import com.wyh.happyyousdk.absorb.HealthHacksActivity;
import com.wyh.happyyousdk.absorb.QuickReadDashboard;
import com.wyh.happyyousdk.contacts.ContactsActivityNew;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityMoreCurrentActivitiesBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalUploadBinding;
import com.wyh.happyyousdk.databinding.CustomPopupRewardsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.diary.AddDiaryActivity;
import com.wyh.happyyousdk.ehr.EhrActivity;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.ira.IRAAnalysisActivity;
import com.wyh.happyyousdk.ira.IraActivity;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.request.rewards.ActivityProgressRequest;
import com.wyh.happyyousdk.model.request.rewards.GetRewardsDashboardRequest;
import com.wyh.happyyousdk.model.request.rewards.StartRewardsActivityRequest;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.rewards.ActivityProgressResponse;
import com.wyh.happyyousdk.model.response.rewards.LevelActivity;
import com.wyh.happyyousdk.model.response.rewards.LevelDashboardResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity;
import com.wyh.happyyousdk.rewards.adapter.level.MoreCurrentActivitiesAdapter;
import com.wyh.happyyousdk.syncDevice.SyncDeviceActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.unwind.UnwindActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
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

public class MoreCurrentActivities extends AppCompatActivity implements MoreCurrentActivitiesAdapter.ClickListenerInterface, ScratchListener {

    ActivityMoreCurrentActivitiesBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    String imageName = "";
    AlertDialog alertDialog, levelActivityAlertDialog, levelActivityJournalUploadAlertDialog, alertDialogBonusRewards;
    Context context;
    private LevelActivity uploadFileActivity;
    String yesNoAnswer, journalContent;
    ArrayList<FileData> fileDataList = new ArrayList<>();
    MoreCurrentActivitiesAdapter allPendingActivitiesAdapter;
    MoreCurrentActivitiesAdapter allPendingPreviousActivitiesAdapter;
    String fileUploadKey = "ActivityUploads";
    CustomPopUpRewardsJournalBinding levelActivityJournalBinding;
    CustomPopupRewardsBinding levelActivityBinding;
    CustomPopUpRewardsJournalUploadBinding customPopUpRewardsJournalUploadBinding;
    String activityEventType = "";
    private static final int CAMERA_PERMISSION_CODE = 100;
    boolean isPositiveBtn = false;
    List<MultipartBody.Part> parts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_more_current_activities);
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
            customObj.put("PAGE_ID", "AllWinningActivities");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

//        showBonusRewardsPopup("Event Name: tribe;150", context);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getRewardsDashboardData();
    }

    private void getRewardsDashboardData() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetRewardsDashboardRequest request = new GetRewardsDashboardRequest(TAG_CURRENT_LEVEL);
        Call<LevelDashboardResponse> call = apiInterfaceWyh.getRewardsDashboardData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<LevelDashboardResponse>() {
            @Override
            public void onResponse(Call<LevelDashboardResponse> call, Response<LevelDashboardResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {
                    refreshAuthToken();
                }
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_success));
                    if (response.body().getData() != null) {
                        List<LevelActivity> currentActivities = response.body().getData().getCurrentLevel().getActivities();
                        List<LevelActivity> tempData = new ArrayList<>();
                        List<LevelActivity> previousData = new ArrayList<>();
                        List<LevelActivity> inProgress = new ArrayList<>();
                        List<LevelActivity> notStarted = new ArrayList<>();
                        List<LevelActivity> completed = new ArrayList<>();
                        for (int i = 0; i < currentActivities.size(); i++) {
                            if (currentActivities.get(i).isIsCompleted())
                                completed.add(currentActivities.get(i));
                            else if (currentActivities.get(i).isStarted())
                                inProgress.add(currentActivities.get(i));
                            else
                                notStarted.add(currentActivities.get(i));
                        }
                        currentActivities.clear();
                        currentActivities.addAll(inProgress);
                        currentActivities.addAll(notStarted);
                        currentActivities.addAll(completed);

                        tempData = currentActivities;

                        for (int i = 0; i < tempData.size(); i++) {
                            if (tempData.get(i).isFromPreviousLevel()) {
                                previousData.add(tempData.get(i));
                            }
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            tempData.removeIf(temp -> (temp.isFromPreviousLevel()));
                        }

                        if (previousData.size() > 0) {
                            allPendingPreviousActivitiesAdapter = new MoreCurrentActivitiesAdapter(context, previousData,
                                    MoreCurrentActivities.this);
                            binding.llPreviousActivities.setVisibility(View.VISIBLE);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                            gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            binding.rvPreviousActivities.setAdapter(allPendingPreviousActivitiesAdapter);
                            binding.rvPreviousActivities.setLayoutManager(gridLayoutManager);
                            binding.rvPreviousActivities.setItemViewCacheSize(30);
                            binding.rvPreviousActivities.setHasFixedSize(true);
                        } else {
                            binding.llPreviousActivities.setVisibility(View.GONE);
                        }

                        setActivitiesData(tempData);
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LevelDashboardResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setActivitiesData(List<LevelActivity> levelActivities) {
        allPendingActivitiesAdapter = new MoreCurrentActivitiesAdapter(context, levelActivities, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvActivities.setAdapter(allPendingActivitiesAdapter);
        binding.rvActivities.setLayoutManager(gridLayoutManager);
        binding.rvActivities.setItemViewCacheSize(30);
        binding.rvActivities.setHasFixedSize(true);
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
                    getRewardsDashboardData();
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
    public void onItemClickActivities(LevelActivity levelActivity, int bgDrawable) {
        if(SharedPref.getCurrentLevel() >= 17){
            sendLogs(levelActivity.getActivityID());
        }
        if (levelActivity != null && levelActivity.getEventType() != null){
            if (levelActivity.getEventType().equalsIgnoreCase("JournalUpload"))
                showCustomPopUpJournalUpload(levelActivity, bgDrawable);
            else if (levelActivity.getEventType().equalsIgnoreCase("journal"))
                showCustomPopUpJournal(levelActivity, bgDrawable);
            else
                showCustomPopUp(levelActivity, bgDrawable);
        }

    }

    private void getActivityProgress(LinearProgressIndicator progressBar, LevelActivity levelActivity, TextView tvSteps) {
        ActivityProgressRequest request = new ActivityProgressRequest(levelActivity.getActivityID(), "Activity");
        Call<ActivityProgressResponse> call = apiInterfaceWyh.getActivityProgress(SharedPref.getAuthToken(), request);
        Log.d("level req", new Gson().toJson(request));
        call.enqueue(new Callback<ActivityProgressResponse>() {
            @Override
            public void onResponse(Call<ActivityProgressResponse> call, Response<ActivityProgressResponse> response) {
                Log.d("level 3", new Gson().toJson(response.body()));
                Log.d("level 3", new Gson().toJson(response.code()));
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
                        Log.d("level 3", progress+"");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            progressBar.setProgress(progress, true);
                        } else
                            progressBar.setProgress(progress);
                    }
                }
            }

            @Override
            public void onFailure(Call<ActivityProgressResponse> call, Throwable t) {
                Log.d("level 3", t.getMessage());
            }
        });
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

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            Log.d("level 2", levelActivity.getProgressPercentage() + " "+ levelActivity.getActivityName()+" "+levelActivity.isIsCompleted());
            levelActivityBinding.progressBar.setVisibility(View.VISIBLE);
            getActivityProgress(levelActivityBinding.progressBar, levelActivity, levelActivityBinding.tvSteps);
        } else if (levelActivity.isIsCompleted()) {
            levelActivityBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                levelActivityBinding.progressBar.setProgress(100, true);
            } else
                levelActivityBinding.progressBar.setProgress(100);
        }

        activityEventType = levelActivity.getEventType();


        checkActivityPopUpConditions(levelActivity, activityEventType);

        Rect displayRectangle = new Rect();
        Window window = ((MoreCurrentActivities) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        levelActivityAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
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
            getActivityProgress(levelActivityJournalBinding.progressBar, levelActivity, null);
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
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        levelActivityAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.8f));
    }

    private void checkActivityPopUpConditionsJournal(LevelActivity levelActivity, String activityEventType) {

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            if (levelActivity.getRedirectTo() != null) {
                levelActivityJournalBinding.btnPositive.setVisibility(View.VISIBLE);
                levelActivityJournalBinding.btnPositive.setText("Complete");
            } else {
                levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
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
            levelActivityJournalBinding.edtJournal.setHint(getResources().getString(R.string.at_least_15_char));
            levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                if(SharedPref.getCurrentLevel() >= 17) {
                    sendLogsOnComplete(levelActivity.getActivityID());
                }
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
                if(SharedPref.getCurrentLevel() >= 17) {
                    sendLogsOnComplete(levelActivity.getActivityID());
                }
                if(levelActivityJournalBinding.edtJournal.getText().length() > 15){
                    if (!TextUtils.isEmpty(levelActivityJournalBinding.edtJournal.getText()) && validateLetters(levelActivityJournalBinding.edtJournal.getText().toString())) {
                        journalContent = levelActivityJournalBinding.edtJournal.getText().toString();
                        fileUploadKey = "JournalUpload";
                        uploadFileActivity = levelActivity;
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                        } else {
                            levelActivityAlertDialog.dismiss();
                            checkImagePicker();
                        }
                    } else {
                        levelActivityJournalBinding.edtJournal.requestFocus();
                    }
                }else{
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                    levelActivityJournalBinding.edtJournal.requestFocus();
                }

            });
        }

        if (!levelActivity.isStarted()) {
            levelActivityJournalBinding.btnPositive.setOnClickListener(view -> {
                if(SharedPref.getCurrentLevel() >= 17) {
                    sendLogsOnStart(levelActivity.getActivityID());
                }
                startNewActivity(levelActivity, "Journal");
            });
        }

        levelActivityJournalBinding.btnNegative.setOnClickListener(view -> {
            if(SharedPref.getCurrentLevel() >= 17){
                sendLogsOnComplete(levelActivity.getActivityID());
            }
            levelActivityAlertDialog.dismiss();
            getRewardsDashboardData();
        });


        if (levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
            levelActivityJournalBinding.llJournal.setVisibility(View.GONE);
        }


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
            getActivityProgress(customPopUpRewardsJournalUploadBinding.progressBar, levelActivity, null);
        } else if (levelActivity.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100, true);
            } else
                customPopUpRewardsJournalUploadBinding.progressBar.setProgress(100);
        }

    }

    public void checkImagePicker(){
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            openImagePicker();
        }else{
            openGalleryOnly();
        }
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
            parts.clear();
            parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
            uploadFileWithContent(parts, uploadFileActivity);
            //uploadFiles(parts, uploadFileActivity, yesNoAnswer, journalContent);
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == 1){
                parts.clear();
                if(data == null){
                    Toast.makeText(context, "No image Selected", Toast.LENGTH_SHORT).show();
                }else{
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

            }

            else if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                List<Image> image2 = ImagePicker.getImages(data);
                for (int i = 0; i < image2.size(); i++) {
                    FileData fileData = new FileData();
                    fileData.setPath(getFilePathFromImage(image2.get(i)));
                    long singleFileSize = getFileSizeFromPath(fileData.getPath());

                    if(singleFileSize > 1000000){
                        FileData fileDataNew = new FileData();
                        Log.d("comparedata singleFileSize", singleFileSize+"");
                        File file1 = CommonUtils.compressImage(context, image2.get(i).getUri());
                        fileDataNew.setPath(file1.getPath());
                        fileDataNew.setMimeType("application/png");
                        imageName = file1.getName();
                        if (file1.length() > 5000000) {
                            Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                        } else {
                            fileDataList.add(fileDataNew);
                        }
                    }else{
                        fileDataList.add(fileData);
                    }
                }
                if (fileDataList.size() > 0) {
                    parts.clear();
                    parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                    uploadFileWithContent(parts, uploadFileActivity);
    //                uploadFiles(parts, uploadFileActivity, yesNoAnswer, journalContent);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //  Log.d("FileData", new Gson().toJson(fileDataList));
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
                case "eng":
                    intent2 = new Intent(context, RewardsActivity.class);
                    intent2.putExtra("currentIndex", 1);
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
                case "ehr":
                    Intent intent6 = new Intent(context, EhrActivity.class);
                    startActivity(intent6);
                    break;
                case "syncdevice":
                    Intent intent7 = new Intent(context, SyncDeviceActivity.class);
                    startActivity(intent7);
                    break;
                case "invite":
                    intent7 = new Intent(context, ContactsActivityNew.class);
                    intent7.putExtra("isFromHRA", true);
                    intent7.putExtra("comingFrom","share");

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
                    intent7.putExtra("comingFrom","moreActivity");
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
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_success));
                    if (response.body().isSuccess()) {
                        Toast.makeText(context, "Activity has been started", Toast.LENGTH_SHORT).show();
                        getRewardsDashboardData();
                        levelActivity.setStarted(true);
                        allPendingActivitiesAdapter.notifyDataSetChanged();
                        if (levelActivity.getRedirectTo() != null) {
                            if (levelActivityAlertDialog != null)
                                levelActivityAlertDialog.dismiss();

                            if (levelActivityJournalUploadAlertDialog != null)
                                levelActivityJournalUploadAlertDialog.dismiss();

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
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openWebView(String url) {
        Intent i = new Intent(context, WebActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("comingFrom","");
        i.putExtra("Url", url);
        context.startActivity(i);
    }

    private void sendLogs(int activityID){
        switch (activityID) {
            case 196 :
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_BAFO",context);
                break;

            case 197:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_GEF",context);
                break;

            case 198:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_FOJ",context);
                break;

            case 199:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_WACH",context);
                break;

            case 200:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SE",context);
                break;

            case 201:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_LAL",context);
                break;

            case 202:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_PY",context);
                break;

            case 203:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_RS",context);
                break;

            case 204:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SH",context);
                break;

            case 205:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_DYSW",context);
                break;

            case 206:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_R2A",context);
                break;

            case 207:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_WOYP",context);
                break;

            case 208:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_act",context);
                break;

            case 209:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SFGF",context);
                break;

            case 210:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_BAFO",context);
                break;

            case 211:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_ROF",context);
                break;

            case 212:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_EH",context);
                break;

            case 213:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_FT",context);
                break;

            case 214:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_VAAS",context);
                break;

            case 215:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_DFAK",context);
                break;

            case 216:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_GG",context);
                break;

            case 217:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_HYH",context);
                break;

            case 218:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_RS",context);
                break;

            case 219:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_SH",context);
                break;

            case 220:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_DYSW",context);
                break;

            case 221:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_R2A",context);
                break;

            case 222:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_WOYP",context);
                break;

            case 223:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_WXNS",context);
                break;

            case 224:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_SFGF",context);
                break;

            case 225:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_BAFO",context);
                break;

            case 226:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_HH",context);
                break;


            case 227:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_TTS",context);
                break;

            case 228:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_TY",context);
                break;

            case 229:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_GY",context);
                break;

            case 230:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_ML",context);
                break;

            case 231:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_MAP",context);
                break;

            case 232:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_GSF",context);
                break;

            case 233:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_CTC",context);
                break;

            case 234:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_DWW",context);
                break;

            case 235:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_PS",context);
                break;

            case 236:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_RS",context);
                break;

            case 237:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_SH",context);
                break;

            case 238:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_DYSW",context);
                break;

            case 239:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_R2A",context);
                break;

            case 240:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_WOYP",context);
                break;
            case 241:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_WXNS",context);
                break;
            case 242:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_SFGF",context);
                break;
            case 243:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_BAFO",context);
                break;

            case 244:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_TAZ",context);
                break;

            case 245:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SMD",context);
                break;

            case 246:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_BAFO",context);
                break;

            case 247:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_FB",context);
                break;
            case 248:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_TL",context);
                break;
            case 249:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_WOTD",context);
                break;
            case 250:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_R2A",context);
                break;
            case 251:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_PAS",context);
                break;
            case 252:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_RS",context);
                break;

            case 253:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SH",context);
                break;

            case 254:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_DYSW",context);
                break;

            case 255:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_R2A2ndtime",context);
                break;

            case 256:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_WOYP",context);
                break;

            case 257:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SFGF",context);
                break;

            case 258:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SFGF",context);
                break;

            case 259:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_act",context);
                break;

            case 260:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_BWYF",context);
                break;

            case 261:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_ABY",context);
                break;

            case 262:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_FJ",context);
                break;

            case 263:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_CYD",context);
                break;

            case 264:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_MYW",context);
                break;

            case 265:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_AHY",context);
                break;

            case 266:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_BTC",context);
                break;

            case 267:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_PAS",context);
                break;

            case 268:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_RS",context);
                break;

            case 269:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_SH",context);
                break;

            case 270:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_DYSW",context);
                break;

            case 271:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_R2A",context);
                break;

            case 272:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_WOYP",context);
                break;

            case 274:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_SFGF",context);
                break;


        }
    }

    private void sendLogsOnStart(int activityID){
        switch (activityID) {
            case 196 :
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_BAFO_Start",context);
                break;

            case 197:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_GEF_Start",context);
                break;

            case 198:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_FOJ_Start",context);
                break;

            case 199:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_WACH_Start",context);
                break;

            case 200:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SE_Start",context);
                break;

            case 201:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_LAL_Start",context);
                break;

            case 202:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_PY_Start",context);
                break;

            case 203:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_RS_Start",context);
                break;

            case 204:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SH_Start",context);
                break;

            case 205:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_DYSW_Start",context);
                break;

            case 206:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_R2A_Start",context);
                break;

            case 207:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_WOYP_Start",context);
                break;

            case 208:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_act_Start",context);
                break;

            case 209:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SFGF_Start",context);
                break;

            case 210:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_BAFO_Start",context);
                break;

            case 211:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_ROF_Start",context);
                break;

            case 212:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_EH_Start",context);
                break;

            case 213:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_FT_Start",context);
                break;

            case 214:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_VAAS_Start",context);
                break;

            case 215:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_DFAK_Start",context);
                break;

            case 216:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_GG_Start",context);
                break;

            case 217:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_HYH_Start",context);
                break;

            case 218:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_RS_Start",context);
                break;

            case 219:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_SH_Start",context);
                break;

            case 220:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_DYSW_Start",context);
                break;

            case 221:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_R2A_Start",context);
                break;

            case 222:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_WOYP_Start",context);
                break;

            case 223:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_WXNS_Start",context);
                break;

            case 224:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_SFGF_Start",context);
                break;

            case 225:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_BAFO_Start",context);
                break;

            case 226:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_HH_Start",context);
                break;


            case 227:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_TTS_Start",context);
                break;

            case 228:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_TY_Start",context);
                break;

            case 229:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_GY_Start",context);
                break;

            case 230:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_ML_Start",context);
                break;

            case 231:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_MAP_Start",context);
                break;

            case 232:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_GSF_Start",context);
                break;

            case 233:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_CTC_Start",context);
                break;

            case 234:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_DWW_Start",context);
                break;

            case 235:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_PS_Start",context);
                break;

            case 236:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_RS_Start",context);
                break;

            case 237:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_SH_Start",context);
                break;

            case 238:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_DYSW_Start",context);
                break;

            case 239:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_R2A_Start",context);
                break;

            case 240:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_WOYP_Start",context);
                break;
            case 241:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_WXNS_Start",context);
                break;
            case 242:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_SFGF_Start",context);
                break;
            case 243:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_BAFO_Start",context);
                break;

            case 244:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_TAZ_Start",context);
                break;

            case 245:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SMD_Start",context);
                break;

            case 246:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_BAFO_Start",context);
                break;

            case 247:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_FB_Start",context);
                break;
            case 248:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_TL_Start",context);
                break;
            case 249:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_WOTD_Start",context);
                break;
            case 250:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_R2A_Start",context);
                break;
            case 251:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_PAS_Start",context);
                break;
            case 252:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_RS_Start",context);
                break;

            case 253:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SH_Start",context);
                break;

            case 254:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_DYSW_Start",context);
                break;

            case 255:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_R2A2ndtime_Start",context);
                break;

            case 256:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_WOYP_Start",context);
                break;

            case 257:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SFGF_Start",context);
                break;

            case 258:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_act_Start",context);
                break;

            case 259:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_act",context);
                break;

            case 260:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_BWYF_Start",context);
                break;

            case 261:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_ABY_Start",context);
                break;

            case 262:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_FJ_Start",context);
                break;

            case 263:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_CYD_Start",context);
                break;

            case 264:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_MYW_Start",context);
                break;

            case 265:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_AHY_Start",context);
                break;

            case 266:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_BTC_Start",context);
                break;

            case 267:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_PAS_Start",context);
                break;

            case 268:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_RS_Start",context);
                break;

            case 269:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_SH_Start",context);
                break;

            case 270:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_DYSW_Start",context);
                break;

            case 271:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_R2A_Start",context);
                break;

            case 272:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_WOYP_Start",context);
                break;

            case 274:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_SFGF_Start",context);
                break;


        }
    }

    private void sendLogsOnClose(int activityID){
        switch (activityID) {
            case 196 :
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_BAFO_Close",context);
                break;

            case 197:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_GEF_Close",context);
                break;

            case 198:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_FOJ_Close",context);
                break;

            case 199:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_WACH_Close",context);
                break;

            case 200:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SE_Close",context);
                break;

            case 201:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_LAL_Close",context);
                break;

            case 202:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_PY_Close",context);
                break;

            case 203:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_RS_Close",context);
                break;

            case 204:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SH_Close",context);
                break;

            case 205:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_DYSW_Close",context);
                break;

            case 206:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_R2A_Close",context);
                break;

            case 207:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_WOYP_Close",context);
                break;

            case 208:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_act_Close",context);
                break;

            case 209:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SFGF_Close",context);
                break;

            case 210:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_BAFO_Close",context);
                break;

            case 211:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_ROF_Close",context);
                break;

            case 212:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_EH_Close",context);
                break;

            case 213:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_FT_Close",context);
                break;

            case 214:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_VAAS_Close",context);
                break;

            case 215:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_DFAK_Close",context);
                break;

            case 216:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_GG_Close",context);
                break;

            case 217:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_HYH_Close",context);
                break;

            case 218:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_RS_Close",context);
                break;

            case 219:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_SH_Close",context);
                break;

            case 220:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_DYSW_Close",context);
                break;

            case 221:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_R2A_Close",context);
                break;

            case 222:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_WOYP_Close",context);
                break;

            case 223:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_WXNS_Close",context);
                break;

            case 224:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_SFGF_Close",context);
                break;

            case 225:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_BAFO_Close",context);
                break;

            case 226:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_HH_Close",context);
                break;


            case 227:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_TTS_Close",context);
                break;

            case 228:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_TY_Close",context);
                break;

            case 229:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_GY_Close",context);
                break;

            case 230:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_ML_Close",context);
                break;

            case 231:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_MAP_Close",context);
                break;

            case 232:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_GSF_Close",context);
                break;

            case 233:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_CTC_Close",context);
                break;

            case 234:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_DWW_Close",context);
                break;

            case 235:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_PS_Close",context);
                break;

            case 236:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_RS_Close",context);
                break;

            case 237:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_SH_Close",context);
                break;

            case 238:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_DYSW_Close",context);
                break;

            case 239:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_R2A_Close",context);
                break;

            case 240:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_WOYP_Close",context);
                break;
            case 241:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_WXNS_Close",context);
                break;
            case 242:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_SFGF_Close",context);
                break;
            case 243:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_BAFO_Close",context);
                break;

            case 244:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_TAZ_Close",context);
                break;

            case 245:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SMD_Close",context);
                break;

            case 246:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_FB_Close",context);
                break;

            case 247:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_TL_Close",context);
                break;
            case 248:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_WOTD_Close",context);
                break;
            case 249:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SLO_Close",context);
                break;
            case 250:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_R2A_Close",context);
                break;
            case 251:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_PAS_Close",context);
                break;
            case 252:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_RS_Close",context);
                break;

            case 253:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SH_Close",context);
                break;

            case 254:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_DYSW_Close",context);
                break;

            case 255:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_R2A2ndtime_Close",context);
                break;

            case 256:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_WOYP_Close",context);
                break;

            case 257:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_WXNS_Close",context);
                break;

            case 258:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SFGF_Close",context);
                break;

            case 259:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_act_Close",context);
                break;

            case 260:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_BWYF_Close",context);
                break;

            case 261:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_ABY_Close",context);
                break;

            case 262:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_FJ_Close",context);
                break;

            case 263:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_CYD_Close",context);
                break;

            case 264:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_MYW_Close",context);
                break;

            case 265:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_AHY_Close",context);
                break;

            case 266:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_BTC_Close",context);
                break;

            case 267:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_PAS_Close",context);
                break;

            case 268:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_RS_Close",context);
                break;

            case 269:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_SH_Close",context);
                break;

            case 270:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_DYSW_Close",context);
                break;

            case 271:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_R2A_Close",context);
                break;

            case 272:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_WOYP_Close",context);
                break;

            case 274:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_SFGF_Close",context);
                break;


        }
    }

    private void sendLogsOnComplete(int activityID){
        switch (activityID) {
            case 196 :
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_BAFO_Complete",context);
                break;

            case 197:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_GEF_Complete",context);
                break;

            case 198:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_FOJ_Complete",context);
                break;

            case 199:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_WACH_Complete",context);
                break;

            case 200:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SE_Complete",context);
                break;

            case 201:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_LAL_Complete",context);
                break;

            case 202:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_PY_Complete",context);
                break;

            case 203:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_RS_Complete",context);
                break;

            case 204:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SH_Complete",context);
                break;

            case 205:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_DYSW_Complete",context);
                break;

            case 206:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_R2A_Complete",context);
                break;

            case 207:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_WOYP_Complete",context);
                break;

            case 208:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_act_Complete",context);
                break;

            case 209:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel17_SFGF_Complete",context);
                break;

            case 210:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_BAFO_Complete",context);
                break;

            case 211:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_ROF_Complete",context);
                break;

            case 212:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_EH_Complete",context);
                break;

            case 213:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_FT_Complete",context);
                break;

            case 214:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_VAAS_Complete",context);
                break;

            case 215:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_DFAK_Complete",context);
                break;

            case 216:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_GG_Complete",context);
                break;

            case 217:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_HYH_Complete",context);
                break;

            case 218:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_RS_Complete",context);
                break;

            case 219:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_SH_Complete",context);
                break;

            case 220:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_DYSW_Complete",context);
                break;

            case 221:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_R2A_Complete",context);
                break;

            case 222:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_WOYP_Complete",context);
                break;

            case 223:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_WXNS_Complete",context);
                break;

            case 224:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel18_SFGF_Complete",context);
                break;

            case 225:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_BAFO_Complete",context);
                break;

            case 226:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_HH_Complete",context);
                break;


            case 227:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_TTS_Complete",context);
                break;

            case 228:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_TY_Complete",context);
                break;

            case 229:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_GY_Complete",context);
                break;

            case 230:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_ML_Complete",context);
                break;

            case 231:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_MAP_Complete",context);
                break;

            case 232:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_GSF_Complete",context);
                break;

            case 233:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_CTC_Complete",context);
                break;

            case 234:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_DWW_Complete",context);
                break;

            case 235:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_PS_Complete",context);
                break;

            case 236:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_RS_Complete",context);
                break;

            case 237:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_SH_Complete",context);
                break;

            case 238:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_DYSW_Complete",context);
                break;

            case 239:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_R2A_Complete",context);
                break;

            case 240:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_WOYP_Complete",context);
                break;
            case 241:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_WXNS_Complete",context);
                break;
            case 242:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel19_SFGF_Complete",context);
                break;
            case 243:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_BAFO_Complete",context);
                break;

            case 244:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_TAZ_Complete",context);
                break;

            case 245:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SMD_Complete",context);
                break;

            case 246:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_FB_Complete",context);
                break;

            case 247:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_TL_Complete",context);
                break;
            case 248:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_WOTD_Complete",context);
                break;
            case 249:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SLO_Complete",context);
                break;
            case 250:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_R2A_Complete",context);
                break;
            case 251:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_PAS_Complete",context);
                break;
            case 252:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_RS_Complete",context);
                break;

            case 253:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SH_Complete",context);
                break;

            case 254:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_DYSW_Complete",context);
                break;

            case 255:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_R2A2ndtime_Complete",context);
                break;

            case 256:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_WOYP_Complete",context);
                break;

            case 257:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_WXNS_Complete",context);
                break;

            case 258:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel20_SFGF_Complete",context);
                break;

            case 259:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_act_Complete",context);
                break;

            case 260:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_BWYF_Complete",context);
                break;

            case 261:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_ABY_Complete",context);
                break;

            case 262:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_FJ_Complete",context);
                break;

            case 263:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_CYD_Complete",context);
                break;

            case 264:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_MYW_Complete",context);
                break;

            case 265:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_AHY_Complete",context);
                break;

            case 266:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_BTC_Complete",context);
                break;

            case 267:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_PAS_Complete",context);
                break;

            case 268:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_RS_Complete",context);
                break;

            case 269:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_SH_Complete",context);
                break;

            case 270:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_DYSW_Complete",context);
                break;

            case 271:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_R2A_Complete",context);
                break;

            case 272:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_WOYP_Complete",context);
                break;

            case 274:
                APILogs.INSTANCE.activityTracker("A_DB_BI_MZ_Win_Lel21_SFGF_Complete",context);
                break;


        }
    }

    private void uploadFiles(List<MultipartBody.Part> parts, LevelActivity levelActivity, String yesNoAnswer, String journalContent) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<CommonSuccessResponse> call = apiInterfaceWyh.uploadLevelFiles(SharedPref.getAuthToken(), parts,
                levelActivity.getActivityID(), levelActivity.getEventType(), TAG_REWARD_EVENT, levelActivity.getActivityName(), yesNoAnswer, journalContent);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_rewards_success));

                    if (response.body().isSuccess()) {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        getRewardsDashboardData();
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_rewards_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_rewards_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileWithContent(List<MultipartBody.Part> parts, LevelActivity levelActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
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
                deleteImage();
                fileDataList.clear();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_level_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                deleteImage();
                fileDataList.clear();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_level_activity_success));
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                                Toast.makeText(context, getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                getRewardsDashboardData();
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                            }
                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                                Toast.makeText(context, getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                getRewardsDashboardData();
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                            }

                            Log.d("data", ""+commonSuccessResponse.getRewards().getReward());

                            showRewardsPopupDialogBox(context);

                        }
                    });
                } else{
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_level_activity_failed));
                }
            }
        });
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

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 3000);

         alertDialog.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            }else{
                showRewardsPopupDialogBox(context);
            }
        });
        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        binding.scratchView.onFullReveal();
//        binding.btnPositive.setText("Collect");
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(MoreCurrentActivities.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            }else{
                showRewardsPopupDialogBox(context);
            }
        });
        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);

        binding.btnPositive.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
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

    public void showRewardsPopupDialogBox(Context context) {
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
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        }
    }

    private void uploadContentOnly(LevelActivity levelActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
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
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_level_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_level_activity_success));
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                            getRewardsDashboardData();
                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                            }
                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                            }

                            showRewardsPopupDialogBox(context);
                        }
                    });
                }else{
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_level_activity_failed));
                }
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
                ((MoreCurrentActivities) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
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
                    levelActivityAlertDialog.dismiss();
                    journalContent = levelActivityBinding.edtJournal.getText().toString();
                    fileUploadKey = "JournalUpload";
                    uploadFileActivity = levelActivity;
                    ((MoreCurrentActivities) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                } else {
                    levelActivityBinding.edtJournal.requestFocus();
                }
            });
        }

        levelActivityBinding.btnNegative.setOnClickListener(view -> {
            if(SharedPref.getCurrentLevel() >= 17){
                sendLogsOnClose(levelActivity.getActivityID());
            }
            levelActivityAlertDialog.dismiss();
            getRewardsDashboardData();
        });

        levelActivityBinding.btnPositive.setOnClickListener(view1 -> {
             //levelActivityAlertDialog.dismiss();

            if (levelActivityBinding.btnPositive.getText() == "Upload" && activityEventType.equalsIgnoreCase("Upload")) {
                fileUploadKey = "ActivityUploads";
                levelActivityAlertDialog.dismiss();
                uploadFileActivity = levelActivity;
                ((MoreCurrentActivities) context).checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
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

        if (levelActivity.isIsCompleted())
            levelActivityBinding.btnPositive.setVisibility(View.GONE);

    }

    private void checkActivityPopUpConditionsJournalUpload(LevelActivity levelActivity, String activityEventType) {

        customPopUpRewardsJournalUploadBinding.btnNegative.setOnClickListener(view -> {
            if(SharedPref.getCurrentLevel() >= 17){
                sendLogsOnClose(levelActivity.getActivityID());
            }
            levelActivityJournalUploadAlertDialog.dismiss();
            getRewardsDashboardData();
        });

        customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view1 -> {
            if(SharedPref.getCurrentLevel() >= 17){
                sendLogsOnStart(levelActivity.getActivityID());
            }
            startNewActivity(levelActivity, "JournalUpload");
        });

        if (!levelActivity.isIsCompleted() && levelActivity.isStarted() && levelActivity.getEventType().equalsIgnoreCase("JournalUpload")) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setText("Submit");
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view -> {
                if(SharedPref.getCurrentLevel() >= 17) {
                    sendLogsOnComplete(levelActivity.getActivityID());
                }
                if (!TextUtils.isEmpty(customPopUpRewardsJournalUploadBinding.edtJournal.getText()) &&
                        customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString().length() > 15) {
                    levelActivityJournalUploadAlertDialog.dismiss();
                    journalContent = customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString();
                    fileUploadKey = "JournalUpload";
                    uploadFileActivity = levelActivity;
                    checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                    customPopUpRewardsJournalUploadBinding.edtJournal.requestFocus();
                }
            });
        }

        if (levelActivity.isIsCompleted())
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.GONE);


        Rect displayRectangle = new Rect();
        Window window = ((MoreCurrentActivities) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        if(levelActivity.isStarted() && !levelActivity.isIsCompleted()){
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.8f));
        }else{
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.6f));
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
            checkImagePicker();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkImagePicker();
                // Showing the toast message
                Toast.makeText(context, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i >= 20) {
            scratchCardLayout.onFullReveal();
            if(alertDialogBonusRewards!= null && alertDialogBonusRewards.isShowing()){
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                }, 3000);
            }
        }
    }

    @Override
    public void onScratchStarted() {

    }

    public void deleteImage(){
        if(imageName != ""){
            CommonUtils.deleteImage(imageName);
            imageName = "";
        }
    }
}