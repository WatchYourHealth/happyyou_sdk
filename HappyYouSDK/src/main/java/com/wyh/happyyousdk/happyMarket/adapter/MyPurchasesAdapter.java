package com.wyh.happyyousdk.happyMarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.MyPurchasesAdapterBinding;

import java.util.List;
import java.util.Objects;

public class MyPurchasesAdapter extends RecyclerView.Adapter<MyPurchasesAdapter.MyViewHolder> {

    Context context;
    List<String> levelActivities;
    ClickListenerInterface clickListenerInterface;

    public MyPurchasesAdapter(Context context, List<String> levelActivities, ClickListenerInterface clickListenerInterface) {
        this.context = context;
        this.levelActivities = levelActivities;
        this.clickListenerInterface = clickListenerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyPurchasesAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.my_purchases_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                for (int i = 0; i < 3; i++) {
                    final int index = i;
                    if (i == 0) {
                        if (levelActivities.size() > 0) {
                            String name = levelActivities.get(i) != null ? levelActivities.get(i).replaceAll("\\s", "").toLowerCase() : "";
                            holder.binding.rlArticle1.setVisibility(View.VISIBLE);
                            holder.binding.tvArticle1.setText(getName(name));

                            if (getName(name).equals("Others")) {
                                holder.binding.lImage1.setVisibility(View.GONE);
                            } else {
                                holder.binding.lImage1.setVisibility(View.VISIBLE);
                            }
                            /*Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);*/

                            holder.binding.ivIcon1.setImageResource(getIcon(name));
                            holder.binding.ivImage1.setImageResource(getImage(name));

                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClick(levelActivities.get(index));
                            });
                        } else {
                            holder.binding.rlArticle1.setVisibility(View.INVISIBLE);
                        }
                    } else if (i == 1) {
                        if (levelActivities.size() > 1) {
                            String name = levelActivities.get(i).replaceAll("\\s", "").toLowerCase();
                            holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                            holder.binding.tvArticle2.setText(getName(name));
                            /*Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);*/
                            holder.binding.ivIcon2.setImageResource(getIcon(name));
                            holder.binding.ivImage2.setImageResource(getImage(name));

                            if (getName(name).equals("Others")) {
                                holder.binding.lImage2.setVisibility(View.GONE);
                            } else {
                                holder.binding.lImage2.setVisibility(View.VISIBLE);
                            }
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClick(levelActivities.get(index));
                            });
                        } else {
                            holder.binding.rlArticle2.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (levelActivities.size() > 2) {
                            String name = levelActivities.get(i).replaceAll("\\s", "").toLowerCase();
                            holder.binding.rlArticle3.setVisibility(View.VISIBLE);
                            holder.binding.tvArticle3.setText(getName(name));
                            /*Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);*/
                            holder.binding.ivIcon3.setImageResource(getIcon(name));
                            holder.binding.ivImage3.setImageResource(getImage(name));
                            if (getName(name).equals("Others")) {
                                holder.binding.lImage3.setVisibility(View.GONE);
                            } else {
                                holder.binding.lImage3.setVisibility(View.VISIBLE);
                            }
                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClick(levelActivities.get(index));
                            });
                        } else {
                            holder.binding.rlArticle3.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (levelActivities.size() < 2) {
                        holder.binding.rlArticle2.setVisibility(View.GONE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    } else if (levelActivities.size() < 3) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }
                }
                break;
            case 1:
                for (int i = 3; i < 6; i++) {
                    final int index = i;
                    if (i == 3) {
                        if (levelActivities.size() > 3) {
                            String name = levelActivities.get(i).replaceAll("\\s", "").toLowerCase();
                            holder.binding.tvArticle1.setText(getName(name));
                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClick(levelActivities.get(index));
                            });
                           /* Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);*/
                            holder.binding.ivIcon1.setImageResource(getIcon(name));
                            holder.binding.ivImage1.setImageResource(getImage(name));
                            if (getName(name).equals("Others")) {
                                holder.binding.lImage1.setVisibility(View.GONE);
                            } else {
                                holder.binding.lImage1.setVisibility(View.VISIBLE);
                            }


                        }
                    } else if (i == 4) {
                        if (levelActivities.size() > 4) {
                            String name = levelActivities.get(i).replaceAll("\\s", "").toLowerCase();
                            holder.binding.tvArticle2.setText(getName(name));
                            /*Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);*/
                            holder.binding.ivIcon2.setImageResource(getIcon(name));
                            holder.binding.ivImage2.setImageResource(getImage(name));

                            if (getName(name).equals("Others")) {
                                holder.binding.lImage2.setVisibility(View.GONE);
                            } else {
                                holder.binding.lImage2.setVisibility(View.VISIBLE);
                            }
                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClick(levelActivities.get(index));
                            });
                        }
                    } else {
                        if (levelActivities.size() > 5) {
                            String name = levelActivities.get(i).replaceAll("\\s", "").toLowerCase();
                            holder.binding.tvArticle3.setText(getName(name));
                            /*Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);*/

                            holder.binding.ivIcon3.setImageResource(getIcon(name));
                            holder.binding.ivImage3.setImageResource(getImage(name));
                            if (getName(name).equals("Others")) {
                                holder.binding.lImage3.setVisibility(View.GONE);
                            } else {
                                holder.binding.lImage3.setVisibility(View.VISIBLE);
                            }
                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClick(levelActivities.get(index));
                            });
                        }
                    }
                }
                if (levelActivities.size() < 4) {
                    holder.binding.rlArticle2.setVisibility(View.GONE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                } else if (levelActivities.size() < 5) {
                    holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                }
                break;
            case 2:
                for (int i = 6; i < 9; i++) {
                    final int index = i;
                    if (i == 6) {
                        if (levelActivities.size() > 6) {
                            String name = levelActivities.get(i).replaceAll("\\s", "").toLowerCase();
                            holder.binding.tvArticle1.setText(getName(name));
                            /*Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage1);*/
                            holder.binding.ivIcon1.setImageResource(getIcon(name));
                            holder.binding.ivImage1.setImageResource(getImage(name));
                            if (getName(name).equals("Others")) {
                                holder.binding.lImage1.setVisibility(View.GONE);
                            } else {
                                holder.binding.lImage1.setVisibility(View.VISIBLE);
                            }

                            holder.binding.rlArticle1.setOnClickListener(view -> {
                                clickListenerInterface.onItemClick(levelActivities.get(index));
                            });
                        }
                    } else if (i == 7) {
                        if (levelActivities.size() > 7) {
                            String name = levelActivities.get(i).replaceAll("\\s", "").toLowerCase();
                            holder.binding.tvArticle2.setText(getName(name));
                            /*Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage2);
*/
                            holder.binding.ivIcon2.setImageResource(getIcon(name));
                            holder.binding.ivImage2.setImageResource(getImage(name));
                            if (getName(name).equals("Others")) {
                                holder.binding.lImage2.setVisibility(View.GONE);
                            } else {
                                holder.binding.lImage2.setVisibility(View.VISIBLE);
                            }

                            holder.binding.rlArticle2.setOnClickListener(view -> {
                                clickListenerInterface.onItemClick(levelActivities.get(index));
                            });
                        }
                    } else {
                        if (levelActivities.size() > 8) {
                            String name = levelActivities.get(i).replaceAll("\\s", "").toLowerCase();
                            holder.binding.tvArticle3.setText(getName(name));
                            /*Glide.with(context)
                                    .load(levelActivities.get(i).getActivityImagePath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.ivImage3);*/
                            holder.binding.ivIcon3.setImageResource(getIcon(name));
                            holder.binding.ivImage3.setImageResource(getImage(name));
                            if (getName(name).equals("Others")) {
                                holder.binding.lImage3.setVisibility(View.GONE);
                            } else {
                                holder.binding.lImage3.setVisibility(View.VISIBLE);
                            }

                            holder.binding.rlArticle3.setOnClickListener(view -> {
                                clickListenerInterface.onItemClick(levelActivities.get(index));
                            });
                        }
                    }
                }
                if (levelActivities.size() < 7) {
                    holder.binding.rlArticle2.setVisibility(View.GONE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
                } else if (levelActivities.size() < 8) {
                    holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                    holder.binding.rlArticle3.setVisibility(View.GONE);
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
        void onItemClick(String vendor);
    }

    @Override
    public int getItemCount() {
        if (levelActivities.size() > 3) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(levelActivities.size())) / 3.0);
        } else {
            return 1;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MyPurchasesAdapterBinding binding;

        public MyViewHolder(@NonNull MyPurchasesAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private int getIcon(String vendor) {
        if (Objects.equals(vendor, "medpayopd")) {
            return R.drawable.ic_physical_consultation;
        } else if (Objects.equals(vendor, "pharmeasy") || Objects.equals(vendor, "medpaypharmacy")) {
            return R.drawable.ic_pharmacy;
        } else if (Objects.equals(vendor, "healthassure") || Objects.equals(vendor, "connectandheal") || Objects.equals(vendor, "alyvehealth")) {
            return R.drawable.ic_diagnostic;
        } else if (Objects.equals(vendor, "coachpro")) {
            return R.drawable.ic_fitness_and_nutrition;
        } else if (Objects.equals(vendor, "mentalWellbeing")) {
            return R.drawable.ic_mental_welbeing;
        } else if (Objects.equals(vendor, "hobbytribe")) {
            return R.drawable.ic_hobby;
        } else if (Objects.equals(vendor, "actofit")) {
            return R.drawable.ic_device_electronics;
        } else if (Objects.equals(vendor, "vouchers")) {
            return R.drawable.ic_stamp;
        } else if (Objects.equals(vendor, "getvisit")) {
            return R.drawable.ic_tele_consultation;
        }
        return R.drawable.ic_device_electronics;
    }

    private String getName(String vendor) {
        if (Objects.equals(vendor, "medpayopd")) {
            return "Physical Consultation";
        } else if (Objects.equals(vendor, "pharmeasy")) {
            return "Pharmacy";
        } else if (Objects.equals(vendor, "healthassure") || Objects.equals(vendor, "connectandheal")) {
            return "Diagnostics\nand LAB Tests";
        } else if (Objects.equals(vendor, "coachpro")) {
            return "Fitness &\nNutrition";
        } else if (Objects.equals(vendor, "mentalWellbeing")) {
            return "Mental\nWellbeing";
        } else if (Objects.equals(vendor, "hobbytribe")) {
            return "Hobby";
        } else if (Objects.equals(vendor, "actofit")) {
            return "Devices and\nMerchandise";
        } else if (Objects.equals(vendor, "vouchers")) {
            return "Others";
        } else if (Objects.equals(vendor, "medpaypharmacy")) {
            return "Medpay\nPharmacy";
        } else if (Objects.equals(vendor, "getvisit")) {
            return "Tele-Consultation";
        }else if (Objects.equals(vendor, "alyvehealth")) {
            return "OPD";
        }
        return "Others";
    }

    private int getImage(String vendor) {
        if (Objects.equals(vendor, "medpayopd") || Objects.equals(vendor, "medpaypharmacy")) {
            return R.drawable.ic_medpay_logo;
        } else if (Objects.equals(vendor, "pharmeasy")) {
            return R.drawable.ic_pharmeasy_logo;
        } else if (Objects.equals(vendor, "healthassure")) {
            return R.drawable.ha_logo;
        } else if (Objects.equals(vendor, "coachpro")) {
            return R.drawable.coach_pro_logo;
        } else if (Objects.equals(vendor, "connectandheal")) {
            return R.drawable.connected_logo;
        } else if (Objects.equals(vendor, "mentalWellbeing")) {
            return R.drawable.mrf_logo_light;
        } else if (Objects.equals(vendor, "hobbytribe")) {
            return R.drawable.hobby_tribe_logo;
        } else if (Objects.equals(vendor, "actofit")) {
            return R.drawable.actofit_logo;
        } else if (Objects.equals(vendor, "vouchers")) {
            return R.drawable.doconline_logo;
        } else if (Objects.equals(vendor, "getvisit")) {
            return R.drawable.vistorlogo;
        } else if (Objects.equals(vendor, "alyvehealth")) {
            return R.drawable.alyve_health_logo;
        }
        return R.drawable.ic_pharmeasy_logo;
    }
}
