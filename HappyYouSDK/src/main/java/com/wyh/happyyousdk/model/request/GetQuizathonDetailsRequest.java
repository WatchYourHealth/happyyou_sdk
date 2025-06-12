package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetQuizathonDetailsRequest {


    @SerializedName("QuizId")
    @Expose
    private int QuizId;

    public GetQuizathonDetailsRequest(int quizId) {
        QuizId = quizId;
    }
}
