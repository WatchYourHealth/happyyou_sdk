package com.wyh.happyyousdk.model.request

data class SondeUserRegistrationRequest(val access_token: String, val gender: String, val yearOfBirth: String, val device: DeviceInfo, val UserType: String)

data class DeviceInfo(val type: String,val manufacturer: String)
