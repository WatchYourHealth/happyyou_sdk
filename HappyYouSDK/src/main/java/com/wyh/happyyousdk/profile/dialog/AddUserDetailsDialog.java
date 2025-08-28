package com.wyh.happyyousdk.profile.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.DialogAddUserDetailsBinding;
import com.wyh.happyyousdk.model.request.UserDetailsRequest;
import com.wyh.happyyousdk.model.response.UserDetailResponse;
import com.wyh.happyyousdk.profile.AddUserDetails;
import com.wyh.happyyousdk.quizathon.QuizathonScoreActivity;
import com.wyh.happyyousdk.quizathon.TimeBoundScoreActivity;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.DataSource;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserDetailsDialog extends Dialog {

    DialogAddUserDetailsBinding binding;
    Calendar mainStartCalender;
    String dobStr;

    public AddUserDetailsDialog(@NonNull Context context) {
        super(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_add_user_details, null, false);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);



        SharedPref.init(context);
        SharedPreference.init(context);
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
            try {
                binding.edtUserDetailsDob.setEnabled(false);
                if (SharedPref.getDOB().contains("-")) {
                    String[] list = SharedPref.getDOB().split("-");
                    dobStr = list[0] + "-" + list[1] + "-" + list[2];
                    binding.edtUserDetailsDob.setText(dobStr);

                } else {
                    String[] list = SharedPref.getDOB().split("/");
                    dobStr = list[2] + "-" + list[1] + "-" + list[0];
                    String dobStrNew = list[0] + "/" + list[1] + "/" + list[2];
                    binding.edtUserDetailsDob.setText(dobStrNew);

                }

            } catch (Exception e) {
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


        binding.edtUserDetailsDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = mainStartCalender.get(Calendar.YEAR);
                int month = mainStartCalender.get(Calendar.MONTH);
                int day = mainStartCalender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog pickerDialog = new DatePickerDialog(context,
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
                    Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (binding.edtUserDetailsEmail.getText().toString().equals("") || !CommonUtils.isValidEmail(binding.edtUserDetailsEmail.getText().toString())) {
                    Toast.makeText(context, "Please enter your valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (binding.edtUserDetailsDob.getText().toString().equals("")) {
                    Toast.makeText(context, "Please enter your DOB", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (binding.userDataGender.getSelectedItem().toString().equalsIgnoreCase("Select your gender")) {
                    Toast.makeText(context, "Please enter your gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateUserDetails(context);
            }
        });
    }

    private void updateUserDetails(Context context) {
        APILogs.INSTANCE.activityTracker("A_DB_HM_UD_Update", context);
        try {
            CommonUtils.showProgressDialige(context);
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
                    if (response.code() == 200 && response.body() != null && response.isSuccessful()) {
                        dismiss();
                        if (context instanceof QuizathonScoreActivity) {
                            if (((QuizathonScoreActivity) context).checkIsFromQuizqathon()) {
                                ((QuizathonScoreActivity) context).FetchQuizReward();
                            }
                        }
                        else if (context instanceof TimeBoundScoreActivity) {
                            if (((TimeBoundScoreActivity) context).checkIsFromQuizqathon()) {
                                ((TimeBoundScoreActivity) context).FetchQuizReward();
                            }
                        } else {
                            Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dismiss();
                        if (response.body() != null && response.body().getMsg() != null) {
                            Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                    dismiss();
                }
            });


        } catch (Exception e) {
            dismiss();
            e.printStackTrace();
        }
    }
}
