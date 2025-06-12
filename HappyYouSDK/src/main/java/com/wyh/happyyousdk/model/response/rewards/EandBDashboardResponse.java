package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.FreeVoucher;

public class EandBDashboardResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private EandBDashboardData data;
    @SerializedName("freevoucher")
    @Expose
    private FreeVoucher freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;

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

    public EandBDashboardData getData() {
        return data;
    }

    public void setData(EandBDashboardData data) {
        this.data = data;
    }

    public FreeVoucher getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(FreeVoucher freevoucher) {
        this.freevoucher = freevoucher;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }


}
