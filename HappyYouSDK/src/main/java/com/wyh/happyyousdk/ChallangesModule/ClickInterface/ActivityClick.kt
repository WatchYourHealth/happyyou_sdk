package com.wyh.happyyousdk.ChallangesModule.ClickInterface

import com.wyh.happyyousdk.databinding.ActivityChallengeDetailBinding
import com.wyh.happyyousdk.model.response.ChallenegActivities

interface ActivityClick {

    fun onActivityClick(challengeActivity: ChallenegActivities, activityBinding: ActivityChallengeDetailBinding)
}