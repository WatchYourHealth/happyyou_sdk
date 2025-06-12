package com.wyh.happyyousdk.happyMarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.MyPurchasesMoreAdapterBinding;

import java.util.List;
import java.util.Objects;

public class MoreMyPurchasesAdapter extends RecyclerView.Adapter<MoreMyPurchasesAdapter.MyViewHolder> {

    Context context;
    List<String> list;
    ClickListenerInterface clickListenerInterface;

    int[] res;
    int bgCount = 0;

    public MoreMyPurchasesAdapter(Context context, List<String> list, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.list = list;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyPurchasesMoreAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.my_purchases_more_adapter, parent, false);
        res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg,
                R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg};
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (bgCount == res.length - 1) {
            bgCount = 0;
        } else
            bgCount++;

        String data = list.get(position);
        String name = data.replaceAll("\\s", "").toLowerCase();

        holder.binding.tvArticle1.setText(getName(name));

        holder.binding.rlMainLayout.setBackground(ContextCompat.getDrawable(context, res[bgCount]));

        holder.binding.ivIcon1.setImageResource(getIcon(name));
        holder.binding.ivImage1.setImageResource(getImage(name));
        holder.binding.rlMainLayout.setOnClickListener(view -> {
            clickListenerInterface.onItemClick(data);
        });

        if(getName(name).equals("Others")){
            holder.binding.lImage.setVisibility(View.GONE);
        }else{
            holder.binding.lImage.setVisibility(View.VISIBLE);
        }

    }

    public interface ClickListenerInterface {
        void onItemClick(String vendor);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MyPurchasesMoreAdapterBinding binding;

        public MyViewHolder(@NonNull MyPurchasesMoreAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private int getIcon(String vendor){
        if(Objects.equals(vendor, "medpayopd")){
            return R.drawable.ic_physical_consultation;
        }else if(Objects.equals(vendor, "pharmeasy") || Objects.equals(vendor, "medpaypharmacy")){
            return R.drawable.ic_pharmacy;
        }else if(Objects.equals(vendor, "healthassure") || Objects.equals(vendor, "connectandheal")  || Objects.equals(vendor, "alyvehealth")){
            return R.drawable.ic_diagnostic;
        }else if(Objects.equals(vendor, "coachpro")){
            return R.drawable.ic_fitness_and_nutrition;
        }else if(Objects.equals(vendor, "mentalWellbeing")){
            return R.drawable.ic_mental_welbeing;
        }else if(Objects.equals(vendor, "hobbytribe")){
            return R.drawable.ic_hobby;
        }else if(Objects.equals(vendor, "actofit")){
            return R.drawable.ic_device_electronics;
        }else if(Objects.equals(vendor, "vouchers")){
            return R.drawable.ic_stamp;
        }else if(Objects.equals(vendor, "Tele-Consultation") || Objects.equals(vendor, "getvisit")){
            return R.drawable.ic_tele_consultation;
        }
        return R.drawable.ic_device_electronics;
    }

    private String getName(String vendor){
        if(Objects.equals(vendor, "medpayopd")){
            return "Physical Consultation";
        }else if(Objects.equals(vendor, "pharmeasy")){
            return "Pharmacy";
        }else if(Objects.equals(vendor, "healthassure") || Objects.equals(vendor, "connectandheal")){
            return "Diagnostics\nand LAB Tests";
        }else if(Objects.equals(vendor, "coachpro")){
            return "Fitness &\nNutrition";
        }else if(Objects.equals(vendor, "mentalWellbeing")){
            return "Mental\nWellbeing";
        }else if(Objects.equals(vendor, "hobbytribe")){
            return "Hobby";
        }else if(Objects.equals(vendor, "actofit")){
            return "Devices and\nMerchandise";
        }else if(Objects.equals(vendor, "vouchers")){
            return "Others";
        }else if(Objects.equals(vendor, "medpaypharmacy")){
            return "Medpay\nPharmacy";
        }else if(Objects.equals(vendor,"getvisit")){
            return "Tele-Consultation";
        }else if (Objects.equals(vendor, "alyvehealth")) {
            return "OPD";
        }
        return "Others";
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
        }else if(Objects.equals(vendor, "connectandheal")){
            return R.drawable.connected_logo;
        }else if(Objects.equals(vendor, "mentalWellbeing")){
            return R.drawable.mrf_logo_light;
        }else if(Objects.equals(vendor, "hobbytribe")){
            return R.drawable.hobby_tribe_logo;
        }else if(Objects.equals(vendor, "actofit")){
            return R.drawable.actofit_logo;
        }else if(Objects.equals(vendor, "vouchers")){
            return R.drawable.doconline_logo;
        }else if(Objects.equals(vendor,"getvisit")){
            return R.drawable.vistorlogo;
        } else if (Objects.equals(vendor, "alyvehealth")) {
            return R.drawable.alyve_health_logo;
        }
        return R.drawable.ic_pharmeasy_logo;
    }
}
