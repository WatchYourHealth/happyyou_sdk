package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllCollectiblesData {
    @SerializedName("activityCollectible")
    @Expose
    private List<RewardsCollectible> activityCollectible;
    @SerializedName("enGCollectible")
    @Expose
    private List<RewardsCollectible> enGCollectible;

    public List<RewardsCollectible> getActivityCollectible() {
        return activityCollectible;
    }

    public void setActivityCollectible(List<RewardsCollectible> activityCollectible) {
        this.activityCollectible = activityCollectible;
    }

    public List<RewardsCollectible> getEnGCollectible() {
        return enGCollectible;
    }

    public void setEnGCollectible(List<RewardsCollectible> enGCollectible) {
        this.enGCollectible = enGCollectible;
    }
}
