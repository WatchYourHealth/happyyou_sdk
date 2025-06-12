package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MPINReq {

    @SerializedName("MPIN")
    @Expose
    String MPIN;
    @SerializedName("hasBiometric")
    @Expose
    boolean hasBiometric;

    public MPINReq(String MPIN, Boolean hasBiometric) {
        this.MPIN = MPIN;
        this.hasBiometric = hasBiometric;
    }

}
