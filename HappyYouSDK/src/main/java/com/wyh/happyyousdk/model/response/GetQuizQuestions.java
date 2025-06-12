package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetQuizQuestions {
    @SerializedName("category")
    @Expose
    public String category;
    @SerializedName("categoryName")
    @Expose
    public String categoryName;
    @SerializedName("questions")
    @Expose
    public List<QuestionModel> questions;

    public GetQuizQuestions(String category, String categoryName, List<QuestionModel> questions) {
        this.category = category;
        this.categoryName = categoryName;
        this.questions = questions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<QuestionModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionModel> questions) {
        this.questions = questions;
    }
}
