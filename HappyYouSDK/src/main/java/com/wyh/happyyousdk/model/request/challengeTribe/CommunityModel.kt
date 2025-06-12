package com.wyh.happyyousdk.model.request.challengeTribe

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import com.wyh.happyyousdk.model.response.BadgesData

data class GetCommunityRankDetailsRequest(
    @SerializedName("ChallengeId") val challengeId: String,
    @SerializedName("CommunityId") val communityId: Int,
    @SerializedName("PeriodType") val periodType: String,
    @SerializedName("PeriodIndex") val periodIndex: Int,
    @SerializedName("ActivityType") val activityType: String,
    @SerializedName("RankType") val rankType: String,
)

data class GetUserRanksRequest(
    @SerializedName("ChallengeId") val challengeId: String,
    @SerializedName("PeriodType") val periodType: String,
    @SerializedName("PeriodIndex") val periodIndex: Int,
    @SerializedName("ActivityType") val activityType: String,
    @SerializedName("CommunityId") val communityId: Int,
)

data class GetCommunityRankDetailsResponse(
    val msg: String,
    val success: Boolean,
    val data: GetCommunityRankDetailsData,
    val freevoucher: Any?,
    @JsonProperty("enGTokens")
    val enGtokens: EnGtokens,
    val rewards: Rewards,
    val userkey: Any?,
    val isGoogleFit: Boolean,
)

data class GetCommunityRankDetailsData(
    val tribeChallengeDashboard: TribeChallengeDashboard,
    val stackedGraphModel: StackedGraphModel
)

data class StackedGraphModel(
    val uuid: String,
    val periodType: String,
    val periodIndex: Long,
    val activityType: String,
    val dataPoints: List<ChallengeTribeDataPoint>,
)

data class ChallengeTribeDataPoint(
    val rtId: Long,
    val recordDate: String,
    val weeklyRange: Any?,
    val point: List<ChallengeTribePoint>,
)

data class ChallengeTribePoint(
    val point: Double,
    val name: String,
    val userId: String,
    val diffpoint: Double,
)

data class Badges(val transactionId: Int, val badgeName: String, val badgeLogo: String)

data class TribeChallengeDashboard(
    val tribeDetails: GetCommunityRankDetailsTribeDetails,
    val tribeUserDetails: List<GetCommunityRankDetailsTribeUserDetail>,
    val tribeLists: List<GetCommunityRankDetailsTribeList>,
    val challengeDetails: ChallengeDetails,
    val badges: ArrayList<BadgesData>
)


data class ChallengeDetails(
    val startDate: String,
    val endDate: String,
)

data class GetCommunityRankDetailsTribeUserDetail(
    val name: String,
    val totalSteps: String,
    val rank: String,
)

data class GetCommunityRankDetailsTribeList(
    val communityId: Long,
    val communityName: String,
)

data class GetCommunityRankDetailsTribeDetails(
    val communitySteps: String,
    val userRank: String = "",
    val tribeName: String = "",
    val tribeSteps: String = "",
    val tribeRank: String = "",
    val communityId: Int = 0,
    val challengeGoal: String = ""
)

data class EnGtokens(
    val tokens: Any?,
    val bonusTokens: Any?,
)

data class Rewards(
    val reward: Any?,
    val bonusRewards: Any?,
)
