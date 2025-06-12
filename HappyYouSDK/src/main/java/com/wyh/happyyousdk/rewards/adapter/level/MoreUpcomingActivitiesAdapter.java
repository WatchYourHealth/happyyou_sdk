package com.wyh.happyyousdk.rewards.adapter.level;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.MoreUpcomingItemBinding;
import com.wyh.happyyousdk.model.response.rewards.LevelActivity;

import java.util.List;

public class MoreUpcomingActivitiesAdapter extends RecyclerView.Adapter<MoreUpcomingActivitiesAdapter.MyViewHolder> {

    Context context;
    List<LevelActivity> levelActivities;
    ClickListenerInterface clickListenerInterface;
    String levelName;

    public MoreUpcomingActivitiesAdapter(Context context, List<LevelActivity> levelActivities, String levelName, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.levelActivities = levelActivities;
        this.levelName = levelName;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MoreUpcomingItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.more_upcoming_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvArticle1.setText(levelActivities.get(position).getActivityName());
        holder.binding.tvTokens1.setText(levelName);
        Glide.with(context)
                .load(levelActivities.get(position).getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivImage1);

                            /*if (levelActivities.get(i).getIsStarted() == 1)
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                            else
                                holder.binding.tvStarted1.setVisibility(View.GONE);*/

        holder.binding.rlArticle1.setOnClickListener(view -> {
//            clickListenerInterface.onItemClickUpcomingActivity(levelActivities.get(index), R.drawable.pink_circle);
        });
    }

    public interface ClickListenerInterface {
        void onItemClickActivities(LevelActivity levelActivity, int bgDrawable);
    }

    @Override
    public int getItemCount() {
        return levelActivities.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MoreUpcomingItemBinding binding;

        public MyViewHolder(@NonNull MoreUpcomingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
