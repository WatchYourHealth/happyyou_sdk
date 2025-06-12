package com.wyh.happyyousdk.model.request.hraSection.goals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoalsHistoryRequest {
    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("ProgramID")
    @Expose
    private String programID;
    @SerializedName("CategoryID")
    @Expose
    private String categoryID;

    public GoalsHistoryRequest(String uuid, String programID, String categoryID) {
        this.uuid = uuid;
        this.programID = programID;
        this.categoryID = categoryID;
    }

    public GoalsHistoryRequest(String uuid, String categoryID) {
        this.uuid = uuid;
        this.categoryID = categoryID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProgramID() {
        return programID;
    }

    public void setProgramID(String programID) {
        this.programID = programID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
