package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyAbhaAdressOtpRequest {


    @SerializedName("accesstoken")
    @Expose
    private String accesstoken;

    @SerializedName("otp")
    @Expose
    private String otp;

    @SerializedName("txnId")
    @Expose
    private String txnId;
    @SerializedName("mode")
    @Expose
    private String mode;



    public VerifyAbhaAdressOtpRequest(String accesstoken, String otp, String txnId,String mode) {
        this.accesstoken = accesstoken;
        this.otp = otp;
        this.txnId = txnId;
        this.mode = mode;
    }
}
