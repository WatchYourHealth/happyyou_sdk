package com.wyh.happyyousdk.model.request.heartAge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetHeartAgeAnalysisRequest {
    @SerializedName("integrationID")
    @Expose
    private String integrationID;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;

    public GetHeartAgeAnalysisRequest(String integrationID, String conversationId) {
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
