package com.wyh.happyyousdk.ehr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityImageReaderBinding;

public class ImageReaderActivity extends AppCompatActivity {

    ActivityImageReaderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_reader);

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());

        String imagePath = getIntent().getStringExtra("image");
        imagePath = imagePath.trim();

        Glide.with(this)
                .load(imagePath)
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imageView);
    }
}