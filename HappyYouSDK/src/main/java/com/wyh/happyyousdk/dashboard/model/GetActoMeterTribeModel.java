package com.wyh.happyyousdk.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.RewardsModel;
import com.wyh.happyyousdk.model.response.dashboard.ActOMeterTribe;

public class GetActoMeterTribeModel {
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("data")
    @Expose
    private ActOMeterTribe data;

    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;

    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;

    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;


    public GetActoMeterTribeModel(String msg, boolean success, ActOMeterTribe data, Object freevoucher, EnGTokensModel enGTokens, RewardsModel rewards) {
        this.msg = msg;
        this.success = success;
        this.data = data;
        this.freevoucher = freevoucher;
        this.enGTokens = enGTokens;
        this.rewards = rewards;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ActOMeterTribe getData() {
        return data;
    }

    public void setData(ActOMeterTribe data) {
        this.data = data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }

    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }
}
