package com.wyh.happyyousdk.model.request.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileValidationRequest {
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("UserName")
    @Expose
    private String userName;

    public MobileValidationRequest(String mobileNo, String userName) {
        this.mobileNo = mobileNo;
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
