package com.wyh.happyyousdk.model.request.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityProgressRequest {
    @SerializedName("eventId")
    @Expose
    private Integer eventId;
    @SerializedName("eventType")
    @Expose
    private String eventType;


    public ActivityProgressRequest(Integer eventId, String eventType) {
        this.eventId = eventId;
        this.eventType = eventType;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
