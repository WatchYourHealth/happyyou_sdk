package com.wyh.happyyousdk.ehr;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.SearchKey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;

;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityHealthLockerHistoryBinding;
import com.wyh.happyyousdk.ehr.Interface.ItemRemove;
import com.wyh.happyyousdk.ehr.adapter.EhrAllRecordsAdapter;
import com.wyh.happyyousdk.model.response.ehr.AllHealthRecordDetailsList;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthLockerHistoryActivity extends AppCompatActivity implements ItemRemove {
    ActivityHealthLockerHistoryBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    AllHealthRecordDetailsList allEhrReportResponseList;
    String viewType = "share";

    String searchKey = "";
    EhrAllRecordsAdapter ehrAllRecordsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_health_locker_history);
        context = this;

        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.la404Bear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_404.json");

        binding.includeToolbar.llBack.setOnClickListener(view -> finish());
        binding.includeToolbar.tvBack.setText("Health Locker History");

        searchKey = getIntent().getStringExtra(SearchKey);

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "HealthLockerHistory");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        



        binding.btnAddRecord.setOnClickListener(view -> {
            //binding.svReportName.setQuery("",false);
            Intent i = new Intent(this, AddNewEhrRecord.class);
            i.putExtra("viewType", "all");
            startActivity(i);
        });

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.btnDelete.setOnClickListener(view -> {
            binding.svReportName.setQuery("",false);
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
            setReportRecyclerView(allEhrReportResponseList);
        });

        getAllEhrList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllEhrList();
    }

    @Override
    public void onItemRemove(int position) {
//        Log.v("Data", position + "Item removed reached!");
        //imagePdfList.remove(position);
        getAllEhrList();

        ehrAllRecordsAdapter.notifyItemChanged(position);
        //imagePdfAdapter.notifyItemRangeChanged(position, imagePdfList.size());
        ehrAllRecordsAdapter.notifyItemRangeChanged(position, allEhrReportResponseList.getData().size());
        //notifyAll();
    }

    private void getAllEhrList() {
        progressDialog.show();

        Call<AllHealthRecordDetailsList> call = apiInterfaceWyh.fetchAllHealthRecords(SharedPref.getAuthToken());
        //Log.v("Ehr_Multipart", call.request().url().toString() + "\n" + SharedPref.getAuthToken() + ", " + SharedPref.getUuid());

        call.enqueue(new Callback<AllHealthRecordDetailsList>() {
            @Override
            public void onResponse(Call<AllHealthRecordDetailsList> call, Response<AllHealthRecordDetailsList> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //Log.v("Ehr_Multipart", response.code() + "," + new Gson().toJson(response.body()));
                if (response.body() != null && response.body().getSuccess() && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_health_locker_success));
                    binding.btnDelete.setVisibility(View.VISIBLE);

                    allEhrReportResponseList = response.body();

                    if (allEhrReportResponseList.getData().size() > 0) {
                        binding.rvReportList.setVisibility(View.VISIBLE);
                        setReportRecyclerView(allEhrReportResponseList);
                        binding.llNoRecordFound.setVisibility(View.GONE);
                    } else {
                        binding.llNoRecordFound.setVisibility(View.VISIBLE);
                        binding.rvReportList.setVisibility(View.GONE);

                    }
//                    reportType.setSelection(0);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_health_locker_failed));
                    binding.rvReportList.setVisibility(View.GONE);
                    binding.llNoRecordFound.setVisibility(View.VISIBLE);
                    binding.btnDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AllHealthRecordDetailsList> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_health_locker_failed));
                }
//                Log.v("Ehr Response Err", t.getMessage());
            }
        });
    }

    private void setReportRecyclerView(AllHealthRecordDetailsList allEhrReportResponseList) {
        ehrAllRecordsAdapter = new EhrAllRecordsAdapter(context, allEhrReportResponseList.getData(), viewType, this::onItemRemove, "HealthLockerHistoryActivity",binding);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvReportList.setLayoutManager(linearLayoutManager);
        binding.rvReportList.setAdapter(ehrAllRecordsAdapter);
        searchResultFilter(ehrAllRecordsAdapter, searchKey);
    }



    private void searchResultFilter(EhrAllRecordsAdapter ehrAllRecordsAdapter, String searchKey) {
        binding.svReportName.setActivated(true);

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
                if(newText.isEmpty()){
                    binding.svReportName.setQueryHint("What are you looking for?");
                }
                ehrAllRecordsAdapter.getFilter().filter(newText);


                return false;
            }
        });
        if(searchKey.equalsIgnoreCase("")){
            binding.svReportName.setQueryHint("What are you looking for?");
        }else{
            binding.svReportName.setQuery(searchKey,true);
        }
    }

}