package com.wyh.happyyousdk.ehr.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ViewMoreEhrItemBinding;
import com.wyh.happyyousdk.databinding.ViewMoreEhrListAdapterBinding;
import com.wyh.happyyousdk.ehr.ImageReaderActivity;
import com.wyh.happyyousdk.ehr.PdfReaderActivity;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.response.ehr.AllHealthRecordDetailsList;
import com.wyh.happyyousdk.model.response.ehr.UploadFileResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMoreEhrItemAdapter extends RecyclerView.Adapter<ViewMoreEhrItemAdapter.MyViewHolder>{


    Context context;
    List<String> dataList;


    public ViewMoreEhrItemAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewMoreEhrItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewMoreEhrItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_more_ehr_item, parent, false);
        return new ViewMoreEhrItemAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMoreEhrItemAdapter.MyViewHolder holder, int position) {
        String data = dataList.get(position);

        String[] bits = data.split("/");
        String lastOne = bits[bits.length-1];
        holder.binding.tvRecordName.setText(lastOne);
        holder.binding.ivView.setOnClickListener(view -> {
            Intent intent;
            if(data.contains(".pdf")){
                intent = new Intent(context, PdfReaderActivity.class);
            }else{
                intent = new Intent(context, ImageReaderActivity.class);
            }
            intent.putExtra("image", data);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ViewMoreEhrItemBinding binding;


        public MyViewHolder(@NonNull ViewMoreEhrItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
