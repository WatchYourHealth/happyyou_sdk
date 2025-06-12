package com.wyh.happyyousdk.rewards.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.EarnAndGrabHistoryAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.RewardsHistoryEnG;

import java.util.List;

public class EarnAndGrabHistoryAdapter extends RecyclerView.Adapter<EarnAndGrabHistoryAdapter.MyViewHolder> {

    Context context;
    List<RewardsHistoryEnG> historyDataList;

    public EarnAndGrabHistoryAdapter(Context context, List<RewardsHistoryEnG> historyDataList) {
        this.context = context;
        this.historyDataList = historyDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EarnAndGrabHistoryAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.earn_and_grab_history_adapter,
                parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RewardsHistoryEnG data = historyDataList.get(position);
        holder.binding.tvName.setText(data.getDescription());
        holder.binding.tvDetails.setText(data.getTokens().toString());
        holder.binding.tvStatus.setText(data.getTransStatus());
    }

    @Override
    public int getItemCount() {
        return historyDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        EarnAndGrabHistoryAdapterBinding binding;
        public MyViewHolder(@NonNull EarnAndGrabHistoryAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
