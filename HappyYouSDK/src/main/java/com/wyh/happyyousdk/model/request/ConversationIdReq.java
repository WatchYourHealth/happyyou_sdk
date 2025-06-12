package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConversationIdReq {
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("integrationID")
    @Expose
    private String integrationID;

    public ConversationIdReq(String integrationID) {
        this.integrationID = integrationID;
    }

    public ConversationIdReq(String customerID, String integrationID) {
        this.customerID = customerID;
        this.integrationID = integrationID;
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
}
