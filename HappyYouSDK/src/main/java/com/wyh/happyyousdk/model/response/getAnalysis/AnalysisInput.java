package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnalysisInput {
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;
    @SerializedName("integrationId")
    @Expose
    private String integrationId;
    @SerializedName("hsDate")
    @Expose
    private Object hsDate;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public Object getHsDate() {
        return hsDate;
    }

    public void setHsDate(Object hsDate) {
        this.hsDate = hsDate;
    }
}
