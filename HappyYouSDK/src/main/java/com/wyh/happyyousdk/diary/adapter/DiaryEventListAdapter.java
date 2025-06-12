package com.wyh.happyyousdk.diary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.DiaryEventListBinding;
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse;
import com.wyh.happyyousdk.model.response.rewards.UnscratchedTokensData;

import java.util.List;

public class DiaryEventListAdapter extends RecyclerView.Adapter<DiaryEventListAdapter.MyViewHolder> {

    Context context;
    DiaryEventListResponse dataList;
    int selectId;
    ClickListenerInterface clickListenerInterface;

    public DiaryEventListAdapter(Context context, DiaryEventListResponse dataList, int selectId, ClickListenerInterface clickListenerInterface ) {
        this.context = context;
        this.dataList = dataList;
        this.clickListenerInterface = clickListenerInterface;
        this.selectId = selectId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DiaryEventListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.diary_event_list, parent, false);
//        screenWidth = displayMetrics.widthPixels
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DiaryEventListResponse.Datum data = dataList.getData().get(position);

        holder.binding.tvEventName.setText(data.getEventName());

        holder.binding.llMain.setOnClickListener(view -> {
            dataList.setSelected(data.getEventId());
            clickListenerInterface.onItemClick(data);
        });



    }

    public interface ClickListenerInterface {
        void onItemClick(DiaryEventListResponse.Datum data);
    }

    @Override
    public int getItemCount() {
        return dataList.getData().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        DiaryEventListBinding binding;

        public MyViewHolder(@NonNull DiaryEventListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
