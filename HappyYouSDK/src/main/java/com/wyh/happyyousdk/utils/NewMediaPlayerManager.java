package com.wyh.happyyousdk.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

public class NewMediaPlayerManager {
    private static MediaPlayer mediaPlayer;
    private static NewMediaPlayerManager instance;
    private PlaybackProgressListener progressListener;
    private PreparedListener preparedListener;
    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private final Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying() && progressListener != null) {
                progressListener.onProgress(mediaPlayer.getCurrentPosition());
                progressHandler.postDelayed(this, 1000); // Update every second
            }
        }
    };

    private NewMediaPlayerManager() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> {
            if (progressListener != null) {
                progressListener.onProgress(mediaPlayer.getDuration());
            }
            progressHandler.removeCallbacks(progressRunnable);
        });
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            mp.reset();
            return true;
        });
    }

    public static synchronized NewMediaPlayerManager getInstance() {
        if (instance == null) {
            instance = new NewMediaPlayerManager();
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
                progressHandler.post(progressRunnable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void prepareMediaPlayerFromUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setLooping(false);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                preparedListener.onPrepared(true);
                progressHandler.post(progressRunnable);
            });
        } catch (IOException e) {
            preparedListener.onPrepared(false);
            e.printStackTrace();
        }
    }

    public synchronized void startMediaPlayerFromUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setLooping(false);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                preparedListener.onPrepared(true);

            });
        } catch (IOException e) {
            preparedListener.onPrepared(false);
            e.printStackTrace();
        }
    }

    public synchronized void pauseMediaPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            progressHandler.removeCallbacks(progressRunnable);
        }
    }

    public synchronized void resumeMediaPlayer(int resumePosition) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(resumePosition*1000);
            mediaPlayer.start();
            progressHandler.post(progressRunnable);
        }
    }

    public synchronized void stopMediaPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            progressHandler.removeCallbacks(progressRunnable);
        }
    }

    public synchronized void resetMediaPlayer() {
        if (mediaPlayer != null ) {
            progressHandler.removeCallbacks(progressRunnable);
        }
    }
    public synchronized void startMediaPlayer() {
        mediaPlayer.start();
        progressHandler.post(progressRunnable);
    }
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public synchronized void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            instance = null;
            progressHandler.removeCallbacks(progressRunnable);
        }
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

    public void setOnMediaCompleteListener(Runnable listener) {
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(mp -> {
                listener.run();
                progressHandler.removeCallbacks(progressRunnable);
            });
        }
    }

    public void setProgressListener(PlaybackProgressListener listener) {
        this.progressListener = listener;
    }

    public void setPreparedListener(PreparedListener listener) {
        this.preparedListener = listener;
    }

    public interface PlaybackProgressListener {
        void onProgress(int currentPosition);
    }

    public interface PreparedListener {
        void onPrepared(boolean isPrepared);
    }
}

