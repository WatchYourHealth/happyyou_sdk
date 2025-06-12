package com.wyh.happyyousdk.model.response.qc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QCOrderCard {

    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("cardPin")
    @Expose
    private String cardPin;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("activationUrl")
    @Expose
    private String activationUrl;
    @SerializedName("activationCode")
    @Expose
    private String activationCode;
    @SerializedName("validity")
    @Expose
    private String validity;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardPin() {
        return cardPin;
    }

    public void setCardPin(String cardPin) {
        this.cardPin = cardPin;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getActivationUrl() {
        return activationUrl;
    }

    public void setActivationUrl(String activationUrl) {
        this.activationUrl = activationUrl;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

}