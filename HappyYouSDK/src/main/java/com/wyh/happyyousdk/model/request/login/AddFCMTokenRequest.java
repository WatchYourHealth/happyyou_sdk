package com.wyh.happyyousdk.model.request.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFCMTokenRequest {

    @SerializedName("fcmToken")
    @Expose
    private String fcmToken;
    @SerializedName("mobile")
    @Expose
    private String mobile;

    public AddFCMTokenRequest(String fcmToken, String mobile) {
        super();
        this.fcmToken = fcmToken;
        this.mobile = mobile;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
