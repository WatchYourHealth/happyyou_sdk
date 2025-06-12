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
import com.wyh.happyyousdk.databinding.GrabOpportunitiesAdapterBinding;
import com.wyh.happyyousdk.model.response.rewards.EandBAvailableVoucher;

import java.util.List;

public class GrabOpportunitiesAdapter extends RecyclerView.Adapter<GrabOpportunitiesAdapter.MyViewHolder> {

    Context context;
    List<EandBAvailableVoucher> grabOpportunitiesList;
    ClickListenerInterface clickListenerInterface;

    public GrabOpportunitiesAdapter(Context context, List<EandBAvailableVoucher> grabOpportunitiesList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.grabOpportunitiesList = grabOpportunitiesList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GrabOpportunitiesAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.grab_opportunities_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        switch (position) {
            case 0:
                for (int i = 0; i < 3; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (grabOpportunitiesList.size() > 0) {
                            if (grabOpportunitiesList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle());
                            holder.binding.tvTokens1.setText("Buy for " + grabOpportunitiesList.get(i).getTokens());
                            Glide.with(context)
                                    .load(grabOpportunitiesList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo1);

                            holder.binding.rlVoucher1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickNewVouchers(grabOpportunitiesList.get(index));
                            });
                        }
                    } else if (i == 1) {
                        if (grabOpportunitiesList.size() > 1) {
                            if (grabOpportunitiesList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead2.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle2.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle2.setText(grabOpportunitiesList.get(i).getVoucherTitle());
                            holder.binding.tvTokens2.setText("Buy for " + grabOpportunitiesList.get(i).getTokens());
                            Glide.with(context)
                                    .load(grabOpportunitiesList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo2);

                            holder.binding.rlVoucher2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickNewVouchers(grabOpportunitiesList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher2.setVisibility(View.GONE);
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
                    } else {
                        if (grabOpportunitiesList.size() > 2) {
                            if (grabOpportunitiesList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead3.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle3.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle3.setText(grabOpportunitiesList.get(i).getVoucherTitle());
                            holder.binding.tvTokens3.setText("Buy for " + grabOpportunitiesList.get(i).getTokens());
                            Glide.with(context)
                                    .load(grabOpportunitiesList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo3);

                            holder.binding.rlVoucher3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickNewVouchers(grabOpportunitiesList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
                    }

                    if (grabOpportunitiesList.size() < 2) {
                        holder.binding.rlVoucher2.setVisibility(View.GONE);
                        holder.binding.rlVoucher3.setVisibility(View.GONE);
                    } else if (grabOpportunitiesList.size() < 3) {
                        holder.binding.rlVoucher2.setVisibility(View.VISIBLE);
                        holder.binding.rlVoucher3.setVisibility(View.GONE);
                    }
                }
                break;
            case 1:
                for (int i = 3; i < 6; i++) {
                    final int index = i;
                    if (i == 3) {
                        if (grabOpportunitiesList.size() > 3) {
                            if (grabOpportunitiesList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle());
                            holder.binding.tvTokens1.setText("Buy for " + grabOpportunitiesList.get(i).getTokens());
                            Glide.with(context)
                                    .load(grabOpportunitiesList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo1);

                            holder.binding.rlVoucher1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickNewVouchers(grabOpportunitiesList.get(index));
                            });
                        }
                    } else if (i == 4) {
                        if (grabOpportunitiesList.size() > 4) {
                            if (grabOpportunitiesList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle());
                            holder.binding.tvTokens1.setText("Buy for " + grabOpportunitiesList.get(i).getTokens());
                            Glide.with(context)
                                    .load(grabOpportunitiesList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo1);

                            holder.binding.rlVoucher1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickNewVouchers(grabOpportunitiesList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher2.setVisibility(View.GONE);
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
                    } else {
                        if (grabOpportunitiesList.size() > 5) {
                            if (grabOpportunitiesList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle());
                            holder.binding.tvTokens1.setText("Buy for " + grabOpportunitiesList.get(i).getTokens());
                            Glide.with(context)
                                    .load(grabOpportunitiesList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo1);

                            holder.binding.rlVoucher1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickNewVouchers(grabOpportunitiesList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case 2:
                for (int i = 6; i < 9; i++) {
                    final int index = i;
                    if (i == 6) {
                        if (grabOpportunitiesList.size() > 6) {
                            if (grabOpportunitiesList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle());
                            holder.binding.tvTokens1.setText("Buy for " + grabOpportunitiesList.get(i).getTokens());
                            Glide.with(context)
                                    .load(grabOpportunitiesList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo1);

                            holder.binding.rlVoucher1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickNewVouchers(grabOpportunitiesList.get(index));
                            });
                        } {
                            if (grabOpportunitiesList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle());
                            Glide.with(context)
                                    .load(grabOpportunitiesList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo2);
                            holder.binding.tvTokens1.setText("Buy for " + grabOpportunitiesList.get(i).getTokens());
                            holder.binding.rlVoucher1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickNewVouchers(grabOpportunitiesList.get(index));
                            });
                        }
                    } else if (i == 7) {
                        if (grabOpportunitiesList.size() > 7) {
                            if (grabOpportunitiesList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle());
                            holder.binding.tvTokens1.setText("Buy for " + grabOpportunitiesList.get(i).getTokens());
                            Glide.with(context)
                                    .load(grabOpportunitiesList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo1);

                            holder.binding.rlVoucher1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickNewVouchers(grabOpportunitiesList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher2.setVisibility(View.GONE);
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
                    } else {
                        if (grabOpportunitiesList.size() > 8) {
                            if (grabOpportunitiesList.get(i).getVoucherTitle().contains("Congratulations")) {
                                holder.binding.tvArticleHead1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[0] + "off");
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle().split("Congratulations ")[1].split("off")[1].trim());
                            } else
                                holder.binding.tvArticle1.setText(grabOpportunitiesList.get(i).getVoucherTitle());
                            holder.binding.tvTokens1.setText("Buy for " + grabOpportunitiesList.get(i).getTokens());
                            Glide.with(context)
                                    .load(grabOpportunitiesList.get(i).getVendorLogo())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivVendorLogo1);

                            holder.binding.rlVoucher1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickNewVouchers(grabOpportunitiesList.get(index));
                            });
                        }else {
                            holder.binding.rlVoucher2.setVisibility(View.GONE);
                            holder.binding.rlVoucher3.setVisibility(View.GONE);
                        }
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
        void onItemClickNewVouchers(EandBAvailableVoucher eandBAvailableVoucher);
    }

    @Override
    public int getItemCount() {
        if (grabOpportunitiesList.size() > 3) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(grabOpportunitiesList.size())) / 3.0);
        } else {
            return 1;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        GrabOpportunitiesAdapterBinding binding;

        public MyViewHolder(@NonNull GrabOpportunitiesAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
