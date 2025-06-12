package com.wyh.happyyousdk.model.request.dass21;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveDass21Request {
    @SerializedName("QuestionId")
    @Expose
    private int questionId;
    @SerializedName("integrationid")
    @Expose
    private String integrationid;
    @SerializedName("surveyVersion")
    @Expose
    private String surveyVersion;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("answerJson")
    @Expose
    private String answerJson;

    public SaveDass21Request(int questionId, String integrationid, String surveyVersion, String status, String answerJson) {
        this.questionId = questionId;
        this.integrationid = integrationid;
        this.surveyVersion = surveyVersion;
        this.status = status;
        this.answerJson = answerJson;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getIntegrationid() {
        return integrationid;
    }

    public void setIntegrationid(String integrationid) {
        this.integrationid = integrationid;
    }

    public String getSurveyVersion() {
        return surveyVersion;
    }

    public void setSurveyVersion(String surveyVersion) {
        this.surveyVersion = surveyVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
    }
}
