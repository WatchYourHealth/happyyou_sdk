package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UuidRequest implements Serializable {

    @SerializedName("UUID")
    @Expose
    private String uUID;

    public UuidRequest(String uUID) {
        this.uUID = uUID;
    }

    public String getUUID() {
        return uUID;
    }

    public void setUUID(String uUID) {
        this.uUID = uUID;
    }

}
