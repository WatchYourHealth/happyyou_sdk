package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;

import java.io.Serializable;

public class SpinActivityResponse implements Serializable {
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
    private RewardItem data;


    @SerializedName("spinTheWheelRewardsDetail")
    @Expose
    private AssignRewardsResponse.SpinRewardsData spinTheWheelRewardsModel;


    @SerializedName("feedbackDetails")
    @Expose
    public FeedbackResponseData feedbackDetails;

    public AssignRewardsResponse.SpinRewardsData getSpinTheWheelRewardsModel() {
        return spinTheWheelRewardsModel;
    }

    public void setSpinTheWheelRewardsModel(AssignRewardsResponse.SpinRewardsData spinTheWheelRewardsModel) {
        this.spinTheWheelRewardsModel = spinTheWheelRewardsModel;
    }

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

    public RewardItem getData() {
        return data;
    }

    public void setData(RewardItem data) {
        this.data = data;
    }
}
