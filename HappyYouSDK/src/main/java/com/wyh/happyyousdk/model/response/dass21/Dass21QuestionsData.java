package com.wyh.happyyousdk.model.response.dass21;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dass21QuestionsData {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("questions")
    @Expose
    private List<Dass21Question> questions = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Dass21Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Dass21Question> questions) {
        this.questions = questions;
    }
}
