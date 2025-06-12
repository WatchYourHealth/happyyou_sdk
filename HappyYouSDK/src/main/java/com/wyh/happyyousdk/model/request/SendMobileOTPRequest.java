package com.wyh.happyyousdk.model.request;

import java.util.ArrayList;

public class SendMobileOTPRequest {

    public String accesstoken;
    public String txnId;

    public SendMobileOTPRequest(String accesstoken, String txnId, ArrayList<String> scope, String loginHint, String loginId, String otpSystem) {
        this.accesstoken = accesstoken;
        this.txnId = txnId;
        this.scope = scope;
        this.loginHint = loginHint;
        this.loginId = loginId;
        this.otpSystem = otpSystem;
    }

    public ArrayList<String> scope;
    public String loginHint;
    public String loginId;
    public String otpSystem;
}
