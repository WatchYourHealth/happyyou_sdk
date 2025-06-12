package com.wyh.happyyousdk.model.response

import com.wyh.happyyousdk.model.response.AssignRewardsResponse.SpinRewardsData
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData

data class GetMentalScoreResponse(val msg: String, val success: Boolean, val data: ScoreData, val feedbackDetails: FeedbackResponseData, val spinTheWheelRewardsDetail: SpinRewardsData, val quizathonRewardData: QuizathonRewardData)

data class ScoreData(val result: ScoreResult)

data class ScoreResult(val filePath: String, val measureName: String, val inferredAt: String,val userIdentifier: String, val id: String,
                       val type: String, val inference: ArrayList<Inference>)

data class Inference(val version: String, val type: String, val voiceFeatures: ArrayList<VoiceFeature>, val score: Score)

data class VoiceFeature(val name: String, val score: String)

data class Score(val value: String)
