package com.wyh.happyyousdk.model.request.qc.placeOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetails {
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("Quantity")
    @Expose
    private int quantity;
    @SerializedName("Points")
    @Expose
    private int points;
    @SerializedName("TransPoints")
    @Expose
    private int transPoints;

    public ProductDetails(String productName, int quantity, int points, int transPoints) {
        this.productName = productName;
        this.quantity = quantity;
        this.points = points;
        this.transPoints = transPoints;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTransPoints() {
        return transPoints;
    }

    public void setTransPoints(int transPoints) {
        this.transPoints = transPoints;
    }
}
