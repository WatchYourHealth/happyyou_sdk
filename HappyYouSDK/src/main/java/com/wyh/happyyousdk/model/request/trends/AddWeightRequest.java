package com.wyh.happyyousdk.model.request.trends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddWeightRequest {

    @SerializedName("userWeight")
    @Expose
    private Double userWeight;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public AddWeightRequest(Double userWeight, String timestamp) {
        this.userWeight = userWeight;
        this.timestamp = timestamp;
    }

    public Double getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(Double userWeight) {
        this.userWeight = userWeight;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
