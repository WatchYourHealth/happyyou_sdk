package com.wyh.happyyousdk.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ChipsLayoutBinding;
import com.wyh.happyyousdk.model.response.ChipsModel;

import java.util.ArrayList;

public class ChipsRecyclerViewAdapter extends RecyclerView.Adapter<ChipsRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<ChipsModel> dataList;
    ChipsRecyclerViewAdapter.OnItemClickListener listener;


    public ChipsRecyclerViewAdapter(Context context, ArrayList<ChipsModel> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChipsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChipsLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chips_layout, parent, false);
        return new ChipsRecyclerViewAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ChipsModel chipsModel = dataList.get(position);

        if (chipsModel.getName() != null) {
            holder.binding.tvChips.setText(chipsModel.getName());
        }

        try {
            if (chipsModel.isSelected()) {
                holder.binding.llChips.setBackgroundResource(R.drawable.blue_rc_bg_8dp);
                holder.binding.tvChips.setTextColor(context.getColor(R.color.white));
            } else {
                holder.binding.llChips.setBackgroundResource(R.drawable.wyh_btn_grey_border);
                holder.binding.tvChips.setTextColor(context.getColor(R.color.black));
            }
        } catch (Exception e) {
            //
        }


        holder.binding.llChips.setOnClickListener(v -> {

            if (chipsModel.isSelected()) {
                chipsModel.setSelected(false);
                listener.itemClick(chipsModel.getName(), false);
            } else {
                chipsModel.setSelected(true);
                listener.itemClick(chipsModel.getName(), true);
            }


            notifyDataSetChanged();


        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnItemClickListener {
        void itemClick(String name, boolean isAdd);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ChipsLayoutBinding binding;

        public MyViewHolder(ChipsLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

