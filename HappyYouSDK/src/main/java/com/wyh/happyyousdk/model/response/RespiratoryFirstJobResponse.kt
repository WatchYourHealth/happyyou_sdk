package com.wyh.happyyousdk.model.response

data class RespiratoryFirstJobResponse(val msg: String, val success: Boolean, val data: FirstJobData)

data class FirstJobData(val id: String, val type: String)
