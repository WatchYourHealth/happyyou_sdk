package com.wyh.happyyousdk.model.request.qc.placeOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QCOrder {
    @SerializedName("address")
    @Expose
    private QCOrderAddress address;
    @SerializedName("billing")
    @Expose
    private QCOrderBilling billing;
    @SerializedName("payments")
    @Expose
    private List<QCOrderPayment> payments;
    @SerializedName("refno")
    @Expose
    private String refno;
    @SerializedName("products")
    @Expose
    private List<QCOrderProduct> products;
    @SerializedName("syncOnly")
    @Expose
    private boolean syncOnly;
    @SerializedName("couponCode")
    @Expose
    private String couponCode;
    @SerializedName("deliveryMode")
    @Expose
    private String deliveryMode;

    public QCOrder(QCOrderAddress address, QCOrderBilling billing, List<QCOrderPayment> payments,
                   String refno, List<QCOrderProduct> products, boolean syncOnly, String couponCode, String deliveryMode) {
        this.address = address;
        this.billing = billing;
        this.payments = payments;
        this.refno = refno;
        this.products = products;
        this.syncOnly = syncOnly;
        this.couponCode = couponCode;
        this.deliveryMode = deliveryMode;
    }

    public QCOrderAddress getAddress() {
        return address;
    }

    public void setAddress(QCOrderAddress address) {
        this.address = address;
    }

    public QCOrderBilling getBilling() {
        return billing;
    }

    public void setBilling(QCOrderBilling billing) {
        this.billing = billing;
    }

    public List<QCOrderPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<QCOrderPayment> payments) {
        this.payments = payments;
    }

    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public List<QCOrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<QCOrderProduct> products) {
        this.products = products;
    }

    public boolean isSyncOnly() {
        return syncOnly;
    }

    public void setSyncOnly(boolean syncOnly) {
        this.syncOnly = syncOnly;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }
}
