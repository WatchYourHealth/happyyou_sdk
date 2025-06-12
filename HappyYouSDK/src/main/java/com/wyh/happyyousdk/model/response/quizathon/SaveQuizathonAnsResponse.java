package com.wyh.happyyousdk.model.response.quizathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;

public class SaveQuizathonAnsResponse extends CommonSuccessResponse {
    @SerializedName("data")
    @Expose
    SaveQuizathonAnsModel data;
    @SerializedName("feedbackRewardModel")
    @Expose
    private QuizathonRewardData feedbackRewardModel;

    public SaveQuizathonAnsModel getData() {
        return data;
    }

    public void setData(SaveQuizathonAnsModel data) {
        this.data = data;
    }

    public QuizathonRewardData getFeedbackRewardModel() {
        return feedbackRewardModel;
    }

    public void setFeedbackRewardModel(QuizathonRewardData feedbackRewardModel) {
        this.feedbackRewardModel = feedbackRewardModel;
    }
}
