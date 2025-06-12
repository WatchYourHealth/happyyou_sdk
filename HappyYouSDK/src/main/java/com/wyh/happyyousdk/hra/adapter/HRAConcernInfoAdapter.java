package com.wyh.happyyousdk.hra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.InfoConcernItemBinding;
import com.wyh.happyyousdk.model.InfoConcernPojo;

import java.util.List;

public class HRAConcernInfoAdapter extends RecyclerView.Adapter<HRAConcernInfoAdapter.MyViewHolder> {

    Context context;
    List<InfoConcernPojo> info;

    public HRAConcernInfoAdapter(Context context, List<InfoConcernPojo> info) {
        this.context = context;
        this.info = info;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InfoConcernItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.info_concern_item, parent, false);
//        screenWidth = displayMetrics.widthPixels
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InfoConcernPojo data = info.get(position);

        holder.binding.tvTitle.setText((position+1)+". "+data.getTitle());
        holder.binding.tvBlueRange.setText(data.getBlue());
        holder.binding.tvOrangeRange.setText(data.getOrange());
        holder.binding.tvPinkRange.setText(data.getPink());

    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        InfoConcernItemBinding binding;

        public MyViewHolder(@NonNull InfoConcernItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
