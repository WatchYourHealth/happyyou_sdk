package com.wyh.happyyousdk.APIEncryption;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;

import android.content.Context;

import androidx.annotation.NonNull;

import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.CommonUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHandler {


    private static final String FACE_BASE_URL = "https://vm-production.xyz/admin-basic/";

    private static Retrofit retrofit;

    private static Retrofit retrofitForFace;
    private static Retrofit retrofitABHA;

    public static APIInterface apiMethods;


    public static Retrofit getRetrofitInstance() {

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new EncyrptRequestInterceptor())
                .addInterceptor(interceptor)
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();


        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(CommonUtils.getBaseUrlForAPI(SDKConstants.SDKMainContext))
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static Retrofit getSondeRetrofitInstance() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();


        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(CommonUtils.getBaseUrlForAPI(SDKConstants.SDKMainContext))
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static Retrofit getRetrofitInstanceForFaceKeys() {

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new BasicAuthInterceptor("wyh-user", "TqKqwecscdfecslkfrSmxS5a"))
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();


        if (retrofitForFace == null) {
            retrofitForFace = new retrofit2.Retrofit.Builder()
                    .baseUrl(FACE_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitForFace;
    }

    public static Retrofit getRetrofitInstanceABHA() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RedirectInterceptor())
                .addInterceptor(interceptor)
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(getCertificatePinner())
                .build();


        if (retrofitABHA == null) {
            retrofitABHA = new retrofit2.Retrofit.Builder()
                    .baseUrl(CommonUtils.getBaseUrlForAPI(SDKConstants.SDKMainContext))
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitABHA;
    }

    public static APIInterface apiInterface() {
        apiMethods = RetrofitHandler.getRetrofitInstance().create(APIInterface.class);
        return apiMethods;
    }

    public static ApiInterfaceWyh apiInterfaceWIthKLIBaseUrl() {
        return RetrofitHandler.getRetrofitInstance().create(ApiInterfaceWyh.class);
    }

    static class RedirectInterceptor implements Interceptor {
        @NonNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(chain.request());
            if (response.code() == 307) {
                request = request.newBuilder()
                        .url(Objects.requireNonNull(response.header("Location")))
                        .build();
                response = chain.proceed(request);
            }
            return response;
        }
    }
}
