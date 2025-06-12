package com.wyh.happyyousdk.model.response.kgi_policy

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class GetPolicyDetailsResponse(
    val msg: String,
    val success: Boolean,
    val data: GetPolicyDetailsResponseData,
)

data class GetPolicyDetailsResponseData (
    val memberDetails: ArrayList<GetPolicyDetailsMemberDetail>,
    val userPolicyDetails: ArrayList<GetPolicyDetailsUserPolicyDetail>?,
)


data class GetPolicyDetailsMemberDetail (
    @SerializedName("policy_No")
    val policyNo: String,
    @SerializedName("member_ID")
    val memberId: Any?,
    @SerializedName("member_Name")
    val memberName: String?,
    val dob: String,
    val gender: String,
    val height: Double?,
    val weight: Double?,
    val age: Double?,
    @SerializedName("mobile_No")
    val mobileNo: String?,
    @SerializedName("email_Id")
    val emailId: String?,
)

data class GetPolicyDetailsUserPolicyDetail(
    @SerializedName("policy_Number")
    val policyNumber: String?,
    @SerializedName("insured_Name")
    val insuredName: String?,
    @SerializedName("policy_Start_Date")
    val policyStartDate: String?,
    @SerializedName("policy_End_Date")
    val policyEndDate: String?,
    @SerializedName("policy_Tenure")
    val policyTenure: String?,
    @SerializedName("policy_Status")
    val policyStatus: String?,
    @SerializedName("product_Name")
    val productName: String?,
    @SerializedName("internal_Product_Code")
    val internalProductCode: String?,
    @SerializedName("irdA_Product_Code")
    val irdAProductCode: String?,
    @SerializedName("plan_Name")
    val planName: String?,
    @SerializedName("email_ID")
    val emailId: String?,
    @SerializedName("policy_Type")
    val policyType: String?,
    @SerializedName("vaS_Category")
    val vaSCategory: String?,
    @SerializedName("mobile_No")
    val mobileNo: String?,
    var isChecked: Boolean = false,
)

data class GetPolicyDetailsShowModel(
    val policyNo: String?,
    val premium: String?,
    val premiumDueDate: String?,
    val premiumStatus: String?,
    val insuranceCover: String?,
    val issueDate: String?,
    val policyTerm: String?,
    val premiumPayingTerm: String?,
    val premiumFrequency: String?,
    val ecs: String?,
    val customerName: String?,
    val mobileNumber: String?,
    val policyStatus: String?,
    val dob: String?,
    val relation: String?,
    val email: String?,

    )