package com.wyh.happyyousdk.ehr;


import static com.wyh.happyyousdk.utils.Constants.SearchKey;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityEhrBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.RefferelCodeDialogeLayoutBinding;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.response.ehr.AllHealthRecordDetailsList;
import com.wyh.happyyousdk.model.response.ehr.HealthRecordTypeResponse;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.PendingActivityDashboard;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EhrActivity extends AppCompatActivity implements ScratchListener {
    ActivityEhrBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    AllHealthRecordDetailsList allEhrReportResponseList;
    HealthRecordTypeResponse healthRecordTypeResponse;
    AlertDialog alertDialogStamp, alertDialogBonusStamp;

    Integer healthRecordTypeId;
    int diagnosticId, hospitalId, drPrescriptionId, dietPlanId, fitnessPlusId, vaccineCertificateId, myPhotosId, otherDocsId,faceScanID;
    int stampId = -1;
    boolean isPositiveBtn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ehr);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = RetrofitHandler.getRetrofitInstance().create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeToolbar.tvBack.setText(context.getText(R.string.title_health_locker));
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "HealthLockerDashboard");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

//        getAllEhrList();
        getHealthRecordTypeList();

        binding.btnHealthLocker.setOnClickListener(view -> {
            Intent i = new Intent(this, HealthLockerHistoryActivity.class);
            i.putExtra(SearchKey, "");
            startActivity(i);
        });


        binding.btnAddRecord.setOnClickListener(view -> {
            Intent i = new Intent(this, AddNewEhrRecord.class);
            i.putExtra("viewType", "all");
            startActivity(i);
        });

        binding.rlDiagnostic.setOnClickListener(view -> {
            Intent i = new Intent(this, HealthRecordTypeWiseReportActivity.class);
            i.putExtra("healthRecordId", diagnosticId);
            i.putExtra("healthRecordName", "Diagnostics");
            i.putExtra("toolbar", "Diagnostics");
            startActivity(i);
        });

        binding.rlDoctorsPrescription.setOnClickListener(view -> {
            Intent i = new Intent(this, HealthRecordTypeWiseReportActivity.class);
            i.putExtra("healthRecordId", drPrescriptionId);
            i.putExtra("healthRecordName", "Doctor's Prescription");
            i.putExtra("toolbar", "Doctor's Prescription");
            startActivity(i);
        });

        binding.rlDietPlan.setOnClickListener(view -> {
            Intent i = new Intent(this, HealthRecordTypeWiseReportActivity.class);
            i.putExtra("healthRecordId", dietPlanId);
            i.putExtra("healthRecordName", "Diet Plans");
            i.putExtra("toolbar", "Diet Plans");
            startActivity(i);
        });
        binding.rlFitnessPlus.setOnClickListener(view -> {
            Intent i = new Intent(this, HealthRecordTypeWiseReportActivity.class);
            i.putExtra("healthRecordId", fitnessPlusId);
            i.putExtra("healthRecordName", "Fitness Plus");
            i.putExtra("toolbar", "Fitness Plus");
            startActivity(i);
        });
        binding.rlHospital.setOnClickListener(view -> {
            Intent i = new Intent(this, HealthRecordTypeWiseReportActivity.class);
            i.putExtra("healthRecordId", hospitalId);
            i.putExtra("healthRecordName", "Hospitals");
            i.putExtra("toolbar", "Hospitals");
            startActivity(i);
        });

        binding.rlMyPhotos.setOnClickListener(view -> {
            Intent i = new Intent(this, HealthLockerPhotosActivity.class);
            startActivity(i);
        });
        binding.rlOtherDocs.setOnClickListener(view -> {
            Intent i = new Intent(this, HealthRecordTypeWiseReportActivity.class);
            i.putExtra("healthRecordId", otherDocsId);
            i.putExtra("healthRecordName", "Other Documents");
            i.putExtra("toolbar", "Other Documents");
            startActivity(i);
        });

        binding.rlVaccinationCert.setOnClickListener(view -> {
            Intent i = new Intent(this, HealthRecordTypeWiseReportActivity.class);
            i.putExtra("healthRecordId", vaccineCertificateId);
            i.putExtra("healthRecordName", "Vaccination Certificates");
            i.putExtra("toolbar", "Vaccination Certificates");
            startActivity(i);
        });

        binding.ehrSearchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDoc();
            }
        });
    }

    private void searchDoc(){
        try{
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            RefferelCodeDialogeLayoutBinding bindingNickName = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.refferel_code_dialoge_layout, null, false);
            alertBuilder.setView(bindingNickName.getRoot());
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.setCancelable(true);
            if (!alertDialog.isShowing())
                alertDialog.show();


            bindingNickName.tvName.setText(getString(R.string.search));
            bindingNickName.edtRefferal.setHint(getString(R.string.search_bar_txt));
            bindingNickName.btnSubmit.setText(getString(R.string.search));

            bindingNickName.btnCancel.setOnClickListener(v -> {
                alertDialog.dismiss();
            });


            bindingNickName.btnSubmit.setOnClickListener(v -> {
                if(!bindingNickName.edtRefferal.getText().toString().isEmpty() && bindingNickName.edtRefferal.getText().toString().length() >= 1){
                    alertDialog.dismiss();
                    bindingNickName.edtRefferal.setError("");
                    Intent intent = new Intent(context, HealthLockerHistoryActivity.class);
                    intent.putExtra(SearchKey, bindingNickName.edtRefferal.getText().toString());
                    startActivity(intent);
                }else{
                    bindingNickName.edtRefferal.setError("Please enter at least one character");
                }
            });


            Rect displayRectangle = new Rect();
            Window window = getWindow();

            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.88f), RelativeLayout.LayoutParams.WRAP_CONTENT);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getHealthRecordTypeList() {
        progressDialog.show();
        Call<HealthRecordTypeResponse> call = apiInterfaceWyh.fetchHealthRecordTypes(SharedPref.getAuthToken());
        //Log.v("Ehr_Multipart", call.request().url().toString() + "\n" + SharedPref.getAuthToken());

        call.enqueue(new Callback<HealthRecordTypeResponse>() {
            @Override
            public void onResponse(Call<HealthRecordTypeResponse> call, Response<HealthRecordTypeResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //Log.v("Ehr_Multipart", call.request().url().toString() + "\n" + new Gson().toJson(response.body()));

                if (response.body() != null && response.body().getSuccess() && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_health_locker_types_success));
                    healthRecordTypeResponse = response.body();
                    getHealthRecordType();


                    if(response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));

                    }

                    if(response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                    }

                    showRewardsPopupDialogBox();


//                    if (reportList.size() > 0) {
//                        getSpinnerEhrTypes(reportList, healthDataTypeIdList);
//                    }
//                    Log.v("Ehr_Multipart_", new Gson().toJson(reportList));
                } else if (response.code() == 401) {
                    refreshAuthToken();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_health_locker_types_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HealthRecordTypeResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_health_locker_types_failed));
                }
