package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoucherIdRequest {
    @SerializedName("voucherId")
    @Expose
    private int voucherId;

    public VoucherIdRequest(int voucherId) {
        this.voucherId = voucherId;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }
}
