package com.wyh.happyyousdk.model.request.hraSection.newHRA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateHRARequest {
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("integrationID")
    @Expose
    private String integrationID;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;
    @SerializedName("SurveyVersion")
    @Expose
    private String surveyVersion;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("AnswerJson")
    @Expose
    private String answerJson;

    public UpdateHRARequest(String customerID, String integrationID, String conversationId, String name, String surveyVersion, String status, String answerJson) {
        this.customerID = customerID;
        this.integrationID = integrationID;
        this.conversationId = conversationId;
        this.surveyVersion = surveyVersion;
        this.status = status;
        this.name = name;
        this.answerJson = answerJson;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getIntegrationID() {
        return integrationID;
    }

    public void setIntegrationID(String integrationID) {
        this.integrationID = integrationID;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
