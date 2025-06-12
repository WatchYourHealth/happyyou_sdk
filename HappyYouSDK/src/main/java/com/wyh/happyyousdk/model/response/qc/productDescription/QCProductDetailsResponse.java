package com.wyh.happyyousdk.model.response.qc.productDescription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.FreeVoucher;

public class QCProductDetailsResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private QCProductDetailsData data;
    @SerializedName("freevoucher")
    @Expose
    private FreeVoucher freevoucher;

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

    public QCProductDetailsData getData() {
        return data;
    }

    public void setData(QCProductDetailsData data) {
        this.data = data;
    }

    public FreeVoucher getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(FreeVoucher freevoucher) {
        this.freevoucher = freevoucher;
    }
}
