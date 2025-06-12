package com.wyh.happyyousdk.dashboard.adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PinUnpinTileRequest {
    @SerializedName("tileId")
    @Expose
    private int tileId;
    @SerializedName("isPinned")
    @Expose
    private boolean isPinned;

    public PinUnpinTileRequest(int tileId, boolean isPinned) {
        this.tileId = tileId;
        this.isPinned = isPinned;
    }

}
