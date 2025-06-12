package com.wyh.happyyousdk.model.request.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardsHistoryRequest {
    @SerializedName("transStatus")
    @Expose
    private String transStatus;

    public RewardsHistoryRequest(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }
}
