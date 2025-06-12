package com.wyh.happyyousdk.model.request

data class SondeTokenResponse(val msg: String, val success: Boolean, val data : TokenDataClass) {
}

data class TokenDataClass(val access_token: String)