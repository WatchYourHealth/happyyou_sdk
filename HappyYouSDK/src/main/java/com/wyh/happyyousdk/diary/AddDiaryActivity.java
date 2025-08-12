package com.wyh.happyyousdk.diary;

import static android.os.Build.VERSION.SDK_INT;
import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;
import static com.wyh.happyyousdk.utils.Constants.TAG_TOP_UP_EVENT;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityAddDiaryBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.diary.AddDiaryRequest;
import com.wyh.happyyousdk.model.request.diary.ImageDeleteRequest;
import com.wyh.happyyousdk.model.request.diary.UpdateDairyRequest;
import com.wyh.happyyousdk.model.request.earnAndGrab.StartActivityRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.request.rewards.StartRewardsActivityRequest;
import com.wyh.happyyousdk.model.response.diary.AddDiaryResponse;
import com.wyh.happyyousdk.model.response.diary.DeleteDiaryResponse;
import com.wyh.happyyousdk.model.response.diary.UploadUserFileResponse;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.APILogConstant;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDiaryActivity extends AppCompatActivity implements ScratchListener {
    ActivityAddDiaryBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    AlertDialog alertDialogBonusRewards;
    Context context;
    String came_from, date, title, content;
    int id, imageId;
    ArrayList<FileData> fileDataList = new ArrayList<>();
    String fileUploadKey = "JournalUpload";
    List<MultipartBody.Part> parts = new ArrayList<>();
    private static final int CAMERA_PERMISSION_CODE = 100;
    DiaryEventListResponse.Datum selectedItem;
    int REQUEST_CODE_CAMERA = 21;

    boolean isPositiveBtn = false;
    boolean isDeleted = false;
    boolean isStamp;
    int stampId = -1;
    AlertDialog alertDialogStamp, alertDialogBonusStamp;

    List<DiaryEventListResponse.Datum> eventListData;
    List<String> eventList;
    List<DiaryEventListResponse.Datum> getEvents = new ArrayList<>();
    List<String> activityList = new ArrayList<>();

    String imageName = "";
    String levelData, leveldataStr, engData, engdataStr, topUpData, topUpdataStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_diary);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);


        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        Glide.with(context)
                .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "ic_unwind_bg.png")
                .into(binding.ivBackground);

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            came_from = extras.getString("came_from");
            title = extras.getString("title");
            content = extras.getString("content");
            date = extras.getString("date");
            id = extras.getInt("id");
            imageId = extras.getInt("imageId");
            getEvents = (List<DiaryEventListResponse.Datum>) extras.getSerializable("dataList");
            String imagePath = extras.getString("imagePath");
            if (imagePath != null && !Objects.equals(imagePath, "") && !(imagePath.isEmpty())) {
                isDeleted = true;
                Glide.with(context)
                        .load(imagePath)
                        .placeholder(R.drawable.dummy_image)
                        .into(binding.imageView);
                String fileName = imagePath.substring(imagePath.lastIndexOf('/') + 1);
                binding.tvFileName.setText(fileName);
                binding.llFileImage.setVisibility(View.VISIBLE);
            }
            if (Objects.equals(came_from, "share")) {
                binding.activitySpinnerLayout.setVisibility(View.VISIBLE);
                binding.llSpinner.setVisibility(View.VISIBLE);
                levelData = extras.getString("levelData");
                leveldataStr = extras.getString("leveldataStr");
                engData = extras.getString("engData");
                engdataStr = extras.getString("engdataStr");
                topUpData = extras.getString("topUpData");
                topUpdataStr = extras.getString("topUpdataStr");
                binding.editTitle.setKeyListener(null);

            } else {
                binding.activitySpinnerLayout.setVisibility(View.GONE);
                binding.llSpinner.setVisibility(View.GONE);
            }

            JSONObject customObj = new JSONObject();
            try {
                customObj.put("PAGE_ID", "ShareAndEarnDiary");
                customObj.put("came_from", "share");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            binding.activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!binding.activitySpinner.getSelectedItem().toString().equalsIgnoreCase("Select your category")) {
                        if (binding.activitySpinner.getSelectedItem().toString().equalsIgnoreCase("level")) {
                            APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_3dots_se_sc_l(), context);
                        } else if (binding.activitySpinner.getSelectedItem().toString().equalsIgnoreCase("earn and grab")) {
                            APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMz_unwind_md_3dots_se_sc_eg(), context);
                        } else {
                            APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMz_unwind_md_3dots_se_sc_tu(), context);
                        }
                    }

                    if (!binding.activitySpinner.getSelectedItem().toString().equalsIgnoreCase("Select your category")) {
                        if (binding.activitySpinner.getSelectedItem().toString().equalsIgnoreCase("level")) {
                            Type type1 = new TypeToken<List<DiaryEventListResponse.Datum>>() {
                            }.getType();
                            Type type2 = new TypeToken<List<String>>() {
                            }.getType();
                            eventListData = new Gson().fromJson(levelData, type1);
                            eventList = new Gson().fromJson(leveldataStr, type2);
                            binding.editTitle.setKeyListener(null);
                            setRvEvent(eventList, "Select your activity", "level");
                        } else if (binding.activitySpinner.getSelectedItem().toString().equalsIgnoreCase("earn and grab")) {
                            Type type1 = new TypeToken<List<DiaryEventListResponse.Datum>>() {
                            }.getType();
                            Type type2 = new TypeToken<List<String>>() {
                            }.getType();
                            eventListData = new Gson().fromJson(engData, type1);
                            eventList = new Gson().fromJson(engdataStr, type2);
                            binding.editTitle.setKeyListener(null);
                            setRvEvent(eventList, "Select your activity", "earn and grab");
                        } else {
                            Type type1 = new TypeToken<List<DiaryEventListResponse.Datum>>() {
                            }.getType();
                            Type type2 = new TypeToken<List<String>>() {
                            }.getType();
                            eventListData = new Gson().fromJson(topUpData, type1);
                            eventList = new Gson().fromJson(topUpdataStr, type2);
                            binding.editTitle.setKeyListener(null);
                            setRvEvent(eventList, "Select your activity", "topup");
                        }
                    } else {
                        binding.levelSpinner.setSelection(0);
                        binding.levelSpinner.setEnabled(false);
                        binding.editTitle.setText("");
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

        setToolBar();
        setData();


        binding.save.setOnClickListener(v -> {
            saveBtn();
        });

        binding.addImage.setOnClickListener(v -> {
            if (came_from.equals("share")) {
                APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_3dots_se_ai(), context);
            } else {
                APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_mj_p_md_ai(), context);
            }
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        });

        binding.ivClosed.setOnClickListener(view -> {

            if (Objects.equals(came_from, "edit") && isDeleted) {
                showPopupDeleteImage(imageId);
            } else {
                fileDataList.clear();
                parts.clear();
                binding.llFileImage.setVisibility(View.GONE);
            }
        });


    }

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(AddDiaryActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AddDiaryActivity.this, new String[]{permission}, requestCode);
        } else {
            showPopup();
        }
    }

    private void saveBtn() {
        String currentTime = CommonUtils.todayDate();
        if (!(binding.editTitle.getText().toString().isEmpty())) {

            if (Objects.equals(came_from, "edit") || Objects.equals(came_from, "add")) {
                if (!(binding.editContent.getText().toString().isEmpty()) && binding.editContent.getText().length() > 15) {
                    if (Objects.equals(came_from, "edit")) {
                        updateDiary(currentTime, binding.editTitle.getText().toString(), binding.editContent.getText().toString());
                    } else if (Objects.equals(came_from, "add")) {
                        addDiary(currentTime, binding.editTitle.getText().toString(), binding.editContent.getText().toString());
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (!(binding.editContent.getText().toString().isEmpty()) && binding.editContent.getText().length() > 15) {
                    if (selectedItem != null && selectedItem.getEventCategory().toLowerCase().equals("topup")) {
                        if (imageValidation()) {
                            startTopUps(selectedItem);
                        }
                    } else if (selectedItem != null && selectedItem.getEventCategory().toLowerCase().equals("eng")) {
                        if (imageValidation()) {
                            startNewActivityEnG(selectedItem);
                        }
                    } else if (selectedItem != null && selectedItem.getEventCategory().toLowerCase().equals("reward_event")) {
                        if (imageValidation()) {
                            startNewActivity(selectedItem);
                        }
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.at_least_15_char), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(context, "Add Title", Toast.LENGTH_SHORT).show();
        }

    }


    private Boolean imageValidation() {
        if (parts.size() > 0) {
            return true;
        } else {
            Toast.makeText(context, "Please select the image", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) {
            try {
                if (data.getData() != null) {
                    Uri imageURI = data.getData();
                    saveImage(imageURI);
                } else {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri imageURI = item.getUri();
                        saveImage(imageURI);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            fileDataList.clear();
            parts.clear();

            List<Image> image2 = ImagePicker.getImages(data);
            FileData fileData = new FileData();
            fileData.setPath(getFilePathFromImage(image2.get(0)));
            long singleFileSize = getFileSizeFromPath(fileData.getPath());
            File imgFile = new File(String.valueOf(fileData.getPath()));
            FileData fileDataNew = new FileData();

            if (singleFileSize > 1000000) {
                File file1 = CommonUtils.compressImage(context, image2.get(0).getUri());
                Log.d("comparedata singleFileSize", singleFileSize + "");
                fileDataNew.setPath(file1.getPath());
                fileDataNew.setMimeType("application/png");
                imageName = file1.getName();
                if (file1.length() > 5000000) {
                    fileDataList.clear();
                    parts.clear();
                    Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                } else {
                    fileDataList.add(fileDataNew);
                }
            } else {
                fileDataList.add(fileData);
            }

            if (fileDataList.size() > 0) {
                Glide.with(context)
                        .asBitmap()
                        .load(image2.get(0).getUri())
                        .error(R.drawable.dummy_image)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.imageView);
                binding.tvFileName.setText(image2.get(0).getName());
                binding.llFileImage.setVisibility(View.VISIBLE);
                parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
            }

        }

    }


    public void saveImage(Uri imageURI) {
        //List<MultipartBody.Part> parts = new ArrayList<>();

        FileData fileData = new FileData();
        fileData.setPath(CommonUtils.getRealPathFromURI(imageURI, context));
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


        if (fileDataList.size() > 0) {
            Glide.with(context)
                    .asBitmap()
                    .load(imageURI)
                    .error(R.drawable.dummy_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.imageView);

            binding.tvFileName.setText(imageName);
            binding.llFileImage.setVisibility(View.VISIBLE);
            parts.clear();
            parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String path) {
        try {
            File file = new File(path);
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        } catch (Exception e) {
            Log.d("call", "error");
        }

        return null;
    }

    private String getFilePathFromImage(Image image) {
        if (image == null)
            return null;
        if (!TextUtils.isEmpty(image.getPath()) && new File(image.getPath()).exists()) {
            return image.getPath();
        } else if (image.getUri() != null) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(image.getUri(),
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


    private long getFileSizeFromPath(String filePath) {
        if (filePath == null)
            return 0;
        File file = new File(filePath);
        return file.length();
    }

    public void openGalleryOnly() {
        ImagePicker.create(this)
                .includeVideo(false).multi()
                .imageDirectory("Camera")
                .limit(1)
                .enableLog(true)
                .showCamera(false)
                .start();
    }

    public void openImagePicker() {
        try {
            Intent intent = new Intent();
            intent.putExtra(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showPopup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.cg_img_upload_option, null);
        alertDialog.setView(view);
        LinearLayout llCamera = view.findViewById(R.id.ll_camera);
        LinearLayout llGallery = view.findViewById(R.id.llGallery);
        ImageView btnClose = view.findViewById(R.id.close);
        AlertDialog alertDialog1;
        alertDialog1 = alertDialog.create();
        alertDialog1.show();


        llCamera.setOnClickListener(v -> {
            if (came_from.equals("share")) {
                APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_3dots_se_ai_c(), context);
            } else {
                APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_mj_p_md_ai_cm(), context);
            }
            try {
                openCameraOnly();
                alertDialog1.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        llGallery.setOnClickListener(v -> {
            if (came_from.equals("share")) {
                APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_3dots_se_ai_g(), context);
            } else {
                APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_mj_p_md_ai_g(), context);
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                openImagePicker();
            } else {
                openGalleryOnly();
            }
            alertDialog1.dismiss();
        });

        btnClose.setOnClickListener(v -> {
            if (came_from.equals("share")) {
                APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_3dots_se_ai_cl(), context);
            } else {
                APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_mj_p_md_ai_c(), context);
            }

            alertDialog1.dismiss();
        });
    }

    public void openCameraOnly() throws IOException {
        ImagePicker.cameraOnly().imageDirectory("Camera").start(this);
    }

    private void setData() {
        if (Objects.equals(came_from, "edit")) {
            binding.editTitle.setText(title);
            binding.editContent.setText(content);
        }
    }

    private void setToolBar() {
        binding.includeToolbar.tvBack.setText("My Diary");
        binding.includeToolbar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.includeToolbar.ivMenu.setVisibility(View.GONE);
    }


    private void addDiary(String date, String title, String content) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        if (came_from.equals("share")) {
            APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_3dots_se_save(), context);
        } else {
            APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_mj_p_md_save(), context);
        }

        AddDiaryRequest request = new AddDiaryRequest(date, title, content);
        Call<AddDiaryResponse> call = apiInterfaceWyh.addDiary(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<AddDiaryResponse>() {
            @Override
            public void onResponse(Call<AddDiaryResponse> call, Response<AddDiaryResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                AddDiaryResponse res = response.body();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.add_diary_success));

                    Toast.makeText(context, "Journal added successfully.", Toast.LENGTH_SHORT).show();
                    if (response.body().getData() > 0) {
                        if (parts.size() > 0) {
                            uploadFileWithContent(parts, response.body().getData());
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("isAdded", true);
                            intent.putExtra("came_from", came_from);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.add_diary_failed));
                    Toast.makeText(context, "API" + getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddDiaryResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.add_diary_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDiary(String date, String title, String content) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        UpdateDairyRequest request = new UpdateDairyRequest(id, date, title, content);
        Call<DeleteDiaryResponse> call = apiInterfaceWyh.updateDiary(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<DeleteDiaryResponse>() {
            @Override
            public void onResponse(Call<DeleteDiaryResponse> call, Response<DeleteDiaryResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.update_diary_success));

                    Log.d("update json", new Gson().toJson(response.body()));
                    if (parts.size() > 0) {
                        uploadFileWithContent(parts, id);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("isAdded", true);
                        intent.putExtra("came_from", came_from);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.update_diary_failed));
                    Toast.makeText(context, "API" + getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteDiaryResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.update_diary_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileWithContent(List<MultipartBody.Part> parts, int diaryId) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
                .addFormDataPart("DiaryId", String.valueOf(diaryId))
                .build();

        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "UserJournal/UploadFiles")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        Log.d("new request", new Gson().toJson(request));

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                fileDataList.clear();
                deleteImageFile();
                Log.d("upload file", e.getMessage());
//                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();


                String res = response.body().string();
                Log.d("upload file", res);
                if (response.code() == 200 && response.body() != null) {
                    fileDataList.clear();
                    deleteImageFile();
                    UploadUserFileResponse uploadUserFileResponse = new Gson().fromJson(res, UploadUserFileResponse.class);
                    if (uploadUserFileResponse.isSuccess()) {
                        Intent intent = new Intent();
                        intent.putExtra("isAdded", true);
                        intent.putExtra("came_from", came_from);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPopup();
            } else {
                /*Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);*/
                Toast.makeText(AddDiaryActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

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

    private void startNewActivity(DiaryEventListResponse.Datum levelActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        StartRewardsActivityRequest request = new StartRewardsActivityRequest(levelActivity.getEventId(), TAG_REWARD_EVENT);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.startRewardsActivity(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_success));
                    uploadFileWithContentLevel(parts, selectedItem);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileWithContentLevel(List<MultipartBody.Part> parts, DiaryEventListResponse.Datum levelActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
                .addFormDataPart("eventId", String.valueOf(levelActivity.getEventId()))
                .addFormDataPart("eventType", "JournalUpload")
                .addFormDataPart("eventName", levelActivity.getEventTag())
                .addFormDataPart("yesNo", "")
                .addFormDataPart("journalContent", binding.editContent.getText().toString())
                .addFormDataPart("eventCategory", TAG_REWARD_EVENT)
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "Rewards/EarnRewards")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                fileDataList.clear();
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                fileDataList.clear();
                if (response.code() == 200 && response.body() != null) {
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                                Toast.makeText(context, getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                            }
                            if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                                Toast.makeText(context, getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                            }

                            showRewardsPopupDialogBox();

                        }
                    });
                }
            }
        });
    }

    private void startNewActivityEnG(DiaryEventListResponse.Datum eandBPendingActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        StartActivityRequest request = new StartActivityRequest(eandBPendingActivity.getEventId());
        Call<CommonSuccessResponse> call = apiInterfaceWyh.startEAndGActivity(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_start_activity_success));
                    uploadFileWithContentEnG(parts, selectedItem);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_start_activity_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.earn_and_burn_start_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileWithContentEnG(List<MultipartBody.Part> parts, DiaryEventListResponse.Datum eandBPendingActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
                .addFormDataPart("eventId", String.valueOf(eandBPendingActivity.getEventId()))
                .addFormDataPart("eventType", eandBPendingActivity.getEventTag())
                .addFormDataPart("eventName", eandBPendingActivity.getEventName())
                .addFormDataPart("journalContent", binding.editContent.getText().toString())
                .addFormDataPart("eventCategory", TAG_REWARD_EVENT)
                .build();
        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "EnG/EarnJournalTokens")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                fileDataList.clear();
//                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                fileDataList.clear();
                if (response.code() == 200 && response.body() != null) {
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    if (commonSuccessResponse.isSuccess()) {
                        fileDataList.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();

                                if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getTokens() != null) {
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, commonSuccessResponse.getEnGTokens().getTokens()));
                                }
                                if (commonSuccessResponse.getEnGTokens() != null && commonSuccessResponse.getEnGTokens().getBonusTokens() != null && !TextUtils.isEmpty(commonSuccessResponse.getEnGTokens().getBonusTokens())) {
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, commonSuccessResponse.getEnGTokens().getBonusTokens()));
                                }

                                showRewardsPopupDialogBox();
                            }
                        });
                    }
                }
            }
        });
    }

    private void startTopUps(DiaryEventListResponse.Datum topUp) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        StartRewardsActivityRequest request = new StartRewardsActivityRequest(topUp.getEventId(), TAG_TOP_UP_EVENT);
        Log.d("AuthToken", "TopUps API Called");
        Call<CommonSuccessResponse> call = apiInterfaceWyh.startRewardsActivity(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_success));
                    uploadFileWithContentTopUP(parts, selectedItem);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                fileDataList.clear();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.rewards_start_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileWithContentTopUP(List<MultipartBody.Part> parts, DiaryEventListResponse.Datum topUp) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
