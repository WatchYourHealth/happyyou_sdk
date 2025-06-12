package com.wyh.happyyousdk.model.response

data class TranscribeJobResponse(val msg: String, val success: Boolean, val isPoolingReq: Boolean, val data: TranscribeData)

data class TranscribeData(val jobId: String)
