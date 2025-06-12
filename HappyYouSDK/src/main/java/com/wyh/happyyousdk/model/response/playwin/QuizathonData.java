package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizathonData implements Serializable {

    @SerializedName("activeQuizathon")
    @Expose
    List<QuizathonModel> quizathonModels = new ArrayList<>();
    @SerializedName("feedbackRewardModel")
    @Expose
    List<QuizFeedbackModel> quizFeedbackModels = new ArrayList<>();
    @SerializedName("getStreakQuizathonModels")
    @Expose
    List<QuizathonModel> quizathonStreakModelList = new ArrayList<>();

    public List<QuizathonModel> getQuizathonStreakModelList() {
        return quizathonStreakModelList;
    }

    public void setQuizathonStreakModelList(List<QuizathonModel> quizathonStreakModelList) {
        this.quizathonStreakModelList = quizathonStreakModelList;
    }

    public List<QuizathonModel> getQuizathonModels() {
        return quizathonModels;
    }

    public void setQuizathonModels(List<QuizathonModel> quizathonModels) {
        this.quizathonModels = quizathonModels;
    }

    public List<QuizFeedbackModel> getQuizFeedbackModels() {
        return quizFeedbackModels;
    }

    public void setQuizFeedbackModels(List<QuizFeedbackModel> quizFeedbackModels) {
        this.quizFeedbackModels = quizFeedbackModels;
    }
}
