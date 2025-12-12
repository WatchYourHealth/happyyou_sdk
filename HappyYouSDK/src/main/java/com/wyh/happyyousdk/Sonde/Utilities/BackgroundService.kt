package com.wyh.happyyousdk.Sonde.Utilities

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.exoplayer2.util.Log
import com.wyh.happyyousdk.Sonde.Activities.AnalysisScreen

class BackgroundService : Service() {

    private var jobID = ""


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AuthToken","BackgroundService Started")
        jobID = intent?.getStringExtra("jobID").toString()
        //Thread.sleep(10000)
        AnalysisScreen().getTranscribeText(jobID,applicationContext)
        return START_STICKY
    }

    override fun onDestroy() {

        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}