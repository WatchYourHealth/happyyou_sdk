package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KLISubRequest {


    @SerializedName("ActivityId")
    @Expose
    private Integer ActivityId;


    public KLISubRequest(Integer activityId) {
        ActivityId = activityId;
    }
}
