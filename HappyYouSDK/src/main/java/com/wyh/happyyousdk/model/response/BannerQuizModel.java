package com.wyh.happyyousdk.model.response;

import com.wyh.happyyousdk.model.response.QuestionModel;

import java.util.ArrayList;

public class BannerQuizModel {
    private Integer QuizId;
    private String category;
    private Integer totalQuestions;
    private Integer userScore;
    private ArrayList<QuestionModel> answerJson;

    public BannerQuizModel(int qId, String categoryName, int size, int totalScore, ArrayList<QuestionModel> answerJson) {
        this.answerJson = answerJson;
        this.QuizId = qId;
        this.category = categoryName;
        this.totalQuestions = size;
        this.userScore = totalScore;
    }

    public Integer getQuizId() {
        return QuizId;
    }

    public void setQuizId(Integer quizId) {
        this.QuizId = quizId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(Integer userScore) {
        this.userScore = userScore;
    }

    public ArrayList<QuestionModel> getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(ArrayList<QuestionModel> answerJson) {
        this.answerJson = answerJson;
    }

}


