package com.wyh.happyyousdk.ice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ItemFetchInjuriesAdapterBinding;
import com.wyh.happyyousdk.ice.FirstAidDetailsActivity;
import com.wyh.happyyousdk.ice.FirstAidOtherDetailsActivity;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.model.response.ice.FetchInjuriesResp;

import java.util.List;

public class FetchInjuriesAdapter extends RecyclerView.Adapter<FetchInjuriesAdapter.FetchInjuriesHolder> {
    Context context;
    List<FetchInjuriesResp.Datum> fetchInjuriesResp;

    public FetchInjuriesAdapter(Context context, List<FetchInjuriesResp.Datum> fetchInjuriesResp) {
        this.fetchInjuriesResp = fetchInjuriesResp;
        this.context = context;
    }

    @NonNull
    @Override
    public FetchInjuriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFetchInjuriesAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_fetch_injuries_adapter, parent, false);
        return new FetchInjuriesHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FetchInjuriesHolder holder, int position) {

        switch(position){
            case 0:
                for (int i = 0; i < 3; i++) {
                    if (i == 0) {
                        holder.binding.ivImageInjury1.setVisibility(View.VISIBLE);
                        holder.binding.ivImageInjury1.setImageResource(R.drawable.cpr_image);
                        holder.binding.tvInjuryName1.setText("CPR");
                        holder.binding.rlInjury1.setOnClickListener(view -> {
                            Intent intent = new Intent(context, FirstAidOtherDetailsActivity
                                    .class);
                            intent.putExtra("came_from", "cpr");
                            context.startActivity(intent);
                        });

                    } else if (i == 1) {
                        holder.binding.ivImageInjury2.setVisibility(View.VISIBLE);
                        holder.binding.ivImageInjury2.setImageResource(R.drawable.stroke_image);
                        holder.binding.tvInjuryName2.setText("Stroke");
                        holder.binding.rlInjury2.setOnClickListener(view -> {
                            Intent intent = new Intent(context, FirstAidOtherDetailsActivity
                                    .class);
                            intent.putExtra("came_from", "stroke");
                            context.startActivity(intent);
                        });

                    } else {
                        holder.binding.ivImageInjury3.setVisibility(View.VISIBLE);
                        holder.binding.ivImageInjury3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.seizure_image));
                        holder.binding.tvInjuryName3.setText("Seizure");
                        holder.binding.rlInjury3.setOnClickListener(view -> {
                            Intent intent = new Intent(context, FirstAidOtherDetailsActivity
                                    .class);
                            intent.putExtra("came_from", "seizure");
                            context.startActivity(intent);
                        });
                    }
                }
                break;
            case 1:
                for (int i = 3; i < 6; i++) {
                    if (i == 3) {
                        holder.binding.ivImageInjury1.setVisibility(View.VISIBLE);
                        holder.binding.ivImageInjury1.setImageResource(R.drawable.loss_of_breathe_image);
                        holder.binding.tvInjuryName1.setText("Loss of Breath");
                        holder.binding.rlInjury1.setOnClickListener(view -> {
                            Intent intent = new Intent(context, FirstAidOtherDetailsActivity
                                    .class);
                            intent.putExtra("came_from", "loss_of_breath");
                            context.startActivity(intent);
                        });

                    } else if (i == 4) {
                        if(fetchInjuriesResp.size() > 0) {
                            setSecondCard(holder, 0);
                        }else{
                            holder.binding.rlInjury2.setVisibility(View.GONE);
                        }
                    } else {
                        if(fetchInjuriesResp.size() > 1) {
                            setThirdCard(holder, 1);
                        }else{
                            holder.binding.rlInjury3.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 2:
                for (int i =6; i < 9; i++) {
                    if (i == 6) {
                        if(fetchInjuriesResp.size() > 2) {
                            setFirstCard(holder, 2);
                        }
                    } else if (i == 7) {
                        if(fetchInjuriesResp.size() > 3) {
                            setSecondCard(holder, 3);
                        }else{
                            holder.binding.rlInjury2.setVisibility(View.GONE);
                        }
                    } else {
                        if(fetchInjuriesResp.size() > 4) {
                            setThirdCard(holder, 4);
                        }else{
                            holder.binding.rlInjury3.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 3:
                for (int i =9; i < 12; i++) {
                    if (i == 9) {
                        if(fetchInjuriesResp.size() > 5) {
                            setFirstCard(holder, 5);
                        }
                    }else {
                        holder.binding.rlInjury2.setVisibility(View.INVISIBLE);
                        holder.binding.rlInjury3.setVisibility(View.INVISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        int size = (fetchInjuriesResp.size())+4;
        return (int) Math.ceil(Double.parseDouble(String.valueOf(size)) / 3.0);
    }

    public static class FetchInjuriesHolder extends RecyclerView.ViewHolder {
        ItemFetchInjuriesAdapterBinding binding;

        public FetchInjuriesHolder(@NonNull ItemFetchInjuriesAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void setFirstCard(FetchInjuriesHolder holder, int indexPosition){
        holder.binding.ivImageInjury1.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(fetchInjuriesResp.get(indexPosition).getInjuryLogo())
                .placeholder(R.drawable.ic_ambulance)
                .into(holder.binding.ivImageInjury1);
        holder.binding.tvInjuryName1.setText(fetchInjuriesResp.get(indexPosition).getInjuryName());
        holder.binding.rlInjury1.setOnClickListener(view -> {
            Intent intent = new Intent(context, FirstAidDetailsActivity
                    .class);
            intent.putExtra("injuryType", fetchInjuriesResp.get(indexPosition).getInjuryName());
            intent.putExtra("source", fetchInjuriesResp.get(indexPosition).getInfoSource());
            context.startActivity(intent);
        });
    }

    private void setSecondCard(FetchInjuriesHolder holder, int indexPosition){
        holder.binding.ivImageInjury2.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(fetchInjuriesResp.get(indexPosition).getInjuryLogo())
                .placeholder(R.drawable.ic_ambulance)
                .into(holder.binding.ivImageInjury2);
        holder.binding.tvInjuryName2.setText(fetchInjuriesResp.get(indexPosition).getInjuryName());
        holder.binding.rlInjury2.setOnClickListener(view -> {
            Intent intent = new Intent(context, FirstAidDetailsActivity
                    .class);
            intent.putExtra("injuryType", fetchInjuriesResp.get(indexPosition).getInjuryName());
            intent.putExtra("source", fetchInjuriesResp.get(indexPosition).getInfoSource());
            context.startActivity(intent);
        });
    }

    private void setThirdCard(FetchInjuriesHolder holder, int indexPosition){
        holder.binding.ivImageInjury3.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(fetchInjuriesResp.get(indexPosition).getInjuryLogo())
                .placeholder(R.drawable.ic_ambulance)
                .into(holder.binding.ivImageInjury3);
        holder.binding.tvInjuryName3.setText(fetchInjuriesResp.get(indexPosition).getInjuryName());
        holder.binding.rlInjury3.setOnClickListener(view -> {
            Intent intent = new Intent(context, FirstAidDetailsActivity
                    .class);
            intent.putExtra("injuryType", fetchInjuriesResp.get(indexPosition).getInjuryName());
            intent.putExtra("source", fetchInjuriesResp.get(indexPosition).getInfoSource());
            context.startActivity(intent);
        });
    }
}
