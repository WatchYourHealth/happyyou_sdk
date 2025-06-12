package com.wyh.happyyousdk.model.request.ira;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConversationIdRequest {

    @SerializedName("IntegrationID")
    @Expose
    private String integrationID;
    @SerializedName("HsDate")
    @Expose
    private String hsDate;

    public ConversationIdRequest(String integrationID, String hsDate) {
        super();
        this.integrationID = integrationID;
        this.hsDate = hsDate;
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

}
