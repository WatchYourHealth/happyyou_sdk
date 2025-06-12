package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddOnDetails {

    @SerializedName("addOnPoints")
    @Expose
    public ArrayList<AddOnPoints> addOnPoints;

    @SerializedName("addOnBadges")
    @Expose
    public ArrayList<AddOnBadges> addOnBadges;

    @SerializedName("addOnScratchCard")
    @Expose
    public ArrayList<AddOnScratchCard> addOnScratchCard;

    @SerializedName("addOnStamps")
    @Expose
    public ArrayList<AddOnStamps> addOnStamps;

    public ArrayList<AddOnPoints> getAddOnPoints() {
        return addOnPoints;
    }

    public ArrayList<AddOnBadges> getAddOnBadges() {
        return addOnBadges;
    }

    public ArrayList<AddOnScratchCard> getAddOnScratchCard() {
        return addOnScratchCard;
    }

    public ArrayList<AddOnStamps> getAddOnStamps() {
        return addOnStamps;
    }
}
