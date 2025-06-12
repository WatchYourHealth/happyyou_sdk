package com.wyh.happyyousdk.absorb.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.YT_THUMBNAIL_URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.dashboard.SearchActivity;
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.absorb.ShareBlogRequest;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.databinding.AbsorbHealthTvAdapterBinding;
import com.wyh.happyyousdk.ice.YoutubeActivity;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthHacksHealthTvAdapter extends RecyclerView.Adapter<HealthHacksHealthTvAdapter.MyViewHolder> {

    Context context;
    List<GetDashboardDataResponse.Data.HealthTv> healthTvList;

    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;
    String comingFrom = "HealthHacksActivity";

    private final OnItemClickListener listener;
    public HealthHacksHealthTvAdapter(Context context, List<GetDashboardDataResponse.Data.HealthTv> healthTvList, OnItemClickListener listener) {
        this.context = context;
        this.healthTvList = healthTvList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AbsorbHealthTvAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.absorb_health_tv_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                for (int i = 0; i < 2; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (healthTvList.size() > 0) {
                            holder.binding.tvVideo1.setText(healthTvList.get(i).getTitle());

                            String videoId = getVideoUrl(healthTvList.get(i).getLink());
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

                            String imgUrl = null;
                            if (videoId != null) {
                                imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
                            }

                            if(healthTvList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }

                            holder.binding.ivBookmark1.setOnClickListener(view -> {
                                listener.onClick(healthTvList.get(0).getId(), healthTvList.get(0).getIsBookMarked() != 1);
                            });

                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstVideo.setOnClickListener(view -> {
                                intentMethod(index);
                            });


                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                            }

                        }
                    } else if (i == 1) {
                        if (healthTvList.size() > 1) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            String videoId = getVideoUrl(healthTvList.get(i).getLink());
                            String imgUrl = null;
                            if (videoId != null) {
                                imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
                            }

                            if(healthTvList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> {
                                listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1);
                            });

                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondVideo.setOnClickListener(view -> {
                                intentMethod(index);
                            });

                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark2.setVisibility(View.GONE);
                            }
                        }
                    }

                    if (healthTvList.size() < 2) {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                    } else if (healthTvList.size() < 3) {
                        holder.binding.cvSecondVideo.setVisibility(View.VISIBLE);
                    }


                }
                break;
            case 1:
                for (int i = 2; i < 4; i++) {
                    final int index = i;

                    if (i == 2) {
                        if (healthTvList.size() > 2) {
                            holder.binding.tvVideo1.setText(healthTvList.get(i).getTitle());

                            if(healthTvList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> {
                                listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1);
                            });

                            String videoId = getVideoUrl(healthTvList.get(i).getLink());
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

                            String imgUrl = null;
                            if (videoId != null) {
                                imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
                            }
                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstVideo.setOnClickListener(view -> {
                                intentMethod(index);
                            });

                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                                
                            }
                        }
                    } else if (i == 3) {
                        if (healthTvList.size() > 3) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            if(healthTvList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> {
                                listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1);
                            });

                            String videoId = getVideoUrl(healthTvList.get(i).getLink());
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

                            String imgUrl = null;
                            if (videoId != null) {
                                imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
                            }
                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondVideo.setOnClickListener(view -> {
                                intentMethod(index);
                            });

                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark2.setVisibility(View.GONE);
                                
                            }
                        }
                    } else {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                        holder.binding.cvFirstVideo.setVisibility(View.GONE);

                    }

                    if (healthTvList.size() < 4) {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                    } else if (healthTvList.size() < 5) {
                        holder.binding.cvSecondVideo.setVisibility(View.VISIBLE);
                    }

                    if (healthTvList.size() < 1) {
                        holder.binding.tvVideo1.setVisibility(View.GONE);
//                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    } /*else if (healthTvList.size() < 2) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }*/

                    /*if (healthTvList.size() < 3) {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                    } else if (healthTvList.size() < 4) {
                        holder.binding.cvSecondVideo.setVisibility(View.VISIBLE);
                    }*/

                }
                break;
            case 2:
                for (int i = 4; i < 6; i++) {
                    final int index = i;

                    if (i == 4) {
                        if (healthTvList.size() > 4) {
                            holder.binding.tvVideo1.setText(healthTvList.get(i).getTitle());
                            if(healthTvList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }

                            holder.binding.ivBookmark1.setOnClickListener(view -> {
                                listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1);
                            });
                            String videoId = getVideoUrl(healthTvList.get(i).getLink());
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

                            String imgUrl = null;
                            if (videoId != null) {
                                imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
                            }
                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstVideo.setOnClickListener(view -> {
                                intentMethod(index);
                            });

                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                                
                            }
                        }
                    } else if (i == 5) {
                        if (healthTvList.size() > 5) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            if(healthTvList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> {
                                listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1);
                            });
                            String videoId = getVideoUrl(healthTvList.get(i).getLink());
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

                            String imgUrl = null;
                            if (videoId != null) {
                                imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
                            }
                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondVideo.setOnClickListener(view -> {
                                intentMethod(index);
                            });

                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark2.setVisibility(View.GONE);
                                
                            }
                        }
                    } else {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                        holder.binding.cvFirstVideo.setVisibility(View.GONE);

                    }

                    if (healthTvList.size() < 6) {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                    } else if (healthTvList.size() < 7) {
                        holder.binding.cvSecondVideo.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case 3:
                for (int i = 6; i < 8; i++) {
                    final int index = i;

                    if (i == 6) {
                        if (healthTvList.size() > 6) {
                            holder.binding.tvVideo1.setText(healthTvList.get(i).getTitle());
                            if(healthTvList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> {
                                listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1);
                            });
                            String videoId = getVideoUrl(healthTvList.get(i).getLink());
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

                            String imgUrl = null;
                            if (videoId != null) {
                                imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
                            }
                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstVideo.setOnClickListener(view -> {
                                intentMethod(index);
                            });

                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                                
                            }
                        }
                    } else if (i == 7) {
                        if (healthTvList.size() > 7) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            if(healthTvList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> {
                                listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1);
                            });
                            String videoId = getVideoUrl(healthTvList.get(i).getLink());
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

                            String imgUrl = null;
                            if (videoId != null) {
                                imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
                            }
                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondVideo.setOnClickListener(view -> {
                                intentMethod(index);
                            });

                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark2.setVisibility(View.GONE);
                                
                            }
                        }
                    } else {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                        holder.binding.cvFirstVideo.setVisibility(View.GONE);

                    }

                    if (healthTvList.size() < 8) {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                    } else if (healthTvList.size() < 9) {
                        holder.binding.cvSecondVideo.setVisibility(View.VISIBLE);
                    }

                }

                break;
            case 4:
                for (int i = 8; i < 10; i++) {
                    final int index = i;

                    if (i == 8) {
                        if (healthTvList.size() > 8) {
                            holder.binding.tvVideo1.setText(healthTvList.get(i).getTitle());
                            if(healthTvList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> {
                                listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1);
                            });
                            String videoId = getVideoUrl(healthTvList.get(i).getLink());
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

                            String imgUrl = null;
                            if (videoId != null) {
                                imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
                            }
                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstVideo.setOnClickListener(view -> {
                                intentMethod(index);
                            });
                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                                
                            }
                        }
                    } else if (i == 9) {
                        if (healthTvList.size() > 9) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            if(healthTvList.get(i).getIsBookMarked() == 1){
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            }else{
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> {
                                listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1);
                            });
                            String videoId = getVideoUrl(healthTvList.get(i).getLink());
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

                            String imgUrl = null;
                            if (videoId != null) {
                                imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
                            }
                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondVideo.setOnClickListener(view -> {
                                intentMethod(index);
                            });

                            if(context instanceof SearchActivity){
                                holder.binding.ivBookmark2.setVisibility(View.GONE);
                                
                            }
                        }
                    } else {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                        holder.binding.cvFirstVideo.setVisibility(View.GONE);

                    }

                   /* if (healthTvList.size() < 1) {
                        holder.binding.tvVideo1.setVisibility(View.GONE);
//                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }*/ /*else if (healthTvList.size() < 2) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }*/

                }
                break;
        }
    }

    public interface ClickInterface {
        void onItemClicked(int index, String tag);
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
        if(url != null){
            if (url.contains("=")) {
                String segments[] = url.split("=");
                // Grab the last segment
                videoIds = segments[segments.length - 1];
            }
        }

        return videoIds;
    }




    @Override
    public int getItemCount() {

        if(healthTvList.size() > 6){
            return (int) Math.ceil(6.0 / 2.0);
        }else{
            return  (int) Math.ceil(Double.parseDouble(String.valueOf(healthTvList.size())) / 2.0);
        }
        /*if (healthTvList.size() > 1) {
            return healthTvList.size() / 2;
        } else {
            return 1;
        }*/
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AbsorbHealthTvAdapterBinding binding;

        public MyViewHolder(@NonNull AbsorbHealthTvAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void intentMethod(int index){
        try{
            Intent intent = new Intent(context, YoutubeActivity.class);
            intent.putExtra("videoId", healthTvList.get(index).getLink().split("=")[1]);
            intent.putExtra("tab", "HOME_TAB");
            intent.putExtra("section", "RECORDED_VIDEO");
            intent.putExtra("cameFrom", "rewards");
            intent.putExtra("url", healthTvList.get(index).getPath());
            intent.putExtra("desc", healthTvList.get(index).getDescription());
            intent.putExtra("product_url", healthTvList.get(index).getTitle());
            context.startActivity(intent);

            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (context instanceof HealthHacksActivity)
//                ((HealthHacksActivity) context).startActivityForResult(intent, 1212);
//            else
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void shareWhatsApp(String link, String title){

        /*try {
            URL url = new URL(image);
            Bitmap bitmapThought = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            File cacheDir = new File(context.getFilesDir(), "MyAppCache");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            final File[] file = new File[1];
            file[0] = new File(cacheDir, "HealthHackTv" + ".png");
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    Bitmap bitmap = bitmapThought;
                    FileOutputStream fOut = new FileOutputStream(file[0]);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    file[0].setReadable(true, false);
                    Uri healthHackURi = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file[0]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/



    }

    private void shareBitmap(String message) {
        try {
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetData(List<GetDashboardDataResponse.Data.HealthTv> newData){
        healthTvList.clear();
        healthTvList = newData;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(int id, boolean isBookmark);
    }
}
