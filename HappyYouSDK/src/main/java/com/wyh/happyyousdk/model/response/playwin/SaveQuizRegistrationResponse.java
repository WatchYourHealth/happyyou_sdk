package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.response.quizathon.DialogModel;

public class SaveQuizRegistrationResponse extends CommonSuccessResponse {

    @SerializedName("data")
    @Expose
    QuizRegistrationData quizRegistrationData;

    public QuizRegistrationData getQuizRegistrationData() {
        return quizRegistrationData;
    }

    public void setQuizRegistrationData(QuizRegistrationData quizRegistrationData) {
        this.quizRegistrationData = quizRegistrationData;
    }
}
