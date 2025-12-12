package com.wyh.happyyousdk.ChallangesModule.Utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat.finishAffinity
import com.trackier.sdk.TrackierSDK
import com.trackier.sdk.TrackierSDK.initialize
import com.trackier.sdk.TrackierSDKConfig
import com.wyh.happyyousdk.SDKConstants
import com.wyh.happyyousdk.utils.RootCheck
import com.wyh.happyyousdk.utils.SharedPref
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ApplicationClass : Application() {

    var context: Context? = null
    var minutes = 0


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        val TR_SDK_KEY  = "771d7226-464a-4a0c-9a9a-56babb4f06fc";
        SharedPref.init(this)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        //

        val sdkConfig = TrackierSDKConfig(this, TR_SDK_KEY, "production")
        sdkConfig.setAppSecret("67bc071a3aec9534afac0c34", "21c316f9-c09f-4c3b-ac3f-9d899d808cb9")
        initialize(sdkConfig)
       Log.d(" TrackierSDK.getTrackierId()",  TrackierSDK.getTrackierId())
        //production, development, testing

        SharedPref.putSessionStartTime(getCurrentTimestamp())
        //setupActivityListener()
    }

    private fun setupActivityListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {


            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })
    }

    override fun onTerminate() {
        super.onTerminate()
        SharedPref.putSessionEndTime(getCurrentTimestamp())
    }

    private fun getCurrentTimestamp(): String {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return isoFormat.format(Date())
    }


}