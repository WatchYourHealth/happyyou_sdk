package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignRewardsRequest {

    @SerializedName("Tempuuid")
    @Expose
    private String Tempuuid;

    @SerializedName("UUID")
    @Expose
    private String UUID;

    public AssignRewardsRequest(String tempuuid, String UUID) {
        Tempuuid = tempuuid;
        this.UUID = UUID;
    }
}
