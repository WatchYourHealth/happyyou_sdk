package com.wyh.happyyousdk.policyDetail;

import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.otpview.OTPTextView;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityPolicyDetailLoginBinding;
import com.wyh.happyyousdk.model.GetClientRes;
import com.wyh.happyyousdk.model.GetOTPReq;
import com.wyh.happyyousdk.model.MPINReq;
import com.wyh.happyyousdk.model.VerifyOTP;
import com.wyh.happyyousdk.policyDetail.helper.FingerprintHelper;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyLoginMPIN extends AppCompatActivity {
    ActivityPolicyDetailLoginBinding binding;
    Context context;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_policy_detail_login);
        context = this;
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        SharedPref.init(context);
        SharedPreference.init(context);
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        showLoginMPINDialog();
        binding.btnSendOTP.setOnClickListener(view -> {
            if (binding.edtOTP.getOtp().length()==6) {
                verifyOTP();
            } else {
                binding.edtOTP.requestFocus();
                Toast.makeText(context, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                showKeyboard(this);
            }
        });
        binding.tvResendOTP.setOnClickListener(view -> {

            sendOTp();
        });

        binding.imBack.setOnClickListener(view -> {

            onBackPressed();
        });
    }

    private void showLoginMPINDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dailog_login_by_mpin, null, false);

        // Build the dialog
         dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView  imSubmitMPIN=dialogView.findViewById(R.id.imSubmitMPIN);
        ImageView  imFingerPrint=dialogView.findViewById(R.id.imFingerPrint);
        TextView  tvOr=dialogView.findViewById(R.id.tvOr);
        TextView  tvForget=dialogView.findViewById(R.id.tvForget);
        if (SharedPref.getHasBiometric())
        {
            imFingerPrint.setVisibility(View.VISIBLE);
            tvOr.setVisibility(View.VISIBLE);
        }
        else {
            imFingerPrint.setVisibility(View.GONE);
            tvOr.setVisibility(View.GONE);
        }
        OTPTextView edtOTP=dialogView.findViewById(R.id.edtOTP);
        imSubmitMPIN.setOnClickListener(view -> {
            if (edtOTP.getOtp() == null || !edtOTP.getOtp().equals(SharedPref.getPolicyMPin())){
                Toast.makeText(context, "Please enter Valid MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(context, PolicyListActivity.class);
            intent.putExtra("intent","Login");
            startActivity(intent);
            dismissDialog();
        });
        tvForget.setOnClickListener(view -> {


            sendOTp();
        });
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                //dialog.setCancelable(true);
                //dialog.dismiss();  // Dismiss the dialog
                Intent intent = new Intent(context, NewDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dismissDialog();// Optionally close the activity
                return true;
            }
            return false;
        });
        imFingerPrint.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                FingerprintHelper.authenticateFingerprint(context, new FingerprintHelper.FingerprintCallback() {
                    @Override
                    public void onAuthenticationSuccess() {
                        Intent intent = new Intent(context, PolicyListActivity.class);
                        intent.putExtra("intent","Login");
                        startActivity(intent);
                        dismissDialog();
                    }

                    @Override
                    public void onAuthenticationError(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }

    /*private void showCustomForgetMPINDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_create_mpin, null);

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        ImageView imSubmit = dialogView.findViewById(R.id.imSubmit);
        OtpTextView edtSetMPIN = dialogView.findViewById(R.id.edtSetMPIN);
        OtpTextView edtOTP = dialogView.findViewById(R.id.edtOTP);
        imSubmit.setOnClickListener(view -> {
            if (edtSetMPIN.getOtp().length()!=4)
            {
                Toast.makeText(context, "Please enter Enter  MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edtOTP.getOtp().length()!=4)
            {
                Toast.makeText(context, "Please enter Re-enter  MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!edtSetMPIN.getOtp().equals(edtOTP.getOtp())){
                Toast.makeText(context, "Please enter Re-enter  MPIN", Toast.LENGTH_SHORT).show();
            }
            //AddUpateMPIN(RSAEncryption.rsaEncrypt(binding.edtSetMPIN.getOTP()));
            Intent intent = new Intent(context, PolicyCreateFingerPrint.class);
            intent.putExtra("intentOtp",edtSetMPIN.getOTP());
            intent.putExtra("intentOtpEncrpt",RSAEncryption.rsaEncrypt(edtSetMPIN.getOTP()));
            startActivity(intent);
            dialog.dismiss();
            dialog.cancel();

            finish();
        });
        dialog.show();
    }*/
    private void verifyOTP( ) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        VerifyOTP request = new VerifyOTP(RSAEncryption.rsaEncrypt(binding.edtOTP.getOtp()));
        Call<GetClientRes> call = apiInterfaceWyh.GetPolicyOTP( SharedPref.getAuthToken(),request);

        call.enqueue(new Callback<GetClientRes>() {
            @Override
            public void onResponse(Call<GetClientRes> call, Response<GetClientRes> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getStatus()) {
                   /* Intent intent = new Intent(context, PolicyCreateMPIN.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();*/
                    binding.tvMessage.setText(response.body().getMsg());
                    binding.getStartedLayout.setVisibility(View.GONE);
                    showCustomForgetMPINDialog();
                } else {
                    //Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetClientRes> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendOTp( ) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetOTPReq request = new GetOTPReq("1","","",SharedPref.getEncryptedMobileNo(),15);
        Log.e("Mobile",RSAEncryption.callDecryptionMethod(SharedPref.getEncryptedMobileNo()));
        Call<GetClientRes> call = apiInterfaceWyh.GetPolicyOTP( SharedPref.getAuthToken(),request);

        call.enqueue(new Callback<GetClientRes>() {
            @Override
            public void onResponse(Call<GetClientRes> call, Response<GetClientRes> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getStatus()) {
                    /*Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_success));*/
                    binding.getStartedLayout.setVisibility(View.VISIBLE);
                    binding.tvMessage.setText(response.body().getMsg());
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    dismissDialog();
                } else {
                    //Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetClientRes> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dialog != null && dialog.isShowing()) {
            dialog.setCancelable(true);
            dismissDialog();
        }
        super.onBackPressed();
        Intent intent = new Intent(context, NewDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();// Close activity

    }
    private void showCustomForgetMPINDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_create_mpin, null);

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView imSubmit = dialogView.findViewById(R.id.imSubmit);
        OTPTextView edtSetMPIN = dialogView.findViewById(R.id.edtSetMPIN);
        OTPTextView edtOTP = dialogView.findViewById(R.id.edtOTP);
        imSubmit.setOnClickListener(view -> {
            if (edtSetMPIN.getOtp().length()!=4)
            {
                Toast.makeText(context, "Please enter valid MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edtOTP.getOtp().length()!=4)
            {
                Toast.makeText(context, "Please enter Re-enter  MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!edtSetMPIN.getOtp().equals(edtOTP.getOtp())){
                Toast.makeText(context, "The MPINs do not match. Please enter the correct MPIN", Toast.LENGTH_SHORT).show();
            }
            if (SharedPref.getHasBiometric())
            {
                Log.e("edtOTP.getOtp()",edtOTP.getOtp());
                AddUpdateMPIN(edtOTP.getOtp(),SharedPref.getHasBiometric());
            }
            else {
                showFingerPrintDialog(edtOTP.getOtp());
                /*Intent intent = new Intent(context, PolicyCreateFingerPrint.class);
                intent.putExtra("intentOtp",edtSetMPIN.getOTP());
                intent.putExtra("intentOtpEncrpt",RSAEncryption.rsaEncrypt(edtSetMPIN.getOTP()));
                startActivity(intent);*/
                dialog.dismiss();
                dialog.cancel();

                //finish();
            }
            //AddUpateMPIN(RSAEncryption.rsaEncrypt(binding.edtSetMPIN.getOTP()));

        });
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                //dialog.setCancelable(true);
                // dialog.dismiss();  // Dismiss the dialog
                Intent intent = new Intent(context, NewDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);// Optionally close the activity
                dialog.dismiss();
                return true;

            }
            return false;
        });
        dialog.show();
    }

    private void showFingerPrintDialog(String intentOtp) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dailog_create_finger_print, null);

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvSkip=dialogView.findViewById(R.id.tvSkip);
        ImageView imFingerPrint=dialogView.findViewById(R.id.imFingerPrint);
        tvSkip.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SKIP_BIOMETRIC",context);
            AddUpdateMPIN(intentOtp,false);
        });
        imFingerPrint.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                FingerprintHelper.authenticateFingerprint(context, new FingerprintHelper.FingerprintCallback() {
                    @Override
                    public void onAuthenticationSuccess() {
                        AddUpdateMPIN(intentOtp,true);
                    }

                    @Override
                    public void onAuthenticationError(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                //dialog.setCancelable(true);
                //dialog.dismiss();  // Dismiss the dialog
                Intent intent = new Intent(context, NewDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);// Optionally close the activity
                dialog.dismiss();
                return true;
            }
            return false;
        });
        dialog.show();
    }
    private void AddUpdateMPIN(String intentOtp,boolean status) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        MPINReq request =new MPINReq(RSAEncryption.rsaEncrypt(intentOtp),status);
        Call<GetClientRes> call = apiInterfaceWyh.AddUpdateMPIN( SharedPref.getAuthToken(),request);

        call.enqueue(new Callback<GetClientRes>() {
            @Override
            public void onResponse(Call<GetClientRes> call, Response<GetClientRes> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getStatus()) {
                    SharedPref.putHasBiometric(status);
                    SharedPref.setPolicyMPin(intentOtp);
                   /* Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, PolicyLoginMPIN.class);
                    startActivity(intent);*/
                    showLoginMPINDialog();

                } else {
                    // Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetClientRes> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                //Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
    void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
