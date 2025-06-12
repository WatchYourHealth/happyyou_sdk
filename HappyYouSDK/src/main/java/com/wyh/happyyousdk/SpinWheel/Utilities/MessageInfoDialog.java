package com.wyh.happyyousdk.SpinWheel.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.DialogNextSpinInfoBinding;
import com.wyh.happyyousdk.databinding.MessageInfoBinding;
import com.wyh.happyyousdk.model.response.postloginreward.BurnSpinData;
import com.wyh.happyyousdk.model.response.postloginreward.FreeSpinData;

public class MessageInfoDialog extends Dialog {

    MessageInfoBinding binding;

    onRewardDialogClick rewardDialogClick;

    public MessageInfoDialog(@NonNull Context context, Drawable infoIcon, String InfoTitle, String InfoMessage) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.message_info, null, false);
        setContentView(binding.getRoot());


        this.rewardDialogClick = rewardDialogClick;

        setCancelable(true);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        binding.ivInfoIcon.setImageDrawable(infoIcon);
        binding.lblInformation.setText(InfoTitle);
        binding.tvInformation.setText(InfoMessage);

        if (binding.tvInformation.getText().toString().startsWith("Quiz ")) {
            binding.tvInformation.setTextColor(ContextCompat.getColor(context, R.color.black));
        }


        binding.ivClose.setOnClickListener(v -> {
            dismiss();
        });

    }
}
