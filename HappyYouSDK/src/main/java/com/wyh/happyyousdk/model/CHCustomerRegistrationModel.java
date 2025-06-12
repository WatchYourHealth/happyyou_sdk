package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.RewardsModel;

public class CHCustomerRegistrationModel {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;
    @SerializedName("freevoucher")
    @Expose
    private FreeVoucher freeVoucher;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private String data;

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

    public FreeVoucher getFreeVoucher() {
        return freeVoucher;
    }

    public void setFreeVoucher(FreeVoucher freeVoucher) {
        this.freeVoucher = freeVoucher;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
