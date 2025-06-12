package com.wyh.happyyousdk.model.response.dashboard_new;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.RewardsModel;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;

import java.util.List;

public class DashboardResponseNew {

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
    private DashboardDataNew data;
    @SerializedName("feedbackDetails")
    @Expose
    private FeedbackResponseData feedbackDetails;
    @SerializedName("freevoucher")
    @Expose
    private FreeVoucher freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;
    @SerializedName("userkey")
    @Expose
    private Object userkey;
    @SerializedName("isGoogleFit")
    @Expose
    private Boolean isGoogleFit;

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

    public DashboardDataNew getData() {
        return data;
    }

    public void setData(DashboardDataNew data) {
        this.data = data;
    }


    public FeedbackResponseData getFeedbackDetails() {
        return feedbackDetails;
    }

    public FreeVoucher getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(FreeVoucher freevoucher) {
        this.freevoucher = freevoucher;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }

    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }

    public Object getUserkey() {
        return userkey;
    }

    public void setUserkey(Object userkey) {
        this.userkey = userkey;
    }

    public Boolean getIsGoogleFit() {
        return isGoogleFit;
    }

    public void setIsGoogleFit(Boolean isGoogleFit) {
        this.isGoogleFit = isGoogleFit;
    }

    public Boolean getPoolingReq() {
        return isPoolingReq;
    }

    public void setPoolingReq(Boolean poolingReq) {
        isPoolingReq = poolingReq;
    }

    public void setFeedbackDetails(FeedbackResponseData feedbackDetails) {
        this.feedbackDetails = feedbackDetails;
    }

    public Boolean getGoogleFit() {
        return isGoogleFit;
    }

    public void setGoogleFit(Boolean googleFit) {
        isGoogleFit = googleFit;
    }
}