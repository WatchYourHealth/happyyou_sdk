package com.wyh.happyyousdk.model.response.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FilePopupModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;
    @SerializedName("expiryDate")
    @Expose
    private String expiryDate;

    @SerializedName("tileIcon")
    @Expose
    private String tileIcon;
    @SerializedName("filePath")
    @Expose
    private String filePath;

    @SerializedName("isExpired")
    @Expose
    private boolean isExpired;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("timeString")
    private  String timeString;
    @SerializedName("dateString")
    private  String dateString;
    @SerializedName("place")
    private  String place;
    @SerializedName("eventId")
    private  String eventId;
    @SerializedName("description1")
    private  String description1;
    @SerializedName("description2")
    private  String description2;
    @SerializedName("description3")
    private  String description3;
    @SerializedName("description4")
    private  String description4;

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDescription3() {
        return description3;
    }

    public void setDescription3(String description3) {
        this.description3 = description3;
    }

    public String getDescription4() {
        return description4;
    }

    public void setDescription4(String description4) {
        this.description4 = description4;
    }




    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTileIcon() {
        return tileIcon;
    }

    public void setTileIcon(String tileIcon) {
        this.tileIcon = tileIcon;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
