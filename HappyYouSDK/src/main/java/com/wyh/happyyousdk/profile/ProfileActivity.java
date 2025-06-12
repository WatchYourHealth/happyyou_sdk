package com.wyh.happyyousdk.profile;

import static android.os.Build.VERSION.SDK_INT;
import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.WEIGHT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

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
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
;
import com.wyh.happyyousdk.APIEncryption.APILogs;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.absorb.HealthTVDashboard;
import com.wyh.happyyousdk.absorb.MoreQuickReadActivity;
import com.wyh.happyyousdk.absorb.adapter.HealthHacksHealthTvAdapter;
import com.wyh.happyyousdk.absorb.adapter.BookmarkBlogListAdapter;
import com.wyh.happyyousdk.databinding.CustomPopUpRewardsJournalBinding;
import com.wyh.happyyousdk.databinding.EditNickNameLayoutBinding;
import com.wyh.happyyousdk.hra.HRAAnalysisActivity;
import com.wyh.happyyousdk.hra.HRAQuestionsActivity;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.absorb.AddBookmarkRequest;
import com.wyh.happyyousdk.model.request.absorb.GetQuickReadRequest;
import com.wyh.happyyousdk.model.request.absorb.VideoBookmarkRequest;
import com.wyh.happyyousdk.model.request.profile.NickNameRequest;
import com.wyh.happyyousdk.model.response.absorb.AddBookmarkResponse;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.model.response.absorb.GetQuickReadResponse;
import com.wyh.happyyousdk.common.adapter.IndicatorsAdapter;
import com.wyh.happyyousdk.databinding.ActivityProfileBinding;
import com.wyh.happyyousdk.model.response.diary.UploadUserFileResponse;
import com.wyh.happyyousdk.model.request.ehr.FileData;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.rewards.LevelActivity;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.model.response.ProfileDetailsResponse;
import com.wyh.happyyousdk.rewards.RewardsHistoryActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.SnapHelperOneByOne;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    public static String categoryCountText = "";
    public static String tribeCount,referralCount,dareReferralCount,totalCount;


    int totalPoints, currentLevel, positionHealthTv = 0, positionReads = 0, positionBookmark = 0, positionTribe = 0;
    GetAnalysisResponse getAnalysisResponse;
    List<GetDashboardDataResponse.Data.TagName> tagNameList;
    List<ProfileDetailsResponse.Data.LatestWinning> profileDetailsResponseLatestWinning;
    ArrayList<FileData> fileDataList = new ArrayList<>();
    List<GetQuickReadResponse.Data.TribeBlog> bookmarksList;
    String fileUploadKey = "ProfileUpload";
    int REQUEST_CODE_CAMERA = 21;
    List<MultipartBody.Part> parts = new ArrayList<>();

    ArrayList<String> tribeList = new ArrayList<>();

    public static ArrayList<ConnectionListModel> tribeListModel = new ArrayList<>();
    public static ArrayList<ConnectionListModel> referenceListModel = new ArrayList<>();
    public static ArrayList<ConnectionListModel> dareListModel = new ArrayList<>();
    ArrayList<String> referenceList = new ArrayList<>();

    ArrayList<String> dareReferralList = new ArrayList<>();
    private static final int CAMERA_PERMISSION_CODE = 100;

    BottomSheetDialog bottomSheetDialog;

    boolean isProfile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        totalPoints = getIntent().getIntExtra("points", 0);
        currentLevel = getIntent().getIntExtra("currentLevel", 0);

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "UserProfile");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));
        binding.includeToolbar.tvBack.setText("Profile");
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());

        binding.ivHome.setOnClickListener(view -> finish());

        getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);

        binding.tvPoints.setText("Points: " + totalPoints);
        binding.levelProgress.setProgress(currentLevel);
        binding.tvStartLevel.setText("" + currentLevel);
        int newLevel = currentLevel + 1;
        binding.tvEndLevel.setText("" + newLevel);

        boolean isAnalysis = (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null && getAnalysisResponse.getAnalysisData().getScore() > 0);


        binding.weightEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TrendsActivity.class);
                intent.putExtra("activityType", WEIGHT);
                startActivity(intent);
            }
        });


        binding.heightEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (isAnalysis) {
                    intent = new Intent(context, HRAAnalysisActivity.class);
                } else {
                    intent = new Intent(context, HRAQuestionsActivity.class);
                }
                startActivity(intent);
            }
        });

        binding.profileConnectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtra("categoryCountText",categoryCountText);
                startActivity(intent);
            }
        });

        getQuickReadDashboardAPI("");

        binding.rlBookmark.setOnClickListener(view -> {
            Intent intent = new Intent(this, MoreQuickReadActivity.class);
            intent.putExtra("data", new Gson().toJson(bookmarksList));
            intent.putExtra("title", "Bookmarks");
            startActivity(intent);
        });

        binding.rlHealthTv.setOnClickListener(view -> {
            getProfileBookmark();
        });



        if(isAnalysis){
            binding.redirectHraCard.setVisibility(View.GONE);
        }else{
            binding.redirectHraCard.setVisibility(View.VISIBLE);
        }

        binding.redirectHraCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (isAnalysis) {
                    intent = new Intent(context, HRAAnalysisActivity.class);
                } else {
                    intent = new Intent(context, HRAQuestionsActivity.class);
                }
                startActivity(intent);
            }
        });


        binding.rlWinnings.setOnClickListener(view -> {
            Intent intent = new Intent(context, RewardsHistoryActivity.class);
            intent.putExtra("comingFrom","profile");
            startActivity(intent);
        });

        binding.ivEdit.setOnClickListener(v->{
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        });

        binding.ivEditNickName.setOnClickListener(v->{
            showPopup();
        });

        LinearSnapHelper linearSnapHelper2 = new SnapHelperOneByOne();
        linearSnapHelper2.attachToRecyclerView(binding.rvBookmark);

    }

    private void showBottomSheetDialog() {

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.profile_bottom_sheet_option);

        LinearLayout camera = bottomSheetDialog.findViewById(R.id.llCamera);
        LinearLayout gallery = bottomSheetDialog.findViewById(R.id.llGallery);
        LinearLayout delete = bottomSheetDialog.findViewById(R.id.llDelete);

        if(isProfile){
            delete.setVisibility(View.VISIBLE);
        }else{
            delete.setVisibility(View.GONE);
        }

        camera.setOnClickListener(v -> {
            try {
                bottomSheetDialog.dismiss();
                openCameraOnly();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gallery.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
                openImagePicker();
            }else{
                openGalleryOnly();
            }
        });

        delete.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showPopupDeleteImage();
        });

        bottomSheetDialog.show();

    }

    public void openCameraOnly() throws IOException {
        ImagePicker.cameraOnly().imageDirectory("Camera").start(this);
    }

    private void getProfileDetails() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<ProfileDetailsResponse> call = apiInterfaceWyh.getUserDetails(SharedPref.getAuthToken());
        call.enqueue(new Callback<ProfileDetailsResponse>() {
            @Override
            public void onResponse(Call<ProfileDetailsResponse> call, Response<ProfileDetailsResponse> response) {
                try{
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.code() == 200 && response.body() != null) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_profile_details_success));

                        if (response.body().getSuccess()) {
                            ProfileDetailsResponse.Data profileDetailsResponse = response.body().getData();

                            if(profileDetailsResponse.getProfile().getNickname() != null && !profileDetailsResponse.getProfile().getNickname().isEmpty()){
                                binding.tvNickName.setText("@"+profileDetailsResponse.getProfile().getNickname());
                            }else{
                                binding.tvNickName.setText("@NickName");
                            }

                            if(profileDetailsResponse.getProfile().getName() != null){
                                SharedPref.putUserName(profileDetailsResponse.getProfile().getName());
                                binding.tvName.setText(profileDetailsResponse.getProfile().getName());
                            }else{
                                binding.tvName.setText(SharedPref.getUserName());

                            }

                            if(profileDetailsResponse.getConnectionLists().size() > 0){
                                tribeListModel.clear();
                                referenceListModel.clear();
                                dareListModel.clear();
                                for(int i=0; i < profileDetailsResponse.getConnectionLists().size(); i++){
                                    if(profileDetailsResponse.getConnectionLists().get(i).getType().equalsIgnoreCase("tribe")){
                                        ConnectionListModel model = new ConnectionListModel(profileDetailsResponse.getConnectionLists().get(i).getName(),profileDetailsResponse.getConnectionLists().get(i).getType(),profileDetailsResponse.getConnectionLists().get(i).getTribeId());
                                        tribeListModel.add(model);
                                    }else if((profileDetailsResponse.getConnectionLists().get(i).getType().equalsIgnoreCase("Dare referral"))){
                                        ConnectionListModel model = new ConnectionListModel(profileDetailsResponse.getConnectionLists().get(i).getName(),profileDetailsResponse.getConnectionLists().get(i).getType(),profileDetailsResponse.getConnectionLists().get(i).getTribeId());
                                        dareListModel.add(model);
                                    }else{
                                        ConnectionListModel model = new ConnectionListModel(profileDetailsResponse.getConnectionLists().get(i).getName(),profileDetailsResponse.getConnectionLists().get(i).getType(),profileDetailsResponse.getConnectionLists().get(i).getTribeId());
                                        referenceListModel.add(model);
                                    }
                                }
                                totalCount = String.valueOf(tribeListModel.size());

                            }

                            if(profileDetailsResponse.getCategoryCount() != null){
                                if(profileDetailsResponse.getCategoryCount().getTribeCount() != null &&
                                        !profileDetailsResponse.getCategoryCount().getTribeCount().equals("")){
                                    String[] count = profileDetailsResponse.getCategoryCount().getTribeCount().split(":");
                                    if(count.length != 0){
                                        tribeCount = count[0];
                                    }

                                }
                            }

                            if(profileDetailsResponse.getCategoryCount() != null){
                                if(profileDetailsResponse.getCategoryCount().getTribeCount() != null &&
                                        !profileDetailsResponse.getCategoryCount().getTribeCount().equals("")){
                                    String[] count = profileDetailsResponse.getCategoryCount().getTribeCount().split(":");
                                    if(count.length != 0){
                                        tribeCount = count[1];
                                    }

                                }
                            }

                            if(profileDetailsResponse.getCategoryCount() != null){
                                if(profileDetailsResponse.getCategoryCount().getReferalCount() != null &&
                                        !profileDetailsResponse.getCategoryCount().getReferalCount().equals("")){
                                    String[] count = profileDetailsResponse.getCategoryCount().getReferalCount().split(":");
                                    if(count.length != 0){
                                        referralCount = count[1];
                                    }

                                }
                            }

                            if(profileDetailsResponse.getCategoryCount() != null){
                                if(profileDetailsResponse.getCategoryCount().getDareReferalCount() != null &&
                                        !profileDetailsResponse.getCategoryCount().getDareReferalCount().equals("")){
                                    String[] count = profileDetailsResponse.getCategoryCount().getDareReferalCount().split(":");
                                    if(count.length != 0){
                                        dareReferralCount = count[1];
                                    }

                                }
                            }

                            categoryCountText = profileDetailsResponse.getCategoryCount().getTribeCount() + "\n" +
                                    profileDetailsResponse.getCategoryCount().getReferalCount() + "\n" +
                                    profileDetailsResponse.getCategoryCount().getDareReferalCount();

                            if (profileDetailsResponse.getProfile().getBloodGroup() != null) {
                                binding.tvBloodGroup.setText(profileDetailsResponse.getProfile().getBloodGroup());
                            } else {
                                binding.tvBloodGroup.setText("NA");
                            }
                            binding.tvWeight.setText(SharedPref.getWeight() + " kgs");
                            binding.tvConnections.setText(profileDetailsResponse.getProfile().getConnectionsCount() + "");
                            binding.tvTribes.setText(profileDetailsResponse.getProfile().getTribeCount() + "");
                            if (profileDetailsResponse.getProfile().getHealthScore() != null) {
                                binding.tvHRA.setText("Health Score: " + profileDetailsResponse.getProfile().getHealthScore() + "");
                            } else {
                                binding.tvHRA.setVisibility(View.GONE);
                            }
                            SharedPref.putReferralCode(profileDetailsResponse.getProfile().getReferralCode());
                            String height;
                            if (String.valueOf(profileDetailsResponse.getProfile().getHeight()).contains(".")) {
                                String heightStr = String.valueOf(profileDetailsResponse.getProfile().getHeight());
                                height = String.valueOf(profileDetailsResponse.getProfile().getHeight()).split("[.]")[0] + "'" +
                                        heightStr.substring(heightStr.indexOf(".") + 1) + "''";
                            } else {
                                height = profileDetailsResponse.getProfile().getHeight() + "'";
                            }
                            binding.tvHeight.setText(height);

                            if (profileDetailsResponse.getLatestWinning().size() > 0) {
                                profileDetailsResponseLatestWinning = profileDetailsResponse.getLatestWinning();

                                for (int i = 0; i < profileDetailsResponse.getLatestWinning().size(); i++) {
                                    if (i == 0) {
                                        binding.tvWinningName1.setText(profileDetailsResponse.getLatestWinning().get(0).getName());
                                        Double point = Double.parseDouble(profileDetailsResponse.getLatestWinning().get(0).getPoint());

                                        binding.tvPointsCount.setText(Math.round(point) + " points");
                                        Glide.with(context)
                                                .load(profileDetailsResponse.getLatestWinning().get(0).getEventImage())
                                                .error(R.drawable.dummy_image)
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true)
                                                .into(binding.ivImage1);
                                    /*binding.rlWinnings1.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, CommunityActivity.class);
                                        intent.putExtra("data", new Gson().toJson(profileDetailsResponse.getLatestWinning().get(0)));
                                        context.startActivity(intent);
                                    });*/
                                    }
                                    if (i == 1) {
                                        binding.rlWinning2.setVisibility(View.VISIBLE);
                                        Double point = Double.parseDouble(profileDetailsResponse.getLatestWinning().get(1).getPoint());
                                        binding.tvWinningName2.setText(profileDetailsResponse.getLatestWinning().get(1).getName());
                                        binding.tvPointsCount2.setText(Math.round(point) + " points");
                                        Glide.with(context)
                                                .load(profileDetailsResponse.getLatestWinning().get(1).getEventImage())
                                                .error(R.drawable.dummy_image)
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true)
                                                .into(binding.ivImage2);
                                    /*binding.rlWinning2.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, CommunityActivity.class);
                                        intent.putExtra("data", new Gson().toJson(profileDetailsResponse.getLatestWinning().get(1)));
                                        context.startActivity(intent);
                                    });*/
                                    }
                                    if (i == 2) {
                                        binding.rlWinnings3.setVisibility(View.VISIBLE);
                                        binding.tvWinningName3.setText(profileDetailsResponse.getLatestWinning().get(2).getName());
                                        if(profileDetailsResponse.getLatestWinning().get(2).getPoint()!=null){
                                            Double point = Double.parseDouble(profileDetailsResponse.getLatestWinning().get(2).getPoint());
                                            binding.tvPointsCount3.setText(Math.round(point) + " points");
                                        }
                                        Glide.with(context)
                                                .load(profileDetailsResponse.getLatestWinning().get(2).getEventImage())
                                                .error(R.drawable.dummy_image)
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true)
                                                .into(binding.ivImage3);
                                        /*binding.rlWinnings3.setOnClickListener(view -> {
                                            Intent intent = new Intent(context, CommunityActivity.class);
                                            intent.putExtra("data", new Gson().toJson(profileDetailsResponse.getLatestWinning().get(2)));
                                            context.startActivity(intent);
                                        }
                                       );*/
                                    }
                                }
                            } else {
                                binding.llWinnings.setVisibility(View.GONE);
                            }

                            if(response.body().getData().getProfile().getProfileImg() != null){
                                String imagePath = response.body().getData().getProfile().getProfileImg();
                                Glide.with(context)
                                        .load(imagePath)
                                        .placeholder(R.drawable.user)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(binding.ivProfile);
                            }

                            if(response.body().getData().getProfile().getProfileImg() != null && !response.body().getData().getProfile().getProfileImg().isEmpty()){
                                isProfile = true;
                            }else{
                                isProfile = false;
                            }

                            if(profileDetailsResponse.getHealthTv()!=null && profileDetailsResponse.getHealthTv().size() > 0){
                                binding.llHealthTv.setVisibility(View.VISIBLE);
                                setHealthTvData(profileDetailsResponse.getHealthTv());
                            }else{
                                binding.llHealthTv.setVisibility(View.GONE);
                            }
                        }
                    } else if (response.code() == 401) {
                       refreshAuthToken();
                    } else {
                        APILogs.INSTANCE.activityTracker("A_110_"+response.code()+"_"+SharedPref.getEncryptedMobileNo(),context);
                        //Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ProfileDetailsResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                APILogs.INSTANCE.activityTracker("A_110_Failed"+SharedPref.getEncryptedMobileNo(),context);
            }
        });
    }

    private void showCustomPopUpJournal(LevelActivity levelActivity, int bgDrawable) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        AlertDialog levelActivityAlertDialog;
        CustomPopUpRewardsJournalBinding
        levelActivityJournalBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_pop_up_rewards_journal, null, false);
        alertBuilder.setView(levelActivityJournalBinding.getRoot());
        levelActivityAlertDialog = alertBuilder.create();
        levelActivityAlertDialog.setCancelable(false);
        if (!levelActivityAlertDialog.isShowing())
            levelActivityAlertDialog.show();

        Glide.with(context)
                .load(levelActivity.getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(levelActivityJournalBinding.ivLogo);
        levelActivityJournalBinding.rlLogo.setBackground(ContextCompat.getDrawable(context, bgDrawable));
        levelActivityJournalBinding.tvTitle.setText(levelActivity.getActivityName());
        levelActivityJournalBinding.tvDescription.setText(levelActivity.getActivityDesc());

        if (levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.btnPositive.setVisibility(View.GONE);
        }

        if (levelActivity.getWhatTo() != null) {
            levelActivityJournalBinding.llWhatHowAndWhy.setVisibility(View.VISIBLE);
            levelActivityJournalBinding.tvWhatToDo.setText(levelActivity.getWhatTo());
            levelActivityJournalBinding.tvHowToDo.setText(levelActivity.getHowTo());
            levelActivityJournalBinding.tvWhyToDo.setText(levelActivity.getWhyTo());
        }

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.progressBar.setVisibility(View.VISIBLE);
            //getActivityProgress(levelActivityJournalBinding.progressBar, levelActivity, null);
        } else if (levelActivity.isIsCompleted()) {
            levelActivityJournalBinding.progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                levelActivityJournalBinding.progressBar.setProgress(100, true);
            } else
                levelActivityJournalBinding.progressBar.setProgress(100);
        }

        //activityEventType = levelActivity.getEventType();

        //checkActivityPopUpConditionsJournal(levelActivity, activityEventType);

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        levelActivityAlertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.8f));
    }

    private void getProfileBookmark() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<ProfileDetailsResponse> call = apiInterfaceWyh.getProfileBookmark(SharedPref.getAuthToken());
        call.enqueue(new Callback<ProfileDetailsResponse>() {
            @Override
            public void onResponse(Call<ProfileDetailsResponse> call, Response<ProfileDetailsResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_profile_bookmark_success));

                    if (response.body().getSuccess()) {
                        ProfileDetailsResponse.Data profileDetailsResponse = response.body().getData();
                        if(profileDetailsResponse.getHealthTv()!=null && profileDetailsResponse.getHealthTv().size() > 0){
                            Intent i = new Intent(context, HealthTVDashboard.class);
                            i.putExtra("healthTV", (Serializable) profileDetailsResponse.getHealthTv());
                            i.putExtra("titleName", "Bookmark TV");
                            startActivity(i);
                        }
                    }
                } else if (response.code() == 401) {
//                    refreshAuthToken();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_profile_bookmark_failed));
                    //Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileDetailsResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_profile_bookmark_failed));
                //Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
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
                    getQuickReadDashboardAPI("");
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                    Master.INSTANCE.logOut(context);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                /*Toast.makeText(context, context.getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    context.startActivity(intent);
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    /*public void getAbsorbDashboard(String tagName) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetDashboardDataRequest dashboardDataRequest = new GetDashboardDataRequest(tagName);
        Call<GetDashboardDataResponse> call = apiInterfaceWyh.getAbsorbDashboard(SharedPref.getAuthToken(), dashboardDataRequest);

        call.enqueue(new Callback<GetDashboardDataResponse>() {
            @Override
            public void onResponse(Call<GetDashboardDataResponse> call, Response<GetDashboardDataResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.absorb_dashboard_success));
                    List<GetDashboardDataResponse.Data.QucikRead> quickReadList = response.body().getData().getQucikReads();
                    tagNameList = response.body().getData().getTagName();

                    if (quickReadList.size() > 0) {
                        binding.llQuickRead.setVisibility(View.VISIBLE);
                        setQuickReadData(quickReadList);
                    } else {
                        binding.llQuickRead.setVisibility(View.GONE);
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.absorb_dashboard_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetDashboardDataResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.absorb_dashboard_failed));
            }
        });
    }*/

    /*private void setQuickReadData(List<GetDashboardDataResponse.Data.QucikRead> quickReadList) {
        //Quick Reads
        TrendsQuickReadsAdapter trendsQuickReadsAdapter = new TrendsQuickReadsAdapter(context,
                quickReadList, true, false, false, false, new TrendsQuickReadsAdapter.OnItemClickListener() {
            @Override
            public void onClick(String articleCode, boolean isBookmark) {
                addBookmark(articleCode, isBookmark);
            }
        });
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(context);
        linearLayoutManager4.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvQuickReads.setLayoutManager(linearLayoutManager4);
        binding.rvQuickReads.setAdapter(trendsQuickReadsAdapter);

        int indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(quickReadList.size())) / 2.0);
        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(context);
        linearLayoutManager5.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter indicatorsAdapter2 = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvQuickReadsIndicator.setAdapter(indicatorsAdapter2);
        binding.rvQuickReadsIndicator.setLayoutManager(linearLayoutManager5);
        binding.rvQuickReadsIndicator.setHasFixedSize(true);

        binding.rvQuickReads.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (linearLayoutManager4.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionReads = linearLayoutManager4.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionReads = linearLayoutManager4.findFirstVisibleItemPosition();
                    indicatorsAdapter2.updateSelectedIndex(positionReads);
                }
            }
        });
    }*/

    public void getQuickReadDashboardAPI(String tagName) {
        GetQuickReadRequest getQuickReadRequest = new GetQuickReadRequest("Quick reads", tagName, "", 0);
        Call<GetQuickReadResponse> call = apiInterfaceWyh.getQuickReadDashboard(SharedPref.getAuthToken(), getQuickReadRequest);
        Log.v("Url_Request_save", call.request().url() + "\n" + new Gson().toJson(getQuickReadRequest) + "\n" + SharedPref.getAuthToken());

        call.enqueue(new Callback<GetQuickReadResponse>() {
            @Override
            public void onResponse(Call<GetQuickReadResponse> call, Response<GetQuickReadResponse> response) {

                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_all_items_success));

                    bookmarksList = response.body().getData().getBookMarkBlogs();
                    if (bookmarksList.size() > 0) {
                        binding.llBookmark.setVisibility(View.VISIBLE);
                        setBookmarksBlogList(bookmarksList);
                    } else {
                        binding.llBookmark.setVisibility(View.GONE);
                    }

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_all_items_failed));
                   // Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetQuickReadResponse> call, Throwable t) {
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_all_items_failed));
            }
        });
    }

    private void setBookmarksBlogList(List<GetQuickReadResponse.Data.TribeBlog> bookmarks) {

        List<GetQuickReadResponse.Data.TribeBlog> dataList = new ArrayList<>();
        if(bookmarks.size() > 6){
            for(int i = 0; i < 6; i++){
                dataList.add(bookmarks.get(i));
            }
        }else{
            dataList.addAll(bookmarks);
        }

        BookmarkBlogListAdapter tribeBlogListAdapter = new BookmarkBlogListAdapter(context, dataList, new BookmarkBlogListAdapter.OnItemClickListener() {
            @Override
            public void onClick(String articleCode, boolean isBookmark) {
                addBookmark(articleCode, isBookmark);
            }
        });
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
        LinearLayoutManager bookmarkLinearLayoutManager = new LinearLayoutManager(context);
        bookmarkLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvBookmark.setLayoutManager(bookmarkLinearLayoutManager);
        binding.rvBookmark.setAdapter(tribeBlogListAdapter);
        binding.rvBookmark.setOnFlingListener(null);

        LinearSnapHelper bookmarkLinearSnapHelper = new SnapHelperOneByOne();
        bookmarkLinearSnapHelper.attachToRecyclerView(binding.rvBookmark);

        int indicatorSize;

        if(bookmarks.size() > 6){
            binding.ivBookmarkMore.setVisibility(View.VISIBLE);
            binding.rlBookmark.setEnabled(true);
            indicatorSize = (int) Math.ceil(6.0 / 2.0);
        }else{
            binding.ivBookmarkMore.setVisibility(View.GONE);
            binding.rlBookmark.setEnabled(false);
            indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(bookmarks.size())) / 2.0);
        }

        if(indicatorSize > 1){
            binding.rvBookmarkIndicator.setVisibility(View.VISIBLE);
        }else{
            binding.rvBookmarkIndicator.setVisibility(View.INVISIBLE);
        }
        LinearLayoutManager bookmarkLinearLayoutManager1 = new LinearLayoutManager(context);
        bookmarkLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter bookmarkIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvBookmarkIndicator.setAdapter(bookmarkIndicatorsAdapter);
        binding.rvBookmarkIndicator.setLayoutManager(bookmarkLinearLayoutManager1);
        binding.rvBookmarkIndicator.setHasFixedSize(true);

        binding.rvBookmark.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (bookmarkLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionBookmark = bookmarkLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionBookmark = bookmarkLinearLayoutManager.findFirstVisibleItemPosition();
                    bookmarkIndicatorsAdapter.updateSelectedIndex(positionBookmark);
                }
            }
        });
    }



    private void addBookmark(String articleCode, boolean isBookmark) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AddBookmarkRequest request = new AddBookmarkRequest(articleCode, isBookmark);
        Call<AddBookmarkResponse> call = apiInterfaceWyh.addBookMark(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<AddBookmarkResponse>() {
            @Override
            public void onResponse(Call<AddBookmarkResponse> call, Response<AddBookmarkResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_success));
                    Log.d("BookMark", response.body().getMsg());
                    if (isBookmark) {
                        Toast.makeText(context, "Successfully added to Bookmark", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Remove from Bookmark", Toast.LENGTH_SHORT).show();
                    }
                    getQuickReadDashboardAPI("");
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_failed));
                    //Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddBookmarkResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.health_hacks_add_bookmark_failed));
                //Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //binding.tvWeight.setText(SharedPref.getWeight());
        getProfileDetails();
    }

    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[] { permission }, requestCode);
        }
        else {
            showBottomSheetDialog();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            parts.clear();
            if(data != null && data.getData() != null){
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
        }else{
            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                List<Image> image2 = ImagePicker.getImages(data);
                FileData fileData = new FileData();
                fileData.setPath(getFilePathFromImage(image2.get(0)));
                long singleFileSize = getFileSizeFromPath(fileData.getPath());

                if (singleFileSize > 5000000) {
                    Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
                } else {
                    fileDataList.clear();
                    fileDataList.add(fileData);
                }
                if (fileDataList.size() > 0) {
                    parts.clear();
                    parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
                    uploadFileWithContent(parts);
                }
            }
        }


    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String path) {
        File file = new File(path);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
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
                .includeVideo(false)
                .imageDirectory("Camera")
                .enableLog(true)
                .includeAnimation(true)
                .limit(1)
                .showCamera(false)
                .start();
    }

    private void uploadFileWithContent(List<MultipartBody.Part> parts) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .certificatePinner(getCertificatePinner())
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(parts.get(0))
                .build();

        Request request = new Request.Builder()
                .url(getBaseUrlForAPI(context) + "Profile/UploadProfileImage")
                .method("POST", body)
                .addHeader("Authorization", SharedPref.getAuthToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
               // runOnUiThread(() -> Toast.makeText(context, getString(R.string.error_string), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    UploadUserFileResponse uploadUserFileResponse = new Gson().fromJson(response.body().string(), UploadUserFileResponse.class);
                    if(uploadUserFileResponse.isSuccess()){
                        runOnUiThread(() -> {
                            Toast.makeText(context, "Profile photo updated successfully", Toast.LENGTH_SHORT).show();
                            getProfileDetails();
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showBottomSheetDialog();
            } else {
                /*Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);*/
                Toast.makeText(ProfileActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
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

    private void showPopup() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        EditNickNameLayoutBinding bindingNickName = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.edit_nick_name_layout, null, false);
        alertBuilder.setView(bindingNickName.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();


        bindingNickName.btnCancel.setOnClickListener(v->{
            alertDialog.dismiss();
        });

        bindingNickName.edtName.setText(binding.tvNickName.getText().toString().substring(1));

        bindingNickName.btnSubmit.setOnClickListener(v->{
            if(!bindingNickName.edtName.getText().toString().equals("") && !bindingNickName.edtName.getText().toString().isEmpty()){
                updateNickName(bindingNickName.edtName.getText().toString(), alertDialog);
            }else{
                Toast.makeText(context,"Please enter nick name", Toast.LENGTH_SHORT).show();
            }
        });


        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.88f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void updateNickName(String nickName, AlertDialog alertDialog) {
        progressDialog.show();
        NickNameRequest request = new NickNameRequest(nickName);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.updateNickName(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.profile_update_nick_name_success));
                    if(response.body().isSuccess()){
                        Toast.makeText(context,"Nickname added successfully.", Toast.LENGTH_SHORT).show();
                        getProfileDetails();
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(context,"Nickname already exits.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.profile_update_nick_name_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.profile_update_nick_name_failed));
            }
        });
    }

    private void deleteProfileImage() {
        progressDialog.show();
        Call<CommonSuccessResponse> call = apiInterfaceWyh.deleteProfileImage(SharedPref.getAuthToken());
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.delete_profile_success));
                    Toast.makeText(context,"Profile deleted successfully.", Toast.LENGTH_SHORT).show();
                    getProfileDetails();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.delete_profile_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.delete_profile_failed));
            }
        });
    }

    private void setHealthTvData(List<GetDashboardDataResponse.Data.HealthTv> healthTvList) {
        List<GetDashboardDataResponse.Data.HealthTv> dataList = new ArrayList<>();
        if(healthTvList.size() > 6){
            for(int i = 0; i < 6; i++){
                dataList.add(healthTvList.get(i));
            }
        }else{
            dataList.addAll(healthTvList);
        }
        HealthHacksHealthTvAdapter absorbHealthTvAdapter = new HealthHacksHealthTvAdapter(context, dataList, new HealthHacksHealthTvAdapter.OnItemClickListener() {
            @Override
            public void onClick(int id, boolean isBookmark) {
                addVideoBookmark(isBookmark, id);
            }
        });
//        GridLayoutManager quickReadLinearLayoutManager = new GridLayoutManager(context,1,  GridLayoutManager.HORIZONTAL, false);
        LinearLayoutManager absorbHealthTvLinearLayoutManager = new LinearLayoutManager(context);
        absorbHealthTvLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvHealthTv.setLayoutManager(absorbHealthTvLinearLayoutManager);
        binding.rvHealthTv.setAdapter(absorbHealthTvAdapter);
        binding.rvHealthTv.setOnFlingListener(null);

        LinearSnapHelper absorbHealthTvLinearSnapHelper = new SnapHelperOneByOne();
        absorbHealthTvLinearSnapHelper.attachToRecyclerView(binding.rvHealthTv);


        int indicatorSize;

        if(healthTvList.size() > 6){
            indicatorSize = (int) Math.ceil(6.0 / 2.0);
        }else{
            indicatorSize = (int) Math.ceil(Double.parseDouble(String.valueOf(healthTvList.size())) / 2.0);
        }

        if(indicatorSize > 1){
            binding.rvHealthTvIndicator.setVisibility(View.VISIBLE);
        }else{
            binding.rvHealthTvIndicator.setVisibility(View.INVISIBLE);
        }

        LinearLayoutManager absorbHealthTvLinearLayoutManager1 = new LinearLayoutManager(context);
        absorbHealthTvLinearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        IndicatorsAdapter absorbHealthTvIndicatorsAdapter = new IndicatorsAdapter(context, indicatorSize, 0);
        binding.rvHealthTvIndicator.setAdapter(absorbHealthTvIndicatorsAdapter);
        binding.rvHealthTvIndicator.setLayoutManager(absorbHealthTvLinearLayoutManager1);
        binding.rvHealthTvIndicator.setHasFixedSize(true);

        binding.rvHealthTv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                        positionHealthTv = absorbHealthTvLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    } else
                        positionHealthTv = absorbHealthTvLinearLayoutManager.findFirstVisibleItemPosition();
                    absorbHealthTvIndicatorsAdapter.updateSelectedIndex(positionHealthTv);
                }
            }
        });
    }

    private void addVideoBookmark(boolean isBookmark, int id) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        VideoBookmarkRequest request = new VideoBookmarkRequest(id, "video" ,isBookmark);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.addBookmarkVideo(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_success));

                    Log.d("BookMark", new Gson().toJson(response.body()));
                    if (isBookmark) {
                        Toast.makeText(context, "Successfully added to Bookmark", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Remove from Bookmark", Toast.LENGTH_SHORT).show();
                    }
                    getProfileDetails();

                    // getAbsorbDashboard(tagName);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_failed));
                    //Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_failed));
                //Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopupDeleteImage() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        View view = LayoutInflater.from(context).inflate(R.layout.delete_popup, null);
        alertBuilder.setView(view);
        Button btn_yes = view.findViewById(R.id.btnOK);
        Button btn_no = view.findViewById(R.id.btnCancel);
        TextView tvMsg = view.findViewById(R.id.tvMsg);

        tvMsg.setText("Are you sure want to remove this profile picture?");


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
        btn_yes.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            deleteProfileImage();
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

    public void openImagePicker(){
        try{
            Intent intent = new Intent();
            intent.putExtra(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,1);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void saveImage(Uri imageURI){
        FileData fileData = new FileData();
        fileData.setPath(CommonUtils.getRealPathFromURI(imageURI,context));
        FileData fileDataNew = new FileData();
        File file1 = CommonUtils.compressImageToJPEG(context, imageURI);
        fileDataNew.setPath(file1.getPath());
        fileDataNew.setMimeType("application/png");
//        imageName = file1.getName();
        if (file1.length() > 5000000) {
            Toast.makeText(context, "File(s) size should not be greater than 5MB", Toast.LENGTH_SHORT).show();
        } else {
            fileDataList.clear();
            fileDataList.add(fileDataNew);
        }
        if (fileDataList.size() > 0) {
            parts.clear();
            parts.add(prepareFilePart(fileUploadKey, fileDataList.get(0).getPath()));
            uploadFileWithContent(parts);
        }
        Log.d("FileData", new Gson().toJson(fileDataList));
    }
}