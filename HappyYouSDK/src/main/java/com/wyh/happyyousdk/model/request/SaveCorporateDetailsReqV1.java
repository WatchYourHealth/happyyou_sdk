package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaveCorporateDetailsReqV1 implements Serializable {
    @SerializedName("EmailId")
    @Expose
    private String EmailId;
    @SerializedName("AnswerJson")
    @Expose
    private String AnswerJson;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("otpType")
    @Expose
    private String otpType;
    @SerializedName("isNewNumber")
    @Expose
    private boolean isNewNumber;
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String otpType) {
        this.otpType = otpType;
    }

    public boolean isNewNumber() {
        return isNewNumber;
    }

    public void setNewNumber(boolean newNumber) {
        isNewNumber = newNumber;
    }


    public String getAnswerJson() {
        return AnswerJson;
    }

    public void setAnswerJson(String answerJson) {
        AnswerJson = answerJson;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public SaveCorporateDetailsReqV1(String emailId, String Mobile,String OtpType,boolean isnenumber, String answerJson) {
        EmailId = emailId;
        mobile = Mobile;
        otpType = OtpType;
        isNewNumber = isnenumber;
        AnswerJson = answerJson;
    }

}
