package com.wyh.happyyousdk.model.response.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOtpResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("isAlreadyRegistered")
    @Expose
    private int isAlreadyRegistered;
    @SerializedName("success")
    @Expose
    private boolean success;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIsAlreadyRegistered() {
        return isAlreadyRegistered;
    }

    public void setIsAlreadyRegistered(int isAlreadyRegistered) {
        this.isAlreadyRegistered = isAlreadyRegistered;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
