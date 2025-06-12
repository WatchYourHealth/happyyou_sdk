package com.wyh.happyyousdk.model.response.hra;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.RewardsModel;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;

public class SaveAnswersResponse extends FeedbackResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;
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

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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
}
