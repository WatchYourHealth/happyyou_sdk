package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddOnPoints {

    @SerializedName("pointsValue")
    @Expose
    public int pointsValue;

    @SerializedName("activityName")
    @Expose
    public String activityName;

    public int getPointsValue() {
        return pointsValue;
    }

    public String getActivityName() {
        return activityName;
    }
}
