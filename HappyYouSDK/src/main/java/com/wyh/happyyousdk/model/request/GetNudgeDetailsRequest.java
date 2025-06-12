package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetNudgeDetailsRequest {

    @SerializedName("Type")
    @Expose
    private String Type;

    @SerializedName("button")
    @Expose
    private String button;

    public GetNudgeDetailsRequest(String type, String button) {
        Type = type;
        this.button = button;
    }
}
