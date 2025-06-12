package com.wyh.happyyousdk.trends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.MyWinnigItemBinding;
import com.wyh.happyyousdk.model.response.ProfileDetailsResponse;

import java.util.List;

public class TrendsMyWinningAdapter extends RecyclerView.Adapter<TrendsMyWinningAdapter.MyViewHolder> {

    Context context;
    List<ProfileDetailsResponse.Data.LatestWinning> myWinningList;
    ClickListenerInterface clickListenerInterface;

    public TrendsMyWinningAdapter(Context context, List<ProfileDetailsResponse.Data.LatestWinning> myWinningList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.myWinningList = myWinningList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyWinnigItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.my_winnig_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                for (int i = 0; i < 3; i++) {
                    if (i == 0) {
                        holder.binding.tvWinningName1.setText(myWinningList.get(i).getName());
                        holder.binding.tvPointsCount.setText(myWinningList.get(i).getPoint().split("[.]")[0] + " points");
                        Glide.with(context)
                                .load(myWinningList.get(i).getEventImage())
                                .error(R.drawable.dummy_image)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(holder.binding.ivImage1);
                                    /*holder.binding.rlWinnings1.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, CommunityActivity.class);
                                        intent.putExtra("data", new Gson().toJson(myWinningList.get(i)));
                                        context.startActivity(intent);
                                    });*/
                    }
                    if (i == 1) {
                       if(myWinningList.size() > 1){
                           holder.binding.rlWinning2.setVisibility(View.VISIBLE);
                           holder.binding.tvWinningName2.setText(myWinningList.get(i).getName());
                           holder.binding.tvPointsCount2.setText(myWinningList.get(i).getPoint().split("[.]")[0] + " points");
                           Glide.with(context)
                                   .load(myWinningList.get(i).getEventImage())
                                   .error(R.drawable.dummy_image)
                                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                                   .skipMemoryCache(true)
                                   .into(holder.binding.ivImage2);
                                    /*holder.binding.rlWinning2.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, CommunityActivity.class);
                                        intent.putExtra("data", new Gson().toJson(myWinningList.get(i)));
                                        context.startActivity(intent);
                                    });*/
                       }else{
                           holder.binding.rlWinning2.setVisibility(View.GONE);
                           holder.binding.rlWinnings3.setVisibility(View.GONE);
                       }
                    }
                    if (i == 2) {
                        if(myWinningList.size() > 2){
                            holder.binding.tvWinningName3.setText(myWinningList.get(i).getName());
                            holder.binding.tvPointsCount3.setText(myWinningList.get(i).getPoint().split("[.]")[0] + " points");
                            Glide.with(context)
                                    .load(myWinningList.get(i).getEventImage())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);
                                    /*holder.binding.rlWinning2.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, CommunityActivity.class);
                                        intent.putExtra("data", new Gson().toJson(myWinningList.get(i)));
                                        context.startActivity(intent);
                                    });*/
                        }else{
                            holder.binding.rlWinnings3.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 1:
                for (int i = 3; i < 6; i++) {
                    if (i == 3) {
                        holder.binding.tvWinningName1.setText(myWinningList.get(i).getName());
                        holder.binding.tvPointsCount.setText(myWinningList.get(i).getPoint().split("[.]")[0] + " points");
                        Glide.with(context)
                                .load(myWinningList.get(i).getEventImage())
                                .error(R.drawable.dummy_image)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(holder.binding.ivImage1);
                                    /*holder.binding.rlWinnings1.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, CommunityActivity.class);
                                        intent.putExtra("data", new Gson().toJson(myWinningList.get(i)));
                                        context.startActivity(intent);
                                    });*/
                    }
                    if (i == 4) {
                        if(myWinningList.size() > 4){
                            holder.binding.tvWinningName2.setText(myWinningList.get(i).getName());
                            holder.binding.tvPointsCount2.setText(myWinningList.get(i).getPoint().split("[.]")[0] + " points");
                            Glide.with(context)
                                    .load(myWinningList.get(i).getEventImage())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);
                                    /*holder.binding.rlWinning2.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, CommunityActivity.class);
                                        intent.putExtra("data", new Gson().toJson(myWinningList.get(i)));
                                        context.startActivity(intent);
                                    });*/
                        }else{
                            holder.binding.rlWinning2.setVisibility(View.GONE);
                            holder.binding.rlWinnings3.setVisibility(View.GONE);
                        }
                    }
                    if (i == 5) {
                        if(myWinningList.size() > 5){
                            holder.binding.tvWinningName3.setText(myWinningList.get(i).getName());
                            holder.binding.tvPointsCount3.setText(myWinningList.get(i).getPoint().split("[.]")[0] + " points");
                            Glide.with(context)
                                    .load(myWinningList.get(i).getEventImage())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);
                                    /*holder.binding.rlWinning2.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, CommunityActivity.class);
                                        intent.putExtra("data", new Gson().toJson(myWinningList.get(i)));
                                        context.startActivity(intent);
                                    });*/
                        }else{
                            holder.binding.rlWinnings3.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 2:
                for (int i = 6; i < 9; i++) {
                    if (i == 6) {
                        holder.binding.tvWinningName1.setText(myWinningList.get(i).getName());
                        holder.binding.tvPointsCount.setText(myWinningList.get(i).getPoint().split("[.]")[0] + " points");
                        Glide.with(context)
                                .load(myWinningList.get(i).getEventImage())
                                .error(R.drawable.dummy_image)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(holder.binding.ivImage1);
                                    /*holder.binding.rlWinnings1.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, CommunityActivity.class);
                                        intent.putExtra("data", new Gson().toJson(myWinningList.get(i)));
                                        context.startActivity(intent);
                                    });*/
                    }
                    if (i == 7) {
                        if(myWinningList.size() > 7){
                            holder.binding.tvWinningName2.setText(myWinningList.get(i).getName());
                            holder.binding.tvPointsCount2.setText(myWinningList.get(i).getPoint().split("[.]")[0] + " points");
                            Glide.with(context)
                                    .load(myWinningList.get(i).getEventImage())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);
                                    /*holder.binding.rlWinning2.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, CommunityActivity.class);
                                        intent.putExtra("data", new Gson().toJson(myWinningList.get(i)));
                                        context.startActivity(intent);
                                    });*/
                        }else{
                            holder.binding.rlWinning2.setVisibility(View.GONE);
                            holder.binding.rlWinnings3.setVisibility(View.GONE);
                        }
                    }
                    if (i == 8) {
                        if(myWinningList.size() > 8){
                            holder.binding.tvWinningName3.setText(myWinningList.get(i).getName());
                            holder.binding.tvPointsCount3.setText(myWinningList.get(i).getPoint().split("[.]")[0] + " points");
                            Glide.with(context)
                                    .load(myWinningList.get(i).getEventImage())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);
                                    /*holder.binding.rlWinning2.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, CommunityActivity.class);
                                        intent.putExtra("data", new Gson().toJson(myWinningList.get(i)));
                                        context.startActivity(intent);
                                    });*/
                        }else{
                            holder.binding.rlWinnings3.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 3:
                holder.binding.rlWinnings1.setVisibility(View.GONE);
                holder.binding.rlWinning2.setVisibility(View.GONE);
                holder.binding.rlWinnings3.setVisibility(View.GONE);
                break;
        }
    }

    public interface ClickListenerInterface {
        void onItemClickTopUps(ProfileDetailsResponse.Data.LatestWinning data, int bgDrawable);
    }

    @Override
    public int getItemCount() {
        if (myWinningList.size() > 2) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(myWinningList.size())) / 3.0);
        } else {
            return 1;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MyWinnigItemBinding binding;

        public MyViewHolder(@NonNull MyWinnigItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
