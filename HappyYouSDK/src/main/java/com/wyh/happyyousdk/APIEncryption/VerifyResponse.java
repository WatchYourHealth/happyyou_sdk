package com.wyh.happyyousdk.APIEncryption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.kgi_policy.GetPolicyDetailsResponseData;

public class VerifyResponse {


    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private Object enGTokens;
    @SerializedName("rewards")
    @Expose
    private Object rewards;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public Object getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(Object enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Object getRewards() {
        return rewards;
    }

    public void setRewards(Object rewards) {
        this.rewards = rewards;
    }

    public Object getUserkey() {
        return userkey;
    }

    public void setUserkey(Object userkey) {
        this.userkey = userkey;
    }

    @SerializedName("userkey")
    @Expose
    private Object userkey;


    public class Data{
        @SerializedName("authToken")
        @Expose
        private String authToken;

        @SerializedName("clientDetails")
        @Expose
        private Object clientDetails;

        @SerializedName("crn")
        @Expose
        private String crn;

        @SerializedName("isRegistered")
        @Expose
        private Boolean isRegistered;

        @SerializedName("policyDetails")
        @Expose
        private Object policyDetails;

        @SerializedName("userkey")
        @Expose
        private String userkey;

        @SerializedName("authTokenIssuedOn")
        @Expose
        private String authTokenIssuedOn;

        @SerializedName("authTokenExpiresOn")
        @Expose
        private String authTokenExpiresOn;

        @SerializedName("kgiPolicyDetails")
        @Expose
        private GetPolicyDetailsResponseData kgiPolicyDetails;

        public GetPolicyDetailsResponseData getKgiPolicyDetails() {
            return kgiPolicyDetails;
        }

        public void setKgiPolicyDetails(GetPolicyDetailsResponseData kgiPolicyDetails) {
            this.kgiPolicyDetails = kgiPolicyDetails;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public Object getClientDetails() {
            return clientDetails;
        }

        public void setClientDetails(Object clientDetails) {
            this.clientDetails = clientDetails;
        }

        public String getCrn() {
            return crn;
        }

        public void setCrn(String crn) {
            this.crn = crn;
        }

        public Boolean getRegistered() {
            return isRegistered;
        }

        public void setRegistered(Boolean registered) {
            isRegistered = registered;
        }

        public Object getPolicyDetails() {
            return policyDetails;
        }

        public void setPolicyDetails(Object policyDetails) {
            this.policyDetails = policyDetails;
        }

        public String getUserkey() {
            return userkey;
        }

        public void setUserkey(String userkey) {
            this.userkey = userkey;
        }

        public String getAuthTokenIssuedOn() {
            return authTokenIssuedOn;
        }

        public void setAuthTokenIssuedOn(String authTokenIssuedOn) {
            this.authTokenIssuedOn = authTokenIssuedOn;
        }

        public String getAuthTokenExpiresOn() {
            return authTokenExpiresOn;
        }

        public void setAuthTokenExpiresOn(String authTokenExpiresOn) {
            this.authTokenExpiresOn = authTokenExpiresOn;
        }
    }

}
