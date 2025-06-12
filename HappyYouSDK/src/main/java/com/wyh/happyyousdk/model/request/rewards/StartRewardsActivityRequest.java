package com.wyh.happyyousdk.model.request.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartRewardsActivityRequest {
    @SerializedName("eventId")
    @Expose
    private int eventId;
    @SerializedName("eventCategory")
    @Expose
    private String eventCategory;

    public StartRewardsActivityRequest(int eventId, String eventCategory) {
        this.eventId = eventId;
        this.eventCategory = eventCategory;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }
}
