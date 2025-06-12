package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EandBDashboardData {
    @SerializedName("userData")
    @Expose
    private EandBUserData userData;
    @SerializedName("pendingActivity")
    @Expose
    private List<EandBPendingActivity> pendingActivity;
    @SerializedName("availableVoucher")
    @Expose
    private List<EandBAvailableVoucher> availableVoucher;
    @SerializedName("unlockedVoucher")
    @Expose
    private List<EandBUnlockedVoucher> unlockedVoucher;
    @SerializedName("unscratchedTokens")
    @Expose
    private List<UnscratchedTokensData> unscratchedTokens;
    @SerializedName("engPopup")
    @Expose
    private List<EarnPopModel> engPopup;

    public EandBUserData getUserData() {
        return userData;
    }

    public void setUserData(EandBUserData userData) {
        this.userData = userData;
    }

    public List<EandBPendingActivity> getPendingActivity() {
        return pendingActivity;
    }

    public void setPendingActivity(List<EandBPendingActivity> pendingActivity) {
        this.pendingActivity = pendingActivity;
    }

    public List<EandBAvailableVoucher> getAvailableVoucher() {
        return availableVoucher;
    }

    public void setAvailableVoucher(List<EandBAvailableVoucher> availableVoucher) {
        this.availableVoucher = availableVoucher;
    }

    public List<EandBUnlockedVoucher> getUnlockedVoucher() {
        return unlockedVoucher;
    }

    public void setUnlockedVoucher(List<EandBUnlockedVoucher> unlockedVoucher) {
        this.unlockedVoucher = unlockedVoucher;
    }

    public List<UnscratchedTokensData> getUnscratchedTokens() {
        return unscratchedTokens;
    }

    public void setUnscratchedTokens(List<UnscratchedTokensData> unscratchedTokens) {
        this.unscratchedTokens = unscratchedTokens;
    }

    public List<EarnPopModel> getEngPopup() {
        return engPopup;
    }

    public void setEngPopup(List<EarnPopModel> engPopup) {
        this.engPopup = engPopup;
    }
}
