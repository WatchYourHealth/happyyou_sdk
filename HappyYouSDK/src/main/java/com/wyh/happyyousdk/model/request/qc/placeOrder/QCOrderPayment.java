package com.wyh.happyyousdk.model.request.qc.placeOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QCOrderPayment {
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("code")
    @Expose
    private String code;

    public QCOrderPayment(int amount, String code) {
        this.amount = amount;
        this.code = code;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
