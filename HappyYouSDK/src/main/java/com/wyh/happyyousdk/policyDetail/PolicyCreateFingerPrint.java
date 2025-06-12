package com.wyh.happyyousdk.policyDetail;

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

import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivitySetupFingerPrintLoginBinding;
import com.wyh.happyyousdk.model.GetClientRes;
import com.wyh.happyyousdk.model.MPINReq;
import com.wyh.happyyousdk.policyDetail.helper.FingerprintHelper;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyCreateFingerPrint extends AppCompatActivity {
   ActivitySetupFingerPrintLoginBinding binding;
    Context context;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    String intentOtp,intentOtpEncrpt;
    String clientID;
    String policyNumber = "";
    String comingFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup_finger_print_login);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        intentOtp=getIntent().getStringExtra("intentOtp");
        intentOtpEncrpt=getIntent().getStringExtra("intentOtpEncrpt");
        Log.e("intentOtp",intentOtp);
        clientID = getIntent().getStringExtra("clientID");
        policyNumber = getIntent().getStringExtra("policyNo");
        comingFrom = getIntent().getStringExtra("comingFrom");
        showCustomDialog();
       /* binding.tvSkip.setOnClickListener(view -> {
            AddUpateMPIN(intentOtp,false);
        });*/
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
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(context, PolicyLoginMPIN.class);
                    startActivity(intent);*/
                    if(comingFrom != null && comingFrom.equalsIgnoreCase("ViewMore")){
                        Intent intent = new Intent(context, PolicyViewMore.class);
                        intent.putExtra("clientID", clientID);
                        intent.putExtra("policyNo", policyNumber);
                        intent.putExtra("intent", "Dashboard");
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, PolicyListActivity.class);
                        intent.putExtra("intent", "Dashboard");
                        startActivity(intent);
                    }
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

    private void showCustomDialog() {
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
            APILogs.INSTANCE.activityTracker("Android_REGISTER_BIOMETRIC",context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                FingerprintHelper.authenticateFingerprint(PolicyCreateFingerPrint.this, new FingerprintHelper.FingerprintCallback() {
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
    }


