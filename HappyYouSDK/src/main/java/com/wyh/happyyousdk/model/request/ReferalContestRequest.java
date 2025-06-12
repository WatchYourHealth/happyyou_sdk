package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferalContestRequest {

    @SerializedName("ClientNumber")
    @Expose
    private String ClientNumber;

    @SerializedName("ReferedBy")
    @Expose
    private String ReferedBy;


    public ReferalContestRequest(String clientNumber, String referedBy) {
        ClientNumber = clientNumber;
        ReferedBy = referedBy;
    }
}
