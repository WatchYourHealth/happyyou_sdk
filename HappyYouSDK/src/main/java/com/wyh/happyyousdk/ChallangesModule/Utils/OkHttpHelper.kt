package com.wyh.happyyousdk.ChallangesModule.Utils

import android.content.Context
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.concurrent.TimeUnit

object OkHttpHelper {


    fun okHttpInit(): OkHttpClient {
        val client = OkHttpClient().newBuilder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(ApiClientWyh.getCertificatePinner())
            .build()

        return client
    }

    fun requestBuilder(context: Context, body: RequestBody): Request {
        var request = Request.Builder()
            .url(CommonUtils.getBaseUrlForAPI(context) + "Challenges/CompleteActivity")
            .method("POST", body)
            .addHeader("Authorization", SharedPref.getAuthToken())
            .build()

        return request
    }
}