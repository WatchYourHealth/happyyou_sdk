package com.wyh.happyyousdk.corporateAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateModel {

    @SerializedName("stateName")
    @Expose
    private String stateName;
    @SerializedName("stateId")
    @Expose
    private Integer stateId;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

}
