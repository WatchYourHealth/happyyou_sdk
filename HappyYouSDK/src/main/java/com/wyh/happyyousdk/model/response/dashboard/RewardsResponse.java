package com.wyh.happyyousdk.model.response.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardsResponse {

    @SerializedName("currentLevel")
    @Expose
    private Integer currentLevel;
    @SerializedName("totalActivities")
    @Expose
    private Integer totalActivities;
    @SerializedName("completedActivities")
    @Expose
    private Integer completedActivities;
    @SerializedName("totalPoints")
    @Expose
    private Integer totalPoints;
    @SerializedName("currentLevelPoints")
    @Expose
    private Integer currentLevelPoints;
    @SerializedName("equivalentAmount")
    @Expose
    private double equivalentAmount;

    public RewardsResponse() {
    }

    public RewardsResponse(Integer currentLevel, Integer totalActivities, Integer completedActivities, Integer totalPoints, Integer currentLevelPoints) {
        super();
        this.currentLevel = currentLevel;
        this.totalActivities = totalActivities;
        this.completedActivities = completedActivities;
        this.totalPoints = totalPoints;
        this.currentLevelPoints = currentLevelPoints;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Integer getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(Integer totalActivities) {
        this.totalActivities = totalActivities;
    }

    public Integer getCompletedActivities() {
        return completedActivities;
    }

    public void setCompletedActivities(Integer completedActivities) {
        this.completedActivities = completedActivities;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getCurrentLevelPoints() {
        return currentLevelPoints;
    }

    public void setCurrentLevelPoints(Integer currentLevelPoints) {
        this.currentLevelPoints = currentLevelPoints;
    }

    public double getEquivalentAmount() {
        return equivalentAmount;
    }

    public void setEquivalentAmount(double equivalentAmount) {
        this.equivalentAmount = equivalentAmount;
    }
}

