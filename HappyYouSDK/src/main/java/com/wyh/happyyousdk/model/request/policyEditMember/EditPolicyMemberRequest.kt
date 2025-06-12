package com.wyh.happyyousdk.model.request.policyEditMember

data class EditPolicyMemberRequest(
    val mobile: String,
    val emailid: String,
    val memberid: String,
    val name: String,
    val relation: String
)