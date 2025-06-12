package com.wyh.happyyousdk.model.response

data class GetBranchUserListResponse(val msg: String, val success: Boolean, val data: ArrayList<BranchUserData>)

data class BranchUserData(val name: String,val enrollDate: String)
