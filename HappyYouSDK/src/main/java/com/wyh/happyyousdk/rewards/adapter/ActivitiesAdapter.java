package com.wyh.happyyousdk.rewards.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivitiesAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.EandBPendingActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyViewHolder> {

    Context context;
    List<EandBPendingActivity> pendingActivityList;
    ClickListenerInterface clickListenerInterface;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ActivitiesAdapter(Context context, List<EandBPendingActivity> pendingActivityList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.pendingActivityList = pendingActivityList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivitiesAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activities_adapter, parent, false);
//        screenWidth = displayMetrics.widthPixels
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                for (int i = 0; i < 3; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (pendingActivityList.size() > 0) {
                            holder.binding.tvArticle1.setText(pendingActivityList.get(i).getEventName());
                            Glide.with(context)
                                    .load(pendingActivityList.get(i).getEventLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);

                            checkForUploadVisibility(i, holder, holder.binding.ivUpload1);

                            if (pendingActivityList.get(i).getIsStarted() == 1){
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow1.setVisibility(View.GONE);

                            } else{
                                holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            }


                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(pendingActivityList.get(index), R.drawable.pink_circle);
                            });
                            //holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_pink));

                            if (pendingActivityList.get(i).getIsCompleted() == 1) {
                                holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity1.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity1.setProgress(4);*/
                                holder.binding.ivCheck1.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted1.setVisibility(View.GONE);
                                holder.binding.ivCheck1.setVisibility(View.GONE);
                            }
                            if(pendingActivityList.get(i).getIsCompleted() == 1){
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity1.setProgress(100);
                            }else {
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(pendingActivityList.get(i).getProgressPercentage());
                                holder.binding.progressActivity1.setProgress(p);
                            }
                        }
                    } else if (i == 1) {
                        if (pendingActivityList.size() > 1) {
                            holder.binding.tvArticle2.setText(pendingActivityList.get(i).getEventName());
                            Glide.with(context)
                                    .load(pendingActivityList.get(i).getEventLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);

                            checkForUploadVisibility(i, holder, holder.binding.ivUpload2);

                            if (pendingActivityList.get(i).getIsStarted() == 1){
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            }

                            else{
                                holder.binding.progressActivity2.setVisibility(View.INVISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            }
                            //holder.binding.progressActivity2.setIndicatorColor(context.getColor(R.color.light_blue));

                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(pendingActivityList.get(index), R.drawable.blue_circle);
                            });
                            if (pendingActivityList.get(i).getIsCompleted() == 1) {
                                holder.binding.tvCompleted2.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity2.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity2.setProgress(4);*/
                                holder.binding.ivCheck2.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted2.setVisibility(View.GONE);
                                holder.binding.ivCheck2.setVisibility(View.GONE);
                            }
                            if(pendingActivityList.get(i).getIsCompleted() == 1){
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity2.setProgress(100);
                            }else {
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                holder.binding.progressActivity2.setProgress(pendingActivityList.get(i).getProgressPercentage());
                            }
                        }else {
                            holder.binding.rlArticle2.setVisibility(View.GONE);
                            holder.binding.rlArticle3.setVisibility(View.GONE);
                        }


                    }
                    else {
                        if (pendingActivityList.size() > 2) {
                            holder.binding.tvArticle3.setText(pendingActivityList.get(i).getEventName());
                            Glide.with(context)
                                    .load(pendingActivityList.get(i).getEventLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);

                            checkForUploadVisibility(i, holder, holder.binding.ivUpload3);

                            if (pendingActivityList.get(i).getIsStarted() == 1){
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            } else {
                                holder.binding.progressActivity3.setVisibility(View.INVISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            }

                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(pendingActivityList.get(index), R.drawable.orange_circle);
                            });
                            //holder.binding.progressActivity3.setIndicatorColor(context.getColor(R.color.light_orange));
                            if (pendingActivityList.get(i).getIsCompleted() == 1) {
                                holder.binding.tvCompleted3.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity3.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity3.setProgress(4);*/
                                holder.binding.ivCheck3.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted3.setVisibility(View.GONE);
                                holder.binding.ivCheck3.setVisibility(View.GONE);
                            }

                            if (pendingActivityList.size() < 2) {
                                holder.binding.rlArticle2.setVisibility(View.GONE);
                                holder.binding.rlArticle3.setVisibility(View.GONE);
                            } else if (pendingActivityList.size() < 3) {
                                holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                                holder.binding.rlArticle3.setVisibility(View.GONE);
                            }

                            if(pendingActivityList.get(i).getIsCompleted() == 1){
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity3.setProgress(100);
                            }else {
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(pendingActivityList.get(i).getProgressPercentage());
                                holder.binding.progressActivity3.setProgress(p);
                            }
                        }else {
                            holder.binding.rlArticle3.setVisibility(View.GONE);
                        }

                    }


                }
                break;
            case 1:
                for (int i = 3; i < 6; i++) {
                    final int index = i;
                    if (i == 3) {
                        if (pendingActivityList.size() > 3) {
                            holder.binding.tvArticle1.setText(pendingActivityList.get(i).getEventName());
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(pendingActivityList.get(index), R.drawable.pink_circle);
                            });
                            //holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_pink));
                            Glide.with(context)
                                    .load(pendingActivityList.get(i).getEventLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);
                            if (pendingActivityList.get(i).getIsCompleted() == 1) {
                                holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity1.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity1.setProgress(4);
                                holder.binding.ivCheck1.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted1.setVisibility(View.GONE);
                                holder.binding.ivCheck1.setVisibility(View.GONE);
                            }
                            if (pendingActivityList.get(i).getIsCompleted() == 1) {
                                holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity1.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity1.setProgress(4);*/
                                holder.binding.ivCheck1.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted1.setVisibility(View.GONE);
                                holder.binding.ivCheck1.setVisibility(View.GONE);
                            }

                            if (pendingActivityList.get(i).getIsStarted() == 1){
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            } else{
                                holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            }

                            if(pendingActivityList.get(i).getIsCompleted() == 1){
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity1.setProgress(100);
                            }else {
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(pendingActivityList.get(i).getProgressPercentage());
                                holder.binding.progressActivity1.setProgress(p);
                            }

                            checkForUploadVisibility(i, holder, holder.binding.ivUpload1);



                        }
                    } else if (i == 4) {
                        if (pendingActivityList.size() > 4) {
                            holder.binding.tvArticle2.setText(pendingActivityList.get(i).getEventName());
                            Glide.with(context)
                                    .load(pendingActivityList.get(i).getEventLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);


                            checkForUploadVisibility(i, holder, holder.binding.ivUpload2);


                            if (pendingActivityList.get(i).getIsStarted() == 1){
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            }

                            else{
                                holder.binding.progressActivity2.setVisibility(View.INVISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            }
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(pendingActivityList.get(index), R.drawable.blue_circle);
                            });
                            //holder.binding.progressActivity2.setIndicatorColor(context.getColor(R.color.light_blue));
                            if (pendingActivityList.get(i).getIsCompleted() == 1) {
                                holder.binding.tvCompleted2.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity2.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity2.setProgress(4);*/
                                holder.binding.ivCheck2.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted2.setVisibility(View.GONE);
                                holder.binding.ivCheck2.setVisibility(View.GONE);
                            }

                            if(pendingActivityList.get(i).getIsCompleted() == 1){
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity2.setProgress(100);
                            }else {
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(pendingActivityList.get(i).getProgressPercentage());
                                holder.binding.progressActivity2.setProgress(p);
                            }
                        }else {
                            holder.binding.rlArticle2.setVisibility(View.GONE);
                            holder.binding.rlArticle3.setVisibility(View.GONE);
                        }


                    } else {
                        if (pendingActivityList.size() > 5) {
                            holder.binding.tvArticle3.setText(pendingActivityList.get(i).getEventName());
                            Glide.with(context)
                                    .load(pendingActivityList.get(i).getEventLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                   .into(holder.binding.ivImage3);

                            checkForUploadVisibility(i, holder, holder.binding.ivUpload3);
                            if (pendingActivityList.get(i).getIsStarted() == 1){
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            } else {
                                holder.binding.progressActivity3.setVisibility(View.INVISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            }
                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(pendingActivityList.get(index), R.drawable.orange_circle);
                            });
                            //holder.binding.progressActivity3.setIndicatorColor(context.getColor(R.color.light_orange));

                            if (pendingActivityList.get(i).getIsCompleted() == 1) {
                                holder.binding.tvCompleted3.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity3.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity3.setProgress(4);*/
                                holder.binding.ivCheck3.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted3.setVisibility(View.GONE);
                                holder.binding.ivCheck3.setVisibility(View.GONE);
                            }
                            if(pendingActivityList.get(i).getIsCompleted() == 1){
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity3.setProgress(100);
                            }else {
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(pendingActivityList.get(i).getProgressPercentage());
                                holder.binding.progressActivity3.setProgress(p);
                            }
                        }else {
                            holder.binding.rlArticle3.setVisibility(View.GONE);
                        }


                    }
                }
                break;
            case 2:
                for (int i = 6; i < 9; i++) {
                    final int index = i;
                    if (i == 6) {
                        if (pendingActivityList.size() > 6) {
                            holder.binding.tvArticle1.setText(pendingActivityList.get(i).getEventName());
                            Glide.with(context)
                                    .load(pendingActivityList.get(i).getEventLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);


                            checkForUploadVisibility(i, holder, holder.binding.ivUpload1);
                            if (pendingActivityList.get(i).getIsStarted() == 1){
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            } else{
                                holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            }
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(pendingActivityList.get(index), R.drawable.pink_circle);
                            });
                            //holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_pink));
                            if (pendingActivityList.get(i).getIsCompleted() == 1) {
                                holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity1.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity1.setProgress(4);*/
                                holder.binding.ivCheck1.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted1.setVisibility(View.GONE);
                                holder.binding.ivCheck1.setVisibility(View.GONE);
                            }

                            if(pendingActivityList.get(i).getIsCompleted() == 1){
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity1.setProgress(100);
                            }else {
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(pendingActivityList.get(i).getProgressPercentage());
                                holder.binding.progressActivity1.setProgress(p);
                            }
                        }
                    } else if (i == 7) {
                        if (pendingActivityList.size() > 7) {
                            holder.binding.tvArticle2.setText(pendingActivityList.get(i).getEventName());
                            Glide.with(context)
                                    .load(pendingActivityList.get(i).getEventLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);


                            checkForUploadVisibility(i, holder, holder.binding.ivUpload2);
                            if (pendingActivityList.get(i).getIsStarted() == 1){
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            }

                            else{
                                holder.binding.progressActivity2.setVisibility(View.INVISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            }
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(pendingActivityList.get(index), R.drawable.blue_circle);
                            });
                            //holder.binding.progressActivity2.setIndicatorColor(context.getColor(R.color.light_blue));

                            if (pendingActivityList.get(i).getIsCompleted() == 1) {
                                holder.binding.tvCompleted2.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity2.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity2.setProgress(4);
                                holder.binding.ivCheck2.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted2.setVisibility(View.GONE);
                                holder.binding.ivCheck2.setVisibility(View.GONE);
                            }

                            if(pendingActivityList.get(i).getIsCompleted() == 1){
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity2.setProgress(100);
                            }else {
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(pendingActivityList.get(i).getProgressPercentage());
                                holder.binding.progressActivity2.setProgress(p);
                            }
                        }else {
                            holder.binding.rlArticle2.setVisibility(View.GONE);
                            holder.binding.rlArticle3.setVisibility(View.GONE);
                        }
                    } else {
                        if (pendingActivityList.size() > 8) {
                            holder.binding.tvArticle3.setText(pendingActivityList.get(i).getEventName());
                            Glide.with(context)
                                    .load(pendingActivityList.get(i).getEventLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);

                            checkForUploadVisibility(i, holder, holder.binding.ivUpload3);
                            if (pendingActivityList.get(i).getIsStarted() == 1){
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            } else {
                                holder.binding.progressActivity3.setVisibility(View.INVISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                            }
                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickActivities(pendingActivityList.get(index), R.drawable.orange_circle);
                            });
                            //holder.binding.progressActivity3.setIndicatorColor(context.getColor(R.color.light_orange));

                            if (pendingActivityList.get(i).getIsCompleted() == 1) {
                                holder.binding.tvCompleted3.setVisibility(View.VISIBLE);
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity3.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity3.setProgress(4);
                                holder.binding.ivCheck3.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.tvCompleted3.setVisibility(View.GONE);
                                holder.binding.ivCheck3.setVisibility(View.GONE);
                            }

                            if(pendingActivityList.get(i).getIsCompleted() == 1){
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity3.setProgress(100);
                            }else {
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                int p= Math.round(pendingActivityList.get(i).getProgressPercentage());
                                holder.binding.progressActivity3.setProgress(p);
                            }
                        }else {
                            holder.binding.rlArticle3.setVisibility(View.GONE);
                        }
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

    private void checkForUploadVisibility(int i, MyViewHolder holder, ImageView ivUpload) {
        //Check for upload
        /*if (pendingActivityList.get(i).getEventType() != null && pendingActivityList.get(i).getEventType().equalsIgnoreCase("upload")) {
            if (pendingActivityList.get(i).getEventEndDate() != null) {
                try {
                    Date endDate = simpleDateFormat.parse(pendingActivityList.get(i).getEventEndDate().split("T")[0]);
                    Calendar endDateCalendar = Calendar.getInstance();
                    Calendar todayCalendar = Calendar.getInstance();
                    Date todayDate = todayCalendar.getTime();
                    long msDiff = endDate.getTime() - todayDate.getTime();
                    long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
                    Log.d("DaysDiff", "" + daysDiff + ", " + i);
                    endDateCalendar.setTime(endDate);
                    if (daysDiff <= 0) {
                        ivUpload.setVisibility(View.VISIBLE);
                    } else
                        ivUpload.setVisibility(View.GONE);

                    ivUpload.setOnClickListener(view -> {
                        clickListenerInterface.onUploadClick(pendingActivityList.get(i));
                    });

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    public interface ClickListenerInterface {
        void onItemClickActivities(EandBPendingActivity eandBPendingActivity, int bgDrawable);

        void onUploadClick(EandBPendingActivity eandBPendingActivity);
    }

    @Override
    public int getItemCount() {
        if (pendingActivityList.size() > 1) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(pendingActivityList.size())) / 3.0);
        } else {
            return 1;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ActivitiesAdapterBinding binding;

        public MyViewHolder(@NonNull ActivitiesAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