//                .addFormDataPart(fileUploadKey, filename,
//                        RequestBody.create(MediaType.parse("application/octet-stream"), new File(fileDataList.get(0).getPath())))
                .addFormDataPart("eventId", String.valueOf(topUp.getEventId()))
                .addFormDataPart("eventType", "JournalUpload")
                .addFormDataPart("eventName", topUp.getEventTag())
                .addFormDataPart("yesNo", "")
                .addFormDataPart("journalContent", binding.editContent.getText().toString())
                .addFormDataPart("eventCategory", TAG_TOP_UP_EVENT)
                .build();

        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "Rewards/EarnRewards")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();
        Log.d("AuthToken", "TopUps API Called");

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                fileDataList.clear();
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                fileDataList.clear();
                if (response.code() == 200 && response.body() != null) {
                    NewDashboardHelper.Companion.getPopUpShowModels().clear();
                    CommonSuccessResponse commonSuccessResponse = new Gson().fromJson(response.body().string(), CommonSuccessResponse.class);
                    if (commonSuccessResponse.isSuccess()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getReward() != null) {
                                    Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, commonSuccessResponse.getRewards().getReward()));
                                }

                                if (commonSuccessResponse.getRewards() != null && commonSuccessResponse.getRewards().getBonusRewards() != null) {
                                    Toast.makeText(context, context.getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show();
                                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, commonSuccessResponse.getRewards().getBonusRewards()));
                                }

                                showRewardsPopupDialogBox();
                            }
                        });
                    }
                }
            }
        });
    }

    private void showRewardsPopupNew(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

        alertDialog.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 3000);


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        binding.scratchView.onFullReveal();
//        binding.btnPositive.setText("Collect");
        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusRewardsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusRewards = alertBuilder.create();
        alertDialogBonusRewards.setCancelable(false);
        if (!alertDialogBonusRewards.isShowing())
            alertDialogBonusRewards.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

        alertDialogBonusRewards.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });
        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusRewards.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
        });
        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusRewards.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showStampsPopup(String rewards, Context context) {
        isStamp = true;
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
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }
        alertDialogStamp.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));
        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogStamp.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.btnNegative.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(this);

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogStamp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        isStamp = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusStamp = alertBuilder.create();
        alertDialogBonusStamp.setCancelable(true);
        if (!alertDialogBonusStamp.isShowing())
            alertDialogBonusStamp.show();

        alertDialogBonusStamp.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });


        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusStamp.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showRewardsPopupDialogBox() {
        if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0) {
            int i = 0;
            PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            switch (firstData.getKey()) {
                case "Rewards":
                    showRewardsPopupNew(firstData.getValue(), context);
                    break;
                case "RewardsBounce":
                    showBonusRewardsPopup(firstData.getValue(), context);
                    break;
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
        } else {
            Intent intent = new Intent();
            intent.putExtra("isAdded", true);
            intent.putExtra("came_from", came_from);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i > 20) {
            if (isStamp) {
                isStamp = false;
                scratchTokenReward();
            }
            scratchCardLayout.onFullReveal();
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

    @Override
    public void onScratchStarted() {

    }

    private void scratchTokenReward() {
        RewardsPopupRequest request = new RewardsPopupRequest(stampId);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.scratchTokenReward(SharedPref.getAuthToken(), request);
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

    private void setRvEvent(List<String> levelList, String tagName, String activity) {
        if (levelList.size() == 1) {
            binding.levelSpinner.setEnabled(false);
            Toast.makeText(context, "No activity found!", Toast.LENGTH_SHORT).show();
        } else {
            binding.levelSpinner.setEnabled(true);
            ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, levelList);
            levelAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            binding.levelSpinner.setPrompt(tagName);
            binding.levelSpinner.setAdapter(levelAdapter);

            binding.levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    if (!Objects.equals(levelAdapter.getItem(position), tagName)) {
                        selectedItem = eventListData.get(position - 1);
                        if (!binding.activitySpinner.getSelectedItem().toString().equalsIgnoreCase("Select your category")) {
                            APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getLogKeys(selectedItem.getEventName(), activity), context);
                        }
                        binding.editTitle.setText(selectedItem.getEventName());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

    }

    private void showPopupDeleteImage(int id) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        View view = LayoutInflater.from(context).inflate(R.layout.delete_popup, null);
        alertBuilder.setView(view);
        Button btn_yes = view.findViewById(R.id.btnOK);
        Button btn_no = view.findViewById(R.id.btnCancel);
        TextView tvMsg = view.findViewById(R.id.tvMsg);

        tvMsg.setText("Are you sure want to delete this journal image?");


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
        btn_yes.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            deleteImage(id);
        });
        btn_no.setOnClickListener(view1 -> {
            alertDialog.dismiss();
        });

        Rect displayRectangle = new Rect();
        Window window;

        window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.45f));
    }

    private void deleteImage(int id) {
        ImageDeleteRequest request = new ImageDeleteRequest(id);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.imageDeleteDiary(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {

                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.diary_image_delete_success));
                    Toast.makeText(context, "Image Deleted successfully", Toast.LENGTH_SHORT).show();
                    isDeleted = false;
                    fileDataList.clear();
                    parts.clear();
                    binding.llFileImage.setVisibility(View.GONE);
                    binding.llFileImage.setVisibility(View.GONE);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.diary_image_delete_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {

                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.diary_image_delete_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteImageFile() {
        if (imageName != "") {
            CommonUtils.deleteImage(imageName);
            imageName = "";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deleteImageFile();
    }
}