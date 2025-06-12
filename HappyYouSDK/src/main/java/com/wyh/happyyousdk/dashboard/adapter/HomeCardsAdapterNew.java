package com.wyh.happyyousdk.dashboard.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.crypto.CryptoHelper;
import com.wyh.happyyousdk.dashboard.model.DashboardBottomCardDataResponse;
import com.wyh.happyyousdk.databinding.HomeCardsAdapterBinding;
import com.wyh.happyyousdk.databinding.HomeCardsAdapterNewBinding;
import com.wyh.happyyousdk.model.response.dashboard.RemindersDetails;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeCardsAdapterNew extends RecyclerView.Adapter<HomeCardsAdapterNew.MyViewHolder> {

    Context context;
    ClickInterface clickInterface;
    MyViewHolder holder;
    List<DashboardBottomCardDataResponse.tilesData> dataList;

    int progressGoals;

    public HomeCardsAdapterNew(Context context, List<DashboardBottomCardDataResponse.tilesData> dataList , int progressGoals, ClickInterface clickInterface) {
        this.context = context;
        this.dataList = dataList;
        this.clickInterface = clickInterface;
        this.progressGoals = progressGoals;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeCardsAdapterNewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_cards_adapter_new, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        List<DashboardBottomCardDataResponse.tilesData> data = getSublist(dataList, position);
        BottomCardItemAdapter adapter = new BottomCardItemAdapter(context, data, progressGoals, new ClickInterface() {
            @Override
            public void onItemClicked(int index, String tag) {
                clickInterface.onItemClicked(index, tag);
            }

            @Override
            public void onPinIconClick(DashboardBottomCardDataResponse.tilesData data) {
                clickInterface.onPinIconClick(data);
            }
        });
        holder.binding.grid.setAdapter(adapter);
    }

    public interface ClickInterface {
        void onItemClicked(int index, String tag);
        void onPinIconClick(DashboardBottomCardDataResponse.tilesData data);
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(Double.parseDouble(String.valueOf(dataList.size())) / 3.0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        HomeCardsAdapterNewBinding binding;

        public MyViewHolder(HomeCardsAdapterNewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static List<DashboardBottomCardDataResponse.tilesData> getSublist(List<DashboardBottomCardDataResponse.tilesData> list, int index) {
        int start = index * 3;
        int end = Math.min(start + 3, list.size()); // Ensure end index does not exceed list size

        // Ensure start index is within bounds
        if (start < 0 || start >= list.size()) {
            return new ArrayList<>();
        }

        return list.subList(start, end);
    }
}
