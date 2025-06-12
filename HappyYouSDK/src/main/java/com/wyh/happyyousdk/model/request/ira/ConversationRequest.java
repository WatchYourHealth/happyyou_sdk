package com.wyh.happyyousdk.model.request.ira;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConversationRequest {

    @SerializedName("IntegrationID")
    @Expose
    private String integrationID;

    @SerializedName("conversationId")
    @Expose
    private String conversationId;

    public ConversationRequest(String integrationID, String conversationId) {
        this.integrationID = integrationID;
        this.conversationId = conversationId;
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
}
