package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConversationIdResponse {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("Integrationid")
    @Expose
    private String integrationid;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;
    @SerializedName("SurveyVersion")
    @Expose
    private String surveyVersion;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("AnswerJson")
    @Expose
    private String answerJson;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
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
