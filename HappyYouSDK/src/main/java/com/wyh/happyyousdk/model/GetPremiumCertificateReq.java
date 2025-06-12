package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPremiumCertificateReq {

    @SerializedName("strClientId")
    @Expose
    String strClientId;
    @SerializedName("strFinYear")
    @Expose
    String strFinYear;

    public GetPremiumCertificateReq(String strClientId, String strFinYear) {
        this.strClientId = strClientId;
        this.strFinYear = strFinYear;
    }
}
