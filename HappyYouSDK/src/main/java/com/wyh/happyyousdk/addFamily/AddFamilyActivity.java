package com.wyh.happyyousdk.addFamily;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.hideKeyboard;
import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityAddFamilyBinding;
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse;
import com.wyh.happyyousdk.ice.AddEmergencyContactActivity;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.GetCityRequestModel;
import com.wyh.happyyousdk.model.request.VoucherIdRequest;
import com.wyh.happyyousdk.model.request.addFamily.AddFamilyRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.AddFamilyResponse;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.GetCityListResponseModel;
import com.wyh.happyyousdk.model.response.addmember.GetFamilyRelation;
import com.wyh.happyyousdk.model.response.ice.FetchEmergencyDetailsResp;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFamilyActivity extends AppCompatActivity implements View.OnFocusChangeListener , rewardDialogCloseListener, ScratchListener {
    ActivityAddFamilyBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    private static final int REQ_PICK_CONTACT = 3;
    private static final int CONTACT_PERMISSION_CODE = 1;
    boolean isPrimaryContact = false;
    String data;

    List<AddFamilyRequest> familyList;
    QuizathonRewardData quizathonRewardData = null;
    VoucherIdRequest voucherIdRequest;
    AlertDialog alertDialogStamp, alertDialogBonusStamp, alertDialogBonusRewards;
    AssignRewardsResponse.SpinRewardsData spinRewardsData = null;
    List<String> relationNameList =new ArrayList<>();;
    List<GetFamilyRelation.MemberRelation>memberRelationList =new ArrayList<>();
    String memberName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(AddFamilyActivity.this, R.layout.activity_add_family);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        data = getIntent().getStringExtra("data");
        if(data != null && !data.isEmpty()){
            Type type1 = new TypeToken<List<AddFamilyRequest>>() {
            }.getType();
            familyList = new Gson().fromJson(data, type1);
        }

        binding.includeBack.tvBack.setText("Add Family Member");
        binding.includeBack.llBack.setOnClickListener(view -> onBackPressed());
        binding.edtMobileNumberPrimary.setOnFocusChangeListener(this::onFocusChange);

        binding.primaryContact.setOnClickListener(v->{
            isPrimaryContact = true;
            openContacts();
        });



        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            onBackPressed();
        });

        getMemberList();
        binding.spinnerMemberList.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                memberName = memberRelationList.get(position).getRelationName();
                if (memberName.equalsIgnoreCase("Other"))
                {
                    binding.edtOtherPrimary.setVisibility(View.VISIBLE);
                }
                else {
                    binding.edtOtherPrimary.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });
        binding.btnSubmit.setOnClickListener(view -> {
            binding.btnSubmit.setEnabled(false);
            if(binding.btnSubmit.getText() != "Edit"){

                if(binding.edtNamePrimary.getText().toString().isEmpty()){
                    editTextValidation(binding.edtNamePrimary,"valid name");
                    return;
                }

                if(binding.edtMobileNumberPrimary.getText().toString().isEmpty()){
                    editTextValidation(binding.edtMobileNumberPrimary,"mobile number");
                    return;
                }

                if(binding.edtMobileNumberPrimary.getText().toString().trim().length() <10){
                    if (binding.edtMobileNumberPrimary.getText().toString().contains(SharedPref.getDecryptMobileNo())) {
                        binding.edtMobileNumberPrimary.requestFocus();
                        binding.edtMobileNumberPrimary.setError("You cannot add your own mobile number");
                        showKeyboard(AddFamilyActivity.this);
                    }else {
                        editTextValidation(binding.edtMobileNumberPrimary,"valid mobile number");
                    }
                    return;
                }

                /*if(binding.edtRelationShipPrimary.getText().toString().isEmpty()){
                    editTextValidation(binding.edtRelationShipPrimary,"relationship");
                    return;
                }*/
                if (memberName.equalsIgnoreCase(""))
                {
                    editTextValidation(binding.edtRelationShipPrimary,"relationship");
                    return;
                }
                if (memberName.equalsIgnoreCase("Other")&&binding.edtOtherPrimary.getText().toString().isEmpty())
                {
                    editTextValidation(binding.edtOtherPrimary,"relationship");
                }

                if(familyList != null && familyList.size() > 0){
                    for(AddFamilyRequest item: familyList){
                        if(item.getMobileNo().equalsIgnoreCase(binding.edtMobileNumberPrimary.getText().toString())){
                            Toast.makeText(context, "Family member already exist", Toast.LENGTH_SHORT).show();
                            binding.btnSubmit.setEnabled(true);
                            return;
                        }
                    }
                }

                addFamilyMember();
            }
        });




        binding.edtNamePrimary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ButtonTextChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.edtMobileNumberPrimary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ButtonTextChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.edtRelationShipPrimary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ButtonTextChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void editTextValidation(EditText editText, String title) {
        editText.requestFocus();
        editText.setError("Please enter " + title);
        showKeyboard(AddFamilyActivity.this);
        binding.btnSubmit.setEnabled(true);
    }

    public void openContacts() {
        if (checkContactPermission()) {
            openContactsDialog();
        } else {
            requestContactPermission();
        }
    }


    // If permission is granted, then open contact box
    private void openContactsDialog() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQ_PICK_CONTACT);
    }

    // Check if the user has the permission granted
    private boolean checkContactPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        return result;
    }

    // Invoke request permission dialog
    private void requestContactPermission() {
        String[] permission = {Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(this, permission, CONTACT_PERMISSION_CODE);
    }

    // After the permission is granted, open the contact dialog
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openContactsDialog();
            }
        }
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Cursor cursor1, cursor2;
                Uri uri = data.getData();

                cursor1 = getContentResolver().query(uri, null, null, null, null);
                if (cursor1.moveToFirst()) {
                    @SuppressLint("Range") String contactId = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                    @SuppressLint("Range") String contactName = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    @SuppressLint("Range") String hasNumber = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if ("1".equals(hasNumber)) {
                        cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                        while (cursor2.moveToNext()) {
                            String phone = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            // Use contactName and contactNumber for your purposes
                            Log.d("phone number", phone);
                            Log.d("phone name", contactName);
//                            phone = "+91 9652 3589 00";

                            if(phone.contains("+")){
                                phone = phone.substring(3);
                            }

                            if(phone.charAt(0) == 48){
                                phone = phone.substring(1);
                            }

                            phone = phone.replace(" ", "");

                            if (isPrimaryContact){
                                binding.edtNamePrimary.setText(contactName);
                                binding.edtMobileNumberPrimary.setText(phone);
                            }else{
                                binding.edtNameSecondary.setText(contactName);
                                binding.edtMobileNumberSecondary.setText(phone);
                            }
                        }
                        cursor2.close();
                    }
                }
                cursor1.close();

            }
        }
    }

    void ButtonTextChange(){
        /*if(data != ""){
            binding.btnSubmit.setText("Update");
        }*/
    }

    private void addFamilyMember() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        String relationShip="";
       /* AddFamilyRequest request = new AddFamilyRequest(0, binding.edtNamePrimary.getText().toString(),
                binding.edtRelationShipPrimary.getText().toString(),
                binding.edtMobileNumberPrimary.getText().toString());*/
        if (memberName.equalsIgnoreCase("Others"))
        {
            relationShip=binding.edtOtherPrimary.getText().toString().trim();
        }
        else {
            relationShip=memberName;
        }
        AddFamilyRequest request = new AddFamilyRequest(0, binding.edtNamePrimary.getText().toString(),
                relationShip,
                binding.edtMobileNumberPrimary.getText().toString());
        Call<CommonSuccessResponse> call = apiInterfaceWyh.addClientFamilyDetails(SharedPref.getAuthToken(), Collections.singletonList(request));
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.body().isSuccess()) {
                     if (checkIsFromQuizqathon()) {
                        FetchQuizReward();
                    }
                     else {
                         Analytics.logEvent(context, context.getClass().getName(), getString(R.string.family_list_success));
                         Toast.makeText(context, "Family member added successfully", Toast.LENGTH_SHORT).show();
                         onBackPressed();
                     }

                } else {
                    binding.btnSubmit.setEnabled(true);
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.family_list_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.family_list_failed));
                binding.btnSubmit.setEnabled(true);
            }
        });
    }


    @Override
    public void onBackPressed() {
        hideKeyboard(AddFamilyActivity.this);
        finish();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(!hasFocus){
            if(binding.edtMobileNumberPrimary.getText().length() < 10){
                binding.edtMobileNumberPrimary.setError("Enter valid mobile number");
                //binding.edtMobileNumberPrimary.requestFocus();

            }
        }

    }
    public boolean checkIsFromQuizqathon() {
        if (NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()) {
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
                        quizathonRewardData=response.body().getQuizathonRewardData();
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
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, spinRewardsData.getRewardType(), this);
            } else if (quizathonRewardData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, quizathonRewardData.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }
    private void updateScratchStatus() {
        Call<CommonSuccessResponse> call = apiInterfaceWyh.updateScratchStatus(SharedPref.getAuthToken(), voucherIdRequest);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_success));
                    voucherIdRequest = null;
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_failed));
            }
        });
    }
    private void getMemberList() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<GetFamilyRelation> call = apiInterfaceWyh.getFamilyRelation(SharedPref.getAuthToken());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetFamilyRelation> call, @NonNull Response<GetFamilyRelation> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess() && response.body().getData()!=null) {

                    memberRelationList=response.body().getData();
                    setSpinnerData(response.body().getData(), context);
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetFamilyRelation> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setSpinnerData(List<GetFamilyRelation.MemberRelation>memberRelationList , Context context)
    {
        for (int i = 0; i < memberRelationList.size(); i++) {
            relationNameList.add(memberRelationList.get(i).getRelationName());
        }

        ArrayAdapter<String> spinnerSpinWheelAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, relationNameList);
        spinnerSpinWheelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerMemberList.setAdapter(spinnerSpinWheelAdapter);

    }

}