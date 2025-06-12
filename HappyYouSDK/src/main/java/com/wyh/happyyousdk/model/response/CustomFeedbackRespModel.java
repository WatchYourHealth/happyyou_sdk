package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomFeedbackRespModel {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("ccmmId")
    @Expose
    private int ccmmId;
    @SerializedName("questionJSON")
    @Expose
    private String questionJSON;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCcmmId() {
        return ccmmId;
    }

    public void setCcmmId(int ccmmId) {
        this.ccmmId = ccmmId;
    }

    public String getQuestionJSON() {
        return questionJSON;
    }

    public void setQuestionJSON(String questionJSON) {
        this.questionJSON = questionJSON;
    }
}
