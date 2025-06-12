package com.wyh.happyyousdk.model.response.ehr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadFileResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;


    public UploadFileResponse(String msg, Boolean success, List<Datum> data) {
        super();
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("size")
        @Expose
        private Integer size;
        @SerializedName("extension")
        @Expose
        private String extension;
        @SerializedName("healthRecordID")
        @Expose
        private Integer healthRecordID;
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
        @SerializedName("isActive")
        @Expose
        private Integer isActive;

        public Datum(Integer id, String name, String path, Integer size, String extension, Integer healthRecordID, String createdOn, String createdBy, String modifiedOn, Object modifiedBy, Integer isActive) {
            super();
            this.id = id;
            this.name = name;
            this.path = path;
            this.size = size;
            this.extension = extension;
            this.healthRecordID = healthRecordID;
            this.createdOn = createdOn;
            this.createdBy = createdBy;
            this.modifiedOn = modifiedOn;
            this.modifiedBy = modifiedBy;
            this.isActive = isActive;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public Integer getHealthRecordID() {
            return healthRecordID;
        }

        public void setHealthRecordID(Integer healthRecordID) {
            this.healthRecordID = healthRecordID;
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

        public Integer getIsActive() {
            return isActive;
        }

        public void setIsActive(Integer isActive) {
            this.isActive = isActive;
        }

    }


}
