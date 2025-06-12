package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddOnActivities {

    @SerializedName("activityName")
    @Expose
    private String activityName;

    @SerializedName("activityDescription")
    @Expose
    private String activityDescription;

    @SerializedName("rewardPoints")
    @Expose
    private int rewardPoints;

    @SerializedName("imagePath")
    @Expose
    private String imagePath;

    @SerializedName("eventName")
    @Expose
    private String eventName;

    @SerializedName("whatToDoInActivity")
    @Expose
    private String whatToDoInActivity;

    @SerializedName("whytoDoTheActivity")
    @Expose
    private String whytoDoTheActivity;

    @SerializedName("howToDoTheActivity")
    @Expose
    private String howToDoTheActivity;

    @SerializedName("redirectTo")
    @Expose
    private String redirectTo;

    @SerializedName("positionColor")
    @Expose
    private Integer positionColor;

    @SerializedName("isStarted")
    @Expose
    private boolean isStarted;

    @SerializedName("isCompleted")
    @Expose
    private boolean isCompleted;

    @SerializedName("eventType")
    @Expose
    private String eventType;

    public void setPositionColor(Integer positionColor) {
        this.positionColor = positionColor;
    }

    public Integer getPositionColor() {

        return positionColor;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getEventName() {
        return eventName;
    }

    public String getWhatToDoInActivity() {
        return whatToDoInActivity;
    }

    public String getWhytoDoTheActivity() {
        return whytoDoTheActivity;
    }

    public String getHowToDoTheActivity() {
        return howToDoTheActivity;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getEventType() {
        return eventType;
    }

    public boolean isStarted() {
        return isStarted;
    }
}
