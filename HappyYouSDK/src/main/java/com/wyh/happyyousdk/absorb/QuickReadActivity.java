package com.wyh.happyyousdk.absorb;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
;

import com.wyh.happyyousdk.Eventbus.Bookmarkevent;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.ScratchCardPopUpBinding;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.request.absorb.AddBookmarkRequest;
import com.wyh.happyyousdk.model.request.absorb.FetchBlogRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.AddReadingBlogDurationResponse;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.absorb.AddBookmarkResponse;
import com.wyh.happyyousdk.model.response.absorb.FetchBlogResponse;

import com.wyh.happyyousdk.databinding.ActivityQuickReadBinding;

import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.FetchRewardsRequest;
import com.wyh.happyyousdk.model.request.absorb.AddBlogDurationRequest;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.FetchRewardsResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickReadActivity extends AppCompatActivity implements ScratchListener, rewardDialogCloseListener {
    ActivityQuickReadBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    AlertDialog alertDialogBonusRewards;
    boolean isBookmark;
    String articleCode;
    long startTime = 0, backTime = 0;
    Handler handlerAddReadingTime;
    boolean isPositiveBtn = false, isBookmarked = false;

    int dataPosition, blogId = 0;
    AssignRewardsResponse.SpinRewardsData spinRewardsData;
    QuizathonRewardData quizathonRewardData;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quick_read);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        startTime = System.currentTimeMillis();

        Intent i = getIntent();
        binding.includeToolbar.llBack.setOnClickListener(view -> onBackPressed());
        binding.includeToolbar.tvBack.setText("Quick Read");
        binding.includeToolbar.tvBack.setTextColor(getColor(R.color.black));
        binding.includeToolbar.ivBack.setColorFilter(getColor(R.color.black));

        articleCode = i.getStringExtra("article_code");
        dataPosition = i.getIntExtra("dataPosition", -1);

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "QuickReadActivity");
            customObj.put("article_code", articleCode);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        //Log.d("article code", articleCode);


        binding.ivHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        fetchBlog(articleCode);

        binding.ivBookmark.setOnClickListener(v -> addBookmark(articleCode, isBookmark));

        handlerAddReadingTime = new Handler(Looper.getMainLooper());
        handlerAddReadingTime.postDelayed(() -> {
            backTime = System.currentTimeMillis();
            int readingMinutes = Math.toIntExact(TimeUnit.MILLISECONDS.toMinutes(backTime - startTime));
            //Log.v("value is", readingMinutes + "");
            addReadingMinutes(readingMinutes);
        }, 60000);