//                Log.v("Ehr Response Err", t.getMessage());
            }
        });
    }

    private void getHealthRecordType() {
        if (healthRecordTypeResponse.getData().size() > 0) {
            for (int i = 0; i < healthRecordTypeResponse.getData().size(); i++) {
                if (healthRecordTypeResponse.getData().get(i).getKey().equals("Diet Plans")) {
                    dietPlanId = healthRecordTypeResponse.getData().get(i).getId();
                } else if (healthRecordTypeResponse.getData().get(i).getKey().equals("Diagnostics")) {
                    diagnosticId = healthRecordTypeResponse.getData().get(i).getId();
                } else if (healthRecordTypeResponse.getData().get(i).getKey().equals("Doctor's Prescription")) {
                    drPrescriptionId = healthRecordTypeResponse.getData().get(i).getId();
                } else if (healthRecordTypeResponse.getData().get(i).getKey().equals("Fitness Plus")) {
                    fitnessPlusId = healthRecordTypeResponse.getData().get(i).getId();
                } else if (healthRecordTypeResponse.getData().get(i).getKey().equals("Vaccination Certificates")) {
                    vaccineCertificateId = healthRecordTypeResponse.getData().get(i).getId();
                } else if (healthRecordTypeResponse.getData().get(i).getKey().equals("Other Documents")) {
                    otherDocsId = healthRecordTypeResponse.getData().get(i).getId();
                } else if (healthRecordTypeResponse.getData().get(i).getKey().equals("My Photos")) {
                    myPhotosId = healthRecordTypeResponse.getData().get(i).getId();
                } else if (healthRecordTypeResponse.getData().get(i).getKey().equals("Hospitals")) {
                    hospitalId = healthRecordTypeResponse.getData().get(i).getId();
                } else if(healthRecordTypeResponse.getData().get(i).getKey().equals("Face Scan")){
                    faceScanID = healthRecordTypeResponse.getData().get(i).getId();
                }
            }
        }
    }

    private void refreshAuthToken() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        String deviceModel = Build.BRAND + " " + Build.MODEL;
        String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
        String appVersion = SDKConstants.appVersionName;
        RefreshTokenRequest request = new RefreshTokenRequest(deviceModel, osVersion, appVersion);
        Call<RefreshTokenResponse> call = apiInterfaceWyh.refreshToken(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess() &&
                        response.body().getData().getAuthToken() != null && !response.body().getData().getAuthToken().equals("")) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer "+response.body().getData().getAuthToken());
                    SharedPreference.init(context);
                    SharedPreference.putAuthToken("Bearer "+response.body().getData().getAuthToken());
                    getHealthRecordTypeList();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                   /*Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                    Master.INSTANCE.logOut(context);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                /*Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    private void getAllEhrList() {
        progressDialog.show();

        Call<AllHealthRecordDetailsList> call = apiInterfaceWyh.fetchAllHealthRecords(SharedPref.getAuthToken());
        //Log.v("Ehr_Multipart", call.request().url().toString() + "\n" + SharedPref.getAuthToken() + ", " + SharedPref.getUuid());

        call.enqueue(new Callback<AllHealthRecordDetailsList>() {
            @Override
            public void onResponse(Call<AllHealthRecordDetailsList> call, Response<AllHealthRecordDetailsList> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //Log.v("Ehr_Multipart", response.code() + "," + new Gson().toJson(response.body()));
                if (response.body() != null && response.body().getSuccess() && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_health_locker_success));
//                    binding.btnDelete.setVisibility(View.VISIBLE);

                    allEhrReportResponseList = response.body();

//                    if (allEhrReportResponseList.getData().size() > 0) {
//                        setReportRecyclerView(allEhrReportResponseList);
//                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_health_locker_failed));
//                    binding.btnDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AllHealthRecordDetailsList> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_health_locker_failed));
                }
//                Log.v("Ehr Response Err", t.getMessage());
            }
        });
    }

    private void showStampsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogStamp = alertBuilder.create();
        alertDialogStamp.setCancelable(true);
        if (!alertDialogStamp.isShowing())
            alertDialogStamp.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if(rewards.split(";").length == 4){
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

         alertDialogStamp.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            }else{
                showRewardsPopupDialogBox();
            }
        });

       /* if(isOrange){
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));
        }else{
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));
        }*/
