package com.wyh.happyyousdk.ehr;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityHealthRecordTypeWiseReportBinding;
import com.wyh.happyyousdk.ehr.Interface.ItemRemove;
import com.wyh.happyyousdk.ehr.adapter.EhrAllRecordsAdapter;
import com.wyh.happyyousdk.model.request.ehr.HealthRecordIdRequest;
import com.wyh.happyyousdk.model.response.ehr.AllHealthRecordDetailsList;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthRecordTypeWiseReportActivity extends AppCompatActivity implements ItemRemove {
    ActivityHealthRecordTypeWiseReportBinding binding;
    Context context;
    EhrAllRecordsAdapter ehrAllRecordsAdapter;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    AllHealthRecordDetailsList ehrReportResponseList;
    String viewType = "share", toolbarName;
    public static String healthRecordTypeName;
    int healthRecordId;

    @Override
    protected void onResume() {
        if (healthRecordId != 0) {
            getEhrReportList();
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_health_record_type_wise_report);
        context = this;

        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        healthRecordId = this.getIntent().getIntExtra("healthRecordId", 0);
        healthRecordTypeName = this.getIntent().getStringExtra("healthRecordName");
        toolbarName = this.getIntent().getStringExtra("toolbar");

        if(healthRecordTypeName.equalsIgnoreCase("FaceScan")){
            binding.btnAddRecord.setVisibility(View.GONE);
        }else{
            binding.btnAddRecord.setVisibility(View.VISIBLE);
        }

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "HealthLockerHistory");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText(toolbarName);

        binding.btnDelete.setOnClickListener(view -> {
            if (viewType.equals("delete")) {
                viewType = "share";
                binding.btnDelete.setText("Delete");
            } else {
                viewType = "delete";
                binding.btnDelete.setText("Share/Download");
            }
//            ehrAllRecordsAdapter.notifyDataSetChanged();
            ehrAllRecordsAdapter.notifyDataSetChanged();
//            ehrAllRecordsAdapter = new EhrAllRecordsAdapter(context, allEhrReportResponseList.getData(), viewType);
            setReportRecyclerView(ehrReportResponseList);
        });
        binding.ivHome.setOnClickListener(view -> {
            Intent i = new Intent(this, NewDashboardActivity.class);
            startActivity(i);
            finish();
        });

        binding.btnAddRecord.setOnClickListener(view -> {
                    Intent i = new Intent(this, AddNewEhrRecord.class);
                    i.putExtra("viewType", healthRecordTypeName);
                    startActivity(i);
                }
        );

        if (healthRecordId != 0) {
            getEhrReportList();
        }
    }

    @Override
    public void onItemRemove(int position) {
//        Log.v("Data", position + "Item removed reached!");
        //imagePdfList.remove(position);
        getEhrReportList();

        ehrAllRecordsAdapter.notifyItemChanged(position);
        //imagePdfAdapter.notifyItemRangeChanged(position, imagePdfList.size());
        ehrAllRecordsAdapter.notifyItemRangeChanged(position, ehrReportResponseList.getData().size());
        //notifyAll();
    }

    private void getEhrReportList() {
        progressDialog.show();
        HealthRecordIdRequest healthRecordIdRequest = new HealthRecordIdRequest(healthRecordId);

        Call<AllHealthRecordDetailsList> call = apiInterfaceWyh.fetchEhrHealthRecordsByType(SharedPref.getAuthToken(), healthRecordIdRequest);
        //Log.v("Ehr_Multipart", call.request().url().toString() + "\n" + SharedPref.getAuthToken() + ", " + SharedPref.getUuid());

        call.enqueue(new Callback<AllHealthRecordDetailsList>() {
            @Override
            public void onResponse(Call<AllHealthRecordDetailsList> call, Response<AllHealthRecordDetailsList> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //Log.v("Ehr_Multipart", response.code() + "," + new Gson().toJson(response.body()));
                if (response.body() != null && response.body().getSuccess() && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_health_locker_by_type_success));
                    ehrReportResponseList = response.body();

                    if (ehrReportResponseList.getData().size() > 0) {
                        binding.rvReportList.setVisibility(View.VISIBLE);
                        binding.btnDelete.setVisibility(View.VISIBLE);
                        setReportRecyclerView(ehrReportResponseList);
                        binding.llNoRecordFound.setVisibility(View.GONE);
                    } else {
                        binding.btnDelete.setVisibility(View.GONE);
                        binding.llNoRecordFound.setVisibility(View.VISIBLE);
                        binding.rvReportList.setVisibility(View.GONE);
                        binding.svReportName.setActivated(true);
                        binding.svReportName.setQueryHint("What are you looking for?");
                        binding.svReportName.onActionViewExpanded();
                        binding.svReportName.setIconified(false);
                        binding.svReportName.clearFocus();
                    }
//                    reportType.setSelection(0);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_health_locker_by_type_failed));
                    binding.rvReportList.setVisibility(View.GONE);
                    binding.llNoRecordFound.setVisibility(View.VISIBLE);
                    binding.btnDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AllHealthRecordDetailsList> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_health_locker_by_type_failed));
                }
//                Log.v("Ehr Response Err", t.getMessage());
            }
        });
    }

    private void setReportRecyclerView(AllHealthRecordDetailsList allEhrReportResponseList) {
        ehrAllRecordsAdapter = new EhrAllRecordsAdapter(context, allEhrReportResponseList.getData(), viewType, this::onItemRemove, "HealthRecordTypeWiseReportActivity",binding);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvReportList.setLayoutManager(linearLayoutManager);
        binding.rvReportList.setAdapter(ehrAllRecordsAdapter);
        searchResultFilter(ehrAllRecordsAdapter);
    }

    private void searchResultFilter(EhrAllRecordsAdapter ehrAllRecordsAdapter) {
        binding.svReportName.setActivated(true);
        binding.svReportName.setQueryHint("What are you looking for?");
        binding.svReportName.onActionViewExpanded();
        binding.svReportName.setIconified(false);
         binding.svReportName.clearFocus();
        binding.svReportName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ehrAllRecordsAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}