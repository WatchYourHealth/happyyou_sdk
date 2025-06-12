package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnscratchedTokensData {

    @SerializedName("ActivityId")
    @Expose
    private Integer activityId;
    @SerializedName("ActivityName")
    @Expose
    private String activityName;
    @SerializedName("ActivityToken")
    @Expose
    private Integer activityToken;

    public UnscratchedTokensData(Integer activityId, String activityName, Integer activityToken) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityToken = activityToken;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getActivityToken() {
        return activityToken;
    }

    public void setActivityToken(Integer activityToken) {
        this.activityToken = activityToken;
    }
}
