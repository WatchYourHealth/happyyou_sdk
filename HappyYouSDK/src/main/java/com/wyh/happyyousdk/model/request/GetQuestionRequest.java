package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetQuestionRequest {
    @SerializedName("integrationID")
    @Expose
    private String integrationID;
    @SerializedName("quizType")
    @Expose
    private String quizType;

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }


    public GetQuestionRequest(String integrationID,String quizType) {
        this.integrationID = integrationID;
        this.quizType = quizType;
    }


    public String getIntegrationID() {
        return integrationID;
    }

    public void setIntegrationID(String integrationID) {
        this.integrationID = integrationID;
    }
}
