package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddWeightRequest {
    @SerializedName("UserUUID")
    @Expose
    private String userUUID;
    @SerializedName("UserWeight")
    @Expose
    private String userWeight;
    @SerializedName("RecordDate")
    @Expose
    private String recordDate;
    @SerializedName("RecordTime")
    @Expose
    private String recordTime;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("BellyInches")
    @Expose
    private String bellyInches;

    public AddWeightRequest(String userUUID, String userWeight, String recordDate, String recordTime, String createdBy) {
        this.userUUID = userUUID;
        this.userWeight = userWeight;
        this.recordDate = recordDate;
        this.recordTime = recordTime;
        this.createdBy = createdBy;
    }

    public AddWeightRequest(String userUUID, String recordDate, String bellyInches) {
        this.userUUID = userUUID;
        this.recordDate = recordDate;
        this.bellyInches = bellyInches;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(String userWeight) {
        this.userWeight = userWeight;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
