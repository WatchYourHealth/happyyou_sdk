package com.wyh.happyyousdk.absorb.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.YT_THUMBNAIL_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.wyh.happyyousdk.absorb.HealthTVDashboard;
import com.wyh.happyyousdk.absorb.RecommandHealthTvDashboradActivity;
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.absorb.ShareBlogRequest;
import com.wyh.happyyousdk.model.request.absorb.VideoBookmarkRequest;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.databinding.HealthTvAdapterBinding;
import com.wyh.happyyousdk.ice.YoutubeActivity;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthTvAdapter extends RecyclerView.Adapter<HealthTvAdapter.MyViewHolder> {

    Context context;
    List<GetDashboardDataResponse.Data.HealthTv> healthTvList;

    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;
    boolean isBookmarkcameFrom;

    public HealthTvAdapter(Context context, List<GetDashboardDataResponse.Data.HealthTv> healthTvList, boolean isBookmarkcameFrom) {
        this.context = context;
        this.healthTvList = healthTvList;
        this.isBookmarkcameFrom = isBookmarkcameFrom;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HealthTvAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.health_tv_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.tvDesc1.setText(healthTvList.get(position).getTitle());
        String videoId = getYoutubeId(healthTvList.get(position).getLink());
        //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

        String imgUrl = null;
        if (videoId != null) {
            imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
        }

        Log.d("Video ID", healthTvList.get(position).getLink());
        Log.d("Video", healthTvList.get(position).getTitle() +","+ videoId + "--->" + imgUrl);
        Glide.with(context)
                .load(imgUrl)
                .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.binding.ivThumbnail);
        holder.binding.cvItem.setOnClickListener(view -> {
            Log.d("vLink", healthTvList.get(position).getLink());
            String link;
            if(healthTvList.get(position).getLink().contains("=")){
                link = healthTvList.get(position).getLink().split("=")[1];
            }else{
                link = healthTvList.get(position).getLink();
            }
            Log.d("vLink", link);
            Intent intent = new Intent(context, YoutubeActivity.class);
            intent.putExtra("videoId", videoId);
            intent.putExtra("tab", "HOME_TAB");
            intent.putExtra("section", "RECORDED_VIDEO");
            intent.putExtra("cameFrom", "rewards");
            intent.putExtra("url", healthTvList.get(position).getPath());
            intent.putExtra("desc", healthTvList.get(position).getDescription());
            intent.putExtra("product_url", healthTvList.get(position).getTitle());
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (context instanceof HealthTVDashboard)
                ((HealthTVDashboard) context).startActivityForResult(intent, 1212);
            else
                ((RecommandHealthTvDashboradActivity) context).startActivityForResult(intent, 1212);

        });

        if(healthTvList.get(position).getIsBookMarked() == 1){
            holder.binding.ivBookmark.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
        }else{
            holder.binding.ivBookmark.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
        }

        holder.binding.ivBookmark.setOnClickListener(view -> addVideoBookmark(healthTvList.get(position).getIsBookMarked() != 1, healthTvList.get(position)));
    }

    private String getVideoUrl(String url) {
        String videoIds = null;
        /*String[] urls = url.split("/");
        if (!urls[urls.length - 1].contains("watch")) {
            videoIds = urls[urls.length - 1];
        } else {
            Pattern pattern = Pattern.compile("=(.*)&");
            Matcher matcher = pattern.matcher(urls[urls.length - 1]);
            if (matcher.find()) {
                videoIds = urls[urls.length - 1];
            }
            Pattern pattern1 = Pattern.compile("=(.*)");
            Matcher matcher1 = pattern1.matcher(urls[urls.length - 1]);
            if (matcher1.find()) {
                videoIds = urls[urls.length - 1];
            }
        }*/
        if (url.contains("=")) {
            String[] segments = url.split("=");
            // Grab the last segment
            videoIds = segments[segments.length - 1];
        }
        return videoIds;
    }

    public static String getYoutubeId(String url) {
        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return healthTvList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        HealthTvAdapterBinding binding;

        public MyViewHolder(@NonNull HealthTvAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void addVideoBookmark(boolean isBookmark, GetDashboardDataResponse.Data.HealthTv data) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

        if (!progressDialog.isShowing())
            progressDialog.show();
        VideoBookmarkRequest request = new VideoBookmarkRequest(data.getId(), "video" ,isBookmark);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.addBookmarkVideo(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_success));

                    Log.d("BookMark", new Gson().toJson(response.body()));
                    if (isBookmark) {
                        data.setIsBookMarked(1);
                        Toast.makeText(context, "Successfully added to Bookmark", Toast.LENGTH_SHORT).show();
                    } else {
                        data.setIsBookMarked(0);
                        Toast.makeText(context, "Remove from Bookmark", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_failed));
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }

                if (isBookmarkcameFrom) {
                    healthTvList.remove(data);
                }

                notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.health_hacks_add_bookmark_video_failed));
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
