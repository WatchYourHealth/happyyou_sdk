package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddOnStamps {

    @SerializedName("stampsValue")
    @Expose
    public int stampsValue;

    @SerializedName("activityName")
    @Expose
    public String activityName;

    public int getStampsValue() {
        return stampsValue;
    }

    public String getActivityName() {
        return activityName;
    }
}
