package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbhaAdressOtpRequest {

    @SerializedName("accesstoken")
    @Expose
    private String accesstoken;

    @SerializedName("authMethod")
    @Expose
    private String authMethod;

    @SerializedName("healthid")
    @Expose
    private String healthid;

    public AbhaAdressOtpRequest(String accesstoken, String authMethod, String healthid) {
        this.accesstoken = accesstoken;
        this.authMethod = authMethod;
        this.healthid = healthid;
    }
}
