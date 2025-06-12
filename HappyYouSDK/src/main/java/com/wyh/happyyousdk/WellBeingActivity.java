package com.wyh.happyyousdk;

import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.IRA_INTEGRATION_ID;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.Sonde.Activities.RespiratoryCheck;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.crypto.RSAEncryption;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.dass21.Dass21AnalysisActivity;
import com.wyh.happyyousdk.dass21.Dass21QuestionsActivity;
import com.wyh.happyyousdk.databinding.ActivityWellbeingBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.PopUpScratchCardScratchableBinding;
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding;
import com.wyh.happyyousdk.databinding.UpdateSerDetailsLayoutBinding;
import com.wyh.happyyousdk.heartAge.HeartAgeAnalysisActivity;
import com.wyh.happyyousdk.heartAge.HeartAgeQuestionsActivity;
import com.wyh.happyyousdk.model.request.CommunityIDs;
import com.wyh.happyyousdk.model.request.FaceScanRegistrationRequest;
import com.wyh.happyyousdk.model.request.FacnScanInTribeRequest;
import com.wyh.happyyousdk.model.request.UserDetailsRequest;
import com.wyh.happyyousdk.model.request.absorb.ShareBlogRequest;
import com.wyh.happyyousdk.model.request.heartAge.GetHeartAgeAnalysisRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.DownloadFaceScanResponse;
import com.wyh.happyyousdk.model.response.FaceScanInTribeResponse;
import com.wyh.happyyousdk.model.response.FaceScanRegistrationResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;
import com.wyh.happyyousdk.model.response.UserDetailResponse;
import com.wyh.happyyousdk.model.request.hra.GetAnalysisRequest;
import com.wyh.happyyousdk.model.response.faceScan.GetFaceKeysResponse;
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeAnalysisResponse;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.ira.IRAAnalysisActivity;
import com.wyh.happyyousdk.ira.IraActivity;
import com.wyh.happyyousdk.model.request.ira.ConversationRequest;
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.IntegrationIdRequest;
import com.wyh.happyyousdk.model.request.VoucherIdRequest;
import com.wyh.happyyousdk.model.request.faceScan.AddFaceScanVitalsRequest;
import com.wyh.happyyousdk.model.response.dass21.DassAnalysisResponse;
import com.wyh.happyyousdk.model.response.faceScan.FaceScanKeyData;
import com.wyh.happyyousdk.model.response.faceScan.FaceScanKeyResponse;
import com.wyh.happyyousdk.model.response.faceScan.FetchFaceScanVitalsData;
import com.wyh.happyyousdk.model.response.faceScan.FetchFaceScanVitalsResponse;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.PendingActivityDashboard;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WellBeingActivity extends AppCompatActivity implements ScratchListener, rewardDialogCloseListener {

    ActivityWellbeingBinding binding;
    Context context;

    ProgressDialog progressDialog;
    AlertDialog alertDialogStamp, alertDialogBonusStamp, alertDialogBonusRewards;
    ApiInterfaceWyh apiInterfaceWyh;
    ApiInterfaceWyh apiInterfaceWyhFaceScan;

    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;


    GetAnalysisResponse getAnalysisResponse;
    String healthScore = "";
    String otherGender = "";
    IRAHealthScoreResponse iraHealthScoreResponse;
    FeedbackResponseData feedbackResponseData;
    AddFaceScanVitalsRequest request;
    DassAnalysisResponse dassAnalysisResponse;
    VoucherIdRequest voucherIdRequest;
    HeartAgeAnalysisResponse heartAgeAnalysisResponse;
    boolean isStamp = false;

    ArrayList<CommunityIDs> list = new ArrayList<>();

    int stampId = -1;

    boolean isPositiveBtn = false;

    Calendar mainStartCalender;
    String gender = "";
    String user = "self";

    String dobStr;

    AssignRewardsResponse.SpinRewardsData spinRewardsData = null;
    QuizathonRewardData quizathonRewardData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wellbeing);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        apiInterfaceWyhFaceScan = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeBack.tvBack.setText("Know Your Wellbeing");
        binding.includeBack.llBack.setOnClickListener(view -> finish());
        binding.includeBack.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeBack.ivBack.setColorFilter(getResources().getColor(R.color.white));

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "KnowYourWellbeingDashboard");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        
        //Need to change
        getDassAnalysis();


        binding.ivInfo1.setOnClickListener(v -> {
            boolean isAnalysis = (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0);
            gotoIntroPage("KnowYourHealth", "Health Score", isAnalysis, false);
        });


        binding.ivInfo2.setOnClickListener(view -> {
            boolean isAnalysis = (iraHealthScoreResponse != null && iraHealthScoreResponse.getIraHealthScoreData() != null &&
                    iraHealthScoreResponse.getIraHealthScoreData().getPlaySports() != null && !iraHealthScoreResponse.getIraHealthScoreData()
                    .getPlaySports().isEmpty() && Integer.parseInt(iraHealthScoreResponse.getIraHealthScoreData().getPlaySports()) > 0);
            gotoIntroPage("KnowYourImmunity", "Immunity Score", isAnalysis, false);
        });

        binding.ivInfo3.setOnClickListener(view -> {
            dassAnalysisResponse = new Gson().fromJson(SharedPref.getDASSAnalysis(), DassAnalysisResponse.class);
            Intent intent;
            boolean isAnalysis = (dassAnalysisResponse != null && dassAnalysisResponse.getData() != null);
            gotoIntroPage("KnowYourDAS", "DAS Score", isAnalysis, false);
        });

        binding.ivInfo4.setOnClickListener(view -> {
            boolean isAnalysis = (!SharedPref.getUserVitals().isEmpty());
            gotoIntroPage("FaceScan", "Face Scan", isAnalysis, false);
        });

        binding.ivInfo5.setOnClickListener(view -> {
            heartAgeAnalysisResponse = new Gson().fromJson(SharedPref.getHeartAgeAnalysis(), HeartAgeAnalysisResponse.class);
            Intent intent;
            boolean isAnalysis = (heartAgeAnalysisResponse != null && heartAgeAnalysisResponse.getData() != null);
            gotoIntroPage("HeartAge", "Heart Age", isAnalysis, false);
        });


        binding.rlKnowYourHealth.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_hra", context);
            boolean isShown = SharedPref.getWellBeingIntroShownHealth();
            boolean isAnalysis = (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0);
            SharedPref.putAnalysisStatus(isAnalysis);
            Log.d("reponse", new Gson().toJson(getAnalysisResponse));
            if (!isShown) {
                SharedPref.putWellBeingIntroShownHealth(true);
                gotoIntroPage("KnowYourHealth", "Health Score", isAnalysis, true);
            } else {
                Intent intent;
                if (isAnalysis) {
                    intent = new Intent(context, HRAAnalysisActivity.class);
                } else {
                    intent = new Intent(context, HRAQuestionsActivity.class);
                }
                startActivity(intent);
            }

        });

        binding.rlKnowYourImmunity.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_immunity", context);
            boolean isAnalysis = (iraHealthScoreResponse != null && iraHealthScoreResponse.getIraHealthScoreData() != null &&
                    iraHealthScoreResponse.getIraHealthScoreData().getPlaySports() != null && !iraHealthScoreResponse.getIraHealthScoreData()
                    .getPlaySports().isEmpty() && Integer.parseInt(iraHealthScoreResponse.getIraHealthScoreData().getPlaySports()) > 0);

            boolean isShown = SharedPref.getWellBeingIntroShownImmunity();
            if (!isShown) {
                SharedPref.putWellBeingIntroShownImmunity(true);
                gotoIntroPage("KnowYourImmunity", "Immunity Score", isAnalysis, true);
            } else {
                Intent intent;
                if (isAnalysis) {
                    intent = new Intent(context, IRAAnalysisActivity.class);
                } else {
                    intent = new Intent(context, IraActivity.class);
                }
                intent.putExtra("from", IRA_STATUS_COMPLETED);
                startActivity(intent);
            }

        });

        binding.rlDass21.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_das", context);
            dassAnalysisResponse = new Gson().fromJson(SharedPref.getDASSAnalysis(), DassAnalysisResponse.class);
            boolean isAnalysis = (dassAnalysisResponse != null && dassAnalysisResponse.getData() != null);
            boolean isShown = SharedPref.getWellBeingIntroShownDAS();
            if (!isShown) {
                SharedPref.putWellBeingIntroShownDAS(true);
                gotoIntroPage("KnowYourDAS", "DAS Score", isAnalysis, true);
            } else {
                Intent intent;
                if (isAnalysis) {
                    intent = new Intent(context, Dass21AnalysisActivity.class).putExtra("comingFrom", "");
                } else {
                    intent = new Intent(context, Dass21QuestionsActivity.class);
                }
                startActivity(intent);
            }
        });

        binding.rlHeartAge.setOnClickListener(view -> {
            heartAgeAnalysisResponse = new Gson().fromJson(SharedPref.getHeartAgeAnalysis(), HeartAgeAnalysisResponse.class);
            boolean isAnalysis = (heartAgeAnalysisResponse != null && heartAgeAnalysisResponse.getData() != null);
            boolean isShown = SharedPref.getWellBeingIntroShownHeartAge();
            if (!isShown) {
                SharedPref.putWellBeingIntroShownHeartAge(true);
                gotoIntroPage("HeartAge", "Heart Age", isAnalysis, true);
            } else {
                Intent intent;
                if (isAnalysis) {
                    intent = new Intent(context, HeartAgeAnalysisActivity.class);
                } else {
                    intent = new Intent(context, HeartAgeQuestionsActivity.class);
                }
                startActivity(intent);
            }

        });

        binding.ivHome.setOnClickListener(view -> {
            Intent i = new Intent(this, NewDashboardActivity.class);
            startActivity(i);
            finish();
        });


        binding.rlMentalWellness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APILogs.INSTANCE.activityTracker("A_DB_BI_AZ_KYW_MW", context);
                APILogs.INSTANCE.activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_mentalwellness", context);
                startActivity(new Intent(context, RespiratoryCheck.class)
                        .putExtra("comingFrom", "metalFitness"));

            }
        });

        binding.rlRespiratory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APILogs.INSTANCE.activityTracker("A_DB_BI_AZ_KYW_RH", context);
                startActivity(new Intent(context, RespiratoryCheck.class)
                        .putExtra("comingFrom", "respiratory"));
            }
        });

    }

    private void getHeartAnalysis() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetHeartAgeAnalysisRequest request = new GetHeartAgeAnalysisRequest(getString(R.string.heartage_i_id), SharedPref.getHeartAgeConversationID());
        Call<HeartAgeAnalysisResponse> call = apiInterfaceWyh.getHeartAgeAnalysis(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<HeartAgeAnalysisResponse>() {
            @Override
            public void onResponse(Call<HeartAgeAnalysisResponse> call, Response<HeartAgeAnalysisResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {

                }
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_get_analysis_success));

                    SharedPref.putHeartAgeAnalysis(new Gson().toJson(response.body()));
                    /*Intent intent = new Intent(context, HeartAgeAnalysisActivity.class);
                    startActivity(intent);
                    finish();*/
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_get_analysis_failed));
//                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HeartAgeAnalysisResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_get_analysis_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getIRAHealthScore() {
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        ConversationRequest iraRequest = new ConversationRequest(IRA_INTEGRATION_ID, "");
        Call<IRAHealthScoreResponse> call = apiInterfaceWyh.getIRAHealthScore(SharedPref.getAuthToken(), iraRequest);
        //Log.v("Url_Request_conve", call.request().url() + "\n" + new Gson().toJson(iraRequest) + "\n" + SharedPref.getAuthToken());

        call.enqueue(new Callback<IRAHealthScoreResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<IRAHealthScoreResponse> call, Response<IRAHealthScoreResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_ira_answers_score_success));
                    if (response.body().getIraHealthScoreData() != null && !response.body().getIraHealthScoreData().getPlaySports().equals("0") && response.body().getIraHealthScoreData().getPlaySports() != null) {
                        healthScore = response.body().getIraHealthScoreData().getPlaySports();
                        iraHealthScoreResponse = response.body();
                        Gson gson = new Gson();
                        String IRAHealthData = gson.toJson(response.body());
                        SharedPref.putIRAHealthData(IRAHealthData);
//                        binding.rlIra.setClickable(false);
                    }
                } else if (response.code() == 411) {
                    SharedPref.putIRAHealthData("");
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_ira_answers_score_failed));
                }
            }

            @Override
            public void onFailure(Call<IRAHealthScoreResponse> call, Throwable t) {
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_ira_answers_score_failed));
            }
        });
    }

    public void showSharePopup(Context context, String fileName) {
        try {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
            ShareOptionPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.share_option_pop_up, null, false);
            alertBuilder.setView(binding.getRoot());
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.setCancelable(true);
            if (!alertDialog.isShowing()) alertDialog.show();
            binding.tvMsg.setText("Share");
            binding.ivWhatsapp.setOnClickListener(v -> {
                APILogs.INSTANCE.activityTracker("A_DB_DB_Tile_FS_Share_Whtsapp", context);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, fileName);
                context.startActivity(shareIntent);
                alertDialog.dismiss();
            });
            Rect displayRectangle = new Rect();
            Window window = ((WellBeingActivity) context).getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.getWindow().setLayout((int) (displayRectangle.width() * 0.88f), RelativeLayout.LayoutParams.WRAP_CONTENT);
        } catch (Exception e) {
            e.toString();
        }
    }

    public void getAnalysis() {
        GetAnalysisRequest request = new GetAnalysisRequest(getString(R.string.hra_i_id), "");
        Call<GetAnalysisResponse> call = apiInterfaceWyh.getAnalysis(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<GetAnalysisResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetAnalysisResponse> call, @NonNull Response<GetAnalysisResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_get_analysis_success));
                    SharedPref.putHRAAnalysis(new Gson().toJson(response.body()));
                    getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
                    boolean isAnalysis = (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0);
                    SharedPref.putAnalysisStatus(isAnalysis);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_get_analysis_failed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetAnalysisResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_get_analysis_failed));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPref.getHeartAgeAnalysis().isEmpty())
            getHeartAnalysis();
        else
            heartAgeAnalysisResponse = new Gson().fromJson(SharedPref.getHeartAgeAnalysis(), HeartAgeAnalysisResponse.class);
        if (SharedPref.getIRAHealthData().isEmpty())
            getIRAHealthScore();
        else
            iraHealthScoreResponse = new Gson().fromJson(SharedPref.getIRAHealthData(), IRAHealthScoreResponse.class);
        if (SharedPref.getHRAAnalysis().isEmpty())
            getAnalysis();
        else
            getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);

    }

    private void getDassAnalysis() {
        IntegrationIdRequest request = new IntegrationIdRequest(getResources().getString(R.string.dass21_i_id));
        Call<DassAnalysisResponse> call = apiInterfaceWyh.getDassAnalysis(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<DassAnalysisResponse>() {
            @Override
            public void onResponse(Call<DassAnalysisResponse> call, Response<DassAnalysisResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().getData() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_das_analysis_success));
                    dassAnalysisResponse = response.body();
                    SharedPref.putDASSAnalysis(new Gson().toJson(response.body()));

                    if (response.body().getFreeVoucher() != null) {
                        showScratchCard(response.body().getFreeVoucher());
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_das_analysis_failed));
                }
            }

            @Override
            public void onFailure(Call<DassAnalysisResponse> call, Throwable t) {
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_das_analysis_failed));
            }
        });
    }

    private void showScratchCard(FreeVoucher freeVoucher) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        PopUpScratchCardScratchableBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pop_up_scratch_card_scratchable, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        if (!freeVoucher.isScratched())
            voucherIdRequest = new VoucherIdRequest(freeVoucher.getFreebieID());

        binding.scratchView.setScratchListener(WellBeingActivity.this);
        binding.tvTitle.setText(freeVoucher.getVendorName());
        binding.tvValue.setText(freeVoucher.getVoucherValue() + " off");
        binding.tvDescription.setText(freeVoucher.getVoucherDescription());
        binding.tvCouponCode.setText(freeVoucher.getVoucherCode());
        binding.tvAmount.setText("₹" + freeVoucher.getVoucherValue());


        binding.btnRedeem.setOnClickListener(view -> alertDialog.dismiss());

        binding.tvCopy.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", freeVoucher.getVoucherCode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
        });

        Glide.with(context)
                .load(freeVoucher.getVendorLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivVendorLogo);

        Rect displayRectangle = new Rect();
        Window window = ((WellBeingActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }

    private void FetchQuizReward() {
        ActivityRewardRequest activityRewardRequest = new ActivityRewardRequest(NewDashboardHelper.Companion.getTrasactionId(), NewDashboardHelper.Companion.getFeatureName());
        Call<CommonSuccessResponse> call = apiInterfaceWyh.FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewDashboardHelper.Companion.setTrasactionId(null);
                    NewDashboardHelper.Companion.setFeatureName(null);
                    NewDashboardHelper.Companion.setActivityName(null);
                    if (response.body().getQuizathonRewardData() != null) {
                        quizathonRewardData=response.body().getQuizathonRewardData();
                        getQuizathonRewardPopup(response.body().getQuizathonRewardData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public boolean checkIsFromQuizqathon() {
        if (NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()) {
            return true;
        }
        return false;
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
                } else if (rewardtype.equalsIgnoreCase("future")) {
                    QuizRewardDialog.INSTANCE.showFutureRewardDialog(context, data.getDialogModel(), data.getClaimDate());
                }

            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void onDialogDismiss() {
        cancelDialog();
    }

    public void cancelDialog() {
        try {
            if (spinRewardsData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, spinRewardsData.getRewardType(), this);
            } else if (quizathonRewardData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, quizathonRewardData.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
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
        if (i >= 20) {
            if (voucherIdRequest != null) {
                updateScratchStatus();
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

    }

    private void updateScratchStatus() {
        Call<CommonSuccessResponse> call = apiInterfaceWyh.updateScratchStatus(SharedPref.getAuthToken(), voucherIdRequest);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_success));
                    voucherIdRequest = null;
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_failed));
            }
        });
    }

    @Override
    public void onScratchStarted() {

    }

    private void gotoIntroPage(String cameFrom, String categoryName, boolean isAnalysis, boolean shownScreen) {
        Intent intent = new Intent(context, WellBeingDisclaimerActivity.class);
        intent.putExtra("came_from", cameFrom);
        intent.putExtra("CategoryName", categoryName);
        intent.putExtra("isAnalysis", isAnalysis);
        intent.putExtra("shownScreen", shownScreen);
        startActivityForResult(intent, 101);
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
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });


        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogStamp.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
        });

        binding.scratchView.setScratchListener(this);

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PendingActivityDashboard) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusStamp = alertBuilder.create();
        alertDialogBonusStamp.setCancelable(true);
        if (!alertDialogBonusStamp.isShowing())
            alertDialogBonusStamp.show();


        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

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

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusStamp.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
        });
        binding.scratchView.setScratchListener(WellBeingActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((WellBeingActivity) context).getWindow();

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
//        binding.btnPositive.setText("Collect");
        binding.btnPositive.setOnClickListener(view -> {
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

    private void showRewardsPopupDialogBox() {
        if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0) {
            Log.d("face scan", new Gson().toJson(NewDashboardHelper.Companion.getPopUpShowModels()));
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
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        }
    }
}