package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HACustomerRegistrationRequest {
    @SerializedName("walletAmount")
    @Expose
    private Integer walletAmount;

    public HACustomerRegistrationRequest(Integer walletAmount) {
        this.walletAmount = walletAmount;
    }

    public Integer getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Integer walletAmount) {
        this.walletAmount = walletAmount;
    }
}
