package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OtherDetail implements Serializable {
    @SerializedName("expiryInMinutes")
    @Expose
    private Integer expiryInMinutes;
    @SerializedName("description")
    @Expose
    private String description;

    public Integer getExpiryInMinutes() {
        return expiryInMinutes;
    }

    public void setExpiryInMinutes(Integer expiryInMinutes) {
        this.expiryInMinutes = expiryInMinutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
