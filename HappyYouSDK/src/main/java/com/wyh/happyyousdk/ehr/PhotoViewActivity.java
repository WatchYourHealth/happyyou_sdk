package com.wyh.happyyousdk.ehr;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.utils.TouchImageView;


public class PhotoViewActivity extends Activity {
    String imgUrl;
    TouchImageView profImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile_photo);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        profImg=findViewById(R.id.prof_img);

        // Added this to handle crash
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        imgUrl = getIntent().getStringExtra("url");

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(10.0f);
        circularProgressDrawable.setColorSchemeColors(getResources().getColor(R.color.happy_dark_grey), getResources().getColor(R.color.happy_light_grey));
        circularProgressDrawable.setCenterRadius(70f);
        circularProgressDrawable.start();

        Glide.with(PhotoViewActivity.this)
                .load(imgUrl)
                .error(R.drawable.dummy_image)
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profImg);
    }
}
