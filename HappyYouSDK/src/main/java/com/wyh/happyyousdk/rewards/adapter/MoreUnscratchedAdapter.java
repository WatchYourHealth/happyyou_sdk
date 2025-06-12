package com.wyh.happyyousdk.rewards.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.MoreUnscratchedAdapterDataBinding;
import com.wyh.happyyousdk.model.response.rewards.UnscratchedTokensData;

import java.util.List;

public class MoreUnscratchedAdapter extends RecyclerView.Adapter<MoreUnscratchedAdapter.MyViewHolder> {

    Context context;
    List<UnscratchedTokensData> unlockedVoucherList;
    ClickListenerInterface clickListenerInterface;

    public MoreUnscratchedAdapter(Context context, List<UnscratchedTokensData> unlockedVoucherList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.unlockedVoucherList = unlockedVoucherList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MoreUnscratchedAdapterDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.more_unscratched_adapter_data, parent, false);
//        screenWidth = displayMetrics.widthPixels
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        holder.binding.tvArticle1.setText(unlockedVoucherList.get(position).getActivityName());
//        holder.binding.tvTokens1.setText(unlockedVoucherList.get(position).getActivityToken()+" Stamp");
        if(unlockedVoucherList.get(position).getActivityId() == -1){
            holder.binding.rlMainLayout.setBackgroundResource(R.drawable.scratch_card_orange_new);
        }else {
            holder.binding.rlMainLayout.setBackgroundResource(R.drawable.scratch_card_pink_new);
        }

        holder.binding.rlMainLayout.setOnClickListener(v->{
            clickListenerInterface.onItemClickUnscratchedVouchers(unlockedVoucherList.get(position));
        });

    }

    public interface ClickListenerInterface {
        void onItemClickUnscratchedVouchers(UnscratchedTokensData data);
    }

    @Override
    public int getItemCount() {
        return unlockedVoucherList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MoreUnscratchedAdapterDataBinding binding;

        public MyViewHolder(@NonNull MoreUnscratchedAdapterDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
