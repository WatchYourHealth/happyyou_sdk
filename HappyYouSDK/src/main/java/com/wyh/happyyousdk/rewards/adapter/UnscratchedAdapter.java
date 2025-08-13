package com.wyh.happyyousdk.rewards.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.databinding.UnscratchedAdapterDataBinding;
import com.wyh.happyyousdk.model.response.rewards.UnscratchedTokensData;
import com.wyh.happyyousdk.utils.CommonUtils;

import java.util.List;

public class UnscratchedAdapter extends RecyclerView.Adapter<UnscratchedAdapter.MyViewHolder> {

    Context context;
    List<UnscratchedTokensData> unlockedVoucherList;
    ClickListenerInterface clickListenerInterface;

    public UnscratchedAdapter(Context context, List<UnscratchedTokensData> unlockedVoucherList, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.unlockedVoucherList = unlockedVoucherList;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UnscratchedAdapterDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.unscratched_adapter_data, parent, false);
//        screenWidth = displayMetrics.widthPixels
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context)
                .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "grab_opportunity_banner.png")
                .into(holder.binding.ivIndicatorSelected);

        switch (position) {
            case 0:
                if (unlockedVoucherList.size() > 0) {
                    holder.binding.llBanner.setVisibility(View.GONE);
                    for (int i = 0; i < 3; i++) {
                        final int index = i;
                        if (i == 0) {
                            if (unlockedVoucherList.size() > 0) {
                                if (i == unlockedVoucherList.size()) {
                                    holder.binding.llbanner1.setVisibility(View.VISIBLE);
                                } else
                                    holder.binding.llbanner1.setVisibility(View.GONE);
                                if (unlockedVoucherList.size() > i) {
                                    if (unlockedVoucherList.get(i).getActivityId() == -1) {
                                        holder.binding.rlArticle1.setBackgroundResource(R.drawable.scratch_card_orange_new);
                                    } else {
                                        holder.binding.rlArticle1.setBackgroundResource(R.drawable.scratch_card_pink_new);
                                    }
                                }
                                holder.binding.rlArticle1.setOnClickListener(v -> {
                                    if (holder.binding.llbanner1.getVisibility() == View.GONE)
                                        clickListenerInterface.onItemClickUnscratchedVouchers(unlockedVoucherList.get(0));
                                });
//                            holder.binding.rlArticle1.setBackgroundResource(R.drawable.ic_light_orange_button_bg);
                            }
                        } else if (i == 1) {
                            if (unlockedVoucherList.size() > 1) {
                                if (i == unlockedVoucherList.size()) {
                                    holder.binding.llbanner2.setVisibility(View.VISIBLE);
                                } else
                                    holder.binding.llbanner2.setVisibility(View.GONE);

                                if (unlockedVoucherList.size() > i) {
                                    if (unlockedVoucherList.get(i).getActivityId() == -1) {
                                        holder.binding.rlArticle2.setBackgroundResource(R.drawable.scratch_card_orange_new);
                                    } else {
                                        holder.binding.rlArticle2.setBackgroundResource(R.drawable.scratch_card_pink_new);
                                    }
                                }
                                holder.binding.rlArticle2.setOnClickListener(v -> {
                                    if (holder.binding.llbanner2.getVisibility() == View.GONE)
                                        clickListenerInterface.onItemClickUnscratchedVouchers(unlockedVoucherList.get(1));
                                });
//                            holder.binding.rlArticle2.setBackgroundResource(R.drawable.ic_light_orange_button_bg);
                            } else {
                                holder.binding.rlArticle2.setVisibility(View.GONE);
                                holder.binding.rlArticle3.setVisibility(View.GONE);
                            }
                        } else {
                            if (unlockedVoucherList.size() > 2) {
                                if (i == unlockedVoucherList.size()) {
                                    holder.binding.llbanner3.setVisibility(View.VISIBLE);
                                } else
                                    holder.binding.llbanner3.setVisibility(View.GONE);

                                if (unlockedVoucherList.size() > i) {
                                    if (unlockedVoucherList.get(i).getActivityId() == -1) {
                                        holder.binding.rlArticle3.setBackgroundResource(R.drawable.scratch_card_orange_new);
                                    } else {
                                        holder.binding.rlArticle3.setBackgroundResource(R.drawable.scratch_card_pink_new);
                                    }
                                }
                                holder.binding.rlArticle3.setOnClickListener(v -> {
                                    if (holder.binding.llbanner3.getVisibility() == View.GONE)
                                        clickListenerInterface.onItemClickUnscratchedVouchers(unlockedVoucherList.get(2));
                                });
//                            holder.binding.rlArticle3.setBackgroundResource(R.drawable.ic_light_orange_button_bg);
                            } else {
                                holder.binding.rlArticle3.setVisibility(View.GONE);
                            }
                        }

                        if (unlockedVoucherList.size() < 2) {
                            holder.binding.rlArticle2.setVisibility(View.GONE);
                            holder.binding.rlArticle3.setVisibility(View.GONE);
                        } else if (unlockedVoucherList.size() < 3) {
                            holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                            holder.binding.rlArticle3.setVisibility(View.GONE);
                        }
                    }
                } else {
                    holder.binding.llBanner.setVisibility(View.VISIBLE);
                    holder.binding.rlArticle1.setVisibility(View.GONE);
                    holder.binding.rlArticle2.setVisibility(View.GONE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                }
                break;
            case 1:
                if (unlockedVoucherList.size() > 3) {
                    holder.binding.llBanner.setVisibility(View.GONE);
                    for (int i = 3; i < 6; i++) {
                        final int index = i;
                        if (i == 3) {
                            if (unlockedVoucherList.size() > 3) {
                                if (i == unlockedVoucherList.size()) {
                                    holder.binding.llbanner1.setVisibility(View.VISIBLE);
                                } else
                                    holder.binding.llbanner1.setVisibility(View.GONE);

                                if (unlockedVoucherList.size() > i) {
                                    if (unlockedVoucherList.get(i).getActivityId() == -1) {
                                        holder.binding.rlArticle1.setBackgroundResource(R.drawable.scratch_card_orange_new);
                                    } else {
                                        holder.binding.rlArticle1.setBackgroundResource(R.drawable.scratch_card_pink_new);
                                    }
                                }
                                holder.binding.rlArticle1.setOnClickListener(v -> {
                                    if (holder.binding.llbanner1.getVisibility() == View.GONE)
                                        clickListenerInterface.onItemClickUnscratchedVouchers(unlockedVoucherList.get(3));
                                });
//                            holder.binding.rlArticle1.setBackgroundResource(R.drawable.ic_light_orange_button_bg);
                            } else {
                                holder.binding.rlArticle1.setVisibility(View.GONE);
                            }
                        } else if (i == 4) {
                            if (unlockedVoucherList.size() > 4) {
                                if (i == unlockedVoucherList.size()) {
                                    holder.binding.llbanner2.setVisibility(View.VISIBLE);
                                } else
                                    holder.binding.llbanner2.setVisibility(View.GONE);

                                if (unlockedVoucherList.size() > i) {
                                    if (unlockedVoucherList.get(i).getActivityId() == -1) {
                                        holder.binding.rlArticle2.setBackgroundResource(R.drawable.scratch_card_orange_new);
                                    } else {
                                        holder.binding.rlArticle2.setBackgroundResource(R.drawable.scratch_card_pink_new);
                                    }
                                }
                                holder.binding.rlArticle2.setOnClickListener(v -> {
                                    if (holder.binding.llbanner2.getVisibility() == View.GONE)
                                        clickListenerInterface.onItemClickUnscratchedVouchers(unlockedVoucherList.get(4));
                                });
//                            holder.binding.rlArticle2.setBackgroundResource(R.drawable.ic_light_orange_button_bg);
                            } else {
                                holder.binding.rlArticle2.setVisibility(View.GONE);
                                holder.binding.rlArticle3.setVisibility(View.GONE);
                            }
                        } else {
                            if (unlockedVoucherList.size() > 5) {
                                if (i == unlockedVoucherList.size()) {
                                    holder.binding.llbanner3.setVisibility(View.VISIBLE);
                                } else
                                    holder.binding.llbanner3.setVisibility(View.GONE);

                                if (unlockedVoucherList.size() > i) {
                                    if (unlockedVoucherList.get(i).getActivityId() == -1) {
                                        holder.binding.rlArticle3.setBackgroundResource(R.drawable.scratch_card_orange_new);
                                    } else {
                                        holder.binding.rlArticle3.setBackgroundResource(R.drawable.scratch_card_pink_new);
                                    }
                                }
                                holder.binding.rlArticle3.setOnClickListener(v -> {
                                    if (holder.binding.llbanner3.getVisibility() == View.GONE)
                                        clickListenerInterface.onItemClickUnscratchedVouchers(unlockedVoucherList.get(5));
                                });
//                            holder.binding.rlArticle3.setBackgroundResource(R.drawable.ic_light_orange_button_bg);
                            } else {
                                holder.binding.rlArticle3.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    holder.binding.llBanner.setVisibility(View.VISIBLE);
                    holder.binding.rlArticle1.setVisibility(View.GONE);
                    holder.binding.rlArticle2.setVisibility(View.GONE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                }
                break;
            case 2:
                holder.binding.llBanner.setVisibility(View.VISIBLE);
                holder.binding.rlArticle1.setVisibility(View.GONE);
                holder.binding.rlArticle2.setVisibility(View.GONE);
                holder.binding.rlArticle3.setVisibility(View.GONE);
                /*if(unlockedVoucherList.size() > 6){
                    holder.binding.llBanner.setVisibility(View.GONE);
                    for (int i = 6; i < 9; i++) {
                        final int index = i;
                        if (i == 6) {
                            if (unlockedVoucherList.size()  > 6) {
                                if (i == unlockedVoucherList.size()) {
                                    holder.binding.llbanner1.setVisibility(View.VISIBLE);
                                } else
                                    holder.binding.llbanner1.setVisibility(View.GONE);

                                if (unlockedVoucherList.size() > i) {
                                    if (unlockedVoucherList.get(i).getActivityId() == -1) {
                                        holder.binding.rlArticle1.setBackgroundResource(R.drawable.scratch_card_orange_new);
                                    } else {
                                        holder.binding.rlArticle1.setBackgroundResource(R.drawable.scratch_card_pink_new);
                                    }
                                }
                                holder.binding.rlArticle1.setOnClickListener(v -> {
                                    if (holder.binding.llbanner1.getVisibility() == View.GONE)
                                        clickListenerInterface.onItemClickUnscratchedVouchers(unlockedVoucherList.get(6));
                                });
//                            holder.binding.rlArticle1.setBackgroundResource(R.drawable.ic_light_orange_button_bg);
                            } else {
                                holder.binding.rlArticle1.setVisibility(View.GONE);
                            }
                        } else if (i == 7) {
                            if (unlockedVoucherList.size()  > 7) {
                                if (i == unlockedVoucherList.size()) {
                                    holder.binding.llbanner2.setVisibility(View.VISIBLE);
                                } else
                                    holder.binding.llbanner2.setVisibility(View.GONE);

                                if (unlockedVoucherList.size() > i) {
                                    if (unlockedVoucherList.get(i).getActivityId() == -1) {
                                        holder.binding.rlArticle2.setBackgroundResource(R.drawable.scratch_card_orange_new);
                                    } else {
                                        holder.binding.rlArticle2.setBackgroundResource(R.drawable.scratch_card_pink_new);
                                    }
                                }
                                holder.binding.rlArticle2.setOnClickListener(v -> {
                                    if (holder.binding.llbanner2.getVisibility() == View.GONE)
                                        clickListenerInterface.onItemClickUnscratchedVouchers(unlockedVoucherList.get(7));
                                });
//                            holder.binding.rlArticle2.setBackgroundResource(R.drawable.ic_light_orange_button_bg);
                            } else {
                                holder.binding.rlArticle2.setVisibility(View.GONE);
                                holder.binding.rlArticle3.setVisibility(View.GONE);
                            }
                        } else {
                            if (unlockedVoucherList.size()  > 8) {
                                if (i == unlockedVoucherList.size()) {
                                    holder.binding.llbanner3.setVisibility(View.VISIBLE);
                                } else
                                    holder.binding.llbanner3.setVisibility(View.GONE);

                                if (unlockedVoucherList.size() > i) {
                                    if (unlockedVoucherList.get(i).getActivityId() == -1) {
                                        holder.binding.rlArticle3.setBackgroundResource(R.drawable.scratch_card_orange_new);
                                    } else {
                                        holder.binding.rlArticle3.setBackgroundResource(R.drawable.scratch_card_pink_new);
                                    }
                                }
                                holder.binding.rlArticle3.setOnClickListener(v -> {
                                    if (holder.binding.llbanner3.getVisibility() == View.GONE)
                                        clickListenerInterface.onItemClickUnscratchedVouchers(unlockedVoucherList.get(8));
                                });
//                            holder.binding.rlArticle3.setBackgroundResource(R.drawable.ic_light_orange_button_bg);
                            } else {
                                holder.binding.rlArticle3.setVisibility(View.GONE);
                            }
                        }

                        if (unlockedVoucherList.size()  < 7) {
                            holder.binding.rlArticle2.setVisibility(View.GONE);
                            holder.binding.rlArticle3.setVisibility(View.GONE);
                        } else if (unlockedVoucherList.size()  < 8) {
                            holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                            holder.binding.rlArticle3.setVisibility(View.GONE);
                        }

                    }
                }else{
                    holder.binding.llBanner.setVisibility(View.VISIBLE);
                    holder.binding.rlArticle1.setVisibility(View.GONE);
                    holder.binding.rlArticle2.setVisibility(View.GONE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                }*/
                break;
            case 3:
                holder.binding.llBanner.setVisibility(View.GONE);
                holder.binding.rlArticle1.setVisibility(View.GONE);
                holder.binding.rlArticle2.setVisibility(View.GONE);
                holder.binding.rlArticle3.setVisibility(View.GONE);
                break;
        }

    }

    public interface ClickListenerInterface {
        void onItemClickUnscratchedVouchers(UnscratchedTokensData data);
    }

    @Override
    public int getItemCount() {
        if (unlockedVoucherList.size() > 6) {
            return 3;
        } else if (unlockedVoucherList.size() > 0) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(unlockedVoucherList.size())) / 3.0) + 1;
        } else {
            return 1;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        UnscratchedAdapterDataBinding binding;

        public MyViewHolder(@NonNull UnscratchedAdapterDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
