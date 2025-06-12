package com.wyh.happyyousdk.ehr.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.HealthLockerPhotosAdapterBinding;
import com.wyh.happyyousdk.databinding.HealthLockerPhotosAdapterListBinding;
import com.wyh.happyyousdk.diary.DiaryActivity;
import com.wyh.happyyousdk.diary.model.DiaryListDataResponse;
import com.wyh.happyyousdk.ehr.ImageReaderActivity;
import com.wyh.happyyousdk.ehr.model.ErhPhotosModel;
import com.wyh.happyyousdk.utils.CommonUtils;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HealthLockerPhotosListAdapter extends RecyclerView.Adapter<HealthLockerPhotosListAdapter.MyViewHolder> {

    Context context;
    ErhPhotosModel dataList;

    public HealthLockerPhotosListAdapter(Context context, ErhPhotosModel dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HealthLockerPhotosAdapterListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.health_locker_photos_adapter_list, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String newDate = CommonUtils.formatDateFromString("yyyy-MM-dd", "dd MMMM yyyy", dataList.getKeys().get(position));
        holder.binding.tvDate.setText(newDate);
        Collections.sort(dataList.getValues().get(position), new sortItems());
        HealthLockerPhotosAdapter healthLockerPhotosAdapter = new HealthLockerPhotosAdapter(context, (List<DiaryListDataResponse>) dataList.getValues().get(position), new HealthLockerPhotosAdapter.ClickListenerInterface() {
            @Override
            public void onItemClick(DiaryListDataResponse data) {
                Intent intent = new Intent(context, ImageReaderActivity.class);
                intent.putExtra("image", data.getImagePath());
                context.startActivity(intent);
            }
        });
        holder.binding.rvHLPhotosItem.setAdapter(healthLockerPhotosAdapter);

    }

    public interface ClickListenerInterface {
        void onItemClick(DiaryListDataResponse data);
    }

    @Override
    public int getItemCount() {
        return dataList.getKeys().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        HealthLockerPhotosAdapterListBinding binding;

        public MyViewHolder(@NonNull HealthLockerPhotosAdapterListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class sortItems implements Comparator<DiaryListDataResponse> {

        // Method of this class
        @Override
        public int compare(DiaryListDataResponse a, DiaryListDataResponse b) {

            // Returning the value after comparing the objects
            // this will sort the data in Ascending order

            return b.getCreatedOn().compareTo(a.getCreatedOn());
        }
    }


}
