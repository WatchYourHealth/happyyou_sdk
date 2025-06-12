package com.wyh.happyyousdk.model.request.dass21;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchDass21QuestionsRequest {
    @SerializedName("integrationID")
    @Expose
    private String integrationID;
    @SerializedName("toWhome")
    @Expose
    private String toWhome;

    public FetchDass21QuestionsRequest(String integrationID, String toWhome) {
        this.integrationID = integrationID;
        this.toWhome = toWhome;
    }

    public String getIntegrationID() {
        return integrationID;
    }

    public void setIntegrationID(String integrationID) {
        this.integrationID = integrationID;
    }

    public String getToWhome() {
        return toWhome;
    }

    public void setToWhome(String toWhome) {
        this.toWhome = toWhome;
    }
}
