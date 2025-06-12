package com.wyh.happyyousdk.model.request

import com.google.gson.annotations.SerializedName

data class AddUserFeedbackRequest(@SerializedName("ModuleName") val moduleName: String,
                                  @SerializedName("Stars") val stars: Int,
                                  @SerializedName("Keywords") val keywords: String,
                                  @SerializedName("Comments") val comments: String,
                                  @SerializedName("DaysMappingId") val daysMappingId: Int)
