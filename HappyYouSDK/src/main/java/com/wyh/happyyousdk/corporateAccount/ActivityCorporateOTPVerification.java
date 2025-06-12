package com.wyh.happyyousdk.corporateAccount;

import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.WelcomeActivity;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.databinding.ActivityCorporateOtpverificationBinding;
import com.wyh.happyyousdk.model.VerifyEmailMobileOTPReq;
import com.wyh.happyyousdk.model.VerifyEmailOTPReq;
import com.wyh.happyyousdk.model.request.SaveCorporateDetailsReq;
import com.wyh.happyyousdk.model.request.SaveCorporateDetailsReqV1;
import com.wyh.happyyousdk.model.request.SaveCorporateStatusReq;
import com.wyh.happyyousdk.model.response.GetEmailOTPResp;
import com.wyh.happyyousdk.model.response.VerifyEmailOTPResp;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import in.aabhasjindal.otptextview.OTPListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCorporateOTPVerification extends AppCompatActivity {
    ActivityCorporateOtpverificationBinding binding;
    Context context;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    String CorporateID;
    String mobilNumber;
    String EmailID,jsonString;
    String otpType="";
    boolean saveData=false;
    private CountDownTimer countDownTimer;
    private static final long TIME_INTERVAL = 60000; // 30 seconds
    String [] totalTime;
    int minutes,seconds;
    int rewardSeconds = 0;
    String userName="",Dob="",Gender="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_corporate_otpverification);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        CorporateID=getIntent().getStringExtra("CorporateID");
        EmailID=getIntent().getStringExtra("EmailID");
        jsonString=getIntent().getStringExtra("jsonString");
        mobilNumber=getIntent().getStringExtra("mobilNumber");
        saveData=getIntent().getBooleanExtra("saveData",false);
        Dob=getIntent().getStringExtra("Dob");
        userName=getIntent().getStringExtra("userName");
        Gender=getIntent().getStringExtra("Gender");
        binding.tvSpinWheel.setText("An OTP will be send to your email ID:\n"+EmailID);
        binding.tvNonSpinWheel.setText("An OTP will be send to your email ID:\n"+EmailID);
        totalTime = SharedPref.getRewardTiwer().split(":");
        try {
            if(totalTime.length != 0){
                minutes = Integer.parseInt(totalTime[0]);
                seconds = Integer.parseInt(totalTime[1]);
                startTimer(minutes,seconds);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (!SharedPref.getSpinWheelStatus()) {
            binding.llNonSpinWheel.setVisibility(View.GONE);
            binding.tvSpinWheel.setVisibility(View.GONE);
            binding.llNonSpinWheelOTP.setVisibility(View.VISIBLE);
        }
        else {
            binding.tvSpinWheel.setVisibility(View.VISIBLE);
            binding.llSpinWheelOTP.setVisibility(View.VISIBLE);
            binding.tvTitle1.setText("One Click. Big Benefits.");
            binding.tvTitle2.setText("Get started with just one click your corporate benefits are ready to go!");
        }
        if (!mobilNumber.equalsIgnoreCase(SharedPref.getDecryptMobileNo()))
        {
            binding.llMobile.setVisibility(View.VISIBLE);
            binding.llSpinMobile.setVisibility(View.VISIBLE);
            binding.tvOTPMobileMessage.setText("An OTP will be send your mobile no: \n" +mobilNumber);
            binding.tvOTPMobileMessageSpin.setText("An OTP will be send your mobile no: \n" +mobilNumber);
            otpType="both";
        }
        else {
            binding.llMobile.setVisibility(View.GONE);
            binding.llSpinMobile.setVisibility(View.GONE);
            otpType="email";
        }
        if (!SharedPref.getSpinWheelStatus()) {
            binding.llSpinWheelOTP.setVisibility(View.GONE);
            binding.llNonSpinWheelOTP.setVisibility(View.VISIBLE);
            binding.tvNonSpinWheel.setVisibility(View.VISIBLE);
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_YES",context);
            binding.btnNonSpinWheelSendOTP.setAlpha(1F);
            binding.llActions.setAlpha(0.5F);
            binding.llActions.setClickable(false);
            binding.llActions.setEnabled(false);
            binding.edtNonSpinWheelEmail.setClickable(true);
            binding.btnNonSpinWheelSendOTP.setClickable(true);
            binding.btnNonSpinWheelSendOTP.setEnabled(true);
            binding.edtNonSpinWheelEmail.setEnabled(true);
            startResendOTPTimer(binding.tvResendOTP);
            startResendOTPTimer(binding.tvResendOTPMobile);
        }
        else {
            binding.llSpinWheelOTP.setVisibility(View.VISIBLE);
            binding.llNonSpinWheelOTP.setVisibility(View.GONE);
            binding.tvSpinWheel.setVisibility(View.VISIBLE);
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_YES",context);
            binding.llSpinWheel.setAlpha(1F);
            binding.llActions.setAlpha(0.5F);
            binding.llActions.setClickable(false);
            binding.llActions.setEnabled(false);
            binding.llSpinWheelSendOTP.setClickable(true);
            binding.edtSpinWheelEmail.setClickable(true);
            binding.llSpinWheelSendOTP.setEnabled(true);
            binding.edtSpinWheelEmail.setEnabled(true);
            binding.llSpinWheel.setClickable(true);
            startResendOTPTimer(binding.tvResendOTPSpinWheel);
            startResendOTPTimer(binding.tvResendOTPSpinWheelMobile);
        }
        binding.tvSkip.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_SKIP",context);
            SaveCorporateStatus();
        });
        binding.tvSkip2.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_OTP_SKIP",context);
            SaveCorporateStatus();
        });
        binding.tvSkipNonSpinWheel.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_SKIP",context);
            SaveCorporateStatus();
        });
        binding.tvSkipNonSpinWheelOTP.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_OTP_SKIP",context);
            SaveCorporateStatus();
        });
        binding.tvSpinWheelContinue.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_OTP_CONTINUE",context);
            if (binding.edtOTPSpinWheel.getOTP().length()==6)
            {
                if (!mobilNumber.equalsIgnoreCase(SharedPref.getDecryptMobileNo()))
                {
                    if (binding.edtOTPSpinWheelMobile.getOTP().length()==6)
                    {
                        verifyOTP();

                    }
                    else {
                        binding.edtOTPSpinWheelMobile.requestFocus();
                        Toast.makeText(context, "Please enter valid mobile OTP", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    verifyOTP();
                }

            }

            else {
                binding.edtOTPSpinWheel.requestFocus();
                Toast.makeText(context, "Please enter valid email OTP", Toast.LENGTH_SHORT).show();
                showKeyboard(this);
            }
        });

        binding.tvNonSpinWheelContinue.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_OTP_CONTINUE",context);
            if (binding.edNonSpinWheelOTP.getOTP().length()==6)
            {
                //verifyOTP();
                if (!mobilNumber.equalsIgnoreCase(SharedPref.getDecryptMobileNo()))
                {
                    if (binding.edNonSpinWheelOTPMobile.getOTP().length()==6)
                    {
                        verifyOTP();

                    }
                    else {
                        binding.edNonSpinWheelOTPMobile.requestFocus();
                        Toast.makeText(context, "Please enter valid mobile OTP", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    verifyOTP();
                }
            }

            else {
                binding.edNonSpinWheelOTP.requestFocus();
                Toast.makeText(context, "Please enter valid email OTP", Toast.LENGTH_SHORT).show();
                showKeyboard(this);
            }
        });
        binding.tvNonSpinWheelEditEmail.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_OTP_EDIT_EMAIL",context);
            onBackPressed();
        });
        binding.tvSpinWheelEditEmail.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_OTP_EDIT_EMAIL",context);
            onBackPressed();
        });
        binding.tvResendOTP.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_WHEEL_CORPORATE_RESEND_EMAIL",context);
            if (binding.tvResendOTP.isEnabled()) {
                // Trigger OTP logic for field 1
                startResendOTPTimer(binding.tvResendOTP);
            }
            otpType="email";
            SaveFormData();

        });
        binding.tvResendOTPSpinWheel.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_RESEND_EMAIL",context);
            if (binding.tvResendOTPSpinWheel.isEnabled()) {
                // Trigger OTP logic for field 1
                startResendOTPTimer(binding.tvResendOTPSpinWheel);
            }
            otpType="email";
            SaveFormData();
        });

        binding.tvResendOTPSpinWheelMobile.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_RESEND_MOBILE",context);
            if (binding.tvResendOTPSpinWheelMobile.isEnabled()) {
                // Trigger OTP logic for field 1
                startResendOTPTimer(binding.tvResendOTPSpinWheelMobile);
            }
            otpType="mobile";
            SaveFormData();


        });
        binding.tvResendOTPMobile.setOnClickListener(view -> {
            if (binding.tvResendOTPMobile.isEnabled()) {
                // Trigger OTP logic for field 1
                startResendOTPTimer(binding.tvResendOTPMobile);
            }
            otpType="mobile";
            SaveFormData();
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_WHEEL_CORPORATE_RESEND_MOBILE",context);

        });

    }
    private void SaveCorporateStatus() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        SaveCorporateStatusReq request = new SaveCorporateStatusReq(false);
        Call<GetEmailOTPResp> call = apiInterfaceWyh.SaveCorporateStatus(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetEmailOTPResp> call, @NonNull Response<GetEmailOTPResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    SharedPref.setCorporateRegistered("NO");
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.putExtra("isCameFromRegistration", true);
                    intent.putExtra("isCameFromSpinWheel",true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getMsg() != null)
                    {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetEmailOTPResp> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void verifyOTP() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        //VerifyEmailOTPReq request;
        VerifyEmailMobileOTPReq request;
        if (SharedPref.getSpinWheelStatus())
        {
          //  request=new VerifyEmailOTPReq(RSAEncryption.rsaEncrypt(EmailID),CorporateID,RSAEncryption.rsaEncrypt(binding.edtOTPSpinWheel.getOTP()));)
            if (!binding.edtOTPSpinWheelMobile.getOTP().isEmpty())
            {
                request=new VerifyEmailMobileOTPReq(RSAEncryption.rsaEncrypt(EmailID),CorporateID,RSAEncryption.rsaEncrypt(mobilNumber),RSAEncryption.rsaEncrypt(binding.edtOTPSpinWheel.getOTP()),RSAEncryption.rsaEncrypt(binding.edtOTPSpinWheelMobile.getOTP()),saveData,true,jsonString);
            }
            else {
                request=new VerifyEmailMobileOTPReq(RSAEncryption.rsaEncrypt(EmailID),CorporateID,RSAEncryption.rsaEncrypt(mobilNumber),RSAEncryption.rsaEncrypt(binding.edtOTPSpinWheel.getOTP()),"",saveData,false,jsonString);
            }
        }
        else {
            //request=new VerifyEmailOTPReq(RSAEncryption.rsaEncrypt(EmailID),CorporateID,RSAEncryption.rsaEncrypt(binding.edNonSpinWheelOTP.getOTP()));

            if (!binding.edNonSpinWheelOTPMobile.getOTP().isEmpty())
            {
                request=new VerifyEmailMobileOTPReq(RSAEncryption.rsaEncrypt(EmailID),CorporateID,RSAEncryption.rsaEncrypt(mobilNumber),RSAEncryption.rsaEncrypt(binding.edNonSpinWheelOTP.getOTP()),RSAEncryption.rsaEncrypt(binding.edNonSpinWheelOTPMobile.getOTP()),saveData,true,jsonString);
            }
            else {
                request=new VerifyEmailMobileOTPReq(RSAEncryption.rsaEncrypt(EmailID),CorporateID,RSAEncryption.rsaEncrypt(mobilNumber),RSAEncryption.rsaEncrypt(binding.edNonSpinWheelOTP.getOTP()),"",saveData,false,jsonString);
            }
        }


        Call<VerifyEmailOTPResp> call = apiInterfaceWyh.VerifyEmailMobileOTP(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerifyEmailOTPResp> call, @NonNull Response<VerifyEmailOTPResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    SharedPref.setCorporateRegistered("YES");
                    if (saveData)
                    {
                        if (!userName.equalsIgnoreCase(""))
                        {
                            SharedPref.putIsUserNameUpdated(true);
                            SharedPref.putUserName(userName);
                        }
                        if (!Dob.equalsIgnoreCase(""))
                        {
                            SharedPref.putDOB(Dob);
                        }
                        if (!Gender.equalsIgnoreCase(""))
                        {
                            SharedPref.putGender(Gender);
                        }

                    }
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.putExtra("isCameFromRegistration", true);
                    intent.putExtra("isCameFromSpinWheel",true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                  //  assert response.body() != null;
                    //Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerifyEmailOTPResp> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void SaveFormData() {
        binding.tvResendOTP.setClickable(false);
        binding.tvResendOTP.setAlpha(0.7f);
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        SaveCorporateDetailsReqV1 request = new SaveCorporateDetailsReqV1(RSAEncryption.rsaEncrypt(EmailID),RSAEncryption.rsaEncrypt(mobilNumber),otpType,true,jsonString);
        Call<GetEmailOTPResp> call = apiInterfaceWyh.SaveCorporateDetailsV1(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetEmailOTPResp> call, @NonNull Response<GetEmailOTPResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    binding.tvResendOTP.setClickable(true);
                    binding.tvResendOTP.setAlpha(1.0f);
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    binding.tvResendOTP.setClickable(true);
                    binding.tvResendOTP.setAlpha(1.0f);
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetEmailOTPResp> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                binding.tvResendOTP.setClickable(true);
                binding.tvResendOTP.setAlpha(1.0f);
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void startResendOTPTimer(TextView targetTextView) {
        targetTextView.setEnabled(false);

        countDownTimer = new CountDownTimer(TIME_INTERVAL, 1000) {
            public void onTick(long millisUntilFinished) {
                targetTextView.setText("Resend OTP in " + millisUntilFinished / 1000 + " sec");
            }

            public void onFinish() {
                targetTextView.setText("Resend OTP");
                targetTextView.setEnabled(true);
            }
        }.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
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
                binding.otpCountdown.setText(timeLeft);
                SharedPref.setWheelTimesUp(false);
                Log.d("CountdownTimer", "Time Left: " + timeLeft);
            }

            @Override
            public void onFinish() {
                Log.d("AuthToken", "Mobile OnFinish");
                APILogs.INSTANCE.activityTracker("SPIN_LOGIN_TIME_OUT", context);
                SharedPref.setWheelTimesUp(true);
                binding.mobileTimerTv.setText("00:00");
                binding.otpCountdown.setText("00:00");
                // binding.rewardsLayout.setVisibility(View.GONE);
               /* binding.timeOutLayout.setVisibility(View.VISIBLE);
                binding.rewardHurryUp.setText("Oops");
                binding.rewardLoginTv.setText("Login to spin again");*/

            }
        };

        countDownTimer.start();
    }
}
