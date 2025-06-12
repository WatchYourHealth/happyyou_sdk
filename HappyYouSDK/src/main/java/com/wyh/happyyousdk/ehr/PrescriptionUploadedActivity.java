package com.wyh.happyyousdk.ehr;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;

import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityPrescriptionUploadedBinding;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.PopUpScratchCardScratchableBinding;
import com.wyh.happyyousdk.databinding.ScratchCardPopUpBinding;
import com.wyh.happyyousdk.ehr.Interface.ItemRemove;
import com.wyh.happyyousdk.ehr.adapter.FileUploadListAdapter;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.response.ehr.UploadFileResponse;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.VoucherIdRequest;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;

import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionUploadedActivity extends AppCompatActivity implements ItemRemove, ScratchListener, rewardDialogCloseListener {
    Context context;
    UploadFileResponse uploadFileResponse;
    FileUploadListAdapter fileUploadListAdapter;
    ActivityPrescriptionUploadedBinding binding;
    String reportName, rewards, rewardsBonus, stamp, stampBonus;
    String feedbackResponse = "";
    FreeVoucher freeVoucher;
    AlertDialog alertDialogBonusRewards, alertDialogStamp, alertDialogBonusStamp;
    ApiInterfaceWyh apiInterfaceWyh;

    VoucherIdRequest voucherIdRequest;
    boolean isRewards, isPositiveBtn = false;
    boolean isStamp = false;
    int stampId = -1;

    public static String reportNametoshare ="";
    public static String  reporttitle ="";
    QuizathonRewardData quizreward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prescription_uploaded);
        context = this;
        SharedPref.init(context);

        reportName = this.getIntent().getStringExtra("reportname");
        reportNametoshare =reportName;
        reporttitle = this.getIntent().getStringExtra("reporttitle");
        rewards = this.getIntent().getStringExtra("rewards");
        rewardsBonus = this.getIntent().getStringExtra("rewardsBonus");
        stamp = this.getIntent().getStringExtra("stamp");
        stampBonus = this.getIntent().getStringExtra("stampBonus");
        feedbackResponse = this.getIntent().getStringExtra("feedback");

        if(!Objects.equals(feedbackResponse, "")){
            if(AddNewEhrRecord.feedbackResponseData != null && AddNewEhrRecord.feedbackResponseData.getStarConfig() != null && AddNewEhrRecord.feedbackResponseData.getFeedbackModel() != null){
                FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                instance.showPopUpFeedback(PrescriptionUploadedActivity.this, AddNewEhrRecord.feedbackResponseData);
            }

        }

        if (rewards != null) {
            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, rewards));
        }
        if(rewardsBonus != null){
            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, rewardsBonus));
        }
        if (stamp != null) {
            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, stamp));
        }
        if(stampBonus != null){
            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, stampBonus));
        }

        freeVoucher = new Gson().fromJson(this.getIntent().getStringExtra("voucher"), FreeVoucher.class);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        if (checkIsFromQuizqathon()) {
            //QuizReward Api Call
            FetchQuizReward();
        }
        binding.includeToolbar.llBack.setOnClickListener(view -> onBackPressed());
        binding.includeToolbar.tvBack.setText("Add New Record");

        uploadFileResponse = new Gson().fromJson(SharedPref.getUploadEhrResponse(), UploadFileResponse.class);

        setUploadedFileData(uploadFileResponse.getData());

        binding.tvPrescriptionMsg.setText("Your " + reportName + " were uploaded");

        if (freeVoucher != null) {
            if(freeVoucher.getVoucherDescription() != null){
                showScratchCard(freeVoucher);
            }
        }

        binding.btnOk.setOnClickListener(view -> {
            if (rewards != null) {
                showRewardsPopupDialogBox();
            } else  if(rewardsBonus != null){
                showRewardsPopupDialogBox();
            }else  if(stamp != null){
                showRewardsPopupDialogBox();
            }else  if(stampBonus != null){
                showRewardsPopupDialogBox();
            }else {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (rewards != null) {
            showRewardsPopupDialogBox();
        } else  if(rewardsBonus != null){
            showRewardsPopupDialogBox();
        }else  if(stamp != null){
            showRewardsPopupDialogBox();
        }else  if(stampBonus != null){
            showRewardsPopupDialogBox();
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public void onItemRemove(int position) {
//        Log.v("Data", position + "Item removed reached!");
        //imagePdfList.remove(position);
        fileUploadListAdapter.notifyItemChanged(position);
        //imagePdfAdapter.notifyItemRangeChanged(position, imagePdfList.size());
        fileUploadListAdapter.notifyItemRangeChanged(position, uploadFileResponse.getData().size());
        //notifyAll();
    }

    private void showRewardsPopup(String rewards) {
        isRewards = true;
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

        binding.btnNegative.setOnClickListener(view -> {
            alertDialog.dismiss();
            finish();
        });
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, RewardsActivity.class);
            context.startActivity(intent);
            finish();
        });
        binding.scratchView.onFullReveal();
        binding.scratchView.setScratchListener(PrescriptionUploadedActivity.this);

        Rect displayRectangle = new Rect();
        Window window = ((PrescriptionUploadedActivity) context).getWindow();

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

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 3000);

         alertDialog.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            }else{
                PrescriptionUploadedActivity.this.rewards = null;
                showRewardsPopupDialogBox();
            }
        });

        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);
        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        binding.scratchView.onFullReveal();

        binding.btnPositive.setOnClickListener(view -> {
           isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(PrescriptionUploadedActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

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

         alertDialogBonusRewards.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            }else{
                rewardsBonus = null;
                showRewardsPopupDialogBox();
            }
        });

        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);

        binding.btnPositive.setOnClickListener(view -> {
           isPositiveBtn = true;
            alertDialogBonusRewards.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(PrescriptionUploadedActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PrescriptionUploadedActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusRewards.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showStampsPopup(String rewards, Context context) {
        isStamp = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogStamp = alertBuilder.create();
        alertDialogStamp.setCancelable(true);
        if (!alertDialogStamp.isShowing())
            alertDialogStamp.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if(rewards.split(";").length == 4){
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

         alertDialogStamp.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            }else{
                stamp = null;
                showRewardsPopupDialogBox();
            }
        });

        /*if(isOrange){
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));
        }else{
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));
        }*/
//        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: "+points);


        /*if (rewards.contains("First Login")) {
            binding.btnNegative.setVisibility(View.GONE);
            binding.btnPositive.setText("OK");
            binding.btnPositive.setOnClickListener(view -> {
                alertDialog.dismiss();
                Intent intent = new Intent(context, SyncDeviceActivity.class);
                startActivity(intent);
            });
        } else {

        }*/

//        binding.scratchView.onFullReveal();


        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogStamp.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(PrescriptionUploadedActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PrescriptionUploadedActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        isStamp = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusStamp = alertBuilder.create();
        alertDialogBonusStamp.setCancelable(true);
        if (!alertDialogBonusStamp.isShowing())
            alertDialogBonusStamp.show();

         alertDialogBonusStamp.setOnDismissListener(dialogInterface -> {
            if(isPositiveBtn){
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            }else{
                stampBonus = null;
                showRewardsPopupDialogBox();
            }
        });
        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if(rewards.split(";").length == 4){
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: "+points);

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusStamp.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(PrescriptionUploadedActivity.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((PrescriptionUploadedActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showRewardsPopupDialogBox(){
        if(NewDashboardHelper.Companion.getPopUpShowModels().size() > 0){
            int i = 0;
            PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            if(NewDashboardHelper.Companion.getPopUpShowModels().size() > 1 && Objects.equals(firstData.getKey(), "Rewards")){
                i = 1;
                firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            }
            switch (firstData.getKey()){
                case "Rewards":
                    showRewardsPopupNew(firstData.getValue(), context);
                    break;
                case "RewardsBounce":
                    showBonusRewardsPopup(firstData.getValue(), context);
                    break;
                case "TokenStamp":
                    showStampsPopup(firstData.getValue(), context);
                    break;
                case "TokenStampBounce":
                    showBonusStampPopup(firstData.getValue(), context);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        }
    }

    private void showScratchCard(FreeVoucher freeVoucher) {
        isRewards = false;

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        PopUpScratchCardScratchableBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pop_up_scratch_card_scratchable, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        if (!freeVoucher.isScratched())
            voucherIdRequest = new VoucherIdRequest(freeVoucher.getFreebieID());

        binding.scratchView.setScratchListener(PrescriptionUploadedActivity.this);
        binding.tvTitle.setText(freeVoucher.getVendorName());
        binding.tvValue.setText(freeVoucher.getVoucherValue() + " off");
        binding.tvDescription.setText(freeVoucher.getVoucherDescription());
        binding.tvCouponCode.setText(freeVoucher.getVoucherCode());
        binding.tvAmount.setText("₹" + freeVoucher.getVoucherValue());


        binding.btnRedeem.setOnClickListener(view -> alertDialog.dismiss());

        binding.tvCopy.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", freeVoucher.getVoucherCode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
        });

        Glide.with(context)
                .load(freeVoucher.getVendorLogo())
                .error(R.drawable.dummy_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivVendorLogo);

        Rect displayRectangle = new Rect();
        Window window = ((PrescriptionUploadedActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.5f));
    }

    private void setUploadedFileData(List<UploadFileResponse.Datum> uploadedFileDataResponse) {
        fileUploadListAdapter = new FileUploadListAdapter(context, uploadedFileDataResponse, reportName, this::onItemRemove, "PrescriptionUploadedActivity");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvReportList.setLayoutManager(linearLayoutManager);
        binding.rvReportList.setAdapter(fileUploadListAdapter);
    }

    @Override
    public void onScratchComplete() {
        if (quizreward != null) {
            QuizRewardDialog.INSTANCE.QuizScratchCard(this, quizreward.getTransId(), true);
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        /*if (i >= 50 && voucherIdRequest != null && !isRewards) {
            updateScratchStatus();
            scratchCardLayout.onFullReveal();
        }
        if (i >= 20 && isStamp) {
            isStamp = false;
            scratchTokenReward();
            scratchCardLayout.onFullReveal();
        }*/

        if (i > 20) {

            if (isStamp) {
                isStamp = false;
                scratchTokenReward();
            }

            if(voucherIdRequest != null && !isRewards){
                updateScratchStatus();
            }

            scratchCardLayout.onFullReveal();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(alertDialogBonusRewards!= null && alertDialogBonusRewards.isShowing()) {
                        alertDialogBonusRewards.dismiss();
                    }
                    if((alertDialogBonusStamp!= null && alertDialogBonusStamp.isShowing())){
                        alertDialogBonusStamp.dismiss();
                    }
                    if((alertDialogStamp!= null && alertDialogStamp.isShowing())){
                        alertDialogStamp.dismiss();
                    }
                }
            }, 3000);
        }

    }

    private void updateScratchStatus() {
        Call<CommonSuccessResponse> call = apiInterfaceWyh.updateScratchStatus(SharedPref.getAuthToken(), voucherIdRequest);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_success));
                    voucherIdRequest = null;
                }else{
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_coupon_failed));
            }
        });
    }

    @Override
    public void onScratchStarted() {

    }

    private void scratchTokenReward() {
        RewardsPopupRequest request = new RewardsPopupRequest(stampId);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.scratchTokenReward(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_success));

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getQuizathonRewardPopup(QuizathonRewardData data) {
        try {
            if (data.getRewardType() != null && !data.getRewardType().isEmpty()) {
                String rewardtype = data.getRewardType();
                if (rewardtype.equalsIgnoreCase("Points")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerPointsPopupCallBack(data.getRewardTitle(), context, "Trends", data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Offers")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerOfferCallBack(context, data.getPartnerLogo(), data.getRewardDescription(), data.getPartnerName()
                            , data.getPartnerUrl(), "Trends", data.getExpiryInHours(), data.getRewardTitle(), data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Voucher")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerVoucherCallBack(context, data.getRewardLogo(), data.getCouponCode(), data.getRewardDescription()
                            , data.getRewardTitle(), data.getExpiryInHours(), "Trends", data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Badge")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerBadgePopupCallBack(context, "Trends", data.getRewardLogo(), data.getRewardTitle(), data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Stamps")) {
                    QuizRewardDialog.INSTANCE.showStampsPopupCallBack(data.getRewardHeader1(), data.getRewardHeader2(), data.getRewardTitle(), data.getRewardValue(), this, this, this);
                }
                else if (rewardtype.equalsIgnoreCase("future")) {
                    QuizRewardDialog.INSTANCE.showFutureRewardDialog(context,data.getDialogModel(),data.getClaimDate());
                }

            }
        } catch (Exception ex) {

        }
    }

    private void FetchQuizReward() {
        ActivityRewardRequest activityRewardRequest = new ActivityRewardRequest(NewDashboardHelper.Companion.getTrasactionId(),NewDashboardHelper.Companion.getFeatureName());
        Call<CommonSuccessResponse> call = apiInterfaceWyh.FetchQuizReward(SharedPref.getAuthToken(), activityRewardRequest);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewDashboardHelper.Companion.setTrasactionId(null);
                    NewDashboardHelper.Companion.setFeatureName(null);
                    NewDashboardHelper.Companion.setActivityName(null);
                    if (response.body().getQuizathonRewardData() != null) {
                        quizreward=response.body().getQuizathonRewardData();
                        getQuizathonRewardPopup(response.body().getQuizathonRewardData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public  boolean checkIsFromQuizqathon(){
        if(NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()){
            return true;
        }
        return false;
    }



    public void cancelDialog() {
        try {
            NudgeDialogue.INSTANCE.spinnerCancelDialog(context, quizreward.getRewardType(), this);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogDismiss() {
        cancelDialog();
    }
}