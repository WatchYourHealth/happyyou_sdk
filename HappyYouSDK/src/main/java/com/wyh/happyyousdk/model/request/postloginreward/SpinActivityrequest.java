package com.wyh.happyyousdk.model.request.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SpinActivityrequest implements Serializable {

    @SerializedName("SpinType")
    @Expose
    private String SpinType;


    @SerializedName("SpinEventId")
    @Expose
    private String SpinEventId;

    public SpinActivityrequest(String spinType, String spinEventId) {
        SpinType = spinType;
        SpinEventId = spinEventId;
    }

    public String getSpinType() {
        return SpinType;
    }

    public void setSpinType(String spinType) {
        SpinType = spinType;
    }

    public String getSpinEventId() {
        return SpinEventId;
    }

    public void setSpinEventId(String spinEventId) {
        SpinEventId = spinEventId;
    }
}
