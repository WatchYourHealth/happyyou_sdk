package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdealBMIWeight {
    @SerializedName("idealBMI")
    @Expose
    private double idealBMI;
    @SerializedName("ibwText")
    @Expose
    private String ibwText;
    @SerializedName("height")
    @Expose
    private double height;
    @SerializedName("weight")
    @Expose
    private double weight;
    @SerializedName("looseWeight")
    @Expose
    private double looseWeight;
    @SerializedName("gainWeight")
    @Expose
    private double gainWeight;

    public double getIdealBMI() {
        return idealBMI;
    }

    public void setIdealBMI(double idealBMI) {
        this.idealBMI = idealBMI;
    }

    public String getIbwText() {
        return ibwText;
    }

    public void setIbwText(String ibwText) {
        this.ibwText = ibwText;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLooseWeight() {
        return looseWeight;
    }

    public void setLooseWeight(double looseWeight) {
        this.looseWeight = looseWeight;
    }

    public double getGainWeight() {
        return gainWeight;
    }

    public void setGainWeight(double gainWeight) {
        this.gainWeight = gainWeight;
    }
}
