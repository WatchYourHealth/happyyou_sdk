package com.wyh.happyyousdk.model.response.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpData {

    @SerializedName("isRegistered")
    @Expose
    private Boolean isRegistered;
    @SerializedName("authToken")
    @Expose
    private Object authToken;
    @SerializedName("clientDetails")
    @Expose
    private ClientDetails clientDetails;
    @SerializedName("crn")
    @Expose
    private Object crn;
    @SerializedName("policyDetails")
    @Expose
    private VerifyOtpResponse.Data.PolicyDetails policyDetails;

    public Boolean getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(Boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public Object getAuthToken() {
        return authToken;
    }

    public void setAuthToken(Object authToken) {
        this.authToken = authToken;
    }

    public ClientDetails getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(ClientDetails clientDetails) {
        this.clientDetails = clientDetails;
    }

    public Object getCrn() {
        return crn;
    }

    public void setCrn(Object crn) {
        this.crn = crn;
    }

    public VerifyOtpResponse.Data.PolicyDetails getPolicyDetails() {
        return policyDetails;
    }

    public void setPolicyDetails(VerifyOtpResponse.Data.PolicyDetails policyDetails) {
        this.policyDetails = policyDetails;
    }

}

