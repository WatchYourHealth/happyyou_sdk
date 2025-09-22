package com.wyh.happyyousdk.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.wyh.happyyousdk.dashboard.model.GetBannerResponse
import com.wyh.happyyousdk.model.AuthDataResp
import com.wyh.happyyousdk.model.ClientResponseDashboard
import com.wyh.happyyousdk.model.EnGTokensModel
import com.wyh.happyyousdk.model.RewardsModel
import com.wyh.happyyousdk.model.response.dashboard_new.ShowAdminRewardsEventsData
import com.wyh.happyyousdk.model.response.playwin.ClaimReClaimRewardModel
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData
import com.wyh.happyyousdk.model.response.rewards.PopRewardModel

data class GetUserDetailResponse(val msg: String, val success: Boolean, var data: UserDetailData, var feedbackDetails: FeedbackResponseData,val enGTokens: EnGTokensModel,val rewards: RewardsModel)


data class UserDetailData(val userDetails: UserDetail, val policyDetails: ArrayList<PolicyDetails>, val levelPopup: String, val popRewards: ArrayList<PopRewardModel>, val addondetails: AddOnDetails,
                          val showAdminRewardsEvents: ArrayList<ShowAdminRewardsEventsData>, var quizathonRewardList: ArrayList<ClaimReClaimRewardModel>,var activitpopuplist: ArrayList<ClaimReClaimRewardModel>, val rewards: RewardsModel, val enGTokens:  EnGTokensModel, val banners : ArrayList<GetBannerResponse.Data>, val referrals: Referrals, val wellbeingActivity : ArrayList<String>, val authData: AuthDataResp, val clientsPolicyData:ArrayList<ClientResponseDashboard>)

data class UserDetail(val name: String,val profileImage: String,val gender: String, val age: String,val abhaNumber: String,val referralCode: String, val currentLevel: String, val totalPoints: String,val connectionCount: String,val stampCount: String,val tribeCount: String,
val referalCount: String,val dareReferalCount: String,val otherCount: String,val corporateLogo: String,val isCorporateEmployee:Int,val corporateName:String,val corporateId:String,val marshUrl : String, val opdId : Int, val opdTileName: String?)

data class PolicyDetails(val policyNo: String, var policyExpiryDate: String)

data class Referrals(val referedBy: String,val isReferrer: Boolean,val showCard: Boolean)




