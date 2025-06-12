package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaveRegistrationAnswer implements Serializable {
    @SerializedName("RegistrationID")
    @Expose
    String RegistrationID;
    @SerializedName("AnswerJson")
    @Expose
    String AnswerJson;

    public SaveRegistrationAnswer(String registrationID, String answerJson) {
        RegistrationID = registrationID;
        AnswerJson = answerJson;
    }

    public String getRegistrationID() {
        return RegistrationID;
    }

    public void setRegistrationID(String registrationID) {
        RegistrationID = registrationID;
    }

    public String getAnswerJson() {
        return AnswerJson;
    }

    public void setAnswerJson(String answerJson) {
        AnswerJson = answerJson;
    }
}
