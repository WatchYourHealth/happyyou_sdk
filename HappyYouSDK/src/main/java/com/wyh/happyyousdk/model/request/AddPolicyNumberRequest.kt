package com.wyh.happyyousdk.model.request

import com.google.gson.annotations.SerializedName

data class AddPolicyNumberRequest(
    @SerializedName("PolicyNo") val PolicyNo: String,
    @SerializedName("UserId") val uuid: String
)