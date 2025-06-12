package com.wyh.happyyousdk.Sonde.Utilities

import com.wyh.happyyousdk.APIEncryption.EncyrptRequestInterceptor
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.utils.SharedPref
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

  private const val BASE_URL = "https://happyyouapi.watchyourhealth.com/hucontestapi/" //UAT

    const val URL = ""

  //private const val BASE_URL = "https://fitnessapi.kotaklifeinsurance.com/huapi/";//PROD


    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(): Retrofit? {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(EncyrptRequestInterceptor())
            .addInterceptor(interceptor)
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(ApiClientWyh.getCertificatePinner())
            .build()
        if (RetrofitHelper.retrofit == null) {
            RetrofitHelper.retrofit = Retrofit.Builder()
                .baseUrl(RetrofitHelper.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return RetrofitHelper.retrofit
    }
}