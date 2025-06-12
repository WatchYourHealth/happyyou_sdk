package com.wyh.happyyousdk.model.request.quizathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ActivityRewardRequest implements Serializable {
    @SerializedName("TransId")
    @Expose
    String transId;

    @SerializedName("FeatureName")
    @Expose
    String featureName;

    public ActivityRewardRequest(String transId, String featureName) {
        this.transId = transId;
        this.featureName = featureName;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
}
