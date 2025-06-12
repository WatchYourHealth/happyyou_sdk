package com.wyh.happyyousdk.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardBottomCardData {
    @SerializedName("timeOfDay")
    @Expose
    private String timeOfTheDay;

    public DashboardBottomCardData(String timeOfTheDay) {
        this.timeOfTheDay = timeOfTheDay;
    }
}
