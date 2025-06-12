package com.wyh.happyyousdk.model.request.challengeTribe

import com.fasterxml.jackson.annotation.JsonProperty

data class ViewMoreTribeResponseModel(
    val msg: String,
    val success: Boolean,
    val data: List<ViewMoreTribeResponseDataModel>,
    val freevoucher: Any?,
    @JsonProperty("enGTokens")
    val enGtokens: EnGtokens,
    val rewards: Rewards,
    val userkey: Any?,
    val isGoogleFit: Boolean,
)

data class ViewMoreTribeResponseDataModel(
    val communityId: Long,
    val communityName: String,
    val communityRank: String,
    val communitySteps: String,
    val isMember: Boolean,
)