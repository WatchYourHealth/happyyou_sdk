package com.wyh.happyyousdk.diary;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityViewDiaryBinding;
import com.wyh.happyyousdk.databinding.ShareOptionPopUpBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.absorb.ShareBlogRequest;
import com.wyh.happyyousdk.model.request.diary.DeleteDiaryRequest;
import com.wyh.happyyousdk.model.request.diary.ImageDeleteRequest;
import com.wyh.happyyousdk.model.response.diary.DeleteDiaryResponse;
import com.wyh.happyyousdk.model.response.diary.DiaryDetailsResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.ThreadExecution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDiaryActivity extends AppCompatActivity implements ThreadExecution {
    ActivityViewDiaryBinding binding;
    String came_from, date, title, content, imagePath;
    int id, imageId;
    boolean journalSource;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    Uri uriforwhatsaap;

    boolean isimageavailable = false;
    Bitmap imagebitmap;

    List<Integer> tribeListId = new ArrayList<>();
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_diary);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("title");
            content = extras.getString("content");
            date = extras.getString("date");
            imagePath = extras.getString("imagePath");
            id = extras.getInt("id");
            imageId = extras.getInt("imageId");
            journalSource = extras.getBoolean("journalSource");
//           viewDiary(id);
        }

        setToolBar();
        setData();
    }

    private void setToolBar() {
        binding.includeToolbar.tvBack.setText("My Diary");
        binding.includeToolbar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.includeToolbar.ivMenu.setVisibility(View.VISIBLE);

        binding.includeToolbar.ivMenu.setOnClickListener(view -> {
            showPopupMenu(binding.includeToolbar.ivMenu);
        });
    }

    private void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.diary_view_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.menu_diary_share) {
                if (imagePath != null && !imagePath.isEmpty()) {
                    try {
                        progressDialog.show();
                        Drawable drawable = binding.imageView.getDrawable();
                        if (drawable instanceof BitmapDrawable) {
                            imagebitmap = ((BitmapDrawable) drawable).getBitmap();
                        } else {
                            URL url = new URL(imagePath);
                            imagebitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        }
                        File cacheDir = new File(context.getFilesDir(), "MyAppCache");
                        if (!cacheDir.exists()) {
                            cacheDir.mkdirs();
                        }
                        final File[] file = new File[1];
                        file[0] = new File(cacheDir, "shareimage" + ".png");

                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(() -> {
                            try {
                                Bitmap bitmap = imagebitmap;
                                FileOutputStream fOut = new FileOutputStream(file[0]);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                file[0].setReadable(true, false);
                                uriforwhatsaap = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file[0]);
                                isWorkCompleted(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                } else {
                    Toast.makeText(context, "No image found to share.", Toast.LENGTH_SHORT).show();
                }
            } else if (itemId == R.id.menu_diary_delete) {
                if (imagePath != null && !imagePath.isEmpty()) {
                    showPopupDeleteImage(imageId);
                } else {
                    Toast.makeText(context, "No image found to delete.", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });
    }


    private void deleteImage(int id) {
        ImageDeleteRequest request = new ImageDeleteRequest(id);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.imageDeleteDiary(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {

                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.diary_image_delete_success));
                    Toast.makeText(context, "Image Deleted successfully", Toast.LENGTH_SHORT).show();
                    imagePath = "";
                    binding.imageView.setVisibility(View.GONE);
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.diary_image_delete_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {

                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.diary_image_delete_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        binding.tvTitle.setText(title);
        binding.tvContent.setText(content);
        binding.tvDate.setText(date);
        if (imagePath != null && !imagePath.equals("") && !imagePath.isEmpty()) {
            binding.imageView.setVisibility(View.VISIBLE);
            isimageavailable = true;
            Glide.with(context)
                    .load(imagePath)
                    .placeholder(R.drawable.dummy_image)
                    .into(binding.imageView);

        } else {
            binding.imageView.setVisibility(View.GONE);
        }
        binding.ivDelete.setOnClickListener(v -> {
            showPopupDelete(id);
        });
        binding.ivEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddDiaryActivity.class);
            intent.putExtra("came_from", "edit");
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            intent.putExtra("date", date);
            intent.putExtra("id", id);
            intent.putExtra("imagePath", imagePath);
            intent.putExtra("imageId", imageId);
            startActivity(intent);
            finish();
        });
        binding.sharedairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String message = title + "\n" + content;
                    shareImage(message);
                    /*progressDialog.show();
                    Drawable drawable = binding.imageView.getDrawable();
                    if (drawable instanceof BitmapDrawable) {
                        imagebitmap = ((BitmapDrawable) drawable).getBitmap();
                    } else {
                        URL url = new URL(imagePath);
                        imagebitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    }
                    File cacheDir = new File(context.getFilesDir(), "MyAppCache");
                    if (!cacheDir.exists()) {
                        cacheDir.mkdirs();
                    }
                    final File[] file = new File[1];
                    file[0] = new File(cacheDir, "shareimage" + ".png");

                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        try {
                            Bitmap bitmap = imagebitmap;
                            FileOutputStream fOut = new FileOutputStream(file[0]);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                            file[0].setReadable(true, false);
                            uriforwhatsaap = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file[0]);
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    });*/
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                /*String message = title + "\n" + content;
                showSharePopup(message, uriforwhatsaap);*/
            }
        });

        if (journalSource) {
            binding.ivDelete.setVisibility(View.VISIBLE);
            binding.ivEdit.setVisibility(View.VISIBLE);
        } else {
            binding.ivDelete.setVisibility(View.GONE);
            binding.ivEdit.setVisibility(View.GONE);
        }

    }


    public void shareImage(String message) {
        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(new File(cachePath, "image.png")); // overwrites this image every time
            Bitmap bitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        uriforwhatsaap = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", newFile);
        isWorkCompleted(true);
        if (uriforwhatsaap != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(uriforwhatsaap, getContentResolver().getType(uriforwhatsaap));
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriforwhatsaap);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));

        }
    }

    private void showSharePopup(String message, Uri uriforwhatsaap) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialogInfo);
        ShareOptionPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.share_option_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing())
            alertDialog.show();

        binding.tvMsg.setText("Share Image");

        binding.ivWhatsapp.setOnClickListener(v -> {
            alertDialog.dismiss();
            try {
                // Create an intent to share the content
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                shareIntent.setDataAndType(uriforwhatsaap, getContentResolver().getType(uriforwhatsaap));
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriforwhatsaap);
                startActivity(Intent.createChooser(shareIntent, "Choose an app"));
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("*/*");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, uriforwhatsaap);
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                shareIntent.setPackage("com.whatsapp");

                if(isAppInstalled("com.whatsapp")) {
                    context.startActivity(Intent.createChooser(shareIntent, "Share to"));
                } else {
                    Toast.makeText(context, "Please install WhatsAap on your device", Toast.LENGTH_SHORT).show();
                }
                /*if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                } else {
                    Toast.makeText(context, "please install whatsaap on your device", Toast.LENGTH_SHORT).show();
                }*/
            } catch (Exception e) {
                Toast.makeText(context, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }


            // shareBitmap("*"+healthTvList.get(position).getTitle()+"* \nvideo link: "+healthTvList.get(position).getLink());
        });


        Rect displayRectangle = new Rect();
