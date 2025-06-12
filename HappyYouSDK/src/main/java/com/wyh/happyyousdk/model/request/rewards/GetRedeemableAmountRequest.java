package com.wyh.happyyousdk.model.request.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetRedeemableAmountRequest {
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("amount")
    @Expose
    private Integer amount;

    public GetRedeemableAmountRequest(String userid, Integer amount) {
        this.userid = userid;
        this.amount = amount;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
