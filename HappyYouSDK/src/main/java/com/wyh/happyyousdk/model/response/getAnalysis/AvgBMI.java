package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvgBMI {
    @SerializedName("bmi")
    @Expose
    private double bmi;
    @SerializedName("bmiText")
    @Expose
    private Object bmiText;
    @SerializedName("avgbmi")
    @Expose
    private double avgbmi;

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public Object getBmiText() {
        return bmiText;
    }

    public void setBmiText(Object bmiText) {
        this.bmiText = bmiText;
    }

    public double getAvgbmi() {
        return avgbmi;
    }

    public void setAvgbmi(double avgbmi) {
        this.avgbmi = avgbmi;
    }
}
