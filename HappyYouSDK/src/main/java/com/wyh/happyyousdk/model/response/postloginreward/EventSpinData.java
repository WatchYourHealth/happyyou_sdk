package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventSpinData implements Serializable {

    @SerializedName("lastEventSpinOn")
    @Expose
    private String lastEventSpinOn;
    @SerializedName("hasEventSpin")
    @Expose
    private Boolean hasEventSpin;
    @SerializedName("remainingEventSpins")
    @Expose
    private Integer remainingEventSpins;
    @SerializedName("eventExpireOn")
    @Expose
    private String eventExpireOn;
    @SerializedName("eventTitle")
    @Expose
    private String eventTitle;
    @SerializedName("eventId")
    @Expose
    private String eventId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getLastEventSpinOn() {
        return lastEventSpinOn;
    }

    public void setLastEventSpinOn(String lastEventSpinOn) {
        this.lastEventSpinOn = lastEventSpinOn;
    }

    public Boolean getHasEventSpin() {
        return hasEventSpin;
    }

    public void setHasEventSpin(Boolean hasEventSpin) {
        this.hasEventSpin = hasEventSpin;
    }

    public Integer getRemainingEventSpins() {
        return remainingEventSpins;
    }

    public void setRemainingEventSpins(Integer remainingEventSpins) {
        this.remainingEventSpins = remainingEventSpins;
    }

    public String getEventExpireOn() {
        return eventExpireOn;
    }

    public void setEventExpireOn(String eventExpireOn) {
        this.eventExpireOn = eventExpireOn;
    }
}
