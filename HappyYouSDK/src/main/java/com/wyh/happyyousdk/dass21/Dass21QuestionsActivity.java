package com.wyh.happyyousdk.dass21;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.HRA_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.HRA_STATUS_IN_PROGRESS;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityDass21QuestionsBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.RewardsModel;
import com.wyh.happyyousdk.model.request.IntegrationIdRequest;
import com.wyh.happyyousdk.model.request.dass21.FetchDass21QuestionsRequest;
import com.wyh.happyyousdk.model.request.dass21.SaveDass21Request;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.dass21.Dass21AnswerJson;
import com.wyh.happyyousdk.model.response.dass21.Dass21Question;
import com.wyh.happyyousdk.model.response.dass21.Dass21QuestionsData;
import com.wyh.happyyousdk.model.response.dass21.Dass21Result;
import com.wyh.happyyousdk.model.response.dass21.DassAnalysisResponse;
import com.wyh.happyyousdk.model.response.dass21.FetchDass21QuestionsResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dass21QuestionsActivity extends AppCompatActivity {

    ActivityDass21QuestionsBinding binding;
    Context context;

    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    List<Dass21QuestionsData> dass21QuestionsDataList = new ArrayList<>();

    int answeredCount = 0, questionsPerSectionCount = 0, answeredCountForProgress = 0;
    String answer;
    Handler handler;
    boolean isPressedOnPrevious = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dass21_questions);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.btnOk.setVisibility(View.INVISIBLE);

        handler = new Handler();

        getDass21Questions();

        initViewTags();

        binding.tvNever.setOnClickListener(view -> {
           /* if (!(boolean) binding.tvNever.getTag())
                optionClicked(binding.tvNever);
            if ((boolean) binding.tvSometimes.getTag())
                optionClicked(binding.tvSometimes);
            if ((boolean) binding.tvOften.getTag())
                optionClicked(binding.tvOften);
            if ((boolean) binding.tvAlways.getTag())
                optionClickedRed(binding.tvAlways);*/
            binding.tvNever.setTag(true);
            binding.tvSometimes.setTag(false);
            binding.tvOften.setTag(false);
            binding.tvAlways.setTag(false);


            binding.tvNever.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
            binding.tvSometimes.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvOften.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvAlways.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            answer = binding.tvNever.getText().toString();
            binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bear_never));
            binding.ivTopBearAlways.setVisibility(View.GONE);
            binding.ivTopBear.setVisibility(View.VISIBLE);
            binding.ivTopBearSometimes.setVisibility(View.GONE);
            startHandler();
        });

        binding.ivHome.setOnClickListener(view -> {
            Intent i = new Intent(this, NewDashboardActivity.class);
            startActivity(i);
            finish();
        });

        binding.tvSometimes.setOnClickListener(view -> {
          /*if(!(boolean) binding.tvOften.getTag())
                optionClicked(binding.tvOften);
            if ((boolean) binding.tvNever.getTag())
                optionClicked(binding.tvNever);
            if ((boolean) binding.tvSometimes.getTag())
                optionClicked(binding.tvSometimes);
            if ((boolean) binding.tvAlways.getTag())
                optionClickedRed(binding.tvAlways);*/

            binding.tvNever.setTag(false);
            binding.tvSometimes.setTag(true);
            binding.tvOften.setTag(false);
            binding.tvAlways.setTag(false);
            binding.tvNever.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvSometimes.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
            binding.tvOften.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvAlways.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));

            answer = binding.tvSometimes.getText().toString();
            binding.ivTopBearAlways.setVisibility(View.GONE);
            binding.ivTopBear.setVisibility(View.GONE);
            binding.ivTopBearSometimes.setVisibility(View.VISIBLE);
            binding.ivTopBearSometimes.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bear_sometimes));
            startHandler();
        });

        binding.tvOften.setOnClickListener(view -> {
            /*if (!(boolean) binding.tvOften.getTag())
                optionClicked(binding.tvOften);
            if ((boolean) binding.tvNever.getTag())
                optionClicked(binding.tvNever);
            if ((boolean) binding.tvSometimes.getTag())
                optionClicked(binding.tvSometimes);
            if ((boolean) binding.tvAlways.getTag())
                optionClickedRed(binding.tvAlways);*/
            binding.tvNever.setTag(false);
            binding.tvSometimes.setTag(false);
            binding.tvOften.setTag(true);
            binding.tvAlways.setTag(false);
            binding.tvNever.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvSometimes.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvOften.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
            binding.tvAlways.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            answer = binding.tvOften.getText().toString();
            binding.ivTopBearAlways.setVisibility(View.GONE);
            binding.ivTopBear.setVisibility(View.VISIBLE);
            binding.ivTopBearSometimes.setVisibility(View.GONE);
            binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bear_often));
            startHandler();
        });

        binding.tvAlways.setOnClickListener(view -> {
           /* if (!(boolean) binding.tvAlways.getTag())
                optionClickedRed(binding.tvAlways);
            if ((boolean) binding.tvNever.getTag())
                optionClicked(binding.tvNever);
            if ((boolean) binding.tvOften.getTag())
                optionClicked(binding.tvOften);
            if ((boolean) binding.tvSometimes.getTag())
                optionClicked(binding.tvSometimes);*/
            binding.tvNever.setTag(false);
            binding.tvSometimes.setTag(false);
            binding.tvOften.setTag(false);
            binding.tvAlways.setTag(true);
            binding.tvNever.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvSometimes.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvOften.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvAlways.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
            answer = binding.tvAlways.getText().toString();
            binding.ivTopBearAlways.setVisibility(View.VISIBLE);
            binding.ivTopBear.setVisibility(View.GONE);
            binding.ivTopBearSometimes.setVisibility(View.GONE);
            binding.ivTopBearAlways.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bear_almost_always));
            startHandler();
        });


        binding.llBack.setOnClickListener(view -> {
            if (answeredCountForProgress > 0) {
                if (questionsPerSectionCount >= 0) {
                    if (answeredCount == 0 && questionsPerSectionCount > 0) {
                        answeredCount = 7;
                        questionsPerSectionCount--;
                    }
                    if (answeredCount != 0) {
                        answeredCount--;
                    }
                    answeredCountForProgress--;
                    setQuestion();
                    binding.progress.setProgress(answeredCountForProgress);
                    refillAnswers();
                    setAnswer();
                }
            } else {
                finish();
            }
            if (answeredCountForProgress == 0)
                binding.tvBack.setText("Back");
            else
                binding.tvBack.setText("Previous");
        });

        binding.btnOk.setOnClickListener(view -> {
            try {
                if ((boolean) binding.tvNever.getTag() || (boolean) binding.tvSometimes.getTag() || (boolean) binding.tvOften.getTag() || (boolean) binding.tvAlways.getTag()) {
                    int score;
                    if ((boolean) binding.tvNever.getTag())
                        score = 0;
                    else if ((boolean) binding.tvSometimes.getTag())
                        score = 1;
                    else if ((boolean) binding.tvOften.getTag())
                        score = 2;
                    else
                        score = 3;
                    dass21QuestionsDataList.get(questionsPerSectionCount).getQuestions().get(answeredCount).setScore(String.valueOf(score));
                    answeredCountForProgress++;
                    binding.progress.setProgress(answeredCountForProgress);
                    handler.removeCallbacksAndMessages(null);
                    answeredCount++;
                    if (answeredCount == 7) {
                        answeredCount = 0;
                        questionsPerSectionCount++;
                    }


                    if (answeredCountForProgress <= 21 && questionsPerSectionCount <= 2) {
                        setQuestion();
                        //resetOptions();
                        refillAnswers();
                        setAnswer();
                    }
                    createAnswerJSON();
                } else {
                    Toast.makeText(context, "Select any one option", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (answeredCountForProgress == 0)
                binding.tvBack.setText("Back");
            else
                binding.tvBack.setText("Previous");
        });


    }

    private void startHandler() {
        if (isPressedOnPrevious)
            isPressedOnPrevious = false;
        else
            handler.postDelayed(() -> binding.btnOk.performClick(), 500);
    }

    private void createAnswerJSON() {
        List<Dass21AnswerJson> dass21AnswerJsonList = new ArrayList<>();
        List<Dass21Result> dass21ResultList = new ArrayList<>();
        List<Dass21Result> dass21ResultList1 = new ArrayList<>();
        List<Dass21Result> dass21ResultList2 = new ArrayList<>();
        int userScore = 0;
        for (int i = 0; i < dass21QuestionsDataList.get(0).getQuestions().size(); i++) {
            Dass21Question dass21Question = dass21QuestionsDataList.get(0).getQuestions().get(i);
            if (dass21Question.getScore() != null) {
                dass21ResultList.add(new Dass21Result(dass21Question.getQuestion(), dass21Question.getScore()));
                userScore += Integer.parseInt(dass21Question.getScore());
            }
        }
        //Log.d("AuthToken ","Das Score "+userScore);

        //userScore += dass21ResultList.size();
        dass21AnswerJsonList.add(new Dass21AnswerJson(dass21QuestionsDataList.get(0).getTitle(), dass21ResultList, userScore,
                dass21ResultList.size(), dass21QuestionsDataList.get(0).getQuestions().size()));
        userScore = 0;
        for (int i = 0; i < dass21QuestionsDataList.get(1).getQuestions().size(); i++) {
            Dass21Question dass21Question = dass21QuestionsDataList.get(1).getQuestions().get(i);
            if (dass21Question.getScore() != null) {
                dass21ResultList1.add(new Dass21Result(dass21Question.getQuestion(), dass21Question.getScore()));
                userScore += Integer.parseInt(dass21Question.getScore());

            }
        }
        //Log.d("AuthToken ","Das Score "+userScore);

        dass21AnswerJsonList.add(new Dass21AnswerJson(dass21QuestionsDataList.get(1).getTitle(), dass21ResultList1, userScore,
                dass21ResultList1.size(), dass21QuestionsDataList.get(1).getQuestions().size()));
        userScore = 0;
        for (int i = 0; i < dass21QuestionsDataList.get(2).getQuestions().size(); i++) {
            Dass21Question dass21Question = dass21QuestionsDataList.get(2).getQuestions().get(i);
            if (dass21Question.getScore() != null) {
                dass21ResultList2.add(new Dass21Result(dass21Question.getQuestion(), dass21Question.getScore()));

                userScore += Integer.parseInt(dass21Question.getScore());

            }
        }
        //Log.d("AuthToken ","Das Score "+userScore);

        dass21AnswerJsonList.add(new Dass21AnswerJson(dass21QuestionsDataList.get(2).getTitle(), dass21ResultList2, userScore,
                dass21ResultList2.size(), dass21QuestionsDataList.get(2).getQuestions().size()));
        Log.d("AuthToken", "Final Answer " + new Gson().toJson(dass21AnswerJsonList));
        savDass21(new Gson().toJson(dass21AnswerJsonList));
    }

   /*private void resetOptions() {
        if (dass21QuestionsDataList.get(questionsPerSectionCount).getQuestions().get(answeredCount).getScore() == -1) {
            binding.ivTopBearAlways.setVisibility(View.GONE);
            binding.ivTopBear.setVisibility(View.VISIBLE);
            binding.ivTopBearSometimes.setVisibility(View.GONE);
            binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
            if ((boolean) binding.tvNever.getTag())
                optionClicked(binding.tvNever);
            if ((boolean) binding.tvSometimes.getTag())
                optionClicked(binding.tvSometimes);
            if ((boolean) binding.tvOften.getTag())
                optionClicked(binding.tvOften);
            if ((boolean) binding.tvAlways.getTag())
                optionClickedRed(binding.tvAlways);
        }
    }*/

    private void setAnswer() {
        try {
            if (dass21QuestionsDataList.get(questionsPerSectionCount).getQuestions().get(answeredCount).getScore() != null) {
                int score = Integer.parseInt(dass21QuestionsDataList.get(questionsPerSectionCount).getQuestions().get(answeredCount).getScore());
                if (score != -1) {
                    isPressedOnPrevious = true;
                    if (score == 0) {
                        binding.tvNever.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
                    } else {
                        binding.tvNever.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                    }
                    if (score == 1) {
                        binding.tvSometimes.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
                    } else {
                        binding.tvSometimes.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                    }
                    if (score == 2) {
                        binding.tvOften.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
                    } else {
                        binding.tvOften.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                    }
                    if (score == 3) {
                        binding.tvAlways.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
                    } else {
                        binding.tvAlways.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                    }
                    isPressedOnPrevious = false;
                } else {
                    binding.tvAlways.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                    binding.tvOften.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                    binding.tvSometimes.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                    binding.tvNever.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                }
            } else {
                binding.tvAlways.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                binding.tvOften.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                binding.tvSometimes.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                binding.tvNever.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refillAnswers() {
        if (dass21QuestionsDataList.get(questionsPerSectionCount).getQuestions().get(answeredCount).getScore() != null) {
            int score = Integer.parseInt(dass21QuestionsDataList.get(questionsPerSectionCount).getQuestions().get(answeredCount).getScore());
            if (score != -1) {
                isPressedOnPrevious = true;
                if (score == 0) {
                    binding.tvNever.performClick();
                } else if (score == 1) {
                    binding.tvSometimes.performClick();
                } else if (score == 2) {
                    binding.tvOften.performClick();
                } else {
                    binding.tvAlways.performClick();
                }
                isPressedOnPrevious = false;
            }
        }

    }

    private void initViewTags() {
        binding.tvNever.setTag(false);
        binding.tvSometimes.setTag(false);
        binding.tvOften.setTag(false);
        binding.tvAlways.setTag(false);
    }

    private void getDass21Questions() {
        try {
            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
            FetchDass21QuestionsRequest request = new FetchDass21QuestionsRequest(getResources().getString(R.string.dass21_i_id), "adult");
            Call<FetchDass21QuestionsResponse> call = apiInterfaceWyh.getDass21Questions(SharedPref.getAuthToken(), request);
            call.enqueue(new Callback<FetchDass21QuestionsResponse>() {
                @Override
                public void onResponse(Call<FetchDass21QuestionsResponse> call, Response<FetchDass21QuestionsResponse> response) {
                    try {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();

                        if (response.code() == 200 && response.body() != null) {
                            Analytics.logEvent(context, context.getClass().getName(), getString(R.string.das_question_success));
                            Type type = new TypeToken<List<Dass21QuestionsData>>() {
                            }.getType();
                            dass21QuestionsDataList = new Gson().fromJson(response.body().getQuestions(), type);
                            setQuestion();
                            setAnswer();
                        } else {
                            Analytics.logEvent(context, context.getClass().getName(), getString(R.string.das_question_failed));
                            Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        APILogs.INSTANCE.sendLogs(String.valueOf(call.request().url()), e.getMessage(), String.valueOf(response.code()), Dass21QuestionsActivity.this);
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<FetchDass21QuestionsResponse> call, Throwable t) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.das_question_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            APILogs.INSTANCE.sendLogs(Constants.EXCEPTION, e.getMessage(), "Exception", Dass21QuestionsActivity.this);
            e.printStackTrace();
        }

    }

    private void savDass21(String dass21AnswerJson) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        SaveDass21Request request = new SaveDass21Request(1, getResources().getString(R.string.dass21_i_id), "1.0.0.0", answeredCountForProgress >= 21 ? HRA_STATUS_COMPLETED : HRA_STATUS_IN_PROGRESS, dass21AnswerJson);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.saveDass21(SharedPref.getAuthToken(), request);
        Log.d("AuthToken", new Gson().toJson(request));
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                try {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.d("request", new Gson().toJson(call.request()));
                    Log.d("response", new Gson().toJson(response.body()));
                    Log.d("response", new Gson().toJson(response.code()));
                    if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.save_das_success));

                        if (answeredCountForProgress >= 21 && questionsPerSectionCount >= 2) {
                            getDassAnalysis(response.body().getRewards(), response.body().getSpinTheWheelRewardsModel(), response.body().getQuizathonRewardData());
                        } else {
                            setAnswer();
                            //initViewTags();
                        }

                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.save_das_failed));
                        Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    APILogs.INSTANCE.sendLogs(String.valueOf(call.request().url()), e.getMessage(), String.valueOf(response.code()), Dass21QuestionsActivity.this);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.save_das_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDassAnalysis(RewardsModel rewards, AssignRewardsResponse.SpinRewardsData spinRewardsData, QuizathonRewardData quizathonRewardData) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        IntegrationIdRequest request = new IntegrationIdRequest(getResources().getString(R.string.dass21_i_id));
        Call<DassAnalysisResponse> call = apiInterfaceWyh.getDassAnalysis(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<DassAnalysisResponse>() {
            @Override
            public void onResponse(Call<DassAnalysisResponse> call, Response<DassAnalysisResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getData() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_das_analysis_success));
                    Toast.makeText(Dass21QuestionsActivity.this, "Assessment completed", Toast.LENGTH_SHORT).show();
                    Log.d("Das res", new Gson().toJson(call.request().url()));
                    Log.d("Das res", new Gson().toJson(response.body().getRewards()));
                    SharedPref.putDASSAnalysis(new Gson().toJson(response.body()));
                    Intent intent = new Intent(context, Dass21AnalysisActivity.class);
                    intent.putExtra("Quizrewards", true);
                    if (spinRewardsData != null) {
                        intent.putExtra("spinrewards", spinRewardsData);
                    } else {
                        if (rewards != null && rewards.getReward() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, rewards.getReward()));

                        }
                        if (rewards != null && rewards.getBonusRewards() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, rewards.getBonusRewards()));

                        }
                    }

                    intent.putExtra("voucher", new Gson().toJson(response.body().getFreeVoucher()));
                    intent.putExtra("popups", new Gson().toJson(NewDashboardHelper.Companion.getPopUpShowModels()));
                    intent.putExtra("comingFrom", "dasQuestion");
                    startActivity(intent);
                    finish();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_das_analysis_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DassAnalysisResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_das_analysis_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setQuestion() {
        binding.tvQuestion.setText(checkAndFormatString(dass21QuestionsDataList.get(questionsPerSectionCount).getQuestions().get(answeredCount).getQuestion()));
    }


    //To avoid beer icon overlapping with the text
    public String checkAndFormatString(String inputString) {
        String result = "";
        if (inputString.length() > 62) {
            int splitIndex = 62;
            if (inputString.charAt(splitIndex) != ' ' && inputString.charAt(splitIndex + 1) != ' ') {
                while (splitIndex > 0 && inputString.charAt(splitIndex) != ' ') {
                    splitIndex--;
                }
            }
            for (int i = 0; i < inputString.length(); i++) {
                result += inputString.charAt(i);
                if (i == splitIndex) {
                    result += "\n";
                }
            }
        } else {
            result = inputString;
        }
        return result;
    }

    private void optionClicked(TextView textView) {
        textView.setTag(!(boolean) textView.getTag());
        if ((boolean) textView.getTag()) {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
//            textView.setTextColor(getResources().getColor(R.color.white));
        } else {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
//            textView.setTextColor(getResources().getColor(R.color.light_blue));
        }
    }

    private void optionClickedRed(TextView textView) {
        textView.setTag(!(boolean) textView.getTag());
        if ((boolean) textView.getTag()) {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
//            textView.setTextColor(getResources().getColor(R.color.white));
        } else {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
//            textView.setTextColor(getResources().getColor(R.color.light_pink));
        }
    }
}