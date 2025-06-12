package com.wyh.happyyousdk.APIEncryption;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class EncyrptRequestInterceptor implements Interceptor {

    private static final String TAG = EncyrptRequestInterceptor.class.getSimpleName();
    private static final boolean DEBUG = true;

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
          try{
            RequestBody oldBody = request.body();
            Buffer buffer = new Buffer();
            if (oldBody != null) {
                oldBody.writeTo(buffer);
            }
            String strOldBody = buffer.readUtf8();
            String encryptBody = "";
            if(strOldBody.contains("billToThis")){
                encryptBody =  new AESEncryption().encryptMsg(strOldBody.replaceAll(" ",""));
            }else{
                encryptBody =  new AESEncryption().encryptMsg(strOldBody);
            }
            Log.d("AuthToken","EncryptedReq "+new AESEncryption().encryptMsg(strOldBody));
            request = request.newBuilder().addHeader("EncryptedKey",encryptBody).build();


        }catch (Exception e){
            e.printStackTrace();
        }
        return chain.proceed(request);
    }
}
