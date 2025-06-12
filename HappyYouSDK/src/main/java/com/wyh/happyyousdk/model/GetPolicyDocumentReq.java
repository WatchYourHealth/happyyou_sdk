package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPolicyDocumentReq {

    @SerializedName("policyNo")
    @Expose
    String policyNo;
    @SerializedName("clientId")
    @Expose
    String clientId;
    public GetPolicyDocumentReq(String policyNo, String clientId) {
        this.policyNo = policyNo;
        this.clientId = clientId;
    }
}
