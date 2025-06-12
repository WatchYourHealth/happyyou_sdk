package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AbhaVerifyOtpResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    private OtpData data;

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public OtpData getData() {
        return data;
    }

    public  class OtpData {

        @SerializedName("txnId")
        @Expose
        private String txnId;

        @SerializedName("authResult")
        @Expose
        private String authResult;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("token")
        @Expose
        private String token;

        @SerializedName("tokens")
        @Expose
        private Tokens tokens;

        @SerializedName("accounts")
        @Expose
        private ArrayList<Accounts> accounts;


        public String getTxnId() {
            return txnId;
        }

        public String getAuthResult() {
            return authResult;
        }

        public String getMessage() {
            return message;
        }

        public String getToken() {
            return token;
        }

        public ArrayList<Accounts> getAccounts() {
            return accounts;
        }
        public Error getError() {
            return error;
        }

        public Error error;

        public void setTxnId(String txnId) {
            this.txnId = txnId;
        }

        public void setAuthResult(String authResult) {
            this.authResult = authResult;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Tokens getTokens() {
            return tokens;
        }

        public void setTokens(Tokens tokens) {
            this.tokens = tokens;
        }

        public void setAccounts(ArrayList<Accounts> accounts) {
            this.accounts = accounts;
        }

        public void setError(Error error) {
            this.error = error;
        }
    }


    public class Error{
        public String getMessage() {
            return message;
        }

        public String message;
    }
    public class Tokens{
        public String getToken() {
            return token;
        }

        public String token;
    }
    public  class Accounts {

        @SerializedName("abhaNumber")
        @Expose
        private String abhaNumber;

        @SerializedName("preferredAbhaAddress")
        @Expose
        private String preferredAbhaAddress;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("profilePhoto")
        @Expose
        private String profilePhoto;


        public String getAbhaNumber() {
            return abhaNumber;
        }

        public String getPreferredAbhaAddress() {
            return preferredAbhaAddress;
        }

        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }
    }

}
