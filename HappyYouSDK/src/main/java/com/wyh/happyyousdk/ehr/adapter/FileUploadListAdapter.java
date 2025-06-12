package com.wyh.happyyousdk.ehr.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.FileUploadListAdapterBinding;
import com.wyh.happyyousdk.ehr.Interface.ItemRemove;
import com.wyh.happyyousdk.ehr.PrescriptionUploadedActivity;
import com.wyh.happyyousdk.model.request.ehr.ConvertToBitlyRequest;
import com.wyh.happyyousdk.model.response.ehr.ConvertToBitlyResponse;
import com.wyh.happyyousdk.model.response.ehr.UploadFileResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadListAdapter extends RecyclerView.Adapter<FileUploadListAdapter.MyViewHolder> {
    Context context;
    List<UploadFileResponse.Datum> uploadFileResponse;
    String reportName;
    ItemRemove itemRemove;
    String activityName;
    String filename;


    public FileUploadListAdapter(Context context, List<UploadFileResponse.Datum> uploadFileResponse, String reportName, ItemRemove itemRemove, String activityName) {
        this.context = context;
        this.uploadFileResponse = uploadFileResponse;
        this.reportName = reportName;
        this.itemRemove = itemRemove;
        this.activityName = activityName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FileUploadListAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.file_upload_list_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        filename = uploadFileResponse.get(position).getName().split("_")[0];

        holder.binding.tvRecordName.setText(filename);
        holder.binding.tvRecordType.setText(reportName);
        holder.binding.tvRecordDate.setText(uploadFileResponse.get(position).getCreatedOn());
        if (activityName.equals("PrescriptionUploadedActivity")) {
            holder.binding.ivShare.setVisibility(View.GONE);
            holder.binding.btnDownloadRecord.setVisibility(View.GONE);
            holder.binding.btnDeleteRecord.setVisibility(View.GONE);
            holder.binding.llDelete.setVisibility(View.GONE);

/*
            if (uploadFileResponse.get(position).getExtension().equals(".png") || uploadFileResponse.get(position).getExtension().equals(".jpg") || uploadFileResponse.get(position).getExtension().equals(".jpeg")) {
                holder.binding.btnDeleteRecord.setImageDrawable(context.getDrawable(R.drawable.ic_png));
//                holder.binding.btnDeleteRecord.setOnClickListener(view -> {
//                    Intent i = new Intent(context, PhotoViewActivity.class);
//                    i.putExtra("url", uploadFileResponse.get(position).getPath());
//                    context.startActivity(i);
//                });
            } else if (uploadFileResponse.get(position).getExtension().equals(".pdf") || uploadFileResponse.get(position).getExtension().equals(".docx")) {
                holder.binding.btnDeleteRecord.setImageDrawable(context.getDrawable(R.drawable.ic_pdf));
//                holder.binding.btnDeleteRecord.setOnClickListener(view -> {
//                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uploadFileResponse.get(position).getPath()));
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(i);
//                });

            }*/
            holder.binding.ivShare.setOnClickListener(view ->
            {
                biltyLink(uploadFileResponse.get(position).getPath(), position);
            });
            holder.binding.btnDownloadRecord.setOnClickListener(view -> {
                downloadFileNew(uploadFileResponse.get(position).getPath());
            });
        } else {
            holder.binding.llDelete.setVisibility(View.VISIBLE);
            holder.binding.ivShare.setVisibility(View.GONE);
            holder.binding.btnDownloadRecord.setVisibility(View.GONE);
            holder.binding.btnDeleteRecord.setVisibility(View.VISIBLE);
            holder.binding.btnDeleteRecord.setOnClickListener(view -> {
            /*uploadFileResponse.remove(position);
            notifyItemRemoved(uploadFileResponse.get(position).getId());
            notifyItemRangeChanged(uploadFileResponse.get(position).getId(), uploadFileResponse.size());*/
                itemRemove.onItemRemove(position);
            });
        }

    }

    private void shareData(UploadFileResponse.Datum uploadResponse, String links) {
        String data = "Report Name: "+PrescriptionUploadedActivity.reporttitle+"\n"+ "Report Type: " + PrescriptionUploadedActivity.reportNametoshare
                + "\n" + "Report Date: " + uploadResponse.getCreatedOn() + "\n";
//        String documents = "";
//
//        if (datum.getPath() != null)
//            documents = separateEachString(datum.getPath());

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, data + links);
        context.startActivity(shareIntent);
    }

    private void downloadFileNew(String downloadUrlOfImage) {
        try {
            String fileExtension = "";
            String filename = downloadUrlOfImage.substring(downloadUrlOfImage.lastIndexOf("/") + 1);
            if (filename.contains(".pdf")) {
                fileExtension = ".pdf";
            } else if (filename.contains(".jpg")) {
                fileExtension = ".jpg";
            } else if (filename.contains(".jpeg")) {
                fileExtension = ".jpeg";

            } else if (filename.contains(".docx")) {
                fileExtension = ".docx";

            } else if (filename.contains(".png")) {
                fileExtension = ".png";
            }
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("*") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + filename + fileExtension);
            dm.enqueue(request);
            Toast.makeText(context, "File download started.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "File download failed.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return uploadFileResponse.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        FileUploadListAdapterBinding binding;


        public MyViewHolder(@NonNull FileUploadListAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void biltyLink(String url, int position){
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        List<ConvertToBitlyRequest> request = new ArrayList<>();
        ConvertToBitlyRequest bitlyRequest = new ConvertToBitlyRequest(url);
        request.add(bitlyRequest);
        /*for(String url : urls){
            String link = url.replace("\n", "").trim();
            if(!link.isEmpty() && !link.equals(" ")){
                ConvertToBitlyRequest bitlyRequest = new ConvertToBitlyRequest(link);
                request.add(bitlyRequest);
            }
        }*/
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        Call<ConvertToBitlyResponse> call = apiInterfaceWyh.getConvertToBitly(SharedPref.getAuthToken(), request);

        Log.d("bitly link Req ", new Gson().toJson(call.request().url()));
        Log.d("bitly link Req ", new Gson().toJson(request));


        call.enqueue(new Callback<ConvertToBitlyResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<ConvertToBitlyResponse> call, Response<ConvertToBitlyResponse> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                /*Log.d("bitly link Res ", new Gson().toJson(response.code()));
                Log.d("bitly link Res ", new Gson().toJson(response.body()));*/
                if (response.body() != null && response.body().getSuccess() && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.convert_bitly_link_success));
                    List<ConvertToBitlyResponse.Datum> shareLinkData = response.body().getData();
                    boolean isFirst = true;
                    StringBuilder stb = new StringBuilder();
                    for(int i=0; i < shareLinkData.size(); i++){
                        if(i == shareLinkData.size() - 1){
                            stb.append(shareLinkData.get(i).getBitlyurl());
                        }else{
                            stb.append(shareLinkData.get(i).getBitlyurl()).append(",\n");
                        }
                    }
                    Log.d("links", stb.toString());
                    shareData(uploadFileResponse.get(position), stb.toString());
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.convert_bitly_link_failed));
                }
            }

            @Override
            public void onFailure(Call<ConvertToBitlyResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.convert_bitly_link_failed));
            }
        });
    }
}
