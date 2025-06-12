package com.wyh.happyyousdk.model.request

data class LogExceptionRequest(val ApiUrl: String, val ExceptionMessage: String, val UUID: String, val ErrorCode: String) {
}