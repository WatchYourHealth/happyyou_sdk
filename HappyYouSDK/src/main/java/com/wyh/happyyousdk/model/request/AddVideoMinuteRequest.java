package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddVideoMinuteRequest {

    @SerializedName("HealthTvId")
    @Expose
    private String HealthTvId;

    @SerializedName("readingmins")
    @Expose
    private String readingmins;

    public AddVideoMinuteRequest(String healthTvId, String readingmins) {
        HealthTvId = healthTvId;
        this.readingmins = readingmins;
    }
}
