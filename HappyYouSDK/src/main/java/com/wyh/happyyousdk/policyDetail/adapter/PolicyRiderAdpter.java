package com.wyh.happyyousdk.policyDetail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.AdapterPolicyRideDetailBinding;
import com.wyh.happyyousdk.model.PolicyListResp;

import java.util.List;

public class PolicyRiderAdpter extends RecyclerView.Adapter<PolicyRiderAdpter.MyViewHolder> {
    List<PolicyListResp.Content> policyDetailsList;
    Context context;
    public PolicyRiderAdpter(Context context, List<PolicyListResp.Content> policyDetailsList) {
        this.context = context;
        this.policyDetailsList=policyDetailsList;

    }

    @NonNull
    @Override
    public PolicyRiderAdpter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterPolicyRideDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_policy_ride_detail, parent, false);
        return new PolicyRiderAdpter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyRiderAdpter.MyViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return policyDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterPolicyRideDetailBinding binding;

        public MyViewHolder(@NonNull AdapterPolicyRideDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



}