package com.wyh.happyyousdk.model.response.ira;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IRAResponse {

    @SerializedName("Answer")
    @Expose
    private List<String> answer = null;
    @SerializedName("Options")
    @Expose
    private List<String> options = null;
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("QuestionId")
    @Expose
    private String questionId;
    @SerializedName("QuestionType")
    @Expose
    private String questionType;

    /**
     * No args constructor for use in serialization
     *
     */
    public IRAResponse() {
    }

    /**
     *
     * @param questionId
     * @param answer
     * @param question
     * @param options
     * @param questionType
     */
    public IRAResponse(List<String> answer, List<String> options, String question, String questionId, String questionType) {
        super();
        this.answer = answer;
        this.options = options;
        this.question = question;
        this.questionId = questionId;
        this.questionType = questionType;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

}