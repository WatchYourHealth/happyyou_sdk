package com.wyh.happyyousdk.Goals.ClickInterface

import com.wyh.happyyousdk.model.response.RecomendedGoalsData

interface RecommendedGoals {
    fun onClick(goalData : RecomendedGoalsData)
}