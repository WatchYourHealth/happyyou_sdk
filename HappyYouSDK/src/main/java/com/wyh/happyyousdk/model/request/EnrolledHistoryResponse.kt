package com.wyh.happyyousdk.model.request

data class EnrolledHistoryResponse(val msg: String, val success: Boolean, val data: HistroyData) {
}

data class HistroyData(val enrollStats: ArrayList<EnrolledData>, val branchDetails: ArrayList<BranchData>)

data class EnrolledData(val myBranchEnrollmentCount: String,val myBranchEnrollmentPercentage: String, val allIndiaEnrollmentCount: String,val allIndiaEnrollmentPercentage: String)
data class BranchData(val branchName: String, val scannedDate: String, val branchCode: String, val branchAddress: String, val participationCode: String)