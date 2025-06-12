package com.wyh.happyyousdk.model.request.absorb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddUserInterestRequest {

    @SerializedName("webinarID")
    @Expose
    private Integer webinarID;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("isInterested")
    @Expose
    private Integer isInterested;

    public AddUserInterestRequest(Integer webinarID, String uuid, Integer isInterested) {
        super();
        this.webinarID = webinarID;
        this.uuid = uuid;
        this.isInterested = isInterested;
    }

    public Integer getWebinarID() {
        return webinarID;
    }

    public void setWebinarID(Integer webinarID) {
        this.webinarID = webinarID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getIsInterested() {
        return isInterested;
    }

    public void setIsInterested(Integer isInterested) {
        this.isInterested = isInterested;
    }

}