package com.wyh.happyyousdk.model.request.quizathon;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaveFeedbackAnswerRequest implements Serializable {
    @SerializedName("answerjson")
    String AnswerJson;
    @SerializedName("status")
    String Status;
    @SerializedName("feedbackId")
    String RetakeId;


    public SaveFeedbackAnswerRequest(String answerJson, String status, String retakeId) {
        AnswerJson = answerJson;
        Status = status;
        RetakeId = retakeId;
    }

    public String getAnswerJson() {
        return AnswerJson;
    }

    public void setAnswerJson(String answerJson) {
        AnswerJson = answerJson;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRetakeId() {
        return RetakeId;
    }

    public void setRetakeId(String retakeId) {
        RetakeId = retakeId;
    }
}
