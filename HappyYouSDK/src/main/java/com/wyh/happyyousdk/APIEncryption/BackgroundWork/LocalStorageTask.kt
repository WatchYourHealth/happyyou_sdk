package com.wyh.happyyousdk.APIEncryption.BackgroundWork

import android.content.Context
import android.util.Log
import com.wyh.happyyousdk.utils.CommonUtils
import com.wyh.happyyousdk.utils.Constants
import com.wyh.happyyousdk.utils.SharedPref
import com.wyhsdk.main.WatchYourHealth
import com.wyhsdk.sharedPreferences.SharedPreference
import com.wyhsdk.utils.Utilities
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit

class LocalStorageTask(context: Context) {

    var watchYourHealth: WatchYourHealth
    var totalStepsDays: Int = 0
    var hourlyStep: Boolean = false
    var totalCalorieBurned: Int = 0
    var totalStandDays: Int = 30;
    var totalMinuteStepsDays: Int = 30;

    init {
        watchYourHealth = WatchYourHealth(context, SharedPref.getUuid())
    }




    fun getStepsCount(){
        try {
            val lastDate = watchYourHealth.lastStepsDate
            //String lastDate = "2019-11-20";
            //String lastDate = "2019-11-20";
            val todayDateNew = Utilities.getTodayDateNew()
            val sdf = SimpleDateFormat("yyyy-MM-dd")

            val date = sdf.parse(lastDate)
            val date1 = sdf.parse(todayDateNew)
            val diff = date1.time - date.time
            val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
            totalStepsDays = days

        }catch (e: Exception){
            e.toString()
        }
    }

     fun getDaysSteps(context: Context) {
        if (watchYourHealth.isStepExist) {
            try {
                val lastDate = watchYourHealth.lastStepsDate
                //String lastDate = "2019-11-20";
                val todayDateNew = Utilities.getTodayDateNew()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val date = sdf.parse(lastDate)
                val date1 = sdf.parse(todayDateNew)
                val diff = date1.time - date.time
                val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
                totalStepsDays = days
                //Toast.makeText(DashboardActivity.this,""+totalStepsDays,Toast.LENGTH_SHORT).show();
                //new ViewStepsCount(WatchYourHealth.LAST_DATA).execute();
                //System.out.println("Days Data: " + totalStepsDays);
                //new ViewStepsCount(WatchYourHealth.LAST_DATA).execute();
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        } else {
            CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR, context)

            //DashboardActivity.ViewStepsCount(WatchYourHealth.YEAR).execute()
        }
    }

    fun getStand(){
        try{
            if (!hourlyStep) {
                val minHourlyStepsDate = watchYourHealth.minHourlyStepsDate
                try {
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    val startDate = formatter.parse(minHourlyStepsDate)
                    val endDate = formatter.parse(Utilities.getTodayDateNew())
                    val start = Calendar.getInstance()
                    start.time = startDate
                    val end = Calendar.getInstance()
                    end.time = endDate
                    //end.add(Calendar.DATE, 1);
                    //Date newEndDate = end.getTime();
                    //Log.v("DAte",""+end);
                    var date = start.time
                    while (start.before(end)) {

                        // Do your job here with `date`.
                        val spf = SimpleDateFormat("yyyy-MM-dd")
                        val convertDate = spf.format(date)
                        println("Stand Date : $convertDate")
                        //String todayDateNew = Utilities.getTodayDateNew();
                        val standCount = watchYourHealth.getTodayHourlyStandCount(convertDate)
                        //System.out.println("Stand Count : " + standCount);
                        watchYourHealth.insertStandCount(standCount, convertDate, true)
                        val activeStandCount = watchYourHealth.getTodayHourlyStandCount(
                            convertDate,
                            Constants.ACTIVE_STEPS_COUNT
                        )
                        //System.out.println("Active Stand Count : " +
                        // activeStandCount);
                        Log.d("Active Stand Count : ", "$convertDate --> $activeStandCount")
                        watchYourHealth.insertActiveHourCount(activeStandCount, convertDate)
                        start.add(Calendar.DATE, 1)
                        date = start.time
                    }
                    hourlyStep = true
                } catch (e: java.lang.Exception) {
                    //Log.e("Stand Exception", e.message!!)
                }
            }

            val standCount = watchYourHealth.getTodayHourlyStandCount(Utilities.getTodayDateNew())
            watchYourHealth.insertStandCount(standCount, Utilities.getTodayDateNew(), true)
            val stand = watchYourHealth.stand

            val activeStandCount = watchYourHealth.getTodayHourlyStandCount(
                Utilities.getTodayDateNew(),
                Constants.ACTIVE_STEPS_COUNT
            )
            Log.d("Active Stand Count : ", Utilities.getTodayDateNew() + " --> " + activeStandCount)
            watchYourHealth.insertActiveHourCount(
                activeStandCount,
                Utilities.getTodayDateNew()
            )
            val activeHour = watchYourHealth.activeHour

        }catch (e: Exception){
            e.toString()
        }
    }

