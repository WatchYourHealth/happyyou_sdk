package com.wyh.happyyousdk.model.response.qc.productDescription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QCProductDetailsData {
    @SerializedName("pdid")
    @Expose
    private int pdid;
    @SerializedName("productDetailsResponse")
    @Expose
    private ProductDetailsResponse productDetailsResponse;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("expiryOn")
    @Expose
    private String expiryOn;
    @SerializedName("active")
    @Expose
    private int active;

    public int getPdid() {
        return pdid;
    }

    public void setPdid(int pdid) {
        this.pdid = pdid;
    }

    public ProductDetailsResponse getProductDetailsResponse() {
        return productDetailsResponse;
    }

    public void setProductDetailsResponse(ProductDetailsResponse productDetailsResponse) {
        this.productDetailsResponse = productDetailsResponse;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExpiryOn() {
        return expiryOn;
    }

    public void setExpiryOn(String expiryOn) {
        this.expiryOn = expiryOn;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
