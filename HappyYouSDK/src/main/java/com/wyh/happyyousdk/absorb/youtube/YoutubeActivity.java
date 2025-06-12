package com.wyh.happyyousdk.absorb.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityYoutubeBinding;
import com.wyh.happyyousdk.utils.SharedPref;

public class YoutubeActivity extends AppCompatActivity {

    ActivityYoutubeBinding binding;
    String videoId, tab, section, videoUrl, videoDescription, productUrl;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayerTracker youTubePlayerTracker;
    YouTubePlayer youTubePlayer;
    float seekTo;
    ImageView back, activityThunder;
    TextView activityScoreTxt;
    RelativeLayout llActivity;
    Button btnProduct;
    Runnable runnable;
    final Handler handler = new Handler();
    FrameLayout activityContainer;
    long sessionId = 0;
    long activityScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_youtube);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPref.init(this);

        btnProduct = findViewById(R.id.btn_product);
        activityThunder = findViewById(R.id.img_activity_thunder);
        youTubePlayerView = findViewById(R.id.player_view);
        back = findViewById(R.id.back);
        activityScoreTxt = findViewById(R.id.txt_activity_score);
        llActivity = findViewById(R.id.ll_activity);
        activityContainer = findViewById(R.id.container);

        Intent intent = getIntent();
        videoId = intent.getStringExtra("videoId");
        tab = intent.getStringExtra("tab");
        section = intent.getStringExtra("section");
        videoUrl = intent.getStringExtra("url");
        videoDescription = intent.getStringExtra("desc");
        seekTo = intent.getFloatExtra("seek", 0f);
        productUrl = intent.getStringExtra("product_url");

        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerTracker = new YouTubePlayerTracker();

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                youTubePlayer = initializedYouTubePlayer;
                youTubePlayer.addListener(youTubePlayerTracker);
                youTubePlayer.cueVideo(videoId, seekTo);
                youTubePlayer.play();
                super.onReady(initializedYouTubePlayer);
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                if (state == PlayerConstants.PlayerState.PLAYING) {
                    //saveVideoLog(todayDateInFormat("yyyy-MM-dd'T'HH:mm:ssZ"), tab, section, "0", youTubePlayerTracker.getCurrentSecond(), youTubePlayerTracker.getVideoDuration());
                }
                if (state == PlayerConstants.PlayerState.PAUSED) {
                   // saveVideoLog(todayDateInFormat("yyyy-MM-dd'T'HH:mm:ssZ"), tab, section, "1", youTubePlayerTracker.getCurrentSecond(), youTubePlayerTracker.getVideoDuration());
                }
                if (state == PlayerConstants.PlayerState.ENDED) {
                    //saveVideoLog(todayDateInFormat("yyyy-MM-dd'T'HH:mm:ssZ"), tab, section, "2", youTubePlayerTracker.getCurrentSecond(), youTubePlayerTracker.getVideoDuration());
                }
                super.onStateChange(youTubePlayer, state);
            }
        });

        back.setOnClickListener(v -> {
            //saveVideoLog(todayDateInFormat("yyyy-MM-dd'T'HH:mm:ssZ"), tab, section, "1", youTubePlayerTracker.getCurrentSecond(), youTubePlayerTracker.getVideoDuration());
            onBackPressed();
        });



        handler.postDelayed(runnable, 0);


    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}