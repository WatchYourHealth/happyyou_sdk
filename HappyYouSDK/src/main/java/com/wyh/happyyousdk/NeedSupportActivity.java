package com.wyh.happyyousdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;

import com.google.gson.Gson;
;
import com.wyh.happyyousdk.APIEncryption.APILogs;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityNeedSupportBinding;
import com.wyh.happyyousdk.databinding.ActivityQuizBinding;
import com.wyh.happyyousdk.diary.AddDiaryActivity;
import com.wyh.happyyousdk.profile.ProfileActivity;
import com.wyh.happyyousdk.utils.CustomYesNoDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class NeedSupportActivity extends AppCompatActivity {

    ActivityNeedSupportBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_need_support);
        setToolBar();
        context = this;

        binding.btnPositive.setOnClickListener(v -> {
            onBackPressed();
        });

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "NeedSupport");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setToolBar() {
        binding.includeBack.tvBack.setText("Need Support");
        binding.includeBack.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeBack.ivBack.setColorFilter(getResources().getColor(R.color.white));
        binding.includeBack.ivMenu.setVisibility(View.VISIBLE);
        binding.includeBack.ivMenu.setColorFilter(getResources().getColor(R.color.white));
        binding.includeBack.ivMenu.setOnClickListener(view -> showPopup(binding.includeBack.ivMenu));
        binding.includeBack.llBack.setOnClickListener(v -> onBackPressed());
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_need_support, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_delete_account) {
                CustomYesNoDialog customYesNoDialog = new CustomYesNoDialog(context, R.style.Theme_Dialog);
                customYesNoDialog.show();
                customYesNoDialog.setCancelable(false);
                customYesNoDialog.binding.llActionRequired.setVisibility(View.VISIBLE);
                customYesNoDialog.binding.popupIcon.setVisibility(View.GONE);
                customYesNoDialog.binding.popupHeading.setVisibility(View.GONE);
                customYesNoDialog.binding.txtInfoPopUpDesc.setText("Are you sure you want to delete your account?");
                customYesNoDialog.binding.btnCancel.setText("No");
                customYesNoDialog.binding.btnYes.setText("Yes");
                customYesNoDialog.binding.btnYes.setBackground(ContextCompat.getDrawable(context, R.drawable.blue_rc_bg_8dp));
                customYesNoDialog.binding.btnCancel.setBackground(ContextCompat.getDrawable(context, R.drawable.pink_rc_bg_8dp));
                customYesNoDialog.binding.btnYes.setOnClickListener(view -> {
                    customYesNoDialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:"+getResources().getString(R.string.kotak_email));
                    intent.setData(data);
                    startActivity(intent);
                });
                customYesNoDialog.binding.btnCancel.setOnClickListener(view -> customYesNoDialog.dismiss());
            }
            return false;
        });
    }
}