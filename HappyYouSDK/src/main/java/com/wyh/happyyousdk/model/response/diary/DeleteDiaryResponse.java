package com.wyh.happyyousdk.model.response.diary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteDiaryResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("data")
    @Expose
    public Object data;
    @SerializedName("freevoucher")
    @Expose
    public Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    public Object enGTokens;
    @SerializedName("rewards")
    @Expose
    public Object rewards;


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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public Object getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(Object enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Object getRewards() {
        return rewards;
    }

    public void setRewards(Object rewards) {
        this.rewards = rewards;
    }
}
