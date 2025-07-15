package com.wyh.happyyousdk.ice;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.showKeyboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityAddEmergencyContactBinding;
import com.wyh.happyyousdk.utils.Master;

import com.wyh.happyyousdk.model.request.ice.AddEmergencyDetailsReq;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.ice.AddEmergencyDetailsResp;
import com.wyh.happyyousdk.model.response.ice.FetchEmergencyDetailsResp;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEmergencyContactActivity extends AppCompatActivity {
    ActivityAddEmergencyContactBinding binding;
    boolean validation;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    FetchEmergencyDetailsResp fetchEmergencyDetailsResp;
    private static final int REQ_PICK_CONTACT = 3;
    private static final int CONTACT_PERMISSION_CODE = 1;
    boolean isPrimaryContact = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_emergency_contact);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeBack.tvBack.setText("ICE (In Case of Emergency)");
        binding.includeBack.llBack.setOnClickListener(view -> finish());

        fetchEmergencyDetailsResp = new Gson().fromJson(getIntent().getStringExtra("data"), FetchEmergencyDetailsResp.class);

        if (fetchEmergencyDetailsResp != null && fetchEmergencyDetailsResp.getData() != null && fetchEmergencyDetailsResp.getData().size() > 0) {
            FetchEmergencyDetailsResp.Datum datum = fetchEmergencyDetailsResp.getData().get(0);
            binding.edtMobileNumberPrimary.setText(datum.getPrimaryContactMobile());
            binding.edtNamePrimary.setText(datum.getPrimaryContactName());
            binding.edtRelationShipPrimary.setText(datum.getPrimaryContactRelation());
            binding.edtMobileNumberSecondary.setText(datum.getSecondaryContactMobile());
            binding.edtNameSecondary.setText(datum.getSecondaryContactName());
            binding.edtRelationShipSecondary.setText(datum.getSecondaryContactRelation());
            binding.btnSubmit.setText("Edit");
        }

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "ICEAddEmergencyContact");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.dailer.setOnClickListener(view -> {
            if (fetchEmergencyDetailsResp != null && fetchEmergencyDetailsResp.getData() != null && fetchEmergencyDetailsResp.getData().size() > 0) {
                FetchEmergencyDetailsResp.Datum datum = fetchEmergencyDetailsResp.getData().get(0);
                if (!datum.getSecondaryContactMobile().isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+datum.getSecondaryContactMobile()));
                    startActivity(intent);
                }

            }

        });

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.btnSubmit.setOnClickListener(view -> {
            if(binding.btnSubmit.getText() != "Edit"){

                if(binding.edtNamePrimary.getText().toString().isEmpty()){
                    editTextValidation(binding.edtNamePrimary,"valid Name");

                }else if(binding.edtMobileNumberPrimary.getText().toString().isEmpty() || binding.edtMobileNumberPrimary.getText().length() <10){
                    if (binding.edtMobileNumberPrimary.getText().toString().contains(SharedPref.getDecryptMobileNo())) {
                        binding.edtMobileNumberPrimary.requestFocus();
                        binding.edtMobileNumberPrimary.setError("You can not add ur own Mobile Number");
                        showKeyboard(AddEmergencyContactActivity.this);

                    }else {
                        editTextValidation(binding.edtMobileNumberPrimary,"valid Mobile Number");
                    }


                }else if(binding.edtRelationShipPrimary.getText().toString().isEmpty()){
                    editTextValidation(binding.edtRelationShipPrimary,"Relationship");

                } else if(binding.edtNameSecondary.getText().toString().isEmpty()){
                    editTextValidation(binding.edtNameSecondary,"valid Name");

                }else if(binding.edtMobileNumberSecondary.getText().toString().isEmpty() || binding.edtMobileNumberSecondary.getText().length() <10  ){
                    if (binding.edtMobileNumberSecondary.getText().toString().contains(SharedPref.getDecryptMobileNo())) {
                        binding.edtMobileNumberSecondary.requestFocus();
                        binding.edtMobileNumberSecondary.setError("You can not add ur own Mobile Number");
                        showKeyboard(AddEmergencyContactActivity.this);

                    }else {
                        editTextValidation(binding.edtMobileNumberSecondary,"valid Mobile Number");
                    }


                }else if(binding.edtMobileNumberPrimary.getText().toString().equals(binding.edtMobileNumberSecondary.getText().toString())){

                    binding.edtMobileNumberSecondary.requestFocus();
                    binding.edtMobileNumberSecondary.setError("Please enter different emergency contacts");
                    showKeyboard(AddEmergencyContactActivity.this);


                }else if(binding.edtRelationShipSecondary.getText().toString().isEmpty()){
                    editTextValidation(binding.edtRelationShipSecondary,"Relationship");

                }else {
                    addEmergencyContact();
                }
               //validation = editTextValidation(binding.edtNamePrimary, "valid Name");
               //validation = editTextValidation(binding.edtMobileNumberPrimary, "valid Mobile Number");
               //validation = editTextValidation(binding.edtRelationShipPrimary, "");
               //validation = editTextValidation(binding.edtNameSecondary, "");
               //validation = editTextValidation(binding.edtMobileNumberSecondary, "valid Mobile Number");
               //validation = editTextValidation(binding.edtRelationShipSecondary, "Relationship");




            }
        });

        binding.primaryContact.setOnClickListener(v->{
            isPrimaryContact = true;
            openContacts();
        });

        binding.secondaryContact.setOnClickListener(v->{
            isPrimaryContact = false;
            openContacts();
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
        binding.edtNameSecondary.addTextChangedListener(new TextWatcher() {
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
        binding.edtMobileNumberSecondary.addTextChangedListener(new TextWatcher() {
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
        binding.edtRelationShipSecondary.addTextChangedListener(new TextWatcher() {
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
        showKeyboard(AddEmergencyContactActivity.this);

      /*  if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.requestFocus();
            editText.setError("Please enter " + title);
            showKeyboard(AddEmergencyContactActivity.this);
            return true;
        } else if (binding.edtMobileNumberPrimary.getText().toString().contains(SharedPref.getDecryptMobileNo())) {
            binding.edtMobileNumberPrimary.requestFocus();
            binding.edtMobileNumberPrimary.setError("You can not add ur own Mobile Number");
            showKeyboard(AddEmergencyContactActivity.this);
            return true;
        } else if (binding.edtMobileNumberSecondary.getText().toString().contains(SharedPref.getDecryptMobileNo())) {
            binding.edtMobileNumberSecondary.requestFocus();
            binding.edtMobileNumberSecondary.setError("You can not add ur own Mobile Number");
            showKeyboard(AddEmergencyContactActivity.this);
            return true;
        } else
            return false;*/


    }

    private void addEmergencyContact() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AddEmergencyDetailsReq.PrimaryContactDetails primaryContactDetails = new AddEmergencyDetailsReq.PrimaryContactDetails(
                binding.edtNamePrimary.getText().toString(), binding.edtMobileNumberPrimary.getText().toString(), binding.edtRelationShipPrimary.getText().toString()
        );
        AddEmergencyDetailsReq.SecondaryContactDetails secondaryContactDetails = new AddEmergencyDetailsReq.SecondaryContactDetails(
                binding.edtNameSecondary.getText().toString(), binding.edtMobileNumberSecondary.getText().toString(), binding.edtRelationShipSecondary.getText().toString()
        );
        AddEmergencyDetailsReq addEmergencyDetailsReq = new AddEmergencyDetailsReq(primaryContactDetails, secondaryContactDetails);
        Call<AddEmergencyDetailsResp> call = apiInterfaceWyh.addEmergencyContact(SharedPref.getAuthToken(), addEmergencyDetailsReq);
        call.enqueue(new Callback<AddEmergencyDetailsResp>() {
            @Override
            public void onResponse(Call<AddEmergencyDetailsResp> call, Response<AddEmergencyDetailsResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {
                    refreshAuthToken();
                }
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_add_emergency_details_success));
                    Toast.makeText(context, "Contact details added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.body() != null) {
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_add_emergency_details_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddEmergencyDetailsResp> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.ice_add_emergency_details_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });


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
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    addEmergencyContact();
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
        if(fetchEmergencyDetailsResp != null && fetchEmergencyDetailsResp.getData() != null && fetchEmergencyDetailsResp.getData().size() > 0){
            binding.btnSubmit.setText("Update");
        }
    }

}
