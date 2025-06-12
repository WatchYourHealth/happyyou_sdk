package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;

import java.util.ArrayList;
import java.util.List;

public class QuizathonResponseModel extends CommonSuccessResponse {
    @SerializedName("data")
    @Expose
    QuizathonData quizathonData;

    public QuizathonData getQuizathonData() {
        return quizathonData;
    }

    public void setQuizathonData(QuizathonData quizathonData) {
        this.quizathonData = quizathonData;
    }
}
