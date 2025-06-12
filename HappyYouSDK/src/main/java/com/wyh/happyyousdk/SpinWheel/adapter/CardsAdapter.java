package com.wyh.happyyousdk.SpinWheel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ItemRewardCardBinding;
import com.wyh.happyyousdk.databinding.ItemRewardCardsGridBinding;
import com.wyh.happyyousdk.model.response.postloginreward.RewardItem;

import java.util.ArrayList;
import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardsViewHolder> {

    List<RewardItem> rewardItemList = new ArrayList<>();
    Context mContext;

    public CardsAdapter(List<RewardItem> rewardItemList) {
        this.rewardItemList = rewardItemList;
    }

    @NonNull
    @Override
    public CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ItemRewardCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_reward_card, parent, false);
        return new CardsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsViewHolder holder, int position) {
        holder.binding.tvRewardTitle.setText(rewardItemList.get(position).getRewardName());
        //adjustGridViewHeight(holder.binding.rvCardRotate, getSubList(0).size());
    }

    @Override
    public int getItemCount() {
        return rewardItemList.size();
    }

    public class CardsViewHolder extends RecyclerView.ViewHolder {
        ItemRewardCardBinding binding;

        public CardsViewHolder(@NonNull ItemRewardCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public List<RewardItem> getSubList(int index) {
        // Determine the start and end indices for the sublist
        int start = index * 3;
        int end = Math.min(start + 3, rewardItemList.size());

        // If start index exceeds the size of the list, return an empty list
        if (start >= rewardItemList.size()) {
            return new ArrayList<>();
        }

        // Return the sublist
        return rewardItemList.subList(start, end);
    }

    private void adjustGridViewHeight(GridView gridView, int itemCount) {
        int rows = (int) Math.ceil((double) itemCount / gridView.getNumColumns());
        int itemHeight = mContext.getResources().getDimensionPixelSize(R.dimen.grid_item_height); // Define this dimension in res/values/dimens.xml
        int verticalSpacing = gridView.getVerticalSpacing();
        int totalHeight = rows * itemHeight + (rows - 1) * verticalSpacing;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }
}
