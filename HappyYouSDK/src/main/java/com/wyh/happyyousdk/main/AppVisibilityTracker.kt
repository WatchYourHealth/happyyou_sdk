package com.wyh.happyyousdk.main

import android.app.Activity
import android.app.Application
import android.os.Bundle

class AppVisibilityTracker : Application.ActivityLifecycleCallbacks {

    private var activityCount = 0
    var isAppinForeground : Boolean = false

    override fun onActivityStarted(activity: Activity) {
        if (activityCount == 0) {
            // App moved to foreground
            onAppForegrounded()
        }
        activityCount++
    }

    override fun onActivityStopped(activity: Activity) {
        activityCount--
        if (activityCount == 0) {
            // App moved to background
            onAppBackgrounded()
        }
    }

    // Other lifecycle methods not mandatory
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    private fun onAppForegrounded() {
        // Host app is in foreground
        isAppinForeground = true
    }

    private fun onAppBackgrounded() {
        // Host app is in background
        isAppinForeground = false
    }
}
