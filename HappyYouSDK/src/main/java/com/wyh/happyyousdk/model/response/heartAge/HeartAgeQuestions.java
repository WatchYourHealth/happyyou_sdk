package com.wyh.happyyousdk.model.response.heartAge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HeartAgeQuestions {
    @SerializedName("QuestionId")
    @Expose
    private String questionId;
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("QuestionType")
    @Expose
    private String questionType;
    @SerializedName("Options")
    @Expose
    private List<String> options = null;
    @SerializedName("Answer")
    @Expose
    private String answer = null;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
