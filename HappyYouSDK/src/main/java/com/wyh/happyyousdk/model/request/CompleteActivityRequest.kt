package com.wyh.happyyousdk.model.request

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part
import java.io.File

data class CompleteActivityRequest(
    @Part("Activityid") val activityID: MultipartBody.Part,
    @Part("ChallengeId") val challengeID: MultipartBody.Part,
    @Part("ActivityUploads") val activityUploads: MultipartBody.Part,
    @Part("EventType") val eventType: MultipartBody.Part

)
