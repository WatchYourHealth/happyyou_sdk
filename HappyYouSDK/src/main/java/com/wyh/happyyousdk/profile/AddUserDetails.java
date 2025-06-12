package com.wyh.happyyousdk.profile;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.crypto.RSAEncryption;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityAddUserDetailsBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.UserDetailsRequest;
import com.wyh.happyyousdk.model.request.VoucherIdRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.UserDetailResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.DataSource;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.Calendar;
import java.util.List;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserDetails extends AppCompatActivity  implements rewardDialogCloseListener, ScratchListener {

    ActivityAddUserDetailsBinding binding;
    Calendar mainStartCalender;
    String dobStr;
    QuizathonRewardData quizathonRewardData = null;
    VoucherIdRequest voucherIdRequest;
    AlertDialog alertDialogStamp, alertDialogBonusStamp, alertDialogBonusRewards;
    AssignRewardsResponse.SpinRewardsData spinRewardsData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_user_details);

        SharedPref.init(this);
        SharedPreference.init(this);
        mainStartCalender = Calendar.getInstance();
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -15);

        if (!SharedPref.getUserName().equalsIgnoreCase("")) {
            binding.edtUserDetailsName.setText(SharedPref.getUserName());
        }
        if (!SharedPref.getEmail().equalsIgnoreCase("")) {
            binding.edtUserDetailsEmail.setText(SharedPref.getEmail());
            binding.edtUserDetailsEmail.setEnabled(false);
        }
        if (!SharedPref.getDOB().equalsIgnoreCase("")) {
            try{
                binding.edtUserDetailsDob.setEnabled(false);
                if(SharedPref.getDOB().contains("-")){
                    String[] list = SharedPref.getDOB().split("-");
                    dobStr = list[2] + "-" + list[1] + "-" + list[0];
                    binding.edtUserDetailsDob.setText(dobStr);

                }else{
                    String[] list = SharedPref.getDOB().split("/");
                    dobStr = list[2] + "-" + list[1] + "-" + list[0];
                    binding.edtUserDetailsDob.setText(dobStr);

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if (!SharedPref.getRegistrationGender().equalsIgnoreCase("")) {
            binding.userDataGender.setEnabled(false);
            if (SharedPref.getRegistrationGender().equalsIgnoreCase("male")) {
                binding.userDataGender.setSelection(1);
            } else {
                binding.userDataGender.setSelection(2);
            }
        }

        binding.includeBack.tvBack.setText("Edit Profile");
        binding.includeBack.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddUserDetails.this, NewDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        binding.edtUserDetailsDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = mainStartCalender.get(Calendar.YEAR);
                int month = mainStartCalender.get(Calendar.MONTH);
                int day = mainStartCalender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog pickerDialog = new DatePickerDialog(AddUserDetails.this,
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
                            binding.edtUserDetailsDob.setText(newDay + "/" + newMonth + "/" + year1);
                            dobStr = year1 + "-" + newMonth + "-" + newDay;
                            mainStartCalender = Calendar.getInstance();
                            mainStartCalender.set(Calendar.YEAR, year1);
                            mainStartCalender.set(Calendar.MONTH, monthOfYear);
                            mainStartCalender.set(Calendar.DATE, dayOfMonth);
                        }, year, month, day);
                pickerDialog.getDatePicker().setMaxDate(prevYear.getTimeInMillis());
                pickerDialog.show();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (binding.edtUserDetailsName.getText().toString().equals("")) {
                    Toast.makeText(AddUserDetails.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (binding.edtUserDetailsEmail.getText().toString().equals("") || !CommonUtils.isValidEmail(binding.edtUserDetailsEmail.getText().toString())) {
                    Toast.makeText(AddUserDetails.this, "Please enter your valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (binding.edtUserDetailsDob.getText().toString().equals("")) {
                    Toast.makeText(AddUserDetails.this, "Please enter your DOB", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (binding.userDataGender.getSelectedItem().toString().equalsIgnoreCase("Select your gender")) {
                    Toast.makeText(AddUserDetails.this, "Please enter your gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateUserDetails();
            }
        });
    }

    private void updateUserDetails() {
        APILogs.INSTANCE.activityTracker("A_DB_HM_UD_Update", AddUserDetails.this);
        try {
            CommonUtils.showProgressDialige(this);
            SharedPref.putUserName(binding.edtUserDetailsName.getText().toString());
            SharedPref.putDOB(binding.edtUserDetailsDob.getText().toString());
            SharedPref.putRegistrationGender(binding.userDataGender.getSelectedItem().toString());
            SharedPref.putEmail(binding.edtUserDetailsEmail.getText().toString());

            UserDetailsRequest userDetailsRequest = new UserDetailsRequest(binding.edtUserDetailsName.getText().toString(),
                    RSAEncryption.rsaEncrypt(binding.edtUserDetailsEmail.getText().toString()), dobStr,
                    binding.userDataGender.getSelectedItem().toString());
            APIInterface apiInterface = RetrofitHandler.apiInterface();
            apiInterface.updateUserDetails(SharedPref.getAuthToken(), userDetailsRequest).enqueue(new Callback<UserDetailResponse>() {
                @Override
                public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                    CommonUtils.dismissDialoge();
                    SharedPref.putIsUserNameUpdated(true);
                    DataSource.getInstance().kgiUpdateDetails(binding.edtUserDetailsName.getText().toString(), dobStr,
                            RSAEncryption.rsaEncrypt(binding.edtUserDetailsEmail.getText().toString()),
                            binding.userDataGender.getSelectedItem().toString());
                    if (response.code() == 200 && response.body() != null && response.isSuccessful()) {
                        if (checkIsFromQuizqathon()) {
                            FetchQuizReward();
                        }
                        else {
                            Toast.makeText(AddUserDetails.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        if(response.body() != null && response.body().getMsg() != null) {
                            Toast.makeText(AddUserDetails.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    DataSource.getInstance().kgiUpdateDetails(binding.edtUserDetailsName.getText().toString(),dobStr,
                            RSAEncryption.rsaEncrypt(binding.edtUserDetailsEmail.getText().toString()),
                            binding.userDataGender.getSelectedItem().toString());
                }
            });


        } catch (Exception e) {
            CommonUtils.dismissDialoge();
            DataSource.getInstance().kgiUpdateDetails(binding.edtUserDetailsName.getText().toString(), dobStr,
                    RSAEncryption.rsaEncrypt(binding.edtUserDetailsEmail.getText().toString()),
                    binding.userDataGender.getSelectedItem().toString());
            e.printStackTrace();
        }
    }
    private void FetchQuizReward() {
        ActivityRewardRequest activityRewardRequest = new ActivityRewardRequest(NewDashboardHelper.Companion.getTrasactionId(), NewDashboardHelper.Companion.getFeatureName());
        APIInterface apiInterface = RetrofitHandler.apiInterface();
        Call<CommonSuccessResponse> call = apiInterface.FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewDashboardHelper.Companion.setTrasactionId(null);
                    NewDashboardHelper.Companion.setFeatureName(null);
                    NewDashboardHelper.Companion.setActivityName(null);
                    if (response.body().getQuizathonRewardData() != null) {
                        getQuizathonRewardPopup(response.body().getQuizathonRewardData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {

            }
        });
    }
    public boolean checkIsFromQuizqathon() {
        if (NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()) {
            return true;
        }
        return false;
    }
    private void getQuizathonRewardPopup(QuizathonRewardData data) {
        try {
            if (data.getRewardType() != null && !data.getRewardType().isEmpty()) {
                String rewardtype = data.getRewardType();
                if (rewardtype.equalsIgnoreCase("Points")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerPointsPopupCallBack(data.getRewardTitle(), this, "Trends", data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Offers")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerOfferCallBack(this, data.getPartnerLogo(), data.getRewardDescription(), data.getPartnerName()
                            , data.getPartnerUrl(), "Trends", data.getExpiryInHours(), data.getRewardTitle(), data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Voucher")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerVoucherCallBack(this, data.getRewardLogo(), data.getCouponCode(), data.getRewardDescription()
                            , data.getRewardTitle(), data.getExpiryInHours(), "Trends", data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Badge")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerBadgePopupCallBack(this, "Trends", data.getRewardLogo(), data.getRewardTitle(), data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Stamps")) {
                    QuizRewardDialog.INSTANCE.showStampsPopupCallBack(data.getRewardHeader1(), data.getRewardHeader2(), data.getRewardTitle(), data.getRewardValue(), this, this, this);
                } else if (rewardtype.equalsIgnoreCase("future")) {
                    QuizRewardDialog.INSTANCE.showFutureRewardDialog(this, data.getDialogModel(), data.getClaimDate());
                }

            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void onScratchComplete() {
        if (quizathonRewardData != null) {
            QuizRewardDialog.INSTANCE.QuizScratchCard(this, quizathonRewardData.getTransId(), true);
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i >= 20) {
            if (voucherIdRequest != null) {
                updateScratchStatus();
                scratchCardLayout.onFullReveal();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
    public void cancelDialog() {
        try {
            if (spinRewardsData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(this, spinRewardsData.getRewardType(), this);
            } else if (quizathonRewardData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(this, quizathonRewardData.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }
    private void updateScratchStatus() {
       ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(this)).create(ApiInterfaceWyh.class);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.updateScratchStatus(SharedPref.getAuthToken(), voucherIdRequest);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(AddUserDetails.this, getClass().getName(), getString(R.string.scratch_coupon_success));
                    voucherIdRequest = null;
                } else {
                    Analytics.logEvent(AddUserDetails.this, getClass().getName(), getString(R.string.scratch_coupon_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                Analytics.logEvent(AddUserDetails.this, getClass().getName(), getString(R.string.scratch_coupon_failed));
            }
        });
    }
}