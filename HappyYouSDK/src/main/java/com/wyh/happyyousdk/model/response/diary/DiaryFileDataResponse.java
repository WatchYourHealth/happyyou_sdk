package com.wyh.happyyousdk.model.response.diary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DiaryFileDataResponse {
    @SerializedName("userJournal")
    @Expose
    public UserJournal userJournal;
    @SerializedName("journalFiles")
    @Expose
    public ArrayList<JournalFile> journalFiles;

    public UserJournal getUserJournal() {
        return userJournal;
    }

    public void setUserJournal(UserJournal userJournal) {
        this.userJournal = userJournal;
    }

    public ArrayList<JournalFile> getJournalFiles() {
        return journalFiles;
    }

    public void setJournalFiles(ArrayList<JournalFile> journalFiles) {
        this.journalFiles = journalFiles;
    }
}
