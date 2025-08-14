package com.wyh.happyyousdk.trends.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.absorb.QuickReadActivity;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.databinding.TrendsQuickReadsAdapterBinding;

import java.util.ArrayList;
import java.util.List;

public class TrendsQuickReadsAdapter extends RecyclerView.Adapter<TrendsQuickReadsAdapter.MyViewHolder> {

    Context context;
    List<GetDashboardDataResponse.Data.QucikRead> quickReadList;
    boolean isFromProfile, isFromActOCom, isFromActO, isFromTrends;
    private final TrendsQuickReadsAdapter.OnItemClickListener listener;


    public TrendsQuickReadsAdapter(Context context, List<GetDashboardDataResponse.Data.QucikRead> quickReadList,
                                   boolean isFromProfile, boolean isFromActOCom, boolean isFromActO, boolean isFromTrends, OnItemClickListener listener) {
        this.context = context;
        this.quickReadList = quickReadList;
        this.isFromProfile = isFromProfile;
        this.isFromActOCom = isFromActOCom;
        this.isFromActO = isFromActO;
        this.isFromTrends = isFromTrends;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(String articleCode, boolean isBookmark);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TrendsQuickReadsAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.trends_quick_reads_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("size", quickReadList.size() + "");
        switch (position) {
            case 0:
                for (int i = 0; i < 2; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (quickReadList.size() > 0) {
                            holder.binding.tvArticle1.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);

                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", quickReadList.get(index).getArticleCode());
                                context.startActivity(intent);
                            });
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> {
                                if(quickReadList.get(index).getIsBookMarked() == 1){
                                    listener.onClick(quickReadList.get(index).getArticleCode(), false);
                                }else{
                                    listener.onClick(quickReadList.get(index).getArticleCode(), true);
                                }
                            });
                        }
                    } else if (i == 1) {
                        if (quickReadList.size() > 1) {
                            holder.binding.tvArticle2.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", quickReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> {
                                if(quickReadList.get(index).getIsBookMarked() == 1){
                                    listener.onClick(quickReadList.get(index).getArticleCode(), false);
                                }else{
                                    listener.onClick(quickReadList.get(index).getArticleCode(), true);
                                }
                            });
                        } else {
                            holder.binding.cvArticle2.setVisibility(View.GONE);
                        }
                    }

                    if (quickReadList.size() < 2) {
                        holder.binding.rlArticle2.setVisibility(View.GONE);
                    } else if (quickReadList.size() < 3) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                    }


//                    if (i == position) {
//                        holder.binding.tvArticle1.setText(quickReadList.get(position).getArticleName());
//                    }
//                    Glide.with(context)
//                            .load(quickReadList.get(i).getImgPath())
//                            .error(R.drawable.dummy_image)
////                .placeholder(circularProgressDrawable)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .skipMemoryCache(true)
//                            .into(holder.binding);
                }
                break;
            case 1:
                for (int i = 2; i < 4; i++) {
                    final int index = i;
                    if (i == 2) {
                        if (quickReadList.size() > 2) {
                            holder.binding.tvArticle1.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", quickReadList.get(index).getArticleCode());
//                                intent.putExtra("title", quickReadList.get(index).getArticleName());
//                                intent.putExtra("description", quickReadList.get(index).getArticleName());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> {
                                if(quickReadList.get(index).getIsBookMarked() == 1){
                                    listener.onClick(quickReadList.get(index).getArticleCode(), false);
                                }else{
                                    listener.onClick(quickReadList.get(index).getArticleCode(), true);
                                }
                            });
                        }
                    } else if (i == 3) {
                        if (quickReadList.size() > 3) {
                            holder.binding.tvArticle2.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", quickReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> {
                                if(quickReadList.get(index).getIsBookMarked() == 1){
                                    listener.onClick(quickReadList.get(index).getArticleCode(), false);
                                }else{
                                    listener.onClick(quickReadList.get(index).getArticleCode(), true);
                                }
                            });
                        } else {
                            holder.binding.cvArticle2.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 2:
                for (int i = 4; i < 6; i++) {
                    final int index = i;
                    if (i == 4) {
                        if (quickReadList.size() > 4) {
                            holder.binding.tvArticle1.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", quickReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> {
                                if(quickReadList.get(index).getIsBookMarked() == 1){
                                    listener.onClick(quickReadList.get(index).getArticleCode(), false);
                                }else{
                                    listener.onClick(quickReadList.get(index).getArticleCode(), true);
                                }
                            });
                        }
                    } else if (i == 5) {
                        if (quickReadList.size() > 5) {
                            holder.binding.tvArticle2.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", quickReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> {
                                if(quickReadList.get(index).getIsBookMarked() == 1){
                                    listener.onClick(quickReadList.get(index).getArticleCode(), false);
                                }else{
                                    listener.onClick(quickReadList.get(index).getArticleCode(), true);
                                }
                            });
                        } else {
                            holder.binding.cvArticle2.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 3:
                for (int i = 6; i < 8; i++) {
                    final int index = i;
                    if (i == 6) {
                        if (quickReadList.size() > 6) {
                            holder.binding.tvArticle1.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", quickReadList.get(index).getArticleCode());
//                                intent.putExtra("title", quickReadList.get(index).getArticleName());
//                                intent.putExtra("description", quickReadList.get(index).getArticleName());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> {
                                if(quickReadList.get(index).getIsBookMarked() == 1){
                                    listener.onClick(quickReadList.get(index).getArticleCode(), false);
                                }else{
                                    listener.onClick(quickReadList.get(index).getArticleCode(), true);
                                }
                            });
                        }
                    } else if (i == 7) {
                        if (quickReadList.size() > 7) {
                            holder.binding.tvArticle2.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", quickReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> {
                                if(quickReadList.get(index).getIsBookMarked() == 1){
                                    listener.onClick(quickReadList.get(index).getArticleCode(), false);
                                }else{
                                    listener.onClick(quickReadList.get(index).getArticleCode(), true);
                                }
                            });
                        } else {
                            holder.binding.cvArticle2.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 4:
                for (int i = 8; i < 10; i++) {
                    final int index = i;
                    if (i == 8) {
                        if (quickReadList.size() > 8) {
                            holder.binding.tvArticle1.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", quickReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> {
                                if(quickReadList.get(index).getIsBookMarked() == 1){
                                    listener.onClick(quickReadList.get(index).getArticleCode(), false);
                                }else{
                                    listener.onClick(quickReadList.get(index).getArticleCode(), true);
                                }
                            });
                        }
                    } else {
                        if (quickReadList.size() > 9) {
                            holder.binding.tvArticle2.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", quickReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> {
                                if(quickReadList.get(index).getIsBookMarked() == 1){
                                    listener.onClick(quickReadList.get(index).getArticleCode(), false);
                                }else{
                                    listener.onClick(quickReadList.get(index).getArticleCode(), true);
                                }
                            });
                        } else {
                            holder.binding.cvArticle2.setVisibility(View.GONE);
                        }

                    }
                }
                break;
            case 5:
                holder.binding.rlArticle1.setVisibility(View.GONE);
                holder.binding.rlArticle2.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (quickReadList.size() > 1) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(quickReadList.size())) / 2.0);
        } else {
            return 1;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TrendsQuickReadsAdapterBinding binding;

        public MyViewHolder(@NonNull TrendsQuickReadsAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
