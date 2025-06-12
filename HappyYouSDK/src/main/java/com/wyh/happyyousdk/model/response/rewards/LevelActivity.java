package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LevelActivity {
    @SerializedName("activityID")
    @Expose
    private int activityID;
    @SerializedName("activityName")
    @Expose
    private String activityName;
    @SerializedName("activityTag")
    @Expose
    private String activityTag;
    @SerializedName("activityDesc")
    @Expose
    private String activityDesc;
    @SerializedName("whatTo")
    @Expose
    private String whatTo;
    @SerializedName("howTo")
    @Expose
    private String howTo;
    @SerializedName("whyTo")
    @Expose
    private String whyTo;
    @SerializedName("activityImagePath")
    @Expose
    private String activityImagePath;
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @SerializedName("redirectTo")
    @Expose
    private String redirectTo;
    @SerializedName("isCompleted")
    @Expose
    private boolean isCompleted;
    @SerializedName("isFromPreviousLevel")
    @Expose
    private boolean isFromPreviousLevel;
    @SerializedName("isStarted")
    @Expose
    private boolean isStarted;
    @SerializedName("point")
    @Expose
    private int point;
    @SerializedName("progressPercentage")
    @Expose
    private int progressPercentage;
    @SerializedName("positionColor")
    @Expose
    private int positionColor;


    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getActivityImagePath() {
        return activityImagePath;
    }

    public void setActivityImagePath(String activityImagePath) {
        this.activityImagePath = activityImagePath;
    }

    public boolean isIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public String getActivityTag() {
        return activityTag;
    }

    public void setActivityTag(String activityTag) {
        this.activityTag = activityTag;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
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

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    public boolean isFromPreviousLevel() {
        return isFromPreviousLevel;
    }

    public void setFromPreviousLevel(boolean fromPreviousLevel) {
        isFromPreviousLevel = fromPreviousLevel;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public int getPositionColor() {
        return positionColor;
    }

    public void setPositionColor(int position) {
        this.positionColor = position;
    }
}
