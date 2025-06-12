package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendAbhOtpRequest {

    @SerializedName("accesstoken")
    @Expose
    public String accesstoken;

    @SerializedName("loginId")
    @Expose
    public String loginId;

    @SerializedName("Mode")
    @Expose
    public String Mode;


    public SendAbhOtpRequest(String accesstoken, String loginId,String Mode) {
        this.accesstoken = accesstoken;
        this.loginId = loginId;
        this.Mode = Mode;
    }
}
