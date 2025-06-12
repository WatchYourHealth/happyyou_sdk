package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedeemableAmountResponse {
    @SerializedName("redeemableAmount")
    @Expose
    private Integer redeemableAmount;

    public Integer getRedeemableAmount() {
        return redeemableAmount;
    }

    public void setRedeemableAmount(Integer redeemableAmount) {
        this.redeemableAmount = redeemableAmount;
    }
}
