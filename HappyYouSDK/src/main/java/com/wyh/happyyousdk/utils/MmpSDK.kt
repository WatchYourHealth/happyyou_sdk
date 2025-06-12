package com.wyh.happyyousdk.utils

import com.trackier.sdk.TrackierEvent
import com.trackier.sdk.TrackierSDK

object MmpSDK {

        fun logMMPEvent(eventName : String, uuid: String){
            val event = TrackierEvent(eventName)
            if(uuid.isNotEmpty()) {
                //TrackierSDK.setUserId(uuid)
            }

            //TrackierSDK.trackEvent(event)
        }
}