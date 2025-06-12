package com.wyh.happyyousdk.model.response

data class RewardListResponse(val msg: String,val success: Boolean,val data: RewardData) {
}

data class RewardData(val rewardsSummary: SummaryData,val stampList: ArrayList<StampListData>,val voucherList: ArrayList<voucherListData>,val badgeList: ArrayList<badgeListData>)

data class StampListData(val activityName: String,val earnedStamps: String,val transactionDate: String)

data class voucherListData(val voucherName: String,val voucherLogo: String,
                           val voucherDesc: String,val isScratched: Boolean,
                           val voucherCode: String,val transactionDate: String,
                           val voucherValue: String,val id: Int,val validity: String,val status: String,val partnerName: String,val partnerLogo: String,val partnerUrl: String)

data class badgeListData(val badgeName: String,val badgeLogo: String,val isScratched: Boolean,val transactionDate: String)

data class SummaryData(val stampCount: String,val availableStamps: String,val voucherCount: String?,val badgeCount: String?)