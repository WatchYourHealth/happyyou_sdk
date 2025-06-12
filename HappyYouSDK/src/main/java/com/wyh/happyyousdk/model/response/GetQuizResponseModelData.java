package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetQuizResponseModelData {

    @SerializedName("isAssigned")
    @Expose
    public boolean isAssigned;
    @SerializedName("qid")
    @Expose
    public int qid;
    @SerializedName("integrationID")
    @Expose
    public String integrationID;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("questions")
    @Expose
    public String questions;
    @SerializedName("createdDate")
    @Expose
    public String createdDate;
    @SerializedName("modifiedDate")
    @Expose
    public String modifiedDate;
    @SerializedName("isActive")
    @Expose
    public boolean isActive;




    public GetQuizResponseModelData(int qid, String integrationID, String name, String questions, String createdDate, String modifiedDate, boolean isActive) {
        this.qid = qid;
        this.integrationID = integrationID;
        this.name = name;
        this.questions = questions;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.isActive = isActive;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getIntegrationID() {
        return integrationID;
    }

    public void setIntegrationID(String integrationID) {
        this.integrationID = integrationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
