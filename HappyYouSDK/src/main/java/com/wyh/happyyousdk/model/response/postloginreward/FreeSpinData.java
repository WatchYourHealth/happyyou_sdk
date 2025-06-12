package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FreeSpinData implements Serializable {
    @SerializedName("lastFreeSpinOn")
    @Expose
    private String lastFreeSpinOn;
    @SerializedName("hasFreeSpin")
    @Expose
    private Boolean hasFreeSpin;
    @SerializedName("remainingFreeSpins")
    @Expose
    private Integer remainingFreeSpins;
    @SerializedName("freeSpinDuration")
    @Expose
    private Integer freeSpinDuration;
    @SerializedName("freeSpinTitle")
    @Expose
    private String freeSpinTitle;

    @SerializedName("freeExpireOn")
    @Expose
    private String freeExpireOn;

    @SerializedName("freeSpinMessage")
    @Expose
    private String freeSpinMessage;

    public String getFreeSpinMessage() {
        return freeSpinMessage;
    }

    public void setFreeSpinMessage(String freeSpinMessage) {
        this.freeSpinMessage = freeSpinMessage;
    }

    public String getFreeExpireOn() {
        return freeExpireOn;
    }

    public void setFreeExpireOn(String freeExpireOn) {
        this.freeExpireOn = freeExpireOn;
    }

    public String getFreeSpinTitle() {
        return freeSpinTitle;
    }

    public void setFreeSpinTitle(String freeSpinTitle) {
        this.freeSpinTitle = freeSpinTitle;
    }

    public String getLastFreeSpinOn() {
        return lastFreeSpinOn;
    }

    public void setLastFreeSpinOn(String lastFreeSpinOn) {
        this.lastFreeSpinOn = lastFreeSpinOn;
    }

    public Boolean getHasFreeSpin() {
        return hasFreeSpin;
    }

    public void setHasFreeSpin(Boolean hasFreeSpin) {
        this.hasFreeSpin = hasFreeSpin;
    }

    public Integer getRemainingFreeSpins() {
        return remainingFreeSpins;
    }

    public void setRemainingFreeSpins(Integer remainingFreeSpins) {
        this.remainingFreeSpins = remainingFreeSpins;
    }

    public Integer getFreeSpinDuration() {
        return freeSpinDuration;
    }

    public void setFreeSpinDuration(Integer freeSpinDuration) {
        this.freeSpinDuration = freeSpinDuration;
    }
}
