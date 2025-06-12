package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateCoopCodeRequest {

    @SerializedName("ReferralCode")
    @Expose
    private String ReferralCode;

    public UpdateCoopCodeRequest(String referralCode) {
        ReferralCode = referralCode;
    }
}
