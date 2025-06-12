package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;

import java.io.Serializable;

public class EarnedRewardResponse implements Serializable {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("isPoolingReq")
    @Expose
    private Boolean isPoolingReq;
    @SerializedName("data")
    @Expose
    private EarnedRewardData data;

    @SerializedName("feedbackDetails")
    @Expose
    public FeedbackResponseData feedbackDetails;

    public FeedbackResponseData getFeedbackDetails() {
        return feedbackDetails;
    }

    public void setFeedbackDetails(FeedbackResponseData feedbackDetails) {
        this.feedbackDetails = feedbackDetails;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getIsPoolingReq() {
        return isPoolingReq;
    }

    public void setIsPoolingReq(Boolean isPoolingReq) {
        this.isPoolingReq = isPoolingReq;
    }

    public Boolean getPoolingReq() {
        return isPoolingReq;
    }

    public void setPoolingReq(Boolean poolingReq) {
        isPoolingReq = poolingReq;
    }

    public EarnedRewardData getData() {
        return data;
    }

    public void setData(EarnedRewardData data) {
        this.data = data;
    }
}
