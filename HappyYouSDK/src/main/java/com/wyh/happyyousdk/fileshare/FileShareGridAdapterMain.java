package com.wyh.happyyousdk.fileshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.databinding.ItemFileShareGridBinding;
import com.wyh.happyyousdk.model.response.FileShareResp;

import java.util.ArrayList;
import java.util.List;


public class FileShareGridAdapterMain extends  RecyclerView.Adapter<FileShareGridAdapterMain.GridCardsViewHolder> {

List<FileShareResp.Data> quizRewardListItems = new ArrayList<>();
Context mContext;
String type;

onRewardClick rewardClick;

public FileShareGridAdapterMain(List<FileShareResp.Data> quizRewardListItems) {
    this.quizRewardListItems = quizRewardListItems;
}

@NonNull
@Override
public FileShareGridAdapterMain.GridCardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemFileShareGridBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_file_share_grid, parent, false);
    return new FileShareGridAdapterMain.GridCardsViewHolder(binding);
}

@Override
public void onBindViewHolder(@NonNull FileShareGridAdapterMain.GridCardsViewHolder holder, int position) {
    FileShareGridAdapter adapter = new FileShareGridAdapter(mContext,getSubList(position));
   /* FileShareGridAdapter adapter = new FileShareGridAdapter(getSubList(position), mContext, type, new onRewardClick() {
        @Override
        public void onRewardClick(Object item,String type) {
            rewardClick.onRewardClick(item,type);
        }
    });*/
    holder.binding.gvCardRotate.setAdapter(adapter);
    //adjustGridViewHeight(holder.binding.gvCardRotate, getSubList(0).size());
}

@Override
public int getItemCount() {
    return (int) Math.ceil((double)quizRewardListItems.size() / 3);
}

public class GridCardsViewHolder extends RecyclerView.ViewHolder {
    ItemFileShareGridBinding binding;

    public GridCardsViewHolder(@NonNull ItemFileShareGridBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}


public List<FileShareResp.Data> getSubList(int index) {
    // Determine the start and end indices for the sublist
    int start = index * 3;
    int end = Math.min(start + 3, quizRewardListItems.size());

    // If start index exceeds the size of the list, return an empty list
    if (start >= quizRewardListItems.size()) {
        return new ArrayList<>();
    }

    // Return the sublist
    return quizRewardListItems.subList(start, end);
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