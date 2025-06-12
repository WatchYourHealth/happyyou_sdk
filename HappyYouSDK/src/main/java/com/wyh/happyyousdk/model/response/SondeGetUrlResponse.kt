package com.wyh.happyyousdk.model.response

data class SondeGetUrlResponse(val msg: String, val success: Boolean, val data: GetUrlData) {
}

data class GetUrlData(val signedURL: String, val filePath: String, val requestId: String)