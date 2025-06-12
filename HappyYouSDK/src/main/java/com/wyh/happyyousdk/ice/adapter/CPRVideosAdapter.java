package com.wyh.happyyousdk.ice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.CprFirstAidItemVideoBinding;
import com.wyh.happyyousdk.databinding.ItemFetchInjuriesAdapterBinding;
import com.wyh.happyyousdk.ice.ExoPlayerActivity;
import com.wyh.happyyousdk.ice.YoutubeActivity;
import com.wyh.happyyousdk.model.response.ice.FetchCPRDetailsResp;
import com.wyh.happyyousdk.model.response.ice.FetchCPRDetailsResp;

public class CPRVideosAdapter extends RecyclerView.Adapter<CPRVideosAdapter.CPRFirstAidVideoHolder> {
    Context context;
    FetchCPRDetailsResp fetchCPRDetailsResp;

    public CPRVideosAdapter(Context context, FetchCPRDetailsResp FetchCPRDetailsResp) {
        this.fetchCPRDetailsResp = FetchCPRDetailsResp;
        this.context = context;
    }

    @NonNull
    @Override
    public CPRFirstAidVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CprFirstAidItemVideoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cpr_first_aid_item_video, parent, false);
        return new CPRFirstAidVideoHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CPRFirstAidVideoHolder holder, int position) {
        Glide.with(context)
                .load(fetchCPRDetailsResp.getData().getCprVideourl().get(position).getThumbnail())
                .placeholder(R.drawable.ic_ambulance)
                .into(holder.binding.ivThumbnail);
        holder.binding.tvDesc.setText(fetchCPRDetailsResp.getData().getCprVideourl().get(position).getTitle());
        holder.binding.iv.setOnClickListener(view -> {
            if (fetchCPRDetailsResp.getData().getCprVideourl().get(position).getVideoPath().contains("mp4")) {
                Intent intent = new Intent(context, ExoPlayerActivity.class);
                intent.putExtra("url", fetchCPRDetailsResp.getData().getCprVideourl().get(position).getVideoPath());
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, YoutubeActivity.class);
                intent.putExtra("videoId", fetchCPRDetailsResp.getData().getCprVideourl().get(position).getVideoPath().split("=")[1]);
                intent.putExtra("tab", "HOME_TAB");
                intent.putExtra("section", "RECORDED_VIDEO");
                intent.putExtra("url", fetchCPRDetailsResp.getData().getCprVideourl().get(position).getVideoPath());
                intent.putExtra("desc", fetchCPRDetailsResp.getData().getCprVideourl().get(position).getTitle());
                intent.putExtra("product_url", fetchCPRDetailsResp.getData().getCprVideourl().get(position).getTitle());
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fetchCPRDetailsResp.getData().getCprVideourl().size();
    }

    public static class CPRFirstAidVideoHolder extends RecyclerView.ViewHolder {
        CprFirstAidItemVideoBinding binding;

        public CPRFirstAidVideoHolder(@NonNull CprFirstAidItemVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
