package com.wyh.happyyousdk.absorb.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.absorb.HealthHacksActivity;
import com.wyh.happyyousdk.absorb.WebinarDetailActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.model.request.absorb.AddUserInterestRequest;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;

import com.wyh.happyyousdk.databinding.AbsorbWebinarAdapterBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.ScratchCardPopUpBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthHacksWebinarAdapter extends RecyclerView.Adapter<HealthHacksWebinarAdapter.MyViewHolder> {

    Context context;
    List<GetDashboardDataResponse.Data.Webinar> webinarList;
    AlertDialog alertDialogBonusRewards;
    boolean isPositiveBtn = false;

    public HealthHacksWebinarAdapter(Context context, List<GetDashboardDataResponse.Data.Webinar> webinarList) {
        this.context = context;
        this.webinarList = webinarList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AbsorbWebinarAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.absorb_webinar_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                for (int i = 0; i < 2; i++) {
                    int index = i;

                    if (i == 0) {
                        if (!webinarList.isEmpty()) {
                            holder.binding.tvVideo1.setText(webinarList.get(i).getTitle());
                            Glide.with(context)
                                    .load(webinarList.get(position).getImgPath())
                                    .error(R.drawable.dummy_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstVideo.setOnClickListener(view -> intentCode(index));

                            holder.binding.icInterest.setOnClickListener(view -> AddUserInterest(webinarList.get(index).getId(), "", 1, holder.binding));
                        }
                    } else if (i == 1) {
                        if (webinarList.size() > 1) {
                            holder.binding.tvVideo2.setText(webinarList.get(i).getTitle());
                            Glide.with(context)
                                    .load(webinarList.get(position).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondVideo.setOnClickListener(view -> intentCode(index));
                            holder.binding.icInterest1.setOnClickListener(view -> AddUserInterest(webinarList.get(index).getId(), "", 1, holder.binding));
                        }
                    }

                    if (webinarList.size() < 2) {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
//                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    } /*else if (webinarList.size() < 2) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }*/

                }
                break;
            case 1:
                for (int i = 2; i < 4; i++) {
                    int index = i;
                    if (i == 2) {
                        if (webinarList.size() > 2) {
                            holder.binding.tvVideo1.setText(webinarList.get(i).getTitle());
                            Glide.with(context)
                                    .load(webinarList.get(position).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstVideo.setOnClickListener(view -> intentCode(index));
                            holder.binding.icInterest.setOnClickListener(view -> AddUserInterest(webinarList.get(index).getId(), "", 1, holder.binding));
                        }
                    } else if (i == 3) {
                        if (webinarList.size() > 3) {
                            holder.binding.tvVideo2.setText(webinarList.get(i).getTitle());
                            Glide.with(context)
                                    .load(webinarList.get(position).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondVideo.setOnClickListener(view -> intentCode(index));
                            holder.binding.icInterest1.setOnClickListener(view -> AddUserInterest(webinarList.get(index).getId(), "", 1, holder.binding));
                        }
                    } else {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                        holder.binding.cvFirstVideo.setVisibility(View.GONE);

                    }

                    if (webinarList.size() < 2) {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
//                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    } /*else if (webinarList.size() < 2) {
                        holder.binding.rlArticle2.setVisibility(View.VISIBLE);
                        holder.binding.rlArticle3.setVisibility(View.GONE);
                    }*/

                }
                break;
            case 2:
                for (int i = 4; i < 6; i++) {
                    int index = i;
                    if (i == 4) {
                        if (webinarList.size() > 4) {
                            holder.binding.tvVideo1.setText(webinarList.get(i).getTitle());
                            Glide.with(context)
                                    .load(webinarList.get(position).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstVideo.setOnClickListener(view -> intentCode(index));
                            holder.binding.icInterest.setOnClickListener(view -> AddUserInterest(webinarList.get(index).getId(), "", 1, holder.binding));
                        }
                    } else if (i == 5) {
                        if (webinarList.size() > 5) {
                            holder.binding.tvVideo2.setText(webinarList.get(i).getTitle());
                            Glide.with(context)
                                    .load(webinarList.get(position).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondVideo.setOnClickListener(view -> intentCode(index));
                            holder.binding.icInterest1.setOnClickListener(view -> AddUserInterest(webinarList.get(index).getId(), "", 1, holder.binding));
                        }
                    } else {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                        holder.binding.cvFirstVideo.setVisibility(View.GONE);

                    }

                }
                break;
            case 3:
                for (int i = 6; i < 8; i++) {
                    int index = i;

                    if (i == 6) {
                        if (webinarList.size() > 6) {
                            holder.binding.tvVideo1.setText(webinarList.get(i).getTitle());
                            Glide.with(context)
                                    .load(webinarList.get(position).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstVideo.setOnClickListener(view -> intentCode(index));
                            holder.binding.icInterest.setOnClickListener(view -> AddUserInterest(webinarList.get(index).getId(), "", 1, holder.binding));
                        }
                    } else if (i == 7) {
                        if (webinarList.size() > 7) {
                            holder.binding.tvVideo2.setText(webinarList.get(i).getTitle());
                            Glide.with(context)
                                    .load(webinarList.get(position).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondVideo.setOnClickListener(view -> intentCode(index));
                            holder.binding.icInterest1.setOnClickListener(view -> AddUserInterest(webinarList.get(index).getId(), "", 1, holder.binding));
                        }
                    } else {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                        holder.binding.cvFirstVideo.setVisibility(View.GONE);

                    }
                }

                break;
            case 4:
                for (int i = 8; i < 10; i++) {
                    int index = i;
                    if (i == 8) {
                        if (webinarList.size() > 8) {
                            holder.binding.tvVideo1.setText(webinarList.get(i).getTitle());
                            Glide.with(context)
                                    .load(webinarList.get(position).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg1);
                            holder.binding.cvFirstVideo.setOnClickListener(view -> intentCode(index));
                            holder.binding.icInterest.setOnClickListener(view -> AddUserInterest(webinarList.get(index).getId(), "", 1, holder.binding));
                        }
                    } else if (i == 9) {
                        if (webinarList.size() > 9) {
                            holder.binding.tvVideo2.setText(webinarList.get(i).getTitle());
                            Glide.with(context)
                                    .load(webinarList.get(position).getImgPath())
                                    .error(R.drawable.dummy_image)
//                .placeholder(circularProgressDrawable)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(holder.binding.bgImg2);
                            holder.binding.cvSecondVideo.setOnClickListener(view -> intentCode(index));
                            holder.binding.icInterest1.setOnClickListener(view -> AddUserInterest(webinarList.get(index).getId(), "", 1, holder.binding));
                        }
                    } else {
                        holder.binding.cvSecondVideo.setVisibility(View.GONE);
                        holder.binding.cvFirstVideo.setVisibility(View.GONE);

                    }
                }
                break;
        }

    }

    private void AddUserInterest(Integer id, String s, int userInterest, AbsorbWebinarAdapterBinding binding) {
        ProgressDialog progressDialog;
        ApiInterfaceWyh apiInterfaceWyh;

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        AddUserInterestRequest addUserInterestRequest = new AddUserInterestRequest(id, s, userInterest);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.addUserInterest(SharedPref.getAuthToken(), addUserInterestRequest);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                //                    refreshAuthToken(id,s,userInterest);
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.webinar_add_user_interest_success));

                    if (response.body().getRewards() != null && response.body().getRewards().getReward() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, response.body().getRewards().getReward()));
                    }

                    if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, response.body().getRewards().getBonusRewards()));
                    }

                    showRewardsPopupDialogBox();
                    binding.icInterest1.setVisibility(View.GONE);
                    binding.icInterest.setVisibility(View.GONE);
                    Toast.makeText(context, "User Interest Added Successfully", Toast.LENGTH_SHORT).show();


                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.webinar_add_user_interest_failed));
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.webinar_add_user_interest_failed));
                Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showRewardsPopup(String rewards) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        ScratchCardPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.scratch_card_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvPoints.setText(points);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, RewardsActivity.class);
            context.startActivity(intent);
        });
        binding.scratchView.onFullReveal();
        binding.scratchView.setScratchListener(((HealthHacksActivity) context));

        Rect displayRectangle = new Rect();
        Window window = ((HealthHacksActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void showRewardsPopupNew(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");
         alertDialog.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(context, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                context.startActivity(intent);
            }else{
                showRewardsPopupDialogBox();
            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(alertDialog::dismiss, 3000);

        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> alertDialog.dismiss());

        binding.scratchView.onFullReveal();

        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, RewardsActivity.class);
            intent.putExtra("currentIndex", 0);
            context.startActivity(intent);
        });
        binding.scratchView.setScratchListener(((HealthHacksActivity) context));

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((HealthHacksActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showBonusRewardsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusRewards = alertBuilder.create();
        alertDialogBonusRewards.setCancelable(false);
        if (!alertDialogBonusRewards.isShowing())
            alertDialogBonusRewards.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");


        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
         alertDialogBonusRewards.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(context, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                context.startActivity(intent);
            }else{
                showRewardsPopupDialogBox();
            }
        });

        binding.btnPositive.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
            Intent intent = new Intent(context, RewardsActivity.class);
            intent.putExtra("currentIndex", 0);
            context.startActivity(intent);
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(((HealthHacksActivity) context));
        binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((HealthHacksActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        Objects.requireNonNull(alertDialogBonusRewards.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getItemCount() {
        if (webinarList.size() > 1) {
            return webinarList.size() / 2;
        } else {
            return 1;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        AbsorbWebinarAdapterBinding binding;

        public MyViewHolder(@NonNull AbsorbWebinarAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void intentCode(int index){
        Intent intent = new Intent(context, WebinarDetailActivity.class);
        intent.putExtra("webinarId", webinarList.get(index).getId() + "");
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void showRewardsPopupDialogBox() {
        if (!NewDashboardHelper.Companion.getPopUpShowModels().isEmpty()) {
            int i = 0;
            PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 1 && Objects.equals(firstData.getKey(), "Rewards")) {
                i = 1;
                firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            }
            switch (firstData.getKey()) {
                case "Rewards":
                    showRewardsPopupNew(firstData.getValue(), context);
                    break;
                case "RewardsBounce":
                    showBonusRewardsPopup(firstData.getValue(), context);
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        }
    }
}
