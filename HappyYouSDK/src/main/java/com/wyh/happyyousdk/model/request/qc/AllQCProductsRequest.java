package com.wyh.happyyousdk.model.request.qc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllQCProductsRequest {
    @SerializedName("categoryID")
    @Expose
    private int categoryID;

    public AllQCProductsRequest(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
}
