package com.wyh.happyyousdk.diary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.EventListItemBinding;
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse;

import java.util.List;

public class EventListItemAdapter extends RecyclerView.Adapter<EventListItemAdapter.MyViewHolder> {

    Context context;
    List<DiaryEventListResponse.Datum> dataList;
    private int selectedItem = 0;
    private int lastSelected = 0;
    private final OnItemClickListener listener;

    public EventListItemAdapter(Context context, List<DiaryEventListResponse.Datum> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(DiaryEventListResponse.Datum data);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EventListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.event_list_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.tvEventName.setText(dataList.get(position).getEventName());
        int background = (position == selectedItem) ? R.drawable.btn_bg_color : R.drawable.btn_bg;
        holder.binding.tvEventName.setBackground(context.getDrawable(background));
        Log.d("position Tag", position+" "+selectedItem);

        holder.binding.tvEventName.setOnClickListener(view -> {

            lastSelected = selectedItem;
            selectedItem = position;
            notifyItemChanged(lastSelected);
            notifyItemChanged(selectedItem);

            listener.onClick(dataList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        EventListItemBinding binding;

        public MyViewHolder(@NonNull EventListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
