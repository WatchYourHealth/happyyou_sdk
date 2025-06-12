package com.wyh.happyyousdk.rewards.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivitiesHistoryAdapterBinding;
import com.wyh.happyyousdk.databinding.LayoutActivtyRewardsHistoryPopUpBinding;
import com.wyh.happyyousdk.model.response.rewards.CommonRewardHistory;
import com.wyh.happyyousdk.rewards.RewardsHistoryActivity;

import java.util.List;

public class ActivitiesHistoryAdapter extends RecyclerView.Adapter<ActivitiesHistoryAdapter.MyViewHolder> {

    Context context;
    List<CommonRewardHistory> activityDataList;

    public ActivitiesHistoryAdapter(Context context, List<CommonRewardHistory> activityDataList) {
        this.context = context;
        this.activityDataList = activityDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivitiesHistoryAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activities_history_adapter,
                parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CommonRewardHistory data = activityDataList.get(position);
        holder.binding.tvName.setText(data.getEventName());
        holder.binding.tvDetails.setText(data.getAmount() + "");
        holder.binding.tvStatus.setText(data.getTransStatus());
        holder.binding.tvDate.setText(formatDateFromString("yyyy-MM-dd", "dd/MM/yyyy", data.getTransDate()));
        if(data.getCampaignID() != null && !data.getCampaignID().isEmpty())
            holder.binding.imgInfo.setVisibility(View.VISIBLE);
        else
            holder.binding.imgInfo.setVisibility(View.GONE);

        holder.binding.imgInfo.setOnClickListener(view -> {
            showFaceScanPopUp(activityDataList.get(position));
        });
    }

    public void showFaceScanPopUp(CommonRewardHistory commonRewardHistory) {
        if (context != null) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);

            LayoutActivtyRewardsHistoryPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_activty_rewards_history_pop_up, null, false);
            alertBuilder.setView(binding.getRoot());
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.setCancelable(false);
            if (!alertDialog.isShowing())
                alertDialog.show();

            binding.tvHeader.setText(commonRewardHistory.getHeader());
            binding.tvStartDate.setText(formatDateFromString("yyyy-MM-dd", "dd/MM/yyyy", commonRewardHistory.getStartDate()));
            binding.tvEndDate.setText(formatDateFromString("yyyy-MM-dd", "dd/MM/yyyy", commonRewardHistory.getEndDate()));
            binding.tvCampaignID.setText(commonRewardHistory.getCampaignID());
            binding.tvSelfStatus.setText(commonRewardHistory.getSelfStatus());

            binding.btnNegative.setOnClickListener(view -> {
                alertDialog.dismiss();
            });

            Rect displayRectangle = new Rect();
            Window window = ((RewardsHistoryActivity) context).getWindow();

            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

            alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.75f), WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public int getItemCount() {
        return activityDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ActivitiesHistoryAdapterBinding binding;

        public MyViewHolder(@NonNull ActivitiesHistoryAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateList(List<CommonRewardHistory> list) {
        activityDataList = list;
        notifyDataSetChanged();
    }
}
