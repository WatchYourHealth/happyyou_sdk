package com.wyh.happyyousdk.model.response.hraSection.goals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoalsHistoryData {
    @SerializedName("ProgramID")
    @Expose
    private Integer programID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Iconlink")
    @Expose
    private String iconLink;
    @SerializedName("History")
    @Expose
    private List<HistoryData> history = null;

    public Integer getProgramID() {
        return programID;
    }

    public void setProgramID(Integer programID) {
        this.programID = programID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HistoryData> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryData> history) {
        this.history = history;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }
}
