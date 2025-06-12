package com.wyh.happyyousdk.login;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.isValidMobNo;
import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nekolaboratory.EmulatorDetector;
import com.scottyab.rootbeer.RootBeer;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityMobileNumber2Binding;
import com.wyh.happyyousdk.model.request.encrDecr.EncryptionRequest;
import com.wyh.happyyousdk.model.request.login.GetOtpRequest;
import com.wyh.happyyousdk.model.response.encrDecr.EncryptionResponse;
import com.wyh.happyyousdk.model.response.login.GetOtpResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.MmpSDK;
import com.wyh.happyyousdk.utils.RootCheck;
import com.wyh.happyyousdk.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileNumberActivity extends AppCompatActivity {

    ActivityMobileNumber2Binding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    String referrerUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init(){
        try{
            binding = DataBindingUtil.setContentView(this, R.layout.activity_mobile_number2);
            context = this;
            SharedPref.init(context);

            apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

            JSONObject customObj = new JSONObject();
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
            setHighlightedOffer(binding.mobileTv2,"Enjoy 1 Year Of Free Tele-Consultation For","1 Year Of Free Tele-Consultation");
            setHighlightedOffer(binding.mobileTv4,"10,000 Points Worth Rewards Over Time!","10,000 Points Worth Rewards");
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
                        CommonUtils.hideKeyboard(MobileNumberActivity.this);
                    }
                }
            });



            binding.btnSendOTP.setOnClickListener(view ->
            {
                if (binding.edtMobileNumber.getText().toString().equals("") ||
                        binding.edtMobileNumber.getText().toString().length() < 10 || !TextUtils.isDigitsOnly(binding.edtMobileNumber.getText())
                        || !isValidMobNo(binding.edtMobileNumber.getText().toString())) {
                    binding.edtMobileNumber.requestFocus();
                    binding.edtMobileNumber.setError("Please enter valid mobile number");
                    showKeyboard(MobileNumberActivity.this);
                } else {
                    encryptRSA(binding.edtMobileNumber.getText().toString());
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RootBeer rootBeer = new RootBeer(context);
        if (com.wyh.happyyousdk.SDKConstants.environment != "debug") {
            if (EmulatorDetector.isEmulator(getApplicationContext())) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Emulator");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("This application is not allowed to run on an emulator.");
                alertDialog.setPositiveButton("Exit", (dialog, which) -> {
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().apply();
                    SharedPref.clearSharedPref();
                    finishAffinity();
                });
                alertDialog.show();
            } else if (rootBeer.isRooted() || rootBeer.isRootedWithBusyBoxCheck() || rootBeer.checkSuExists()) {
                //if (new CheckRootedDevice(this).isRTWithoutBBCheck()) {
                // device is rooted
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Root device");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("This application is not allowed on root device.");
                alertDialog.setPositiveButton("Exit", (dialog, which) -> {
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().apply();
                    SharedPref.clearSharedPref();
                    finishAffinity();
                });
                alertDialog.show();
            } else if (RootCheck.isDeviceRooted()) {
                //if (new CheckRootedDevice(this).isRTWithoutBBCheck()) {
                // device is rooted
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Root device");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("This application is not allowed on root device.");
                alertDialog.setPositiveButton("Exit", (dialog, which) -> {
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().apply();
                    SharedPref.clearSharedPref();
                    finishAffinity();
                });
                alertDialog.show();
            }
        }
    }

    public void encryptRSA(String valueToEncrypt) {
        try{
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
                        Toast.makeText(MobileNumberActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EncryptionResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    Toast.makeText(MobileNumberActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            CommonUtils.dismissDialoge();
            e.printStackTrace();
        }

    }

    public void setHighlightedOffer(TextView view, String fullText, String highLighted) {
        // Create the text for the TextView
        String text = fullText;
        SpannableString spannableString = new SpannableString(fullText);

        int start = fullText.indexOf(highLighted);
        if (start >= 0) {
            int end = start + highLighted.length();

            spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        view.setText(spannableString);
        view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        view.setGravity(Gravity.CENTER);
    }

    public void sendOTP(String encryptedMobileNo, String decryptMobileNo) {
        try{
            CommonUtils.showProgressDialige(this);
            GetOtpRequest getOtpRequest = new GetOtpRequest(encryptedMobileNo);
            Call<GetOtpResponse> call = apiInterfaceWyh.getOTP(getOtpRequest);
            Log.d("login ", new Gson().toJson(getOtpRequest));
            Log.d("mobile number request: ",new Gson().toJson(getOtpRequest));
            Log.d("mobile number request: ",new Gson().toJson(call.request().url()));
            call.enqueue(new Callback<GetOtpResponse>() {
                @Override
                public void onResponse(Call<GetOtpResponse> call, Response<GetOtpResponse> response) {
                    CommonUtils.dismissDialoge();
                    Log.d("mobile number res code: ",new Gson().toJson(response.code()));
                    Log.d("mobile number res body: ",new Gson().toJson(response.body()));
                    if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.send_otp_success));
                        Toast.makeText(context, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, OtpActivity.class);
                        intent.putExtra("isAlreadyRegistered", response.body().getIsAlreadyRegistered());
                        intent.putExtra("mobileNo", encryptedMobileNo);
                        intent.putExtra("decryptMobileNo", decryptMobileNo);
                        startActivity(intent);
//                    finish();
                    } else {
                        Analytics.logEvent(context, "", "A_114_"+response.code()+"_"+encryptedMobileNo);
                        Toast.makeText(MobileNumberActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetOtpResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    Analytics.logEvent(context, "", "A_114_Failed_"+encryptedMobileNo);
                    Toast.makeText(MobileNumberActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            CommonUtils.dismissDialoge();
            e.printStackTrace();
        }

    }
}