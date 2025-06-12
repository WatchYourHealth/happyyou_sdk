package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;

public class RetakeResponseModel extends CommonSuccessResponse {

    @SerializedName("data")
    @Expose
    RetakeDatamodel datamodel;

    public RetakeDatamodel getDatamodel() {
        return datamodel;
    }

    public void setDatamodel(RetakeDatamodel datamodel) {
        this.datamodel = datamodel;
    }
}
