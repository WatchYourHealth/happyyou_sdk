package com.wyh.happyyousdk.model.response.dass21;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DassAnalysisData {
    @SerializedName("depressionScore")
    @Expose
    private int depressionScore;
    @SerializedName("depressionLevel")
    @Expose
    private String depressionLevel;
    @SerializedName("depressionDesc")
    @Expose
    private String depressionDesc;
    @SerializedName("stressScore")
    @Expose
    private int stressScore;
    @SerializedName("stressLevel")
    @Expose
    private String stressLevel;
    @SerializedName("stressDesc")
    @Expose
    private String stressDesc;
    @SerializedName("anxietyScore")
    @Expose
    private int anxietyScore;
    @SerializedName("anxietyLevel")
    @Expose
    private String anxietyLevel;
    @SerializedName("anxietyDesc")
    @Expose
    private String anxietyDesc;
    @SerializedName("integrationId")
    @Expose
    private String integrationId;
    @SerializedName("answersId")
    @Expose
    private int answersId;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    public int getDepressionScore() {
        return depressionScore;
    }

    public void setDepressionScore(int depressionScore) {
        this.depressionScore = depressionScore;
    }

    public String getDepressionLevel() {
        return depressionLevel;
    }

    public void setDepressionLevel(String depressionLevel) {
        this.depressionLevel = depressionLevel;
    }

    public int getStressScore() {
        return stressScore;
    }

    public void setStressScore(int stressScore) {
        this.stressScore = stressScore;
    }

    public String getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(String stressLevel) {
        this.stressLevel = stressLevel;
    }

    public int getAnxietyScore() {
        return anxietyScore;
    }

    public void setAnxietyScore(int anxietyScore) {
        this.anxietyScore = anxietyScore;
    }

    public String getAnxietyLevel() {
        return anxietyLevel;
    }

    public void setAnxietyLevel(String anxietyLevel) {
        this.anxietyLevel = anxietyLevel;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public int getAnswersId() {
        return answersId;
    }

    public void setAnswersId(int answersId) {
        this.answersId = answersId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDepressionDesc() {
        return depressionDesc;
    }

    public void setDepressionDesc(String depressionDesc) {
        this.depressionDesc = depressionDesc;
    }

    public String getStressDesc() {
        return stressDesc;
    }

    public void setStressDesc(String stressDesc) {
        this.stressDesc = stressDesc;
    }

    public String getAnxietyDesc() {
        return anxietyDesc;
    }

    public void setAnxietyDesc(String anxietyDesc) {
        this.anxietyDesc = anxietyDesc;
    }
}
