package com.wyh.happyyousdk.absorb.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.YT_THUMBNAIL_URL;

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
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.absorb.HealthTvListingActivity;
import com.wyh.happyyousdk.dashboard.SearchActivity;
import com.wyh.happyyousdk.databinding.AbsorbHealthTvAdapterBinding;
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding;
import com.wyh.happyyousdk.ice.YoutubeActivity;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.absorb.ShareBlogRequest;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
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

public class AllVideosAdapter extends RecyclerView.Adapter<AllVideosAdapter.MyViewHolder> {

    Context context;
    List<GetDashboardDataResponse.Data.HealthTv> healthTvList;

    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;

    private final HealthHacksHealthTvAdapter.OnItemClickListener listener;

    public AllVideosAdapter(Context context, List<GetDashboardDataResponse.Data.HealthTv> healthTvList, HealthHacksHealthTvAdapter.OnItemClickListener listener) {
        this.context = context;
        this.healthTvList = healthTvList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AllVideosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AbsorbHealthTvAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.absorb_health_tv_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllVideosAdapter.MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                for (int i = 0; i < 2; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (!healthTvList.isEmpty()) {
                            holder.binding.tvVideo1.setText(healthTvList.get(i).getTitle());

                            String videoId = getVideoUrl(healthTvList.get(i).getLink());
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

                            String imgUrl = null;
                            if (videoId != null) {
                                imgUrl = YT_THUMBNAIL_URL.replace("###", videoId);
                            }

                            if (healthTvList.get(i).getIsBookMarked() == 1) {
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            } else {
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }

                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(healthTvList.get(0).getId(), healthTvList.get(0).getIsBookMarked() != 1));

                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstVideo.setOnClickListener(view -> intentMethod(index));


                            if (context instanceof SearchActivity) {
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

                            if (healthTvList.get(i).getIsBookMarked() == 1) {
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            } else {
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1));

                            Glide.with(context)
                                    .load(imgUrl)
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondVideo.setOnClickListener(view -> intentMethod(index));

                            if (context instanceof SearchActivity) {
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

                            if (healthTvList.get(i).getIsBookMarked() == 1) {
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            } else {
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1));

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
                            holder.binding.cvFirstVideo.setOnClickListener(view -> intentMethod(index));

                            if (context instanceof SearchActivity) {
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                            }
                        }
                    } else if (i == 3) {
                        if (healthTvList.size() > 3) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            if (healthTvList.get(i).getIsBookMarked() == 1) {
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            } else {
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1));

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
                            holder.binding.cvSecondVideo.setOnClickListener(view -> intentMethod(index));

                            if (context instanceof SearchActivity) {
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

                    if (healthTvList.isEmpty()) {
                        holder.binding.tvVideo1.setVisibility(View.GONE);
//                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }
                }
                break;
            case 2:
                for (int i = 4; i < 6; i++) {
                    final int index = i;

                    if (i == 4) {
                        if (healthTvList.size() > 4) {
                            holder.binding.tvVideo1.setText(healthTvList.get(i).getTitle());
                            if (healthTvList.get(i).getIsBookMarked() == 1) {
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            } else {
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }

                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1));
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
                            holder.binding.cvFirstVideo.setOnClickListener(view -> intentMethod(index));

                            if (context instanceof SearchActivity) {
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                            }
                        }
                    } else if (i == 5) {
                        if (healthTvList.size() > 5) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            if (healthTvList.get(i).getIsBookMarked() == 1) {
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            } else {
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1));
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
                            holder.binding.cvSecondVideo.setOnClickListener(view -> intentMethod(index));

                            if (context instanceof SearchActivity) {
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
                            if (healthTvList.get(i).getIsBookMarked() == 1) {
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            } else {
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1));
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
                            holder.binding.cvFirstVideo.setOnClickListener(view -> intentMethod(index));

                            if (context instanceof SearchActivity) {
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                            }
                        }
                    } else if (i == 7) {
                        if (healthTvList.size() > 7) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            if (healthTvList.get(i).getIsBookMarked() == 1) {
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            } else {
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1));
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
                            holder.binding.cvSecondVideo.setOnClickListener(view -> intentMethod(index));

                            if (context instanceof SearchActivity) {
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
                            if (healthTvList.get(i).getIsBookMarked() == 1) {
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            } else {
                                holder.binding.ivBookmark1.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark1.setOnClickListener(view -> listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1));
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
                            holder.binding.cvFirstVideo.setOnClickListener(view -> intentMethod(index));
                            if (context instanceof SearchActivity) {
                                holder.binding.ivBookmark1.setVisibility(View.GONE);
                            }
                        }
                    } else if (i == 9) {
                        if (healthTvList.size() > 9) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            if (healthTvList.get(i).getIsBookMarked() == 1) {
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                            } else {
                                holder.binding.ivBookmark2.setBackgroundResource(R.drawable.ic_baseline_bookmark_border_24);
                            }
                            holder.binding.ivBookmark2.setOnClickListener(view -> listener.onClick(healthTvList.get(index).getId(), healthTvList.get(index).getIsBookMarked() != 1));
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
                            holder.binding.cvSecondVideo.setOnClickListener(view -> intentMethod(index));

                            if (context instanceof SearchActivity) {
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

    @Override
    public int getItemCount() {
        if (healthTvList.size() > 6) {
            return (int) Math.ceil(6.0 / 2.0);
        } else {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(healthTvList.size())) / 2.0);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        AbsorbHealthTvAdapterBinding binding;

        public MyViewHolder(@NonNull AbsorbHealthTvAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String getVideoUrl(String url) {
        String videoIds = null;
        if (url.contains("=")) {
            String[] segments = url.split("=");
            // Grab the last segment
            videoIds = segments[segments.length - 1];
        }
        return videoIds;
    }

    private void intentMethod(int index) {
        try {
            Intent intent = new Intent(context, YoutubeActivity.class);
            intent.putExtra("videoId", healthTvList.get(index).getLink().split("=")[1]);
            intent.putExtra("Id", String.valueOf(healthTvList.get(index).getId()));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnItemClickListener {
        void onClick(int id, boolean isBookmark);
    }
}
