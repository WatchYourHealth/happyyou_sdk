package com.wyh.happyyousdk.model.request

data class AddIndividualGoalRequest(val goalName: String, val goalDescription: String,val noOfDays: Int,val goalTarget: Int) {
}