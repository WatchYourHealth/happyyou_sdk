package com.wyh.happyyousdk.model.response

data class SondeHistoryResponse(val msg: String, val success: Boolean, val data: ArrayList<SondeHistoryData>)

data class SondeHistoryData(val score: String, val feature: String, val TestDate: String, val subFeatureScore: String)

data class MentalWellnessVoiceFeature(val name: String, val score: String)
