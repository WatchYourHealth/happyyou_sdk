package com.wyh.happyyousdk.model.request.unwind;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnwindShareRequest {
    @SerializedName("unwindType")
    @Expose
    private String unwindType;
    @SerializedName("unwindId")
    @Expose
    private Integer unwindId;
    @SerializedName("sharedTo")
    @Expose
    private String sharedTo;

    public UnwindShareRequest(String unwindType, Integer unwindId, String sharedTo) {
        this.unwindType = unwindType;
        this.unwindId = unwindId;
        this.sharedTo = sharedTo;
    }

    public String getUnwindType() {
        return unwindType;
    }

    public void setUnwindType(String unwindType) {
        this.unwindType = unwindType;
    }

    public Integer getUnwindId() {
        return unwindId;
    }

    public void setUnwindId(Integer unwindId) {
        this.unwindId = unwindId;
    }

    public String getSharedTo() {
        return sharedTo;
    }

    public void setSharedTo(String sharedTo) {
        this.sharedTo = sharedTo;
    }
}
