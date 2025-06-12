package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUnitLinkedPolicyStatementPROReq {

    @SerializedName("strFromDate")
    @Expose
    String strFromDate;
    @SerializedName("strToDate")
    @Expose
    String strToDate;
    @SerializedName("strPolicyNumber")
    @Expose
    String strPolicyNumber;
    public GetUnitLinkedPolicyStatementPROReq(String strFromDate, String strToDate, String strPolicyNumber) {
        this.strFromDate = strFromDate;
        this.strToDate = strToDate;
        this.strPolicyNumber = strPolicyNumber;
    }


}
