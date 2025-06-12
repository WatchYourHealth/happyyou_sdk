package com.wyh.happyyousdk.model.request.faceScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFaceScanVitalsRequest {
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

    @SerializedName("scanId")
    @Expose
    private String scanId;

    public AddFaceScanVitalsRequest(int heartRate, int respiratoryRate, int oxygen, String stressStatus, String bloodPressureStatus, int systolic, int diastolic, int hrv, String scanID) {
        this.heartRate = heartRate;
        this.respiratoryRate = respiratoryRate;
        this.oxygen = oxygen;
        this.stressStatus = stressStatus;
        this.bloodPressureStatus = bloodPressureStatus;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.hrv = hrv;
        this.scanId = scanID;
    }

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
}
