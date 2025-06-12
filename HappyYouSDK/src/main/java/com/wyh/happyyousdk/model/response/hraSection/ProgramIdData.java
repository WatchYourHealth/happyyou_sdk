package com.wyh.happyyousdk.model.response.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProgramIdData {

    @SerializedName("ProgramID")
    @Expose
    private Integer programID;
    @SerializedName("UUID")
    @Expose
    private String uuid;

    public Integer getProgramID() {
        return programID;
    }

    public void setProgramID(Integer programID) {
        this.programID = programID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
