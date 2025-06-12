package com.wyh.happyyousdk.model.response.faceScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceScanDataHRV {

    @SerializedName("sdnn")
    @Expose
    private double hrv = 0;

    public double getHrv() {
        return hrv;
    }

    public void setHrv(double hrv) {
        this.hrv = hrv;
    }
}
