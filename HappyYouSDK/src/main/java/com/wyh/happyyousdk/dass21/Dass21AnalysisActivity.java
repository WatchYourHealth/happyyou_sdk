package com.wyh.happyyousdk.dass21;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityDass21AnalysisBinding;
import com.wyh.happyyousdk.databinding.DassScoreInterpretationLayoutBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.PopUpScratchCardScratchableBinding;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.VoucherIdRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.dass21.DassAnalysisData;
import com.wyh.happyyousdk.model.response.dass21.DassAnalysisResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dass21AnalysisActivity extends AppCompatActivity implements ScratchListener,
        rewardDialogCloseListener {

    ActivityDass21AnalysisBinding binding;
    Context context;

    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    DassAnalysisResponse dassAnalysisResponse;
    String comingFrom = "";
    FreeVoucher freeVoucher;
    VoucherIdRequest voucherIdRequest;
    DassAnalysisData dassAnalysisData = new DassAnalysisData();

    List<PopUpShowModel> popUpShowModelsList;
    boolean isPositiveBtn = false;
    AlertDialog alertDialogBonusRewards;
    AssignRewardsResponse.SpinRewardsData spinRewardsData = null;
    QuizathonRewardData quizathonRewardData = null;
    boolean Quizrewards = false;

    boolean isDepressionInGoodRange = false, isAnxietyInGoodRange = false, isStressInGoodRange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dass21_analysis);
        context = this;
        try {
            SharedPref.init(context);
            SharedPreference.init(context);

            apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
            progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            try {
                if (getIntent().getExtras() != null && getIntent().getExtras().get("spinrewards") != null) {
                    spinRewardsData = (AssignRewardsResponse.SpinRewardsData) getIntent().getExtras().get("spinrewards");
                } else {
                    spinRewardsData = null;
                }
                if (getIntent().getExtras() != null && getIntent().getExtras().get("Quizrewards") != null) {
                    Quizrewards = getIntent().getBooleanExtra("Quizrewards", false);
                } else {
                    Quizrewards = false;
                }

            } catch (Exception ex) {

            }
            comingFrom = getIntent().getStringExtra("comingFrom");
            freeVoucher = new Gson().fromJson(getIntent().getStringExtra("voucher"), FreeVoucher.class);

            String popups = getIntent().getStringExtra("popups");
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            if (popups != null) {
                popUpShowModelsList = gson.fromJson(popups, new TypeToken<List<PopUpShowModel>>() {
                }.getType());
                showRewardsPopupDialogBox();
            }

            if (spinRewardsData != null) {
                getSpinRewardPopup(spinRewardsData);
            } else if (checkIsFromQuizqathon() && Quizrewards) {
                FetchQuizReward();
                //getQuizathonRewardPopup(quizathonRewardData);
            } else if (freeVoucher != null) {
                showScratchCard(freeVoucher);
            }

            JSONObject customObj = new JSONObject();
            try {
                customObj.put("PAGE_ID", "DASSAnalysis");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            

            binding.includeBack.ivBack.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            binding.includeBack.tvBack.setText("DAS Score");
            binding.includeBack.tvBack.setTextColor(getResources().getColor(R.color.white));
            binding.includeBack.llBack.setOnClickListener(view -> finish());
            binding.includeBack.ivMenu.setVisibility(View.VISIBLE);
            binding.includeBack.ivMenu.setImageResource(R.drawable.ic_info);
            binding.includeBack.ivMenu.setColorFilter(getResources().getColor(R.color.white));
            binding.includeBack.ivMenu.setOnClickListener(view -> {
                showDasInfoLayout();
            });

            dassAnalysisResponse = new Gson().fromJson(SharedPref.getDASSAnalysis(), DassAnalysisResponse.class);

            if (comingFrom.equalsIgnoreCase("dasQuestion") && comingFrom != "") {
                if (dassAnalysisResponse.getFeedbackDetails() != null && dassAnalysisResponse.getFeedbackDetails() != null && dassAnalysisResponse.getFeedbackDetails().getStarConfig() != null) {
                    FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                    instance.showPopUpFeedback(Dass21AnalysisActivity.this, dassAnalysisResponse.getFeedbackDetails());
                }
            }

            dassAnalysisData = dassAnalysisResponse.getData().get(0);
            int anxietyScore = dassAnalysisData.getAnxietyScore();
            int depressionScore = dassAnalysisData.getDepressionScore();
            int stressScore = dassAnalysisData.getStressScore();
            binding.tvAnxietyScore.setText(anxietyScore + "/21");
            binding.tvDepressionScore.setText(depressionScore + "/21");
            binding.tvStressScore.setText(stressScore + "/21");

            binding.progressAnxiety.setProgress(anxietyScore);
            binding.progressDepression.setProgress(depressionScore);
            binding.progressStress.setProgress(stressScore);


            if (depressionScore >= 0 && depressionScore <= 4) {
                binding.progressDepression.setProgressBarColor(getResources().getColor(R.color.light_blue));
                isDepressionInGoodRange = true;
            } else if (depressionScore >= 5 && depressionScore <= 6) {
                isDepressionInGoodRange = true;
                binding.progressDepression.setProgressBarColor(getResources().getColor(R.color.blue));
                binding.progressDepression.setProgressBarColor(getResources().getColor(R.color.blue));
            } else if (depressionScore >= 7 && depressionScore <= 10)
                binding.progressDepression.setProgressBarColor(getResources().getColor(R.color.light_orange));
            else if (depressionScore >= 11 && depressionScore <= 13)
                binding.progressDepression.setProgressBarColor(getResources().getColor(R.color.light_pink));
            else if (depressionScore >= 14)
                binding.progressDepression.setProgressBarColor(getResources().getColor(R.color.pink));

            if (anxietyScore >= 0 && anxietyScore <= 3) {
                isAnxietyInGoodRange = true;
                binding.progressAnxiety.setProgressBarColor(getResources().getColor(R.color.light_blue));
            } else if (anxietyScore >= 4 && anxietyScore <= 5) {
                isAnxietyInGoodRange = true;
                binding.progressAnxiety.setProgressBarColor(getResources().getColor(R.color.blue));
            } else if (anxietyScore >= 6 && anxietyScore <= 7)
                binding.progressAnxiety.setProgressBarColor(getResources().getColor(R.color.light_orange));
            else if (anxietyScore >= 8 && anxietyScore <= 9)
                binding.progressAnxiety.setProgressBarColor(getResources().getColor(R.color.light_pink));
            else if (anxietyScore >= 10)
                binding.progressAnxiety.setProgressBarColor(getResources().getColor(R.color.pink));

            if (stressScore >= 0 && stressScore <= 7) {
                isStressInGoodRange = true;
                binding.progressStress.setProgressBarColor(getResources().getColor(R.color.light_blue));
            } else if (stressScore >= 8 && stressScore <= 9) {
                isStressInGoodRange = true;
                binding.progressStress.setProgressBarColor(getResources().getColor(R.color.blue));
            } else if (stressScore >= 10 && stressScore <= 12)
                binding.progressStress.setProgressBarColor(getResources().getColor(R.color.light_orange));
            else if (stressScore >= 13 && stressScore <= 16)
                binding.progressStress.setProgressBarColor(getResources().getColor(R.color.light_pink));
            else if (stressScore >= 17)
                binding.progressStress.setProgressBarColor(getResources().getColor(R.color.pink));

            try {
                binding.tvDepressionDesc.loadData(dassAnalysisData.getDepressionDesc(), "text/html", "utf-8");
                binding.tvAnxietyDesc.loadData(dassAnalysisData.getAnxietyDesc(), "text/html", "utf-8");
                binding.tvStressDesc.loadData(dassAnalysisData.getStressDesc().replace("&#39;", "\'"), "text/html", "utf-8");
                binding.tvDepressionDesc.setBackgroundColor(Color.TRANSPARENT);
                binding.tvAnxietyDesc.setBackgroundColor(Color.TRANSPARENT);
                binding.tvStressDesc.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (isDepressionInGoodRange && isAnxietyInGoodRange && isStressInGoodRange) {
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ira_analysis_blue_bg));
            } else
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ira_analysis_red_bg));

            binding.btnOk.setOnClickListener(view -> finish());

            binding.btnRetake.setOnClickListener(view -> {
                Intent intent = new Intent(context, Dass21QuestionsActivity.class);
                startActivity(intent);
                finish();
            });

            binding.ivHome.setOnClickListener(view -> {
                Intent intent = new Intent(this, NewDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean checkIsFromQuizqathon() {
        if (NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()) {
            return true;
        }
        return false;
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
                        quizathonRewardData = response.body().getQuizathonRewardData();
                        getQuizathonRewardPopup(response.body().getQuizathonRewardData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {

            }
        });
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
            quizathonRewardData = data;
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


    private void showScratchCard(FreeVoucher freeVoucher) {
        try {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

            PopUpScratchCardScratchableBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pop_up_scratch_card_scratchable, null, false);
            alertBuilder.setView(binding.getRoot());
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.setCancelable(true);
            if (!alertDialog.isShowing())
                alertDialog.show();

            if (!freeVoucher.isScratched())
                voucherIdRequest = new VoucherIdRequest(freeVoucher.getFreebieID());

            binding.scratchView.setScratchListener(Dass21AnalysisActivity.this);
            binding.tvTitle.setText(freeVoucher.getVendorName());
            binding.tvValue.setText(freeVoucher.getVoucherValue() + " off");
            binding.tvDescription.setText(freeVoucher.getVoucherDescription());
            binding.tvCouponCode.setText(freeVoucher.getVoucherCode());
            binding.tvAmount.setText("₹" + freeVoucher.getVoucherValue());


            Glide.with(context)
                    .load(freeVoucher.getVendorLogo())
                    .error(R.drawable.dummy_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.ivVendorLogo);

            voucherIdRequest = new VoucherIdRequest(freeVoucher.getFreebieID());

            binding.btnRedeem.setOnClickListener(view -> alertDialog.dismiss());

            binding.tvCopy.setOnClickListener(view -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", freeVoucher.getVoucherCode());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            });

            Rect displayRectangle = new Rect();
            Window window = ((Dass21AnalysisActivity) context).getWindow();

            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.7f), (int) (displayRectangle.height() * 0.5f));
        } catch (Exception e) {
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
        if (i >= 20 && voucherIdRequest != null) {
            updateScratchStatus();
            scratchCardLayout.onFullReveal();
        } else if (i >= 20) {
            scratchCardLayout.onFullReveal();
            {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (alertDialogBonusRewards != null && alertDialogBonusRewards.isShowing()) {
                            alertDialogBonusRewards.dismiss();
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

    private void showDasInfoLayout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
        DassScoreInterpretationLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dass_score_interpretation_layout, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        List<DassInfoPojo> data = new ArrayList<>();

        data.add(new DassInfoPojo("Depression", "0 - 4", "5 - 6", "7 - 10", "11 - 13", "≥ 14"));
        data.add(new DassInfoPojo("Anxiety", "0 - 3", "4 - 5", "6 - 7", "8 - 9", "≥ 10"));
        data.add(new DassInfoPojo("Stress", "0 - 7", "8 - 9", "10 - 12", "13 - 16", "≥ 17"));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        DassInfoAdapter dassInfoAdapter = new DassInfoAdapter(context, data);
        binding.rvDassData.setAdapter(dassInfoAdapter);
        binding.rvDassData.setLayoutManager(linearLayoutManager);
        binding.rvDassData.setHasFixedSize(true);

        binding.btnOK.setOnClickListener(view -> alertDialog.dismiss());
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome), FROM_HTML_MODE_LEGACY));
        }else{
            binding.tvContent.setText(Html.fromHtml(getString(R.string.life_style_welcome)));
        }

        binding.btnOK.setOnClickListener(v-> {
            alertDialog.dismiss();
        });
*/
        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.7f));
    }

    private void showRewardsPopupDialogBox() {
        if (!popUpShowModelsList.isEmpty()) {
            int i = 0;
            PopUpShowModel firstData = popUpShowModelsList.get(i);
            if (popUpShowModelsList.size() > 1 && Objects.equals(firstData.getKey(), "Rewards")) {
                i = 1;
                firstData = popUpShowModelsList.get(i);
            }
            popUpShowModelsList.remove(i);
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
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
        }
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
            Intent intent = new Intent(context, HRAAnalysisActivity.class);
            startActivity(intent);
            finish();
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
            finish();
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
}