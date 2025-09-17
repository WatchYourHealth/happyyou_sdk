package com.wyh.happyyousdk.policyDetail;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.getDateInFormat;
import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.crypto.CryptoHelper;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityAddPolicyBySearchBinding;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.GetClientIDReq;
import com.wyh.happyyousdk.model.GetClientRes;
import com.wyh.happyyousdk.model.GetOTPReq;
import com.wyh.happyyousdk.model.PolicyListReq;
import com.wyh.happyyousdk.model.PolicyListResp;
import com.wyh.happyyousdk.model.VerifyOTP;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicySearchActivity extends AppCompatActivity implements ScratchListener , rewardDialogCloseListener {
    ActivityAddPolicyBySearchBinding binding;
    Context context;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    ApiInterfaceWyh apiInterfaceWyhNew;
    Calendar mainStartCalender;
    String dobStr;

    List<GetClientRes.Data.Client> client = new ArrayList<>();
    QuizathonRewardData quizathonRewardData = null;
    boolean isStamp = false;
    AlertDialog alertDialogBonusRewards, alertDialogStamp, alertDialogBonusStamp;
    int stampId = -1;
    AssignRewardsResponse.SpinRewardsData spinRewardsData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_policy_by_search);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);
        mainStartCalender = Calendar.getInstance();
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -15);

        apiInterfaceWyh = RetrofitHandler.apiInterface();
        apiInterfaceWyhNew = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        getClientIdByMobile(SharedPref.getEncryptedMobileNo());
        binding.tvSearch.setOnClickListener(view -> {

            if (binding.edtPolicyNumber.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Policy Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.edtDOB.getText().toString().isEmpty() && binding.edtDOB.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please Enter Date of birth", Toast.LENGTH_SHORT).show();
                return;
            }
            searchPolicy(binding.edtPolicyNumber.getText().toString(), binding.edtDOB.getText().toString());
        });

        binding.btnSendOTP.setOnClickListener(view -> {
            if (binding.edtOTP.getText() != null && binding.edtOTP.getText().toString().length()==6) {
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

        binding.imBack.setOnClickListener(view -> {

            onBackPressed();
        });

        binding.edtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = mainStartCalender.get(Calendar.YEAR);
                int month = mainStartCalender.get(Calendar.MONTH);
                int day = mainStartCalender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog pickerDialog = new DatePickerDialog(PolicySearchActivity.this,
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
                            APILogs.INSTANCE.activityTracker("A_DB_HM_KP_SP_SDOB", context);
                            dobStr = year1 + "-" + newMonth + "-" + newDay;
                            mainStartCalender = Calendar.getInstance();
                            mainStartCalender.set(Calendar.YEAR, year1);
                            mainStartCalender.set(Calendar.MONTH, monthOfYear);
                            mainStartCalender.set(Calendar.DATE, dayOfMonth);
                        }, year, month, day);
                //pickerDialog.getDatePicker().setMaxDate(prevYear.getTimeInMillis());
                pickerDialog.show();
            }
        });
    }

    private void searchPolicy(String policyId, String dob) {
        boolean isPolicyFound = false;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(dob);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = outputFormat.format(date);
        System.out.println("Formatted Date String: " + formattedDate);
        for (int i = 0; i < client.size(); i++) {
            if (client.get(i).getPolicyId().equals(policyId) && client.get(i).getDob().equals(formattedDate)) {
                isPolicyFound = true;
                SharedPref.setClientId(client.get(i).getClientId());
                sendOTp();
                break;
                //return;
            }
        }
        if (!isPolicyFound)
            Toast.makeText(context, "No policy found", Toast.LENGTH_SHORT).show();
    }


    private void getClientIdByMobile(String mobileNumber) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetClientIDReq request = new GetClientIDReq(SharedPref.getEncryptedMobileNo());
        Call<GetClientRes> call = apiInterfaceWyh.getClientID(SharedPref.getAuthToken(), request);

            Log.d("AuthToken", "Req: "+new Gson().toJson(request));

            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<GetClientRes> call, Response<GetClientRes> response) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.d("AuthToken", response.code()+", Res: "+new Gson().toJson(response.body()));
                    if (response.code() == 200 && response.body() != null && response.body().getStatus()) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_success));
                        if (response.body().getData() != null && response.body().getData().getClients().size() > 0) {
                            client = response.body().getData().getClients();
                            if (response.body().getData().getClients().size() == 1) {
                                SharedPref.setClientId(response.body().getData().getClients().get(0).getClientId());
                                Log.e("ClientID", SharedPref.getClientId());
                                sendOTp();
                            } else if (response.body().getData().getClients().size() > 1) {
                                //sendOTp();
                            /*Intent intent = new Intent(context, PolicyCreateMPIN.class);
                            startActivity(intent);
                            finish();*/
                                /*sendOTp(SharedPref.getEncryptedMobileNo());*/
                            } else {
                                binding.llSearch.setVisibility(View.GONE);
                                binding.tvNoPolicyFound.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "No policy found", Toast.LENGTH_SHORT).show();
                            }
                            if (checkIsFromQuizqathon()) {
                                FetchQuizReward();
                            }
                            //attachUserPolicy(policyNumber);

                        } else {
                            binding.llSearch.setVisibility(View.GONE);
                            binding.tvNoPolicyFound.setVisibility(View.VISIBLE);
                            //Toast.makeText(context, "No policy found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
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

    private void sendOTp() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        GetOTPReq request = new GetOTPReq("1", "", "", SharedPref.getEncryptedMobileNo(), 15);
        Log.e("Mobile", RSAEncryption.callDecryptionMethod(SharedPref.getEncryptedMobileNo()));
        Call<GetClientRes> call = apiInterfaceWyh.GetPolicyOTP(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<GetClientRes>() {
            @Override
            public void onResponse(Call<GetClientRes> call, Response<GetClientRes> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getStatus()) {
                    /*Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_success));*/
                    binding.tvMessage.setText(response.body().getMsg());
                    binding.imTransparent1.setVisibility(View.VISIBLE);
                    binding.getStartedLayout.setVisibility(View.VISIBLE);
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

    private void verifyOTP() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        VerifyOTP request = new VerifyOTP(RSAEncryption.rsaEncrypt(binding.edtOTP.getText().toString()));
        Call<GetClientRes> call = apiInterfaceWyh.GetPolicyOTP(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<GetClientRes>() {
            @Override
            public void onResponse(Call<GetClientRes> call, Response<GetClientRes> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getStatus()) {
                    /*Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_success));*/
                    getPolicy();

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


    private void getPolicy() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Log.e("ClientID", SharedPref.getClientId());
        PolicyListReq request = new PolicyListReq("0", "999", SharedPref.getClientId());
        Call<PolicyListResp> call = apiInterfaceWyh.GetPolicyDetailsPROD(SharedPref.getAuthToken(), request);
        Log.d("policy req", new Gson().toJson(request));
        call.enqueue(new Callback<PolicyListResp>() {
            @Override
            public void onResponse(Call<PolicyListResp> call, Response<PolicyListResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Intent intent = new Intent(context, PolicyCreateMPIN.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
    /*private void attachUserPolicy(String policyNumber) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        AttachUserPolicyRequest request = new AttachUserPolicyRequest(policyNumber, dobStr);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.attachUserPolicy(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.attach_policy_success));
                    Intent intent = new Intent(context, PolicyDetailsActivity.class);
                    startActivity(intent);
                    finish();
                } else if ((response.code() == 200 && response.body().getMsg().equalsIgnoreCase("Policy already attached"))){
                    Toast.makeText(context, "Policy already attached", Toast.LENGTH_SHORT).show();
                }
                else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.attach_policy_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.attach_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    public  boolean checkIsFromQuizqathon(){
        if(NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()){
            return true;
        }
        return false;
    }
    private void FetchQuizReward() {
        ActivityRewardRequest activityRewardRequest = new ActivityRewardRequest(NewDashboardHelper.Companion.getTrasactionId(), NewDashboardHelper.Companion.getFeatureName());
        Call<CommonSuccessResponse> call = apiInterfaceWyh.FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewDashboardHelper.Companion.setTrasactionId(null);
                    NewDashboardHelper.Companion.setFeatureName(null);
                    NewDashboardHelper.Companion.setActivityName(null);
                    if (response.body().getQuizathonRewardData() != null) {
                        quizathonRewardData = response.body().getQuizathonRewardData();
                        getQuizathonRewardPopup(response.body().getQuizathonRewardData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {

            }
        });
    }
    private void getQuizathonRewardPopup(QuizathonRewardData data) {
        try {
            if (data.getRewardType() != null && !data.getRewardType().isEmpty()) {
                String rewardtype = data.getRewardType();
                if (rewardtype.equalsIgnoreCase("Points")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerPointsPopupCallBack(data.getRewardTitle(), context, "Trends", data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Offers")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerOfferCallBack(context, data.getPartnerLogo(), data.getRewardDescription(), data.getPartnerName()
                            , data.getPartnerUrl(), "Trends", data.getExpiryInHours(), data.getRewardTitle(), data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Voucher")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerVoucherCallBack(context, data.getRewardLogo(), data.getCouponCode(), data.getRewardDescription()
                            , data.getRewardTitle(), data.getExpiryInHours(), "Trends", data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Badge")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerBadgePopupCallBack(context, "Trends", data.getRewardLogo(), data.getRewardTitle(), data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Stamps")) {
                    QuizRewardDialog.INSTANCE.showStampsPopupCallBack(data.getRewardHeader1(), data.getRewardHeader2(), data.getRewardTitle(), data.getRewardValue(), this, this, this);
                } else if (rewardtype.equalsIgnoreCase("future")) {
                    QuizRewardDialog.INSTANCE.showFutureRewardDialog(context, data.getDialogModel(), data.getClaimDate());
                }

            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void onScratchComplete() {
        if (quizathonRewardData != null) {
            QuizRewardDialog.INSTANCE.QuizScratchCard(PolicySearchActivity.this, quizathonRewardData.getTransId(), true);
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i > 20) {
            scratchCardLayout.onFullReveal();
            {
                if (isStamp) {
                    isStamp = false;
                    scratchTokenReward();
                }
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (alertDialogBonusRewards != null && alertDialogBonusRewards.isShowing()) {
                            alertDialogBonusRewards.dismiss();
                        }
                        if ((alertDialogBonusStamp != null && alertDialogBonusStamp.isShowing())) {
                            alertDialogBonusStamp.dismiss();
                        }
                        if ((alertDialogStamp != null && alertDialogStamp.isShowing())) {
                            alertDialogStamp.dismiss();
                        }
                    }
                }, 3000);
            }
        }
    }

    @Override
    public void onScratchStarted() {

    }

    @Override
    public void onDialogDismiss() {
        cancelDialog();
    }
    private void scratchTokenReward() {
        RewardsPopupRequest request = new RewardsPopupRequest(stampId);
        Call<CommonSuccessResponse> call = apiInterfaceWyhNew.scratchTokenReward(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {

                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_success));

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {

                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void cancelDialog() {
        try {
            if (spinRewardsData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, spinRewardsData.getRewardType(), this);
            } else if (quizathonRewardData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, quizathonRewardData.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }
}