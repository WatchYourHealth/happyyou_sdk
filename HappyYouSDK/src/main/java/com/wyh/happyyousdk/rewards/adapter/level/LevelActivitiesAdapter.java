package com.wyh.happyyousdk.rewards.adapter.level;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.LevelActivityAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.LevelActivity;

import java.util.List;

public class LevelActivitiesAdapter extends RecyclerView.Adapter<LevelActivitiesAdapter.MyViewHolder> {

    Context context;
    List<LevelActivity> levelActivities;
    ClickListenerInterface clickListenerInterface;

    public LevelActivitiesAdapter(Context context, List<LevelActivity> levelActivities, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.levelActivities = levelActivities;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LevelActivityAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.level_activity_adapter, parent, false);
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
                            holder.binding.rlArticle1.setVisibility(View.VISIBLE);
                            holder.binding.tvArticle1.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens1.setText(levelActivities.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);

                            if (levelActivities.get(i).getActivityName().toLowerCase().contains("sync your device")) {
                                holder.binding.progressActivity1.setProgress(2);
                            }

                            if (levelActivities.get(i).isStarted()){
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            }


                            if (levelActivities.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity1.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity1.setProgress(4);*/
                                holder.binding.ivCheck1.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted1.setVisibility(View.GONE);
                                holder.binding.ivCheck1.setVisibility(View.GONE);
                            }

