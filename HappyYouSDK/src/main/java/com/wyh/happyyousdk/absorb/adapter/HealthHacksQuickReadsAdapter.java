package com.wyh.happyyousdk.absorb.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.DisplayMetrics;
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
import com.wyh.happyyousdk.absorb.HealthHacksActivity;
import com.wyh.happyyousdk.absorb.QuickReadActivity;
import com.wyh.happyyousdk.dashboard.SearchActivity;
import com.wyh.happyyousdk.databinding.AbsorbQuickReadsAdapterBinding;
import com.wyh.happyyousdk.model.request.absorb.ShareBlogRequest;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
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

public class HealthHacksQuickReadsAdapter extends RecyclerView.Adapter<HealthHacksQuickReadsAdapter.MyViewHolder> {

    Context context;
    List<GetDashboardDataResponse.Data.QucikRead> quickReadList;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    int screenWidth = 0;
    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;
    private final HealthHacksQuickReadsAdapter.OnItemClickListener listener;

    public HealthHacksQuickReadsAdapter(Context context, List<GetDashboardDataResponse.Data.QucikRead> quickReadList, OnItemClickListener listener) {
        this.context = context;
        this.quickReadList = quickReadList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(String articleCode, boolean isBookmark);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AbsorbQuickReadsAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.absorb_quick_reads_adapter, parent, false);

        if(context instanceof HealthHacksActivity){
            ((HealthHacksActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }else if(context instanceof SearchActivity){
            ((SearchActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }
        screenWidth = displayMetrics.widthPixels;
//        screenWidth = displayMetrics.widthPixels
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        switch (position) {
            case 0:
                for (int i = 0; i < 3; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (!quickReadList.isEmpty()) {
                            holder.binding.tvArticle1.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);

                            holder.binding.cvArticle1.setOnClickListener(view -> intentCode(index));

                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(quickReadList.get(index).getArticleCode(), quickReadList.get(index).getIsBookMarked() != 1));

                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                                
                            }
                        }else{
                            holder.binding.cvArticle1.setVisibility(View.GONE);
                            holder.binding.cvArticle2.setVisibility(View.GONE);

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
                            holder.binding.cvArticle2.setOnClickListener(view -> intentCode(index));
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(quickReadList.get(index).getArticleCode(), quickReadList.get(index).getIsBookMarked() != 1));

                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark2.setVisibility(View.GONE);
                                
                            }
                        }else{
                            holder.binding.cvArticle2.setVisibility(View.GONE);
                        }
                    }
                    /*else {
                        if (quickReadList.size() > 2) {
                            holder.binding.tvArticle3.setText(quickReadList.get(i).getArticleName());
                            holder.binding.ivShare3.setOnClickListener(view -> {
                                getAllUserCommunities(quickReadList.get(index).getArticleID());
                            });
                            Glide.with(context)
                                    .load(quickReadList.get(position).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg2);
                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                Intent intent = new Intent(context, QuickReadActivity.class);
                                intent.putExtra("article_code", quickReadList.get(index).getArticleCode());

                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                        }
                    }*/

                    /*if (quickReadList.size() < 1) {
                        holder.binding.rlArticle2.setVisibility(View.GONE);
                    } else if (quickReadList.size() < 2) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                    }*/


//                    if (i == position) {
//                        holder.binding.tvArticle1.setText(quickReadList.get(position).getArticleName());
//                    }
//                    Glide.with(context)
//                            .load(quickReadList.get(position).getImgPath())
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
                            holder.binding.cvArticle1.setOnClickListener(view -> intentCode(index));
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(quickReadList.get(index).getArticleCode(), quickReadList.get(index).getIsBookMarked() != 1));
                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                                
                            }
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
                            holder.binding.cvArticle2.setOnClickListener(view -> intentCode(index));
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(quickReadList.get(index).getArticleCode(), quickReadList.get(index).getIsBookMarked() != 1));
                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark2.setVisibility(View.GONE);
                                
                            }
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
                        if (quickReadList.size() > 4) {
                            holder.binding.tvArticle1.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.cvArticle1.setOnClickListener(view -> intentCode(index));
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(quickReadList.get(index).getArticleCode(), quickReadList.get(index).getIsBookMarked() != 1));
                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                                
                            }
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
                            holder.binding.cvArticle2.setOnClickListener(view -> intentCode(index));
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(quickReadList.get(index).getArticleCode(), quickReadList.get(index).getIsBookMarked() != 1));
                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark2.setVisibility(View.GONE);
                                
                            }
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
                        if (quickReadList.size() > 6) {
                            holder.binding.tvArticle1.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.cvArticle1.setOnClickListener(view -> intentCode(index));
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(quickReadList.get(index).getArticleCode(), quickReadList.get(index).getIsBookMarked() != 1));
                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                                
                            }
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
                            holder.binding.cvArticle2.setOnClickListener(view -> intentCode(index));
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(quickReadList.get(index).getArticleCode(), quickReadList.get(index).getIsBookMarked() != 1));

                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark2.setVisibility(View.GONE);
                                
                            }
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
                        if (quickReadList.size() > 8) {
                            holder.binding.tvArticle1.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg);
                            holder.binding.cvArticle1.setOnClickListener(view -> intentCode(index));
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(quickReadList.get(index).getArticleCode(), quickReadList.get(index).getIsBookMarked() != 1));
                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                                
                            }
                        }
                    } else if (i == 9) {
                        if (quickReadList.size() > 9) {
                            holder.binding.tvArticle2.setText(quickReadList.get(i).getArticleName());
                            Glide.with(context)
                                    .load(quickReadList.get(i).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.icArticleImg1);
                            holder.binding.cvArticle2.setOnClickListener(view -> intentCode(index));
                            if(quickReadList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(quickReadList.get(index).getArticleCode(), quickReadList.get(index).getIsBookMarked() != 1));
                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark2.setVisibility(View.GONE);
                                
                            }
                        }else{
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

    private void intentCode(int index){
        try{
            Intent intent = new Intent(context, QuickReadActivity.class);
            intent.putExtra("article_code", quickReadList.get(index).getArticleCode());
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    @Override
    public int getItemCount() {
        if (quickReadList.size() > 2) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(quickReadList.size())) / 2.0);
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


}
