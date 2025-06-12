package com.wyh.happyyousdk.model.response

data class BadgesResponse(val msg: String, val success: Boolean, val data: ArrayList<RewardsBadgesData>)


data class RewardsBadgesData(val BadgeName: String, val BadgeLogo: String, val TransactionId: String, val IsScratched: String)
