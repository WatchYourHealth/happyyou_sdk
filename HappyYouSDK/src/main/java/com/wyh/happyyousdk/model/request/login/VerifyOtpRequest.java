package com.wyh.happyyousdk.model.request.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpRequest {
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("DeviceModel")
    @Expose
    private String deviceModel;
    @SerializedName("OSVersion")
    @Expose
    private String oSVersion;
    @SerializedName("AppVersion")
    @Expose
    private String appVersion;

    @SerializedName("userkey")
    @Expose
    private String userkey;



    public VerifyOtpRequest(String mobile, String otp, String deviceModel, String oSVersion, String appVersion, String userkey) {
        this.mobile = mobile;
        this.otp = otp;
        this.deviceModel = deviceModel;
        this.oSVersion = oSVersion;
        this.appVersion = appVersion;
        this.userkey = userkey;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getOSVersion() {
        return oSVersion;
    }

    public void setOSVersion(String oSVersion) {
        this.oSVersion = oSVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }
}
