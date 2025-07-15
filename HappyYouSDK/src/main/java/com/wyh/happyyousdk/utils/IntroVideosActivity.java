package com.wyh.happyyousdk.utils;

import static com.wyh.happyyousdk.utils.Constants.HappyMart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.WellBeingActivity;
import com.wyh.happyyousdk.databinding.ActivityIntroVideosBinding;
import com.wyh.happyyousdk.happyMarket.NewHappyMartActivity;

import com.wyh.happyyousdk.rewards.RewardsActivity;

public class IntroVideosActivity extends AppCompatActivity {

    ActivityIntroVideosBinding binding;

    ExoPlayer exoPlayer;
    String redirectTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_videos);
        SharedPref.init(this);

        exoPlayer = new ExoPlayer.Builder(this).build();

        binding.exoPlayer.setPlayer(exoPlayer);

        binding.tvSkip.setOnClickListener(view -> redirectToScreens());

        String url = getIntent().getStringExtra("url");
        redirectTo = getIntent().getStringExtra("redirectTo");
        Uri urlUri = Uri.parse(url);
        MediaItem mediaItem = MediaItem.fromUri(urlUri);
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    redirectToScreens();
                }
                Player.Listener.super.onPlaybackStateChanged(playbackState);
            }
        });
    }

    private void redirectToScreens() {
        exoPlayer.release();
        switch (redirectTo) {
            case "HappyMart":
                SharedPref.putIsHappyMartIntroShown(true);
                Intent intent = new Intent(IntroVideosActivity.this, NewHappyMartActivity.class);
                startActivity(intent);
                finish();
                break;
            case "Rewards":
                SharedPref.putIsWinningsIntroShown(true);
                intent = new Intent(IntroVideosActivity.this, RewardsActivity.class);
                startActivity(intent);
                finish();
                break;
            case "Wellbeing":
                SharedPref.putIsWellbeingIntroShown(true);
                intent = new Intent(IntroVideosActivity.this, WellBeingActivity.class);
                startActivity(intent);
                finish();
                break;
            case "Home":
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(exoPlayer != null){
            exoPlayer.play();
        }
    }
}