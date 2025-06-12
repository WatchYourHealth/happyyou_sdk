package com.wyh.happyyousdk.model.response.dass21;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dass21AnswerJson {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("results")
    @Expose
    private List<Dass21Result> results = null;
    @SerializedName("userScore")
    @Expose
    private int userScore;
    @SerializedName("attemptedQuestions")
    @Expose
    private int attemptedQuestions;
    @SerializedName("totalQuestions")
    @Expose
    private int totalQuestions;

    public Dass21AnswerJson(String title, List<Dass21Result> results, int userScore, int attemptedQuestions, int totalQuestions) {
        this.title = title;
        this.results = results;
        this.userScore = userScore;
        this.attemptedQuestions = attemptedQuestions;
        this.totalQuestions = totalQuestions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Dass21Result> getResults() {
        return results;
    }

    public void setResults(List<Dass21Result> results) {
        this.results = results;
    }

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    public int getAttemptedQuestions() {
        return attemptedQuestions;
    }

    public void setAttemptedQuestions(int attemptedQuestions) {
        this.attemptedQuestions = attemptedQuestions;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
