package com.wyh.happyyousdk;

import static com.trackier.sdk.TrackierSDK.initialize;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trackier.sdk.TrackierSDK;
import com.trackier.sdk.TrackierSDKConfig;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.APIEncryption.VerifyResponse;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.model.request.encrDecr.EncryptionRequest;
import com.wyh.happyyousdk.model.request.login.VerifyOtpRequest;
import com.wyh.happyyousdk.model.request.registration.RegistrationRequest;
import com.wyh.happyyousdk.model.response.encrDecr.EncryptionResponse;
import com.wyh.happyyousdk.model.response.login.VerifyOtpResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.MmpSDK;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.ChallangesModule.Utils.ExceptionHandler;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HappyYouSDK implements HappyYouInterface {

    Context context;
    ApiInterfaceWyh apiInterfaceWyh;
    ProgressDialog progressDialog;
    Boolean isRegistered;

    @Override
    public void init(Context context, String mobileNumber, String environment, String appVersionName, String source) {
        SDKConstants.mobileNumber = mobileNumber;
        SDKConstants.environment = environment;
        SDKConstants.appVersionName = appVersionName;
        SDKConstants.source = source;
        this.context = context;
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        String TR_SDK_KEY = "771d7226-464a-4a0c-9a9a-56babb4f06fc";
        SharedPref.init(context);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(context));

        TrackierSDKConfig sdkConfig = new TrackierSDKConfig(context, TR_SDK_KEY, "production");
        sdkConfig.setAppSecret("67bc071a3aec9534afac0c34", "21c316f9-c09f-4c3b-ac3f-9d899d808cb9");
        initialize(sdkConfig);

        //production, development, testing

        SharedPref.putSessionStartTime(getCurrentTimestamp());

        registerUserSSO();

    }

    private String getCurrentTimestamp() {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return isoFormat.format(new Date());
    }

    public void registerUserSSO() {
        if (SharedPref.getIsLoggedIn()) {
            Intent intent = new Intent(context, NewDashboardActivity.class);
            context.startActivity(intent);
        } else {
            encryptRSA(SDKConstants.mobileNumber);
        }
    }

    public void encryptRSA(String valueToEncrypt) {
        try {
            CommonUtils.showProgressDialige(context);
            EncryptionRequest encryptionRequest = new EncryptionRequest(new String[]{valueToEncrypt});
            Call<EncryptionResponse> call = apiInterfaceWyh.encryptRSA(encryptionRequest);
            call.enqueue(new Callback<EncryptionResponse>() {
                @Override
                public void onResponse(Call<EncryptionResponse> call, Response<EncryptionResponse> response) {
                    if (response.code() == 200 && response.body() != null && response.body().getData() != null &&
                            response.body().getData().get(0).getEncryptedStr() != null) {
                        verifyOTP(response.body().getData().get(0).getEncryptedStr(),
                                "JcdrskSPEYv+ya6/zk+EcDzoCzCYz6l3i3/PPsWnLqTT/ROG2t5BLDjaPp4/2bhwtNXBQdwpMybNmWq+OQiojn7PHLhQhO3AwWpXWDGtJswPX2Lzf5Ajpgdt8+sZTrPCjRfvmkAoRpfSDNWSNFB8W3Kc5RUKSsxEO1ltJ41hifw=");
                    } else {
                        CommonUtils.dismissDialoge();
                        Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EncryptionResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            CommonUtils.dismissDialoge();
            e.printStackTrace();
        }

    }

    private void verifyOTP(String mobileNo, String otp) {
        try {
            String deviceModel = Build.BRAND + " " + Build.MODEL;
            String userKey = RSAEncryption.rsaEncrypt(SDKConstants.mobileNumber + CommonUtils.generateRandonNumber(8));
            String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
            String appVersion = SDKConstants.appVersionName;
            Log.d("AuthToken", "MobileNumber - "+mobileNo+", OTP - "+otp);
            VerifyOtpRequest request = new VerifyOtpRequest(mobileNo.replaceAll("\\s", ""), otp.replaceAll("\\s", ""), deviceModel.replaceAll("\\s", ""), osVersion.replaceAll("\\s", ""), appVersion.replaceAll("\\s", ""), userKey.replaceAll("\\s", ""));
            APIInterface apiInterface = RetrofitHandler.apiInterface();

            apiInterface.VerifyOTPV5(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                                    json1.put("authtoken", responseData.getData().getAuthToken());
                                    json1.put("clientdetails", clientJSON);
                                    json1.put("crn", responseData.getData().getCrn());
                                    json1.put("isregistered", responseData.getData().getRegistered());
                                    json1.put("policydetails", policyJSON);
                                    json1.put("userkey", responseData.getData().getUserkey());
                                    encryptUsingAESAPI(String.valueOf(json1), object, apiInterface, otp, mobileNo);
                                } else {
                                    Toast.makeText(context, "Please enter valid otp", Toast.LENGTH_SHORT).show();
                                }

                            }

                        } catch (Exception e) {
                            CommonUtils.dismissDialoge();
                            APILogs.INSTANCE.sendLogs(String.valueOf(call.request().url()), e.getMessage(), String.valueOf(response.code()), context);
                            e.printStackTrace();
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            Log.d("data error", e.getMessage());
                        }
                    } else {
                        CommonUtils.dismissDialoge();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("AuthToken", "Exception - "+t.getMessage());
                    APILogs.INSTANCE.sendLogs(String.valueOf(call.request().url()), t.getMessage(), "onFailure", context);
                    CommonUtils.dismissDialoge();
                }
            });

        } catch (Exception e) {
            CommonUtils.dismissDialoge();//
            e.printStackTrace();
        }
    }

    private void encryptUsingAESAPI(String json, VerifyOtpResponse object, APIInterface apiInterface, String otp, String mobileNo) {


        if (object.getMsg().contains("Invalid")) {
            Toast.makeText(context, object.getMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (object.getMsg().contains("Expired")) {
            Toast.makeText(context, object.getMsg(), Toast.LENGTH_SHORT).show();
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
                    decryptUsingAESAPI(key, object, apiInterface, otp, mobileNo);
                }
            }

            @Override
            public void onFailure(Call<EncryptionResponse> call, Throwable t) {
                Log.d("data error", t.getMessage());
            }
        });

    }

    private void decryptUsingAESAPI(String key, VerifyOtpResponse object, APIInterface apiInterface, String otp, String mobileNo) {
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
                                    SharedPref.putMobileNo(RSAEncryption.rsaEncrypt(SDKConstants.mobileNumber));
                                    SharedPref.putDecryptMobileNo(SDKConstants.mobileNumber);
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
                                    Intent intent = new Intent(context, WelcomeActivity.class);
                                    context.startActivity(intent);
                                }

                            } else {
                                Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show();
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
        String deviceModel = Build.BRAND + " " + Build.MODEL;
        String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
        String appVersion = SDKConstants.appVersionName;
        String refferealCode = SharedPref.getReferralCode();

        RegistrationRequest request = new RegistrationRequest(SharedPref.getEncryptedMobileNo(), otp, "", "", "", deviceModel, osVersion, appVersion, SharedPref.getReferralCode(), TrackierSDK.getTrackierId()+"_Android", SDKConstants.source);
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
                        SharedPref.putCurrentTimeStamp(response.body().getData().getAuthTokenIssuedOn());
                        SharedPref.putCurrentTimeStampExpires(response.body().getData().getAuthTokenExpiresOn());
                        SharedPref.putUserName(response.body().getData().getClientDetails().getName());
                        SharedPref.putDOB(response.body().getData().getClientDetails().getDob());
                        SharedPref.putEmail("");
                        SharedPref.putProfileDetailedValued(false);
                        SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                        SharedPref.putDecryptMobileNo(SDKConstants.mobileNumber);
                        SharedPref.putMobileNo(RSAEncryption.rsaEncrypt(SDKConstants.mobileNumber));
                        SharedPref.putGender(response.body().getData().getClientDetails().getGender());
                        SharedPref.putProfilePicPath(response.body().getData().getClientDetails().getProfilePicPath());
                        SharedPref.putUuid(response.body().getData().getCrn());
                        SharedPreference.putUuid(response.body().getData().getCrn());
                        SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                        SharedPref.putIsLoggedIn(true);
                        Intent intent = new Intent(context, WelcomeActivity.class);
                        intent.putExtra("isCameFromRegistration", true);
                        context.startActivity(intent);
                    } else if (response.body().getData().getReferralStatus().equalsIgnoreCase("Wrong referral code")) {
                        Toast.makeText(context, "Please enter correct referral code", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                Log.d("login re Exception: ", new Gson().toJson(t.getMessage()));
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
