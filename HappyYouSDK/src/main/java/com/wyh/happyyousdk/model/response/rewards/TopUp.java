package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopUp {
    @SerializedName("topUpID")
    @Expose
    private int topUpID;
    @SerializedName("topUpTag")
    @Expose
    private String topUpTag;
    @SerializedName("topUpName")
    @Expose
    private String topUpName;
    @SerializedName("topUpDesc")
    @Expose
    private String topUpDesc;
    @SerializedName("isStarted")
    @Expose
    private boolean isStarted;
    @SerializedName("isCompleted")
    @Expose
    private boolean isCompleted;
    @SerializedName("point")
    @Expose
    private int point;
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @SerializedName("redirectTo")
    @Expose
    private String redirectTo;
    @SerializedName("whatTo")
    @Expose
    private String whatTo;
    @SerializedName("howTo")
    @Expose
    private String howTo;
    @SerializedName("whyTo")
    @Expose
    private String whyTo;
    @SerializedName("topupIcon")
    @Expose
    private String topupIcon;
    @SerializedName("activityImagePath")
    @Expose
    private String activityImagePath;
    @SerializedName("recurrenceDays")
    @Expose
    private Integer recurrenceDays;
    @SerializedName("progressPercentage")
    @Expose
    private Integer progressPercentage;
    @SerializedName("positionColor")
    @Expose
    private Integer positionColor;


    public TopUp() {
    }

    public TopUp(String topUpTag, String topUpName, String topUpDesc, int point, String redirectTo, String whatTo, String howTo, String whyTo, String activityImagePath, Integer progressPercentage, String eventType, boolean isStarted, boolean isCompleted,String topupIcon) {
        this.topUpTag = topUpTag;
        this.topUpName = topUpName;
        this.topUpDesc = topUpDesc;
        this.point = point;
        this.redirectTo = redirectTo;
        this.whatTo = whatTo;
        this.howTo = howTo;
        this.whyTo = whyTo;
        this.activityImagePath = activityImagePath;
        this.progressPercentage = progressPercentage;
        this.eventType = eventType;
        this.isStarted = isStarted;
        this.isCompleted = isCompleted;
        this.topupIcon = topupIcon;
    }

    public int getTopUpID() {
        return topUpID;
    }

    public void setTopUpID(int topUpID) {
        this.topUpID = topUpID;
    }

    public String getTopUpTag() {
        return topUpTag;
    }

    public void setTopUpTag(String topUpTag) {
        this.topUpTag = topUpTag;
    }

    public String getTopUpName() {
        return topUpName;
    }

    public void setTopUpName(String topUpName) {
        this.topUpName = topUpName;
    }

    public String getTopUpDesc() {
        return topUpDesc;
    }

    public void setTopUpDesc(String topUpDesc) {
        this.topUpDesc = topUpDesc;
    }


    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    public boolean isIsStarted() {
        return isStarted;
    }

    public void setIsStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public boolean isIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
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

    public String getTopupIcon() {
        return topupIcon;
    }

    public void setTopupIcon(String topupIcon) {
        this.topupIcon = topupIcon;
    }

    public String getActivityImagePath() {
        return activityImagePath;
    }

    public void setActivityImagePath(String activityImagePath) {
        this.activityImagePath = activityImagePath;
    }

    public Integer getRecurrenceDays() {
        return recurrenceDays;
    }

    public void setRecurrenceDays(Integer recurrenceDays) {
        this.recurrenceDays = recurrenceDays;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public Integer getPositionColor() {
        return positionColor;
    }

    public void setPositionColor(Integer positionColor) {
        this.positionColor = positionColor;
    }
}
