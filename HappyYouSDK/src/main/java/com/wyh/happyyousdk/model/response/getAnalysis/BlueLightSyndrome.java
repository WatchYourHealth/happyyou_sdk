package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlueLightSyndrome {
    @SerializedName("blsFlag")
    @Expose
    private boolean blsFlag;
    @SerializedName("blsText")
    @Expose
    private String blsText;
    @SerializedName("addictedTV")
    @Expose
    private String addictedTV;
    @SerializedName("moreScreenTime")
    @Expose
    private String moreScreenTime;
    @SerializedName("soundSleep")
    @Expose
    private String soundSleep;

    public boolean isBlsFlag() {
        return blsFlag;
    }

    public void setBlsFlag(boolean blsFlag) {
        this.blsFlag = blsFlag;
    }

    public String getBlsText() {
        return blsText;
    }

    public void setBlsText(String blsText) {
        this.blsText = blsText;
    }

    public String getAddictedTV() {
        return addictedTV;
    }

    public void setAddictedTV(String addictedTV) {
        this.addictedTV = addictedTV;
    }

    public String getMoreScreenTime() {
        return moreScreenTime;
    }

    public void setMoreScreenTime(String moreScreenTime) {
        this.moreScreenTime = moreScreenTime;
    }

    public String getSoundSleep() {
        return soundSleep;
    }

    public void setSoundSleep(String soundSleep) {
        this.soundSleep = soundSleep;
    }
}
