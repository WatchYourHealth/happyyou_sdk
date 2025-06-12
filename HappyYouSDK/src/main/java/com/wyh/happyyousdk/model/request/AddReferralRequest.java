package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddReferralRequest {
    @SerializedName("referredByUUID")
    @Expose
    private String referredByUUID;

    public AddReferralRequest(String referredByUUID) {
        this.referredByUUID = referredByUUID;
    }

    public String getReferredByUUID() {
        return referredByUUID;
    }

    public void setReferredByUUID(String referredByUUID) {
        this.referredByUUID = referredByUUID;
    }
}
