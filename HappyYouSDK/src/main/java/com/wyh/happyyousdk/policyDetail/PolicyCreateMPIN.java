package com.wyh.happyyousdk.policyDetail;

import static com.wyh.happyyousdk.policyDetail.helper.FingerprintHelper.isFingerprintAvailable;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.wyh.happyyousdk.databinding.ActvityCreateMpinBinding;
import com.wyh.happyyousdk.model.GetClientRes;
import com.wyh.happyyousdk.model.MPINReq;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyCreateMPIN extends AppCompatActivity {
    ActvityCreateMpinBinding binding;
    Context context;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    String clientID;
    String policyNumber = "";
    String comingFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.actvity_create_mpin);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        apiInterfaceWyh = RetrofitHandler.apiInterface();

        clientID = getIntent().getStringExtra("clientID");
        policyNumber = getIntent().getStringExtra("policyNo");
        comingFrom = getIntent().getStringExtra("comingFrom");

        showCustomDialog();
        /*binding.imSubmit.setOnClickListener(view -> {
            if (binding.edtSetMPIN.getOtp().length()!=4)
            {
                Toast.makeText(context, "Please enter Enter  MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.edtOTP.getOtp().length()!=4)
            {
                Toast.makeText(context, "Please enter Re-enter  MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!binding.edtSetMPIN.getOtp().equals(binding.edtOTP.getOtp())){
                Toast.makeText(context, "Please enter Re-enter  MPIN", Toast.LENGTH_SHORT).show();
            }
             //AddUpateMPIN(RSAEncryption.rsaEncrypt(binding.edtSetMPIN.getOTP()));
            Intent intent = new Intent(context, PolicyCreateFingerPrint.class);
            intent.putExtra("intentOtp",binding.edtSetMPIN.getOTP());
            intent.putExtra("intentOtpEncrpt",RSAEncryption.rsaEncrypt(binding.edtSetMPIN.getOTP()));
            startActivity(intent);
            finish();
        });*/

    }


    private void showCustomDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_create_mpin, null);

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView imSubmit = dialogView.findViewById(R.id.imSubmit);
        EditText edtSetMPIN = dialogView.findViewById(R.id.edtSetMPIN);
        EditText edtOTP = dialogView.findViewById(R.id.edtOTP);
        imSubmit.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_SUBMIT_MPIN",context);
            if (edtSetMPIN.getText() == null || edtSetMPIN.getText().toString().length()!=4)
            {
                Toast.makeText(context, "Please enter valid MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edtOTP.getText() == null || edtOTP.getText().toString().length()!=4)
            {
                Toast.makeText(context, "Please enter Re-enter MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!edtSetMPIN.getText().toString().equals(edtOTP.getText().toString())){
                Toast.makeText(context, "The MPINs do not match. Please enter the correct MPIN", Toast.LENGTH_SHORT).show();
            }
             //AddUpateMPIN(RSAEncryption.rsaEncrypt(binding.edtSetMPIN.getOTP()));
            if (isFingerprintAvailable(context))
            {
                Intent intent = new Intent(context, PolicyCreateFingerPrint.class);
                intent.putExtra("intentOtp",edtSetMPIN.getText().toString());
                intent.putExtra("intentOtpEncrpt",RSAEncryption.rsaEncrypt(edtSetMPIN.getText().toString()));
                if(clientID != null && policyNumber != null && comingFrom != null) {
                    intent.putExtra("clientID", clientID);
                    intent.putExtra("policyNo", policyNumber);
                    intent.putExtra("comingFrom", comingFrom);
                }
                startActivity(intent);
            }
            else {
                AddUpdateMPIN(edtSetMPIN.getText().toString(),false);
            }

            dialog.dismiss();
            dialog.cancel();

            finish();
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
}
