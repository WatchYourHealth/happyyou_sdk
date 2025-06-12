package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PolicyListReq {

    @SerializedName("startIndex")
    @Expose
    String startIndex;
    @SerializedName("pageSize")
    @Expose
    String pageSize;
    @SerializedName("userId")
    @Expose
    String userId;

    public PolicyListReq(String startIndex, String pageSize, String userId) {
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.userId = userId;
    }


}
