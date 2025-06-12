package com.wyh.happyyousdk.model.response.faceScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchFaceScanVitalsData {
    @SerializedName("heartRate")
    @Expose
    private int heartRate;
    @SerializedName("respiratoryRate")
    @Expose
    private int respiratoryRate;
    @SerializedName("oxygen")
    @Expose
    private int oxygen;
    @SerializedName("stressLevel")
    @Expose
    private String stressStatus;
    @SerializedName("bloodPressureStatus")
    @Expose
    private String bloodPressureStatus;
    @SerializedName("systolic")
    @Expose
    private int systolic;
    @SerializedName("diastolic")
    @Expose
    private int diastolic;
    @SerializedName("hrv")
    @Expose
    private int hrv;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("modifiedOn")
    @Expose
    private String modifiedOn;
    @SerializedName("modifiedBy")
    @Expose
    private Object modifiedBy;
    @SerializedName("isDeleted")
    @Expose
    private boolean isDeleted;

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(int respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public int getOxygen() {
        return oxygen;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = oxygen;
    }

    public String getStressStatus() {
        return stressStatus;
    }

    public void setStressStatus(String stressStatus) {
        this.stressStatus = stressStatus;
    }

    public String getBloodPressureStatus() {
        return bloodPressureStatus;
    }

    public void setBloodPressureStatus(String bloodPressureStatus) {
        this.bloodPressureStatus = bloodPressureStatus;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public int getHrv() {
        return hrv;
    }

    public void setHrv(int hrv) {
        this.hrv = hrv;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Object getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
