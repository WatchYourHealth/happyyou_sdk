package com.wyh.happyyousdk.diary.Fragment;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.APILogs;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.databinding.NewWinningsLayoutBinding;
import com.wyh.happyyousdk.diary.AddDiaryActivity;
import com.wyh.happyyousdk.diary.ViewDiaryActivity;
import com.wyh.happyyousdk.diary.adapter.NewDiaryListAdapter;
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Master;

import com.wyh.happyyousdk.model.request.NewMyDiaryRequest;
import com.wyh.happyyousdk.model.request.diary.DeleteDiaryRequest;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.NewMyDiaryResponse;
import com.wyh.happyyousdk.model.response.diary.DeleteDiaryResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.APILogConstant;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinningFragment extends Fragment {

    NewWinningsLayoutBinding binding;
    Context context;
    ApiInterfaceWyh apiInterfaceWyh;
    ProgressDialog progressDialog;
    List<NewMyDiaryResponse.NewDiaryData> list = new ArrayList<>();
    List<String> levelList, engList, topUpList;

    List<NewMyDiaryResponse.NewDiaryData> searchDiary = new ArrayList<>();
    List<DiaryEventListResponse.Datum> levelListData, engListData, topUpListData;
    NewDiaryListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.new_winnings_layout,container,false);
        context = getActivity();
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "bear_rocking_chair_reading_book.json");
        binding.laBear.setAnimationFromUrl(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "anim_bear_404.json");

        getAllDiary();

        APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_act(), context);

        binding.winningSearchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APILogs.INSTANCE.activityTracker(APILogConstant.INSTANCE.getMyzone_unwind_md_act_search(), context);
                if(binding.winningSearchLayout.getVisibility() == View.INVISIBLE){
                    binding.winningSearchLayout.setVisibility(View.VISIBLE);
                }else{
                    binding.winningSearchLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.winningSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    binding.winningSearchCancel.setVisibility(View.INVISIBLE);
                    binding.winningListRecyclerView.setVisibility(View.VISIBLE);
                    binding.llNoRecordFound.setVisibility(View.GONE);
                    setAdapter(list);
                }else{
                    binding.winningSearchCancel.setVisibility(View.VISIBLE);
                    searchJournal(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.winningSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.winningSearchEt.setText("");
            }
        });

        return binding.getRoot();
    }

    public void getAllDiary(){
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        NewMyDiaryRequest newMyDiaryRequest = new NewMyDiaryRequest(false);
        Call<NewMyDiaryResponse> call = apiInterfaceWyh. fetchAllUserJournalsMyDiary(SharedPref.getAuthToken(),newMyDiaryRequest);
        call.enqueue(new Callback<NewMyDiaryResponse>() {
            @Override
            public void onResponse(Call<NewMyDiaryResponse> call, Response<NewMyDiaryResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if(response.body() != null){
                    if(response.code() == 200 && response.isSuccessful()){
                        if(response.body().getData() != null && response.body().getData().size() > 0){
                            binding.heading.setVisibility(View.VISIBLE);
                            binding.winningListRecyclerView.setVisibility(View.VISIBLE);
                            binding.llNoRecordFound.setVisibility(View.GONE);
                            list = response.body().getData();
                            list.sort(new MyDiaryFragment.sortItems());
                            //Collections.reverse(list);
                            setAdapter(list);
                        } else{
                            binding.heading.setVisibility(View.GONE);
                            binding.winningListRecyclerView.setVisibility(View.GONE);
                            binding.llNoRecordFound.setVisibility(View.VISIBLE);
                        }
                    }else if (response.code() == 401) {
                        refreshAuthToken();
                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_diary_failed));
                        Toast.makeText(context, "API: " + getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<NewMyDiaryResponse> call, Throwable t) {

            }
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
                binding.winningListRecyclerView.setVisibility(View.GONE);
                binding.llNoRecordFound.setVisibility(View.VISIBLE);
            }else{
                binding.winningListRecyclerView.setVisibility(View.VISIBLE);
                binding.llNoRecordFound.setVisibility(View.GONE);
            }

            setAdapter(searchDiary);
        }catch (Exception e){
            e.printStackTrace(

            );
        }
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
                    /* Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
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
                /* Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }
    private void
    setAdapter(List<NewMyDiaryResponse.NewDiaryData> list) {
        adapter = new NewDiaryListAdapter(context, list, new NewDiaryListAdapter.OnItemClickListener() {
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

        binding.winningListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.winningListRecyclerView.setAdapter(adapter);
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

    static class sortItems implements Comparator<NewMyDiaryResponse.NewDiaryData> {
        @Override
        public int compare(NewMyDiaryResponse.NewDiaryData newDiaryData, NewMyDiaryResponse.NewDiaryData t1) {
            return t1.getCreatedOn().compareTo(newDiaryData.getCreatedOn());
        }
    }

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

        window = getActivity().getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() * 0.7f), (int) (displayRectangle.height() * 0.45f));
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
