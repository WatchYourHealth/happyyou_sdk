package com.wyh.happyyousdk.model.response.hra;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetHraAnswersResponse {

    @SerializedName("integrationid")
    @Expose
    private String integrationid;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;
    @SerializedName("surveyVersion")
    @Expose
    private String surveyVersion;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("answerJson")
    @Expose
    private String answerJson;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;

    public String getIntegrationid() {
        return integrationid;
    }

    public void setIntegrationid(String integrationid) {
        this.integrationid = integrationid;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
