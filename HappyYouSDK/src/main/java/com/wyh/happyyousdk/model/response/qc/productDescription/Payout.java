package com.wyh.happyyousdk.model.response.qc.productDescription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Payout {
    @SerializedName("payment_methods")
    @Expose
    private List<String> paymentMethods;

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<String> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
