package com.wyh.happyyousdk.happyMarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.happyMarket.HappyMartDisclaimerActivity;
import com.wyh.happyyousdk.model.request.KLISubResponse;
import com.wyh.happyyousdk.utils.Constants;

import java.util.ArrayList;

public class KLISubAdapter extends RecyclerView.Adapter<KLISubAdapter.MyViewHolder> {

    Context context;
    ArrayList<KLISubResponse.Data> kliList;

    public KLISubAdapter(Context context, ArrayList<KLISubResponse.Data> kliList) {
        this.context = context;
        this.kliList = kliList;
    }

    @NonNull
    @Override
    public KLISubAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.kli_sub_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KLISubAdapter.MyViewHolder holder, int position) {
        Glide.with(context).
              load(kliList.get(position).getActivityLogo()).
              into(holder.iv_partner);



        holder.kli_sub_parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HappyMartDisclaimerActivity.class);
                intent.putExtra("came_from", Constants.KLI_OTHER);
                intent.putExtra("toolbarname", kliList.get(holder.getAdapterPosition()).getActivityName());
                intent.putExtra("disclaimer",kliList.get(holder.getAdapterPosition()).getDisclaimer());
                intent.putExtra("url",kliList.get(holder.getAdapterPosition()).getRedirectUrl());
                intent.putExtra("disclaimerURL",kliList.get(holder.getAdapterPosition()).getDisclaimerImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kliList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_partner;
        LinearLayout kli_sub_parent_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_partner = itemView.findViewById(R.id.kli_partner_image);
            kli_sub_parent_layout = itemView.findViewById(R.id.kli_sub_parent_layout);
        }
    }
}
