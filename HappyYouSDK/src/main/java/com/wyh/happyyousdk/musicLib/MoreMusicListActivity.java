package com.wyh.happyyousdk.musicLib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityMoreMusicListBinding;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.musicLib.adapter.MusicLibListAdapter;

import java.lang.reflect.Type;
import java.util.List;

public class MoreMusicListActivity extends AppCompatActivity {

    ActivityMoreMusicListBinding binding;
    Context context;
    List<GetDashboardDataResponse.Data.AudioFiles> dataList;
    MusicLibListAdapter musicLibListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_more_music_list);
        context = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String data = extras.getString("data");
            Type type = new TypeToken<List<GetDashboardDataResponse.Data.AudioFiles>>() {}.getType();
            dataList = new Gson().fromJson(data, type);

            binding.includeToolbar.tvBack.setText("Music Library");
            binding.includeToolbar.llBack.setOnClickListener(v->{
                onBackPressed();
            });
            setRv();

            binding.ivHome.setOnClickListener(view -> {
                Intent intent = new Intent(this, NewDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });

        }
    }

    private void setRv(){
        musicLibListAdapter = new MusicLibListAdapter(context, dataList);
        binding.rvList.setAdapter(musicLibListAdapter);
        binding.rvList.setOnFlingListener(null);
    }
}