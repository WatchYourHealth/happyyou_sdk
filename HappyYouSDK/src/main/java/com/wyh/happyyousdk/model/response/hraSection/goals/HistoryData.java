package com.wyh.happyyousdk.model.response.hraSection.goals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryData {
    @SerializedName("DateOfEnroll")
    @Expose
    private String dateOfEnroll;
    @SerializedName("DateOfExit")
    @Expose
    private String dateOfExit;
    @SerializedName("ValueAtEnroll")
    @Expose
    private String valueAtEnroll;
    @SerializedName("ValueAtExit")
    @Expose
    private String valueAtExit;
    @SerializedName("Percentage")
    @Expose
    private String percentage;
    @SerializedName("Status")
    @Expose
    private String status;

    public String getDateOfEnroll() {
        return dateOfEnroll;
    }

    public void setDateOfEnroll(String dateOfEnroll) {
        this.dateOfEnroll = dateOfEnroll;
    }

    public String getDateOfExit() {
        return dateOfExit;
    }

    public void setDateOfExit(String dateOfExit) {
        this.dateOfExit = dateOfExit;
    }

    public String getValueAtEnroll() {
        return valueAtEnroll;
    }

    public void setValueAtEnroll(String valueAtEnroll) {
        this.valueAtEnroll = valueAtEnroll;
    }

    public String getValueAtExit() {
        return valueAtExit;
    }

    public void setValueAtExit(String valueAtExit) {
        this.valueAtExit = valueAtExit;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
