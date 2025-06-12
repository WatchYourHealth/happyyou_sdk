package com.wyh.happyyousdk.model.request.earnAndGrab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BurnTokenRequest {
    @SerializedName("TokenValue")
    @Expose
    private Integer tokenValue;
    @SerializedName("VoucherId")
    @Expose
    private Integer voucherId;

    public BurnTokenRequest(Integer tokenValue, Integer voucherId) {
        this.tokenValue = tokenValue;
        this.voucherId = voucherId;
    }

    public Integer getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(Integer tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }
}
