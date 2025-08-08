package com.wyh.happyyousdk.model.request.registration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationRequest {
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("DOB")
    @Expose
    private String dob;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("DeviceModel")
    @Expose
    private String deviceModel;
    @SerializedName("OSVersion")
    @Expose
    private String oSVersion;
    @SerializedName("AppVersion")
    @Expose
    private String appVersion;
    @SerializedName("referredby")
    @Expose
    private String referredby;

    @SerializedName("RegistrationSource")
    @Expose
    private String registrationSource;

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    @SerializedName("transactionid")
    @Expose
    private String transactionid;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("arnNumber")
    @Expose
    private String arnNumber;


    public RegistrationRequest(String mobile, String otp, String name, String dob,
                               String email, String deviceModel, String oSVersion, String appVersion,
                               String referredby,String transactionid, String source, String arnNumber) {
        this.mobile = mobile;
        this.otp = otp;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.deviceModel = deviceModel;
        this.oSVersion = oSVersion;
        this.appVersion = appVersion;
        this.referredby = referredby;
        this.transactionid = transactionid;
        this.source = source;
        this.arnNumber = arnNumber;
    }

    public RegistrationRequest(String mobile, String name, String dob, String email, String deviceModel, String oSVersion, String appVersion, String registrationSource) {
        this.mobile = mobile;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.deviceModel = deviceModel;
        this.oSVersion = oSVersion;
        this.appVersion = appVersion;
        this.registrationSource = registrationSource;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getoSVersion() {
        return oSVersion;
    }

    public void setoSVersion(String oSVersion) {
        this.oSVersion = oSVersion;
    }
}
