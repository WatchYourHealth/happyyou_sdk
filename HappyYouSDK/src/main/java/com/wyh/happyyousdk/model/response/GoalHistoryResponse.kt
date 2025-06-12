package com.wyh.happyyousdk.model.response



data class GoalHistoryResponse(val msg: String,val success: Boolean, val data: GoalHistoryData)

data class GoalHistoryData(val pendingGoals: ArrayList<HistoryData>, val completedGoals: ArrayList<HistoryData>, val deletedGoals: ArrayList<HistoryData>)

data class HistoryData (val goalId: String, val goalName: String, val goalDescription: String, val onDashboard: Boolean,val noOfDays: Int,val goalTarget: Int, val progressPercentage: Float)