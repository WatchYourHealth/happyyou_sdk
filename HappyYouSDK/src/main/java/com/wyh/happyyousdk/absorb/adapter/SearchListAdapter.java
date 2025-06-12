package com.wyh.happyyousdk.absorb.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.absorb.SearchKeyClick;

import java.util.ArrayList;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    Context context;
    ArrayList<String> searchKeyWordList;

    SearchKeyClick searchKeyClick;

    public SearchListAdapter(Context context, ArrayList<String> searchKeyWordList, SearchKeyClick searchKeyClick) {
        this.context = context;
        this.searchKeyWordList = searchKeyWordList;
        this.searchKeyClick = searchKeyClick;
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.search_key_tv.setText(searchKeyWordList.get(position));
        holder.search_text_parent.setOnClickListener(view -> searchKeyClick.onSearchClick(searchKeyWordList.get(position)));
    }

    @Override
    public int getItemCount() {
        return searchKeyWordList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView search_key_tv;
        LinearLayout search_text_parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            search_key_tv = itemView.findViewById(R.id.search_key_tv);
            search_text_parent = itemView.findViewById(R.id.search_text_parent);
        }
    }
}
