package com.wyh.happyyousdk.model.request.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMoodRequest {
    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("MoodID")
    @Expose
    private String moodID;
    @SerializedName("DietPatternID")
    @Expose
    private String dietPatternID;
    @SerializedName("DateOfEntry")
    @Expose
    private String dateOfEntry;
    @SerializedName("ModifiedOn")
    @Expose
    private String modifiedOn;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;

    public AddMoodRequest(String uuid, String moodID, String dateOfEntry, String modifiedOn, String createdOn, String dietPatternID) {
        this.uuid = uuid;
        this.moodID = moodID;
        this.dietPatternID = dietPatternID;
        this.dateOfEntry = dateOfEntry;
        this.modifiedOn = modifiedOn;
        this.createdOn = createdOn;
    }



    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMoodID() {
        return moodID;
    }

    public void setMoodID(String moodID) {
        this.moodID = moodID;
    }

    public String getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(String dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDietPatternID() {
        return dietPatternID;
    }

    public void setDietPatternID(String dietPatternID) {
        this.dietPatternID = dietPatternID;
    }
}
