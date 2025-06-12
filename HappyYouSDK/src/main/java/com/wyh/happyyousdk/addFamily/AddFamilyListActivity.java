package com.wyh.happyyousdk.addFamily;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.addFamily.adapter.AddFamilyListAdapter;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityAddFamilyListBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.addFamily.AddFamilyRequest;
import com.wyh.happyyousdk.model.response.AddFamilyResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFamilyListActivity extends AppCompatActivity implements AddFamilyListAdapter.OnItemClickListener{

    ActivityAddFamilyListBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    List<AddFamilyRequest> familyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_family_list);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");


        binding.includeBack.tvBack.setText("Add Family Member");
        binding.includeBack.llBack.setOnClickListener(view -> finish());

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.btnaddFamily.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddFamilyActivity.class);
            intent.putExtra("data", new Gson().toJson(familyList));
            startActivity(intent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getFamilyList();
    }


    private void showPopupDelete(int id) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        View view = LayoutInflater.from(context).inflate(R.layout.delete_popup, null);
        alertBuilder.setView(view);
        Button btn_yes = view.findViewById(R.id.btnOK);
        Button btn_no = view.findViewById(R.id.btnCancel);
        TextView tvMsg = view.findViewById(R.id.tvMsg);

        tvMsg.setText("Are you sure want to Delete this family member from your list?");
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
        btn_yes.setOnClickListener(view1 -> {
            Log.d("journalId", "" + id);
            alertDialog.dismiss();
            deleteFamily(id);
        });
        btn_no.setOnClickListener(view1 -> {
            alertDialog.dismiss();
        });

        Rect displayRectangle = new Rect();
        Window window;

        window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.45f));
    }

    private void getFamilyList() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<AddFamilyResponse> call = apiInterfaceWyh.getClientFamilyDetails(SharedPref.getAuthToken());
        call.enqueue(new Callback<AddFamilyResponse>() {
            @Override
            public void onResponse(Call<AddFamilyResponse> call, Response<AddFamilyResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    familyList = new ArrayList<>();
                    familyList.clear();
                    if(response.isSuccessful() && response.body().getData() != null){
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.family_list_success));
                        List<AddFamilyRequest> dataList = new ArrayList<>();
                        dataList.add(new AddFamilyRequest(0, SharedPref.getUserName(), "You", SharedPref.getDecryptMobileNo()));
                        dataList.addAll(response.body().getData());
                        familyList.addAll(dataList);
                        setData(dataList);
                    }else{
                        List<AddFamilyRequest> dataList = new ArrayList<>();
                        dataList.add(new AddFamilyRequest(0, SharedPref.getUserName(), "You", SharedPref.getDecryptMobileNo()));
                        familyList.addAll(dataList);
                        setData(dataList);
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.family_list_failed));
                }
            }

            @Override
            public void onFailure(Call<AddFamilyResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.family_list_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFamily(int id) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        AddFamilyRequest request = new AddFamilyRequest(id, "","","");
        Call<CommonSuccessResponse> call = apiInterfaceWyh.deletePolicyDetails(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.delete_family_success));
                    Toast.makeText(context, "Family member deleted successfully", Toast.LENGTH_SHORT).show();
                    getFamilyList();
                } else {
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.delete_family_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.delete_family_failed));
            }
        });
    }


    private void setData(List<AddFamilyRequest> listData){
        AddFamilyListAdapter levelActivitiesAdapter = new AddFamilyListAdapter(context, listData, this);
        binding.rvList.setAdapter(levelActivitiesAdapter);
    }

    @Override
    public void onClick(int position, AddFamilyRequest data) {
        showPopupDelete(data.getId());
    }
}