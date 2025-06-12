package com.wyh.happyyousdk.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.corporateAccount.StateModel;
import com.wyh.happyyousdk.model.response.GetCityListResponseModel;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapterCity extends RecyclerView.Adapter<SearchListAdapterCity.ViewHolder> {
    private List<GetCityListResponseModel.CityDataModel> originalList;
    private List<GetCityListResponseModel.CityDataModel> filteredList;
    private OnItemClickListener listener;
    int position;

    public interface OnItemClickListener {
        void onItemClick(String item,int position);
    }

    public SearchListAdapterCity(List<GetCityListResponseModel.CityDataModel> list, OnItemClickListener listener) {
        this.originalList = list;
        this.filteredList = new ArrayList<>(list);
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(android.R.id.text1);
        }
    }

    @NonNull
    @Override
    public SearchListAdapterCity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapterCity.ViewHolder holder, int position) {
        final String item = filteredList.get(position).getCityName();
        holder.textView.setText(item);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item,position));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query == null || query.trim().isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (GetCityListResponseModel.CityDataModel item : originalList) {
                if (item.getCityName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}