package com.wyh.happyyousdk.model.request.quizathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RetakeQuestionRequest {
    @SerializedName("quizId")
    @Expose
    String quizId;

    public RetakeQuestionRequest(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
