package com.wyh.happyyousdk.model.response

data class SondeQuestionResponse(val msg: String, val success: Boolean, val data: questionData)

data class questionData(val questionname: String)
