package com.wyh.happyyousdk.model.response

data class RecomendedGoalsResponse(val msg: String, val success: Boolean,val data: ArrayList<RecomendedGoalsData>)

data class RecomendedGoalsData(val goalName: String, val goalText: String, val target: String, val modelName: String, val progressEntity: String
,var goaladded : Int)
