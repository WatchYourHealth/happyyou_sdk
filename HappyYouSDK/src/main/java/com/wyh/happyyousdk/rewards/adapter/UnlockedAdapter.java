package com.wyh.happyyousdk.rewards.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.UnlockedAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.EandBUnlockedVoucher;

import java.util.List;

public class UnlockedAdapter extends RecyclerView.Adapter<UnlockedAdapter.MyViewHolder> {

    Context context;
    List<EandBUnlockedVoucher> unlockedVoucherList;
    ClickListenerInterface clickListenerInterface;

    public UnlockedAdapter(Context context, List<EandBUnlockedVoucher> unlockedVoucherList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.unlockedVoucherList = unlockedVoucherList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UnlockedAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.unlocked_adapter, parent, false);
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
                        if (unlockedVoucherList.size() > 0) {
                            if (unlockedVoucherList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead1.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle1.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle1.setText(unlockedVoucherList.get(i).getVoucherTitle());
                            Glide.with(context)
                                    .load(unlockedVoucherList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo1);

                            holder.binding.rlVoucher1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUnlockedVouchers(unlockedVoucherList.get(index));
                            });
                        }
                    } else if (i == 1) {
                        if (unlockedVoucherList.size() > 1) {
                            if (unlockedVoucherList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead2.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle2.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle2.setText(unlockedVoucherList.get(i).getVoucherTitle());
                            Glide.with(context)
                                    .load(unlockedVoucherList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo2);

                            holder.binding.rlVoucher2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUnlockedVouchers(unlockedVoucherList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher2.setVisibility(View.GONE);
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
                    } else {
                        if (unlockedVoucherList.size() > 2) {
                            if (unlockedVoucherList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead3.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle3.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle3.setText(unlockedVoucherList.get(i).getVoucherTitle());
                            Glide.with(context)
                                    .load(unlockedVoucherList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo3);

                            holder.binding.rlVoucher3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUnlockedVouchers(unlockedVoucherList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
                    }

                    if (unlockedVoucherList.size() < 2) {
                        holder.binding.rlVoucher2.setVisibility(View.GONE);
                        holder.binding.rlVoucher3.setVisibility(View.GONE);
                    } else if (unlockedVoucherList.size() < 3) {
                        holder.binding.rlVoucher2.setVisibility(View.VISIBLE);
                        holder.binding.rlVoucher3.setVisibility(View.GONE);
                    }
                }
                break;
            case 1:
                for (int i = 3; i < 6; i++) {
                    final int index = i;
                    if (i == 3) {
                        if (unlockedVoucherList.size() > 3) {
                            if (unlockedVoucherList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead1.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle1.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else {
                                holder.binding.tvArticle1.setText(unlockedVoucherList.get(i).getVoucherTitle());
                            }
                            Glide.with(context)
                                    .load(unlockedVoucherList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo2);
                            holder.binding.rlVoucher1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUnlockedVouchers(unlockedVoucherList.get(index));
                            });
                        }
                    } else if (i == 4) {
                        if (unlockedVoucherList.size() > 4) {
                            if (unlockedVoucherList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead2.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle2.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else {
                                holder.binding.tvArticle2.setText(unlockedVoucherList.get(i).getVoucherTitle());
                            }
                            Glide.with(context)
                                    .load(unlockedVoucherList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo2);
                            holder.binding.rlVoucher2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUnlockedVouchers(unlockedVoucherList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher2.setVisibility(View.GONE);
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
                    } else {
                        if (unlockedVoucherList.size() > 5) {
                            if (unlockedVoucherList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead3.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle3.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else {
                                holder.binding.tvArticle3.setText(unlockedVoucherList.get(i).getVoucherTitle());
                            }
                            Glide.with(context)
                                    .load(unlockedVoucherList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo2);
                            holder.binding.rlVoucher3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUnlockedVouchers(unlockedVoucherList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
                    }

                    if (unlockedVoucherList.size() < 4) {
                        holder.binding.rlVoucher2.setVisibility(View.GONE);
                        holder.binding.rlVoucher3.setVisibility(View.GONE);
                    } else if (unlockedVoucherList.size() < 5) {
                        holder.binding.rlVoucher2.setVisibility(View.VISIBLE);
                        holder.binding.rlVoucher3.setVisibility(View.GONE);
                    }
                }
                break;
            case 2:
                for (int i = 6; i < 9; i++) {
                    final int index = i;
                    if (i == 6) {
                        if (unlockedVoucherList.size() > 6) {
                            if (unlockedVoucherList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead1.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle1.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else {
                                holder.binding.tvArticle1.setText(unlockedVoucherList.get(i).getVoucherTitle());
                            }
                            Glide.with(context)
                                    .load(unlockedVoucherList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo2);
                            holder.binding.rlVoucher1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUnlockedVouchers(unlockedVoucherList.get(index));
                            });
                        }
                    } else if (i == 7) {
                        if (unlockedVoucherList.size() > 7) {
                            if (unlockedVoucherList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead2.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle2.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else {
                                holder.binding.tvArticle2.setText(unlockedVoucherList.get(i).getVoucherTitle());
                            }
                            Glide.with(context)
                                    .load(unlockedVoucherList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo2);
                            holder.binding.rlVoucher2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUnlockedVouchers(unlockedVoucherList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher2.setVisibility(View.GONE);
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
                    } else {
                        if (unlockedVoucherList.size() > 8) {
                            if (unlockedVoucherList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead3.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle3.setText(unlockedVoucherList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle3.setText(unlockedVoucherList.get(i).getVoucherTitle());
                            Glide.with(context)
                                    .load(unlockedVoucherList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo2);
                            holder.binding.rlVoucher3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickUnlockedVouchers(unlockedVoucherList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
                    }
                    if (unlockedVoucherList.size() < 7) {
                        holder.binding.rlVoucher2.setVisibility(View.GONE);
                        holder.binding.rlVoucher3.setVisibility(View.GONE);
                    } else if (unlockedVoucherList.size() < 8) {
                        holder.binding.rlVoucher2.setVisibility(View.VISIBLE);
                        holder.binding.rlVoucher3.setVisibility(View.GONE);
                    }
                }
                break;
            case 3:
                holder.binding.rlVoucher1.setVisibility(View.GONE);
                holder.binding.rlVoucher2.setVisibility(View.GONE);
                holder.binding.rlVoucher3.setVisibility(View.GONE);
                break;
        }

    }

    public interface ClickListenerInterface {
        void onItemClickUnlockedVouchers(EandBUnlockedVoucher eandBUnlockedVoucher);
    }

    @Override
    public int getItemCount() {
        if (unlockedVoucherList.size() > 1) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(unlockedVoucherList.size())) / 3.0);

        } else {
            return 1;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        UnlockedAdapterBinding binding;

        public MyViewHolder(@NonNull UnlockedAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
