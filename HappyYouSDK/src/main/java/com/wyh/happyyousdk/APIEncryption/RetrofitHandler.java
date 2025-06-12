package com.wyh.happyyousdk.APIEncryption;

import static com.wyh.happyyousdk.network.ApiClientWyh.getCertificatePinner;

import androidx.annotation.NonNull;

import com.wyh.happyyousdk.network.ApiInterfaceWyh;

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

    public static final String BASE_URL = "https://happyyouapi.watchyourhealth.com/huapinew/";  //UAT
    //public static final String BASE_URL = "https://fitnessuatapi.kotaklifeinsurance.com/huapi/";  //KLI-UAT
//    public static final String BASE_URL = "https://fitnessapi.kotaklifeinsurance.com/huapi/";//PROD

    //private static final String BASE_URL_ABHA = "http://testwyhb2c.watchyourhealth.com/phr/api/";  //UAT
    private static final String BASE_URL_ABHA = " https://happyyouapi.watchyourhealth.com/abha/";  //UAT

    //public static final String KGI_BASE_URL = " https://happyyou.zurichkotak.com"; //kgi prod
    public static final String KGI_BASE_URL = "https://happyyouuat.zurichkotak.com/API/"; //kgi

    private static final String FACE_BASE_URL = "https://vm-production.xyz/admin-basic/";

    private static Retrofit retrofit;

    private static Retrofit retrofitForFace;
    private static Retrofit retrofitABHA;

    public static APIInterface apiMethods;
    private static Retrofit retrofitForKji;


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
                    .baseUrl(BASE_URL)
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
                    .baseUrl(BASE_URL)
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
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitABHA;
    }

    public static Retrofit getKjiRetrofitInstance() {

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //HttpUrl url = new HttpUrl.Builder().scheme("http").host("happyyouuat.kotakgeneral.com").build();
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new EncyrptRequestInterceptor())
                .addInterceptor(interceptor)
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();


        if (retrofitForKji == null) {
            retrofitForKji = new retrofit2.Retrofit.Builder()
                    .baseUrl(KGI_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitForKji;
    }

    public static APIInterface apiInterface() {
        apiMethods = RetrofitHandler.getRetrofitInstance().create(APIInterface.class);
        return apiMethods;
    }

    public static ApiInterfaceWyh kjiInterfaceJava() {
        return RetrofitHandler.getKjiRetrofitInstance().create(ApiInterfaceWyh.class);
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
