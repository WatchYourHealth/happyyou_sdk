package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SleepAnalysis {
    @SerializedName("sleepHours")
    @Expose
    private int sleepHours;
    @SerializedName("saText")
    @Expose
    private String saText;

    public int getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(int sleepHours) {
        this.sleepHours = sleepHours;
    }

    public String getSaText() {
        return saText;
    }

    public void setSaText(String saText) {
        this.saText = saText;
    }
}
