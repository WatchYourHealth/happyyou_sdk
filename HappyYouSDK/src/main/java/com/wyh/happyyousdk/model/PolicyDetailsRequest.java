package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PolicyDetailsRequest {

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("policyNumber")
    @Expose
    private String policyNumber;

    @SerializedName("DOB")
    @Expose
    private String DOB;

    public PolicyDetailsRequest(String mobileNumber, String policyNumber, String DOB) {
        this.mobileNumber = mobileNumber;
        this.policyNumber = policyNumber;
        this.DOB = DOB;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

}
