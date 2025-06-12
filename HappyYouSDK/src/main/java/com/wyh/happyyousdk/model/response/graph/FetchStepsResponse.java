package com.wyh.happyyousdk.model.response.graph;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.DataPoint;
import com.wyh.happyyousdk.model.response.DateRange;

import java.util.List;

public class FetchStepsResponse {

    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("tab")
    @Expose
    private String tab;
    @SerializedName("dateRange")
    @Expose
    private DateRange dateRange;
    @SerializedName("dataPoints")
    @Expose
    private List<DataPoint> dataPoints = null;
    @SerializedName("goal")
    @Expose
    private Integer goal;
    private DataPoint lastdataPoints;
    @SerializedName("currentValue")
    @Expose
    private double currentValue;

    public FetchStepsResponse(String uuid, String tab, DateRange dateRange, List<DataPoint> dataPoints) {
        this.uuid = uuid;
        this.tab = tab;
        this.dateRange = dateRange;
        this.dataPoints = dataPoints;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public DataPoint getLastdataPoints() {
        return lastdataPoints;
    }

    public void setLastdataPoints(DataPoint lastdataPoints) {
        this.lastdataPoints = lastdataPoints;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

}
