package com.wyh.happyyousdk.model.response.ice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchEmergencyDetailsResp {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Datum> data;

    {
        data = null;
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

        @SerializedName("PrimaryContactName")
        @Expose
        private String primaryContactName;
        @SerializedName("PrimaryContactMobile")
        @Expose
        private String primaryContactMobile;
        @SerializedName("PrimaryContactRelation")
        @Expose
        private String primaryContactRelation;
        @SerializedName("SecondaryContactName")
        @Expose
        private String secondaryContactName;
        @SerializedName("SecondaryContactMobile")
        @Expose
        private String secondaryContactMobile;
        @SerializedName("SecondaryContactRelation")
        @Expose
        private String secondaryContactRelation;

        public String getPrimaryContactName() {
            return primaryContactName;
        }

        public void setPrimaryContactName(String primaryContactName) {
            this.primaryContactName = primaryContactName;
        }

        public String getPrimaryContactMobile() {
            return primaryContactMobile;
        }

        public void setPrimaryContactMobile(String primaryContactMobile) {
            this.primaryContactMobile = primaryContactMobile;
        }

        public String getPrimaryContactRelation() {
            return primaryContactRelation;
        }

        public void setPrimaryContactRelation(String primaryContactRelation) {
            this.primaryContactRelation = primaryContactRelation;
        }

        public String getSecondaryContactName() {
            return secondaryContactName;
        }

        public void setSecondaryContactName(String secondaryContactName) {
            this.secondaryContactName = secondaryContactName;
        }

        public String getSecondaryContactMobile() {
            return secondaryContactMobile;
        }

        public void setSecondaryContactMobile(String secondaryContactMobile) {
            this.secondaryContactMobile = secondaryContactMobile;
        }

        public String getSecondaryContactRelation() {
            return secondaryContactRelation;
        }

        public void setSecondaryContactRelation(String secondaryContactRelation) {
            this.secondaryContactRelation = secondaryContactRelation;
        }

    }
}
