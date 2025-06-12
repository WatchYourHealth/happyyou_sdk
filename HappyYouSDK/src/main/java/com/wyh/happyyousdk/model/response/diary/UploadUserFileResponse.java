package com.wyh.happyyousdk.model.response.diary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UploadUserFileResponse {
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("data")
    @Expose
    public ArrayList<Datum> data;
    @SerializedName("freevoucher")
    @Expose
    public Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    public Object enGTokens;
    @SerializedName("rewards")
    @Expose
    public Object rewards;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public Object getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(Object enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Object getRewards() {
        return rewards;
    }

    public void setRewards(Object rewards) {
        this.rewards = rewards;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("filepath")
        @Expose
        private String filepath;
        @SerializedName("diaryId")
        @Expose
        private Integer diaryId;
        @SerializedName("createdOn")
        @Expose
        private String createdOn;
        @SerializedName("createdBy")
        @Expose
        private String createdBy;
        @SerializedName("modifiedOn")
        @Expose
        private String modifiedOn;
        @SerializedName("modifiedBy")
        @Expose
        private Object modifiedBy;
        @SerializedName("isDeleted")
        @Expose
        private Boolean isDeleted;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFilepath() {
            return filepath;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }

        public Integer getDiaryId() {
            return diaryId;
        }

        public void setDiaryId(Integer diaryId) {
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

        public Boolean getDeleted() {
            return isDeleted;
        }

        public void setDeleted(Boolean deleted) {
            isDeleted = deleted;
        }
    }
}
