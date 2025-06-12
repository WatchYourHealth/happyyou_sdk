package com.wyh.happyyousdk.ehr;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityHealthLockerPhotosBinding;
import com.wyh.happyyousdk.diary.model.DiaryListDataResponse;
import com.wyh.happyyousdk.diary.model.DiaryListResponse;
import com.wyh.happyyousdk.ehr.adapter.HealthLockerPhotosAdapter;
import com.wyh.happyyousdk.ehr.adapter.HealthLockerPhotosListAdapter;
import com.wyh.happyyousdk.ehr.model.ErhPhotosModel;
import com.wyh.happyyousdk.utils.Master;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthLockerPhotosActivity extends AppCompatActivity implements HealthLockerPhotosAdapter.ClickListenerInterface {


    ActivityHealthLockerPhotosBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_health_locker_photos);

        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.includeBack.tvBack.setText("My Photos");
        binding.includeBack.llBack.setOnClickListener(view -> finish());

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "HealthLockerMyPhotos");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        

        getAllDiary();
    }

    private void setAdapter(ErhPhotosModel dataList) {
        HealthLockerPhotosListAdapter healthLockerPhotosAdapter = new HealthLockerPhotosListAdapter(context, dataList);
        binding.rvHLPhotos.setAdapter(healthLockerPhotosAdapter);
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
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_diary_success));

                    if (response.body().getData() != null && response.body().getData().size() > 0) {

                        List<DiaryListDataResponse> list = new ArrayList<>();
                        List<DiaryListDataResponse> data = response.body().getData();
                        for (DiaryListDataResponse item : data) {
                            if (!item.getJournalSource().toLowerCase().equals("unwind")
                                    && item.getImagePath() != null
                                    && !Objects.equals(item.getImagePath(), "")) {
                                list.add(item);
                            }
                        }


                        if (list.size() > 0) {
                            binding.rvHLPhotos.setVisibility(View.VISIBLE);
                            binding.llNoRecordFound.setVisibility(View.GONE);
                            setAdapter(groupBy(list));
                        } else {
                            binding.rvHLPhotos.setVisibility(View.GONE);
                            binding.llNoRecordFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.rvHLPhotos.setVisibility(View.GONE);
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
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_all_diary_failed));
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

    @Override
    public void onItemClick(DiaryListDataResponse data) {
        Intent intent = new Intent(context, ImageReaderActivity.class);
        intent.putExtra("image", data.getImagePath());
        startActivity(intent);
    }

    public static ErhPhotosModel groupBy(List<DiaryListDataResponse> list) {
        Map<String, List<DiaryListDataResponse>> map = new HashMap<String, List<DiaryListDataResponse>>();

        for (DiaryListDataResponse item : list) {
            String key = item.getJournalDate().split("T")[0];
            //Log.d("Date: ", key);
            if (map.containsKey(key)) {
                List<DiaryListDataResponse> filterList = map.get(key);
                filterList.add(item);
            } else {
                List<DiaryListDataResponse> filterList = new ArrayList<DiaryListDataResponse>();
                filterList.add(item);
                map.put(key, filterList);
            }

        }

        map = sortValues(map);

        //Log.d("Date data: ", new Gson().toJson(map));

        Set<String> keySet = map.keySet();

        // Creating an ArrayList of keys
        // by passing the keySet
        ArrayList<String> listOfKeys
                = new ArrayList<String>(keySet);

        Collections.reverse(listOfKeys);

        // Getting Collection of values from HashMap
        Collection<List<DiaryListDataResponse>> values = map.values();

        // Creating an ArrayList of values
        ArrayList<List<DiaryListDataResponse>> listOfValues
                = new ArrayList<>(values);

        Collections.reverse(listOfValues);

        return new ErhPhotosModel(listOfKeys, listOfValues);
    }

    private static Map<String, List<DiaryListDataResponse>> sortValues(Map<String, List<DiaryListDataResponse>> map) {
        List list = new LinkedList(map.entrySet());
//Custom Comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
            }
        });
//copying the sorted list in HashMap to preserve the iteration order
        Map<String, List<DiaryListDataResponse>> sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put((String) entry.getKey(), (List<DiaryListDataResponse>) entry.getValue());
        }

        /*Map<String, List<DiaryListDataResponse>> inverseMap = new HashMap<>();
        for (Map.Entry entry : sortedHashMap.entrySet()){
            inverseMap.put((String) entry.getKey(), (List<DiaryListDataResponse>) entry.getValue());
        }*/
        return sortedHashMap;
    }
}