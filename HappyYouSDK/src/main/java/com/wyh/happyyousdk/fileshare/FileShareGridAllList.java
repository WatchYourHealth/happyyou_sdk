package com.wyh.happyyousdk.fileshare;

import static com.wyh.happyyousdk.utils.CommonUtils.downloadAndViewFile;
import static com.wyh.happyyousdk.utils.CommonUtils.downloadImage;
import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;
import static com.wyh.happyyousdk.utils.rd.utils.DensityUtils.dpToPx;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.AdapterFileShareListBinding;
import com.wyh.happyyousdk.fileshare.PhotoViewActivityFileShare;
import com.wyh.happyyousdk.model.UpdateFileStatusReq;
import com.wyh.happyyousdk.model.response.FileShareResp;
import com.wyh.happyyousdk.model.response.UpdateFileStatusResp;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileShareGridAllList extends BaseAdapter {
    Context context;
    List<FileShareResp.Data> fileShareResps;
    int[] res, resCircle;
    int bgCount = -1;
    boolean isAllItemsLoaded = false;
    APIInterface apiInterfaceWyh;
    public FileShareGridAllList(Context context, List<FileShareResp.Data> policyDetailsList) {
        this.context = context;
        this.fileShareResps = policyDetailsList;
        apiInterfaceWyh = RetrofitHandler.apiInterface();
    }

    @Override
    public int getCount() {
        return fileShareResps.size();
    }

    @Override
    public Object getItem(int position) {
        return "";
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        res = new int[]{R.drawable.ic_light_pink_button_bg, R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg,
                R.drawable.ic_light_blue_button_bg, R.drawable.ic_light_orange_button_bg, R.drawable.ic_light_pink_button_bg
        };
        @SuppressLint("ViewHolder") AdapterFileShareListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_file_share_list, parent, false);
        FileShareResp.Data data = fileShareResps.get(position);

        if (bgCount == res.length - 1) {
            bgCount = 0;
        } else {
            bgCount++;
        }
        binding.llMain.setBackground(ContextCompat.getDrawable(context, res[bgCount]));

        if (data.getIsExpired()) {
            binding.llMain.setBackground(ContextCompat.getDrawable(context, R.drawable.expired_card_bg));
        }
        binding.tvTitle1.setText(data.getCategoryName());
        binding.tvTitle2.setText(data.getSubCategoryName());
        Glide.with(context)
                .load(data.getTileIcon())
                .error(R.drawable.ic_image_holder_reward)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivRewardIcon);
        binding.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!data.getIsExpired())
                    ShowFileShareDialogNew(data);
            }
        });
        if (fileShareResps.size() - 1 == position)
            isAllItemsLoaded = true;
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    public void ShowFileShareDialogNew(FileShareResp.Data data)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_file_share_new, null);

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        if (window != null) {
            window.setDimAmount(0.9f); // Set dim amount (0.0f = no dim, 1.0f = fully dimmed)
        }
        TextView tvTitle=dialogView.findViewById(R.id.tvTitle);
