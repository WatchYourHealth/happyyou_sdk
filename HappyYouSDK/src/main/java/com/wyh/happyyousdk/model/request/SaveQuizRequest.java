package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveQuizRequest {
    @SerializedName("QuestionId")
    @Expose
    public int questionId;
    @SerializedName("integrationid")
    @Expose
    public String integrationid;
    @SerializedName("Category")
    @Expose
    public String category;
    @SerializedName("totalQuestions")
    @Expose
    public int totalQuestions;
    @SerializedName("score")
    @Expose
    public int score;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("answerJson")
    @Expose
    public String answerJson;


    public SaveQuizRequest(int questionId, String integrationid, String category, int totalQuestions, int score, String status, String answerJson) {
        this.questionId = questionId;
        this.integrationid = integrationid;
        this.category = category;
        this.totalQuestions = totalQuestions;
        this.score = score;
        this.status = status;
        this.answerJson = answerJson;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getIntegrationid() {
        return integrationid;
    }

    public void setIntegrationid(String integrationid) {
        this.integrationid = integrationid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
    }
}
