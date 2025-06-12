package com.wyh.happyyousdk.network;

import androidx.annotation.NonNull;


import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientAbhaWyh {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url) {
        if (getOkHttpClientDebug() == null) {
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClientDebug())
                    .build();
        }
        return retrofit;
    }

    public static OkHttpClient getOkHttpClientDebug() {
        OkHttpClient okHttpClient;
        GzipInterceptor gzipInterceptor = new GzipInterceptor();
        RedirectInterceptor redirectInterceptor = new RedirectInterceptor();
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(redirectInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }

}

class  RedirectInterceptor implements Interceptor {
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
