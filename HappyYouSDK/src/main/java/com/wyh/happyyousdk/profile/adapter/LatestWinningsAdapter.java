package com.wyh.happyyousdk.profile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.LatestWinningsAdapterBinding;
import com.wyh.happyyousdk.model.response.ProfileDetailsResponse;

import java.util.List;

public class LatestWinningsAdapter extends RecyclerView.Adapter<LatestWinningsAdapter.MyViewHolder> {

    Context context;
    List<ProfileDetailsResponse.Data.LatestWinning> latestWinnings;
    LayoutInflater mInflater;
    int[] res;
    int bgCount = 0;

    public LatestWinningsAdapter(Context context, List<ProfileDetailsResponse.Data.LatestWinning> latestWinnings) {
        this.context = context;
        this.latestWinnings = latestWinnings;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LatestWinningsAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.latest_winnings_adapter, parent, false);
        res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg,
                R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg};
        return new MyViewHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.latest_winnings_adapter;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (bgCount == res.length - 1) {
            bgCount = 0;
        } else
            bgCount++;
//        holder.binding..setVisibility(View.GONE);

        holder.binding.tvWinningName1.setText(latestWinnings.get(position).getName());
        holder.binding.rlMainLayout.setBackground(context.getResources().getDrawable(res[bgCount]));

        holder.binding.tvPointsCount.setText(latestWinnings.get(position).getPoint() + " points");
        Glide.with(context)
                .load(latestWinnings.get(position).getEventImage())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivImage1);
        /*holder.rlItem.setOnClickListener(view -> {
            Intent intent = new Intent(context, CommunityActivity.class);
            intent.putExtra("data", new Gson().toJson(dataList.get(position)));
            context.startActivity(intent);
        });*/
    }

    @Override
    public int getItemCount() {
        return latestWinnings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LatestWinningsAdapterBinding binding;

        public MyViewHolder(@NonNull LatestWinningsAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
