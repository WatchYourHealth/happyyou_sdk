package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.RewardsModel;

public class ScratchAndWinResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;
    @SerializedName("data")
    @Expose
    private Integer data;
    @SerializedName("freevoucher")
    @Expose
    private FreeVoucher freeVoucher;
    @SerializedName("success")
    @Expose
    private boolean success;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public FreeVoucher getFreeVoucher() {
        return freeVoucher;
    }

    public void setFreeVoucher(FreeVoucher freeVoucher) {
        this.freeVoucher = freeVoucher;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }
}
