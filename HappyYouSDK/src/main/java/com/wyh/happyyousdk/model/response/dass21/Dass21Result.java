package com.wyh.happyyousdk.model.response.dass21;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dass21Result {
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("Score")
    @Expose
    private String score = "";

    public Dass21Result(String question, String score) {
        this.question = question;
        this.score = score;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