//        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: "+points);


        /*if (rewards.contains("First Login")) {
            binding.btnNegative.setVisibility(View.GONE);
            binding.btnPositive.setText("OK");
            binding.btnPositive.setOnClickListener(view -> {
                alertDialog.dismiss();
                Intent intent = new Intent(context, SyncDeviceActivity.class);
                startActivity(intent);
            });
        } else {

        }*/

//        binding.scratchView.onFullReveal();
        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogStamp.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PendingActivityDashboard) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusStamp = alertBuilder.create();
        alertDialogBonusStamp.setCancelable(true);
        if (!alertDialogBonusStamp.isShowing())
            alertDialogBonusStamp.show();


        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if(rewards.split(";").length == 4){
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

         alertDialogBonusStamp.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            }else{
                showRewardsPopupDialogBox();
            }
        });

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: "+points);

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusStamp.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(EhrActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((EhrActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showRewardsPopupDialogBox(){
        if(NewDashboardHelper.Companion.getPopUpShowModels().size() > 0){
            int i = 0;
            PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            if(NewDashboardHelper.Companion.getPopUpShowModels().size() > 1 && Objects.equals(firstData.getKey(), "Rewards")){
                i = 1;
                firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            }
            switch (firstData.getKey()){
                case "TokenStamp":
                    showStampsPopup(firstData.getValue(), context);
                    break;
                case "TokenStampBounce":
                    showBonusStampPopup(firstData.getValue(), context);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        }
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i >= 20) {
            scratchTokenReward();
            scratchCardLayout.onFullReveal();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if((alertDialogBonusStamp!= null && alertDialogBonusStamp.isShowing())){
                        alertDialogBonusStamp.dismiss();
                    }
                    if((alertDialogStamp!= null && alertDialogStamp.isShowing())){
                        alertDialogStamp.dismiss();
                    }
                }
            }, 3000);
        }
    }

    @Override
    public void onScratchStarted() {

    }

    private void scratchTokenReward() {
        RewardsPopupRequest request = new RewardsPopupRequest(stampId);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.scratchTokenReward(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_success));

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

}