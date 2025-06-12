package com.wyh.happyyousdk.model.request.ira;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerJsonRequest {

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
    private List<Object> answer = null;

    public AnswerJsonRequest(String questionId, String question, String questionType, List<String> options, List<Object> answer) {
        super();
        this.questionId = questionId;
        this.question = question;
        this.questionType = questionType;
        this.options = options;
        this.answer = answer;
    }

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

    public List<Object> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Object> answer) {
        this.answer = answer;
    }

}
