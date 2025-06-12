package com.wyh.happyyousdk.model.response.ehr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;

import java.util.List;

public class HealthRecordTypeResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public HealthRecordTypeResponse(String msg, Boolean success, List<Datum> data) {
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

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("isActive")
        @Expose
        private Integer isActive;

        public Datum(Integer id, String key, String name, Integer isActive) {
            super();
            this.id = id;
            this.key = key;
            this.name = name;
            this.isActive = isActive;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getIsActive() {
            return isActive;
        }

        public void setIsActive(Integer isActive) {
            this.isActive = isActive;
        }


    }


}
