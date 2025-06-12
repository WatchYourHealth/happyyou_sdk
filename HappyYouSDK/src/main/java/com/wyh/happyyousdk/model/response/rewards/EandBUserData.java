package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EandBUserData {
    @SerializedName("UserTokens")
    @Expose
    private int userTokens;
    @SerializedName("CurrentMileStone")
    @Expose
    private int currentMileStone;
    @SerializedName("CurrentMileStoneTotalEvents")
    @Expose
    private int currentMileStoneTotalEvents;
    @SerializedName("CurrentMileStoneEventsCompleted")
    @Expose
    private int currentMileStoneEventsCompleted;

    public int getUserTokens() {
        return userTokens;
    }

    public void setUserTokens(int userTokens) {
        this.userTokens = userTokens;
    }

    public int getCurrentMileStone() {
        return currentMileStone;
    }

    public void setCurrentMileStone(int currentMileStone) {
        this.currentMileStone = currentMileStone;
    }

    public int getCurrentMileStoneTotalEvents() {
        return currentMileStoneTotalEvents;
    }

    public void setCurrentMileStoneTotalEvents(int currentMileStoneTotalEvents) {
        this.currentMileStoneTotalEvents = currentMileStoneTotalEvents;
    }

    public int getCurrentMileStoneEventsCompleted() {
        return currentMileStoneEventsCompleted;
    }

    public void setCurrentMileStoneEventsCompleted(int currentMileStoneEventsCompleted) {
        this.currentMileStoneEventsCompleted = currentMileStoneEventsCompleted;
    }
}
