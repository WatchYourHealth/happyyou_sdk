package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EarnedRewardData implements Serializable {
    @SerializedName("activeList")
    @Expose
    private List<RewardItem> activeList;
    @SerializedName("completedList")
    @Expose
    private List<RewardItem> completedList;
    @SerializedName("expiredList")
    @Expose
    private List<RewardItem> expiredList;

    public List<RewardItem> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<RewardItem> activeList) {
        this.activeList = activeList;
    }

    public List<RewardItem> getCompletedList() {
        return completedList;
    }

    public void setCompletedList(List<RewardItem> completedList) {
        this.completedList = completedList;
    }

    public List<RewardItem> getExpiredList() {
        return expiredList;
    }

    public void setExpiredList(List<RewardItem> expiredList) {
        this.expiredList = expiredList;
    }
}
