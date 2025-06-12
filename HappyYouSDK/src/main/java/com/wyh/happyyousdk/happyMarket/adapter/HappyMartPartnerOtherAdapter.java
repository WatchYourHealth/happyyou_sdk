package com.wyh.happyyousdk.happyMarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.AdapterOtherItemBinding;
import com.wyh.happyyousdk.model.response.CategoryData;
import com.wyh.happyyousdk.model.response.GetKliActivitiesResponse;
import com.wyh.happyyousdk.model.response.HappyMartSubCategoryChildResponse;

import java.util.ArrayList;
import java.util.List;


public class HappyMartPartnerOtherAdapter extends RecyclerView.Adapter<HappyMartPartnerOtherAdapter.MyViewHolder> {

    Context context;
    List<GetKliActivitiesResponse.Data> list;

    ArrayList<CategoryData> categoryData;
    int[] res;
    int bgCount = -1;
   // private final HappyMartPartnerOtherAdapter.OnItemClickListener listener;

    HappyMartPartnerOtherAdapter.onOthersItemClick clickEvents;


    public HappyMartPartnerOtherAdapter(Context context, List<GetKliActivitiesResponse.Data> list) {
        this.context = context;
        this.list = list;
    }

    public HappyMartPartnerOtherAdapter(Context context, ArrayList<CategoryData> categoryData, onOthersItemClick clickEvents) {
        this.context = context;
        this.categoryData = categoryData;
        this.clickEvents = clickEvents;
    }

    @NonNull
    @Override
    public HappyMartPartnerOtherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterOtherItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_other_item, parent, false);
        res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg,
                R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg};

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (bgCount == res.length - 1) {
            bgCount = 0;
        } else
            bgCount++;

        holder.binding.rlQuiksilver.setBackground(ContextCompat.getDrawable(context, res[bgCount]));

        if (position == 0){
            holder.binding.ivIcon.setBackgroundResource(R.drawable.ic_stamp_white);
        }
        else{
            Glide.with(context)
                    .load(categoryData.get(position).getHappyMartSubCategoryResponse().getVendorLogo())
                    .error(R.drawable.dummy_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.binding.ivIcon);
        }

        holder.binding.tvTitle.setText(categoryData.get(position).getHappyMartSubCategoryResponse().getVendorName());

        holder.binding.rlQuiksilver.setOnClickListener(view -> {
            clickEvents.onClick(categoryData.get(position).getHappyMartSubCategoryResponse().getHappyMartSubCategoryChildResponse());
        });

    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterOtherItemBinding binding;

        public MyViewHolder(@NonNull AdapterOtherItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick(GetKliActivitiesResponse.Data data, Boolean isCouponCheck, Integer checkID, String activityName);
    }

    public interface onOthersItemClick {
        void onClick(ArrayList<HappyMartSubCategoryChildResponse> categoryData);
    }
}
