package com.wyh.happyyousdk.ChallangesModule.Utils

import android.content.Context
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.utils.SharedPref
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.concurrent.TimeUnit

object OkHttpHelper {

    const val BASE_URL = "https://happyyouapi.watchyourhealth.com/huapinew/"; //UAT
    //private const val BASE_URL = "https://fitnessuatapi.kotaklifeinsurance.com/huapi/" //KLI-UAT
//    private const val BASE_URL = "https://fitnessapi.kotaklifeinsurance.com/huapi/"; //PROD


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
            .url(BASE_URL + "Challenges/CompleteActivity")
            .method("POST", body)
            .addHeader("Authorization", SharedPref.getAuthToken())
            .build()

        return request
    }
}