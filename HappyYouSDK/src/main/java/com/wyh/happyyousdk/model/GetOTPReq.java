package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOTPReq {

    @SerializedName("applicationID")
    @Expose
    String applicationID;
    @SerializedName("emailID")
    @Expose
    String emailID;
    @SerializedName("userID")
    @Expose
    String userID;
    @SerializedName("mobileNo")
    @Expose
    String mobileNo;

    @SerializedName("validityDuration")
    @Expose
    int validityDuration;

    public GetOTPReq(String applicationID, String emailID, String userID, String mobileNo, int validityDuration) {
        this.applicationID = applicationID;
        this.emailID = emailID;
        this.userID = userID;
        this.mobileNo = mobileNo;
        this.validityDuration = validityDuration;
    }
}
