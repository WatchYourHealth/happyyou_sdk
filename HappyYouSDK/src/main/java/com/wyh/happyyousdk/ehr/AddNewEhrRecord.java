package com.wyh.happyyousdk.ehr;

import static android.os.Build.VERSION.SDK_INT;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.todayDateInFormat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityAddNewEhrRecordBinding;
import com.wyh.happyyousdk.ehr.Interface.ItemRemove;
import com.wyh.happyyousdk.ehr.adapter.FileUploadListAdapter;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.request.ehr.AddHealthRecordRequest;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;
import com.wyh.happyyousdk.model.response.ehr.HealthRecordTypeResponse;
import com.wyh.happyyousdk.model.response.ehr.UploadFileResponse;

import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewEhrRecord extends AppCompatActivity implements ItemRemove {
    ActivityAddNewEhrRecordBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    ArrayList<String> reportList = new ArrayList<>();
    ArrayList<Integer> healthDataTypeIdList = new ArrayList<>();

    static FeedbackResponseData feedbackResponseData;

    String reportName, viewType;
    int healthDataTypeId;
    ArrayAdapter<String> spinnerAdapter;
    int REQUEST_CODE_CAMERA = 21;
    private final static int PDF_REQUEST_CODE = 102;
    public static int PICK_FILE_REQUEST_CODE = 111;
    ArrayList<FileData> fileDataList = new ArrayList<>();
    FileUploadListAdapter fileUploadListAdapter;
    List<UploadFileResponse.Datum> uploadedFileDataResponse;
    List<UploadFileResponse.Datum> newUploadedFileDataResponse;
    String imageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_ehr_record);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        getHealthRecordTypeList();

        binding.llUploadFile.setOnClickListener(view -> showPopup());

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Add New Record");


        viewType = this.getIntent().getStringExtra("viewType");
        /*if(viewType.equalsIgnoreCase("Diet Plans")){
            binding.etName.setHint("Enter Name");
        }*/

        binding.btnEhrRecordUpload.setOnClickListener(view -> {
            binding.btnEhrRecordUpload.setEnabled(false);
            if (!binding.etName.getText().toString().equals("")) {
                if (uploadedFileDataResponse.size() > 0 && healthDataTypeId != 0) {
                    addHealthRecord();
                }else {
                    binding.btnEhrRecordUpload.setEnabled(true);
                }
            } else {
                binding.btnEhrRecordUpload.setEnabled(true);
                binding.etName.setError("Please enter title");
//                Toast.makeText(context, "Please enter Test name", Toast.LENGTH_SHORT).show();
            }
        });

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "AddHealthLocker");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.ivHome.setOnClickListener(view -> {
            Intent i = new Intent(this, NewDashboardActivity.class);
            startActivity(i);
            finish();
        });


    }

    private void addHealthRecord() {
        progressDialog.show();

        String currentDate = todayDateInFormat("yyyy-MM-dd");

        AddHealthRecordRequest addHealthRecordRequest = new AddHealthRecordRequest(healthDataTypeId, currentDate, "", binding.etName.getText().toString(), uploadedFileDataResponse);

        Call<CommonSuccessResponse> call = apiInterfaceWyh.addHealthRecord(SharedPref.getAuthToken(), addHealthRecordRequest);

        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.body() != null && response.body().isSuccess() && response.code() == 200) {
                    fileDataList.clear();
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.add_health_locker_success));
                    finish();
                    Intent i = new Intent(context, PrescriptionUploadedActivity.class);
                    i.putExtra("reportname", reportName);
                    i.putExtra("reporttitle",binding.etName.getText().toString());
                    if (response.body().getRewards() != null && response.body().getRewards().getReward() != null)
                        i.putExtra("rewards", response.body().getRewards().getReward());
                    if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null)
                        i.putExtra("rewardsBonus", response.body().getRewards().getBonusRewards());
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null)
                        i.putExtra("stamp", response.body().getEnGTokens().getTokens());
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null)
                        i.putExtra("stampBonus", response.body().getEnGTokens().getBonusTokens());
                    if(response.body().getFeedbackDetails() != null && response.body().getFeedbackDetails() != null && response.body().getFeedbackDetails().getStarConfig() != null){
                        i.putExtra("feedback","showFeedback");
                        feedbackResponseData = response.body().getFeedbackDetails();
                    }

                    i.putExtra("voucher", new Gson().toJson(response.body().getFreeVoucher()));
                    startActivity(i);
                }
                else if(response.code() == 403){
                    Toast.makeText(AddNewEhrRecord.this, "File size should be less than 5MB", Toast.LENGTH_SHORT).show();
                }
                else {
                    binding.btnEhrRecordUpload.setEnabled(true);
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.add_health_locker_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    binding.btnEhrRecordUpload.setEnabled(true);
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.add_health_locker_failed));
                }
