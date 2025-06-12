package com.wyh.happyyousdk.ehr.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityHealthLockerHistoryBinding;
import com.wyh.happyyousdk.databinding.ActivityHealthRecordTypeWiseReportBinding;
import com.wyh.happyyousdk.databinding.EhrAllRecordsAdapterBinding;
import com.wyh.happyyousdk.databinding.FileListEhrLayoutBinding;
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding;
import com.wyh.happyyousdk.ehr.HealthLockerHistoryActivity;
import com.wyh.happyyousdk.ehr.HealthRecordTypeWiseReportActivity;
import com.wyh.happyyousdk.ehr.Interface.ItemRemove;
import com.wyh.happyyousdk.model.request.CommunityIDs;
import com.wyh.happyyousdk.model.request.FacnScanInTribeRequest;
import com.wyh.happyyousdk.model.request.ehr.ConvertToBitlyRequest;
import com.wyh.happyyousdk.model.request.ehr.DeleteIdRequest;
import com.wyh.happyyousdk.model.response.FaceScanInTribeResponse;
import com.wyh.happyyousdk.model.response.QuestionModel;
import com.wyh.happyyousdk.model.response.ehr.AllHealthRecordDetailsList;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.response.ehr.ConvertToBitlyResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EhrAllRecordsAdapter extends RecyclerView.Adapter<EhrAllRecordsAdapter.MyViewHolder> {
    Context context;
    ValueFilter valueFilter;

    List<AllHealthRecordDetailsList.Datum> allHealthRecordDetailsList;
    List<AllHealthRecordDetailsList.Datum> mData;
    String reportDate;
    ProgressDialog progressDialog;
    String viewType;
    ApiInterfaceWyh apiInterfaceWyh;
    ItemRemove itemRemove;
    AlertDialog alertDialog;
    String documents = "", fromActivity;
    String[] imgPath;

    ActivityHealthLockerHistoryBinding binding;
    List<Integer> tribeListId = new ArrayList<>();
    ArrayList<CommunityIDs> list = new ArrayList<>();



    ActivityHealthRecordTypeWiseReportBinding binding2;




    public EhrAllRecordsAdapter(Context context, List<AllHealthRecordDetailsList.Datum> allEhrReportResponseList, String viewType, ItemRemove itemRemove, String fromActivity) {
        this.context = context;
        allHealthRecordDetailsList = allEhrReportResponseList;
        mData = allEhrReportResponseList;
        this.viewType = viewType;
        this.itemRemove = itemRemove;
        this.fromActivity = fromActivity;
    }

    public EhrAllRecordsAdapter(Context context, List<AllHealthRecordDetailsList.Datum> allEhrReportResponseList, String viewType, ItemRemove itemRemove, String fromActivity, ActivityHealthLockerHistoryBinding binding) {
        this.context = context;
        allHealthRecordDetailsList = allEhrReportResponseList;
        mData = allEhrReportResponseList;
        this.viewType = viewType;
        this.itemRemove = itemRemove;
        this.fromActivity = fromActivity;
        this.binding = binding;
    }

    public EhrAllRecordsAdapter(Context context, List<AllHealthRecordDetailsList.Datum> allEhrReportResponseList, String viewType, ItemRemove itemRemove, String fromActivity, ActivityHealthRecordTypeWiseReportBinding binding) {
        this.context = context;
        allHealthRecordDetailsList = allEhrReportResponseList;
        mData = allEhrReportResponseList;
        this.viewType = viewType;
        this.itemRemove = itemRemove;
        this.fromActivity = fromActivity;
        this.binding2 = binding;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EhrAllRecordsAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.ehr_all_records_adapter, parent, false);

        return new MyViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (viewType.equals("delete")) {
            holder.binding.ivShare.setVisibility(View.GONE);
            holder.binding.ivDownload.setVisibility(View.GONE);
            holder.binding.btnDeleteRecord.setVisibility(View.VISIBLE);
        } else {
            holder.binding.ivShare.setVisibility(View.VISIBLE);
            holder.binding.ivDownload.setVisibility(View.VISIBLE);
            holder.binding.btnDeleteRecord.setVisibility(View.GONE);
        }
        holder.binding.tvRecordName.setText(allHealthRecordDetailsList.get(position).getTestName());
        reportDate = formatDateFromString("yyyy-MM-dd", "dd/MM/yyyy", allHealthRecordDetailsList.get(position).getReportDate());
        holder.binding.tvRecordType.setText(allHealthRecordDetailsList.get(position).getHealthRecordTypeName());
        holder.binding.tvRecordDate.setText(reportDate);

        holder.binding.ivShare.setOnClickListener(view -> {
            reportDate = formatDateFromString("yyyy-MM-dd", "dd/MM/yyyy", allHealthRecordDetailsList.get(position).getReportDate());
            if (allHealthRecordDetailsList.get(position).getPath() != null)
                documents = separateEachString(allHealthRecordDetailsList.get(position).getPath());

            imgPath = documents.split(",");
            biltyLink(imgPath, position,allHealthRecordDetailsList.get(position).getPath());
        });


        holder.binding.ivDownload.setOnClickListener(view -> {
            if (allHealthRecordDetailsList.get(position).getPath() != null)
                documents = separateEachString(allHealthRecordDetailsList.get(position).getPath());

            imgPath = documents.split(",");
            for (int i = 0; i < imgPath.length - 1; i++) {
                if (imgPath[i].contains("\n"))
                    imgPath[i] = imgPath[i].replace("\n", "");
                downloadFileNew(imgPath[i].trim());
            }//String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        });

        holder.binding.btnDeleteRecord.setOnClickListener(view -> showPopup(position));

        holder.binding.llMain.setOnClickListener(view -> {
            if (allHealthRecordDetailsList.get(position).getPath() != null)
                documents = separateEachString(allHealthRecordDetailsList.get(position).getPath());

            imgPath = documents.split(",");
            List<String> dataList = new ArrayList<>();
            for (int i = 0; i < imgPath.length - 1; i++) {
                if (imgPath[i].contains("\n"))
                    imgPath[i] = imgPath[i].replace("\n", "");
                dataList.add(imgPath[i].trim());
            }
            showLayout(dataList, allHealthRecordDetailsList.get(position).getHealthRecordTypeName());

        });

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showPopup(int position) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        View view = LayoutInflater.from(context).inflate(R.layout.delete_popup, null);
        alertBuilder.setView(view);
        Button btn_yes = view.findViewById(R.id.btnOK);
        Button btn_no = view.findViewById(R.id.btnCancel);
        TextView tv_msg = view.findViewById(R.id.tvMsg);
        tv_msg.setText("Are you sure want to delete this record?");
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
        btn_yes.setOnClickListener(view1 -> {

            alertDialog.dismiss();
            deleteEhrRecord(allHealthRecordDetailsList.get(position));
        });
        btn_no.setOnClickListener(view1 -> {
            alertDialog.dismiss();
        });

        Rect displayRectangle = new Rect();


        Window window;
        if (fromActivity.equals("HealthLockerHistoryActivity")) {
            window = ((HealthLockerHistoryActivity) context).getWindow();
        } else {
            window = ((HealthRecordTypeWiseReportActivity) context).getWindow();
        }

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.45f));
    }


    private void shareData(AllHealthRecordDetailsList.Datum datum, String links) {
        String data = "Report Name:" + datum.getTestName() + "\n" + "Report Type:" + datum.getHealthRecordTypeName()
                + "\n" + "Report Date:" + reportDate + "\n";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, data + links);
        context.startActivity(shareIntent);
    }

    public String separateEachString(String path) {
        String[] ducumentsArray = path.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        for (String document : ducumentsArray) {
            stringBuilder.append(document.trim().replaceAll(" ", "%20"));
            stringBuilder.append(", \n");
        }

        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return allHealthRecordDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        EhrAllRecordsAdapterBinding binding;


        public MyViewHolder(@NonNull EhrAllRecordsAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<AllHealthRecordDetailsList.Datum> filterList = new ArrayList<>();
                for (int i = 0; i < mData.size(); i++) {
                    if ((mData.get(i).getTestName().toUpperCase()).contains(constraint.toString().toUpperCase()) ||
                            (mData.get(i).getHealthRecordTypeName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mData.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mData.size();
                results.values = mData;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(fromActivity.equalsIgnoreCase("HealthRecordTypeWiseReportActivity")){
                if(results.count == 0){
                    binding2.llNoRecordFound.setVisibility(View.VISIBLE);
                   // binding2.historyMain.setVisibility(View.GONE);
                }else{
                    binding2.llNoRecordFound.setVisibility(View.GONE);
                   // binding2.historyMain.setVisibility(View.VISIBLE);
                }
            }else{
                if(results.count == 0){
                    binding.llNoRecordFound.setVisibility(View.VISIBLE);
                    binding.historyMain.setVisibility(View.GONE);
                }else{
                    binding.llNoRecordFound.setVisibility(View.GONE);
                    binding.historyMain.setVisibility(View.VISIBLE);
                }
            }

            Log.d("AuthToken", String.valueOf(results.count));
            allHealthRecordDetailsList = (List<AllHealthRecordDetailsList.Datum>) results.values;
            notifyDataSetChanged();
        }
    }

    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private void downloadFileNew(String downloadUrlOfImage) {
        try {
            String fileExtension = "";
            String filename = downloadUrlOfImage.substring(downloadUrlOfImage.lastIndexOf("/") + 1);
            String type = null;
            if (filename.contains(".pdf")) {
                fileExtension = ".pdf";
                type = "application/pdf";
            } else if (filename.contains(".jpg") || filename.contains(".jpeg")) {
                fileExtension = ".jpg";
                type = "image/jpeg";
            } else if (filename.contains(".docx")) {
                fileExtension = ".docx";
                type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            } else if (filename.contains(".png")) {
                fileExtension = ".png";
                type = "image/png";
            } else {
                throw new IllegalArgumentException("Unsupported file type");
            }

            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType(type)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + filename + fileExtension);

            dm.enqueue(request);

            // Create a new PendingIntent to open the downloaded file
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/Happy/", filename + fileExtension);
            Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
            openFileIntent.setDataAndType(uriForFile, type);
            openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openFileIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Build a new notification that launches the PendingIntent when clicked
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                    .setSmallIcon(R.drawable.ic_notification_logo)
                    .setContentTitle("File Downloaded")
                    .setContentText("The file " + filename + " has been downloaded.")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            // Show the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0, builder.build());

            Toast.makeText(context, "File download started.", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, "Unsupported file type.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "File download failed.", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteEhrRecord(AllHealthRecordDetailsList.Datum datum) {
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        DeleteIdRequest deleteIdRequest = new DeleteIdRequest(datum.getId());
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.deleteHealthRecord(SharedPref.getAuthToken(), deleteIdRequest);

        call.enqueue(new Callback<CommonSuccessResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.body().isSuccess() && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.delete_health_locker_success));

                    allHealthRecordDetailsList.remove(datum.getId());
                    notifyItemRemoved(datum.getId());
                    notifyItemRangeChanged(datum.getId(), allHealthRecordDetailsList.size());
                    itemRemove.onItemRemove(datum.getId());
                    Toast.makeText(context.getApplicationContext(),"Records Deleted",Toast.LENGTH_SHORT).show();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.delete_health_locker_failed));
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.delete_health_locker_failed));
            }
        });
    }

    private void showLayout(List<String> healthRecordFilesList, String name) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
        FileListEhrLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.file_list_ehr_layout, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        binding.tvTitle.setText(name);

        ViewMoreEhrItemAdapter viewMoreEhrAdapter = new ViewMoreEhrItemAdapter(context, healthRecordFilesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        binding.rvReportList.setLayoutManager(linearLayoutManager);
        binding.rvReportList.setAdapter(viewMoreEhrAdapter);

        Rect displayRectangle = new Rect();
//            Window window = ((HealthLockerHistoryActivity) context).getWindow();
        Window window;
        if (context instanceof HealthRecordTypeWiseReportActivity){
            window = ((HealthRecordTypeWiseReportActivity) context).getWindow();
        }else {
            window = ((HealthLockerHistoryActivity) context).getWindow();
        }

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public interface OnItemClickListener {
        void onClick(String name, List<QuestionModel> question);
    }


    public void biltyLink(String[] urls, int position, String fileName){
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        List<ConvertToBitlyRequest> request = new ArrayList<>();
        for(String url : urls){
            String link = url.replace("\n", "").trim();
            if(!link.isEmpty() && !link.equals(" ")){
                ConvertToBitlyRequest bitlyRequest = new ConvertToBitlyRequest(link);
                request.add(bitlyRequest);
            }
        }
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
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
                    if(HealthRecordTypeWiseReportActivity.healthRecordTypeName.equalsIgnoreCase("FaceScan")){
                        showSharePopup(context,fileName,stb,position);
                    }else{
                        shareData(allHealthRecordDetailsList.get(position), stb.toString());
                    }
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

    public void showSharePopup(Context context, String fileName, StringBuilder stb, int position) {
        try {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
            ShareOptionPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.share_option_pop_up, null, false);
            alertBuilder.setView(binding.getRoot());
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.setCancelable(true);
            if (!alertDialog.isShowing()) alertDialog.show();
            binding.tvMsg.setText("Share");
            binding.ivWhatsapp.setOnClickListener(v -> {
                shareData(allHealthRecordDetailsList.get(position), stb.toString());

            });
            Rect displayRectangle = new Rect();
            Window window = ((Activity) context).getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.getWindow().setLayout((int) (displayRectangle.width() * 0.88f), RelativeLayout.LayoutParams.WRAP_CONTENT);
        } catch (Exception e) {
            e.toString();
        }
    }

    public void shareFaceScan(String fileName){
        try{
            CommonUtils.showProgressDialige(context);
            for(int i=0; i < tribeListId.size() ; i++){
                CommunityIDs communityIDs = new CommunityIDs(tribeListId.get(i));
                list.add(communityIDs);
            }
            FacnScanInTribeRequest request = new FacnScanInTribeRequest(fileName,list);
            APIInterface apiInterface = RetrofitHandler.apiInterface();
            apiInterface.shareFaceScan(SharedPref.getAuthToken(),request).enqueue(new Callback<FaceScanInTribeResponse>() {
                @Override
                public void onResponse(Call<FaceScanInTribeResponse> call, Response<FaceScanInTribeResponse> response) {
                    CommonUtils.dismissDialoge();
                    if(response.body() != null && response.body().getSuccess() && response.code() == 200){
                        Toast.makeText(context,"Message send",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FaceScanInTribeResponse> call, Throwable t) {
                    CommonUtils.dismissDialoge();

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}



