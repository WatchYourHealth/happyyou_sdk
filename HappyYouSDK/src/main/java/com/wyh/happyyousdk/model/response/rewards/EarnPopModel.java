package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EarnPopModel {

    @SerializedName("ActivityId")
    @Expose
    private Integer activityId;
    @SerializedName("ActivityName")
    @Expose
    private String activityName;
    @SerializedName("ActivityToken")
    @Expose
    private String activityToken;


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

    public String getActivityToken() {
        return activityToken;
    }

    public void setActivityToken(String activityToken) {
        this.activityToken = activityToken;
    }
}
