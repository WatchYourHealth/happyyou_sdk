package com.wyh.happyyousdk.model;

import com.google.gson.annotations.SerializedName;

public class AuthDataResp {

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("mpin")
    private String mpin;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMpin() {
        return mpin;
    }

    public void setMpin(String mpin) {
        this.mpin = mpin;
    }

    public boolean isHasBiometric() {
        return hasBiometric;
    }

    public void setHasBiometric(boolean hasBiometric) {
        this.hasBiometric = hasBiometric;
    }

    @SerializedName("hasBiometric")
    private boolean hasBiometric;
}
