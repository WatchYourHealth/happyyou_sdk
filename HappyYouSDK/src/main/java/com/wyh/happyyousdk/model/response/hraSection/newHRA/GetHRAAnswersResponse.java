package com.wyh.happyyousdk.model.response.hraSection.newHRA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetHRAAnswersResponse {

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

    public GetHRAAnswersResponse(String integrationid, String conversationId, String surveyVersion, String status, String answerJson, String createdDate, String modifiedDate) {
        super();
        this.integrationid = integrationid;
        this.conversationId = conversationId;
        this.surveyVersion = surveyVersion;
        this.status = status;
        this.answerJson = answerJson;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

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