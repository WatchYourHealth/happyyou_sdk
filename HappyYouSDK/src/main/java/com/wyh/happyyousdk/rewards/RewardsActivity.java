package com.wyh.happyyousdk.rewards;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.esafirm.imagepicker.features.ImagePicker;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
;
import com.wyh.happyyousdk.ChallangesModule.Activities.BadgesActivity;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityRewardsBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.diary.AddDiaryActivity;
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.fragment.EarnAndBurnFragment;
import com.wyh.happyyousdk.rewards.fragment.LevelFragment;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardsActivity extends AppCompatActivity implements ScratchListener {
    public ActivityRewardsBinding binding;
    Context context;

    public AlertDialog alertDialogBonusRewards, alertDialogStamp, alertDialogBonusStamp;
    EarnAndBurnFragment earnAndBurnFragment = new EarnAndBurnFragment();
    LevelFragment levelFragment = new LevelFragment();
    public static String comingFrom = "this";

    int totalStamp = 0;
    private static final int CAMERA_PERMISSION_CODE = 100;

    boolean isPositiveBtn = false, isFromLevel = true;

    List<DiaryEventListResponse.Datum> levelListData, engListData, topUpListData;
    List<String> levelList, engList, topUpList;
    private int[] tabIcons = {
            R.drawable.level_blue,
            R.drawable.earn_grab_blue,
            R.drawable.ic_levels,
            R.drawable.ic_earn_and_grab,
    };
    ApiInterfaceWyh apiInterfaceWyh;
    ProgressDialog progressDialog;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rewards);
        context = this;

        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        if(getIntent().getStringExtra("comingFrom") != null){
            comingFrom = getIntent().getStringExtra("comingFrom");
            if(getIntent().getStringExtra("comingFrom").equalsIgnoreCase("NewDashboard")){
                binding.includeToolbar.ivBack.setVisibility(View.GONE);
                binding.includeToolbar.tvBack.setText("Winnings");
                binding.bottomParentNav.setVisibility(View.VISIBLE);
                binding.ivHome.setVisibility(View.GONE);
            }else{
                binding.bottomParentNav.setVisibility(View.GONE);
                binding.includeToolbar.ivBack.setVisibility(View.VISIBLE);
                binding.ivHome.setVisibility(View.VISIBLE);
            }
        }else{
            binding.bottomParentNav.setVisibility(View.GONE);
            binding.ivHome.setVisibility(View.VISIBLE);
        }

        currentIndex = getIntent().getIntExtra("currentIndex", 0);

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "WinningsDashboard");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comingFrom.equalsIgnoreCase("NewDashboard")){
                    startActivity(new Intent(RewardsActivity.this,NewDashboardActivity.class));
                }else{
                    finish();
                }
            }
        });
        binding.includeToolbar.tvBack.setText("Winnings");
        binding.includeToolbar.ivMenu.setVisibility(View.VISIBLE);

        binding.includeToolbar.ivMenu.setOnClickListener(view -> {
            showPopup(binding.includeToolbar.ivMenu);
        });

        navigationClicks();

        // Give the TabLayout the ViewPager
        setupViewPager(binding.viewpager);
        binding.slidingTabs.setupWithViewPager(binding.viewpager);
        setupTabIcons();

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

