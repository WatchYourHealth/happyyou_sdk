package com.wyh.happyyousdk.model.request.heartAge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchHeartAgeQuestionsRequest {

    @SerializedName("integrationid")
    @Expose
    private String integrationid;
    @SerializedName("version")
    @Expose
    private String version;

    public FetchHeartAgeQuestionsRequest(String integrationid, String version) {
        super();
        this.integrationid = integrationid;
        this.version = version;
    }

    public String getIntegrationid() {
        return integrationid;
    }

    public void setIntegrationid(String integrationid) {
        this.integrationid = integrationid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
