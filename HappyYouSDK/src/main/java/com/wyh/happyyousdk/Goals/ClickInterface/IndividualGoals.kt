package com.wyh.happyyousdk.Goals.ClickInterface

import android.content.Context

interface IndividualGoals {

    fun menuClick(name : String)

    fun goalClick(name : String, noOfDays: Int, target: Int, context: Context)

    fun goalRedirection(goalName : String,goalDesc: String)
}
