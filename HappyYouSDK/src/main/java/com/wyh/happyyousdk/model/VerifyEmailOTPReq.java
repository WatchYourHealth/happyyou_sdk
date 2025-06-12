package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VerifyEmailOTPReq implements Serializable {

    @SerializedName("Email")
    @Expose
    private String Email;

    @SerializedName("CorpID")
    @Expose
    private  String CorpID;

    @SerializedName("otp")
    @Expose String otp;
    public VerifyEmailOTPReq(String email, String corpID, String otp) {
        Email = email;
        CorpID = corpID;
        this.otp = otp;
    }


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getCorpID() {
        return CorpID;
    }

    public void setCorpID(String corpID) {
        CorpID = corpID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


}
