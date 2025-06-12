package com.wyh.happyyousdk.happyMarket.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.happyMarket.Model.HappyMartListModel;
import com.wyh.happyyousdk.model.response.HappyMartTilesData;

import java.util.ArrayList;

public class HappyMartListAdapter extends RecyclerView.Adapter<HappyMartListAdapter.MyViewHolder> {

    Context context;
    ArrayList<HappyMartTilesData> happyMartListModels;
    HappyMartClick clickListenerInterface;
    boolean isAllItemsLoaded = false;


    int[] res, resCircle;
    int bgCount = -1;

    public HappyMartListAdapter(Context context, ArrayList<HappyMartTilesData> happyMartListModels, HappyMartClick clickListenerInterface) {
        this.context = context;
        this.happyMartListModels = happyMartListModels;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public HappyMartListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg
        };


        View view = LayoutInflater.from(context).inflate(R.layout.happy_mart_list_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HappyMartListAdapter.MyViewHolder holder, int position) {
        if (!isAllItemsLoaded) {
            if (bgCount == res.length - 1) {
                bgCount = 0;
            } else {
                bgCount++;
            }
            holder.happy_mart_parent_layout.setBackground(ContextCompat.getDrawable(context, res[bgCount]));
        }
        holder.happy_mart_parent_layout.setBackground(ContextCompat.getDrawable(context, res[bgCount]));
        holder.happy_mart_list_tv.setText(happyMartListModels.get(position).getCategoryName());
        Glide.with(context)
                .load(happyMartListModels.get(position).getCategoryImagePath())
                .into(holder.happy_mart_list_img);
        //holder.happy_mart_list_img.setImageResource(R.drawable.ic_diagnostic);
        holder.happy_mart_parent_layout.setOnClickListener(view -> {
            clickListenerInterface.onClick(happyMartListModels.get(position));
        });

        if (happyMartListModels.size() - 1 == position)
            isAllItemsLoaded = true;
        Log.d("AuthToken", "Size - "+happyMartListModels.size());
    }

    @Override
    public int getItemCount() {
        return happyMartListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView happy_mart_list_img;
        TextView happy_mart_list_tv;
        RelativeLayout happy_mart_parent_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            happy_mart_list_img = itemView.findViewById(R.id.happy_mart_list_img);
            happy_mart_list_tv = itemView.findViewById(R.id.happy_mart_list_tv);
            happy_mart_parent_layout = itemView.findViewById(R.id.happy_mart_parent_layout);
        }
    }

    public interface HappyMartClick {
        void onClick(HappyMartTilesData tileData);
    }

  /*  public void setData(ArrayList<HappyMartListModel> data){
        happyMartListModels.clear();
        happyMartListModels = data;
        notifyDataSetChanged();

    }*/
}
