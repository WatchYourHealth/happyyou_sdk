package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardsHistoryActivityRewardsData {
    @SerializedName("activityName")
    @Expose
    private String activityName;
    @SerializedName("rewardType")
    @Expose
    private String rewardType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("transactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("activityStartDate")
    @Expose
    private String activityStartDate;
    @SerializedName("activityEndDate")
    @Expose
    private String activityEndDate;
    @SerializedName("activityHeader")
    @Expose
    private String activityHeader;
    @SerializedName("activityCampaignId")
    @Expose
    private Integer activityCampaignId;
    @SerializedName("selfStatus")
    @Expose
    private String selfStatus;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getActivityStartDate() {
        return activityStartDate;
    }

    public void setActivityStartDate(String activityStartDate) {
        this.activityStartDate = activityStartDate;
    }

    public String getActivityEndDate() {
        return activityEndDate;
    }

    public void setActivityEndDate(String activityEndDate) {
        this.activityEndDate = activityEndDate;
    }

    public String getActivityHeader() {
        return activityHeader;
    }

    public void setActivityHeader(String activityHeader) {
        this.activityHeader = activityHeader;
    }

    public Integer getActivityCampaignId() {
        return activityCampaignId;
    }

    public void setActivityCampaignId(Integer activityCampaignId) {
        this.activityCampaignId = activityCampaignId;
    }

    public String getSelfStatus() {
        return selfStatus;
    }

    public void setSelfStatus(String selfStatus) {
        this.selfStatus = selfStatus;
    }
}
