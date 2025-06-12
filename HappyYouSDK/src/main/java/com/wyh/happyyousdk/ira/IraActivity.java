package com.wyh.happyyousdk.ira;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.FeedbackPOPUP;
import static com.wyh.happyyousdk.utils.Constants.IRA_INTEGRATION_ID;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_IN_PROGRESS;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_NEW;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;

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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityIraBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.ScratchCardPopUpBinding;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.request.ConversationIdReq;
import com.wyh.happyyousdk.model.request.ira.ConversationIdRequest;
import com.wyh.happyyousdk.model.request.ira.ConversationRequest;
import com.wyh.happyyousdk.model.request.ira.IRARequest;
import com.wyh.happyyousdk.model.request.ira.SaveIraRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse;
import com.wyh.happyyousdk.model.response.ira.IRAResponse;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.ConversationIdResponse;
import com.wyh.happyyousdk.model.response.hra.SaveAnswersResponse;
import com.wyh.happyyousdk.model.response.hraSection.newHRA.GetHRAAnswersResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IraActivity extends AppCompatActivity implements ScratchListener {
    ActivityIraBinding binding;
    Context context;
    List<IRAResponse> iraJsonList = new ArrayList<>();
    int queNo = 0;
    String selectedAns = null;
    List<IRAResponse> questionAnswer;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    String status;
    Intent intent;
    Handler handler;
    boolean isPressedOnPrevious = false;
    boolean isStamps = false, isPositiveBtn = false, isFromRetake;
    boolean isFirst = true;
    AssignRewardsResponse.SpinRewardsData spinRewardsData = null;
    QuizathonRewardData quizreward;


    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ira);
        context = this;
        SharedPref.init(context);

        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        handler = new Handler();

        binding.btnOk.setVisibility(View.INVISIBLE);

        isFromRetake = getIntent().getBooleanExtra("isFromRetake", false);

        intent = getIntent();
        /*if (intent.getStringExtra("from").equals(IRA_STATUS_COMPLETED)) {
            status = IRA_STATUS_COMPLETED;
            getIRAQuestions();
        } else if (intent.getStringExtra("from").equals(IRA_STATUS_NEW)) {
            status = IRA_STATUS_COMPLETED;
            getIRAQuestions();
//            getConversationId(new ArrayList<>());
        }*/

        getIRAQuestions();

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

        binding.btnYes.setOnClickListener(view -> {
            selectedYesView();
            startHandler();
//            setAnswers();
        });

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
//        refreshAuthToken("", new ArrayList<>());
        binding.btnNo.setOnClickListener(view -> {
            selectedNoView();
            startHandler();
        });
        binding.btnOk.setOnClickListener(view ->
                {
                    if (queNo < iraJsonList.size() && !iraJsonList.isEmpty()) {
                        handler.removeCallbacksAndMessages(null);

                        if (selectedAns != null) {
                            binding.tvBack.setText("Previous");
                            iraJsonList.get(queNo).setAnswer(Collections.singletonList(selectedAns));
                            questionAnswer = iraJsonList;
                            selectedAns = null;

                            queNo = queNo + 1;
                            if (queNo != iraJsonList.size()) {
                                binding.tvHraQue.setText(iraJsonList.get(queNo).getQuestion());

                                if (!iraJsonList.get(queNo).getAnswer().isEmpty() && iraJsonList.get(queNo).getAnswer().get(0).equalsIgnoreCase("No")) {
                                    selectedNoView();
//                                binding.tvHraQue.setText(iraJsonList.get(queNo).getQuestion());
                                    iraJsonList.get(queNo).setAnswer(Collections.singletonList(selectedAns));
                                } else if (!iraJsonList.get(queNo).getAnswer().isEmpty() && iraJsonList.get(queNo).getAnswer().get(0).equalsIgnoreCase("Yes")) {
                                    selectedYesView();
//                                binding.tvHraQue.setText(iraJsonList.get(queNo).getQuestion());
                                    iraJsonList.get(queNo).setAnswer(Collections.singletonList(selectedAns));
                                } else {
                                    notSelectedView();
                                }
                                binding.hraProgress.setProgress(queNo);
                            }
                            //Log.v("Url_Response_3", new Gson().toJson(questionAnswer));
//                                        Log.v("Url_Response_3", new Gson().toJson(iraJsonList.get(queNo).getAnswer()));
//                                        Log.v("Url_Response_3", new Gson().toJson(iraJsonList));

                        } else if (!iraJsonList.get(queNo).getAnswer().isEmpty() && selectedAns == null) {
                            binding.tvBack.setText("Previous");
                            queNo = queNo + 1;
                            if (queNo != iraJsonList.size()) {
                                binding.tvHraQue.setText(iraJsonList.get(queNo).getQuestion());
                                if (!iraJsonList.get(queNo).getAnswer().isEmpty() && iraJsonList.get(queNo).getAnswer().get(0).equalsIgnoreCase("No")) {
                                    selectedNoView();
//                                binding.tvHraQue.setText(iraJsonList.get(queNo).getQuestion());
                                    iraJsonList.get(queNo).setAnswer(Collections.singletonList(selectedAns));
                                } else if (!iraJsonList.get(queNo).getAnswer().isEmpty() && iraJsonList.get(queNo).getAnswer().get(0).equalsIgnoreCase("Yes")) {
                                    selectedYesView();
//                                binding.tvHraQue.setText(iraJsonList.get(queNo).getQuestion());
                                    iraJsonList.get(queNo).setAnswer(Collections.singletonList(selectedAns));
                                } else {
                                    notSelectedView();
                                }
                                binding.hraProgress.setProgress(queNo);
                            }
                            questionAnswer = iraJsonList;

                            binding.ivBack.setVisibility(View.VISIBLE);

                            selectedAns = null;
                        }
                    }
                    setTopCurveBg(queNo);

//                    Log.d("que", "" + queNo);
                    if (queNo == 1) {
                        status = IRA_STATUS_NEW;
                    } else if (queNo == iraJsonList.size()) {
                        status = IRA_STATUS_COMPLETED;
                    } else {
                        status = IRA_STATUS_IN_PROGRESS;
                    }

//                    Log.d("que", "" + status);
                    saveAnswer();

                    /*if (queNo == iraJsonList.size()) {
                        Log.v("Url_Response_4", queNo + "\n" + iraJsonList.size() + "\n" + new Gson().toJson(questionAnswer));

                    }*/
                }
        );
        binding.ivBack.setOnClickListener(view -> {
            if (/*queNo < iraJsonList.size() && */!iraJsonList.isEmpty()) {
                queNo = queNo - 1;
                if (queNo == -1) {
                    finish();
//                    Intent i = new Intent(context, IRAAnalysisActivity.class);
//                    startActivity(i);
                    return;
                }

                if (queNo != 0) {
                    binding.ivBack.setVisibility(View.VISIBLE);
                } else {
                    binding.tvBack.setText("Back");
                }

                binding.tvHraQue.setText(iraJsonList.get(queNo).getQuestion());
                binding.hraProgress.setProgress(queNo);

                if (iraJsonList.get(queNo).getAnswer().get(0).equalsIgnoreCase("No")) {
                    selectedNoView();
                } else if (iraJsonList.get(queNo).getAnswer().get(0).equalsIgnoreCase("Yes")) {
                    selectedYesView();
                } else {
                    notSelectedView();
                }
                iraJsonList.get(queNo).setAnswer(Collections.singletonList(selectedAns));
                questionAnswer = iraJsonList;


                //Log.v("Url_Response_3", new Gson().toJson(questionAnswer));
                setTopCurveBg(queNo);

                selectedAns = null;
            }
        });
    }

    private void saveAnswer() {
//        Log.d("save", "call");
        saveIRAAnswers(questionAnswer);
    }

    private void startHandler() {
        handler.postDelayed(() -> binding.btnOk.performClick(), 500);
    }

    private void getConversationId(List<IRAResponse> questions) {
        ProgressDialog prDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        prDialog.setMessage("Please wait ...");
        prDialog.setCancelable(false);
        prDialog.show();
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        ConversationIdRequest conversationIdRequest = new ConversationIdRequest(IRA_INTEGRATION_ID, "");
        Call<ConversationIdResponse> call = apiInterfaceWyh.getConversationId(SharedPref.getAuthToken(), conversationIdRequest);
        call.enqueue(new Callback<ConversationIdResponse>() {
            @Override
            public void onResponse(Call<ConversationIdResponse> call, Response<ConversationIdResponse> response) {
                if (prDialog.isShowing())
                    prDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_create_fresh_conversation_new_success));
                    SharedPref.putIRAConversationId(response.body().getConversationId());
                    if (intent.getStringExtra("from").equals(IRA_STATUS_COMPLETED)) {
                        saveIRAAnswers(questions);
                    } else if (intent.getStringExtra("from").equals(IRA_STATUS_NEW)) {
                        getIRAQuestions();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_create_fresh_conversation_new_failed));
                }
            }

            @Override
            public void onFailure(Call<ConversationIdResponse> call, Throwable t) {
                if (prDialog != null && prDialog.isShowing())
                    prDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_create_fresh_conversation_new_failed));
            }
        });
    }


    private void setTopCurveBg(int queNo) {
        switch (queNo) {
            case 1:
            case 10:
                binding.llQuestionBg.setBackground(context.getDrawable(R.drawable.ic_hra_orange_bg));
                break;
            case 2:
            case 5:
            case 9:
            case 11:
                binding.llQuestionBg.setBackground(context.getDrawable(R.drawable.ic_hra_blue_bg));
                break;
            case 3:
            case 6:
            case 8:
            case 0:
                binding.llQuestionBg.setBackground(context.getDrawable(R.drawable.ic_hra_pink_bg));
                break;
            case 4:
            case 7:
            case 12:
                binding.llQuestionBg.setBackground(context.getDrawable(R.drawable.ic_hra_orange_bg));
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void notSelectedView() {
        binding.btnYes.setBackground(context.getDrawable(R.drawable.light_gray_border_rc_bg_8dp));
        binding.btnNo.setBackground(context.getDrawable(R.drawable.light_gray_border_rc_bg_8dp));
        binding.ivBearIc.setVisibility(View.GONE);
        binding.ivNoAnswerBearIc.setVisibility(View.VISIBLE);
//        binding.ivBearIc.setImageDrawable(context.getDrawable(R.drawable.ic_ira_no_answer));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectedNoView() {
        binding.ivBearIc.setVisibility(View.VISIBLE);
        binding.btnNo.setBackground(context.getDrawable(R.drawable.light_red_border_rc_bg_8dp));
        binding.btnYes.setBackground(context.getDrawable(R.drawable.light_gray_border_rc_bg_8dp));
        binding.ivBearIc.setImageDrawable(context.getDrawable(R.drawable.ic_hra_bear_no));
        binding.ivNoAnswerBearIc.setVisibility(View.GONE);
        selectedAns = "No";
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectedYesView() {
        binding.ivBearIc.setVisibility(View.VISIBLE);
        binding.btnYes.setBackground(context.getDrawable(R.drawable.light_blue_border_rc_bg_8dp));
        binding.btnNo.setBackground(context.getDrawable(R.drawable.light_gray_border_rc_bg_8dp));
        binding.ivBearIc.setImageDrawable(context.getDrawable(R.drawable.ic_hra_bear_yes));
        binding.ivNoAnswerBearIc.setVisibility(View.GONE);
        selectedAns = "Yes";
    }

    private void getIRAQuestions() {
        ProgressDialog prDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        prDialog.setMessage("Please wait ...");
        prDialog.setCancelable(false);
        prDialog.show();
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        IRARequest iraRequest = new IRARequest(SharedPref.getUuid(), IRA_INTEGRATION_ID, "", SharedPref.getIRAConversationId());
        Call<GetHRAAnswersResponse> call = apiInterfaceWyh.getIRAQuestions(SharedPref.getAuthToken(), iraRequest);
        //Log.v("Url_Request", call.request().url() + "\n" + new Gson().toJson(iraRequest) + "\n" + SharedPref.getAuthToken());

        call.enqueue(new Callback<GetHRAAnswersResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<GetHRAAnswersResponse> call, Response<GetHRAAnswersResponse> response) {
                if (prDialog.isShowing())
                    prDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_ira_answers_success));
                    Type type = new TypeToken<List<IRAResponse>>() {
                    }.getType();
                    GetHRAAnswersResponse getHRAAnswersResponse = response.body();
                    SharedPref.putIRAConversationId(response.body().getConversationId());
                    //Log.v("Url_Response_1", new Gson().toJson(getHRAAnswersResponse));
                    iraJsonList.clear();
                    iraJsonList = new Gson().fromJson(getHRAAnswersResponse.getAnswerJson(), type);
                    if (iraJsonList != null) {
//                    Log.v("Url_Response_1", new Gson().toJson(iraJsonList));
//                    for (int i = 0; i < iraJsonList.size(); i++) {
//
                        if (response.body().getStatus().equalsIgnoreCase(IRA_STATUS_COMPLETED) || response.body().getStatus().equalsIgnoreCase(IRA_STATUS_NEW)) {
                            generateConversationID();
                            for (IRAResponse item : iraJsonList) {
                                if (item.getAnswer().size() > 0) {
                                    item.getAnswer().clear();
                                }
                            }
                        }

                        /*if (response.body().getStatus().equalsIgnoreCase(IRA_STATUS_NEW)) {
                            if (response.body().getConversationId().isEmpty() && SharedPref.getConversationID().isEmpty()) {
                                generateConversationID();
                            }
                        }*/

                        binding.tvHraQue.setText(iraJsonList.get(queNo).getQuestion());
                        binding.hraProgress.setMax(iraJsonList.size());
                        if (!iraJsonList.get(queNo).getAnswer().isEmpty()) {
                            if (iraJsonList.get(queNo).getAnswer().get(0).equalsIgnoreCase("No")) {
                                selectedNoView();
                            } else if (iraJsonList.get(queNo).getAnswer().get(0).equalsIgnoreCase("Yes")) {
                                selectedYesView();
                            } else {
                                notSelectedView();
                            }
                        } else {
                            notSelectedView();
                        }
                        setTopCurveBg(queNo);
                        if (selectedAns != null) {
                            iraJsonList.get(queNo).setAnswer(Collections.singletonList(selectedAns));
                        }
                        questionAnswer = iraJsonList;
                    }

                } else if (response.code() == 401) {
                    refreshAuthToken("saveIRAAnswers", new ArrayList<>());
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_ira_answers_failed));
                    Toast.makeText(IraActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetHRAAnswersResponse> call, Throwable t) {
                if (prDialog != null && prDialog.isShowing())
                    prDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_ira_answers_failed));
            }
        });
    }

    private void saveIRAAnswers(List<IRAResponse> questionAnswer) {
        ProgressDialog prDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        prDialog.setMessage("Please wait ...");
        prDialog.setCancelable(false);
        prDialog.show();
        Gson gson = new Gson();
        String ansJsonString = gson.toJson(questionAnswer);
        SaveIraRequest saveIRAAnswerReq = new SaveIraRequest(IRA_INTEGRATION_ID, SharedPref.getIRAConversationId(), "1.0.0.0", status, ansJsonString, "", "");
        Call<SaveAnswersResponse> call = apiInterfaceWyh.saveIRAAnswer(SharedPref.getAuthToken(), saveIRAAnswerReq);
        //Log.v("Url_Request_save", call.request().url() + "\n" + new Gson().toJson(saveIRAAnswerReq) + "\n" + SharedPref.getAuthToken());
//        Log.v("Url_Request_save", status);

        call.enqueue(new Callback<SaveAnswersResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<SaveAnswersResponse> call, Response<SaveAnswersResponse> response) {
                if (context != null && prDialog != null && prDialog.isShowing())
                    prDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.save_ira_answers_success));

                    if (Objects.equals(status, IRA_STATUS_COMPLETED)) {
                        if (response.body().getRewards() != null && response.body().getRewards().getReward() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, response.body().getRewards().getReward()));
                        }
                        if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, response.body().getRewards().getBonusRewards()));

                        }
                        if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));

                        }
                        if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));

                        }

                        if (response.body().getFeedbackDetails() != null && response.body().getFeedbackDetails().getFeedbackModel() != null && response.body().getFeedbackDetails().getStarConfig() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(FeedbackPOPUP, ""));
                            NewDashboardHelper.Companion.setFeedbackResponseData(response.body().getFeedbackDetails());
                        }

                        if (response.body().getSpinTheWheelRewardsModel() != null) {
                            spinRewardsData = response.body().getSpinTheWheelRewardsModel();
                        }
                        if (response.body().getQuizathonRewardData() != null) {
                            quizreward = response.body().getQuizathonRewardData();
                        }

                        getIRAHealthScore();
                    }
                    /*Intent i = new Intent(IraActivity.this, IRAAnalysisActivity.class);
                    startActivity(i);*/
                } else if (response.code() == 401) {
                    refreshAuthToken("saveIRAAnswers", questionAnswer);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.save_ira_answers_failed));
                    Toast.makeText(IraActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaveAnswersResponse> call, Throwable t) {
                if (prDialog != null && prDialog.isShowing())
                    prDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.save_ira_answers_failed));
            }
        });
    }

    private void getIRAHealthScore() {

        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        ConversationRequest iraRequest = new ConversationRequest(IRA_INTEGRATION_ID, "");
        Call<IRAHealthScoreResponse> call = apiInterfaceWyh.getIRAHealthScore(SharedPref.getAuthToken(), iraRequest);

        if (SharedPref.getIRAHealthData().isEmpty()) {
            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
        }
        call.enqueue(new Callback<IRAHealthScoreResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<IRAHealthScoreResponse> call, Response<IRAHealthScoreResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_ira_answers_score_success));

                    if (response.body().getIraHealthScoreData() != null &&
                            response.body().getIraHealthScoreData().getPlaySports() != null) {
                        Gson gson = new Gson();
                        String IRAHealthData = gson.toJson(response.body());
                        SharedPref.putIRAHealthData(IRAHealthData);
                        Intent intent = new Intent(context, IRAAnalysisActivity.class);
                        intent.putExtra("quizrewards", true);
                        if (spinRewardsData != null) {
                            intent.putExtra("spinrewards", spinRewardsData);
                        }
                        intent.putExtra("popups", new Gson().toJson(NewDashboardHelper.Companion.getPopUpShowModels()));
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_ira_answers_score_failed));
                }
            }

            @Override
            public void onFailure(Call<IRAHealthScoreResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_ira_answers_score_failed));
            }
        });
    }

    private void showRewardsPopup(String rewards) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        ScratchCardPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.scratch_card_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvPoints.setText(points);

        binding.btnNegative.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, IRAAnalysisActivity.class);
            startActivity(intent);
            finish();
        });
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, RewardsActivity.class);
            context.startActivity(intent);
            finish();
        });

        binding.scratchView.onFullReveal();
        binding.scratchView.setScratchListener(IraActivity.this);

        Rect displayRectangle = new Rect();
        Window window = ((IraActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
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

        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, IRAAnalysisActivity.class);
            startActivity(intent);
            finish();
        });

        binding.scratchView.onFullReveal();

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(IraActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    /*private void showBonusStampPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        Log.d("stamp:", rewards);
        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];

        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


        binding.btnPositive.setOnClickListener(view -> {
           isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(IraActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((QuizScoreActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusRewardsPopup(String rewards, Context context) {
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


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE)



        binding.btnPositive.setOnClickListener(view -> {
           isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(IraActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((DashboardActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
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
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        }
    }*/

    private void refreshAuthToken(String cameFrom, List<IRAResponse> questionAnswer) {
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
                    SharedPreference.init(context);
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    if (Objects.equals(cameFrom, "saveIRAAnswers")) {
                        saveIRAAnswers(questionAnswer);
                    } else if (Objects.equals(cameFrom, "getIRAQuestions")) {
                        getIRAQuestions();
                    }
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
                /*Toast.makeText(context, context.getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    context.startActivity(intent);
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i > 20) {
            scratchCardLayout.onFullReveal();
        }
    }

    @Override
    public void onScratchStarted() {

    }

    /*private void showStampsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        Log.d("stamps", rewards);

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
*//*
        if(isOrange){
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));
        }else{
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));
        }*//*
//        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


        *//*if (rewards.contains("First Login")) {
            binding.btnNegative.setVisibility(View.GONE);
            binding.btnPositive.setText("OK");
            binding.btnPositive.setOnClickListener(view -> {
                alertDialog.dismiss();
                Intent intent = new Intent(context, SyncDeviceActivity.class);
                startActivity(intent);
            });
        } else {

        }*//*

//        binding.scratchView.onFullReveal();
        binding.btnNegative.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialog.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((QuizScoreActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }*/

    private void generateConversationID() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        ConversationIdReq conversationIdReq = new ConversationIdReq(IRA_INTEGRATION_ID);
        Call<ConversationIdResponse> call = apiInterfaceWyh.createConversationI(SharedPref.getAuthToken(), conversationIdReq);
        call.enqueue(new Callback<ConversationIdResponse>() {
            @Override
            public void onResponse(Call<ConversationIdResponse> call, Response<ConversationIdResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ira_create_fresh_conversation_success));
                    SharedPref.putIRAConversationId(response.body().getConversationId());
                    Log.d("response genrate", response.body().getConversationId());
                    Log.d("response Share genrate", SharedPref.getConversationID());
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ira_create_fresh_conversation_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConversationIdResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ira_create_fresh_conversation_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });

    }
}