package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbhaVerifyOtpRequest {

    @SerializedName("accesstoken")
    @Expose
    private String accesstoken;

    @SerializedName("authData")
    @Expose
    private AuthData authData;
    @SerializedName("mode")
    @Expose
    private String  mode;

    public AbhaVerifyOtpRequest(String accesstoken, AuthData authData,String mode) {
        this.accesstoken = accesstoken;
        this.authData = authData;
        this.mode = mode;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public static class AuthData {
        @SerializedName("otp")
        @Expose
        private OTP otp;


        public OTP getOtp() {
            return otp;
        }

        public AuthData(OTP otp) {
            this.otp = otp;
        }
    }

    public static class OTP {

        @SerializedName("txnId")
        @Expose
        private String txnId;

        @SerializedName("otpValue")
        @Expose
        private String otpValue;

        public OTP(String txnId, String otpValue) {
            this.txnId = txnId;
            this.otpValue = otpValue;
        }

        public String getTxnId() {
            return txnId;
        }

        public String getOtpValue() {
            return otpValue;
        }
    }

}
