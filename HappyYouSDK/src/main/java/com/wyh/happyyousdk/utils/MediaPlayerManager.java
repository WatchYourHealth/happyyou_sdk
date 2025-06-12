package com.wyh.happyyousdk.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;
public class MediaPlayerManager {
    private static MediaPlayer mediaPlayer;
    private static MediaPlayerManager instance;

    private MediaPlayerManager() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> {
            // Handle playback completion
        });
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            // Handle error
            mp.reset();
            return true;
        });
    }

    public static synchronized MediaPlayerManager getInstance() {
        if (instance == null) {
            instance = new MediaPlayerManager();
        }
        return instance;
    }

    public synchronized void startMediaPlayer(Context context, int rawResourceId) {
        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawResourceId);
            if (afd != null) {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mediaPlayer.setLooping(false);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void startMediaPlayerFromUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setLooping(false);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void pauseMediaPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public synchronized void resumeMediaPlayer(int resumePosition) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
        }
    }

    public synchronized void stopMediaPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public synchronized void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            instance = null;
        }
    }

    public void start() {
        mediaPlayer.start();
    }

    public long getDuration() {
        return mediaPlayer.getDuration();
    }

    public long getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDurationInSeconds() {
        return mediaPlayer.getDuration() / 1000;
    }

    public int getCurrentPositionInSeconds() {
        return mediaPlayer.getCurrentPosition() / 1000;
    }

    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    // Listener for completion
    public void setOnMediaCompleteListener(Runnable listener) {
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(mp -> listener.run());

        }
    }
}
