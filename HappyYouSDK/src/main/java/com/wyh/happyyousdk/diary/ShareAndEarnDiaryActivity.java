package com.wyh.happyyousdk.diary;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.databinding.ActivityShareAndEarnDiaryBinding;
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareAndEarnDiaryActivity extends AppCompatActivity {

    ActivityShareAndEarnDiaryBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;

    ArrayAdapter<String> levelAdapter, engAdapter, topUpAdapter;
    List<String> levelList, engList, topUpList;
    List<DiaryEventListResponse.Datum> levelListData, engListData, topUpListData;
    List<DiaryEventListResponse.Datum> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_and_earn_diary);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);



        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_404.json");


        setToolBar();
        getEventList();

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "ShareAndEarnDiary");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        




        /*ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, tribeNames);
        ArrayAdapter<String> engAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, tribeNames);
        ArrayAdapter<String> topUpAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, tribeNames);*/



    }

    private void setToolBar(){
        binding.includeToolbar.tvBack.setText("Back");
        binding.includeToolbar.llBack.setOnClickListener(v->{onBackPressed();});
    }

    public void setLevelAdapter(){
        levelAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.levelSpinner.setPrompt("Level");
        binding.levelSpinner.setAdapter(levelAdapter);

        binding.levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(!Objects.equals(levelAdapter.getItem(position), "Level")){
                    Intent intent = new Intent();
                    intent.putExtra("success", true);
                    intent.putExtra("event", new Gson().toJson(levelListData.get(position-1)));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setEngAdapterAdapter(){
        engAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.engSpinner.setPrompt("EnG");
        binding.engSpinner.setAdapter(engAdapter);

        binding.engSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(!Objects.equals(engAdapter.getItem(position), "EnG")){
                    Intent intent = new Intent();
                    intent.putExtra("success", true);
                    intent.putExtra("event", new Gson().toJson(engListData.get(position-1)));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setTopUpAdapter(){
        topUpAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.topUpSpinner.setPrompt("TOPUP");
        binding.topUpSpinner.setAdapter(topUpAdapter);

        binding.topUpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(!Objects.equals(topUpAdapter.getItem(position), "Topup")){
                    Intent intent = new Intent();
                    intent.putExtra("success", true);
                    intent.putExtra("event", new Gson().toJson(topUpListData.get(position-1)));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getEventList() {
        progressDialog.isShowing();
        Call<DiaryEventListResponse> call = apiInterfaceWyh.getDiaryEventList(SharedPref.getAuthToken());
        call.enqueue(new Callback<DiaryEventListResponse>() {
            @Override
            public void onResponse(Call<DiaryEventListResponse> call, Response<DiaryEventListResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.fetch_pending_diary_events_success));

                    data = response.body().getData();

                    if(data != null && data.size() > 0){
                        setData();
                        binding.llNoRecordFound.setVisibility(View.GONE);
                        binding.llSpinner.setVisibility(View.VISIBLE);
                    }else{
                        binding.llNoRecordFound.setVisibility(View.VISIBLE);
                        binding.llSpinner.setVisibility(View.GONE);
                    }

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.fetch_pending_diary_events_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DiaryEventListResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.fetch_pending_diary_events_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(){
        levelList = new ArrayList<>();
        engList = new ArrayList<>();
        topUpList = new ArrayList<>();
        levelListData = new ArrayList<>();
        engListData = new ArrayList<>();
        topUpListData = new ArrayList<>();
        levelList.add("Level");
        engList.add("EnG");
        topUpList.add("Topup");

        for(DiaryEventListResponse.Datum item : data){
            if(item.getEventCategory().toLowerCase().equals("topup")){
                topUpList.add(item.getEventName());
                topUpListData.add(item);
            }else if(item.getEventCategory().toLowerCase().equals("eng")){
                engList.add(item.getEventName());
                engListData.add(item);
            }else{
                levelList.add(item.getEventName());
                levelListData.add(item);
            }
        }

        if(levelList.size() > 1){
            binding.levelSpinner.setVisibility(View.VISIBLE);
            levelAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, levelList);
            setLevelAdapter();
        }else{
            binding.levelSpinner.setVisibility(View.GONE);
        }

        if(engList.size() > 1){
            binding.engSpinner.setVisibility(View.VISIBLE);
            engAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, engList);
            setEngAdapterAdapter();
        }else{
            binding.engSpinner.setVisibility(View.GONE);
        }

        if(topUpList.size() > 1){
            binding.topUpSpinner.setVisibility(View.VISIBLE);
            topUpAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, topUpList);
            setTopUpAdapter();
        }else{
            binding.topUpSpinner.setVisibility(View.GONE);
        }
    }
}