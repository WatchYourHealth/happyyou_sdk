package com.wyh.happyyousdk.dass21;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.DassInterpretationInfoBinding;

import java.util.List;

public class DassInfoAdapter extends RecyclerView.Adapter<DassInfoAdapter.MyViewHolder> {

    Context context;
    List<DassInfoPojo> info;

    public DassInfoAdapter(Context context, List<DassInfoPojo> info) {
        this.context = context;
        this.info = info;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DassInterpretationInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dass_interpretation_info, parent, false);
//        screenWidth = displayMetrics.widthPixels
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DassInfoPojo data = info.get(position);

        holder.binding.tvTitle.setText((position+1)+". "+data.getTitle());
        holder.binding.tvNormalVal.setText(data.getNormal());
        holder.binding.tvMildVal.setText(data.getMild());
        holder.binding.tvModerateVal.setText(data.getModerate());
        holder.binding.tvSevereVal.setText(data.getSevere());
        holder.binding.tvExtremelyVal.setText(data.getExtremely());

    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        DassInterpretationInfoBinding binding;

        public MyViewHolder(@NonNull DassInterpretationInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
