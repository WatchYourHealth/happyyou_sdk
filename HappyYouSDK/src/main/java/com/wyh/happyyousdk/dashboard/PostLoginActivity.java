package com.wyh.happyyousdk.dashboard;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.MEDITATION;
import static com.wyh.happyyousdk.utils.Constants.TAG_CURRENT_LEVEL;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;
import static com.wyh.happyyousdk.utils.Constants.WATER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.WebActivity;
import com.wyh.happyyousdk.absorb.HealthHacksActivity;
import com.wyh.happyyousdk.absorb.QuickReadDashboard;
import com.wyh.happyyousdk.contacts.ContactsActivityNew;
import com.wyh.happyyousdk.dashboard.adapter.PostLoginActivitiesAdapter;
import com.wyh.happyyousdk.databinding.ActivityPostLoginBinding;
import com.wyh.happyyousdk.databinding.CustomPopupRewardsBinding;
import com.wyh.happyyousdk.ehr.EhrActivity;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.ira.IRAAnalysisActivity;
import com.wyh.happyyousdk.ira.IraActivity;
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.rewards.GetRewardsDashboardRequest;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.quiz.QuizCategoryActivity;
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.model.request.rewards.StartRewardsActivityRequest;
import com.wyh.happyyousdk.model.response.rewards.LevelActivity;
import com.wyh.happyyousdk.model.response.rewards.LevelDashboardResponse;
import com.wyh.happyyousdk.model.response.rewards.RewardsLevel;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLoginActivity extends AppCompatActivity implements PostLoginActivitiesAdapter.ClickListenerInterface{

    ActivityPostLoginBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;

    AlertDialog alertDialog;
    RewardsLevel currentLevelActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_login);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.llSkip.setOnClickListener(v -> {
            finish();
        });

        binding.rlMyTribe.setOnClickListener(v -> {

        });

        getRewardsDashboardData();
    }

    public void getRewardsDashboardData() {
        GetRewardsDashboardRequest request = new GetRewardsDashboardRequest(TAG_CURRENT_LEVEL);
        Call<LevelDashboardResponse> call = apiInterfaceWyh.getRewardsDashboardData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<LevelDashboardResponse>() {
            @Override
            public void onResponse(Call<LevelDashboardResponse> call, Response<LevelDashboardResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_success));

                    if (response.body().getData() != null) {
                        if (response.body().getData().getCurrentLevel() != null)
                            currentLevelActivity = response.body().getData().getCurrentLevel();
                        setActivitiesAdapter(currentLevelActivity.getActivities());
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

    private void setActivitiesAdapter(List<LevelActivity> activities) {
        PostLoginActivitiesAdapter allPendingActivitiesAdapter = new PostLoginActivitiesAdapter(context, activities, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvActivities.setAdapter(allPendingActivitiesAdapter);
        binding.rvActivities.setLayoutManager(gridLayoutManager);
        binding.rvActivities.setItemViewCacheSize(30);
    }

    @Override
    public void onItemClickActivities(LevelActivity levelActivity, int bgDrawable) {
        showCustomPopUp(levelActivity, bgDrawable);
    }

    private void showCustomPopUp(LevelActivity levelActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        CustomPopupRewardsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_rewards, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        Glide.with(context)
                .load(levelActivity.getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivLogo);
        binding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        binding.tvTitle.setText(levelActivity.getActivityName());
        binding.tvDescription.setText(levelActivity.getActivityDesc());
        if (levelActivity.isStarted() || levelActivity.isIsCompleted()) {
            binding.btnPositive.setVisibility(View.GONE);
        }

        if (levelActivity.getWhatTo() != null) {
            binding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            binding.tvWhatToDo.setText(levelActivity.getWhatTo());
            binding.tvHowToDo.setText(levelActivity.getHowTo());
            binding.tvWhyToDo.setText(levelActivity.getWhyTo());
        }

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        binding.btnPositive.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            startNewActivity(levelActivity);
        });

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void startNewActivity(LevelActivity levelActivity) {
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
                                case "waterintake":
                                    Intent intent2 = new Intent(context, TrendsActivity.class);
                                    intent2.putExtra("activityType", WATER);
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
                                    Intent intent5 = new Intent(context, HealthHacksActivity.class);
                                    startActivity(intent5);
                                    break;
                                case "ehr":
                                    Intent intent6 = new Intent(context, EhrActivity.class);
                                    startActivity(intent6);
                                    break;
                                case "invite":
                                    intent6 = new Intent(context, ContactsActivityNew.class);
                                    intent6.putExtra("comingFrom","");
                                    startActivity(intent6);
                                    break;
                                case "syncdevice":
                                    /*Intent intent7 = new Intent(context, SyncDeviceActivity.class);
                                    startActivity(intent7);
                                    break;*/
                                case "calorieintake":
                                    openWebView(CommonUtils.getBaseUrlForAddFood(context));
                                    break;
                            }
                        }else {
                            Intent intent = new Intent(context, RewardsActivity.class);
                            startActivity(intent);
                        }
                        finish();
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
        i.putExtra("Url", url);
        i.putExtra("comingFrom","postLogin");
        context.startActivity(i);
    }
}