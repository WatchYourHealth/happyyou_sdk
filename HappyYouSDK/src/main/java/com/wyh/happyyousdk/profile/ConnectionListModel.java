package com.wyh.happyyousdk.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectionListModel {

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("tribeId")
    @Expose
    String tribeId;

    public ConnectionListModel(String name, String type, String tribeId) {
        this.name = name;
        this.type = type;
        this.tribeId = tribeId;
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTribeId() {
        return tribeId;
    }
}
