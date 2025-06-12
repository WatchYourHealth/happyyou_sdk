package com.wyh.happyyousdk.model.response.hraSection.newHRA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HRAQuestions {
    @SerializedName("QuestionId")
    @Expose
    private String questionId;
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("Options")
    @Expose
    private List<String> options = null;
    @SerializedName("Answer")
    @Expose
    private List<String> answer = null;

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

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}
