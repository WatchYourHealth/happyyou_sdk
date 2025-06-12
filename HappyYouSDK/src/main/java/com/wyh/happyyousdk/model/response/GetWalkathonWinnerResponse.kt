package com.wyh.happyyousdk.model.response

import com.google.gson.annotations.SerializedName


data class GetWalkathonWinnerResponse(
    val msg: String,
    val success: Boolean,
    val isPoolingReq: Boolean,
    val data: GetWalkathonWinnerData,
    val feedbackDetails: Any?,
    val freevoucher: Any?,
    @SerializedName("enGTokens")
    val enGtokens: EnGtokens,
    val rewards: Rewards,
    val userkey: Any?,
    val isGoogleFit: Boolean,
    val userDetail: Any?,
)

data class GetWalkathonWinnerData(
    val userSteps: List<WinnerUserStep>,
    val branchSteps: List<WinnerBranchStep>,
)

data class WinnerUserStep(
    val username: String,
    val avgSteps: Long,
    val totalSteps: Long,
    val allIndiaRank: String,
    val branchName: String,
)

data class WinnerBranchStep(
    val branchName: String?,
    val participationPercentage: Double?,
    val avgSteps: Long?,
    val totalSteps: Long?,
    val branchRanking: String?,
)

data class EnGtokens(
    val tokens: Any?,
    val bonusTokens: Any?,
)

data class Rewards(
    val reward: Any?,
    val bonusRewards: Any?,
)