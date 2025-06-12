package com.wyh.happyyousdk.unwind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.JokeItemAdapterBinding;
import com.wyh.happyyousdk.databinding.ThoughtItemAdapterBinding;
import com.wyh.happyyousdk.databinding.ThoughtPopUpBinding;

import java.util.List;

public class ThoughtAdapter extends RecyclerView.Adapter<ThoughtAdapter.MyViewHolder> {

    Context context;
    List<String> list;

    public ThoughtAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ThoughtAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ThoughtItemAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.thought_item_adapter, parent, false);
        return new ThoughtAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ThoughtAdapter.MyViewHolder holder, int position) {
        String data = list.get(position);
        Glide.with(context)
                .load(data)
                .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivUnwind);
//            rl_main_layout.setBackgroundColor(getColor(R.color.joke_blue));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ThoughtItemAdapterBinding binding;

        public MyViewHolder(@NonNull ThoughtItemAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

   /* public interface OnItemClickListener {
        void onClick(int position, String name);
    }*/
}
