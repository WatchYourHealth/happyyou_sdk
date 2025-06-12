package com.wyh.happyyousdk.model.response.ehr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.RewardsModel;

import java.util.List;

public class FetchHealthRecordByIDResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }

    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }


    public class Data {

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
        private Object path;
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
        private List<UploadFileResponse.Datum> healthRecordFilesList;

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

        public Object getPath() {
            return path;
        }

        public void setPath(Object path) {
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

        public List<UploadFileResponse.Datum> getHealthRecordFilesList() {
            return healthRecordFilesList;
        }

        public void setHealthRecordFilesList(List<UploadFileResponse.Datum> healthRecordFilesList) {
            this.healthRecordFilesList = healthRecordFilesList;
        }

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
