package com.wyh.happyyousdk.model.response

data class ChallengeRankResponse(val msg: String, val success: String, val data: ChallengeRankData) {}

data class ChallengeRankData( val totalSteps: String, val totalParticipation: String,val showbranch: Boolean,val showindividual: Boolean, val branchRankings: BranchRankingsData?, var userRankings: UserRankingData?, val globalRankings: GlobalRankingData?)

data class BranchRankingsData( val totalSteps: String, val avgSteps: String, val rank: String,val enrollmentCount: String, val enrollmentPercentage: String, val branchRankings: ArrayList<BranchRanking>)

data class BranchRanking(val branchName: String, val avgSteps: String,val rank: String,val participation: String, val totalHeadCount:String)

data class UserRankingData(val userBranchRank: String,val branchRank: String,val allIndiaUserRank: String,val userRankings: ArrayList<UserRanking>)

data class UserRanking(val branchName: String, val userName: String,val steps: String,val rank: String)

data class GlobalRankingData(val totalSteps: String, val avgSteps: String,val rank: String, val userAllIndiaRank: String,val userRankings: ArrayList<GlobalRanking>)

data class GlobalRanking(val branchName: String, val userName: String, val steps: String, val rank: String,val communityId: Int)

