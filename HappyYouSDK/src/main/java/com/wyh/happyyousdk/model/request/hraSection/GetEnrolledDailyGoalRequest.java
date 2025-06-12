package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetEnrolledDailyGoalRequest {

    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("ProgramID")
    @Expose
    private String programID;
    @SerializedName("GoalID")
    @Expose
    private String goalID;
    @SerializedName("TrackedOn")
    @Expose
    private String trackedOn;

    public GetEnrolledDailyGoalRequest(String uuid) {
        this.uuid = uuid;
    }

    public GetEnrolledDailyGoalRequest(String uuid, String programID, String goalID) {
        super();
        this.uuid = uuid;
        this.programID = programID;
        this.goalID = goalID;
    }

    public GetEnrolledDailyGoalRequest(String uuid, String programID, String goalID, String trackedOn) {
        super();
        this.uuid = uuid;
        this.programID = programID;
        this.goalID = goalID;
        this.trackedOn = trackedOn;
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

    public String getTrackOn() {
        return trackedOn;
    }

    public void setTrackOn(String trackOn) {
        this.trackedOn = trackOn;
    }

}