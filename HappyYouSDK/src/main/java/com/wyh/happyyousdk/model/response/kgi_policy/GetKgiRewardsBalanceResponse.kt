package com.wyh.happyyousdk.model.response.kgi_policy

data class GetKgiRewardsBalanceResponse(
    val msg: String,
    val success: Boolean,
    val data: GetKgiRewardsBalanceDataResponse,
)

data class GetKgiRewardsBalanceDataResponse(
    val activities: List<GetKgiRewardsBalanceDataActivity>,
)

data class GetKgiRewardsBalanceDataActivity(
    val eventName: String,
    val description: String,
    val amount: Long,
    val transDate: String,
    val transStatus: String,
)