package com.wyh.happyyousdk.SpinWheel.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivitySpinWheelBinding;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.model.request.LockRewardsRequest;
import com.wyh.happyyousdk.model.response.GetQuadrantsResponse;
import com.wyh.happyyousdk.model.response.LockRewardResponse;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.MmpSDK;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.wheelview.LuckyWheel;
import com.wyh.happyyousdk.utils.wheelview.OnLuckyWheelReachTheTarget;
import com.wyh.happyyousdk.utils.wheelview.OnRotationListener;
import com.wyh.happyyousdk.utils.wheelview.WheelItem;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpinWheelActivity extends AppCompatActivity {

    List<WheelItem> wheelItems ;
    ActivitySpinWheelBinding binding;
    APIInterface apiInterface;
    String rewardType,rewardDesc,rewardName,headerOne,headerTwo;
    String description;

    int minutes;
    int rewardPosition;

    ImageView arrowImage;
    TextView textView;
    CountDownTimer rewardsTimer,timer;

    ArrayList<GetQuadrantsResponse.QuadrantData> getData = new ArrayList<>();
    Context context;
    List<WheelItem> updatedWheelItems = new ArrayList<>();

    Boolean isUserSkipped = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_spin_wheel);
        context = this;
        SharedPref.init(context);


        SharedPreference.init(context);
        apiInterface = RetrofitHandler.apiInterface();
        Log.d("AuthToken",new Gson().toJson(CommonUtils.wheelItems));
        binding.lwv.addWheelItems(CommonUtils.wheelItems);
        lockReward();


        if(getIntent().getStringExtra("description") != null){
            description = getIntent().getStringExtra("description");
            if(!description.equalsIgnoreCase("")){
                binding.spinDesc.setVisibility(View.VISIBLE);
                binding.spinDesc.setText(description);
            }else{
                binding.spinDesc.setVisibility(View.GONE);
            }
        }else{
            binding.spinDesc.setVisibility(View.GONE);

        }

        minutes = getIntent().getIntExtra("timer",0);
        arrowImage = binding.lwv.findViewById(R.id.iv_arrow);
        arrowImage.setVisibility(View.GONE);

        binding.clickToSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APILogs.INSTANCE.activityTracker("SPIN_THE_WHEEL",SpinWheelActivity.this);
                MmpSDK.INSTANCE.logMMPEvent("7HdzPITjkm","");
                binding.clickToSpin.setVisibility(View.GONE);
                binding.clickToDisabledSpin.setVisibility(View.VISIBLE);
                binding.lwv.rotateWheelTo(rewardPosition);
            }
        });

        binding.skipToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUserSkipped = true;
                APILogs.INSTANCE.activityTracker("SKIP_TO_LOGIN",SpinWheelActivity.this);
                MmpSDK.INSTANCE.logMMPEvent("C6biNllp5W","");
                SharedPref.setSpinWheelStatus(false);
                startActivity(new Intent(SpinWheelActivity.this, MobileNumberActivity.class));
                finish();
            }
        });

        binding.lwv.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
                if(!isUserSkipped){
                    timer = new CountDownTimer((long) minutes * 60 * 1000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            int minutes = (int) (millisUntilFinished / 1000) / 60;
                            int seconds = (int) (millisUntilFinished / 1000) % 60;
                            String timeLeft = String.format("%02d:%02d", minutes, seconds);
                            //binding.timerTv.setText(timeLeft);
                            SharedPref.setRewardTimer(timeLeft);
                            Log.d("AuthToken", "Time Left: " + timeLeft);
                        }

                        @Override
                        public void onFinish() {

                        }
                    }.start();

                    rewardsTimer = new CountDownTimer(2000,1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            SharedPref.setSpinWheelStatus(true);
                            timer.cancel();
                            startActivity(new Intent(SpinWheelActivity.this,SpinRewardLoginActivity.class)
                                    .putExtra("rewardName",rewardName)
                                    .putExtra("rewardDesc",rewardDesc)
                                    .putExtra("headerOne",headerOne)
                                    .putExtra("headerTwo",headerTwo));
                            finish();


                        }
                    }.start();
                }


            }
        });

        binding.lwv.setOnRotationListener(new OnRotationListener() {
            @Override
            public void onFinishRotation() {

            }
        });
    }


    private void lockReward(){
        try{
            CommonUtils.showProgressDialige(this);
            String uniqueId = UUID.randomUUID().toString();
            Log.d("AuthToken",uniqueId);
            LockRewardsRequest lockRewardsRequest = new LockRewardsRequest(uniqueId);
            apiInterface.lockRewards(lockRewardsRequest).enqueue(new Callback<LockRewardResponse>() {
                @Override
                public void onResponse(Call<LockRewardResponse> call, Response<LockRewardResponse> response) {
                    CommonUtils.dismissDialoge();
                    Log.d("AuthToken","Lock "+ new Gson().toJson(response.body()));
                    if(response.body() != null && response.code() == 200 && response.isSuccessful()){
                        SharedPref.putTempUUID(uniqueId);
                        if(response.body().getData().getRewardDescription() != null){
                            rewardDesc = response.body().getData().getRewardDescription();
                        }
                        if(response.body().getData().getQuadrantPosition() != null){
                            rewardPosition = response.body().getData().getQuadrantPosition();
                        }
                        if(response.body().getData().getRewardType() != null){
                            rewardType = response.body().getData().getRewardType();
                        }
                        if(response.body().getData().getRewardName() != null){
                            rewardName = response.body().getData().getRewardName();
                        }

                        if(response.body().getData().getRewardHeader1() != null){
                            headerOne = response.body().getData().getRewardHeader1();
                        }

                        if(response.body().getData().getRewardHeader2() != null){
                            headerTwo = response.body().getData().getRewardHeader2();
                        }

                    }
                }

                @Override
                public void onFailure(Call<LockRewardResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();

                }
            });

        }catch (Exception e){
            CommonUtils.dismissDialoge();
            e.printStackTrace();
        }
    }

}


