package com.wyh.happyyousdk.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityLatestWinningsBinding;
import com.wyh.happyyousdk.profile.adapter.LatestWinningsAdapter;
import com.wyh.happyyousdk.model.response.ProfileDetailsResponse;

import java.util.List;

public class ActivityLatestWinnings extends AppCompatActivity {
    ActivityLatestWinningsBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_latest_winnings);
        context=this;

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            List<ProfileDetailsResponse.Data.LatestWinning> latestWinning = (List<ProfileDetailsResponse.Data.LatestWinning>) extras.getSerializable("LatestWinning");
            setLatestWinningsRv(latestWinning);
            String cameFrom = extras.getString("came_From", "Latest Winnings");

            binding.includeToolbar.tvBack.setText(cameFrom);
            // do something with the customer
        }


        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


    }

    private void setLatestWinningsRv(List<ProfileDetailsResponse.Data.LatestWinning> latestWinning) {
        LatestWinningsAdapter latestWinningsAdapterBinding = new LatestWinningsAdapter(context, latestWinning);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvLatestWinnings.setAdapter(latestWinningsAdapterBinding);
        binding.rvLatestWinnings.setLayoutManager(gridLayoutManager);
    }
}