//        Window window = ((AbsorbActivity) context).getWindow();

        Window window;
        window = getWindow();


        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.88f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showPopupDelete(int id) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        View view = LayoutInflater.from(context).inflate(R.layout.delete_popup, null);
        alertBuilder.setView(view);
        Button btn_yes = view.findViewById(R.id.btnOK);
        Button btn_no = view.findViewById(R.id.btnCancel);


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
        btn_yes.setOnClickListener(view1 -> {
            Log.d("journalId", "" + id);
            alertDialog.dismiss();
            deleteDiary(id);
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

    private void showPopupDeleteImage(int id) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        View view = LayoutInflater.from(context).inflate(R.layout.delete_popup, null);
        alertBuilder.setView(view);
        Button btn_yes = view.findViewById(R.id.btnOK);
        Button btn_no = view.findViewById(R.id.btnCancel);
        TextView tvMsg = view.findViewById(R.id.tvMsg);

        tvMsg.setText("Are you sure want to delete this journal image?");


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
        btn_yes.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            deleteImage(id);
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

    private void deleteDiary(int id) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        DeleteDiaryRequest request = new DeleteDiaryRequest(id);
        Log.d("delete json", new Gson().toJson(request));
        Call<DeleteDiaryResponse> call = apiInterfaceWyh.deleteDiary(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<DeleteDiaryResponse>() {
            @Override
            public void onResponse(Call<DeleteDiaryResponse> call, Response<DeleteDiaryResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.delete_diary_success));
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.delete_diary_failed));
                    Toast.makeText(context, "API" + getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteDiaryResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.delete_diary_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean isWorkCompleted(boolean status) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (uriforwhatsaap != null) {
            String message = title + "\n" + content;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showSharePopup(message, uriforwhatsaap);
                }
            });
        }
        return false;
    }

    private boolean isAppInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }
}