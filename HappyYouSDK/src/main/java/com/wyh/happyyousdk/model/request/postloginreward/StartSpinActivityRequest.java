package com.wyh.happyyousdk.model.request.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StartSpinActivityRequest implements Serializable {
    @SerializedName("ActivityTransId")
    @Expose
    private int ActivityTransId;

    public StartSpinActivityRequest(int activityTransId) {
        ActivityTransId = activityTransId;
    }

    public int getActivityTransId() {
        return ActivityTransId;
    }

    public void setActivityTransId(int activityTransId) {
        ActivityTransId = activityTransId;
    }
}
