package com.wyh.happyyousdk.qc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.QcOrderListAdapterBinding;
import com.wyh.happyyousdk.databinding.QcProductsAdapterBinding;
import com.wyh.happyyousdk.model.response.qc.QCOrderCard;
import com.wyh.happyyousdk.model.response.qc.QCOrderResponse;
import com.wyh.happyyousdk.model.response.qc.allProducts.Products;

import java.util.List;

public class QCOrderListAdapter extends RecyclerView.Adapter<QCOrderListAdapter.MyViewHolder> {

    Context context;
    List<QCOrderCard> productsList;
    ClickListenerInterface clickListenerInterface;

    int[] res;
    int bgCount = 0;

    public QCOrderListAdapter(Context context, List<QCOrderCard> productsList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.productsList = productsList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QcOrderListAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.qc_order_list_adapter, parent, false);
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

        QCOrderCard product = productsList.get(position);
        holder.binding.tvTitle.setText(product.getProductName());
        if (product.getSku() == null)
            holder.binding.tvTokens1.setText(product.getAmount());
        else
            holder.binding.tvTokens1.setText("Rs. " + product.getAmount());
        /*Glide.with(context)
                .load(product.getActivationUrl())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivImage1);*/


        holder.binding.rlMainLayout.setOnClickListener(view -> {
            if (holder.binding.rlMainLayout.getBackground() == ContextCompat.getDrawable(context, R.drawable.ic_light_pink_button_bg))
                clickListenerInterface.onItemClickActivities(product, R.drawable.pink_circle);
            else if (holder.binding.rlMainLayout.getBackground() == ContextCompat.getDrawable(context, R.drawable.ic_light_blue_button_bg))
                clickListenerInterface.onItemClickActivities(product, R.drawable.blue_circle);
            else
                clickListenerInterface.onItemClickActivities(product, R.drawable.orange_circle);
        });
    }

    public interface ClickListenerInterface {
        void onItemClickActivities(QCOrderCard product, int bgDrawable);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        QcOrderListAdapterBinding binding;

        public MyViewHolder(@NonNull QcOrderListAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /*public void updateList(List<Products> list){
        productsList = list;
        notifyDataSetChanged();
    }*/
}
