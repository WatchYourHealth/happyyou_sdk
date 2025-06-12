package com.wyh.happyyousdk.model.request.riddle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiddleSaveRequest {
    @SerializedName("riddleId")
    @Expose
    private int id;
    @SerializedName("riddleAnswer")
    @Expose
    private String riddleAnswer;

    public RiddleSaveRequest(int id, String userAnswer) {
        this.id = id;
        this.riddleAnswer = userAnswer;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserAnswer() {
        return riddleAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.riddleAnswer = userAnswer;
    }
}
