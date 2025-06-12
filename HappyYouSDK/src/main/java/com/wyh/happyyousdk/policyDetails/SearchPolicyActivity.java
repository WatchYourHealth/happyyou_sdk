package com.wyh.happyyousdk.policyDetails;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivitySearchPolicyBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.policy.AttachUserPolicyRequest;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.model.PolicyDetailsRequest;
import com.wyh.happyyousdk.model.PolicyDetailsResponse;
import com.wyh.happyyousdk.profile.AddUserDetails;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPolicyActivity extends AppCompatActivity {
    ActivitySearchPolicyBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    Calendar  mainStartCalender;
    String dobStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_policy);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);
        mainStartCalender = Calendar.getInstance();
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -15);

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Kotak Policy");
        binding.includeToolbar.tvBack.setTextColor(getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getColor(R.color.white));

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "SearchPolicy");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.btnSearchPolicy.setOnClickListener(view -> {

            if (binding.edtPolicyNumber.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Policy Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.edtPolicyDob.getText().toString().isEmpty() && binding.edtPolicyDob.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please Enter Date of birth", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!binding.edtPolicyNumber.getText().toString().isEmpty() && binding.edtPolicyNumber.getText().length() >= 10) {
                Toast.makeText(this, "Please Enter Valid Policy Number", Toast.LENGTH_SHORT).show();
                return;
            }

            searchPolicy(binding.edtPolicyNumber.getText().toString(), SharedPref.getDecryptMobileNo());
        });

        binding.edtPolicyDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = mainStartCalender.get(Calendar.YEAR);
                int month = mainStartCalender.get(Calendar.MONTH);
                int day = mainStartCalender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog pickerDialog = new DatePickerDialog(SearchPolicyActivity.this,
                        (view1, year1, monthOfYear, dayOfMonth) ->  {
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
                            binding.edtPolicyDob.setText(newDay + "/" + newMonth + "/" + year1);
                            APILogs.INSTANCE.activityTracker("A_DB_HM_KP_SP_SDOB",context);
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
    }

    private void searchPolicy(String policyNumber, String mobileNumber) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        PolicyDetailsRequest request = new PolicyDetailsRequest(mobileNumber, policyNumber,dobStr);
        Call<PolicyDetailsResponse> call = apiInterfaceWyh.searchPolicies(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<PolicyDetailsResponse>() {
            @Override
            public void onResponse(Call<PolicyDetailsResponse> call, Response<PolicyDetailsResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_success));
                    if(response.body().getData() != null && response.body().getData().size() > 0){
                        attachUserPolicy(policyNumber);
                    }else{
                        Toast.makeText(context, "No policy found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PolicyDetailsResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attachUserPolicy(String policyNumber) {
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
    }

}