package com.wyh.happyyousdk.model.response

data class GetGoalDayResponse(val msg: String, val success: Boolean, val data: ArrayList<GoalDayData>) {}

data class GoalDayData(val  goalName: String, val goalText: String,val target: String, val priority: String,val modelName: String
,val currentValue: String,val progressEntity: String)