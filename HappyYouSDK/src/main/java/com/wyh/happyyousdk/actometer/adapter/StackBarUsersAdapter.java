package com.wyh.happyyousdk.actometer.adapter;


import static com.wyh.happyyousdk.utils.Constants.INDIVIDUAL;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.actometer.ActOMeterActivity;

import com.wyh.happyyousdk.databinding.UsersListAdapterBinding;

import java.util.List;

public class StackBarUsersAdapter extends RecyclerView.Adapter<StackBarUsersAdapter.MyViewHolder> {

    Context context;
    List<String> userName;
    String communityName, communityId, communityType,activityType;
    int[] colors;

    public StackBarUsersAdapter(Context context, List<String> userName, String communityId, String communityName, String communityType, int[] colors, String activityType) {
        this.context = context;
        this.userName = userName;
        this.communityId = communityId;
        this.communityName = communityName;
        this.communityType = communityType;
        this.colors=colors;
        this.activityType=activityType;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UsersListAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.users_list_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    /*public void updatePeriodType(String periodType) {
        this.periodType = periodType;
    }*/

    public void updateActivityType(String activityType) {
        this.activityType = activityType;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.tvUserName.setText(userName.get(position));
        //        holder.binding.tvArticleTag.setBackground(context.getDrawable(R.drawable.wyh_round_btn_grey));
//        int background = (position == selectedItem) ? R.drawable.ic_blue_button_bg : R.drawable.ic_gray_button_bg;

//        holder.binding.tvUserName.setBackgroundColor(context.getColor(colors[position]));

        holder.binding.tvUserName.setOnClickListener(view -> {

            Intent intent = new Intent(context, ActOMeterActivity.class);
            intent.putExtra("activityType", activityType);
            intent.putExtra("type", INDIVIDUAL);
            intent.putExtra("communityId", communityId);
            intent.putExtra("communityName", communityName);
            intent.putExtra("communityType", communityType);
            intent.putExtra("user", userName.get(position));
            context.startActivity(intent);
//            String tagName = activityType;
//            if (context instanceof ActOMeterActivity) {
//                String diagnosticType;
//                if (tagNameList.get(position).equals("BloodPressure")) {
//                    diagnosticType = "Systolic";
//                } else {
//                    diagnosticType = tagNameList.get(position);
//                }
//                ((ActOMeterActivity) context).fetchCommunityGraphDataSingle(periodType, tagName, communityId, DAILY, diagnosticType);

        });
    }

    @Override
    public int getItemCount() {
        return userName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        UsersListAdapterBinding binding;

        public MyViewHolder(@NonNull UsersListAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
