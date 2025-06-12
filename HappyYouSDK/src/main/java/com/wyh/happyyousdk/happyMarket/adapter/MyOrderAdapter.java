package com.wyh.happyyousdk.happyMarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.MyOrderItemBinding;
import com.wyh.happyyousdk.model.MyOrderResponse;
import com.wyh.happyyousdk.utils.CommonUtils;

import java.util.List;
import java.util.Objects;


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {

    Context context;
    List<MyOrderResponse.Data> myOrderDataList;
    private final MyOrderAdapter.OnItemClickListener listener;

    public MyOrderAdapter(Context context, List<MyOrderResponse.Data> myOrderDataList, MyOrderAdapter.OnItemClickListener listener) {
        this.context = context;
        this.myOrderDataList = myOrderDataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyOrderItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.my_order_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.MyViewHolder holder, int position) {
        MyOrderResponse.Data data = myOrderDataList.get(position);

        String date = data.getDate().split("T")[0];
        holder.binding.tvDate.setText(CommonUtils.formatDateFromString("yyyy-mm-dd", "dd/mm/yyyy", date));

        String vendor = data.getVendor().replaceAll("\\s", "").toLowerCase();
        holder.binding.tvDocumentType.setText(data.getVendor());
        holder.binding.tvStatus.setText(data.getStatus());
        //holder.binding.ivImage.setImageResource(getImage(vendor));
        Glide.with(context).load(data.getVendorlogo()).into(holder.binding.ivImage);
        holder.binding.llMain.setOnClickListener(c->{
            listener.onClick(data);
        });

    }

    @Override
    public int getItemCount() {
        return myOrderDataList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MyOrderItemBinding binding;

        public MyViewHolder(@NonNull MyOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick(MyOrderResponse.Data data);
    }

    public void updateList(List<MyOrderResponse.Data> list){
        myOrderDataList = list;
        notifyDataSetChanged();
    }


    private int getImage(String vendor){
        if(Objects.equals(vendor, "medpayopd") || Objects.equals(vendor, "medpaypharmacy")){
            return R.drawable.ic_medpay_logo;
        }else if(Objects.equals(vendor, "pharmeasy")){
            return R.drawable.ic_pharmeasy_logo;
        }else if(Objects.equals(vendor, "healthassure")){
            return R.drawable.ha_logo;
        }else if(Objects.equals(vendor, "coachpro")){
            return R.drawable.coach_pro_logo;
        }else if(Objects.equals(vendor, "connect&heal")){
            return R.drawable.connected_logo;
        }else if(Objects.equals(vendor, "mindresearch")){
            return R.drawable.mrf_logo_light;
        }else if(Objects.equals(vendor, "hobbytribe")){
            return R.drawable.hobby_tribe_logo;
        }else if(Objects.equals(vendor, "actofit")){
            return R.drawable.actofit_logo;
        }else if(Objects.equals(vendor, "vouchers")){
            return R.drawable.doconline_logo;
        }else if(Objects.equals(vendor, "rewardsvendor")){
            return R.drawable.doconline_logo;
        }else if(Objects.equals(vendor, "vitalform.io")){
            return R.drawable.doconline_logo;
        } else if(Objects.equals(vendor,"getvisit")){
            return R.drawable.vistorlogo;
        }else if(Objects.equals(vendor,"techcustomercapital")){
            return R.drawable.shopstacc_logo_bg;
        }

        return R.drawable.ic_pharmeasy_logo;
    }
}
