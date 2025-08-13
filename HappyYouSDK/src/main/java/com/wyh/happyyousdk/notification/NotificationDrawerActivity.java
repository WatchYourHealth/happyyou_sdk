package com.wyh.happyyousdk.notification;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.MEDITATION;
import static com.wyh.happyyousdk.utils.Constants.SLEEP;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.WATER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityNotificationDrawerBinding;
import com.wyh.happyyousdk.model.response.notification.NotificationDrawerResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.notification.adapter.NotificationCardItemAdapter;
import com.wyh.happyyousdk.notification.adapter.NotificationDrawerAdapter;
import com.wyh.happyyousdk.model.response.notification.NotificationDataResponse;
import com.wyh.happyyousdk.model.response.notification.NotificationResponse;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationDrawerActivity extends AppCompatActivity {

    ActivityNotificationDrawerBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    NotificationDrawerAdapter adapter;
    int commuityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_drawer);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        binding.notificationListRecyclerView.setVisibility(View.GONE);
        binding.llNoRecordFound.setVisibility(View.GONE);

        binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_404.json");

        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));

        setToolBar();

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "NotificationDrawer");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        
        getNotificationData();

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setToolBar() {
        binding.includeToolbar.tvBack.setText("Notifications");
        binding.includeToolbar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setAdapter(List<NotificationDataResponse> list) {
        adapter = new NotificationDrawerAdapter(context, list,listener);
        binding.notificationListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.notificationListRecyclerView.setAdapter(adapter);
    }


    private void getNotificationData() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<NotificationResponse> call = apiInterfaceWyh.getNotificationData(SharedPref.getAuthToken());
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotificationResponse> call, @NonNull Response<NotificationResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.notification_drawer_success));

                    Log.d("res:", new Gson().toJson(response.body()));
                    if (response.body().getData().size() > 0) {
                        binding.notificationListRecyclerView.setVisibility(View.VISIBLE);
                        binding.llNoRecordFound.setVisibility(View.GONE);
                        setAdapter(response.body().getData());
                    } else {
                        binding.notificationListRecyclerView.setVisibility(View.GONE);
                        binding.llNoRecordFound.setVisibility(View.VISIBLE);
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.notification_drawer_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.notification_drawer_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }



    NotificationCardItemAdapter.OnItemClickListener listener = new NotificationCardItemAdapter.OnItemClickListener() {
        @Override
        public void onClick( NotificationDrawerResponse data) {
            commuityId = Integer.parseInt(data.getCommunityId());
            processNotificationData(data.getTitle(),"");
        }
    };
    private void processNotificationData(String reminderName, String id) {
        String s = reminderName.toLowerCase();
        if (s.contains("water")) {
            Intent intent = new Intent(getApplicationContext(), TrendsActivity.class);
            intent.putExtra("activityType", WATER);
            startActivity(intent);
        } else if (s.contains("meditation")) {
            Intent intent = new Intent(getApplicationContext(), TrendsActivity.class);
            intent.putExtra("activityType", MEDITATION);
            startActivity(intent);
        } else if (s.contains("sleep")) {
            Intent intent = new Intent(getApplicationContext(), TrendsActivity.class);
            intent.putExtra("activityType", SLEEP);
            startActivity(intent);
        } else if (s.contains("steps")) {
            Intent intent = new Intent(getApplicationContext(), TrendsActivity.class);
            intent.putExtra("activityType", STEPS);
            startActivity(intent);
        } else if ("reward".equals(s)) {
            Intent intent = new Intent(getApplicationContext(), RewardsActivity.class);
            startActivity(intent);
        }
    }



}