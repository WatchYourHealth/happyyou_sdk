package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPoint {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("point")
    @Expose
    private Float point;
    @SerializedName("point1")
    @Expose
    private Float point1;

    public Float getPoint1() {
        return point1;
    }

    public void setPoint1(Float point1) {
        this.point1 = point1;
    }

    public DataPoint(String date, Float point) {
        this.date = date;
        this.point = point;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

}