//        TextView tvTitle2=dialogView.findViewById(R.id.tvTitle2);
        TextView tvSubTitle1=dialogView.findViewById(R.id.tvSubTitle1);
        TextView tvSubTitle2=dialogView.findViewById(R.id.tvSubTitle2);
        TextView tvSubTitle3=dialogView.findViewById(R.id.tvSubTitle3);
        TextView tvSubTitle4=dialogView.findViewById(R.id.tvSubTitle4);
        TextView tvSubTitle5=dialogView.findViewById(R.id.tvSubTitle5);
        TextView tvSubTitle6=dialogView.findViewById(R.id.tvSubTitle6);
        TextView tvSubTitle1Value=dialogView.findViewById(R.id.tvSubTitle1Value);
        TextView tvSubTitle2Value=dialogView.findViewById(R.id.tvSubTitle2Value);
        TextView tvSubTitle3Value=dialogView.findViewById(R.id.tvSubTitle3Value);
        TextView tvSubTitle4Value=dialogView.findViewById(R.id.tvSubTitle4Value);
        TextView tvSubTitle5Value=dialogView.findViewById(R.id.tvSubTitle5Value);
        TextView tvSubTitle6Value=dialogView.findViewById(R.id.tvSubTitle6Value);
        TextView tvDesc1=dialogView.findViewById(R.id.tvDesc1);
        TextView tvDesc2=dialogView.findViewById(R.id.tvDesc2);
        TextView tvDesc3=dialogView.findViewById(R.id.tvDesc3);
        TextView tvExpireOn=dialogView.findViewById(R.id.tvExpireOn);
        TextView tvFileName=dialogView.findViewById(R.id.tvFileName);
        RelativeLayout btnDownload=dialogView.findViewById(R.id.btnDownload);
        RelativeLayout btnView=dialogView.findViewById(R.id.btnView);
        ImageView imFileShareImage=dialogView.findViewById(R.id.imFileShareImage);
        ImageView btnClose=dialogView.findViewById(R.id.imClose);
        TextView tvNote = dialogView.findViewById(R.id.tvNote);
        btnDownload.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_FILE_DOWNLOAD",context);
            saveFileShare(data.getCategoryName(),"Download");
            if (data.getFilePath()!=null&!data.getFilePath().isEmpty())
            {
                downloadImage(context,data.getFilePath(),data.getFileName());
            }
            dialog.dismiss();
        });
        btnClose.setOnClickListener(v -> { dialog.dismiss();
            APILogs.INSTANCE.activityTracker("Android_FILE_CANCEL",context);
        });
        btnView.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_FILE_VIEW",context);
            saveFileShare(data.getCategoryName(),"View");
            if (data.getFilePath()!=null&!data.getFilePath().isEmpty())
            {
                if (data.getFilePath().endsWith(".pdf")||data.getFilePath().endsWith(".doc")||data.getFilePath().endsWith(".docx"))
                {
                    downloadAndViewFile(context,data.getFilePath());
                }
                else
                {
                    viewImage(data.getFilePath());
                }
            }
            //dialog.dismiss();
        });
        Glide.with(context)
                .load(data.getTileIcon())
                .error(R.drawable.ic_file_share)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imFileShareImage);

        tvTitle.setText(data.getCategoryName());
        tvSubTitle1.setText(data.getEventId().split(";")[0]);
        tvSubTitle2.setText(data.getDateString().split(";")[0]);
        tvSubTitle3.setText(data.getTimeString().split(";")[0]);
        tvSubTitle4.setText(data.getPlace().split(";")[0]);
        tvSubTitle5.setText("End Date");
        tvSubTitle6.setText("End Time");
        tvSubTitle1Value.setText(data.getEventId().split(";")[1]);
        tvSubTitle2Value.setText(data.getDateString().split(";")[1]);
        tvSubTitle3Value.setText(data.getTimeString().split(";")[1]);
        tvSubTitle4Value.setText(data.getPlace().split(";")[1]);
        tvSubTitle5Value.setText(formatDateFromString("yyyy-MM-dd'T'HH:mm","dd-MM-yyyy",data.getExpiryDate()));
        tvSubTitle6Value.setText(formatDateFromString("yyyy-MM-dd'T'HH:mm","hh:mm a",data.getExpiryDate()));
        tvDesc1.setText(data.getDescription1().replace("\\n", "\n"));
        tvDesc2.setText(data.getDescription2().replace("\\n", "\n"));
        tvDesc3.setText(data.getDescription3().replace("\\n", "\n"));
        tvExpireOn.setVisibility(View.GONE);
        tvExpireOn.setText("Expire On "+formatDateFromString("yyyy-MM-dd'T'HH:mm","dd/MM/yyyy",data.getExpiryDate()));
        tvFileName.setText(data.getFileName());
        tvNote.setVisibility(View.GONE);
        tvNote.setText(data.getNote());
        dialog.show();
    }

    public void ShowFileShareDialog(FileShareResp.Data data) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_file_share, null);

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
         dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        if (window != null) {
            window.setDimAmount(0.9f); // Set dim amount (0.0f = no dim, 1.0f = fully dimmed)
        }
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        TextView tvTitle2 = dialogView.findViewById(R.id.tvTitle2);
        TextView tvExpireOn = dialogView.findViewById(R.id.tvExpireOn);
        TextView tvFileName = dialogView.findViewById(R.id.tvFileName);
        RelativeLayout btnDownload = dialogView.findViewById(R.id.btnDownload);
        RelativeLayout btnView = dialogView.findViewById(R.id.btnView);
        ImageView imFileShareImage = dialogView.findViewById(R.id.imFileShareImage);
        ImageView btnClose = dialogView.findViewById(R.id.imClose);
        TextView tvNote = dialogView.findViewById(R.id.tvNote);
        btnDownload.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_FILE_DOWNLOAD",context);
            saveFileShare(data.getCategoryName(),"Download");
            downloadImage(context, data.getFilePath(),  data.getFilePath().substring(data.getFilePath().lastIndexOf('/') + 1));
            //dialog.dismiss();
        });
        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
            APILogs.INSTANCE.activityTracker("Android_FILE_CANCEL",context);
        });
        btnView.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_FILE_VIEW",context);
            if (data.getFilePath()!=null&!data.getFilePath().isEmpty())
            {
                if (data.getFilePath().endsWith(".pdf")||data.getFilePath().endsWith(".doc")||data.getFilePath().endsWith(".docx"))
                {
                    downloadAndViewFile(context,data.getFilePath());
                }
                else
                {
                    saveFileShare(data.getCategoryName(),"View");
                    viewImage(data.getFilePath());
                }
            }
            //dialog.dismiss();
        });
        Glide.with(context)
                .load(data.getTileIcon())
                .error(R.drawable.ic_file_share)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imFileShareImage);

        tvTitle.setText(data.getCategoryName());
        tvTitle2.setText(data.getSubCategoryName());
        tvExpireOn.setText("Expire On " + formatDateFromString("yyyy-MM-dd'T'HH:mm", "dd/MM/yyyy", data.getExpiryDate()));
        tvFileName.setText(data.getFileName());
        tvNote.setText(data.getNote());
        dialog.show();
    }

    private void viewImage(String url) {
        if (url.endsWith(".pdf")) {
            // Handle PDF files
            Uri path = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
            }
        } else if (url.endsWith(".doc") || url.endsWith(".docx")) {
            // Handle DOC and DOCX files
            Uri path = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/msword");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No Application Available to View Word Document", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle image files
            Intent i = new Intent(context, PhotoViewActivityFileShare.class);
            i.putExtra("url", url);
            context.startActivity(i);
        }
    }
    private void saveFileShare(String corporateCat,String UserAction) {
        UpdateFileStatusReq request = new UpdateFileStatusReq(corporateCat,UserAction);
        Call<UpdateFileStatusResp> call = apiInterfaceWyh.UpdateFileStatus(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UpdateFileStatusResp> call, @NonNull Response<UpdateFileStatusResp> response) {

            }

            @Override
            public void onFailure(@NonNull Call<UpdateFileStatusResp> call, @NonNull Throwable t) {

            }
        });
    }
}