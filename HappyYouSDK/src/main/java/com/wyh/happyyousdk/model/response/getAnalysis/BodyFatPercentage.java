package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyFatPercentage {
    @SerializedName("bodyFatPercentage")
    @Expose
    private double bodyFatPercentage;
    @SerializedName("bfDesc")
    @Expose
    private String bfDesc;

    public double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public String getBfDesc() {
        return bfDesc;
    }

    public void setBfDesc(String bfDesc) {
        this.bfDesc = bfDesc;
    }
}
