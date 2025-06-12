package com.wyh.happyyousdk.model.response

import com.wyh.happyyousdk.model.response.dashboard.ActOMeterResponse
import com.wyh.happyyousdk.model.response.dashboard.ActOMeterTribe
import com.wyh.happyyousdk.model.response.dashboard.FilePopupModel
import com.wyh.happyyousdk.model.response.dashboard.RewardsResponse

data class NewDashboardResponse(val msg: String, val success: Boolean, val data: DashboardData) {
}

data class DashboardData(
    val fetchdashboarddata: FetchDashboardData,
    val nudgeRedirection: NudgeRedirection,
    val challengesDetails: ArrayList<challengesData>
)

data class FetchDashboardData(
    val actoMeter: ActOMeterResponse,
    val actoMeterTribe: ActOMeterTribe,
    val rewards: RewardsResponse,
    val hraTaken: Boolean,
    val corpReferralCode: String,
    val blogs: ArrayList<Blogs>,
    val showSpin: Boolean,
    val filePopupModellist: ArrayList<FilePopupModel>
)

data class Blogs(
    val articleID: Int,
    val articleName: String,
    val tags: String,
    val articleCode: String,
    val imgPath: String,
    val searchkey: String?,
    val isBookMarked: Boolean?
)


