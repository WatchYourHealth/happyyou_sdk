package com.wyh.happyyousdk.rewards;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.validateLetters;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.TAG_ADD_ON;
import static com.wyh.happyyousdk.utils.Constants.TAG_TOP_UPS;
import static com.wyh.happyyousdk.utils.Constants.TAG_TOP_UP_EVENT;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
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

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.contacts.ContactsActivityNew;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityMoreTopUpsBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpFeedbackBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalUploadBinding;
import com.wyh.happyyousdk.databinding.CustomPopupRewardsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.happyMarket.NewHappyMartActivity;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.request.rewards.ActivityProgressRequest;
import com.wyh.happyyousdk.model.request.rewards.GetRewardsDashboardRequest;
import com.wyh.happyyousdk.model.response.AddOnActivities;
import com.wyh.happyyousdk.model.response.ScratchAndWinResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.rewards.ActivityProgressResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity;
import com.wyh.happyyousdk.rewards.adapter.level.AddOnActivitiesAdapter;
import com.wyh.happyyousdk.rewards.adapter.level.MoreTopUpsAdapter;
import com.wyh.happyyousdk.model.request.rewards.AppFeedbackRequest;
import com.wyh.happyyousdk.model.request.rewards.StartRewardsActivityRequest;
import com.wyh.happyyousdk.model.response.rewards.LevelDashboardResponse;
import com.wyh.happyyousdk.model.response.rewards.TopUp;
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

public class MoreTopUpsActivity extends AppCompatActivity implements MoreTopUpsAdapter.ClickListenerInterface, ScratchListener, AddOnActivitiesAdapter.AddOnCLick {

    ActivityMoreTopUpsBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    String imageName = "";
    AlertDialog alertDialog, levelActivityJournalUploadAlertDialog, alertDialogBonusRewards, alertDialogScratchAndWin;
    Context context;
    CustomPopUpRewardsJournalUploadBinding customPopUpRewardsJournalUploadBinding;
    CustomPopupRewardsBinding customPopupRewardsBinding;
    String fileUploadKey = "ActivityUploads";
    private TopUp uploadFileTopUp;
    String yesNoAnswer, journalContent;
    private static final int CAMERA_PERMISSION_CODE = 100;
    ArrayList<FileData> fileDataList = new ArrayList<>();
    boolean isPositiveBtn = false;
    List<MultipartBody.Part> parts = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_more_top_ups);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Top-Ups");

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "AllTopUpActivities");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        getRewardsDashboardData();
    }

    private void getRewardsDashboardData() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetRewardsDashboardRequest request = new GetRewardsDashboardRequest(TAG_TOP_UPS);
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
                        List<TopUp> data = response.body().getData().getTopUps();
                        List<TopUp> inProgress = new ArrayList<>();
                        List<TopUp> notStarted = new ArrayList<>();
                        List<TopUp> completed = new ArrayList<>();
                        List<TopUp> comingSoon = new ArrayList<>();
                        List<TopUp> recurring = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            if(data.get(i).getTopUpName().toLowerCase().contains("challenge") && !data.get(i).getTopUpName().equalsIgnoreCase("Safety challenge"))
                                comingSoon.add(data.get(i));
                            else if(data.get(i).isIsCompleted() && data.get(i).getRecurrenceDays() >= 7)
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
                        setActivitiesData(data);

                        if(response.body().getData().getAddOnActivities() != null && response.body().getData().getAddOnActivities().size() != 0){
                            setAddOnActivities(response.body().getData().getAddOnActivities());
                        }else{
                            binding.rvAddOnActivities.setVisibility(View.GONE);
                        }
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

    private void setActivitiesData(List<TopUp> topUpList) {
        MoreTopUpsAdapter moreTopUpsAdapter = new MoreTopUpsAdapter(context, topUpList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvTopUps.setAdapter(moreTopUpsAdapter);
        binding.rvTopUps.setLayoutManager(gridLayoutManager);
        binding.rvTopUps.setItemViewCacheSize(30);
    }

    private void setAddOnActivities(ArrayList<AddOnActivities> addOnActivities){
        binding.rvAddOnActivities.setVisibility(View.VISIBLE);
        AddOnActivitiesAdapter addOnActivitiesAdapter = new AddOnActivitiesAdapter(context,addOnActivities,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvAddOnActivities.setAdapter(addOnActivitiesAdapter);
        binding.rvAddOnActivities.setLayoutManager(gridLayoutManager);
        binding.rvAddOnActivities.setItemViewCacheSize(30);
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
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    getRewardsDashboardData();
                } else {
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
                /*Toast.makeText(context, context.getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    context.startActivity(intent);
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    @Override
    public void onItemClickTopUps(TopUp topUp, int bgDrawable) {
        if (topUp.getTopUpName().toLowerCase().equals("scratch & win")) {
            if (!topUp.isIsCompleted()) {
                startScratchAndWin(topUp, false);
            }else{
                showCustomPopUpTopUp(topUp, bgDrawable, false);
            }
        } else if (topUp.getEventType().equalsIgnoreCase("JournalUpload"))
            showCustomPopUpJournalUploadTopUp(topUp, bgDrawable, false);
        else if (topUp.getEventType().equalsIgnoreCase("Feedback"))
            showCustomPopUpFeedback(topUp, bgDrawable, false);
        else
            showCustomPopUpTopUp(topUp, bgDrawable, false);
    }

    @Override
    public void onItemClickAddons(AddOnActivities addOnActivities,int bgDrawable) {
        TopUp topUp = new TopUp(addOnActivities.getActivityName(),addOnActivities.getEventName(),addOnActivities.getActivityDescription()
                ,addOnActivities.getRewardPoints(),addOnActivities.getRedirectTo(),addOnActivities.getWhatToDoInActivity()
                ,addOnActivities.getHowToDoTheActivity(),addOnActivities.getWhytoDoTheActivity(),addOnActivities.getImagePath(),addOnActivities.getRewardPoints(),"Journal",addOnActivities.isStarted(),addOnActivities.isCompleted()
        ,addOnActivities.getImagePath());

        if(topUp.getEventType() != null){
            if (topUp.getEventType().toLowerCase().equals("scratch & win")) {
                if (!topUp.isIsCompleted()) {
                    startScratchAndWin(topUp, true);
                }else{
                    showCustomPopUpTopUp(topUp, bgDrawable, true);
                }
            } else if (topUp.getEventType().equalsIgnoreCase("JournalUpload"))
                showCustomPopUpJournalUploadTopUp(topUp, bgDrawable, true);
            else if (topUp.getEventType().equalsIgnoreCase("Journal"))
                showCustomPopUpJournalUploadTopUp(topUp, bgDrawable, true);
            else if (topUp.getEventType().equalsIgnoreCase("Feedback"))
                showCustomPopUpFeedback(topUp, bgDrawable, true);
            else
                showCustomPopUpTopUp(topUp, bgDrawable, true);
        }
    }

    private void showCustomPopUpTopUp(TopUp topUp, int bgDrawable, boolean isComingFromAddOn) {
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

        if(topUp.getTopUpName().toLowerCase().equals("scratch & win")){
            customPopupRewardsBinding.progressBar.setVisibility(View.GONE);
        }else{
            if (topUp.isStarted() && !topUp.isIsCompleted()) {
                customPopupRewardsBinding.progressBar.setVisibility(View.VISIBLE);
                getActivityProgress(customPopupRewardsBinding.progressBar, topUp, customPopupRewardsBinding.tvSteps);
            } else if (topUp.isIsCompleted()) {
                customPopupRewardsBinding.progressBar.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    customPopupRewardsBinding.progressBar.setProgress(100, true);
                } else
                    customPopupRewardsBinding.progressBar.setProgress(100);
            }else{
                customPopupRewardsBinding.progressBar.setVisibility(View.GONE);
            }
        }



        if (topUp.isIsCompleted()) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);
        }




        checkTopUpCondition(topUp, isComingFromAddOn);
        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void getActivityProgress(LinearProgressIndicator progressBar, TopUp topUp, TextView tvSteps) {
        ActivityProgressRequest request;
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

    private void showCustomPopUpFeedback(TopUp topUp, int bgDrawable, boolean isComingFromAddOn) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        CustomPopUpFeedbackBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_feedback, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        Glide.with(context)
                .load(topUp.getTopupIcon())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivLogo);
        binding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        binding.tvTitle.setText(topUp.getTopUpName());
        binding.tvDescription.setText(topUp.getTopUpDesc());
        if (topUp.isIsCompleted()) {
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
                String msg = binding.edtFeedback.getText().toString().replace(" ", "");
                if (!TextUtils.isEmpty(binding.edtFeedback.getText()) && msg.length() >= 35) {
                    alertDialog.dismiss();
                    topUpFeedback(binding.edtFeedback.getText().toString());
                } else {
                    binding.edtFeedback.requestFocus();
                    Toast.makeText(context, getResources().getString(R.string.at_least_35_char), Toast.LENGTH_SHORT).show();
                }
            });

            startTopUps(topUp, isComingFromAddOn);
        });

        if (topUp.isIsStarted() && !topUp.isIsCompleted()) {
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
            binding.llJournal.setVisibility(View.GONE);
            binding.btnPositive.setVisibility(View.GONE);
        }

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.7f));
    }

    private void showCustomPopUpJournalUploadTopUp(TopUp topUp, int bgDrawable, boolean isComingFromAddOn) {
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

        checkActivityPopUpConditionsJournalUploadTopUp(topUp, isComingFromAddOn);

        if (topUp.getWhatTo() != null) {
            customPopUpRewardsJournalUploadBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.tvWhatToDo.setText(topUp.getWhatTo());
            customPopUpRewardsJournalUploadBinding.tvHowToDo.setText(topUp.getHowTo());
            customPopUpRewardsJournalUploadBinding.tvWhyToDo.setText(topUp.getWhyTo());
        }

    }

    private void checkActivityPopUpConditionsJournalUploadTopUp(TopUp topUp, boolean isComingFromAddOn) {
        customPopUpRewardsJournalUploadBinding.btnNegative.setOnClickListener(view -> {
            levelActivityJournalUploadAlertDialog.dismiss();
            getRewardsDashboardData();
        });

        customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view1 -> {
            startTopUps(topUp, isComingFromAddOn);
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
                        //levelActivityJournalUploadAlertDialog.dismiss();
                        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                    } else {
                        checkImagePicker();
                    }
                } else {
                    customPopUpRewardsJournalUploadBinding.edtJournal.requestFocus();
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (!topUp.isCompleted() && topUp.isStarted() && topUp.getEventType().equalsIgnoreCase("journal")) {
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setText("Submit");
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.VISIBLE);
            customPopUpRewardsJournalUploadBinding.edtJournal.setHint(getResources().getString(R.string.at_least_15_char));
            customPopUpRewardsJournalUploadBinding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(customPopUpRewardsJournalUploadBinding.edtJournal.getText()) &&
                        customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString().length() > 15) {
                    levelActivityJournalUploadAlertDialog.dismiss();
                    journalContent = customPopUpRewardsJournalUploadBinding.edtJournal.getText().toString();
                    uploadContentOnlyTopUP(topUp, isComingFromAddOn);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                    customPopUpRewardsJournalUploadBinding.edtJournal.requestFocus();
                }
            });
        }

        if (topUp.isIsCompleted()) {
            customPopUpRewardsJournalUploadBinding.llJournal.setVisibility(View.GONE);
            customPopUpRewardsJournalUploadBinding.btnPositive.setVisibility(View.GONE);
        }

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        if(topUp.isStarted() && !topUp.isIsCompleted()){
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.8f));
        }else{
            levelActivityJournalUploadAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), (int) (displayRectangle.height() * 0.6f));
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

    private void checkTopUpCondition(TopUp topUp, boolean isComingFromAddOn) {

        customPopupRewardsBinding.btnNegative.setOnClickListener(view1 -> {
            if (alertDialog != null)
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
                    startScratchAndWin(topUp, false);
                } else
                    startTopUps(topUp, isComingFromAddOn);
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
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            });
        }

        if (topUp.isIsStarted() && topUp.getEventType().equalsIgnoreCase("yesno") && !topUp.isIsCompleted()) {
            customPopupRewardsBinding.llYesNo.setVisibility(View.VISIBLE);
            customPopupRewardsBinding.btnYes.setOnClickListener(view -> {
                alertDialog.dismiss();
                yesNoAnswer = "Yes";
                uploadContentOnlyTopUP(topUp, false);
            });
            customPopupRewardsBinding.btnNo.setOnClickListener(view -> {
                alertDialog.dismiss();
                yesNoAnswer = "No";
                uploadContentOnlyTopUP(topUp, false);
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
                    uploadContentOnlyTopUP(topUp, false);
                } else {
                    customPopupRewardsBinding.edtJournal.requestFocus();
                }
            });
        }

        if (topUp.getTopUpName().toLowerCase().contains("challenge") && !topUp.getTopUpName().equalsIgnoreCase("Safety challenge")) {
            customPopupRewardsBinding.btnPositive.setVisibility(View.GONE);
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
            if (uploadFileTopUp != null) {
                parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                uploadFileWithContentTopUP(parts, uploadFileTopUp);
            }
            //uploadFiles(parts, uploadFileActivity, yesNoAnswer, journalContent);
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2121) {
            getRewardsDashboardData();
        } else if(requestCode == 1){
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


        } else if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<Image> image2 = ImagePicker.getImages(data);
            for (int i = 0; i < image2.size(); i++) {
                FileData fileData = new FileData();
                fileData.setPath(getFilePathFromImage(image2.get(i)));
                long singleFileSize = getFileSizeFromPath(fileData.getPath());
                FileData fileDataNew = new FileData();
                Log.d("comparedata singleFileSize", singleFileSize+"");
                File file1 = CommonUtils.compressImageToJPEG(context, image2.get(i).getUri());
                fileDataNew.setPath(file1.getPath());
                fileDataNew.setMimeType("application/png");
                imageName = file1.getName();
                if (file1.length() > 5000000) {
                    Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                } else {
                    fileDataList.add(fileDataNew);
                }

                if(singleFileSize > 1000000){

                }else{
                    fileDataList.add(fileData);
                }
            }
            if (fileDataList.size() > 0) {
                if (uploadFileTopUp != null) {
                    parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                    uploadFileWithContentTopUP(parts, uploadFileTopUp);
                }
//                uploadFiles(parts, uploadFileActivity, yesNoAnswer, journalContent);
            }
        }else{
            Toast.makeText(context, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
        //Log.d("FileData", new Gson().toJson(fileDataList));
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

    private void topUpRedirection(TopUp topUp) {
        if (topUp.getRedirectTo() != null) {
            switch (topUp.getRedirectTo().toLowerCase()) {
                case "invite":
                    Intent intent = new Intent(context, ContactsActivityNew.class);
                    intent.putExtra("comingFrom","share");
                    intent.putExtra("isFromHRA", true);
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

    private void startScratchAndWin(TopUp topUp, boolean isComingFromAddOn) {
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
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
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

    private void startTopUps(TopUp topUp, boolean isComingFromAddOn) {
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
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_success));
                    topUp.setStarted(true);
                    if (response.body().isSuccess()) {
                        getRewardsDashboardData();
                        if (!topUp.getEventType().equalsIgnoreCase("Feedback")) {
                            if (topUp.getRedirectTo() != null) {
                                if (alertDialog != null)
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
                                    checkActivityPopUpConditionsJournalUploadTopUp(topUp, isComingFromAddOn);
                                } else {
                                    checkTopUpCondition(topUp, isComingFromAddOn);
                                }
                            }
                        }

                        Toast.makeText(context, "Top up has been started", Toast.LENGTH_SHORT).show();
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

    private void uploadFileWithContentTopUP(List<MultipartBody.Part> parts, TopUp topUp) {
        CommonUtils.showProgressDialige(this);
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
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_top_up_activity_failed));
                runOnUiThread(() -> Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                CommonUtils.dismissDialoge();
                deleteImage();
                fileDataList.clear();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_top_up_activity_success));
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    if (commonSuccessResponse.isSuccess()) {
                        uploadFileTopUp = null;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                levelActivityJournalUploadAlertDialog.dismiss();
                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                                    Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                    getRewardsDashboardData();
                                    NewDashboardHelper.Companion.getPopUpShowModels().clear();
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                                }

                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                                    Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                    getRewardsDashboardData();
                                    NewDashboardHelper.Companion.getPopUpShowModels().clear();
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                                }

                                showRewardsPopupDialogBox(context);
                            }
                        });
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_top_up_activity_failed));
                }
            }
        });
    }

    private void uploadContentOnlyTopUP(TopUp topUp, boolean isComingFromAddOn) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("ActivityUploads", "")
                .addFormDataPart("eventId", String.valueOf(topUp.getTopUpID()))
                .addFormDataPart("eventType", topUp.getEventType())
                .addFormDataPart("eventName", topUp.getTopUpTag())
                .addFormDataPart("yesNo", yesNoAnswer == null ? "" : yesNoAnswer)
                .addFormDataPart("journalContent", journalContent == null ? "" : journalContent)
                .addFormDataPart("eventCategory", isComingFromAddOn ? TAG_ADD_ON : TAG_TOP_UP_EVENT)
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
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_top_up_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_top_up_activity_success));
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    deleteImage();
                    fileDataList.clear();
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    if (commonSuccessResponse.getRewards() != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                getRewardsDashboardData();

                                NewDashboardHelper.Companion.getPopUpShowModels().clear();
                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                                }

                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                                }

                                showRewardsPopupDialogBox(context);
                            }
                        });
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.upload_top_up_activity_failed));
                }
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
                    showRewardsPopupDialogBox(context);
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

    public void showScratchAndWinPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogScratchAndWin = alertBuilder.create();
        alertDialogScratchAndWin.setCancelable(false);
        if (!alertDialogScratchAndWin.isShowing())
            alertDialogScratchAndWin.show();

        String title = "Scratch & Win";
        binding.scratchView.setScratchDrawable(getResources().getDrawable(R.drawable.scratch_card_blue_new));
        binding.tvPoints.setText(rewards);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

        alertDialogScratchAndWin.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                getRewardsDashboardData();
            }
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogScratchAndWin.dismiss();
        });

//        binding.btnPositive.setText("Collect");
        binding.btnPositive.setOnClickListener(view -> {
            alertDialogScratchAndWin.dismiss();
        });
        binding.scratchView.setScratchListener(this);

        binding.btnNegative.setOnClickListener(view -> alertDialogScratchAndWin.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogScratchAndWin.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialogScratchAndWin.getWindow().setLayout((int) (displayRectangle.width() *
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
        binding.scratchView.setScratchListener(this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();

        Window window =  getWindow();

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
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            } else {
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
        alertDialogBonusRewards.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i >= 20) {
            scratchCardLayout.onFullReveal();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(alertDialogBonusRewards!= null && alertDialogBonusRewards.isShowing()) {
                            alertDialogBonusRewards.dismiss();
                        }
                        if(alertDialogScratchAndWin!= null && alertDialogScratchAndWin.isShowing()){
                            alertDialogScratchAndWin.dismiss();
                        }
                    }
                }, 3000);
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