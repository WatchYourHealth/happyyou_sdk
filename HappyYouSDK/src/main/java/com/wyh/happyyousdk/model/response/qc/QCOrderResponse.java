package com.wyh.happyyousdk.model.response.qc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;

import java.util.List;

public class QCOrderResponse extends CommonSuccessResponse {
    @SerializedName("data")
    @Expose
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("cards")
        @Expose
        private List<QCOrderCard> cards;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("productName")
        @Expose
        private String productName;

        public List<QCOrderCard> getCards() {
            return cards;
        }

        public void setCards(List<QCOrderCard> cards) {
            this.cards = cards;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }


}
