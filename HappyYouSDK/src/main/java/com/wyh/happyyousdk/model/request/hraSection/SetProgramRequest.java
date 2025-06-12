package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetProgramRequest {

    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("GoalID")
    @Expose
    private String goalID;
    @SerializedName("ProgramID")
    @Expose
    private String programID;
    @SerializedName("DateOfEnroll")
    @Expose
    private String dateOfEnroll;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("ExitDate")
    @Expose
    private String exitDate;
    @SerializedName("ForDays")
    @Expose
    private int forDays;
    @SerializedName("ValueAtEnroll")
    @Expose
    private String valueAtEnroll;

    public SetProgramRequest() {
    }

    public SetProgramRequest(String uuid, String goalID, String programID, String dateOfEnroll, String status, String startDate, String endDate, String exitDate, int forDays, String valueAtEnroll) {
        super();
        this.uuid = uuid;
        this.goalID = goalID;
        this.programID = programID;
        this.dateOfEnroll = dateOfEnroll;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.exitDate = exitDate;
        this.forDays = forDays;
        this.valueAtEnroll = valueAtEnroll;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGoalID() {
        return goalID;
    }

    public void setGoalID(String goalID) {
        this.goalID = goalID;
    }

    public String getProgramID() {
        return programID;
    }

    public void setProgramID(String programID) {
        this.programID = programID;
    }

    public String getDateOfEnroll() {
        return dateOfEnroll;
    }

    public void setDateOfEnroll(String dateOfEnroll) {
        this.dateOfEnroll = dateOfEnroll;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExitDate() {
        return exitDate;
    }

    public void setExitDate(String exitDate) {
        this.exitDate = exitDate;
    }

    public int getForDays() {
        return forDays;
    }

    public void setExitDate(int forDays) {
        this.forDays = forDays;
    }

    public String getValueAtEnroll() {
        return valueAtEnroll;
    }

    public void setValueAtEnroll(String valueAtEnroll) {
        this.valueAtEnroll = valueAtEnroll;
    }

}
