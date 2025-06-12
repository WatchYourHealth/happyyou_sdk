package com.wyh.happyyousdk.model.response

data class GetChallengesResponse(val msg: String, val success: Boolean, val data: ArrayList<challengesData>)



data class challengesData(val ChallengeName: String, val ChallengeId: String, val IsUserEnrolled: String,
val ChallengeLogo: String, val ChallengePercentage: Int, val ChallengeStartDate: String,
                          val ChallengeEndDate: String, var ChallengeType: String = "",
                          val IsStarted:Int, var IsWinnerAnnounced:Boolean, var IsEventEnded:Boolean, )