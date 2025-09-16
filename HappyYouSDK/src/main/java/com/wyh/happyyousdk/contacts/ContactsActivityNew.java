package com.wyh.happyyousdk.contacts;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import static java.nio.file.Files.createLink;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.adapter.ContactAdapter;
import com.wyh.happyyousdk.databinding.ContactsActivityNewBinding;
import com.wyh.happyyousdk.model.request.rewards.MobileValidationRequest;
import com.wyh.happyyousdk.model.response.rewards.MobileValidationData;
import com.wyh.happyyousdk.model.response.rewards.MobileValidationResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivityNew extends AppCompatActivity {

    ContactsActivityNewBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    public String comName, shortLink, referralCode = "";
    int communityId;
    private static final int REQ_PICK_CONTACT = 3;
    private static final int CONTACT_PERMISSION_CODE = 1;
    String phone;
    boolean isSMS = false;
    boolean isWhatsapp = false;

    boolean isEmail = false;
    boolean isFromHRA = false;

    String comingFrom = "";


    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.contacts_activity_new);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        comName = getIntent().getStringExtra("name");
        communityId = getIntent().getIntExtra("id", 0);
        isFromHRA = getIntent().getBooleanExtra("isFromHRA", false);
        referralCode = SharedPref.getReferralCode();

        binding.includeBack.llBack.setOnClickListener(view -> finish());
        binding.includeBack.tvBack.setText("Share Invite");
        binding.includeBack.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeBack.ivBack.setColorFilter(getResources().getColor(R.color.white));

        if(getIntent().getStringExtra("comingFrom") != null && !getIntent().getStringExtra("comingFrom").equals("") ){
            comingFrom = getIntent().getStringExtra("comingFrom");
        }else{
            comingFrom = "";
        }

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "ShareInvite");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.btnShareViaSMS.setOnClickListener(view -> {
            openContacts();
            isSMS = true;
            isWhatsapp = false;
            isEmail = false;
        });

        binding.btnShareViaWhatsapp.setOnClickListener(view -> {
            openContacts();
            isSMS = false;
            isWhatsapp = true;
            isEmail = false;
        });

        binding.btnShareViaEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSMS = false;
                isWhatsapp = false;
                isEmail = true;

            }
        });

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }

    // Onclick handler
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
            ArrayList<String> phoneNumbers = new ArrayList<>();
            String lastNumber = "";
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
                            phone = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll(" ","");
                            if(lastNumber.equals(phone )){

                            }else{
                                lastNumber = phone.replace("+91","");
                                phoneNumbers.add(phone.replace("+91",""));

                                /*lastNumber = phone;
                                if(!phone.contains("+91")){
                                    phoneNumbers.add("+91"+phone);
                                }else{
                                    phoneNumbers.add(phone);

                                }*/
                            }
                            //Use contactName and contactNumber for your purposes
                        }
                        cursor2.close();
                    }
                }
                Set<String> set = new HashSet<>(phoneNumbers);
                phoneNumbers.clear();
                phoneNumbers.addAll(set);
                cursor1.close();
                if(phoneNumbers.size() != 0){
                    ContactDialoge(phoneNumbers);
                }
               // validateMobileNumbers();
//                    createLink("https://play.google.com/store/apps/details?id=com.wyh.happyyousdk&referrer="
//                        + SharedPref.getUserName() + "&referrerID=" + SharedPref.getUuid() + "&referredFor=AppInvite" + "&isFromHRA=" + isFromHRA);
            }
        }
    }




    public void ContactDialoge(ArrayList<String> contactNum) {
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomAlertDialogInfo);
            View view = LayoutInflater.from(context).inflate(R.layout.custom_contact_picker, null);
            builder.setView(view);


            ImageView imgClosed = view.findViewById(R.id.btnClosed);
            RecyclerView recyclerView = view.findViewById(R.id.contact_recycler_view);
            alertDialog = builder.create();
            alertDialog.setCancelable(true);

            ContactAdapter contactAdapter = new ContactAdapter(this, contactNum, new ContactAdapter.OnItemClickListener() {
                @Override
                public void clickOnContact(String contact) {

                    alertDialog.dismiss();
                    validateMobileNumbers(contact);
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();


            imgClosed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.hasFixedSize();
            recyclerView.setAdapter(contactAdapter);


            Rect displayRectangle = new Rect();
            Window window = getWindow();

            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

            alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.8f), RelativeLayout.LayoutParams.WRAP_CONTENT);
            alertDialog.show();
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }


    }

    public void  validateMobileNumbers(String number) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        if (phone.contains("+")) {
            phone = phone.substring(3, phone.length());
        }
        List<MobileValidationRequest> mobileValidationRequests = new ArrayList<>();
        MobileValidationRequest mobileValidationRequest = new MobileValidationRequest(number.replaceAll(" ", ""), "");
        mobileValidationRequests.add(mobileValidationRequest);
        Call<MobileValidationResponse> call = apiInterfaceWyh.validateMobileNumbers(SharedPref.getAuthToken(), mobileValidationRequests);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MobileValidationResponse> call, Response<MobileValidationResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.validate_community_mobile_success));
                    List<MobileValidationData> mobileValidationDataList = response.body().getData();
                    if (Boolean.parseBoolean(mobileValidationDataList.get(0).getIshappyuuser())) {
                        Toast.makeText(ContactsActivityNew.this, "Contact is already a HappyYou user", Toast.LENGTH_SHORT).show();
                    } else {
                        CommonUtils.clickEvent(ContactsActivityNew.this, "Invite", isSMS ? "Message" : "WhatsApp", "0", "Share", mobileValidationDataList.get(0).getMobile());
                        String message = "";
                        message = getResources().getString(R.string.download_app_msg_share_invite) + "\nAndroid: https://play.google.com/store/apps/details?id=com.wyh.happyyou"
                                + " \niOS: https://apps.apple.com/in/app/happyyou-by-kotak-life/id6448199779" + "Your Referral Code is " +
                                SharedPref.getReferralCode() + " " + getResources().getString(R.string.download_app_msg_powered_by);
                        if (isWhatsapp) {
                            sendWhatsappViaDefaultApp(phone, message);
                        } else if (isSMS) {
                            sendSMSViaDefaultApp(phone, message);
                        } else if (isEmail) {
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Referral Code");
                            intent.putExtra(Intent.EXTRA_TEXT, message);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }
                    }

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.validate_community_mobile_failed));
                    Toast.makeText(ContactsActivityNew.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MobileValidationResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.validate_community_mobile_failed));
                Toast.makeText(ContactsActivityNew.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendSMSViaDefaultApp(String phone, String msg) {

        Uri sms_uri = Uri.parse("smsto:" + phone);
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        sms_intent.setType("vnd.android-dir/mms-sms");
        sms_intent.setData(sms_uri);
        sms_intent.putExtra("sms_body", msg);
        startActivity(sms_intent);
    }

    public void sendWhatsappViaDefaultApp(String phone, String msg) {
        String selectedPhone = "";
        if(!phone.contains("+91")){
            selectedPhone = "+91"+phone;
        }else{
            selectedPhone = phone;

        }
        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + selectedPhone + "&text=" + msg);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);



        startActivity(sendIntent);
    }
    
}