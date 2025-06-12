package com.wyh.happyyousdk.model.request.hraSection.mentalWellness;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddGoodThingsRequest {
    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("ProgramID")
    @Expose
    private String programID;
    @SerializedName("GoodThings")
    @Expose
    private String goodThings;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;

    public AddGoodThingsRequest(String uuid, String programID, String goodThings, String isActive, String createdOn) {
        this.uuid = uuid;
        this.programID = programID;
        this.goodThings = goodThings;
        this.isActive = isActive;
        this.createdOn = createdOn;
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

    public String getGoodThings() {
        return goodThings;
    }

    public void setGoodThings(String goodThings) {
        this.goodThings = goodThings;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
