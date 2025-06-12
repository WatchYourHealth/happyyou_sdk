package com.wyh.happyyousdk.ChallangesModule.ClickInterface

import androidx.recyclerview.widget.RecyclerView
import com.wyh.happyyousdk.model.response.challengesData

interface ChallengeClick {
    fun onClickPerform(challengeName: String, position: Int)

    fun joinNowClick(data: challengesData, challengeID: String, position: Int, challengeType: String, communityID : Int, recyclerView: RecyclerView)

}