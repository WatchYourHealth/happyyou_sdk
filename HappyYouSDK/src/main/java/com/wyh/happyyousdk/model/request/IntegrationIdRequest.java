package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IntegrationIdRequest {
    @SerializedName("integrationId")
    @Expose
    private String integrationId;

    public IntegrationIdRequest(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getConversationId() {
        return integrationId;
    }

    public void setConversationId(String integrationId) {
        this.integrationId = integrationId;
    }
}
