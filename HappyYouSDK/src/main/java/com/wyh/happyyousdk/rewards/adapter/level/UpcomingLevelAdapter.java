package com.wyh.happyyousdk.rewards.adapter.level;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.UpcomingLevelAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.LevelActivity;
import com.wyh.happyyousdk.model.response.rewards.RewardsLevel;

import java.util.List;

public class UpcomingLevelAdapter extends RecyclerView.Adapter<UpcomingLevelAdapter.MyViewHolder> {

    Context context;
    List<LevelActivity> levelActivities;
    ClickListenerInterface clickListenerInterface;
    RewardsLevel upcomingLevelActivity;

    public UpcomingLevelAdapter(Context context, List<LevelActivity> levelActivities, RewardsLevel upcomingLevelActivity, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.levelActivities = levelActivities;
        this.upcomingLevelActivity = upcomingLevelActivity;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UpcomingLevelAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.upcoming_level_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                for (int i = 0; i < 3; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (levelActivities.size() > 0) {
                            holder.binding.tvArticle1.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens1.setText(upcomingLevelActivity.getLevelName());
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);

                            /*if (levelActivities.get(i).getIsStarted() == 1)
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                            else
                                holder.binding.tvStarted1.setVisibility(View.GONE);*/

                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUpcomingActivity(levelActivities.get(index), R.drawable.pink_circle);
                            });
                        }
                    } else if (i == 1) {
                        if (levelActivities.size() > 1) {
                            holder.binding.tvArticle2.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens2.setText(upcomingLevelActivity.getLevelName());
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);


                            /*if (levelActivities.get(i).getIsStarted() == 1)
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                            else
                                holder.binding.tvStarted2.setVisibility(View.GONE);*/

                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUpcomingActivity(levelActivities.get(index), R.drawable.blue_circle);
                            });
                        }
                    } else {
                        if (levelActivities.size() > 2) {
                            holder.binding.tvArticle3.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens3.setText(upcomingLevelActivity.getLevelName());
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);


                            /*if (levelActivities.get(i).getIsStarted() == 1)
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                            else
                                holder.binding.tvStarted3.setVisibility(View.GONE);*/

                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUpcomingActivity(levelActivities.get(index), R.drawable.orange_circle);
                            });
                        }
                    }

                    if (levelActivities.size() < 2) {
                        holder.binding.rlArticle2.setVisibility(View.GONE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    } else if (levelActivities.size() < 3) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }
                }
                break;
            case 1:
                for (int i = 3; i < 6; i++) {
                    final int index = i;
                    if (i == 3) {
                        if (levelActivities.size() > 3) {
                            holder.binding.tvArticle1.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens1.setText(upcomingLevelActivity.getLevelName());
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUpcomingActivity(levelActivities.get(index), R.drawable.pink_circle);
                            });
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);


                            /*if (levelActivities.get(i).getIsStarted() == 1)
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                            else
                                holder.binding.tvStarted1.setVisibility(View.GONE);*/
                        }
                    } else if (i == 4) {
                        if (levelActivities.size() > 4) {
                            holder.binding.tvArticle2.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens2.setText(upcomingLevelActivity.getLevelName());
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);


                            /*if (levelActivities.get(i).getIsStarted() == 1)
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                            else
                                holder.binding.tvStarted2.setVisibility(View.GONE);*/

                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUpcomingActivity(levelActivities.get(index), R.drawable.blue_circle);
                            });
                        }
                    } else {
                        if (levelActivities.size() > 5) {
                            holder.binding.tvArticle3.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens3.setText(upcomingLevelActivity.getLevelName());
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);


                            /*if (levelActivities.get(i).getIsStarted() == 1)
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                            else
                                holder.binding.tvStarted3.setVisibility(View.GONE);*/

                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUpcomingActivity(levelActivities.get(index), R.drawable.orange_circle);
                            });
                        }
                    }

                    if (levelActivities.size() < 5) {
                        holder.binding.rlArticle2.setVisibility(View.GONE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    } else if (levelActivities.size() < 6) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }
                }
                break;
            case 2:
                for (int i = 6; i < 9; i++) {
                    final int index = i;
                    if (i == 6) {
                        if (levelActivities.size() > 6) {
                            holder.binding.tvArticle1.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens1.setText(upcomingLevelActivity.getLevelName());
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);


                            /*if (levelActivities.get(i).getIsStarted() == 1)
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                            else
                                holder.binding.tvStarted1.setVisibility(View.GONE);*/

                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUpcomingActivity(levelActivities.get(index), R.drawable.pink_circle);
                            });
                        }
                    } else if (i == 7) {
                        if (levelActivities.size() > 7) {
                            holder.binding.tvArticle2.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens2.setText(upcomingLevelActivity.getLevelName());
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);

                            /*if (levelActivities.get(i).getIsStarted() == 1)
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                            else
                                holder.binding.tvStarted2.setVisibility(View.GONE);*/

                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUpcomingActivity(levelActivities.get(index), R.drawable.blue_circle);
                            });
                        }
                    } else {
                        if (levelActivities.size() > 8) {
                            holder.binding.tvArticle3.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens3.setText(upcomingLevelActivity.getLevelName());
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);

                            /*if (levelActivities.get(i).getIsStarted() == 1)
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                            else
                                holder.binding.tvStarted3.setVisibility(View.GONE);*/

                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUpcomingActivity(levelActivities.get(index), R.drawable.orange_circle);
                            });
                        }
                    }

                    if (levelActivities.size() < 8) {
                        holder.binding.rlArticle2.setVisibility(View.GONE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    } else if (levelActivities.size() < 9) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }
                }
                break;
            case 3:
                holder.binding.rlArticle1.setVisibility(View.GONE);
                holder.binding.rlArticle2.setVisibility(View.GONE);
                holder.binding.rlArticle3.setVisibility(View.GONE);
                break;
        }
    }

    public interface ClickListenerInterface {
        void onItemClickUpcomingActivity(LevelActivity levelActivity, int bgDrawable);
    }

    @Override
    public int getItemCount() {
        if (levelActivities.size() > 1) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(levelActivities.size())) / 3.0);
        } else {
            return 1;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        UpcomingLevelAdapterBinding binding;

        public MyViewHolder(@NonNull UpcomingLevelAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
