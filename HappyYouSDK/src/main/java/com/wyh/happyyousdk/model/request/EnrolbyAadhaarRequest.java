package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public  class EnrolbyAadhaarRequest {
    public  EnrolbyAadhaarRequest(String accesstoken, AuthData authData,Consent consent) {
        this.accesstoken = accesstoken;
        this.authData = authData;
        this.consent = consent;
        this.otp = otp;
    }

    public String accesstoken;
    public AuthData authData;
    public Consent consent;
    public Otp  otp;
    public static class AuthData{
        public AuthData(ArrayList<String> authMethods, Otp otp) {
            this.authMethods = authMethods;
            this.otp = otp;
        }

        public ArrayList<String> authMethods;
        public Otp otp;
    }

    public static class Consent{
        public String code;
        public String version;

        public Consent(String code, String version) {
            this.code = code;
            this.version = version;
        }
    }

    public static class Otp{
        public Otp(String timeStamp, String txnId, String otpValue, String mobile) {
            this.timeStamp = timeStamp;
            this.txnId = txnId;
            this.otpValue = otpValue;
            this.mobile = mobile;
        }

        public String timeStamp;
        public String txnId;
        public String otpValue;
        public String mobile;
    }



}
