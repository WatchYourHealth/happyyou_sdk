package com.wyh.happyyousdk.model.request.policy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPolicyRequest {
    @SerializedName("userMobile")
    @Expose
    private String userMobile;

    public GetPolicyRequest(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
}
