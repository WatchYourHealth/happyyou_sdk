package com.wyh.happyyousdk.corporateAccount;

import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.WelcomeActivity;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.databinding.ActivityCorporateEmailLoginBinding;
import com.wyh.happyyousdk.model.VerifyEmailOTPReq;
import com.wyh.happyyousdk.model.request.GetEmailOTPReq;
import com.wyh.happyyousdk.model.request.SaveCorporateStatusReq;
import com.wyh.happyyousdk.model.response.GetEmailOTPResp;
import com.wyh.happyyousdk.model.response.VerifyEmailOTPResp;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCorporateAccountLink extends AppCompatActivity {
    ActivityCorporateEmailLoginBinding binding;
    Context context;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    List<String> corporateName = new ArrayList<>();
    List<GetEmailOTPResp.Data.corporateDetails> corporateDetails = new ArrayList<>();
    String CorporateID = "";
    String CorporateName = "";
    List<StateModel> stateData = new ArrayList<StateModel>();
    String EmailID = "";
    public static CountDownTimer countDownTimer;
    int rewardSeconds = 0;
    int minutes, seconds;
    String[] totalTime;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    String jsonData = "";
    String CorporateData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_corporate_email_login);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        binding.llSpinWheelSendOTP.setClickable(false);
        binding.llSpinWheelSendOTP.setEnabled(false);
        binding.tvSpinWheelSendOTP.setClickable(false);
        binding.tvSpinWheelSendOTP.setEnabled(false);
        binding.edtSpinWheelEmail.setClickable(false);
        binding.edtSpinWheelEmail.setEnabled(false);
        totalTime = SharedPref.getRewardTiwer().split(":");
        binding.timeOutLayout.setVisibility(View.GONE);
        //binding.rewardsLayout.setVisibility(View.VISIBLE);
        Log.d("AuthToken", String.valueOf(totalTime));
        try {
            if (totalTime.length != 0) {
                minutes = Integer.parseInt(totalTime[0]);
                seconds = Integer.parseInt(totalTime[1]);
                startTimer(minutes, seconds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!SharedPref.getSpinWheelStatus()) {
            binding.llSpinWheel.setVisibility(View.GONE);
            binding.llNonSpinWheel.setVisibility(View.VISIBLE);
        } else {
            binding.llSpinWheel.setVisibility(View.VISIBLE);
            binding.llNonSpinWheel.setVisibility(View.GONE);
        }
        binding.btnYes.setOnClickListener(view -> {
            binding.btnNo.setEnabled(false);
            binding.btnNo.setClickable(false);
            if (!SharedPref.getSpinWheelStatus()) {

                APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_YES", context);
                binding.btnNonSpinWheelSendOTP.setAlpha(1F);
                binding.llActions.setAlpha(0.5F);
                binding.llActions.setClickable(false);
                binding.llActions.setEnabled(false);
                binding.edtNonSpinWheelEmail.setClickable(true);
                binding.btnNonSpinWheelSendOTP.setClickable(true);
                binding.btnNonSpinWheelSendOTP.setEnabled(true);
                binding.edtNonSpinWheelEmail.setEnabled(true);
            } else {
                APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_YES", context);
                binding.llSpinWheel.setAlpha(1F);
                binding.llActions.setAlpha(0.5F);
                binding.llActions.setClickable(false);
                binding.llActions.setEnabled(false);
                binding.llSpinWheelSendOTP.setClickable(true);
                binding.edtSpinWheelEmail.setClickable(true);
                binding.llSpinWheelSendOTP.setEnabled(true);
                binding.edtSpinWheelEmail.setEnabled(true);
                binding.llSpinWheel.setClickable(true);

            }

        });

        binding.btnNo.setOnClickListener(view -> {
            if (!SharedPref.getSpinWheelStatus()) {
                APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_NO", context);
            } else {
                APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_NO", context);
            }
            SaveCorporateStatus();
        });

        binding.tvSkip.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_SKIP", context);
            SaveCorporateStatus();
        });
        binding.tvSkip2.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_OTP_SKIP", context);
            SaveCorporateStatus();
        });
        binding.tvSkipNonSpinWheel.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_SKIP", context);
            SaveCorporateStatus();
        });
        binding.tvSkipNonSpinWheelOTP.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_OTP_SKIP", context);
            SaveCorporateStatus();
        });
        binding.llSpinWheelSendOTP.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_EMAIL_CONTINUE", context);
            if (isValidEmail(binding.edtSpinWheelEmail.getText().toString())) {
                //sendOTp();
                EmailID = binding.edtSpinWheelEmail.getText().toString().trim();
                GetCorporateDetails();
            } else {
                Toast.makeText(context, "Please enter valid Email ID", Toast.LENGTH_SHORT).show();
            }

        });
        binding.tvResendOTPSpinWheel.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_EMAIL_CONTINUE", context);
            if (isValidEmail(binding.edtSpinWheelEmail.getText().toString())) {
                sendOTp();
            } else {
                Toast.makeText(context, "Please enter valid Email ID", Toast.LENGTH_SHORT).show();
            }

        });
        binding.tvResendOTP.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_EMAIL_CONTINUE", context);
            if (isValidEmail(binding.edtNonSpinWheelEmail.getText().toString())) {
                sendOTp();
            } else {
                Toast.makeText(context, "Please enter valid Email ID", Toast.LENGTH_SHORT).show();
            }

        });


        binding.btnNonSpinWheelSendOTP.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_EMAIL_CONTINUE", context);
            if (isValidEmail(binding.edtNonSpinWheelEmail.getText().toString())) {
                // sendOTp();
                EmailID = binding.edtNonSpinWheelEmail.getText().toString().trim();
                GetCorporateDetails();
            } else {
                Toast.makeText(context, "Please enter valid Email ID", Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvSpinWheelContinue.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_OTP_CONTINUE", context);
            Intent intent = new Intent(context, ActivityCorporateRegisterForm.class);
            intent.putExtra("jsonData", jsonData);
            intent.putExtra("CorporateID", CorporateID);
            intent.putExtra("CorporateName", CorporateName);
            intent.putExtra("EmailID", EmailID);
            intent.putExtra("stateData", new Gson().toJson(stateData));
            intent.putExtra("corporateDetails", new Gson().toJson(corporateDetails));
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            /*if (binding.edtOTPSpinWheel.getOTP().length()==6)
            {
                verifyOTP();
            }

            else {
                binding.edtOTPSpinWheel.requestFocus();
                Toast.makeText(context, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                showKeyboard(this);
            }*/
        });

        binding.tvNonSpinWheelContinue.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_OTP_CONTINUE", context);
            Intent intent = new Intent(context, ActivityCorporateRegisterForm.class);
            intent.putExtra("jsonData", jsonData);
            intent.putExtra("CorporateID", CorporateID);
            intent.putExtra("CorporateName", CorporateName);
            intent.putExtra("EmailID", EmailID);
            intent.putExtra("stateData", new Gson().toJson(stateData));
            intent.putExtra("corporateDetails", new Gson().toJson(corporateDetails));
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

           /* if (binding.edNonSpinWheelOTP.getOTP().length()==6)
            {
                verifyOTP();
            }

            else {
                binding.edNonSpinWheelOTP.requestFocus();
                Toast.makeText(context, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                showKeyboard(this);
            }*/
        });
        binding.tvSpinWheelEditEmail.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_OTP_EDIT_EMAIL", context);
            corporateDetails.clear();
            corporateName.clear();
            CorporateID = "";
            binding.edtOTPSpinWheel.setOTP("");
            //binding.edtSpinWheelEmail.setText("");
            binding.llSpinWheel.setVisibility(View.VISIBLE);
            binding.llSpinWheelOTP.setVisibility(View.GONE);
            binding.llNonSpinWheel.setVisibility(View.GONE);
            binding.llNonSpinWheelOTP.setVisibility(View.GONE);
        });

        binding.tvNonSpinWheelEditEmail.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_SPIN_CORPORATE_OTP_EDIT_EMAIL", context);
            corporateDetails.clear();
            corporateName.clear();
            CorporateID = "";
            //binding.edtNonSpinWheelEmail.setText("");
            binding.edNonSpinWheelOTP.setOTP("");
            binding.llSpinWheel.setVisibility(View.GONE);
            binding.llSpinWheelOTP.setVisibility(View.GONE);
            binding.llNonSpinWheel.setVisibility(View.VISIBLE);
            binding.llNonSpinWheelOTP.setVisibility(View.GONE);
        });

        binding.spinnerSpinWheel.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                CorporateID = String.valueOf(corporateDetails.get(position).getCorpId());
                CorporateName = String.valueOf(corporateDetails.get(position).getCorporateNames());
                jsonData = corporateDetails.get(position).getQuestionJson();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });
        binding.spinnerNonSpinWheel.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                CorporateID = String.valueOf(corporateDetails.get(position).getCorpId());
                CorporateName = String.valueOf(corporateDetails.get(position).getCorporateNames());
                jsonData = corporateDetails.get(position).getQuestionJson();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });
    }

    private void sendOTp() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetEmailOTPReq request;
        if (SharedPref.getSpinWheelStatus()) {
            request = new GetEmailOTPReq(RSAEncryption.rsaEncrypt(binding.edtSpinWheelEmail.getText().toString()));
        } else {
            request = new GetEmailOTPReq(RSAEncryption.rsaEncrypt(binding.edtNonSpinWheelEmail.getText().toString()));
        }

        Call<GetEmailOTPResp> call = apiInterfaceWyh.getEmailOTP(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetEmailOTPResp> call, @NonNull Response<GetEmailOTPResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess() && !response.body().getData().getClients().isEmpty()) {
                    binding.tvSpinWheel.setText("An OTP will be send to your email ID:\n" + binding.edtSpinWheelEmail.getText().toString());
                    binding.tvNonSpinWheel.setText("An OTP will be send to your email ID:\n" + binding.edtNonSpinWheelEmail.getText().toString());
                    corporateDetails = response.body().getData().getClients();
                    setSpinnerData(corporateDetails);
                    if (!SharedPref.getSpinWheelStatus()) {
                        binding.llNonSpinWheel.setVisibility(View.GONE);
                        binding.llNonSpinWheelOTP.setVisibility(View.VISIBLE);
                    } else {
                        binding.llSpinWheel.setVisibility(View.GONE);
                        binding.llSpinWheelOTP.setVisibility(View.VISIBLE);
                        binding.tvTitle1.setText("One Click. Big Benefits.");
                        binding.tvTitle2.setText("Get started with just one click your corporate benefits are ready to go!");
                    }
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    SharedPref.putCorporateAccountNotFound(true);
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.putExtra("isCameFromRegistration", true);
                    intent.putExtra("isCameFromSpinWheel", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, "No Corporate Account found", Toast.LENGTH_SHORT).show();
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

    private void GetCorporateDetails() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetEmailOTPReq request;
        if (SharedPref.getSpinWheelStatus()) {
            //request= new GetEmailOTPReq(RSAEncryption.rsaEncrypt(binding.edtSpinWheelEmail.getText().toString()));
            request = new GetEmailOTPReq(binding.edtSpinWheelEmail.getText().toString());
        } else {
            // request= new GetEmailOTPReq(RSAEncryption.rsaEncrypt(binding.edtNonSpinWheelEmail.getText().toString()));
            request = new GetEmailOTPReq(binding.edtNonSpinWheelEmail.getText().toString());
        }

        Call<GetEmailOTPResp> call = apiInterfaceWyh.getCorporateDetails(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetEmailOTPResp> call, @NonNull Response<GetEmailOTPResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess() && !response.body().getData().getClients().isEmpty()) {
                    binding.tvSpinWheel.setText("An OTP will be send to your email ID:\n" + binding.edtSpinWheelEmail.getText().toString());
                    binding.tvNonSpinWheel.setText("An OTP will be send to your email ID:\n" + binding.edtNonSpinWheelEmail.getText().toString());
                    corporateDetails = response.body().getData().getClients();
                    stateData = response.body().getData().getStateList();
                    setSpinnerData(corporateDetails);
                    /*setSpinnerData(corporateDetails);
                    if (!SharedPref.getSpinWheelStatus()) {
                        binding.llNonSpinWheel.setVisibility(View.GONE);
                        binding.llNonSpinWheelOTP.setVisibility(View.VISIBLE);
                    }
                    else {
                        binding.llSpinWheel.setVisibility(View.GONE);
                        binding.llSpinWheelOTP.setVisibility(View.VISIBLE);
                        binding.tvTitle1.setText("One Click. Big Benefits.");
                        binding.tvTitle2.setText("Get started with just one click your corporate benefits are ready to go!");
                    }*/
                    Intent intent = new Intent(context, ActivityCorporateRegisterForm.class);
                    intent.putExtra("jsonData", jsonData);
                    intent.putExtra("CorporateID", CorporateID);
                    intent.putExtra("CorporateName", CorporateName);
                    intent.putExtra("EmailID", EmailID);
                    intent.putExtra("stateData", new Gson().toJson(stateData));
                    intent.putExtra("corporateDetails", new Gson().toJson(corporateDetails));
                    startActivity(intent);

                } else {
                    SharedPref.putCorporateAccountNotFound(true);
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.putExtra("isCameFromRegistration", true);
                    intent.putExtra("isCameFromSpinWheel", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, "No Corporate Account found", Toast.LENGTH_SHORT).show();
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
        VerifyEmailOTPReq request;
        if (SharedPref.getSpinWheelStatus()) {
            request = new VerifyEmailOTPReq(RSAEncryption.rsaEncrypt(binding.edtSpinWheelEmail.getText().toString()), CorporateID, RSAEncryption.rsaEncrypt(binding.edtOTPSpinWheel.getOTP()));
        } else {
            request = new VerifyEmailOTPReq(RSAEncryption.rsaEncrypt(binding.edtNonSpinWheelEmail.getText().toString()), CorporateID, RSAEncryption.rsaEncrypt(binding.edNonSpinWheelOTP.getOTP()));
        }

        Call<VerifyEmailOTPResp> call = apiInterfaceWyh.verifyEmailOTP(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerifyEmailOTPResp> call, @NonNull Response<VerifyEmailOTPResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    SharedPref.setCorporateRegistered("YES");
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.putExtra("isCameFromRegistration", true);
                    intent.putExtra("isCameFromSpinWheel", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    assert response.body() != null;
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
                    intent.putExtra("isCameFromSpinWheel", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
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


    public void setSpinnerData(List<GetEmailOTPResp.Data.corporateDetails> corporateDetails) {
        for (int i = 0; i < corporateDetails.size(); i++) {
            corporateName.add(corporateDetails.get(i).getCorporateNames());
        }
        if (corporateName.size() > 1) {
            binding.imSpinnerNonSpinWheel.setVisibility(View.VISIBLE);
            binding.imSpinnerSpinWheel.setVisibility(View.VISIBLE);
        } else {
            binding.imSpinnerNonSpinWheel.setVisibility(View.INVISIBLE);
            binding.imSpinnerSpinWheel.setVisibility(View.INVISIBLE);
        }
        ArrayAdapter<String> spinnerSpinWheelAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, corporateName);
        ArrayAdapter<String> spinnerNonSpinWheelAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, corporateName);
        spinnerSpinWheelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNonSpinWheelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSpinWheel.setAdapter(spinnerSpinWheelAdapter);
        binding.spinnerNonSpinWheel.setAdapter(spinnerNonSpinWheelAdapter);
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

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        // Alternative: return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}
