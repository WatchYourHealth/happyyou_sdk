package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateEnrolledDailyGoalRequest {

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
    @SerializedName("TagName")
    @Expose
    private String tagName;
    @SerializedName("TagValue")
    @Expose
    private String tagValue;
    @SerializedName("Status")
    @Expose
    private String status;

    public UpdateEnrolledDailyGoalRequest(String uuid, String programID, String goalID, String date, String tagName, String tagValue,String status) {
        super();
        this.uuid = uuid;
        this.programID = programID;
        this.goalID = goalID;
        this.date = date;
        this.tagName = tagName;
        this.tagValue = tagValue;
        this.status=status;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

}