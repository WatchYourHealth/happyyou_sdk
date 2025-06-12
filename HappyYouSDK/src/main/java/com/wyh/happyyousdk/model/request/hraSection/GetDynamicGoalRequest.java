package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDynamicGoalRequest {
    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("ProgramID")
    @Expose
    private String programID;
    @SerializedName("TrackedOn")
    @Expose
    private String trackedOn;

    public GetDynamicGoalRequest(String uuid, String programID, String trackedOn) {
        this.uuid = uuid;
        this.programID = programID;
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

    public String getTrackedOn() {
        return trackedOn;
    }

    public void setTrackedOn(String trackedOn) {
        this.trackedOn = trackedOn;
    }
}
