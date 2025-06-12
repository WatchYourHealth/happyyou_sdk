package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetRainbowDietRequest {
    @SerializedName("ColorID")
    @Expose
    private Integer colorID;

    public GetRainbowDietRequest(Integer colorID) {
        this.colorID = colorID;
    }

    public Integer getColorID() {
        return colorID;
    }

    public void setColorID(Integer colorID) {
        this.colorID = colorID;
    }
}
