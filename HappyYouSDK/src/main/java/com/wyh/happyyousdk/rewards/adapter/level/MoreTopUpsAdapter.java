package com.wyh.happyyousdk.rewards.adapter.level;

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
import com.wyh.happyyousdk.databinding.MoreTopUpsAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.TopUp;

import java.util.List;

public class MoreTopUpsAdapter extends RecyclerView.Adapter<MoreTopUpsAdapter.MyViewHolder> {

    Context context;
    List<TopUp> topUpList;
    ClickListenerInterface clickListenerInterface;

    int[] res, resCircle;
    int bgCount = -1;

    public MoreTopUpsAdapter(Context context, List<TopUp> topUpList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.topUpList = topUpList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MoreTopUpsAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.more_top_ups_adapter, parent, false);
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


        topUpList.get(position).setPositionColor(bgCount);
        holder.binding.rlMainLayout.setBackground(ContextCompat.getDrawable(context, res[bgCount]));

        holder.binding.tvArticle1.setText(topUpList.get(position).getTopUpName());

        Glide.with(context)
                .load(topUpList.get(position).getTopupIcon())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivImage1);

        if(topUpList.get(position).getTopUpName().toLowerCase().equals("scratch & win")){
            if(topUpList.get(position).isIsCompleted()){
                holder.binding.tvStart1.setText(topUpList.get(position).getPoint() + " points");
                holder.binding.ivInfo1.setVisibility(View.VISIBLE);
            } else {
                holder.binding.tvStart1.setText("Win Points");
                holder.binding.ivInfo1.setVisibility(View.GONE);
            }
        }else{
            holder.binding.tvStart1.setText(topUpList.get(position).getPoint() + " points");
            holder.binding.ivInfo1.setVisibility(View.VISIBLE);
        }

        if (topUpList.get(position).isIsStarted() && !topUpList.get(position).isIsCompleted()){
            holder.binding.progressActivity1.setVisibility(View.VISIBLE);
            holder.binding.tvStarted1.setVisibility(View.VISIBLE);
            holder.binding.ivCheckyellow1.setVisibility(View.GONE);
        } else{
            holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
            holder.binding.tvStarted1.setVisibility(View.GONE);
            holder.binding.ivCheckyellow1.setVisibility(View.GONE);
        }


        if (topUpList.get(position).isIsCompleted()) {
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



        if (topUpList.get(position).isIsCompleted()) {
            holder.binding.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.binding.ivCheck.setVisibility(View.GONE);
        }

        if (topUpList.get(position).getTopUpName().toLowerCase().contains("challenge") && !topUpList.get(position).getTopUpName().equalsIgnoreCase("Safety challenge")) {
            holder.binding.llbanner1.setVisibility(View.VISIBLE);
            holder.binding.tvBanner1.setText("Coming Soon");
        } else {
            if (topUpList.get(position).isIsCompleted() && topUpList.get(position).getRecurrenceDays() != -1 && topUpList.get(position).getRecurrenceDays() != 0) {
                holder.binding.llbanner1.setVisibility(View.VISIBLE);
//                holder.binding.llbanner1.setVisibility(View.GONE);
                String msg = "Come back next ";
                int recurrenceDays = topUpList.get(position).getRecurrenceDays();
                if(recurrenceDays == 7){
                    msg += "week";
                }else if(recurrenceDays == 30){
                    msg += "month";
                }else if(recurrenceDays == 90){
                    msg += "quarter";
                }else if(recurrenceDays == 365){
                    msg += "year";
                }
                holder.binding.tvBanner1.setText(msg);
            }
            else{
                holder.binding.llbanner1.setVisibility(View.GONE);
            }
        }

        if(topUpList.get(position).isIsCompleted()){
            holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
            holder.binding.progressActivity1.setProgress(100);
        }else{
            if(topUpList.get(position).getProgressPercentage() != null) {
                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                holder.binding.progressActivity1.setProgress(topUpList.get(position).getProgressPercentage());
            }
        }

        holder.binding.rlMainLayout.setOnClickListener(view -> {
            clickListenerInterface.onItemClickTopUps(topUpList.get(position), resCircle[topUpList.get(position).getPositionColor()]);
        });




    }

    public interface ClickListenerInterface {
        void onItemClickTopUps(TopUp levelActivity, int bgDrawable);
    }

    @Override
    public int getItemCount() {
        return topUpList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MoreTopUpsAdapterBinding binding;

        public MyViewHolder(@NonNull MoreTopUpsAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
