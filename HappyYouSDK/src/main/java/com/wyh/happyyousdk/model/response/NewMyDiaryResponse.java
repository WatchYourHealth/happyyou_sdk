package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Objects;

public class NewMyDiaryResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    private ArrayList<NewDiaryData> data;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


    public ArrayList<NewDiaryData> getData() {
        return data;
    }

    public void setData(ArrayList<NewDiaryData> data) {
        this.data = data;
    }

    public class NewDiaryData {

        @SerializedName("journalid")
        @Expose
        private Integer journalid;

        @SerializedName("userId")
        @Expose
        private Objects userId;

        @SerializedName("journalDate")
        @Expose
        private String journalDate;

        @SerializedName("journalName")
        @Expose
        private String journalName;

        @SerializedName("journalContent")
        @Expose
        private String journalContent;

        @SerializedName("journalSource")
        @Expose
        private String journalSource;

        @SerializedName("imagePath")
        @Expose
        private String imagePath;

        @SerializedName("imageId")
        @Expose
        private Integer imageId;

        @SerializedName("createdOn")
        @Expose
        private String createdOn;

        @SerializedName("isDeleted")
        @Expose
        private Boolean isDeleted;


        public Integer getJournalid() {
            return journalid;
        }

        public void setJournalid(Integer journalid) {
            this.journalid = journalid;
        }

        public Objects getUserId() {
            return userId;
        }

        public void setUserId(Objects userId) {
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

        public Integer getImageId() {
            return imageId;
        }

        public void setImageId(Integer imageId) {
            this.imageId = imageId;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public Boolean getDeleted() {
            return isDeleted;
        }

        public void setDeleted(Boolean deleted) {
            isDeleted = deleted;
        }
    }
}

