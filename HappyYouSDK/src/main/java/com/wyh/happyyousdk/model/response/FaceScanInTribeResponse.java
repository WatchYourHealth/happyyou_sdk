package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.http.Body;

public class FaceScanInTribeResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }
}
