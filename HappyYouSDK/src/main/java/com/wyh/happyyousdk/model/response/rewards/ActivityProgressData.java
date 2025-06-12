package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityProgressData {
    @SerializedName("progressPercentage")
    @Expose
    private Integer progressPercentage;
    @SerializedName("userSteps")
    @Expose
    private Integer userSteps;
    @SerializedName("totalSteps")
    @Expose
    private Integer totalSteps;
    @SerializedName("userCount")
    @Expose
    private Integer userCount;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public Integer getUserSteps() {
        return userSteps;
    }

    public void setUserSteps(Integer userSteps) {
        this.userSteps = userSteps;
    }

    public Integer getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Integer totalSteps) {
        this.totalSteps = totalSteps;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
