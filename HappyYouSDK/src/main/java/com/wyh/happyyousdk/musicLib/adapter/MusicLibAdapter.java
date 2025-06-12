package com.wyh.happyyousdk.musicLib.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.MusicLibAdapterItemBinding;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.musicLib.MusicLibPlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class MusicLibAdapter extends RecyclerView.Adapter<MusicLibAdapter.MyViewHolder> {
    Context context;
    List<GetDashboardDataResponse.Data.AudioFiles> healthTvList;

    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;
    public MusicLibAdapter(Context context, List<GetDashboardDataResponse.Data.AudioFiles> healthTvList) {
        this.context = context;
        this.healthTvList = healthTvList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MusicLibAdapterItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.music_lib_adapter_item, parent, false);
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
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);


                            Glide.with(context)
                                    .load(healthTvList.get(0).getThumbnailImage())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);

                            holder.binding.cvFirstMusic.setOnClickListener(view -> {
                                Intent intent = new Intent(context, MusicLibPlayerActivity.class);
                                intent.putExtra("musicData", new Gson().toJson(healthTvList.get(0)));
                                context.startActivity(intent);
                            });

                        }
                    } else if (i == 1) {
                        if (healthTvList.size() > 1) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            //Log.d("Video", recordedData.getVideoUrl() + "," + videoId);

                            Glide.with(context)
                                    .load(healthTvList.get(1).getThumbnailImage())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondMusic.setOnClickListener(view -> {
                                Intent intent = new Intent(context, MusicLibPlayerActivity.class);
                                intent.putExtra("musicData", new Gson().toJson(healthTvList.get(1)));
                                context.startActivity(intent);
                            });
                        }
                    }

                    if (healthTvList.size() < 2) {
                        holder.binding.cvSecondMusic.setVisibility(View.GONE);
                    } else if (healthTvList.size() < 3) {
                        holder.binding.cvSecondMusic.setVisibility(View.VISIBLE);
                    }


                }
                break;
            case 1:
                for (int i = 2; i < 4; i++) {
                    final int index = i;

                    if (i == 2) {
                        if (healthTvList.size() > 2) {
                            holder.binding.tvVideo1.setText(healthTvList.get(i).getTitle());
                            Glide.with(context)
                                    .load(healthTvList.get(2).getThumbnailImage())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstMusic.setOnClickListener(view -> {
                                Intent intent = new Intent(context, MusicLibPlayerActivity.class);
                                intent.putExtra("musicData", new Gson().toJson(healthTvList.get(2)));
                                context.startActivity(intent);
                            });
                        }
                    } else if (i == 3) {
                        if (healthTvList.size() > 3) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            Glide.with(context)
                                    .load(healthTvList.get(3).getThumbnailImage())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondMusic.setOnClickListener(view -> {
                                Intent intent = new Intent(context, MusicLibPlayerActivity.class);
                                intent.putExtra("musicData", new Gson().toJson(healthTvList.get(3)));
                                context.startActivity(intent);
                            });
                        }
                    } else {
                        holder.binding.cvSecondMusic.setVisibility(View.GONE);
                        holder.binding.cvFirstMusic.setVisibility(View.GONE);

                    }

                    if (healthTvList.size() < 4) {
                        holder.binding.cvSecondMusic.setVisibility(View.GONE);
                    } else if (healthTvList.size() < 5) {
                        holder.binding.cvSecondMusic.setVisibility(View.VISIBLE);
                    }

                    if (healthTvList.size() < 1) {
                        holder.binding.tvVideo1.setVisibility(View.GONE);
//                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    } /*else if (healthTvList.size() < 2) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }*/

                    /*if (healthTvList.size() < 3) {
                        holder.binding.cvSecondMusic.setVisibility(View.GONE);
                    } else if (healthTvList.size() < 4) {
                        holder.binding.cvSecondMusic.setVisibility(View.VISIBLE);
                    }*/

                }
                break;
            case 2:
                for (int i = 4; i < 6; i++) {
                    final int index = i;

                    if (i == 4) {
                        if (healthTvList.size() > 4) {
                            holder.binding.tvVideo1.setText(healthTvList.get(i).getTitle());
                            Glide.with(context)
                                    .load(healthTvList.get(4).getThumbnailImage())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstMusic.setOnClickListener(view -> {
                                Intent intent = new Intent(context, MusicLibPlayerActivity.class);
                                intent.putExtra("musicData", new Gson().toJson(healthTvList.get(4)));
                                context.startActivity(intent);
                            });
                        }
                    } else if (i == 5) {
                        if (healthTvList.size() > 5) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            Glide.with(context)
                                    .load(healthTvList.get(5).getThumbnailImage())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondMusic.setOnClickListener(view -> {
                                Intent intent = new Intent(context, MusicLibPlayerActivity.class);
                                intent.putExtra("musicData", new Gson().toJson(healthTvList.get(5)));
                                context.startActivity(intent);
                            });
                        }
                    } else {
                        holder.binding.cvSecondMusic.setVisibility(View.GONE);
                        holder.binding.cvFirstMusic.setVisibility(View.GONE);

                    }

                    if (healthTvList.size() < 6) {
                        holder.binding.cvSecondMusic.setVisibility(View.GONE);
                    } else if (healthTvList.size() < 7) {
                        holder.binding.cvSecondMusic.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case 3:
                for (int i = 6; i < 8; i++) {
                    final int index = i;

                    if (i == 6) {
                        if (healthTvList.size() > 6) {
                            holder.binding.tvVideo1.setText(healthTvList.get(i).getTitle());
                            Glide.with(context)
                                    .load(healthTvList.get(6).getThumbnailImage())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstMusic.setOnClickListener(view -> {
                                Intent intent = new Intent(context, MusicLibPlayerActivity.class);
                                intent.putExtra("musicData", new Gson().toJson(healthTvList.get(6)));
                                context.startActivity(intent);
                            });
                        }
                    } else if (i == 7) {
                        if (healthTvList.size() > 7) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            Glide.with(context)
                                    .load(healthTvList.get(7).getThumbnailImage())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondMusic.setOnClickListener(view -> {
                                Intent intent = new Intent(context, MusicLibPlayerActivity.class);
                                intent.putExtra("musicData", new Gson().toJson(healthTvList.get(7)));
                                context.startActivity(intent);
                            });
                        }
                    } else {
                        holder.binding.cvSecondMusic.setVisibility(View.GONE);
                        holder.binding.cvFirstMusic.setVisibility(View.GONE);

                    }

                    if (healthTvList.size() < 8) {
                        holder.binding.cvSecondMusic.setVisibility(View.GONE);
                    } else if (healthTvList.size() < 9) {
                        holder.binding.cvSecondMusic.setVisibility(View.VISIBLE);
                    }

                }

                break;
            case 4:
                for (int i = 8; i < 10; i++) {
                    final int index = i;

                    if (i == 8) {
                        if (healthTvList.size() > 8) {
                            holder.binding.tvVideo1.setText(healthTvList.get(i).getTitle());
                            Glide.with(context)
                                    .load(healthTvList.get(8).getThumbnailImage())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstMusic.setOnClickListener(view -> {
                                Intent intent = new Intent(context, MusicLibPlayerActivity.class);
                                intent.putExtra("musicData", new Gson().toJson(healthTvList.get(8)));
                                context.startActivity(intent);
                            });
                        }
                    } else if (i == 9) {
                        if (healthTvList.size() > 9) {
                            holder.binding.tvVideo2.setText(healthTvList.get(i).getTitle());
                            Glide.with(context)
                                    .load(healthTvList.get(9).getThumbnailImage())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondMusic.setOnClickListener(view -> {
                                Intent intent = new Intent(context, MusicLibPlayerActivity.class);
                                intent.putExtra("musicData", new Gson().toJson(healthTvList.get(9)));
                                context.startActivity(intent);
                            });
                        }
                    } else {
                        holder.binding.cvSecondMusic.setVisibility(View.GONE);
                        holder.binding.cvFirstMusic.setVisibility(View.GONE);

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
        if(healthTvList.size() > 6){
            return (int) Math.ceil(6.0 / 2.0);
        }else{
            return  (int) Math.ceil(Double.parseDouble(String.valueOf(healthTvList.size())) / 2.0);
        }
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MusicLibAdapterItemBinding binding;

        public MyViewHolder(@NonNull MusicLibAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
