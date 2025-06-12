package com.wyh.happyyousdk.model.response

data class GetHappyMartTiles(val msg: String, val success: Boolean, val data: ArrayList<HappyMartTilesData>) {
}

data class HappyMartTilesData(val Id: Int, val CategoryName: String, val CategoryImagePath: String)