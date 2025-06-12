package com.wyh.happyyousdk.musicLib;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityMusicLibPlayerBinding;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MusicLibPlayerActivity extends AppCompatActivity {

    ActivityMusicLibPlayerBinding binding;
    Context context;

    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();
    ;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private boolean isPlay = false;
    public static int oneTimeOnly = 0;
    ProgressDialog progressDialog;
    boolean isPrepared = false;

    String musicUrl;

    GetDashboardDataResponse.Data.AudioFiles musicData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_lib_player);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_music_lib_player);
        context = this;
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        SharedPref.init(context);
        SharedPreference.init(context);

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        String dataList = getIntent().getStringExtra("musicData");
        musicData = new Gson().fromJson(dataList, GetDashboardDataResponse.Data.AudioFiles.class);

        mediaPlayer = new MediaPlayer();

        binding.llBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.imageViewBlur.setVisibility(View.GONE);
        Log.d("seekBar oneTimeOnly", oneTimeOnly + "");
        Glide.with(context)
                .load(musicData.getThumbnailImage())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imageView);

        binding.seekBar.setClickable(false);
        binding.title.setText(musicData.getTitle());
        binding.llPlayerControl.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                isPlay = false;
                mediaPlayer.pause();
                binding.playerControl.setImageResource(R.drawable.ic_play);
                binding.pauseBtn.setEnabled(false);
                binding.playBtn.setEnabled(true);
            } else {
                if (binding.startTime.getText().toString().equalsIgnoreCase("00:00")) {
                    //progressDialog.show();
                    binding.llMusicProgress.setVisibility(View.VISIBLE);
                    binding.playerControl.setVisibility(View.GONE);
                    binding.llPlayerControl.setEnabled(false);
                    Handler playHandler = new Handler();
                    playHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playAudio();
                        }
                    }, 200);
                } else {
                    isPlay = true;
                    mediaPlayer.start();
                    binding.playerControl.setImageResource(R.drawable.ic_pause);
                }
            }
        });

        binding.seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            isPlay = false;
            mediaPlayer.pause();
            binding.playerControl.setImageResource(R.drawable.ic_play);
            binding.pauseBtn.setEnabled(false);
            binding.playBtn.setEnabled(true);
            oneTimeOnly = 0;
        });


        /*binding.sound.setOnClickListener(v -> {
            binding.sound.setVisibility(View.GONE);
            binding.sbVolumn.setVisibility(View.VISIBLE);

            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            binding.sbVolumn.setMax(maxVolume);

            int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            binding.sbVolumn.setProgress(currVolume);
        });

        binding.sbVolumn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do Nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do Nothing
            }
        });*/


        /*binding.llMain.setOnClickListener(v->{
            if(binding.sound.getVisibility() == View.GONE){
                binding.sound.setVisibility(View.VISIBLE);
                binding.sbVolumn.setVisibility(View.GONE);
            }
        });*/


    }

    private void playAudio() {
//        progressDialog.show();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            if (!isPrepared) {
                mediaPlayer.setDataSource(musicData.getAudioUrl());
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        isPrepared = true;
                        binding.llPlayerControl.setEnabled(true);
                        mediaPlayer.start();
                        if (mediaPlayer.isPlaying()) {
                            binding.llMusicProgress.setVisibility(View.GONE);
                            binding.playerControl.setVisibility(View.VISIBLE);
                            /*if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }*/
                        }
                    }
                });
            } else {
                binding.llMusicProgress.setVisibility(View.GONE);
                binding.playerControl.setVisibility(View.VISIBLE);
                /*if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                binding.llPlayerControl.setEnabled(true);
                mediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        isPlay = true;
        binding.playerControl.setImageResource(R.drawable.ic_pause);
        binding.seekBar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 1000);
        binding.pauseBtn.setEnabled(true);
        binding.playBtn.setEnabled(false);



        /*binding.startTime.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );*/

    }

    Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (mediaPlayer != null) {
                startTime = mediaPlayer.getCurrentPosition();
                String mint = TimeUnit.MILLISECONDS.toMinutes((long) startTime) + "";
                String sec = TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) startTime)
                        ) + "";

                if (mint.length() == 1)
                    mint = "0" + mint;
                if (sec.length() == 1)
                    sec = "0" + sec;
                binding.startTime.setText(mint + ":" + sec);

                finalTime = mediaPlayer.getDuration();

                if (oneTimeOnly == 0) {
                    binding.seekBar.setMax((int) finalTime);
                    binding.endTime.setText(
                            addZero(TimeUnit.MILLISECONDS.toMinutes((long) finalTime) + "") + ":" +
                                    addZero(TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                    finalTime)) + "")
                    );
                    oneTimeOnly = 1;
                }

                binding.seekBar.setProgress((int) startTime);
                myHandler.postDelayed(this, 1000);
            } else {
                oneTimeOnly = 0;
            }
        }
    };

    private String addZero(String number) {
        if (number.length() == 1) {
            return "0" + number;
        }
        return number;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        oneTimeOnly = 0;
        myHandler.removeCallbacksAndMessages(null);
        finish();
    }

}