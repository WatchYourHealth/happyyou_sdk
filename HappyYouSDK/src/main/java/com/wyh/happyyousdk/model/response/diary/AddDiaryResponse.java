package com.wyh.happyyousdk.model.response.diary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;

public class AddDiaryResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("data")
    @Expose
    public int data;
    @SerializedName("freevoucher")
    @Expose
    public Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    public Object enGTokens;
    @SerializedName("rewards")
    @Expose
    public Object rewards;


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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public Object getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(Object enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Object getRewards() {
        return rewards;
    }

    public void setRewards(Object rewards) {
        this.rewards = rewards;
    }
}
