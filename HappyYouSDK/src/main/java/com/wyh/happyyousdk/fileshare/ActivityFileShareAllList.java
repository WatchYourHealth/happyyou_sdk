package com.wyh.happyyousdk.fileshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityAllFileShareListBinding;
import com.wyh.happyyousdk.model.response.FileShareResp;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFileShareAllList extends AppCompatActivity {
    ActivityAllFileShareListBinding binding;
    Context context;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_file_share_list);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);


        apiInterfaceWyh = RetrofitHandler.apiInterface();
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        binding.llMain.setOnClickListener(v -> {
            onBackPressed();
        });
        type=getIntent().getStringExtra("type");
        binding.tvHeadTitle.setText(type);
        GetShareFileList();
    }
    private void GetShareFileList() {
        try {
            CommonUtils.dismissDialoge();
            CommonUtils.showProgressDialige(context);
            apiInterfaceWyh.GetFilePopup(SharedPref.getAuthToken()).enqueue(new Callback<FileShareResp>() {
                @Override
                public void onResponse(@NonNull Call<FileShareResp> call, @NonNull Response<FileShareResp> response) {
                    CommonUtils.dismissDialoge();
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getData() != null) {
                            setData(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FileShareResp> call, @NonNull Throwable t) {
                    CommonUtils.dismissDialoge();


                }
            });
        } catch (Exception e) {
            CommonUtils.dismissDialoge();

        }
    }
    public void setData(FileShareResp rewardResponse) {
        List<FileShareResp.Data> jsonList = rewardResponse.getData()
                .stream()
                .filter(FileShareResp.Data::getIsExpired)  // Keep only items where isExpired == false
                .collect(Collectors.toList());


        List<FileShareResp.Data> jsonList2 = rewardResponse.getData()
                .stream()
                .filter(item -> !item.getIsExpired())  // Keep only items where isExpired == false
                .collect(Collectors.toList());
        if (!jsonList2.isEmpty()&&type.equalsIgnoreCase("Active")) {
            binding.rlActive.setVisibility(View.VISIBLE);
            binding.gvCardFileShare.setVisibility(View.VISIBLE);
            FileShareGridAllList gridCardsAdapter = new FileShareGridAllList(context,jsonList2);
            binding.gvCardFileShare.setColumnWidth(3);
            binding.gvCardFileShare.setAdapter(gridCardsAdapter);

        }

        if (!jsonList.isEmpty()&&type.equalsIgnoreCase("Expired")) {
            binding.gvCardFileShare.setVisibility(View.VISIBLE);
            FileShareGridAllList gridCardsAdapter = new FileShareGridAllList(context,jsonList);
              binding.gvCardFileShare.setColumnWidth(3);
              binding.gvCardFileShare.setAdapter(gridCardsAdapter);
        }


    }
}
