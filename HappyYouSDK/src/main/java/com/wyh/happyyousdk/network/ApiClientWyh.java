package com.wyh.happyyousdk.network;


import com.wyh.happyyousdk.SDKConstants;

import java.util.concurrent.TimeUnit;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientWyh {

    //public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
//    static Gson gson = new GsonBuilder()
//            .setLenient()
//            .create();

    public static Retrofit getClient(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public static Retrofit getClientFaceScan(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientFaceScan)
                .build();
        return retrofit;
    }

    public static Retrofit getClientForUploadImage(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientForUploadImage)
                .build();
        return retrofit;
    }

    /*public static CertificatePinner getFaceCertificate(){
        CertificatePinner certPinner = null;
        certPinner = new CertificatePinner.Builder()
                .add("happyyouapi.watchyourhealth.com",
                        "sha256/ggCKZ0l23O+Mgo8h4amKSSEJ9hi5Y2GX0qN62jUmjWU=")
                .add("rppg-prod.xyz", "sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=")
                .build();

        return certPinner;
    }*/

    public static CertificatePinner getCertificatePinner() {
        CertificatePinner certPinner = null;
        if (SDKConstants.environment.contentEquals("debug")) {
            certPinner = new CertificatePinner.Builder()
                    .add("happyyouapi.watchyourhealth.com",
                            "sha256/ggCKZ0l23O+Mgo8h4amKSSEJ9hi5Y2GX0qN62jUmjWU=")
                    .add("happyyouapi.watchyourhealth.com",
                            "sha256/VzztHaxlYHG1CTL5zaUd6vFCyI6YoQlQw3AgaXZBDBk=")
                    .add("fitnessuatapi.kotaklifeinsurance.com",
                            "sha256/7wcLwQG5s6cJwpz5NHnxT4TRw7XFqOqkWWyKY8E8lZA=")
                    .add("fitnessuatapi.kotaklifeinsurance.com",
                            "sha256/i7WTqTvh0OioIruIfFR4kMPnBqrS2rdiVPl/s2uC/CY=")
                    .add("happyyouapi.watchyourhealth.com",
                            "sha256/7EIVAOHPlEQsDwJ1Yo68k0VXf+jr2spjJM8nVEA4bss=")
                    .add("api.sondeservices.com",
                            "sha256/zmIUzlI8Tn2n0WZfz31C90hThVB0h0mFmCUB8kjrPak=")
                    .add("rppg-prod.xyz", "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=")
                    .build();
        } else if (SDKConstants.environment.equals("uat")) {
            certPinner = new CertificatePinner.Builder()
                    //This is wrong need to change once got the correct SHA key and need to be added in network config file
                    .add("fitnessuatapi.kotaklifeinsurance.com",
                            "sha256/n5+mwIPRIoP+WR0HrSbhmSwqr6J355F7NQU+XE79K2k=")
                    .add("happyyouapi.watchyourhealth.com",
                            "sha256/VzztHaxlYHG1CTL5zaUd6vFCyI6YoQlQw3AgaXZBDBk=")
                    .add("api.sondeservices.com",
                            "sha256/zmIUzlI8Tn2n0WZfz31C90hThVB0h0mFmCUB8kjrPak=")
                    .add("fitnessuatapi.kotaklifeinsurance.com",
                            "sha256/i7WTqTvh0OioIruIfFR4kMPnBqrS2rdiVPl/s2uC/CY=")
                    .add("rppg-prod.xyz", "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=")
                    .build();
        } else {
            certPinner = new CertificatePinner.Builder()
                    .add("fitnessapi.kotaklifeinsurance.com",
                            "sha256/i7WTqTvh0OioIruIfFR4kMPnBqrS2rdiVPl/s2uC/CY=")
                    .add("fitnessuatapi.kotaklifeinsurance.com",
                            "sha256/n5+mwIPRIoP+WR0HrSbhmSwqr6J355F7NQU+XE79K2k=")
                    .add("fitnessuatapi.kotaklifeinsurance.com",
                            "sha256/7wcLwQG5s6cJwpz5NHnxT4TRw7XFqOqkWWyKY8E8lZA=")
                    .add("fitnessapi.kotaklifeinsurance.com",
                            "sha256/n5+mwIPRIoP+WR0HrSbhmSwqr6J355F7NQU+XE79K2k=")
                    .add("api.sondeservices.com",
                            "sha256/zmIUzlI8Tn2n0WZfz31C90hThVB0h0mFmCUB8kjrPak=")
                    .add("fitnessuatapi.kotaklifeinsurance.com",
                            "sha256/i7WTqTvh0OioIruIfFR4kMPnBqrS2rdiVPl/s2uC/CY=")
                    .add("happyyouapi.watchyourhealth.com",
                            "sha256/VzztHaxlYHG1CTL5zaUd6vFCyI6YoQlQw3AgaXZBDBk=")
                    .add("rppg-prod.xyz", "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=")
                    .build();
        }
        return certPinner;
    }
    static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(getCertificatePinner())
            .addInterceptor(interceptor)
            .build();


    static final OkHttpClient okHttpClientFaceScan = new OkHttpClient
            .Builder()
            .connectTimeout(25, TimeUnit.SECONDS)
            .readTimeout(25, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build();

    /*static final OkHttpClient okHttpClientFaceScan = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(new FaceScanInterceptor("wyh-user", "TqKqwecscdfecslkfrSmxS5a"))
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();*/

    static final OkHttpClient okHttpClientForUploadImage = new OkHttpClient.Builder()
            .readTimeout(3000, TimeUnit.SECONDS)
            .connectTimeout(3000, TimeUnit.SECONDS)
            .writeTimeout(3000, TimeUnit.SECONDS)
            .certificatePinner(getCertificatePinner())
            .build();



    public static void postRequest(String url, String req, Callback callback) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, req);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
