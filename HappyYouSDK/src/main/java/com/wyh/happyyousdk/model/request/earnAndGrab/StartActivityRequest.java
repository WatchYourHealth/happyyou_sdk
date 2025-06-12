package com.wyh.happyyousdk.model.request.earnAndGrab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartActivityRequest {
    @SerializedName("ActivityId")
    @Expose
    private Integer activityId;

    public StartActivityRequest(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
}
