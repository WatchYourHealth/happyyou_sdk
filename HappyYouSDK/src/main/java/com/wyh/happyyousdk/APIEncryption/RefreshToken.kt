package com.wyh.happyyousdk.APIEncryption

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.wyh.happyyousdk.BuildConfig
import com.wyh.happyyousdk.R
import com.wyh.happyyousdk.SDKConstants
import com.wyh.happyyousdk.login.MobileNumberActivity
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse
import com.wyh.happyyousdk.network.ApiClientWyh
import com.wyh.happyyousdk.network.ApiInterfaceWyh
import com.wyh.happyyousdk.utils.Analytics
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.sharedPreferences.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RefreshToken {


    fun refreshToken(context: Context) : String{
        try{
            var newToken = ""
            val deviceModel = Build.BRAND + " " + Build.MODEL
            val osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")"
            val appVersion = SDKConstants.appVersionName
            val request = RefreshTokenRequest(deviceModel, osVersion, appVersion)
            val apiInterfaceWyh  = ApiClientWyh.getClient(CommonUtils.getBaseUrlForAPI(context))
                .create(ApiInterfaceWyh::class.java)

           val call = apiInterfaceWyh.refreshToken(SharedPref.getAuthToken(), request)
            Log.d("AuthToken", SharedPref.getAuthToken() + "URL: " + call.request().url())
            call.enqueue(object : Callback<RefreshTokenResponse?> {
                override fun onResponse(call: Call<RefreshTokenResponse?>, response: Response<RefreshTokenResponse?>) {
                    Log.d("AuthToken", "Refresh Res: " + Gson().toJson(response.body()))
                    if (response.code() == 200 && response.body() != null && response.body()!!.isSuccess && response.body()!!.data.authToken != null && response.body()!!.data.authToken != "") {
                        SharedPref.putAuthToken("Bearer " + response.body()!!.data.authToken)
                        SharedPreference.putAuthToken("Bearer " + response.body()!!.data.authToken)
                        newToken = response.body()!!.data.authToken
                    } else {
                        Toast.makeText(context, context.resources.getString(R.string.session_time_out), Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MobileNumberActivity::class.java)
                        context.startActivity(intent)
                        SharedPref.clearSharedPref()
                    }
                }

                override fun onFailure(call: Call<RefreshTokenResponse?>, t: Throwable) {
                    Toast.makeText(context, context.resources.getString(R.string.session_time_out), Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MobileNumberActivity::class.java)
                    context.startActivity(intent)
                    SharedPref.clearSharedPref()
                }
            })

            return newToken

        }catch (e: Exception){
            e.toString()
        }

        return ""

    }
}