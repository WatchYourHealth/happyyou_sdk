package com.wyh.happyyousdk.APIEncryption

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.wyh.happyyousdk.model.request.ActivityTrackerRequest
import com.wyh.happyyousdk.model.request.LogExceptionRequest
import com.wyh.happyyousdk.model.request.LogExceptionResponse
import com.wyh.happyyousdk.model.response.ActivityTrackerResponse
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object APILogs {

    fun sendLogs(apiUrl: String,message: String,code: String, context: Context){
        try{
            CommonUtils.showProgressDialige(context)
            val apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            val logExceptionRequest = LogExceptionRequest(apiUrl,message,SharedPref.getUuid(),code)
            apiInterface.sendLogs(SharedPref.getAuthToken(),logExceptionRequest).enqueue(object : Callback<LogExceptionResponse>{
                override fun onResponse(call: Call<LogExceptionResponse>, response: Response<LogExceptionResponse>) {
                    CommonUtils.dismissDialoge()
                    if(response.body() != null){
                        if(response.isSuccessful){

                        }else{
                            Toast.makeText(context,"Failed to track",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<LogExceptionResponse>, t: Throwable) {
                    CommonUtils.dismissDialoge()

                }

            })
        }catch (e: Exception){
            CommonUtils.dismissDialoge()
            e.toString()
        }
    }

    fun activityTracker(eventName: String, context: Context){
        try{
            val request = ActivityTrackerRequest(eventName)
            val apiInterface = RetrofitHandler.getRetrofitInstance().create(APIInterface::class.java)
            apiInterface.ativityTracker(SharedPref.getAuthToken(),request).enqueue(object : Callback<ActivityTrackerResponse>{
                override fun onResponse(call: Call<ActivityTrackerResponse>, response: Response<ActivityTrackerResponse>) {
                    Log.d("AuthToken","Tracker Api Response")
                }

                override fun onFailure(call: Call<ActivityTrackerResponse>, t: Throwable) {

                }

            })

        }catch (e: Exception){
            e.toString()
        }
    }
}