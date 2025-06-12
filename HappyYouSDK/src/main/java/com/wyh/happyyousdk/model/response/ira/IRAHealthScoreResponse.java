package com.wyh.happyyousdk.model.response.ira;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.RewardsModel;

import java.io.Serializable;

public class IRAHealthScoreResponse implements Serializable {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private IRAHealthScoreData iraHealthScoreData;

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

    public IRAHealthScoreData getIraHealthScoreData() {
        return iraHealthScoreData;
    }

    public void setIraHealthScoreData(IRAHealthScoreData iraHealthScoreData) {
        this.iraHealthScoreData = iraHealthScoreData;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }
}
