package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BurnSpinData implements Serializable {
    @SerializedName("lastBurnSpin")
    @Expose
    private String lastBurnSpin;
    @SerializedName("hasBurnSpin")
    @Expose
    private Boolean hasBurnSpin;
    @SerializedName("isPointsBurned")
    @Expose
    private Boolean isPointsBurned;
    @SerializedName("pointsToBurn")
    @Expose
    private Integer pointsToBurn;
    @SerializedName("remainingBurnSpins")
    @Expose
    private Integer remainingBurnSpins;
    @SerializedName("burnSpinDuration")
    @Expose
    private Integer burnSpinDuration;
    @SerializedName("burnSpinTitle")
    @Expose
    private String burnSpinTitle;

    @SerializedName("burnExpireOn")
    @Expose
    private String burnExpireOn;

    @SerializedName("hasSufficientBalance")
    @Expose
    private boolean hasSufficientBalance;
    @SerializedName("showPopup")
    @Expose
    private boolean showPopup;
    @SerializedName("popupTitle")
    @Expose
    private String popupTitle;
    @SerializedName("popupMessage")
    @Expose
    private String popupMessage;

    public boolean isHasSufficientBalance() {
        return hasSufficientBalance;
    }

    public void setHasSufficientBalance(boolean hasSufficientBalance) {
        this.hasSufficientBalance = hasSufficientBalance;
    }

    public boolean isShowPopup() {
        return showPopup;
    }

    public void setShowPopup(boolean showPopup) {
        this.showPopup = showPopup;
    }

    public String getPopupTitle() {
        return popupTitle;
    }

    public void setPopupTitle(String popupTitle) {
        this.popupTitle = popupTitle;
    }

    public String getPopupMessage() {
        return popupMessage;
    }

    public void setPopupMessage(String popupMessage) {
        this.popupMessage = popupMessage;
    }

    public String getBurnExpireOn() {
        return burnExpireOn;
    }

    public void setBurnExpireOn(String burnExpireOn) {
        this.burnExpireOn = burnExpireOn;
    }

    public Boolean getPointsBurned() {
        return isPointsBurned;
    }

    public void setPointsBurned(Boolean pointsBurned) {
        isPointsBurned = pointsBurned;
    }

    public String getBurnSpinTitle() {
        return burnSpinTitle;
    }

    public void setBurnSpinTitle(String burnSpinTitle) {
        this.burnSpinTitle = burnSpinTitle;
    }

    public String getLastBurnSpin() {
        return lastBurnSpin;
    }

    public void setLastBurnSpin(String lastBurnSpin) {
        this.lastBurnSpin = lastBurnSpin;
    }

    public Boolean getHasBurnSpin() {
        return hasBurnSpin;
    }

    public void setHasBurnSpin(Boolean hasBurnSpin) {
        this.hasBurnSpin = hasBurnSpin;
    }

    public Boolean getIsPointsBurned() {
        return isPointsBurned;
    }

    public void setIsPointsBurned(Boolean isPointsBurned) {
        this.isPointsBurned = isPointsBurned;
    }

    public Integer getPointsToBurn() {
        return pointsToBurn;
    }

    public void setPointsToBurn(Integer pointsToBurn) {
        this.pointsToBurn = pointsToBurn;
    }

    public Integer getRemainingBurnSpins() {
        return remainingBurnSpins;
    }

    public void setRemainingBurnSpins(Integer remainingBurnSpins) {
        this.remainingBurnSpins = remainingBurnSpins;
    }

    public Integer getBurnSpinDuration() {
        return burnSpinDuration;
    }

    public void setBurnSpinDuration(Integer burnSpinDuration) {
        this.burnSpinDuration = burnSpinDuration;
    }
}
