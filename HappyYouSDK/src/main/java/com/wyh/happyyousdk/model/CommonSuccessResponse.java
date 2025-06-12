package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;

public class CommonSuccessResponse extends FeedbackResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;
    @SerializedName("freevoucher")
    @Expose
    private FreeVoucher freeVoucher;
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("spinTheWheelRewardsDetail")
    @Expose
    private AssignRewardsResponse.SpinRewardsData spinTheWheelRewardsModel;

    @SerializedName("quizathonRewardsDetail")
    @Expose
    private QuizathonRewardData quizathonRewardData;

    public QuizathonRewardData getQuizathonRewardData() {
        return quizathonRewardData;
    }

    public void setQuizathonRewardData(QuizathonRewardData quizathonRewardData) {
        this.quizathonRewardData = quizathonRewardData;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public FreeVoucher getFreeVoucher() {
        return freeVoucher;
    }

    public void setFreeVoucher(FreeVoucher freeVoucher) {
        this.freeVoucher = freeVoucher;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }

    public AssignRewardsResponse.SpinRewardsData getSpinTheWheelRewardsModel() {
        return spinTheWheelRewardsModel;
    }

    public void setSpinTheWheelRewardsModel(AssignRewardsResponse.SpinRewardsData spinTheWheelRewardsModel) {
        this.spinTheWheelRewardsModel = spinTheWheelRewardsModel;
    }
}
