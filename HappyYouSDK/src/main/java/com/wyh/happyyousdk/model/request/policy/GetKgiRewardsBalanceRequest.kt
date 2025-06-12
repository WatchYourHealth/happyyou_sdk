package com.wyh.happyyousdk.model.request.policy

import com.google.gson.annotations.SerializedName

data class GetKgiRewardsBalanceRequest(@SerializedName("TransStatus") val transStatus:String)