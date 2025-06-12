package com.wyh.happyyousdk.SpinWheel.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {


    private ImageDownloadCallback callback;

    // Constructor with callback
    public DownloadImage(ImageDownloadCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        Bitmap image = null;
        Log.d("AuthToken","doInBackground");

        try {
            InputStream input = new URL(url).openStream();
            image = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (callback != null) {
            callback.onImageDownloaded(bitmap);  // Return the image through callback
        }

    }
}
