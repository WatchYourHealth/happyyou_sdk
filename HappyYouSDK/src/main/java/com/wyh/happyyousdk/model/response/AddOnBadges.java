package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddOnBadges {

    @SerializedName("badgeName")
    @Expose
    public String badgeName;

    @SerializedName("badgeLogo")
    @Expose
    public String badgeLogo;

    public String getBadgeName() {
        return badgeName;
    }

    public String getBadgeLogo() {
        return badgeLogo;
    }
}
