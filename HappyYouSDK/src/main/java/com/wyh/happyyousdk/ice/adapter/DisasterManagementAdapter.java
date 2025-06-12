package com.wyh.happyyousdk.ice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.DisasterManagementAdapterBinding;
import com.wyh.happyyousdk.databinding.ItemFetchInjuriesAdapterBinding;
import com.wyh.happyyousdk.ice.FirstAidDetailsActivity;
import com.wyh.happyyousdk.model.response.ice.FetchInjuriesResp;

import java.util.List;

public class DisasterManagementAdapter extends RecyclerView.Adapter<DisasterManagementAdapter.FetchInjuriesHolder> {
    Context context;
    List<FetchInjuriesResp.Datum> fetchInjuriesResp;

    public DisasterManagementAdapter(Context context, List<FetchInjuriesResp.Datum> fetchInjuriesResp) {
        this.fetchInjuriesResp = fetchInjuriesResp;
        this.context = context;
    }

    @NonNull
    @Override
    public FetchInjuriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DisasterManagementAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.disaster_management_adapter, parent, false);
        return new FetchInjuriesHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FetchInjuriesHolder holder, int position) {
        Glide.with(context)
                .load(fetchInjuriesResp.get(position).getInjuryLogo())
                .placeholder(R.drawable.ic_ambulance)
                .into(holder.binding.ivImageInjury);
        holder.binding.tvInjuryName.setText(fetchInjuriesResp.get(position).getInjuryName());
        holder.binding.rlInjury.setOnClickListener(view -> {
            Intent intent = new Intent(context, FirstAidDetailsActivity
                    .class);
            intent.putExtra("injuryType", fetchInjuriesResp.get(position).getInjuryName());
            intent.putExtra("source", fetchInjuriesResp.get(position).getInfoSource());
            context.startActivity(intent);
        });
        if (fetchInjuriesResp.get(position).getColorCode().equalsIgnoreCase("Red")) {
            holder.binding.rlInjury.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_pink_button_bg));
        } else if (fetchInjuriesResp.get(position).getColorCode().equalsIgnoreCase("Blue")) {
            holder.binding.rlInjury.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_button_bg));
        } else {
            holder.binding.rlInjury.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_orange_button_bg));
        }
    }

    @Override
    public int getItemCount() {
        return fetchInjuriesResp.size();
    }

    public static class FetchInjuriesHolder extends RecyclerView.ViewHolder {
        DisasterManagementAdapterBinding binding;

        public FetchInjuriesHolder(@NonNull DisasterManagementAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
