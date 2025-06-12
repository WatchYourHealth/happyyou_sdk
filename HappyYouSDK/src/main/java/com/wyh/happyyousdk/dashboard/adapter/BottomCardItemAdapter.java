package com.wyh.happyyousdk.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.crypto.CryptoHelper;
import com.wyh.happyyousdk.dashboard.model.DashboardBottomCardDataResponse;
import com.wyh.happyyousdk.databinding.DashboardBottomCardItemBinding;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.List;

public class BottomCardItemAdapter extends ArrayAdapter<DashboardBottomCardDataResponse.tilesData> {

    Context context;
    HomeCardsAdapterNew.ClickInterface clickInterface;
    List<DashboardBottomCardDataResponse.tilesData> dataList;

    int progressGoals;
    //int bgIndex = 0;

    int[] res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg};

    public BottomCardItemAdapter(Context context, List<DashboardBottomCardDataResponse.tilesData> dataList, int progressGoals, HomeCardsAdapterNew.ClickInterface clickInterface) {
        super(context, 0, dataList);
        this.context = context;
        this.dataList = dataList;
        this.clickInterface = clickInterface;
        this.progressGoals = progressGoals;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            DashboardBottomCardItemBinding itemBinding = DashboardBottomCardItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new ViewHolder(itemBinding);
            holder.view = itemBinding.getRoot();
            holder.view.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.binding.tvGoal1.setText(dataList.get(position).getName());
        holder.binding.tvGoalDesc1.setText(dataList.get(position).getDescription());

        if (dataList.get(position) != null && dataList.get(position).getName() != null && dataList.get(position).getName().toLowerCase().equals("goals")) {
            int stepGoals = SharedPref.getStepsGoals();
            holder.binding.tvStepGoal1.setVisibility(View.VISIBLE);
            holder.binding.tvStepGoalUnit1.setVisibility(View.VISIBLE);
            holder.binding.progressGoal1.setVisibility(View.VISIBLE);
            holder.binding.tvStepGoal1.setText(stepGoals + "");
            holder.binding.progressGoal1.setProgress(progressGoals);
        } else {
            holder.binding.tvStepGoal1.setVisibility(View.GONE);
            holder.binding.tvStepGoalUnit1.setVisibility(View.GONE);
            holder.binding.progressGoal1.setVisibility(View.GONE);
        }

        if (dataList.get(position).getName().equalsIgnoreCase("Health Score") || dataList.get(position).getName().equalsIgnoreCase("Vitals") || dataList.get(position).getName().equalsIgnoreCase("Face Scan")) {
            holder.binding.ivHeart1.setVisibility(View.GONE);
            Glide.with(context)
                    .load(R.drawable.facescan_new)
                    .error(R.drawable.dummy_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.binding.ivGoalIcon1);

        } else if (dataList.get(position).getName().equalsIgnoreCase("Referred by")) {
            try {
                Glide.with(context)
                        .load(R.drawable.ic_like)
                        .error(R.drawable.dummy_image)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.binding.ivGoalIcon1);
                if (dataList.get(position).getMobileNumber() != null) {
                    holder.binding.ivHeart1.setVisibility(View.GONE);
                    holder.binding.disableLayout.setVisibility(View.VISIBLE);
                    holder.binding.disableLayoutTv.setText(CryptoHelper.decrypt(context, dataList.get(position).getMobileNumber()));
                    holder.binding.tvGoal1Layout.setVisibility(View.GONE);
                    holder.binding.rlMain1.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rewards_gray_button_bg));
                } else {
                    holder.binding.ivHeart1.setVisibility(View.GONE);
                }
            } catch (Exception e) {

            }
        } else {
            if (dataList.get(position).getName().equalsIgnoreCase("Family")) {
                holder.binding.ivGoalIcon1.setImageResource(R.drawable.ic_family_hub);
                holder.binding.ivHeart1.setVisibility(View.GONE);
            }else if(dataList.get(position).isKgiItem()){
                Glide.with(context)
                        .load(dataList.get(position).getIconImage())
                        .error(R.drawable.dummy_image)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.binding.ivGoalIcon1);
                //holder.binding.ivGoalIcon1.setBackgroundResource(dataList.get(position).getIconImage());
                holder.binding.ivHeart1.setVisibility(View.GONE);
            } else {
                holder.binding.ivHeart1.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(dataList.get(position).getImagePath())
                        .error(R.drawable.dummy_image)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.binding.ivGoalIcon1);
                if (dataList.get(position) != null && dataList.get(position).getIsPinned()) {
                    holder.binding.ivHeart1.setImageResource(R.drawable.ic_favorite);
                } else {
                    holder.binding.ivHeart1.setImageResource(R.drawable.ic_favorite_border);
                }
            }
        }

        /*if(dataList.get(position).getImagePath() == null && dataList.get(position).getImagePath().isEmpty()) {
            holder.binding.ivGoalIcon1.setVisibility(View.GONE);
        }else{
            holder.binding.ivGoalIcon1.setVisibility(View.VISIBLE);
        }*/


        /*if(bgIndex >= 2) {
            bgIndex = 0;
        } else {
            bgIndex++;
        }
*/
        holder.binding.rlMain1.setBackground(ContextCompat.getDrawable(context, res[position]));

        holder.binding.rlMain1.setOnClickListener(v -> {
            clickInterface.onItemClicked(position, dataList.get(position).getName());
        });
        holder.binding.ivHeart1.setOnClickListener(v -> {
            clickInterface.onPinIconClick(dataList.get(position));
        });

        return holder.view;
    }


    private static class ViewHolder {
        private View view;
        private DashboardBottomCardItemBinding binding;

        ViewHolder(DashboardBottomCardItemBinding binding) {
            this.view = binding.getRoot();
            this.binding = binding;
        }
    }
}
