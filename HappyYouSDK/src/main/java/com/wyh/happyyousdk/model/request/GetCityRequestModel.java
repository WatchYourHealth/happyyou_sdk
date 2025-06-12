package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCityRequestModel {

    @SerializedName("StateId")
    @Expose
    private int stateId;

    public GetCityRequestModel(int StateId) {
        this.stateId = StateId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }
}
