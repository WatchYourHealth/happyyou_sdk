package com.wyh.happyyousdk.model.response.postloginreward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SpinTheWheelDetail implements Serializable {
    @SerializedName("eventSpinData")
    @Expose
    private EventSpinData eventSpinData;
    @SerializedName("freeSpinData")
    @Expose
    private FreeSpinData freeSpinData;
    @SerializedName("burnSpinData")
    @Expose
    private BurnSpinData burnSpinData;

    public EventSpinData getEventSpinData() {
        return eventSpinData;
    }

    public void setEventSpinData(EventSpinData eventSpinData) {
        this.eventSpinData = eventSpinData;
    }

    public FreeSpinData getFreeSpinData() {
        return freeSpinData;
    }

    public void setFreeSpinData(FreeSpinData freeSpinData) {
        this.freeSpinData = freeSpinData;
    }

    public BurnSpinData getBurnSpinData() {
        return burnSpinData;
    }

    public void setBurnSpinData(BurnSpinData burnSpinData) {
        this.burnSpinData = burnSpinData;
    }
}
