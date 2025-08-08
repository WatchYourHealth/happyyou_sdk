package com.wyh.happyyousdk.dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.VideoReaderActivity;
import com.wyh.happyyousdk.dashboard.PostLoginActivity;
import com.wyh.happyyousdk.databinding.PostLoginActivitiesAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.LevelActivity;

import java.util.List;

public class PostLoginActivitiesAdapter extends RecyclerView.Adapter<PostLoginActivitiesAdapter.MyViewHolder> {

    Context context;
    List<LevelActivity> levelActivities;
    ClickListenerInterface clickListenerInterface;

    public PostLoginActivitiesAdapter(Context context, List<LevelActivity> levelActivities, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.levelActivities = levelActivities;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostLoginActivitiesAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.post_login_activities_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position == levelActivities.size()) {
            holder.binding.rlLoginBounce.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_blue_button_bg));
            holder.binding.ivLock.setVisibility(View.VISIBLE);
            holder.binding.tvLock1.setText("Unlocked");
            holder.binding.tvArticle1.setTextColor(context.getResources().getColor(R.color.white));
            holder.binding.tvArticle1.setText("HappyYou Tour");
            Glide.with(context)
                    .load(ContextCompat.getDrawable(context, R.drawable.ic_happy_you_tour))
                    .error(R.drawable.dummy_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.binding.ivImage1);

            holder.binding.rlLoginBounce.setOnClickListener(view -> {
                Intent intent = new Intent(context, VideoReaderActivity.class);
                //intent.putExtra("url", "android.resource://" + context.getPackageName() + "/" + R.raw.intro_happy_you);
                intent.putExtra("redirectTo", "Home");
                context.startActivity(intent);
                ((PostLoginActivity) context).finish();
            });
        } else {
            LevelActivity levelActivity = levelActivities.get(position);
            holder.binding.ivImage1.setVisibility(View.VISIBLE);
            if (!levelActivity.isIsCompleted()) {
                holder.binding.rlLoginBounce.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_blue_button_bg));
                holder.binding.ivLock.setVisibility(View.VISIBLE);
                holder.binding.tvLock1.setText("Unlocked");
                holder.binding.tvArticle1.setTextColor(context.getResources().getColor(R.color.white));
                holder.binding.ivImage1.setColorFilter(context.getResources().getColor(R.color.white));
            } else {
                holder.binding.rlLoginBounce.setBackground(ContextCompat.getDrawable(context, R.drawable.login_bonus_claimed));
                holder.binding.ivLock.setVisibility(View.GONE);
                holder.binding.tvLock1.setText("Claimed");
                holder.binding.tvArticle1.setTextColor(context.getResources().getColor(in.aabhasjindal.otptextview.R.color.grey));
                holder.binding.ivImage1.setColorFilter(context.getResources().getColor(in.aabhasjindal.otptextview.R.color.grey));
            }
            holder.binding.tvArticle1.setText(levelActivity.getActivityName());
            Glide.with(context)
                    .load(levelActivity.getActivityImagePath())
                    .error(R.drawable.dummy_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.binding.ivImage1);

            holder.binding.rlLoginBounce.setOnClickListener(view -> {
                clickListenerInterface.onItemClickActivities(levelActivity, R.drawable.pink_circle);
            });
        }

    }

    public interface ClickListenerInterface {
        void onItemClickActivities(LevelActivity levelActivity, int bgDrawable);
    }

    @Override
    public int getItemCount() {
        return levelActivities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        PostLoginActivitiesAdapterBinding binding;

        public MyViewHolder(@NonNull PostLoginActivitiesAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
