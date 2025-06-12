package com.wyh.happyyousdk.model.response.hraSection.mentalHealth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoodThingsData {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("GoodThings")
    @Expose
    private String goodThings;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGoodThings() {
        return goodThings;
    }

    public void setGoodThings(String goodThings) {
        this.goodThings = goodThings;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
