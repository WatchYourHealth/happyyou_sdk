package com.wyh.happyyousdk.corporateAccount;

import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
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
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityCorporateAccountLinkBinding;
import com.wyh.happyyousdk.model.VerifyEmailOTPReq;
import com.wyh.happyyousdk.model.request.GetEmailOTPReq;
import com.wyh.happyyousdk.model.response.GetEmailOTPResp;
import com.wyh.happyyousdk.model.response.VerifyEmailOTPResp;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCorporateAccountAddEdit extends AppCompatActivity {
    ActivityCorporateAccountLinkBinding binding;
    Context context;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    List<GetEmailOTPResp.Data.corporateDetails> corporateDetails = new ArrayList<>();
    String CorporateID="",CorporateName="";
    List<String> corporateName = new ArrayList<>();
    AlertDialog dialog;
    String jsonData="";
    List<StateModel> stateData= new ArrayList<StateModel>();
    String EmailId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_corporate_account_link);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);
        //startTimer(minutes,seconds);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        binding.rlUpperCard.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.tvSendOTP.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_EMAIL_CONTINUE",context);
            if (isValidEmail(binding.etEmailID.getText().toString()))
            {
                //sendOTp();
                GetCorporateDetails();
            }
            else {
                Toast.makeText(context,"Please enter valid Email ID",Toast.LENGTH_SHORT).show();
            }

        });
        binding.tvResendOTP.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_SPIN_WHEEL_CORPORATE_EMAIL_CONTINUE",context);
            if (isValidEmail(binding.etEmailID.getText().toString()))
            {
                corporateDetails.clear();
                corporateName.clear();
                CorporateID="";
                //sendOTp();
                GetCorporateDetails();
            }
            else {
                Toast.makeText(context,"Please enter valid Email ID",Toast.LENGTH_SHORT).show();
            }

        });
        binding.tvNotNow.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_NOT_NOW",context);
            Intent intent = new Intent(context, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });



        binding.tvContinue.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_EMAIL_CONTINUE",context);
            /*if (binding.edtOTP.getOTP().length()==6)
            {
                //verifyOTP();
                showCorporateAlert();
            }

            else {
                binding.edtOTP.requestFocus();
                Toast.makeText(context, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                showKeyboard(this);
            }*/
            Intent intent = new Intent(context, ActivityCorporatePostRegisterForm.class);
            intent.putExtra("jsonData", jsonData);
            intent.putExtra("CorporateID",CorporateID);
            intent.putExtra("CorporateName",CorporateName);
            intent.putExtra("EmailID",EmailId);
            intent.putExtra("stateData",new Gson().toJson(stateData));
            intent.putExtra("corporateDetails",new Gson().toJson(corporateDetails));

            startActivity(intent);
        });

        binding.tvEditEmail.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_OTP_EDIT_EMAIL",context);
            corporateDetails.clear();
            corporateName.clear();
            CorporateID="";
            binding.edtOTP.setOTP("");
            //binding.etEmailID.setText("");
            binding.llSection1.setVisibility(View.VISIBLE);
            binding.etEmailID.setVisibility(View.VISIBLE);
            binding.tvSendOTP.setVisibility(View.VISIBLE);
            binding.llSection2.setVisibility(View.GONE);

        });
        binding.spinnerCorporateName.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                CorporateID= String.valueOf(corporateDetails.get(position).getCorpId());
                CorporateName= String.valueOf(corporateDetails.get(position).getCorporateNames());
                jsonData= corporateDetails.get(position).getQuestionJson();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });
    }
    private void GetCorporateDetails() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        EmailId = binding.etEmailID.getText().toString();
        GetEmailOTPReq request = new GetEmailOTPReq(binding.etEmailID.getText().toString());;

        Call<GetEmailOTPResp> call = apiInterfaceWyh.getCorporateDetails(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetEmailOTPResp> call, @NonNull Response<GetEmailOTPResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess() && !response.body().getData().getClients().isEmpty()) {
                    corporateDetails=response.body().getData().getClients();
                    stateData = response.body().getData().getStateList();
                    setSpinnerData(corporateDetails);
                   /* binding.llSection1.setVisibility(View.GONE);
                    binding.etEmailID.setVisibility(View.GONE);
                    binding.tvSendOTP.setVisibility(View.GONE);
                    binding.llSection2.setVisibility(View.VISIBLE);*/
                    Intent intent = new Intent(context, ActivityCorporatePostRegisterForm.class);
                    intent.putExtra("jsonData", jsonData);
                    intent.putExtra("CorporateID",CorporateID);
                    intent.putExtra("CorporateName",CorporateName);
                    intent.putExtra("EmailID",binding.etEmailID.getText().toString().trim());
                    intent.putExtra("stateData",new Gson().toJson(stateData));
                    intent.putExtra("corporateDetails",new Gson().toJson(corporateDetails));
                    startActivity(intent);
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                } else {
                    /*SharedPref.putCorporateAccountNotFound(true);
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.putExtra("isCameFromRegistration", true);
                    intent.putExtra("isCameFromSpinWheel",true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();*/
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

    private void sendOTp() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetEmailOTPReq request ;
        request= new GetEmailOTPReq(RSAEncryption.rsaEncrypt(binding.etEmailID.getText().toString()));
        Call<GetEmailOTPResp> call = apiInterfaceWyh.getEmailOTP(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetEmailOTPResp> call, @NonNull Response<GetEmailOTPResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()&& !response.body().getData().getClients().isEmpty()) {
                    corporateDetails=response.body().getData().getClients();
                    setSpinnerData(corporateDetails);
                    binding.llSection1.setVisibility(View.GONE);
                    binding.etEmailID.setVisibility(View.GONE);
                    binding.tvSendOTP.setVisibility(View.GONE);
                    binding.llSection2.setVisibility(View.VISIBLE);
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
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
        request=new VerifyEmailOTPReq(RSAEncryption.rsaEncrypt(binding.etEmailID.getText().toString()),CorporateID,RSAEncryption.rsaEncrypt(binding.edtOTP.getOTP()));
        Call<VerifyEmailOTPResp> call = apiInterfaceWyh.verifyEmailOTP(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerifyEmailOTPResp> call, @NonNull Response<VerifyEmailOTPResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    SharedPref.setCorporateRegistered("YES");
                    SharedPref.setCorporateId(response.body().getData().getCorpId()+"");
                    SharedPref.setCorporateName(response.body().getData().getCorpName());
                    SharedPref.setCorporateImage(response.body().getData().getCorpLogo());
                    Intent intent = new Intent(context, ActivityCorporateAccountMain.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    assert response.body() != null;
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
    public void setSpinnerData(List<GetEmailOTPResp.Data.corporateDetails> corporateDetails)
    {
        for (int i=0;i<corporateDetails.size();i++)
        {
            corporateName.add(corporateDetails.get(i).getCorporateNames());
        }
        if (corporateName.size()>1)
        {
            binding.imSpinner.setVisibility(View.VISIBLE);
        }
        else {
            binding.imSpinner.setVisibility(View.INVISIBLE);
        }
        ArrayAdapter<String> spinnerSpinWheelAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, corporateName);
        spinnerSpinWheelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCorporateName.setAdapter(spinnerSpinWheelAdapter);

    }
    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        // Alternative: return email != null && EMAIL_PATTERN.matcher(email).matches();
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
        TextView tvTitle=dialogView.findViewById(R.id.tvTitle);
        Button btnYes=dialogView.findViewById(R.id.btnYes);
        Button btnNo=dialogView.findViewById(R.id.btnNo);
        tvTitle.setText("Are you Sure Do you want to Add\n" + "your Corporate?");
        btnYes.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_CONFIRM_YES",context);
               verifyOTP();
            dismissDialog();
        });
        btnNo.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_CONFIRM_NO",context);
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
}