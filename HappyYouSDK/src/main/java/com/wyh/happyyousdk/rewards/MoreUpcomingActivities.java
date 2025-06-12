package com.wyh.happyyousdk.rewards;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.IRA_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.MEDITATION;
import static com.wyh.happyyousdk.utils.Constants.TAG_REWARD_EVENT;
import static com.wyh.happyyousdk.utils.Constants.WATER;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.absorb.QuickReadDashboard;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityMoreUpcomingActivitiesBinding;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalUploadBinding;
import com.wyh.happyyousdk.databinding.CustomPopupRewardsBinding;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.ira.IRAAnalysisActivity;
import com.wyh.happyyousdk.ira.IraActivity;
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.request.rewards.GetRewardsDashboardRequest;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.adapter.level.MoreUpcomingActivitiesAdapter;
import com.wyh.happyyousdk.model.request.rewards.StartRewardsActivityRequest;
import com.wyh.happyyousdk.model.response.rewards.LevelActivity;
import com.wyh.happyyousdk.model.response.rewards.LevelDashboardResponse;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreUpcomingActivities extends AppCompatActivity implements MoreUpcomingActivitiesAdapter.ClickListenerInterface {

    ActivityMoreUpcomingActivitiesBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    AlertDialog alertDialog;
    Context context;
    private LevelActivity uploadFileActivity;
    String yesNoAnswer, journalContent;
    ArrayList<FileData> fileDataList = new ArrayList<>();
    MoreUpcomingActivitiesAdapter upcomingLevelAdapter;
    String fileUploadKey = "ActivityUploads";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_more_upcoming_activities);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Upcoming Activities");

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "AllUpcomingActivities");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        getRewardsDashboardData();

    }

    public void getRewardsDashboardData() {
        GetRewardsDashboardRequest request = new GetRewardsDashboardRequest("");
        Call<LevelDashboardResponse> call = apiInterfaceWyh.getRewardsDashboardData(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<LevelDashboardResponse>() {
            @Override
            public void onResponse(Call<LevelDashboardResponse> call, Response<LevelDashboardResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {
                    refreshAuthToken();
                }
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_success));
                    if (response.body().getData() != null) {
                        setActivitiesData(response.body().getData().getUpcomingLevel().getActivities(), response.body().getData().getUpcomingLevel().getLevelName());
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LevelDashboardResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_rewards_dash_board_data_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setActivitiesData(List<LevelActivity> levelActivities, String levelName) {
        upcomingLevelAdapter = new MoreUpcomingActivitiesAdapter(context, levelActivities, levelName, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvUpcomingActivities.setAdapter(upcomingLevelAdapter);
        binding.rvUpcomingActivities.setLayoutManager(gridLayoutManager);
        binding.rvUpcomingActivities.setItemViewCacheSize(30);
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
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    getRewardsDashboardData();
                } else {
                    /*Toast.makeText(context, context.getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    context.startActivity(intent);
                    finishAffinity();*/
                    Master.INSTANCE.logOut(context);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                /*Toast.makeText(context, context.getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    context.startActivity(intent);
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    private void showCustomPopUp(LevelActivity levelActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        CustomPopupRewardsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_rewards, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        Glide.with(context)
                .load(levelActivity.getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivLogo);
        binding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        binding.tvTitle.setText(levelActivity.getActivityName());
        binding.tvDescription.setText(levelActivity.getActivityDesc());
        if (levelActivity.isStarted() || levelActivity.isIsCompleted()) {
            binding.btnPositive.setVisibility(View.GONE);
        }

        if (levelActivity.getWhatTo() != null) {
            binding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            binding.tvWhatToDo.setText(levelActivity.getWhatTo());
            binding.tvHowToDo.setText(levelActivity.getHowTo());
            binding.tvWhyToDo.setText(levelActivity.getWhyTo());
        }

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        binding.btnPositive.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            startNewActivity(levelActivity);
        });

        if (!levelActivity.isIsCompleted() && levelActivity.isStarted() && levelActivity.getEventType().equalsIgnoreCase("upload")) {
            binding.btnPositive.setVisibility(View.VISIBLE);
            binding.btnPositive.setText("Upload");
            binding.btnPositive.setOnClickListener(view -> {
                fileUploadKey = "ActivityUploads";
                alertDialog.dismiss();
                openGalleryOnly();
                uploadFileActivity = levelActivity;
            });
        }

        if (!levelActivity.isIsCompleted() && levelActivity.isStarted() && levelActivity.getEventType().equalsIgnoreCase("yesno")) {
            binding.llYesNo.setVisibility(View.VISIBLE);
            binding.btnYes.setOnClickListener(view -> {
                alertDialog.dismiss();
                yesNoAnswer = "Yes";
                uploadContentOnly(levelActivity);
            });
            binding.btnNo.setOnClickListener(view -> {
                alertDialog.dismiss();
                yesNoAnswer = "No";
                uploadContentOnly(levelActivity);
            });
        }

        if (!levelActivity.isIsCompleted() && levelActivity.isStarted() && levelActivity.getEventType().equalsIgnoreCase("journal")) {
            binding.btnPositive.setVisibility(View.VISIBLE);
            binding.btnPositive.setText("Submit");
            binding.llJournal.setVisibility(View.VISIBLE);
            binding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(binding.edtJournal.getText())) {
                    alertDialog.dismiss();
                    journalContent = binding.edtJournal.getText().toString();
                    uploadContentOnly(levelActivity);
                } else {
                    binding.edtJournal.requestFocus();
                }
            });
        }

        if (!levelActivity.isIsCompleted() && levelActivity.isStarted() && levelActivity.getEventType().equalsIgnoreCase("JournalUpload")) {
            binding.btnPositive.setVisibility(View.VISIBLE);
            binding.btnPositive.setText("Submit");
            binding.llJournal.setVisibility(View.VISIBLE);
            binding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(binding.edtJournal.getText())) {
                    alertDialog.dismiss();
                    journalContent = binding.edtJournal.getText().toString();
                    openGalleryOnly();
                    fileUploadKey = "JournalUpload";
                    uploadFileActivity = levelActivity;
                } else {
                    binding.edtJournal.requestFocus();
                }
            });
        }

        Rect displayRectangle = new Rect();
        Window window = ((MoreUpcomingActivities) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void showCustomPopUpJournalUpload(LevelActivity levelActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        CustomPopUpRewardsJournalUploadBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_rewards_journal_upload, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        Glide.with(context)
                .load(levelActivity.getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivLogo);
        binding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        binding.tvTitle.setText(levelActivity.getActivityName());
        binding.tvDescription.setText(levelActivity.getActivityDesc());
        if (levelActivity.isStarted() || levelActivity.isIsCompleted()) {
            binding.btnPositive.setVisibility(View.GONE);
        }

        if (levelActivity.getWhatTo() != null) {
            binding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            binding.tvWhatToDo.setText(levelActivity.getWhatTo());
            binding.tvHowToDo.setText(levelActivity.getHowTo());
            binding.tvWhyToDo.setText(levelActivity.getWhyTo());
        }

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        binding.btnPositive.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            startNewActivity(levelActivity);
        });

        if (!levelActivity.isIsCompleted() && levelActivity.isStarted() && levelActivity.getEventType().equalsIgnoreCase("JournalUpload")) {
            binding.btnPositive.setVisibility(View.VISIBLE);
            binding.btnPositive.setText("Submit");
            binding.llJournal.setVisibility(View.VISIBLE);
            binding.btnPositive.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(binding.edtJournal.getText())) {
                    alertDialog.dismiss();
                    journalContent = binding.edtJournal.getText().toString();
                    openGalleryOnly();
                    fileUploadKey = "JournalUpload";
                    uploadFileActivity = levelActivity;
                } else {
                    binding.edtJournal.requestFocus();
                }
            });
        }

        Rect displayRectangle = new Rect();
        Window window = ((MoreUpcomingActivities) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.8f));
    }

    public void openGalleryOnly() {
        ImagePicker.create(this)
                .includeVideo(false)
                .imageDirectory("Camera")
                .enableLog(true)
                .includeAnimation(true)
                .limit(1)
                .showCamera(true)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<Image> image2 = ImagePicker.getImages(data);
            for (int i = 0; i < image2.size(); i++) {
                FileData fileData = new FileData();
                fileData.setPath(getFilePathFromImage(image2.get(i)));
                long singleFileSize = getFileSizeFromPath(fileData.getPath());

                if (singleFileSize > 5000000) {
                    Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                } else {
                    fileDataList.add(fileData);
                }
            }
            if (fileDataList.size() > 0) {
                parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                uploadFileWithContent(parts, uploadFileActivity);
//                uploadFiles(parts, uploadFileActivity, yesNoAnswer, journalContent);
            }
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String path) {
        File file = new File(path);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
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

    private void startNewActivity(LevelActivity levelActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        StartRewardsActivityRequest request = new StartRewardsActivityRequest(levelActivity.getActivityID(), TAG_REWARD_EVENT);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.startRewardsActivity(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_success));
                    if (response.body().isSuccess()) {
                        Toast.makeText(context, "Activity has been started", Toast.LENGTH_SHORT).show();
                        getRewardsDashboardData();
                        upcomingLevelAdapter.notifyDataSetChanged();
                        if (levelActivity.getRedirectTo() != null) {
                            switch (levelActivity.getRedirectTo().toLowerCase()) {
                                case "hra":
                                    Intent intent;
                                    GetAnalysisResponse getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
                                    if (getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0) {
                                        intent = new Intent(context, HRAAnalysisActivity.class);
                                    } else {
                                        intent = new Intent(context, HRAQuestionsActivity.class);
                                    }
                                    startActivity(intent);
                                    break;
                                case "ira":
                                    IRAHealthScoreResponse iraHealthScoreResponse = new Gson().fromJson(SharedPref.getIRAHealthData(), IRAHealthScoreResponse.class);
                                    String healthScore = "";
                                    if (iraHealthScoreResponse != null && iraHealthScoreResponse.getIraHealthScoreData() != null)
                                        healthScore = iraHealthScoreResponse.getIraHealthScoreData().getPlaySports();
                                    if (!healthScore.equals("0") && !healthScore.equals("")) {
                                        intent = new Intent(context, IRAAnalysisActivity.class);
                                    } else {
                                        intent = new Intent(context, IraActivity.class);
                                        intent.putExtra("from", IRA_STATUS_COMPLETED);
                                    }
                                    startActivity(intent);
                                    break;
                                case "blogread":
                                    Intent intent1 = new Intent(context, QuickReadDashboard.class);
                                    startActivity(intent1);
                                    break;
                                case "waterintake":
                                    Intent intent2 = new Intent(context, TrendsActivity.class);
                                    intent2.putExtra("activityType", WATER);
                                    startActivity(intent2);
                                    break;
                                case "meditation":
                                    Intent intent3 = new Intent(context, TrendsActivity.class);
                                    intent3.putExtra("activityType", MEDITATION);
                                    startActivity(intent3);
                                    break;
                            }
                        }
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.rewards_start_activity_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFiles(List<MultipartBody.Part> parts, LevelActivity levelActivity, String yesNoAnswer, String journalContent) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<CommonSuccessResponse> call = apiInterfaceWyh.uploadLevelFiles(SharedPref.getAuthToken(), parts,
                levelActivity.getActivityID(), levelActivity.getEventType(), TAG_REWARD_EVENT, levelActivity.getActivityName(), yesNoAnswer, journalContent);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_rewards_success));
                    if (response.body().isSuccess()) {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        getRewardsDashboardData();
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_rewards_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.earn_and_burn_rewards_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileWithContent(List<MultipartBody.Part> parts, LevelActivity levelActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
                .addFormDataPart("eventId", String.valueOf(levelActivity.getActivityID()))
                .addFormDataPart("eventType", levelActivity.getEventType())
                .addFormDataPart("eventName", levelActivity.getActivityTag())
                .addFormDataPart("yesNo", yesNoAnswer == null ? "" : yesNoAnswer)
                .addFormDataPart("journalContent", journalContent == null ? "" : journalContent)
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
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(() -> Toast.makeText(context, getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show());
                    getRewardsDashboardData();
                }
            }
        });
    }

    private void uploadContentOnly(LevelActivity levelActivity) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("ActivityUploads", "")
                .addFormDataPart("eventId", String.valueOf(levelActivity.getActivityID()))
                .addFormDataPart("eventType", levelActivity.getEventType())
                .addFormDataPart("eventName", levelActivity.getActivityTag())
                .addFormDataPart("yesNo", yesNoAnswer == null ? "" : yesNoAnswer)
                .addFormDataPart("journalContent", journalContent == null ? "" : journalContent)
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
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.code() == 200 && response.body() != null) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(() -> Toast.makeText(context, getResources().getString(R.string.points_earned_msg), Toast.LENGTH_SHORT).show());
                    getRewardsDashboardData();
                }
            }
        });
    }


    @Override
    public void onItemClickActivities(LevelActivity levelActivity, int bgDrawable) {

    }
}