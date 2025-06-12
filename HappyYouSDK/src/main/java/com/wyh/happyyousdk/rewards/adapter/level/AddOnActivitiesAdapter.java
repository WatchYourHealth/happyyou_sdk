package com.wyh.happyyousdk.rewards.adapter.level;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.MoreTopUpsAdapterBinding;
import com.wyh.happyyousdk.model.response.AddOnActivities;
import com.wyh.happyyousdk.model.response.rewards.TopUp;

import java.util.ArrayList;

public class AddOnActivitiesAdapter extends RecyclerView.Adapter<AddOnActivitiesAdapter.ViewHolder> {
    Context context;
    ArrayList<AddOnActivities> addOnActivities;
    AddOnCLick addOnCLick;
    int[] res, resCircle;
    int bgCount = -1;

    public AddOnActivitiesAdapter(Context context, ArrayList<AddOnActivities> addOnActivities, AddOnCLick addOnCLick) {
        this.context = context;
        this.addOnActivities = addOnActivities;
        this.addOnCLick = addOnCLick;
    }

    @NonNull
    @Override
    public AddOnActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MoreTopUpsAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.more_top_ups_adapter, parent, false);
        res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg,
                R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg};
        resCircle = new int[]{R.drawable.pink_circle, R.drawable.blue_circle, R.drawable.orange_circle,
                R.drawable.blue_circle, R.drawable.orange_circle, R.drawable.pink_circle,
                R.drawable.orange_circle, R.drawable.pink_circle, R.drawable.blue_circle};
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddOnActivitiesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (bgCount == res.length - 1) {
            bgCount = 0;
        } else
            bgCount++;

        addOnActivities.get(position).setPositionColor(bgCount);
        holder.binding.rlMainLayout.setBackground(ContextCompat.getDrawable(context, res[bgCount]));
        holder.binding.tvArticle1.setText(addOnActivities.get(position).getActivityName());
        Glide.with(context)
                .load(addOnActivities.get(position).getImagePath())
                .into(holder.binding.ivImage1);
        holder.binding.progressActivity1.setProgress(addOnActivities.get(position).getRewardPoints());
        holder.binding.tvStart1.setText(addOnActivities.get(position).getRewardPoints() + "points");

        if(addOnActivities.get(position).getActivityName().toLowerCase().equals("scratch & win")){
            if(addOnActivities.get(position).isCompleted()){
                holder.binding.tvStart1.setText(addOnActivities.get(position).getRewardPoints() + " points");
                holder.binding.ivInfo1.setVisibility(View.VISIBLE);
            } else {
                holder.binding.tvStart1.setText("Win Points");
                holder.binding.ivInfo1.setVisibility(View.GONE);
            }
        }else{
            holder.binding.tvStart1.setText(addOnActivities.get(position).getRewardPoints() + " points");
            holder.binding.ivInfo1.setVisibility(View.VISIBLE);
        }

        if (addOnActivities.get(position).isStarted() && !addOnActivities.get(position).isCompleted()){
            holder.binding.progressActivity1.setVisibility(View.VISIBLE);
            holder.binding.tvStarted1.setVisibility(View.VISIBLE);
            holder.binding.ivCheckyellow1.setVisibility(View.GONE);
        } else{
            holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
            holder.binding.tvStarted1.setVisibility(View.GONE);
            holder.binding.ivCheckyellow1.setVisibility(View.GONE);
        }

        if (addOnActivities.get(position).isCompleted()) {
            holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
            holder.binding.progressActivity1.setVisibility(View.VISIBLE);
            holder.binding.ivCheckyellow1.setVisibility(View.GONE);
            holder.binding.tvStarted1.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.progressActivity1.setProgress(100, true);
            } else
                holder.binding.progressActivity1.setProgress(100);
        } else
            holder.binding.tvCompleted1.setVisibility(View.GONE);

        if (addOnActivities.get(position).isCompleted()) {
            holder.binding.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.binding.ivCheck.setVisibility(View.GONE);
        }

        if(addOnActivities.get(position).isCompleted()){
            holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
            holder.binding.progressActivity1.setProgress(100);
        }else{
            /*if(topUpList.get(position).getProgressPercentage() != null) {
                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                holder.binding.progressActivity1.setProgress(topUpList.get(position).getProgressPercentage());
            }*/
        }

        holder.binding.rlMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOnCLick.onItemClickAddons(addOnActivities.get(position),resCircle[addOnActivities.get(position).getPositionColor()]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addOnActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MoreTopUpsAdapterBinding binding;

        public ViewHolder(@NonNull MoreTopUpsAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface AddOnCLick {
        void onItemClickAddons(AddOnActivities addOnActivities,int bgDrawable);
    }
}


