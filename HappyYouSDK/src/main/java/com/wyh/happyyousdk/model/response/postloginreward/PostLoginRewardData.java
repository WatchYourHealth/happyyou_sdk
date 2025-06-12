package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PostLoginRewardData implements Serializable {
    @SerializedName("quadrants")
    @Expose
    private List<Quadrant> quadrants;
    @SerializedName("otherDetail")
    @Expose
    private OtherDetail otherDetail;
    @SerializedName("spinTheWheelDetail")
    @Expose
    private SpinTheWheelDetail spinTheWheelDetail;

    public List<Quadrant> getQuadrants() {
        return quadrants;
    }

    public void setQuadrants(List<Quadrant> quadrants) {
        this.quadrants = quadrants;
    }

    public OtherDetail getOtherDetail() {
        return otherDetail;
    }

    public void setOtherDetail(OtherDetail otherDetail) {
        this.otherDetail = otherDetail;
    }

    public SpinTheWheelDetail getSpinTheWheelDetail() {
        return spinTheWheelDetail;
    }

    public void setSpinTheWheelDetail(SpinTheWheelDetail spinTheWheelDetail) {
        this.spinTheWheelDetail = spinTheWheelDetail;
    }
}