//        binding.im.setText(title);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        long lastTime = System.currentTimeMillis();
        int readingMinutes = Math.toIntExact(TimeUnit.MILLISECONDS.toMinutes(lastTime - startTime));
        //Log.v("value is", readingMinutes + "");

        if (readingMinutes >= 1) {
            updateReadingMinutes(readingMinutes);
        } else {
            handlerAddReadingTime.removeCallbacksAndMessages(null);
            finish();
        }


        if (isBookmarked && dataPosition != -1) {
            EventBus.getDefault().postSticky(new Bookmarkevent(dataPosition));
        }
    }

    private void addReadingMinutes(int readingMinutes) {
        CommonUtils.showProgressDialige(context);
        AddBlogDurationRequest request = new AddBlogDurationRequest(articleCode, readingMinutes, 0, "");
        Log.d("add Reading blog Request", new Gson().toJson(request));
        Call<AddReadingBlogDurationResponse> call = apiInterfaceWyh.blogReadingDuration(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AddReadingBlogDurationResponse> call, @NonNull Response<AddReadingBlogDurationResponse> response) {
                CommonUtils.dismissDialoge();
                Log.d("add Reading blog Response", response.code() + " " + new Gson().toJson(response.body()));

                try {
                    if (response.body() != null && response.body().isSuccess()) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.blog_reading_duration_success));
                        blogId = response.body().getData().getBlogId();
                        NewDashboardHelper.Companion.getPopUpShowModels().clear();

                        if (response.body().getSpinTheWheelRewardsModel() != null) {
                            spinRewardsData = response.body().getSpinTheWheelRewardsModel();
                            getSpinRewardPopup(response.body().getSpinTheWheelRewardsModel());
                        } else if (checkIsFromQuizqathon()) {
                            FetchQuizReward();
                        }

                        if (response.body().getRewards() != null && response.body().getRewards().getReward() != null) {
//                        showRewardsPopup(response.body().getRewards().getReward());
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, response.body().getRewards().getReward()));
                        }

                        if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null) {
//                        showBonusRewardsPopup(response.body().getRewards().getBonusRewards(), context);
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, response.body().getRewards().getBonusRewards()));
                        }

                        if (!NewDashboardHelper.Companion.getPopUpShowModels().isEmpty())
                            showRewardsPopupDialogBox();
                        else {
                            fetchRewards();
                        }
                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.blog_reading_duration_failed));
                    }
                } catch (Exception e) {
                    CommonUtils.dismissDialoge();

                }
            }

            @Override
            public void onFailure(@NonNull Call<AddReadingBlogDurationResponse> call, @NonNull Throwable t) {
                CommonUtils.dismissDialoge();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.blog_reading_duration_failed));
            }
        });
    }

    private void updateReadingMinutes(int readingMinutes) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AddBlogDurationRequest request = new AddBlogDurationRequest(articleCode, readingMinutes, blogId, "");
        Log.d("update Reading blog", new Gson().toJson(request));
        Call<CommonSuccessResponse> call = apiInterfaceWyh.updateBlogReadingDuration(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.body().isSuccess()) {
                    Log.d("update Reading blog", new Gson().toJson(response.body()));
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.blog_update_reading_duration_success));
                    finish();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.blog_update_reading_duration_failed));
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.blog_update_reading_duration_failed));
                finish();
            }
        });
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

    private void fetchRewards() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        FetchRewardsRequest request = new FetchRewardsRequest("blogread");
        Call<FetchRewardsResponse> call = apiInterfaceWyh.fetchRewards(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<FetchRewardsResponse> call, @NonNull Response<FetchRewardsResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getData() != null) {
                        if (!response.body().getData().isEmpty()) {
                            showRewardsPopupNew(response.body().getData().get(0).getPopupMessage(), context);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchRewardsResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
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

    private void showRewardsPopup(String rewards) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        ScratchCardPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.scratch_card_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();


        alertDialog.setOnDismissListener(dialogInterface -> {
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

        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvPoints.setText(points);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, RewardsActivity.class);
            context.startActivity(intent);
        });
        binding.scratchView.onFullReveal();
        binding.scratchView.setScratchListener(QuickReadActivity.this);


        Rect displayRectangle = new Rect();
        Window window = ((QuickReadActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
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
        handler.postDelayed(alertDialog::dismiss, 3000);

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> alertDialog.dismiss());

        binding.scratchView.onFullReveal();

        binding.btnPositive.setOnClickListener(view -> isPositiveBtn = true);
        binding.scratchView.setScratchListener(QuickReadActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


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
        binding.scratchView.setScratchListener(QuickReadActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((QuickReadActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialogBonusRewards.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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
            Log.e("HappyYou", "Error occurred", ex);
        }
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
        }
    }

    private void fetchBlog(String articleCode) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        FetchBlogRequest fetchBlogRequest = new FetchBlogRequest(articleCode);
        Call<FetchBlogResponse> call = apiInterfaceWyh.fetchBlogAPI(SharedPref.getAuthToken(), fetchBlogRequest);
        call.enqueue(new Callback<FetchBlogResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchBlogResponse> call, @NonNull Response<FetchBlogResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {
                    refreshAuthToken();
                }
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_fetch_blogs_success));
                    binding.tvBlogDescription.loadData(response.body().getData().getHtmlContent(), "text/html", "utf-8");
                    Glide.with(context)
                            .load(response.body().getData().getImgPath())
                            .error(R.drawable.dummy_image)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(binding.ivActivityBg);
                    Glide.with(context)
                            .load(response.body().getData().getImgPath())
                            .error(R.drawable.dummy_image)
                            .apply(bitmapTransform(new BlurTransformation()))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(binding.ivActivityBgBlur);
                    isBookmark = response.body().getData().getIsBookMarked();
                    if (response.body().getData().getIsBookMarked()) {
                        binding.ivBookmark.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                    } else {
                        binding.ivBookmark.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_fetch_blogs_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchBlogResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_fetch_blogs_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBookmark(String articleCode, boolean b) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AddBookmarkRequest request = new AddBookmarkRequest(articleCode, !isBookmark);
        Call<AddBookmarkResponse> call = apiInterfaceWyh.addBookMark(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AddBookmarkResponse> call, @NonNull Response<AddBookmarkResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {
                    refreshAuthToken();
                }
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_success));
                    if (!isBookmark) {
                        binding.ivBookmark.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                        Toast.makeText(context, "Successfully added to Bookmark", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.ivBookmark.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                        Toast.makeText(context, "Removed from Bookmark", Toast.LENGTH_SHORT).show();
                    }
                    isBookmarked = true;

                    fetchBlog(articleCode);

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddBookmarkResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
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
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RefreshTokenResponse> call, @NonNull Response<RefreshTokenResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess() &&
                        response.body().getData().getAuthToken() != null && !response.body().getData().getAuthToken().isEmpty()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    fetchBlog(articleCode);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                   /* Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
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
              /*  Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MobileNumberActivity.class);
                startActivity(intent);
                SharedPref.clearSharedPref();
                finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
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
            scratchCardLayout.onFullReveal();
            {
                if (alertDialogBonusRewards != null && alertDialogBonusRewards.isShowing()) {
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialogBonusRewards.dismiss();
                        }
                    }, 3000);
                }
            }
        }
    }

    @Override
    public void onScratchStarted() {

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
            Log.e("HappyYou", "Error occurred", e);
        }
    }

    public boolean checkIsFromQuizqathon() {
        if (NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()) {
            return true;
        }
        return false;
    }
}