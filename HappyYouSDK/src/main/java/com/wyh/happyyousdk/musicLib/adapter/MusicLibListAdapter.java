package com.wyh.happyyousdk.musicLib.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.MoreMusicAdapterItemBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.absorb.ShareBlogRequest;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.musicLib.MusicLibPlayerActivity;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicLibListAdapter extends RecyclerView.Adapter<MusicLibListAdapter.MyViewHolder> {
    Context context;
    List<GetDashboardDataResponse.Data.AudioFiles> healthTvList;

    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;
    public MusicLibListAdapter(Context context, List<GetDashboardDataResponse.Data.AudioFiles> healthTvList) {
        this.context = context;
        this.healthTvList = healthTvList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MoreMusicAdapterItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.more_music_adapter_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvDesc1.setText(healthTvList.get(position).getTitle());

        holder.binding.ivShare1.setOnClickListener(view -> {
//            getAllUserCommunities(healthTvList.get(position).getId());
        });

        Glide.with(context)
                .load(healthTvList.get(position).getThumbnailImage())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivThumbnail);

        holder.binding.cvItem.setOnClickListener(view -> {
            Intent intent = new Intent(context, MusicLibPlayerActivity.class);
            intent.putExtra("musicData", new Gson().toJson(healthTvList.get(position)));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return healthTvList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MoreMusicAdapterItemBinding binding;

        public MyViewHolder(@NonNull MoreMusicAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
