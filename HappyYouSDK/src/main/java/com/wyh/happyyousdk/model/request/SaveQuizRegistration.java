package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaveQuizRegistration implements Serializable {
    @SerializedName("QuizId")
    @Expose
    String QuizId;
    @SerializedName("AnswerJson")
    @Expose
    String AnswerJson;

    public SaveQuizRegistration(String quizid, String answerJson) {
        QuizId = quizid;
        AnswerJson = answerJson;
    }

    public String getQuizId() {
        return QuizId;
    }

    public void setQuizId(String quizid) {
        QuizId = quizid;
    }

    public String getAnswerJson() {
        return AnswerJson;
    }

    public void setAnswerJson(String answerJson) {
        AnswerJson = answerJson;
    }
}
