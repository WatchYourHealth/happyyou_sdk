package com.wyh.happyyousdk.model.request.ehr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConvertToBitlyRequest {
    @SerializedName("url")
    @Expose
    private String url;

    public ConvertToBitlyRequest(String url) {
        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