                            if(levelActivities.get(i).isIsCompleted()){
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity1.setProgress(100);
                            }else {
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(levelActivities.get(i).getProgressPercentage());
                                holder.binding.progressActivity1.setProgress(p);
                            }
                            //holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_pink));

                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(levelActivities.get(index), R.drawable.pink_circle);
                            });
                        } else {
                            holder.binding.rlArticle1.setVisibility(View.INVISIBLE);
                        }
                    } else if (i == 1) {
                        if (levelActivities.size() > 1) {
                            holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                            holder.binding.tvArticle2.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens2.setText(levelActivities.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);

                            if (levelActivities.get(i).getActivityName().toLowerCase().contains("sync your device")) {
                                holder.binding.progressActivity2.setProgress(2);
                            }

                            if (levelActivities.get(i).isStarted()){
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);

                            }

                            if (levelActivities.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity2.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity2.setProgress(4);*/
                                holder.binding.ivCheck2.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted2.setVisibility(View.GONE);
                                holder.binding.ivCheck2.setVisibility(View.GONE);
                            }

                            if(levelActivities.get(i).isIsCompleted()){
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity2.setProgress(100);
                            }else {
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(levelActivities.get(i).getProgressPercentage());
                                holder.binding.progressActivity2.setProgress(p);
                            }

                            //holder.binding.progressActivity2.setIndicatorColor(context.getColor(R.color.light_blue));
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(levelActivities.get(index), R.drawable.blue_circle);
                            });
                        } else {
                            holder.binding.rlArticle2.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (levelActivities.size() > 2) {
                            holder.binding.rlArticle3.setVisibility(View.VISIBLE);
                            holder.binding.tvArticle3.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens3.setText(levelActivities.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);


                            if (levelActivities.get(i).getActivityName().toLowerCase().contains("sync your device")) {
                                holder.binding.progressActivity3.setProgress(2);
                            }
                            if (levelActivities.get(i).isStarted()){
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            }  else{
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.progressActivity3.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            }



                            if (levelActivities.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity3.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity3.setProgress(4);*/
                                holder.binding.ivCheck3.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted3.setVisibility(View.GONE);
                                holder.binding.ivCheck3.setVisibility(View.GONE);
                            }

                            if(levelActivities.get(i).isIsCompleted()){
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity3.setProgress(100);
                            }else {
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(levelActivities.get(i).getProgressPercentage());
                                holder.binding.progressActivity3.setProgress(p);
                            }

                            // holder.binding.progressActivity3.setIndicatorColor(context.getColor(R.color.light_orange));
                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(levelActivities.get(index), R.drawable.orange_circle);
                            });
                        } else {
                            holder.binding.rlArticle3.setVisibility(View.INVISIBLE);
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
                            holder.binding.tvTokens1.setText(levelActivities.get(i).getPoint() + " points");
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(levelActivities.get(index), R.drawable.pink_circle);
                            });
                            //holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_pink));
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);

                            if (levelActivities.get(i).getActivityName().toLowerCase().contains("sync your device")) {
                                holder.binding.progressActivity1.setProgress(2);
                            }

                            if (levelActivities.get(i).isStarted()){
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            }
                            if (levelActivities.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    holder.binding.progressActivity1.setProgress(4, true);
//                                } else
//                                    holder.binding.progressActivity1.setProgress(4);
                                holder.binding.ivCheck1.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted1.setVisibility(View.GONE);
                                holder.binding.ivCheck1.setVisibility(View.GONE);
                            }

                            if(levelActivities.get(i).isIsCompleted()){
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity1.setProgress(100);
                            }else {
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(levelActivities.get(i).getProgressPercentage());
                                holder.binding.progressActivity1.setProgress(p);
                            }

                        }
                    } else if (i == 4) {
                        if (levelActivities.size() > 4) {
                            holder.binding.tvArticle2.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens2.setText(levelActivities.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);

                            if (levelActivities.get(i).getActivityName().toLowerCase().contains("sync your device")) {
                                holder.binding.progressActivity2.setProgress(2);
                            }
                            if (levelActivities.get(i).isStarted()){
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            }
                            if (levelActivities.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity2.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity2.setProgress(4);*/
                                holder.binding.ivCheck2.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted2.setVisibility(View.GONE);
                                holder.binding.ivCheck2.setVisibility(View.GONE);
                            }
                            //holder.binding.progressActivity2.setIndicatorColor(context.getColor(R.color.light_blue));
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(levelActivities.get(index), R.drawable.blue_circle);
                            });

                            if(levelActivities.get(i).isIsCompleted()){
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity2.setProgress(100);
                            }else {
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(levelActivities.get(i).getProgressPercentage());
                                holder.binding.progressActivity2.setProgress(p);
                            }
                        }
                    } else {
                        if (levelActivities.size() > 5) {
                            holder.binding.tvArticle3.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens3.setText(levelActivities.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);

                            if (levelActivities.get(i).getActivityName().toLowerCase().contains("sync your device")) {
                                holder.binding.progressActivity3.setProgress(2);
                            }
                            if (levelActivities.get(i).isStarted()){
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            }  else{
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.progressActivity3.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            }
                            if (levelActivities.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity3.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity3.setProgress(4);
                                holder.binding.ivCheck3.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted3.setVisibility(View.GONE);
                                holder.binding.ivCheck3.setVisibility(View.GONE);
                            }
                            //holder.binding.progressActivity3.setIndicatorColor(context.getColor(R.color.light_orange));

                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(levelActivities.get(index), R.drawable.orange_circle);
                            });

                            if(levelActivities.get(i).isIsCompleted()){
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity3.setProgress(100);
                            }else {
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                int p= Math.round(levelActivities.get(i).getProgressPercentage());
                                holder.binding.progressActivity3.setProgress(p);
                            }
                        }
                    }
                }
                if (levelActivities.size() < 5) {
                    holder.binding.rlArticle2.setVisibility(View.GONE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                } else if (levelActivities.size() < 6) {
                    holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                }
                break;
            case 2:
                for (int i = 6; i < 9; i++) {
                    final int index = i;
                    if (i == 6) {
                        if (levelActivities.size() > 6) {
                            holder.binding.tvArticle1.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens1.setText(levelActivities.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);

                            if (levelActivities.get(i).getActivityName().toLowerCase().contains("sync your device")) {
                                holder.binding.progressActivity1.setProgress(2);
                            }
                            if (levelActivities.get(i).isStarted()){
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            }
                            if (levelActivities.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity1.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity1.setProgress(4);*/
                                holder.binding.ivCheck1.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted1.setVisibility(View.GONE);
                                holder.binding.ivCheck1.setVisibility(View.GONE);
                            }
                            //holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_pink));
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(levelActivities.get(index), R.drawable.pink_circle);
                            });
                            if(levelActivities.get(i).isIsCompleted()){
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity1.setProgress(100);
                            }else {
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(levelActivities.get(i).getProgressPercentage());
                                holder.binding.progressActivity1.setProgress(p);
                            }
                        }
                    } else if (i == 7) {
                        if (levelActivities.size() > 7) {
                            holder.binding.tvArticle2.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens2.setText(levelActivities.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);
                            if (levelActivities.get(i).getActivityName().toLowerCase().contains("sync your device")) {
                                holder.binding.progressActivity2.setProgress(2);
                            }
                            if (levelActivities.get(i).isStarted()){
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);

                            }
                            if (levelActivities.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity2.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity2.setProgress(4);*/
                                holder.binding.ivCheck2.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted2.setVisibility(View.GONE);
                                holder.binding.ivCheck2.setVisibility(View.GONE);
                            }
                            //holder.binding.progressActivity2.setIndicatorColor(context.getColor(R.color.light_blue));
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(levelActivities.get(index), R.drawable.blue_circle);
                            });

                            if(levelActivities.get(i).isIsCompleted()){
                                holder.binding.progressActivity2.setProgress(100);
                            }else {
                                int p= Math.round(levelActivities.get(i).getProgressPercentage());
                                holder.binding.progressActivity2.setProgress(p);
                            }
                        }
                    } else {
                        if (levelActivities.size() > 8) {
                            holder.binding.tvArticle3.setText(levelActivities.get(i).getActivityName());
                            holder.binding.tvTokens3.setText(levelActivities.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);

                            if (levelActivities.get(i).getActivityName().toLowerCase().contains("sync your device")) {
                                holder.binding.progressActivity3.setProgress(2);
                            }
                            if (levelActivities.get(i).isStarted()){
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            }  else{
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.progressActivity3.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            }
                            if (levelActivities.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity3.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity3.setProgress(4);*/
                                holder.binding.ivCheck3.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted3.setVisibility(View.GONE);
                                holder.binding.ivCheck3.setVisibility(View.GONE);
                            }
                            //holder.binding.progressActivity3.setIndicatorColor(context.getColor(R.color.light_orange));
                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(levelActivities.get(index), R.drawable.orange_circle);
                            });
                            if(levelActivities.get(i).isIsCompleted()){
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity3.setProgress(100);
                            }else {
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(levelActivities.get(i).getProgressPercentage());
                                holder.binding.progressActivity3.setProgress(p);
                            }
                        }
                    }
                }
                if (levelActivities.size() < 8) {
                    holder.binding.rlArticle2.setVisibility(View.GONE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                } else if (levelActivities.size() < 9) {
                    holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
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
        void onItemClickActivities(LevelActivity levelActivity, int bgDrawable);

        void onUploadClick(LevelActivity levelActivity);
    }

    @Override
    public int getItemCount() {
        if (levelActivities.size() > 3 && levelActivities.size() <= 9) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(levelActivities.size())) / 3.0);
        } else if (levelActivities.size() > 9)
            return 3;
        else {
            return 1;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LevelActivityAdapterBinding binding;

        public MyViewHolder(@NonNull LevelActivityAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
