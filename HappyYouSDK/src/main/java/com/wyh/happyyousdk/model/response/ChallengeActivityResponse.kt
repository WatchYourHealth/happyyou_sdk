package com.wyh.happyyousdk.model.response

data class ChallengeActivityResponse(val msg: String,val success: Boolean, val data: challengeActivityData) {
}


data class challengeActivityData(val userStatus: UserStatus, val challengebanners: ArrayList<ChallengeBanners>,
                                 val challengeActivityDetails : ArrayList<ChallenegActivities>, val badges: ArrayList<BadgesData> )

data class UserStatus(val userRank: Int, val activityCompleted: Int, val totalChallengeActivity: Int, val challengeName: String, val totalJoined: Int,val badgeCount: Int)

data class ChallengeBanners(val bannerName: String, val bannerLogo: String)

data class ChallenegActivities(val activityId: Int, val activityName: String, val activityType: String, val activityDesc: String
, val whatToDo: String, val howToDo: String, val whyToDo: String, var isStarted: Boolean, var isCompleted: Boolean, var progressPercentage: Int,val activityImage:String,val shareContent: ArrayList<String>
,val  redirection: String)

