package com.wyh.happyyousdk.model.response.diary;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JournalFile {
     @SerializedName("id")
    @Expose
    public int id;
     @SerializedName("filepath")
    @Expose
    public String filepath;
     @SerializedName("diaryId")
    @Expose
    public int diaryId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(int diaryId) {
        this.diaryId = diaryId;
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
}
