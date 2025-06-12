package com.wyh.happyyousdk.model.response.hraSection.newHRA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HRAJson {
    @SerializedName("sectionId")
    @Expose
    private String sectionId;
    @SerializedName("isCompleted")
    @Expose
    private boolean isCompleted;
    @SerializedName("Questions")
    @Expose
    private List<HRAQuestions> questions = null;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public List<HRAQuestions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<HRAQuestions> questions) {
        this.questions = questions;
    }
}