    fun getSteps(){
        try {

            //watchYourHealth = new WatchYourHealth(DashboardActivity.this);
            if (SharedPreference.getGoogleFitConnection()) {
                val stepCount = watchYourHealth.getTotalSteps(com.wyhsdk.utils.Constants.SOURCE_GOOGLEFIT)
                SharedPref.putTodaySteps(stepCount)
                if(SharedPref.getUserCalorieBurned() == 0){
                    val totalWalkedSteps = stepCount.toInt() * Constants.perStepInKm
                    val walkedValue = String.format("%.2f", totalWalkedSteps)

                    totalCalorieBurned = Math.round(stepCount.toInt() * Constants.perCalorieInStep).toInt()
                    SharedPref.putUserCalorieBurned(totalCalorieBurned)
                    Log.d("Calories", totalCalorieBurned.toString())
                }

                /*if (homeCarouselAdapter != null)
                    homeCarouselAdapter.notifyDataSetChanged()*/
                /*if (homeCardsAdapter != null)
                homeCardsAdapter.notifyDataSetChanged();*/
            }

        }catch (e: Exception){
            e.toString()
        }
    }

    fun getSleep(){
        try {
            //watchYourHealth = new WatchYourHealth(DashboardActivity.this);
            var sleep = 0
            if (SharedPreference.getGoogleFitConnection()) {
                sleep = watchYourHealth.getSleep(com.wyhsdk.utils.Constants.SOURCE_GOOGLEFIT, CommonUtils.todayDate())
                Log.d("AuthToken", "Coroutine Sleep Data $sleep")
            }
            Log.d("Sleep data", "sleep $sleep")
            if (sleep != 0) {
                val sleepHourCount = CommonUtils.convertMinutesIntoHour(sleep)
                Log.d("AuthToken", "Coroutine Sleep Hour $sleepHourCount")

                //SharedPref.putTodaySleep(sleepHourCount.toString())
            } else {
                /*sleep = watchYourHealth.getSleep(com.wyhsdk.utils.Constants.SOURCE_GOOGLEFIT, CommonUtils.todayDate())
                val sleepHourCount = CommonUtils.convertMinutesIntoHour(sleep)

                SharedPref.putTodaySleep(sleepHourCount.toString())
                Log.d("AuthToken", "Coroutine Sleep Data else $sleep")
                Log.d("AuthToken", "Coroutine Sleep Hour else$sleepHourCount")*/
            }
        }catch (e: Exception){
            e.toString()
        }
    }

    fun getHourlySteps(context: Context){
        try{
            if (watchYourHealth.isHourlyStepExist) {
                try {
                    val lastDate = watchYourHealth.lastStandDate
                    //String lastDate = "2019-11-20";
                    val todayDateNew = Utilities.getTodayDateNew()
                    //Toast.makeText(getApplicationContext(), lastDate, Toast
                    //LENGTH_LONG).show();
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val date = sdf.parse(lastDate)
                    val date1 = sdf.parse(todayDateNew)
                    val diff = date1.time - date.time
                    val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
                    Log.d("AuthToken", "getHourlySteps $days")
                    totalStandDays = days
//                    CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR_HOUR, context)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            } else {
                totalStandDays = 30
//                CoroutineClass().runBackGroundTask(WatchYourHealth.YEAR_HOUR, context)
            }
        }catch (e: Exception){
            e.toString()
        }
    }

    fun getMinuteSteps(context: Context){
        try{
            if (watchYourHealth.isMinuteStepExist) {
                try {
                    val lastDate = watchYourHealth.lastMinuteStepsDate
                    //String lastDate = "2019-11-20";
                    val todayDateNew = Utilities.getTodayDateNew()
                    //Toast.makeText(MainService.this, lastDate, Toast.LENGTH_LONG).show();
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    if (lastDate != null) {
                        val date = sdf.parse(lastDate)
                        val date1 = sdf.parse(todayDateNew)
                        val diff = date1!!.time - date!!.time
                        val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
                        totalMinuteStepsDays = days
                        Log.d("AuthToken", "getMinuteSteps $days")
                        Log.v("Sleep Last Date", "$lastDate.$days")
                    } else {
                        totalMinuteStepsDays = 30
                    }
//                    CoroutineClass().runBackGroundTask(WatchYourHealth.SLEEP_DATA, context)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            } else {
                totalMinuteStepsDays = 30
//                CoroutineClass().runBackGroundTask(WatchYourHealth.SLEEP_DATA, context)
            }
        }catch (e: Exception){
            e.toString()
        }
    }



}