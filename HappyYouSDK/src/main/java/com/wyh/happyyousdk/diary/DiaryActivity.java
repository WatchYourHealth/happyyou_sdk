package com.wyh.happyyousdk.diary;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;
;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityDiaryBinding;
import com.wyh.happyyousdk.diary.adapter.DiaryListAdapter;
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse;
import com.wyh.happyyousdk.diary.model.DiaryListDataResponse;
import com.wyh.happyyousdk.diary.model.DiaryListResponse;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.request.diary.DeleteDiaryRequest;
import com.wyh.happyyousdk.model.response.diary.DeleteDiaryResponse;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiaryActivity extends AppCompatActivity {
    ActivityDiaryBinding binding;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    DiaryListAdapter adapter;

    List<DiaryEventListResponse.Datum> levelListData, engListData, topUpListData;
    List<DiaryListDataResponse> list = new ArrayList<>();
    List<DiaryListDataResponse> searchDiary = new ArrayList<>();
    List<String> levelList, engList, topUpList;

    List<DiaryEventListResponse.Datum> getEventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        binding.heading.setVisibility(View.GONE);
        binding.diaryListRecyclerView.setVisibility(View.GONE);
        binding.llNoRecordFound.setVisibility(View.GONE);

        setToolBar();

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "MyDiaryDashboard");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        


        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.diarySearchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.diarySearchLayout.getVisibility() == View.INVISIBLE){
                    binding.diarySearchLayout.setVisibility(View.VISIBLE);
                }else{
                    binding.diarySearchLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.diarySearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    binding.diarySearchCancel.setVisibility(View.INVISIBLE);
                    binding.diaryListRecyclerView.setVisibility(View.VISIBLE);
                    binding.llNoRecordFound.setVisibility(View.GONE);
                    setAdapter(list);
                }else{
                    binding.diarySearchCancel.setVisibility(View.VISIBLE);
                    searchJournal(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.diarySearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.diarySearchEt.setText("");
            }
        });
        getAllDiary();
    }

    private void
    setAdapter(List<DiaryListDataResponse> list) {
//        Collections.reverse(list);
        adapter = new DiaryListAdapter(context, list, new DiaryListAdapter.OnItemClickListener() {
            @Override
            public void onEdit(int id, String titleName, String content, String date, String imagePath, int imageId) {
                Intent intent = new Intent(context, AddDiaryActivity.class);
                intent.putExtra("came_from", "edit");
                intent.putExtra("title", titleName);
                intent.putExtra("content", content);
                intent.putExtra("date", date);
                intent.putExtra("id", id);
                intent.putExtra("imagePath", imagePath);
                intent.putExtra("imageId", imageId);
                startActivity(intent);
            }

            @Override
            public void onView(int id, String titleName, String content, String date, String imagePath, boolean journalSource, int imageId) {
                Intent intent = new Intent(context, ViewDiaryActivity.class);
                intent.putExtra("title", titleName);
                intent.putExtra("content", content);
                intent.putExtra("date", date);
                intent.putExtra("id", id);
                intent.putExtra("journalSource", journalSource);
                intent.putExtra("imagePath", imagePath);
                intent.putExtra("imageId", imageId);
                startActivity(intent);
            }

            @Override
            public void onDelete(int id) {
                showPopupDelete(id);
            }
        });
        binding.diaryListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.diaryListRecyclerView.setAdapter(adapter);
    }

    private void setToolBar() {
        binding.includeToolbar.tvBack.setText("My Diary");
        binding.includeToolbar.ivAdd.setVisibility(View.GONE);

        binding.includeToolbar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.includeToolbar.ivMenu.setVisibility(View.VISIBLE);

        binding.includeToolbar.ivMenu.setOnClickListener(view -> {
            showPopup(binding.includeToolbar.ivMenu);
        });

    }

    private void searchJournal(String searchkey){
        try{
            searchDiary.clear();
            for(int i=0; i < list.size() ; i++){
                if(list.get(i).getJournalName().toLowerCase().contains(searchkey)){
                    searchDiary.add(list.get(i));
                }
            }

            if(searchDiary.size() == 0){
                binding.diaryListRecyclerView.setVisibility(View.GONE);
                binding.llNoRecordFound.setVisibility(View.VISIBLE);
            }else{
                binding.diaryListRecyclerView.setVisibility(View.VISIBLE);
                binding.llNoRecordFound.setVisibility(View.GONE);
            }

            setAdapter(searchDiary);
        }catch (Exception e){
            e.printStackTrace(

            );
        }
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.diary_add_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_add_user_journal) {
                Intent intent = new Intent(this, AddDiaryActivity.class);
                intent.putExtra("came_from", "add");
                startActivity(intent);

//                case R.id.menu_level_event:
//                    if (levelListData.size() > 0) {
//                        intent = new Intent(this, AddDiaryActivity.class);
//                        intent.putExtra("came_from", "level");
//                        intent.putExtra("data", new Gson().toJson(levelListData));
//                        intent.putExtra("dataStr", new Gson().toJson(levelList));
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(context, "No activity found!", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case R.id.menu_eng_event:
//                    if (engListData.size() > 0) {
//                        intent = new Intent(this, AddDiaryActivity.class);
//                        intent.putExtra("came_from", "eng");
//                        intent.putExtra("data", new Gson().toJson(engListData));
//                        intent.putExtra("dataStr", new Gson().toJson(engList));
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(context, "No activity found!", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case R.id.menu_topup_event:
//                    if (topUpListData.size() > 0) {
//                        intent = new Intent(this, AddDiaryActivity.class);
//                        intent.putExtra("came_from", "topup");
//                        intent.putExtra("data", new Gson().toJson(topUpListData));
//                        intent.putExtra("dataStr", new Gson().toJson(topUpList));
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(context, "No activity found!", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
            }
            return false;
        });
    }

    private void getAllDiary() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<DiaryListResponse> call = apiInterfaceWyh.fetchAllUserJournals(SharedPref.getAuthToken());
        call.enqueue(new Callback<DiaryListResponse>() {
            @Override
            public void onResponse(Call<DiaryListResponse> call, Response<DiaryListResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                getEventList();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_diary_success));

                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        binding.heading.setVisibility(View.VISIBLE);
                        binding.diaryListRecyclerView.setVisibility(View.VISIBLE);
                        binding.llNoRecordFound.setVisibility(View.GONE);
                        list = response.body().getData();
                        Collections.sort(list, new sortItems());
//                        Collections.reverse(list);
                        setAdapter(list);
                    }else{
                        binding.heading.setVisibility(View.GONE);
                        binding.diaryListRecyclerView.setVisibility(View.GONE);
                        binding.llNoRecordFound.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == 401) {
                    refreshAuthToken();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_diary_failed));
                    Toast.makeText(context, "API: " + getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DiaryListResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                getEventList();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_diary_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
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

                    Toast.makeText(context, "Journal Deleted Successfully", Toast.LENGTH_SHORT).show();
                    getAllDiary();
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

    private void refreshAuthToken() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        String deviceModel = Build.BRAND + " " + Build.MODEL;
        String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
        String appVersion = SDKConstants.appVersionName;
        RefreshTokenRequest request = new RefreshTokenRequest(deviceModel, osVersion, appVersion);
        Call<RefreshTokenResponse> call = apiInterfaceWyh.refreshToken(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess() &&
                        response.body().getData().getAuthToken() != null && !response.body().getData().getAuthToken().equals("")) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    getAllDiary();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                   /*Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                    Master.INSTANCE.logOut(context);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                /*Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
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

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() * 0.7f), (int) (displayRectangle.height() * 0.45f));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!binding.diarySearchEt.equals("")){
            searchJournal(binding.diarySearchEt.getText().toString());
        }/*else{
            getAllDiary();
        }*/

    }

    static class sortItems implements Comparator<DiaryListDataResponse> {

        // Method of this class
        @Override
        public int compare(DiaryListDataResponse a, DiaryListDataResponse b) {

            // Returning the value after comparing the objects
            // this will sort the data in Ascending order

            return b.getCreatedOn().compareTo(a.getCreatedOn());
        }
    }

    public void getEventList() {
        progressDialog.isShowing();
        Call<DiaryEventListResponse> call = apiInterfaceWyh.getDiaryEventList(SharedPref.getAuthToken());
        call.enqueue(new Callback<DiaryEventListResponse>() {
            @Override
            public void onResponse(Call<DiaryEventListResponse> call, Response<DiaryEventListResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.fetch_pending_diary_events_success));

                    List<DiaryEventListResponse.Datum> data = response.body().getData();
                    getEventList = response.body().getData();
                    setData(data);

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.fetch_pending_diary_events_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DiaryEventListResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.fetch_pending_diary_events_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(List<DiaryEventListResponse.Datum> data) {
        levelList = new ArrayList<>();
        engList = new ArrayList<>();
        topUpList = new ArrayList<>();
        levelListData = new ArrayList<>();
        engListData = new ArrayList<>();
        topUpListData = new ArrayList<>();
        levelList.add("Select your activity");
        engList.add("Select your activity");
        topUpList.add("Select your activity");

        if(data != null && data.size() > 0){
            for(DiaryEventListResponse.Datum item : data){
                if(item.getEventCategory().toLowerCase().equals("topup")){
                    topUpList.add(item.getEventName());
                    topUpListData.add(item);
                }else if(item.getEventCategory().toLowerCase().equals("eng")){
                    engList.add(item.getEventName());
                    engListData.add(item);
                }else{
                    levelList.add(item.getEventName());
                    levelListData.add(item);
                }
            }
        }

    }
}