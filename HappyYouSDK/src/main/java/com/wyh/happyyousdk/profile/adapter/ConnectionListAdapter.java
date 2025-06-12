package com.wyh.happyyousdk.profile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ConnectionAdapterLayoutBinding;
import com.wyh.happyyousdk.profile.ConnectionListModel;

import java.util.ArrayList;

public class ConnectionListAdapter extends RecyclerView.Adapter<ConnectionListAdapter.ViewHolder> {

    Context context;
    ArrayList<ConnectionListModel> connectionList;
    ConnectionAdapterLayoutBinding binding;

    public ConnectionListAdapter(Context context, ArrayList<ConnectionListModel> connectionList) {
        this.context = context;
        this.connectionList = connectionList;
    }

    @NonNull
    @Override
    public ConnectionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.connection_adapter_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.connectionTv.setText(connectionList.get(position).getName());
        holder.srNo.setText(String.valueOf(position + 1));

    }

    @Override
    public int getItemCount() {
        return connectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView connectionTv,srNo,noDataFound;
        ImageView tribeImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            connectionTv = itemView.findViewById(R.id.connection_name_tv);
            srNo = itemView.findViewById(R.id.connection_sr_no);
            tribeImg = itemView.findViewById(R.id.moveToTribe);
        }
    }
}
