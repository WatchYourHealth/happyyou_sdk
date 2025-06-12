package com.wyh.happyyousdk.model.request.quizathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClaimRewardRequest implements Serializable {


    @SerializedName("TransId")
    @Expose
    String TransId;
    @SerializedName("IsClaim")
    @Expose
    boolean IsClaim;
    @SerializedName("IsReclaim")
    @Expose
    boolean IsReclaim;
    @SerializedName("BurnValue")
    @Expose
    String BurnValue;

    public ClaimRewardRequest(String transId, boolean isClaim, boolean isReclaim, String burnValue) {
        TransId = transId;
        IsClaim = isClaim;
        IsReclaim = isReclaim;
        BurnValue = burnValue;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public boolean isClaim() {
        return IsClaim;
    }

    public void setClaim(boolean claim) {
        IsClaim = claim;
    }

    public boolean isReclaim() {
        return IsReclaim;
    }

    public void setReclaim(boolean reclaim) {
        IsReclaim = reclaim;
    }

    public String getBurnValue() {
        return BurnValue;
    }

    public void setBurnValue(String burnValue) {
        BurnValue = burnValue;
    }
}
