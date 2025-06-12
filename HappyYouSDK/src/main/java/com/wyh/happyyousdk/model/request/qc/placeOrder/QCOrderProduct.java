package com.wyh.happyyousdk.model.request.qc.placeOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QCOrderProduct {
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("qty")
    @Expose
    private int qty;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("giftMessage")
    @Expose
    private String giftMessage;
    @SerializedName("theme")
    @Expose
    private String theme;

    public QCOrderProduct(String sku, int price, int qty, String currency, String giftMessage, String theme) {
        this.sku = sku;
        this.price = price;
        this.qty = qty;
        this.currency = currency;
        this.giftMessage = giftMessage;
        this.theme = theme;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public void setGiftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
