package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.response.AddOnActivities;

import java.util.ArrayList;
import java.util.List;

public class LevelDashboardData {
    @SerializedName("freebieVoucher")
    @Expose
    private List<FreeVoucher> freeVoucher;
    @SerializedName("currentLevel")
    @Expose
    private RewardsLevel currentLevel;
    @SerializedName("topUps")
    @Expose
    private List<TopUp> topUps;
    @SerializedName("upcomingLevel")
    @Expose
    private RewardsLevel upcomingLevel;
    @SerializedName("totalPoint")
    @Expose
    private int totalPoints;
    @SerializedName("equivalentAmount")
    @Expose
    private double equivalentAmount;
    @SerializedName("popRewards")
    @Expose
    private List<PopRewardModel> popRewards;
    @SerializedName("levelPopup")
    @Expose
    private String levelPopup;
    @SerializedName("progressPercentage")
    @Expose
    private int progressPercentage;

    @SerializedName("addOnActivities")
    @Expose
    private ArrayList<AddOnActivities> addOnActivities;


    public List<FreeVoucher> getFreebieVoucher() {
        return freeVoucher;
    }

    public void setFreebieVoucher(List<FreeVoucher> freebieVoucher) {
        this.freeVoucher = freebieVoucher;
    }

    public RewardsLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(RewardsLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public List<TopUp> getTopUps() {
        return topUps;
    }

    public void setTopUps(List<TopUp> topUps) {
        this.topUps = topUps;
    }

    public RewardsLevel getUpcomingLevel() {
        return upcomingLevel;
    }

    public void setUpcomingLevel(RewardsLevel upcomingLevel) {
        this.upcomingLevel = upcomingLevel;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public double getEquivalentAmount() {
        return equivalentAmount;
    }

    public void setEquivalentAmount(double equivalentAmount) {
        this.equivalentAmount = equivalentAmount;
    }

    public List<PopRewardModel> getPopRewards() {
        return popRewards;
    }

    public void setPopRewards(List<PopRewardModel> popRewards) {
        this.popRewards = popRewards;
    }

    public String getLevelPopup() {
        return levelPopup;
    }

    public void setLevelPopup(String levelPopup) {
        this.levelPopup = levelPopup;
    }

    public List<FreeVoucher> getFreeVoucher() {
        return freeVoucher;
    }

    public void setFreeVoucher(List<FreeVoucher> freeVoucher) {
        this.freeVoucher = freeVoucher;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public ArrayList<AddOnActivities> getAddOnActivities() {
        return addOnActivities;
    }
}
