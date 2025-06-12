package com.wyh.happyyousdk.model.response

import com.wyh.happyyousdk.model.response.AssignRewardsResponse.SpinRewardsData
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData

data class RespiratorySecondJobResponse(val msg: String, val success: Boolean, val data: SecondJobData,val spinTheWheelRewardsDetail:SpinRewardsData,val quizathonRewardData: QuizathonRewardData,val feedbackDetails: FeedbackResponseData)

data class SecondJobData(val score: String)
