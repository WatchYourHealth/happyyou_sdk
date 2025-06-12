package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RewardsHistoryData {
    @SerializedName("activities")
    @Expose
    private List<RewardsHistoryActivityData> activities;
    @SerializedName("enG")
    @Expose
    private List<RewardsHistoryEnG> enG;
    @SerializedName("activityRewards")
    @Expose
    private List<RewardsHistoryActivityRewardsData> activityRewards;

    public List<RewardsHistoryActivityData> getActivities() {
        return activities;
    }

    public void setActivities(List<RewardsHistoryActivityData> activities) {
        this.activities = activities;
    }

    public List<RewardsHistoryEnG> getEnG() {
        return enG;
    }

    public void setEnG(List<RewardsHistoryEnG> enG) {
        this.enG = enG;
    }

    public List<RewardsHistoryActivityRewardsData> getActivityRewards() {
        return activityRewards;
    }

    public void setActivityRewards(List<RewardsHistoryActivityRewardsData> activityRewards) {
        this.activityRewards = activityRewards;
    }
}
