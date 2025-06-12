package com.wyh.happyyousdk.model.response.dass21;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.RewardsModel;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;

import java.util.List;

public class DassAnalysisResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private List<DassAnalysisData> data = null;
    @SerializedName("freevoucher")
    @Expose
    private FreeVoucher freeVoucher;

    @SerializedName("feedbackDetails")
    @Expose
    private FeedbackResponseData feedbackDetails;

    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;

    @SerializedName("spinTheWheelRewardsDetail")
    @Expose
    private AssignRewardsResponse.SpinRewardsData spinTheWheelRewardsModel;

    public AssignRewardsResponse.SpinRewardsData getSpinTheWheelRewardsModel() {
        return spinTheWheelRewardsModel;
    }

    public void setSpinTheWheelRewardsModel(AssignRewardsResponse.SpinRewardsData spinTheWheelRewardsModel) {
        this.spinTheWheelRewardsModel = spinTheWheelRewardsModel;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DassAnalysisData> getData() {
        return data;
    }

    public void setData(List<DassAnalysisData> data) {
        this.data = data;
    }

    public FreeVoucher getFreeVoucher() {
        return freeVoucher;
    }

    public void setFreeVoucher(FreeVoucher freeVoucher) {
        this.freeVoucher = freeVoucher;
    }

    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }

    public FeedbackResponseData getFeedbackDetails() {
        return feedbackDetails;
    }
}
