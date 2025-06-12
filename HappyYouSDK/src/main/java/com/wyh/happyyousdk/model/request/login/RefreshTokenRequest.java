package com.wyh.happyyousdk.model.request.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefreshTokenRequest {
    @SerializedName("DeviceModel")
    @Expose
    private String deviceModel;
    @SerializedName("OSVersion")
    @Expose
    private String oSVersion;
    @SerializedName("AppVersion")
    @Expose
    private String appVersion;

    public RefreshTokenRequest(String deviceModel, String oSVersion, String appVersion) {
        this.deviceModel = deviceModel;
        this.oSVersion = oSVersion;
        this.appVersion = appVersion;
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
}
