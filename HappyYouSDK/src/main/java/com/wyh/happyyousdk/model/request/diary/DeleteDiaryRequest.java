package com.wyh.happyyousdk.model.request.diary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteDiaryRequest {
    @SerializedName("journalid")
    @Expose
    public int journalId;

    public DeleteDiaryRequest(int journalId) {
        this.journalId = journalId;
    }

    public int getJournalId() {
        return journalId;
    }

    public void setJournalId(int journalId) {
        this.journalId = journalId;
    }
}
