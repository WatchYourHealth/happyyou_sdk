package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopRewardModel {

    @SerializedName("activityId")
    @Expose
    private Integer activityId;
    @SerializedName("popupMessage")
    @Expose
    private String popupMessage;

    public PopRewardModel(Integer activityId, String popupMessage) {
        this.activityId = activityId;
        this.popupMessage = popupMessage;
    }

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
