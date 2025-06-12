package com.wyh.happyyousdk.diary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiaryListDataResponse {
    @SerializedName("journalid")
    @Expose
    public int journalid;
    @SerializedName("userId")
    @Expose
    public String userId;
    @SerializedName("journalDate")
    @Expose
    public String journalDate;
    @SerializedName("journalName")
    @Expose
    public String journalName;
    @SerializedName("journalContent")
    @Expose
    public String journalContent;
    @SerializedName("createdOn")
    @Expose
    public String createdOn;
    @SerializedName("createdBy")
    @Expose
    public String createdBy;
    @SerializedName("modifiedOn")
    @Expose
    public String modifiedOn;
    @SerializedName("modifiedBy")
    @Expose
    public Object modifiedBy;
    @SerializedName("isDeleted")
    @Expose
    public boolean isDeleted;
    @SerializedName("journalSource")
    @Expose
    public String journalSource;
    @SerializedName("imagePath")
    @Expose
    public String imagePath;
    @SerializedName("imageId")
    @Expose
    public int imageId;


    public int getJournalid() {
        return journalid;
    }

    public void setJournalid(int journalid) {
        this.journalid = journalid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Object getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getJournalSource() {
        return journalSource;
    }

    public void setJournalSource(String journalSource) {
        this.journalSource = journalSource;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
