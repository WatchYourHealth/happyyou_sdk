package com.wyh.happyyousdk.notification.adapter;

import static com.wyh.happyyousdk.utils.Constants.MEDITATION;
import static com.wyh.happyyousdk.utils.Constants.SLEEP;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.WATER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.NotificationDrawerItemBinding;
import com.wyh.happyyousdk.model.response.notification.NotificationDataResponse;

import java.util.List;


public class NotificationDrawerAdapter extends RecyclerView.Adapter<NotificationDrawerAdapter.MyViewHolder> {

    Context context;
    List<NotificationDataResponse> list;
    NotificationCardItemAdapter.OnItemClickListener listener;

    public NotificationDrawerAdapter(Context context, List<NotificationDataResponse> list, NotificationCardItemAdapter.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }



    @NonNull
    @Override
    public NotificationDrawerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationDrawerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.notification_drawer_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationDrawerAdapter.MyViewHolder holder, int position) {
        NotificationDataResponse data = list.get(position);

        holder.binding.tvDate.setText(data.getNotifyDate().split(" ")[0]);
        NotificationCardItemAdapter adapter = new NotificationCardItemAdapter(context, data.getDrawer(),listener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        holder.binding.drawerListRecyclerView.setLayoutManager(linearLayoutManager);
        holder.binding.drawerListRecyclerView.setAdapter(adapter);




    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        NotificationDrawerItemBinding binding;

        public MyViewHolder(@NonNull NotificationDrawerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }




}
