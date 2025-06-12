package com.wyh.happyyousdk.model.request

data class RespiratorySecondJobRequest(val filePath: String, val access_token: String, val inferenceid: String, val userIdentifier: String, val signedURL: String) {
}