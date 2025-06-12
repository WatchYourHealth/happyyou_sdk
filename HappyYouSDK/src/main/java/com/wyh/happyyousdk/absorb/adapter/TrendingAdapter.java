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
import com.wyh.happyyousdk.databinding.AbsorbQuickReadsAdapterBinding;
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

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.MyViewHolder> {

    Context context;
    List<GetQuickReadResponse.Data.TrendingBlog> trendingBlogList;
    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;
    private final TrendingAdapter.OnItemClickListener listener;

    public TrendingAdapter(Context context, List<GetQuickReadResponse.Data.TrendingBlog> trendingBlogList, OnItemClickListener listener) {
        this.context = context;
        this.trendingBlogList = trendingBlogList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AbsorbQuickReadsAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.absorb_quick_reads_adapter, parent, false);
//        screenWidth = displayMetrics.widthPixels
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.d("data", trendingBlogList.size() + "");
        Log.d("data", (trendingBlogList.size() / 2) + "");
        Log.d("data", position + "");

        switch (position) {
            case 0:
                for (int i = 0; i < 2; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (!trendingBlogList.isEmpty()) {
                            holder.binding.tvArticle1.setText(trendingBlogList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(trendingBlogList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);

                            

                            holder.binding.cvArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", trendingBlogList.get(index).getArticleCode());
//                                intent.putExtra("title", trendingBlogList.get(index).getArticleName());
//                                intent.putExtra("description", trendingBlogList.get(index).getArticleName());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });

                            if(trendingBlogList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(trendingBlogList.get(index).getArticleCode(), trendingBlogList.get(index).getIsBookMarked() != 1));
                        }
                    }
                    else {
                        if (trendingBlogList.size() > 1) {
                            holder.binding.tvArticle2.setText(trendingBlogList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(trendingBlogList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.cvArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", trendingBlogList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(trendingBlogList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(trendingBlogList.get(index).getArticleCode(), trendingBlogList.get(index).getIsBookMarked() != 1));
                        } else {
                            holder.binding.rlArticle2Hide.setVisibility(View.GONE);
                        }
                    }

                    if (trendingBlogList.size() < 2) {
                        holder.binding.cvArticle2.setVisibility(View.GONE);
                    } else if (trendingBlogList.size() < 3) {
                        holder.binding.cvArticle2.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case 1:
                for (int i = 2; i < 4; i++) {
                    final int index = i;
                    if (i == 2) {
                        if (trendingBlogList.size() > 2) {
                            holder.binding.tvArticle1.setText(trendingBlogList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(trendingBlogList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.cvArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", trendingBlogList.get(index).getArticleCode());
//                                intent.putExtra("title", trendingBlogList.get(index).getArticleName());
//                                intent.putExtra("description", trendingBlogList.get(index).getArticleName());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(trendingBlogList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(trendingBlogList.get(index).getArticleCode(), trendingBlogList.get(index).getIsBookMarked() != 1));
                        }
                    } else if (i == 3) {
                        if (trendingBlogList.size() > 3) {
                            holder.binding.tvArticle2.setText(trendingBlogList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(trendingBlogList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.cvArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", trendingBlogList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(trendingBlogList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(trendingBlogList.get(index).getArticleCode(), trendingBlogList.get(index).getIsBookMarked() != 1));
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
                        if (trendingBlogList.size() > 4) {
                            holder.binding.tvArticle1.setText(trendingBlogList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(trendingBlogList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.cvArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", trendingBlogList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(trendingBlogList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(trendingBlogList.get(index).getArticleCode(), trendingBlogList.get(index).getIsBookMarked() != 1));
                        }
                    } else {
                        if (trendingBlogList.size() > 5) {
                            holder.binding.tvArticle2.setText(trendingBlogList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(trendingBlogList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.cvArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", trendingBlogList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(trendingBlogList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(trendingBlogList.get(index).getArticleCode(), trendingBlogList.get(index).getIsBookMarked() != 1));
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
                        if (trendingBlogList.size() > 6) {
                            holder.binding.tvArticle1.setText(trendingBlogList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(trendingBlogList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.cvArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", trendingBlogList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(trendingBlogList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(trendingBlogList.get(index).getArticleCode(), trendingBlogList.get(index).getIsBookMarked() != 1));
                        }
                    } else {
                        if (trendingBlogList.size() > 7) {
                            holder.binding.tvArticle2.setText(trendingBlogList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(trendingBlogList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.cvArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", trendingBlogList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(trendingBlogList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(trendingBlogList.get(index).getArticleCode(), trendingBlogList.get(index).getIsBookMarked() != 1));
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
                        if (trendingBlogList.size() > 8) {
                            holder.binding.tvArticle1.setText(trendingBlogList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(trendingBlogList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.cvArticle1.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", trendingBlogList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(trendingBlogList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(trendingBlogList.get(index).getArticleCode(), trendingBlogList.get(index).getIsBookMarked() != 1));
                        }
                    } else {
                        if (trendingBlogList.size() > 9) {
                            holder.binding.tvArticle2.setText(trendingBlogList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(trendingBlogList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.cvArticle2.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", trendingBlogList.get(index).getArticleCode());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                            
                            if(trendingBlogList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(trendingBlogList.get(index).getArticleCode(), trendingBlogList.get(index).getIsBookMarked() != 1));
                        } else {
                            holder.binding.cvArticle2.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 5:
                holder.binding.cvArticle1.setVisibility(View.GONE);
                holder.binding.cvArticle2.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public int getItemCount() {
        if(trendingBlogList.size() > 6){
            return 6;
        }else if (trendingBlogList.size() > 2) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(trendingBlogList.size())) / 2.0);
        } else {
            return 1;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        AbsorbQuickReadsAdapterBinding binding;

        public MyViewHolder(@NonNull AbsorbQuickReadsAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick(String articleCode, boolean isBookmark);
    }

}
