package com.wyh.happyyousdk.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.happyMarket.Model.HappyMartListModel;
import com.wyh.happyyousdk.happyMarket.adapter.HappyMartListAdapter;

import java.util.ArrayList;

public class LifestyleAdapter extends RecyclerView.Adapter<LifestyleAdapter.ViewHolder> {

    Context context;
    ArrayList<HappyMartListModel> lifeStyleList;

    LifeStyleClick clickListenerInterface;

    public LifestyleAdapter(Context context, ArrayList<HappyMartListModel> lifeStyleList, LifeStyleClick clickListenerInterface) {
        this.context = context;
        this.lifeStyleList = lifeStyleList;
        this.clickListenerInterface = clickListenerInterface;
    }

    int[] res, resCircle;
    int bgCount = -1;


    @NonNull
    @Override
    public LifestyleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg,
                R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg};

        View view = LayoutInflater.from(context).inflate(R.layout.life_style_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LifestyleAdapter.ViewHolder holder, int position) {
        if (bgCount == res.length - 1) {
            bgCount = 0;
        } else {
            bgCount++;
        }
        holder.lifeStyleListImg.setImageResource(lifeStyleList.get(position).getHappyMartImg());
        holder.lifeStyleListTv.setText(lifeStyleList.get(position).getHappyMartTile());
        holder.lifeStyleListParentLayout.setBackground(ContextCompat.getDrawable(context, res[bgCount]));

        holder.lifeStyleListParentLayout.setOnClickListener(view -> {
            clickListenerInterface.onClick(lifeStyleList.get(position).getHappyMartTile());
        });



    }

    @Override
    public int getItemCount() {
        return lifeStyleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView lifeStyleListImg;
        TextView lifeStyleListTv;
        RelativeLayout lifeStyleListParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lifeStyleListImg = itemView.findViewById(R.id.life_style_list_img);
            lifeStyleListTv = itemView.findViewById(R.id.life_style_list_tv);
            lifeStyleListParentLayout = itemView.findViewById(R.id.life_style_list_parent_layout);
        }
    }

    public interface LifeStyleClick {
        void onClick(String lifeStyleTitle);
    }
}
