package com.wyh.happyyousdk.APIEncryption.BackgroundWork

import android.content.Context
import android.content.Intent
import android.util.Log
import com.wyh.happyyousdk.sendActivityData.SendDataToServerReceiver
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.main.NewSleepLogic
import com.wyhsdk.main.WatchYourHealth
import com.wyhsdk.main.WatchYourHealth.SLEEP_DATA
import com.wyhsdk.sharedPreferences.SharedPreference
import com.wyhsdk.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class CoroutineClass {


    lateinit var watchYourHealth: WatchYourHealth
    var sendToServer = false

    fun runBackGroundTask(dateFormat : Int, context: Context){
        watchYourHealth = WatchYourHealth(context, SharedPref.getUuid())
        CoroutineScope(Dispatchers.IO).launch {
            if(SharedPreference.getGoogleFitConnection()){
                watchYourHealth.connectAPIClient()
            }

            when(dateFormat) {
                WatchYourHealth.DAY_OF_WEEK_IN_MONTH ->{
                    watchYourHealth.getStepsData(WatchYourHealth.DAY_OF_WEEK_IN_MONTH,0,TimeUnit.DAYS)
                    Log.d("AuthToken", "Coroutine DAY_OF_WEEK_IN_MONTH $dateFormat")


                }
                WatchYourHealth.YEAR -> {
                    watchYourHealth.getStepsData(WatchYourHealth.YEAR,-30,TimeUnit.DAYS)
                    Log.d("AuthToken", "Coroutine YEAR $dateFormat")

                }
                WatchYourHealth.YEAR_HOUR -> {
                    watchYourHealth.getStepsData(WatchYourHealth.YEAR_HOUR,LocalStorageTask(context).totalStandDays,TimeUnit.HOURS)
                    Log.d("AuthToken", "Coroutine YEAR_HOUR $dateFormat")

                }
                WatchYourHealth.MONTH -> {
                    watchYourHealth.getStepsData(WatchYourHealth.DAY_OF_WEEK_IN_MONTH,0,TimeUnit.DAYS)
                    Log.d("AuthToken", "Coroutine $dateFormat")

                }
                WatchYourHealth.LAST_DATA -> {
                    watchYourHealth.getStepsData(WatchYourHealth.LAST_DATA,LocalStorageTask(context).totalStepsDays,TimeUnit.DAYS)
                    Log.d("AuthToken", "Coroutine MONTH $dateFormat")

                }
                WatchYourHealth.DAY_OF_WEEK_IN_HOURS -> {
                    watchYourHealth.getStepsData(WatchYourHealth.DAY_OF_WEEK_IN_MONTH,0,TimeUnit.HOURS)
                    Log.d("AuthToken", "Coroutine DAY_OF_WEEK_IN_HOURS $dateFormat")

                }
                SLEEP_DATA -> {
                    watchYourHealth.getStepsData(SLEEP_DATA,LocalStorageTask(context).totalMinuteStepsDays,TimeUnit.MINUTES)
                    Log.d("AuthToken", "Coroutine SLEEP_DATA $SLEEP_DATA")

                }
            }

            launch(Dispatchers.IO) {
                when(dateFormat){
                    WatchYourHealth.DAY_OF_WEEK_IN_MONTH ->{
                        null
                    }
                    WatchYourHealth.YEAR -> {
                        SharedPreference.init(context)
                        SharedPreference.putAllStepDataSync(true)
                        LocalStorageTask(context).getDaysSteps(context)
                        CoroutineClass().runBackGroundTask(WatchYourHealth.LAST_DATA, context)
                        Log.d("AuthToken", "Coroutine $dateFormat")

                    }
                    WatchYourHealth.YEAR_HOUR ->{
                        CoroutineClass().runBackGroundTask(WatchYourHealth.DAY_OF_WEEK_IN_MONTH, context)
                        LocalStorageTask(context).getStand()
                        Log.d("AuthToken", "Coroutine $dateFormat")
                    }
                    WatchYourHealth.MONTH ->{
                        //Log.v("Count", "Inside Step Month");
                        LocalStorageTask(context).getSteps()
                        LocalStorageTask(context).getSleep()
                        LocalStorageTask(context).getHourlySteps(context)
                        LocalStorageTask(context).getMinuteSteps(context)
                        Log.d("AuthToken", "Coroutine $dateFormat")
                    }
                    WatchYourHealth.LAST_DATA ->{
                        CoroutineClass().runBackGroundTask(WatchYourHealth.MONTH, context)
                        Log.d("AuthToken", "Coroutine $dateFormat")

                    }
                    WatchYourHealth.DAY_OF_WEEK_IN_HOURS ->{
                        LocalStorageTask(context).getStand()
                        Log.d("AuthToken", "Coroutine $dateFormat")
                    }
                    SLEEP_DATA ->{
                        //Log.v("Count", "Inside Sleep Data");
                        Log.d("AuthToken", "Coroutine SLEEP_DATA $dateFormat")
                        NewSleepLogic.getNewSleepTime(context, LocalStorageTask(context).totalMinuteStepsDays, true)
                        LocalStorageTask(context).getSteps()
                        LocalStorageTask(context).getSleep()
                        LocalStorageTask(context).getStand()
                        var source = ""
                        if (SharedPreference.getGoogleFitConnection()) {
                            source = Constants.SOURCE_GOOGLEFIT
                        }
                        val sleep = watchYourHealth.getSleep(source, CommonUtils.todayDate())
                        SharedPref.putTodaySteps(watchYourHealth.getTotalSteps(source))
                        SharedPref.putTodaySleep(sleep.toString())
                        // Send Data to server
                        // Send Data to server
                        val intent = Intent(context, SendDataToServerReceiver::class.java)
                        context.sendBroadcast(intent)
                        sendToServer

                    }

                }
            }


        }
    }
}