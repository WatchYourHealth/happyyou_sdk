package com.wyh.happyyousdk.model.response.dass21;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchDass21QuestionsResponse {
    @SerializedName("qid")
    @Expose
    private int qid;
    @SerializedName("integrationID")
    @Expose
    private String integrationID;
    @SerializedName("surveyVersion")
    @Expose
    private String surveyVersion;
    @SerializedName("questions")
    @Expose
    private String questions;
    @SerializedName("logo")
    @Expose
    private String logo;

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

    public String getSurveyVersion() {
        return surveyVersion;
    }

    public void setSurveyVersion(String surveyVersion) {
        this.surveyVersion = surveyVersion;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
