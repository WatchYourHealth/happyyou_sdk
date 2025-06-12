package com.wyh.happyyousdk.happyMarket.Model;

public class OrderInfoCardModel {

    String key;
    String Value;

    public OrderInfoCardModel(String key, String value) {
        this.key = key;
        Value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
