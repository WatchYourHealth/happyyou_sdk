package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FreebieVoucher {
    @SerializedName("freebieID")
    @Expose
    private int freebieID;
    @SerializedName("freebieName")
    @Expose
    private String freebieName;

    public int getFreebieID() {
        return freebieID;
    }

    public void setFreebieID(int freebieID) {
        this.freebieID = freebieID;
    }

    public String getFreebieName() {
        return freebieName;
    }

    public void setFreebieName(String freebieName) {
        this.freebieName = freebieName;
    }
}
