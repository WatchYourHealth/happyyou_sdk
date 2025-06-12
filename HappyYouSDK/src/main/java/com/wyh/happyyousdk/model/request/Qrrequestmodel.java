package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Qrrequestmodel {
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("qrjson")
    @Expose
    private String qrjson;

    public Qrrequestmodel(String location, String qrjson) {
        this.location = location;
        this.qrjson = qrjson;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQrjson() {
        return qrjson;
    }

    public void setQrjson(String qrjson) {
        this.qrjson = qrjson;
    }
}
