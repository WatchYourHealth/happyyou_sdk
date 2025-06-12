package com.wyh.happyyousdk.APIEncryption

import android.content.Context
import android.util.Log
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this){
            val newToken = RefreshToken().refreshToken(context)
            if(newToken != null && newToken != ""){
                Log.d("AuthToken","Refresh Token Called")
                return response.request().newBuilder()
                    .header("Authorization","Bearer $newToken")
                    .build()
            }
        }

        return null

    }
}