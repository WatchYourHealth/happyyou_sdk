package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaveCorporateDetailsReq implements Serializable {

    @SerializedName("EmailId")
    @Expose
    private String EmailId;

    public String getAnswerJson() {
        return AnswerJson;
    }

    public void setAnswerJson(String answerJson) {
        AnswerJson = answerJson;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public SaveCorporateDetailsReq(String emailId, String answerJson) {
        EmailId = emailId;
        AnswerJson = answerJson;
    }

    @SerializedName("AnswerJson")
    @Expose
    private String AnswerJson;
}
