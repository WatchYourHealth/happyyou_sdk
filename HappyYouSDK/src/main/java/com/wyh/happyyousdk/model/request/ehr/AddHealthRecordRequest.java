package com.wyh.happyyousdk.model.request.ehr;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.ehr.UploadFileResponse;

public class AddHealthRecordRequest {

    @SerializedName("HealthRecordTypeID")
    @Expose
    private Integer healthRecordTypeID;
    @SerializedName("ReportDate")
    @Expose
    private String reportDate;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("TestName")
    @Expose
    private String testName;
    @SerializedName("HealthRecordFilesList")
    @Expose
    private List<UploadFileResponse.Datum> healthRecordFilesList = null;

    public AddHealthRecordRequest(Integer healthRecordTypeID, String reportDate, String remarks, String testName, List<UploadFileResponse.Datum> healthRecordFilesList) {
        super();
        this.healthRecordTypeID = healthRecordTypeID;
        this.reportDate = reportDate;
        this.remarks = remarks;
        this.testName = testName;
        this.healthRecordFilesList = healthRecordFilesList;
    }

    public Integer getHealthRecordTypeID() {
        return healthRecordTypeID;
    }

    public void setHealthRecordTypeID(Integer healthRecordTypeID) {
        this.healthRecordTypeID = healthRecordTypeID;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public List<UploadFileResponse.Datum> getHealthRecordFilesList() {
        return healthRecordFilesList;
    }

    public void setHealthRecordFilesList(List<UploadFileResponse.Datum> healthRecordFilesList) {
        this.healthRecordFilesList = healthRecordFilesList;
    }

    public class HealthRecordFiles {

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

        /**
         * No args constructor for use in serialization
         */
        public HealthRecordFiles() {
        }

        /**
         * @param path
         * @param extension
         * @param modifiedOn
         * @param size
         * @param createdBy
         * @param name
         * @param healthRecordID
         * @param modifiedBy
         * @param id
         * @param isActive
         * @param createdOn
         */
        public HealthRecordFiles(Integer id, String name, String path, Integer size, String extension, Integer healthRecordID, String createdOn, String createdBy, String modifiedOn, Object modifiedBy, Integer isActive) {
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

