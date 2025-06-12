package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RewardsLevel {
    @SerializedName("levelID")
    @Expose
    private int levelID;
    @SerializedName("levelName")
    @Expose
    private String levelName;
    @SerializedName("activities")
    @Expose
    private List<LevelActivity> activities;
    @SerializedName("currentPoint")
    @Expose
    private int currentPoint;
    @SerializedName("completedActivities")
    @Expose
    private int completedActivities;
    @SerializedName("minimumPoint")
    @Expose
    private int minimumPoint;
    @SerializedName("maximumPoint")
    @Expose
    private int maximumPoint;

    public int getLevelID() {
        return levelID;
    }

    public void setLevelID(int levelID) {
        this.levelID = levelID;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public List<LevelActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<LevelActivity> activities) {
        this.activities = activities;
    }

    public int getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(int currentPoint) {
        this.currentPoint = currentPoint;
    }

    public int getMinimumPoint() {
        return minimumPoint;
    }

    public void setMinimumPoint(int minimumPoint) {
        this.minimumPoint = minimumPoint;
    }

    public int getMaximumPoint() {
        return maximumPoint;
    }

    public void setMaximumPoint(int maximumPoint) {
        this.maximumPoint = maximumPoint;
    }

    public int getCompletedActivities() {
        return completedActivities;
    }

    public void setCompletedActivities(int completedActivities) {
        this.completedActivities = completedActivities;
    }
}
