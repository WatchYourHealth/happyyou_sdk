package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.playwin.QuizathonModel;

public class GetQuizathonDetailsResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    private QuizathonModel data;


    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public QuizathonModel getData() {
        return data;
    }
}
