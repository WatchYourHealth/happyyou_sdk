package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CommonResponse implements Serializable {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("isPoolingReq")
    @Expose
    private Boolean isPoolingReq;

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

    public Boolean getPoolingReq() {
        return isPoolingReq;
    }

    public void setPoolingReq(Boolean poolingReq) {
        isPoolingReq = poolingReq;
    }
}
