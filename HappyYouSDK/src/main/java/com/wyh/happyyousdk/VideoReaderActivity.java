package com.wyh.happyyousdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityVideoReaderBinding;
import com.wyh.happyyousdk.happyMarket.NewHappyMartActivity;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.SharedPref;

public class VideoReaderActivity extends AppCompatActivity {

    ActivityVideoReaderBinding binding;
    String redirectTo, url,referelURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_reader);
        SharedPref.init(this);


        url = getIntent().getStringExtra("url");
        redirectTo = getIntent().getStringExtra("redirectTo");
        referelURL = getIntent().getStringExtra("referelURL");
//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(binding.exoPlayer);
//        binding.exoPlayer.setMediaController(mediaController);
        binding.exoPlayer.setVideoURI(Uri.parse(url));
        binding.exoPlayer.start();
        if(redirectTo.equalsIgnoreCase("Splash")){
            if (SharedPref.getIsLoggedIn()) {
                binding.tvSkip.setVisibility(View.VISIBLE);
            } else {
                binding.tvSkip.setVisibility(View.GONE);
            }

        }

        binding.exoPlayer.setOnCompletionListener(v->{
            redirectToScreens();
        });

        binding.tvSkip.setOnClickListener(v->{
            redirectToScreens();
        });
    }

    private void redirectToScreens() {
        binding.exoPlayer.stopPlayback();
        switch (redirectTo) {
            case "HappyMart":
                SharedPref.putIsHappyMartIntroShown(true);
                Intent intent = new Intent(VideoReaderActivity.this, NewHappyMartActivity.class);
                startActivity(intent);
                finish();
                break;
            case "HappyYou":
                SharedPref.putIsHappyYouIntroShown(true);
                intent = new Intent(VideoReaderActivity.this, MobileNumberActivity.class);
                startActivity(intent);
                finish();
                break;
            case "Rewards":
                SharedPref.putIsWinningsIntroShown(true);
                intent = new Intent(VideoReaderActivity.this, RewardsActivity.class);
                startActivity(intent);
                finish();
                break;
            case "Wellbeing":
                SharedPref.putIsWellbeingIntroShown(true);
                intent = new Intent(VideoReaderActivity.this, WellBeingActivity.class);
                startActivity(intent);
                finish();
                break;
            case "Splash":
                if (SharedPref.getIsLoggedIn()) {
                     intent = new Intent(this, NewDashboardActivity.class);
                    intent.putExtra("referrerUrl", referelURL);
                    startActivity(intent);
                    finish();
                } else {
                     intent = new Intent(this, ActivityMainIntroScreen.class);
                    intent.putExtra("referrerUrl", referelURL);
                    startActivity(intent);
                    finish();
                }



            case "Home":
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.exoPlayer.stopPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.exoPlayer.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        binding.exoPlayer.stopPlayback();
        binding.exoPlayer.seekTo(0);
        /*binding.exoPlayer.setVideoURI(Uri.parse(url));
        binding.exoPlayer.start();*/
    }

}