//                Log.v("Ehr Response Err", t.getMessage());
            }
        });
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
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        reportList.add(response.body().getData().get(i).getName());
                        healthDataTypeIdList.add(response.body().getData().get(i).getId());
                    }
                    if (reportList.size() > 0) {
                        getSpinnerEhrTypes(reportList, healthDataTypeIdList);
                    }
                    //Log.v("Ehr_Multipart_", new Gson().toJson(reportList));
                } else if (response.code() == 401) {
                    refreshAuthToken();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_health_locker_types_failed));
                    Toast.makeText(AddNewEhrRecord.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
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


    private void getSpinnerEhrTypes(ArrayList<String> healthrecord, ArrayList<Integer> healthDataTypeIds) {

        spinnerAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, healthrecord);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.reportType.setPrompt("Select Type");
        binding.reportType.setAdapter(spinnerAdapter);
//        if (viewType.equals("Fitness Plus")) {
        int position = spinnerAdapter.getPosition(viewType);
        binding.reportType.setSelection(position);
//        }

        binding.reportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.v("Url_Response_spinner_item", activityNames.get(position) + "\n" + activityId);
                reportName = healthrecord.get(position);
                healthDataTypeId = healthDataTypeIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void checkImagePicker(){
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            openImagePicker();
        }else{
            openGalleryOnly();
        }
    }

    public void openImagePicker(){
        try{
            Intent intent = new Intent();
            intent.putExtra(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,1);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void openGalleryOnly() {
        ImagePicker.create(this)
                .includeVideo(false).multi()
                .imageDirectory("Camera")
                .multi()
                .enableLog(true)
                .showCamera(false)
                .start();
    }

    private void showPopup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.img_upload_options_dialog, null);
        alertDialog.setView(view);
        LinearLayout llCamera = view.findViewById(R.id.ll_camera);
        LinearLayout llGallery = view.findViewById(R.id.llGallery);
        LinearLayout llFile = view.findViewById(R.id.llFile);
        ImageView btnClose = view.findViewById(R.id.close);
        AlertDialog alertDialog1;
        alertDialog1 = alertDialog.create();
        alertDialog1.show();

        llCamera.setOnClickListener(v -> {
            try {
                openCameraOnly();
                alertDialog1.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        llGallery.setOnClickListener(v -> {
            checkImagePicker();
            alertDialog1.dismiss();
        });
        llFile.setOnClickListener(v -> {
            showFileChooser();
            alertDialog1.dismiss();
        });
        btnClose.setOnClickListener(v -> alertDialog1.dismiss());
    }

    public void openCameraOnly() throws IOException {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(context,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission
                            .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    REQUEST_CODE_CAMERA);
        } else {
            // write your logic code if permission already granted
            ImagePicker.cameraOnly().imageDirectory("Camera").start(this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0) {
                //boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writeExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    ImagePicker.cameraOnly().imageDirectory("Camera").start(this);
                } else {
                    if (cameraPermission && writeExternalFile) {
                        ImagePicker.cameraOnly().imageDirectory("Camera").start(this);
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private String getFileName(Uri uri) throws IllegalArgumentException {
        // Obtain a cursor with information regarding this uri
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            throw new IllegalArgumentException("Can't obtain file name, cursor is empty");
        }
        cursor.moveToFirst();
        String fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
        cursor.close();
        return fileName;
    }

    @SuppressLint("Range")
    private String getRealPathFromURI(Uri uri, String uriString, File myFile) {
        String displayName = null;
        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.getName();
        }
        return displayName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<MultipartBody.Part> parts = new ArrayList<>();
        if(requestCode == 1){
            if(data == null){
                Toast.makeText(context, "No image Selected", Toast.LENGTH_SHORT).show();
            }else{
                if(data.getData() != null){
                    Uri imageURI = data.getData();
                    saveImage(imageURI);
                }else{
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri imageURI = item.getUri();
                        saveImage(imageURI);
                    }
                }
            }

        }

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    if (data.getClipData() != null) {
                        int itemCount = data.getClipData().getItemCount();

                        for (int i = 0; i < itemCount; i++) {
                            Uri uri = data.getClipData().getItemAt(i).getUri();
                            String fileName = getFileName(data.getClipData().getItemAt(i).getUri());
                            String extension = fileName.split("\\.")[1];
                            String mimeType = null;
                            //Log.d("File", "Extension: " + extension);
                            if (extension.equals("pdf") || extension.equals("doc") || extension.equals("docx") || extension.equals("xls") || extension.equals("xlsx")) {
                                mimeType = "application/" + extension;
                            }
                            //Log.v("File", i + " : " + uri.getPath());
                            String uriString = uri.toString();
                            File myFile = new File(uriString);
                            String path = myFile.getAbsolutePath();
                            String displayName = getRealPathFromURI(uri, uriString, myFile);
                            File file1 = null;
                            try {
                                InputStream inputStream;
                                if (uriString.startsWith("content://")) {
                                    inputStream = getContentResolver().openInputStream(uri);
                                } else {
                                    inputStream = new FileInputStream(myFile);
                                }
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                byte[] buf = new byte[1024];
                                try {
                                    for (int readNum; (readNum = inputStream.read(buf)) != -1; ) {
                                        bos.write(buf, 0, readNum); //no doubt here is 0
                                        //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                                        //System.out.println("PDF read " + readNum + " bytes,");
                                    }
                                } catch (IOException ex) {
                                    //Log.d("File", ex.getMessage());
                                }
                                byte[] bytes = bos.toByteArray();

                                //File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Happy/");
                                File folder = new File(getFilesDir() + "/Happy/");

                                if (!folder.exists()) {
                                    folder.mkdirs();
                                } else {
                                    //Log.d("File", "Folder not exists");
                                }
                                //copyDirectoryOneLocationToAnotherLocation(myFile, file1);
                                file1 = new File(folder, displayName);
                                if(!file1.exists()) {
                                    try {
                                        file1.createNewFile();
                                    } catch (IOException e) {
                                        //Log.d("File", e.getMessage());
                                    }
                                    FileOutputStream fos = new FileOutputStream(file1, true);
                                    fos.write(bytes);
                                    fos.flush();
                                    fos.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //Log.v("File", e.getMessage());
                            }
                            String path1 = file1.getPath();
                            FileData fileData = new FileData();
                            fileData.setPath(path1);
                            fileData.setMimeType(mimeType);
                            Log.d("file size 1", file1.length()+" "+file1.getName());
                            if (file1.length() > 5000000) {
                                fileDataList.clear();
                                Toast.makeText(this, "File size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                            } else {
                                fileDataList.add(fileData);
//                                setFileData(fileDataList);
                                //openAlertDialog();
                            }
                            //Log.v("File", path1);
                        }
                        //imagePdfList.addAll(pdfList);
                    } else {
                        String fileName = getFileName(data.getData());
                        String extension = fileName.split("\\.")[1];
                        String mimeType = null;
                        //Log.d("File", "Extension: " + extension);
                        if (extension.equals("pdf") || extension.equals("doc") || extension.equals("docx") || extension.equals("xls") || extension.equals("xlsx")) {
                            mimeType = "application/" + extension;
                        }
                        Uri uri = data.getData();
                        String uriString = uri.toString();
                        File myFile = new File(uriString);
                        String path = myFile.getAbsolutePath();
                        String displayName = getRealPathFromURI(uri, uriString, myFile);

                        File file1 = null;
                        try {
                            InputStream inputStream;
                            if (uriString.startsWith("content://")) {
                                inputStream = getContentResolver().openInputStream(uri);
                            } else {
                                inputStream = new FileInputStream(myFile);
                            }
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            byte[] buf = new byte[1024];
                            try {
                                for (int readNum; (readNum = inputStream.read(buf)) != -1; ) {
                                    bos.write(buf, 0, readNum); //no doubt here is 0
                                    //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                                    //System.out.println("PDF read " + readNum + " bytes,");
                                }
                            } catch (IOException ex) {
                                Log.d("File", ex.getMessage());
                                Toast.makeText(context, ""+ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            byte[] bytes = bos.toByteArray();

                            //File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Happy/");
                            File folder = new File(getFilesDir() + "/Happy/");

                            if (!folder.exists()) {
                                folder.mkdirs();
                            } else {
                                //Log.d("File", "Folder not exists");
                            }
                            //copyDirectoryOneLocationToAnotherLocation(myFile, file1);
                            file1 = new File(folder, displayName);
                            Log.d("file size 2", !file1.exists()+"");
                            if(!file1.exists()){
                                try {
                                    file1.createNewFile();
                                } catch (IOException e) {
                                    Log.d("File", e.getMessage());
                                }
                                FileOutputStream fos = new FileOutputStream(file1, true);
                                fos.write(bytes);
                                fos.flush();
                                fos.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "File format not supported", Toast.LENGTH_SHORT).show();
                            Log.d("ty", e.getMessage());
                            //Log.v("File", e.getMessage());
                        }
                        String path1 = file1.getPath();
                        FileData fileData = new FileData();
                        fileData.setPath(path1);
                        fileData.setMimeType("mimeType");
                        Log.d("file path:", path1);
                        Log.d("file path:", file1.length()+"");
                        Log.d("file size 2", file1.length()+" "+file1.getName());
                        if (file1.length() > 5000000) {
                            fileDataList.clear();
                            Toast.makeText(this, "File size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                        } else {
                            fileDataList.add(fileData);
//                            setFileData(fileDataList);
                            //openAlertDialog();
                        }
                        //Log.v("File", path1);
                    }
                    //Log.d("File", new Gson().toJson(fileDataList));
                } catch (Exception e) {
                    Log.d("File", e.getMessage());
                    Toast.makeText(context, "File format not supported", Toast.LENGTH_SHORT).show();
                    //  showCustomErrorToast(AddEditEhrActivityNew.this, , getString(R.string.file));
                }
            } else {
                //Log.v("File", "Null");
            }
        }
        long allFileSize = 0;
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<Image> image2 = ImagePicker.getImages(data);
            for (int i = 0; i < image2.size(); i++) {

                // if (image != null) {
                //Log.d("File_URI", "" + image.getUri());
                FileData fileData = new FileData();
                fileData.setPath(getFilePathFromImage(image2.get(i)));
                long singleFileSize = getFileSizeFromPath(fileData.getPath());
                FileData fileDataNew = new FileData();
                Log.d("comparedata singleFileSize", singleFileSize+"");
                File file1 = CommonUtils.compressImage(context, image2.get(i).getUri());
                fileDataNew.setPath(file1.getPath());
                fileDataNew.setMimeType("application/png");
                imageName = file1.getName();
                allFileSize += getFileSizeFromPath(fileData.getPath());
                fileDataList.add(fileDataNew);

              /*  if(singleFileSize > 1000000){
                  *//*  FileData fileDataNew = new FileData();
                    Log.d("comparedata singleFileSize", singleFileSize+"");
                    File file1 = CommonUtils.compareImage(context, image2.get(i).getUri());
                    fileDataNew.setPath(file1.getPath());
                    fileDataNew.setMimeType("application/png");
                    imageName = file1.getName();
                    allFileSize += getFileSizeFromPath(fileDataNew.getPath());*//*
                    if (file1.length() > 5000000) {
                        Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                    } else {
                        fileDataList.add(fileDataNew);
                    }
                }else{
                    *//*allFileSize += getFileSizeFromPath(fileData.getPath());
                    fileDataList.add(fileData);*//*
                }*/

            }
        }
        for (int i = 0; i < fileDataList.size(); i++) {
            parts.add(prepareFilePart("EHRupload", fileDataList.get(i).getPath()));
        }
        if (fileDataList.size() > 0) {
            if (allFileSize > 5000000) {
                Toast.makeText(this, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile(parts);
            }
        }
    }

    public void saveImage(Uri imageURI){
        FileData fileData = new FileData();
        fileData.setPath(CommonUtils.getRealPathFromURI(imageURI,context));
        FileData fileDataNew = new FileData();
        File file1 = CommonUtils.compressImageToJPEG(context, imageURI);
        fileDataNew.setPath(file1.getPath());
        fileDataNew.setMimeType("application/png");
        imageName = file1.getName();
        if (file1.length() > 5000000) {
            Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
        } else {
            fileDataList.add(fileDataNew);
        }
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String path) {
        File file = new File(path);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        //Log.v("PDF MimeType", mimeType);
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        //RequestBody requestFile = RequestBody.create(MediaType.parse(("multipart/form-data")), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        //return MultipartBody.Part.createFormData(partName, file.getName());
    }


    private long getFileSizeFromPath(String filePath) {
        if (filePath == null)
            return 0;
        File file = new File(filePath);
        return file.length();
    }

    private String getFilePathFromImage(Image image) {
        if (image == null)
            return null;
        if (!TextUtils.isEmpty(image.getPath()) && new File(image.getPath()).exists()) {
            return image.getPath();
        } else if (image.getUri() != null) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getContentResolver().query(image.getUri(),
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgFilePath = null;
            if (columnIndex != -1) {
                imgFilePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return imgFilePath;
        }
        return null;
    }

    @Override
    public void onItemRemove(int position) {
//        Log.v("Data", position + "Item removed reached!");
        //imagePdfList.remove(position);
//        //imagePdfAdapter.notifyItemRangeChanged(position, imagePdfList.size());
//        fileUploadListAdapter.notifyItemRangeChanged(position, fileDataList.size());
        uploadedFileDataResponse.remove(position);
        fileUploadListAdapter.notifyDataSetChanged();

        if (uploadedFileDataResponse.size() == 0) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }

        //notifyAll();
    }

    private void uploadFile(List<MultipartBody.Part> parts) {
        progressDialog.show();
        if (parts.size() > 0 && reportName != null) {
            Call<UploadFileResponse> call = apiInterfaceWyh.ehrFileUploadResponse(SharedPref.getAuthToken(), parts);
            //Log.v("Ehr_Multipart", call.request().url().toString() + "\n" + SharedPref.getAuthToken());

            call.enqueue(new Callback<UploadFileResponse>() {
                @Override
                public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    String responseStr = new Gson().toJson(response.body());
                    Log.v("Ehr_Multipart_res", new Gson().toJson(response.body()));

                    /*fileDataList.clear();*/
                    deleteImage();
                    fileDataList.clear();
                    if(response.code() == 403){
                        Toast.makeText(AddNewEhrRecord.this, "File size should be less than 5MB", Toast.LENGTH_SHORT).show();
                    }

                    if (response.body() == null) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_locker_upload_files_failed));
                        Toast.makeText(getApplicationContext(), "File not uploaded!",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (response.body() != null && response.body().getSuccess() && response.code() == 200) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_locker_upload_files_success));
                        uploadedFileDataResponse = response.body().getData();
                        if (uploadedFileDataResponse.size() > 0) {
//                            binding.llTopLayout.setVisibility(View.GONE);
                            setUploadedFileData(uploadedFileDataResponse);
                            binding.btnEhrRecordUpload.setVisibility(View.VISIBLE);
//                            binding.btnDelete.setVisibility(View.VISIBLE);
                            Gson gson = new Gson();
                            String UploadedResponse = gson.toJson(response.body());
                            SharedPref.putUploadEhrResponse(UploadedResponse);
                        } else {
                            binding.btnEhrRecordUpload.setVisibility(View.GONE);
//                            binding.btnDelete.setVisibility(View.GONE);
                        }
//                        List<EhrFileUploadResponse> ehrFileUploadResponseList = response.body();
//                        addEhr(ehrFileUploadResponseList);
                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_locker_upload_files_failed));
                    }
                }

                @Override
                public void onFailure(Call<UploadFileResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_locker_upload_files_failed));
                        deleteImage();
                        fileDataList.clear();
                    }
