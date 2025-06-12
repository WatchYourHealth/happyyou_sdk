package com.wyh.happyyousdk.model.request.ira;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IRARequest {

    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("IntegrationID")
    @Expose
    private String integrationID;
    @SerializedName("HsDate")
    @Expose
    private String hsDate;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;

    public IRARequest(String customerID, String integrationID, String hsDate, String conversationId) {
        this.customerID = customerID;
        this.integrationID = integrationID;
        this.hsDate = hsDate;
        this.conversationId = conversationId;
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
