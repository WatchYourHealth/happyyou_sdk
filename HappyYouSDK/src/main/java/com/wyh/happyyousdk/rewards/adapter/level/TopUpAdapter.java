package com.wyh.happyyousdk.rewards.adapter.level;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.TopUpAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.TopUp;

import java.util.List;

public class TopUpAdapter extends RecyclerView.Adapter<TopUpAdapter.MyViewHolder> {

    Context context;
    List<TopUp> topUpList;
    ClickListenerInterface clickListenerInterface;

    public TopUpAdapter(Context context, List<TopUp> topUpList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.topUpList = topUpList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TopUpAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.top_up_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                for (int i = 0; i < 3; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (topUpList.size() > 0) {
                            holder.binding.tvArticle1.setText(topUpList.get(i).getTopUpName());
                            holder.binding.tvStart1.setText(topUpList.get(i).getPoint() + " points");

                            Glide.with(context)
                                    .load(topUpList.get(i).getTopupIcon())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);

                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickTopUps(topUpList.get(index), R.drawable.pink_circle);
                            });
                            //holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_pink));

                            getScratchAndWinPos1(topUpList.get(index), holder);
                            if (topUpList.get(i).isIsStarted() && !topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            }

                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity1.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity1.setProgress(4);*/
                            } else
                                holder.binding.tvCompleted1.setVisibility(View.GONE);

                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.ivCheck1.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.ivCheck1.setVisibility(View.GONE);
                            }

                            if(topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity1.setProgress(100);
                            }else {
                                if(topUpList.get(i).getProgressPercentage() != null){
                                    holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                    holder.binding.progressActivity1.setProgress(topUpList.get(i).getProgressPercentage());
                                }
                            }

                            if (topUpList.get(i).getTopUpName().toLowerCase().contains("challenge") && !topUpList.get(i).getTopUpName().equalsIgnoreCase("Safety challenge")) {
                                holder.binding.llbanner1.setVisibility(View.VISIBLE);
                                holder.binding.tvBanner1.setText("Coming Soon");
                            } else {
                                if (topUpList.get(i).isIsCompleted() && topUpList.get(i).getRecurrenceDays() != -1 && topUpList.get(i).getRecurrenceDays() != 0) {
                                    holder.binding.llbanner1.setVisibility(View.VISIBLE);
                                    String msg = "Come back next ";
                                    if(topUpList.get(i).getRecurrenceDays() == 7){
                                        holder.binding.tvBanner1.setText(msg+"week");
                                    }else if(topUpList.get(i).getRecurrenceDays() == 30){
                                        holder.binding.tvBanner1.setText(msg+"month");
                                    }else if(topUpList.get(i).getRecurrenceDays() == 90){
                                        holder.binding.tvBanner1.setText(msg+"quarter");
                                    }else if(topUpList.get(i).getRecurrenceDays() == 365){
                                        holder.binding.tvBanner1.setText(msg+"year");
                                    }else{
                                        holder.binding.tvBanner1.setText(msg);
                                    }
                                }
                                else{
                                    holder.binding.llbanner1.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else if (i == 1) {
                        if (topUpList.size() > 1) {
                            holder.binding.tvArticle2.setText(topUpList.get(i).getTopUpName());
                            holder.binding.tvStart2.setText(topUpList.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(topUpList.get(i).getTopupIcon())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickTopUps(topUpList.get(index), R.drawable.blue_circle);
                            });
                            //holder.binding.progressActivity2.setIndicatorColor(context.getColor(R.color.light_blue));
                            getScratchAndWinPos2(topUpList.get(index), holder);

                            if (topUpList.get(i).isIsStarted() && !topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            }


                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity2.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity2.setProgress(4);*/
                            } else
                                holder.binding.tvCompleted2.setVisibility(View.GONE);

                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.ivCheck2.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.ivCheck2.setVisibility(View.GONE);
                            }

                            if(topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity2.setProgress(100);
                            }else {
                                if(topUpList.get(i).getProgressPercentage() != null) {
                                    holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                    int p = Math.round(topUpList.get(i).getProgressPercentage());
                                    holder.binding.progressActivity2.setProgress(p);
                                }
                            }

                            if (topUpList.get(i).getTopUpName().toLowerCase().contains("challenge") && !topUpList.get(i).getTopUpName().equalsIgnoreCase("Safety challenge")) {
                                holder.binding.llbanner2.setVisibility(View.VISIBLE);
                                holder.binding.tvBanner2.setText("Coming Soon");
                            } else {
                                if (topUpList.get(i).isIsCompleted() && topUpList.get(i).getRecurrenceDays() != -1 && topUpList.get(i).getRecurrenceDays() != 0) {
                                    holder.binding.llbanner2.setVisibility(View.VISIBLE);
                                    String msg = "Come back next ";
                                    if(topUpList.get(i).getRecurrenceDays() == 7){
                                        msg += "week";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 30){
                                        msg += "month";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 90){
                                        msg += "quarter";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 365){
                                        msg += "year";
                                    }
                                    holder.binding.tvBanner2.setText(msg);
                                }
                                else{
                                    holder.binding.llbanner2.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
                        if (topUpList.size() > 2) {
                            holder.binding.tvArticle3.setText(topUpList.get(i).getTopUpName());
                            holder.binding.tvStart3.setText(topUpList.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(topUpList.get(i).getTopupIcon())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);

                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickTopUps(topUpList.get(index), R.drawable.orange_circle);
                            });
                            //holder.binding.progressActivity3.setIndicatorColor(context.getColor(R.color.light_orange));
                            getScratchAndWinPos3(topUpList.get(index), holder);
                            if (topUpList.get(i).isIsStarted() && !topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow3.setVisibility(View.GONE);

                            } else{
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.progressActivity3.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);

                            }
                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);

                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity3.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity3.setProgress(4);*/
                            } else
                                holder.binding.tvCompleted3.setVisibility(View.GONE);

                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.ivCheck3.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.ivCheck3.setVisibility(View.GONE);
                            }

                            if(topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity3.setProgress(100);
                            }else {
                                if(topUpList.get(i).getProgressPercentage() != null) {
                                    holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                    int p = Math.round(topUpList.get(i).getProgressPercentage());
                                    holder.binding.progressActivity3.setProgress(p);
                                }
                            }


                            if (topUpList.get(i).getTopUpName().toLowerCase().contains("challenge") && !topUpList.get(i).getTopUpName().equalsIgnoreCase("Safety challenge")) {
                                holder.binding.llbanner3.setVisibility(View.VISIBLE);
                                holder.binding.tvBanner3.setText("Coming Soon");
                            } else {
                                if (topUpList.get(i).isIsCompleted() && topUpList.get(i).getRecurrenceDays() != -1 && topUpList.get(i).getRecurrenceDays() != 0) {
                                    holder.binding.llbanner3.setVisibility(View.VISIBLE);
                                    String msg = "Come back next ";
                                    if(topUpList.get(i).getRecurrenceDays() == 7){
                                        msg += "week";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 30){
                                        msg += "month";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 90){
                                        msg += "quarter";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 365){
                                        msg += "year";
                                    }
                                    holder.binding.tvBanner3.setText(msg);
                                }
                                else{
                                    holder.binding.llbanner3.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    if (topUpList.size() < 2) {
                        holder.binding.rlArticle2.setVisibility(View.GONE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    } else if (topUpList.size() < 3) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }
                }
                break;
            case 1:
                for (int i = 3; i < 6; i++) {
                    final int index = i;
                    if (i == 3) {
                        if (topUpList.size() > 3) {
                            holder.binding.tvArticle1.setText(topUpList.get(i).getTopUpName());
                            holder.binding.tvStart1.setText(topUpList.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(topUpList.get(i).getTopupIcon())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickTopUps(topUpList.get(index), R.drawable.pink_circle);
                            });
                            //holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_pink));
                            if (topUpList.get(i).isIsStarted() && !topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            }

                            getScratchAndWinPos1(topUpList.get(index), holder);
                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity1.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity1.setProgress(4);*/
                            } else
                                holder.binding.tvCompleted1.setVisibility(View.GONE);

                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.ivCheck1.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.ivCheck1.setVisibility(View.GONE);
                            }

                            if(topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity1.setProgress(100);
                            }else {
                                if(topUpList.get(i).getProgressPercentage() != null) {
                                    holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                    int p = Math.round(topUpList.get(i).getProgressPercentage());
                                    holder.binding.progressActivity1.setProgress(p);
                                }
                            }

                            if (topUpList.get(i).getTopUpName().toLowerCase().contains("challenge") && !topUpList.get(i).getTopUpName().equalsIgnoreCase("Safety challenge")) {
                                holder.binding.llbanner1.setVisibility(View.VISIBLE);
                                holder.binding.tvBanner1.setText("Coming Soon");
                            } else {
                                if (topUpList.get(i).isIsCompleted() && topUpList.get(i).getRecurrenceDays() != -1 && topUpList.get(i).getRecurrenceDays() != 0) {
                                    holder.binding.llbanner1.setVisibility(View.VISIBLE);
                                    String msg = "Come back next ";
                                    if(topUpList.get(i).getRecurrenceDays() == 7){
                                        msg += "week";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 30){
                                        msg += "month";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 90){
                                        msg += "quarter";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 365){
                                        msg += "year";
                                    }
                                    holder.binding.tvBanner1.setText(msg);
                                }
                                else{
                                    holder.binding.llbanner1.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else if (i == 4) {
                        if (topUpList.size() > 4) {
                            holder.binding.tvArticle2.setText(topUpList.get(i).getTopUpName());
                            holder.binding.tvStart2.setText(topUpList.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(topUpList.get(i).getTopupIcon())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickTopUps(topUpList.get(index), R.drawable.blue_circle);
                            });
                            //holder.binding.progressActivity2.setIndicatorColor(context.getColor(R.color.light_blue));
                            getScratchAndWinPos2(topUpList.get(index), holder);
                            if (topUpList.get(i).isIsStarted() && !topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            }

                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity2.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity2.setProgress(4);*/
                            } else
                                holder.binding.tvCompleted2.setVisibility(View.GONE);

                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.ivCheck2.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.ivCheck2.setVisibility(View.GONE);
                            }

                            if(topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity2.setProgress(100);
                            }else {
                                if(topUpList.get(i).getProgressPercentage() != null) {
                                    holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                    int p = Math.round(topUpList.get(i).getProgressPercentage());
                                    holder.binding.progressActivity2.setProgress(p);
                                }
                            }

                            if (topUpList.get(i).getTopUpName().toLowerCase().contains("challenge") && !topUpList.get(i).getTopUpName().equalsIgnoreCase("Safety challenge")) {
                                holder.binding.llbanner2.setVisibility(View.VISIBLE);
                                holder.binding.tvBanner2.setText("Coming Soon");
                            } else {
                                if (topUpList.get(i).isIsCompleted() && topUpList.get(i).getRecurrenceDays() != -1 && topUpList.get(i).getRecurrenceDays() != 0) {
                                    holder.binding.llbanner2.setVisibility(View.VISIBLE);
                                    String msg = "Come back next ";
                                    if(topUpList.get(i).getRecurrenceDays() == 7){
                                        msg += "week";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 30){
                                        msg += "month";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 90){
                                        msg += "quarter";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 365){
                                        msg += "year";
                                    }
                                    holder.binding.tvBanner2.setText(msg);
                                }
                                else{
                                    holder.binding.llbanner2.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
                        if (topUpList.size() > 5) {
                            holder.binding.tvArticle3.setText(topUpList.get(i).getTopUpName());
                            holder.binding.tvStart3.setText(topUpList.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(topUpList.get(i).getTopupIcon())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);
                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickTopUps(topUpList.get(index), R.drawable.orange_circle);
                            });
                            //holder.binding.progressActivity3.setIndicatorColor(context.getColor(R.color.light_orange));
                            if (topUpList.get(i).isIsStarted() && !topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow3.setVisibility(View.GONE);

                            } else{
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.progressActivity3.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);

                            }
                            getScratchAndWinPos3(topUpList.get(index), holder);
                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);

                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity3.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity3.setProgress(4);*/
                            } else
                                holder.binding.tvCompleted3.setVisibility(View.GONE);

                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.ivCheck3.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.ivCheck3.setVisibility(View.GONE);
                            }

                            if(topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity3.setProgress(100);
                            }else {
                                if(topUpList.get(i).getProgressPercentage() != null) {
                                    holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                    int p = Math.round(topUpList.get(i).getProgressPercentage());
                                    holder.binding.progressActivity3.setProgress(p);
                                }
                            }


                            if (topUpList.get(i).getTopUpName().toLowerCase().contains("challenge") && !topUpList.get(i).getTopUpName().equalsIgnoreCase("Safety challenge")) {
                                holder.binding.llbanner3.setVisibility(View.VISIBLE);
                                holder.binding.tvBanner3.setText("Coming Soon");
                            } else {
                                if (topUpList.get(i).isIsCompleted() && topUpList.get(i).getRecurrenceDays() != -1 && topUpList.get(i).getRecurrenceDays() != 0) {
                                    holder.binding.llbanner3.setVisibility(View.VISIBLE);
                                    String msg = "Come back next ";
                                    if(topUpList.get(i).getRecurrenceDays() == 7){
                                        msg += "week";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 30){
                                        msg += "month";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 90){
                                        msg += "quarter";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 365){
                                        msg += "year";
                                    }
                                    holder.binding.tvBanner3.setText(msg);
                                }
                                else{
                                    holder.binding.llbanner3.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
                if (topUpList.size() < 4) {
                    holder.binding.rlArticle2.setVisibility(View.GONE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                } else if (topUpList.size() < 5) {
                    holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                }
                break;
            case 2:
                for (int i = 6; i < 9; i++) {
                    final int index = i;
                    if (i == 6) {
                        if (topUpList.size() > 6) {
                            holder.binding.tvArticle1.setText(topUpList.get(i).getTopUpName());
                            holder.binding.tvStart1.setText(topUpList.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(topUpList.get(i).getTopupIcon())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickTopUps(topUpList.get(index), R.drawable.pink_circle);
                            });
                            //holder.binding.progressActivity1.setIndicatorColor(context.getColor(R.color.light_pink));
                            if (topUpList.get(i).isIsStarted() && !topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.progressActivity1.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                            }
                            getScratchAndWinPos1(topUpList.get(index), holder);
                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted1.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted1.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow1.setVisibility(View.GONE);
                                holder.binding.progressActivity1.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity1.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity1.setProgress(4);*/
                            } else
                                holder.binding.tvCompleted1.setVisibility(View.GONE);

                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.ivCheck1.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.ivCheck1.setVisibility(View.GONE);
                            }

                            if(topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity1.setProgress(100);
                            }else {
                                if(topUpList.get(i).getProgressPercentage() != null) {
                                    holder.binding.progressActivity1.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                    int p = Math.round(topUpList.get(i).getProgressPercentage());
                                    holder.binding.progressActivity1.setProgress(p);
                                }
                            }

                            if (topUpList.get(i).getTopUpName().toLowerCase().contains("challenge") && !topUpList.get(i).getTopUpName().equalsIgnoreCase("Safety challenge")) {
                                holder.binding.llbanner1.setVisibility(View.VISIBLE);
                                holder.binding.tvBanner1.setText("Coming Soon");
                            } else {
                                if (topUpList.get(i).isIsCompleted() && topUpList.get(i).getRecurrenceDays() != -1 && topUpList.get(i).getRecurrenceDays() != 0) {
                                    holder.binding.llbanner1.setVisibility(View.VISIBLE);
                                    String msg = "Come back next ";
                                    if(topUpList.get(i).getRecurrenceDays() == 7){
                                        msg += "week";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 30){
                                        msg += "month";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 90){
                                        msg += "quarter";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 365){
                                        msg += "year";
                                    }
                                    holder.binding.tvBanner1.setText(msg);
                                }
                                else{
                                    holder.binding.llbanner1.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else if (i == 7) {
                        if (topUpList.size() > 7) {
                            holder.binding.tvArticle2.setText(topUpList.get(i).getTopUpName());
                            holder.binding.tvStart2.setText(topUpList.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(topUpList.get(i).getTopupIcon())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickTopUps(topUpList.get(index), R.drawable.blue_circle);
                            });
                            //holder.binding.progressActivity2.setIndicatorColor(context.getColor(R.color.light_blue));
                            if (topUpList.get(i).isIsStarted() && !topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.VISIBLE);
                                 holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                                 holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                            } else{
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);

                            }
                            getScratchAndWinPos2(topUpList.get(index), holder);
                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted2.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted2.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);

                                holder.binding.ivCheckyellow2.setVisibility(View.GONE);
                                holder.binding.progressActivity2.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity2.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity2.setProgress(4);*/
                            } else
                                holder.binding.tvCompleted2.setVisibility(View.GONE);

                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.ivCheck2.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.ivCheck2.setVisibility(View.GONE);
                            }

                            if(topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity2.setProgress(100);
                            }else {
                                if(topUpList.get(i).getProgressPercentage() != null) {
                                    holder.binding.progressActivity2.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                    int p = Math.round(topUpList.get(i).getProgressPercentage());
                                    holder.binding.progressActivity2.setProgress(p);
                                }
                            }

                            if (topUpList.get(i).getTopUpName().toLowerCase().contains("challenge") && !topUpList.get(i).getTopUpName().equalsIgnoreCase("Safety challenge")) {
                                holder.binding.llbanner2.setVisibility(View.VISIBLE);
                                holder.binding.tvBanner2.setText("Coming Soon");
                            } else {
                                if (topUpList.get(i).isIsCompleted() && topUpList.get(i).getRecurrenceDays() != -1 && topUpList.get(i).getRecurrenceDays() != 0) {
                                    holder.binding.llbanner2.setVisibility(View.VISIBLE);
                                    String msg = "Come back next ";
                                    if(topUpList.get(i).getRecurrenceDays() == 7){
                                        msg += "week";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 30){
                                        msg += "month";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 90){
                                        msg += "quarter";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 365){
                                        msg += "year";
                                    }
                                    holder.binding.tvBanner2.setText(msg);
                                }
                                else{
                                    holder.binding.llbanner2.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
                        if (topUpList.size() > 8) {
                            holder.binding.tvArticle3.setText(topUpList.get(i).getTopUpName());
                            holder.binding.tvStart3.setText(topUpList.get(i).getPoint() + " points");
                            Glide.with(context)
                                    .load(topUpList.get(i).getTopupIcon())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);
                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickTopUps(topUpList.get(index), R.drawable.orange_circle);
                            });
                            //holder.binding.progressActivity3.setIndicatorColor(context.getColor(R.color.light_orange));
                            if (topUpList.get(i).isIsStarted() && !topUpList.get(i).isIsCompleted()){
                                 holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.VISIBLE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);

                            } else{
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.progressActivity3.setVisibility(View.INVISIBLE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);

                            }
                            getScratchAndWinPos3(topUpList.get(index), holder);
                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.tvCompleted3.setVisibility(View.VISIBLE);
                                holder.binding.tvStarted3.setVisibility(View.GONE);
                                holder.binding.ivCheckyellow3.setVisibility(View.GONE);

                                holder.binding.progressActivity3.setVisibility(View.VISIBLE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    holder.binding.progressActivity3.setProgress(4, true);
                                } else
                                    holder.binding.progressActivity3.setProgress(4);*/
                            } else
                                holder.binding.tvCompleted3.setVisibility(View.GONE);

                            if (topUpList.get(i).isIsCompleted()) {
                                holder.binding.ivCheck3.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.ivCheck3.setVisibility(View.GONE);
                            }

                            if(topUpList.get(i).isIsCompleted()){
                                holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar));
                                holder.binding.progressActivity3.setProgress(100);
                            }else {
                                if(topUpList.get(i).getProgressPercentage() != null) {
                                    holder.binding.progressActivity3.setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_incomplete));
                                    int p = Math.round(topUpList.get(i).getProgressPercentage());
                                    holder.binding.progressActivity3.setProgress(p);
                                }
                            }

                            if (topUpList.get(i).getTopUpName().toLowerCase().contains("challenge") && !topUpList.get(i).getTopUpName().equalsIgnoreCase("Safety challenge")) {
                                holder.binding.llbanner3.setVisibility(View.VISIBLE);
                                holder.binding.tvBanner3.setText("Coming Soon");
                            } else {
                                if (topUpList.get(i).isIsCompleted() && topUpList.get(i).getRecurrenceDays() != -1 && topUpList.get(i).getRecurrenceDays() != 0) {
                                    holder.binding.llbanner3.setVisibility(View.VISIBLE);
                                    String msg = "Come back next ";
                                    if(topUpList.get(i).getRecurrenceDays() == 7){
                                        msg += "week";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 30){
                                        msg += "month";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 90){
                                        msg += "quarter";
                                    }else if(topUpList.get(i).getRecurrenceDays() == 365){
                                        msg += "year";
                                    }
                                    holder.binding.tvBanner3.setText(msg);
                                }
                                else{
                                    holder.binding.llbanner3.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
                if (topUpList.size() < 7) {
                    holder.binding.rlArticle2.setVisibility(View.GONE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                } else if (topUpList.size() < 8) {
                    holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                }
                break;
            case 3:
                holder.binding.rlArticle1.setVisibility(View.GONE);
                holder.binding.rlArticle2.setVisibility(View.GONE);
                holder.binding.rlArticle3.setVisibility(View.GONE);
                break;
        }
    }

    public interface ClickListenerInterface {
        void onItemClickTopUps(TopUp topUp, int bgDrawable);
    }

    @Override
    public int getItemCount() {
        if (topUpList.size() > 3 && topUpList.size() <= 9) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(topUpList.size())) / 3.0);
        } else if (topUpList.size() > 9)
            return 3;
        else {
            return 1;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TopUpAdapterBinding binding;

        public MyViewHolder(@NonNull TopUpAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void getScratchAndWinPos1(TopUp topUp, MyViewHolder holder){
        if(topUp.getTopUpName().toLowerCase().equals("scratch & win")){
            if(topUp.isIsCompleted()){
                holder.binding.tvStart1.setText(topUp.getPoint() + " points");
            } else {
                holder.binding.tvStart1.setText("Win Points");
            }
            holder.binding.ivInfo1.setVisibility(View.GONE);
        }else{
            holder.binding.tvStart1.setText(topUp.getPoint() + " points");
            holder.binding.ivInfo1.setVisibility(View.VISIBLE);
        }
    }
    private void getScratchAndWinPos2(TopUp topUp, MyViewHolder holder){
        if(topUp.getTopUpName().toLowerCase().equals("scratch & win")){
            if(topUp.isIsCompleted()){
                holder.binding.tvStart2.setText(topUp.getPoint() + " points");
            } else {
                holder.binding.tvStart2.setText("Win Points");
            }
            holder.binding.ivInfo2.setVisibility(View.GONE);
        }else{
            holder.binding.tvStart2.setText(topUp.getPoint() + " points");
            holder.binding.ivInfo2.setVisibility(View.VISIBLE);
        }
    }
    private void getScratchAndWinPos3(TopUp topUp, MyViewHolder holder){
        if(topUp.getTopUpName().toLowerCase().equals("scratch & win")){
            if(topUp.isIsCompleted()){
                holder.binding.tvStart3.setText(topUp.getPoint() + " points");
            } else {
                holder.binding.tvStart3.setText("Win Points");
            }
            holder.binding.ivInfo3.setVisibility(View.GONE);
        }else{
            holder.binding.tvStart3.setText(topUp.getPoint() + " points");
            holder.binding.ivInfo3.setVisibility(View.VISIBLE);
        }
    }
}
