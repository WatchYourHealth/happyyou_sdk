package com.wyh.happyyousdk.quiz;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.model.response.BannerQuizModel;
import com.wyh.happyyousdk.dashboard.model.SaveBannerQuizRequestModel;
import com.wyh.happyyousdk.databinding.ActivityQuizHistoryBinding;
import com.wyh.happyyousdk.model.request.SaveQuizRequest;
import com.wyh.happyyousdk.model.response.SaveQuizResponseModel;
import com.wyh.happyyousdk.model.response.quiz.TriviaHistoryResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.quiz.adapter.QuizTriviaHistoryAdapter;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizHistoryActivity extends AppCompatActivity {
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    ProgressDialog progressDialog;
    QuizTriviaHistoryAdapter adapter;
    ActivityQuizHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        context = this;
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        getTriviaData();

        binding.includeToolbar.tvBack.setText("History");
        binding.includeToolbar.ivBack.setOnClickListener(v -> finish());
        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));
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
                    adapter = new QuizTriviaHistoryAdapter(response.body().getData());
                    binding.rvTriviaHistory.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<TriviaHistoryResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(QuizHistoryActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
}