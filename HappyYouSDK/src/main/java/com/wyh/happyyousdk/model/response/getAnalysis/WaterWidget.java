package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaterWidget {
    @SerializedName("actualDrinkingGlass")
    @Expose
    private int actualDrinkingGlass;
    @SerializedName("shouldDrinkGlass")
    @Expose
    private int shouldDrinkGlass;
    @SerializedName("wwText")
    @Expose
    private String wwText;

    public int getActualDrinkingGlass() {
        return actualDrinkingGlass;
    }

    public void setActualDrinkingGlass(int actualDrinkingGlass) {
        this.actualDrinkingGlass = actualDrinkingGlass;
    }

    public int getShouldDrinkGlass() {
        return shouldDrinkGlass;
    }

    public void setShouldDrinkGlass(int shouldDrinkGlass) {
        this.shouldDrinkGlass = shouldDrinkGlass;
    }

    public String getWwText() {
        return wwText;
    }

    public void setWwText(String wwText) {
        this.wwText = wwText;
    }
}
