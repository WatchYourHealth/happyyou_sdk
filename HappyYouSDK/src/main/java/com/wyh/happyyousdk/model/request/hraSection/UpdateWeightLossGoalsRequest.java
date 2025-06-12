package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateWeightLossGoalsRequest {
    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("ProgramID")
    @Expose
    private String programID;
    @SerializedName("GoalID")
    @Expose
    private String goalID;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Tags")
    @Expose
    private List<Tags> tags = null;
    @SerializedName("Status")
    @Expose
    private String status;

    public UpdateWeightLossGoalsRequest(String uuid, String programID, String goalID, String date, List<Tags> tags, String status) {
        this.uuid = uuid;
        this.programID = programID;
        this.goalID = goalID;
        this.date = date;
        this.tags = tags;
        this.status = status;
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

    public String getGoalID() {
        return goalID;
    }

    public void setGoalID(String goalID) {
        this.goalID = goalID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
