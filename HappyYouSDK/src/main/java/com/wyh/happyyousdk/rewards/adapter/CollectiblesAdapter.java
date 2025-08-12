package com.wyh.happyyousdk.rewards.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.wyh.happyyousdk.databinding.CollectiblesAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.RewardsCollectible;

import java.util.List;

public class CollectiblesAdapter extends RecyclerView.Adapter<CollectiblesAdapter.MyViewHolder> {

    Context context;
    List<RewardsCollectible> rewardsCollectibleList;
    ClickListenerInterface clickListenerInterface;
//    int[] res;
//    int bgCount = 0;

    public CollectiblesAdapter(Context context, List<RewardsCollectible> rewardsCollectibleList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.rewardsCollectibleList = rewardsCollectibleList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CollectiblesAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.collectibles_adapter, parent, false);
        /*res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg,
                R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg};*/
        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*if (bgCount == res.length - 1) {
            bgCount = 0;
        } else
            bgCount++;*/
//        holder.binding.rlMainLayout.setBackground(ContextCompat.getDrawable(context, res[bgCount]));
        RewardsCollectible rewardsCollectible = rewardsCollectibleList.get(position);
        holder.binding.rlMainLayout.setVisibility(View.GONE);
//        holder.binding.tvArticleHead1.setText("\u20B9" + rewardsCollectible.getVoucherValue());
//        holder.binding.tvArticle1.setText(rewardsCollectible.getVendorName());
        if (rewardsCollectible.isIsscrached()){

            holder.binding.cvLogo1.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.binding.rlMainLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_orange_button_bg));

            holder.binding.tvArticleHead1.setText(rewardsCollectible.getVendorName());
            holder.binding.tvArticle1.setText(rewardsCollectible.getVoucherValue() + "rs off");
            Glide.with(context)
                    .load(rewardsCollectible.getVendorLogo())
                    .error(R.drawable.dummy_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .into(holder.binding.ivVendorLogo1);


            holder.binding.rlMainLayout.setVisibility(View.VISIBLE);
        }else{

            holder.binding.rlMainLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.scratch_card_orange_new));
            holder.binding.rlMainLayout.setVisibility(View.VISIBLE);


        }


        holder.binding.rlMainLayout.setOnClickListener(view -> {
            clickListenerInterface.onItemClickScratchCard(rewardsCollectible, R.drawable.scratch_card_pink_new);
        });
    }

    @Override
    public int getItemCount() {
        return rewardsCollectibleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CollectiblesAdapterBinding binding;

        public MyViewHolder(@NonNull CollectiblesAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ClickListenerInterface {
        void onItemClickScratchCard(RewardsCollectible rewardsCollectible, int bgDrawable);
    }
}
