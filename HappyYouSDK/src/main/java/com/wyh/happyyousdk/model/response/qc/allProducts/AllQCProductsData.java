package com.wyh.happyyousdk.model.response.qc.allProducts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllQCProductsData {
    @SerializedName("plid")
    @Expose
    private int plid;
    @SerializedName("productListResponse")
    @Expose
    private ProductListResponse productListResponse;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("expiryOn")
    @Expose
    private String expiryOn;
    @SerializedName("active")
    @Expose
    private int active;

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid = plid;
    }

    public ProductListResponse getProductListResponse() {
        return productListResponse;
    }

    public void setProductListResponse(ProductListResponse productListResponse) {
        this.productListResponse = productListResponse;
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
