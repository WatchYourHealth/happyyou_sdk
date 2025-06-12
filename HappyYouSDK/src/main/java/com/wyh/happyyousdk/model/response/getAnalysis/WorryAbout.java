package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorryAbout {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("optimalRating")
    @Expose
    private String optimalRating;
    @SerializedName("calculatedRating")
    @Expose
    private String calculatedRating;
    @SerializedName("cls")
    @Expose
    private String cls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptimalRating() {
        return optimalRating;
    }

    public void setOptimalRating(String optimalRating) {
        this.optimalRating = optimalRating;
    }

    public String getCalculatedRating() {
        return calculatedRating;
    }

    public void setCalculatedRating(String calculatedRating) {
        this.calculatedRating = calculatedRating;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }
}
