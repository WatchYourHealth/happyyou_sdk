package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOTP {

    @SerializedName("otp")
    @Expose
    String otp;

    public VerifyOTP(String otp) {
        this.otp = otp;
    }
}
