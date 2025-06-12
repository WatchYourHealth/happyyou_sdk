package com.wyh.happyyousdk.happyMarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.happyMarket.adapter.KLISubAdapter;
import com.wyh.happyyousdk.model.request.KLISubRequest;
import com.wyh.happyyousdk.model.request.KLISubResponse;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HappyMartKLIActivity extends AppCompatActivity {

    ArrayList<KLISubResponse.Data> kliList = new ArrayList<>();
    RecyclerView kli_recycler_list;
    KLISubAdapter kliSubAdapter;

    TextView tvBack;

    Integer checkID;

    ImageView ivHome;

    @SuppressLint({"ResourceAsColor", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy_mart_kliactivity);

        kli_recycler_list = findViewById(R.id.kli_recycler_list);
        tvBack = findViewById(R.id.tvBack);
        ivHome = findViewById(R.id.ivHome);
        Bundle extras = getIntent().getExtras();
        checkID = extras.getInt("CheckID");
        getKLIDetails(checkID);

        tvBack.setText(extras.getString("activityName"));
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HappyMartKLIActivity.this, NewDashboardActivity.class));
                finish();
            }
        });
    }

    public void getKLIDetails(int checkID){
        try{
            CommonUtils.showProgressDialige(this);
            KLISubRequest kliSubRequest = new KLISubRequest(checkID);
            APIInterface apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface.class);
            apiInterface.getKLIActivity(SharedPref.getAuthToken(),kliSubRequest).enqueue(new Callback<KLISubResponse>() {
                @Override
                public void onResponse(Call<KLISubResponse> call, Response<KLISubResponse> response) {
                    if(response.code() == 200 && response.body().getSuccess().equalsIgnoreCase("true")){
                        CommonUtils.dismissDialoge();
                        kliList = response.body().getData();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HappyMartKLIActivity.this);
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        GridLayoutManager layoutManager=new GridLayoutManager(HappyMartKLIActivity.this,2);
                        kli_recycler_list.setLayoutManager(layoutManager);
                        kliSubAdapter = new KLISubAdapter(HappyMartKLIActivity.this,kliList);
                        kli_recycler_list.setAdapter(kliSubAdapter);

                    }else{
                        CommonUtils.dismissDialoge();
                    }
                }

                @Override
                public void onFailure(Call<KLISubResponse> call, Throwable t) {

                    CommonUtils.dismissDialoge();
                }
            });

        }catch (Exception e){
            CommonUtils.dismissDialoge();
        }
    }
}