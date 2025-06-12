package com.wyh.happyyousdk.model.response.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.rewards.PopRewardModel;

import java.util.List;

public class DashboardData {
    @SerializedName("actoMeter")
    @Expose
    private ActOMeterResponse actOMeterResponse;
    @SerializedName("actoMeterTribe")
    @Expose
    private ActOMeterTribe actOMeterTribeList;

    @SerializedName("rewards")
    @Expose
    private RewardsResponse rewardsResponses;

    @SerializedName("reminderDetails")
    @Expose
    private RemindersDetails reminderDetails;
    @SerializedName("widgets")
    @Expose
    private WidgetsData widgets;
    @SerializedName("messages")
    @Expose
    private List<String> messages;

    @SerializedName("levelPopup")
    @Expose
    private String levelPopup;

    @SerializedName("currentWeight")
    @Expose
    private String currentWeight;

    @SerializedName("referralCode")
    @Expose
    private String referralCode;

    @SerializedName("corpReferralCode")
    @Expose
    private String corpReferralCode;

    @SerializedName("dynamicLabel")
    @Expose
    private String dynamicLabel;

    @SerializedName("hraTaken")
    @Expose
    private Boolean hraTaken = false;


    public String getDynamicLabel() {
        return dynamicLabel;
    }

    public void setDynamicLabel(String dynamicLabel) {
        this.dynamicLabel = dynamicLabel;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getCorpReferralCode() {
        return corpReferralCode;
    }

    public void setCorpReferralCode(String corpReferralCode) {
        this.corpReferralCode = corpReferralCode;
    }

    public String getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(String currentWeight) {
        this.currentWeight = currentWeight;
    }

    private int bannerCount;

    public int getBannerCount() {
        return bannerCount;
    }

    public void setBannerCount(int bannerCount) {
        this.bannerCount = bannerCount;
    }

    @SerializedName("popRewards")
    @Expose
    private List<PopRewardModel> popRewards;

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public ActOMeterResponse getActOMeterResponse() {
        return actOMeterResponse;
    }

    public void setActOMeterResponse(ActOMeterResponse actOMeterResponse) {
        this.actOMeterResponse = actOMeterResponse;
    }

    public ActOMeterTribe getActOMeterTribeList() {
        return actOMeterTribeList;
    }

    public void setActOMeterTribeList(ActOMeterTribe actOMeterTribeList) {
        this.actOMeterTribeList = actOMeterTribeList;
    }

    public RewardsResponse getRewardsList() {
        return rewardsResponses;
    }

    public void setRewardsResponses(RewardsResponse rewardsResponses) {
        this.rewardsResponses = rewardsResponses;
    }

    public RemindersDetails getReminderDetails() {
        return reminderDetails;
    }

    public void setReminderDetails(RemindersDetails reminderDetails) {
        this.reminderDetails = reminderDetails;
    }

    public WidgetsData getWidgets() {
        return widgets;
    }

    public void setWidgets(WidgetsData widgets) {
        this.widgets = widgets;
    }

    public String getLevelPopup() {
        return levelPopup;
    }

    public void setLevelPopup(String levelPopup) {
        this.levelPopup = levelPopup;
    }


    public RewardsResponse getRewardsResponses() {
        return rewardsResponses;
    }

    public List<PopRewardModel> getPopRewards() {
        return popRewards;
    }

    public void setPopRewards(List<PopRewardModel> popRewards) {
        this.popRewards = popRewards;
    }

    public Boolean getHraTaken() {
        return hraTaken;
    }

    public void setHraTaken(Boolean hraTaken) {
        this.hraTaken = hraTaken;
    }
}
