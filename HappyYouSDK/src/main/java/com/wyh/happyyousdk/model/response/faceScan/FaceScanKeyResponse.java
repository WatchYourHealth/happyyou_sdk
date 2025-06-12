package com.wyh.happyyousdk.model.response.faceScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.FreeVoucher;

import java.util.List;

public class FaceScanKeyResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private List<FaceScanKeyData> data;
    @SerializedName("freevoucher")
    @Expose
    private FreeVoucher freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private Object enGTokens;

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

    public List<FaceScanKeyData> getData() {
        return data;
    }

    public void setData(List<FaceScanKeyData> data) {
        this.data = data;
    }

    public FreeVoucher getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(FreeVoucher freevoucher) {
        this.freevoucher = freevoucher;
    }

    public Object getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(Object enGTokens) {
        this.enGTokens = enGTokens;
    }
}
