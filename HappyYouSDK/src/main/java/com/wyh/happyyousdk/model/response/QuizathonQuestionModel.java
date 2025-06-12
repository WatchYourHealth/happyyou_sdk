package com.wyh.happyyousdk.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QuizathonQuestionModel {

    @JsonProperty(required = false)
    @SerializedName("questionId")
    @Expose
    public Integer questionId;
    @SerializedName("question")
    @Expose
    public String question;
    @SerializedName("questionType")
    @Expose
    public String questionType;
    @SerializedName("options")
    @Expose
    public List<String> options;
    @SerializedName("answer")
    @Expose
    public String answer;
    @SerializedName("answers")
    @Expose
    public List<String> answers =new ArrayList<>();
    @SerializedName("userAns")
    @Expose
    public Object userAns;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("explanation")
    @Expose
    public String explanation;

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public QuizathonQuestionModel(Integer questionId, String question, List<String> options, String answer, Object userAns, String image, String explanation) {
        this.questionId = questionId;
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.userAns = userAns;
        this.image = image;
        this.explanation = explanation;
    }
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Object getUserAns() {
        return userAns;
    }

    public void setUserAns(Object userAns) {
        this.userAns = userAns;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

}
