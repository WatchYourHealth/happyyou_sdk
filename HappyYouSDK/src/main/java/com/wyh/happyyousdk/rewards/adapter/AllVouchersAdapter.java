package com.wyh.happyyousdk.rewards.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.AllVouchersAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.EandBAvailableVoucher;

import java.util.List;

public class AllVouchersAdapter extends RecyclerView.Adapter<AllVouchersAdapter.MyViewHolder> {

    Context context;
    List<EandBAvailableVoucher> grabOpportunitiesList;
    ClickListenerInterface clickListenerInterface;
    int[] res;
    int bgCount = 0;

    public AllVouchersAdapter(Context context, List<EandBAvailableVoucher> grabOpportunitiesList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.grabOpportunitiesList = grabOpportunitiesList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllVouchersAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.all_vouchers_adapter, parent, false);
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
        holder.binding.rlMainLayout.setBackground(ContextCompat.getDrawable(context, res[bgCount]));
        if (grabOpportunitiesList.get(position).getVoucherTitle().contains("Congratulations")) {
            holder.binding.tvArticleHead1.setText(grabOpportunitiesList.get(position).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
            holder.binding.tvArticle1.setText(grabOpportunitiesList.get(position).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
        } else
            holder.binding.tvArticle1.setText(grabOpportunitiesList.get(position).getVoucherTitle());
        holder.binding.tvTokens1.setText("Buy for " + grabOpportunitiesList.get(position).getTokens());
        Glide.with(context)
                .load(grabOpportunitiesList.get(position).getVendorLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivVendorLogo1);

        holder.binding.rlMainLayout.setOnClickListener(view -> {
            clickListenerInterface.onItemClickNewVouchers(grabOpportunitiesList.get(position));
        });
    }

    public interface ClickListenerInterface {
        void onItemClickNewVouchers(EandBAvailableVoucher eandBAvailableVoucher);
    }

    @Override
    public int getItemCount() {
        return grabOpportunitiesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AllVouchersAdapterBinding binding;

        public MyViewHolder(@NonNull AllVouchersAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
