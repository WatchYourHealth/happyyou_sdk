package com.wyh.happyyousdk.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveBannerQuizRequestModel {


    @SerializedName("answerJson")
    @Expose
    public String bannerQuizModel;

    @SerializedName("QuizID")
    @Expose
    public int quizID;

    public SaveBannerQuizRequestModel(int qId, String bannerQuizModel) {
        this.quizID = qId;
        this.bannerQuizModel = bannerQuizModel;
    }


    public String getBannerQuizModel() {
        return bannerQuizModel;
    }

    public void setBannerQuizModel(String bannerQuizModel) {
        this.bannerQuizModel = bannerQuizModel;
    }

    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }
}