//                        Log.v("Ehr_Multipart_res", t.getMessage());
                }
            });
        }
    }


    private void setUploadedFileData(List<UploadFileResponse.Datum> uploadedFileDataResponse) {
        fileUploadListAdapter = new FileUploadListAdapter(context, uploadedFileDataResponse, reportName, this::onItemRemove, "AddNewEhrRecord");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvReportList.setLayoutManager(linearLayoutManager);
        binding.rvReportList.setAdapter(fileUploadListAdapter);
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
//                Log.v("Permission", "Permission is granted");
                return true;
            } else {
//                Log.v("Permission", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, PDF_REQUEST_CODE);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v("Permission", "Permission is granted");
            return true;
        }
    }

    public void showFileChooser() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

            // Update with mime types
            intent.setType("application/pdf");

            String[] mimeTypes = {"application/pdf"};

            // Update with additional mime types here using a String[].
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

            // Only pick openable and local files. Theoretically we could pull files from google drive
            // or other applications that have networked files, but that's unnecessary for this example.
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            // REQUEST_CODE = <some-integer>
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        } else if (isStoragePermissionGranted()) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

            // Update with mime types
            intent.setType("application/pdf");

            String[] mimeTypes = {"application/pdf"};

            // Update with additional mime types here using a String[].
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

            // Only pick openable and local files. Theoretically we could pull files from google drive
            // or other applications that have networked files, but that's unnecessary for this example.
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            // REQUEST_CODE = <some-integer>
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        }
    }


    public void deleteImage(){
        if(imageName != ""){
            CommonUtils.deleteImage(imageName);
            imageName = "";
        }
    }



}