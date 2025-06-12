package com.wyh.happyyousdk.model.request;

import java.util.ArrayList;

public class AuthByAadharRequest {

    public String accesstoken;

    public  AuthByAadharRequest(String accesstoken, ArrayList<String> scope, AuthData authData) {
        this.accesstoken = accesstoken;
        this.scope = scope;
        this.authData = authData;
    }

    public ArrayList<String> scope;
    public AuthData authData;
    public static class AuthData{
        public AuthData(ArrayList<String> authMethods, Otp otp) {
            this.authMethods = authMethods;
            this.otp = otp;
        }

        public ArrayList<String> authMethods;
        public Otp otp;
    }

    public static  class Otp{
        public Otp(String timeStamp, String txnId, String otpValue) {
            this.timeStamp = timeStamp;
            this.txnId = txnId;
            this.otpValue = otpValue;
        }

        public String timeStamp;
        public String txnId;
        public String otpValue;
    }
}
