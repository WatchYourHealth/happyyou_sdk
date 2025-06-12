package com.wyh.happyyousdk.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetClientIDReq {
    @SerializedName("mobileNo")
    @Expose
    String mobileNo;

    public GetClientIDReq(String mobileNo) {
        this.mobileNo = mobileNo;
    }


}
