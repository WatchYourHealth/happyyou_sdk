package com.wyh.happyyousdk.quiz;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.Activities.spinwheelreward.RewardsListActivity;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;

import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.dashboard.model.SaveBannerQuizRequestModel;
import com.wyh.happyyousdk.databinding.ActivityQuizBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.SaveQuizRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.BannerQuizModel;
import com.wyh.happyyousdk.model.response.QuestionModel;
import com.wyh.happyyousdk.model.response.SaveQuizResponseModel;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.quiz.adapter.QuizOptionAdapter;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.PendingActivityDashboard;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity implements ScratchListener {
    ActivityQuizBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    QuizOptionAdapter adapter;
    int index, totalScore, qId;
    String categoryName, comingFrom, rewardDate;
    ArrayList<QuestionModel> data;
    boolean isStamp = false;
    AlertDialog alertDialogBonusRewards, alertDialogStamp, alertDialogBonusStamp;
    boolean isPositiveBtn = false;
    int stampId = -1;

    boolean isAttempted = false;

    public boolean isAttempted() {
        return isAttempted;
    }

    public void setAttempted(boolean attempted) {
        isAttempted = attempted;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAttempted) {
                    //binding.nextBtn.setVisibility(View.VISIBLE);
                } else {
                    binding.nextBtn.setVisibility(View.GONE);
                }
            }
        }, 200);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");


        index = 0;


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("data");
            comingFrom = extras.getString("comingFrom");
            categoryName = extras.getString("CategoryName");
            qId = extras.getInt("qId");
            rewardDate = extras.getString("rewardDate");
            binding.includeToolbar.tvBack.setText(categoryName);
            final GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            data = gson.fromJson(value, new TypeToken<List<QuestionModel>>() {
            }.getType());
        }

        setToolBar();
        setQuestion(-1);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        binding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevQuestion();
            }
        });
        setProgressBar(0);
    }

    private void setToolBar() {
        binding.includeToolbar.tvBack.setText(categoryName);
        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));
        binding.includeToolbar.ivMenu.setVisibility(View.GONE);
        binding.includeToolbar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });

        setPrevBtn();

    }

    private void setQuestion(int ans) {
        binding.tvQuesion.setText("Q." + (index + 1) + " " + data.get(index).getQuestion());
        Glide.with(this).load(data.get(index).getImage()).into(binding.ivImageQuestion);
        String answer = data.get(index).getUserAns() != null ? data.get(index).getUserAns().toString() : "";
        isAttempted = false;
        adapter = new QuizOptionAdapter(context, data.get(index).options, answer, new QuizOptionAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, String name) {
                if (index < data.size()) {
                    if (data.get(index).getUserAns() == null || data.get(index).getUserAns().equals("")) {
                        data.get(index).setUserAns(data.get(index).getOptions().get(position));
                        if (Objects.equals(data.get(index).getAnswer(), name)) {
                            totalScore++;
                        }
                    } else {
                        String prevAns = data.get(index).getUserAns().toString();
                        if (!prevAns.equals(name)) {
                            data.get(index).setUserAns(data.get(index).getOptions().get(position));
                            if (!Objects.equals(data.get(index).getAnswer(), name)) {
                                if (totalScore > 0) {
                                    totalScore--;
                                }
                            } else {
                                totalScore++;
                            }
                        }
                    }
                    binding.recyclerViewOption.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.nextBtn.setVisibility(View.GONE);
                            nextQuestion();
                        }
                    }, 500);
                }
            }
        });

        binding.recyclerViewOption.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerViewOption.setAdapter(adapter);

    }

    public void saveQuestionData(Boolean isFromDashboardBanner) {
        Call<SaveQuizResponseModel> call = null;
        if (isFromDashboardBanner) {
            BannerQuizModel bannerQuizModel = new BannerQuizModel(qId, categoryName, data.size(), totalScore, data);
            SaveBannerQuizRequestModel request = new SaveBannerQuizRequestModel(qId, new Gson().toJson(bannerQuizModel));
            call = apiInterfaceWyh.saveQuizBannerHistory(SharedPref.getAuthToken(), request);
        } else {
            SaveQuizRequest request = new SaveQuizRequest(qId, getString(R.string.quiz_i_id), categoryName, data.size(), totalScore, "Completed", new Gson().toJson(data));
            call = apiInterfaceWyh.saveQuestions(SharedPref.getAuthToken(), request);
        }
        call.enqueue(new Callback<SaveQuizResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SaveQuizResponseModel> call, @NonNull Response<SaveQuizResponseModel> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_save_answer_new_success));
                    Intent intent = new Intent(QuizActivity.this, QuizScoreActivity.class);
                    intent.putExtra("data", new Gson().toJson(data));
                    intent.putExtra("your_score", totalScore);
                    intent.putExtra("total_score", data.size());
                    intent.putExtra("CategoryName", categoryName);
                    intent.putExtra("data_savequiz", response.body().getData());
                    if (isFromDashboardBanner) {
                        intent.putExtra("comingFrom", comingFrom);
                        intent.putExtra("rewardDate", rewardDate);

                    } else {
                        intent.putExtra("comingFrom", "quiz");
                        intent.putExtra("rewardText", "");

                    }


                    if (response.body().getQuizathonRewardData() != null) {
                        intent.putExtra("quizrewards", response.body().getQuizathonRewardData());
                    } else if (response.body().getSpinTheWheelRewardsModel() != null) {
                        intent.putExtra("spinrewards", response.body().getSpinTheWheelRewardsModel());
                    } else if (response.body().getRewards() != null && response.body().getRewards().getReward() != null) {
                        intent.putExtra("rewards", response.body().getRewards().getReward());
                    } else {
                        intent.putExtra("rewards", "");
                    }
                    if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null) {
                        intent.putExtra("rewardsBonus", response.body().getRewards().getBonusRewards());
                    } else {
                        intent.putExtra("rewardsBonus", "");
                    }

                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                        intent.putExtra("stamps", response.body().getEnGTokens().getTokens());
                    } else {
                        intent.putExtra("stamps", "");
                    }

                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                        intent.putExtra("stampsBonus", response.body().getEnGTokens().getBonusTokens());
                    } else {
                        intent.putExtra("stampsBonus", "");
                    }

                    startActivity(intent);
                    finish();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_save_answer_new_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaveQuizResponseModel> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_save_answer_new_failed));
                Toast.makeText(QuizActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void nextQuestion() {
        index++;
        int ans = -1;
        if (index <= (data.size() - 1)) {
            if (data.get(index).getUserAns() != null && !data.get(index).getUserAns().equals("")) {
                //ans = (int) data.get(index).getUserAns();
                binding.nextBtn.setVisibility(View.VISIBLE);
            } else {
                binding.nextBtn.setVisibility(View.GONE);
            }
            setPrevBtn();
            setQuestion(ans);
            setProgressBar(index);
        } else {
            setProgressBar(index);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (comingFrom.equalsIgnoreCase("bannerQuiz"))
                        saveQuestionData(true);
                    else
                        saveQuestionData(false);
                }
            }, 200);
        }

    }

    public void prevQuestion() {
        binding.nextBtn.setVisibility(View.VISIBLE);
        index--;
        if (index > 0) {
            getPrevQuestion();
        } else {
            getPrevQuestion();
            setPrevBtn();
        }
    }

    private void getPrevQuestion() {
        int ans = -1;
        if (data.get(index).getUserAns() != null) {
            try {
                ans = (int) data.get(index).getUserAns();
            } catch (Exception e) {

            }
        }
        setProgressBar(index);
        setQuestion(ans);
    }

    private void setPrevBtn() {
        if (index > 0) {
            binding.prevBtn.setVisibility(View.VISIBLE);
        } else {
            binding.prevBtn.setVisibility(View.INVISIBLE);
        }

        if (data != null && index == 0 && data.get(index).getUserAns() != null) {
            binding.nextBtn.setVisibility(View.VISIBLE);
        }

        if (index == 0 && data.get(index).getUserAns() == null) {
            binding.nextBtn.setVisibility(View.GONE);
        }
    }

    private void setProgressBar(int value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.progressIndicator.setProgress(value, true);
        } else {
            binding.progressIndicator.setProgress(value);
        }
        binding.progressIndicator.setMax(data.size());
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

    @Override
    public void onScratchStarted() {

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
        binding.scratchView.setScratchListener(QuizActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((QuizActivity) context).getWindow();

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
        binding.scratchView.setScratchListener(QuizActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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
                    instance.showPopUpFeedback(QuizActivity.this, NewDashboardHelper.Companion.getFeedbackResponseData());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        }
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
        binding.scratchView.setScratchListener(QuizActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((RewardsListActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusRewards.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }
}