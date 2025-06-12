package com.wyh.happyyousdk.Goals.ClickInterface

import com.wyh.happyyousdk.model.response.HistoryData

interface HistorySeeAll {

    fun goalHistoryClick(data : ArrayList<HistoryData>, comingFrom : String)
}