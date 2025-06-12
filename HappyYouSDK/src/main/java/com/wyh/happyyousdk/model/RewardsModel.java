package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RewardsModel implements Serializable {
    @SerializedName("reward")
    @Expose
    private String reward;
    @SerializedName("bonusRewards")
    @Expose
    private String bonusRewards;

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getBonusRewards() {
        return bonusRewards;
    }

    public void setBonusRewards(String bonusRewards) {
        this.bonusRewards = bonusRewards;
    }
}
