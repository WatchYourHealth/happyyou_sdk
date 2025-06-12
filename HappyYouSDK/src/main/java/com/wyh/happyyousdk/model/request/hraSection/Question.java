package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {
    @SerializedName("QuestionID")
    @Expose
    private String questionID;
    @SerializedName("QuestionAns")
    @Expose
    private String questionAns;

    public Question(String questionID, String questionAns) {
        this.questionID = questionID;
        this.questionAns = questionAns;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestionAns() {
        return questionAns;
    }

    public void setQuestionAns(String questionAns) {
        this.questionAns = questionAns;
    }
}
