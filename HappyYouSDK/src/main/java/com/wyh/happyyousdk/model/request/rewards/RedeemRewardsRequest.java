package com.wyh.happyyousdk.model.request.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedeemRewardsRequest {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("transDate")
    @Expose
    private String transDate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("redeemValue")
    @Expose
    private int redeemValue;

    public RedeemRewardsRequest(String userId, String transDate, int redeemValue, String description) {
        this.userId = userId;
        this.transDate = transDate;
        this.redeemValue = redeemValue;
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public int getRedeemValue() {
        return redeemValue;
    }

    public void setRedeemValue(int redeemValue) {
        this.redeemValue = redeemValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
