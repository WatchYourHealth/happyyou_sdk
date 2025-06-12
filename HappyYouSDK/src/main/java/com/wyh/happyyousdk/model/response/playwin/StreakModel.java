package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StreakModel implements Serializable {

    @SerializedName("quizDate")
    @Expose
    String quizDate;
    @SerializedName("isQuizTaken")
    @Expose
    boolean isQuizTaken;
    @SerializedName("quizGroup")
    @Expose
    String quizGroup;
    @SerializedName("quizId")
    @Expose
    String quizId;

    public String getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(String quizDate) {
        this.quizDate = quizDate;
    }

    public boolean isQuizTaken() {
        return isQuizTaken;
    }

    public void setQuizTaken(boolean quizTaken) {
        isQuizTaken = quizTaken;
    }

    public String getQuizGroup() {
        return quizGroup;
    }

    public void setQuizGroup(String quizGroup) {
        this.quizGroup = quizGroup;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
