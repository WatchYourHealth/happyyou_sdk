package com.wyh.happyyousdk.model.response.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDynamicGoalData {
    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("ProgramID")
    @Expose
    private String programID;
    @SerializedName("TrackedOn")
    @Expose
    private String trackedOn;
    @SerializedName("QuestionID")
    @Expose
    private String questionID;
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("QuestionAns")
    @Expose
    private String questionAns;
    @SerializedName("QuestionType")
    @Expose
    private String questionType;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("ImageURL")
    @Expose
    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    public String getTrackedOn() {
        return trackedOn;
    }

    public void setTrackedOn(String trackedOn) {
        this.trackedOn = trackedOn;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionAns() {
        return questionAns;
    }

    public void setQuestionAns(String questionAns) {
        this.questionAns = questionAns;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
