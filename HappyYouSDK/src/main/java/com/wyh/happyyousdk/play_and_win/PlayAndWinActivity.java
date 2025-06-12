package com.wyh.happyyousdk.play_and_win;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.ActivityClaimReclaimList;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.Activities.spinwheelreward.SpinWheelRewardsActivity;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityPlayAndWinBinding;
import com.wyh.happyyousdk.fileshare.ActivityFileShareList;
import com.wyh.happyyousdk.model.BurnRegPointsReq;
import com.wyh.happyyousdk.model.BurnRegPointsResp;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.GetQuestionRequest;
import com.wyh.happyyousdk.model.request.SaveQuizRegistration;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.GetQuizQuestions;
import com.wyh.happyyousdk.model.response.GetQuizResponseModel;
import com.wyh.happyyousdk.model.response.playwin.QuizFeedbackModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonResponseModel;
import com.wyh.happyyousdk.model.response.playwin.SaveQuizRegistrationResponse;
import com.wyh.happyyousdk.model.response.quiz.TriviaHistoryData;
import com.wyh.happyyousdk.model.response.quiz.TriviaHistoryResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.play_and_win.adapter.GridPlaywinAdapter;
import com.wyh.happyyousdk.quiz.QuizActivity;
import com.wyh.happyyousdk.quiz.QuizHistoryActivity;
import com.wyh.happyyousdk.quiz.QuizScoreActivity;
import com.wyh.happyyousdk.quiz.adapter.QuizOptionAdapter;
import com.wyh.happyyousdk.quizathon.FeedbackActivityRewardList;
import com.wyh.happyyousdk.quizathon.QuizActivityRewardList;
import com.wyh.happyyousdk.quizathon.QuizClaimActivityRewardList;
import com.wyh.happyyousdk.quizathon.QuizScratchListActivity;
import com.wyh.happyyousdk.quizathon.QuizathonActivity;
import com.wyh.happyyousdk.quizathon.QuizathonScoreActivity;
import com.wyh.happyyousdk.quizathon.QuizathonTimeboundScoreActivity;
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity;
import com.wyh.happyyousdk.quizathon.TimeBoundScoreActivity;
import com.wyh.happyyousdk.quizathon.dialog.QuizMessageDialog;
import com.wyh.happyyousdk.quizathon.dialog.RetakeAlertDialog;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyh.happyyousdk.utils.dialog.RegistrationDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayAndWinActivity extends AppCompatActivity implements onRewardClick {
    ActivityPlayAndWinBinding binding;
    APIInterface apiInterface;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    boolean isAssigned = false;

    int qId = 0;

    List<QuizathonModel> quizathonModelList;
    List<QuizFeedbackModel> quizFeedbackModels;
    List<GetQuizQuestions> quizQuestionsList;
    PopupMenu popupMenu;
    PopupMenu popupMenuFeedback;

    QuizathonModel quizathonModelPointBurn;
    QuizathonModel quizathonModelRegister;
    String burnType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_and_win);
        apiInterface = RetrofitHandler.apiInterface();
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        SharedPref.init(context);
        binding.ivBackPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        popupMenu = new PopupMenu(PlayAndWinActivity.this, binding.tvQuizVewAll1);
        popupMenuFeedback = new PopupMenu(PlayAndWinActivity.this, binding.tvFeedVewAll);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.quiz_view_menu, popupMenu.getMenu());
        popupMenuFeedback.getMenuInflater().inflate(R.menu.feedback_view_menu, popupMenuFeedback.getMenu());


        binding.tvQuizVewAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APILogs.INSTANCE.activityTracker("A_PlayAndWinDashboard_Quizathon_3DotButton_Clicked", context);
                // Handling menu item click events
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getTitle().equals("More Quiz")) {
                        APILogs.INSTANCE.activityTracker("A_PlayAndWinDashboard_Quizathon_ViewAll", context);
                        Intent in = new Intent(PlayAndWinActivity.this, QuizathonViewAllActivity.class);
                        in.putExtra("type", "quizathon");
                        in.putExtra("name", "Quiz");
                        startActivity(in);
                    } else if (menuItem.getTitle().equals("Claim Rewards")) {
                        APILogs.INSTANCE.activityTracker("A_PlayAndWinDashboard_Quizathon_ViewActivityList", context);
                        //Intent in = new Intent(PlayAndWinActivity.this, QuizActivityRewardList.class);
                        Intent in = new Intent(PlayAndWinActivity.this, QuizClaimActivityRewardList.class);
                        startActivity(in);
                    } else if (menuItem.getTitle().equals("View Scratch List")) {
                        Intent in = new Intent(PlayAndWinActivity.this, QuizScratchListActivity.class);
                        startActivity(in);
                    } else if (menuItem.getTitle().equals("View File")) {
                        Intent in = new Intent(PlayAndWinActivity.this, ActivityFileShareList.class);
                        startActivity(in);
                    } else if (menuItem.getTitle().equals("Claim Reclaim")) {
                        Intent in = new Intent(PlayAndWinActivity.this, ActivityClaimReclaimList.class);
                        startActivity(in);
                    }
                    return true;
                });

                // Showing the popup menu
                popupMenu.show();
            }
        });

        binding.tvOthersVewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APILogs.INSTANCE.activityTracker("A_PlayAndWinDashboard_PlayNLearn_ViewAll_Clicked", context);
                Intent in = new Intent(PlayAndWinActivity.this, QuizathonViewAllActivity.class);
                in.putExtra("type", "quiz");
                in.putExtra("quiz_cat", "All");
                in.putExtra("name", "Play and Learn");
                startActivity(in);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
       // quizathonModelPointBurn=null;
        //quizathonModelRegister=null;
        setSpinWheelData();
        GetActiveQuizathon();
        getQuestionData();
        setOthersData();
    }

    public void setOthersData() {
        List<QuizathonModel> getQuizQuestionsList = new ArrayList<>();
        QuizathonModel quizathonModel = new QuizathonModel();
        quizathonModel.setQuizTitle("Files");
        quizathonModel.setQuizCategory("Shared Files");
        quizathonModel.setQuizDesc("Shared Files");
        quizathonModel.setQuizButtonText("View");
        getQuizQuestionsList.add(quizathonModel);


        GridPlaywinAdapter gridPlaywinAdapter = new GridPlaywinAdapter(null, getQuizQuestionsList, null, "Others", PlayAndWinActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlayAndWinActivity.this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvGridOthersNew.setLayoutManager(linearLayoutManager);
        binding.rvGridOthersNew.setAdapter(gridPlaywinAdapter);
        LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
        binding.rvGridOthersNew.setOnFlingListener(null);
        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvGridOthersNew);
    }

    public void setSpinWheelData() {
        List<QuizathonModel> getQuizQuestionsList = new ArrayList<>();
        QuizathonModel quizathonModel = new QuizathonModel();
        quizathonModel.setQuizTitle("Spin and Win");
        getQuizQuestionsList.add(quizathonModel);


        GridPlaywinAdapter gridPlaywinAdapter = new GridPlaywinAdapter(null, getQuizQuestionsList, null, "Spin", PlayAndWinActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlayAndWinActivity.this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvGridPlayWin.setLayoutManager(linearLayoutManager);
        binding.rvGridPlayWin.setAdapter(gridPlaywinAdapter);
        LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
        binding.rvGridPlayWin.setOnFlingListener(null);
        quickReadLinearSnapHelper.attachToRecyclerView(binding.rvGridPlayWin);
    }

    private void GetActiveQuizathon() {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);
            apiInterface.GetActiveQuizathon(SharedPref.getAuthToken()).enqueue(new Callback<QuizathonResponseModel>() {
                @Override
                public void onResponse(Call<QuizathonResponseModel> call, Response<QuizathonResponseModel> response) {
                    CommonUtils.dismissDialoge();
                    Log.d("Assign Response", new Gson().toJson(response.body()));
                    if (response.body() != null && response.code() == 200 && response.body().getQuizathonData() != null && response.body().getQuizathonData().getQuizathonModels() != null) {
                        quizathonModelList = response.body().getQuizathonData().getQuizathonModels();
                        if(response.body().getQuizathonData() != null && response.body().getQuizathonData().getQuizathonStreakModelList() != null && response.body().getQuizathonData().getQuizathonStreakModelList().size() > 0 ){
                            quizathonModelList.addAll(response.body().getQuizathonData().getQuizathonStreakModelList());
                        }
                        if (quizathonModelPointBurn!=null && Objects.equals(burnType, "")) {
                            callRegisterAfterPointBurn(quizathonModelPointBurn);
                        }
                        if (quizathonModelRegister!=null && Objects.equals(burnType, "")) {
                            callQuizAfterPointBurn(quizathonModelRegister);
                        }
                        if (quizathonModelList != null && quizathonModelList.size() > 0) {
                            binding.rvGridQuiz.setVisibility(View.VISIBLE);
                            binding.rvGridQuizIndicator.setVisibility(View.VISIBLE);
                            binding.rlQuizathon.setVisibility(View.VISIBLE);
                            int maxLength = response.body().getQuizathonData().getQuizathonModels().size();
                            if (response.body().getQuizathonData().getQuizathonModels().size() > 6) {
                                maxLength = 6;
                            }
                            MenuItem item = popupMenu.getMenu().findItem(R.id.viewAll); // Replace with actual ID
                            if (response.body().getQuizathonData().getQuizathonModels().size() < 7) {
                                item.setVisible(false); // Hides the item
                                // item.setEnabled(false); // If you want to disable instead of hide
                            }

                            GridPlaywinAdapter gridPlaywinAdapter = new GridPlaywinAdapter(null, response.body().getQuizathonData().getQuizathonModels().subList(0, maxLength), null, "Quizathon", PlayAndWinActivity.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlayAndWinActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.rvGridQuiz.setLayoutManager(linearLayoutManager);
                            binding.rvGridQuiz.setAdapter(gridPlaywinAdapter);
                            LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
                            binding.rvGridQuiz.setOnFlingListener(null);
                            quickReadLinearSnapHelper.attachToRecyclerView(binding.rvGridQuiz);

                            LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(PlayAndWinActivity.this);
                            absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
                            int indicatorSize = 1;

                            if (response.body().getQuizathonData().getQuizathonModels().size() > 2) {
                                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(maxLength)) / 2);
                            }
                            Menu menu = popupMenu.getMenu();
                            if (indicatorSize > 1) {
                                binding.rvGridQuizIndicator.setVisibility(View.VISIBLE);
                            } else {
                                binding.rvGridQuizIndicator.setVisibility(View.GONE);
                            }

//                            if (indicatorSize > 1) {
//                                binding.tvQuizVewAll1.setVisibility(View.VISIBLE);
//                                //menu.findItem(R.id.viewAll).setVisible(true);
//                            } else {
//                                binding.tvQuizVewAll1.setVisibility(View.GONE);
//                                //menu.findItem(R.id.viewAll).setVisible(false);
//                            }
                            IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(PlayAndWinActivity.this, indicatorSize, 0);
                            binding.rvGridQuizIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
                            binding.rvGridQuizIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
                            binding.rvGridQuizIndicator.setHasFixedSize(true);

                            binding.rvGridQuiz.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    int position = 0;
                                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                            position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                                        } else
                                            position = linearLayoutManager.findFirstVisibleItemPosition();
                                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(position);
                                    }
                                }
                            });
                        } else {
                            binding.rvGridQuiz.setVisibility(View.GONE);
                            binding.rvGridQuizIndicator.setVisibility(View.GONE);
                            binding.rlQuizathon.setVisibility(View.GONE);
                        }


                        quizFeedbackModels = response.body().getQuizathonData().getQuizFeedbackModels();
                        if (quizFeedbackModels != null && quizFeedbackModels.size() > 0) {
                            binding.rvGridFeed.setVisibility(View.VISIBLE);
                            binding.rvGridFeedIndicator.setVisibility(View.VISIBLE);
                            binding.rlFeedathon.setVisibility(View.VISIBLE);

                            int maxLength = response.body().getQuizathonData().getQuizFeedbackModels().size();
                            if (response.body().getQuizathonData().getQuizFeedbackModels().size() > 6) {
                                maxLength = 6;
                            }
                            GridPlaywinAdapter gridPlaywinAdapter = new GridPlaywinAdapter(response.body().getQuizathonData().getQuizFeedbackModels().subList(0, maxLength), null, null, "Feedback", PlayAndWinActivity.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlayAndWinActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.rvGridFeed.setLayoutManager(linearLayoutManager);
                            binding.rvGridFeed.setAdapter(gridPlaywinAdapter);
                            LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
                            binding.rvGridFeed.setOnFlingListener(null);
                            quickReadLinearSnapHelper.attachToRecyclerView(binding.rvGridFeed);

                            LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(PlayAndWinActivity.this);
                            absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
                            int indicatorSize = 1;

                            if (response.body().getQuizathonData().getQuizFeedbackModels().size() > 2) {
                                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(maxLength)) / 2);
                            }
                            Menu menu = popupMenuFeedback.getMenu();
                            if (indicatorSize > 1) {
                                binding.rvGridFeedIndicator.setVisibility(View.VISIBLE);
                            } else {
                                binding.rvGridFeedIndicator.setVisibility(View.GONE);
                            }

                            if (indicatorSize > 1) {
                                //  binding.tvFeedVewAll.setVisibility(View.VISIBLE);
                                menu.findItem(R.id.viewAll).setVisible(true);
                            } else {
                                //binding.tvFeedVewAll.setVisibility(View.GONE);
                            }


                            IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(PlayAndWinActivity.this, indicatorSize, 0);
                            binding.rvGridFeedIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
                            binding.rvGridFeedIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
                            binding.rvGridFeedIndicator.setHasFixedSize(true);

                            binding.rvGridFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    int position = 0;
                                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                            position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                                        } else
                                            position = linearLayoutManager.findFirstVisibleItemPosition();
                                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(position);
                                    }
                                }
                            });

                            binding.tvFeedVewAll.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    APILogs.INSTANCE.activityTracker("A_PlayAndWinDashboard_Feedback_ViewAll_Clicked", context);
                                    popupMenuFeedback.setOnMenuItemClickListener(menuItem -> {
                                        if (menuItem.getTitle().equals("Feedback")) {
                                            //APILogs.INSTANCE.activityTracker("A_PlayAndWinDashboard_Quizathon_ViewAll", context);
                                            Intent in = new Intent(PlayAndWinActivity.this, QuizathonViewAllActivity.class);
                                            in.putExtra("type", "feedback");
                                            in.putExtra("name", "Feedback");
                                            startActivity(in);
                                        } else if (menuItem.getTitle().equals("Feedback Activity")) {
                                            //APILogs.INSTANCE.activityTracker("A_PlayAndWinDashboard_Quizathon_ViewActivityList", context);
                                            Intent in = new Intent(PlayAndWinActivity.this, FeedbackActivityRewardList.class);
                                            startActivity(in);
                                        }
                                        return true;
                                    });

                                    // Showing the popup menu
                                    popupMenuFeedback.show();
                                }
                            });

                        } else {
                            binding.rvGridFeed.setVisibility(View.GONE);
                            binding.rvGridFeedIndicator.setVisibility(View.GONE);
                            binding.rlFeedathon.setVisibility(View.GONE);
                        }
                    } else {
                        binding.rvGridFeed.setVisibility(View.GONE);
                        binding.rvGridFeedIndicator.setVisibility(View.GONE);
                        binding.rlFeedathon.setVisibility(View.GONE);

                        binding.rvGridQuiz.setVisibility(View.GONE);
                        binding.rvGridQuizIndicator.setVisibility(View.GONE);
                        binding.rlQuizathon.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<QuizathonResponseModel> call, Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }

    private void getQuestionData() {
        GetQuestionRequest request = new GetQuestionRequest(getString(R.string.quiz_i_id),"All");
        Call<GetQuizResponseModel> call = apiInterfaceWyh.getQuestions(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<GetQuizResponseModel>() {
            @Override
            public void onResponse(Call<GetQuizResponseModel> call, Response<GetQuizResponseModel> response) {

                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_get_questions_success));

                    if (response.body().getData().getQuestions() != null) {
                        GetQuizResponseModel getQuizResponseModel = response.body();
//                        String str = "[{\"category\":\"nutrition\",\"categoryName\":\"Nutrition\",\"questions\":[{\"question\":\"Which among the following nutrients DO NOT provide energy to our body?\",\"options\":[\"Carbohydrates\",\"Protiens\",\"Vitamins\",\"Fats\"],\"answer\":\"Vitamins\",\"userAns\":null,\"image\":\"nutri.png\",\"explanation\":\"Vitamins do not supply energy but play an important role in regulation of metabolic activity and help in the utilisation of fats, carbohydrates and proteins.\"},{\"question\":\"Which among the following nutrients forms an important component of muscle?\",\"options\":[\"Protiens\",\"Carbohydrates\",\"Vitamins\",\"Minerals\"],\"answer\":\"Protiens\",\"userAns\":null,\"image\":\"nutri.png\",\"explanation\":\"Proteins help to build muscle mass and help in repairing the wear and tear of muscle fibres and other tissues in the body.\"},{\"question\":\"Which among the following nutrients provide maximum energy to the body?\",\"options\":[\"Carbohydrates\",\"Fats\",\"Protiens\",\"Minerals\"],\"answer\":\"Fats\",\"userAns\":null,\"image\":\"nutri.png\",\"explanation\":\"1 g of protein gives 4 kcal, 1 g of fat gives 9 kcal, 1 g of carbohydrates gives 4kcal. Minerals do not provide energy.\"},{\"question\":\"Which vitamin is required for bone growth and calcium metabolism?\",\"options\":[\"Vitamin A\",\"Vitamin B12\",\"Vitamin C\",\"Vitamin D\"],\"answer\":\"Vitamin D\",\"userAns\":null,\"image\":\"nutri.png\",\"explanation\":\"Vitamin D helps in absorption of dietary calcium from the intestine and its deposition in bone. Deficiency of Vitamin D can cause bone deformities like rickets and osteomalacia.\"},{\"question\":\"Which Vitamin deficiency can cause blindness in children?\",\"options\":[\"Vitamin A\",\"Vitamin B12\",\"Vitamin C\",\"Vitamin D\"],\"answer\":\"Vitamin A\",\"userAns\":null,\"image\":\"nutri.png\",\"explanation\":\"Sources of Vitamin A include vegetables like spinach, drumstick, tomatoes, yellow pumpkin and fruits like papaya and mangoes.\"},{\"question\":\"Vitamin B complex is composed of how many units of Vitamin B?\",\"options\":[\"4\",\"6\",\"8\",\"10\"],\"answer\":\"8\",\"userAns\":null,\"image\":\"nutri.png\",\"explanation\":\"Vitamin B complex is composed of 8 B-Vitamins which are: B1 (Thiamine), B2 (Riboflavin), B3 (Niacin), B5 (Pantothenic acid), B6 (Pyridoxine), B7 (Biotin), B9 (Folic acid) and B12 (Cobalamin)\"},{\"question\":\"What should be the daily intake of common salt in our diet?\",\"options\":[\"15g\",\"10g\",\"5g\",\"20g\"],\"answer\":\"5g\",\"userAns\":null,\"image\":\"nutri.png\",\"explanation\":\"Sodium is lost in urine and particularly in sweat. Sodium present in foods is not adequate to meet the body requirement. Hence salt has to be included in the diet.\"}]}]";
                        Log.d("json", getQuizResponseModel.getData().getQuestions());
                        isAssigned = getQuizResponseModel.getData().isAssigned();
                        ArrayList<GetQuizQuestions> getQuizQuestions = new Gson().fromJson(
                                getQuizResponseModel.getData().getQuestions(),
                                new TypeToken<List<GetQuizQuestions>>() {
                                }.getType());
                        Log.d("json", new Gson().toJson(getQuizQuestions));
                        qId = response.body().getData().getQid();
                        //setAdapter(getQuizQuestions, qId);
                        if (getQuizQuestions != null && getQuizQuestions.size() > 0) {

                            binding.rvGridOthers.setVisibility(View.VISIBLE);
                            binding.rvGridOthersIndicator.setVisibility(View.VISIBLE);
                            binding.rlOthers.setVisibility(View.VISIBLE);

                            int maxLength = getQuizQuestions.size();
                            if (getQuizQuestions.size() > 6) {
                                maxLength = 6;
                            }
                            GridPlaywinAdapter gridPlaywinAdapter = new GridPlaywinAdapter(null, null, getQuizQuestions.subList(0, maxLength), "Others", new onRewardClick() {
                                @Override
                                public void onRewardClick(Object item, String type) {
                                    if (item instanceof GetQuizQuestions) {
                                        GetQuizQuestions quizCategory = (GetQuizQuestions) item;
                                        if (quizCategory.getCategoryName().equalsIgnoreCase("Trivia") && !isAssigned) {
                                            getTriviaData();
                                        } else {
                                            Intent intent = new Intent(context, QuizActivity.class);
                                            intent.putExtra("data", new Gson().toJson(quizCategory.getQuestions()));
                                            intent.putExtra("CategoryName", quizCategory.getCategoryName());
                                            intent.putExtra("Category", quizCategory.getCategory());
                                            intent.putExtra("comingFrom", "");
                                            intent.putExtra("qId", qId);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlayAndWinActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.rvGridOthers.setLayoutManager(linearLayoutManager);
                            binding.rvGridOthers.setAdapter(gridPlaywinAdapter);
                            LinearSnapHelper quickReadLinearSnapHelper = new SnapHelperOneByOne();
                            binding.rvGridOthers.setOnFlingListener(null);
                            quickReadLinearSnapHelper.attachToRecyclerView(binding.rvGridOthers);

                            LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(PlayAndWinActivity.this);
                            absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
                            int indicatorSize = 1;

                            if (getQuizQuestions.size() > 3) {
                                indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(maxLength)) / 2);
                            }

                            if (indicatorSize > 1) {
                                binding.rvGridOthersIndicator.setVisibility(View.VISIBLE);
                            } else {
                                binding.rvGridOthersIndicator.setVisibility(View.GONE);
                            }
                            if (indicatorSize > 2) {
                                binding.tvOthersVewAll.setVisibility(View.VISIBLE);
                            } else {
                                binding.tvOthersVewAll.setVisibility(View.GONE);
                            }
                            IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(PlayAndWinActivity.this, indicatorSize, 0);
                            binding.rvGridOthersIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
                            binding.rvGridOthersIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
                            binding.rvGridOthersIndicator.setHasFixedSize(true);

                            binding.rvGridOthers.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    int position = 0;
                                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                                            position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                                        } else
                                            position = linearLayoutManager.findFirstVisibleItemPosition();
                                        absorbHealthTvIndicatorsAdapter.updateSelectedIndex(position);
                                    }
                                }
                            });
                        } else {
                            binding.rvGridOthers.setVisibility(View.GONE);
                            binding.rvGridOthersIndicator.setVisibility(View.GONE);
                            binding.rlOthers.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_get_questions_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetQuizResponseModel> call, Throwable t) {

                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_get_questions_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRewardClick(Object item, String type) {
        if (item instanceof GetQuizQuestions) {
            try {
                GetQuizQuestions quizCategory = (GetQuizQuestions) item;
                if (quizCategory.getCategoryName().equalsIgnoreCase("Trivia") && !isAssigned) {
                    getTriviaData();
                } else {
                    Intent intent = new Intent(context, QuizActivity.class);
                    intent.putExtra("data", new Gson().toJson(quizCategory.getQuestions()));
                    intent.putExtra("CategoryName", quizCategory.getCategoryName());
                    intent.putExtra("Category", quizCategory.getCategory());
                    intent.putExtra("comingFrom", "");
                    intent.putExtra("qId", qId);
                    startActivity(intent);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (item instanceof QuizathonModel) {
            try {
                QuizathonModel quizathonModel = (QuizathonModel) item;
                if (type.equalsIgnoreCase("Spin")) {
                    startActivity(new Intent(PlayAndWinActivity.this, SpinWheelRewardsActivity.class));
                } else if (type.equalsIgnoreCase("Others")) {
                    Intent in = new Intent(PlayAndWinActivity.this, ActivityFileShareList.class);
                    startActivity(in);
                } else {
                    if (quizathonModel.getAnswerJson() != null && !quizathonModel.getAnswerJson().isEmpty() && quizathonModel.isQuizCompleted()) {
                        Intent intent = new Intent(context, QuizathonScoreActivity.class);
                        if(quizathonModel.getQuizType() != null && quizathonModel.getQuizType().equalsIgnoreCase("Streak")){
                            intent = new Intent(PlayAndWinActivity.this, QuizathonTimeboundScoreActivity.class);
                        }
                        intent.putExtra("data", quizathonModel.getAnswerJson());
                        intent.putExtra("your_score", quizathonModel.getUserScore());
                        intent.putExtra("total_score", quizathonModel.getTotalScore());
                        intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                        intent.putExtra("Category", quizathonModel.getQuizDesc());
                        intent.putExtra("qid", quizathonModel.getQuizId());
                        intent.putExtra("comingFrom", "quiz");
                        intent.putExtra("rewardText", "");
                        intent.putExtra("withActivity", quizathonModel.getShowActivity());
                        intent.putExtra("quizdata", quizathonModel.getModel());
                        intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                        intent.putExtra("isRetakeAvailable", quizathonModel.getRetakeAvailable());
                        intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                        intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                        intent.putExtra("hasSufficientBalance", quizathonModel.isHasSufficientBalance());
                        intent.putExtra("retakePointstoBurn", quizathonModel.getRetakePointstoBurn());
                        intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                        intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());
                        intent.putExtra("quizModel", quizathonModel);
                        intent.putExtra("subCategory", quizathonModel.getQuizDesc());
                        context.startActivity(intent);
                    } else if (!quizathonModel.getIsUserRegistered() && quizathonModel.getRegistrationQuestions() != null && !quizathonModel.getRegistrationQuestions().isEmpty() && quizathonModel.getIsRegistrationRequired() && !quizathonModel.isRegistrationAllowed() && quizathonModel.getRegistrationPointBurn() >= 0) {
                        burnType="QuizRegistration";
                        quizathonModelPointBurn=quizathonModel;
                        showBurnQuizDialog(quizathonModel.getRegistrationPointBurn(),quizathonModel.getBurnRegTitle(),quizathonModel.getBurnRegIcon(),quizathonModel.getBurnRegMessage(), quizathonModel.getQuizId());
                    } else if (!quizathonModel.getIsUserRegistered() && quizathonModel.getRegistrationQuestions() != null && !quizathonModel.getRegistrationQuestions().isEmpty() && quizathonModel.getIsRegistrationRequired()) {
                        Intent intent = new Intent(context, QuizathonActivity.class);
                        intent.putExtra("data",
                                (quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted())
                                        ? quizathonModel.getAnswerJson()
                                        : quizathonModel.getQuestionJson());
                        intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                        intent.putExtra("Category", quizathonModel.getQuizDesc());
                        intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                        intent.putExtra("comingFrom", "");
                        intent.putExtra("IsRetake", quizathonModel.getRetake());
                        intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                        intent.putExtra("qId", quizathonModel.getQuizId());
                        intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                        intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());
                        intent.putExtra("type", type);
                        intent.putExtra("quizModel", quizathonModel);
                        RegistrationDialog dialog = new RegistrationDialog(PlayAndWinActivity.this, quizathonModel.getRegistrationQuestions(), quizathonModel.getRegTitle(), quizathonModel.getQuizId(), intent, quizathonModel.getQuizStarted(), quizathonModel.getQuizDesc());
                        if (quizathonModel.getRegIcon() != null && !quizathonModel.getRegIcon().isEmpty()) {
                            dialog.setIconUrl(quizathonModel.getRegIcon());
                        }
                        dialog.show();
                    } else if (!quizathonModel.getIsRegistrationRequired()) {
                        if (Integer.parseInt(quizathonModel.getQuizBurnPoints())>0)
                        {
                            burnType="QuizEntry";
                            quizathonModelRegister=quizathonModel;
                            showBurnQuizDialog(Integer.parseInt(quizathonModel.getQuizBurnPoints()), "Quiz", quizathonModel.getBurnRegIcon(), "You can participate in the quiz using "+quizathonModel.getQuizBurnPoints()+" HappyYou points", quizathonModel.getQuizId());
                        }
                        else {
                            Intent intent = new Intent(context, QuizathonActivity.class);
                            intent.putExtra("data",
                                    (quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted())
                                            ? quizathonModel.getAnswerJson()
                                            : quizathonModel.getQuestionJson());
                            intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                            intent.putExtra("Category", quizathonModel.getQuizDesc());
                            intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                            intent.putExtra("comingFrom", "");
                            intent.putExtra("IsRetake", quizathonModel.getRetake());
                            intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                            intent.putExtra("qId", quizathonModel.getQuizId());
                            intent.putExtra("type", type);
                            intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                            intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());
                            intent.putExtra("quizModel", quizathonModel);
                            context.startActivity(intent);
                        }

                    } else if (quizathonModel.getDialog_model() != null && quizathonModel.getDialog_model().getType() != null) {
                        QuizMessageDialog dialog = new QuizMessageDialog(PlayAndWinActivity.this, quizathonModel.getDialog_model(), apiInterfaceWyh);
                        dialog.show();
                    } else {
                        Intent intent = new Intent(context, QuizathonActivity.class);
                        intent.putExtra("data", quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted() ? quizathonModel.getAnswerJson() : quizathonModel.getQuestionJson());
                        intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                        intent.putExtra("Category", quizathonModel.getQuizDesc());
                        intent.putExtra("comingFrom", "");
                        intent.putExtra("IsRetake", quizathonModel.getRetake());
                        intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                        intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                        intent.putExtra("qId", quizathonModel.getQuizId());
                        intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                        intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());
                        intent.putExtra("type", type);
                        intent.putExtra("quizModel", quizathonModel);
                        startActivity(intent);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (item instanceof QuizFeedbackModel) {
            try {
                QuizFeedbackModel quizFeedbackModel = (QuizFeedbackModel) item;
                if (quizFeedbackModel.getFeedbackAnswerJson() != null && !quizFeedbackModel.getFeedbackAnswerJson().isEmpty() && quizFeedbackModel.getIsFeedbackTaken() && quizFeedbackModel.isShowActivity()) {

                    Intent intent = new Intent(PlayAndWinActivity.this, QuizathonScoreActivity.class);
                    intent.putExtra("data", quizFeedbackModel.getFeedbackAnswerJson());
                    intent.putExtra("your_score", 0);
                    intent.putExtra("total_score", 0);
                    intent.putExtra("CategoryName", quizFeedbackModel.getFeedbackDesc());
                    intent.putExtra("Category", quizFeedbackModel.getFeedbackDesc());
                    intent.putExtra("type", type);
                    intent.putExtra("rewardText", "");
                    intent.putExtra("quizdata", quizFeedbackModel.getModel());
                    intent.putExtra("withActivity", quizFeedbackModel.isShowActivity());
                    intent.putExtra("comingFrom", "Feedback");

                    startActivity(intent);


                } else {
                    Intent intent = new Intent(PlayAndWinActivity.this, QuizathonActivity.class);
                    intent.putExtra("data", quizFeedbackModel.getFeedbackQuestionJson());
                    intent.putExtra("CategoryName", quizFeedbackModel.getFeedbackDesc());
                    intent.putExtra("Category", quizFeedbackModel.getFeedbackDesc());
                    intent.putExtra("comingFrom", "Feedback");
                    intent.putExtra("IsRetake", false);
                    intent.putExtra("RetakeId", "0");
                    intent.putExtra("rewardDate", "");
                    intent.putExtra("qId", quizFeedbackModel.getFeedbackId());
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void getTriviaData() {
        Call<TriviaHistoryResponse> call = null;

        call = apiInterfaceWyh.GetTriviaHistory(SharedPref.getAuthToken());
        call.enqueue(new Callback<TriviaHistoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<TriviaHistoryResponse> call, @NonNull Response<TriviaHistoryResponse> response) {

                if (response.body() != null && response.code() == 200) {
                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        TriviaHistoryData triviaHistoryData = response.body().getData().get(0);
                        Intent intent = new Intent(context, QuizScoreActivity.class);
                        intent.putExtra("data", triviaHistoryData.getAnswerJson());
                        intent.putExtra("your_score", triviaHistoryData.getTriviaScore());
                        intent.putExtra("total_score", triviaHistoryData.getTriviaTotalScore());
                        intent.putExtra("CategoryName", triviaHistoryData.getCategory());

                        intent.putExtra("comingFrom", "quiz");
                        intent.putExtra("rewardText", "");

                        context.startActivity(intent);
                    } else {
                        startActivity(new Intent(context, QuizHistoryActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<TriviaHistoryResponse> call, Throwable t) {

                Toast.makeText(PlayAndWinActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SaveQuizRegistration(
            String RegistrationID,
            String AnswerJson, Intent inte, boolean isQuizStarted) {
        CommonUtils.dismissDialoge();
        CommonUtils.showProgressDialige(context);

        SaveQuizRegistration request = new SaveQuizRegistration(RegistrationID, AnswerJson);
        Call<SaveQuizRegistrationResponse> call = apiInterfaceWyh.SaveQuizRegistration(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<SaveQuizRegistrationResponse>() {
            @Override
            public void onResponse(Call<SaveQuizRegistrationResponse> call, Response<SaveQuizRegistrationResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    CommonUtils.dismissDialoge();
                    try {
                        if (isQuizStarted) {
                            startActivity(inte);
                        } else {
                            QuizMessageDialog dialog = new QuizMessageDialog(PlayAndWinActivity.this,
                                    response.body().getQuizRegistrationData().getDialogModel(), apiInterfaceWyh);
                            dialog.show();
                            GetActiveQuizathon();
                        }
                    } catch (Exception ex) {
                        CommonUtils.dismissDialoge();
                    }
                }
            }

            @Override
            public void onFailure(Call<SaveQuizRegistrationResponse> call, Throwable t) {
                CommonUtils.dismissDialoge();
                Toast.makeText(context, "Something went wrong! Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBurnQuizDialog(int pointsToBurn,String title,String icon,String message, String qid) {

        RetakeAlertDialog retakeAlertDialog = new RetakeAlertDialog(
                PlayAndWinActivity.this,
                pointsToBurn,
                icon,
                title,
                message,
                getRetakeDialogListener(qid, pointsToBurn) // Use extracted listener
        );

        retakeAlertDialog.show();
    }

    // Separate method to handle dialog button clicks
    private QuizOptionAdapter.OnItemClickListener getRetakeDialogListener(String qid, int pointsToBurn) {
        return (position, name) -> {
            if (name.equalsIgnoreCase("Close")) {
                burnType="";
                quizathonModelRegister=null;
                quizathonModelPointBurn=null;
                // Optional: Add logging or tracking here
            } else if (name.equalsIgnoreCase("Retake")) {
                //Toast.makeText(context, "Points burned. You can now register the Quiz", Toast.LENGTH_SHORT).show();
                BurnSpinData(Integer.parseInt(qid), pointsToBurn);
            }
        };
    }

    private void BurnSpinData(int quizId, int pointsToBurn) {
        BurnRegPointsReq request = new BurnRegPointsReq(quizId, pointsToBurn,burnType);
        Call<BurnRegPointsResp> call;
        if (burnType.equals("QuizEntry"))
        {
            call = apiInterface.BurnQuizPoints(SharedPref.getAuthToken(), request);
        }
        else{
            call = apiInterface.BurnQuizPoints(SharedPref.getAuthToken(), request);
        }

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BurnRegPointsResp> call, @NonNull Response<BurnRegPointsResp> response) {
                if (response.body() != null && response.code() == 200 ) {
                    if (response.body().getSuccess()) {
                        Toast.makeText(context, "Points burned. You can now register the Quiz", Toast.LENGTH_SHORT).show();
                        burnType = "";
                        GetActiveQuizathon();
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        quizathonModelPointBurn = null;
                        quizathonModelRegister = null;
                        burnType = "";
                    }

                } else {
                    //Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    quizathonModelPointBurn = null;
                    quizathonModelRegister = null;
                    burnType = "";
                }

            }

            @Override
            public void onFailure(@NonNull Call<BurnRegPointsResp> call, @NonNull Throwable t) {

            }
        });
    }
    public void callRegisterAfterPointBurn(QuizathonModel quizathonModel) {
        if (!quizathonModel.getIsUserRegistered() && quizathonModel.getRegistrationQuestions() != null && !quizathonModel.getRegistrationQuestions().isEmpty() && quizathonModel.getIsRegistrationRequired()) {
            Intent intent = new Intent(context, QuizathonActivity.class);
            intent.putExtra("data",
                    (quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted())
                            ? quizathonModel.getAnswerJson()
                            : quizathonModel.getQuestionJson());
            intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
            intent.putExtra("Category", quizathonModel.getQuizDesc());
            intent.putExtra("rewardDate", quizathonModel.getRewardDate());
            intent.putExtra("comingFrom", "");
            intent.putExtra("IsRetake", quizathonModel.getRetake());
            intent.putExtra("RetakeId", quizathonModel.getRetakeId());
            intent.putExtra("qId", quizathonModel.getQuizId());
            intent.putExtra("type", "Quizathon");
            intent.putExtra("quizModel", quizathonModel);
            RegistrationDialog dialog = new RegistrationDialog(PlayAndWinActivity.this, quizathonModel.getRegistrationQuestions(), quizathonModel.getRegTitle(), quizathonModel.getQuizId(), intent, quizathonModel.getQuizStarted(), quizathonModel.getQuizDesc());
            if (quizathonModel.getRegIcon() != null && !quizathonModel.getRegIcon().isEmpty()) {
                dialog.setIconUrl(quizathonModel.getRegIcon());
            }
            dialog.show();
            quizathonModelPointBurn=null;
            burnType="";
        }
    }
    public void callQuizAfterPointBurn(QuizathonModel quizathonModel) {
        Intent intent = new Intent(context, QuizathonActivity.class);
        intent.putExtra("data",
                (quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted())
                        ? quizathonModel.getAnswerJson()
                        : quizathonModel.getQuestionJson());
        intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
        intent.putExtra("Category", quizathonModel.getQuizDesc());
        intent.putExtra("rewardDate", quizathonModel.getRewardDate());
        intent.putExtra("comingFrom", "");
        intent.putExtra("IsRetake", quizathonModel.getRetake());
        intent.putExtra("RetakeId", quizathonModel.getRetakeId());
        intent.putExtra("qId", quizathonModel.getQuizId());
        intent.putExtra("type", "Quizathon");
        intent.putExtra("quizModel", quizathonModel);
        context.startActivity(intent);
        quizathonModelRegister=null;
        burnType="";
    }

}