package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LockRewardsRequest {

    @SerializedName("Tempuuid")
    @Expose
    private String Tempuuid;

    public LockRewardsRequest(String tempuuid) {
        Tempuuid = tempuuid;
    }
}
