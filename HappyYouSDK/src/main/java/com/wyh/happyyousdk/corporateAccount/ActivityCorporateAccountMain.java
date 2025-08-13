package com.wyh.happyyousdk.corporateAccount;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityCorporateAccountDetailBinding;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

public class ActivityCorporateAccountMain extends AppCompatActivity {
    ActivityCorporateAccountDetailBinding binding;
    Context context;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_corporate_account_detail);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        binding.tvCorporateName.setText(SharedPref.getCorporateName());

        //Changed RL BG
        Glide.with(context)
                .asBitmap()
                .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "bg_corporate_main_new.png")
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(getResources(), resource);
                        binding.rlMain.setBackground(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        Glide.with(context)
                .asBitmap()
                .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "bg_corporate_email.png")
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(getResources(), resource);
                        binding.llSection2.setBackground(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        Glide.with(context)
                .load(SharedPref.getCorporateImage())
                .into(binding.corporateLog);
        binding.tvChangeCorporate.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_CHANGE_CORPORATE", context);
            showCorporateAlert();
        });
        binding.rlUpperCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void showCorporateAlert() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_corporate_account, null, false);

        // Build the dialog
        dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        Button btnYes = dialogView.findViewById(R.id.btnYes);
        Button btnNo = dialogView.findViewById(R.id.btnNo);
        tvTitle.setText("Are you Sure Do You Want To Change \nYour Corporate?");
        btnYes.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_CONFIRM_YES", context);
            SharedPref.putCorporateAccountNotFound(false);
            dismissDialog();
            Intent intent = new Intent(context, ActivityCorporateAccountAddEdit.class);
            startActivity(intent);
        });
        btnNo.setOnClickListener(view -> {
            APILogs.INSTANCE.activityTracker("Android_POST_LOGIN_CORPORATE_CONFIRM_NO", context);
            SharedPref.putCorporateAccountNotFound(false);
            dismissDialog();
        });

        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                //dialog.setCancelable(true);
                //dialog.dismiss();  // Dismiss the dialog
                SharedPref.putCorporateAccountNotFound(false);
                dismissDialog();// Optionally close the activity
                return true;
            }
            return false;
        });
        dialog.show();

    }

    void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, NewDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
