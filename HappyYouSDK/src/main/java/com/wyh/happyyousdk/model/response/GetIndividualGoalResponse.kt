package com.wyh.happyyousdk.model.response

data class GetIndividualGoalResponse(val msg: String, val success: Boolean, val data: IndividualGoalData)

data class IndividualGoalData(val regularGoals: ArrayList<RegualrGoalsData>, val otherGoals: ArrayList<OtherGoalsData>)
data class OtherGoalsData(val goalId: String,val goalName: String,val goalDescription: String,val onDashboard: Boolean,val startDate:String,val isCompleted: Boolean,val progressPercentage: Int)
data class RegualrGoalsData(val goalId: String,val goalName: String,val goalDescription: String,val noOfDays: Int,val goalTarget: Int,val endDate: String,val onDashboard: Boolean
,var type: String = "regular",val progressPercentage: Int)