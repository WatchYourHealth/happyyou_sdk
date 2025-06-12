package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetQuizResponseModel {

    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("data")
    @Expose
    public GetQuizResponseModelData data;

    public GetQuizResponseModel(String msg, boolean success, GetQuizResponseModelData data) {
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

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

    public GetQuizResponseModelData getData() {
        return data;
    }

    public void setData(GetQuizResponseModelData data) {
        this.data = data;
    }
}
