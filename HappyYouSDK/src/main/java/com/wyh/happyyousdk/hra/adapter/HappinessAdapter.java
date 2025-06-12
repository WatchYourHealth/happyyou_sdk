package com.wyh.happyyousdk.hra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.HappinessAdapterBinding;
import com.wyh.happyyousdk.model.response.getAnalysis.HappinessIndex;

import java.util.List;

public class HappinessAdapter extends RecyclerView.Adapter<HappinessAdapter.MyViewHolder> {

    Context context;
    List<HappinessIndex> happinessIndexList;

    public HappinessAdapter(Context context, List<HappinessIndex> happinessIndexList) {
        this.context = context;
        this.happinessIndexList = happinessIndexList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HappinessAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.happiness_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HappinessIndex happinessIndex = happinessIndexList.get(position);
        holder.binding.tvHormoneName.setText(happinessIndex.gethIharmones());
        holder.binding.tvDesc.setText(happinessIndex.getDescription());
        if (happinessIndex.isThumbsUp())
            holder.binding.ivHormone.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_happiness_index_yes));
        else
            holder.binding.ivHormone.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_happiness_index_no));
    }

    @Override
    public int getItemCount() {
        return happinessIndexList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        HappinessAdapterBinding binding;

        public MyViewHolder(@NonNull HappinessAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
