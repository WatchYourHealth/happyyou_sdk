package com.wyh.happyyousdk.SpinWheel.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.DialogNextSpinInfoBinding;
import com.wyh.happyyousdk.model.response.postloginreward.BurnSpinData;
import com.wyh.happyyousdk.model.response.postloginreward.FreeSpinData;

public class NextSpininfoDialog extends Dialog {

    DialogNextSpinInfoBinding binding;

    onRewardDialogClick rewardDialogClick;
    BurnSpinData burnSpinData;
    FreeSpinData freeSpinData;

    public NextSpininfoDialog(@NonNull Context context, boolean isShowFreeSpinMessage, FreeSpinData freeSpinData, BurnSpinData burnSpinData, onRewardDialogClick rewardDialogClick) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_next_spin_info, null, false);
        setContentView(binding.getRoot());

        this.freeSpinData = freeSpinData;
        this.burnSpinData = burnSpinData;
        this.rewardDialogClick = rewardDialogClick;
        //setContentView(R.layout.dialog_next_spin_info);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        if (!isShowFreeSpinMessage) {
            if (!burnSpinData.isHasSufficientBalance()) {
                binding.btnEvent.setVisibility(View.GONE);
            } else {
                binding.btnEvent.setVisibility(View.VISIBLE);
                binding.lblInformation.setText("Confirmation");
                binding.btnEvent.setText("Spin with " + burnSpinData.getPointsToBurn() + " HY Points.");
            }
            binding.tvInformation.setText(burnSpinData.getPopupMessage());
        } else {
            binding.btnEvent.setVisibility(View.GONE);
            binding.tvInformation.setText(freeSpinData.getFreeSpinMessage());
        }
        binding.btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APILogs.INSTANCE.activityTracker("A_SPINDASHBOARD__BURNHYPOINT_POPUP_SPINTAPPED",context);
                rewardDialogClick.onStartClick();
            }
        });

        binding.ivClose.setOnClickListener(v -> {
            dismiss();
        });

    }
}
