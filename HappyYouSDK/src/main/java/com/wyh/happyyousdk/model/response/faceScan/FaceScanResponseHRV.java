package com.wyh.happyyousdk.model.response.faceScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceScanResponseHRV {
    @SerializedName("messageType")
    @Expose
    private String messageType;
    @SerializedName("data")
    @Expose
    private FaceScanDataHRV faceScanDataHRV;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public FaceScanDataHRV getFaceScanDataHRV() {
        return faceScanDataHRV;
    }

    public void setFaceScanDataHRV(FaceScanDataHRV faceScanDataHRV) {
        this.faceScanDataHRV = faceScanDataHRV;
    }
}
