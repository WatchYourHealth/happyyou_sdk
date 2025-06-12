package com.wyh.happyyousdk.model.request.encrDecr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EncryptionRequest {
    @SerializedName("str")
    @Expose
    private String[] str;

    public EncryptionRequest(String[] str) {
        this.str = str;
    }

    public String[] getStr() {
        return str;
    }

    public void setStr(String[] str) {
        this.str = str;
    }
}
