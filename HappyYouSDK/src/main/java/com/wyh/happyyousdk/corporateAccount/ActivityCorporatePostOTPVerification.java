package com.wyh.happyyousdk.corporateAccount;

import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.WelcomeActivity;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityCorporatePostOtpVerificationBinding;
import com.wyh.happyyousdk.model.VerifyEmailMobileOTPReq;
import com.wyh.happyyousdk.model.VerifyEmailOTPReq;
import com.wyh.happyyousdk.model.request.GetEmailOTPReq;
import com.wyh.happyyousdk.model.request.SaveCorporateDetailsReq;
import com.wyh.happyyousdk.model.request.SaveCorporateDetailsReqV1;
import com.wyh.happyyousdk.model.request.SaveCorporateStatusReq;
import com.wyh.happyyousdk.model.response.GetEmailOTPResp;
import com.wyh.happyyousdk.model.response.VerifyEmailOTPResp;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCorporatePostOTPVerification extends AppCompatActivity {
    ActivityCorporatePostOtpVerificationBinding binding;
    Context context;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    String CorporateID;
    String EmailID;
    AlertDialog dialog;
    String jsonString;
    private CountDownTimer countDownTimer;
    String mobilNumber;
    private static long TIME_INTERVAL = 60000;
    private static final long RESEND_INTERVAL = 30000; // 30 seconds

    String otpType = "";
    private boolean isTimerRunning = false;
    private long endTime = 0L;
    boolean saveData = false;
    String userName = "", Dob = "", Gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_corporate_post_otp_verification);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        apiInterfaceWyh = RetrofitHandler.apiInterface();

        //Changed RL BG
        Glide.with(context)
                .asBitmap()
                .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "bg_corporate_main_new.png")
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(getResources(), resource);
                        binding.rlMain.setBackground(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        Glide.with(context)
                .asBitmap()
                .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "bg_corporate_email.png")
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(getResources(), resource);
                        binding.llSection2.setBackground(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        CorporateID = getIntent().getStringExtra("CorporateID");
        EmailID = getIntent().getStringExtra("EmailID");
        mobilNumber = getIntent().getStringExtra("mobilNumber");
        jsonString = getIntent().getStringExtra("jsonString");
        saveData = getIntent().getBooleanExtra("saveData", false);
        Dob = getIntent().getStringExtra("Dob");
        userName = getIntent().getStringExtra("userName");
        Gender = getIntent().getStringExtra("Gender");
        Log.e("saveDate", saveData + "");
        binding.tvOTPMessage.setText("An OTP will be send to your email ID:\n" + EmailID);
        startResendOTPTimer(binding.tvResendOTP);
        if (!mobilNumber.equalsIgnoreCase(SharedPref.getDecryptMobileNo())) {
            startResendOTPTimer(binding.tvResendOTPMobile);
            binding.llMobile.setVisibility(View.VISIBLE);
            binding.tvOTPMobileMessage.setText("An OTP will be send your mobile no: \n" + mobilNumber);
            otpType = "both";
        } else {
            binding.llMobile.setVisibility(View.GONE);
            otpType = "email";
        }
        binding.rlUpperCard.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.tvContinue.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_EMAIL_CONTINUE", context);
            if (binding.edtOTP.getText() != null && binding.edtOTP.getText().toString().length() == 6) {
                if (!mobilNumber.equalsIgnoreCase(SharedPref.getDecryptMobileNo())) {
                    if (binding.edOTPMobile.getText() != null && binding.edOTPMobile.getText().toString().length() == 6) {
                        showCorporateAlert();

                    } else {
                        binding.edOTPMobile.requestFocus();
                        Toast.makeText(context, "Please enter valid mobile OTP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showCorporateAlert();
                }
                //showCorporateAlert();
            } else {
                binding.edtOTP.requestFocus();
                Toast.makeText(context, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                showKeyboard(this);
            }
        });
        binding.tvResendOTP.setOnClickListener(view -> {
            /*if (binding.tvResendOTP.isEnabled()) {
                // Trigger OTP logic for field 1
                startResendOTPTimer(binding.tvResendOTP);
            }*/
            TIME_INTERVAL = 60000;
            startResendOTPTimer(binding.tvResendOTP);
            otpType = "email";
            SaveFormData();
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_RESEND_EMAIL", context);

        });

        binding.tvResendOTPMobile.setOnClickListener(view -> {
           /* if (binding.tvResendOTPMobile.isEnabled()) {
                // Trigger OTP logic for field 1
                startResendOTPTimer(binding.tvResendOTPMobile);
            }*/
            TIME_INTERVAL = 60000;
            startResendOTPTimer(binding.tvResendOTPMobile);
            otpType = "mobile";
            SaveFormData();
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_RESEND_MOBILE", context);

        });

        binding.tvNotNow.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_NOT_NOW", context);
            Intent intent = new Intent(context, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        binding.tvEditEmail.setOnClickListener(view -> {
            onBackPressed();

        });
    }

    private void SaveFormData() {
        binding.tvResendOTP.setClickable(false);
        binding.tvResendOTP.setAlpha(0.7f);
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        // SaveCorporateDetailsReq request = new SaveCorporateDetailsReq(RSAEncryption.rsaEncrypt(EmailID),jsonString);

        SaveCorporateDetailsReqV1 request = new SaveCorporateDetailsReqV1(RSAEncryption.rsaEncrypt(EmailID), RSAEncryption.rsaEncrypt(mobilNumber), otpType, true, jsonString);
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


    private void verifyOTP() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        //VerifyEmailOTPReq request;
        VerifyEmailMobileOTPReq request;
        // request=new VerifyEmailOTPReq(RSAEncryption.rsaEncrypt(EmailID),CorporateID,RSAEncryption.rsaEncrypt(binding.edtOTP.getOtp()));
        if (binding.edOTPMobile.getText() != null && !binding.edOTPMobile.getText().toString().isEmpty()) {
            request = new VerifyEmailMobileOTPReq(RSAEncryption.rsaEncrypt(EmailID), CorporateID, RSAEncryption.rsaEncrypt(mobilNumber), RSAEncryption.rsaEncrypt(binding.edtOTP.getText().toString()), RSAEncryption.rsaEncrypt(binding.edOTPMobile.getText().toString()), saveData, true, jsonString);
        } else {
            request = new VerifyEmailMobileOTPReq(RSAEncryption.rsaEncrypt(EmailID), CorporateID, RSAEncryption.rsaEncrypt(mobilNumber), RSAEncryption.rsaEncrypt(binding.edtOTP.getText().toString()), "", saveData, false, jsonString);
        }


        Call<VerifyEmailOTPResp> call = apiInterfaceWyh.VerifyEmailMobileOTP(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerifyEmailOTPResp> call, @NonNull Response<VerifyEmailOTPResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    SharedPref.setCorporateRegistered("YES");
                    if (response.body().getData() != null) {
                        SharedPref.setCorporateId(response.body().getData().getCorpId() + "");
                        SharedPref.setCorporateName(response.body().getData().getCorpName());
                        SharedPref.setCorporateImage(response.body().getData().getCorpLogo());
                        if (saveData) {
                            if (!userName.equalsIgnoreCase("")) {
                                SharedPref.putIsUserNameUpdated(true);
                                SharedPref.putUserName(userName);
                            }
                            if (!Dob.equalsIgnoreCase("")) {
                                SharedPref.putDOB(Dob);
                            }
                            if (!Gender.equalsIgnoreCase("")) {
                                SharedPref.putGender(Gender);
                            }

                        }

                    }
                    Intent intent = new Intent(context, ActivityCorporateAccountMain.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    //assert response.body() != null;
                    try {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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

    private void showCorporateAlert() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_corporate_account, null, false);

        // Build the dialog
        dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        Button btnYes = dialogView.findViewById(R.id.btnYes);
        Button btnNo = dialogView.findViewById(R.id.btnNo);
        tvTitle.setText("Are you Sure Do you want to Add\n" + "your Corporate?");
        btnYes.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_CONFIRM_YES", context);
            //otpType="both";
            verifyOTP();
            dismissDialog();
        });
        btnNo.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_CONFIRM_NO", context);
            dismissDialog();
        });

        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                //dialog.setCancelable(true);
                //dialog.dismiss();  // Dismiss the dialog
                SharedPref.putCorporateAccountNotFound(false);
                dismissDialog();// Optionally close the activity
                return true;
            }
            return false;
        });
        dialog.show();

    }

    void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void startResendOTPTimer(TextView targetTextView) {
        targetTextView.setEnabled(false);
        endTime = System.currentTimeMillis() + TIME_INTERVAL;
        countDownTimer = new CountDownTimer(TIME_INTERVAL, 1000) {
            public void onTick(long millisUntilFinished) {
                TIME_INTERVAL = millisUntilFinished;
                targetTextView.setText("Resend OTP in " + millisUntilFinished / 1000 + " sec");
            }

            public void onFinish() {
                isTimerRunning = false;
                targetTextView.setText("Resend");
                targetTextView.setEnabled(true);
            }
        }.start();
        isTimerRunning = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        TIME_INTERVAL = endTime - System.currentTimeMillis();
        if (TIME_INTERVAL > 0) {
            startResendOTPTimer(binding.tvResendOTP);
            startResendOTPTimer(binding.tvResendOTPMobile);
        } else {
            binding.tvResendOTP.setEnabled(true);
            binding.tvResendOTP.setText("Resend");
            binding.tvResendOTPMobile.setEnabled(true);
            binding.tvResendOTPMobile.setText("Resend");
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}

