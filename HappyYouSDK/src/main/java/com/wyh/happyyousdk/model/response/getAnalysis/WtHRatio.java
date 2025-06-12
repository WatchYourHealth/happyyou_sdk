package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WtHRatio {
    @SerializedName("wthRatio")
    @Expose
    private double wthRatio;
    @SerializedName("wthText")
    @Expose
    private String wthText;
    @SerializedName("height")
    @Expose
    private double height;
    @SerializedName("waist")
    @Expose
    private int waist;

    public double getWthRatio() {
        return wthRatio;
    }

    public void setWthRatio(double wthRatio) {
        this.wthRatio = wthRatio;
    }

    public String getWthText() {
        return wthText;
    }

    public void setWthText(String wthText) {
        this.wthText = wthText;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }
}
