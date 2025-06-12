package com.wyh.happyyousdk.model.request.diary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddDiaryRequest {
    @SerializedName("journalDate")
    @Expose
    public String journalDate;
    @SerializedName("journalName")
    @Expose
    public String journalName;
    @SerializedName("journalContent")
    @Expose
    public String journalContent;

    public AddDiaryRequest(String journalDate, String journalName, String journalContent) {
        this.journalDate = journalDate;
        this.journalName = journalName;
        this.journalContent = journalContent;
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
