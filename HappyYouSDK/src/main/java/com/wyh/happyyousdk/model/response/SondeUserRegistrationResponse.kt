package com.wyh.happyyousdk.model.response

data class SondeUserRegistrationResponse(val msg: String, val success: Boolean, val data: UserRegistrationData)

data class UserRegistrationData(val userIdentifier: String, val requestId: String)
