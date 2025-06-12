package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlternativeNumberRequestModel {

    @SerializedName("AlternateNumber")
    @Expose
    private String number;

    public AlternativeNumberRequestModel(String number) {
        this.number = number;
    }
}
