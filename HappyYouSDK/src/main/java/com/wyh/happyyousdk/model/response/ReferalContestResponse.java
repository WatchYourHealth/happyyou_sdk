package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferalContestResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    private Integer data;


    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Integer getData() {
        return data;
    }
}