//        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, false);


        binding.slidingTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setTint(getResources().getColor(R.color.blue_cyan, getTheme()));
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setTint(getResources().getColor(R.color.happy_dark_grey, getTheme()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*if (!SharedPref.getIsWinningsIntroShown() && SharedPref.getNewUser()) {
            Intent intent = new Intent(RewardsActivity.this, VideoReaderActivity.class);
                intent.putExtra("url", "android.resource://" + getPackageName() + "/" +
                        R.raw.intro_winning);
            intent.putExtra("redirectTo", "Rewards");
            startActivity(intent);
            finish();

        }*/
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(comingFrom.equalsIgnoreCase("NewDashboard")){
            startActivity(new Intent(RewardsActivity.this,NewDashboardActivity.class));
            finish();
        }else{
            finish();
        }
    }

    public void navigationClicks(){


        binding.ivDashboardIce.setOnClickListener(view -> {
            startActivity(new Intent(this,NewDashboardActivity.class)
                    .putExtra("comingFrom","iceFragment"));
            finish();

        });

        binding.ivDashHappyMart.setOnClickListener(view -> {
            startActivity(new Intent(this,NewDashboardActivity.class)
                    .putExtra("comingFrom","happyMartFragment"));
            finish();
        });

        binding.ivDashOthers.setOnClickListener(view -> {
            startActivity(new Intent(this,NewDashboardActivity.class)
                    .putExtra("comingFrom","happyMartOther"));
            finish();
        });



        binding.ivDashboardHome.setOnClickListener(view -> {
            try {
                startActivity(new Intent(this,NewDashboardActivity.class)
                        .putExtra("comingFrom",""));
                finish();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void checkPermission(String permission, int requestCode, boolean isFromLevel) {
        this.isFromLevel = isFromLevel;
        // Checking if permission is not granted
        if (context == null) {
            context = this;
        }
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
        } else {
            if (isFromLevel)
                levelFragment.checkImagePicker();
            else
                earnAndBurnFragment.checkImagePicker();
        }

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
    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_rewards, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.menu_history) {
                Intent intent = new Intent(context, RewardsHistoryActivity.class);
                intent.putExtra("comingFrom", "rewards");
                startActivity(intent);
            } else if (itemId == R.id.menu_collectible) {
                Intent intent;
                intent = new Intent(context, RewardsCollectiblesActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.menu_share_and_earn) {
                Intent intent2 = new Intent(this, AddDiaryActivity.class);
                intent2.putExtra("came_from", "share");
                intent2.putExtra("levelData", new Gson().toJson(levelListData));
                intent2.putExtra("leveldataStr", new Gson().toJson(levelList));
                intent2.putExtra("engData", new Gson().toJson(engListData));
                intent2.putExtra("engdataStr", new Gson().toJson(engList));
                intent2.putExtra("topUpData", new Gson().toJson(topUpListData));
                intent2.putExtra("topUpdataStr", new Gson().toJson(topUpList));
                startActivity(intent2);
            } else if (itemId == R.id.menu_badges) {
                startActivity(new Intent(this, BadgesActivity.class));
            }
            return false;
        });
    }
    private void setupViewPager(ViewPager viewPager) {
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(levelFragment, "Level");
        adapter.addFragment(earnAndBurnFragment, "Earn And Grab");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentIndex);
    }

    private void setupTabIcons() {
        binding.slidingTabs.getTabAt(0).setIcon(tabIcons[2]);
        binding.slidingTabs.getTabAt(1).setIcon(tabIcons[3]);
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (i >= 20 && levelFragment.voucherIdRequest != null) {
            updateScratchStatus();
            scratchCardLayout.onFullReveal();

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(alertDialogBonusRewards!= null && alertDialogBonusRewards.isShowing()){
                        alertDialogBonusRewards.dismiss();
                    }
                    if(levelFragment.alertDialogBonusRewards!= null && levelFragment.alertDialogBonusRewards.isShowing()){
                        levelFragment.alertDialogBonusRewards.dismiss();
                    }
                    if((alertDialogBonusStamp!= null && alertDialogBonusStamp.isShowing())){
                        alertDialogBonusStamp.dismiss();
                    }
                    if((alertDialogStamp!= null && alertDialogStamp.isShowing())){
                        alertDialogStamp.dismiss();
                    }
                    if(earnAndBurnFragment.alertDialogBonusStamp!= null && earnAndBurnFragment.alertDialogBonusStamp.isShowing()){
                        earnAndBurnFragment.alertDialogBonusStamp.dismiss();
                    }
                    if(earnAndBurnFragment.alertDialogStamp!= null && earnAndBurnFragment.alertDialogStamp.isShowing()){
                        earnAndBurnFragment.alertDialogStamp.dismiss();
                    }
                    if(levelFragment.alertDialogScratched!= null && levelFragment.alertDialogScratched.isShowing()){
                        levelFragment.alertDialogScratched.dismiss();
                    }
                }
            }, 3000);
        }
    }

    private void updateScratchStatus() {
        Call<CommonSuccessResponse> call = apiInterfaceWyh.updateScratchStatus(SharedPref.getAuthToken(), levelFragment.voucherIdRequest);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_success));
                    levelFragment.getRewardsDashboardData();
                    levelFragment.voucherIdRequest = null;
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


    @Override
    public void onScratchStarted() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(isFromLevel){
                    levelFragment.openGalleryOnly();
                }else {
                    earnAndBurnFragment.openGalleryOnly();
                }
                // Showing the toast message
                Toast.makeText(RewardsActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RewardsActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

   /* public void getEearnAndBurnDashboardData() {
        if (apiInterfaceWyh == null) {
            RewardsActivity context = new RewardsActivity();
            apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        }
        Call<EandBDashboardResponse> call = apiInterfaceWyh.getEandBDashboardData(SharedPref.getAuthToken());
        call.enqueue(new Callback<EandBDashboardResponse>() {
            @Override
            public void onResponse(Call<EandBDashboardResponse> call, Response<EandBDashboardResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.eng_user_dashboard_success));

                    EandBUserData userData = response.body().getData().getUserData();

                    totalStamp = userData.getUsEarnerTokens();
                    int points = (int) Math.round(levelFragment.totalEarnPoints);

                    SharedPref.putStampsHistory(totalStamp);
                    SharedPref.putPointsHistory(points);


                } else if (response.code() == 401) {
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.eng_user_dashboard_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EandBDashboardResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.eng_user_dashboard_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void showRewardsPopupDialogBox() {
        Log.d("sized", NewDashboardHelper.Companion.getPopUpShowModels().size() + "");
        if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0) {
            int i = 0;
            PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 1 && Objects.equals(firstData.getKey(), "Rewards")) {
                i = 1;
                firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
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
            Log.d("sized", NewDashboardHelper.Companion.getPopUpShowModels().size() + "");
        }
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
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            //stampId = Integer.parseInt(id);
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
        /*if(isOrange){
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));
        }else{
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));
        }*/
//        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


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
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.btnNegative.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(RewardsActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PendingActivityDashboard) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogStamp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusStamp = alertBuilder.create();
        alertDialogBonusStamp.setCancelable(true);
        if (!alertDialogBonusStamp.isShowing())
            alertDialogBonusStamp.show();


        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            //stampId = Integer.parseInt(id);
        }

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);

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

        binding.btnPositive.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(RewardsActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PendingActivityDashboard) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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
                intent.putExtra("currentIndex", 1);
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
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(RewardsActivity.this);

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
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });
        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);

        binding.btnPositive.setOnClickListener(view -> {
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
        alertDialogBonusRewards.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }


    public void getEventList() {
        progressDialog.isShowing();
        Call<DiaryEventListResponse> call = apiInterfaceWyh.getDiaryEventList(SharedPref.getAuthToken());
        call.enqueue(new Callback<DiaryEventListResponse>() {
            @Override
            public void onResponse(Call<DiaryEventListResponse> call, Response<DiaryEventListResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.fetch_pending_diary_events_success));

                    List<DiaryEventListResponse.Datum> data = response.body().getData();

                    setData(data);

                } else {
                    Analytics.logEvent(context,"","A_104_"+response.code()+"_"+SharedPref.getEncryptedMobileNo());

                }
            }

            @Override
            public void onFailure(Call<DiaryEventListResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context,"","A_104_Failed"+SharedPref.getEncryptedMobileNo());
                //Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(List<DiaryEventListResponse.Datum> data) {
        levelList = new ArrayList<>();
        engList = new ArrayList<>();
        topUpList = new ArrayList<>();
        levelListData = new ArrayList<>();
        engListData = new ArrayList<>();
        topUpListData = new ArrayList<>();
        levelList.add("Select your activity");
        engList.add("Select your activity");
        topUpList.add("Select your activity");

        if(data != null && data.size() > 0){
            for(DiaryEventListResponse.Datum item : data){
                if(item.getEventCategory().toLowerCase().equals("topup")){
                    topUpList.add(item.getEventName());
                    topUpListData.add(item);
                }else if(item.getEventCategory().toLowerCase().equals("eng")){
                    engList.add(item.getEventName());
                    engListData.add(item);
                }else{
                    levelList.add(item.getEventName());
                    levelListData.add(item);
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getEventList();
    }
}