package com.wyh.happyyousdk.model.request.ehr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.ehr.AllHealthRecordDetailsList;

import java.util.List;

public class FetchHealthRecordByIDRequest {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("healthRecordTypeID")
    @Expose
    private Integer healthRecordTypeID;
    @SerializedName("reportDate")
    @Expose
    private String reportDate;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("mobile")
    @Expose
    private Object mobile;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("testName")
    @Expose
    private String testName;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("isActive")
    @Expose
    private String isActive;
    @SerializedName("tags")
    @Expose
    private Object tags;
    @SerializedName("healthRecordTypeName")
    @Expose
    private String healthRecordTypeName;
    @SerializedName("healthRecordFilesList")
    @Expose
    private List<AllHealthRecordDetailsList.Datum.HealthRecordFiles> healthRecordFilesList = null;

    public FetchHealthRecordByIDRequest(Integer id, String uuid, Integer healthRecordTypeID, String reportDate, String path, Object name, Object mobile, Object address, String remarks, String testName, String createdBy, String isActive, Object tags, String healthRecordTypeName, List<AllHealthRecordDetailsList.Datum.HealthRecordFiles> healthRecordFilesList) {
        super();
        this.id = id;
        this.uuid = uuid;
        this.healthRecordTypeID = healthRecordTypeID;
        this.reportDate = reportDate;
        this.path = path;
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.remarks = remarks;
        this.testName = testName;
        this.createdBy = createdBy;
        this.isActive = isActive;
        this.tags = tags;
        this.healthRecordTypeName = healthRecordTypeName;
        this.healthRecordFilesList = healthRecordFilesList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getMobile() {
        return mobile;
    }

    public void setMobile(Object mobile) {
        this.mobile = mobile;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Object getTags() {
        return tags;
    }

    public void setTags(Object tags) {
        this.tags = tags;
    }

    public String getHealthRecordTypeName() {
        return healthRecordTypeName;
    }

    public void setHealthRecordTypeName(String healthRecordTypeName) {
        this.healthRecordTypeName = healthRecordTypeName;
    }

    public List<AllHealthRecordDetailsList.Datum.HealthRecordFiles> getHealthRecordFilesList() {
        return healthRecordFilesList;
    }

    public void setHealthRecordFilesList(List<AllHealthRecordDetailsList.Datum.HealthRecordFiles> healthRecordFilesList) {
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
