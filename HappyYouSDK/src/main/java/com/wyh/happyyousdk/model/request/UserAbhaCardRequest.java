package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAbhaCardRequest {

    @SerializedName("usertoken")
    @Expose
    private String usertoken;

    @SerializedName("sessiontoken")
    @Expose
    private String sessiontoken;

    public UserAbhaCardRequest(String usertoken, String sessiontoken) {
        this.usertoken = usertoken;
        this.sessiontoken = sessiontoken;
    }
}
