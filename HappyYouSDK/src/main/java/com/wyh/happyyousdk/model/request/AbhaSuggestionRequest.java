package com.wyh.happyyousdk.model.request;

public class AbhaSuggestionRequest {

    public AbhaSuggestionRequest(String accesstoken, String transactionId) {
        this.accesstoken = accesstoken;
        this.transactionId = transactionId;
    }

    String accesstoken;
    String transactionId;
}
