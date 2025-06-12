package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HappinessIndexNewScore {
    @SerializedName("Quest")
    @Expose
    private String quest;
    @SerializedName("score")
    @Expose
    private int score;

    public String getQuest() {
        return quest;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
