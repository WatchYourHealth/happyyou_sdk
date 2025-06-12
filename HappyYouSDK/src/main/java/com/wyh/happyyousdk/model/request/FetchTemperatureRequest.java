package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchTemperatureRequest {

    @SerializedName("city")
    @Expose
    private String city;

    public FetchTemperatureRequest(String city) {
        super();
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
