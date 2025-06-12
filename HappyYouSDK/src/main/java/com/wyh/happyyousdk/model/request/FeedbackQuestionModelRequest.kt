package com.wyh.happyyousdk.model.request

import com.google.gson.annotations.SerializedName

data class FeedbackQuestionModelRequest(
    @SerializedName("CCMMId") val ccmmid: Int,
    @SerializedName("FeatureModuleName") val featureModuleName: String,
    @SerializedName("AnswerJSON") var answerJSON: String,
)