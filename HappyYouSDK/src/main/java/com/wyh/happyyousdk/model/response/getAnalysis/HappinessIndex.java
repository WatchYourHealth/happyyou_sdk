package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HappinessIndex {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("thumbsUp")
    @Expose
    private boolean thumbsUp;
    @SerializedName("hIharmones")
    @Expose
    private String hIharmones;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(boolean thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public String gethIharmones() {
        return hIharmones;
    }

    public void sethIharmones(String hIharmones) {
        this.hIharmones = hIharmones;
    }
}
