package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveProgram {
    @SerializedName("activeProgram")
    @Expose
    private Object activeProgram;
    @SerializedName("goalDetail")
    @Expose
    private Object goalDetail;

    public Object getActiveProgram() {
        return activeProgram;
    }

    public void setActiveProgram(Object activeProgram) {
        this.activeProgram = activeProgram;
    }

    public Object getGoalDetail() {
        return goalDetail;
    }

    public void setGoalDetail(Object goalDetail) {
        this.goalDetail = goalDetail;
    }
}
