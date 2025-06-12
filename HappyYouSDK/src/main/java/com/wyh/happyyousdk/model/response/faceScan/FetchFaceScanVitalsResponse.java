package com.wyh.happyyousdk.model.response.faceScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.FeedbackResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;

public class FetchFaceScanVitalsResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private FetchFaceScanVitalsData data;




    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public FetchFaceScanVitalsData getData() {
        return data;
    }

    public void setData(FetchFaceScanVitalsData data) {
        this.data = data;
    }


}
