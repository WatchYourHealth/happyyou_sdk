package com.wyh.happyyousdk.model.response

data class GetActivityResponse(var msg: String, val success: Boolean, val data: ActivityData)

data class ActivityData(
    var activityLists: ArrayList<ActivityListData>,
    var userActivities: ArrayList<UserActivitiesListData>
)

data class ActivityListData(var activityId: Int, val activityName: String, val activityType: String?)

data class UserActivitiesListData(
    var activityRewards: String?,
    val activityName: String,
    val isVerified: Boolean,
    val activityFilePath: String,
    val comment: String?,
    val addeddate: String?,
    val status: String?
)
