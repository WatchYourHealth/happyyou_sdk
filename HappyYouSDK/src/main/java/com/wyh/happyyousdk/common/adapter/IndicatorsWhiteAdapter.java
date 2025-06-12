package com.wyh.happyyousdk.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.IndicatorsAdapterBinding;
import com.wyh.happyyousdk.databinding.IndicatorsWhiteAdapterBinding;

public class IndicatorsWhiteAdapter extends RecyclerView.Adapter<IndicatorsWhiteAdapter.MyViewHolder> {

    Context context;
    int size;
    int selectedIndex;

    public IndicatorsWhiteAdapter(Context context, int size, int selectedIndex) {
        this.context = context;
        this.size = size;
        this.selectedIndex = selectedIndex;
    }

    public void updateSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IndicatorsWhiteAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.indicators_white_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (selectedIndex == position) {
            holder.binding.ivIndicatorSelected.setVisibility(View.VISIBLE);
            holder.binding.ivIndicatorUnselected.setVisibility(View.GONE);
        } else {
            holder.binding.ivIndicatorSelected.setVisibility(View.GONE);
            holder.binding.ivIndicatorUnselected.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        IndicatorsWhiteAdapterBinding binding;

        public MyViewHolder(@NonNull IndicatorsWhiteAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
