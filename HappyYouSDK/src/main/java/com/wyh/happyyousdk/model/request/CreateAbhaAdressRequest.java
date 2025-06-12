package com.wyh.happyyousdk.model.request;

public class CreateAbhaAdressRequest {
    public CreateAbhaAdressRequest(String accesstoken, String txnId, String preferred, String abhaAddress) {
        this.accesstoken = accesstoken;
        this.txnId = txnId;
        this.preferred = preferred;
        this.abhaAddress = abhaAddress;
    }

    String accesstoken;
    String txnId;
    String preferred;
    String abhaAddress;
}
