package com.wyh.happyyousdk.model.response.hra;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HRAAnswersJson {
    @SerializedName("isCompleted")
    @Expose
    private boolean isCompleted;
    @SerializedName("Questions")
    @Expose
    private List<HraAnswersData> questions = null;

    public boolean isIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public List<HraAnswersData> getQuestions() {
        return questions;
    }

    public void setQuestions(List<HraAnswersData> questions) {
        this.questions = questions;
    }
}
