package com.wyh.happyyousdk.login;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nekolaboratory.EmulatorDetector;
import com.scottyab.rootbeer.RootBeer;
import com.trackier.sdk.TrackierSDK;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.APIEncryption.VerifyResponse;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.WelcomeActivity;
import com.wyh.happyyousdk.corporateAccount.ActivityCorporateAccountLink;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.databinding.ActivityOtp1Binding;
import com.wyh.happyyousdk.model.request.registration.RegistrationRequest;
import com.wyh.happyyousdk.utils.MmpSDK;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.model.request.encrDecr.EncryptionRequest;
import com.wyh.happyyousdk.model.request.login.GetOtpRequest;
import com.wyh.happyyousdk.model.request.login.VerifyOtpRequest;
import com.wyh.happyyousdk.model.response.encrDecr.EncryptionResponse;
import com.wyh.happyyousdk.model.response.login.GetOtpResponse;
import com.wyh.happyyousdk.model.response.login.VerifyOtpResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.RootCheck;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SmsBroadcastReceiver;
import com.wyhsdk.sharedPreferences.SharedPreference;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import in.aabhasjindal.otptextview.OTPListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    ActivityOtp1Binding binding;
    Context context;
    SmsBroadcastReceiver smsBroadcastReceiver;
    private static int REQ_USER_CONSENT = 123165;
    String mobileNo, decryptMobileNo;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;

    Boolean isKgiPolicy = false;

    Boolean isRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(" TrackierSDK.getTrackierId()",  TrackierSDK.getTrackierId());
        init();
    }

    private void init() {
        try {

            binding = DataBindingUtil.setContentView(this, R.layout.activity_otp1);
            context = this;
            SharedPref.init(context);
            SharedPreference.init(context);

            apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
            mobileNo = getIntent().getStringExtra("mobileNo");
            decryptMobileNo = getIntent().getStringExtra("decryptMobileNo");

            Log.d("AuthToken", "Intent Mobile - "+mobileNo);



            progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            setHighlightedOffer(binding.otpTv2,"Save Big! Up To ₹10,000 On Health Services With Top Brand Partners","₹10,000 On Health Services");
            setHighlightedOffer(binding.otpTv4,"Exclusive Monthly Webinars & Hero Offers, Just For You!","Exclusive Monthly Webinars & Hero Offers");



            //binding.otpTv2.setText(Html.fromHtml("Save Big! Up to ₹10,000 on Health Services with Top Brand Partners"));
            //setHighLightedText(binding.otpTv2,"Save Big! Up to ₹10,000 on Health Services< with Top Brand Partners","₹10,000 on Health Services");

            binding.tvResendOTP.setPaintFlags(binding.tvResendOTP.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            binding.tvResendOTP.setOnClickListener(view -> {
                sendOTP(mobileNo);
            });


            /*binding.llBack.setOnClickListener(view -> {
                onBackPressed();
            });*/

            binding.edtOTP.setOtpListener(new OTPListener() {
                @Override
                public void onInteractionListener() {

                }

                @Override
                public void onOTPComplete(String otp) {
                    CommonUtils.hideKeyboard(OtpActivity.this);
                    //encryptRSA(otp);
                }
            });

            binding.btnSubmit.setOnClickListener(view -> {
                if (binding.edtOTP.getOTP().length() == 6) {
                    MmpSDK.INSTANCE.logMMPEvent("tXnsvs1eRT","");
                    encryptRSA(binding.edtOTP.getOTP());
                } else {
                    binding.edtOTP.requestFocus();
                    Toast.makeText(context, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                    showKeyboard(OtpActivity.this);
                }
            });

            startSmsUserConsent();

        } catch (Exception e) {
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

    public void sendOTP(String encryptedMobileNo) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetOtpRequest getOtpRequest = new GetOtpRequest(encryptedMobileNo);
        Call<GetOtpResponse> call = apiInterfaceWyh.getOTP(getOtpRequest);
        call.enqueue(new Callback<GetOtpResponse>() {
            @Override
            public void onResponse(Call<GetOtpResponse> call, Response<GetOtpResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.resend_otp_success));
                    Toast.makeText(OtpActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.resend_otp_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetOtpResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.resend_otp_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void encryptRSA(String valueToEncrypt) {
        try {
            CommonUtils.showProgressDialige(this);
            EncryptionRequest encryptionRequest = new EncryptionRequest(new String[]{valueToEncrypt});
            Call<EncryptionResponse> call = apiInterfaceWyh.encryptRSA(encryptionRequest);
            call.enqueue(new Callback<EncryptionResponse>() {
                @Override
                public void onResponse(Call<EncryptionResponse> call, Response<EncryptionResponse> response) {
                    CommonUtils.dismissDialoge();
                    if (response.code() == 200 && response.body() != null && response.body().getData() != null
                            && response.body().getData().get(0).getEncryptedStr() != null) {
                        verifyOTP(mobileNo, response.body().getData().get(0).getEncryptedStr());
                    } else {
                        CommonUtils.dismissDialoge();
                        Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EncryptionResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            CommonUtils.dismissDialoge();
            e.printStackTrace();
        }

    }

    private void verifyOTP(String mobileNo, String otp) {
        try {
            CommonUtils.showProgressDialige(this);
            String deviceModel = Build.BRAND + " " + Build.MODEL;
            String userKey = RSAEncryption.rsaEncrypt(decryptMobileNo + CommonUtils.generateRandonNumber(8));
            String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
            String appVersion = SDKConstants.appVersionName;
            Log.d("AuthToken", "MobileNumber - "+mobileNo+", OTP - "+otp);
            VerifyOtpRequest request = new VerifyOtpRequest(mobileNo.replaceAll("\\s", ""), otp.replaceAll("\\s", ""), deviceModel.replaceAll("\\s", ""), osVersion.replaceAll("\\s", ""), appVersion.replaceAll("\\s", ""), userKey.replaceAll("\\s", ""));
//            Call<VerifyOtpResponse> call = apiInterfaceWyh.verifyOTP(request);
            Log.d("AuthToken", "" + " " + new Gson().toJson(request));
            APIInterface apiInterface = RetrofitHandler.apiInterface();

            apiInterface.VerifyOTPV5(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    CommonUtils.dismissDialoge();
                    Log.d("AuthToken", "VerifyOTPReq " + new Gson().toJson(call.request().url()));
                    Log.d("AuthToken", "VerifyOTPReq " + new Gson().toJson(response.code()));
                    Log.d("AuthToken", "VerifyOTPReq " + new Gson().toJson(response.body()));
                    if (response.code() == 200 && response.isSuccessful()) {
                        try {
                            if (response.body() != null) {
                                JsonParser parser = new JsonParser();
                                String json = response.body().string().replaceAll("\\/", "/");
                                JsonElement mJson = parser.parse(json);
                                JsonElement json2 = parser.parse(json);
                                JSONObject jsonObject = new JSONObject(json.toLowerCase().replaceAll("\\s", ""));
                                Gson gson = new GsonBuilder().serializeNulls().create();
                                VerifyOtpResponse object = gson.fromJson(mJson, VerifyOtpResponse.class);
                                if (object.getSuccess()) {
                                    jsonObject.getJSONObject("data").toString();
                                    isRegistered = object.getData().getIsRegistered();
                                    object.getData().getClientDetails();
                                    VerifyResponse responseData = gson.fromJson(json2, VerifyResponse.class);
                                    JSONObject json1 = new JSONObject();
                                    String client = gson.toJson(responseData.getData().getClientDetails());
                                    String policy = gson.toJson(responseData.getData().getPolicyDetails());
                                    TreeMap<String, Object> clientMap = gson.fromJson(client, TreeMap.class);
                                    TreeMap<String, Object> policyMap = gson.fromJson(policy, TreeMap.class);
                                    String clientSorted = gson.toJson(clientMap);
                                    String policySorted = gson.toJson(policyMap);
                                    JSONObject clientJSON = new JSONObject(clientSorted);
                                    JSONObject policyJSON = new JSONObject(policySorted);
                                    SharedPref.setCorporateRegistered(object.getData().getClientDetails().getCorporateRegistered());
                                    if (responseData.getData().getKgiPolicyDetails() != null && responseData.getData().getKgiPolicyDetails().getUserPolicyDetails() != null && !responseData.getData().getKgiPolicyDetails().getUserPolicyDetails().isEmpty()) {
                                        isKgiPolicy = true;
                                        SharedPref.putKGIPolicyDetails(new Gson().toJson(responseData.getData().getKgiPolicyDetails().getUserPolicyDetails()));
                                        SharedPref.putKGIPolicyVASType(new Gson().toJson(responseData.getData().getKgiPolicyDetails().getUserPolicyDetails().get(0).getVaSCategory()));
                                    } else {
                                        isKgiPolicy = false;
                                    }
                                    json1.put("authtoken", responseData.getData().getAuthToken());
                                    json1.put("clientdetails", clientJSON);
                                    json1.put("crn", responseData.getData().getCrn());
                                    json1.put("isregistered", responseData.getData().getRegistered());
                                    json1.put("policydetails", policyJSON);
                                    json1.put("userkey", responseData.getData().getUserkey());
                                    encryptUsingAESAPI(String.valueOf(json1), object, apiInterface, otp);
                                } else {
                                    Toast.makeText(OtpActivity.this, "Please enter valid otp", Toast.LENGTH_SHORT).show();
                                }

                            }

                        } catch (Exception e) {
                            CommonUtils.dismissDialoge();
                            APILogs.INSTANCE.sendLogs(String.valueOf(call.request().url()), e.getMessage(), String.valueOf(response.code()), OtpActivity.this);
                            e.printStackTrace();
                            Toast.makeText(OtpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            Log.d("data error", e.getMessage());
                        }
                    } else {
                        Toast.makeText(OtpActivity.this, "Please enter valid otp", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("AuthToken", "Exception - "+t.getMessage());
                    APILogs.INSTANCE.sendLogs(String.valueOf(call.request().url()), t.getMessage(), "onFailure", OtpActivity.this);
                    CommonUtils.dismissDialoge();
                }
            });

        } catch (Exception e) {
            CommonUtils.dismissDialoge();//
            e.printStackTrace();
        }
    }

    private void encryptUsingAESAPI(String json, VerifyOtpResponse object, APIInterface apiInterface, String otp) {
        //String key = new AESEncryption().encryptMsg(String.valueOf(json1));

        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.verify_otp_success));

        if (object.getMsg().contains("Invalid")) {
            Toast.makeText(OtpActivity.this, object.getMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (object.getMsg().contains("Expired")) {
            Toast.makeText(OtpActivity.this, object.getMsg(), Toast.LENGTH_SHORT).show();
            return;
        }

        EncryptionRequest request = new EncryptionRequest(new String[]{json});
        Call<EncryptionResponse> call = apiInterface.encryptAES(request);
        Log.d("AuthToken", "key Req: " + new Gson().toJson(call.request().url()));
        call.enqueue(new Callback<EncryptionResponse>() {
            @Override
            public void onResponse(Call<EncryptionResponse> call, Response<EncryptionResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    String key = response.body().getData().get(0).getEncryptedStr();
                    Log.d("AuthToken", "key Res: " + new Gson().toJson(key));
                    decryptUsingAESAPI(key, object, apiInterface, otp);
                }
            }

            @Override
            public void onFailure(Call<EncryptionResponse> call, Throwable t) {
                Log.d("data error", t.getMessage());
            }
        });

    }

    public void setHighlightedOffer(TextView view,String fullText,String highLighted) {
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

    private void decryptUsingAESAPI(String key, VerifyOtpResponse object, APIInterface apiInterface, String otp) {
        Log.d("json", "object key: " + new Gson().toJson(object));
        EncryptionRequest request = new EncryptionRequest(new String[]{key, String.valueOf(object.getUserkey())});
        Call<EncryptionResponse> call = apiInterface.decryptAES(request);
        String url = RetrofitHandler.BASE_URL + "EncryptDecrypt/AESDecrypt";
        ApiClientWyh.postRequest(url, new Gson().toJson(request), new okhttp3.Callback() {

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
                Log.d("json", "object key: " + new Gson().toJson(response.body().toString()));
                Log.d("json", "object key: " + new Gson().toJson(response.code()));
                try {
                    if (response.code() == 200 && response.body() != null) {
                        String res = response.body().toString();
                        JsonParser parser = new JsonParser();
                        String json = response.body().string().replaceAll("\\/", "/");
                        JsonElement mJson = parser.parse(json);
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        EncryptionResponse resConvertObject = gson.fromJson(mJson, EncryptionResponse.class);
                        String decResp = resConvertObject.getData().get(0).getDecryptedStr().replaceAll("\\\\+", "");
                        String apiDecResp = resConvertObject.getData().get(1).getDecryptedStr();
                        try {
                            JSONObject jsonObject = new JSONObject(apiDecResp);
                            JSONObject jsonObjectData = new JSONObject(decResp);
                            String apiKey = jsonObject.getString("userkey");
                            String myKey = jsonObjectData.getString("userkey");

                            if (apiKey.equalsIgnoreCase(myKey)) {
                                SharedPref.putCurrentTimeStamp(object.getData().getAuthTokenIssuedOn());
                                SharedPref.putCurrentTimeStampExpires(object.getData().getAuthTokenExpiresOn());
                                SharedPref.putEncryptedMobileNo(mobileNo);
                                if (!isRegistered) {
                                    SharedPref.putNewUser(true);
                                    if (object.getData().getPolicyDetails().getSuccess()) {
                                        String policyDetails = new Gson().toJson(object.getData().getPolicyDetails());
                                        SharedPref.putPolicyDetails(policyDetails);
                                    }
                                    registerUser(otp);
                                    SharedPref.putIsUserNameUpdated(false);
                                } else {
                                    Log.d("AuthToken", "Otp Response" + new Gson().toJson(object));
                                    SharedPref.putAuthToken("Bearer " + object.getData().getAuthToken());
                                    SharedPreference.init(context);
                                    SharedPreference.putAuthToken("Bearer " + object.getData().getAuthToken());
                                    SharedPref.putEmail(object.getData().getClientDetails().getEmail());
                                    //SharedPref.putMobileNo(object.getData().getClientDetails().getMobile());
                                    SharedPref.putMobileNo(RSAEncryption.rsaEncrypt(decryptMobileNo));
                                    SharedPref.putDecryptMobileNo(decryptMobileNo);
                                    SharedPref.putUserName(object.getData().getClientDetails().getName());
                                    SharedPref.putDOB(object.getData().getClientDetails().getDob());
                                    SharedPref.putGender(object.getData().getClientDetails().getGender());
                                    SharedPref.putRegistrationGender(object.getData().getClientDetails().getGender());
                                    SharedPref.putProfilePicPath(object.getData().getClientDetails().getProfilePicPath());
                                    SharedPref.putUuid(object.getData().getCrn());
                                    SharedPreference.putUuid(object.getData().getCrn());
                                    SharedPreference.putAuthToken("Bearer " + object.getData().getAuthToken());
                                    SharedPref.putIsLoggedIn(true);
                                    SharedPref.putNewUser(false);
                                    SharedPref.putIsUserNameUpdated(true);
                                    SharedPref.setSpinWheelStatus(false);
                                   /* Intent intent = new Intent(context, WelcomeActivity.class);
                                    startActivity(intent);
                                    finish();*/
                                    Intent intent;
                                   // intent = new Intent(context, ActivityCorporateAccountLink.class);
                                    if (SharedPref.getCorporateRegistered().isEmpty()) {
                                        intent = new Intent(context, ActivityCorporateAccountLink.class);
                                    }
                                    else {
                                        intent = new Intent(context, WelcomeActivity.class);
                                    }
                                    intent.putExtra("isCameFromSpinWheel",true);
                                    MmpSDK.INSTANCE.logMMPEvent("7c0luC0WRX",object.getData().getCrn());
                                   // Intent intent = new Intent(context, WelcomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                Log.d("isKgiPolicy 1", "" + isKgiPolicy);

                            } else {
                                Toast.makeText(OtpActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("json catch error", e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    Log.d("data error", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException t) {
                Log.d("json data error", t.getMessage());
            }
        });
    }


    private void registerUser(String otp) {
       /* if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();*/
        String deviceModel = Build.BRAND + " " + Build.MODEL;
        String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
        String appVersion = SDKConstants.appVersionName;
        String refferealCode = SharedPref.getReferralCode();
        //RegistrationRequest request = new RegistrationRequest(SharedPref.getEncryptedMobileNo(), otp, name, dobStr, emailId, deviceModel, osVersion, appVersion);

        RegistrationRequest request = new RegistrationRequest(SharedPref.getEncryptedMobileNo(), otp, "", "", "", deviceModel, osVersion, appVersion, SharedPref.getReferralCode(), TrackierSDK.getTrackierId()+"_Android");
        Call<VerifyOtpResponse> call = apiInterfaceWyh.registerUserV4(request);
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
                        SharedPref.putUserName(response.body().getData().getClientDetails().getName());
                        SharedPref.putDOB(response.body().getData().getClientDetails().getDob());
                        SharedPref.putEmail("");
                        SharedPref.putProfileDetailedValued(false);
                        SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                        SharedPref.putDecryptMobileNo(decryptMobileNo);
                        //SharedPref.putMobileNo(response.body().getData().getClientDetails().getMobile());
                        SharedPref.putMobileNo(RSAEncryption.rsaEncrypt(decryptMobileNo));
                        SharedPref.putGender(response.body().getData().getClientDetails().getGender());
                        SharedPref.putProfilePicPath(response.body().getData().getClientDetails().getProfilePicPath());
                        SharedPref.putUuid(response.body().getData().getCrn());
                        SharedPreference.putUuid(response.body().getData().getCrn());
                        SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                        SharedPref.putIsLoggedIn(true);
                        //SharedPref.putGoogleFitStatus(response.body().getIsGoogleFit());
                        //Toast.makeText(OtpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        //Meta Event logging
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", SharedPref.getUuid());
//                        MetaSDK.INSTANCE.logAppEvent(context,"A_user_registered", null);//// Meta Removed 17/03/2025
                        //MmpSDK.INSTANCE.logMMPEvent("5kEM4CJzor",response.body().getData().getCrn());

                        //SharedPref.putGoogleFitStatus(response.body().getIsGoogleFit());
                        //Toast.makeText(OtpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                       /* Intent intent = new Intent(context, WelcomeActivity.class);
                        intent.putExtra("isCameFromRegistration", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();*/
                        Log.e("Corporate",SharedPref.getCorporateRegistered());
                        Intent intent;
                        //intent = new Intent(context, ActivityCorporateAccountLink.class);
                                    if (SharedPref.getCorporateRegistered().isEmpty()) {
                                        intent = new Intent(context, ActivityCorporateAccountLink.class);
                                    }
                                    else {
                                        intent = new Intent(context, WelcomeActivity.class);
                                    }
                        intent.putExtra("isCameFromSpinWheel",true);
                        startActivity(intent);
                        finish();
                        /*String dob = "";
                        String name = "";
                        if (response.body().getData().getClientDetails().getDob() != null) {
                            dob = response.body().getData().getClientDetails().getDob();
                        }
                        if (response.body().getData().getClientDetails().getName() != null) {
                            name = response.body().getData().getClientDetails().getName();
                        }
                        callKgi(name, dob, "", intent);*/
                    } else if (response.body().getData().getReferralStatus().equalsIgnoreCase("Wrong referral code")) {
                        Toast.makeText(OtpActivity.this, "Please enter correct referral code", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.register_user_failed));
                    Toast.makeText(OtpActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                Log.d("login re Exception: ", new Gson().toJson(t.getMessage()));
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.register_user_failed));
                Toast.makeText(OtpActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }

                    @Override
                    public void onFailure() {

                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(smsBroadcastReceiver, intentFilter, RECEIVER_EXPORTED);
        } else {
            registerReceiver(smsBroadcastReceiver, intentFilter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerBroadcastReceiver();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsBroadcastReceiver);
    }


    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(aVoid -> {

        }).addOnFailureListener(e -> {

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            binding.edtOTP.setOTP(matcher.group(0));
        }
    }
}