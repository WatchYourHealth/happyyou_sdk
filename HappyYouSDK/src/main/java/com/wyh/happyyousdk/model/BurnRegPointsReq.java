package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BurnRegPointsReq implements Serializable {

    @SerializedName("QuizId")
    @Expose
    private Integer QuizId;
    @SerializedName("Points")
    @Expose
    private Integer Points;

    public String getBurnType() {
        return burnType;
    }

    public void setBurnType(String burnType) {
        this.burnType = burnType;
    }

    @SerializedName("burnType")
    @Expose
    private String burnType;

    public BurnRegPointsReq(Integer quizId, Integer points,String BurnType) {
        QuizId = quizId;
        Points = points;
        burnType = BurnType;
    }

    public Integer getQuizId() {
        return QuizId;
    }

    public void setQuizId(Integer quizId) {
        QuizId = quizId;
    }

    public Integer getPoints() {
        return Points;
    }

    public void setPoints(Integer points) {
        Points = points;
    }


}
