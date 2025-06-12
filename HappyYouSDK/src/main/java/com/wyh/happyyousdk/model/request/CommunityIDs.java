package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommunityIDs {

    @SerializedName("CommunityId")
    @Expose
    private int CommunityId;

    public CommunityIDs(int communityId) {
        CommunityId = communityId;
    }

}
