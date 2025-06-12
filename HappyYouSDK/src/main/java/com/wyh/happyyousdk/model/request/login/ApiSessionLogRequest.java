package com.wyh.happyyousdk.model.request.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiSessionLogRequest implements Serializable {
    @SerializedName("StartTime")
    @Expose
    private String StartTime;
    @SerializedName("EndTime")
    @Expose
    private String EndTime;

    public ApiSessionLogRequest(String startTime, String endTime) {
        StartTime = startTime;
        EndTime = endTime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}
