package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EandBPendingActivity {
    @SerializedName("EventId")
    @Expose
    private Integer eventId;
    @SerializedName("EventName")
    @Expose
    private String eventName;
    @SerializedName("EventToken")
    @Expose
    private Integer eventToken;
    @SerializedName("EventType")
    @Expose
    private String eventType;
    @SerializedName("IsStarted")
    @Expose
    private Integer isStarted;
    @SerializedName("IsCompleted")
    @Expose
    private Integer isCompleted;
    @SerializedName("EventStartDate")
    @Expose
    private String eventStartDate;
    @SerializedName("EventEndDate")
    @Expose
    private String eventEndDate;
    @SerializedName("EventLogo")
    @Expose
    private String eventLogo;
    @SerializedName("EventDescription")
    @Expose
    private String eventDescription;
    @SerializedName("whatTo")
    @Expose
    private String whatTo;
    @SerializedName("howTo")
    @Expose
    private String howTo;
    @SerializedName("whyTo")
    @Expose
    private String whyTo;
    @SerializedName("ProgressPercentage")
    @Expose
    private int progressPercentage;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getEventToken() {
        return eventToken;
    }

    public void setEventToken(Integer eventToken) {
        this.eventToken = eventToken;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(Integer isStarted) {
        this.isStarted = isStarted;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public Integer getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Integer isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getEventLogo() {
        return eventLogo;
    }

    public void setEventLogo(String eventLogo) {
        this.eventLogo = eventLogo;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getWhatTo() {
        return whatTo;
    }

    public void setWhatTo(String whatTo) {
        this.whatTo = whatTo;
    }

    public String getHowTo() {
        return howTo;
    }

    public void setHowTo(String howTo) {
        this.howTo = howTo;
    }

    public String getWhyTo() {
        return whyTo;
    }

    public void setWhyTo(String whyTo) {
        this.whyTo = whyTo;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}
