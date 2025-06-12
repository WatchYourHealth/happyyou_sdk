package com.wyh.happyyousdk.SpinWheel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.databinding.ItemRewardCardsGridBinding;
import com.wyh.happyyousdk.model.response.postloginreward.RewardItem;

import java.util.ArrayList;
import java.util.List;

public class GridCardsAdapter extends RecyclerView.Adapter<GridCardsAdapter.GridCardsViewHolder> {

    List<RewardItem> rewardItemList = new ArrayList<>();
    Context mContext;
    String type;

    onRewardClick rewardClick;

    public GridCardsAdapter(List<RewardItem> rewardItemList, String type, onRewardClick onRewardClick) {
        this.rewardItemList = rewardItemList;
        this.type = type;
        this.rewardClick = onRewardClick;
    }

    @NonNull
    @Override
    public GridCardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ItemRewardCardsGridBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_reward_cards_grid, parent, false);
        return new GridCardsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GridCardsViewHolder holder, int position) {
        RewardViewAdapter adapter = new RewardViewAdapter(getSubList(position), mContext, type, new onRewardClick() {
            @Override
            public void onRewardClick(Object item, String type) {
                rewardClick.onRewardClick(item, type);
                if (type != null && !type.isEmpty()) {
                    String t = type;
                    if (type.equalsIgnoreCase("Redeemed")) {
                        t = "Completed";
                    }
                    APILogs.INSTANCE.activityTracker("A_PlayAndWinDashboard_ViewActivityList_" + t + "Tile_Clicked", mContext);
                }
            }
        });
        holder.binding.gvCardRotate.setAdapter(adapter);
        //adjustGridViewHeight(holder.binding.gvCardRotate, getSubList(0).size());
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) rewardItemList.size() / 3);
    }

    public class GridCardsViewHolder extends RecyclerView.ViewHolder {
        ItemRewardCardsGridBinding binding;

        public GridCardsViewHolder(@NonNull ItemRewardCardsGridBinding binding) {
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
