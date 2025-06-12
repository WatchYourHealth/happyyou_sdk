package com.wyh.happyyousdk.model.response.quiz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TriviaHistoryResponse implements Serializable {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("isPoolingReq")
    @Expose
    private Boolean isPoolingReq;
    @SerializedName("data")
    @Expose
    private List<TriviaHistoryData> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getIsPoolingReq() {
        return isPoolingReq;
    }

    public void setIsPoolingReq(Boolean isPoolingReq) {
        this.isPoolingReq = isPoolingReq;
    }

    public List<TriviaHistoryData> getData() {
        return data;
    }

    public void setData(List<TriviaHistoryData> data) {
        this.data = data;
    }
}
