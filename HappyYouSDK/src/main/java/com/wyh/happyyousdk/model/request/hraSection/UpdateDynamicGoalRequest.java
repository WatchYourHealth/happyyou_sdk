package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateDynamicGoalRequest {
    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("ProgramID")
    @Expose
    private String programID;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Questions")
    @Expose
    private List<Question> questions = null;
    @SerializedName("Status")
    @Expose
    private String status;

    public UpdateDynamicGoalRequest(String uuid, String programID, String date, List<Question> questions, String status) {
        this.uuid = uuid;
        this.programID = programID;
        this.date = date;
        this.questions = questions;
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProgramID() {
        return programID;
    }

    public void setProgramID(String programID) {
        this.programID = programID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
