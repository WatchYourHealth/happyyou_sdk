package com.wyh.happyyousdk.happyMarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.HappyMartPartnerItemBinding;
import com.wyh.happyyousdk.model.HappyMartModel;

import java.util.List;


public class HappyMartPartnerAdapter extends RecyclerView.Adapter<HappyMartPartnerAdapter.MyViewHolder> {

    Context context;
    List<HappyMartModel> list;
    private final HappyMartPartnerAdapter.OnItemClickListener listener;

    public HappyMartPartnerAdapter(Context context, List<HappyMartModel> list, HappyMartPartnerAdapter.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HappyMartPartnerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HappyMartPartnerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.happy_mart_partner_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HappyMartModel data = list.get(position);
        holder.binding.ivPartner.setImageResource(data.getImage());
        holder.binding.ivPartner.setOnClickListener(v->{
            listener.onClick(data);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        HappyMartPartnerItemBinding binding;

        public MyViewHolder(@NonNull HappyMartPartnerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick(HappyMartModel image);
    }
}
