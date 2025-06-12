package com.wyh.happyyousdk.ehr.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.DiaryEventListBinding;
import com.wyh.happyyousdk.databinding.HealthLockerPhotosAdapterBinding;
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse;
import com.wyh.happyyousdk.diary.model.DiaryListDataResponse;
import com.wyh.happyyousdk.utils.CommonUtils;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HealthLockerPhotosAdapter extends RecyclerView.Adapter<HealthLockerPhotosAdapter.MyViewHolder> {

    Context context;
    List<DiaryListDataResponse> dataList;
    ClickListenerInterface clickListenerInterface;

    public HealthLockerPhotosAdapter(Context context, List<DiaryListDataResponse> dataList, ClickListenerInterface clickListenerInterface ) {
        this.context = context;
        this.dataList = dataList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HealthLockerPhotosAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.health_locker_photos_adapter, parent, false);
//        screenWidth = displayMetrics.widthPixels
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DiaryListDataResponse data = dataList.get(position);

        Log.d("HLP", position+" - "+data.getImagePath());

        /*Picasso.with(context)
                .load(data.getImagePath())
                .into(holder.binding.ivHLPhotos);*/

        Glide.with(context)
                .load(data.getImagePath())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivHLPhotos);

        holder.binding.ivHLPhotos.setOnClickListener(v->{
            clickListenerInterface.onItemClick(data);
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = new Date();
        String newTime = "";
        try {
            date = simpleDateFormat.parse(data.getCreatedOn());
            newTime = simpleDateFormat1.format(date);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }



        /*String t = data.getCreatedOn().split("T")[1].substring(0,5);
        String newTime = timeFormate(t);*/
        holder.binding.tvDate.setText(newTime);


    }

    public interface ClickListenerInterface {
        void onItemClick(DiaryListDataResponse data);
    }

    public String convertTo12HourFormat(String dateStr) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        DateFormat outputFormat = new SimpleDateFormat("hh:mm a");
        try {
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        HealthLockerPhotosAdapterBinding binding;

        public MyViewHolder(@NonNull HealthLockerPhotosAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String timeFormate(String time){

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");
        try {
            Date date = dateFormat.parse(time);

            String out = dateFormat2.format(date);
            //Log.e("Time", out);
            return out;
        } catch (ParseException e) {
        }
        return "00:00";
    }


}
