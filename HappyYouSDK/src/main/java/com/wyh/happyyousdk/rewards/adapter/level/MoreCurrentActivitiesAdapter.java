package com.wyh.happyyousdk.rewards.adapter.level;

import android.content.Context;
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
import com.wyh.happyyousdk.databinding.MoreCurrentActivitiesAdapterBinding;
import com.wyh.happyyousdk.model.ProgressItem;
import com.wyh.happyyousdk.model.response.rewards.LevelActivity;

import java.util.ArrayList;
import java.util.List;

public class MoreCurrentActivitiesAdapter extends RecyclerView.Adapter<MoreCurrentActivitiesAdapter.MyViewHolder> {

    Context context;
    List<LevelActivity> levelActivities;
    ClickListenerInterface clickListenerInterface;

    int[] res;
    int[] resCircle;
    int bgCount = -1;

    private ArrayList<ProgressItem> progressItemList;
    private ProgressItem mProgressItem;

    public MoreCurrentActivitiesAdapter(Context context, List<LevelActivity> levelActivities, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.levelActivities = levelActivities;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MoreCurrentActivitiesAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.more_current_activities_adapter, parent, false);
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
        } else
            bgCount++;

        LevelActivity levelActivity = levelActivities.get(position);

        levelActivity.setPositionColor(bgCount);


        Log.d("activity",  levelActivity.getProgressPercentage()+" "+levelActivity.isIsCompleted()+" "+levelActivity.isStarted()+" "+levelActivity.getActivityName());

        holder.binding.rlMainLayout.setBackground(ContextCompat.getDrawable(context, res[bgCount]));
        /*holder.binding.seekBar.getThumb().mutate().setAlpha(0);*/

        holder.binding.tvArticle1.setText(levelActivity.getActivityName());
        holder.binding.tvTokens1.setText(levelActivity.getPoint() + " points");
        Glide.with(context)
                .load(levelActivity.getActivityImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivImage1);

        if (levelActivity.isStarted() && !levelActivity.isIsCompleted()){
            holder.binding.progressBar.setVisibility(View.VISIBLE);
            holder.binding.tvStarted1.setVisibility(View.VISIBLE);
             holder.binding.ivCheckyellow1.setVisibility(View.GONE);
        } else{
            holder.binding.progressBar.setVisibility(View.INVISIBLE);
            holder.binding.tvStarted1.setVisibility(View.GONE);
            holder.binding.ivCheckyellow1.setVisibility(View.GONE);
        }

        Log.d("level", levelActivity.getProgressPercentage() + " "+ levelActivity.getActivityName()+" "+levelActivity.isIsCompleted());
        if (levelActivity.isIsCompleted()) {
            holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
            holder.binding.progressBar.setVisibility(View.VISIBLE);
            holder.binding.tvStarted1.setVisibility(View.GONE);
            holder.binding.ivCheckyellow1.setVisibility(View.GONE);
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.progressBar.setProgress(4, true);
            } else
                holder.binding.progressBar.setProgress(4);*/
        } else
            holder.binding.tvCompleted1.setVisibility(View.GONE);


        holder.binding.rlMainLayout.setOnClickListener(view -> {
                clickListenerInterface.onItemClickActivities(levelActivity, resCircle[levelActivity.getPositionColor()]);
        });

        if(levelActivity.isIsCompleted()){
            holder.binding.ivCheck.setVisibility(View.VISIBLE);
        }else{
            holder.binding.ivCheck.setVisibility(View.GONE);
        }

        if(levelActivity.isIsCompleted()){
            holder.binding.progressBar.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
            holder.binding.progressBar.setProgress(100);
        }else {
            holder.binding.progressBar.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
            holder.binding.progressBar.setProgress(Math.round(levelActivity.getProgressPercentage()));
        }



    }

    public interface ClickListenerInterface {
        void onItemClickActivities(LevelActivity levelActivity, int bgDrawable);
    }

    @Override
    public int getItemCount() {
        return levelActivities.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MoreCurrentActivitiesAdapterBinding binding;

        public MyViewHolder(@NonNull MoreCurrentActivitiesAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /*private void initDataToSeekbar(MoreCurrentActivitiesAdapterBinding binding, int percentage) {

        progressItemList = new ArrayList<ProgressItem>();

        if(percentage == 0){
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = 100;
            mProgressItem.color = R.color.grayLight;
            progressItemList.add(mProgressItem);
        }

        if(percentage > 0 && percentage <= 33){
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = percentage;
            mProgressItem.color = R.color.light_pink;
            progressItemList.add(mProgressItem);


            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = 100 - percentage;
            mProgressItem.color = R.color.grayLight;
            progressItemList.add(mProgressItem);

        }

        if(percentage > 33 && percentage <= 66){
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = 33;
            mProgressItem.color = R.color.light_pink;
            progressItemList.add(mProgressItem);

            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = 66 - percentage;
            mProgressItem.color = R.color.light_blue;
            progressItemList.add(mProgressItem);

            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = 100 - percentage;
            mProgressItem.color = R.color.grayLight;
            progressItemList.add(mProgressItem);
        }

        if(percentage > 66 && percentage <= 100){
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = 33;
            mProgressItem.color = R.color.light_pink;
            progressItemList.add(mProgressItem);

            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = percentage - 67;
            mProgressItem.color = R.color.light_blue;
            progressItemList.add(mProgressItem);

            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = percentage;
            mProgressItem.color = R.color.light_orange;
            progressItemList.add(mProgressItem);

            if(percentage != 100){
                mProgressItem = new ProgressItem();
                mProgressItem.progressItemPercentage = 100 - percentage;
                mProgressItem.color = R.color.grayLight;
                progressItemList.add(mProgressItem);
            }
        }

        binding.seekBar.initData(progressItemList);
        binding.seekBar.invalidate();
    }*/

}
