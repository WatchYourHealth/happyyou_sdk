package com.wyh.happyyousdk.absorb.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.absorb.MoreQuickReadActivity;
import com.wyh.happyyousdk.databinding.MoreQuickReadBinding;
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.absorb.ShareBlogRequest;
import com.wyh.happyyousdk.model.response.absorb.GetQuickReadResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreQuickReadAdapter extends RecyclerView.Adapter<MoreQuickReadAdapter.MyViewHolder> {

    Context context;
    List<GetQuickReadResponse.Data.TrendingBlog> trendingBlogList;
    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;
    String title;
    private final MoreQuickReadAdapter.OnItemClickListener listener;

    public MoreQuickReadAdapter(Context context, List<GetQuickReadResponse.Data.TrendingBlog> trendingBlogList, String title,MoreQuickReadAdapter.OnItemClickListener listener) {
        this.context = context;
        this.trendingBlogList = trendingBlogList;
        this.title = title;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoreQuickReadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MoreQuickReadBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.more_quick_read, parent, false);
//        screenWidth = displayMetrics.widthPixels
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreQuickReadAdapter.MyViewHolder holder, int position) {
        holder.binding.tvArticle1.setText(trendingBlogList.get(position).getArticleName());
        Glide.with(context)
                .load(trendingBlogList.get(position).getImgPath())
                .error(R.drawable.dummy_image)
                .placeholder(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.icArticleImg);

        holder.binding.cvArticle1.setOnClickListener(view -> listener.onClickItem(position));

        if(Objects.equals(title, "Bookmarks")){
            holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
        }else{
            if(trendingBlogList.get(position).getIsBookMarked() == 1){
                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
            }else{
                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
            }
        }

        holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(trendingBlogList.get(position).getArticleCode(), trendingBlogList.get(position).getIsBookMarked() != 1, position));
    }


    @Override
    public int getItemCount() {
        return trendingBlogList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MoreQuickReadBinding binding;

        public MyViewHolder(@NonNull MoreQuickReadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onClick(String articleCode, boolean isBookmark, int position);
        void onClickItem(int position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void resetData(List<GetQuickReadResponse.Data.TrendingBlog> dataList){
        trendingBlogList = dataList;
        notifyDataSetChanged();
    }

}

