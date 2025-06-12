package com.wyh.happyyousdk.actometer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;

public class UserLabelAdapter extends RecyclerView.Adapter<UserLabelAdapter.MyViewHolder> {

    Context context;
    String[] userNames;
    int[] colors;

    public UserLabelAdapter(Context context, String[] userNames) {
        this.context = context;
        this.userNames = userNames;
    }

    @NonNull
    @Override
    public UserLabelAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_label_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserLabelAdapter.MyViewHolder holder, int position) {
        holder.username_tv.setText(userNames[position]);
        holder.color_tv.setBackgroundColor(colors[position]);

    }

    @Override
    public int getItemCount() {
        return userNames.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView color_tv,username_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            color_tv = itemView.findViewById(R.id.color_tv);
            username_tv = itemView.findViewById(R.id.username_tv);
        }
    }
}
