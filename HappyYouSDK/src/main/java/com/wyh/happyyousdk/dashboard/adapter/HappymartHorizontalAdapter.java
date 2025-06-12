package com.wyh.happyyousdk.dashboard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.happyMarket.Model.HappyMartListModel;

import java.util.ArrayList;

public class HappymartHorizontalAdapter extends RecyclerView.Adapter<HappymartHorizontalAdapter.ViewHolder> {

    Context context;
    ArrayList<HappyMartListModel> lifeStyleList;

    LifeStyleClick clickListenerInterface;

    public HappymartHorizontalAdapter(Context context, ArrayList<HappyMartListModel> lifeStyleList, LifeStyleClick clickListenerInterface) {
        this.context = context;
        this.lifeStyleList = lifeStyleList;
        this.clickListenerInterface = clickListenerInterface;
    }


    @NonNull
    @Override
    public HappymartHorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_horizontal_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HappymartHorizontalAdapter.ViewHolder holder, int position) {
        ArrayList<HappyMartListModel> data = new ArrayList<>();

        if(position == 0){
            if(lifeStyleList.size() > 3){
                data.addAll(lifeStyleList.subList(0,3));
            }else{
                data.addAll(lifeStyleList);
            }
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

        SearchGridViewAdapter adapter = new SearchGridViewAdapter(data, context);

        holder.gridView.setAdapter(adapter);
        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                clickListenerInterface.onClick(data.get(p).getHappyMartTile());
            }
        });




    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(Double.parseDouble(String.valueOf(lifeStyleList.size())) / 3.0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        GridView gridView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gridView = itemView.findViewById(R.id.idGRV);

        }
    }

    public interface LifeStyleClick {
        void onClick(String lifeStyleTitle);
    }
}
