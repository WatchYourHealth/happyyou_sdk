package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serial;
import java.io.Serializable;

public class RetakeDatamodel implements Serializable {
    @SerializedName("quizId")
    @Expose
    String quizId;
    @SerializedName("questionJson")
    @Expose
    String questionJson;

    @SerializedName("retakeId")
    @Expose
    String retakeId;
    @SerializedName("message")
    @Expose
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuestionJson() {
        return questionJson;
    }

    public void setQuestionJson(String questionJson) {
        this.questionJson = questionJson;
    }

    public String getRetakeId() {
        return retakeId;
    }

    public void setRetakeId(String retakeId) {
        this.retakeId = retakeId;
    }
}
