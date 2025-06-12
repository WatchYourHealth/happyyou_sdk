package com.wyh.happyyousdk.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.CustomYesNoDialogBinding;

public class CustomYesNoDialog extends Dialog {

    Context context;
    boolean isBlueBackground = false;
    public CustomYesNoDialogBinding binding;

    public CustomYesNoDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public CustomYesNoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public CustomYesNoDialog(@NonNull Context context, int themeResId, boolean isBlueBackground) {
        super(context, themeResId);
        this.context = context;
        this.isBlueBackground = isBlueBackground;
    }

    public CustomYesNoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_yes_no_dialog, null, false);
        setContentView(binding.getRoot());
        if (isBlueBackground)
            getWindow().getDecorView().setBackgroundResource(R.drawable.custom_pop_up_style_red);
        else
            getWindow().getDecorView().setBackgroundResource(R.drawable.custom_pop_up_style);
    }
}
