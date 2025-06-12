package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.RewardsModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;

import java.io.Serializable;

public class SaveQuizResponseModel {
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("data")
    @Expose
    public SaveQuizAnswerData data;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SaveQuizAnswerData getData() {
        return data;
    }

    public void setData(SaveQuizAnswerData data) {
        this.data = data;
    }

    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }
}


