package com.wyh.happyyousdk.model.request.policy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttachUserPolicyRequest {


    @SerializedName("policyNumber")
    @Expose
    private String policyNumber;
    @SerializedName("dob")
    @Expose
    private String dob;


    public AttachUserPolicyRequest(String policyNumber, String dob) {
        this.policyNumber = policyNumber;
        this.dob = dob;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
