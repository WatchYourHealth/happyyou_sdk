package com.wyh.happyyousdk.quiz;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityQuizCategoryBinding;
import com.wyh.happyyousdk.model.request.GetQuestionRequest;
import com.wyh.happyyousdk.model.response.GetQuizQuestions;
import com.wyh.happyyousdk.model.response.GetQuizResponseModel;
import com.wyh.happyyousdk.model.response.QuestionModel;
import com.wyh.happyyousdk.model.response.quiz.TriviaHistoryData;
import com.wyh.happyyousdk.model.response.quiz.TriviaHistoryResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.quiz.adapter.QuizCategoryAdapter;
import com.wyh.happyyousdk.quizathon.QuizathonActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizCategoryActivity extends AppCompatActivity {

    ActivityQuizCategoryBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    QuizCategoryAdapter adapter;

    boolean isAssigned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz_category);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "QuizDashboard");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        setToolBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getQuestionData();
    }

    private void setToolBar() {
        binding.includeToolbar.tvBack.setText("Quiz");
        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));
        binding.includeToolbar.ivMenu.setVisibility(View.GONE);
        binding.includeToolbar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setAdapter(List<GetQuizQuestions> getQuizQuestions, int qId) {
        adapter = new QuizCategoryAdapter(context, getQuizQuestions, new QuizCategoryAdapter.OnItemClickListener() {
            @Override
            public void onClick(String name, List<QuestionModel> question) {
                if (name.equalsIgnoreCase("Trivia") && !isAssigned) {
                    getTriviaData();
                } else {
                    Intent intent = new Intent(context, QuizActivity.class);
                    intent.putExtra("data", new Gson().toJson(question));
                    intent.putExtra("CategoryName", name);
                    intent.putExtra("comingFrom", "");
                    intent.putExtra("qId", qId);
                    startActivity(intent);
                }
            }
        });
        binding.recyclerViewOption.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerViewOption.setAdapter(adapter);
    }

    private void getQuestionData() {
        progressDialog.show();
        GetQuestionRequest request = new GetQuestionRequest(getString(R.string.quiz_i_id),"");
        Call<GetQuizResponseModel> call = apiInterfaceWyh.getQuestionsV2(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<GetQuizResponseModel>() {
            @Override
            public void onResponse(Call<GetQuizResponseModel> call, Response<GetQuizResponseModel> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
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
                        int qId = response.body().getData().getQid();
                        setAdapter(getQuizQuestions, qId);
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
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_get_questions_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTriviaData() {
        Call<TriviaHistoryResponse> call = null;

        call = apiInterfaceWyh.GetTriviaHistory(SharedPref.getAuthToken());
        call.enqueue(new Callback<TriviaHistoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<TriviaHistoryResponse> call, @NonNull Response<TriviaHistoryResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
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
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(QuizCategoryActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

}