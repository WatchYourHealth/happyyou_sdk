package com.wyh.happyyousdk.model.response.quiz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TriviaHistoryData implements Serializable {
    @SerializedName("customerID")
    @Expose
    private String customerID;
    @SerializedName("integrationid")
    @Expose
    private String integrationid;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("triviaScore")
    @Expose
    private Integer triviaScore;
    @SerializedName("triviaTotalScore")
    @Expose
    private Integer triviaTotalScore;
    @SerializedName("answerJson")
    @Expose
    private String answerJson;
    @SerializedName("assignedOn")
    @Expose
    private String assignedOn;
    @SerializedName("completedOn")
    @Expose
    private String completedOn;
    @SerializedName("activityTitle")
    @Expose
    private String activityTitle;
    @SerializedName("rewardTitle")
    @Expose
    private String rewardTitle;
    @SerializedName("rewardValue")
    @Expose
    private String rewardValue;
    @SerializedName("rewardType")
    @Expose
    private String rewardType;

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getRewardTitle() {
        return rewardTitle;
    }

    public void setRewardTitle(String rewardTitle) {
        this.rewardTitle = rewardTitle;
    }

    public String getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(String rewardValue) {
        this.rewardValue = rewardValue;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getIntegrationid() {
        return integrationid;
    }

    public void setIntegrationid(String integrationid) {
        this.integrationid = integrationid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTriviaScore() {
        return triviaScore;
    }

    public void setTriviaScore(Integer triviaScore) {
        this.triviaScore = triviaScore;
    }

    public Integer getTriviaTotalScore() {
        return triviaTotalScore;
    }

    public void setTriviaTotalScore(Integer triviaTotalScore) {
        this.triviaTotalScore = triviaTotalScore;
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
    }

    public String getAssignedOn() {
        return assignedOn;
    }

    public void setAssignedOn(String assignedOn) {
        this.assignedOn = assignedOn;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }
}
