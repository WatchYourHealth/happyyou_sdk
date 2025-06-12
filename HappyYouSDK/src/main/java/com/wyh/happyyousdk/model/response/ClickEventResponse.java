package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClickEventResponse {

    @SerializedName("msg")
    @Expose
    private String msg;


    @SerializedName("success")
    @Expose
    private String success;


    public String getMsg() {
        return msg;
    }

    public String getSuccess() {
        return success;
    }
}
