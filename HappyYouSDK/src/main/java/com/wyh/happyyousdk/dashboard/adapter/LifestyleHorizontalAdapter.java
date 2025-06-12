package com.wyh.happyyousdk.dashboard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.happyMarket.Model.HappyMartListModel;

import java.util.ArrayList;

public class LifestyleHorizontalAdapter extends RecyclerView.Adapter<LifestyleHorizontalAdapter.ViewHolder> {

    Context context;
    ArrayList<HappyMartListModel> lifeStyleList;

    LifeStyleClick clickListenerInterface;

    public LifestyleHorizontalAdapter(Context context, ArrayList<HappyMartListModel> lifeStyleList, LifeStyleClick clickListenerInterface) {
        this.context = context;
        this.lifeStyleList = lifeStyleList;
        this.clickListenerInterface = clickListenerInterface;
    }

    int[] res, resCircle;
    int bgCount = -1;


    @NonNull
    @Override
    public LifestyleHorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg,
                R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg};
        */
        View view = LayoutInflater.from(context).inflate(R.layout.search_horizontal_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LifestyleHorizontalAdapter.ViewHolder holder, int position) {
       /* if (bgCount == res.length - 1) {
            bgCount = 0;
        } else {
            bgCount++;
        }

        */
        Log.d("search list2", ""+lifeStyleList.size());
        ArrayList<HappyMartListModel> data = new ArrayList<>();

        if(position == 0){
            if(lifeStyleList.size() > 3){
                data.addAll(lifeStyleList.subList(0,3));
            }else{
                data.addAll(lifeStyleList);
            }
            Log.d("search list3", ""+data.size());
        }else if(position == 1){
            if(lifeStyleList.size() > 6){
                data.addAll(lifeStyleList.subList(3,6));
            }else{
                data.addAll(lifeStyleList.subList(3, lifeStyleList.size()));
            }
        }else if(position == 2){
            if(lifeStyleList.size() >= 9){
                data.addAll(lifeStyleList.subList(6,9));
            }else{
                data.addAll(lifeStyleList.subList(6, lifeStyleList.size()));
            }
        }

        //Log.d("size", data.size()+" "+position+" "+lifeStyleList.size());
        SearchGridViewAdapter adapter = new SearchGridViewAdapter(data, context);

        holder.gridView.setAdapter(adapter);
        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                clickListenerInterface.onClick(data.get(p).getHappyMartTile());
            }
        });

        /*holder.lifeStyleListImg.setImageResource(lifeStyleList.get(position).getHappyMartImg());
        holder.lifeStyleListTv.setText(lifeStyleList.get(position).getHappyMartTile());
        holder.lifeStyleListParentLayout.setBackground(ContextCompat.getDrawable(context, res[bgCount]));

        holder.lifeStyleListParentLayout.setOnClickListener(view -> {
            clickListenerInterface.onClick(lifeStyleList.get(position).getHappyMartTile());
        });*/



    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(Double.parseDouble(String.valueOf(lifeStyleList.size())) / 3.0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        /*ImageView lifeStyleListImg;
        TextView lifeStyleListTv;
        RelativeLayout lifeStyleListParentLayout;*/

        GridView gridView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gridView = itemView.findViewById(R.id.idGRV);

            /*lifeStyleListImg = itemView.findViewById(R.id.life_style_list_img);
            lifeStyleListTv = itemView.findViewById(R.id.life_style_list_tv);
            lifeStyleListParentLayout = itemView.findViewById(R.id.life_style_list_parent_layout);*/
        }
    }

    public interface LifeStyleClick {
        void onClick(String lifeStyleTitle);
    }
}
