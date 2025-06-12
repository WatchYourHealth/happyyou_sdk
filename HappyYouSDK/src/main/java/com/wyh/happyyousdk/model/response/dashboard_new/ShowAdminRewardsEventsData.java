package com.wyh.happyyousdk.model.response.dashboard_new;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowAdminRewardsEventsData {
    @SerializedName("EventId")
    @Expose
    private String eventId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("RewardPoints")
    @Expose
    private Integer rewardPoints;
    @SerializedName("RewardPointsINR")
    @Expose
    private Integer rewardPointsINR;
    @SerializedName("Recurrence")
    @Expose
    private Integer recurrence;
    @SerializedName("CompletionDays")
    @Expose
    private Integer completionDays;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("ImagePath")
    @Expose
    private String imagePath;
    @SerializedName("EventType")
    @Expose
    private String eventType;
    @SerializedName("EventName")
    @Expose
    private String eventName;
    @SerializedName("WhatToDoInActivity")
    @Expose
    private String whatToDoInActivity;
    @SerializedName("HowToDoTheActivity")
    @Expose
    private String howToDoTheActivity;
    @SerializedName("WhyToDoTheActivity")
    @Expose
    private String whyToDoTheActivity;
    @SerializedName("RedirectTo")
    @Expose
    private String redirectTo;
    @SerializedName("IsStarted")
    @Expose
    private Boolean isStarted;
    @SerializedName("IsCompleted")
    @Expose
    private Boolean isCompleted;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(Integer rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public Integer getRewardPointsINR() {
        return rewardPointsINR;
    }

    public void setRewardPointsINR(Integer rewardPointsINR) {
        this.rewardPointsINR = rewardPointsINR;
    }

    public Integer getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Integer recurrence) {
        this.recurrence = recurrence;
    }

    public Integer getCompletionDays() {
        return completionDays;
    }

    public void setCompletionDays(Integer completionDays) {
        this.completionDays = completionDays;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getWhatToDoInActivity() {
        return whatToDoInActivity;
    }

    public void setWhatToDoInActivity(String whatToDoInActivity) {
        this.whatToDoInActivity = whatToDoInActivity;
    }

    public String getHowToDoTheActivity() {
        return howToDoTheActivity;
    }

    public void setHowToDoTheActivity(String howToDoTheActivity) {
        this.howToDoTheActivity = howToDoTheActivity;
    }

    public String getWhyToDoTheActivity() {
        return whyToDoTheActivity;
    }

    public void setWhyToDoTheActivity(String whyToDoTheActivity) {
        this.whyToDoTheActivity = whyToDoTheActivity;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    public Boolean getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(Boolean isStarted) {
        this.isStarted = isStarted;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
