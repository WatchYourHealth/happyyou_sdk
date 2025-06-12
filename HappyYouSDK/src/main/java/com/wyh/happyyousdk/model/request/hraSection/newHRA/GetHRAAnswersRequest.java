package com.wyh.happyyousdk.model.request.hraSection.newHRA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetHRAAnswersRequest {
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("integrationID")
    @Expose
    private String integrationID;

    public GetHRAAnswersRequest(String customerID, String integrationID) {
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
