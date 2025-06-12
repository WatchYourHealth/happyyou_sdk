package com.wyh.happyyousdk.SpinWheel.Activities;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.isValidMobNo;
import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivitySpinRewardLoginBinding;
import com.wyh.happyyousdk.model.request.encrDecr.EncryptionRequest;
import com.wyh.happyyousdk.model.request.login.GetOtpRequest;
import com.wyh.happyyousdk.model.response.encrDecr.EncryptionResponse;
import com.wyh.happyyousdk.model.response.login.GetOtpResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.MmpSDK;
import com.wyh.happyyousdk.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  SpinRewardLoginActivity extends AppCompatActivity {

    ActivitySpinRewardLoginBinding binding;
    ApiInterfaceWyh apiInterfaceWyh;
    String referrerUrl;
    Context context;
    int minutes, seconds;
    String[] totalTime;
    public static CountDownTimer countDownTimer;
    int rewardSeconds = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_spin_reward_login);
        init();
    }

    private void init() {
        try {
            context = this;
            SharedPref.init(context);

            apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

            JSONObject customObj = new JSONObject();
            if (getIntent().getStringExtra("rewardName") != null) {
                binding.rewardName.setText(getIntent().getStringExtra("rewardName"));
            }
            if (getIntent().getStringExtra("rewardDesc") != null) {
                binding.rewardDesc.setText(getIntent().getStringExtra("rewardDesc"));
            }
            if(getIntent().getStringExtra("headerOne") != null){
                binding.headerOne.setText(getIntent().getStringExtra("headerOne"));
            }
            if(getIntent().getStringExtra("headerTwo") != null){
                binding.headerTwo.setText(getIntent().getStringExtra("headerTwo"));
            }

            totalTime = SharedPref.getRewardTiwer().split(":");
            if (totalTime.length != 0) {
                minutes = Integer.parseInt(totalTime[0]);
                seconds = Integer.parseInt(totalTime[1]);
            }

            startTimer(minutes, seconds);

            try {
                customObj.put("PAGE_ID", "EnterMobileNumber");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            

            if (SharedPref.getIsLoggedIn()) {
                Intent intent = new Intent(context, NewDashboardActivity.class);
                startActivity(intent);
                finish();
            }

            binding.edtMobileNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().trim().length() == 10) {
                        CommonUtils.hideKeyboard(SpinRewardLoginActivity.this);
                    }
                }
            });

            binding.edtMobileNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.edtMobileNumber.setError(null);

                }
            });


            binding.continueLogin.setOnClickListener(view ->
            {
                if (binding.edtMobileNumber.getText().toString().equals("") ||
                        binding.edtMobileNumber.getText().toString().length() < 10 || !TextUtils.isDigitsOnly(binding.edtMobileNumber.getText())
                        || !isValidMobNo(binding.edtMobileNumber.getText().toString())) {
                    binding.edtMobileNumber.requestFocus();
                    binding.edtMobileNumber.setError("Please enter valid mobile number");
                    //showKeyboard(SpinRewardLoginActivity.this);
                } else {
                    APILogs.INSTANCE.activityTracker("SPIN_LOGIN", SpinRewardLoginActivity.this);
                    encryptRSA(binding.edtMobileNumber.getText().toString());
                }
            });

            Glide.with(this).load(R.raw.reward_celebration).into(binding.ivCelebration);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    public void encryptRSA(String valueToEncrypt) {
        try {
            CommonUtils.showProgressDialige(this);
            EncryptionRequest encryptionRequest = new EncryptionRequest(new String[]{valueToEncrypt});
            Call<EncryptionResponse> call = apiInterfaceWyh.encryptRSA(encryptionRequest);
            call.enqueue(new Callback<EncryptionResponse>() {
                @Override
                public void onResponse(Call<EncryptionResponse> call, Response<EncryptionResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.code() == 200 && response.body() != null && response.body().getData() != null &&
                            response.body().getData().get(0).getEncryptedStr() != null) {
                        MmpSDK.INSTANCE.logMMPEvent("kQvGzr4JhF","");
                        sendOTP(response.body().getData().get(0).getEncryptedStr(), valueToEncrypt);
                    } else {
                        CommonUtils.dismissDialoge();
                        Toast.makeText(SpinRewardLoginActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EncryptionResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    Toast.makeText(SpinRewardLoginActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            CommonUtils.dismissDialoge();
            e.printStackTrace();
        }

    }

    public void sendOTP(String encryptedMobileNo, String decryptMobileNo) {
        try {
            CommonUtils.showProgressDialige(this);
            GetOtpRequest getOtpRequest = new GetOtpRequest(encryptedMobileNo);
            Call<GetOtpResponse> call = apiInterfaceWyh.getOTP(getOtpRequest);
            Log.d("login ", new Gson().toJson(getOtpRequest));
            Log.d("mobile number request: ", new Gson().toJson(getOtpRequest));
            Log.d("mobile number request: ", new Gson().toJson(call.request().url()));
            call.enqueue(new Callback<GetOtpResponse>() {
                @Override
                public void onResponse(Call<GetOtpResponse> call, Response<GetOtpResponse> response) {
                    CommonUtils.dismissDialoge();
                    Log.d("mobile number res code: ", new Gson().toJson(response.code()));
                    Log.d("mobile number res body: ", new Gson().toJson(response.body()));
                    if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                        //countDownTimer.cancel();
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.send_otp_success));
                        Toast.makeText(context, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, SpinOtpActivity.class);
                        intent.putExtra("isAlreadyRegistered", response.body().getIsAlreadyRegistered());
                        intent.putExtra("mobileNo", encryptedMobileNo);
                        intent.putExtra("decryptMobileNo", decryptMobileNo);
                        intent.putExtra("rewardName", binding.rewardName.getText().toString());
                        intent.putExtra("rewardDesc", binding.rewardDesc.getText().toString());
                        intent.putExtra("headerOne",binding.headerOne.getText().toString());
                        intent.putExtra("headerTwo",binding.headerTwo.getText().toString());
                        startActivity(intent);
//                    finish();
                    } else {
                        Analytics.logEvent(context, "", "A_114_" + response.code() + "_" + encryptedMobileNo);
                        Toast.makeText(SpinRewardLoginActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetOtpResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    Analytics.logEvent(context, "", "A_114_Failed_" + encryptedMobileNo);
                    Toast.makeText(SpinRewardLoginActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (SharedPref.getWheelTimesUp()) {
            SharedPref.setWheelTimesUp(false);
        }
    }


    private void startTimer(int minutes, int seconds) {
        long startTimeInMillis = (minutes * 60 + seconds) * 1000;
        rewardSeconds = 0;
        countDownTimer = new CountDownTimer(startTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int remainingMinutes = (int) (millisUntilFinished / 1000) / 60;
                int remainingSeconds = (int) (millisUntilFinished / 1000) % 60;
                if (rewardSeconds == 2) {
                    binding.ivCelebration.setVisibility(View.GONE);
                } else {
                    rewardSeconds++;
                }
                // Format time as mm:ss
                String timeLeft = String.format("%02d:%02d", remainingMinutes, remainingSeconds);
                SharedPref.setRewardTimer(timeLeft);
                binding.mobileTimerTv.setText(timeLeft);
                SharedPref.setWheelTimesUp(false);
                Log.d("CountdownTimer", "Time Left: " + timeLeft);
            }

            @Override
            public void onFinish() {
                Log.d("AuthToken", "Mobile OnFinish");
                APILogs.INSTANCE.activityTracker("SPIN_LOGIN_TIME_OUT", SpinRewardLoginActivity.this);
                SharedPref.setWheelTimesUp(true);
                binding.mobileTimerTv.setText("00:00");
                binding.rewardsLayout.setVisibility(View.GONE);
                binding.timeOutLayout.setVisibility(View.VISIBLE);
                binding.rewardHurryUp.setText("Oops");
                binding.rewardLoginTv.setText("Login to spin again");

            }
        };

        countDownTimer.start();
    }


}