package com.wyh.happyyousdk.model.request.diary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateDairyRequest {
    @SerializedName("journalid")
    @Expose
    public int journalId;
    @SerializedName("journalDate")
    @Expose
    public String journalDate;
    @SerializedName("journalName")
    @Expose
    public String journalName;
    @SerializedName("journalContent")
    @Expose
    public String journalContent;

    public UpdateDairyRequest(int journalId, String journalDate, String journalName, String journalContent) {
        this.journalId = journalId;
        this.journalDate = journalDate;
        this.journalName = journalName;
        this.journalContent = journalContent;
    }

    public int getJournalId() {
        return journalId;
    }

    public void setJournalId(int journalId) {
        this.journalId = journalId;
    }

    public String getJournalDate() {
        return journalDate;
    }

    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }

    public String getJournalName() {
        return journalName;
    }

    public void setJournalName(String journalName) {
        this.journalName = journalName;
    }

    public String getJournalContent() {
        return journalContent;
    }

    public void setJournalContent(String journalContent) {
        this.journalContent = journalContent;
    }
}
