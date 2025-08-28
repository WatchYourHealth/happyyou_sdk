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
}
