package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Quadrant implements Serializable {
    @SerializedName("quadrantPosition")
    @Expose
    private Integer quadrantPosition;
    @SerializedName("rewardIcon")
    @Expose
    private String rewardIcon;
    @SerializedName("rewardName")
    @Expose
    private String rewardName;
    @SerializedName("quadrantColor")
    @Expose
    private String quadrantColor;
    @SerializedName("rewardType")
    @Expose
    private String rewardType;

    public Integer getQuadrantPosition() {
        return quadrantPosition;
    }

    public void setQuadrantPosition(Integer quadrantPosition) {
        this.quadrantPosition = quadrantPosition;
    }

    public String getRewardIcon() {
        return rewardIcon;
    }

    public void setRewardIcon(String rewardIcon) {
        this.rewardIcon = rewardIcon;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getQuadrantColor() {
        return quadrantColor;
    }

    public void setQuadrantColor(String quadrantColor) {
        this.quadrantColor = quadrantColor;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }
}
