package com.wyh.happyyousdk.model.response.hraSection.mentalHealth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MentalHealthData {
    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("TrackedOn")
    @Expose
    private String trackedOn;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("ProgramID")
    @Expose
    private Integer programID;
    @SerializedName("MHCID")
    @Expose
    private Integer mhcid;
    @SerializedName("MHCValue")
    @Expose
    private String mHCValue;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("TagName")
    @Expose
    private String tagName;
    @SerializedName("TagValue")
    @Expose
    private String tagValue;
    @SerializedName("IsActive")
    @Expose
    private String isActive;

    public MentalHealthData(String uuid, Integer programID, String status) {
        this.uuid = uuid;
        this.programID = programID;
        this.status = status;
    }

    public MentalHealthData(String uuid, String date, String programID, String mhcid, String mHCValue, String status, String isActive) {
        this.uuid = uuid;
        this.date = date;
        this.programID = Integer.valueOf(programID);
        this.mhcid = Integer.valueOf(mhcid);
        this.mHCValue = mHCValue;
        this.status = status;
        this.isActive = isActive;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTrackedOn() {
        return trackedOn;
    }

    public void setTrackedOn(String trackedOn) {
        this.trackedOn = trackedOn;
    }

    public Integer getProgramID() {
        return programID;
    }

    public void setProgramID(Integer programID) {
        this.programID = programID;
    }

    public Integer getMhcid() {
        return mhcid;
    }

    public void setMhcid(Integer mhcid) {
        this.mhcid = mhcid;
    }

    public String getMHCValue() {
        return mHCValue;
    }

    public void setMHCValue(String mHCValue) {
        this.mHCValue = mHCValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
