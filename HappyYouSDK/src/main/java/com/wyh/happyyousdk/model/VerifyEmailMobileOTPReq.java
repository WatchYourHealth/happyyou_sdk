package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VerifyEmailMobileOTPReq implements Serializable {

    @SerializedName("Email")
    @Expose
    private String Email;

    @SerializedName("CorpID")
    @Expose
    private  String CorpID;

    @SerializedName("otp")
    @Expose String otp;
    @SerializedName("mobile")
    @Expose String mobile;

    @SerializedName("EmailOtp")
    @Expose String EmailOtp;
    @SerializedName("otpType")
    @Expose String otpType;
    @SerializedName("MobileOtp")
    @Expose String MobileOtp;
    @SerializedName("saveData")
    @Expose boolean saveData;
    @SerializedName("isNewNumber")
    @Expose boolean isNewNumber;
    public VerifyEmailMobileOTPReq(String email, String corpID,  String mobile, String emailOtp, String mobileOtp,boolean savedata,boolean isNewnumber, String answerJson) {
        Email = email;
        CorpID = corpID;
        this.mobile = mobile;
        EmailOtp = emailOtp;
        MobileOtp = mobileOtp;
        saveData = savedata;
        isNewNumber = isNewnumber;
        AnswerJson = answerJson;
    }



    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String otpType) {
        this.otpType = otpType;
    }



    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCorpID() {
        return CorpID;
    }

    public void setCorpID(String corpID) {
        CorpID = corpID;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmailOtp() {
        return EmailOtp;
    }

    public void setEmailOtp(String emailOtp) {
        EmailOtp = emailOtp;
    }

    public String getMobileOtp() {
        return MobileOtp;
    }

    public void setMobileOtp(String mobileOtp) {
        MobileOtp = mobileOtp;
    }

    public String getAnswerJson() {
        return AnswerJson;
    }

    public void setAnswerJson(String answerJson) {
        AnswerJson = answerJson;
    }

    @SerializedName("AnswerJson")
    @Expose String AnswerJson;

}
