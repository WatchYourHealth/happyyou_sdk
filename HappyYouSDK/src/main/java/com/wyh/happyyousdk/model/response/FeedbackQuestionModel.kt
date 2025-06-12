package com.wyh.happyyousdk.model.response

import com.google.gson.annotations.SerializedName

data class FeedbackQuestionModel(@SerializedName("custom_feedback") val customFeedback: List<FeedbackQuestionModelData>,
                                 @SerializedName("description") val description: String)


data class FeedbackQuestionModelData(
    @SerializedName("question") val question: String,
    @SerializedName("type") val type: String,
    @SerializedName("ans") var ans: String,
    @SerializedName("option") val option: List<FeedbackOption>,
)

data class FeedbackOption (
    @SerializedName("option") val option: String,
    @SerializedName("nextquestion") val nextquestion: Int,
)
