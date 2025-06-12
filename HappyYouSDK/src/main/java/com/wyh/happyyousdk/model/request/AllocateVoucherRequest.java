package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllocateVoucherRequest {
    @SerializedName("voucherCode")
    @Expose
    private String voucherCode;
    @SerializedName("activityId")
    @Expose
    private Integer activityId;
    @SerializedName("userId")
    @Expose
    private String userId;

    public AllocateVoucherRequest(String voucherCode, Integer activityId, String userId) {
        this.voucherCode = voucherCode;
        this.activityId = activityId;
        this.userId = userId;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
