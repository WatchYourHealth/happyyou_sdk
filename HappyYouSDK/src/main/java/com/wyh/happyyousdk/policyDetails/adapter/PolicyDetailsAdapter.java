package com.wyh.happyyousdk.policyDetails.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.PolicyDetailsAdapterBinding;
import com.wyh.happyyousdk.model.PolicyDetailsResponse;
import com.wyh.happyyousdk.utils.CommonUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PolicyDetailsAdapter extends RecyclerView.Adapter<PolicyDetailsAdapter.MyViewHolder> {

    Context context;
    List<PolicyDetailsResponse.Datum> policyDetailsList;

    public PolicyDetailsAdapter(Context context, List<PolicyDetailsResponse.Datum> policyDetailsList) {
        this.context = context;
        this.policyDetailsList = policyDetailsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PolicyDetailsAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.policy_details_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PolicyDetailsResponse.Datum data = policyDetailsList.get(position);
        String premiumDueDate, issueDate;

        if (data.getPremium() == null || data.getPremium().isEmpty() || data.getPremium().equalsIgnoreCase("n/A") || data.getPremium().equalsIgnoreCase("na")) {
            holder.binding.tvPremiumAmt.setText(data.getPremium());
        } else {
            holder.binding.tvPremiumAmt.setText(numberFormatConverter(policyDetailsList.get(position).getPremium()));
        }

        if (data.getInsuranceCover() == null || data.getInsuranceCover().isEmpty() || data.getInsuranceCover().equalsIgnoreCase("n/A") || data.getInsuranceCover().equalsIgnoreCase("na")) {
            holder.binding.tvInsuranceCover.setText(data.getInsuranceCover());
            holder.binding.tvPolicyDescriptionHead.setText("Your total cover is \nRs 0");
        } else {
            holder.binding.tvInsuranceCover.setText(numberFormatConverter(policyDetailsList.get(position).getInsuranceCover()));
            holder.binding.tvPolicyDescriptionHead.setText("Your total cover is \nRs " + numberFormatConverter(data.getInsuranceCover()));
        }

        if (data.getPremiumDueDate() == null || data.getPremiumDueDate().isEmpty() || data.getPremiumDueDate().equalsIgnoreCase("n/A") || data.getPremiumDueDate().equalsIgnoreCase("na")) {
            premiumDueDate = "N/A";
        } else {
            if (data.getPremiumDueDate().contains("T")) {
                premiumDueDate = CommonUtils.formatDateFromString("yyyy-MM-dd'T'HH:mm", "dd/MM/yyyy", data.getPremiumDueDate());
            } else {
                premiumDueDate = data.getPremiumDueDate();
            }
        }

        if (data.getIssueDate() == null || data.getIssueDate().isEmpty() || data.getIssueDate().equalsIgnoreCase("n/A") || data.getIssueDate().equalsIgnoreCase("na")) {
            issueDate = "N/A";
        } else {
            if (data.getPremiumDueDate().contains("T")) {
                issueDate = CommonUtils.formatDateFromString("yyyy-MM-dd'T'HH:mm", "dd/MM/yyyy", data.getIssueDate());
            } else {
                issueDate = data.getPremiumDueDate();
            }
        }

        holder.binding.tvPolicyNumber.setText(policyDetailsList.get(position).getPolicyNo());
        holder.binding.tvCustomerName.setText(policyDetailsList.get(position).getCustomerName());
        holder.binding.tvPremiumDueDate.setText(premiumDueDate);
        holder.binding.tvPremiumStatus.setText(policyDetailsList.get(position).getPremiumStatus());
        holder.binding.tvDateOfIssue.setText(issueDate);

        holder.binding.tvPolicyTerm.setText(policyDetailsList.get(position).getPolicyTerm());
        holder.binding.tvPremiumPayingTerm.setText(policyDetailsList.get(position).getPremiumPayingTerm());
        holder.binding.tvPremiumFrequency.setText(policyDetailsList.get(position).getPremiumFrequency());
        holder.binding.tvEcs.setText(policyDetailsList.get(position).getEcs());

    }

    @Override
    public int getItemCount() {
        return policyDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        PolicyDetailsAdapterBinding binding;

        public MyViewHolder(@NonNull PolicyDetailsAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public String numberFormatConverter(String amount) {
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("en", "in"));
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format.format(Double.valueOf(amount));
    }
}
