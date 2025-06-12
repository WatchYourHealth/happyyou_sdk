package com.wyh.happyyousdk.model.request.hra;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetHraRequest {
    @SerializedName("IntegrationID")
    @Expose
    private String integrationID;
    @SerializedName("HsDate")
    @Expose
    private String hsDate;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;

    public GetHraRequest(String integrationID, String hsDate, String conversationId) {
        this.integrationID = integrationID;
        this.hsDate = hsDate;
        this.conversationId = conversationId;
    }

    public String getIntegrationID() {
        return integrationID;
    }

    public void setIntegrationID(String integrationID) {
        this.integrationID = integrationID;
    }

    public String getHsDate() {
        return hsDate;
    }

    public void setHsDate(String hsDate) {
        this.hsDate = hsDate;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
