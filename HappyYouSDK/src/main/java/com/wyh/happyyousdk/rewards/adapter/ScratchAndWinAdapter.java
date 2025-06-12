package com.wyh.happyyousdk.rewards.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ScratchAndWinAdapterBinding;
import com.wyh.happyyousdk.model.FreeVoucher;

import java.util.List;

public class ScratchAndWinAdapter extends RecyclerView.Adapter<ScratchAndWinAdapter.MyViewHolder> {

    Context context;
    List<FreeVoucher> freeVoucherList;
    ClickListenerInterface clickListenerInterface;

    public ScratchAndWinAdapter(Context context, List<FreeVoucher> freeVoucherList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.freeVoucherList = freeVoucherList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ScratchAndWinAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.scratch_and_win_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        switch (position) {
            case 0:
                for (int i = 0; i < 3; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (freeVoucherList.size() > 0) {

                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickScratchCard(freeVoucherList.get(index), R.drawable.scratch_card_orange_new);
                            });
                            if (freeVoucherList.get(i).isScratched()) {
                                holder.binding.cvLogo1.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                                holder.binding.rlArticle1.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_orange_button_bg));
                                Glide.with(context)
                                        .load(freeVoucherList.get(i).getVendorLogo())
                                        .error(R.drawable.dummy_image)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(holder.binding.ivVendorLogo1);
                                holder.binding.tvArticleHead1.setText(freeVoucherList.get(i).getVendorName());
                                holder.binding.tvArticle1.setText(freeVoucherList.get(i).getVoucherValue() + "rs off");

                            } else {
                                holder.binding.rlArticle1.setBackground(ContextCompat.getDrawable(context, R.drawable.scratch_card_orange_new));
                            }
                        }
                    } else if (i == 1) {
                        if (freeVoucherList.size() > 1) {

                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickScratchCard(freeVoucherList.get(index), R.drawable.scratch_card_orange_new);
                            });
                            if (freeVoucherList.get(i).isScratched()) {
                                holder.binding.cvLogo2.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                                holder.binding.rlArticle2.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_orange_button_bg));
                                Glide.with(context)
                                        .load(freeVoucherList.get(i).getVendorLogo())
                                        .error(R.drawable.dummy_image)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(holder.binding.ivVendorLogo2);
                                holder.binding.tvArticleHead2.setText(freeVoucherList.get(i).getVendorName());
                                holder.binding.tvArticle2.setText(freeVoucherList.get(i).getVoucherValue() + "rs off");

                            } else {
                                holder.binding.rlArticle2.setBackground(ContextCompat.getDrawable(context, R.drawable.scratch_card_orange_new));
                            }
                        }
                    } else {
                        if (freeVoucherList.size() > 2) {


                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickScratchCard(freeVoucherList.get(index), R.drawable.scratch_card_orange_new);
                            });
                            if (freeVoucherList.get(i).isScratched()) {
                                holder.binding.cvLogo3.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                                Glide.with(context)
                                        .load(freeVoucherList.get(i).getVendorLogo())
                                        .error(R.drawable.dummy_image)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(holder.binding.ivVendorLogo3);
                                holder.binding.tvArticleHead3.setText(freeVoucherList.get(i).getVendorName());
                                holder.binding.tvArticle3.setText(freeVoucherList.get(i).getVoucherValue() + "rs off");
                                holder.binding.rlArticle3.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_orange_button_bg));
                            } else {
                                holder.binding.rlArticle3.setBackground(ContextCompat.getDrawable(context, R.drawable.scratch_card_orange_new));
                            }
                        }
                    }

                    if (freeVoucherList.size() < 2) {
                        holder.binding.rlArticle2.setVisibility(View.INVISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.INVISIBLE);
                    } else if (freeVoucherList.size() < 3) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case 1:
                for (int i = 3; i < 6; i++) {
                    final int index = i;
                    if (i == 3) {
                        if (freeVoucherList.size() > 3) {

                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickScratchCard(freeVoucherList.get(index), R.drawable.scratch_card_orange_new);
                            });
                            if (freeVoucherList.get(i).isScratched()) {
                                holder.binding.rlArticle1.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_orange_button_bg));
                                Glide.with(context)
                                        .load(freeVoucherList.get(i).getVendorLogo())
                                        .error(R.drawable.dummy_image)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(holder.binding.ivVendorLogo1);
                                holder.binding.tvArticleHead1.setText(freeVoucherList.get(i).getVendorName());
                                holder.binding.tvArticle1.setText(freeVoucherList.get(i).getVoucherValue() + "rs off");

                            } else {
                                holder.binding.rlArticle1.setBackground(ContextCompat.getDrawable(context, R.drawable.scratch_card_orange_new));
                            }
                        }
                    } else if (i == 4) {
                        if (freeVoucherList.size() > 4) {

                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickScratchCard(freeVoucherList.get(index), R.drawable.scratch_card_orange_new);
                            });
                            if (freeVoucherList.get(i).isScratched()) {
                                Glide.with(context)
                                        .load(freeVoucherList.get(i).getVendorLogo())
                                        .error(R.drawable.dummy_image)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(holder.binding.ivVendorLogo2);
                                holder.binding.tvArticleHead2.setText(freeVoucherList.get(i).getVendorName());
                                holder.binding.tvArticle2.setText(freeVoucherList.get(i).getVoucherValue() + "rs off");
                                holder.binding.rlArticle2.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_orange_button_bg));
                            } else {
                                holder.binding.rlArticle2.setBackground(ContextCompat.getDrawable(context, R.drawable.scratch_card_orange_new));
                            }
                        }
                    } else {
                        if (freeVoucherList.size() > 5) {

                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickScratchCard(freeVoucherList.get(index), R.drawable.scratch_card_orange_new);
                            });
                            if (freeVoucherList.get(i).isScratched()) {
                                Glide.with(context)
                                        .load(freeVoucherList.get(i).getVendorLogo())
                                        .error(R.drawable.dummy_image)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(holder.binding.ivVendorLogo3);
                                holder.binding.tvArticleHead3.setText(freeVoucherList.get(i).getVendorName());
                                holder.binding.tvArticle3.setText(freeVoucherList.get(i).getVoucherValue() + "rs off");
                                holder.binding.rlArticle3.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_orange_button_bg));
                            } else {
                                holder.binding.rlArticle3.setBackground(ContextCompat.getDrawable(context, R.drawable.scratch_card_orange_new));
                            }
                        }
                    }
                }
                if (freeVoucherList.size() < 4) {
                    holder.binding.rlArticle2.setVisibility(View.INVISIBLE);
                    holder.binding.rlArticle3.setVisibility(View.INVISIBLE);
                } else if (freeVoucherList.size() < 5) {
                    holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                    holder.binding.rlArticle3.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                for (int i = 6; i < 9; i++) {
                    final int index = i;
                    if (i == 6) {
                        if (freeVoucherList.size() > 6) {

                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickScratchCard(freeVoucherList.get(index), R.drawable.scratch_card_orange_new);
                            });
                            if (freeVoucherList.get(i).isScratched()) {
                                Glide.with(context)
                                        .load(freeVoucherList.get(i).getVendorLogo())
                                        .error(R.drawable.dummy_image)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(holder.binding.ivVendorLogo1);
                                holder.binding.tvArticleHead1.setText(freeVoucherList.get(i).getVendorName());
                                holder.binding.tvArticle1.setText(freeVoucherList.get(i).getVoucherValue() + "rs off");
                                holder.binding.rlArticle1.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_orange_button_bg));
                            } else
                                holder.binding.rlArticle1.setBackground(ContextCompat.getDrawable(context, R.drawable.scratch_card_orange_new));
                        }
                    } else if (i == 7) {
                        if (freeVoucherList.size() > 7) {

                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickScratchCard(freeVoucherList.get(index), R.drawable.scratch_card_orange_new);
                            });
                            if (freeVoucherList.get(i).isScratched()) {
                                Glide.with(context)
                                        .load(freeVoucherList.get(i).getVendorLogo())
                                        .error(R.drawable.dummy_image)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(holder.binding.ivVendorLogo2);
                                holder.binding.tvArticleHead2.setText(freeVoucherList.get(i).getVendorName());
                                holder.binding.tvArticle2.setText(freeVoucherList.get(i).getVoucherValue() + "rs off");
                                holder.binding.rlArticle2.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_orange_button_bg));
                            } else
                                holder.binding.rlArticle1.setBackground(ContextCompat.getDrawable(context, R.drawable.scratch_card_orange_new));
                        }
                    } else {
                        if (freeVoucherList.size() > 8) {

                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClickScratchCard(freeVoucherList.get(index), R.drawable.scratch_card_orange_new);
                            });
                            if (freeVoucherList.get(i).isScratched()) {
                                Glide.with(context)
                                        .load(freeVoucherList.get(i).getVendorLogo())
                                        .error(R.drawable.dummy_image)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(holder.binding.ivVendorLogo3);
                                holder.binding.tvArticleHead3.setText(freeVoucherList.get(i).getVendorName());
                                holder.binding.tvArticle3.setText(freeVoucherList.get(i).getVoucherValue() + "rs off");
                                holder.binding.rlArticle3.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_light_orange_button_bg));
                            } else
                                holder.binding.rlArticle3.setBackground(ContextCompat.getDrawable(context, R.drawable.scratch_card_orange_new));
                        }
                    }
                }
                if (freeVoucherList.size() < 7) {
                    holder.binding.rlArticle2.setVisibility(View.INVISIBLE);
                    holder.binding.rlArticle3.setVisibility(View.INVISIBLE);
                } else if (freeVoucherList.size() < 8) {
                    holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                    holder.binding.rlArticle3.setVisibility(View.INVISIBLE);
                }
                break;
            case 3:
                holder.binding.rlArticle1.setVisibility(View.GONE);
                holder.binding.rlArticle2.setVisibility(View.GONE);
                holder.binding.rlArticle3.setVisibility(View.GONE);
                break;
        }

    }

    public interface ClickListenerInterface {
        void onItemClickScratchCard(FreeVoucher freeVoucher, int bgDrawable);
    }

    @Override
    public int getItemCount() {
        if (freeVoucherList.size() > 3 && freeVoucherList.size() <= 9) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(freeVoucherList.size())) / 3.0);
        } else if (freeVoucherList.size() > 9)
            return 3;
        else {
            return 1;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ScratchAndWinAdapterBinding binding;

        public MyViewHolder(@NonNull ScratchAndWinAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
