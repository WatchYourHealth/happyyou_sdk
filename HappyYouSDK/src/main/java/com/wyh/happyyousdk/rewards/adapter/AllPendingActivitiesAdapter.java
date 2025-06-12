package com.wyh.happyyousdk.rewards.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
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
import com.wyh.happyyousdk.databinding.AllPendingActivitiesAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.PendingActivitiesResponse;

import java.util.List;

public class AllPendingActivitiesAdapter extends RecyclerView.Adapter<AllPendingActivitiesAdapter.MyViewHolder> {

    Context context;
    List<PendingActivitiesResponse.Datum> allPendingActivityList;
    int[] res, resCircle;
    int bgCount = -1;
    ClickListenerInterface clickListenerInterface;

    public AllPendingActivitiesAdapter(Context context, List<PendingActivitiesResponse.Datum> allPendingActivityList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.allPendingActivityList = allPendingActivityList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllPendingActivitiesAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.all_pending_activities_adapter, parent, false);
//        screenWidth = displayMetrics.widthPixels
        res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg,
                R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg};
        resCircle = new int[]{R.drawable.pink_circle, R.drawable.blue_circle, R.drawable.orange_circle,
                R.drawable.blue_circle, R.drawable.orange_circle, R.drawable.pink_circle,
                R.drawable.orange_circle, R.drawable.pink_circle, R.drawable.blue_circle};
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (bgCount == res.length - 1) {
            bgCount = 0;
        } else {
            bgCount++;
        }

        allPendingActivityList.get(position).setPositionColor(bgCount);
        holder.binding.tvArticle1.setText(allPendingActivityList.get(position).getEventName());
        Glide.with(context)
                .load(allPendingActivityList.get(position).getEventLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivImage1);
        if (allPendingActivityList.get(position).getIsStarted() == 1) {
            holder.binding.progressActivity1.setVisibility(View.VISIBLE);
            holder.binding.tvStarted1.setVisibility(View.VISIBLE);
            holder.binding.ivCheckyellow1.setVisibility(View.GONE);
        } else {
            holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
            holder.binding.tvStarted1.setVisibility(View.GONE);
            holder.binding.ivCheckyellow1.setVisibility(View.GONE);

        }


        holder.binding.rlMainLayout.setOnClickListener(view -> {
            clickListenerInterface.onItemClickActivities(allPendingActivityList.get(position), resCircle[allPendingActivityList.get(position).getPositionColor()]);
        });
        Log.d("progress bar :", allPendingActivityList.get(position).getProgressPercentage() + " " + allPendingActivityList.get(position).getEventName());
        Log.d("index bg :", res[bgCount] + "");
        holder.binding.rlMainLayout.setBackground(ContextCompat.getDrawable(context, res[bgCount]));

        if (allPendingActivityList.get(position).getIsCompleted() == 1) {
            holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
            holder.binding.ivCheck.setVisibility(View.VISIBLE);
            holder.binding.ivCheckyellow1.setVisibility(View.GONE);
            holder.binding.progressActivity1.setVisibility(View.VISIBLE);
            holder.binding.tvStarted1.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.progressActivity1.setProgress(100, true);
            } else {
                holder.binding.progressActivity1.setProgress(100);
            }
        } else {
            holder.binding.tvCompleted1.setVisibility(View.GONE);
            holder.binding.ivCheck.setVisibility(View.GONE);
        }

        if (allPendingActivityList.get(position).getIsCompleted() == 1) {
            holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
            holder.binding.progressActivity1.setProgress(100);
        } else {
            if (allPendingActivityList.get(position).getProgressPercentage() != null) {
                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                holder.binding.progressActivity1.setProgress(allPendingActivityList.get(position).getProgressPercentage());
            }
        }
        /*if(holder.binding.progressActivity1.getVisibility() == View.VISIBLE){
            if (res[bgCount] == R.drawable.ic_light_pink_button_bg)
                holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_pink));
            else if (res[bgCount] == R.drawable.ic_light_blue_button_bg)
                holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_blue));
            else
                holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_orange));



        }*/

        /*if (allPendingActivityList.get(position).getIsStarted() == 1 && allPendingActivityList.get(position).getIsCompleted() == 0)
            holder.binding.progressActivity1.setVisibility(View.VISIBLE);
        else
            holder.binding.progressActivity1.setVisibility(View.GONE);

        if (allPendingActivityList.get(position).getIsCompleted() == 1) {
            holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
            holder.binding.progressActivity1.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.progressActivity1.setProgress(4, true);
            } else
                holder.binding.progressActivity1.setProgress(4);
        } else
            holder.binding.tvCompleted1.setVisibility(View.GONE);*/
    }

    public interface ClickListenerInterface {
        void onItemClickActivities(PendingActivitiesResponse.Datum datum, int bgDrawable);
    }

    @Override
    public int getItemCount() {
        return allPendingActivityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AllPendingActivitiesAdapterBinding binding;

        public MyViewHolder(@NonNull AllPendingActivitiesAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
