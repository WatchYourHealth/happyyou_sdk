package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDailyProgramRequest {

    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("ProgramID")
    @Expose
    private String programID;
    @SerializedName("TrackedOn")
    @Expose
    private String trackedOn;
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
    public GetDailyProgramRequest() {
    }

    /**
     *
     * @param uuid
     * @param programID
     * @param trackedOn
     * @param status
     */
    public GetDailyProgramRequest(String uuid, String programID, String trackedOn, String status) {
        super();
        this.uuid = uuid;
        this.programID = programID;
        this.trackedOn = trackedOn;
        this.status = status;
    }

    public GetDailyProgramRequest(String uuid, String programID, String trackedOn, String status, String goalId) {
        this.uuid = uuid;
        this.programID = programID;
        this.trackedOn = trackedOn;
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

    public String getTrackedOn() {
        return trackedOn;
    }

    public void setTrackedOn(String trackedOn) {
        this.trackedOn = trackedOn;
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
