package com.wyh.happyyousdk.policyDetail;

import static com.wyh.happyyousdk.policyDetail.helper.FingerprintHelper.isFingerprintAvailable;
import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.otpview.OTPTextView;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.WebActivity;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityPolicyListBinding;
import com.wyh.happyyousdk.model.GetClientRes;
import com.wyh.happyyousdk.model.GetOTPReq;
import com.wyh.happyyousdk.model.MPINReq;
import com.wyh.happyyousdk.model.PolicyListReq;
import com.wyh.happyyousdk.model.PolicyListResp;
import com.wyh.happyyousdk.model.VerifyOTP;
import com.wyh.happyyousdk.policyDetail.adapter.PolicyListAdapter;
import com.wyh.happyyousdk.policyDetail.helper.FingerprintHelper;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;
import java.util.List;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyListActivity extends AppCompatActivity {
    ActivityPolicyListBinding binding;
    Context context;
    PolicyListResp policyDetails;
    int positionPolicy = 0;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    AlertDialog dialog;
    String autoPayURL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_policy_list);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);


        apiInterfaceWyh = RetrofitHandler.apiInterface();
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        binding.imAddPolicy.setOnClickListener(view -> {
            Intent intent = new Intent(context, PolicySearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });
        binding.imBack.setOnClickListener(view -> {

        onBackPressed();
        });
        String intent=getIntent().getStringExtra("intent");
        if (intent.equals("Dashboard"))
        {
            if (!SharedPref.getPolicyMPin().equals(""))
            {
                showCustomDialog();
            }
            else {
                Intent intentnew = new Intent(context, PolicyCreateMPIN.class);
                startActivity(intentnew);
                intentnew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();

            }
        }
        else {
            //getPolicy();
        }
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
            APILogs.INSTANCE.activityTracker("Android_POLICY_OTP",context);
            sendOTp();
        });
    }

    private void openWebView(String clickButton){
        Intent intent = new Intent(context, WebActivity.class);
        if(clickButton.equalsIgnoreCase("autoPay")){
            APILogs.INSTANCE.activityTracker("Android_POLICY_AUTO_PAY",context);
            intent.putExtra("Url", autoPayURL);
        }else  if (clickButton.equalsIgnoreCase("Product Brochure"))
        {
            intent.putExtra("Url", "https://www.kotaklife.com/how-do-i/get-policy-brochure");
        }
        else {
            APILogs.INSTANCE.activityTracker("Android_POLICY_RENEW_POLICY",context);
            if (SharedPref.getPolicyRenewableURL() != null && !SharedPref.getPolicyRenewableURL().isEmpty()) {
                intent.putExtra("Url", SharedPref.getPolicyRenewableURL());
            } else {
                Toast.makeText(context, "Please try after sometime.", Toast.LENGTH_SHORT).show();
            }
        }
        intent.putExtra("comingFrom","PolicyListActivity");
        context.startActivity(intent);

    }

    private void setRvForPolicyDetails(List<PolicyListResp.Content> policyDetailsList) {
        Log.e("Size",policyDetailsList.size()+"");
        PolicyListAdapter policyDetailsAdapter = new PolicyListAdapter(context, policyDetailsList,
                new PolicyListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String clickButton) {
                        openWebView(clickButton);
                    }
                });
        LinearLayoutManager policyDetailsLinearLayoutManager = new LinearLayoutManager(context);
        policyDetailsLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rlPolicyList.setLayoutManager(policyDetailsLinearLayoutManager);
        binding.rlPolicyList.setAdapter(policyDetailsAdapter);


       /* LinearSnapHelper policyDetailsLinearSnapHelper = new SnapHelperOneByOne();
        binding.rlPolicyList.setOnFlingListener(null);
        policyDetailsLinearSnapHelper.attachToRecyclerView(binding.rlPolicyList);*/

        LinearLayoutManager policyDetailsLinearLayoutManager1 = new LinearLayoutManager(context);
        policyDetailsLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);

        binding.imBack.setOnClickListener(view -> {

            onBackPressed();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getPolicy() {
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        try {
            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.e("ClientID",SharedPref.getClientId());
        PolicyListReq request = new PolicyListReq("0","999",SharedPref.getClientId());
        Call<PolicyListResp> call = apiInterfaceWyh.GetPolicyDetailsPROD(SharedPref.getAuthToken(), request);
        Log.d("policy req", new Gson().toJson(request));
        call.enqueue(new Callback<PolicyListResp>() {
            @Override
            public void onResponse(Call<PolicyListResp> call, Response<PolicyListResp> response) {
                try {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Log.d("policy res", new Gson().toJson(response.body()));
                    /*Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_policy_success));*/
                    policyDetails = response.body();
                    if (policyDetails.getData().getPolicy().getContent().isEmpty()) {
                        /*binding.llNoPolicyFound.setVisibility(View.VISIBLE);
                        binding.includeToolbar.tvBack.setTextColor(getColor(R.color.black));
                        binding.includeToolbar.ivBack.setColorFilter(getColor(R.color.black));*/
                    } else if (policyDetails.getData().getPolicy().getContent().size() > 0) {
                        setRvForPolicyDetails(policyDetails.getData().getPolicy().getContent());
                        if(policyDetails.getData().getPolicy().getPaymentrenewalurl() != null)
                            autoPayURL = policyDetails.getData().getPolicy().getAutodebiturl();
                        if(policyDetails.getData().getPolicy().getPaymentrenewalurl() != null) {
                            SharedPref.setPolicyRenewableURL(policyDetails.getData().getPolicy().getPaymentrenewalurl());
                        }
                        SharedPref.setPolicyContent(new Gson().toJson(policyDetails.getData().getPolicy().getContent()));
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_policy_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PolicyListResp> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, NewDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    private void showCustomDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dailog_login_by_mpin, null);

        // Build the dialog
        dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        if (window != null) {
            window.setDimAmount(0.9f); // Set dim amount (0.0f = no dim, 1.0f = fully dimmed)
        }
        TextView imSubmitMPIN=dialogView.findViewById(R.id.imSubmitMPIN);
        ImageView  imFingerPrint=dialogView.findViewById(R.id.imFingerPrint);
        TextView tvOr=dialogView.findViewById(R.id.tvOr);
        TextView  tvForget=dialogView.findViewById(R.id.tvForget);

        if (isFingerprintAvailable(this)) {
            imFingerPrint.setVisibility(View.VISIBLE);
        } else {
            // Not supported or no fingerprints enrolled

            imFingerPrint.setVisibility(View.GONE);
            tvOr.setVisibility(View.GONE);
        }
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
        edtOTP.requestFocus();
        edtOTP.requestFocusOTP();
        EditText hiddenOTP = dialogView.findViewById(R.id.hiddenOTP);
        imSubmitMPIN.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_SUBMIT_MPIN",context);
            if (edtOTP.getOtp() == null || !edtOTP.getOtp().toString().equals(SharedPref.getPolicyMPin())){
                Toast.makeText(context, "Please enter Valid MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.dismiss();
            getPolicy();
        });

        tvForget.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_FORGET_MPIN",context);
            sendOTp();
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
        imFingerPrint.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_LOGIN_WITH_BIOMETRIC",context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                FingerprintHelper.authenticateFingerprint(PolicyListActivity.this, new FingerprintHelper.FingerprintCallback() {
                    @Override
                    public void onAuthenticationSuccess() {
                        dialog.dismiss();
                        getPolicy();
                    }

                    @Override
                    public void onAuthenticationError(String errorMessage) {
                        Toast.makeText(PolicyListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
       /* hiddenOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Mask OTP digits
                StringBuilder masked = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    masked.append("•");  // Using bullet character to mask digits
                }

                edtOTP.setOTP(masked.toString());
            }
        });*/
        dialog.show();
    }
    private void sendOTp( ) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
         Log.e("Mobile",RSAEncryption.callDecryptionMethod(SharedPref.getEncryptedMobileNo()));
        GetOTPReq request = new GetOTPReq("1","","",SharedPref.getEncryptedMobileNo(),15);
        Call<GetClientRes> call = apiInterfaceWyh.GetPolicyOTP( SharedPref.getAuthToken(),request);

        call.enqueue(new Callback<GetClientRes>() {
            @Override
            public void onResponse(Call<GetClientRes> call, Response<GetClientRes> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getStatus()) {
                    binding.tvMessage.setText(response.body().getMsg());
                    binding.imTransparent1.setVisibility(View.VISIBLE);
                    binding.getStartedLayout.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
                    /*Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_success));*/
                    binding.tvMessage.setText(response.body().getMsg());
                    binding.imTransparent1.setVisibility(View.GONE);
                    binding.getStartedLayout.setVisibility(View.GONE);
                    binding.edtOTP.setOTP("");
                    showCustomCreateMPINDialog();


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
    private void showCustomCreateMPINDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_create_mpin, null);

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        if (window != null) {
            window.setDimAmount(0.9f);// Set dim amount (0.0f = no dim, 1.0f = fully dimmed)
        }
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
                Toast.makeText(context, "Please enter valid MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!edtSetMPIN.getOtp().equals(edtOTP.getOtp())){
                Toast.makeText(context, "The MPINs do not match. Please enter the correct MPIN", Toast.LENGTH_SHORT).show();
                return;
            }
            //AddUpateMPIN(RSAEncryption.rsaEncrypt(binding.edtSetMPIN.getOTP()));
            if (SharedPref.getHasBiometric())
            {
                AddUpdateMPIN(edtOTP.getOtp(),SharedPref.getHasBiometric());
            }
            else {
                if (isFingerprintAvailable(context)){
                    showCustomFingerDialog(edtOTP.getOtp());
                }
                else {
                    AddUpdateMPIN(edtOTP.getOtp(),SharedPref.getHasBiometric());
                }

            }

            //getPolicy();
            dialog.dismiss();
            dialog.cancel();

           // finish();
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
    private void showCustomFingerDialog(String intentOtp) {
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
            dialog.dismiss();
            AddUpdateMPIN(intentOtp,false);
        });
        imFingerPrint.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_POLICY_LOGIN_WITH_BIOMETRIC",context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                FingerprintHelper.authenticateFingerprint(PolicyListActivity.this, new FingerprintHelper.FingerprintCallback() {
                    @Override
                    public void onAuthenticationSuccess() {
                        dialog.dismiss();
                        AddUpdateMPIN(intentOtp,true);
                    }

                    @Override
                    public void onAuthenticationError(String errorMessage) {
                        Toast.makeText(PolicyListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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
                    showCustomDialog();
                } else {
                    // Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
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