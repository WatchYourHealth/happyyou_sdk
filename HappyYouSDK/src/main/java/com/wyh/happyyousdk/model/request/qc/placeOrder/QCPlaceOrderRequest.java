package com.wyh.happyyousdk.model.request.qc.placeOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QCPlaceOrderRequest {
    @SerializedName("QCOrder")
    @Expose
    private QCOrder qCOrder;
    @SerializedName("ProductDetails")
    @Expose
    private ProductDetails productDetails;

    public QCPlaceOrderRequest(QCOrder qCOrder, ProductDetails productDetails) {
        this.qCOrder = qCOrder;
        this.productDetails = productDetails;
    }

    public QCOrder getQCOrder() {
        return qCOrder;
    }

    public void setQCOrder(QCOrder qCOrder) {
        this.qCOrder = qCOrder;
    }

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }
}
