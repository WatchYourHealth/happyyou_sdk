package com.wyh.happyyousdk.login;

import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.hideKeyboard;
import static com.wyh.happyyousdk.utils.CommonUtils.isValidEmail;
import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;
import static com.wyh.happyyousdk.utils.CommonUtils.validateLetters;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;

import com.wyh.happyyousdk.PrivacyPolicyActivity;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.TermsAndConditionActivity;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityRegistration1Binding;
import com.wyh.happyyousdk.databinding.LoginConsentLayoutBinding;
import com.wyh.happyyousdk.model.request.registration.RegistrationRequest;
import com.wyh.happyyousdk.model.response.kgi_policy.GetPolicyDetailsUserPolicyDetail;
import com.wyh.happyyousdk.model.response.login.VerifyOtpResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.MetaSDK;
import com.wyh.happyyousdk.utils.MmpSDK;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistration1Binding binding;
    Context context;
    Calendar todayCalender, mainStartCalender, mainCalenderSlot;
    String dobStr;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    String otp, decryptMobileNo;
    boolean isKgiPolicy;
    VerifyOtpResponse.Data.PolicyDetails policyDetails;
    List<GetPolicyDetailsUserPolicyDetail> kgiPolicyDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration1);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        otp = getIntent().getStringExtra("otp");
        decryptMobileNo = getIntent().getStringExtra("decryptMobileNo");
        isKgiPolicy = getIntent().getBooleanExtra("isKgiPolicy", false);

        todayCalender = Calendar.getInstance();
        mainStartCalender = Calendar.getInstance();
        mainCalenderSlot = Calendar.getInstance();
        SpannableString textCondition = new SpannableString("I accept the Terms of Use and Privacy policy");
        ClickableSpan termsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TermsAndConditionActivity.class);
                startActivity(intent);
            }
        };
        ClickableSpan privacyPolicy = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        };
        textCondition.setSpan(termsAndCondition, 13, 25, 0);
        textCondition.setSpan(privacyPolicy, 30, 44, 0);

        binding.tvTandc.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvTandc.setText(textCondition, TextView.BufferType.SPANNABLE);

        if (!SharedPref.getPolicyDetails().isEmpty()) {
            policyDetails = new Gson().fromJson(SharedPref.getPolicyDetails(), VerifyOtpResponse.Data.PolicyDetails.class);
        }

        if (!SharedPref.getKGIPolicyDetails().isEmpty()) {
            String str = SharedPref.getKGIPolicyDetails();
            if(!str.isEmpty()) {
                Type type = new TypeToken<List<GetPolicyDetailsUserPolicyDetail>>() {
                }.getType();
                kgiPolicyDetails = new Gson().fromJson(str, type);
            }
        }

        if (kgiPolicyDetails != null && !kgiPolicyDetails.isEmpty()) {
            binding.edtName.setText(kgiPolicyDetails.get(0).getInsuredName());
            binding.edtMail.setText(kgiPolicyDetails.get(0).getEmailId());
        }

        if (!policyDetails.getData().isEmpty()) {
            Log.d("login", policyDetails.getData().get(0).getDob());
            dobStr = policyDetails.getData().get(0).getDob();
            String birthDate = formatDateFromString("yyyy-MM-dd", "dd/MM/yyyy", policyDetails.getData().get(0).getDob());
            binding.edtName.setText(policyDetails.getData().get(0).getName());
            binding.edtMail.setText(policyDetails.getData().get(0).getEmail());
            binding.edtDOB.setText(birthDate);
        }

        if (SharedPref.getReferralCode() != "") {
            binding.refeeralCode.setText(SharedPref.getReferralCode());
            binding.refeeralCode.setEnabled(false);
        } else {
            binding.refeeralCode.setEnabled(true);

        }

        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -15);

        binding.edtDOB.setOnClickListener(view -> {
            int year = mainStartCalender.get(Calendar.YEAR);
            int month = mainStartCalender.get(Calendar.MONTH);
            int day = mainStartCalender.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog pickerDialog = new DatePickerDialog(RegistrationActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        String newDay;
                        if (String.valueOf(dayOfMonth).length() == 1) {
                            newDay = "0" + dayOfMonth;
                        } else {
                            newDay = String.valueOf(dayOfMonth);
                        }
                        String newMonth;
                        if (String.valueOf((monthOfYear + 1)).length() == 1) {
                            newMonth = "0" + (monthOfYear + 1);
                        } else {
                            newMonth = String.valueOf((monthOfYear + 1));
                        }
                        binding.edtDOB.setText(newDay + "/" + newMonth + "/" + year1);
                        dobStr = year1 + "-" + newMonth + "-" + newDay;
                        //Toast.makeText(getApplicationContext(), dobStr, Toast.LENGTH_LONG).show();
                        mainStartCalender = Calendar.getInstance();
                        mainStartCalender.set(Calendar.YEAR, year1);
                        mainStartCalender.set(Calendar.MONTH, monthOfYear);
                        mainStartCalender.set(Calendar.DATE, dayOfMonth);
                    }, year, month, day);
            //pickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            pickerDialog.getDatePicker().setMaxDate(prevYear.getTimeInMillis());
            pickerDialog.show();
        });

        binding.btnSubmit.setOnClickListener(view -> {
            hideKeyboard(RegistrationActivity.this);
            if (TextUtils.isEmpty(binding.edtName.getText()) || !validateLetters(binding.edtName.getText().toString())) {
                binding.edtName.requestFocus();
                binding.edtName.setError("Please enter valid name");
                showKeyboard(RegistrationActivity.this);
                return;
            }
            if (dobStr == null) {
                Toast.makeText(this, "Please select your date of birth", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(binding.edtMail.getText()) && !isValidEmail(binding.edtMail.getText().toString())) {
                binding.edtMail.requestFocus();
                binding.edtMail.setError("Please enter valid email");
                showKeyboard(RegistrationActivity.this);
                return;
            }
            if (!binding.checkBox.isChecked()) {
                Toast.makeText(context, "Please accept the terms of use", Toast.LENGTH_SHORT).show();
                return;
            }

//            Toast.makeText(context, "Validated", Toast.LENGTH_SHORT).show();
            registerUser(binding.edtName.getText().toString(), dobStr, binding.edtMail.getText().toString());
        });

    }

    private void registerUser(String name, String dobStr, String emailId) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        String deviceModel = Build.BRAND + " " + Build.MODEL;
        String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
        String appVersion = SDKConstants.appVersionName;
        String refferealCode = SharedPref.getReferralCode();
        //RegistrationRequest request = new RegistrationRequest(SharedPref.getEncryptedMobileNo(), otp, name, dobStr, emailId, deviceModel, osVersion, appVersion);

        RegistrationRequest request = new RegistrationRequest(SharedPref.getEncryptedMobileNo(), otp, name, dobStr, emailId, deviceModel, osVersion, appVersion, binding.refeeralCode.getText().toString(),"");
        Call<VerifyOtpResponse> call = apiInterfaceWyh.registerUser(request);
        Log.d("login request: ", new Gson().toJson(request));


        call.enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("login res code: ", new Gson().toJson(response.code()));
                Log.d("login res body: ", new Gson().toJson(response.body()));
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    if (response.body().getData().getReferralStatus().equals("")) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.register_user_success));

                        SharedPref.putCurrentTimeStamp(response.body().getData().getAuthTokenIssuedOn());
                        SharedPref.putCurrentTimeStampExpires(response.body().getData().getAuthTokenExpiresOn());
                        SharedPref.putUserName(name.trim());
                        SharedPref.putDOB(response.body().getData().getClientDetails().getDob());
                        SharedPref.putEmail(emailId);
                        SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                        //SharedPref.putKgiAuthToken("Bearer " + response.body().getData().getAuthToken());
                        SharedPref.putDecryptMobileNo(decryptMobileNo);
                        // SharedPref.putMobileNo(response.body().getData().getClientDetails().getMobile());
                        SharedPref.putMobileNo(RSAEncryption.rsaEncrypt(decryptMobileNo));
                        SharedPref.putGender(response.body().getData().getClientDetails().getGender());
                        SharedPref.putProfilePicPath(response.body().getData().getClientDetails().getProfilePicPath());
                        SharedPref.putUuid(response.body().getData().getCrn());
                        SharedPreference.putUuid(response.body().getData().getCrn());
                        SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                        SharedPref.putIsLoggedIn(true);
                        //SharedPref.putGoogleFitStatus(response.body().getIsGoogleFit());
