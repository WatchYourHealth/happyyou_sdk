package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateDailyProgramRequest {

    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("ProgramID")
    @Expose
    private String programID;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("TagName")
    @Expose
    private String tagName;
    @SerializedName("TagValue")
    @Expose
    private String tagValue;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("ModifiedOn")
    @Expose
    private String modifiedOn;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("GoalId")
    @Expose
    private String goalId;

    /**
     * No args constructor for use in serialization
     *
     */
    public UpdateDailyProgramRequest() {
    }
    public UpdateDailyProgramRequest(String uuid, String programID, String date, String tagName, String tagValue, String isActive, String modifiedOn, String status) {
        this.uuid = uuid;
        this.programID = programID;
        this.date = date;
        this.tagName = tagName;
        this.tagValue = tagValue;
        this.isActive = isActive;
        this.modifiedOn = modifiedOn;
        this.status = status;
    }

    public UpdateDailyProgramRequest(String uuid, String programID, String date, String tagName, String tagValue, String isActive, String modifiedOn, String status, String goalId) {
        this.uuid = uuid;
        this.programID = programID;
        this.date = date;
        this.tagName = tagName;
        this.tagValue = tagValue;
        this.isActive = isActive;
        this.modifiedOn = modifiedOn;
        this.status = status;
        this.goalId = goalId;
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

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }
}
