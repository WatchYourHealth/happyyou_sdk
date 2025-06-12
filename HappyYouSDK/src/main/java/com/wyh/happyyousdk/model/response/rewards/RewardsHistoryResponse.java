package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.FreeVoucher;

public class RewardsHistoryResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private RewardsHistoryData data;
    @SerializedName("freevoucher")
    @Expose
    private FreeVoucher freevoucher;

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

    public RewardsHistoryData getData() {
        return data;
    }

    public void setData(RewardsHistoryData data) {
        this.data = data;
    }

    public FreeVoucher getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(FreeVoucher freevoucher) {
        this.freevoucher = freevoucher;
    }
}