//                        MetaSDK.INSTANCE.logAppEvent(context,"User Registration successful", null);//// Meta Removed 17/03/2025
                        MmpSDK.INSTANCE.logMMPEvent("5kEM4CJzor",response.body().getData().getCrn());
                        Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, NewDashboardActivity.class);
                        intent.putExtra("isCameFromRegistration", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        Log.d("isKgiPolicy 2", "" + isKgiPolicy);

                        if (isKgiPolicy) {
                            registerKGIUser(SharedPref.getEncryptedMobileNo(),
                                    name,
                                    dobStr,
                                    emailId,
                                    intent);
                        } else {
                            startActivity(intent);
                            finish();
                        }


                    } else if (response.body().getData().getReferralStatus().equalsIgnoreCase("Wrong referral code")) {
                        Toast.makeText(RegistrationActivity.this, "Please enter correct referral code", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.register_user_failed));
                    Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                Log.d("login re Exception: ", new Gson().toJson(t.getMessage()));
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.register_user_failed));
                Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showConsentLayout() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        LoginConsentLayoutBinding bindingConsentLayout = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.login_consent_layout, null, false);
        alertBuilder.setView(bindingConsentLayout.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        SpannableString textCondition = new SpannableString("I accept the Terms of Use and Privacy policy");
        ClickableSpan termsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TermsAndConditionActivity.class);
                startActivity(intent);
            }
        };
        ClickableSpan privacyPolicy = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        };
        textCondition.setSpan(termsAndCondition, 13, 25, 0);
        textCondition.setSpan(privacyPolicy, 30, 44, 0);

        bindingConsentLayout.tvTandc.setMovementMethod(LinkMovementMethod.getInstance());
        bindingConsentLayout.tvTandc.setText(textCondition, TextView.BufferType.SPANNABLE);

        SpannableString clickableContent = new SpannableString("PLEASE CLICK HERE TO READ MORE ABOUT OUR TERMS OF USE");
        clickableContent.setSpan(new UnderlineSpan(), 0, clickableContent.length(), 0);
        bindingConsentLayout.tvMoreContent.setText(clickableContent);

        bindingConsentLayout.tvContent.loadData(getResources().getString(R.string.tandc_consent), "text/html", "utf-8");
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bindingConsentLayout.tvContent.setText(Html.fromHtml(getString(R.string.tandc_consent), FROM_HTML_MODE_LEGACY));
        } else {
            bindingConsentLayout.tvContent.setText(Html.fromHtml(getString(R.string.tandc_consent)));
        }*/

        bindingConsentLayout.btnOK.setOnClickListener(v -> {
            if (bindingConsentLayout.checkBox.isChecked()) {
                alertDialog.dismiss();
                registerUser(binding.edtName.getText().toString(), dobStr, binding.edtMail.getText().toString());
            } else {
                Toast.makeText(context, "Please accept the Terms of Use", Toast.LENGTH_SHORT).show();
            }
        });
        bindingConsentLayout.btnNo.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        bindingConsentLayout.tvMoreContent.setOnClickListener(v -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, TermsAndConditionActivity.class);
            startActivity(intent);
        });

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.9f), (int) (displayRectangle.height() * 0.8f));
    }


    private void registerKGIUser(String mobileNumber, String kgiName, String kgiDob, String kgiEmailId, Intent intent) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        String deviceModel = Build.BRAND + " " + Build.MODEL;
        String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
        String appVersion = SDKConstants.appVersionName;
        ApiInterfaceWyh kgiInterface = RetrofitHandler.kjiInterfaceJava();
        RegistrationRequest request = new RegistrationRequest(mobileNumber, kgiName, kgiDob, kgiEmailId, deviceModel, osVersion, appVersion, "HappyYou");
        Call<VerifyOtpResponse> call = kgiInterface.kgiRegisterUser(request);
        Log.d("login request: kgi", new Gson().toJson(request));
        Log.d("login request: kgi", new Gson().toJson(call.request().url()));
        Log.d("login request: kgi", new Gson().toJson(request));


        call.enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("login res code: kgi", new Gson().toJson(response.code()));
                Log.d("login res body: kgi", new Gson().toJson(response.body()));
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.register_kgi_user_success));
                    SharedPref.putKgiAuthToken("Bearer " + response.body().getData().getAuthToken());
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.register_kgi_user_failed));
                    Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }

                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                Log.d("login re Exception: kgi", new Gson().toJson(t.getMessage()));
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.register_kgi_user_failed));
                Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
    }

}