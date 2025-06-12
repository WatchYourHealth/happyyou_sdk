package com.wyh.happyyousdk.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
//import com.facebook.appevents.AppEventsLogger //// Meta Removed 17/03/2025
import com.wyh.happyyousdk.crypto.RSAEncryption

object MetaSDK {
    //// Meta Removed 17/03/2025

    /*fun initialise() {

    }

    fun logAppEvent(context: Context, event: String, bundle: Bundle?) {
        SharedPref.init(context)
        if (bundle != null)
            AppEventsLogger.Companion.newLogger(context).logEvent(event, bundle)
        else
            AppEventsLogger.Companion.newLogger(context).logEvent(event)

        val uuid : String = RSAEncryption.callDecryptionMethod(SharedPref.getUuid())

        AppEventsLogger.setUserID(uuid)
    }*/
}