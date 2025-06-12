package com.wyh.happyyousdk.policyDetail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.AdapterPolicyNomineeDetailBinding;
import com.wyh.happyyousdk.model.PolicyListResp;

import java.util.List;

;

public class PolicyNomineeAdpter  extends RecyclerView.Adapter<PolicyNomineeAdpter.MyViewHolder> {
    List<PolicyListResp.Content> policyDetailsList;
    Context context;
    public PolicyNomineeAdpter(Context context, List<PolicyListResp.Content> policyDetailsList) {
        this.context = context;
       this.policyDetailsList=policyDetailsList;

    }

    @NonNull
    @Override
    public PolicyNomineeAdpter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterPolicyNomineeDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_policy_nominee_detail, parent, false);
        return new PolicyNomineeAdpter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyNomineeAdpter.MyViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return policyDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterPolicyNomineeDetailBinding binding;

        public MyViewHolder(@NonNull AdapterPolicyNomineeDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



}

