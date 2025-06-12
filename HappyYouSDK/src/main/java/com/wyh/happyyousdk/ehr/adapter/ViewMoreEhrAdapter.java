package com.wyh.happyyousdk.ehr.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ViewMoreEhrListAdapterBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.response.ehr.UploadFileResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMoreEhrAdapter extends RecyclerView.Adapter<ViewMoreEhrAdapter.MyViewHolder>{


    Context context;
    List<UploadFileResponse.Datum> dataList;
    String reportName;
    /*ItemRemove itemRemove;
    String activityName;
    String filename;*/

    public ViewMoreEhrAdapter(Context context, List<UploadFileResponse.Datum> dataList, String reportName) {
        this.context = context;
        this.dataList = dataList;
        this.reportName = reportName;
    }

    @NonNull
    @Override
    public ViewMoreEhrAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewMoreEhrListAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_more_ehr_list_adapter, parent, false);
        return new ViewMoreEhrAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMoreEhrAdapter.MyViewHolder holder, int position) {
        UploadFileResponse.Datum data = dataList.get(position);
        holder.binding.tvRecordName.setText(data.getHealthRecordID()+"");
        holder.binding.tvRecordType.setText(reportName);
        holder.binding.tvRecordDate.setText(data.getCreatedOn().split(" ")[0]);
        holder.binding.btnDelete.setOnClickListener(view -> deleteRecords(data));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ViewMoreEhrListAdapterBinding binding;


        public MyViewHolder(@NonNull ViewMoreEhrListAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void deleteRecords(UploadFileResponse.Datum request) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);

        progressDialog.show();


        Call<CommonSuccessResponse> call = apiInterfaceWyh.deleteHealthRecord(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().isSuccess() && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.update_health_locker_success));
                    Toast.makeText(context, "Data Delete Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.update_health_locker_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.update_health_locker_failed));
                }
            }
        });
    }
}
