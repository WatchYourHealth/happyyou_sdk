package com.wyh.happyyousdk.model.request.ehr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthRecordIdRequest {

    @SerializedName("healthRecordTypeID")
    @Expose
    private Integer healthRecordTypeID;

    public HealthRecordIdRequest(Integer healthRecordTypeID) {
        super();
        this.healthRecordTypeID = healthRecordTypeID;
    }

    public Integer getHealthRecordTypeID() {
        return healthRecordTypeID;
    }

    public void setHealthRecordTypeID(Integer healthRecordTypeID) {
        this.healthRecordTypeID = healthRecordTypeID;
    }

}
