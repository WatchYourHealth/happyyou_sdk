package com.wyh.happyyousdk;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.animation.Animator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.nekolaboratory.EmulatorDetector;
import com.scottyab.rootbeer.RootBeer;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.SpinWheel.Utilities.DownloadImage;
import com.wyh.happyyousdk.SpinWheel.Activities.SpinWheelActivity;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.databinding.ActivitySplashScreenBinding;
import com.wyh.happyyousdk.databinding.InfoLayoutBinding;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.model.AlternativeNumberRequestModel;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.encrDecr.EncryptionRequest;
import com.wyh.happyyousdk.model.request.registration.RegistrationRequest;
import com.wyh.happyyousdk.model.response.GetQuadrantsResponse;
import com.wyh.happyyousdk.model.response.encrDecr.EncryptionResponse;
import com.wyh.happyyousdk.model.response.login.VerifyOtpResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.RootCheck;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.wheelview.WheelItem;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.io.Serializable;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    Context context;
    String referrerUrl;
    String notification_type = "", communityId;
    boolean isComingFromOtherApp;
    String kgiMobileNumber = "";
    String kgiName = "";
    String kgiDob = "";
    String kgiEmailId = "";
    String redirectionURL = "";
    String vendorLogo = "";
    int apiCalledCount = 0;
    Boolean isRefferalCalled = false;
    String happyMartImageURL,happyMartRedirectionURL,happyMartCategory;
    APIInterface apiInterface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);
        apiInterface = RetrofitHandler.apiInterface();

        RootBeer rootBeer = new RootBeer(context);

        if (com.wyh.happyyousdk.SDKConstants.environment != "debug") {
            if (EmulatorDetector.isEmulator(getApplicationContext())) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Emulator");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("This application is not allowed to run on an emulator.");
                alertDialog.setPositiveButton("Exit", (dialog, which) -> {
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().apply();
                    SharedPref.clearSharedPref();
                    finishAffinity();
                });
                alertDialog.show();
            } else if (rootBeer.isRooted() || rootBeer.isRootedWithBusyBoxCheck() || rootBeer.checkSuExists()) {
                //if (new CheckRootedDevice(this).isRTWithoutBBCheck()) {
                // device is rooted
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Root device");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("This application is not allowed on root device.");
                alertDialog.setPositiveButton("Exit", (dialog, which) -> {
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().apply();
                    SharedPref.clearSharedPref();
                    finishAffinity();
                });
                alertDialog.show();
            } else if (RootCheck.isDeviceRooted()) {
                //if (new CheckRootedDevice(this).isRTWithoutBBCheck()) {
                // device is rooted
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Root device");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("This application is not allowed on root device.");
                alertDialog.setPositiveButton("Exit", (dialog, which) -> {
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().apply();
                    SharedPref.clearSharedPref();
                    finishAffinity();
                });
                alertDialog.show();
            } else {
                continueProcess();
            }
        } else {
            continueProcess();
        }
    }

    private void continueProcess() {

        binding.laBear.clearAnimation();
        binding.laBear.setAnimation(R.raw.anim_splash_animation);


        notification_type = getIntent().getStringExtra("notification_type");
        communityId = getIntent().getStringExtra("CommunityId");
        redirectionURL = getIntent().getStringExtra("redirectionURL");
        vendorLogo = getIntent().getStringExtra("vendorLogo");
        if(redirectionURL == null || redirectionURL.isEmpty()){
            redirectionURL = getIntent().getStringExtra("RedirectionUrl");
            vendorLogo = getIntent().getStringExtra("VendorLogo");
            SharedPref.putHobbyTribeLogo(vendorLogo);
            SharedPref.putHobbyTribeUrl(redirectionURL);

        }
//        isComingFromOtherApp = getIntent().getBooleanExtra("isComingFromOtherApp", false);
//        kgiMobileNumber = getIntent().getStringExtra("kgiMobileNumber");
//        kgiName = getIntent().getStringExtra("kgiName");
//        kgiDob = getIntent().getStringExtra("kgiDob");
//        kgiEmailId = getIntent().getStringExtra("kgiEmailId");

        try {
            if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
                Uri uri = getIntent().getData();
                Log.d("Redirection", "" + getIntent().getData());
                Log.d("Redirection", "" + Objects.requireNonNull(getIntent().getData()).getQueryParameterNames());
                if (uri != null) {
                    isComingFromOtherApp = Boolean.parseBoolean(uri.getQueryParameter("isComingFromOtherApp"));
                    kgiMobileNumber = uri.getQueryParameter("kgiMobileNumber");
                    kgiName = uri.getQueryParameter("kgiName");
                    kgiDob = uri.getQueryParameter("kgiDob");
                    kgiEmailId = uri.getQueryParameter("kgiEmailId");
                }
            }
        } catch (Exception e) {
            Log.d("Redirection error", "" + e.getMessage());
        }

           /* Log.d("data", isComingFromOtherApp+"");
            Log.d("data", kgiDob);
            Log.d("data", kgiName);
            Log.d("data", kgiMobileNumber);
            Log.d("data", kgiEmailId);*/

        if (notification_type == null) {
            notification_type = getIntent().getStringExtra("NotificationType");
            communityId = getIntent().getStringExtra("CommunityId");
        }

        if (SharedPref.getIsLoggedIn()) {
            if (isComingFromOtherApp) {
                binding.tvSkip.setVisibility(View.GONE);
            } else {
                binding.tvSkip.setVisibility(View.GONE);
            }
        } else {
            binding.tvSkip.setVisibility(View.GONE);
        }


        binding.tvSkip.setOnClickListener(view -> {
            Log.d("AUthToken","Inside SKIP");

            Intent intent = new Intent(context, NewDashboardActivity.class);
            intent.putExtra("referrerUrl", referrerUrl);
            intent.putExtra("notification_type", notification_type);
            intent.putExtra("communityId", communityId);
            intent.putExtra("isKgiCommingFrom", isComingFromOtherApp);
            intent.putExtra("redirectionURL", redirectionURL);
            intent.putExtra("vendorLogo", vendorLogo);
            startActivity(intent);
            finish();
        });

        IsLoggedIn();


        binding.laBear.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isComingFromOtherApp) {
                    if (SharedPref.getIsLoggedIn()) {
                        Log.d("AUthToken","InAnimation END");
                        Intent intent = new Intent(context, NewDashboardActivity.class);
                        intent.putExtra("referrerUrl", referrerUrl);
                        intent.putExtra("communityId", communityId);
                        intent.putExtra("notification_type", notification_type);
                        intent.putExtra("happyMartImageURL", happyMartImageURL);
                        intent.putExtra("happyMartCategory", happyMartCategory);
                        intent.putExtra("happyMartRedirectionURL", happyMartRedirectionURL);
                        startActivity(intent);
                        finish();
                    } else{
                        getQuadrants();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void getQuadrants(){
        try{
            CommonUtils.showProgressDialige(this);
            apiInterface.getQuadrant().enqueue(new Callback<GetQuadrantsResponse>() {
                @Override
                public void onResponse(Call<GetQuadrantsResponse> call, Response<GetQuadrantsResponse> response) {
                    if(response.body() != null && response.code() == 200){
                        if(response.body().getData() != null && !response.body().getData().getQuadrantData().isEmpty()){
                            CommonUtils.wheelItems.clear();
                            for (int i = 0; i < response.body().getData().getQuadrantData().size(); i++) {
                                int finalI = i;
                                final Bitmap[] imageBitmap = {null};
                                new DownloadImage(bitmap -> {
                                    if (bitmap != null) {
                                        imageBitmap[0] = bitmap;
                                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap[0], 80, 80, true);

                                        WheelItem wheelItem = new WheelItem(Color.parseColor(response.body().getData().getQuadrantData().get(finalI).getQuadrantColor()),
                                                scaledBitmap, response.body().getData().getQuadrantData().get(finalI).getRewardName());
                                        CommonUtils.wheelItems.add(wheelItem);
                                        Log.d("AuthToken", String.valueOf(CommonUtils.wheelItems.size()));

                                        if(CommonUtils.wheelItems.size() == response.body().getData().getQuadrantData().size()){
                                            CommonUtils.dismissDialoge();
                                            Intent intent = new Intent(context, SpinWheelActivity.class);
                                            intent.putExtra("quadrantData", (Serializable) response.body().getData().getQuadrantData());
                                            intent.putExtra("description",response.body().getData().getDescription());
                                            intent.putExtra("timer",response.body().getData().getExpiryInMinutes());
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else {
                                        CommonUtils.dismissDialoge();
                                        //Log.e("DownloadError", "Failed to download image.");
                                    }
                                }).execute(response.body().getData().getQuadrantData().get(i).getRewardIcon());
                            }
                        }else{
                            Intent intent = new Intent(context, MobileNumberActivity.class);
                            intent.putExtra("referrerUrl", referrerUrl);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        Intent intent = new Intent(context, MobileNumberActivity.class);
                        intent.putExtra("referrerUrl", referrerUrl);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<GetQuadrantsResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    intent.putExtra("referrerUrl", referrerUrl);
                    startActivity(intent);
                    finish();

                }
            });
        }catch (Exception e){
            CommonUtils.dismissDialoge();
            Intent intent = new Intent(context, MobileNumberActivity.class);
            intent.putExtra("referrerUrl", referrerUrl);
            startActivity(intent);
            finish();
            e.printStackTrace();
        }
    }


    private void redirectToVideo() {
        try {
            if (!isComingFromOtherApp && !SharedPref.getIsLoggedIn()) {
                Intent intent1 = new Intent(SplashScreenActivity.this, VideoReaderActivity.class);
                intent1.putExtra("url", "android.resource://" + getPackageName() + "/" +
                        R.raw.splash_screen);
                intent1.putExtra("redirectTo", "Splash");
                intent1.putExtra("referelURL", referrerUrl);
                startActivity(intent1);
                finish();
            }/*else{
                Intent intent1 = new Intent(SplashScreenActivity.this, VideoReaderActivity.class);
                intent1.putExtra("url", "android.resource://" + getPackageName() + "/" +
                        R.raw.splash_screen);
                intent1.putExtra("redirectTo", "Splash");
                intent1.putExtra("referelURL",referrerUrl);
                startActivity(intent1);
                finish();
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void IsLoggedIn(){
        try{
            if (SharedPref.getIsLoggedIn() && (isRefferalCalled || apiCalledCount > 3)) {
                apiCalledCount = 0;
                if (SharedPref.getSplashScreenShowedOn().equals(CommonUtils.todayDateInFormat("dd/MM/yyyy"))) {
                    Log.d("AUthToken","Inside IsLogged");
                    Intent intent = new Intent(context, NewDashboardActivity.class);
                    intent.putExtra("referrerUrl", referrerUrl);
                    intent.putExtra("communityId", communityId);
                    intent.putExtra("notification_type", notification_type);
                    intent.putExtra("happyMartCategory", happyMartCategory);
                    intent.putExtra("happyMartImageURL", happyMartImageURL);
                    intent.putExtra("happyMartRedirectionURL", happyMartRedirectionURL);
                    startActivity(intent);
                    finish();
                } else {
                    SharedPref.putSplashScreenShowedOn(CommonUtils.todayDateInFormat("dd/MM/yyyy"));
                }
            }else{
                apiCalledCount =+ 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        IsLoggedIn();
                    }
                },150);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}