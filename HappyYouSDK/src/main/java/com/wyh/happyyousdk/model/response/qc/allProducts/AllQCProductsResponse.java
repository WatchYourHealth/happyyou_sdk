package com.wyh.happyyousdk.model.response.qc.allProducts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.FreeVoucher;

public class AllQCProductsResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private AllQCProductsData data;
    @SerializedName("freevoucher")
    @Expose
    private FreeVoucher freevoucher;

    @SerializedName("userkey")
    @Expose
    private Object userkey;

    public Object getUserkey() {
        return userkey;
    }

    public void setUserkey(Object userkey) {
        this.userkey = userkey;
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

    public AllQCProductsData getData() {
        return data;
    }

    public void setData(AllQCProductsData data) {
        this.data = data;
    }

    public FreeVoucher getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(FreeVoucher freevoucher) {
        this.freevoucher = freevoucher;
    }
}
