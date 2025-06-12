package com.wyh.happyyousdk.model.request.quizathon;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaveQuizathonAnswerRequest implements Serializable {
    @SerializedName("QuizId")
    String QuizId;
    @SerializedName("AnswerJson")
    String AnswerJson;
    @SerializedName("Status")
    String Status;
    @SerializedName("IsRetake")
    boolean IsRetake;
    @SerializedName("RetakeId")
    String RetakeId;
    @SerializedName("UserScore")
    String UserScore;
    @SerializedName("TotalScore")
    String TotalScore;

    public SaveQuizathonAnswerRequest(String quizId, String answerJson, String status, boolean isRetake, String retakeId, String userScore, String totalScore) {
        QuizId = quizId;
        AnswerJson = answerJson;
        Status = status;
        IsRetake = isRetake;
        RetakeId = retakeId;
        UserScore = userScore;
        TotalScore = totalScore;
    }

    public String getQuizId() {
        return QuizId;
    }

    public void setQuizId(String quizId) {
        QuizId = quizId;
    }

    public String getAnswerJson() {
        return AnswerJson;
    }

    public void setAnswerJson(String answerJson) {
        AnswerJson = answerJson;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public boolean getIsRetake() {
        return IsRetake;
    }

    public void setIsRetake(boolean isRetake) {
        IsRetake = isRetake;
    }

    public String getRetakeId() {
        return RetakeId;
    }

    public void setRetakeId(String retakeId) {
        RetakeId = retakeId;
    }

    public String getUserScore() {
        return UserScore;
    }

    public void setUserScore(String userScore) {
        UserScore = userScore;
    }

    public String getTotalScore() {
        return TotalScore;
    }

    public void setTotalScore(String totalScore) {
        TotalScore = totalScore;
    }
}
