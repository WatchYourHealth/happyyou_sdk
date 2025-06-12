package com.wyh.happyyousdk.model.response

data class FaceScanRegistrationResponse(val msg: String, val success: Boolean,val data: FaceScanData)

data class FaceScanData(val scanId: String)
