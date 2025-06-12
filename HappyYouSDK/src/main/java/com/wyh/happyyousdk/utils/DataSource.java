package com.wyh.happyyousdk.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.KgiUpdateDetailsRequest;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSource {
    private static DataSource instance;

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    public void kgiUpdateDetails(String name, String dob, String email, String gender) {

        String kgiPolicyDetails = SharedPref.getKGIPolicyDetails();
        if (!kgiPolicyDetails.isEmpty()) {

            ApiInterfaceWyh kgiInterface = RetrofitHandler.kjiInterfaceJava();
            if(name == null){
                name = "";
            }
            if(email == null){
                email = "";
            }
            if(dob == null){
                dob = "";
            }
            if(gender == null){
                gender = "";
            }
            KgiUpdateDetailsRequest request = new KgiUpdateDetailsRequest(name, email, dob, gender);
            String token = SharedPref.getKgiAuthToken();
            Call<CommonSuccessResponse> call = kgiInterface.kgiUpdateDetails(token, request);
            Log.d("update url: kgi", new Gson().toJson(call.request().url()));
            Log.d("update policy req: kgi", new Gson().toJson(request));

            call.enqueue(new Callback<CommonSuccessResponse>() {
                @Override
                public void onResponse(@NonNull Call<CommonSuccessResponse> call, @NonNull Response<CommonSuccessResponse> response) {
                    Log.d("update policy code: kgi", new Gson().toJson(response.code()));
                    Log.d("update policy body: kgi", new Gson().toJson(response.body()));
                }

                @Override
                public void onFailure(@NonNull Call<CommonSuccessResponse> call, @NonNull Throwable t) {
                    Log.d("login re Exception: kgi", new Gson().toJson(t.getMessage()));

                }
            });

        }
    }
}
