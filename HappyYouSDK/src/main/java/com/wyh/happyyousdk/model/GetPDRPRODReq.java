package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPDRPRODReq {

    @SerializedName("clientId")
    @Expose
    String clientId;
    @SerializedName("policyNo")
    @Expose
    String policyNo;
    @SerializedName("year")
    @Expose
    String year;
    @SerializedName("month")
    @Expose
    String month;
    public GetPDRPRODReq(String clientId, String policyNo, String year, String month) {
        this.clientId = clientId;
        this.policyNo = policyNo;
        this.year = year;
        this.month = month;
    }


}
