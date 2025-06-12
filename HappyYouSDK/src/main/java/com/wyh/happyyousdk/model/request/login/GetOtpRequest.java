package com.wyh.happyyousdk.model.request.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOtpRequest {
    @SerializedName("mobile")
    @Expose
    private String mobile;

    public GetOtpRequest(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
