package com.wyh.happyyousdk.notification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.NotificationCardItemBinding;
import com.wyh.happyyousdk.model.response.notification.NotificationDrawerResponse;

import java.util.List;


public class NotificationCardItemAdapter extends RecyclerView.Adapter<NotificationCardItemAdapter.MyViewHolder> {

    Context context;
    List<NotificationDrawerResponse> list;
    NotificationCardItemAdapter.OnItemClickListener listener;

    public NotificationCardItemAdapter(Context context, List<NotificationDrawerResponse> list,NotificationCardItemAdapter.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener =listener;
    }

    @NonNull
    @Override
    public NotificationCardItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationCardItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.notification_card_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationCardItemAdapter.MyViewHolder holder, int position) {
        NotificationDrawerResponse data = list.get(position);

        holder.binding.title.setText(data.getTitle());
        holder.binding.des.setText(data.getNotificationMessgae());
        holder.binding.tvDate.setText(data.getNotificationTime());
        holder.binding.notificationImage.setImageResource(getImage(data.getTitle()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data);
            }
        });

    }

    public int getImage(String name){
        String s = name.toLowerCase();
        if (s.contains("meditation")) {
            return R.drawable.ic_notification_meditation_1;
        } else if (s.contains("steps")) {
            return R.drawable.ic_steps_1;
        } else if (s.contains("water")) {
            return R.drawable.ic_notification_water;
        } else if (s.contains("invite") || s.contains("tribe") || s.contains("medicines") || s.contains("others")) {
            return R.drawable.ic_notification_tribe;
        } else if (s.contains("sleep")) {
            return R.drawable.ic_notification_sleep;
        } else if (s.contains("riddle")) {
            return R.drawable.ic_bulb_notification;
        } else {
            return R.drawable.ic_notification_cal;
        }
        /*else if (s.contains("tribe")) {
            return R.drawable.ic_notification_tribe;
        }*/
//        return R.drawable.ic_notification_cal;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        NotificationCardItemBinding binding;

        public MyViewHolder(@NonNull NotificationCardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick( NotificationDrawerResponse data);
    }
}
