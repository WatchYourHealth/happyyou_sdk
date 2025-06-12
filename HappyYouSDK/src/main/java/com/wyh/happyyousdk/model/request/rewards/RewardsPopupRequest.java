package com.wyh.happyyousdk.model.request.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardsPopupRequest {
    @SerializedName("activityId")
    @Expose
    private int activityId;

    public RewardsPopupRequest(int activityId) {
        this.activityId = activityId;
    }

}
