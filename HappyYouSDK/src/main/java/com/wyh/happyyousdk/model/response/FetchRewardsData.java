package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchRewardsData {
    @SerializedName("activityId")
    @Expose
    private Integer activityId;
    @SerializedName("popupMessage")
    @Expose
    private String popupMessage;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getPopupMessage() {
        return popupMessage;
    }

    public void setPopupMessage(String popupMessage) {
        this.popupMessage = popupMessage;
    }
}
