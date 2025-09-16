package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileValidationData {
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("ishappyuuser")
    @Expose
    private String ishappyuuser;
    @SerializedName("userId")
    @Expose
    private String userId;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIshappyuuser() {
        return ishappyuuser;
    }

    public void setIshappyuuser(String ishappyuuser) {
        this.ishappyuuser = ishappyuuser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
