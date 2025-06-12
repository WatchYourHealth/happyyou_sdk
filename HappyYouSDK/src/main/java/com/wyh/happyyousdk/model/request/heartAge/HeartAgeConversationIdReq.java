package com.wyh.happyyousdk.model.request.heartAge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeartAgeConversationIdReq {
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("integrationID")
    @Expose
    private String integrationID;
    @SerializedName("surveyVersion")
    @Expose
    private String surveyVersion;

    public HeartAgeConversationIdReq(String integrationID,String surveyVersion) {
        this.integrationID = integrationID;
        this.surveyVersion=surveyVersion;
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
    public String getSurveyVersion() {
        return surveyVersion;
    }

    public void setSurveyVersion(String surveyVersion) {
        this.surveyVersion = surveyVersion;
    }
}
