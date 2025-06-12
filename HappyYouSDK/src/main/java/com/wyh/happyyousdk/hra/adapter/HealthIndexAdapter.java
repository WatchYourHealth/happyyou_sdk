package com.wyh.happyyousdk.hra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.HealthIndexAdapterBinding;

import java.util.List;

public class HealthIndexAdapter extends RecyclerView.Adapter<HealthIndexAdapter.MyViewHolder> {

    Context context;
    List<String> healthIndexFactorsList;

    public HealthIndexAdapter(Context context, List<String> healthIndexFactorsList) {
        this.context = context;
        this.healthIndexFactorsList = healthIndexFactorsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HealthIndexAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.health_index_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String html ="<div align=\"justify\" class=\"container\">\n" +
                "<div class=\"container-body\">\n <p style=\"font-size:12px\"><b>" +healthIndexFactorsList.get(position)+ "</b></p></div>";
        holder.binding.tvDesc.loadData(html, "text/html", "utf-8");

    }

    @Override
    public int getItemCount() {
        return healthIndexFactorsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        HealthIndexAdapterBinding binding;

        public MyViewHolder(@NonNull HealthIndexAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
