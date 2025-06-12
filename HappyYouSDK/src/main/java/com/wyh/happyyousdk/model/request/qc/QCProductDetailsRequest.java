package com.wyh.happyyousdk.model.request.qc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QCProductDetailsRequest {
    @SerializedName("categoryID")
    @Expose
    private int categoryID;
    @SerializedName("sku")
    @Expose
    private String sku;

    public QCProductDetailsRequest(int categoryID, String sku) {
        this.categoryID = categoryID;
        this.sku = sku;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
