package com.wyh.happyyousdk.absorb.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.absorb.QuickReadActivity;
import com.wyh.happyyousdk.absorb.QuickReadDashboard;
import com.wyh.happyyousdk.model.request.absorb.ShareBlogRequest;
import com.wyh.happyyousdk.model.response.absorb.GetQuickReadResponse;
import com.wyh.happyyousdk.databinding.AbsorbMostReadAdapterBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostReadAdapter extends RecyclerView.Adapter<MostReadAdapter.MyViewHolder> {

    Context context;
    List<GetQuickReadResponse.Data.MostRead> mostReadList;
    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;
    private final MostReadAdapter.OnItemClickListener listener;

    public MostReadAdapter(Context context, List<GetQuickReadResponse.Data.MostRead> mostReadList, OnItemClickListener listener) {
        this.context = context;
        this.mostReadList = mostReadList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AbsorbMostReadAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.absorb_most_read_adapter, parent, false);
//        screenWidth = displayMetrics.widthPixels
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        switch (position) {
            case 0:
                for (int i = 0; i < 2; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (!mostReadList.isEmpty()) {
                            holder.binding.tvArticle1.setText(mostReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(mostReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);

                            holder.binding.cvArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", mostReadList.get(index).getArticleCode());
//                                intent.putExtra("title", mostReadList.get(index).getArticleName());
//                                intent.putExtra("description", mostReadList.get(index).getArticleName());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            if(mostReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> {
                                if(mostReadList.get(index).getIsBookMarked() == 1){
                                    mostReadList.get(index).setIsBookMarked(0);
                                    listener.onClick(mostReadList.get(index).getArticleCode(), false);
                                }else{
                                    mostReadList.get(index).setIsBookMarked(1);
                                    listener.onClick(mostReadList.get(index).getArticleCode(), true);
                                }
                            });
                        }
                    } else {
                        if (mostReadList.size() > 1) {
                            holder.binding.tvArticle2.setText(mostReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(mostReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);

                            holder.binding.cvArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", mostReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(mostReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(mostReadList.get(index).getArticleCode(), mostReadList.get(index).getIsBookMarked() != 1));
                        }
                    }

                    if (mostReadList.size() < 2) {
                        holder.binding.cvArticle2.setVisibility(View.GONE);
                    } else if (mostReadList.size() < 3) {
                        holder.binding.cvArticle2.setVisibility(View.VISIBLE);
                    }


//                    if (i == position) {
//                        holder.binding.tvArticle1.setText(mostReadList.get(position).getArticleName());
//                    }
//                    Glide.with(context)
//                            .load(mostReadList.get(i).getImgPath())
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
                        if (mostReadList.size() > 2) {
                            holder.binding.tvArticle1.setText(mostReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(mostReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.cvArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", mostReadList.get(index).getArticleCode());
//                                intent.putExtra("title", mostReadList.get(index).getArticleName());
//                                intent.putExtra("description", mostReadList.get(index).getArticleName());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(mostReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(mostReadList.get(index).getArticleCode(), mostReadList.get(index).getIsBookMarked() != 1));
                        }
                    } else {
                        if (mostReadList.size() > 3) {
                            holder.binding.tvArticle2.setText(mostReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(mostReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.cvArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", mostReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(mostReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(mostReadList.get(index).getArticleCode(), mostReadList.get(index).getIsBookMarked() != 1));
                        }else{
                            holder.binding.cvArticle2.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 2:
                for (int i = 4; i < 6; i++) {
                    final int index = i;
                    if (i == 4) {
                        if (mostReadList.size() > 4) {
                            holder.binding.tvArticle1.setText(mostReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(mostReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.cvArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", mostReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(mostReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(mostReadList.get(index).getArticleCode(), mostReadList.get(index).getIsBookMarked() != 1));
                        }
                    } else if (i == 5) {
                        if (mostReadList.size() > 5) {
                            holder.binding.tvArticle2.setText(mostReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(mostReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.cvArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", mostReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(mostReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(mostReadList.get(index).getArticleCode(), mostReadList.get(index).getIsBookMarked() != 1));
                        }else{
                            holder.binding.cvArticle2.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 3:
                for (int i = 6; i < 8; i++) {
                    final int index = i;
                    if (i == 6) {
                        if (mostReadList.size() > 6) {
                            holder.binding.tvArticle1.setText(mostReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(mostReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.cvArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", mostReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(mostReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(mostReadList.get(index).getArticleCode(), mostReadList.get(index).getIsBookMarked() != 1));
                        }
                    } else if (i == 7) {
                        if (mostReadList.size() > 7) {
                            holder.binding.tvArticle2.setText(mostReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(mostReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.cvArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", mostReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(mostReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(mostReadList.get(index).getArticleCode(), mostReadList.get(index).getIsBookMarked() != 1));
                        }else{
                            holder.binding.cvArticle2.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 4:
                for (int i = 8; i < 10; i++) {
                    final int index = i;
                    if (i == 8) {
                        if (mostReadList.size() > 8) {
                            holder.binding.tvArticle1.setText(mostReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(mostReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.cvArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", mostReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(mostReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(mostReadList.get(index).getArticleCode(), mostReadList.get(index).getIsBookMarked() != 1));
                        }
                    } else if (i == 9) {
                        if (mostReadList.size() > 9) {
                            holder.binding.tvArticle2.setText(mostReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(mostReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.cvArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", mostReadList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(mostReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(mostReadList.get(index).getArticleCode(), mostReadList.get(index).getIsBookMarked() != 1));
                        }else{
                            holder.binding.cvArticle2.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 5:
                holder.binding.cvArticle2.setVisibility(View.GONE);
                holder.binding.cvArticle2.setVisibility(View.GONE);
                break;
        }

    }


    @Override
    public int getItemCount() {
        if (mostReadList.size() > 2) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(mostReadList.size())) / 2.0);
        } else {
            return 1;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        AbsorbMostReadAdapterBinding binding;

        public MyViewHolder(@NonNull AbsorbMostReadAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick(String articleCode, boolean isBookmark);
    }

}
